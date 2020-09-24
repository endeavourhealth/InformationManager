package org.endeavourhealth.informationmanager.common.transform;

import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Takes an ontology and converts selected text IRIs to Snomed London exentions if not already
 * <p>
 *     The selected IRIs also converts their subclasses or sub properties
 * </p>
 * <p>
 * Warning, this increments the Snomed counter which may cause a clash if Snomed concepts are allocated from elsewhere
 *</p>
 */
public class SnomedAssigner {
    private DefaultPrefixManager prefixManager;
    private OWLDataFactory dataFactory;
    private OWLReasoner reasoner;
    private OWLReasonerFactory reasonerFactory;
    private NodeSet<OWLEntity> nodes;
    private OWLReasonerConfiguration config;
    private OWLOntology ontology;
    private String coreIri;
    private OWLOntologyManager manager;
    private Integer counter;
    private OWLDataPropertyAssertionAxiom counterAx;
    private NodeSet<OWLEntity> candidates;
    private String namespace;
    List<IRI> toChange;

    /**
     * Converts class or property none Snomed IRIs into Snomed IRIs based on the ontology Snomed id seed counter
     *
     * @param m The ontology manager that manages the ontology that is being updated
     * @param o The ontology to update
     *          since: 1.0.0
     *          author: David Stables Endeavour
     */
    public SnomedAssigner(OWLOntologyManager m
            , OWLOntology o, String ns) {
        manager = m;
        ontology = o;
        namespace= ns;

        dataFactory = manager.getOWLDataFactory();
        //Needs to find the default prefix without which it cannot assign new Snomed IRIs
        OWLDocumentFormat ontologyFormat = new FunctionalSyntaxDocumentFormat();
        if (ontologyFormat instanceof PrefixDocumentFormat) {
            prefixManager = new DefaultPrefixManager();
            prefixManager.copyPrefixesFrom((PrefixDocumentFormat) ontologyFormat);
            prefixManager.setPrefixComparator(((PrefixDocumentFormat) ontologyFormat).getPrefixComparator());
            coreIri = prefixManager.getDefaultPrefix();
        } else {
            throw new IllegalStateException(("ontology is in wrong format"));
        }

        //Creates reasoner and computes inferences
        reasonerFactory = new StructuralReasonerFactory();
        ConsoleProgressMonitor monitor = new ConsoleProgressMonitor();
        config = new SimpleConfiguration(monitor);
        reasoner = reasonerFactory.createReasoner(ontology, config);
        reasoner.precomputeInferences();

        //Finds the Snomed counter using these IRI identifiers
        OntologyQuery query = new OntologyQuery();
        counterAx = query.getDataPropertyAxiom(ontology
                , "http://www.DiscoveryDataService.org/InformationModel/Ontology#890231000252108",
                "http://www.DiscoveryDataService.org/InformationModel/Ontology#hasIncrementalFrom");

        if (counterAx == null)
            throw new IllegalArgumentException("Ontology does not have access to Snomed counter");
        //Otherwise use the counter;
        counter = Integer.parseInt(counterAx.getObject().getLiteral());


    }

    /**
     * Converts the none Snomed IRIs to snomed codes for a selected IRI and its descendants
     *
     * @param parentIri the IRI of the class or property to convert, including all its subclasses
     * @return returns the modified ontology
     */
    public OWLOntology convert(String parentIri) {

        //Collects the IRIs to change
        setIRIsToChange(parentIri);

        //Adds ontology to changer
        Set<OWLOntology> ontologies = new HashSet<>();
        ontologies.add(ontology);

        //Renamer object
        OWLEntityRenamer renamer = new OWLEntityRenamer(manager, ontologies);

        //Cycles through the map and changes the IRIs
        for (IRI fromIri : toChange) {
            String concept = new SnomedConcept(counter,namespace).getConcept().toString();
            IRI toIri = IRI.create(coreIri + concept);
            List<OWLOntologyChange> changes = renamer.changeIRI((IRI) fromIri
                    , toIri);
            for (OWLOntologyChange change : changes)
                if (manager.applyChange(change) == ChangeApplied.UNSUCCESSFULLY)
                    System.err.println("Change not applied successfully");
            //changes.stream().forEach(manager::applyChange);
            counter = counter + 1;
        }
        ;
        //Remove old counter and add new one
        OWLLiteral literal = dataFactory.getOWLLiteral(counter);
        List<OWLOntologyChange> updates = new ArrayList<>();
        updates.add(new RemoveAxiom(ontology, counterAx));
        updates.add(new AddAxiom(ontology,
                dataFactory.getOWLDataPropertyAssertionAxiom(
                        counterAx.getProperty().asOWLDataProperty(),
                        counterAx.getSubject().asOWLNamedIndividual(),
                        literal)));
        manager.applyChanges(updates);

        return ontology;
    }

    //Finds all the IRIs that need converting
    private void setIRIsToChange(String parentIri) {
        //Collects the annotation property iris that do not need converting
        IRI parent = IRI.create(parentIri);
        if (ontology.containsClassInSignature(parent)) {
            OWLClass parentClass = dataFactory.getOWLClass(parent);
            candidates = (NodeSet) reasoner.getSubClasses(parentClass, true);
        } else {
            if (ontology.containsObjectPropertyInSignature(parent)) {
                OWLObjectProperty parentOp = dataFactory.getOWLObjectProperty(parent);
                candidates = (NodeSet) reasoner.getSubObjectProperties(parentOp, true);
            } else {
                if (ontology.containsDataPropertyInSignature(parent)) {
                    OWLDataProperty parentDp = dataFactory.getOWLDataProperty(parent);
                    candidates = (NodeSet) reasoner.getSubDataProperties(parentDp, true);
                } else {
                    System.err.println("Parent IRI not found");
                }
            }
        }

        if (candidates != null) {

            //Initialises the list and regex pattern
            toChange = new ArrayList<>();

            //loops through the entities to change and tests each one
            candidates.forEach(this::isNotSnomed);

        } else {
            System.err.println("No concepts found to change");
        }
    }

    private void isNotSnomed(Node<OWLEntity> n){
        IRI testIri = n.getRepresentativeElement().getIRI();
        //Snomed-CT pattern
        Pattern pattern= Pattern.compile("[0-9]+");
        String testPattern = getLocalNumber(coreIri, testIri.toString());
        if (testPattern != "") {
            Matcher m = pattern.matcher(testPattern);
            if (!m.matches())
                toChange.add(testIri);
        }
    }


    //Strips the local name which is a none standard XML local name as its a number
    private String getLocalNumber(String coreIri, String longIri) {

        if (longIri.length() > coreIri.length()) {
            String test = longIri.substring(0, coreIri.length());
            if (test.equals(coreIri)) {
                return (longIri.substring(coreIri.length()));
            }
        }
        return "";
    }

    //Collects all the annotation property iris
    private List<IRI> getAnnotationIRIs(OWLOntology ontology) {
        List<IRI> result= new ArrayList<>();
        for (OWLDeclarationAxiom da: ontology.getAxioms(AxiomType.DECLARATION)) {
            OWLEntity e = da.getEntity();
            if (e.getEntityType() == EntityType.ANNOTATION_PROPERTY)
                result.add(e.getIRI());
        }
        return result;
    }



}
