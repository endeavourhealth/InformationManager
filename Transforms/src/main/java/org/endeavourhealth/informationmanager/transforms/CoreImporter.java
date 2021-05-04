package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.nio.file.Path;
import java.sql.Connection;


public class CoreImporter implements TTImport {
   private static final String[] CoreConcepts = {".*\\\\CoreOntologyDocument.json"};



   public CoreImporter validateFiles(String inFolder){
      ImportUtils.validateFiles(inFolder,CoreConcepts);
      return this;
   }

   @Override
   public TTImport validateLookUps(Connection conn) {
      return null;
   }


   public TTImport importData(String inFolder) throws Exception {
      System.out.println("Importing Discovery concepts");
      Path file = ImportUtils.findFileForId(inFolder, CoreConcepts[0]);
      TTManager manager= new TTManager();
      TTDocument document = manager.loadDocument(file.toFile());
      TTDocumentFiler filer= new TTDocumentFiler(document.getGraph());
      filer.fileDocument(document);
      return this;
   }


   @Override
   public void close() throws Exception {

   }
}
