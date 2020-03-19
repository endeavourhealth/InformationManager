package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import uk.ac.manchester.cs.owl.owlapi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class DiscoveryToOWL {
    private DefaultPrefixManager prefixManager;
    private OWLDataFactory dataFactory;

    public OWLOntology transform(Ontology document) throws OWLOntologyCreationException, IOException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology owlOntology = manager.createOntology(IRI.create(document.getDocumentInfo().getDocumentId()));
        dataFactory = manager.getOWLDataFactory();

        processPrefixes(owlOntology, document.getNamespace());

        processClasses(owlOntology, document.getClazz());
        processObjectProperties(owlOntology, document.getObjectProperty());
        processDataProperties(owlOntology, document.getDataProperty());
        processDataTypes(owlOntology, document.getDataType());
        processAnnotationProperties(owlOntology, document.getAnnotationProperty());

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
        for (Clazz clazz: clazzes) {
            IRI iri = getIri(clazz.getIri());
            OWLClass owlClass = dataFactory.getOWLClass(iri);
            addConceptDeclaration(ontology, owlClass, clazz);

            if (clazz.getSubClassOf() != null) {
                OWLSubClassOfAxiom subAx = dataFactory.getOWLSubClassOfAxiom(
                    dataFactory.getOWLClass(iri),
                    getClassExpressionAsOWLClassExpression(clazz.getSubClassOf())
                );
                ontology.addAxiom(subAx);
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
        } else if (cex.getObjectSome() != null) {
            IRI some = getIri(cex.getObjectSome().getProperty());
            return dataFactory.getOWLObjectSomeValuesFrom(
                dataFactory.getOWLObjectProperty(some),
                getClassExpressionAsOWLClassExpression(cex.getObjectSome())
            );
        } else if (cex.getObjectCardinality() != null) {
            IRI prop = getIri(cex.getObjectCardinality().getProperty());
            OPECardinalityRestriction card = cex.getObjectCardinality();
            if (card.getExact() != null) {
                return dataFactory.getOWLObjectExactCardinality(
                    card.getExact(),
                    dataFactory.getOWLObjectProperty(prop),
                    getClassExpressionAsOWLClassExpression(card)
                );
            } else if (card.getMin() != null) {
                return dataFactory.getOWLObjectMinCardinality(
                    card.getMin(),
                    dataFactory.getOWLObjectProperty(prop),
                    getClassExpressionAsOWLClassExpression(card)
                );
            } else if (card.getMax() != null){
                return dataFactory.getOWLObjectMaxCardinality(
                    card.getMax(),
                    dataFactory.getOWLObjectProperty(prop),
                    getClassExpressionAsOWLClassExpression(card)
                );
            } else {
                System.err.println(card);
                return dataFactory.getOWLClass("unknown card");
            }
        } else if (cex.getDataCardinality() != null) {
            IRI prop = getIri(cex.getDataCardinality().getProperty());
            DPECardinalityRestriction card = cex.getDataCardinality();
            if (card.getExact() != null) {
                return dataFactory.getOWLDataExactCardinality(
                    card.getExact(),
                    dataFactory.getOWLDataProperty(prop)
                );
            } else if (card.getMin() != null) {
                return dataFactory.getOWLDataMinCardinality(
                    card.getMin(),
                    dataFactory.getOWLDataProperty(prop)
                );
            } else if (card.getMax() != null) {
                return dataFactory.getOWLDataMaxCardinality(
                    card.getMax(),
                    dataFactory.getOWLDataProperty(prop)
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

    }

    private void processObjectProperties(OWLOntology ontology, List<ObjectProperty> objectProperties) {
        for (ObjectProperty op: objectProperties) {
            IRI iri = getIri(op.getIri());
            OWLObjectProperty owlOP = dataFactory.getOWLObjectProperty(iri);
            addConceptDeclaration(ontology, owlOP, op);

            if (op.getSubObjectPropertyOf() != null) {
                IRI sub = getIri(op.getSubObjectPropertyOf().getProperty());
                OWLSubObjectPropertyOfAxiom subAx = dataFactory.getOWLSubObjectPropertyOfAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    dataFactory.getOWLObjectProperty(sub)
                );
                ontology.addAxiom(subAx);
            }

            if (op.getPropertyDomain() != null) {
                IRI dom = getIri(op.getPropertyDomain().getClazz());
                OWLObjectPropertyDomainAxiom domAx = dataFactory.getOWLObjectPropertyDomainAxiom(
                    dataFactory.getOWLObjectProperty(iri),
                    dataFactory.getOWLClass(dom)
                );
                ontology.addAxiom(domAx);
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

            if (op.getTransitive() != null && op.getTransitive()) {
                OWLTransitiveObjectPropertyAxiom trnsAx = dataFactory.getOWLTransitiveObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );
                ontology.addAxiom(trnsAx);
            }

            if (op.getFunctional() != null && op.getFunctional()) {
                OWLFunctionalObjectPropertyAxiom fncAx = dataFactory.getOWLFunctionalObjectPropertyAxiom(
                    dataFactory.getOWLObjectProperty(iri)
                );
                ontology.addAxiom(fncAx);
            }
        }
    }

    private void processDataProperties(OWLOntology ontology, List<DataProperty> dataProperties) {
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

            if (dp.getFunctional() != null && dp.getFunctional()) {
                OWLFunctionalDataPropertyAxiom fncAx = dataFactory.getOWLFunctionalDataPropertyAxiom(
                    dataFactory.getOWLDataProperty(iri)
                );
                ontology.addAxiom(fncAx);
            }
        }
    }

    private void processDataTypes(OWLOntology ontology, List<DataType> dataTypes) {
        for (DataType dt: dataTypes) {
            IRI iri = getIri(dt.getIri());
            OWLDatatype owlOP = dataFactory.getOWLDatatype(iri);
            addConceptDeclaration(ontology, owlOP, dt);
        }
    }

    private void processAnnotationProperties(OWLOntology ontology, List<AnnotationProperty> annotationProperties) {
        for (AnnotationProperty ap: annotationProperties) {
            IRI iri = getIri(ap.getIri());
            OWLAnnotationProperty owlAP = dataFactory.getOWLAnnotationProperty(iri);
            addConceptDeclaration(ontology, owlAP, ap);

            if (ap.getSubAnnotaionPropertyOf() != null) {
                for (String subAnnotation: ap.getSubAnnotaionPropertyOf()) {
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
