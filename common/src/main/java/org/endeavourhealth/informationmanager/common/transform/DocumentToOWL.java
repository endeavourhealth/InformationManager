package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.transform.model.Clazz;
import org.endeavourhealth.informationmanager.common.transform.model.Namespace;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDeclarationAxiomImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DocumentToOWL {
    public OWLOntology transform(Ontology document) throws OWLOntologyCreationException, IOException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology owlOntology = manager.createOntology();
        DefaultPrefixManager prefixManager = new DefaultPrefixManager();

        processPrefixes(prefixManager, document.getNamespace());
        processConcepts(owlOntology, prefixManager, document.getClazz());

        return owlOntology;
    }

    private void processPrefixes(DefaultPrefixManager prefixManager, List<Namespace> namespace) {
        for(Namespace ns: namespace) {
            prefixManager.setPrefix(ns.getPrefix(), ns.getIri());
        }
    }

    private void processConcepts(OWLOntology ontology, DefaultPrefixManager prefixManager, List<Clazz> clazzes) {
        for (Clazz clazz: clazzes) {
            OWLClass owlClass = new OWLClassImpl(
                prefixManager.getIRI(clazz.getIri())
            );

            List<OWLAnnotation> annotations = new ArrayList<>();

            OWLDeclarationAxiomImpl declaration = new OWLDeclarationAxiomImpl(owlClass, annotations);

            ontology.addAxiom(declaration);
        }
    }
}
