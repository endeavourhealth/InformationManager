package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.transform.model.Concept;
import org.endeavourhealth.informationmanager.common.transform.model.Document;
import org.endeavourhealth.informationmanager.common.transform.model.InformationModel;
import org.endeavourhealth.informationmanager.common.transform.model.Namespace;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDeclarationAxiomImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DocumentToOWL {
    public OWLOntology transform(Document document) throws OWLOntologyCreationException, IOException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.createOntology();
        DefaultPrefixManager prefixManager = new DefaultPrefixManager();

        InformationModel im = document.getInformationModel();

        processPrefixes(prefixManager, im.getNamespace());
        processConcepts(ontology, prefixManager, im.getConcept());

        return ontology;
    }

    private void processPrefixes(DefaultPrefixManager prefixManager, List<Namespace> namespace) {
        for(Namespace ns: namespace) {
            prefixManager.setPrefix(ns.getPrefix(), ns.getIri());
        }
    }

    private void processConcepts(OWLOntology ontology, DefaultPrefixManager prefixManager, List<Concept> concepts) {
        for (Concept concept: concepts) {
            OWLClass owlClass = new OWLClassImpl(
                prefixManager.getIRI(concept.getIri())
            );

            List<OWLAnnotation> annotations = new ArrayList<>();

            OWLDeclarationAxiomImpl declaration = new OWLDeclarationAxiomImpl(owlClass, annotations);

            ontology.addAxiom(declaration);
        }
    }
}
