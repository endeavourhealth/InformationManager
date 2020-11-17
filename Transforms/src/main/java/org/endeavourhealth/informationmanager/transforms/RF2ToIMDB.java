package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.TermConcept;
import org.endeavourhealth.informationmanager.OntologyFiler;
import org.endeavourhealth.informationmanager.common.transform.DOWLManager;
import org.endeavourhealth.informationmanager.common.transform.EntailmentType;
import org.endeavourhealth.informationmanager.common.transform.OntologyIri;
import org.endeavourhealth.informationmanager.common.transform.OntologyModuleIri;
import org.endeavourhealth.imapi.model.Ontology;

import java.util.Arrays;
import java.util.List;

public class RF2ToIMDB {
   public static void main(String[] args) throws Exception {
      try {
         RF2ToDiscovery importer = new RF2ToDiscovery();
         Ontology ontology = importer.importRF2ToDiscovery(args[0]);
         OntologyFiler filer = new OntologyFiler();
         filer.bulkStartOptimise();
         filer.fileOntology(ontology);
         filer = new OntologyFiler();
         //filer.bulkStartOptimise();
         //filer.fileClassification(ontology.getConcept(), ontology.getModule());
         //List<TermConcept> terms = importer.importRF2Terms(args[0]);
         //filer = new OntologyFiler();
         //filer.fileTerms(terms);
      } catch (Exception e){
         System.err.println(e.toString());
         Arrays.stream(e.getStackTrace()).forEach(l-> System.err.println(l.toString()));
      }
   }


}
