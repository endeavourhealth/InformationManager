package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class DiscoveryToOWL {
    private DefaultPrefixManager prefixManager;
    private OWLDataFactory dataFactory;

    public OWLOntology transform(Document document) throws OWLOntologyCreationException, FileFormatException {

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        if (document == null || document.getInformationModel() == null)
            throw new FileFormatException("Missing InformationModel");

        Ontology ontology = document.getInformationModel();

        String ontologyIri = null;
        if (ontology.getIri() != null)
            ontologyIri = ontology.getIri();

        if (ontologyIri == null)
            throw new FileFormatException("Missing ontology Iri");

        OWLOntology owlOntology = manager.createOntology(IRI.create(ontologyIri));
        dataFactory = manager.getOWLDataFactory();

        processImports(owlOntology, dataFactory, manager, ontology.getImports());
        processPrefixes(owlOntology, ontology.getNamespace());
        processClasses(owlOntology, ontology.getClazz());
        processObjectProperties(owlOntology, ontology.getObjectProperty());
        processDataProperties(owlOntology, ontology.getDataProperty());
        processDataTypes(owlOntology, ontology.getDataType());
        processAnnotationProperties(owlOntology, ontology.getAnnotationProperty());

        return owlOntology;
    }


    private void processImports(OWLOntology owlOntology, OWLDataFactory dataFactory,
                                OWLOntologyManager manager, List<String> imports) {
        if (imports != null) {
            for (String importIri : imports) {
                OWLImportsDeclaration importDeclaration = dataFactory.getOWLImportsDeclaration(IRI.create(importIri));
                manager.applyChange(new AddImport(owlOntology, importDeclaration));
            }

        }
    }

    private void processPrefixes(OWLOntology owlOntology, List<Namespace> namespace) {
        prefixManager = new DefaultPrefixManager();
        for (Namespace ns : namespace) {
            prefixManager.setPrefix(ns.getPrefix(), ns.getIri());
        }
        OWLDocumentFormat ontologyFormat = owlOntology.getNonnullFormat();
        if (ontologyFormat instanceof PrefixDocumentFormat) {
            ((PrefixDocumentFormat) ontologyFormat).copyPrefixesFrom(prefixManager);
        }
    }

    private void processClasses(OWLOntology ontology, List<Clazz> clazzes) {
        if (clazzes == null || clazzes.size() == 0)
            return;
        Integer classno = 0;

        for (Clazz clazz : clazzes) {
            classno = classno + 1;
            IRI iri = getIri(clazz.getIri());

            OWLClass owlClass = dataFactory.getOWLClass(iri);
            addConceptDeclaration(ontology, owlClass, clazz);

            if (clazz.getSubClassOf() != null) {
                for (ClassAxiom subclass : clazz.getSubClassOf()) {
                    List<OWLAnnotation> ans = getAxiomAnnotations(subclass);

                    OWLSubClassOfAxiom subAx;
                    if (ans != null) {
                        subAx = dataFactory.getOWLSubClassOfAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(subclass),
                                ans
                        );
                    }
                    else {
                        subAx = dataFactory.getOWLSubClassOfAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(subclass)
                        );
                    }
                    ontology.addAxiom(subAx);
                }

            }
            if (clazz.getEquivalentTo() != null) {
                for (ClassAxiom equiclass : clazz.getEquivalentTo()) {
                    List<OWLAnnotation> ans = getAxiomAnnotations(equiclass);
                    OWLEquivalentClassesAxiom equAx;
                    if (ans != null) {
                        equAx = dataFactory.getOWLEquivalentClassesAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(equiclass),
                                ans
                        );

                    }
                    else {
                        equAx = dataFactory.getOWLEquivalentClassesAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(equiclass)
                        );
                    }
                    ontology.addAxiom(equAx);

                }
            }

        }
    }


    private OWLClassExpression getClassExpressionAsOWLClassExpression(ClassExpression cex) {
        if (cex.getClazz() != null) {
            return dataFactory.getOWLClass(getIri(cex.getClazz()));
        } else if (cex.getIntersection() != null) {
            return dataFactory.getOWLObjectIntersectionOf(
                    cex.getIntersection()
                            .stream()
                            .map(this::getClassExpressionAsOWLClassExpression)
            );
        } else if (cex.getDataHasValue() != null) {
            IRI prop = getIri(cex.getDataHasValue().getProperty());

            return dataFactory.getOWLDataHasValue(
                    dataFactory.getOWLDataProperty(prop),
                    dataFactory.getOWLLiteral(
                            cex.getDataHasValue().getValue(),
                            OWL2Datatype.getDatatype(
                                    getIri(cex.getDataHasValue().getDataType())
                            )
                    )
            );

        } else if (cex.getPropertyObject() != null) {
            IRI prop = getIri(cex.getPropertyObject().getProperty());
            OPECardinalityRestriction card = cex.getPropertyObject();
            String quant = card.getquantification();
            if (quant != null) {
                if (quant.equals("some")) {
                    return dataFactory.getOWLObjectSomeValuesFrom(
                            dataFactory.getOWLObjectProperty(prop),
                            getClassExpressionAsOWLClassExpression(card)
                    );
                } else {
                    System.err.println(card);
                    return dataFactory.getOWLClass("unknown quantification");
                }
            } else if (card.getExact() != null) {
                return dataFactory.getOWLObjectExactCardinality(
                        card.getExact(),
                        dataFactory.getOWLObjectProperty(prop),
                        getClassExpressionAsOWLClassExpression(card)
                );
            } else if (card.getMin() != null && card.getMax() != null) {
                return dataFactory.getOWLObjectIntersectionOf(
                        Arrays.asList(
                                dataFactory.getOWLObjectMinCardinality(
                                        card.getMin(),
                                        dataFactory.getOWLObjectProperty(prop),
                                        getClassExpressionAsOWLClassExpression(card)
                                ),
                                dataFactory.getOWLObjectMaxCardinality(
                                        card.getMax(),
                                        dataFactory.getOWLObjectProperty(prop),
                                        getClassExpressionAsOWLClassExpression(card)
                                )
                        )
                );
            } else if (card.getMin() != null) {
                return dataFactory.getOWLObjectMinCardinality(
                        card.getMin(),
                        dataFactory.getOWLObjectProperty(prop),
                        getClassExpressionAsOWLClassExpression(card)
                );
            } else if (card.getMax() != null) {
                return dataFactory.getOWLObjectMaxCardinality(
                        card.getMax(),
                        dataFactory.getOWLObjectProperty(prop),
                        getClassExpressionAsOWLClassExpression(card)
                );
            } else {
                return dataFactory.getOWLObjectSomeValuesFrom(
                        dataFactory.getOWLObjectProperty(prop),
                        getClassExpressionAsOWLClassExpression(card)
                );
            }
        } else if (cex.getPropertyData() != null) {
            IRI prop = getIri(cex.getPropertyData().getProperty());
            DPECardinalityRestriction card = cex.getPropertyData();
            if (card.getExact() != null) {
                return dataFactory.getOWLDataExactCardinality(
                        card.getExact(),
                        dataFactory.getOWLDataProperty(prop),
                        dataFactory.getOWLDatatype(getIri(card.getDataType()))
                );
            } else if (card.getMin() != null) {
                return dataFactory.getOWLDataMinCardinality(
                        card.getMin(),
                        dataFactory.getOWLDataProperty(prop),
                        dataFactory.getOWLDatatype(getIri(card.getDataType()))
                );
            } else if (card.getMax() != null) {
                return dataFactory.getOWLDataMaxCardinality(
                        card.getMax(),
                        dataFactory.getOWLDataProperty(prop),
                        dataFactory.getOWLDatatype(getIri(card.getDataType()))
                );
            } else {
                System.err.println(card);
                return dataFactory.getOWLClass("unknown card");
            }
        } else if (cex.getUnion() != null) {
            return dataFactory.getOWLObjectUnionOf(
                    cex.getUnion()
                            .stream()
                            .map(this::getClassExpressionAsOWLClassExpression)
            );
        } else {
            System.err.println(cex);
            return dataFactory.getOWLClass("unknown cex");
        }
    }

    private void addConceptDeclaration(OWLOntology ontology, OWLEntity owlClass, Concept concept) {
        OWLDeclarationAxiom declaration = dataFactory.getOWLDeclarationAxiom(owlClass);
        ontology.addAxiom(declaration);

        if (concept.getName() != null && !concept.getName().isEmpty()) {
            OWLAnnotation label = dataFactory.getOWLAnnotation(
                    dataFactory.getRDFSLabel(),
                    dataFactory.getOWLLiteral(concept.getName())
            );
            ontology.addAxiom(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), label));
        }

        if (concept.getDescription() != null && !concept.getDescription().isEmpty()) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getRDFSComment(),
                    dataFactory.getOWLLiteral(concept.getDescription())
            );
            ontology.addAxiom(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }
        if (concept.getId() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_ID)),
                    dataFactory.getOWLLiteral(concept.getId())
            );
            ontology.addAxiom(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }

        if (concept.getStatus() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_STATUS)),
                    dataFactory.getOWLLiteral(concept.getStatus().getName())
            );
            ontology.addAxiom(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }
        if (concept.getVersion() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_VERSION)),
                    dataFactory.getOWLLiteral(concept.getVersion())
            );
            ontology.addAxiom(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }

        if (concept.getCode() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_CODE)),
                    dataFactory.getOWLLiteral(concept.getCode())
            );
            ontology.addAxiom(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }

        if (concept.getScheme() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_SCHEME)),
                    dataFactory.getOWLLiteral(concept.getScheme())
            );
            ontology.addAxiom(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }
    }

    private void processObjectProperties(OWLOntology ontology, List<ObjectProperty> objectProperties) {
        if (objectProperties == null || objectProperties.size() == 0)
            return;

        for (ObjectProperty op : objectProperties) {
            IRI iri = getIri(op.getIri());
            OWLObjectProperty owlOP = dataFactory.getOWLObjectProperty(iri);
            addConceptDeclaration(ontology, owlOP, op);

            if (op.getSubObjectPropertyOf() != null) {
                for (PropertyAxiom sop : op.getSubObjectPropertyOf()) {
                    List<OWLAnnotation> axiomAnnots = getAxiomAnnotations(sop);
                    IRI sub = getIri(sop.getProperty());
                    OWLSubObjectPropertyOfAxiom subAx;
                    if (axiomAnnots!=null) {
                        subAx = dataFactory.getOWLSubObjectPropertyOfAxiom(
                                dataFactory.getOWLObjectProperty(iri),
                                dataFactory.getOWLObjectProperty(sub),
                                axiomAnnots
                        );

                    }
                    else {
                        subAx = dataFactory.getOWLSubObjectPropertyOfAxiom(
                                dataFactory.getOWLObjectProperty(iri),
                                dataFactory.getOWLObjectProperty(sub)
                        );
                    }
                    ontology.addAxiom(subAx);
                }
            }

            if (op.getPropertyDomain() != null) {
                for (ClassAxiom ce : op.getPropertyDomain()) {
                    if (ce.getClazz() != null) {
                        IRI dom = getIri(ce.getClazz());
                        List<OWLAnnotation> ans = getAxiomAnnotations(ce);
                        OWLObjectPropertyDomainAxiom domAx;
                        if (ans != null) {
                            domAx = dataFactory.getOWLObjectPropertyDomainAxiom(
                                    dataFactory.getOWLObjectProperty(iri),
                                    dataFactory.getOWLClass(dom),
                                    ans
                            );
                        } else {
                            domAx = dataFactory.getOWLObjectPropertyDomainAxiom(
                                    dataFactory.getOWLObjectProperty(iri),
                                    dataFactory.getOWLClass(dom)
                            );

                        }
                        ontology.addAxiom(domAx);

                    } else if (ce.getUnion() != null) {
                        OWLObjectPropertyDomainAxiom domAx = dataFactory.getOWLObjectPropertyDomainAxiom(
                                dataFactory.getOWLObjectProperty(iri),
                                dataFactory.getOWLObjectUnionOf(
                                        ce.getUnion()
                                                .stream()
                                                .map(this::getClassExpressionAsOWLClassExpression)
                                )
                        );
                        ontology.addAxiom(domAx);
                    }
                }
            }

            if (op.getPropertyRange() != null) {
                for (ClassAxiom ce : op.getPropertyRange()) {
                    IRI rng = getIri(ce.getClazz());
                    List<OWLAnnotation> ans = getAxiomAnnotations(ce);
                    OWLObjectPropertyRangeAxiom rngAx;
                    if (ans != null) {
                        rngAx = dataFactory.getOWLObjectPropertyRangeAxiom(
                                dataFactory.getOWLObjectProperty(iri),
                                dataFactory.getOWLClass(rng),
                                ans
                        );
                    } else {
                        rngAx = dataFactory.getOWLObjectPropertyRangeAxiom(
                                dataFactory.getOWLObjectProperty(iri),
                                dataFactory.getOWLClass(rng)
                        );
                    }
                    ontology.addAxiom(rngAx);
                }
            }

            if (op.getInversePropertyOf() != null) {
                List<OWLAnnotation> annotations = getAxiomAnnotations(op.getInversePropertyOf());
                IRI inv = getIri(op.getInversePropertyOf().getProperty());
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
                ontology.addAxiom(invAx);
            }

            if (op.getDisjointWithProperty() != null) {
                for (PropertyAxiom disjoint : op.getDisjointWithProperty()) {
                    IRI disIri = getIri(disjoint.getProperty());
                    List<OWLAnnotation> annotations = getAxiomAnnotations(disjoint);
                    OWLDisjointObjectPropertiesAxiom disAx;
                    if (annotations != null) {
                        disAx = dataFactory.getOWLDisjointObjectPropertiesAxiom(
                                dataFactory.getOWLObjectProperty(iri),
                                dataFactory.getOWLObjectProperty(disIri),
                                annotations
                        );
                    } else {
                        disAx = dataFactory.getOWLDisjointObjectPropertiesAxiom(
                                dataFactory.getOWLObjectProperty(iri),
                                dataFactory.getOWLObjectProperty(disIri)
                        );
                    }
                    ontology.addAxiom(disAx);
                }

            }

            if (op.getSubPropertyChain() != null) {
                for (SubPropertyChain chain : op.getSubPropertyChain()) {
                    List<OWLAnnotation> annotations = getAxiomAnnotations(chain);
                    OWLSubPropertyChainOfAxiom chnAx;
                    if (annotations != null) {
                        chnAx = dataFactory.getOWLSubPropertyChainOfAxiom(
                                chain.getProperty()
                                        .stream()
                                        .map(c -> dataFactory.getOWLObjectProperty(getIri(c)))
                                        .collect(Collectors.toList()),
                                dataFactory.getOWLObjectProperty(iri),
                                annotations
                        );
                    } else {
                        chnAx = dataFactory.getOWLSubPropertyChainOfAxiom(
                                chain.getProperty()
                                        .stream()
                                        .map(c -> dataFactory.getOWLObjectProperty(getIri(c)))
                                        .collect(Collectors.toList()),
                                dataFactory.getOWLObjectProperty(iri)
                        );
                    }
                    ontology.addAxiom(chnAx);

                }
            }

            if (op.getIsTransitive() != null) {
                List<OWLAnnotation> annotations = getAxiomAnnotations(op.getIsTransitive());
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
                ontology.addAxiom(trnsAx);
            }
            if (op.getIsFunctional() != null) {
                List<OWLAnnotation> annotations = getAxiomAnnotations(op.getIsFunctional());

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
                ontology.addAxiom(fncAx);

            }
            if (op.getIsReflexive() != null) {
                List<OWLAnnotation> annotations = getAxiomAnnotations(op.getIsReflexive());
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
                ontology.addAxiom(rflxAx);

            }
            if (op.getIsSymmetric() != null) {
                List<OWLAnnotation> annotations = getAxiomAnnotations(op.getIsSymmetric());
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
                ontology.addAxiom(symAx);

            }
        }


    }

    private void processDataProperties(OWLOntology ontology, List<DataProperty> dataProperties) {
        if (dataProperties == null || dataProperties.size() == 0)
            return;

        for (DataProperty dp : dataProperties) {
            IRI iri = getIri(dp.getIri());
            OWLDataProperty owlOP = dataFactory.getOWLDataProperty(iri);
            addConceptDeclaration(ontology, owlOP, dp);

            if (dp.getSubDataPropertyOf() != null) {
                for (PropertyAxiom sp : dp.getSubDataPropertyOf()) {
                    List<OWLAnnotation> annotations = getAxiomAnnotations(sp);
                    IRI sub = getIri(sp.getProperty());
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
                    ontology.addAxiom(subAx);
                }

                if (dp.getPropertyRange() != null) {
                    for (ClassAxiom ce : dp.getPropertyRange()) {
                        IRI rng = getIri(ce.getClazz());
                        List<OWLAnnotation> annots = getAxiomAnnotations(ce);
                        OWLDataPropertyRangeAxiom rngAx;
                        if (annots != null) {
                            rngAx = dataFactory.getOWLDataPropertyRangeAxiom(
                                    dataFactory.getOWLDataProperty(iri),
                                    dataFactory.getOWLDatatype(rng),
                                    annots
                            );
                        } else {
                            rngAx = dataFactory.getOWLDataPropertyRangeAxiom(
                                    dataFactory.getOWLDataProperty(iri),
                                    dataFactory.getOWLDatatype(rng)
                            );
                        }
                        ontology.addAxiom(rngAx);
                    }
                }

                if (dp.getPropertyDomain() != null) {
                    for (ClassAxiom ce : dp.getPropertyDomain()) {
                        IRI dom = getIri(ce.getClazz());
                        List<OWLAnnotation> annots = getAxiomAnnotations(ce);

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
                        ontology.addAxiom(domAx);
                    }
                }
            }

            if (dp.getDisjointWithProperty() != null) {
                for (PropertyAxiom disjoint : dp.getDisjointWithProperty()) {
                    IRI disIri = getIri(disjoint.getProperty());
                    List<OWLAnnotation> annotations = getAxiomAnnotations(disjoint);
                    OWLDisjointDataPropertiesAxiom disAx;
                    if (annotations != null) {
                        disAx = dataFactory.getOWLDisjointDataPropertiesAxiom(
                                dataFactory.getOWLDataProperty(iri),
                                dataFactory.getOWLDataProperty(disIri),
                                annotations
                        );
                    } else {

                        disAx = dataFactory.getOWLDisjointDataPropertiesAxiom(
                                dataFactory.getOWLDataProperty(iri),
                                dataFactory.getOWLDataProperty(disIri)
                        );
                    }
                    ontology.addAxiom(disAx);
                }

            }
            if (dp.getIsFunctional() != null) {
                List<OWLAnnotation> annotations = getAxiomAnnotations(dp.getIsFunctional());
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
                ontology.add(fncAx);
            }
        }
    }

    private void processDataTypes(OWLOntology ontology, List<DataType> dataTypes) {
        if (dataTypes == null || dataTypes.size() == 0)
            return;

        for (DataType dt : dataTypes) {
            IRI iri = getIri(dt.getIri());

            if (dt.getName() != null && !dt.getName().isEmpty()) {
                OWLAnnotation label = dataFactory.getOWLAnnotation(
                        dataFactory.getRDFSLabel(),
                        dataFactory.getOWLLiteral(dt.getName())
                );
                ontology.addAxiom(dataFactory.getOWLAnnotationAssertionAxiom(iri, label));
            }

            if (dt.getDescription() != null && !dt.getDescription().isEmpty()) {
                OWLAnnotation comment = dataFactory.getOWLAnnotation(
                        dataFactory.getRDFSComment(),
                        dataFactory.getOWLLiteral(dt.getDescription())
                );
                ontology.addAxiom(dataFactory.getOWLAnnotationAssertionAxiom(iri, comment));
            }

            for (DataTypeDefinition def : dt.getDataTypeDefinition()) {
                List<OWLFacetRestriction> restrictions = def.getDataTypeRestriction()
                        .getFacetRestriction()
                        .stream()
                        .map(f -> dataFactory.getOWLFacetRestriction(
                                OWLFacet.getFacet(getIri(f.getFacet())),
                                dataFactory.getOWLLiteral(f.getConstrainingFacet()))
                        )
                        .collect(Collectors.toList());

                OWLDatatypeDefinitionAxiom defAx = dataFactory.getOWLDatatypeDefinitionAxiom(
                        dataFactory.getOWLDatatype(iri),
                        dataFactory.getOWLDatatypeRestriction(
                                dataFactory.getOWLDatatype(getIri(def.getDataTypeRestriction().getDataType())),
                                restrictions
                        )
                );
                ontology.addAxiom(defAx);
            }
        }
    }

    private IRI getIri(String iri) {
        if (iri.toLowerCase().startsWith("http:") || iri.toLowerCase().startsWith("https:"))
            return IRI.create(iri);
        else
            return prefixManager.getIRI(iri);
    }

    private List<OWLAnnotation> getAxiomAnnotations(ClassAxiom Axiom) {
        if (Axiom.getId() != null || (Axiom.getStatus() != null) || (Axiom.getVersion() != null)) {
            List<OWLAnnotation> annots = new ArrayList();
            if (Axiom.getId() != null) {
                annots.add(dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_ID)),
                        dataFactory.getOWLLiteral(Axiom.getId())
                ));
            }
            if (Axiom.getStatus() != null) {
                annots.add(dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_STATUS)),
                        dataFactory.getOWLLiteral(Axiom.getStatus().getName())
                ));
            }
            if (Axiom.getVersion() != null) {
                annots.add(dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_VERSION)),
                        dataFactory.getOWLLiteral(Axiom.getVersion())
                ));
            }
            return annots;

        }
        return null;
    }

    private List<OWLAnnotation> getAxiomAnnotations(Axiom Axiom) {
        if (Axiom.getId() != null || (Axiom.getStatus() != null) || (Axiom.getVersion() != null)) {
            List<OWLAnnotation> annots = new ArrayList();
            if (Axiom.getId() != null) {
                annots.add(dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_ID)),
                        dataFactory.getOWLLiteral(Axiom.getId())
                ));
            }
            if (Axiom.getStatus() != null) {
                annots.add(dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_STATUS)),
                        dataFactory.getOWLLiteral(Axiom.getStatus().getName())
                ));
            }
            if (Axiom.getVersion() != null) {
                annots.add(dataFactory.getOWLAnnotation(
                        dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_VERSION)),
                        dataFactory.getOWLLiteral(Axiom.getVersion())
                ));
            }
            return annots;

        }
        return null;
    }

    private void processAnnotationProperties(OWLOntology ontology, List<AnnotationProperty> annotationProperties) {
        if (annotationProperties == null || annotationProperties.size() == 0)
            return;

        for (AnnotationProperty ap : annotationProperties) {
            IRI iri = getIri(ap.getIri());
            OWLAnnotationProperty owlAP = dataFactory.getOWLAnnotationProperty(iri);
            addConceptDeclaration(ontology, owlAP, ap);

            if (ap.getSubAnnotationPropertyOf() != null) {
                for (PropertyAxiom subAnnotation : ap.getSubAnnotationPropertyOf()) {
                    IRI sub = getIri(subAnnotation.getProperty());
                    List<OWLAnnotation> annotations = getAxiomAnnotations(subAnnotation);
                    OWLSubAnnotationPropertyOfAxiom subAx;
                    if (annotations != null) {
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
                    ontology.addAxiom(subAx);
                }
            }

            if (ap.getPropertyRange() != null) {
                for (ClassAxiom ce : ap.getPropertyRange()) {
                    List<OWLAnnotation> annotations = getAxiomAnnotations(ce);
                    OWLAnnotationPropertyRangeAxiom prAx;
                    if (annotations != null) {
                        prAx = dataFactory.getOWLAnnotationPropertyRangeAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                getIri(ce.getClazz()),
                                annotations
                        );
                    } else {
                        prAx = dataFactory.getOWLAnnotationPropertyRangeAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                getIri(ce.getClazz())
                        );
                    }
                    ontology.addAxiom(prAx);

                }
            }
        }
    }

}
