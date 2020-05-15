package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.*;
import java.util.stream.Collectors;

public class OWLToDiscovery {

    private List<String> ignoreIris = Collections.singletonList("owl:topObjectProperty");
    private DefaultPrefixManager defaultPrefixManager;
    private Map<String, Concept> concepts = new HashMap<>();

    public Ontology transform(OWLOntology owl) {
        initializePrefixManager(owl);

        Ontology discovery = new Ontology();

        processOntology(owl, discovery);

        processPrefixes(owl, discovery);

        for (OWLDeclarationAxiom da: owl.getAxioms(AxiomType.DECLARATION))
            processDeclarationAxiom(da, discovery);

        for (OWLAxiom a: owl.getAxioms()) {
            if (a.getAxiomType() != AxiomType.DECLARATION)
                processAxiom(a, discovery);
        }

        return discovery;
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
        document.setDocumentInfo(
            new DocumentInfo()
            .setDocumentIri(ontology.getOntologyID().getOntologyIRI().get().toString())
        );
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
            System.out.println("Ignoring named individual: [" + iri + "]");
        } else
            System.err.println("OWL Declaration: " + a);
    }

    private void processAxiom(OWLAxiom a, Ontology discovery) {
        if (a.isOfType(AxiomType.OBJECT_PROPERTY_DOMAIN))
            processObjectPropertyDomainAxiom((OWLObjectPropertyDomainAxiom) a);
        else if (a.isOfType(AxiomType.DISJOINT_CLASSES))
            processDisjointAxiom((OWLDisjointClassesAxiom) a);
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
            processInverseFunctionalObjectProperty((OWLInverseFunctionalObjectPropertyAxiom) a);
        else
            System.err.println("Axiom: " + a);
    }

    private void processObjectPropertyDomainAxiom(OWLObjectPropertyDomainAxiom a) {
        String propertyIri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty) concepts.get(propertyIri);
        ClassExpression pd = op.getPropertyDomain();
        if (pd == null)
            op.setPropertyDomain(pd = new ClassExpression());

        if (a.getDomain().getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            String domainIri = getIri(a.getDomain().asOWLClass().getIRI());
            pd.setClazz(domainIri);
        } else if (a.getDomain().getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
            pd.setUnion(getOWLUnion((OWLObjectUnionOf)a.getDomain()));
        } else {
            System.err.println("Invalid object property domain : " + propertyIri);
        }
    }

    private void processDisjointAxiom(OWLDisjointClassesAxiom a) {
        List<String> iris = a.getOperandsAsList()
            .stream()
            .map(e -> getIri(((OWLClass) e).getIRI()))
            .collect(Collectors.toList());

        for (String iri : iris) {
            List<ClassExpression> others = iris
                .stream()
                .filter(i -> !i.equals(iri))
                .map(i -> new ClassExpression().setClazz(i))
                .collect(Collectors.toList());
            Clazz c = (Clazz)this.concepts.get(iri);
            c.addAllDisjointClasses(others);
        }
    }

    private void processSubClassAxiom(OWLSubClassOfAxiom a) {
        String iri = getIri(((OWLClass) a.getSubClass()).getIRI());

        Clazz c = (Clazz)concepts.get(iri);
        if (c == null)
            System.out.println("Ignoring abstract subClass: [" + iri + "]");
        else {
            ClassExpression subClassOf = new ClassExpression();
            addOwlClassExpressionToClassExpression(a.getSuperClass(), subClassOf);

            c.addSubClassOf(subClassOf);
        }


        /*
            SubObjectPropertyOf
         String iri = getIri(((OWLClass) a.getSubClass()).getIRI());

         Concept c = concepts.get(iri);
         if (c == null)
         System.out.println("Ignoring abstract subClass: [" + iri + "]");
         else {
         if (c instanceof Clazz) {
         ClassExpression superClassExpression = new ClassExpression();
         addOwlClassExpressionToClassExpression(a.getSuperClass(), superClassExpression);
         ((Clazz)c).addSubClassOf(superClassExpression);
         } else if (c instanceof ObjectProperty) {
         ((ObjectProperty)c).setSubObjectPropertyOf(
         new SubObjectProperty().setProperty(getIri(a.getSuperClass().asOWLClass().getIRI()))
         );
         } else {
         System.err.println("Unknown subclass concept type");
         }
         }
         */
    }

    private void addOwlClassExpressionToClassExpression(OWLClassExpression oce, ClassExpression cex) {
        if (oce.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            cex.setClazz(
                getIri(oce.asOWLClass().getIRI())
            );
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
            cex.setIntersection(getOWLIntersection((OWLObjectIntersectionOf) oce));
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
            cex.setObjectSome(getOpeRestriction((OWLObjectSomeValuesFrom) oce));
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
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM) {
                result.add(getOWLObjectSomeValuesAsClassExpression((OWLObjectSomeValuesFrom) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_MIN_CARDINALITY) {
                result.add(getOWLObjectMinCardinalityAsClassExpression((OWLObjectMinCardinality) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
                result.add(getOWLObjectIntersectionAsClassExpression((OWLObjectIntersectionOf) c));
            } else if (c.getClassExpressionType() == ClassExpressionType.OBJECT_UNION_OF) {
                result.add(getOWLObjectUnionAsClassExpression((OWLObjectUnionOf) c));
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

        result.setObjectCardinality(cardinalityRestriction);

        return result;
    }

    private ClassExpression getOWLObjectMaxCardinalityAsClassExpression(OWLObjectMaxCardinality maxCardinality) {
        ClassExpression result = new ClassExpression();

        OPECardinalityRestriction cardinalityRestriction = new OPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(maxCardinality.getProperty().asOWLObjectProperty().getIRI()))
            .setMax(maxCardinality.getCardinality());

        addOwlClassExpressionToClassExpression(maxCardinality.getFiller(), cardinalityRestriction);

        result.setObjectCardinality(cardinalityRestriction);

        return result;
    }

    private ClassExpression getOWLDataExactCardinalityAsClassExpression(OWLDataExactCardinality exactCardinality) {
        ClassExpression result = new ClassExpression();

        DPECardinalityRestriction cardinalityRestriction = new DPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(exactCardinality.getProperty().asOWLDataProperty().getIRI()))
            .setExact(exactCardinality.getCardinality())
            .setDataType(getIri(exactCardinality.getFiller().asOWLDatatype().getIRI()));

        result.setDataCardinality(cardinalityRestriction);

        return result;
    }

    private ClassExpression getOWLDataMaxCardinalityAsClassExpression(OWLDataMaxCardinality maxCardinality) {
        ClassExpression result = new ClassExpression();

        DPECardinalityRestriction cardinalityRestriction = new DPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(maxCardinality.getProperty().asOWLDataProperty().getIRI()))
            .setMax(maxCardinality.getCardinality())
            .setDataType(getIri(maxCardinality.getFiller().asOWLDatatype().getIRI()));

        result.setDataCardinality(cardinalityRestriction);

        return result;
    }

    private ClassExpression getOWLObjectSomeValuesAsClassExpression(OWLObjectSomeValuesFrom someValuesFrom) {
        ClassExpression result = new ClassExpression();

        OPERestriction oper = getOpeRestriction(someValuesFrom);

        result.setObjectSome(oper);

        return result;
    }

    private OPERestriction getOpeRestriction(OWLObjectSomeValuesFrom someValuesFrom) {
        OPERestriction oper = new OPERestriction();

        oper.setProperty(getIri(someValuesFrom.getProperty().asOWLObjectProperty().getIRI()));
        OWLClassExpression cex = someValuesFrom.getFiller();
        if (cex.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            oper.setClazz(getIri(someValuesFrom.getFiller().asOWLClass().getIRI()));
        } else if (cex.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
            oper.addIntersection(getOWLObjectIntersectionAsClassExpression((OWLObjectIntersectionOf)cex));
        } else {
            System.err.println("OpeRestriction:" + cex.getClassExpressionType().getName());
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

        result.setObjectCardinality(cardinalityRestriction);

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
        op.setInversePropertyOf(new SimpleProperty().setProperty(secondIri));

        op = (ObjectProperty)concepts.get(secondIri);
        op.setInversePropertyOf(new SimpleProperty().setProperty(firstIri));
    }

    private void processObjectPropertyRangeAxiom(OWLObjectPropertyRangeAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty)concepts.get(iri);
        ClassExpression cex = op.getPropertyRange();
        if (cex == null)
            op.setPropertyRange(cex = new ClassExpression());

        addOwlClassExpressionToClassExpression(a.getRange(), cex);
        op.setPropertyRange(cex);
    }

    private void processDifferentIndividualsAxiom(OWLDifferentIndividualsAxiom a) {
        System.out.println("Ignoring different individuals: [" + a.toString() + "]");
    }

    private void processFunctionalObjectPropertyAxiom(OWLFunctionalObjectPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty)concepts.get(iri);

        op.setFunctional(true);
    }

    private void processFunctionalDataPropertyAxiom(OWLFunctionalDataPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLDataProperty().getIRI());

        DataProperty dp = (DataProperty)concepts.get(iri);

        dp.setFunctional(true);
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
        else {
            System.out.println("Ignoring annotation [" + property + "]");
        }
    }

    private void processAnnotationPropertyRangeAxiom(OWLAnnotationPropertyRangeAxiom a) {
        String iri = getIri(a.getProperty().asOWLAnnotationProperty().getIRI());

        AnnotationProperty dp = (AnnotationProperty) concepts.get(iri);

        ClassExpression range = dp.getPropertyRange();

        if (range == null)
            dp.setPropertyRange(range = new ClassExpression());

        range.setClazz(getIri(a.getRange()));
    }

    private void processEquivalentClassesAxiom(OWLEquivalentClassesAxiom a) {
        Iterator<OWLClassExpression> i = a.getClassExpressions().iterator();
        String iri = getIri(i.next().asOWLClass().getIRI());

        Clazz c = (Clazz)concepts.get(iri);

        if (c == null)
            System.out.println("Ignoring abstract class: [" + iri + "]");
        else {
            while (i.hasNext()) {
                ClassExpression cex = new ClassExpression();
                addOwlClassExpressionToClassExpression(i.next(), cex);
                c.addEquivalentTo(cex);
            }
        }
    }

    private void processSubObjectPropertyAxiom(OWLSubObjectPropertyOfAxiom a) {
        String iri = getIri(a.getSubProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty) concepts.get(iri);
        SubObjectProperty sop = op.getSubObjectPropertyOf();

        if(sop == null)
            op.setSubObjectPropertyOf(sop = new SubObjectProperty());

        if (a.getSuperProperty().isOWLObjectProperty())
            sop.setProperty(getIri(a.getSuperProperty().asOWLObjectProperty().getIRI()));
        else
            sop.setInverseProperty(getIri(a.getSuperProperty().asObjectPropertyExpression().getInverseProperty().asOWLObjectProperty().getIRI()));

    }

    private void processClassAssertionAxiom(OWLClassAssertionAxiom a) {
        if (a.getIndividual().isOWLNamedIndividual())
            System.out.println("Ignoring class assertion: [" + getIri(a.getIndividual().asOWLNamedIndividual().getIRI()) + "]");
        else
            System.out.println("Ignoring class assertion: [" + a.getIndividual().asOWLAnonymousIndividual().getID().toString() + "]");
    }

    private void processSubDataPropertyAxiom(OWLSubDataPropertyOfAxiom a) {
        String iri = getIri(a.getSubProperty().asOWLDataProperty().getIRI());
        DataProperty dp = (DataProperty) concepts.get(iri);
        dp.setSubDataPropertyOf(
            new SimpleProperty()
            .setProperty(getIri(a.getSuperProperty().asOWLDataProperty().getIRI()))
        );
    }

    private void processSubAnnotationPropertyAxiom(OWLSubAnnotationPropertyOfAxiom a) {
        String iri = getIri(a.getSubProperty().asOWLAnnotationProperty().getIRI());
        AnnotationProperty ap = (AnnotationProperty) concepts.get(iri);
        ap.addSubAnnotationPropertyOf(
            getIri(a.getSuperProperty().asOWLAnnotationProperty().getIRI())
        );
    }

    private void processDataPropertyRangeAxiom(OWLDataPropertyRangeAxiom a) {
        String iri = getIri(a.getProperty().asOWLDataProperty().getIRI());
        DataProperty dp = (DataProperty) concepts.get(iri);
        ClassExpression cex = new ClassExpression()
            .setClazz(getIri(a.getRange().asOWLDatatype().getIRI()));
        dp.setPropertyRange(cex);
    }

    private void processTransitiveObjectPropertyAxiom(OWLTransitiveObjectPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());
        if (ignoreIris.contains(iri))
            return;

        ObjectProperty op = (ObjectProperty) concepts.get(iri);
        op.setTransitive(true);
    }

    private void processDatatypeDefinitionAxiom(OWLDatatypeDefinitionAxiom a) {
        String iri = getIri(a.getDatatype().asOWLDatatype().getIRI());
        DataType dt = (DataType) concepts.get(iri);
        OWLDataRange r = a.getDataRange();

        DataTypeDefinition dtd = new DataTypeDefinition();

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
        System.out.println("Ignoring data property assertion: [" + getIri(a.getSubject().asOWLNamedIndividual().getIRI()) + "]");
    }

    private void processObjectPropertyAssertionAxiom(OWLObjectPropertyAssertionAxiom a) {
        System.out.println("Ignoring object property assertion: [" + getIri(a.getSubject().asOWLNamedIndividual().getIRI()) + "]");
    }

    private void processDataPropertyDomainAxiom(OWLDataPropertyDomainAxiom a) {
        String propertyIri = getIri(a.getProperty().asOWLDataProperty().getIRI());
        String domainIri = getIri(a.getDomain().asOWLClass().getIRI());

        DataProperty dp = (DataProperty)concepts.get(propertyIri);
        ClassExpression pd = dp.getPropertyDomain();
        if (pd == null)
            dp.setPropertyDomain(pd = new ClassExpression());

        pd.setClazz(domainIri);
    }

    private void processInverseFunctionalObjectProperty(OWLInverseFunctionalObjectPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());

        System.out.println("Ignoring inverse functional object property axiom: [" + iri + "]");
    }

    private String getIri(IRI iri) {
        String result = defaultPrefixManager.getPrefixIRI(iri);

        return (result == null)
            ? iri.toString()
            : result;
    }
}
