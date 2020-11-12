package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.Logger;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.models.ConceptType;
import org.endeavourhealth.informationmanager.common.models.QuantificationType;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLFacet;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Manages the conversion of an OWL ontology to Discovery syntax using the Manc OWL API and Discovery APO
 * <p>Can be configured to remove auto generated declarations made by Protege</p>
 * @author      Richard Collier David Stables
 * @version     1.0
 * @since       1.0
 */
public class OWLToDiscovery {
    private List<String> ignoreIris = Collections.singletonList("owl:topObjectProperty");
    private DefaultPrefixManager defaultPrefixManager;
    private Map<String, Concept> concepts = new HashMap<>();
    private Map<String,Individual> individuals = new HashMap<>();
    private Ontology ontology;
    private OWLReasoner reasoner;


    public Ontology getOntology() {
        return ontology;
    }

    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }


    private String shortIRI(String iri) {
        if (ontology.getNamespace() != null) {
            for (Namespace ns : ontology.getNamespace()) {
                String front = ns.getIri();
                if (iri.startsWith(front))
                    return (ns.getPrefix() + iri.substring(front.length()));
            }
        }
        return iri;
    }

    private List<String> filteredNs;

    /**
     * Generates a Discovery syntax inferred ontology view with "isa" relationships
     *
     * @param owlOntology the ontology as an OWL2 ontology
     * @param owlFormat   prefix format which contains the prefixes for the namespaces in the target document
     * @return Document a Discovery json Document
     */
    public Document generateInferredView(OWLOntology owlOntology,
                                         OWLDocumentFormat owlFormat,
                                         OWLReasonerConfiguration config) throws Exception {
        reasoner = new FaCTPlusPlusReasonerFactory().createReasoner(owlOntology, config);
        reasoner.precomputeInferences();

        //Sets the standard ontology headers namespaces imports etc
        processDocumentHeaders(owlOntology, owlFormat);

        //Creates the class property declarations
        for (OWLDeclarationAxiom da : owlOntology.getAxioms(AxiomType.DECLARATION))
            processDeclarationAxiom(da, ontology);

        //Adds the annotation axioms to the classes
        for (OWLAnnotationAssertionAxiom a : owlOntology.getAxioms(AxiomType.ANNOTATION_ASSERTION))
            processAnnotationAssertionAxiom(a);

        //Steps down the trees
        OWLClass topClass = reasoner.getTopClassNode().getRepresentativeElement();
        for (Node<OWLClass> owlSubClass :
            reasoner.getSubClasses(topClass, true))
            processClassSubClasses(owlSubClass);

        OWLObjectProperty topOp = reasoner.getTopObjectPropertyNode().getRepresentativeElement().asOWLObjectProperty();
        for (Node<OWLObjectPropertyExpression> owlSubOp : reasoner.getSubObjectProperties(topOp, true))
            processObjectPropertySubProperties(owlSubOp);

        OWLDataProperty topDp = reasoner.getTopDataPropertyNode().getRepresentativeElement().asOWLDataProperty();
        for (Node<OWLDataProperty> owlSubDp : reasoner.getSubDataProperties(topDp, true))
            processDataPropertySubProperties(owlSubDp);

        reasoner.dispose();
        return new Document().setInformationModel(ontology);
    }

    private void processObjectPropertySubProperties(Node<OWLObjectPropertyExpression> parentOp) {
        OWLObjectPropertyExpression owlParent = parentOp.getRepresentativeElement();
        if (!owlParent.isAnonymous()) {
            ObjectProperty superOb = (ObjectProperty) concepts.get(getIri(owlParent.asOWLObjectProperty().getIRI()));
            Concept newSuper = new Concept();
            newSuper.setDbid((superOb.getDbid()));
            newSuper.setIri(superOb.getIri());
            newSuper.setName(superOb.getName());
            for (Node<OWLObjectPropertyExpression> owlSubOb : reasoner.getSubObjectProperties(owlParent, true)) {
                if (!owlSubOb.getRepresentativeElement().isAnonymous()) {
                    String iri = getIri(owlSubOb.getRepresentativeElement().asOWLObjectProperty().getIRI());
                    if (!iri.equals("owl:bottomObjectProperty")) {
                        ObjectProperty op = (ObjectProperty) concepts.get(iri);
                        op.addIsa(newSuper);
                        processObjectPropertySubProperties(owlSubOb);
                    }
                }
            }
        }

    }

    private void processDataPropertySubProperties(Node<OWLDataProperty> parentDp) {
        OWLDataProperty owlParent = parentDp.getRepresentativeElement().asOWLDataProperty();
        DataProperty superDp = (DataProperty) concepts.get(getIri(owlParent.asOWLDataProperty().getIRI()));
        if (superDp != null) {
            Concept newSuper = new Concept();
            newSuper.setDbid(superDp.getDbid());
            newSuper.setIri(superDp.getIri());
            newSuper.setName(superDp.getName());
            for (Node<OWLDataProperty> owlSubDp : reasoner.getSubDataProperties(owlParent, true)) {
                String iri = getIri(owlSubDp.getRepresentativeElement().asOWLDataProperty().getIRI());
                if (!iri.equals("owl:bottomDataProperty")) {
                    DataProperty dp = (DataProperty) concepts.get(iri);
                    dp.addIsa(newSuper);
                    processDataPropertySubProperties(owlSubDp);
                }
            }
        }
    }

    private void processClassSubClasses(Node<OWLClass> parentClass) {
        OWLClass owlParent = parentClass.getRepresentativeElement();
        Concept superC = (Concept) concepts.get(getIri(owlParent.asOWLClass().getIRI()));
        if (superC == null)
            return;
        Concept newSuper = new Concept();
        newSuper.setDbid(superC.getDbid());
        newSuper.setIri(superC.getIri());
        newSuper.setName(superC.getName());
        for (Node<OWLClass> owlSubclass : reasoner.getSubClasses(owlParent, true)) {
            String iri = getIri(owlSubclass.getRepresentativeElement().getIRI());
            if (!iri.equals("owl:Nothing")) {
                Concept c = concepts.get(iri);
                c.addIsa(newSuper);
                processClassSubClasses(owlSubclass);
            }
        }


    }


    /**
     * Transforms an ontology to Discovery syntax
     *
     * @param owlOntology      the owl ontology
     * @param filterNamespaces A list of namespace prefixes, whose declarations that you do not need in the final output but were auto generated by Protege
     * @return Document - A Discovery information model ontology document serializable as json
     */
    public Document transform(OWLOntology owlOntology, OWLDocumentFormat owlFormat, List<String> filterNamespaces) throws Exception {

        filteredNs = filterNamespaces;

        processDocumentHeaders(owlOntology, owlFormat);

        for (OWLDeclarationAxiom da : owlOntology.getAxioms(AxiomType.DECLARATION))
            processDeclarationAxiom(da, ontology);

        for (OWLAxiom a : owlOntology.getAxioms()) {
            if (a.getAxiomType() != AxiomType.DECLARATION)
                processAxiom(a, ontology);
        }
        clean();

        return new Document().setInformationModel(ontology);
    }

    private void processDocumentHeaders(OWLOntology owlOntology, OWLDocumentFormat owlFormat) {

        initializePrefixManager(owlOntology, owlFormat);

        setOntology(new Ontology());

        processOntology(owlOntology, ontology);

        processPrefixes(owlOntology, ontology);

        processImports(owlOntology, ontology);
    }


    private boolean isFiltered(Concept concept) {
        String[] prefix = concept.getIri().split(":");
        if (filteredNs.contains(prefix[0]))
            return true;
        return false;
    }

    private void removeConcepts(List<Object> removals, HashSet<Concept> conceptList) {
        if ((removals != null & (conceptList != null))) {
            removals.stream().forEach(conceptList::remove);
            removals.clear();
        }
    }

    private void clean() {
        List<Object> toRemove = new ArrayList<>();
        if (ontology.getConcept() != null)
            for (Concept c : ontology.getConcept())
                if (isFiltered(c))
                    toRemove.add(c);

        removeConcepts(toRemove, (HashSet) ontology.getConcept());

    }


    private void processImports(OWLOntology owlOntology, Ontology ontology) {
        if (owlOntology.getImports() != null) {
            if (owlOntology.getImportsDeclarations() != null) {
                owlOntology.getImportsDeclarations()
                    .forEach(y -> ontology.addImport(y.getIRI().toString()));
            }
        }
    }


    private void initializePrefixManager(OWLOntology ontology, OWLDocumentFormat owlFormat) {
        defaultPrefixManager = new DefaultPrefixManager();
        defaultPrefixManager.copyPrefixesFrom(owlFormat.asPrefixOWLOntologyFormat());
        OWLDocumentFormat ontologyFormat = new FunctionalSyntaxDocumentFormat();
        if (ontologyFormat instanceof PrefixDocumentFormat) {
            defaultPrefixManager.copyPrefixesFrom((PrefixDocumentFormat) ontologyFormat);
            defaultPrefixManager.setPrefixComparator(((PrefixDocumentFormat) ontologyFormat).getPrefixComparator());
            defaultPrefixManager.setDefaultPrefix(NamespaceIri.DISCOVERY.getValue());
            defaultPrefixManager.setPrefix("sn:", NamespaceIri.SNOMED.getValue());
        }

    }

    private void processPrefixes(OWLOntology ontology, Ontology discovery) {
        for (Map.Entry<String, String> prefix : defaultPrefixManager.getPrefixName2PrefixMap().entrySet()) {
            discovery.addNamespace(
                new Namespace()
                    .setPrefix(prefix.getKey())
                    .setIri(prefix.getValue())
            );
        }
    }

    private void processOntology(OWLOntology ontology, Ontology discovery) {
        discovery.setDocumentInfo(new DocumentInfo());
        discovery.setIri(OntologyIri.DISCOVERY.getValue());
        discovery.setModule(ontology.getOntologyID().getOntologyIRI().get().toString());

    }

    private void processDeclarationAxiom(OWLDeclarationAxiom a, Ontology discovery) throws Exception {
        OWLEntity e = a.getEntity();
        String iri = getIri(e.getIRI());

        if (e.getEntityType() == EntityType.CLASS) {
            Concept concept = new Concept();
            concept.setIri(iri);
            addToConcepts(iri, concept, discovery);
        } else if (e.getEntityType() == EntityType.OBJECT_PROPERTY) {
            ObjectProperty op = new ObjectProperty();
            op.setIri(iri);
            addToConcepts(iri, op, discovery);
        } else if (e.getEntityType() == EntityType.DATA_PROPERTY) {
            DataProperty dp = new DataProperty();
            dp.setIri(iri);
            addToConcepts(iri, dp, discovery);
        } else if (e.getEntityType() == EntityType.DATATYPE) {
            DataType dt = new DataType();
            dt.setIri(iri);
            addToConcepts(iri, dt, discovery);
        } else if (e.getEntityType() == EntityType.ANNOTATION_PROPERTY) {
            AnnotationProperty ap = new AnnotationProperty();
            ap.setIri(iri);
            addToConcepts(iri, ap, discovery);                               // <-- HERE
        } else if (e.getEntityType() == EntityType.NAMED_INDIVIDUAL) {
            Individual individual = new Individual();
            individual.setIri(iri);
            addToIndividuals(iri,individual,discovery);
        } else
            Logger.error("OWL Declaration: " + a);
    }

    private Individual addToIndividuals(String iri,Individual indi,Ontology discovery){
        Individual already= individuals.get(iri);
        if (already!=null)
            return already;
        Individual newInd= new Individual();
        newInd.setIri(iri);
        individuals.put(iri,newInd);
        discovery.addIndividual(newInd);
        return newInd;

    }

    private Concept addToConcepts(String iri, Concept concept, Ontology discovery) throws Exception {

        //This logic checks for clashes in types
        Concept already = concepts.get(iri);
        if (already != null) {
            if (already.getConceptType() != concept.getConceptType()) {
                if (already.getConceptType() == ConceptType.CLASSONLY) {
                    discovery.getConcept().remove(already);
                    concepts.remove(already);
                    concepts.put(iri, concept);
                    ontology.addConcept(concept);
                    return concept;
                } else if (concept.getConceptType() != ConceptType.CLASSONLY) {
                    Logger.error("Invalid concept, cannot be two types of property");
                    throw new Exception("Property type punning not allowed");
                }
                return already;
            }
            return already;
        } else {
            concepts.put(iri, concept);
            ontology.addConcept(concept);
            return concept;
        }
    }

    private void processAxiom(OWLAxiom a, Ontology discovery) {
        if (a.isOfType(AxiomType.OBJECT_PROPERTY_DOMAIN))
            processObjectPropertyDomainAxiom((OWLObjectPropertyDomainAxiom) a);
        else if (a.isOfType(AxiomType.SUBCLASS_OF))
            processSubClassAxiom((OWLSubClassOfAxiom) a);
        else if (a.isOfType(AxiomType.INVERSE_OBJECT_PROPERTIES))
            processInverseAxiom((OWLInverseObjectPropertiesAxiom) a);
        else if (a.isOfType(AxiomType.OBJECT_PROPERTY_RANGE))
            processObjectPropertyRangeAxiom((OWLObjectPropertyRangeAxiom) a);
        else if (a.isOfType(AxiomType.DIFFERENT_INDIVIDUALS))
            processDifferentIndividualsAxiom((OWLDifferentIndividualsAxiom) a);
        else if (a.isOfType(AxiomType.FUNCTIONAL_OBJECT_PROPERTY))
            processFunctionalObjectPropertyAxiom((OWLFunctionalObjectPropertyAxiom) a);
        else if (a.isOfType(AxiomType.FUNCTIONAL_DATA_PROPERTY))
            processFunctionalDataPropertyAxiom((OWLFunctionalDataPropertyAxiom) a);
        else if (a.isOfType(AxiomType.ANNOTATION_ASSERTION))
            processAnnotationAssertionAxiom((OWLAnnotationAssertionAxiom) a);
        else if (a.isOfType(AxiomType.ANNOTATION_PROPERTY_RANGE))
            processAnnotationPropertyRangeAxiom((OWLAnnotationPropertyRangeAxiom) a);
        else if (a.isOfType(AxiomType.EQUIVALENT_CLASSES))
            processEquivalentClassesAxiom((OWLEquivalentClassesAxiom) a);
        else if (a.isOfType(AxiomType.SUB_OBJECT_PROPERTY))
            processSubObjectPropertyAxiom((OWLSubObjectPropertyOfAxiom) a);
        else if (a.isOfType(AxiomType.CLASS_ASSERTION))
            processClassAssertionAxiom((OWLClassAssertionAxiom) a);
        else if (a.isOfType(AxiomType.SUB_DATA_PROPERTY))
            processSubDataPropertyAxiom((OWLSubDataPropertyOfAxiom) a);
        else if (a.isOfType(AxiomType.SUB_ANNOTATION_PROPERTY_OF))
            processSubAnnotationPropertyAxiom((OWLSubAnnotationPropertyOfAxiom) a);
        else if (a.isOfType(AxiomType.DATA_PROPERTY_RANGE))
            processDataPropertyRangeAxiom((OWLDataPropertyRangeAxiom) a);
        else if (a.isOfType(AxiomType.TRANSITIVE_OBJECT_PROPERTY))
            processTransitiveObjectPropertyAxiom((OWLTransitiveObjectPropertyAxiom) a);
        else if (a.isOfType(AxiomType.DATATYPE_DEFINITION))
            processDatatypeDefinitionAxiom((OWLDatatypeDefinitionAxiom) a);
        else if (a.isOfType(AxiomType.DATA_PROPERTY_ASSERTION))
            processDataPropertyAssertionAxiom((OWLDataPropertyAssertionAxiom) a);
        else if (a.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION))
            processObjectPropertyAssertionAxiom((OWLObjectPropertyAssertionAxiom) a);
        else if (a.isOfType(AxiomType.DATA_PROPERTY_DOMAIN))
            processDataPropertyDomainAxiom((OWLDataPropertyDomainAxiom) a);
        else if (a.isOfType(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY))
            processInverseFunctionalObjectPropertyAxiom((OWLInverseFunctionalObjectPropertyAxiom) a);
        else if (a.isOfType(AxiomType.SYMMETRIC_OBJECT_PROPERTY))
            processSymmetricObjectPropertyAxiom((OWLSymmetricObjectPropertyAxiom) a);
        else if (a.isOfType(AxiomType.SUB_PROPERTY_CHAIN_OF))
            processSubPropertyChainAxiom((OWLSubPropertyChainOfAxiom) a);
        else if (a.isOfType(AxiomType.DATA_PROPERTY_ASSERTION))
            processDataPropertyAssertionAxiom((OWLDataPropertyAssertionAxiom) a);
        else if (a.isOfType(AxiomType.DISJOINT_CLASSES))
            processDisjointClassesAxion((OWLDisjointClassesAxiom) a);
        else
            Logger.error("Axiom: " + a);
    }

    private void processObjectPropertyDomainAxiom(OWLObjectPropertyDomainAxiom a) {
        String propertyIri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty) concepts.get(propertyIri);
        ClassAxiom pd = new ClassAxiom();
        processAxiomAnnotations(a, pd);
        op.addPropertyDomain(pd);

        if (a.getDomain().getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            String domainIri = getIri(a.getDomain().asOWLClass().getIRI());
            pd.setClazz(new ConceptReference(domainIri));
        } else if (a.getDomain().getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            pd.setUnion(getOWLUnion((OWLObjectUnionOf) a.getDomain()));
        } else {
            Logger.error("Invalid object property domain : " + propertyIri);
        }
    }


    private void processSubClassAxiom(OWLSubClassOfAxiom a) {
        String iri = getIri(((OWLClass) a.getSubClass()).getIRI());

        Concept c = concepts.get(iri);
        if (c == null)
            Logger.info("Ignoring abstract subClass: [" + iri + "]");
        else {
            ClassAxiom subClassOf = new ClassAxiom();
            processAxiomAnnotations(a, subClassOf);
            addOwlClassExpressionToClassExpression(a.getSuperClass(), subClassOf);

            c.addSubClassOf(subClassOf);
        }

    }

    private void addOwlClassExpressionToPropertyValue(OWLClassExpression oce, ObjectPropertyValue pv){
        if (oce.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            pv.setValueType(new ConceptReference(
                getIri(oce.asOWLClass().getIRI())));
        } else
            pv.setExpression(getOWLClassExpression(oce)) ;
    }


    private void addOwlClassExpressionToClassExpression(OWLClassExpression oce, ClassExpression cex) {
        if (oce.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            cex.setClazz(new ConceptReference(
                getIri(oce.asOWLClass().getIRI()))
            );
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
            cex.setIntersection(getOWLIntersection((OWLObjectIntersectionOf) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            cex.setUnion(getOWLUnion((OWLObjectUnionOf) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
            cex.setObjectPropertyValue(getObjectSomeValuesFrom((OWLObjectSomeValuesFrom) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.DATA_HAS_VALUE) {
            cex.setDataPropertyValue(getDataHasValue((OWLDataHasValue) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_EXACT_CARDINALITY) {
            cex.setObjectPropertyValue(getObjectExactCardinality((OWLObjectExactCardinality) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_MAX_CARDINALITY) {
            cex.setObjectPropertyValue(getObjectMaxCardinality((OWLObjectMaxCardinality) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.DATA_EXACT_CARDINALITY) {
            cex.setDataPropertyValue(getDataExactCardinality((OWLDataExactCardinality) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.DATA_MAX_CARDINALITY) {
            cex.setDataPropertyValue(getDataMaxCardinality((OWLDataMaxCardinality) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.DATA_MIN_CARDINALITY) {
            cex.setDataPropertyValue(getDataMinCardinality((OWLDataMinCardinality) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.DATA_SOME_VALUES_FROM) {
            cex.setDataPropertyValue(getDataSomeValues((OWLDataSomeValuesFrom) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
            cex.setObjectPropertyValue(getObjectSomeValuesFrom((OWLObjectSomeValuesFrom) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_MIN_CARDINALITY) {
            cex.setObjectPropertyValue(getObjectMinCardinality((OWLObjectMinCardinality) oce));

        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_HAS_VALUE) {
            cex.setObjectPropertyValue(getObjectHasValue((OWLObjectHasValue) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_COMPLEMENT_OF) {
            cex.setComplementOf(getOWLClassExpression(oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_ONE_OF) {
            cex.setObjectOneOf(getOWLObjectOneOf((OWLObjectOneOf) oce));

        } else {
            Logger.error("OWL Class expression: " + oce);
            throw new IllegalStateException("Unhandled class expression type: " + oce.getClassExpressionType().getName());
        }
    }

    private List<ConceptReference> getOWLObjectOneOf(OWLObjectOneOf oce) {
        List<ConceptReference> oneOfList = new ArrayList<>();
        for (OWLIndividual individual : oce.getIndividuals()) {
            String oneOf = getIri(individual.asOWLNamedIndividual().getIRI());
            oneOfList.add(new ConceptReference(oneOf));
        }
        return oneOfList;
    }


    private List<ClassExpression> getOWLIntersection(OWLObjectIntersectionOf oi) {
        List<ClassExpression> result = new ArrayList<>();

        for (OWLClassExpression c : oi.getOperandsAsList()) {
            result.add(getOWLClassExpression(c));
            if (result == null)
                Logger.error("OWLIntersection:" + c);
        }
        return result;
    }

    private ClassExpression getOWLClassExpression(OWLClassExpression c) {
        if (c instanceof OWLClass) {
            return getOWLClassAsClassExpression(c.asOWLClass());
        } else if (c.getClassExpressionType() == ClassExpressionType.DATA_HAS_VALUE) {
            return getOWLDataHasValueAsClassExpression((OWLDataHasValue) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_EXACT_CARDINALITY) {
            return getOWLObjectExactCardinalityAsClassExpression((OWLObjectExactCardinality) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_MAX_CARDINALITY) {
            return getOWLObjectMaxCardinalityAsClassExpression((OWLObjectMaxCardinality) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.DATA_EXACT_CARDINALITY) {
            return getOWLDataExactCardinalityAsClassExpression((OWLDataExactCardinality) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.DATA_MAX_CARDINALITY) {
            return getOWLDataMaxCardinalityAsClassExpression((OWLDataMaxCardinality) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.DATA_MIN_CARDINALITY) {
            return (getOWLDataMinCardinalityAsClassExpression((OWLDataMinCardinality) c));
        } else if (c.getClassExpressionType() == ClassExpressionType.DATA_SOME_VALUES_FROM) {
            return getOWLDataSomeValuesAsClassExpression((OWLDataSomeValuesFrom) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
            return getOWLObjectSomeValuesAsClassExpression((OWLObjectSomeValuesFrom) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_MIN_CARDINALITY) {
            return getOWLObjectMinCardinalityAsClassExpression((OWLObjectMinCardinality) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
            return getOWLObjectIntersectionAsClassExpression((OWLObjectIntersectionOf) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            return getOWLObjectUnionAsClassExpression((OWLObjectUnionOf) c);
        } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_HAS_VALUE) {
            Logger.info("Ignoring OWLIntersection:ObjectHasValue: " + getIri(((OWLObjectHasValue) c).getFiller().asOWLNamedIndividual().getIRI()));
        } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_COMPLEMENT_OF) {
            return getOWLObjectComplementOfAsClassExpression((OWLObjectComplementOf) c);
        } else {
            Logger.error("OWLIntersection:" + c);
        }
        return null;
    }


    private List<ClassExpression> getOWLUnion(OWLObjectUnionOf ou) {
        List<ClassExpression> result = new ArrayList<>();

        for (OWLClassExpression c : ou.getOperandsAsList()) {
            if (c instanceof OWLClass) {
                result.add(getOWLClassAsClassExpression(c.asOWLClass()));
            } else if (c.getClassExpressionType() == ClassExpressionType.DATA_HAS_VALUE) {
                result.add(getOWLDataHasValueAsClassExpression((OWLDataHasValue) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_EXACT_CARDINALITY) {
                result.add(getOWLObjectExactCardinalityAsClassExpression((OWLObjectExactCardinality) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.DATA_EXACT_CARDINALITY) {
                result.add(getOWLDataExactCardinalityAsClassExpression((OWLDataExactCardinality) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.DATA_MAX_CARDINALITY) {
                result.add(getOWLDataMaxCardinalityAsClassExpression((OWLDataMaxCardinality) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.DATA_SOME_VALUES_FROM) {
                result.add(getOWLDataSomeValuesAsClassExpression((OWLDataSomeValuesFrom) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
                result.add(getOWLObjectSomeValuesAsClassExpression((OWLObjectSomeValuesFrom) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_MIN_CARDINALITY) {
                result.add(getOWLObjectMinCardinalityAsClassExpression((OWLObjectMinCardinality) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
                result.add(getOWLObjectIntersectionAsClassExpression((OWLObjectIntersectionOf) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
                result.add(getOWLObjectUnionAsClassExpression((OWLObjectUnionOf) c));
            } else
                Logger.error("OWLUnion:" + c);
        }

        return result;
    }

    private ClassExpression getOWLClassAsClassExpression(OWLClass owlClass) {
        return new ClassExpression().setClazz(new ConceptReference(getIri(owlClass.getIRI())));
    }

    private ClassExpression getOWLDataHasValueAsClassExpression(OWLDataHasValue dataHasValue) {
        ClassExpression result = new ClassExpression();
        result.setDataPropertyValue(getDataHasValue(dataHasValue));
        return result;
    }

    private ObjectPropertyValue getObjectHasValue(OWLObjectHasValue objectHasValue) {

        ObjectPropertyValue ope = new ObjectPropertyValue();
        ope.setProperty(new ConceptReference(getIri(objectHasValue
            .getProperty()
            .asOWLObjectProperty()
            .getIRI())));
        ope.setIndividual(getIri(objectHasValue.getValue().asOWLNamedIndividual().getIRI()));
        return ope;
    }

    private DataPropertyValue getDataHasValue(OWLDataHasValue dataHasValue) {
        OWLLiteral lit = dataHasValue.getValue();
        DataPropertyValue dpe = new DataPropertyValue();
        dpe.setProperty(new ConceptReference(getIri(dataHasValue
            .getProperty()
            .asOWLDataProperty()
            .getIRI())));
        dpe.setMin(1);
        dpe.setMax(1);
        dpe.setValueData(lit.getLiteral());
        dpe.setDataType(getDTIri(getIri(lit.getDatatype().getIRI())));
        return dpe;
    }

    private ClassExpression getOWLObjectExactCardinalityAsClassExpression(OWLObjectExactCardinality exactCardinality) {
        ClassExpression result = new ClassExpression();
        result.setObjectPropertyValue(getObjectExactCardinality(exactCardinality));
        return result;
    }

    private ObjectPropertyValue getObjectExactCardinality(OWLObjectExactCardinality exactCardinality) {

        ObjectPropertyValue cardinalityRestriction = new ObjectPropertyValue();
        if (exactCardinality.getProperty().isAnonymous()){
            cardinalityRestriction
                .setInverseOf(new ConceptReference(getIri(exactCardinality
                    .getProperty()
                    .getInverseProperty()
                    .asOWLObjectProperty()
                    .getIRI())))
                .setMin(exactCardinality.getCardinality())
                .setMax(exactCardinality.getCardinality());

        } else {

            cardinalityRestriction
                .setProperty(new ConceptReference(getIri(exactCardinality
                    .getProperty()
                    .asOWLObjectProperty()
                    .getIRI())))
                .setMin(exactCardinality.getCardinality())
                .setMax(exactCardinality.getCardinality());
        }

        addOwlClassExpressionToPropertyValue(exactCardinality.getFiller(), cardinalityRestriction);

        return cardinalityRestriction;
    }

    private ClassExpression getOWLObjectMaxCardinalityAsClassExpression(OWLObjectMaxCardinality maxCardinality) {
        ClassExpression result = new ClassExpression();
        result.setObjectPropertyValue(getObjectMaxCardinality(maxCardinality));
        return result;
    }

    private ObjectPropertyValue getObjectMaxCardinality(OWLObjectMaxCardinality maxCardinality) {

        ObjectPropertyValue cardinalityRestriction = new ObjectPropertyValue();
        if (maxCardinality.getProperty().isAnonymous()){
            cardinalityRestriction
                .setInverseOf(new ConceptReference(getIri(maxCardinality
                    .getProperty()
                    .getInverseProperty()
                    .asOWLObjectProperty()
                    .getIRI())))
                .setMax(maxCardinality.getCardinality());

        } else {
            cardinalityRestriction
                .setProperty(new ConceptReference(getIri(maxCardinality
                    .getProperty()
                    .asOWLObjectProperty()
                    .getIRI())))
                .setMax(maxCardinality.getCardinality());
        }

        addOwlClassExpressionToPropertyValue(maxCardinality.getFiller(), cardinalityRestriction);

        return cardinalityRestriction;
    }

    private ClassExpression getOWLDataExactCardinalityAsClassExpression(OWLDataExactCardinality exactCardinality) {
        ClassExpression result = new ClassExpression();
        result.setDataPropertyValue(getDataExactCardinality(exactCardinality));
        return result;
    }

    private DataPropertyValue getDataExactCardinality(OWLDataExactCardinality exactCardinality) {

        DataPropertyValue card = new DataPropertyValue();
        card.setProperty(new ConceptReference(getIri(exactCardinality
            .getProperty()
            .asOWLDataProperty()
            .getIRI())))
            .setMin(exactCardinality.getCardinality())
            .setMax(exactCardinality.getCardinality());
        return getFiller(exactCardinality.getFiller(), card);

    }

    private ClassExpression getOWLDataMaxCardinalityAsClassExpression(OWLDataMaxCardinality maxCardinality) {
        ClassExpression result = new ClassExpression();

        result.setDataPropertyValue(getDataMaxCardinality(maxCardinality));

        return result;
    }

    private ClassExpression getOWLDataMinCardinalityAsClassExpression(OWLDataMinCardinality minCardinality) {
        ClassExpression result = new ClassExpression();

        result.setDataPropertyValue(getDataMinCardinality(minCardinality));

        return result;
    }


    private DataPropertyValue getDataMaxCardinality(OWLDataMaxCardinality maxCardinality) {

        DataPropertyValue card = new DataPropertyValue();
        card.setProperty(new ConceptReference(getIri(maxCardinality
            .getProperty()
            .asOWLDataProperty()
            .getIRI())))
            .setMax(maxCardinality.getCardinality());
        return getFiller(maxCardinality.getFiller(), card);
    }

    private DataPropertyValue getFiller(OWLDataRange owlRange,
                                          DataPropertyValue card) {

        DataRangeType rangeType = owlRange.getDataRangeType();
        if (rangeType == DataRangeType.DATA_ONE_OF) {
            Logger.error("Data one of is not supported. Create new restricted data type instead");

            getOWLOneOfAsPropertyValue((OWLDataOneOf) owlRange, card);
            return card;
        } else if (rangeType == DataRangeType.DATATYPE) {
            card.setDataType(owlRange.asOWLDatatype().getIRI().toString());
            return card;
        } else if (rangeType == DataRangeType.DATATYPE_RESTRICTION) {
            Logger.error("data range of data type restriction is not supported. Create new data type instead");
            String valueData = "[";
            OWLDatatypeRestriction owlDts = (OWLDatatypeRestriction) owlRange;
            card.setDataType(owlDts.getDatatype().getIRI().toString());
            for (OWLFacetRestriction facet : owlDts.getFacetRestrictions()) {
                if (facet.getFacet() == OWLFacet.MIN_INCLUSIVE) {
                    valueData = "[>=" + facet.getFacetValue().getLiteral();
                } else if (facet.getFacet() == OWLFacet.MIN_EXCLUSIVE) {
                    valueData = "[>" + facet.getFacetValue().getLiteral();
                } else if (facet.getFacet() == OWLFacet.MAX_INCLUSIVE) {
                    valueData = valueData + "][<=" + facet.getFacetValue().getLiteral() + "]";
                } else if (facet.getFacet() == OWLFacet.MAX_EXCLUSIVE) {
                    valueData = valueData + "][<" + facet.getFacetValue().getLiteral() + "]";
                } else if (facet.getFacet() == OWLFacet.PATTERN)
                    valueData = "Regex:" + facet.getFacetValue().getLiteral();
                else {
                    Logger.error("unsupported owl facet type. Set to pattern ");
                    valueData = "Regex:" + facet.getFacetValue().getLiteral();
                }
            }
            card.setValueData(valueData);
            return card;

        } else {
            Logger.error("unrecognised OWL data property restriction");
            card.setValueData(rangeType.getName());
            return card;
        }
    }


    private DataPropertyValue getDataMinCardinality(OWLDataMinCardinality minCardinality) {
        DataPropertyValue card = new DataPropertyValue();
        card.setProperty(new ConceptReference(getIri(minCardinality.getProperty()
            .asOWLDataProperty()
            .getIRI())))
            .setMin(minCardinality.getCardinality());
        return getFiller(minCardinality.getFiller(), card);
    }

    private ClassExpression getOWLDataSomeValuesAsClassExpression(OWLDataSomeValuesFrom some) {
        ClassExpression result = new ClassExpression();
        result.setDataPropertyValue(getDataSomeValues(some));


        return result;
    }


    private DataPropertyValue getDataSomeValues(OWLDataSomeValuesFrom some) {
        DataPropertyValue card = new DataPropertyValue();
        card.setProperty(new ConceptReference(getIri(some.getProperty()
            .asOWLDataProperty()
            .getIRI())))
            .setQuantification(QuantificationType.SOME);
        return getFiller(some.getFiller(), card);

    }

    private DataPropertyValue getOWLOneOfAsPropertyValue(OWLDataOneOf owlOneOf, DataPropertyValue card) {
        String oneOf = "";
        for (OWLLiteral one : owlOneOf.getValues()) {
            card.setDataType(getDTIri(getIri(one.getDatatype().getIRI())));
            if (oneOf != "")
                oneOf = oneOf + "," + one.getLiteral();
            else
                oneOf = one.getLiteral();
        }
        oneOf = "[" + oneOf + "]";
        card.setValueData(oneOf);
        return card;
    }

    private String getDTIri(String rdfType) {
        if (rdfType.equals("rdf:PlainLiteral"))
            return "xsd:string";
        else
            return rdfType;
    }


    private ClassExpression getOWLObjectSomeValuesAsClassExpression(OWLObjectSomeValuesFrom someValuesFrom) {
        ClassExpression result = new ClassExpression();

        ObjectPropertyValue oper = getObjectSomeValuesFrom(someValuesFrom);

        result.setObjectPropertyValue(oper);

        return result;
    }

    private ObjectPropertyValue getObjectSomeValuesFrom(OWLObjectSomeValuesFrom someValuesFrom) {
        ObjectPropertyValue oper = new ObjectPropertyValue();
       if (someValuesFrom.getProperty().isAnonymous()) {
           oper.setInverseOf(new ConceptReference(getIri(someValuesFrom.getProperty()
               .getInverseProperty()
               .asOWLObjectProperty()
               .getIRI())));
           oper.setQuantification(QuantificationType.SOME);
        } else {
           oper.setProperty(new ConceptReference(getIri(someValuesFrom.getProperty()
               .asOWLObjectProperty()
               .getIRI())));
           oper.setQuantification(QuantificationType.SOME);
       }
       addOwlClassExpressionToPropertyValue(someValuesFrom.getFiller(), oper);
       return oper;
    }

    private ClassExpression getOWLObjectMinCardinalityAsClassExpression(OWLObjectMinCardinality minCardinality) {
        ClassExpression result = new ClassExpression();

        ObjectPropertyValue cardinalityRestriction = getObjectMinCardinality(minCardinality);

        result.setObjectPropertyValue(cardinalityRestriction);

        return result;
    }


    private ObjectPropertyValue getObjectMinCardinality(OWLObjectMinCardinality minCardinality) {
        ObjectPropertyValue cardinalityRestriction = new ObjectPropertyValue();
        if (minCardinality.getProperty().isAnonymous()) {
            cardinalityRestriction
                .setInverseOf(new ConceptReference(getIri(minCardinality.getProperty()
                    .getInverseProperty()
                    .asOWLObjectProperty()
                    .getIRI())))
                .setMin(minCardinality.getCardinality());

        } else {
        cardinalityRestriction
            .setProperty(new ConceptReference(getIri(minCardinality.getProperty()
                .asOWLObjectProperty()
                .getIRI())))
            .setMin(minCardinality.getCardinality());
        }
        addOwlClassExpressionToPropertyValue(minCardinality.getFiller(), cardinalityRestriction);
        return cardinalityRestriction;
    }

    private ClassExpression getOWLObjectComplementOfAsClassExpression(OWLObjectComplementOf complement) {
        ClassExpression result = new ClassExpression();
        result.setComplementOf(getOWLClassExpression(complement.getOperand()));
        return result;
    }

    private ClassExpression getOWLObjectIntersectionAsClassExpression(OWLObjectIntersectionOf intersectionOf) {
        ClassExpression result = new ClassExpression();

        result.setIntersection(getOWLIntersection(intersectionOf));

        return result;
    }

    private ClassExpression getOWLObjectUnionAsClassExpression(OWLObjectUnionOf unionOf) {
        ClassExpression result = new ClassExpression();

        result.setUnion(getOWLUnion(unionOf));

        return result;
    }

    private void processInverseAxiom(OWLInverseObjectPropertiesAxiom a) {
        String firstIri = getIri(a.getFirstProperty().getNamedProperty().getIRI());
        String secondIri = getIri(a.getSecondProperty().getNamedProperty().getIRI());

        ObjectProperty op = (ObjectProperty) concepts.get(firstIri);
        PropertyAxiom inverse = new PropertyAxiom();
        processAxiomAnnotations(a, inverse);
        inverse.setProperty(new ConceptReference(secondIri));
        op.setInversePropertyOf(inverse);
    }

    private void processObjectPropertyRangeAxiom(OWLObjectPropertyRangeAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty) concepts.get(iri);
        ClassAxiom cex = new ClassAxiom();
        processAxiomAnnotations(a, cex);
        op.addObjectPropertyRange(cex);

        addOwlClassExpressionToClassExpression(a.getRange(), cex);

    }

    private void processDifferentIndividualsAxiom(OWLDifferentIndividualsAxiom a) {
        Logger.info("Ignoring different individuals: [" + a.toString() + "]");
    }

    private void processFunctionalObjectPropertyAxiom(OWLFunctionalObjectPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty) concepts.get(iri);
        Axiom isFunc = new Axiom();
        processAxiomAnnotations(a, isFunc);
        op.setIsFunctional(isFunc);
    }

    private void processFunctionalDataPropertyAxiom(OWLFunctionalDataPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLDataProperty().getIRI());

        DataProperty dp = (DataProperty) concepts.get(iri);
        Axiom isFunc = new Axiom();
        processAxiomAnnotations(a, isFunc);
        dp.setIsFunctional(isFunc);


    }

    private void processAnnotationAssertionAxiom(OWLAnnotationAssertionAxiom a) {
        String iri = getIri((IRI) a.getSubject());

        if (ignoreIris.contains(iri))
            return;

        String property = getIri(a.getProperty().asOWLAnnotationProperty().getIRI());

        String value;
        if (a.getValue().isLiteral()) {
            value = a.getValue().asLiteral().get().getLiteral();
        } else if (a.getValue().isIRI()) {
            value = getIri(a.getValue().asIRI().get());
        } else {
            Logger.error("Annotation has no literal!");
            return;
        }

        Concept c = concepts.get(iri);
        if (c==null)
            c= individuals.get(iri);
        if (c == null) {
            Logger.error("Annotation assertion for undeclared concept: [" + iri + "]");
            return;
        }
        if (property.equals("prov:definition")) {
                c.setDescription(value);
        } else if (property.equals("rdfs:comment")) {
                if (c.getDescription() == null) {
                    c.setDescription(value);
                } else {
                    Annotation annotation = new Annotation();
                    annotation.setProperty(property);
                    annotation.setValue(value);
                    c.addAnnotation(annotation);
                }
        } else if (property.equals("rdfs:label"))
            c.setName(value);
         else if (property.equals(Common.HAS_STATUS))
                c.setStatus(ConceptStatus.byName(value));
        else if (property.equals(Common.HAS_CODE))
            c.setCode(value);
        else if (property.equals(Common.HAS_SCHEME))
            c.setScheme(value);
        else if (property.equals(Common.HAS_VERSION))
            c.setVersion(Integer.parseInt(value));
        else {
            Annotation annotation = new Annotation();
            annotation.setProperty(property);
            annotation.setValue(value);
            c.addAnnotation(annotation);
        }

    }

    private void processAnnotationPropertyRangeAxiom(OWLAnnotationPropertyRangeAxiom a) {
        String iri = getIri(a.getProperty().asOWLAnnotationProperty().getIRI());

        AnnotationProperty dp = (AnnotationProperty) concepts.get(iri);
        AnnotationPropertyRangeAxiom range = new AnnotationPropertyRangeAxiom();
        processAxiomAnnotations(a, range);
        dp.addPropertyRange(range);
        range.setIri(getIri(a.getRange()));

    }

    private void processEquivalentClassesAxiom(OWLEquivalentClassesAxiom a) {
        Iterator<OWLClassExpression> i = a.getClassExpressions().iterator();
        String iri = getIri(i.next().asOWLClass().getIRI());

        Concept c = concepts.get(iri);

        if (c == null)
            Logger.info("Ignoring abstract class: [" + iri + "]");
        else {
            while (i.hasNext()) {
                ClassAxiom cex = new ClassAxiom();
                processAxiomAnnotations(a, cex);
                addOwlClassExpressionToClassExpression(i.next(), cex);
                c.addEquivalentTo(cex);
            }
        }
    }

    private void processAxiomAnnotations(OWLAxiom a, Axiom im) {
        if (!a.getAnnotations().isEmpty()) {
            {
                a.getAnnotations()
                    .forEach(y ->
                        {
                            String property = getIri(y.getProperty().asOWLAnnotationProperty().getIRI());
                            String value = y.getValue().asLiteral().get().getLiteral();
                            switch (property) {
                                case Common.HAS_STATUS:
                                    im.setStatus(ConceptStatus.byName(value));
                                    break;

                                case Common.HAS_VERSION:
                                    im.setVersion(Integer.parseInt(value));
                                    break;
                            }

                        }
                    );
            }
        }
    }

    private void processAxiomAnnotations(OWLAxiom a, ClassAxiom im) {
        if (!a.getAnnotations().isEmpty()) {
            {
                a.getAnnotations()
                    .forEach(y ->
                        {
                            String property = getIri(y.getProperty().asOWLAnnotationProperty().getIRI());
                            String value = y.getValue().asLiteral().get().getLiteral();
                            switch (property) {
                                case Common.HAS_STATUS:
                                    im.setStatus(ConceptStatus.byName(value));
                                    break;
                                case Common.HAS_VERSION:
                                    im.setVersion(Integer.parseInt(value));
                                    break;
                            }

                        }
                    );
            }
        }
    }

    private void processSubObjectPropertyAxiom(OWLSubObjectPropertyOfAxiom a) {
        String iri = getIri(a.getSubProperty().asOWLObjectProperty().getIRI());
        try {
            ObjectProperty op = (ObjectProperty) concepts.get(iri);

            PropertyAxiom sp = new PropertyAxiom();
            processAxiomAnnotations(a, sp);
            sp.setProperty(getIri(a.getSuperProperty().asOWLObjectProperty().getIRI()));
            op.addSubObjectPropertyOf(sp);
        } catch (Exception e) {
            Logger.error("annotation property as object property ? : " + iri);
        }

    }

    private void processClassAssertionAxiom(OWLClassAssertionAxiom a) {
        String indiIri = getIri(a.getIndividual().asOWLNamedIndividual().getIRI());
        String classIri = getIri(a.getClassExpression().asOWLClass().getIRI());
        Individual ind = (Individual) individuals.get(indiIri);
        ind.setIsType(new ConceptReference(classIri));
    }

    private void processSubDataPropertyAxiom(OWLSubDataPropertyOfAxiom a) {
        String iri = getIri(a.getSubProperty().asOWLDataProperty().getIRI());
        //Logger.error(iri);
        DataProperty dp = (DataProperty) concepts.get(iri);
        PropertyAxiom sp = new PropertyAxiom();
        processAxiomAnnotations(a, sp);
        sp.setProperty(getIri(a.getSuperProperty().asOWLDataProperty().getIRI()));
        dp.addSubDataPropertyOf((sp));
    }

    private void processSubAnnotationPropertyAxiom(OWLSubAnnotationPropertyOfAxiom a) {
        String iri = getIri(a.getSubProperty().asOWLAnnotationProperty().getIRI());
        AnnotationProperty ap = (AnnotationProperty) concepts.get(iri);
        PropertyAxiom sp = new PropertyAxiom();
        processAxiomAnnotations(a, sp);
        ap.addSubAnnotationPropertyOf(sp);
        sp.setProperty(
            getIri(a.getSuperProperty().asOWLAnnotationProperty().getIRI())
        );
    }

    private void processDataPropertyRangeAxiom(OWLDataPropertyRangeAxiom a) {
        String iri = getIri(a.getProperty().asOWLDataProperty().getIRI());
        DataProperty dp = (DataProperty) concepts.get(iri);
        DataPropertyRange prax = new DataPropertyRange();
        processAxiomAnnotations(a, prax);
        dp.addDataPropertyRange(prax);
        if (a.getRange().getDataRangeType() == DataRangeType.DATATYPE)
            prax.setDataType(getIri(a.getRange().asOWLDatatype().getIRI()));
    }

    private void processTransitiveObjectPropertyAxiom(OWLTransitiveObjectPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());
        if (ignoreIris.contains(iri))
            return;

        ObjectProperty op = (ObjectProperty) concepts.get(iri);
        Axiom isTrans = new Axiom();
        processAxiomAnnotations(a, isTrans);
        op.setIsTransitive(isTrans);
    }
    private void processDatatypeDefinitionAxiom(OWLDatatypeDefinitionAxiom a) {
        String iri = getIri(a.getDatatype().asOWLDatatype().getIRI());
        DataType dt = (DataType) concepts.get(iri);
        OWLDataRange owlRange = a.getDataRange();

        DataTypeDefinition dtd = new DataTypeDefinition();
        processAxiomAnnotations(a, dtd);
        DataRangeType rangeType = owlRange.getDataRangeType();
        if (rangeType == DataRangeType.DATA_ONE_OF) {
            OWLDataOneOf owlOneOf = (OWLDataOneOf) owlRange;
            for (OWLLiteral one : owlOneOf.getValues())
                dtd.addOneOf(one.getLiteral());
        } else if (rangeType == DataRangeType.DATATYPE_RESTRICTION) {
            OWLDatatypeRestriction owlDts = (OWLDatatypeRestriction) owlRange;
            OWLDatatype owldt= ((OWLDatatypeRestriction) owlRange).getDatatype();
            dtd.setDataType(new ConceptReference(getIri(owldt.getIRI())));
            for (OWLFacetRestriction facet : owlDts.getFacetRestrictions()) {
                if (facet.getFacet() == OWLFacet.MIN_INCLUSIVE) {
                    dtd.setMinOperator(">=");
                    dtd.setMinValue(facet.getFacetValue().getLiteral());
                } else if (facet.getFacet() == OWLFacet.MIN_EXCLUSIVE) {
                    dtd.setMinOperator(">");
                    dtd.setMinValue(facet.getFacetValue().getLiteral());
                } else if (facet.getFacet() == OWLFacet.MAX_INCLUSIVE) {
                    dtd.setMaxOperator("<=");
                    dtd.setMaxValue(facet.getFacetValue().getLiteral());
                } else if (facet.getFacet() == OWLFacet.MAX_EXCLUSIVE) {
                    dtd.setMaxOperator("<");
                    dtd.setMaxValue(facet.getFacetValue().getLiteral());
                } else if (facet.getFacet() == OWLFacet.PATTERN)
                    dtd.setPattern(facet.getFacetValue().getLiteral());
                else {
                    Logger.error("unsupported owl facet type. Set to pattern [" +
                        facet.getFacetValue().getLiteral());
                    dtd.setPattern(facet.getFacetValue().getLiteral());
                }
            }
            dt.setDataTypeDefinition(dtd);
        }
    }


        private void processDataPropertyAssertionAxiom(OWLDataPropertyAssertionAxiom a) {
            String indiIri = getIri(a.getSubject().asOWLNamedIndividual().getIRI());
            String propertyIri = getIri(a.getProperty().asOWLDataProperty().getIRI());
            Individual ind = individuals.get(indiIri);
            DataPropertyValue pval = new DataPropertyValue();
            pval.setProperty(new ConceptReference(getIri(a.getProperty()
                .asOWLDataProperty()
                .getIRI())));
            pval.setDataType(getIri(a.getObject().getDatatype().getIRI()));
            pval.setValueData((a.getObject().getLiteral()));
            ind.addDataPropertyAssertion(pval);

        }


        private void processObjectPropertyAssertionAxiom(OWLObjectPropertyAssertionAxiom a) {
            String indiIri = getIri(a.getSubject().asOWLNamedIndividual().getIRI());
            String propertyIri = getIri(a.getProperty().asOWLObjectProperty().getIRI());
            Individual ind = (Individual) individuals.get(indiIri);
            ObjectPropertyValue pval = new ObjectPropertyValue();
            pval.setProperty(new ConceptReference(getIri(a.getProperty()
                .asOWLObjectProperty()
                .getIRI())));
            pval.setValueType(getIri(a.getObject().asOWLNamedIndividual().getIRI()));
            ind.addObjectPropertyAssertion(pval);

        }

        private void processDataPropertyDomainAxiom(OWLDataPropertyDomainAxiom a) {
            String propertyIri = getIri(a.getProperty().asOWLDataProperty().getIRI());
            String domainIri = getIri(a.getDomain().asOWLClass().getIRI());

            DataProperty dp = (DataProperty)concepts.get(propertyIri);
            ClassAxiom pd = new ClassAxiom();
            processAxiomAnnotations(a,pd);
            dp.addPropertyDomain(pd);
            pd.setClazz(new ConceptReference(domainIri));
        }
        private void processDisjointClassesAxion(OWLDisjointClassesAxiom a) {
            Set<String> disjoints = new HashSet<>();
            a.getClassExpressions().forEach(x -> disjoints.add(getIri(x.asOWLClass().getIRI())));
            for (String iri:disjoints){
                Concept c= concepts.get(iri);
                for (String with:disjoints){
                    if (!with.equals(iri)){
                        c.addDisjointWith(with);
                    }
                }
            }

        }

        private void processInverseFunctionalObjectPropertyAxiom(OWLInverseFunctionalObjectPropertyAxiom a) {
            String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

            Logger.info("Ignoring inverse functional object property axiom: [" + iri + "]");
        }

        private void processSymmetricObjectPropertyAxiom(OWLSymmetricObjectPropertyAxiom a) {
            String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

            ObjectProperty op = (ObjectProperty)concepts.get(iri);

            Axiom isSymmetric = new Axiom();
            processAxiomAnnotations(a, isSymmetric);
            op.setIsSymmetric(isSymmetric);
        }


        private void processSubPropertyChainAxiom(OWLSubPropertyChainOfAxiom a) {
            String iri = getIri(a.getSuperProperty().asOWLObjectProperty().getIRI());

            ObjectProperty op = (ObjectProperty)concepts.get(iri);
            SubPropertyChain chain = new SubPropertyChain();
            processAxiomAnnotations(a,chain);

            op.addSubPropertyChain(chain
                .setProperty(
                    a.getPropertyChain().stream()
                        .map(ope -> new ConceptReference(getIri(ope.asOWLObjectProperty().getIRI())))
                        .collect(Collectors.toSet())

                )
            );
        }

        private String getIri(IRI iri) {
            String result = defaultPrefixManager.getPrefixIRI(iri);

            return (result == null)
                ? shortIRI(iri.toString())
                : result;
        }


    }


