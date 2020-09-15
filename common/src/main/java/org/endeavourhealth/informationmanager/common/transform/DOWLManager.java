package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.common.transform.model.Document;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * OWL Manager with the a load ontology method that loads and transforms Discovery JSON into an OWL ontology
 * @since version 1.0
 * @author David Stables Endeavour, Richard Collier Ergonomic systems
 */
public class DOWLManager extends OWLManager {

    /**
     * Loads and saves an OWL2 ontology from/to Discovery syntax
     * @param inputFile file to input data
     * @return OWL2 ontology
     * @throws IOException
     * @throws OWLOntologyCreationException
     * @throws FileFormatException
     */
    public OWLOntology loadOWLFromDiscovery (File inputFile) throws IOException, OWLOntologyCreationException, FileFormatException {
        ObjectMapper objectMapper = new ObjectMapper();
        Document document = objectMapper.readValue(inputFile, Document.class);
        return  new DiscoveryToOWL().transform(document);
    }
    public void saveOWLAsDiscovery(OWLOntology ontology, List<String> filterNamespaces,
                                        File outputFile) throws IOException {
        Document document = new OWLToDiscovery().transform(ontology, filterNamespaces);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(json);
    }

}
