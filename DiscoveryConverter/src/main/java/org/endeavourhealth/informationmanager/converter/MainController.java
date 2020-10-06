package org.endeavourhealth.informationmanager.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.informationmanager.transforms.*;
import org.endeavourhealth.informationmanager.common.transform.model.Document;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;


import javax.swing.*;
import java.io.*;
import java.util.*;

public class MainController {
    private Properties config;
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
    private TextField snomedInput;
    @FXML
    private TextField snomedOutput;

    public void setStage(Stage stage) {
        loadConfig();
        this._stage = stage;
    }

    private void loadConfig() {
        config = new Properties();
        try {
            FileInputStream cs = new FileInputStream("DiscoveryConverter.cfg");
            config.load(cs);
            String si = config.getProperty("snomedInputFolder");
            String so = config.getProperty("snomedOutputFolder");
            String ns = config.getProperty("snomedNamespace");
            String pe = config.getProperty("parentEntity");
            snomedInput.setText((si != null) ? si : "");
            snomedOutput.setText((so != null) ? so : "");
            snomedNamespace.setText((ns != null) ? ns : "1000252");
            parentEntity.setText((pe!=null) ? pe:"");


        } catch (FileNotFoundException nf) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveConfig(){

            config= new Properties();
            try {
                FileOutputStream cs = new FileOutputStream("DiscoveryConverter.cfg");
                config.setProperty("snomedInputFolder",snomedInput.getText());
                config.setProperty("snomedOutputFolder",snomedOutput.getText());
                config.setProperty("snomedNamespace",snomedNamespace.getText());
                config.setProperty("parentEntity",parentEntity.getText());
                config.store(cs,"saved configuration");

            } catch (FileNotFoundException nf) {

            } catch (IOException e) {
                e.printStackTrace();
            }


    }
    @FXML
    protected void owlToDiscovery(ActionEvent event) {
        saveConfig();
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
    protected void assignSnomed(ActionEvent event) {
        saveConfig();
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
        saveConfig();
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
            Set<OWLOntology> ontologies = owlManager.getOntologies();
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
        logger.redo();
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

    public void generateInferred(ActionEvent actionEvent) {

        saveConfig();
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


    public void indexOntology(ActionEvent actionEvent) {
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
        try {
            clearlog();
            log("Initializing");
            DOWLManager dmanager = new DOWLManager();
            log("Loading JSON and indexing");
            Ontology ontology = dmanager.loadFromDiscovery(inputFile);
            dmanager.addOntology(ontology);
            dmanager.rebuildIndexes();
            log("Done");
            alert("Action complete", "Discovery Indexing", "Index complete");

        } catch (Exception e) {
            ErrorController.ShowError(_stage, e);
        }
    }

    public void setSnomedInput(ActionEvent actionEvent) {

        DirectoryChooser chooser= new DirectoryChooser();
        chooser.setTitle("Select Snomed input folder");
        File current = new File(snomedInput.getText());
        if (current.exists())
             chooser.setInitialDirectory(new File(snomedInput.getText()));
        File of = chooser.showDialog(_stage);
        if (of!=null)
            snomedInput.setText(of.toString());

    }

    public void setSnomedOutput(ActionEvent actionEvent) {
        DirectoryChooser chooser= new DirectoryChooser();
        chooser.setTitle("Select Snomed output folder");
        File current = new File(snomedOutput.getText());
        if (current.exists())
            chooser.setInitialDirectory(new File(snomedOutput.getText()));
        File of = chooser.showDialog(_stage);
        if (of!=null)
            snomedOutput.setText(of.toString());
    }

    public void importMRCM(ActionEvent actionEvent) {
        saveConfig();
        Snomed snomed = new Snomed();
        String si = snomedInput.getText();
        String so = snomedOutput.getText();
        try {
            clearlog();
            log("Importing MRCM files");
            SnomedMRCM mrcm= new SnomedMRCM();
            Ontology ontology = mrcm.saveMRCMAsDiscovery(si,so);
            log("Done");
            alert("Action complete", "MRCM ontology", "created and saved with UUI map");


        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

}
