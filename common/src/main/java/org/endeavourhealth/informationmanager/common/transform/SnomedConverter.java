package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.transform.model.Document;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Takes the core contology and converts IRIs o Snomed if not already
//Warning, this increments the Snomed counter whicn may cause a clash if Snomed concepts
//are allocated from elsewhere

public class SnomedConverter {
    private DefaultPrefixManager prefixManager;
    private OWLDataFactory dataFactory;

    //Converts the none Snomed IRIs to snomed codes, excluding annotations
    public OWLOntology convert(OWLOntologyManager manager, OWLOntology ontology){
        prefixManager = new DefaultPrefixManager();
        dataFactory = manager.getOWLDataFactory();
        OWLDocumentFormat ontologyFormat = ontology.getNonnullFormat();
        if (ontologyFormat instanceof PrefixDocumentFormat) {
            prefixManager.copyPrefixesFrom((PrefixDocumentFormat) ontologyFormat);
            prefixManager.setPrefixComparator(((PrefixDocumentFormat) ontologyFormat).getPrefixComparator());
        }

        //Sets the core IRI as that is the only ones that might need converting
        String coreIri = "http://www.DiscoveryDataService.org/InformationModel/Ontology#";

        //Collects the IRIs to chage
        List<IRI> toChange = getIRIsToChange(ontology,coreIri);

        //Adds ontology to changer
        List<OWLOntology> ontologies= new ArrayList<>();
        ontologies.add(ontology);


        //Renamer object
        OWLEntityRenamer renamer = new OWLEntityRenamer(manager,ontologies);

        //Finds the Snomed counter using these IRI identifiers
        OntologyQuery query = new OntologyQuery();
        OWLDataPropertyAssertionAxiom counterAx= query.getDataPropertyAxiom(ontology
                ,"http://www.DiscoveryDataService.org/InformationModel/Ontology#890231000252108",
                "http://www.DiscoveryDataService.org/InformationModel/Ontology#890241000252104");

        if (counterAx==null)
            throw new IllegalArgumentException("Ontology does not have access to Snomed counter");

        //Otherwise use the counter;
        Integer counter= Integer.parseInt(counterAx.getObject().getLiteral());

        //Cycles through the map and changes the IRIs
        for (IRI fromIri: toChange) {
            String concept = new SnomedConcept(counter).getConcept().toString();
            IRI toIri = IRI.create(coreIri + concept);
            List<OWLOntologyChange> changes = renamer.changeIRI((IRI) fromIri
                    , toIri);
           for (OWLOntologyChange change:changes)
             if (manager.applyChange(change) ==ChangeApplied.UNSUCCESSFULLY)
                 System.err.println("Change not applied successfully");
            //changes.stream().forEach(manager::applyChange);
            counter=counter+1;
        };
        //Remove old counter and add new one
        OWLLiteral literal = dataFactory.getOWLLiteral(counter);
        List<OWLOntologyChange> updates = new ArrayList<>();
        updates.add(new  RemoveAxiom(ontology,counterAx));
        updates.add(new AddAxiom(ontology,
                dataFactory.getOWLDataPropertyAssertionAxiom(
                counterAx.getProperty().asOWLDataProperty(),
                counterAx.getSubject().asOWLNamedIndividual(),
                literal)));
        manager.applyChanges(updates);

        return ontology;
    }

    //Finds all the IRIs that need converting
    private List<IRI> getIRIsToChange(OWLOntology ontology,String coreIri){
        //Collects the annotation property iris that do not need converting
        List<IRI> annotations = getAnnotationIRIs(ontology);

        //Initialises the list and regex pattern
        List<IRI> toChange = new ArrayList<>();

        Pattern pattern= Pattern.compile("[0-9]+");

        //loops through declarations looking for non Snomed

        for (OWLDeclarationAxiom da: ontology.getAxioms(AxiomType.DECLARATION)){
            IRI testIri = da.getEntity().getIRI();
            if (!annotations.contains(testIri)) {
                String testPattern = getLocalNumber(coreIri, testIri.toString());
                if (testPattern!=""){
                    Matcher m = pattern.matcher(testPattern);
                    if (!m.matches())
                        toChange.add(testIri);

                    }
                }
            }
        return toChange;

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
