package org.endeavourhealth.informationmanager.common.transform;

import com.google.common.base.Strings;
import org.endeavourhealth.informationmanager.common.Logger;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;
import uk.ac.manchester.cs.owl.owlapi.OWLDataOneOfImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

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

    public DiscoveryToOWL(){
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
        prefixManager = new DefaultPrefixManager();
    }


    /**
     * Transforms a Discovery JSON ontology to an OWL ontology
     * @param document
     * @return OWLOntology manager together with one ontology (optional) and a set of prefixes
     * @throws OWLOntologyCreationException
     * @throws FileFormatException
     */
    public OWLOntologyManager transform(Document document) throws OWLOntologyCreationException, FileFormatException {


        if (document == null || document.getInformationModel() == null)
            throw new FileFormatException("Missing InformationModel");

        Ontology ontology = document.getInformationModel();
        return transform(ontology);
    }
    public OWLOntologyManager transform(Ontology ontology) throws FileFormatException, OWLOntologyCreationException {

        String ontologyIri = null;
        //A Discovery module is an owl ontology
        if (ontology.getModule() != null)
            ontologyIri = ontology.getModule();

        if (ontologyIri == null)
            throw new FileFormatException("Missing ontology Iri");

        OWLOntology owlOntology = manager.createOntology(IRI.create(ontologyIri));

        processImports(owlOntology, dataFactory, manager, ontology.getImports());
        processPrefixes(manager, owlOntology,ontology.getNamespace());
        processClasses(owlOntology, manager, ontology.getClazz());
        processObjectProperties(owlOntology, manager, ontology.getObjectProperty());
        processDataProperties(owlOntology, manager, ontology.getDataProperty());
        processDataTypes(owlOntology, manager, ontology.getDataType());
        processAnnotationProperties(owlOntology, manager, ontology.getAnnotationProperty());
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

    public DiscoveryToOWL addNamespace(String prefix, String iri){
        this.prefixManager.setPrefix(prefix,iri);
        return this;
    }
    private void processPrefixes(OWLOntologyManager manager, OWLOntology owlOntology, Set<Namespace> namespace) {
        for (Namespace ns : namespace) {
            prefixManager.setPrefix(ns.getPrefix(), ns.getIri());
        }
        PrefixDocumentFormat ontologyFormat = new FunctionalSyntaxDocumentFormat();
        ontologyFormat.copyPrefixesFrom(prefixManager);
        manager.setOntologyFormat(owlOntology,ontologyFormat);
    }

    private void processClasses(OWLOntology ontology, OWLOntologyManager manager, Set<Clazz> clazzes) {
        if (clazzes == null || clazzes.size() == 0)
            return;
        int classno = 0;

        for (Clazz clazz : clazzes) {
            classno = classno + 1;
            IRI iri = getIri(clazz.getIri());
            // if ((classno % 1000)==0)
              // Logger.info(classno + " classes loaded");

            OWLClass owlClass = dataFactory.getOWLClass(iri);
            addConceptDeclaration(ontology, manager, owlClass, clazz);

            if (clazz.getSubClassOf() != null) {
                for (ClassAxiom subclass : clazz.getSubClassOf()) {
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
            if (clazz.getEquivalentTo() != null) {
                for (ClassAxiom equiclass : clazz.getEquivalentTo()) {
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
            if (clazz.getDisjointWithClass() != null) {
                Set<OWLClass> cex = new HashSet<>();
                cex.add(dataFactory.getOWLClass(getIri(clazz.getIri())));
                for (ConceptReference disjoint : clazz.getDisjointWithClass()) {
                    IRI disIri = getIri(disjoint.getIri());
                    cex.add(dataFactory.getOWLClass(disIri));
                }
                OWLDisjointClassesAxiom disax = dataFactory.getOWLDisjointClassesAxiom(cex);
                manager.addAxiom(ontology, disax);
            }
            if (clazz.getExpression() != null) {
                Logger.error("Unhandled expression for class [" + iri + "]");
            }
        }
    }

    private OWLClassExpression getOPERestrictionAsOWlClassExpression(ClassExpression cex) {
        OWLObjectPropertyExpression owlOpe;

        OPECardinalityRestriction card = cex.getPropertyObject();

        if (card.getProperty()!=null) {
            IRI prop = getIri(card.getProperty().getIri());
            owlOpe = dataFactory.getOWLObjectProperty(prop);
        } else {
            IRI prop=getIri(card.getInverseOf().getIri());
            owlOpe=dataFactory
                    .getOWLObjectInverseOf(
                            dataFactory.getOWLObjectProperty(prop));
        }

        if (card.getQuantification() != null) {
            String quant = card.getQuantification();
            if (quant.equals("some")) {
                return dataFactory.getOWLObjectSomeValuesFrom(
                        owlOpe,
                        getClassExpressionAsOWLClassExpression(card)
                );
            } else {
                Logger.error("Unknown quantification [" + quant + "]");
                return dataFactory.getOWLClass("unknown quantification", prefixManager);
            }
        } else if (card.getExact() != null) {
            return dataFactory.getOWLObjectExactCardinality(
                    card.getExact(),
                    owlOpe,
                    getClassExpressionAsOWLClassExpression(card)
            );
        } else if (card.getMin() != null && card.getMax() != null) {

            return dataFactory.getOWLObjectIntersectionOf(
                    new HashSet<>(Arrays.asList(
                            dataFactory.getOWLObjectMinCardinality(
                                    card.getMin(),
                                    owlOpe,
                                    getClassExpressionAsOWLClassExpression(card)
                            ),
                            dataFactory.getOWLObjectMaxCardinality(
                                    card.getMax(),
                                    owlOpe,
                                    getClassExpressionAsOWLClassExpression(card)
                            )
                    ))
            );
        } else if (card.getMin() != null) {
            return dataFactory.getOWLObjectMinCardinality(
                    card.getMin(),
                    owlOpe,
                    getClassExpressionAsOWLClassExpression(card)
            );
        } else if (card.getMax() != null) {
            return dataFactory.getOWLObjectMaxCardinality(
                    card.getMax(),
                    owlOpe,
                    getClassExpressionAsOWLClassExpression(card)
            );
        } else if (card.getIndividual() != null) {
            return dataFactory.getOWLObjectHasValue(
                    owlOpe
                    , dataFactory.getOWLNamedIndividual(getIri(card.getIndividual().getIri())));
        } else {
            Logger.error("Unknown propertyObject format");
            return dataFactory.getOWLClass("unknown propertyObject", prefixManager);
        }
    }

    private OWLClassExpression getDPERestrictionAsOWLClassExpression(ClassExpression cex) {

        IRI prop = getIri(cex.getPropertyData().getProperty().getIri());
        DPECardinalityRestriction card = cex.getPropertyData();

        // Unhandled properties
        if (!isNullOrEmpty(card.getOneOf())) Logger.error("Unhandled DPECardinalityRestriction = OneOf");
        if (card.getDataTypeRestriction() != null) Logger.error("Unhandled DPECardinalityRestriction = DataTypeRestriction");

        if (card.getExact() != null) {
            return dataFactory.getOWLDataExactCardinality(
                    card.getExact(),
                    dataFactory.getOWLDataProperty(prop),
                    getOWLDataRange(card)
            );
        } else if (card.getMin() != null) {
            return dataFactory.getOWLDataMinCardinality(
                    card.getMin(),
                    dataFactory.getOWLDataProperty(prop),
                    getOWLDataRange(card)
            );
        } else if (card.getMax() != null) {
            return dataFactory.getOWLDataMaxCardinality(
                    card.getMax(),
                    dataFactory.getOWLDataProperty(prop),
                    getOWLDataRange(card)
            );
        } else if (card.getQuantification() != null) {
            return dataFactory.getOWLDataSomeValuesFrom(
                    dataFactory.getOWLDataProperty(prop),
                    getOWLDataRange(card)
            );
        } else if (card.getExactValue() != null) {
            return dataFactory.getOWLDataHasValue(
                    dataFactory.getOWLDataProperty(prop),
                    dataFactory.getOWLLiteral(
                            card.getExactValue(),
                            OWL2Datatype.getDatatype(
                                    getIri(card.getDataType().getIri())
                            )
                    )
            );
        } else {
            Logger.error("Unknown DPE cardinality");
            Logger.error(card.toString());
            return dataFactory.getOWLClass("unknown DPE cardinality", prefixManager);
        }
    }

    public OWLClassExpression getClassExpressionAsOWLClassExpression(ClassExpression cex) {
        if (cex.getClazz() != null) {
            return dataFactory.getOWLClass(getIri(cex.getClazz().getIri()));
        } else if (cex.getIntersection() != null) {
            return dataFactory.getOWLObjectIntersectionOf(
                    cex.getIntersection()
                            .stream()
                            .map(this::getClassExpressionAsOWLClassExpression)
                            .collect(Collectors.toSet())
            );
        } else if (cex.getPropertyObject() != null) {
            return getOPERestrictionAsOWlClassExpression(cex);
        } else if (cex.getPropertyData() != null) {
            return getDPERestrictionAsOWLClassExpression(cex);
        } else if (cex.getObjectOneOf()!=null) {
            return getOneOfAsOWLClassExpression(cex);
        } else if (cex.getComplementOf()!=null){
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
        Set<OWLNamedIndividual> indiList= new HashSet<>();
        for (ConceptReference oneOf:cex.getObjectOneOf()){
            indiList.add(dataFactory.getOWLNamedIndividual(getIri(oneOf.getIri())));
        }
        return dataFactory.getOWLObjectOneOf(indiList);
    }

    private OWLDataRange getOWLDataRange(DataRange dr) {
        if (dr.getOneOf() != null) {
            Set<OWLLiteral> literals = new HashSet<>();
            for (ConceptReference v : dr.getOneOf()) {
                OWLLiteral literal = new OWLLiteralImpl
                        (v.getIri(), null,
                                dataFactory.getOWLDatatype(getIri(dr.getDataType().getIri())));
                literals.add(literal);
            }
            OWLDataOneOf oneOf = new OWLDataOneOfImpl(literals);
            return oneOf;
        } else

            return dataFactory.getOWLDatatype(getIri(dr.getDataType().getIri()));
    }

    private void addConceptDeclaration(OWLOntology ontology, OWLOntologyManager manager, OWLEntity owlClass, Concept concept) {
        OWLDeclarationAxiom declaration = dataFactory.getOWLDeclarationAxiom(owlClass);
        manager.addAxiom(ontology, declaration);

        if (concept.getIsA() != null) Logger.error("Unhandled concept declaration = IsA");

        if (concept.getName() != null && !concept.getName().isEmpty()) {
            OWLAnnotation label = dataFactory.getOWLAnnotation(
                    dataFactory.getRDFSLabel(),
                    dataFactory.getOWLLiteral(concept.getName())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), label));
        }

        if (concept.getDescription() != null && !concept.getDescription().isEmpty()) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getRDFSComment(),
                    dataFactory.getOWLLiteral(concept.getDescription())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }
        if (concept.getDbid() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_ID)),
                    dataFactory.getOWLLiteral(concept.getDbid())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }

        if (concept.getStatus() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_STATUS)),
                    dataFactory.getOWLLiteral(concept.getStatus().getName())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }
        if (concept.getVersion() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_VERSION)),
                    dataFactory.getOWLLiteral(concept.getVersion())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }

        if (concept.getCode() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_CODE)),
                    dataFactory.getOWLLiteral(concept.getCode())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }

        if (concept.getScheme() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_SCHEME)),
                    dataFactory.getOWLLiteral(concept.getScheme().getIri())
            );
            manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }
        if (concept.getAnnotations()!=null){
            for (Annotation annot: concept.getAnnotations()){
                OWLAnnotation owlAnnot= dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(annot.getProperty().getIri())),
                                dataFactory.getOWLLiteral(annot.getValue()));
                manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), owlAnnot));
            }
        }

    }

    private void processObjectProperties(OWLOntology ontology, OWLOntologyManager manager, Set<ObjectProperty> objectProperties) {
        if (objectProperties == null || objectProperties.size() == 0)
            return;
        // int opno=0;

        for (ObjectProperty op : objectProperties) {
            IRI iri = getIri(op.getIri());
            // opno++;
            //if ((opno % 1000)==0)
              //  Logger.info(opno + " object properties loaded");
            OWLObjectProperty owlOP = dataFactory.getOWLObjectProperty(iri);
            addConceptDeclaration(ontology, manager, owlOP, op);

            if (op.getSubObjectPropertyOf() != null) {
                for (PropertyAxiom sop : op.getSubObjectPropertyOf()) {
                    Set<OWLAnnotation> axiomAnnots = getAxiomAnnotations(sop);
                    IRI sub = getIri(sop.getProperty().getIri());
                    OWLSubObjectPropertyOfAxiom subAx;
                    if (axiomAnnots!=null) {
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
                for (ClassAxiom ce : op.getPropertyDomain()) {
                    Set<OWLAnnotation> ans = getAxiomAnnotations(ce);
                    OWLObjectPropertyDomainAxiom domAx;
                    if (ans!=null) {
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
                for (ClassAxiom ce : op.getObjectPropertyRange()) {
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
                        Logger.error("Invalid object property range "+ iri);
                    }
                }
            }

            if (op.getInversePropertyOf() != null) {
                Set<OWLAnnotation> annotations = getAxiomAnnotations(op.getInversePropertyOf());
                IRI inv = getIri(op.getInversePropertyOf().getProperty().getIri());
                OWLInverseObjectPropertiesAxiom invAx;
                if (annotations!=null) {
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
                    if (annotations!=null) {
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
                if (annotations!=null) {
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
                if (annotations!=null) {
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
                if (annotations!=null) {
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
                if (annotations!=null) {
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


    }

    private void processDataProperties(OWLOntology ontology, OWLOntologyManager manager, Set<DataProperty> dataProperties) {
        if (dataProperties == null || dataProperties.size() == 0)
            return;

        for (DataProperty dp : dataProperties) {
            IRI iri = getIri(dp.getIri());
            OWLDataProperty owlOP = dataFactory.getOWLDataProperty(iri);
            addConceptDeclaration(ontology, manager, owlOP, dp);

            if (dp.getSubDataPropertyOf() != null) {
                for (PropertyAxiom sp : dp.getSubDataPropertyOf()) {
                    Set<OWLAnnotation> annotations = getAxiomAnnotations(sp);
                    IRI sub = getIri(sp.getProperty().getIri());
                    OWLSubDataPropertyOfAxiom subAx;
                    if (annotations!=null) {
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
                for (DataRangeAxiom pr : dp.getDataPropertyRange()) {
                    Set<OWLAnnotation> annots = getAxiomAnnotations(pr);
                    OWLDataPropertyRangeAxiom rngAx =
                            getPropertyRangeAxiom(pr, iri, annots);
                    manager.addAxiom(ontology, rngAx);
                }
            }

            if (dp.getPropertyDomain() != null) {
                for (ClassAxiom ce : dp.getPropertyDomain()) {
                    IRI dom = getIri(ce.getClazz().getIri());
                    Set<OWLAnnotation> annots = getAxiomAnnotations(ce);

                    OWLDataPropertyDomainAxiom domAx;
                    if (annots!=null) {
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
                if (annotations!=null) {

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
    }

    private void processIndividuals(OWLOntology ontology, OWLOntologyManager manager, Set<Individual> individuals) {
        if (individuals != null) {
            for (Individual ind : individuals) {
                IRI iri = getIri(ind.getIri());
                //Create named individual
                OWLNamedIndividual owlNamed = dataFactory.getOWLNamedIndividual(iri);
                addConceptDeclaration(ontology, manager, owlNamed, ind);

                //Add data property axioms
                if (ind.getPropertyDataValue() != null) {
                    for (DataPropertyAssertionAxiom dv : ind.getPropertyDataValue()) {
                        Set<OWLAnnotation> annots = getAxiomAnnotations(ind);
                        OWLDataPropertyAssertionAxiom dpax;
                        OWLLiteral literal = dataFactory.getOWLLiteral(dv.getValue()
                                , dataFactory.getOWLDatatype(getIri(dv.getDataType().getIri())));
                        if (annots!=null) {
                            dpax = dataFactory.getOWLDataPropertyAssertionAxiom(
                                    dataFactory.getOWLDataProperty(getIri(dv.getProperty().getIri())),
                                    dataFactory.getOWLNamedIndividual(iri),
                                    literal,
                                    annots
                            );
                        } else {
                            dpax = dataFactory.getOWLDataPropertyAssertionAxiom(
                                    dataFactory.getOWLDataProperty(getIri(dv.getProperty().getIri())),
                                    dataFactory.getOWLNamedIndividual(iri),
                                    literal
                            );

                        }
                        manager.addAxiom(ontology, dpax);
                    }

                }
                if (ind.getIsType() != null) {
                    Set<OWLAnnotation> annots = getAxiomAnnotations(ind);
                    OWLClassAssertionAxiom assax;
                    if (annots!=null) {
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

        }
    }

    private OWLDataPropertyRangeAxiom getPropertyRangeAxiom
            (DataRangeAxiom pr,
             IRI iri,
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

    private void processDataTypes(OWLOntology ontology, OWLOntologyManager manager, Set<DataType> dataTypes) {
        if (dataTypes == null || dataTypes.size() == 0)
            return;

        for (DataType dt : dataTypes) {
            IRI iri = getIri(dt.getIri());

            // Unhandled (inherited) properties
            if (!isNullOrEmpty(dt.getAnnotations())) Logger.error("Unhandled DataType = Annotations");
            if (!Strings.isNullOrEmpty(dt.getCode())) Logger.error("Unhandled DataType = Code");
            if (dt.getDbid() != null) Logger.error("Unhandled DataType = Dbid");
            if (!Strings.isNullOrEmpty(dt.getIri())) Logger.error("Unhandled DataType = Iri");
            if (!isNullOrEmpty(dt.getIsA())) Logger.error("Unhandled DataType = IsA");
            if (!Strings.isNullOrEmpty(dt.getScheme().getIri())) Logger.error("Unhandled DataType = Scheme");
            if (dt.getStatus() != null) Logger.error("Unhandled DataType = Status");
            if (dt.getVersion() != null) Logger.error("Unhandled DataType = Version");


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

            for (DataTypeDefinition def : dt.getDataTypeDefinition()) {
                Set<OWLFacetRestriction> restrictions = def.getDataTypeRestriction()
                        .getFacetRestriction()
                        .stream()
                        .map(f -> dataFactory.getOWLFacetRestriction(
                                OWLFacet.getFacet(getIri(f.getFacet())),
                                dataFactory.getOWLLiteral(f.getConstrainingFacet()))
                        )
                        .collect(Collectors.toSet());

                OWLDatatypeDefinitionAxiom defAx = dataFactory.getOWLDatatypeDefinitionAxiom(
                        dataFactory.getOWLDatatype(iri),
                        dataFactory.getOWLDatatypeRestriction(
                                dataFactory.getOWLDatatype(getIri(def.getDataTypeRestriction().getDataType().getIri())),
                                restrictions
                        )
                );
                manager.addAxiom(ontology, defAx);
            }
        }
    }

    private IRI getIri(String iri) {
        if (iri.toLowerCase().startsWith("http:") || iri.toLowerCase().startsWith("https:"))
            return IRI.create(iri);
        else
            return prefixManager.getIRI(iri);
    }

    private Set<OWLAnnotation> getAxiomAnnotations(ClassAxiom Axiom) {
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

    private void processAnnotationProperties(OWLOntology ontology, OWLOntologyManager manager, Set<AnnotationProperty> annotationProperties) {
        if (annotationProperties == null || annotationProperties.size() == 0)
            return;

        for (AnnotationProperty ap : annotationProperties) {
            IRI iri = getIri(ap.getIri());
            OWLAnnotationProperty owlAP = dataFactory.getOWLAnnotationProperty(iri);
            addConceptDeclaration(ontology, manager, owlAP, ap);

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

            if (ap.getPropertyRange() != null) {
                for (AnnotationPropertyRangeAxiom arax : ap.getPropertyRange()) {
                    Set<OWLAnnotation> annotations = getAxiomAnnotations(arax);
                    OWLAnnotationPropertyRangeAxiom prAx;
                    if (annotations!=null) {
                        prAx = dataFactory.getOWLAnnotationPropertyRangeAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                getIri(arax.getConcept().getIri()),
                                annotations
                        );
                    } else {
                        prAx = dataFactory.getOWLAnnotationPropertyRangeAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                getIri(arax.getConcept().getIri())
                        );
                    }
                    manager.addAxiom(ontology, prAx);

                }
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
