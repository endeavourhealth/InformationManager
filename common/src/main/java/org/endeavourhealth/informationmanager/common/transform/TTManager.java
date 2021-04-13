package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.informationmanager.common.Logger;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.*;
import java.util.*;

/**
 * Various utility functions to support triple tree concepts and documents.
 * Create document creates a document with default common prefixes.
 */
public class TTManager {
   private Map<String, TTConcept> conceptMap;
   private Map<String, TTConcept> nameMap;
   private TTDocument document;
   private TTContext context;
   // private List<TTPrefix> defaultPrefixes;
   // private Map<String,String> prefixMap;

   public TTManager(){
       createDefaultContext();
   }
   public TTManager(TTDocument document){
       createDefaultContext();
       this.document= document;
   }

   public TTDocument createDocument(String graph){
        createDefaultContext();
      document = new TTDocument();
      document.setContext(context);
      document.setGraph(TTIriRef.iri(graph));
      return document;
   }

   /**
    * Gets a concept from an iri or null if not found
    * @param searchKey the iri or name of the concept you are looking for
    * @return concept, which may be a subtype that may be downcasted
    */
   public TTConcept getConcept(String searchKey){
      if (conceptMap==null)
         createIndex();
      TTConcept result=conceptMap.get(searchKey);
      if (result!=null)
         return result;
      else {
         if (searchKey.contains(":")) {
            result= conceptMap.get(expand(searchKey));
            if (result!=null)
               return result;
         }

         return nameMap.get(searchKey.toLowerCase());
      }
   }


   /**
       * Gets a concept from an iri or null if not found
    * @param searchKey the iri or name of the concept you are looking for
       * @return concept, which may be a subtype that may be downcasted
    */
   public TTConcept getIndividual(String searchKey){
      if (document.getIndividuals()==null)
         return null;

      String searchIri = expand(searchKey);
      searchKey= searchKey.toLowerCase();
      for (TTConcept indi:document.getIndividuals()){
         if (indi.getIri().equals(searchIri))
            return indi;
         if (indi.getName().equals(searchKey))
            return indi;
      }
      return null;
   }

   public void createDefaultContext() {
       context = new TTContext();
       context.add(IM.NAMESPACE, "im");
       context.add(SNOMED.NAMESPACE, "sn");
       context.add(OWL.NAMESPACE, "owl");
       context.add(RDF.NAMESPACE, "rdf");
       context.add(RDFS.NAMESPACE, "rdfs");
       context.add(XSD.NAMESPACE, "xsd");
       context.add("http://endhealth.info/READ2#", "r2");
       context.add("http://endhealth.info/CTV3#", "ctv3");
       context.add("http://endhealth.info/ICD10#", "icd10");
       context.add("http://endhealth.info/OPCS4#", "opcs4");
       context.add("http://endhealth.info/EMIS#", "emis");
       context.add("http://endhealth.info/TPP#", "tpp");
       context.add("http://endhealth.info/Barts_Cerner#", "bc");
       context.add(SHACL.NAMESPACE, "sh");
       context.add("http://www.w3.org/ns/prov#", "prov");
       context.add("https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-OrganizationRole-1#", "orole");
   }

   /**
    * Loads an information model document file in  JSON-LD/RDF syntax
    * @param inputFile  the file name to load
    * @return the IM triple tree document
    * @throws IOException
    */
   public TTDocument loadDocument (File inputFile) throws IOException {
      ObjectMapper objectMapper = new ObjectMapper();
      document = objectMapper.readValue(inputFile, TTDocument.class);
      return document;
   }

   /**
    * Saves an OWL ontology in functional syntax format
    * @param manager
    * @param outputFile
    * @throws IOException
    */

   public void saveOWLOntology(OWLOntologyManager manager,File outputFile) throws IOException {
      FileWriter writer=new FileWriter(outputFile);
      manager.getOntologies().forEach(o-> {
         try{
            OWLDocumentFormat format= manager.getOntologyFormat(o);
            format.setAddMissingTypes(false);
            o.saveOntology(format,new FileOutputStream(outputFile));

         } catch (IOException | OWLOntologyStorageException e) {
            e.printStackTrace();
         }

      });
   }


   /**
    * Indexes the concepts held in the manager's TTDocument document so they can be quicly retrieced via their IRI.
    */
   public void createIndex(){
      conceptMap = new HashMap();
      nameMap= new HashMap();

      //Loops through the 3 main concept types and add them to the IRI map
      //Note that an IRI may be both a class and a property so both are added
      if (document.getConcepts()!=null)
         document.getConcepts().forEach(p-> {conceptMap.put(p.getIri(),p);
            if (p.getName()!=null)
               nameMap.put(p.getName().toLowerCase(),p);});
   }

   private String expand(String iri) {
       return context.expand(iri);
   }

   public TTDocument getDocument() {
      return document;
   }

   public TTManager setDocument(TTDocument document) {
      this.document = document;
      return this;
   }

   /**
    * Saves the Discovery ontology held by the manager
    * @param outputFile file to save ontology to
    * @throws IOException
    */
   public void saveDocument(File outputFile) throws IOException {
      if (document==null)
         throw new NullPointerException("Manager has no ontology document assigned");
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      String json = objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT,true).writeValueAsString(document);
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
         writer.write(json);
      }
      catch (Exception e) {
         Logger.error("Unable to save ontology in JSON format");
      }

   }


   /**
    * Tests whether a concept is a descendant of an ancestor, concept test against iri
    * uses standard prefixes in this version
    * @param descendant the descendant concept
    * @param ancestor the ancestor IRI
    * @return true if found false if not a descendant
    */
   public boolean isA(TTConcept descendant, TTIriRef ancestor){
      Set<TTIriRef> done= new HashSet<>();
      if (conceptMap==null)
         createIndex();
      if (conceptMap.get(ancestor)==null)
         throw new NoSuchElementException("ancestor not found in this module");
      return isA1(descendant,ancestor,done);
   }

   /**
    * tests isa relationship between two iris.
    * @param descendant
    * @param ancestor
    * @return
    */
   public boolean isA(TTIriRef descendant, TTIriRef ancestor){
      Set<TTIriRef> done= new HashSet<>();
      if (conceptMap==null)
         createIndex();
      TTConcept descendantConcept= conceptMap.get(descendant.getIri());
      if (descendantConcept==null)
        return false;
      if (conceptMap.get(ancestor.getIri())==null)
         return false;
      return isA1(descendantConcept,ancestor,done);
   }
   private boolean isA1(TTConcept descendant, TTIriRef ancestor,Set<TTIriRef> done){
      if (TTIriRef.iri(descendant.getIri()).equals(ancestor))
         return true;
      boolean isa= false;
      if (descendant.get(IM.IS_A)!=null)
         for (TTValue ref:descendant.get(IM.IS_A).asArray().getElements())
            if (ref.equals(ancestor))
               return true;
            else {
               TTIriRef parent= ref.asIriRef();
               if (!done.contains(parent)) {
                  done.add(parent);
                  TTConcept parentConcept = conceptMap.get(parent.getIri());
                  if (parentConcept != null)
                     isa = isA1(parentConcept, ancestor, done);
                  if (isa)
                     return true;
               }
            }
      return false;
   }
}
