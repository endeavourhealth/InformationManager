package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTImportByType;

/**
 * Manager Class which imports specialised data from a legacy classification or the core ontology using specialised importers
 */
public class Importer implements TTImportByType {


   /**
    * Creates a type specific importer and imports and files rthe data
    * @param importType The graph IRI for the particular source data type
    * @param inFolder root folder holding the data files (if any)
    * @return TTImport object for reuse
    * @throws Exception if one of the sources is invalid
    */
   @Override
   public TTImportByType importByType(TTIriRef importType, String inFolder) throws Exception {
      try (TTImport importer= getImporter(importType)){
         importer.validateFiles(inFolder);
         importer.validateLookUps(ImportUtils.getConnection());
         importer.importData(inFolder);
      }
      return null;
   }

   @Override
   public TTImportByType validateByType(TTIriRef importType, String inFolder) throws Exception {
      try (TTImport importer= getImporter(importType)){
         importer.validateFiles(inFolder);
         importer.validateLookUps(ImportUtils.getConnection());
      }
      return this;
   }


   private TTImport getImporter(TTIriRef importType) throws Exception {
      if (IM.GRAPH_DISCOVERY.equals(importType))
         return new CoreImporter();
      else if (IM.GRAPH_SNOMED.equals(importType))
         return new SnomedImporter();
      else if (IM.GRAPH_EMIS.equals(importType))
         return new R2EMISVisionImport();
      else if (IM.GRAPH_CTV3.equals(importType))
         return new CTV3TPPImporter();
      else if (IM.GRAPH_OPCS4.equals(importType))
         return new OPCS4Importer();
      else if (IM.GRAPH_ICD10.equals(importType))
             return new ICD10Importer();
      else if (IM.GRAPH_MAPS_DISCOVERY.equals(importType))
         return new DiscoveryMapsImporter();
      else if (IM.GRAPH_VALUESETS.equals(importType))
         return new ImportValueSet();
      else if (IM.GRAPH_READ2.equals(importType))
         return new R2NHSImport();
      else if (IM.GRAPH_PRSB.equals(importType))
         return new PRSBImport();
      else if (IM.GRAPH_APEX_KINGS.equals(importType))
         return new ApexKingsImport();
      else if (IM.GRAPH_WINPATH_KINGS.equals(importType))
         return new WinPathKingsImport();
      else
         throw new Exception("Unrecognised import type");
   }


}
