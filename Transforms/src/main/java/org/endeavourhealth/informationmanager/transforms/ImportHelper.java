package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class ImportHelper {
   public static final String[] concepts = {
       ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_INT_.*\\.txt",
       ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_.*\\.txt",
       ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Concept_Snapshot_.*\\.txt",
   };

   public static final String[] refsets = {
       ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_INT_.*\\.txt",
       ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_.*\\.txt",
       ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Language\\\\der2_cRefset_LanguageSnapshot-en_.*\\.txt",
   };

   public static final String[] descriptions = {

       ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_INT_.*\\.txt",
       ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_.*\\.txt",
       ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Description_Snapshot-en_.*\\.txt",
   };

   public static final String[] relationships = {
       ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Relationship_Snapshot_INT_.*\\.txt",
       ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Relationship_Snapshot_.*\\.txt",
       ".*\\\\SnomedCT_UKDrugRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_Relationship_Snapshot_.*\\.txt",
   };

   public static final String[] substitutions = {
       ".*\\\\SnomedCT_UKClinicalRF2_PRODUCTION_.*\\\\Resources\\\\HistorySubstitutionTable\\\\xres2_HistorySubstitutionTable_.*\\.txt",
   };

   public static final String[] attributeRanges = {
       ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Metadata\\\\der2_ssccRefset_MRCMAttributeRangeSnapshot_INT_.*\\.txt",
   };

   public static final String[] attributeDomains = {
       ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Refset\\\\Metadata\\\\der2_cissccRefset_MRCMAttributeDomainSnapshot_INT_.*\\.txt",
   };
   public static final String[] statedAxioms = {
       ".*\\\\SnomedCT_InternationalRF2_PRODUCTION_.*\\\\Snapshot\\\\Terminology\\\\sct2_sRefset_OWLExpressionSnapshot_INT_.*\\.txt"
   };
   private static final String[] EMISConcepts = {".*\\\\EMIS\\\\EMISCodes.csv"};

   public static void addMap(TTConcept c, TTIriRef assuranceLevel,String target, String targetTermCode,
                             TTIriRef matchType, Integer priority, String advice) {
      if (matchType==null)
         matchType= IM.MATCHED_TO;
      TTValue maps= c.get(IM.HAS_MAP);
      if (maps==null) {
         maps = new TTArray();
         c.set(IM.HAS_MAP, maps);
      }
      TTNode map= new TTNode();
      maps.asArray().add(map);
      map.set(iri(IM.NAMESPACE+"assuranceLevel"),assuranceLevel);
      map.set(matchType,iri(target));
      if (targetTermCode!=null)
         if (!targetTermCode.equals(""))
            map.set(IM.MATCHED_TERM_CODE,literal(targetTermCode));
      if (advice!=null)
         map.set(TTIriRef.iri(IM.NAMESPACE+ "mapAdvice"),TTLiteral.literal(advice));
      if (priority!=null)
         map.set(TTIriRef.iri(IM.NAMESPACE+"mapPriority"),TTLiteral.literal(priority));
   }

   public static void validateSources(String path) throws SQLException, ClassNotFoundException {
      validateFiles(path,concepts, descriptions,
          relationships, refsets, attributeRanges, attributeDomains,substitutions,EMISConcepts);
      try {
         Connection conn = IMConnection.getConnection();
         PreparedStatement getTPP = conn.prepareStatement("Select ctv3_code from tpp_ctv3_lookup_2 limit 1");
         ResultSet rs = getTPP.executeQuery();
         if (!rs.next()) {
            System.err.println("No tpp look up table (tpp_ctv3_lookup_2)");
            System.exit(-1);
         }
         PreparedStatement getTPPs = conn.prepareStatement("Select ctv3_code from tpp_ctv3_to_snomed limit 1");
         rs = getTPPs.executeQuery();
         if (!rs.next()) {
            System.err.println("No TPP Snomed look up table (tpp_ctv3_to_snomed)");
            System.exit(-1);
         }
         PreparedStatement getVision = conn.prepareStatement("Select read_code from vision_read2_code limit 1");
         rs = getVision.executeQuery();
         if (!rs.next()) {
            System.err.println("No Vision read look up table (vision_read2_code)");
            System.exit(-1);
         }
         PreparedStatement getVisions = conn.prepareStatement("Select read_code from vision_read2_to_snomed_map limit 1");
         rs = getVisions.executeQuery();
         if (!rs.next()) {
            System.err.println("No Vision Snomed look up table (vision_read2_to_snomed_map)");
            System.exit(-1);
         }
      } catch (SQLException e){
         System.err.println("The vision or tpp look up tables are not there");
         throw e;
      }

   }


   /**
    * Validates the presence of various files from a root folder path

    * @param path  the root folder that holds the files as subfolders
    * @throws IOException if the files are not all present
    */
   public static void validateFiles(String path,String[] ... values) {
      Arrays.stream(values).sequential().forEach(fileArray ->
          Arrays.stream(fileArray).sequential().forEach(file-> {
         try {
            Path p = findFileForId(path, file);
            System.out.println("Found " + p.toString());
         } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
         }
      }
          )
      );
   }

   public static Path findFileForId(String path, String regex) throws IOException {
      List<Path> paths = Files.find(Paths.get(path), 16,
          (file, attr) -> file.toString().matches(regex))
          .collect(Collectors.toList());

      if (paths.size() == 1)
         return paths.get(0);

      if (paths.isEmpty())
         throw new IOException("No files found in [" + path + "] for expression [" + regex + "]");
      else
         throw new IOException("Multiple files found in [" + path + "] for expression [" + regex + "]");
   }

   public static List<Path> findFilesForId(String path, String regex) throws IOException {
      return Files.find(Paths.get(path), 16,
          (file, attr) -> file.toString()
              .matches(regex))
          .collect(Collectors.toList());
   }



}
