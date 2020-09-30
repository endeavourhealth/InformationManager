package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.common.transform.model.Document;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * OWL Manager subclass with the a load/generate/ transform /save ontology methods that loads Discovery JSON into OWL syntax or saves from OWL into Discovery
 * <p>Includes saving asserted ontology and inferred views</p>
 * @since version 1.0
 * @author David Stables Endeavour, Richard Collier Ergonomic systems
 */
public class DOWLManager extends OWLManager {

    /**
     * Loads an ontology in Discovery syntax, transforms and returns in OWL2 format
     * @param inputFile file to input data
     * @return OWL2 ontology as an OWL manager containing an optional ontology and Prefix format with prefixes
     * @throws IOException
     * @throws OWLOntologyCreationException
     * @throws FileFormatException
     */
    public OWLOntologyManager loadOWLFromDiscovery (File inputFile) throws IOException, OWLOntologyCreationException, FileFormatException {
        ObjectMapper objectMapper = new ObjectMapper();
        Document document = objectMapper.readValue(inputFile, Document.class);
        return  new DiscoveryToOWL().transform(document);
    }

    /**
     * Transforms an OWL2 ontology into Discovery JSON syntax and saves it
     * @param ontology the OWL 2 ontology
     * @param filterNamespaces  namespaces that should be ommitted from the transform e.g. external classes
     * @param outputFile
     * @throws IOException
     */
    public void saveOWLAsDiscovery(OWLOntologyManager manager , OWLOntology ontology
                                    ,List<String> filterNamespaces, File outputFile) throws IOException {

        OWLDocumentFormat format= manager.getOntologyFormat(ontology);
        Document document = new OWLToDiscovery().transform(ontology, format,filterNamespaces);
        saveDiscovery(document,outputFile);
    }

    /**
     * Generates a Discovery Syntax inferred view from an OWL ontology and saves it
     * @param ontology  the OWL2 ontology
     * @param outputFile
     * @throws IOException
     */
    public void saveOWLAsInferred(OWLOntologyManager manager
                                , OWLOntology ontology,
                                   File outputFile) throws IOException {

        OWLDocumentFormat format= manager.getOntologyFormat(ontology);
        Document document = new OWLToDiscovery().generateInferredView(ontology,format);
        saveDiscovery(document,outputFile);

    }

    /**
     * Saves a Discovery JSON syntax ontology
     * @param document  the Discovery ontology document
     * @param outputFile
     * @throws IOException
     */
    public void saveDiscovery(Document document,
                                  File outputFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(json);
        }
        catch (Exception e) {
            System.err.println("Unable to transform and save ontology in JSON format");
        }
    }

}
