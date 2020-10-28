package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import javafx.concurrent.Task;
import org.apache.commons.collections.map.MultiValueMap;
import org.endeavourhealth.informationmanager.common.Logger;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import java.io.*;
import java.util.*;

/**
 * A manager class to handle ontologies and cross ontology activities
 * <p> includes conversion to and from OWL2 syntaxes</p>
 * <p> includes load/generate/ transform /save ontology methods combining load/ save and conversion between Discovery JSON into OWL syntax or saves from OWL into Discovery
 * <p>Includes saving asserted ontology and generating inferred views via a reasoner</p>
 * @since version 1.0
 * @author David Stables Endeavour, Richard Collier Ergonomics ltd.
 */
public class DOWLManager extends Task implements ReasonerProgressMonitor {

 private List<Ontology> ontologies;
 private Map<String,Ontology> ontologyList;
 private Map<String,Map<String, MultiValueMap>> indexes;
 private String inputFolder;
 private File inputFile;
 private String outputFolder;
 private File outputFile;
 private ConversionType conversionType;
 private String messageLines= "";


 public DOWLManager() {
     ontologies= new ArrayList<>();
     ontologyList= new HashMap<>();
     indexes = new HashMap<>();

 }

    /**
     * Sets the conversion type a folder name for batch conversion when operating as a thread
     * @param inputFolder
     * @return itself
     */
 public DOWLManager setIOFolder(ConversionType conversionType,
                                String inputFolder,
                                String outputFolder){
     this.inputFolder= inputFolder;
     this.outputFolder= outputFolder;
     this.conversionType= conversionType;
     inputFile=null;
     return this;
 }

    /**
     * Sets the conversion type and input file for threading. Sets input folder to null
     * @param inputFile
     * @return modified object
     */
    public DOWLManager setIOFile(ConversionType conversionType,
                                 File inputFile,
                                 File outputFile){
        this.inputFile= inputFile;
        this.outputFile= outputFile;
        this.conversionType= conversionType;
        inputFolder=null;
        return this;
    }

    @Override
    protected Object call() throws Exception {
        if (conversionType == null || (inputFile == null & (inputFolder == null)))
            throw new IllegalStateException("No conversion parameters set");
        switch (conversionType) {
            case DISCOVERY_TO_OWL_FILE:
                convertDiscoveryFileToOWL(inputFile, outputFile);
                break;
            case DISCOVERY_TO_OWL_FOLDER:
                convertDiscoveryFolderToOWL(inputFolder, outputFolder);
                break;
            case OWL_TO_DISCOVERY_FILE:
                convertOWLFileToDiscovery(inputFile, outputFile);
                break;
            case OWL_TO_DISCOVERY_FOLDER:
                convertOWLFolderToDiscovery(inputFolder, outputFolder);
                break;
            case OWL_TO_DISCOVERY_ISA_FILE:
                convertOWLFileToDiscoveryIsa(inputFile, outputFile);
                break;
            default:
                throw new Exception("conversion task type not set");
        }
        return 1;
    }

    private void updateMessageLine(String line,boolean eol){
        messageLines=messageLines+ line+ ((eol==true) ? "\n":", ");
        updateMessage(messageLines);
    }

    private void convertOWLFolderToDiscoveryIsa(String inputFolder, String outputFolder) throws OWLOntologyCreationException, IOException {
        File directory = new File(inputFolder);
        File[] fileList = directory.listFiles((dir, name) -> name.endsWith(".owl"));
        if (fileList != null) {
            for (File inFile : fileList) {
                String inFileName= inFile.getName();
                String outFileName= outputFolder+"\\" +
                        inFileName
                                .substring(0,inFileName.lastIndexOf("."));
                outFileName=outFileName + "-inferred.json";
                File outFile=new File(outFileName);
                updateMessageLine("Converting "+ inFile.getName(),true);
                convertOWLFileToDiscoveryIsa(inFile,outFile);
                if (isCancelled()) return;
            }
        }

    }



    public void convertOWLFileToDiscoveryIsa(File inputFile, File outputFile) throws OWLOntologyCreationException, IOException {
        //Creates ontology manager
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntologyLoaderConfiguration loader = new OWLOntologyLoaderConfiguration();
        manager.setOntologyLoaderConfiguration(loader.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
        updateMessageLine("Loading owl file "+inputFile,false);
        OWLOntology ontology = manager.loadOntology(IRI.create(inputFile));

        updateProgress(1,10);
        saveOWLAsInferred(manager,ontology,outputFile);
    }


    public void convertOWLFileToDiscovery(File inputFile, File outputFile) throws OWLOntologyCreationException, IOException {
        convertOWLFileToDiscovery(inputFile, outputFile, null, null, null);
    }

    public void convertOWLFileToDiscovery(File inputFile, File outputFile, String ontologyIri, String moduleIri, UUID documentId) throws OWLOntologyCreationException, IOException {

        //Creates ontology manager
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntologyLoaderConfiguration loader = new OWLOntologyLoaderConfiguration();
        manager.setOntologyLoaderConfiguration(loader.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT));
        OWLOntology ontology = manager.loadOntology(IRI.create(inputFile));

        // Do not convert snomed
        List<String> filterNamespaces = new ArrayList<>();
        filterNamespaces.add("sn");
        OWLDocumentFormat format= manager.getOntologyFormat(ontology);

        //Create Discovery ontology and convert
        Document document = new OWLToDiscovery().transform(ontology, format,filterNamespaces);

        // Set/override ids
        if (!Strings.isNullOrEmpty(ontologyIri)) document.getInformationModel().setIri(ontologyIri);
        document.getInformationModel().setModule(moduleIri);
        document.getInformationModel().getDocumentInfo().setDocumentId(documentId);

        saveDiscovery(document,outputFile);
    }

    public void convertDiscoveryFolderToOWL(String inputFolder, String outputFolder) throws Exception {

        File directory = new File(inputFolder);
        File[] fileList = directory.listFiles((dir, name) -> name.endsWith(".json"));
        if (fileList.length==0){
            throw new Exception("no json files found");
        }
        int fileNumber=0;
        if (fileList != null) {
            int totalFiles= fileList.length;
            for (File inFile : fileList) {
                updateMessageLine("Converting "+ inFile.getName(),true);
                String inFileName= inFile.getName();
                String outFileName= outputFolder+"\\" +
                        inFileName
                                .substring(0,inFileName.lastIndexOf("."));
                outFileName=outFileName + ".owl";
                File outFile=new File(outFileName);
                convertDiscoveryFileToOWL(inFile,outFile);
                fileNumber++;
                updateProgress(fileNumber,totalFiles);
            }
        }
    }


    public void convertOWLFolderToDiscovery(String inputFolder, String outputFolder) throws Exception {
        File directory = new File(inputFolder);
        File[] fileList = directory.listFiles((dir, name) -> name.endsWith(".owl"));
        if (fileList.length==0){
            throw new Exception("no json files found");
        }
        if (fileList != null) {
            for (File inFile : fileList) {
                String inFileName= inFile.getName();
                String outFileName= outputFolder+"\\" +
                        inFileName
                                .substring(0,inFileName.lastIndexOf("."));
                outFileName=outFileName + ".json";
                File outFile=new File(outFileName);
                convertOWLFileToDiscovery(inFile,outFile);
            }
        }

    }


    public static MultiValueMap getConceptMap(Ontology ontology){
     MultiValueMap conceptMap = new MultiValueMap();

     //Loops through the 3 main concept types and add them to the IRI map
     //Note that an IRI may be both a class and a property so both are added
     if (ontology.getObjectProperty()!=null)
         ontology.getObjectProperty().forEach(p-> conceptMap.put(p.getIri(),p));
     if (ontology.getDataProperty()!=null)
         ontology.getDataProperty().forEach((d-> conceptMap.put(d.getIri(),d)));
     if (ontology.getClazz()!=null)
         ontology.getClazz().forEach(c->conceptMap.put(c.getIri(),c));
     return conceptMap;
 }

    public void convertDiscoveryFileToOWL(File inputFile,File outputFile) throws IOException, OWLOntologyCreationException, FileFormatException, OWLOntologyStorageException {

        OWLOntologyManager owlManager = loadOWLFromDiscovery(inputFile);
        OWLDocumentFormat format = new FunctionalSyntaxDocumentFormat();
        format.setAddMissingTypes(false);   // Prevent auto-declaration of "anonymous" classes
        FileWriter writer=new FileWriter(outputFile);
        owlManager.getOntologies().forEach(o-> {
           try{
               owlManager.setOntologyFormat(o,format);
               o.saveOntology(format,new FileOutputStream(outputFile));

            } catch (IOException | OWLOntologyStorageException e) {
                e.printStackTrace();
            }

        });

    }
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
     * Loads a discovery document file in JSON syntax
     * @param inputFile  the file name to load
     * @return the Discovery document
     * @throws IOException
     * @throws OWLOntologyCreationException
     * @throws FileFormatException
     */
    public Ontology loadFromDiscovery (File inputFile) throws IOException, OWLOntologyCreationException, FileFormatException {
        ObjectMapper objectMapper = new ObjectMapper();
        Document document = objectMapper.readValue(inputFile, Document.class);
        return  document.getInformationModel();
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
        updateMessageLine("Computing inferences... Please wait",true);
        OWLReasonerConfiguration config = new SimpleConfiguration(this);
        Document document = new OWLToDiscovery().generateInferredView(ontology,format,config);
        updateProgress(9,10);
        saveDiscovery(document,outputFile);

    }

    /**
     * Saves a Discovery JSON syntax ontology
     * @param document  the Discovery ontology document
     * @param outputFile
     * @throws IOException
     */
    public static void saveDiscovery(Document document,
                                  File outputFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(json);
        }
        catch (Exception e) {
            Logger.error("Unable to transform and save ontology in JSON format");
        }
    }

    public static void saveDiscovery(Ontology ontology,
                                     File outputFile) throws IOException {
        Document document = new Document();
        document.setInformationModel(ontology);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(json);
        }
        catch (Exception e) {
            Logger.error("Unable to transform and save ontology in JSON format");
        }
    }

    public List<Ontology> getOntologies() {
        return ontologies;
    }

    public DOWLManager setOntologies(List<Ontology> ontologies) {
        this.ontologies = ontologies;
        return this;
    }

    /**
     * Adds to the ontology list managed by the manager
     * @param ontology
     * @return modified manager
     */
    public DOWLManager addOntology(Ontology ontology){
        ontologies.add(ontology);
        ontologyList.put(ontology.getIri(),ontology);
        return this;
    }

    /**
     * Gets a previously loaded/created ontology that has the specified ontology IR
     * @param ontologyId
     * @return The ontology, or null if not found
     */
    public Ontology getOntology(String ontologyId){
        return ontologyList.get(ontologyId);
    }

    /**
     * gets a named index from a particular ontology from the list of ontologies loaded
     * <p>Example of index name would be "concept.name" for a list of </p>
     * <p>if the index has not been generated all the indexes for that ontology  are first generated </p></o>
     * @return a multivalued list sorted by default in case sensitive string order
     */
    public MultiValueMap getIndexByName(String ontologyIri, String indexName){
       Map ontologyIndex= getIndexesForOntology(ontologyIri);
       if (ontologyIndex==null)
        return null;
       return (MultiValueMap) ontologyIndex.get(indexName);
    }

    /**
     * Gets the set of indexes for an ontology
     * @param ontologyIri  the iri of the ontology for an ontology already added to manager
     * @return a map of indexes for the ontology or null if the ontology is not loaded
     */
    public Map getIndexesForOntology(String ontologyIri){

        //Does the manager have the ontology?
        if (!ontologyList.containsKey(ontologyIri))
            return null;
        Ontology ontology = ontologyList.get(ontologyIri);
        //Index not created?
        if (!indexes.containsKey(ontologyIri)) {
            createIndexesForOntology(ontology);
        }

        return indexes.get(ontologyIri);
    }

    private void createIndexesForOntology(Ontology ontology) {

        //Creates the ontology indexes for this ontology and add to list of indexes
        Map<String, MultiValueMap> ontologyIndexes = new HashMap<>();
        indexes.put(ontology.getIri(), ontologyIndexes);

        //List of indexes to build for this ontology
        String[] indexNames = {"name"};

        //Initialise the indexes;
        for (String s : indexNames)
            ontologyIndexes.put(s, new MultiValueMap());

        //Get a list of concepts from the ontology
        List<Concept> conceptList = getConcepts(ontology);
        for (Concept c : conceptList)
            for (String indName : indexNames) {
                MultiValueMap index = ontologyIndexes.get(indName);
                if (indName.equals("name"))
                    if (c.getName() != null)
                        index.put(c.getName(), c.getIri());
            }
    }

    private List<Concept> getConcepts(Ontology ontology) {
        List<Concept> conceptList= new ArrayList<>();
        if (ontology.getClazz()!=null)
            for (Concept c:ontology.getClazz())
                conceptList.add(c);
        if (ontology.getObjectProperty()!=null)
            for (Concept c:ontology.getObjectProperty())
                conceptList.add(c);
        if (ontology.getDataProperty()!=null)
            for (Concept c:ontology.getDataProperty())
               conceptList.add(c);
        if (ontology.getDataType()!=null)
            for (Concept c:ontology.getDataType())
               conceptList.add(c);
        if (ontology.getAnnotationProperty()!=null)
            for (Concept c:ontology.getAnnotationProperty())
                conceptList.add(c);
        return conceptList;
    }

    public void rebuildIndexes() {
        indexes.clear();
        ontologies.forEach(o-> createIndexesForOntology(o));
    }

    public static Ontology createOntology(String iri, String moduleIri) {
        Ontology ontology = new Ontology();
        ontology.setIri(iri);
        ontology.setModule(moduleIri);
        setDefaultNamespaces(ontology);
        ontology.setDocumentInfo(
            new DocumentInfo().setDocumentId(UUID.randomUUID())
        );
        return ontology;
    }

    private static void setDefaultNamespaces(Ontology ontology) {
        Map<String,String> ns= new HashMap<>();
        ns.put(":",NamespaceIri.DISCOVERY.getValue() + "#");
        ns.put("sn:",NamespaceIri.SNOMED.getValue() + "#");
        ns.put("owl:","http://www.w3.org/2002/07/owl#");
        ns.put("rdf:","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        ns.put("xml:","http://www.w3.org/XML/1998/namespace");
        ns.put("xsd:","http://www.w3.org/2001/XMLSchema#");
        ns.put("rdfs:","http://www.w3.org/2000/01/rdf-schema#");
        ns.forEach((a,b) -> ontology.addNamespace(new Namespace().setPrefix(a).setIri(b)));
    }

    public static Concept createConcept(String id, ConceptStatus status,
                                        Integer version, String iri,String name,
                                        String description,String code,
                                        String codeScheme){
        Concept concept= new Concept().setId(iri)
                .setStatus(status)
                .setVersion(version)
                .setIri(iri)
                .setName(name)
                .setDescription(description)
                 .setCode(code).setScheme(":891101000252101");
        return concept;
    }
    public static ObjectProperty conceptAsObjectProperty (Concept c){
        return (ObjectProperty) new ObjectProperty().setId(c.getId())
                .setStatus(c.getStatus())
                .setVersion(c.getVersion())
                .setIri(c.getIri())
                .setName(c.getName())
                .setCode(c.getCode())
                .setScheme(c.getScheme());
    }

    public static Annotation createAnnotation(String property, String value){
        Annotation annotation= new Annotation();
        annotation.setProperty(property);
        annotation.setValue(value);
        return annotation;
    }

    @Override
    public void reasonerTaskStarted(String s) {

    }

    @Override
    public void reasonerTaskStopped() {

    }

    @Override
    public void reasonerTaskProgressChanged(int i, int i1) {
        if (i>0)
              if (i%1000==0) {
                updateProgress(i, i1);
                updateMessageLine(String.valueOf(Math.round((double)i/(double) i1*100)) + "%",
                        false);
              }
    }

    @Override
    public void reasonerTaskBusy() {

    }

    public ClassExpression convertEclToDiscoveryExpression(String ecl){
        ECLToDiscovery eclConverter= new ECLToDiscovery();
        return eclConverter.getClassExpression(ecl);

    }


    public String convertEclToDiscoveryString(String ecl) throws JsonProcessingException {

        ClassExpression cex= convertEclToDiscoveryExpression(ecl);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cex);
        return json;
    }

    public String convertEclToOWLString(String ecl) {

        ECLToDiscovery eclConverter= new ECLToDiscovery();
        String outString= eclConverter.getClassExpressionAsFS(ecl);
        return outString;

    }

    public Ontology loadAndSaveSimpleInferred(File inputFile, File outputFile) throws OWLOntologyCreationException, FileFormatException, IOException {
        Ontology ontology =loadFromDiscovery(inputFile);
        return generateSimpleInferred(ontology);
    }

    public Ontology generateSimpleInferred(Ontology ontology){
        DiscoveryReasoner reasoner = new DiscoveryReasoner(ontology);
        return reasoner.classify();

    }
}
