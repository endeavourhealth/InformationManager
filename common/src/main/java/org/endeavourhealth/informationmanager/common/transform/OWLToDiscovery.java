package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class OWLToDiscovery {

    /**
     * @author      Richard Collier David Stables
     * @version     1.0
     * @since       1.0
     */
    private List<String> ignoreIris = Collections.singletonList("owl:topObjectProperty");
    private DefaultPrefixManager defaultPrefixManager;
    private Map<String, Concept> concepts = new HashMap<>();
    private Ontology ontology;


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
                if (iri.length() > front.length()) {
                    String test = iri.substring(0, front.length());
                    if (test.equals(front)) {
                        return (ns.getPrefix() + iri.substring(front.length()));
                    }

                }
            }
        }
        return iri;
    }
    private List<String> filteredNs;

    /**
     * transforms a functional syntax owl ontology to Discovery JSON
     * @param owlOntology  the ontology to convert
     * @param filterNamespaces  the namespaces of declared entities that if they have no axioms are excluded
     * @return  returns an information model ontology json document
     */
    public Document transform(OWLOntology owlOntology,List<String> filterNamespaces) {
        filteredNs= filterNamespaces;

        initializePrefixManager(owlOntology);

        setOntology(new Ontology());

        processOntology(owlOntology, ontology);

        processPrefixes(owlOntology, ontology);

        processImports(owlOntology, ontology);

        for (OWLDeclarationAxiom da : owlOntology.getAxioms(AxiomType.DECLARATION))
            processDeclarationAxiom(da, ontology);

        for (OWLAxiom a : owlOntology.getAxioms()) {
            if (a.getAxiomType() != AxiomType.DECLARATION)
                processAxiom(a, ontology);
        }
        cleanAndAddUUIDs();

        return new Document().setInformationModel(ontology);
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
                if (p.getPropertyRange() != null) {
                    defined = true;
                    for (ClassAxiom cax : p.getPropertyRange())
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
                if (d.getPropertyRange() != null) {
                    defined = true;
                    for (PropertyRangeAxiom rax : d.getPropertyRange())
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
        if (owlOntology.imports()!=null)
        {
            if (owlOntology.importsDeclarations() !=null)
            {
                owlOntology.importsDeclarations()
                        .forEach(y -> ontology.addImport(y.getIRI().toString()));
            }
        }
    }


    private void initializePrefixManager(OWLOntology ontology) {
        defaultPrefixManager = new DefaultPrefixManager();
        OWLDocumentFormat ontologyFormat = ontology.getNonnullFormat();
        if (ontologyFormat instanceof PrefixDocumentFormat) {
            defaultPrefixManager.copyPrefixesFrom((PrefixDocumentFormat) ontologyFormat);
            defaultPrefixManager.setPrefixComparator(((PrefixDocumentFormat) ontologyFormat).getPrefixComparator());
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

    private void processOntology(OWLOntology ontology, Ontology document) {
        document.setEntailmentType("Asserted");
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
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
            cex.setPropertyObject(getOpeRestriction((OWLObjectSomeValuesFrom) oce));

        } else {
            System.err.println("OWL Class expression: " + oce);
            throw new IllegalStateException("Unhandled class expression type: " + oce.getClassExpressionType().getName());
        }
    }

    private List<ClassExpression> getOWLIntersection(OWLObjectIntersectionOf oi) {
        List<ClassExpression> result = new ArrayList<>();

        for(OWLClassExpression c: oi.getOperandsAsList()) {
            if (c.isOWLClass()) {
                result.add(getOWLClassAsClassExpression(c.asOWLClass()));
            } else if (c.getClassExpressionType() == ClassExpressionType.DATA_HAS_VALUE) {
                result.add(getOWLDataHasValueAsClassExpression((OWLDataHasValue) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_EXACT_CARDINALITY) {
                result.add(getOWLObjectExactCardinalityAsClassExpression((OWLObjectExactCardinality) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_MAX_CARDINALITY) {
                result.add(getOWLObjectMaxCardinalityAsClassExpression((OWLObjectMaxCardinality) c));
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
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_HAS_VALUE) {
                System.out.println("Ignoring OWLIntersection:ObjectHasValue: " + getIri(((OWLObjectHasValue)c).getFiller().asOWLNamedIndividual().getIRI()));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_COMPLEMENT_OF) {
                System.out.println("Ignoring OWLIntersection:ObjectComplementOf: " + c);
            } else
                System.err.println("OWLIntersection:" + c);
        }

        return result;
    }

    private List<ClassExpression> getOWLUnion(OWLObjectUnionOf ou) {
        List<ClassExpression> result = new ArrayList<>();

        for(OWLClassExpression c: ou.getOperandsAsList()) {
            if (c.isOWLClass()) {
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
        OWLLiteral lit = dataHasValue.getValue();

        result.setDataHasValue(
            new DataValueRestriction()
            .setProperty(getIri(dataHasValue.getProperty().asOWLDataProperty().getIRI()))
            .setValue(lit.getLiteral())
            .setDataType(getIri(lit.getDatatype().getIRI()))
        );
        return result;
    }

    private ClassExpression getOWLObjectExactCardinalityAsClassExpression(OWLObjectExactCardinality exactCardinality) {
        ClassExpression result = new ClassExpression();

        OPECardinalityRestriction cardinalityRestriction = new OPECardinalityRestriction();

        cardinalityRestriction
            .setProperty(getIri(exactCardinality.getProperty().asOWLObjectProperty().getIRI()))
            .setExact(exactCardinality.getCardinality());

        addOwlClassExpressionToClassExpression(exactCardinality.getFiller(), cardinalityRestriction);

        result.setPropertyObject(cardinalityRestriction);

        return result;
    }

    private ClassExpression getOWLObjectMaxCardinalityAsClassExpression(OWLObjectMaxCardinality maxCardinality) {
        ClassExpression result = new ClassExpression();

        OPECardinalityRestriction cardinalityRestriction = new OPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(maxCardinality.getProperty().asOWLObjectProperty().getIRI()))
            .setMax(maxCardinality.getCardinality());

        addOwlClassExpressionToClassExpression(maxCardinality.getFiller(), cardinalityRestriction);

        result.setPropertyObject(cardinalityRestriction);

        return result;
    }

    private ClassExpression getOWLDataExactCardinalityAsClassExpression(OWLDataExactCardinality exactCardinality) {
        ClassExpression result = new ClassExpression();

        DPECardinalityRestriction cardinalityRestriction = new DPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(exactCardinality.getProperty().asOWLDataProperty().getIRI()))
            .setExact(exactCardinality.getCardinality())
            .setDataType(getIri(exactCardinality.getFiller().asOWLDatatype().getIRI()));

        result.setPropertyData(cardinalityRestriction);

        return result;
    }

    private ClassExpression getOWLDataMaxCardinalityAsClassExpression(OWLDataMaxCardinality maxCardinality) {
        ClassExpression result = new ClassExpression();

        DPECardinalityRestriction cardinalityRestriction = new DPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(maxCardinality.getProperty().asOWLDataProperty().getIRI()))
            .setMax(maxCardinality.getCardinality())
            .setDataType(getIri(maxCardinality.getFiller().asOWLDatatype().getIRI()));

        result.setPropertyData(cardinalityRestriction);

        return result;
    }
    private ClassExpression getOWLDataSomeValuesAsClassExpression(OWLDataSomeValuesFrom some) {
        ClassExpression result = new ClassExpression();

        DPECardinalityRestriction cardinalityRestriction = new DPECardinalityRestriction();
        cardinalityRestriction
                .setProperty(getIri(some.getProperty().asOWLDataProperty().getIRI()))
                .setQuantification("some");
        if (some.getFiller().getDataRangeType()== DataRangeType.DATATYPE)
                cardinalityRestriction.setDataType(getIri(some.getFiller().asOWLDatatype().getIRI()));
        else if (some.getFiller().getDataRangeType()==DataRangeType.DATA_ONE_OF)
            getOWLOneOfAsDataRange((OWLDataOneOf) some.getFiller(),cardinalityRestriction);

        result.setPropertyData(cardinalityRestriction);

        return result;
    }
    private DataRange getOWLOneOfAsDataRange
            (OWLDataOneOf owlOneOf,
            DataRange dr) {
        for (OWLLiteral one : owlOneOf.getOperandsAsList()) {
            dr.setDataType(getIri(one.getDatatype().getIRI()));
            dr.addOneOf(one.getLiteral());
        }
        return dr;
    }


    private ClassExpression getOWLObjectSomeValuesAsClassExpression(OWLObjectSomeValuesFrom someValuesFrom) {
        ClassExpression result = new ClassExpression();

        OPECardinalityRestriction oper = getOpeRestriction(someValuesFrom);

        result.setPropertyObject(oper);

        return result;
    }

    private OPECardinalityRestriction getOpeRestriction(OWLObjectSomeValuesFrom someValuesFrom) {
        OPECardinalityRestriction oper = new OPECardinalityRestriction();

        oper.setProperty(getIri(someValuesFrom.getProperty().asOWLObjectProperty().getIRI()));
        oper.setquantification("some");
        OWLClassExpression cex = someValuesFrom.getFiller();
        if (cex.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            oper.setClazz(getIri(someValuesFrom.getFiller().asOWLClass().getIRI()));
        } else if (cex.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
            oper.setIntersection(getOWLIntersection((OWLObjectIntersectionOf) cex));
        } else if (cex.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            oper.setUnion(getOWLUnion((OWLObjectUnionOf) cex));
        } else if (cex.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
            oper.setPropertyObject(getOpeRestriction((OWLObjectSomeValuesFrom) cex));
        } else {
            System.err.println("OpeRestriction:" + cex);
        }
        return oper;
    }

    private ClassExpression getOWLObjectMinCardinalityAsClassExpression(OWLObjectMinCardinality minCardinality) {
        ClassExpression result = new ClassExpression();

        OPECardinalityRestriction cardinalityRestriction = new OPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(minCardinality.getProperty().asOWLObjectProperty().getIRI()))
            .setMin(minCardinality.getCardinality())
            .setClazz(getIri(minCardinality.getFiller().asOWLClass().getIRI()));

        result.setPropertyObject(cardinalityRestriction);

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
        op.addPropertyRange(cex);

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
        String iri = getIri(a.getSubject().asIRI().get());

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

        if (property.equals("rdfs:comment"))
            c.setDescription(value);
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
            System.out.println("Ignoring annotation [" + property + "]");
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
        if (a.annotations() != null) {
            {
                a.annotations()
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
        if (a.annotations() != null) {
            {
                a.annotations()
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

        ObjectProperty op = (ObjectProperty) concepts.get(iri);
        PropertyAxiom sp = new PropertyAxiom();
        processAxiomAnnotations(a, sp);
        sp.setProperty(getIri(a.getSuperProperty().asOWLObjectProperty().getIRI()));
        op.addSubObjectPropertyOf(sp);

    }

    private void processClassAssertionAxiom(OWLClassAssertionAxiom a) {
        String indiIri = getIri(a.getIndividual().asOWLNamedIndividual().getIRI());
        String classIri = getIri(a.getClassExpression().asOWLClass().getIRI());
        Individual ind = (Individual) concepts.get(indiIri);
        ind.setIsType(classIri);
    }

    private void processSubDataPropertyAxiom(OWLSubDataPropertyOfAxiom a) {
        String iri = getIri(a.getSubProperty().asOWLDataProperty().getIRI());
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
        dp.addPropertyRange(prax);
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
            : shortIRI(result);
    }
}
