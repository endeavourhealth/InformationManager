package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

public class DiscoveryMapsImporter implements TTImport {
   private static final String[] noneCoreConcepts ={ ".*\\\\Discovery\\\\DiscoveryNoneCore\\\\NoneCoreOntology.json"};


   public TTImport importData(String inFolder) throws Exception {
      System.out.println("Importing Discovery concepts");
      importNoneCoreFile(inFolder);
      return this;
   }

   private void importNoneCoreFile(String inFolder) throws Exception {
      Path file = ImportUtils.findFileForId(inFolder, noneCoreConcepts[0]);
      TTManager manager= new TTManager();
      TTDocument document = manager.loadDocument(file.toFile());
      TTDocumentFiler filer= new TTDocumentFiler(document.getGraph());
      filer.fileDocument(document);
   }

   public DiscoveryMapsImporter validateFiles(String inFolder){
      ImportUtils.validateFiles(inFolder,noneCoreConcepts);
      return this;
   }

   @Override
   public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
      return null;
   }


   @Override
   public void close() throws Exception {

   }
}
