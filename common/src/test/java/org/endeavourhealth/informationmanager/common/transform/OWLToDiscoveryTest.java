package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class OWLToDiscoveryTest {

    @Test
    void transform() throws OWLOntologyCreationException, IOException {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntology(IRI.create(new File("IMCore.owl")));

        Ontology document = new OWLToDiscovery().transform(ontology);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("IMCoreFunc.json"))) {
            writer.write(json);
        }
    }
}
