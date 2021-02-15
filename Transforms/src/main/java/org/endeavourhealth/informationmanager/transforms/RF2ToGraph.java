package org.endeavourhealth.informationmanager.transforms;

import javafx.concurrent.Task;
import org.endeavourhealth.imapi.model.Ontology;
import org.endeavourhealth.informationmanager.OntologyFiler;

import java.util.Arrays;

public class RF2ToGraph extends Task {
   private String inputFolder;

   public RF2ToGraph(String inputFolder) throws Exception {
      this.inputFolder=inputFolder;
   }
   public static void main(String[] args) throws Exception {
       long start = System.currentTimeMillis();
       try {
           RF2AssertedToDiscovery importer = new RF2AssertedToDiscovery();
           Ontology ontology = importer.importRF2ToDiscovery(args[0]);
           OntologyFiler filer = new OntologyFiler();
           System.out.println("Filing onw ontology");
           filer.fileOntology(ontology, true);

           RF2InferredToDiscovery inferredImporter = new RF2InferredToDiscovery();
           ontology = inferredImporter.importRF2ToDiscovery(args[0]);
           filer = new OntologyFiler();
           System.out.println("Filing onw ontology");
           filer.fileOntology(ontology, true);
       } catch (Exception e) {
           System.err.println(e.toString());
           Arrays.stream(e.getStackTrace()).forEach(l -> System.err.println(l.toString()));
           throw e;
       } finally {
           long end = System.currentTimeMillis();
           long duration = (end - start) / 1000 / 60;

           System.out.println("Duration = " + String.valueOf(duration) + " minutes");
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
