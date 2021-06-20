package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;

import org.endeavourhealth.informationmanager.TTImport;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Imports members for a value set from an RF2 release containing only members of the reference set
 * Current version supports only one value set (Unified test list) for a beta release from TRUD
 */
public class ImportValueSet implements TTImport {

   public static final String[] valueSets = {
       ".*\\\\uk_sct2pt_0.7.0_BETA_.*\\\\SnomedCT_UKEditionRF2_BETA_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_.*\\.txt"};
   private TTDocument document;
   private static final TTIriRef valueSet= TTIriRef.iri(IM.NAMESPACE+"VSET_UnifiedTestList");


   @Override
   public TTImport importData(String inFolder) throws Exception {
      System.out.println("UTL value set not yet imported");
      /*
     TTManager manager= new TTManager();
     document= manager.createDocument(IM.GRAPH_VALUESETS.getIri());
     document.setCrudOperation(IM.UPDATE_PREDICATES);
     document= importEntityList(inFolder,document);
      TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
      filer.fileDocument(document);

       */
     return this;
   }


   private TTDocument importEntityList(String path,TTDocument document) throws IOException {
      int i = 0;
      for (String entityFile : valueSets) {
         Path file = ImportUtils.findFilesForId(path, entityFile).get(0);
         System.out.println("Processing entities in " + file.getFileName().toString());
         TTEntity entity= new TTEntity().setIri(valueSet.getIri());
         document.addEntity(entity);
         try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            reader.readLine();    // Skip header
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
               String[] fields = line.split("\t");
                entity.addObject(IM.HAS_MEMBER,TTIriRef.iri(SNOMED.NAMESPACE+fields[0]));
               i++;
               line = reader.readLine();
            }
         }
      }
      System.out.println("Imported " + i + " entities");
      return document;
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
