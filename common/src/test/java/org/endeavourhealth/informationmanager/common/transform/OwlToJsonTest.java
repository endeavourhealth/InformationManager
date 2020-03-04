package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import static org.junit.jupiter.api.Assertions.*;

class OwlToJsonTest {

    @org.junit.jupiter.api.Test
    void transform() throws OWLOntologyCreationException, OWLOntologyStorageException, JsonProcessingException {
        new OwlToJson().transform();
    }
}
