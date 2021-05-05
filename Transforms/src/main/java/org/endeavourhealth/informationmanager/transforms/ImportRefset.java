package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.TTImportByType;

import java.sql.Connection;
import java.sql.SQLException;

public class ImportRefset implements TTImport {

   public static final String[] valueSets = {
       ".*\\\\uk_sct2pt_0.7.0_BETA_.*\\\\SnomedCT_UKEditionRF2_BETA_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_.*\\.txt"};


   @Override
   public TTImport importData(String inFolder) throws Exception {
     return this;
   }

   @Override
   public TTImport validateFiles(String inFolder) {
      ImportUtils.validateFiles(inFolder,valueSets);
      return this;
   }

   @Override
   public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
      return this;
   }

   @Override
   public void close() throws Exception {

   }
}
