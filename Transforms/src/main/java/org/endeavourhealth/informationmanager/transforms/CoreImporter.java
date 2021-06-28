package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;


public class CoreImporter implements TTImport {
   private static final String[] coreEntities = {
     ".*\\\\SemanticWeb\\\\RDFOntology.json",
     ".*\\\\SemanticWeb\\\\RDFSOntology.json",
     ".*\\\\SemanticWeb\\\\OWLOntology.json",
     ".*\\\\SemanticWeb\\\\SHACLOntology.json",
     ".*\\\\DiscoveryCore\\\\CoreOntologyDocument.json"};




   public CoreImporter validateFiles(String inFolder){
      ImportUtils.validateFiles(inFolder,coreEntities);
      return this;
   }

   @Override
   public TTImport validateLookUps(Connection conn) {
      return this;
   }

   /**
    * Imports the core ontology document
    * @param inFolder root folder containing the Core ontology document
    * @return TTImport object builder pattern
    * @throws Exception invalid document
    */
   public TTImport importData(String inFolder) throws Exception {
      System.out.println("Importing Core entities");
      for (String coreFile : coreEntities) {
         TTManager manager = new TTManager();
         Path path = ImportUtils.findFileForId(inFolder, coreFile);
         manager.loadDocument(path.toFile());
         TTDocumentFiler filer = new TTDocumentFiler(manager.getDocument().getGraph());
         filer.fileDocument(manager.getDocument());
      }
      return this;
   }

   /**
    * Loads the core ontology document, available as TTDocument for various purposes
    * @param inFolder root folder containing core ontology document
    * @return TTDocument containing Discovery ontology
    * @throws IOException in the event of an IO failure
    */
   public TTDocument loadFile(String inFolder) throws IOException {
      Path file = ImportUtils.findFileForId(inFolder, coreEntities[0]);
      TTManager manager= new TTManager();
      TTDocument document = manager.loadDocument(file.toFile());
      return document;

   }


   @Override
   public void close() throws Exception {

   }
}
