package org.endeavourhealth.informationmanager.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.endeavourhealth.informationmanager.common.transform.DOWLManager;
import org.endeavourhealth.informationmanager.common.transform.DiscoveryToOWL;
import org.endeavourhealth.informationmanager.common.transform.SnomedAssigner;
import org.endeavourhealth.informationmanager.common.transform.OWLToDiscovery;
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
    @FXML private TextArea logger;
    @FXML private CheckBox check;
    @FXML private TextField parentEntity;
    @FXML private TextField namespace;

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
            dmanager.saveOWLAsDiscovery(ontology, filterNamespaces, outputFile);
            log("Done");
            alert("Transform complete", "OWL -> Discovery Transformer", "Transform finished");

            } catch (Exception e) {
              ErrorController.ShowError(_stage, e);
            }

        }

    @FXML
    protected void AssignSnomed(ActionEvent event) {
        System.out.println("IRIs to Snomed");

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
                        new FileChooser.ExtensionFilter("OWL Files", "*.owl")
                );
        File outputFile = outFileChooser.showSaveDialog(_stage);
        if (outputFile == null)
            return;
        String parentIri = parentEntity.getText();
        String extension= namespace.getText();

        if (!parentIri.equals("")) {
            if (!extension.equals("")) {
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
            else {
             alert("Unable to complete","Reason","no snomed namespace included");
            }
        }
        else{
            alert("Unable to complete","Reason","no parent IRI included");
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
            DOWLManager dmanager= new DOWLManager();
            log("Loading JSON and transforming");
            OWLOntology ontology = dmanager.loadOWLFromDiscovery(inputFile);
           /* ObjectMapper objectMapper = new ObjectMapper();

            log("Loading JSON");
            Document document = objectMapper.readValue(inputFile, Document.class);

            log("Transforming");
            OWLOntology ontology = new DiscoveryToOWL().transform(document);
            */


            if (check.isSelected()) {
                log("Checking consistency");
                checkConsistency(ontology);
            } else {
                log("Skipping consistency check");
            }

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
}
