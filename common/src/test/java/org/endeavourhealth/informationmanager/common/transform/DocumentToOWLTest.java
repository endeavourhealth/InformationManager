package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.model.Document;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.File;
import java.io.IOException;

class DocumentToOWLTest {

    @Test
    void transform() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        ObjectMapper objectMapper = new ObjectMapper();
        Document document = objectMapper.readValue(new File("IMCore.json"), Document.class);

        OWLOntology ontology = new DocumentToOWL().transform(document);

        OWLManager
            .createOWLOntologyManager()
            .saveOntology(
                ontology,
                new FunctionalSyntaxDocumentFormat(),
                System.out
            );
    }
}
