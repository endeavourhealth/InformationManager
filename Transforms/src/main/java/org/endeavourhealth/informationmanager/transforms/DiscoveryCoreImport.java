package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.nio.file.Path;


public class DiscoveryCoreImport {
   private static final String[] CoreConcepts = {".*\\\\DISCOVERY\\\\CoreOntologyDocument.json"};

   public void importCore(String inFolder) throws Exception {
      ImportHelper.validateFiles(inFolder,CoreConcepts);
      System.out.println("Importing Discovery concepts");
      importCoreFile(inFolder);

   }



   private void importCoreFile(String inFolder) throws Exception {
      Path file = ImportHelper.findFileForId(inFolder, CoreConcepts[0]);
      TTManager manager= new TTManager();
      TTDocument document = manager.loadDocument(file.toFile());
      TTDocumentFiler filer= new TTDocumentFiler(document.getGraph());
      filer.fileDocument(document);
   }



}
