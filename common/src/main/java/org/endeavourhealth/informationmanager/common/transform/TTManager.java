package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.Document;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.Ontology;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Various utility functions to support triple tree concepts and documents.
 * Create document creates a document with default common prefixes.
 */
public class TTManager {
   private Map<TTIriRef, TTConcept> conceptMap;
   private Map<String, TTConcept> nameMap;
   private TTDocument document;

   public TTDocument createDocument(String graph){
      document = new TTDocument();
      document.setPrefixes(getDefaultPrefixes());
      document.setGraph(graph);
      return document;
   }


   public static List<TTPrefix> getDefaultPrefixes() {
      List<TTPrefix> ps= new ArrayList<>();
      ps.add(new TTPrefix(IM.NAMESPACE,"im"));
      ps.add(new TTPrefix(SNOMED.NAMESPACE,"sn"));
      ps.add(new TTPrefix(OWL.NAMESPACE,"owl"));
      ps.add(new TTPrefix(RDF.NAMESPACE,"rdf"));
      ps.add(new TTPrefix(RDFS.NAMESPACE,"rdfs"));
      ps.add(new TTPrefix(XSD.NAMESPACE,"xsd"));
      ps.add(new TTPrefix("http://endhealth.info/READ2#","r2"));
      ps.add(new TTPrefix("http://endhealth.info/CTV3#","ctv3"));
      ps.add(new TTPrefix("http://endhealth.info/EMIS#","emis"));
      ps.add(new TTPrefix("http://endhealth.info/TPP#","tpp"));
      ps.add(new TTPrefix("http://endhealth.info/Barts_Cerner#","bc"));
      ps.add(new TTPrefix(SHACL.NAMESPACE,"sh"));
      ps.add(new TTPrefix("http://www.w3.org/ns/prov#","prov"));
      ps.add(new TTPrefix("https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-OrganizationRole-1#","orole"));
      return ps;

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
         document.getConcepts().forEach(p-> {conceptMap.put(TTIriRef.iri(p.getIri()),p);
            if (p.getName()!=null)
               nameMap.put(p.getName().toLowerCase(),p);});
   }


}
