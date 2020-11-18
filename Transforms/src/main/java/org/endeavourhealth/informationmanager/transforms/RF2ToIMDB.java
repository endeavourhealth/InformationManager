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
   public void importRF2(String[] args) throws Exception {
      try {
         OntologyFilerJDBCDAL dal= new OntologyFilerJDBCDAL();
         dal.dropIndexes();
         dal.close();
         RF2ToDiscovery importer = new RF2ToDiscovery();
         Ontology ontology = importer.importRF2ToDiscovery(args[0]);
         OntologyFiler filer = new OntologyFiler();
         filer.fileOntology(ontology);
         filer = new OntologyFiler();
         filer.fileClassification(ontology.getConcept(), ontology.getModule());
         List<TermConcept> terms = importer.importRF2Terms(args[0]);
         filer = new OntologyFiler();
         filer.fileTerms(terms);
         dal= new OntologyFilerJDBCDAL();
         dal.restoreIndexes();
         dal.close();
      } catch (Exception e){
         System.err.println(e.toString());
         Arrays.stream(e.getStackTrace()).forEach(l-> System.err.println(l.toString()));
      }
   }


   @Override
   protected Object call() throws Exception {
      String[] args= new String[1];
      args[0]= inputFolder;
      importRF2(args);
      return null;
   }
}
