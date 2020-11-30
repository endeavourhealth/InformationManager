package org.endeavourhealth.informationmanager.transforms;

import javafx.concurrent.Task;
import org.endeavourhealth.imapi.model.TermConcept;
import org.endeavourhealth.informationmanager.OntologyFiler;
import org.endeavourhealth.imapi.model.Ontology;
import org.endeavourhealth.informationmanager.OntologyFilerJDBCDAL;

import java.util.Arrays;
import java.util.List;

public class RF2ToIMDB extends Task {
   private String inputFolder;

   public RF2ToIMDB(String inputFolder) throws Exception {
      this.inputFolder=inputFolder;
   }
   public static void main(String[] args) throws Exception {
      try {
         long start = System.currentTimeMillis();
         RF2ToDiscovery importer = new RF2ToDiscovery();
         Ontology ontology;
         if (args.length==1)
            ontology = importer.importRF2ToDiscovery(args[0]);
         else
            ontology = importer.importRF2ToDiscovery(args[0],args[1]);
         OntologyFiler filer = new OntologyFiler();
         filer.fileOntology(ontology, true);
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
