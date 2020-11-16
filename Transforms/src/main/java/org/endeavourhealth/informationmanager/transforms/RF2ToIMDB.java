package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.informationmanager.common.transform.DOWLManager;
import org.endeavourhealth.informationmanager.common.transform.EntailmentType;
import org.endeavourhealth.informationmanager.common.transform.OntologyIri;
import org.endeavourhealth.informationmanager.common.transform.OntologyModuleIri;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;

public class RF2ToIMDB {
   public Ontology importRF2(String inputFolder) throws Exception {

      Ontology ontology = DOWLManager.createOntology(
          OntologyIri.DISCOVERY.getValue(),
          OntologyModuleIri.SNOMED.getValue()
      );

      RF2ToDiscovery.validateFiles(inputFolder);
      RF2ToDiscovery.importConceptFiles(inputFolder, ontology);
      RF2ToDiscovery.importDescriptionFiles(inputFolder, ontology);
      RF2ToDiscovery.importRelationshipFiles(inputFolder, ontology);
      RF2ToDiscovery.importMRCMDomainFiles(inputFolder);
      RF2ToDiscovery.importMRCMRangeFiles(inputFolder);
      return ontology;
   }

   private void importMCRM() throws Exception
}
