package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.*;
import java.util.stream.Collectors;

public class OWLToDiscovery {
    private DefaultPrefixManager defaultPrefixManager;
    private Map<String, Concept> concepts = new HashMap<>();

    public Ontology transform(OWLOntology owl) throws JsonProcessingException {
        initializePrefixManager(owl);

        Ontology discovery = new Ontology();

        processPrefixes(owl, discovery);

        processOntology(owl, discovery);

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
            .setDocumentId(ontology.getOntologyID().getOntologyIRI().get().toString())
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
            // Ignore instances
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
            processAnnotationAssertionAxiom((OWLAnnotationAssertionAxiom) a);   // TODO: CHECK!!
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
            processTransitiveObjectProperty((OWLTransitiveObjectPropertyAxiom) a);
        else
            System.err.println("Axiom: " + a);
    }

    private void processObjectPropertyDomainAxiom(OWLObjectPropertyDomainAxiom a) {
        String propertyIri = getIri(a.getProperty().asOWLObjectProperty().getIRI());
        String domainIri = getIri(a.getDomain().asOWLClass().getIRI());

        ObjectProperty op = (ObjectProperty)concepts.get(propertyIri);
        ClassExpression pd = op.getPropertyDomain();
        if (pd == null)
            op.setPropertyDomain(pd = new ClassExpression());

        pd.setClazz(domainIri);
    }

    private void processDisjointAxiom(OWLDisjointClassesAxiom a) {
        List<String> iris = a.getOperandsAsList()
            .stream()
            .map(e -> defaultPrefixManager.getPrefixIRI(((OWLClass) e).getIRI()))
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
        String iri = defaultPrefixManager.getPrefixIRI(((OWLClass) a.getSubClass()).getIRI());

        Clazz c = (Clazz)concepts.get(iri);
        ClassExpression subClassOf = c.getSubClassOf();

        if (subClassOf == null)
            c.setSubClassOf(subClassOf = new ClassExpression());

        OWLClassExpression superClass = a.getSuperClass();
        addOwlClassExpressionToClassExpression(superClass, subClassOf);
    }

    private void addOwlClassExpressionToClassExpression(OWLClassExpression oce, ClassExpression cex) {
        if (oce.getClassExpressionType() == ClassExpressionType.OWL_CLASS) {
            cex.setClazz(
                defaultPrefixManager.getPrefixIRI(oce.asOWLClass().getIRI())
            );
        } else if (oce.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF) {
            cex.setIntersection(getOWLIntersection((OWLObjectIntersectionOf)oce));
        } else {
            System.err.println("OWL Class expression: " + oce);
            // throw new IllegalStateException("Unhandled class expression type");
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
            .setDataType(defaultPrefixManager.getPrefixIRI(lit.getDatatype().getIRI()))
        );
        return result;
    }

    private ClassExpression getOWLObjectExactCardinalityAsClassExpression(OWLObjectExactCardinality exactCardinality) {
        ClassExpression result = new ClassExpression();

        OPECardinalityRestriction cardinalityRestriction = new OPECardinalityRestriction();
        cardinalityRestriction
            .setProperty(getIri(exactCardinality.getProperty().asOWLObjectProperty().getIRI()))
            .setExact(exactCardinality.getCardinality())
            .setClazz(getIri(exactCardinality.getFiller().asOWLClass().getIRI()));


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

        OPERestriction oper = new OPERestriction();

        oper
            .setProperty(getIri(someValuesFrom.getProperty().asOWLObjectProperty().getIRI()))
            .setClazz(getIri(someValuesFrom.getFiller().asOWLClass().getIRI()));

        result.setObjectSome(oper);

        return result;
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
        // Ignore
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

        String property = getIri(a.getProperty().asOWLAnnotationProperty().getIRI());

        String value = a.getValue().asLiteral().get().getLiteral();

        Concept c = concepts.get(iri);
        if (property.equals("rdfs:comment"))
            c.setDescription(value);
        else if (property.equals("rdfs:label"))
            c.setName(value);
        else {
            System.err.println("Ignoring annotation [" + property + "]");
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

        while (i.hasNext()) {
            ClassExpression cex = new ClassExpression();
            addOwlClassExpressionToClassExpression(i.next(), cex);
            c.addEquivalentTo(cex);
        }
    }

    private void processSubObjectPropertyAxiom(OWLSubObjectPropertyOfAxiom a) {
        String iri = getIri(a.getSubProperty().asOWLObjectProperty().getIRI());

        ObjectProperty op = (ObjectProperty) concepts.get(iri);
        SubObjectProperty sop = op.getSubObjectPropertyOf();

        if(sop == null)
            op.setSubObjectPropertyOf(sop = new SubObjectProperty());

        sop.setProperty(getIri(a.getSuperProperty().asOWLObjectProperty().getIRI()));

    }

    private void processClassAssertionAxiom(OWLClassAssertionAxiom a) {
        // Ignore/Do nothing!
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

    private void processTransitiveObjectProperty(OWLTransitiveObjectPropertyAxiom a) {
        String iri = getIri(a.getProperty().asOWLObjectProperty().getIRI());
        ObjectProperty op = (ObjectProperty) concepts.get(iri);
        op.setTransitive(true);
    }

    private String getIri(IRI iri) {
        return defaultPrefixManager.getPrefixIRI(iri);
    }
}
