package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.impl.OWLClassNode;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
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
     * @param owlOntology the ontology as an OWL2 ontology
     * @param owlFormat prefix format which contains the prefixes for the namespaces in the target document
     * @return Document a Discovery json Document
     */
    public Document generateInferredView(OWLOntology owlOntology,
                                         OWLDocumentFormat owlFormat,
                                         OWLReasonerConfiguration config){
        reasoner = new FaCTPlusPlusReasonerFactory().createReasoner(owlOntology,config);
        reasoner.precomputeInferences();

        //Sets the standard ontology headers namespaces imports etc
        processDocumentHeaders(owlOntology,"Inferred",owlFormat);

        //Creates the class property declarations
        for (OWLDeclarationAxiom da : owlOntology.getAxioms(AxiomType.DECLARATION))
            processDeclarationAxiom(da, ontology);

        //Adds the annotation axioms to the classes
        for (OWLAnnotationAssertionAxiom a : owlOntology.getAxioms(AxiomType.ANNOTATION_ASSERTION))
            processAnnotationAssertionAxiom(a);

        //Steps down the trees
        OWLClass topClass= reasoner.getTopClassNode().getRepresentativeElement();
        for (Node<OWLClass> owlSubClass:
                reasoner.getSubClasses(topClass,true))
                     processClassSubClasses(owlSubClass);

        OWLObjectProperty topOp= reasoner.getTopObjectPropertyNode().getRepresentativeElement().asOWLObjectProperty();
        for (Node<OWLObjectPropertyExpression> owlSubOp: reasoner.getSubObjectProperties(topOp,true))
            processObjectPropertySubProperties(owlSubOp);

        OWLDataProperty topDp = reasoner.getTopDataPropertyNode().getRepresentativeElement().asOWLDataProperty();
        for (Node<OWLDataProperty> owlSubDp: reasoner.getSubDataProperties(topDp,true))
            processDataPropertySubProperties(owlSubDp);

        reasoner.dispose();
        return new Document().setInformationModel(ontology);
    }

    private void processObjectPropertySubProperties(Node<OWLObjectPropertyExpression> parentOp) {
        OWLObjectPropertyExpression owlParent= parentOp.getRepresentativeElement();
        if (!owlParent.isAnonymous()) {
            ObjectProperty superOb = (ObjectProperty) concepts.get(getIri(owlParent.asOWLObjectProperty().getIRI()));
            Concept newSuper = new Concept();
            newSuper.setId((superOb.getId()));
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
            newSuper.setId(superDp.getId());
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
        OWLClass owlParent= parentClass.getRepresentativeElement();
        Clazz superC = (Clazz) concepts.get(getIri(owlParent.asOWLClass().getIRI()));
        Concept newSuper= new Concept();
        newSuper.setId(superC.getId());
        newSuper.setIri(superC.getIri());
        newSuper.setName(superC.getName());
        for (Node<OWLClass> owlSubclass: reasoner.getSubClasses(owlParent,true)) {
            String iri = getIri(owlSubclass.getRepresentativeElement().getIRI());
            if (!iri.equals("owl:Nothing")) {
                Clazz c = (Clazz) concepts.get(iri);
                c.addIsa(newSuper);
                processClassSubClasses(owlSubclass);
            }
        }


    }


    /**
     * Transforms an ontology to Discovery syntax
     * @param owlOntology  the owl ontology
     * @param filterNamespaces A list of namespace prefixes, whose declarations that you do not need in the final output but were auto generated by Protege
     * @return Document - A Discovery information model ontology document serializable as json
     */
    public Document transform(OWLOntology owlOntology,OWLDocumentFormat owlFormat, List<String> filterNamespaces) {

        filteredNs= filterNamespaces;

        processDocumentHeaders(owlOntology,"Asserted",owlFormat);

        for (OWLDeclarationAxiom da : owlOntology.getAxioms(AxiomType.DECLARATION))
            processDeclarationAxiom(da, ontology);

        for (OWLAxiom a : owlOntology.getAxioms()) {
            if (a.getAxiomType() != AxiomType.DECLARATION)
                processAxiom(a, ontology);
        }
        cleanAndAddUUIDs();

        return new Document().setInformationModel(ontology);
    }

    private void processDocumentHeaders(OWLOntology owlOntology, String axiomMode
                                    , OWLDocumentFormat owlFormat) {

        initializePrefixManager(owlOntology,owlFormat);

        setOntology(new Ontology());

        processOntology(owlOntology, ontology,axiomMode);

        processPrefixes(owlOntology, ontology);

        processImports(owlOntology, ontology);
    }


    private boolean isFiltered(Concept concept){
        String[] prefix=concept.getIri().split(":");
        if (filteredNs.contains(prefix[0]))
            return true;
        return false;
    }
    private void removeConcepts(List<Object> removals, List<Concept> conceptList){
        if ((removals!=null&(conceptList!=null))) {
            removals.stream().forEach(conceptList::remove);
            removals.clear();
        }
    }
    private void cleanAndAddUUIDs() {
        List<Object> toRemove = new ArrayList<>();
        if (ontology.getClazz() != null)
            for (Clazz c : ontology.getClazz()) {
                boolean defined = false;
                testForUUID(c);
                if (c.getEquivalentTo() != null) {
                    defined = true;
                    for (ClassAxiom equ : c.getEquivalentTo())
                        testForUUID(equ);
                }
                if (c.getSubClassOf() != null) {
                    defined = true;
                    for (ClassAxiom sub : c.getSubClassOf())
                        testForUUID(sub);
                }
                if (isFiltered(c) & (!defined))
                    toRemove.add(c);
            }
            removeConcepts(toRemove,(List) ontology.getClazz());

        if (ontology.getObjectProperty() != null)
            for (ObjectProperty p : ontology.getObjectProperty()) {
                boolean defined= false;
                testForUUID(p);
                if (p.getSubObjectPropertyOf() != null) {
                    defined = true;
                    for (PropertyAxiom pax : p.getSubObjectPropertyOf())
                        testForUUID(pax);
                }
                if (p.getInversePropertyOf() != null) {
                    defined = true;
                    testForUUID(p.getInversePropertyOf());
                }
                if (p.getPropertyDomain() != null) {
                    defined = true;
                    for (ClassAxiom cax : p.getPropertyDomain())
                        testForUUID(cax);
                }
                if (p.getObjectPropertyRange() != null) {
                    defined = true;
                    for (ClassAxiom cax : p.getObjectPropertyRange())
                        testForUUID(cax);
                }
                if (p.getSubPropertyChain() != null) {
                    defined = true;
                    for (SubPropertyChain chain : p.getSubPropertyChain())
                        testForUUID(chain);
                }
                if (p.getIsFunctional() != null) {
                    defined = true;
                    testForUUID(p.getIsFunctional());
                }
                if (p.getIsReflexive() != null) {
                    defined = true;
                    testForUUID(p.getIsReflexive());
                }
                if (p.getIsSymmetric() != null) {
                    defined = true;
                    testForUUID(p.getIsSymmetric());
                }
                if (p.getIsTransitive() != null) {
                    defined = true;
                    testForUUID(p.getIsTransitive());
                }
                if ((!defined)&(isFiltered(p)))
                    toRemove.add(p);
            }
        removeConcepts(toRemove,(List) ontology.getObjectProperty());

        if (ontology.getDataProperty() != null)
            for (DataProperty d : ontology.getDataProperty()) {
                boolean defined = false;
                testForUUID(d);
                if (d.getSubDataPropertyOf() != null) {
                    defined = true;
                    for (PropertyAxiom pax : d.getSubDataPropertyOf())
                        testForUUID(pax);
                }
                if (d.getDataPropertyRange() != null) {
                    defined = true;
                    for (PropertyRangeAxiom rax : d.getDataPropertyRange())
                        testForUUID(rax);
                }
                if (d.getPropertyDomain() != null) {
                    defined = true;
                    for (ClassAxiom cax : d.getPropertyDomain())
                        testForUUID(cax);
                }
                if (d.getIsFunctional() != null) {
                    defined = true;
                    testForUUID(d.getIsFunctional());
                }
                if ((!defined)&(isFiltered(d)))
                    toRemove.add(d);
            }
        removeConcepts(toRemove,(List) ontology.getDataProperty());

        if (ontology.getAnnotationProperty() != null)
            for (AnnotationProperty an : ontology.getAnnotationProperty()) {
                boolean defined= false;
                testForUUID(an);
                if (an.getSubAnnotationPropertyOf() != null) {
                    defined = true;
                    for (PropertyAxiom pax : an.getSubAnnotationPropertyOf())
                        testForUUID(pax);
                }
                if (an.getPropertyRange() != null) {
                    defined = true;
                    for (AnnotationPropertyRangeAxiom rax : an.getPropertyRange())
                        testForUUID(rax);
                }
                if ((!defined)&(isFiltered(an)))
                    toRemove.add(an);
            }
        removeConcepts(toRemove,(List) ontology.getAnnotationProperty());

        if (ontology.getDataType() != null)
            for (DataType dt : ontology.getDataType())
                testForUUID(dt);
    }


    private void testForUUID(IMEntity entity) {
                if (entity.getId() == null)
                    entity.setId(UUID.randomUUID().toString());
    }

    private void processImports(OWLOntology owlOntology, Ontology ontology){
        if (owlOntology.getImports()!=null)
        {
            if (owlOntology.getImportsDeclarations() !=null)
            {
                owlOntology.getImportsDeclarations()
                        .forEach(y -> ontology.addImport(y.getIRI().toString()));
            }
        }
    }


    private void initializePrefixManager(OWLOntology ontology,OWLDocumentFormat owlFormat) {
        defaultPrefixManager = new DefaultPrefixManager();
        defaultPrefixManager.copyPrefixesFrom(owlFormat.asPrefixOWLOntologyFormat());
        OWLDocumentFormat ontologyFormat = new FunctionalSyntaxDocumentFormat();
        if (ontologyFormat instanceof PrefixDocumentFormat) {
            defaultPrefixManager.copyPrefixesFrom((PrefixDocumentFormat) ontologyFormat);
            defaultPrefixManager.setPrefixComparator(((PrefixDocumentFormat) ontologyFormat).getPrefixComparator());
        defaultPrefixManager.setDefaultPrefix("http://www.DiscoveryDataService.org/InformationModel/Ontology#");
        defaultPrefixManager.setPrefix("sn:","http://snomed.info/sct#");
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

    private void processOntology(OWLOntology ontology,
                                 Ontology document, String axiomMode) {
        document.setEntailmentType(axiomMode);
        document.setDocumentInfo(
            new DocumentInfo()
            .setDocumentIri(ontology.getOntologyID().getOntologyIRI().get().toString())
        );
        document.setIri(ontology.getOntologyID().getOntologyIRI().get().toString());
    }

    private void processDeclarationAxiom(OWLDeclarationAxiom a, Ontology discovery) {
        OWLEntity e = a.getEntity();
        String iri = getIri(e.getIRI());

        if (e.getEntityType() == EntityType.CLASS) {
            Clazz clazz = new Clazz();
            clazz.setIri(iri);
            discovery.addClazz(clazz);
            concepts.put(iri, clazz);
        } else if (e.getEntityType() == EntityType.OBJECT_PROPERTY) {
            ObjectProperty op = new ObjectProperty();
            op.setIri(iri);
            discovery.addObjectProperty(op);
            concepts.put(iri, op);
        } else if (e.getEntityType() == EntityType.DATA_PROPERTY) {
            DataProperty dp = new DataProperty();
            dp.setIri(iri);
            discovery.addDataProperty(dp);
            concepts.put(iri, dp);
        } else if (e.getEntityType() == EntityType.DATATYPE) {
            DataType dt = new DataType();
            dt.setIri(iri);
            discovery.addDataType(dt);
            concepts.put(iri, dt);
        } else if (e.getEntityType() == EntityType.ANNOTATION_PROPERTY) {
                AnnotationProperty ap = new AnnotationProperty();
                ap.setIri(iri);
                discovery.addAnnotationProperty(ap);
                if (!concepts.containsKey(iri))                               // <-- HERE
                    concepts.put(iri, ap);
        } else if (e.getEntityType() == EntityType.NAMED_INDIVIDUAL) {
            Individual individual = new Individual();
            individual.setIri(iri);
            discovery.addIndividual(individual);
            concepts.put(iri,individual);
        } else
            System.err.println("OWL Declaration: " + a);
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
            System.err.println("Axiom: " + a);
    }

    private void processObjectPropertyDomainAxiom(OWLObjectPropertyDomainAxiom a) {
        String propertyIri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty) concepts.get(propertyIri);
        ClassAxiom pd = new ClassAxiom();
        processAxiomAnnotations(a, pd);
        op.addPropertyDomain(pd);

        if (a.getDomain().getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            String domainIri = getIri(a.getDomain().asOWLClass().getIRI());
            pd.setClazz(domainIri);
        } else if (a.getDomain().getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            pd.setUnion(getOWLUnion((OWLObjectUnionOf)a.getDomain()));
        } else {
            System.err.println("Invalid object property domain : " + propertyIri);
        }
    }


    private void processSubClassAxiom(OWLSubClassOfAxiom a) {
        String iri = getIri(((OWLClass) a.getSubClass()).getIRI());

        Clazz c = (Clazz)concepts.get(iri);
        if (c == null)
            System.out.println("Ignoring abstract subClass: [" + iri + "]");
        else {
            ClassAxiom subClassOf = new ClassAxiom();
            processAxiomAnnotations(a,subClassOf);
            addOwlClassExpressionToClassExpression(a.getSuperClass(), subClassOf);

            c.addSubClassOf(subClassOf);
        }

    }

    private void addOwlClassExpressionToClassExpression(OWLClassExpression oce, ClassExpression cex) {
        if (oce.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            cex.setClazz(
                getIri(oce.asOWLClass().getIRI())
            );
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
            cex.setIntersection(getOWLIntersection((OWLObjectIntersectionOf) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
              cex.setUnion( getOWLUnion((OWLObjectUnionOf) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
            cex.setPropertyObject(getObjectSomeValuesFrom((OWLObjectSomeValuesFrom) oce));
            } else if (oce.getClassExpressionType() == ClassExpressionType.DATA_HAS_VALUE) {
                cex.setPropertyData(getDataHasValue((OWLDataHasValue) oce));
            } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_EXACT_CARDINALITY) {
                cex.setPropertyObject(getObjectExactCardinality((OWLObjectExactCardinality) oce));
            } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_MAX_CARDINALITY) {
                cex.setPropertyObject(getObjectMaxCardinality((OWLObjectMaxCardinality) oce));
            } else if (oce.getClassExpressionType() == ClassExpressionType.DATA_EXACT_CARDINALITY) {
                cex.setPropertyData(getDataExactCardinality((OWLDataExactCardinality) oce));
            } else if (oce.getClassExpressionType() == ClassExpressionType.DATA_MAX_CARDINALITY) {
            cex.setPropertyData(getDataMaxCardinality((OWLDataMaxCardinality) oce));
        }
                else if (oce.getClassExpressionType() == ClassExpressionType.DATA_MIN_CARDINALITY) {
                cex.setPropertyData(getDataMinCardinality((OWLDataMinCardinality) oce));
            } else if (oce.getClassExpressionType() == ClassExpressionType.DATA_SOME_VALUES_FROM) {
                cex.setPropertyData(getDataSomeValues((OWLDataSomeValuesFrom) oce));
            } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
                cex.setPropertyObject(getObjectSomeValuesFrom((OWLObjectSomeValuesFrom) oce));
            } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_MIN_CARDINALITY) {
                cex.setPropertyObject(getObjectMinCardinality((OWLObjectMinCardinality) oce));

            } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_HAS_VALUE) {
              cex.setPropertyObject(getObjectHasValue((OWLObjectHasValue) oce));
            } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_COMPLEMENT_OF) {
            cex.setComplementOf(getOWLClassExpression(oce));
            }
            else  if (oce.getClassExpressionType()==ClassExpressionType.OBJECT_ONE_OF){
                cex.setObjectOneOf(getOWLObjectOneOf((OWLObjectOneOf) oce));

            } else {
            System.err.println("OWL Class expression: " + oce);
            throw new IllegalStateException("Unhandled class expression type: " + oce.getClassExpressionType().getName());
        }
    }

    private List<String> getOWLObjectOneOf(OWLObjectOneOf oce) {
        List<String> oneOfList= new ArrayList<>();
        for (OWLIndividual individual:oce.getIndividuals())
        {
            String oneOf= getIri(individual.asOWLNamedIndividual().getIRI());
            oneOfList.add(oneOf);
        }
        return oneOfList;
    }


    private List<ClassExpression> getOWLIntersection(OWLObjectIntersectionOf oi) {
        List<ClassExpression> result = new ArrayList<>();

        for (OWLClassExpression c : oi.getOperandsAsList()) {
            result.add(getOWLClassExpression(c));
            if (result == null)
                System.err.println("OWLIntersection:" + c);
        }
        return result;
    }

    private ClassExpression getOWLClassExpression(OWLClassExpression c){
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
                System.out.println("Ignoring OWLIntersection:ObjectHasValue: " + getIri(((OWLObjectHasValue)c).getFiller().asOWLNamedIndividual().getIRI()));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_COMPLEMENT_OF) {
                System.out.println("Ignoring OWLIntersection:ObjectComplementOf: " + c);
                return null;
            } else {
                System.err.println("OWLIntersection:" + c);
            }
            return null;
    }


    private List<ClassExpression> getOWLUnion(OWLObjectUnionOf ou) {
        List<ClassExpression> result = new ArrayList<>();

        for(OWLClassExpression c: ou.getOperandsAsList()) {
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
                System.err.println("OWLUnion:" + c);
        }

        return result;
    }

    private ClassExpression getOWLClassAsClassExpression(OWLClass owlClass) {
        return new ClassExpression().setClazz(getIri(owlClass.getIRI()));
    }

    private ClassExpression getOWLDataHasValueAsClassExpression(OWLDataHasValue dataHasValue) {
        ClassExpression result = new ClassExpression();
        result.setPropertyData(getDataHasValue(dataHasValue));
        return result;
    }

    private OPECardinalityRestriction getObjectHasValue(OWLObjectHasValue objectHasValue) {

        OPECardinalityRestriction ope= new OPECardinalityRestriction();
        ope.setProperty(getIri(objectHasValue.getProperty().asOWLObjectProperty().getIRI()));
        ope.setIndividual(getIri(objectHasValue.getValue().asOWLNamedIndividual().getIRI()));
        return ope;
    }

    private DPECardinalityRestriction getDataHasValue(OWLDataHasValue dataHasValue) {
        OWLLiteral lit = dataHasValue.getValue();
        DPECardinalityRestriction dpe= new DPECardinalityRestriction();
        dpe.setProperty(getIri(dataHasValue.getProperty().asOWLDataProperty().getIRI()));
        dpe.setExactValue(lit.getLiteral());
        dpe.setDataType(getIri(lit.getDatatype().getIRI()));
        return dpe;
    }

    private ClassExpression getOWLObjectExactCardinalityAsClassExpression(OWLObjectExactCardinality exactCardinality) {
        ClassExpression result = new ClassExpression();
        result.setPropertyObject(getObjectExactCardinality(exactCardinality));
        return result;
    }

    private OPECardinalityRestriction getObjectExactCardinality(OWLObjectExactCardinality exactCardinality){

        OPECardinalityRestriction cardinalityRestriction = new OPECardinalityRestriction();

        cardinalityRestriction
            .setProperty(getIri(exactCardinality.getProperty().asOWLObjectProperty().getIRI()))
            .setExact(exactCardinality.getCardinality());

        addOwlClassExpressionToClassExpression(exactCardinality.getFiller(), cardinalityRestriction);

        return cardinalityRestriction;
    }

    private ClassExpression getOWLObjectMaxCardinalityAsClassExpression(OWLObjectMaxCardinality maxCardinality) {
        ClassExpression result = new ClassExpression();
        result.setPropertyObject(getObjectMaxCardinality(maxCardinality));
        return result;
    }

    private OPECardinalityRestriction getObjectMaxCardinality(OWLObjectMaxCardinality maxCardinality){

        OPECardinalityRestriction cardinalityRestriction = new OPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(maxCardinality.getProperty().asOWLObjectProperty().getIRI()))
            .setMax(maxCardinality.getCardinality());

        addOwlClassExpressionToClassExpression(maxCardinality.getFiller(), cardinalityRestriction);

        return cardinalityRestriction;
    }

    private ClassExpression getOWLDataExactCardinalityAsClassExpression(OWLDataExactCardinality exactCardinality) {
        ClassExpression result = new ClassExpression();
        result.setPropertyData(getDataExactCardinality(exactCardinality));
        return result;
    }

    private DPECardinalityRestriction getDataExactCardinality(OWLDataExactCardinality exactCardinality){

        DPECardinalityRestriction cardinalityRestriction = new DPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(exactCardinality.getProperty().asOWLDataProperty().getIRI()))
            .setExact(exactCardinality.getCardinality())
            .setDataType(getIri(exactCardinality.getFiller().asOWLDatatype().getIRI()));

        return cardinalityRestriction;

    }

    private ClassExpression getOWLDataMaxCardinalityAsClassExpression(OWLDataMaxCardinality maxCardinality) {
        ClassExpression result = new ClassExpression();

        result.setPropertyData(getDataMaxCardinality(maxCardinality));

        return result;
    }


    private DPECardinalityRestriction getDataMaxCardinality(OWLDataMaxCardinality maxCardinality) {
        DPECardinalityRestriction cardinalityRestriction = new DPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(maxCardinality.getProperty().asOWLDataProperty().getIRI()))
            .setMax(maxCardinality.getCardinality())
            .setDataType(getIri(maxCardinality.getFiller().asOWLDatatype().getIRI()));
        return cardinalityRestriction;
    }


    private DPECardinalityRestriction getDataMinCardinality(OWLDataMinCardinality minCardinality) {
        DPECardinalityRestriction cardinalityRestriction = new DPECardinalityRestriction();
        cardinalityRestriction
                .setProperty(getIri(minCardinality.getProperty().asOWLDataProperty().getIRI()))
                .setMax(minCardinality.getCardinality())
                .setDataType(getIri(minCardinality.getFiller().asOWLDatatype().getIRI()));
        return cardinalityRestriction;
    }

    private ClassExpression getOWLDataSomeValuesAsClassExpression(OWLDataSomeValuesFrom some) {
        ClassExpression result = new ClassExpression();

        DPECardinalityRestriction cardinalityRestriction = getDataSomeValues(some);

        result.setPropertyData(cardinalityRestriction);

        return result;
    }


    private DPECardinalityRestriction getDataSomeValues(OWLDataSomeValuesFrom some) {
        DPECardinalityRestriction cardinalityRestriction = new DPECardinalityRestriction();
        cardinalityRestriction
                .setProperty(getIri(some.getProperty().asOWLDataProperty().getIRI()))
                .setQuantification("some");
        if (some.getFiller().getDataRangeType()== DataRangeType.DATATYPE)
                cardinalityRestriction.setDataType(getIri(some.getFiller().asOWLDatatype().getIRI()));
        else if (some.getFiller().getDataRangeType()==DataRangeType.DATA_ONE_OF)
            getOWLOneOfAsDataRange((OWLDataOneOf) some.getFiller(),cardinalityRestriction);
        return cardinalityRestriction;
    }

    private DataRange getOWLOneOfAsDataRange
            (OWLDataOneOf owlOneOf,
            DataRange dr) {
        for (OWLLiteral one : owlOneOf.getValues()) {
            dr.setDataType(getIri(one.getDatatype().getIRI()));
            dr.addOneOf(one.getLiteral());
        }
        return dr;
    }


    private ClassExpression getOWLObjectSomeValuesAsClassExpression(OWLObjectSomeValuesFrom someValuesFrom) {
        ClassExpression result = new ClassExpression();

        OPECardinalityRestriction oper = getObjectSomeValuesFrom(someValuesFrom);

        result.setPropertyObject(oper);

        return result;
    }

    private OPECardinalityRestriction getObjectSomeValuesFrom(OWLObjectSomeValuesFrom someValuesFrom) {
        OPECardinalityRestriction oper = new OPECardinalityRestriction();

        oper.setProperty(getIri(someValuesFrom.getProperty().asOWLObjectProperty().getIRI()));
        oper.setQuantification("some");
        addOwlClassExpressionToClassExpression(someValuesFrom.getFiller(), oper);
        return oper;
    }

    private ClassExpression getOWLObjectMinCardinalityAsClassExpression(OWLObjectMinCardinality minCardinality) {
        ClassExpression result = new ClassExpression();

        OPECardinalityRestriction cardinalityRestriction = getObjectMinCardinality(minCardinality);

        result.setPropertyObject(cardinalityRestriction);

        return result;
    }


    private OPECardinalityRestriction getObjectMinCardinality(OWLObjectMinCardinality minCardinality) {
        OPECardinalityRestriction cardinalityRestriction = new OPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(minCardinality.getProperty().asOWLObjectProperty().getIRI()))
            .setMin(minCardinality.getCardinality())
            .setClazz(getIri(minCardinality.getFiller().asOWLClass().getIRI()));
        return cardinalityRestriction;
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

        ObjectProperty op = (ObjectProperty)concepts.get(firstIri);
        PropertyAxiom inverse = new PropertyAxiom();
        processAxiomAnnotations(a,inverse);
        inverse.setProperty(secondIri);
        op.setInversePropertyOf(inverse);
    }

    private void processObjectPropertyRangeAxiom(OWLObjectPropertyRangeAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty)concepts.get(iri);
        ClassAxiom cex = new ClassAxiom();
        processAxiomAnnotations(a, cex);
        op.addObjectPropertyRange(cex);

        addOwlClassExpressionToClassExpression(a.getRange(), cex);

    }

    private void processDifferentIndividualsAxiom(OWLDifferentIndividualsAxiom a) {
        System.out.println("Ignoring different individuals: [" + a.toString() + "]");
    }

    private void processFunctionalObjectPropertyAxiom(OWLFunctionalObjectPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty)concepts.get(iri);
        Axiom isFunc = new Axiom();
        processAxiomAnnotations(a,isFunc);
        op.setIsFunctional(isFunc);
    }

    private void processFunctionalDataPropertyAxiom(OWLFunctionalDataPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLDataProperty().getIRI());

        DataProperty dp = (DataProperty)concepts.get(iri);
        Axiom isFunc = new Axiom();
        processAxiomAnnotations(a,isFunc);
        dp.setIsFunctional(isFunc);


    }

    private void processAnnotationAssertionAxiom(OWLAnnotationAssertionAxiom a) {
        String iri = getIri((IRI)a.getSubject());

        if (ignoreIris.contains(iri))
            return;

        String property = getIri(a.getProperty().asOWLAnnotationProperty().getIRI());

        String value;
        if (a.getValue().isLiteral()) {
            value = a.getValue().asLiteral().get().getLiteral();
        } else if (a.getValue().isIRI()) {
            value = getIri(a.getValue().asIRI().get());
        } else {
            System.err.println("Annotation has no literal!");
            return;
        }

        Concept c = concepts.get(iri);

        if (c==null) {
            System.err.println("Annotation assertion for undeclared concept: [" + iri + "]");
            return;
        }
        if (property.equals("prov:definition"))
            c.setDescription(value);
        else if (property.equals("rdfs:comment"))
            if (c.getDescription()==null)
               c.setDescription(value);
            else {
                Annotation annotation= new Annotation();
                annotation.setProperty(property);
                annotation.setValue(value);
                c.addAnnotation(annotation);
            }
        else if (property.equals("rdfs:label"))
            c.setName(value);
        else if (property.equals(Common.HAS_STATUS))
            c.setStatus(ConceptStatus.byName(value));
        else if (property.equals(Common.HAS_CODE))
            c.setCode(value);
        else if (property.equals(Common.HAS_SCHEME))
            c.setScheme(value);
        else if (property.equals(Common.HAS_ID))
            c.setId(value);
        else if (property.equals(Common.HAS_VERSION))
            c.setVersion(Integer.parseInt(value));

        else {
            Annotation annotation= new Annotation();
            annotation.setProperty(property);
            annotation.setValue(value);
            c.addAnnotation(annotation);
        }
    }

    private void processAnnotationPropertyRangeAxiom(OWLAnnotationPropertyRangeAxiom a) {
        String iri = getIri(a.getProperty().asOWLAnnotationProperty().getIRI());

        AnnotationProperty dp = (AnnotationProperty) concepts.get(iri);
        AnnotationPropertyRangeAxiom range = new AnnotationPropertyRangeAxiom();
        processAxiomAnnotations(a,range);
        dp.addPropertyRange(range);
        range.setIri(getIri(a.getRange()));

    }

    private void processEquivalentClassesAxiom(OWLEquivalentClassesAxiom a) {
        Iterator<OWLClassExpression> i = a.getClassExpressions().iterator();
        String iri = getIri(i.next().asOWLClass().getIRI());

        Clazz c = (Clazz)concepts.get(iri);

        if (c == null)
            System.out.println("Ignoring abstract class: [" + iri + "]");
        else {
            while (i.hasNext()) {
                ClassAxiom cex = new ClassAxiom();
                processAxiomAnnotations(a,cex);
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
                            String value=y.getValue().asLiteral().get().getLiteral();
                            switch (property) {
                                case Common.HAS_STATUS:
                                    im.setStatus(ConceptStatus.byName(value));
                                    break;
                                case Common.HAS_ID:
                                    im.setId(value);
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
                                    String value=y.getValue().asLiteral().get().getLiteral();
                                    switch (property) {
                                        case Common.HAS_STATUS:
                                            im.setStatus(ConceptStatus.byName(value));
                                            break;
                                        case Common.HAS_ID:
                                            im.setId(value);
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
            System.err.println("annotation property as object property ? : "+ iri);
        }

    }

    private void processClassAssertionAxiom(OWLClassAssertionAxiom a) {
        String indiIri = getIri(a.getIndividual().asOWLNamedIndividual().getIRI());
        String classIri = getIri(a.getClassExpression().asOWLClass().getIRI());
        Individual ind = (Individual) concepts.get(indiIri);
        ind.setIsType(classIri);
    }

    private void processSubDataPropertyAxiom(OWLSubDataPropertyOfAxiom a) {
        String iri = getIri(a.getSubProperty().asOWLDataProperty().getIRI());
        //System.err.println(iri);
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
        processAxiomAnnotations(a,sp);
        ap.addSubAnnotationPropertyOf(sp);
        sp.setProperty(
            getIri(a.getSuperProperty().asOWLAnnotationProperty().getIRI())
        );
    }

    private void processDataPropertyRangeAxiom(OWLDataPropertyRangeAxiom a) {
        String iri = getIri(a.getProperty().asOWLDataProperty().getIRI());
        DataProperty dp = (DataProperty) concepts.get(iri);
        PropertyRangeAxiom prax = new PropertyRangeAxiom();
        processAxiomAnnotations(a, prax);
        dp.addDataPropertyRange(prax);
        if (a.getRange().getDataRangeType()==DataRangeType.DATATYPE)
          prax.setDataType(getIri(a.getRange().asOWLDatatype().getIRI()));
    }

    private void processTransitiveObjectPropertyAxiom(OWLTransitiveObjectPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());
        if (ignoreIris.contains(iri))
            return;

        ObjectProperty op = (ObjectProperty) concepts.get(iri);
        Axiom isTrans = new Axiom();
        processAxiomAnnotations(a,isTrans);
        op.setIsTransitive(isTrans);
    }

    private void processDatatypeDefinitionAxiom(OWLDatatypeDefinitionAxiom a) {
        String iri = getIri(a.getDatatype().asOWLDatatype().getIRI());
        DataType dt = (DataType) concepts.get(iri);
        OWLDataRange r = a.getDataRange();

        DataTypeDefinition dtd = new DataTypeDefinition();
        processAxiomAnnotations(a, dtd);

        if (r.getDataRangeType() == DataRangeType.DATATYPE_RESTRICTION) {
            OWLDatatypeRestriction dtr = ((OWLDatatypeRestriction)r);
            dtd.setDataTypeRestriction(
                    getDatatypeRestriction(dtr)
            );
        } else {
            System.err.println("Unknown data range type");
        }

        dt.addDataTypeDefinition(dtd);
    }

    private DataTypeRestriction getDatatypeRestriction(OWLDatatypeRestriction restriction) {
        return new DataTypeRestriction()
            .setDataType(getIri(restriction.getDatatype().getIRI()))
            .setFacetRestriction(
                restriction.getFacetRestrictions()
                    .stream()
                    .map(f -> new FacetRestriction()
                        .setFacet(getIri(f.getFacet().getIRI()))
                        .setConstrainingFacet(f.getFacetValue().getLiteral())
                    )
                    .collect(Collectors.toList())
            );
    }

    private void processDataPropertyAssertionAxiom(OWLDataPropertyAssertionAxiom a) {
        String indiIri = getIri(a.getSubject().asOWLNamedIndividual().getIRI());
        String propertyIri = getIri(a.getProperty().asOWLDataProperty().getIRI());
        Individual ind = (Individual) concepts.get(indiIri);
        DataPropertyAssertionAxiom pval = new DataPropertyAssertionAxiom();
        processAxiomAnnotations(a, pval);
        pval.setProperty(getIri(a.getProperty().asOWLDataProperty().getIRI()));
        pval.setDataType(getIri(a.getObject().getDatatype().getIRI()));
        pval.setValue((a.getObject().getLiteral()));
        ind.addPropertyDataValue(pval);

    }


    private void processObjectPropertyAssertionAxiom(OWLObjectPropertyAssertionAxiom a) {
        System.out.println("Ignoring object property assertion: [" + getIri(a.getSubject().asOWLNamedIndividual().getIRI()) + "]");
    }

    private void processDataPropertyDomainAxiom(OWLDataPropertyDomainAxiom a) {
        String propertyIri = getIri(a.getProperty().asOWLDataProperty().getIRI());
        String domainIri = getIri(a.getDomain().asOWLClass().getIRI());

        DataProperty dp = (DataProperty)concepts.get(propertyIri);
        ClassAxiom pd = new ClassAxiom();
        processAxiomAnnotations(a,pd);
        dp.addPropertyDomain(pd);
        pd.setClazz(domainIri);
    }
    private void processDisjointClassesAxion(OWLDisjointClassesAxiom a) {
        List<String> disjoints = new ArrayList<>();
        a.getClassExpressions().forEach(x -> disjoints.add(getIri(x.asOWLClass().getIRI())));
        for (String iri:disjoints){
            Clazz c= (Clazz)concepts.get(iri);
            for (String with:disjoints){
                if (!with.equals(iri)){
                    c.addDisjointWithClass(with);
                }
            }
        }

    }

    private void processInverseFunctionalObjectPropertyAxiom(OWLInverseFunctionalObjectPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        System.out.println("Ignoring inverse functional object property axiom: [" + iri + "]");
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
                    .map(ope -> getIri(ope.asOWLObjectProperty().getIRI()))
                    .collect(Collectors.toList())

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
