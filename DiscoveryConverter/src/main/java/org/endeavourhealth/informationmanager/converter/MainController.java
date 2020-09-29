package org.endeavourhealth.informationmanager.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.informationmanager.common.transform.model.Document;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;


import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainController {
    private Stage _stage;
    @FXML
    private TextArea logger;
    @FXML
    private CheckBox check;
    @FXML
    private TextField parentEntity;
    @FXML
    private TextField snomedNamespace;
    @FXML
    private TextField namespace;

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

            OWLOntologyLoaderConfiguration loader = new OWLOntologyLoaderConfiguration();
            manager.setOntologyLoaderConfiguration(loader.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
            OWLOntology ontology = manager.loadOntology(IRI.create(inputFile));



            if (check.isSelected()) {
                log("Checking consistency");
                checkConsistency(ontology);
            } else {
                log("Skipping consistency check");
            }

            log("Transforming");
            List<String> filterNamespaces = new ArrayList<>();
            filterNamespaces.add("sn");
            DOWLManager dmanager = new DOWLManager();
            dmanager.saveOWLAsDiscovery(manager, ontology, filterNamespaces, outputFile);
            log("Done");
            alert("Transform complete", "OWL -> Discovery Transformer", "Transform finished");

        } catch (Exception e) {
            ErrorController.ShowError(_stage, e);
        }

    }

    @FXML
    protected void AssignSnomed(ActionEvent event) {
        if (parentEntity.getText().equals("") | (snomedNamespace.getText().equals(""))) {
            alert("Incomplete information", "Missing information", "Please set namespace and parent IRI");
            return;
        }

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
        outFileChooser.setTitle("Select output (OWL) file");
        outFileChooser.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter("OWL Files", "*.owl")
                );
        File outputFile = outFileChooser.showSaveDialog(_stage);
        if (outputFile == null)
            return;
        String parentIri = parentEntity.getText();
        String extension = snomedNamespace.getText();
        try {
            clearlog();
            log("Initializing OWL API");
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntologyLoaderConfiguration loader = new OWLOntologyLoaderConfiguration();
            manager.setOntologyLoaderConfiguration(loader.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
            OWLOntology ontology = manager.loadOntology(IRI.create(inputFile));

            if (check.isSelected()) {
                log("Checking consistency");
                checkConsistency(ontology);
            } else {
                log("Skipping consistency check");
            }

            log("Converting");
            SnomedAssigner converter = new SnomedAssigner(manager, ontology, extension);
            ontology = converter.convert(parentIri);

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
            alert("Conversion complete", "IRIs to Snomed", "Conversion finished");
        } catch (Exception e) {
            ErrorController.ShowError(_stage, e);
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
            DOWLManager dmanager = new DOWLManager();
            log("Loading JSON and transforming");
            OWLOntologyManager owlManager = dmanager.loadOWLFromDiscovery(inputFile);
            Optional<OWLOntology> ontology = owlManager.getOntologies().stream().findFirst();
            if (ontology.isPresent()) {
                if (check.isSelected()) {
                    log("Checking consistency");
                    checkConsistency(ontology.get());
                } else {
                    log("Skipping consistency check");
                }


                log("Writing output");
                OWLDocumentFormat format = new FunctionalSyntaxDocumentFormat();
                format.setAddMissingTypes(false);   // Prevent auto-declaration of "anonymous" classes
                owlManager.saveOntology(ontology.get()
                        , owlManager.getOntologyFormat(ontology.get())
                        ,new FileOutputStream(outputFile)
                );

            } else {
                alert("Process complete", "Discovery-OWL Transformer", "No ontology created");
            }
            log("Done");
            alert("Transform complete", "Discovery -> OWL Transformer", "Transform finished");

        } catch (Exception e) {
            ErrorController.ShowError(_stage, e);
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

    private File getOutputFile(String fileType) {
        FileChooser outFileChooser = new FileChooser();
        outFileChooser.setTitle("Select output (" + fileType + ") file");
        outFileChooser.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(fileType + "Files"
                                , "*." + fileType.toLowerCase())
                );
        return outFileChooser.showSaveDialog(_stage);
    }

    private File getInputFile(String fileType) {
        FileChooser inFileChooser = new FileChooser();

        inFileChooser.setTitle("Select input (" + fileType + ") file");
        inFileChooser.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(fileType + "Files"
                                , "*." + fileType.toLowerCase())
                );
        return inFileChooser.showOpenDialog(_stage);

    }

    public void GenerateInferred(ActionEvent actionEvent) {

        File inputFile = getInputFile("OWL");
        if (inputFile == null)
            return;
        File outputFile = getOutputFile("JSON");
        if (outputFile == null)
            return;

        String parentIri = parentEntity.getText();
        try {
            clearlog();
            log("Initializing OWL API");
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntologyLoaderConfiguration loader = new OWLOntologyLoaderConfiguration();
            manager.setOntologyLoaderConfiguration(loader.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
            OWLOntology ontology = manager.loadOntology(IRI.create(inputFile));
            log("Converting");
            DOWLManager dowlManager = new DOWLManager();
            dowlManager.saveOWLAsInferred(manager,ontology,outputFile);
            log("Done");
            alert("Generation complete", "OWL -> Discovery inferred view", "generated and saved");


        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private void saveJSON(Document document, File outputFile){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(json);
            } catch (Exception e) {
                System.err.println("Unable to save JSON format");

            }
        } catch (Exception e) {
            System.err.println("Unable to save JSON format");
        }
    }
}
