package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.Logger;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.imapi.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Converts Discovery JSON syntax document to OWL functional syntax using an OWL factory
 *
 */
public class DiscoveryToOWL {
    private DefaultPrefixManager prefixManager;
    private OWLDataFactory dataFactory;
    private OWLOntologyManager manager;
    private Integer anon=0;
    private Map<String,OWLPropertyType> owlPropertyTypeMap;
    private List<ConceptType> ignoreTypes;

    public DiscoveryToOWL() {
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        prefixManager = new DefaultPrefixManager();
    }

    public void setIgnoreTypes(List<ConceptType> ignoreTypes){
        this.ignoreTypes= ignoreTypes;
    }


    /**
     * Transforms a Discovery JSON ontology to an OWL ontology
     *
     * @param document
     * @return OWLOntology manager together with one ontology (optional) and a set of prefixes
     * @throws OWLOntologyCreationException
     * @throws FileFormatException
     */
    public OWLOntologyManager transform(Document document) throws Exception {


        if (document == null || document.getInformationModel() == null)
            throw new FileFormatException("Missing InformationModel");

        Ontology ontology = document.getInformationModel();
        return transform(ontology);
    }

    public OWLOntologyManager transform(Ontology ontology) throws FileFormatException, OWLOntologyCreationException {

        String ontologyIri = OntologyIri.DISCOVERY.getValue();
        if (ontologyIri == null)
            throw new FileFormatException("Missing ontology Iri");

        OWLOntology owlOntology = manager.createOntology(IRI.create(ontologyIri));

        processImports(owlOntology, dataFactory, manager, ontology.getImports());
        processPrefixes(manager, owlOntology, ontology.getNamespace());
        mapOWLPropertyTypes(ontology.getConcept());
        processConcepts(owlOntology, manager, ontology.getConcept());
        processIndividuals(owlOntology, manager, ontology.getIndividual());

        return manager;
    }


    private void processImports(OWLOntology owlOntology, OWLDataFactory dataFactory,
                                OWLOntologyManager manager, Set<String> imports) {
        if (imports != null) {
            for (String importIri : imports) {
                OWLImportsDeclaration importDeclaration = dataFactory.getOWLImportsDeclaration(IRI.create(importIri));
                manager.applyChange(new AddImport(owlOntology, importDeclaration));
            }

        }
    }

    public DiscoveryToOWL addNamespace(String prefix, String iri) {
        this.prefixManager.setPrefix(prefix, iri);
        return this;
    }

    private void processPrefixes(OWLOntologyManager manager, OWLOntology owlOntology, Set<Namespace> namespace) {
        for (Namespace ns : namespace) {
            prefixManager.setPrefix(ns.getPrefix(), ns.getIri());
        }
        PrefixDocumentFormat ontologyFormat = new FunctionalSyntaxDocumentFormat();
        ontologyFormat.copyPrefixesFrom(prefixManager);
        manager.setOntologyFormat(owlOntology, ontologyFormat);
    }

    private void processIndividuals(OWLOntology ontology, OWLOntologyManager manager, Set<Individual> indis) {
        if (indis == null || indis.size() == 0)
            return;
        for (Individual ind : indis) {
            OWLEntity owlIndi = addConceptDeclaration(ontology, manager, ind);
            IRI iri = getIri(ind.getIri());
            processIndividual(ontology, manager, ind);
        }
    }

    private void mapOWLPropertyTypes(Set<Concept> concepts){
        owlPropertyTypeMap= new HashMap<>();
        if (concepts == null || concepts.size() == 0)
            return;
        for (Concept c:concepts){
            if (c.getConceptType()== ConceptType.ANNOTATION)
                owlPropertyTypeMap.put(c.getIri(),OWLPropertyType.ANNOTATION);
            else if (c.getConceptType()==ConceptType.DATAPROPERTY)
                owlPropertyTypeMap.put(c.getIri(),OWLPropertyType.DATA);
            else if (c.getConceptType()==ConceptType.OBJECTPROPERTY)
                owlPropertyTypeMap.put(c.getIri(),OWLPropertyType.OBJECT);
        }
    }

    private boolean ignoreConcept(Concept concept){
        if (ignoreTypes!=null)
            if (ignoreTypes.contains(concept.getConceptType()))
                return true;
        return false;
    }

    private void processConcepts(OWLOntology ontology, OWLOntologyManager manager, Set<Concept> concepts) {
        if (concepts == null || concepts.size() == 0)
            return;
        int classno = 0;

        for (Concept concept : concepts) {
            classno = classno + 1;
            IRI iri = getIri(concept.getIri());
            if (!ignoreConcept(concept)) {
                addConceptDeclaration(ontology, manager, concept);
                if (concept.getSubClassOf() != null) {
                    for (ClassExpression subclass : concept.getSubClassOf()) {
                        Set<OWLAnnotation> ans = getAxiomAnnotations(subclass);

                        OWLSubClassOfAxiom subAx;
                        if (ans != null) {
                            subAx = dataFactory.getOWLSubClassOfAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(subclass),
                                ans
                            );
                        } else {
                            subAx = dataFactory.getOWLSubClassOfAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(subclass)
                            );
                        }
                        manager.addAxiom(ontology, subAx);
                    }

                }
                if (concept.getEquivalentTo() != null) {
                    for (ClassExpression equiclass : concept.getEquivalentTo()) {
                        Set<OWLAnnotation> ans = getAxiomAnnotations(equiclass);
                        OWLEquivalentClassesAxiom equAx;
                        if (ans != null) {
                            equAx = dataFactory.getOWLEquivalentClassesAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(equiclass),
                                ans
                            );

                        } else {
                            equAx = dataFactory.getOWLEquivalentClassesAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(equiclass)
                            );
                        }
                        manager.addAxiom(ontology, equAx);

                    }
                }
                if (concept.getDisjointWith() != null) {
                    Set<OWLClass> cex = new HashSet<>();
                    cex.add(dataFactory.getOWLClass(getIri(concept.getIri())));
                    for (ConceptReference disjoint : concept.getDisjointWith()) {
                        IRI disIri = getIri(disjoint.getIri());
                        cex.add(dataFactory.getOWLClass(disIri));
                    }
                    OWLDisjointClassesAxiom disax = dataFactory.getOWLDisjointClassesAxiom(cex);
                    manager.addAxiom(ontology, disax);
                }
                if (concept.getConceptType() == ConceptType.OBJECTPROPERTY) {
                    processObjectProperty(ontology, manager, concept);
                } else if (concept.getConceptType() == ConceptType.DATAPROPERTY) {
                    processDataProperty(ontology, manager, concept);
                } else if (concept.getConceptType() == ConceptType.DATATYPE) {
                    processDataType(ontology, manager, concept);
                } else if (concept.getConceptType() == ConceptType.ANNOTATION) {
                    processAnnotationProperty(ontology, manager, concept);
                }
            }
        }
    }


    private OWLClassExpression getOPERestrictionAsOWlClassExpression(ClassExpression cex) {
        OWLObjectPropertyExpression owlOpe;

        PropertyValue card = cex.getPropertyValue();

        if (card.getProperty() != null) {
            IRI prop = getIri(card.getProperty().getIri());
            owlOpe = dataFactory.getOWLObjectProperty(prop);
        } else {
            IRI prop = getIri(card.getInverseOf().getIri());
            owlOpe = dataFactory
                .getOWLObjectInverseOf(
                    dataFactory.getOWLObjectProperty(prop));
        }
        QuantificationType quant= card.getQuantificationType();

        if (quant== QuantificationType.SOME) {
                return dataFactory.getOWLObjectSomeValuesFrom(
                    owlOpe,
                    getValueAsOWLClassExpression(card)
                );

        } else if (quant==QuantificationType.ONLY){
            return dataFactory.getOWLObjectAllValuesFrom(
                owlOpe,
                getValueAsOWLClassExpression(card)
            );

        }
        else if (quant==QuantificationType.RANGE){
            return dataFactory.getOWLObjectIntersectionOf(
                new HashSet<>(Arrays.asList(
                    dataFactory.getOWLObjectMinCardinality(
                        card.getMin(),
                        owlOpe,
                        getValueAsOWLClassExpression(card)
                    ),
                    dataFactory.getOWLObjectMaxCardinality(
                        card.getMax(),
                        owlOpe,
                        getValueAsOWLClassExpression(card)
                    )
                ))
            );
        } else if (quant==QuantificationType.EXACT){
            return dataFactory.getOWLObjectExactCardinality(
                card.getMin(),
                owlOpe,
                getValueAsOWLClassExpression(card)
            );

        }
          else if (quant == QuantificationType.MIN) {
            return dataFactory.getOWLObjectMinCardinality(
                card.getMin(),
                owlOpe,
                getValueAsOWLClassExpression(card)
            );
        } else if (quant==QuantificationType.MAX) {
            return dataFactory.getOWLObjectMaxCardinality(
                card.getMax(),
                owlOpe,
                getValueAsOWLClassExpression(card)
            );
        } else if (card.getIndividual() != null) {
            return dataFactory.getOWLObjectHasValue(
                owlOpe
                , dataFactory.getOWLNamedIndividual(getIri(card.getIndividual())));
        } else {
            Logger.error("Unknown propertyObject format");
            return dataFactory.getOWLClass("unknown propertyObject", prefixManager);
        }
    }

    /**
     * produces either a single data property restriction or an object intersection of several cardinalities
     *
     * @param cex Discovery propertyData expression
     * @return OWL Class expression
     */
    private OWLClassExpression getDPERestrictionAsOWLClassExpression(ClassExpression cex) {

        IRI prop = getIri(cex.getPropertyValue().getProperty().getIri());
        PropertyValue card = cex.getPropertyValue();
        if (card.getValueData() != null) {
            return dataFactory.getOWLDataHasValue(
                dataFactory.getOWLDataProperty(prop),
                dataFactory.getOWLLiteral(
                    card.getValueData(),
                    OWL2Datatype.getDatatype(
                        getIri(card.getValueType().getIri())
                    )
                )
            );
        }
        QuantificationType quant= card.getQuantificationType();
        if (quant==QuantificationType.EXACT) {
                return dataFactory.getOWLDataExactCardinality(
                    card.getMin(),
                    dataFactory.getOWLDataProperty(prop),
                    getOWLDataRange(card));
        } else if (quant==QuantificationType.RANGE){
                return dataFactory.getOWLObjectIntersectionOf(
                    new HashSet<>(Arrays.asList(
                        dataFactory.getOWLDataMinCardinality(
                            card.getMin(),
                            dataFactory.getOWLDataProperty(prop),
                            getOWLDataRange(card)
                        ),
                        dataFactory.getOWLDataMaxCardinality(
                            card.getMax(),
                            dataFactory.getOWLDataProperty(prop),
                            getOWLDataRange(card)))));
        } else if (quant==QuantificationType.MIN) {
            return dataFactory.getOWLDataMinCardinality(
                card.getMin(),
                dataFactory.getOWLDataProperty(prop),
                getOWLDataRange(card)
            );
        } else if (quant==QuantificationType.MAX) {
            return dataFactory.getOWLDataMinCardinality(
                card.getMax(),
                dataFactory.getOWLDataProperty(prop),
                getOWLDataRange(card)
            );

        } else if (quant==QuantificationType.SOME) {
            return dataFactory.getOWLDataSomeValuesFrom(
                dataFactory.getOWLDataProperty(prop),
                getOWLDataRange(card)
            );
        } else if (quant==QuantificationType.ONLY){
            return dataFactory.getOWLDataSomeValuesFrom(
                dataFactory.getOWLDataProperty(prop),
                getOWLDataRange(card)
            );
        } else {
                Logger.error("Peculiar property data structure");
                return dataFactory.getOWLClass("unknown DPE cardinality", prefixManager);
            }
    }

    public OWLClassExpression getValueAsOWLClassExpression(PropertyValue pv) {
        if (pv.getValueType() != null) {
            return dataFactory
                .getOWLClass(getIri(pv.getValueType().getIri()));
        } else if (pv.getExpression() != null)
            return getClassExpressionAsOWLClassExpression(pv.getExpression());
        else {
            Logger.error("Invalid property object restriction");
            return dataFactory.getOWLClass(getIri("http://DiscoveryDataService.org/InformationModel/Ontology#Invalidiri"));
        }

    }

    public OWLClassExpression getClassExpressionAsOWLClassExpression(ClassExpression cex) {
        if (cex.getUnion()!=null){
            if (cex.getUnion().size()>50) {
                anon++;
                return dataFactory.getOWLClass(getIri(":_large_union_" + 1));
            }
        }
        if (cex.getClazz() != null) {
            return dataFactory.getOWLClass(getIri(cex.getClazz().getIri()));
        } else if (cex.getIntersection() != null) {
            return dataFactory.getOWLObjectIntersectionOf(
                cex.getIntersection()
                    .stream()
                    .map(this::getClassExpressionAsOWLClassExpression)
                    .collect(Collectors.toSet())
            );
        } else if (cex.getPropertyValue() != null) {
            OWLPropertyType owlType = owlPropertyTypeMap.get(cex.getPropertyValue()
                .getProperty().getIri());
            if (owlType == OWLPropertyType.DATA)
                return getDPERestrictionAsOWLClassExpression(cex);
            else
                return getOPERestrictionAsOWlClassExpression(cex);
        } else if (cex.getObjectOneOf() != null) {
            return getOneOfAsOWLClassExpression(cex);
        } else if (cex.getComplementOf() != null) {
            return (getComplementOfAsAOWLClassExpression(cex));
        } else if (cex.getUnion() != null) {
            return dataFactory.getOWLObjectUnionOf(
                cex.getUnion()
                    .stream()
                    .map(this::getClassExpressionAsOWLClassExpression)
                    .collect(Collectors.toSet())
            );
        } else {
            Logger.error("Unknown cex");
            Logger.error(cex.toString());
            return dataFactory.getOWLClass("unknown cex", prefixManager);
        }
    }

    private OWLClassExpression getComplementOfAsAOWLClassExpression(ClassExpression cex) {
        return dataFactory
            .getOWLObjectComplementOf(
                getClassExpressionAsOWLClassExpression(
                    cex.getComplementOf()));
    }

    private OWLClassExpression getOneOfAsOWLClassExpression(ClassExpression cex) {
        Set<OWLNamedIndividual> indiList = new HashSet<>();
        for (ConceptReference oneOf : cex.getObjectOneOf()) {
            indiList.add(dataFactory.getOWLNamedIndividual(getIri(oneOf.getIri())));
        }
        return dataFactory.getOWLObjectOneOf(indiList);
    }

    private OWLDataRange getOWLDataRange(PropertyValue dr) {
        IRI fdt= getIri(dr.getValueType().getIri());
        if (dr.getOneOf()!=null){
            Set<OWLLiteral> owlList= new HashSet<>();
            dataFactory.getOWLDataOneOf(owlList);
            for (String s:dr.getOneOf()){
                owlList.add(dataFactory.getOWLLiteral(s));
            }
            return dataFactory.getOWLDataOneOf(owlList);
        }
        Set<OWLFacetRestriction> facets = new HashSet<>();
        OWLDatatype owldt= dataFactory.getOWLDatatype(getIri(dr.getValueType().getIri()));
        if (dr.getMinInclusive() != null) {
                OWLFacetRestriction owlRest = dataFactory
                    .getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE,
                        dataFactory.getOWLLiteral(dr.getMinInclusive(),
                            owldt));
                facets.add(owlRest);
            } else if (dr.getMinExclusive()!=null) {
            OWLFacetRestriction owlRest = dataFactory
                .getOWLFacetRestriction(OWLFacet.MIN_EXCLUSIVE,
                    dataFactory.getOWLLiteral(dr.getMinExclusive(),
                        dataFactory.getOWLDatatype(fdt)));
            facets.add(owlRest);
        }
        if (dr.getMaxInclusive()!=null) {
            OWLFacetRestriction owlRest = dataFactory
                .getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE,
                    dataFactory.getOWLLiteral(dr.getMaxInclusive(),
                        dataFactory.getOWLDatatype(fdt)));
            facets.add(owlRest);
        } else if (dr.getMinExclusive()!=null) {
            OWLFacetRestriction owlRest = dataFactory
                .getOWLFacetRestriction(OWLFacet.MAX_EXCLUSIVE,
                    dataFactory.getOWLLiteral(dr.getMaxExclusive(),
                        dataFactory.getOWLDatatype(fdt)));
            facets.add(owlRest);

        }

        if (dr.getPattern() != null) {
            OWLFacetRestriction owlRest = dataFactory
                .getOWLFacetRestriction(OWLFacet.PATTERN,
                    dataFactory.getOWLLiteral(dr.getPattern()));
            facets.add(owlRest);
        }
        if (facets.size()>0)
            return dataFactory.getOWLDatatypeRestriction(owldt,facets);
        else
            return owldt;
    }

    private OWLDataRange getOWLDataRange(DataPropertyRange dr) {
        return dataFactory.getOWLDatatype(getIri(dr.getDataType().getIri()));
    }

    private OWLEntity addConceptDeclaration(OWLOntology ontology, OWLOntologyManager manager, Concept concept) {
        OWLEntity entity;
        IRI iri = getIri(concept.getIri());
        if (concept.getConceptType() == ConceptType.CLASSONLY)
            entity = dataFactory.getOWLEntity(EntityType.CLASS, iri);
        else if (concept.getConceptType() == ConceptType.OBJECTPROPERTY)
            entity = dataFactory.getOWLEntity(EntityType.OBJECT_PROPERTY, iri);
        else if (concept.getConceptType() == ConceptType.DATAPROPERTY)
            entity = dataFactory.getOWLEntity(EntityType.DATA_PROPERTY, iri);
        else if (concept.getConceptType() == ConceptType.ANNOTATION)
            entity = dataFactory.getOWLEntity(EntityType.ANNOTATION_PROPERTY, iri);
        else if (concept.getConceptType() == ConceptType.DATATYPE)
            entity = dataFactory.getOWLEntity(EntityType.DATATYPE, iri);
        else if (concept.getConceptType()==ConceptType.RECORD)
            entity= dataFactory.getOWLEntity(EntityType.CLASS,iri);
        else if (concept.getConceptType()==ConceptType.VALUESET)
            entity= dataFactory.getOWLEntity(EntityType.CLASS,iri);
        else if (concept.getConceptType() == ConceptType.INDIVIDUAL)
            entity = dataFactory.getOWLEntity(EntityType.NAMED_INDIVIDUAL, iri);
        else {
            Logger.error("unknown concept type");
            throw new UnknownFormatConversionException("unrecognised concept type : " + iri);
        }

        OWLDeclarationAxiom declaration = dataFactory.getOWLDeclarationAxiom(entity);
        manager.addAxiom(ontology, declaration);

        if (concept.getName() != null && !concept.getName().isEmpty()) {
            OWLAnnotation label = dataFactory.getOWLAnnotation(
                dataFactory.getRDFSLabel(),
                dataFactory.getOWLLiteral(concept.getName())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), label));
        }

        if (concept.getDescription() != null && !concept.getDescription().isEmpty()) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                dataFactory.getRDFSComment(),
                dataFactory.getOWLLiteral(concept.getDescription())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), comment));
        }
        if (concept.getDbid() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_ID)),
                dataFactory.getOWLLiteral(concept.getDbid())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), comment));
        }

        if (concept.getStatus() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_STATUS)),
                dataFactory.getOWLLiteral(concept.getStatus().getName())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), comment));
        }
        if (concept.getVersion() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_VERSION)),
                dataFactory.getOWLLiteral(concept.getVersion())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), comment));
        }

        if (concept.getCode() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_CODE)),
                dataFactory.getOWLLiteral(concept.getCode())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), comment));
        }

        if (concept.getScheme() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_SCHEME)),
                dataFactory.getOWLLiteral(concept.getScheme().getIri())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), comment));
        }
        if (concept.getAnnotations() != null) {
            for (Annotation annot : concept.getAnnotations()) {
                OWLAnnotation owlAnnot = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(annot.getProperty().getIri())),
                    dataFactory.getOWLLiteral(annot.getValue()));
                manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(entity.getIRI(), owlAnnot));
            }
        }
        return entity;

    }

    private void processObjectProperty(OWLOntology ontology, OWLOntologyManager manager, Concept op) {
        IRI iri = getIri(op.getIri());
        // opno++;
        //if ((opno % 1000)==0)
        //  Logger.info(opno + " object properties loaded");

        if (op.getSubObjectPropertyOf() != null) {
            for (PropertyAxiom sop : op.getSubObjectPropertyOf()) {
                Set<OWLAnnotation> axiomAnnots = getAxiomAnnotations(sop);
                IRI sub = getIri(sop.getProperty().getIri());
                OWLSubObjectPropertyOfAxiom subAx;
                if (axiomAnnots != null) {
                    subAx = dataFactory.getOWLSubObjectPropertyOfAxiom(
                        dataFactory.getOWLObjectProperty(iri),
                        dataFactory.getOWLObjectProperty(sub),
                        axiomAnnots
                    );

                } else {
                    subAx = dataFactory.getOWLSubObjectPropertyOfAxiom(
                        dataFactory.getOWLObjectProperty(iri),
                        dataFactory.getOWLObjectProperty(sub)
                    );
                }
                manager.addAxiom(ontology, subAx);
            }
        }

        if (op.getPropertyDomain() != null) {
            for (ClassExpression ce : op.getPropertyDomain()) {
                Set<OWLAnnotation> ans = getAxiomAnnotations(ce);
                OWLObjectPropertyDomainAxiom domAx;
                if (ans != null) {
                    domAx = dataFactory.getOWLObjectPropertyDomainAxiom(
                        dataFactory.getOWLObjectProperty(iri),
                        getClassExpressionAsOWLClassExpression(ce),
                        ans
                    );
                } else {
                    domAx = dataFactory.getOWLObjectPropertyDomainAxiom(
                        dataFactory.getOWLObjectProperty(iri),
                        getClassExpressionAsOWLClassExpression(ce)
                    );

                }
                manager.addAxiom(ontology, domAx);
            }
        }

        if (op.getObjectPropertyRange() != null) {
            for (ClassExpression ce : op.getObjectPropertyRange()) {
                try {
                    Set<OWLAnnotation> ans = getAxiomAnnotations(ce);
                    OWLObjectPropertyRangeAxiom rngAx;
                    if (ans != null) {
                        rngAx = dataFactory.getOWLObjectPropertyRangeAxiom(
                            dataFactory.getOWLObjectProperty(iri),
                            getClassExpressionAsOWLClassExpression(ce),
                            ans
                        );
                    } else {
                        rngAx = dataFactory.getOWLObjectPropertyRangeAxiom(
                            dataFactory.getOWLObjectProperty(iri),
                            getClassExpressionAsOWLClassExpression(ce)
                        );
                    }
                    manager.addAxiom(ontology, rngAx);
                } catch (Exception e) {
                    Logger.error("Invalid object property range " + iri);
                }
            }
        }

        if (op.getInversePropertyOf() != null) {
            Set<OWLAnnotation> annotations = getAxiomAnnotations(op.getInversePropertyOf());
            IRI inv = getIri(op.getInversePropertyOf().getProperty().getIri());
            OWLInverseObjectPropertiesAxiom invAx;
            if (annotations != null) {
                invAx = dataFactory.getOWLInverseObjectPropertiesAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    dataFactory.getOWLObjectProperty(inv),
                    annotations
                );
            } else {
                invAx = dataFactory.getOWLInverseObjectPropertiesAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    dataFactory.getOWLObjectProperty(inv)
                );

            }
            manager.addAxiom(ontology, invAx);
        }


        if (op.getSubPropertyChain() != null) {
            for (SubPropertyChain chain : op.getSubPropertyChain()) {
                Set<OWLAnnotation> annotations = getAxiomAnnotations(chain);
                OWLSubPropertyChainOfAxiom chnAx;
                if (annotations != null) {
                    chnAx = dataFactory.getOWLSubPropertyChainOfAxiom(
                        chain.getProperty()
                            .stream()
                            .map(c -> dataFactory.getOWLObjectProperty(getIri(c.getIri())))
                            .collect(Collectors.toList()),
                        dataFactory.getOWLObjectProperty(iri),
                        annotations
                    );
                } else {
                    chnAx = dataFactory.getOWLSubPropertyChainOfAxiom(
                        chain.getProperty()
                            .stream()
                            .map(c -> dataFactory.getOWLObjectProperty(getIri(c.getIri())))
                            .collect(Collectors.toList()),
                        dataFactory.getOWLObjectProperty(iri)
                    );
                }
                manager.addAxiom(ontology, chnAx);

            }
        }

        if (op.getIsTransitive() != null) {
            Set<OWLAnnotation> annotations = getAxiomAnnotations(op.getIsTransitive());
            OWLTransitiveObjectPropertyAxiom trnsAx;
            if (annotations != null) {
                trnsAx = dataFactory.getOWLTransitiveObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    annotations
                );
            } else {
                trnsAx = dataFactory.getOWLTransitiveObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );
            }
            manager.addAxiom(ontology, trnsAx);
        }
        if (op.getIsFunctional() != null) {
            Set<OWLAnnotation> annotations = getAxiomAnnotations(op.getIsFunctional());

            OWLFunctionalObjectPropertyAxiom fncAx;
            if (annotations != null) {
                fncAx = dataFactory.getOWLFunctionalObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    annotations
                );
            } else {
                fncAx = dataFactory.getOWLFunctionalObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );
            }
            manager.addAxiom(ontology, fncAx);

        }
        if (op.getIsReflexive() != null) {
            Set<OWLAnnotation> annotations = getAxiomAnnotations(op.getIsReflexive());
            OWLReflexiveObjectPropertyAxiom rflxAx;
            if (annotations != null) {
                rflxAx = dataFactory.getOWLReflexiveObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    annotations
                );
            } else {
                rflxAx = dataFactory.getOWLReflexiveObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );

            }
            manager.addAxiom(ontology, rflxAx);

        }
        if (op.getIsSymmetric() != null) {
            Set<OWLAnnotation> annotations = getAxiomAnnotations(op.getIsSymmetric());
            OWLSymmetricObjectPropertyAxiom symAx;
            if (annotations != null) {
                symAx = dataFactory.getOWLSymmetricObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    annotations
                );
            } else {
                symAx = dataFactory.getOWLSymmetricObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );
            }
            manager.addAxiom(ontology, symAx);

        }

    }

    private void processDataProperty(OWLOntology ontology, OWLOntologyManager manager, Concept dp) {
        IRI iri = getIri(dp.getIri());


        if (dp.getSubDataPropertyOf() != null) {
            for (PropertyAxiom sp : dp.getSubDataPropertyOf()) {
                Set<OWLAnnotation> annotations = getAxiomAnnotations(sp);
                IRI sub = getIri(sp.getProperty().getIri());
                OWLSubDataPropertyOfAxiom subAx;
                if (annotations != null) {
                    subAx = dataFactory.getOWLSubDataPropertyOfAxiom(
                        dataFactory.getOWLDataProperty(iri),
                        dataFactory.getOWLDataProperty(sub),
                        annotations
                    );
                } else {
                    subAx = dataFactory.getOWLSubDataPropertyOfAxiom(
                        dataFactory.getOWLDataProperty(iri),
                        dataFactory.getOWLDataProperty(sub)
                    );
                }
                manager.addAxiom(ontology, subAx);
            }
        }

        if (dp.getDataPropertyRange() != null) {
            for (DataPropertyRange pr : dp.getDataPropertyRange()) {
                Set<OWLAnnotation> annots = getAxiomAnnotations(pr);
                OWLDataPropertyRangeAxiom rngAx =
                    getPropertyRangeAxiom(pr, iri, annots);
                manager.addAxiom(ontology, rngAx);
            }
        }

        if (dp.getPropertyDomain() != null) {
            for (ClassExpression ce : dp.getPropertyDomain()) {
                IRI dom = getIri(ce.getClazz().getIri());
                Set<OWLAnnotation> annots = getAxiomAnnotations(ce);

                OWLDataPropertyDomainAxiom domAx;
                if (annots != null) {
                    domAx = dataFactory.getOWLDataPropertyDomainAxiom(
                        dataFactory.getOWLDataProperty(iri),
                        dataFactory.getOWLClass(dom),
                        annots
                    );
                } else {
                    domAx = dataFactory.getOWLDataPropertyDomainAxiom(
                        dataFactory.getOWLDataProperty(iri),
                        dataFactory.getOWLClass(dom)
                    );
                }
                manager.addAxiom(ontology, domAx);
            }
        }

        if (dp.getIsFunctional() != null) {
            Set<OWLAnnotation> annotations = getAxiomAnnotations(dp.getIsFunctional());
            OWLFunctionalDataPropertyAxiom fncAx;
            if (annotations != null) {

                fncAx = dataFactory.getOWLFunctionalDataPropertyAxiom(
                    dataFactory.getOWLDataProperty(iri),
                    annotations
                );
            } else {

                fncAx = dataFactory.getOWLFunctionalDataPropertyAxiom(
                    dataFactory.getOWLDataProperty(iri)
                );
            }
            manager.addAxiom(ontology, fncAx);
        }

    }

    private void processIndividual(OWLOntology ontology, OWLOntologyManager manager, Individual ind) {

        IRI iri = getIri(ind.getIri());

        //Add data property axioms
        if (ind.getRoleGroup() != null) {
            for (ConceptRoleGroup rg : ind.getRoleGroup()) {
                for (ConceptRole dv:rg.getRole()) {
                    if (dv.getValueData() != null) {
                        OWLDataPropertyAssertionAxiom dpax;
                        OWLLiteral literal = dataFactory.getOWLLiteral(dv.getValueData()
                            , dataFactory.getOWLDatatype(getIri(dv.getValueType().getIri())));

                        dpax = dataFactory.getOWLDataPropertyAssertionAxiom(
                            dataFactory.getOWLDataProperty(getIri(dv.getProperty().getIri())),
                            dataFactory.getOWLNamedIndividual(iri),
                            literal
                        );

                        manager.addAxiom(ontology, dpax);

                    } else {
                        OWLObjectPropertyAssertionAxiom opax;
                        opax = dataFactory.getOWLObjectPropertyAssertionAxiom(
                            dataFactory.getOWLObjectProperty(getIri(dv.getProperty().getIri())),
                            dataFactory.getOWLNamedIndividual(iri),
                            dataFactory.getOWLNamedIndividual(getIri(dv.getValueType().getIri()))
                        );
                        manager.addAxiom(ontology, opax);
                    }
                }

            }
        }
        if (ind.getIsType() != null) {
            Set<OWLAnnotation> annots = getAxiomAnnotations(ind);
            OWLClassAssertionAxiom assax;
            if (annots != null) {
                assax = dataFactory.getOWLClassAssertionAxiom(
                    dataFactory.getOWLClass(getIri(ind.getIsType().getIri())),
                    dataFactory.getOWLNamedIndividual(iri),
                    annots
                );
            } else {
                assax = dataFactory.getOWLClassAssertionAxiom(
                    dataFactory.getOWLClass(getIri(ind.getIsType().getIri())),
                    dataFactory.getOWLNamedIndividual(iri)
                );
            }
            manager.addAxiom(ontology, assax);
        }


    }

    private OWLDataPropertyRangeAxiom getPropertyRangeAxiom(DataPropertyRange pr, IRI iri,
                                                            Set<OWLAnnotation> annots) {
        OWLDataRange owlr = getOWLDataRange(pr);
        OWLDataPropertyRangeAxiom rngAx;

        if (annots != null) {
            rngAx = dataFactory.getOWLDataPropertyRangeAxiom(
                dataFactory.getOWLDataProperty(iri),
                owlr,
                annots
            );
        } else {
            rngAx = dataFactory.getOWLDataPropertyRangeAxiom(
                dataFactory.getOWLDataProperty(iri),
                owlr
            );
        }
        return rngAx;
    }

    private void processDataType(OWLOntology ontology, OWLOntologyManager manager, Concept dt) {

        IRI iri = getIri(dt.getIri());

        if (dt.getName() != null && !dt.getName().isEmpty()) {
            OWLAnnotation label = dataFactory.getOWLAnnotation(
                dataFactory.getRDFSLabel(),
                dataFactory.getOWLLiteral(dt.getName())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(iri, label));
        }

        if (dt.getDescription() != null && !dt.getDescription().isEmpty()) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                dataFactory.getRDFSComment(),
                dataFactory.getOWLLiteral(dt.getDescription())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(iri, comment));
        }

            DataTypeDefinition def = dt.getDataTypeDefinition();
            IRI fdt = getIri(def.getDataType().getIri());
            Set<OWLFacetRestriction> facets = new HashSet<>();
            if (def.getMinInclusive() != null) {
                    OWLFacetRestriction owlRest = dataFactory
                        .getOWLFacetRestriction(OWLFacet.MIN_INCLUSIVE,
                            dataFactory.getOWLLiteral(def.getMinInclusive(),
                                dataFactory.getOWLDatatype(fdt)));
                    facets.add(owlRest);
                }
            if (def.getMinExclusive()!=null) {
                    OWLFacetRestriction owlRest = dataFactory
                        .getOWLFacetRestriction(OWLFacet.MIN_EXCLUSIVE,
                            dataFactory.getOWLLiteral(def.getMinExclusive(),
                                dataFactory.getOWLDatatype(fdt)));
                    facets.add(owlRest);
            }
            if (def.getMaxInclusive() != null) {
                    OWLFacetRestriction owlRest = dataFactory
                        .getOWLFacetRestriction(OWLFacet.MAX_INCLUSIVE,
                            dataFactory.getOWLLiteral(def.getMaxInclusive(),
                                dataFactory.getOWLDatatype(fdt)));
                    facets.add(owlRest);
                }
            if (def.getMaxExclusive()!=null) {
                    OWLFacetRestriction owlRest = dataFactory
                        .getOWLFacetRestriction(OWLFacet.MAX_EXCLUSIVE,
                            dataFactory.getOWLLiteral(def.getMaxExclusive(),
                                dataFactory.getOWLDatatype(fdt)));
                    facets.add(owlRest);
            }
            if (def.getPattern() != null) {
                OWLFacetRestriction owlRest = dataFactory
                    .getOWLFacetRestriction(OWLFacet.PATTERN,
                        dataFactory.getOWLLiteral(def.getPattern()));
                facets.add(owlRest);
            }
            OWLDatatypeDefinitionAxiom ax = dataFactory
                .getOWLDatatypeDefinitionAxiom(dataFactory.getOWLDatatype(getIri(dt.getIri())),
                    dataFactory.getOWLDatatypeRestriction(
                        dataFactory.getOWLDatatype(getIri(def.getDataType().getIri())), facets));
            manager.addAxiom(ontology, ax);
    }

    private IRI getIri(String iri) {
        if (iri.toLowerCase().startsWith("http:") || iri.toLowerCase().startsWith("https:"))
            return IRI.create(iri);
        else
            return prefixManager.getIRI(iri);
    }

    private Set<OWLAnnotation> getAxiomAnnotations(ClassExpression Axiom) {
        return getOwlAnnotations(Axiom.getDbid(), Axiom.getStatus(), Axiom.getVersion(), Axiom);
    }

    private Set<OWLAnnotation> getOwlAnnotations
            (Integer id, ConceptStatus status, Integer version, IMEntity Axiom) {
        if (id != null || (status != null) || (version != null)) {
            Set<OWLAnnotation> annots = new HashSet<>();
            if (id != null) {
                annots.add(dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_ID)),
                        dataFactory.getOWLLiteral(id)
                ));
            }
            if (status != null) {
                annots.add(dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_STATUS)),
                        dataFactory.getOWLLiteral(status.getName())
                ));
            }
            if (version != null) {
                annots.add(dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_VERSION)),
                        dataFactory.getOWLLiteral(version)
                ));
            }
            return annots;

        }
        return null;
    }

    private Set<OWLAnnotation> getAxiomAnnotations(IMEntity Axiom) {
        return getOwlAnnotations(Axiom.getDbid(), Axiom.getStatus(), Axiom.getVersion(), Axiom);
    }

    private void processAnnotationProperty(OWLOntology ontology, OWLOntologyManager manager, Concept ap) {

            IRI iri = getIri(ap.getIri());

            if (ap.getSubAnnotationPropertyOf() != null) {
                for (PropertyAxiom subAnnotation : ap.getSubAnnotationPropertyOf()) {
                    IRI sub = getIri(subAnnotation.getProperty().getIri());
                    Set<OWLAnnotation> annotations = getAxiomAnnotations(subAnnotation);
                    OWLSubAnnotationPropertyOfAxiom subAx;
                    if (annotations!=null) {
                        subAx = dataFactory.getOWLSubAnnotationPropertyOfAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                dataFactory.getOWLAnnotationProperty(sub),
                                annotations
                        );
                    } else {
                        subAx = dataFactory.getOWLSubAnnotationPropertyOfAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                dataFactory.getOWLAnnotationProperty(sub)
                        );

                    }
                    manager.addAxiom(ontology, subAx);
                }
            }

            if (ap.getObjectPropertyRange() != null) {
                for (ClassExpression arax : ap.getObjectPropertyRange()) {
                    Set<OWLAnnotation> annotations = getAxiomAnnotations(arax);
                    OWLAnnotationPropertyRangeAxiom prAx;
                    if (annotations!=null) {
                        prAx = dataFactory.getOWLAnnotationPropertyRangeAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                getIri(arax.getClazz().getIri()),
                                annotations
                        );
                    } else {
                        prAx = dataFactory.getOWLAnnotationPropertyRangeAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                getIri(arax.getClazz().getIri())
                        );
                    }
                    manager.addAxiom(ontology, prAx);

                }
            }

    }

    private boolean isNullOrEmpty(List<?> list) {
        return (list == null || list.size() == 0);
    }

    private boolean isNullOrEmpty(Set<?> list) {
        return (list == null || list.size() == 0);
    }
}
