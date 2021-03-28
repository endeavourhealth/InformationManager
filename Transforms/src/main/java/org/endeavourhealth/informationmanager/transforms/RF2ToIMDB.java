package org.endeavourhealth.informationmanager.transforms;

import javafx.concurrent.Task;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.ClosureGenerator;
import org.endeavourhealth.imapi.model.Ontology;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

import java.util.Arrays;

public class RF2ToIMDB extends Task {
   private String inputFolder;

   public RF2ToIMDB(String inputFolder) throws Exception {
      this.inputFolder=inputFolder;
   }
   public static void main(String[] args) throws Exception {
     // fileRdf(args);

      try {


         long start = System.currentTimeMillis();
         RF2ToTTDocument importer = new RF2ToTTDocument();
         TTDocument document= importer.importRF2(args[0]);
         boolean noDelete=false;
         if (args[1].equalsIgnoreCase("nodelete"))
            noDelete = true;
         TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
         System.out.println("Filing Snomed ontology");
         filer.fileDocument(document);
         System.out.println("Building closure table");
         ClosureGenerator builder= new ClosureGenerator();
         builder.generateClosure(args[2]);


         long end =System.currentTimeMillis();
         long duration = (end-start)/1000/60;

         System.out.println("Duration = "+ String.valueOf(duration)+" minutes");
      } catch (Exception e){
         System.err.println(e.toString());
         Arrays.stream(e.getStackTrace()).forEach(l-> System.err.println(l.toString()));
         throw e;
      }

   }

   @Override
   protected Object call() throws Exception {
      String[] args= new String[1];
      args[0]= inputFolder;
      main(args);
      return null;
   }
}
