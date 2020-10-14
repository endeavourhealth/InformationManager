package org.endeavourhealth.informationmanager.converter;

import javafx.concurrent.Task;
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

    private Thread importThread;
    private Task conversionTask;
    private Thread conversionThread;
    private File inputFile;
    private File outputFile;

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

    private void setIOFiles(String from,String to){
        FileChooser inFileChooser = new FileChooser();

        inFileChooser.setTitle("Select input ("+ from.toUpperCase()+") file");
        inFileChooser.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(from.toUpperCase()+"Files","*."+from));

        inputFile = inFileChooser.showOpenDialog(_stage);
        if (inputFile == null)
            return;

        FileChooser outFileChooser = new FileChooser();
        outFileChooser.setTitle("Select output ("+ to.toUpperCase()+") file");
        outFileChooser.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(to.toUpperCase()+ " Files", "*."+ to));
        outputFile = outFileChooser.showSaveDialog(_stage);
        if (outputFile == null)
            return;
    }

    @FXML
    protected void owlToDiscovery(ActionEvent event) {
        saveConfig();
        setIOFiles("owl","json");
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
            DOWLManager manager = new DOWLManager();
            manager.convertOWLFileToDiscovery(inputFile,outputFile);
            //DOWLManager.convertOWLFileToDiscovery(inputFile,outputFile);

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

            conversionTask = setConversionTask(ConversionType.OWL_TO_DISCOVERY_ISA_FOLDER);
            conversionThread= new Thread(conversionTask);
            conversionThread.start();

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
        conversionTask= setConversionTask(ConversionType.SNOMED_MRCM_TO_DISCOVERY);
        importThread= new Thread(conversionTask);
        importThread.start();

    }



    private Task setConversionTask(ConversionType conversionType){
        if (conversionTask!=null)
            conversionTask.cancel();
        progressBar.progressProperty().unbind();
        logger.textProperty().unbind();
        Task conversionTask;
        switch (conversionType) {
            case OWL_TO_DISCOVERY_ISA_FOLDER: {
                DOWLManager manager = new DOWLManager()
                        .setIOFolder(conversionType, inputFolder.getText(), outputFolder.getText());
                setTaskEvent(manager, "OWL to ISA file conversion");
            }
            case OWL_TO_DISCOVERY_ISA_FILE: {
                DOWLManager manager = new DOWLManager()
                        .setIOFile(conversionType, inputFile, outputFile);
                setTaskEvent(manager, "OWL to ISA conversion");
            }
            case DISCOVERY_TO_OWL_FOLDER: {
                DOWLManager manager = new DOWLManager()
                        .setIOFolder(conversionType, inputFolder.getText(), outputFolder.getText());
                return setTaskEvent(manager, "Discovery to OWL Folder conversion");
            }
            case DISCOVERY_TO_OWL_FILE: {
                DOWLManager manager = new DOWLManager()
                        .setIOFile(conversionType, inputFile, outputFile);
                return setTaskEvent(manager, "Discovery to OWL file conversion");
            }
            case OWL_TO_DISCOVERY_FOLDER: {
                DOWLManager manager = new DOWLManager()
                        .setIOFolder(conversionType, inputFolder.getText(), outputFolder.getText());
                return setTaskEvent(manager, "OWL to Discovery Folder conversion");
            }
            case OWL_TO_DISCOVERY_FILE: {
                DOWLManager manager = new DOWLManager()
                        .setIOFile(conversionType, inputFile, outputFile);
                return setTaskEvent(manager, "OWL to Discovery file conversion");
            }
            case RF2_TO_DISCOVERY_FOLDER:{
                RF2ImportTask importer= new RF2ImportTask()
                        .setinputFolder(inputFolder.getText())
                        .setOutputFolder(outputFolder.getText())
                        .setImportType(conversionType)
                        .setUuidFolder(idMapOutput.getText());
                return setTaskEvent(importer,"Snomed RF2 import");
            }
            case SNOMED_MRCM_TO_DISCOVERY: {
                RF2ImportTask importer = new RF2ImportTask()
                        .setinputFolder(inputFolder.getText())
                        .setOutputFolder(outputFolder.getText())
                        .setImportType(conversionType)
                        .setUuidFolder(idMapOutput.getText());
                return setTaskEvent(importer, "Snomed MRCM import");
            }

            default: {
                alert("Task type", "unsupported task type", "not supported");
                ErrorController.ShowError(_stage, new Exception("Invalid task type"));
                return null;
            }
        }
    }

    private Task setTaskEvent(Task task , String taskDescription) {

        progressBar.setVisible(true);
        progressBar.setProgress(0);
        if (conversionThread!=null){
            conversionTask.cancel();
            conversionThread.interrupt();;
        }
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(task.progressProperty());
        logger.textProperty().bind(task.messageProperty());
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                (EventHandler<WorkerStateEvent>) t -> {
                    progressBar.setVisible(false);
                    log(taskDescription+ " completed ");
                    alert("Action complete", "", taskDescription);
                });
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                (EventHandler<WorkerStateEvent>) t -> {
                    log("Problem with task see error");
                    alert("Action failed","Discovery converter ","unable to "+ taskDescription);
                    ErrorController.ShowError(_stage, (Exception) task.getException());                });
        return task;
    }

    public void importSnomed(ActionEvent actionEvent) {
        saveConfig();
        conversionTask= setConversionTask(ConversionType.RF2_TO_DISCOVERY_FOLDER);
        importThread= new Thread(conversionTask);
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
        conversionTask = setConversionTask(ConversionType.DISCOVERY_TO_OWL_FOLDER);
        conversionThread= new Thread(conversionTask);
        conversionThread.start();

    }

    public void batchConvertIsa(ActionEvent actionEvent) {
        saveConfig();
        conversionTask = setConversionTask(ConversionType.OWL_TO_DISCOVERY_ISA_FOLDER);
        conversionThread= new Thread(conversionTask);
        conversionThread.start();


    }
}
