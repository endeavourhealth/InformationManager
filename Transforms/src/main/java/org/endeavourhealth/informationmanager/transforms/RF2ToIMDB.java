package org.endeavourhealth.informationmanager.transforms;

import javafx.concurrent.Task;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.informationmanager.OntologyFiler;
import org.endeavourhealth.imapi.model.Ontology;
import org.endeavourhealth.informationmanager.OntologyFilerJDBCDAL;
import org.endeavourhealth.informationmanager.parser.OWLFSParser;

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
         RF2AssertedToDiscovery importer = new RF2AssertedToDiscovery();
         Ontology ontology = importer.importRF2ToDiscovery(args[0]);
         OntologyFiler filer = new OntologyFiler();
         System.out.println("Filing asserted ontology");
         filer.fileOntology(ontology, true);

         RF2RelationshipsToDiscovery inferredImporter = new RF2RelationshipsToDiscovery();
         ontology = inferredImporter.importRF2ToDiscovery(args[0]);
         filer = new OntologyFiler();
         System.out.println("Filing onw ontology");
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
