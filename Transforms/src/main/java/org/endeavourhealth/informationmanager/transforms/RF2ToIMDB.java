package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

import java.util.Arrays;

public class RF2ToIMDB implements Runnable {
   private String inputFolder;

   public RF2ToIMDB(String inputFolder) throws Exception {
      this.inputFolder=inputFolder;
   }
   public static void main(String[] args) throws Exception {
       // fileRdf(args);
       if (args.length != 1) {
           System.err.println("arg 1 = root for Snomed");
           System.exit(-1);
       }

       try {

           long start = System.currentTimeMillis();
           RF2ToTTDocument importer = new RF2ToTTDocument();
           TTDocument document = importer.importRF2(args[0]);
           TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
           System.out.println("Filing Snomed ontology");
           filer.fileDocument(document);

           long end = System.currentTimeMillis();
           long duration = (end - start) / 1000 / 60;

           System.out.println("Duration = " + String.valueOf(duration) + " minutes");
       } catch (Exception e) {
           System.err.println(e.toString());
           Arrays.stream(e.getStackTrace()).forEach(l -> System.err.println(l.toString()));
           throw e;
       }

   }

   @Override
   public void run() {
      String[] args= new String[1];
      args[0]= inputFolder;
      try {
          main(args);
      } catch (Exception e) {
          e.printStackTrace();
      }
   }
}
