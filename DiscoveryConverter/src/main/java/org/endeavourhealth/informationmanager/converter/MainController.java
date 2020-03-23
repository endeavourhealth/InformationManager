package org.endeavourhealth.informationmanager.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.endeavourhealth.informationmanager.common.transform.DiscoveryToOWL;
import org.endeavourhealth.informationmanager.common.transform.OWLToDiscovery;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class MainController {
    private Stage _stage;
    @FXML private TextArea logger;

    public void setStage(Stage stage) {
        this._stage = stage;
    }

    @FXML
    protected void owlToDiscovery(ActionEvent event) {
        System.out.println("OWL -> Discovery");
        FileChooser inFileChooser = new FileChooser();

        inFileChooser.setTitle("Select input (OWL) file");
        inFileChooser.getExtensionFilters()
            .add(
                new FileChooser.ExtensionFilter("OWL Files", "*.owl")
            );
        File inputFile = inFileChooser.showOpenDialog(_stage);
        if (inputFile == null)
            return;

        FileChooser outFileChooser = new FileChooser();
        outFileChooser.setTitle("Select output (JSON) file");
        outFileChooser.getExtensionFilters()
            .add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
            );
        File outputFile = outFileChooser.showSaveDialog(_stage);
        if (outputFile == null)
            return;

        try {
            clearlog();
            log("Initializing OWL API");
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology ontology = manager.loadOntology(IRI.create(inputFile));

            log("Checking consistency");
            checkConsistency(ontology);

            log("Transforming");
            Ontology document = new OWLToDiscovery().transform(ontology);

            log("Writing output");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(json);
            }
            log("Done");

            alert("Transform complete", "OWL -> Discovery Transformer", "Transform finished");
        } catch (Exception e) {
            alert("Transform error", "OWL -> Discovery Transformer", e.getMessage());
        }
    }

    @FXML
    protected void discoveryToOWL(ActionEvent event) {
        System.out.println("Discovery -> OWL");
        FileChooser inFileChooser = new FileChooser();

        inFileChooser.setTitle("Select input (JSON) file");
        inFileChooser.getExtensionFilters()
            .add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
            );
        File inputFile = inFileChooser.showOpenDialog(_stage);
        if (inputFile == null)
            return;

        FileChooser outFileChooser = new FileChooser();
        outFileChooser.setTitle("Select output (OWL) file");
        outFileChooser.getExtensionFilters()
            .add(
                new FileChooser.ExtensionFilter("OWL Files", "*.owl")
            );
        File outputFile = outFileChooser.showSaveDialog(_stage);
        if (outputFile == null)
            return;

        try {
            clearlog();
            log("Initializing");
            ObjectMapper objectMapper = new ObjectMapper();

            log("Loading JSON");
            Ontology discovery = objectMapper.readValue(inputFile, Ontology.class);

            log("Transforming");
            OWLOntology ontology = new DiscoveryToOWL().transform(discovery);

            log("Checking consistency");
            checkConsistency(ontology);

            log("Writing output");
            OWLDocumentFormat format = new FunctionalSyntaxDocumentFormat();
            format.setAddMissingTypes(false);   // Prevent auto-declaration of "anonymous" classes

            OWLManager
                .createOWLOntologyManager()
                .saveOntology(
                    ontology,
                    format,
                    new FileOutputStream(outputFile)
                );
            log("Done");
            alert("Transform complete", "Discovery -> OWL Transformer", "Transform finished");
        } catch (Exception e) {
            alert("Transform error", "Discovery -> OWL Transformer", e.getMessage());
        }
    }

    private void alert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    private void checkConsistency(OWLOntology ontology) {
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        ConsoleProgressMonitor monitor = new ConsoleProgressMonitor();
        OWLReasonerConfiguration config = new SimpleConfiguration(monitor);
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
        reasoner.precomputeInferences();
        boolean consistent = reasoner.isConsistent();
        if (!consistent) {
            log("Ontology inconsistent!");
        } else {
            log("Ontology is consistent");
        }
    }

    private void clearlog() {
        logger.clear();
    }

    private void log(String message) {
        logger.appendText(message + "\n");
    }
}
