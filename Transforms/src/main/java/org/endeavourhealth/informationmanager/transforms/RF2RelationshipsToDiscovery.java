package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.informationmanager.common.transform.DOWLManager;
import org.endeavourhealth.informationmanager.common.transform.ECLToDiscovery;
import org.endeavourhealth.informationmanager.common.transform.OWLToDiscovery;
import org.endeavourhealth.informationmanager.common.transform.OntologyIri;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

public class RF2RelationshipsToDiscovery {
      private String country;
      private Map<String, Concept> conceptMap;
      private OWLOntologyManager owlManager;
      private Set<String> clinicalPharmacyRefsetIds = new HashSet<>();
      private ECLToDiscovery eclConverter = new ECLToDiscovery();
      private Ontology ontology;




      private Map<String, SnomedMeta> idMap = new HashMap<>(1000000);

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


      public static final String FULLY_SPECIFIED = "900000000000003001";
      public static final String CLINICAL_REFSET = "999001261000000100";
      public static final String PHARMACY_REFSET = "999000691000001104";
      public static final String NECESSARY_INSUFFICIENT = "900000000000074008";
      public static final String IS_A = "116680003";
      public static final String SNOMED_ROOT = "sn:138875005";
      public static final String HIERARCHY_POSITION = ":CM_ValueTerminology";
      public static final String CODE_SCHEME = ":891101000252101";
      public static final String SN = "sn:";
      public static final String ROLE_GROUP = "sn:609096000";
      public static final String MEMBER_OF = "sn:394852005";
      public static final String ALL_CONTENT = "723596005";
      public static final String ACTIVE = "1";
      public static final String REPLACED_BY = "370124000";
      public static final String IN_ROLE_GROUP_OF = ":inRoleGroupOf";
      private Integer axiomCount;





      //======================PUBLIC METHODS============================

      /**
       * A per country method of loading e.g. INT or UK, used mostly for utility. It is better to use the country neutral method
       *
       * @param inFolder input route folder containining the RF2 elease folders
       * @param country  a string that differentiates one county or import from another e.g. uk INT UKClinical
       * @return Discovery Ontology object containing the Snomed ontology
       * @throws Exception
       */
      public Ontology importRF2ToDiscovery(String inFolder, String country) throws Exception {
         this.country = country.toLowerCase();
         return importRF2ToDiscovery(inFolder);
      }

      /**
       * Loads a multi country RF2 release package into a Discovery ontology will process international followed by uk clinical
       * followed by uk drug. Loads MRCM models also. Does not load reference sets.
       *
       * @param inFolder root folder containing the RF2 folders
       * @return Discovery ontology
       * @throws Exception
       */

      public Ontology importRF2ToDiscovery(String inFolder) throws Exception {
         validateFiles(inFolder);
         conceptMap = new HashMap<>();

         DOWLManager manager = new DOWLManager();
         ontology = manager.createOntology(
             OntologyIri.DISCOVERY.getValue());

         importRelationshipFiles(inFolder);

         return ontology;
      }




      /**
       * Validates the presence of the various RF2 files from a root folder path
       * Note to include the history substitution file
       *
       * @param path
       * @throws IOException if the files are not all present
       */
      public void validateFiles(String path) throws IOException {
         String[] files = Stream.of(relationships)
             .flatMap(Stream::of)
             .toArray(String[]::new);

         for (String file : files) {
            List<Path> matches = findFilesForId(path, file);
            if (matches.size() != 1) {
               System.err.println("Could not find " + file + " in " + path);
               throw new IOException("No RF2 files in inout directory");
            } else {
               System.out.println("Found: " + matches.get(0).toString());
            }

         }
      }


      //=================private methods========================



      private boolean isCountry(Path file) {
         if (country == null)
            return true;
         if (file.toString().toLowerCase().contains(country))
            return true;
         else return false;
      }



      private void importRelationshipFiles(String path) throws IOException {
         int i = 0;
         for (String relationshipFile : relationships) {
            Path file = findFilesForId(path, relationshipFile).get(0);
            if (isCountry(file)) {
               System.out.println("Processing relationships in " + file.getFileName().toString());
               try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
                  String line = reader.readLine(); // Skip header
                  line = reader.readLine();
                  while (line != null && !line.isEmpty()) {
                     String[] fields = line.split("\t");
                     //if (fields[4].equals("73211009")) {
                     Concept c = conceptMap.get(SN+ fields[4]);
                     if (c==null){
                        c= new Concept();
                        c.setIri(SN+ fields[4]);
                        c.setRef(true);
                        ontology.addConcept(c);
                        conceptMap.put(SN+ fields[4],c);
                     }
                     int group = Integer.parseInt(fields[6]);
                     String relationship = fields[7];
                     String target = fields[5];
                     if (ACTIVE.equals(fields[2]) | (relationship.equals(REPLACED_BY))) {
                        addRelationship(c, group, relationship, target);
                        //  }
                     }
                     i++;
                     line = reader.readLine();
                  }
               }

            }
         }
         System.out.println("Imported " + i + " relationships");
      }

      private void addRelationship(Concept c, int group, String relationship, String target) {
         if (relationship.equals(IS_A) || relationship.equals(REPLACED_BY)) {
            c.addIsa(new ConceptReference(SN + target));
            if (relationship.equals(REPLACED_BY)) {
               Concept replacement = conceptMap.get(SN + target);
               if (replacement == null) {
                  replacement = new Concept();
                  replacement.setIri(SN + target);
                  replacement.setRef(true);
                  replacement.addIsa(new ConceptReference(c.getIri()));
                  ontology.addConcept(replacement);
                  conceptMap.put(SN + target, replacement);
               }
            }
         } else {
            if (group == 0) {
               c.addRole(getRole(relationship, target));
            } else {
               ConceptRole roleGroup = getRoleGroup(c, group);
               roleGroup.addSubrol(getRole(relationship, target));
            }
         }
      }
      private ConceptRole getRole(String relationship,String target){
            ConceptRole subrole= new ConceptRole();
            subrole.setProperty(new ConceptReference(SN+ relationship));
            subrole.setValueType(new ConceptReference(SN+ target));
            return subrole;
         }

      private ConceptRole getRoleGroup(Concept c, int group) {
         if (c.getRole()!=null){
            for (ConceptRole r:c.getRole())
               if (r.getGroupNumber()==group)
                  return r;

         }
         ConceptRole newGroup= new ConceptRole();
         newGroup.setGroupNumber(group);
         newGroup.setProperty(new ConceptReference(ROLE_GROUP));
         c.addRole(newGroup);
         return newGroup;

      }



      private List<Path> findFilesForId(String path, String regex) throws IOException {
         return Files.find(Paths.get(path), 16,
             (file, attr) -> file.toString()
                 .matches(regex))
             .collect(Collectors.toList());
      }


   }
