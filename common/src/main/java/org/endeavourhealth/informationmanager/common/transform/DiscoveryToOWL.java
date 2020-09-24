package org.endeavourhealth.informationmanager.common.transform;

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

/** Converts Discovery JSON syntax document to OWL functional syntax
 *
 */
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
        processIndividuals(owlOntology,ontology.getIndividual());

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
        OWLDocumentFormat ontologyFormat = new FunctionalSyntaxDocumentFormat();
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
                            new HashSet<>(ans)
                        );
                    }
                    else {
                        subAx = dataFactory.getOWLSubClassOfAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(subclass)
                        );
                    }
                    ontology.getAxioms().add(subAx);
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
                                new HashSet<>(ans)
                        );

                    }
                    else {
                        equAx = dataFactory.getOWLEquivalentClassesAxiom(
                                dataFactory.getOWLClass(iri),
                                getClassExpressionAsOWLClassExpression(equiclass)
                        );
                    }
                    ontology.getAxioms().add(equAx);

                }
            }
            if (clazz.getDisjointWithClass() != null) {
                Set<OWLClass> cex = new HashSet<>();
                cex.add(dataFactory.getOWLClass(getIri(clazz.getIri())));
                for (String disjoint : clazz.getDisjointWithClass()) {
                    IRI disIri = getIri(disjoint);
                    cex.add(dataFactory.getOWLClass(disIri));
                }
                OWLDisjointClassesAxiom disax = dataFactory.getOWLDisjointClassesAxiom(cex);
                ontology.getAxioms().add(disax);
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
                            .collect(Collectors.toSet())
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
                    return dataFactory.getOWLClass("unknown quantification", prefixManager);
                }
            } else if (card.getExact() != null) {
                return dataFactory.getOWLObjectExactCardinality(
                        card.getExact(),
                        dataFactory.getOWLObjectProperty(prop),
                        getClassExpressionAsOWLClassExpression(card)
                );
            } else if (card.getMin() != null && card.getMax() != null) {

                return dataFactory.getOWLObjectIntersectionOf(
                    new HashSet<>(Arrays.asList(
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
                    ))
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
            } else if (card.getQuantification()!=null){
                return dataFactory.getOWLDataSomeValuesFrom(
                        dataFactory.getOWLDataProperty(prop),
                        getOWLDataRange(card)
                );
            }
            else {
                System.err.println(card);
                return dataFactory.getOWLClass("unknown DPE cardinality", prefixManager);
            }
        } else if (cex.getUnion() != null) {
            return dataFactory.getOWLObjectUnionOf(
                cex.getUnion()
                    .stream()
                    .map(this::getClassExpressionAsOWLClassExpression)
                    .collect(Collectors.toSet())
            );
        } else {
            System.err.println(cex);
            return dataFactory.getOWLClass("unknown cex", prefixManager);
        }
    }

    private OWLDataRange getOWLDataRange(DataRange dr){
        if (dr.getOneOf()!=null){
            Set<OWLLiteral> literals = new HashSet<>();
            for (String v: dr.getOneOf()){
                OWLLiteral literal = new OWLLiteralImpl
                        (v,null,
                                dataFactory.getOWLDatatype(getIri(dr.getDataType())));
                literals.add(literal);
            }
            OWLDataOneOf oneOf = new OWLDataOneOfImpl(literals);
            return oneOf;
        }
        else
         return dataFactory.getOWLDatatype(getIri(dr.getDataType()));
    }

    private void addConceptDeclaration(OWLOntology ontology, OWLEntity owlClass, Concept concept) {
        OWLDeclarationAxiom declaration = dataFactory.getOWLDeclarationAxiom(owlClass);
        ontology.getAxioms().add(declaration);

        if (concept.getName() != null && !concept.getName().isEmpty()) {
            OWLAnnotation label = dataFactory.getOWLAnnotation(
                    dataFactory.getRDFSLabel(),
                    dataFactory.getOWLLiteral(concept.getName())
            );
            ontology.getAxioms().add(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), label));
        }

        if (concept.getDescription() != null && !concept.getDescription().isEmpty()) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getRDFSComment(),
                    dataFactory.getOWLLiteral(concept.getDescription())
            );
            ontology.getAxioms().add(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }
        if (concept.getId() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_ID)),
                    dataFactory.getOWLLiteral(concept.getId())
            );
            ontology.getAxioms().add(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }

        if (concept.getStatus() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_STATUS)),
                    dataFactory.getOWLLiteral(concept.getStatus().getName())
            );
            ontology.getAxioms().add(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }
        if (concept.getVersion() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_VERSION)),
                    dataFactory.getOWLLiteral(concept.getVersion())
            );
            ontology.getAxioms().add(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }

        if (concept.getCode() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_CODE)),
                    dataFactory.getOWLLiteral(concept.getCode())
            );
            ontology.getAxioms().add(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
        }

        if (concept.getScheme() != null) {
            OWLAnnotation comment = dataFactory.getOWLAnnotation(
                    dataFactory.getOWLAnnotationProperty(getIri(Common.HAS_SCHEME)),
                    dataFactory.getOWLLiteral(concept.getScheme())
            );
            ontology.getAxioms().add(dataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), comment));
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
                    Set<OWLAnnotation> axiomAnnots = new HashSet<>(getAxiomAnnotations(sop));
                    IRI sub = getIri(sop.getProperty());
                    OWLSubObjectPropertyOfAxiom subAx;
                    if (!axiomAnnots.isEmpty()) {
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
                    ontology.getAxioms().add(subAx);
                }
            }

            if (op.getPropertyDomain() != null) {
                for (ClassAxiom ce : op.getPropertyDomain()) {
                    if (ce.getClazz() != null) {
                        IRI dom = getIri(ce.getClazz());
                        Set<OWLAnnotation> ans = new HashSet<>(getAxiomAnnotations(ce));
                        OWLObjectPropertyDomainAxiom domAx;
                        if (!ans.isEmpty()) {
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
                        ontology.getAxioms().add(domAx);

                    } else if (ce.getUnion() != null) {
                        OWLObjectPropertyDomainAxiom domAx = dataFactory.getOWLObjectPropertyDomainAxiom(
                                dataFactory.getOWLObjectProperty(iri),
                                dataFactory.getOWLObjectUnionOf(
                                        ce.getUnion()
                                                .stream()
                                                .map(this::getClassExpressionAsOWLClassExpression)
                                    .collect(Collectors.toSet())
                                )
                        );
                        ontology.getAxioms().add(domAx);
                    }
                }
            }

            if (op.getPropertyRange() != null) {
                for (ClassAxiom ce : op.getPropertyRange()) {
                    IRI rng = getIri(ce.getClazz());
                    Set<OWLAnnotation> ans = new HashSet<>(getAxiomAnnotations(ce));
                    OWLObjectPropertyRangeAxiom rngAx;
                    if (!ans.isEmpty()) {
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
                    ontology.getAxioms().add(rngAx);
                }
            }

            if (op.getInversePropertyOf() != null) {
                Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(op.getInversePropertyOf()));
                IRI inv = getIri(op.getInversePropertyOf().getProperty());
                OWLInverseObjectPropertiesAxiom invAx;
                if (!annotations.isEmpty()) {
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
                ontology.getAxioms().add(invAx);
            }



            if (op.getSubPropertyChain() != null) {
                for (SubPropertyChain chain : op.getSubPropertyChain()) {
                    Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(chain));
                    OWLSubPropertyChainOfAxiom chnAx;
                    if (!annotations.isEmpty()) {
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
                    ontology.getAxioms().add(chnAx);

                }
            }

            if (op.getIsTransitive() != null) {
                Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(op.getIsTransitive()));
                OWLTransitiveObjectPropertyAxiom trnsAx;
                if (!annotations.isEmpty()) {
                    trnsAx = dataFactory.getOWLTransitiveObjectPropertyAxiom(
                            dataFactory.getOWLObjectProperty(iri),
                            annotations
                    );
                } else {
                    trnsAx = dataFactory.getOWLTransitiveObjectPropertyAxiom(
                            dataFactory.getOWLObjectProperty(iri)
                    );
                }
                ontology.getAxioms().add(trnsAx);
            }
            if (op.getIsFunctional() != null) {
                Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(op.getIsFunctional()));

                OWLFunctionalObjectPropertyAxiom fncAx;
                if (!annotations.isEmpty()) {
                    fncAx = dataFactory.getOWLFunctionalObjectPropertyAxiom(
                            dataFactory.getOWLObjectProperty(iri),
                            annotations
                    );
                } else {
                    fncAx = dataFactory.getOWLFunctionalObjectPropertyAxiom(
                            dataFactory.getOWLObjectProperty(iri)
                    );
                }
                ontology.getAxioms().add(fncAx);

            }
            if (op.getIsReflexive() != null) {
                Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(op.getIsReflexive()));
                OWLReflexiveObjectPropertyAxiom rflxAx;
                if (!annotations.isEmpty()) {
                    rflxAx = dataFactory.getOWLReflexiveObjectPropertyAxiom(
                            dataFactory.getOWLObjectProperty(iri),
                            annotations
                    );
                } else {
                    rflxAx = dataFactory.getOWLReflexiveObjectPropertyAxiom(
                            dataFactory.getOWLObjectProperty(iri)
                    );

                }
                ontology.getAxioms().add(rflxAx);

            }
            if (op.getIsSymmetric() != null) {
                Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(op.getIsSymmetric()));
                OWLSymmetricObjectPropertyAxiom symAx;
                if (!annotations.isEmpty()) {
                    symAx = dataFactory.getOWLSymmetricObjectPropertyAxiom(
                            dataFactory.getOWLObjectProperty(iri),
                            annotations
                    );
                } else {
                    symAx = dataFactory.getOWLSymmetricObjectPropertyAxiom(
                            dataFactory.getOWLObjectProperty(iri)
                    );
                }
                ontology.getAxioms().add(symAx);

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
                    Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(sp));
                    IRI sub = getIri(sp.getProperty());
                    OWLSubDataPropertyOfAxiom subAx;
                    if (!annotations.isEmpty()) {
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
                    ontology.getAxioms().add(subAx);
                }
            }

            if (dp.getPropertyRange() != null) {
                for (PropertyRangeAxiom pr : dp.getPropertyRange()) {
                    List<OWLAnnotation> annots = getAxiomAnnotations(pr);
                    OWLDataPropertyRangeAxiom rngAx =
                            getPropertyRangeAxiom(pr, iri, annots);
                    ontology.getAxioms().add(rngAx);
                }
            }

            if (dp.getPropertyDomain() != null) {
                    for (ClassAxiom ce : dp.getPropertyDomain()) {
                        IRI dom = getIri(ce.getClazz());
                        Set<OWLAnnotation> annots = new HashSet<>(getAxiomAnnotations(ce));

                        OWLDataPropertyDomainAxiom domAx;
                        if (!annots.isEmpty()) {
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
                        ontology.getAxioms().add(domAx);
                    }
                }

            if (dp.getDisjointWithProperty() != null) {
                for (PropertyAxiom disjoint : dp.getDisjointWithProperty()) {
                    IRI disIri = getIri(disjoint.getProperty());
                    Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(disjoint));
                    OWLDisjointDataPropertiesAxiom disAx;
                    if (!annotations.isEmpty()) {
                        disAx = dataFactory.getOWLDisjointDataPropertiesAxiom(
                            new HashSet<>(Arrays.asList(
                                dataFactory.getOWLDataProperty(iri),
                                dataFactory.getOWLDataProperty(disIri)
                            )),
                                annotations
                        );
                    } else {

                        disAx = dataFactory.getOWLDisjointDataPropertiesAxiom(
                                dataFactory.getOWLDataProperty(iri),
                                dataFactory.getOWLDataProperty(disIri)
                        );
                    }
                    ontology.getAxioms().add(disAx);
                }

            }
            if (dp.getIsFunctional() != null) {
                Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(dp.getIsFunctional()));
                OWLFunctionalDataPropertyAxiom fncAx;
                if (!annotations.isEmpty()) {

                    fncAx = dataFactory.getOWLFunctionalDataPropertyAxiom(
                            dataFactory.getOWLDataProperty(iri),
                            annotations
                    );
                } else {

                    fncAx = dataFactory.getOWLFunctionalDataPropertyAxiom(
                            dataFactory.getOWLDataProperty(iri)
                    );
                }
                ontology.getAxioms().add(fncAx);
            }
        }
    }
    private void processIndividuals(OWLOntology ontology, List<Individual> individuals) {
        if (individuals!=null) {
            for (Individual ind : individuals) {
                IRI iri = getIri(ind.getIri());
                //Create named individual
                OWLNamedIndividual owlNamed = dataFactory.getOWLNamedIndividual(iri);
                addConceptDeclaration(ontology,owlNamed,ind);


                //Add data property axioms
                if (ind.getPropertyDataValue() != null) {
                    for (DataPropertyAssertionAxiom dv : ind.getPropertyDataValue()) {
                        Set<OWLAnnotation> annots = new HashSet<>(getAxiomAnnotations(ind));
                        OWLDataPropertyAssertionAxiom dpax;
                        OWLLiteral literal = dataFactory.getOWLLiteral(dv.getValue()
                                        ,dataFactory.getOWLDatatype(getIri(dv.getDataType())));
                        if (!annots.isEmpty()) {
                            dpax = dataFactory.getOWLDataPropertyAssertionAxiom(
                                    dataFactory.getOWLDataProperty(getIri(dv.getProperty())),
                                    dataFactory.getOWLNamedIndividual(iri),
                                    literal,
                                    annots
                            );
                        }
                        else {
                            dpax = dataFactory.getOWLDataPropertyAssertionAxiom(
                                    dataFactory.getOWLDataProperty(getIri(dv.getProperty())),
                                    dataFactory.getOWLNamedIndividual(iri),
                                    literal
                            );

                        }
                        ontology.getAxioms().add(dpax);
                    }

                }
                if (ind.getIsType()!=null){
                    Set<OWLAnnotation> annots = new HashSet<>(getAxiomAnnotations(ind));
                    OWLClassAssertionAxiom assax;
                    if (!annots.isEmpty()){
                        assax= dataFactory.getOWLClassAssertionAxiom(
                                dataFactory.getOWLClass(getIri(ind.getIsType())),
                                dataFactory.getOWLNamedIndividual(iri),
                                annots
                        );
                    }
                    else {
                        assax= dataFactory.getOWLClassAssertionAxiom(
                                dataFactory.getOWLClass(getIri(ind.getIsType())),
                                dataFactory.getOWLNamedIndividual(iri)
                        );
                    }
                    ontology.getAxioms().add(assax);
                }
            }

        }
    }

    private OWLDataPropertyRangeAxiom getPropertyRangeAxiom
            ( PropertyRangeAxiom pr,
              IRI iri,
              List<OWLAnnotation> annots) {
        OWLDataRange owlr = getOWLDataRange(pr);
        OWLDataPropertyRangeAxiom rngAx;
        if (annots != null) {
            rngAx = dataFactory.getOWLDataPropertyRangeAxiom(
                        dataFactory.getOWLDataProperty(iri),
                        owlr,
                        new HashSet<>(annots)
                );


        } else {
                rngAx = dataFactory.getOWLDataPropertyRangeAxiom(
                        dataFactory.getOWLDataProperty(iri),
                        owlr
                );
        }
        return rngAx;
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
                ontology.getAxioms().add(dataFactory.getOWLAnnotationAssertionAxiom(iri, label));
            }

            if (dt.getDescription() != null && !dt.getDescription().isEmpty()) {
                OWLAnnotation comment = dataFactory.getOWLAnnotation(
                        dataFactory.getRDFSComment(),
                        dataFactory.getOWLLiteral(dt.getDescription())
                );
                ontology.getAxioms().add(dataFactory.getOWLAnnotationAssertionAxiom(iri, comment));
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
                                dataFactory.getOWLDatatype(getIri(def.getDataTypeRestriction().getDataType())),
                                restrictions
                        )
                );
                ontology.getAxioms().add(defAx);
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
        return getOwlAnnotations(Axiom.getId(), Axiom.getStatus(), Axiom.getVersion(), Axiom);
    }

    private List<OWLAnnotation> getOwlAnnotations
            (String id, ConceptStatus status, Integer version, IMEntity Axiom) {
        if (id != null || (status != null) || (version != null)) {
            List<OWLAnnotation> annots = new ArrayList();
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

    private List<OWLAnnotation> getAxiomAnnotations(IMEntity Axiom) {
        return getOwlAnnotations(Axiom.getId(), Axiom.getStatus(), Axiom.getVersion(), Axiom);
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
                    Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(subAnnotation));
                    OWLSubAnnotationPropertyOfAxiom subAx;
                    if (!annotations.isEmpty()) {
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
                    ontology.getAxioms().add(subAx);
                }
            }

            if (ap.getPropertyRange() != null) {
                for (AnnotationPropertyRangeAxiom arax : ap.getPropertyRange()) {
                    Set<OWLAnnotation> annotations = new HashSet<>(getAxiomAnnotations(arax));
                    OWLAnnotationPropertyRangeAxiom prAx;
                    if (!annotations.isEmpty()) {
                        prAx = dataFactory.getOWLAnnotationPropertyRangeAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                getIri(arax.getIri()),
                                annotations
                        );
                    } else {
                        prAx = dataFactory.getOWLAnnotationPropertyRangeAxiom(
                                dataFactory.getOWLAnnotationProperty(iri),
                                getIri(arax.getIri())
                        );
                    }
                    ontology.getAxioms().add(prAx);

                }
            }
        }
    }

}
