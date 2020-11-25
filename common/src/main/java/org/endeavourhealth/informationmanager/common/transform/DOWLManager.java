package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.sun.org.apache.xml.internal.utils.NameSpace;
import javafx.concurrent.Task;
import org.apache.commons.collections.map.MultiValueMap;
import org.endeavourhealth.informationmanager.common.Logger;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.imapi.model.*;
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

 private Ontology ontology;
 private Map<String,Concept> iriMap;
 private Map<String,Concept> nameMap;
 private String inputFolder;
 private File inputFile;
 private String outputFolder;
 private File outputFile;
 private ConversionType conversionType;
 private String messageLines= "";


 public DOWLManager() {

 }
   public DOWLManager(Ontology ontology) {
    this.ontology= ontology;
   }


   public static String getShortIri(Set<Namespace> nameSet,String iri){
      if (iri.startsWith("http")) {
         int hash = iri.indexOf("#");
         if (hash > -1) {
            String nsIri = iri.substring(0, hash) + "#";
            Optional<Namespace> match = nameSet.stream()
                .filter(ns -> ns.getIri().equals(nsIri))
                .findFirst();
            if (match.isPresent())
               return match.get().getPrefix() + iri.substring(hash + 1);
            else return iri;
         } else
            return iri;
      } else {
            int colon = iri.lastIndexOf(":");
            if (colon > -1) {
               String clientPrefix = iri.substring(0, colon) + ":";
               Optional<Namespace> match = nameSet.stream()
                   .filter(ns -> ns.getPrefix().equals(clientPrefix))
                   .findFirst();
               if (match.isPresent())
                  return iri;
               else
                  return null;
            } else
               return null;
         }
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

            case OWL_TO_DISCOVERY_FILE:
                convertOWLFileToDiscovery(inputFile, outputFile);
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



    public void convertOWLFileToDiscovery(File inputFile, File outputFile) throws Exception {
        convertOWLFileToDiscovery(inputFile, outputFile, null, null, null);
    }

    public void convertOWLFileToDiscovery(File inputFile, File outputFile, String ontologyIri, String moduleIri, UUID documentId) throws Exception {

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

        DOWLManager mgr= new DOWLManager(document.getInformationModel());
        mgr.saveOntology(outputFile);
    }


    public void createIndex(){
     iriMap = new HashMap();
     nameMap= new HashMap();

     //Loops through the 3 main concept types and add them to the IRI map
     //Note that an IRI may be both a class and a property so both are added
     if (ontology.getConcept()!=null)
         ontology.getConcept().forEach(p-> {iriMap.put(p.getIri(),p);
                                if (p.getName()!=null)
                                    nameMap.put(p.getName().toLowerCase(),p);});
 }

    public void convertDiscoveryFileToOWL(File inputFile,File outputFile) throws Exception {

        OWLOntologyManager owlManager = loadOWLFromDiscovery(inputFile);
        FileWriter writer=new FileWriter(outputFile);
        owlManager.getOntologies().forEach(o-> {
           try{
              OWLDocumentFormat format= owlManager.getOntologyFormat(o);
              format.setAddMissingTypes(false);
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
    public OWLOntologyManager loadOWLFromDiscovery (File inputFile) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Document document = objectMapper.readValue(inputFile, Document.class);
        ontology = document.getInformationModel();
        addOWLPlaceHolder();
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
    public Ontology loadOntology (File inputFile) throws IOException, OWLOntologyCreationException, FileFormatException {
        ObjectMapper objectMapper = new ObjectMapper();
        Document document = objectMapper.readValue(inputFile, Document.class);
        ontology= document.getInformationModel();
        return  document.getInformationModel();
    }




    /**
     * Saves the Discovery ontology held by the manager
     * @param outputFile file to save ontology to
     * @throws IOException
     */
    public void saveOntology(File outputFile) throws IOException {
       if (ontology==null)
          throw new NullPointerException("Manager has no ontology assigned");
        Document document = new Document();
        document.setInformationModel(ontology);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(json);
        }
        catch (Exception e) {
            Logger.error("Unable to transform and save ontology in JSON format");
        }
    }




    public Ontology createOntology(String iri, String moduleIri) {
        ontology = new Ontology();
        ontology.setIri(iri);
        ontology.setModule(moduleIri);
        setDefaultNamespaces();
        ontology.setDocumentInfo(
            new DocumentInfo().setDocumentId(UUID.randomUUID())
        );
        return ontology;
    }

    private void setDefaultNamespaces() {
        Map<String,String> ns= new HashMap<>();
        ns.put(":",NamespaceIri.DISCOVERY.getValue());
        ns.put("sn:",NamespaceIri.SNOMED.getValue());
        ns.put("owl:","http://www.w3.org/2002/07/owl#");
        ns.put("rdf:","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        ns.put("xml:","http://www.w3.org/XML/1998/namespace#");
        ns.put("xsd:","http://www.w3.org/2001/XMLSchema#");
        ns.put("rdfs:","http://www.w3.org/2000/01/rdf-schema#");
        ns.put("r2:","http://www.DiscoveryDataService.org/InformationModel/Legacy/READ2#");
        ns.put("ctv3:","http://www.DiscoveryDataService.org/InformationModel/LegacyCTV3#");
        ns.put("emis:","http://www.DiscoveryDataService.org/InformationModel/Legacy/EMIS#");
        ns.put("tpp:","http://www.DiscoveryDataService.org/InformationModel/Legacy/TPP#");
       ns.put("bc:","http://www.DiscoveryDataService.org/InformationModel/Legacy/Barts_Cerner#");
        ns.forEach((a,b) -> ontology.addNamespace(new Namespace().setPrefix(a).setIri(b)));

    }

    public void addOWLPlaceHolder(){
       createIndex();
       List<Concept> newConcepts = new ArrayList<>();
       Concept placeHolder= new Concept();
       placeHolder.setIri(":OWLPlaceHolder");
       ontology.addConcept(placeHolder);
       for (Concept c:ontology.getConcept()){
          if (c.getSubClassOf()!=null)
             for (ClassAxiom ax:c.getSubClassOf())
                addOWLPlaceHolderEx(ax,newConcepts);
          if (c.getEquivalentTo()!=null)
             for (ClassAxiom ax:c.getEquivalentTo())
                addOWLPlaceHolderEx(ax,newConcepts);
          if (c.getConceptType()== ConceptType.OBJECTPROPERTY) {
             ObjectProperty p = (ObjectProperty) c;
             if (p.getObjectPropertyRange() != null)
                for (ClassExpression rex:p.getObjectPropertyRange())
                  addOWLPlaceHolderEx(rex, newConcepts);
             if (p.getPropertyDomain()!=null)
                for (ClassExpression dex:p.getPropertyDomain())
                   addOWLPlaceHolderEx(dex,newConcepts);

          }
       }
       if (newConcepts.size()>0)
          for (Concept c:newConcepts)
             ontology.addConcept(c);
    }

   public ObjectPropertyValue getObjectPropertyValue(Concept concept, String property){
       ObjectPropertyValue opv= null;
       if (concept.getSubClassOf()!=null)
          for (ClassAxiom axiom:concept.getSubClassOf())
             opv=getOpv(axiom,property);
             if (opv!=null)
                return opv;
       else if (concept.getEquivalentTo()!=null)
          for (ClassAxiom axiom:concept.getEquivalentTo())
             opv=getOpv(axiom,property);
                      if (opv!=null)
                         return opv;;
       return opv;
   }

   private ObjectPropertyValue getOpv(ClassExpression exp,String property) {
      if (exp.getIntersection() != null) {
         for (ClassExpression inter : exp.getIntersection())
            if (inter.getObjectPropertyValue() != null) {
               if (inter.getObjectPropertyValue().getProperty().getIri().equals(property))
                  return inter.getObjectPropertyValue();
               if (inter.getObjectPropertyValue().getExpression() != null) {
                  return getOpv(inter.getObjectPropertyValue().getExpression(), property);
               }
            }
      } else if (exp.getObjectPropertyValue()!=null) {
          if (exp.getObjectPropertyValue().getProperty().getIri().equals(property))
             return exp.getObjectPropertyValue();
       }
       return null;
   }


   private void addOWLPlaceHolderEx(ClassExpression ex,List<Concept> newConcepts) {
       if (ex.getClazz()!=null)
          if (getConcept(ex.getClazz().getIri())==null)
             addOWLPlaceHolderIri(ex.getClazz().getIri(),newConcepts);
       if (ex.getIntersection()!=null)
          for (ClassExpression inter:ex.getIntersection())
             addOWLPlaceHolderEx(inter,newConcepts);
       if (ex.getUnion()!=null)
          for (ClassExpression union:ex.getUnion())
             addOWLPlaceHolderEx(union,newConcepts);
       if (ex.getObjectPropertyValue()!=null){
          ObjectPropertyValue pv= ex.getObjectPropertyValue();
          if (pv.getValueType()!=null)
             addOWLPlaceHolderIri(pv.getValueType().getIri(),newConcepts);
          if (pv.getExpression()!=null)
             addOWLPlaceHolderEx(pv.getExpression(),newConcepts);
       }
       if (ex.getComplementOf()!=null)
          addOWLPlaceHolderEx(ex.getComplementOf(),newConcepts);
   }

   private void addOWLPlaceHolderIri(String iri,List<Concept> newConcepts) {
       if (getConcept(iri)==null) {
          Concept external = new Concept();
          external.setIri(iri);
          external.addSubClassOf((ClassAxiom) new ClassAxiom()
              .setClazz(new ConceptReference(":OWLPlaceHolder")));
          newConcepts.add(external);
       }
    }


   public static Concept createConcept(Integer id, ConceptStatus status,
                                        Integer version, String iri,String name,
                                        String description,String code,
                                        String codeScheme){
        Concept concept= new Concept().setDbid(id)
                .setStatus(status)
                .setVersion(version)
                .setIri(iri)
                .setName(name)
                .setDescription(description)
                 .setCode(code).setScheme(":891101000252101");
        return concept;
    }

   /**
    * Tests whether a descendant concept (iri) is a descendand of an ancestor (iri) within this module
    * uses standard prefixes in this version i.e. candidate IRIs use prefix
    * @param descendant  the iri of the descendant
    * @param ancestor the iri of the ancestor
    * @return
    */
    public boolean isA(String descendant, String ancestor){
       if (iriMap==null)
          createIndex();
       Concept c=iriMap.get(descendant);
       if (c==null)
          throw new NoSuchElementException("descendant not in this module");
       return isA(c,ancestor);
    }

   /**
    * Tests whether a concept is a descendant of an ancestor, concept test against iri
    * uses standard prefixes in this version
    * @param descendant the descendant concept
    * @param ancestor the ancestor IRI
    * @return true if found false if not a descendant
    */
    public boolean isA(Concept descendant, String ancestor){
       Set<String> done= new HashSet<>();
       if (iriMap==null)
          createIndex();
       if (iriMap.get(ancestor)==null)
          throw new NoSuchElementException("ancestor not found in this module");
       return isA1(descendant,ancestor,done);
    }

   /**
    * Gets a concept from an iri or null if not found
    * @param searchKey the iri or name of the concept you are looking for
    * @return concept, which may be a subtype that may be downcasted
    */
    public Concept getConcept(String searchKey){
       if (iriMap==null)
          createIndex();
       Concept result=iriMap.get(searchKey);
       if (result!=null)
          return result;
       else
          return nameMap.get(searchKey.toLowerCase());
    }

   /**
    * Finds and returns a namespace or null of not found
    * @param iri te iri of the namespace, includin the appended # as part of the prefix
    * @return namespace or null;
    */
    public Namespace getNamespace(String iri){
       if (ontology==null)
          throw new NoSuchElementException("No ontology loaded");
       for (Namespace ns:ontology.getNamespace())
          if (ns.getIri().equals(iri)) {
             return ns;
          }
       return null;
    }

    public Ontology classify() throws OWLOntologyCreationException, FileFormatException {
       if (ontology==null)
          throw new NoSuchElementException("No ontology loaded");
       DiscoveryReasoner reasoner= new DiscoveryReasoner();
       ontology = reasoner.classify(ontology);
       return ontology;
    }
    private boolean isA1(Concept descendant, String ancestor,Set<String> done){
       if (descendant.getIri().equals(ancestor))
          return true;
       boolean isa= false;
       if (descendant.getIsA()!=null)
        for (ConceptReference ref:descendant.getIsA())
           if (ref.getIri().equals(ancestor))
              return true;
           else {
              if (!done.contains(ref.getIri())) {
                 done.add(ref.getIri());
                 Concept parent = iriMap.get(ref.getIri());
                 if (parent != null)
                    isa = isA1(parent, ancestor, done);
                 if (isa)
                    return true;
              }
           }
           return false;
    }

    public static ObjectProperty conceptAsObjectProperty (Concept c){
        return (ObjectProperty) new ObjectProperty().setDbid(c.getDbid())
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

   public Ontology getOntology() {
      return ontology;
   }

   public DOWLManager setOntology(Ontology ontology) {
      this.ontology = ontology;
      return this;
   }
}
