package org.endeavourhealth.informationmanager.converter;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.informationmanager.transforms.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;


import java.io.*;
import java.util.*;

public class MainController {
    @FXML
    private TextField idMapOutput;

    private Properties config;
    private Stage _stage;
    @FXML
    private TextArea logger;

    @FXML
    private TextField parentEntity;
    @FXML
    private TextField snomedNamespace;
    @FXML
    private TextField inputFolder;
    @FXML
    private TextField outputFolder;

    @FXML
    private ProgressBar progressBar;

    private ImportTask importTask;
    private Thread importThread;
    private DOWLManager conversionTask;
    private Thread conversionThread;

    public void setStage(Stage stage) {
        loadConfig();
        this._stage = stage;

    }

    private void loadConfig() {
        config = new Properties();
        try {
            FileInputStream cs = new FileInputStream("DiscoveryConverter.cfg");
            config.load(cs);
            String si = config.getProperty("inputFolderFolder");
            String so = config.getProperty("outputFolderFolder");
            String ns = config.getProperty("snomedNamespace");
            String pe = config.getProperty("parentEntity");
            String uid= config.getProperty("uuidMapFolder");
            inputFolder.setText((si != null) ? si : "");
            outputFolder.setText((so != null) ? so : "");
            snomedNamespace.setText((ns != null) ? ns : "1000252");
            parentEntity.setText((pe!=null) ? pe:"");
            idMapOutput.setText((uid!=null)? uid:"");


        } catch (FileNotFoundException nf) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveConfig(){

            config= new Properties();
            try {
                FileOutputStream cs = new FileOutputStream("DiscoveryConverter.cfg");
                config.setProperty("inputFolderFolder",inputFolder.getText());
                config.setProperty("outputFolderFolder",outputFolder.getText());
                config.setProperty("snomedNamespace",snomedNamespace.getText());
                config.setProperty("parentEntity",parentEntity.getText());
                config.setProperty("uuidMapFolder",idMapOutput.getText());
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

            log("Transforming");
            DOWLManager.convertOWLFileToDiscovery(inputFile,outputFile);

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
            dmanager.convertDiscoveryFileToOWL(inputFile,outputFile);

            log("Done");
            alert("Transform complete", "Discovery -> OWL Transformer", "Transform finished");

        } catch (Exception e) {
            alert("Process complete", "Discovery-OWL Transformer", "No ontology created");
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
        logger.textProperty().unbind();
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

    public void setSnomedInput(ActionEvent actionEvent) {

        DirectoryChooser chooser= new DirectoryChooser();
        chooser.setTitle("Select Snomed input folder");
        File current = new File(inputFolder.getText());
        if (current.exists())
             chooser.setInitialDirectory(new File(inputFolder.getText()));
        File of = chooser.showDialog(_stage);
        if (of!=null)
            inputFolder.setText(of.toString());

    }

    public void setSnomedOutput(ActionEvent actionEvent) {
        DirectoryChooser chooser= new DirectoryChooser();
        chooser.setTitle("Select Snomed output folder");
        File current = new File(outputFolder.getText());
        if (current.exists())
            chooser.setInitialDirectory(new File(outputFolder.getText()));
        File of = chooser.showDialog(_stage);
        if (of!=null)
            outputFolder.setText(of.toString());
    }

    public void importMRCM(ActionEvent actionEvent) {
        saveConfig();
        setTask(ImportType.MRCM);

        //Adds event handlers
        importTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                (EventHandler<WorkerStateEvent>) t -> {
                   progressBar.setVisible(false);
                   log("Snomed MRCM imported");
                   alert("Action complete", "MRCM ontology", "created and saved with UUI map");
                });
        importTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                (EventHandler<WorkerStateEvent>) t -> {
            log("IO problem creating MRCM ontology");
            progressBar.setVisible(false);
            alert("Action failed","MRCM Ontology","unable to create or save ontology. Check input and output paths");
            ErrorController.ShowError(_stage, (Exception) importTask.getException());
                });
        importThread= new Thread(importTask);
        importThread.start();


    }

    //checks thread has completed and binds task to progress bar
    private void setTask(ImportType importType){
        progressBar.progressProperty().unbind();
        logger.textProperty().unbind();
        progressBar.setVisible(true);
        progressBar.setProgress(0);
        if (importThread!=null){
            importTask.cancel();
            importThread.interrupt();;
        }


        String si = inputFolder.getText();
        String so = outputFolder.getText();
        String uui= idMapOutput.getText();
        importTask= new ImportTask(si,so,uui,importType);
        // Bind progress property
        // Unbind progress property
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(importTask.progressProperty());
        logger.textProperty().bind(importTask.messageProperty());

    }

    private void setConversionTask(){
        progressBar.progressProperty().unbind();
        logger.textProperty().unbind();
        progressBar.setVisible(true);
        progressBar.setProgress(0);
        if (conversionThread!=null){
            conversionTask.cancel();
            conversionThread.interrupt();;
        }
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(conversionTask.progressProperty());
        logger.textProperty().bind(conversionTask.messageProperty());

    }

    public void importSnomed(ActionEvent actionEvent) {
        saveConfig();
        setTask(ImportType.SNOMEDFULL);
        //Adds event handlers
        importTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                (EventHandler<WorkerStateEvent>) t -> {
                    progressBar.setVisible(false);
                    log("Snomed imported");
                    alert("Action complete", "Snomed ontology", "created and saved with UUI map");
                });
        importTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                (EventHandler<WorkerStateEvent>) t -> {
                    progressBar.setVisible(false);
                    log("IO problem creating Snomed ontology");
                    alert("Action failed","Snomed Ontology","unable to create or save ontology. Check input and output paths");
                    ErrorController.ShowError(_stage, (Exception) importTask.getException());                });

        importThread= new Thread(importTask);
        importThread.start();
    }

    public void setuuidMapOutput(ActionEvent actionEvent) {
        DirectoryChooser chooser= new DirectoryChooser();
        chooser.setTitle("Select UUID output folder");
        File current = new File(idMapOutput.getText());
        if (current.exists())
            chooser.setInitialDirectory(new File(idMapOutput.getText()));
        File of = chooser.showDialog(_stage);
        if (of!=null)
            idMapOutput.setText(of.toString());
    }

    public void batchDiscoveryToOwl(ActionEvent actionEvent) {
        saveConfig();
        conversionTask = new DOWLManager();
        setConversionTask();
        conversionTask.setIOFolder(TaskType.DISCOVERY_TO_OWL,
                inputFolder.getText(),
                outputFolder.getText());
        //Adds event handlers
        conversionTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                (EventHandler<WorkerStateEvent>) t -> {
                    progressBar.setVisible(false);
                    log("Discovery files converted");
                    alert("Action complete", "OWL Files", "converted and saved");
                });
        conversionTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                (EventHandler<WorkerStateEvent>) t -> {
                    log("IO problem converting Discovery files");
                    alert("Action failed","Discovery Ontology","unable to create or save OWL ontology. Check input and output paths");
                    ErrorController.ShowError(_stage, (Exception) importTask.getException());                });

        conversionThread= new Thread(conversionTask);
        conversionThread.start();

    }
}
