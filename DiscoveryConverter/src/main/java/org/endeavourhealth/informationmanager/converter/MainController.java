package org.endeavourhealth.informationmanager.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.endeavourhealth.dataaccess.ConceptServiceV3;
import org.endeavourhealth.dataaccess.graph.ConceptServiceRDF4J;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.informationmanager.OntologyImport;
import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.transforms.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.IRI;


import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

public class MainController {



    private Properties config;
    private Stage _stage;
    private ConceptServiceRDF4J service;
    private RepositoryConnection conn;
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
        service= new ConceptServiceRDF4J();


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
            String query= config.getProperty("loggerText");
            snomedNamespace.setText((ns != null) ? ns : "1000252");
            parentEntity.setText((pe!=null) ? pe:"");
            logger.setText((query!=null) ? query:getPrefixes());




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
                config.setProperty("loggerText",logger.getText());
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
                long duration= (end-start)/1000/60;
                log("Discovery file classified and saved in "+ String.valueOf(duration) + " minutes");
                alert("Classifier","Discovery ontology classify","completed");

            }
        }
    }


    public void discoveryToManchester(ActionEvent actionEvent) throws Exception {
        File inputFile= getInputFile("json");
        if (inputFile!=null){
            File outputFile= getOutputFile("txt");
            if (outputFile!=null){
                long start = System.currentTimeMillis();
                DOWLManager manager= new DOWLManager();
                manager.convertDiscoveryFileToMOWL(inputFile,outputFile);
                long end = System.currentTimeMillis();
                long duration= (end-start)/1000/60;
                log("Discovery file converted to manchester syntax and saved in "+ String.valueOf(duration) + " minutes");
                alert("Discovery","Discovery ontology to owl","completed");

            }
        }
    }

    public void RunGraphQuery(ActionEvent actionEvent) {
        String queryText= logger.getText();
         //SPARQLRepository db= new SPARQLRepository("http://dbpedia.org/sparql");
       // Repository db = new HTTPRepository("http://localhost:7200/", "InformationModel");
       // RepositoryConnection conn = db.getConnection();

        if (queryText.contains("DELETE")) {
            Update deleteConcept = conn.prepareUpdate(queryText);
            ValueFactory vf = conn.getValueFactory();
            //deleteConcept.setBinding("concept", vf.createIRI("http://endhealth.info/im#903261000252100"));
            long start = System.currentTimeMillis();
            deleteConcept.execute();
            long end =System.currentTimeMillis();
            long duration = (end-start);
            logger.appendText("\n\n Duration = "+ duration+ " ms\n");

            logger.appendText("\n\nResult:\n");
            logger.appendText("Done");
        } else {
            TupleQuery query = conn.prepareTupleQuery(queryText);
            long start = System.currentTimeMillis();
            try (TupleQueryResult result = query.evaluate()) {
                long end =System.currentTimeMillis();
                long duration = (end-start);
                logger.appendText("\n\nDuration= "+ duration+ " ms\n");
                int i = 0;
                StringBuilder builder= new StringBuilder();
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    //if (i==0) {
                    //  Set<String> variables = bindingSet.getBindingNames();
                    //variables.forEach(v -> builder.append(bindingSet.getValue(v).stringValue() + "   "));
                    //logger.appendText(builder.toString());
                    //}
                    i++;
                }
                result.close();
                long endResult= System.currentTimeMillis();
                logger.appendText("\n"+ "Duration with fetch= " + (endResult-start+" ms"));

            }
        }
    }
    private String getPrefixes(){
        String prefixes="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
        +"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
        +"PREFIX : <http://endhealth.info/im#>\n"
        +"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
        +"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
        +"PREFIX sn: <http://snomed.info/sct#>\n"
        +"PREFIX sh: <http://www.w3.org/ns/shacl#>\n"
        +"PREFIX onto: <http://www.ontotext.com/>\n"
        +"PREFIX prov: <http://www.w3.org/ns/prov#>\n";
        return prefixes;

    }

    private List<org.eclipse.rdf4j.model.Namespace> getDefaultPrefixes(){
        List<org.eclipse.rdf4j.model.Namespace> prefixes= new ArrayList<>();
        prefixes.add(new SimpleNamespace("rdfs","http://www.w3.org/1999/02/22-rdf-syntax-ns#"));
        prefixes.add(new SimpleNamespace("rdf","http://www.w3.org/2000/01/rdf-schema#"));
        prefixes.add(new SimpleNamespace("im","http://endhealth.info/im#"));
        prefixes.add(new SimpleNamespace("dc","http://purl.org/dc/elements/1.1/"));
        prefixes.add(new SimpleNamespace("owl","http://www.w3.org/2002/07/owl#"));
        prefixes.add(new SimpleNamespace("sn","http://snomed.info/sct#"));
        prefixes.add(new SimpleNamespace("sh","http://www.w3.org/ns/shacl#"));
        prefixes.add(new SimpleNamespace("onto","http://www.ontotext.com/"));
        prefixes.add(new SimpleNamespace("prov","http://www.w3.org/ns/prov#"));
        return prefixes;
    }

   public void convertEcl(ActionEvent actionEvent) throws JsonProcessingException {
        String ecl= logger.getText();
        ECLToExpression eclConverter= new ECLToExpression();
        try {
            ClassExpression exp = eclConverter.getClassExpression(ecl);

       ObjectMapper objectMapper = new ObjectMapper();
       objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
       objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
       objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
       String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(exp);
        logger.appendText("\n\n"+ json);
        } catch (Exception e){
            logger.appendText("\n\n"+ e.toString());
        }
   }

    public void getConceptDefinition(ActionEvent actionEvent) throws JsonProcessingException {
        List<org.eclipse.rdf4j.model.Namespace> namespaces= getDefaultPrefixes();
        String siri= parentEntity.getText();
        org.eclipse.rdf4j.model.IRI iri = IMAccess.getFullIri(siri,namespaces);
        List<String> display= IMAccess.getDisplayDefinition(conn,iri,namespaces);
        if (!display.isEmpty()){
            for (String item:display)
                logger.appendText("\n"+ item);
        }

    }


    public void getRDBConcept(ActionEvent actionEvent) throws JsonProcessingException {
        ConceptServiceV3 conceptService= new ConceptServiceV3();
        Concept concept= conceptService.getConcept(parentEntity.getText());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(concept);
        logger.appendText("\n\n"+ json);
    }

    public void newSnomed(ActionEvent actionEvent) throws OWLOntologyCreationException, FileFormatException, IOException {
        File inputFile = getInputFile("json");
        if (inputFile != null) {
            try {
                DOWLManager manager= new DOWLManager();
                Ontology ontology= manager.loadOntology(inputFile);
                Individual counter= getIndividual(ontology,":890231000252108");
                AtomicInteger incFrom=new AtomicInteger(0);
                Stream<Integer> increment= counter.getRoleGroup()
                    .stream()
                    .map(this::getIncrement);
                String snomed= SnomedConcept.createConcept(increment.findFirst().get(),false);
                manager.saveOntology(inputFile);
                logger.appendText("\n"+ snomed);

            } catch (Exception e) {
                logger.appendText("\n\n" + e.getStackTrace().toString());
            }
        }
    }

    private Individual getIndividual(Ontology ontology,String iri) {
        for (Individual ind:ontology.getIndividual()){
            if (ind.getIri().equals(iri))
                return ind;
        }
        return null;
    }

    private Integer getIncrement(ConceptRoleGroup conceptRoleGroup)  {
        for (ConceptRole role:conceptRoleGroup.getRole()) {
            if (role.getProperty().getIri().equals(":hasIncrementalFrom")) {
                Integer counter = Integer.parseInt(role.getValueData());
                counter = counter + 1;
                role.setValueData(counter.toString());
                return counter;
            }
        }
        logger.appendText("No snomed counter found in ontology");
        return null;

    }

    public void getValueSetExpansion(ActionEvent actionEvent) throws DataFormatException {
        saveConfig();
        String valueSetIri= parentEntity.getText();
        Set<String> members = service.getValueSetExpansion(valueSetIri);
        logger.appendText("\n"+"Found "+ String.valueOf(members.size())+" concepts. First 20 of which are:");
        for (int i=0; (i<members.size())&(i<10); i++)
            logger.appendText("\n"+ members.toArray()[i]);

    }
}
