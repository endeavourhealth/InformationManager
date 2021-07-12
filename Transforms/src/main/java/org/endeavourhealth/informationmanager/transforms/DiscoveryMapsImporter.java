package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTDocumentFilerJDBC;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class DiscoveryMapsImporter implements TTImport {
   private static final String[] noneCoreEntities ={ ".*\\\\DiscoveryNoneCore\\\\NoneCoreOntology.json"};


   public TTImport importData(String inFolder, boolean bulkImport, Map<String,Integer> entityMap) throws Exception {
      System.out.println("Importing Discovery entities");
      importNoneCoreFile(inFolder,bulkImport,entityMap);
      return this;
   }

   private void importNoneCoreFile(String inFolder, boolean bulkImport, Map<String,Integer> entityMap) throws Exception {
      Path file = ImportUtils.findFileForId(inFolder, noneCoreEntities[0]);
      TTManager manager= new TTManager();
      TTDocument document = manager.loadDocument(file.toFile());
      TTDocumentFiler filer= new TTDocumentFilerJDBC();
      filer.fileDocument(document,bulkImport,entityMap);
   }

   public DiscoveryMapsImporter validateFiles(String inFolder){
      ImportUtils.validateFiles(inFolder,noneCoreEntities);
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
