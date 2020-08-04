package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class DiscoveryToOWL {
    private DefaultPrefixManager prefixManager;
    private OWLDataFactory dataFactory;

    public OWLOntology transform(Document document) throws OWLOntologyCreationException, FileFormatException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        if (document == null || document.getInformationModel() == null)
            throw new FileFormatException("Missing InformationModel");

        Ontology ontology = document.getInformationModel();

        String documentIri = null;
        if (ontology.getDocumentInfo() != null)
            documentIri = ontology.getDocumentInfo().getDocumentIri();
        else {
            Optional<Namespace> ns = ontology.getNamespace().stream().filter(n -> n.getPrefix().equals(":")).findFirst();
            if (ns.isPresent())
                documentIri = ns.get().getIri();
        }

        if (documentIri == null)
            throw new FileFormatException("Missing documentInfo/documentIri");

        if (documentIri.endsWith("#"))
            documentIri = documentIri.substring(0, documentIri.length() - 1);

        OWLOntology owlOntology = manager.createOntology(IRI.create(documentIri));
        dataFactory = manager.getOWLDataFactory();

        processPrefixes(owlOntology, ontology.getNamespace());

        processClasses(owlOntology, ontology.getClazz());
        processObjectProperties(owlOntology, ontology.getObjectProperty());
        processDataProperties(owlOntology, ontology.getDataProperty());
        processDataTypes(owlOntology, ontology.getDataType());
        processAnnotationProperties(owlOntology, ontology.getAnnotationProperty());

        return owlOntology;
    }

    private void processPrefixes(OWLOntology owlOntology, List<Namespace> namespace) {
        prefixManager = new DefaultPrefixManager();
        for(Namespace ns: namespace) {
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
        Integer classno=0;

        for (Clazz clazz: clazzes) {
            classno=classno+1;
            IRI iri = getIri(clazz.getIri());
            OWLClass owlClass = dataFactory.getOWLClass(iri);
            addConceptDeclaration(ontology, owlClass, clazz);

            if (clazz.getSubClassOf() != null) {
                for (ClassExpression subclass: clazz.getSubClassOf()) {
                    OWLSubClassOfAxiom subAx = dataFactory.getOWLSubClassOfAxiom(
                        dataFactory.getOWLClass(iri),
                        getClassExpressionAsOWLClassExpression(subclass)
                    );
                    ontology.addAxiom(subAx);
                }
            }

            if (clazz.getEquivalentTo() != null) {
                OWLClass c = dataFactory.getOWLClass(iri);
                List<OWLClassExpression> cex = new ArrayList<>();
                cex.add(c);
                cex.addAll(
                    clazz.getEquivalentTo()
                        .stream()
                        .map(this::getClassExpressionAsOWLClassExpression)
                    .collect(Collectors.toList())
                );
                OWLEquivalentClassesAxiom equivAx = dataFactory.getOWLEquivalentClassesAxiom(cex);
                ontology.addAxiom(equivAx);
            }

            if (clazz.getDisjointWithClass() != null) {
                OWLClass c = dataFactory.getOWLClass(iri);
                List<OWLClassExpression> cex = new ArrayList<>();
                cex.add(c);
                cex.addAll(
                    clazz.getDisjointWithClass()
                        .stream()
                        .map(this::getClassExpressionAsOWLClassExpression)
                        .collect(Collectors.toList())
                );
                OWLDisjointClassesAxiom disAx = dataFactory.getOWLDisjointClassesAxiom(cex);
                ontology.addAxiom(disAx);
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
            }else if (card.getExact() != null) {
                return dataFactory.getOWLObjectExactCardinality(
                        card.getExact(),
                        dataFactory.getOWLObjectProperty(prop),
                        getClassExpressionAsOWLClassExpression(card)
                );
            }
            else if (card.getMin() != null) {
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

        if (concept.getStatus() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_STATUS)),
                dataFactory.getOWLLiteral(concept.getStatus().getName())
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

        for (ObjectProperty op: objectProperties) {
            IRI iri = getIri(op.getIri());
            OWLObjectProperty owlOP = dataFactory.getOWLObjectProperty(iri);
            addConceptDeclaration(ontology, owlOP, op);

            if (op.getSubObjectPropertyOf() != null) {
                for (String sop : op.getSubObjectPropertyOf()) {
                    IRI sub = getIri(sop);
                    OWLSubObjectPropertyOfAxiom subAx = dataFactory.getOWLSubObjectPropertyOfAxiom(
                        dataFactory.getOWLObjectProperty(iri),
                        dataFactory.getOWLObjectProperty(sub)
                    );
                    ontology.addAxiom(subAx);
                }
            }

            if (op.getPropertyDomain() != null) {
                if (op.getPropertyDomain().getClazz() != null) {
                    IRI dom = getIri(op.getPropertyDomain().getClazz());
                    OWLObjectPropertyDomainAxiom domAx = dataFactory.getOWLObjectPropertyDomainAxiom(
                        dataFactory.getOWLObjectProperty(iri),
                        dataFactory.getOWLClass(dom)
                    );
                    ontology.addAxiom(domAx);
                } else if (op.getPropertyDomain().getUnion() != null) {
                    OWLObjectPropertyDomainAxiom domAx = dataFactory.getOWLObjectPropertyDomainAxiom(
                        dataFactory.getOWLObjectProperty(iri),
                        dataFactory.getOWLObjectUnionOf(
                            op.getPropertyDomain().getUnion()
                                .stream()
                                .map(this::getClassExpressionAsOWLClassExpression)
                        )
                    );
                    ontology.addAxiom(domAx);
                }
            }

            if (op.getPropertyRange() != null) {
                IRI rng = getIri(op.getPropertyRange().getClazz());
                OWLObjectPropertyRangeAxiom rngAx = dataFactory.getOWLObjectPropertyRangeAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    dataFactory.getOWLClass(rng)
                );
                ontology.addAxiom(rngAx);
            }

            if (op.getInversePropertyOf() != null) {
                IRI inv = getIri(op.getInversePropertyOf().getProperty());
                OWLInverseObjectPropertiesAxiom invAx = dataFactory.getOWLInverseObjectPropertiesAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    dataFactory.getOWLObjectProperty(inv)
                );
                ontology.addAxiom(invAx);
            }

            if (op.getDisjointWithProperty() != null) {
                OWLDisjointClassesAxiom disAx = dataFactory.getOWLDisjointClassesAxiom(
                    op.getDisjointWithProperty()
                        .stream()
                        .map(c -> dataFactory.getOWLClass(c.getProperty()))
                );
                ontology.addAxiom(disAx);
            }

            if (op.getSubPropertyChain() != null) {
                for(SubPropertyChain chain : op.getSubPropertyChain()) {
                    OWLSubPropertyChainOfAxiom chnAx = dataFactory.getOWLSubPropertyChainOfAxiom(
                        chain.getProperty()
                            .stream()
                            .map(c -> dataFactory.getOWLObjectProperty(getIri(c)))
                            .collect(Collectors.toList()),
                        dataFactory.getOWLObjectProperty(iri)
                    );

                    ontology.addAxiom(chnAx);
                }
            }

            if (op.getTransitive() != null && op.getTransitive()) {
                OWLTransitiveObjectPropertyAxiom trnsAx = dataFactory.getOWLTransitiveObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );
                ontology.addAxiom(trnsAx);
            }

            if (op.getReflexive() != null && op.getReflexive()) {
                OWLReflexiveObjectPropertyAxiom rflxAx = dataFactory.getOWLReflexiveObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );
                ontology.addAxiom(rflxAx);
            }

            if (op.getFunctional() != null && op.getFunctional()) {
                OWLFunctionalObjectPropertyAxiom fncAx = dataFactory.getOWLFunctionalObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );
                ontology.addAxiom(fncAx);
            }

            if (op.getSymmetric() != null && op.getSymmetric()) {
                OWLSymmetricObjectPropertyAxiom symAx = dataFactory.getOWLSymmetricObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );
                ontology.addAxiom(symAx);
            }
        }
    }

    private void processDataProperties(OWLOntology ontology, List<DataProperty> dataProperties) {
        if (dataProperties == null || dataProperties.size() == 0)
            return;

        for (DataProperty dp: dataProperties) {
            IRI iri = getIri(dp.getIri());
            OWLDataProperty owlOP = dataFactory.getOWLDataProperty(iri);
            addConceptDeclaration(ontology, owlOP, dp);

            if (dp.getSubDataPropertyOf() != null) {
                IRI sub = getIri(dp.getSubDataPropertyOf().getProperty());
                OWLSubDataPropertyOfAxiom subAx = dataFactory.getOWLSubDataPropertyOfAxiom(
                    dataFactory.getOWLDataProperty(iri),
                    dataFactory.getOWLDataProperty(sub)
                );
                ontology.addAxiom(subAx);
            }

            if (dp.getPropertyRange() != null) {
                IRI rng = getIri(dp.getPropertyRange().getClazz());
                OWLDataPropertyRangeAxiom rngAx = dataFactory.getOWLDataPropertyRangeAxiom(
                    dataFactory.getOWLDataProperty(iri),
                    dataFactory.getOWLDatatype(rng)
                );
                ontology.addAxiom(rngAx);
            }

            if (dp.getPropertyDomain() != null) {
                IRI dom = getIri(dp.getPropertyDomain().getClazz());
                OWLDataPropertyDomainAxiom domAx = dataFactory.getOWLDataPropertyDomainAxiom(
                    dataFactory.getOWLDataProperty(iri),
                    dataFactory.getOWLClass(dom)
                );
                ontology.addAxiom(domAx);
            }

            if (dp.getDisjointWithProperty() != null) {
                OWLDisjointClassesAxiom disAx = dataFactory.getOWLDisjointClassesAxiom(
                    dp.getDisjointWithProperty()
                        .stream()
                        .map(c -> dataFactory.getOWLClass(c.getProperty()))
                );
                ontology.addAxiom(disAx);
            }

            if (dp.getFunctional() != null && dp.getFunctional()) {
                OWLFunctionalDataPropertyAxiom fncAx = dataFactory.getOWLFunctionalDataPropertyAxiom(
                    dataFactory.getOWLDataProperty(iri)
                );
                ontology.addAxiom(fncAx);
            }
        }
    }

    private void processDataTypes(OWLOntology ontology, List<DataType> dataTypes) {
        if (dataTypes == null || dataTypes.size() == 0)
            return;

        for (DataType dt: dataTypes) {
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

            for (DataTypeDefinition def: dt.getDataTypeDefinition()) {
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

    private void processAnnotationProperties(OWLOntology ontology, List<AnnotationProperty> annotationProperties) {
        if (annotationProperties == null || annotationProperties.size() == 0)
            return;

        for (AnnotationProperty ap: annotationProperties) {
            IRI iri = getIri(ap.getIri());
            OWLAnnotationProperty owlAP = dataFactory.getOWLAnnotationProperty(iri);
            addConceptDeclaration(ontology, owlAP, ap);

            if (ap.getSubAnnotationPropertyOf() != null) {
                for (String subAnnotation: ap.getSubAnnotationPropertyOf()) {
                    IRI sub = getIri(subAnnotation);
                    OWLSubAnnotationPropertyOfAxiom subAx = dataFactory.getOWLSubAnnotationPropertyOfAxiom(
                        dataFactory.getOWLAnnotationProperty(iri),
                        dataFactory.getOWLAnnotationProperty(sub)
                    );
                    ontology.addAxiom(subAx);
                }
            }

            if (ap.getPropertyRange() != null) {
                OWLAnnotationPropertyRangeAxiom prAx = dataFactory.getOWLAnnotationPropertyRangeAxiom(
                    dataFactory.getOWLAnnotationProperty(iri),
                    getIri(ap.getPropertyRange().getClazz())
                );
                ontology.addAxiom(prAx);
            }
        }
    }

    private IRI getIri(String iri) {
        if (iri.toLowerCase().startsWith("http:") || iri.toLowerCase().startsWith("https:"))
            return IRI.create(iri);
        else
            return prefixManager.getIRI(iri);
    }
}
