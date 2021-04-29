package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LegacyTermsImport {
   private static final String[] NoneCoreConcepts ={ ".*\\\\DISCOVERY\\\\NoneCoreOntologyDocument.json"};


   public void importTermCodes(String inFolder) throws Exception {
      ImportHelper.validateFiles(inFolder,NoneCoreConcepts);
      System.out.println("Importing Discovery concepts");
      importNoneCoreFile(inFolder);
   }

   private void importNoneCoreFile(String inFolder) throws Exception {
      Path file = ImportHelper.findFileForId(inFolder, NoneCoreConcepts[0]);
      TTManager manager= new TTManager();
      TTDocument document = manager.loadDocument(file.toFile());
      TTDocumentFiler filer= new TTDocumentFiler(document.getGraph());
      filer.fileDocument(document);
   }


}
