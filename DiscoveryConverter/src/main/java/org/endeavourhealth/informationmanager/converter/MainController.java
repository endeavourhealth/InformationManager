package org.endeavourhealth.informationmanager.converter;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.endeavourhealth.informationmanager.OntologyImport;
import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.imapi.model.Ontology;
import org.endeavourhealth.informationmanager.transforms.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.*;



import java.io.*;
import java.util.*;

public class MainController {



    private Properties config;
    private Stage _stage;
    @FXML
    private TextArea logger;

    @FXML
    private TextField parentEntity;
    @FXML
    private TextField snomedNamespace;


    @FXML
    private ProgressBar progressBar;

    private Task conversionTask;
    private Thread conversionThread;
    private File inputFile;
    private File outputFile;
    private File inputFolder;
    private File outputFolder;

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
                config.setProperty("snomedNamespace",snomedNamespace.getText());
                config.setProperty("parentEntity",parentEntity.getText());
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

        DiscoveryIdsResult ids = DiscoveryIdsController.PromptDocDetails(_stage);
        if (ids == null)
            return;

        try {
            clearlog();

            log("Transforming");
            DOWLManager manager = new DOWLManager();
            manager.convertOWLFileToDiscovery(inputFile,outputFile, ids.getOntologyIri(), ids.getOntologyModuleIri(), ids.getDocumentId());

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
    private boolean checkOK(String checkInfo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(checkInfo);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK)
            return true;
        return false;
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


    public File getInputFolder() {

        DirectoryChooser chooser= new DirectoryChooser();
        chooser.setTitle("Select input folder");
        if (inputFolder!=null)
             chooser.setInitialDirectory(inputFolder);
        File of = chooser.showDialog(_stage);
        if (of!=null)
            inputFolder= of;
        return inputFolder;

    }




    private Task setConversionTask(ConversionType conversionType){
        if (conversionTask!=null)
            conversionTask.cancel();
        progressBar.progressProperty().unbind();
        logger.textProperty().unbind();
        Task conversionTask;
        switch (conversionType) {


            case DISCOVERY_TO_OWL_FILE: {
                DOWLManager manager = new DOWLManager()
                        .setIOFile(conversionType, inputFile, outputFile);
                return setTaskEvent(manager, "Discovery to OWL file conversion");
            }

            case OWL_TO_DISCOVERY_FILE: {
                DOWLManager manager = new DOWLManager()
                        .setIOFile(conversionType, inputFile, outputFile);
                return setTaskEvent(manager, "OWL to Discovery file conversion");
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
                    progressBar.setVisible(false);
                    log("Problem with task see error");
                    alert("Action failed","Discovery converter ","unable to "+ taskDescription);
                    ErrorController.ShowError(_stage, (Exception) task.getException());                });
        return task;
    }




    public void eclToDiscovery(ActionEvent actionEvent) throws IOException {
        _stage.close();
        Stage newStage = new Stage();
        newStage.setTitle("ECL Converter");

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ECLImporter.fxml"));
        Parent root = loader.load();

        ECLController controller = loader.getController();
        controller.setStage(newStage);

        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();

    }




    @FXML
   private void fileOntology(ActionEvent actionEvent) throws Exception {
        //Pair<String,String> user= loginAndFile();
        //if (user==null)
          //  return;
        fileDiscovery("","");
    }
    private void fileDiscovery(String id, String id2){
        File inputFile = getInputFile("json");
        if (inputFile!=null){

            try {

            OntologyImport.fileOntology(inputFile,false);
            log("Ontology filed and classification filed");
            alert("Ontology filer", "Discovery -> IMDB filer", "Filer finished");

            } catch (Exception e) {
                alert("Process complete", "Ontology filer ", "Not filed");
                ErrorController.ShowError(_stage, e);
            }

        }
   }

    private Pair<String,String> loginAndFile() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Data base credentials");
        dialog.setHeaderText("Enter user name and password");
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {

                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        if (result.isPresent())
            return result.get();
        else
            return null;

    }

    public void importRF2Release(ActionEvent actionEvent) throws Exception {
        getInputFolder();
        if (inputFolder!=null){
            RF2ToIMDB importer= new RF2ToIMDB(inputFolder.toString());
            Thread rf2Thread= new Thread(importer);
            rf2Thread.start();

        }

    }

    public void classifyDiscovery(ActionEvent actionEvent) throws Exception {
        File inputFile= getInputFile("json");
        if (inputFile!=null){
            File outputFile= getOutputFile("json");
            if (outputFile!=null){
                long start = System.currentTimeMillis();
                DOWLManager manager= new DOWLManager();
                Ontology ontology= manager.loadOntology(inputFile);
                DiscoveryReasoner reasoner = new DiscoveryReasoner();
                reasoner.classify(ontology);
                manager.saveOntology(outputFile);
                long end = System.currentTimeMillis();
                long duration= end-start/1000/60;
                log("Discovery file classified and saved in "+ String.valueOf(duration) + " minutes");
                alert("Classifier","Discovery ontology classify","completed");

            }
        }
    }
}
