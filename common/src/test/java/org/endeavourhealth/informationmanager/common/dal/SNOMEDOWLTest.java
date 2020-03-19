package org.endeavourhealth.informationmanager.common.dal;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;

class SNOMEDOWLTest {

    @Test
    void loadSnomed() throws OWLOntologyCreationException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        manager.loadOntology(IRI.create(new File("Snomed-CT.owl")));
    }
}
