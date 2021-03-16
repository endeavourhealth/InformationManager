package org.endeavourhealth.informationmanager.transforms;
import org.eclipse.rdf4j.model.vocabulary.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.common.transform.*;
import org.endeavourhealth.imapi.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

public class RF2ToTTDocument {
   private String country;
   private Map<String, TTConcept> conceptMap;
   private Set<String> clinicalPharmacyRefsetIds = new HashSet<>();
   private ECLToTT eclConverter = new ECLToTT();
   private TTDocument document;
   private Map<String,String> prefixMap = new HashMap<>();



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
   public static final String ROLE_GROUP = ":roleGroup";
   public static final String MEMBER_OF = "sn:394852005";
   public static final String ALL_CONTENT = "723596005";
   public static final String ACTIVE = "1";
   public static final String REPLACED_BY = "370124000";
   public static final String IN_ROLE_GROUP_OF = ":inRoleGroupOf";
   private Integer axiomCount;





   //======================PUBLIC METHODS============================


   /**
    * Loads a multi country RF2 release package into a Discovery ontology will process international followed by uk clinical
    * followed by uk drug. Loads MRCM models also. Does not load reference sets.
    *
    * @param inFolder root folder containing the RF2 folders
    * @return Discovery ontology
    * @throws Exception
    */

   public TTDocument importRF2(String inFolder) throws Exception {
      validateFiles(inFolder);
      conceptMap = new HashMap<>();

      document= TTManager.createDocument(SNOMED.GRAPH.getIri());


      setPrefixMap();
      importConceptFiles(inFolder);
      importDescriptionFiles(inFolder);
      importMRCMRangeFiles(inFolder);
      importRefsetFiles(inFolder);
      importMRCMDomainFiles(inFolder);
      importStatedFiles(inFolder);
      importRelationshipFiles(inFolder);

      return document;
   }

   private void setPrefixMap() {
      for (TTPrefix iri:document.getPrefixes())
         prefixMap.put(iri.getPrefix(),iri.getIri());
      prefixMap.put("",SNOMED.NAMESPACE);
   }


   /**
    * Validates the presence of the various RF2 files from a root folder path
    * Note to include the history substitution file
    *
    * @param path
    * @throws IOException if the files are not all present
    */
   public void validateFiles(String path) throws IOException {
      String[] files = Stream.of(concepts, descriptions,
          relationships, refsets, attributeRanges, attributeDomains)
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

   private void importConceptFiles(String path) throws IOException {
      int i = 0;
      for (String conceptFile : concepts) {
         Path file = findFilesForId(path, conceptFile).get(0);
         if (isCountry(file)) {
            System.out.println("Processing concepts in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
               String line = reader.readLine();    // Skip header
               line = reader.readLine();
               while (line != null && !line.isEmpty()) {
                  String[] fields = line.split("\t");
                  if (!conceptMap.containsKey(fields[0])) {
                     TTConcept c = new TTConcept();
                     c.setIri(SNOMED.NAMESPACE+fields[0]);
                     c.set(IM.CODE, TTLiteral.literal(fields[0]));
                     c.set(IM.MODELTYPE, OWL.CLASS);
                     c.set(RDF.TYPE,new TTArray().add(OWL.CLASS));
                     c.set(IM.HAS_SCHEME,IM.CODE_SCHEME_SNOMED);
                     c.set(IM.STATUS,ACTIVE.equals(fields[2]) ? IM.ACTIVE : IM.INACTIVE);
                     document.addConcept(c);
                     conceptMap.put(fields[0],c);
                  }
                  i++;
                  line = reader.readLine();
               }
            }
         }
      }
      System.out.println("Imported " + i + " concepts");
   }

   private void importRefsetFiles(String path) throws IOException {
      int i = 0;
      for (String refsetFile : refsets) {
         List<Path> paths = findFilesForId(path, refsetFile);
         Path file = paths.get(0);
         System.out.println("Processing refsets in " + file.getFileName().toString());
         try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();    // Skip header
            line = reader.readLine();
            while (line != null && !line.isEmpty()) {
               String[] fields = line.split("\t");

               if (!clinicalPharmacyRefsetIds.contains(fields[5])) {
                  if (ACTIVE.equals(fields[2])
                      && (
                      CLINICAL_REFSET.equals(fields[4]) || PHARMACY_REFSET.equals(fields[4])
                  )) {
                     clinicalPharmacyRefsetIds.add(fields[5]);

                  }
               }
               i++;
               line = reader.readLine();
            }

         }

      }
      System.out.println("Imported " + i + " refset");
   }

   private void importDescriptionFiles(String path) throws IOException, DataFormatException {
      int i = 0;
      for (String descriptionFile : descriptions) {

         Path file = findFilesForId(path, descriptionFile).get(0);
         if (isCountry(file)) {
            System.out.println("Processing  descriptions in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
               String line = reader.readLine(); // Skip header
               line = reader.readLine();
               while (line != null && !line.isEmpty()) {
                  String[] fields = line.split("\t");
                  TTConcept c = conceptMap.get(fields[4]);
                  if (c == null)
                     throw new DataFormatException(fields[4] + " not recognised as concept");
                  if (fields[7].contains("(attribute)")) {
                     c.set(IM.MODELTYPE, OWL.OBJECTPROPERTY);
                     c.get(RDF.TYPE).asArray().add(OWL.OBJECTPROPERTY);
                  }
                  if (FULLY_SPECIFIED.equals(fields[6])
                      && ACTIVE.equals(fields[2])
                      && c != null) {
                     c.set(RDFS.LABEL,TTLiteral.literal(fields[7]));
                  }
                  if (!FULLY_SPECIFIED.equals(fields[6]))
                     if (ACTIVE.equals(fields[2])) {
                        TTNode termCode= new TTNode();
                        c.set(IM.HAS_SYNONYM,termCode);
                        termCode.set(IM.CODE,TTLiteral.literal(fields[0]));
                        termCode.set(RDFS.LABEL,TTLiteral.literal(fields[7]));
                     }
                  i++;
                  line = reader.readLine();
               }
            }

         }
      }
      System.out.println("Imported " + i + " descriptions");
   }

   private boolean isCountry(Path file) {
      if (country == null)
         return true;
      if (file.toString().toLowerCase().contains(country))
         return true;
      else return false;
   }


   private void importStatedFiles(String path) throws IOException {
      int i = 0;
      OWLToTT owlConverter= new OWLToTT();
      for (String relationshipFile : statedAxioms) {
         Path file = findFilesForId(path, relationshipFile).get(0);
         if (isCountry(file)) {
            System.out.println("Processing owl expressions in " + file.getFileName().toString());
            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
               String line = reader.readLine(); // Skip header
               line = reader.readLine();
               axiomCount = 0;
               while (line != null && !line.isEmpty()) {
                  String[] fields = line.split("\t");
                  TTConcept c = conceptMap.get(fields[5]);
                  String axiom = fields[6];
                  if (!axiom.startsWith("Prefix"))
                     if (ACTIVE.equals(fields[2]))
                        if (!axiom.startsWith("Ontology"))
                           try {
                              //System.out.println(c.getIri());
                              owlConverter.convertAxiom(c, axiom,prefixMap);
                           } catch (Exception e){
                              System.err.println(e.getStackTrace());
                              throw new IOException("owl parser error");
                           }
                  i++;
                  line = reader.readLine();
               }
            } catch (Exception e){
               System.err.println(e.getStackTrace());
               throw new IOException("stated file input problem");
            }

         }
      }

      System.out.println("Imported " + i + " OWL Axioms");
   }





   private static String getHeader(){
      String header="Prefix(bc:=<http://www.EndeavourHealth.org/InformationModel/Legacy/Barts_Cerner#>)\n"
          +"Prefix(:=<http://snomed.info/sct#>)\n"
          +"Prefix(owl:=<http://www.w3.org/2002/07/owl#>)\n"
          +"Prefix(rdf:=<http://www.w3.org/1999/02/22-rdf-syntax-ns#>)\n"
          +"Prefix(xml:=<http://www.w3.org/XML/1998/namespace#>)\n"
          +"Prefix(xsd:=<http://www.w3.org/2001/XMLSchema#>)\n"
          +"Prefix(rdfs:=<http://www.w3.org/2000/01/rdf-schema#>)\n"
          +"Ontology(<http://snomed.info/sct>\n";
      return header;
   }







   private List<Path> findFilesForId(String path, String regex) throws IOException {
      return Files.find(Paths.get(path), 16,
          (file, attr) -> file.toString()
              .matches(regex))
          .collect(Collectors.toList());
   }

   private void importMRCMDomainFiles(String path) throws IOException {
      int i = 0;

      //gets attribute domain files (usually only 1)
      for (String domainFile : attributeDomains) {
         Path file = findFilesForId(path, domainFile).get(0);
         System.out.println("Processing property domains in " + file.getFileName().toString());
         try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();    // Skip header
            line = reader.readLine();
            while (line != null && !line.isEmpty()) {
               String[] fields = line.split("\t");
               //Only process axioms relating to all snomed authoring
               if (fields[11].equals(ALL_CONTENT)) {
                  TTConcept op = conceptMap.get(fields[5]);
                  addSnomedPropertyDomain(op, fields[6], Integer.parseInt(fields[7])
                      , fields[8], fields[9], ConceptStatus.byValue(Byte.parseByte(fields[2])));
               }
               i++;
               line = reader.readLine();
            }
         }
      }
      System.out.println("Imported " + i + " property domain axioms");
   }

   private void importMRCMRangeFiles(String path) throws IOException {
      int i = 0;
      //gets attribute range files (usually only 1)
      for (String rangeFile : attributeRanges) {
         Path file = findFilesForId(path, rangeFile).get(0);
         System.out.println("Processing property ranges in " + file.getFileName().toString());
         try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line = reader.readLine();    // Skip header
            line = reader.readLine();
            while (line != null && !line.isEmpty()) {
               String[] fields = line.split("\t");
               if (fields[2].equals("1")) {
                  TTConcept op = conceptMap.get(fields[5]);
                  addSnomedPropertyRange(op, fields[6]);
               }
               i++;
               line = reader.readLine();
            }
         }
      }
      System.out.println("Imported " + i + " property range axioms");
   }

   private void addSnomedPropertyRange(TTConcept op, String ecl) {
      TTValue expression= eclConverter.getClassExpression(ecl);
      TTValue already= op.get(RDFS.RANGE);
      if (already==null) {
         op.set(RDFS.RANGE,expression);
      } else {
         if (already.isIriRef()) {
            TTNode range = new TTNode();
            op.set(RDFS.RANGE, range);
            TTArray unions = new TTArray();
            range.set(OWL.UNIONOF, unions);
         }
         TTArray alreadyUnions = already.asNode().get(OWL.UNIONOF).asArray();
         if (expression.isIriRef()) {
            if (!alreadyInUnion(alreadyUnions, expression.asIriRef())) {
               alreadyUnions.add(expression);
            }
         } else {
            TTArray newUnions = expression.asNode().get(OWL.UNIONOF).asArray();
            for (TTValue newRange : newUnions.getElements()) {
               if (!alreadyInUnion(alreadyUnions, newRange.asIriRef()))
                  alreadyUnions.add(newRange);
            }
         }
      }
   }

   private boolean alreadyInUnion(TTArray alreadyUnions, TTIriRef newRange) {
      for (TTValue union : alreadyUnions.getElements()) {
         if (union.asIriRef() == newRange) {
            return true;
         }
      }
       return false;
   }

   private void addToRangeAxiom(ClassExpression rangeAx, ClassExpression ce){
      if (rangeAx.getUnion()!=null) {
         if (!duplicateRange(rangeAx,ce))
            rangeAx.addUnion(ce);
      }
      else {
         if (rangeAx.getClazz() == null) {
            if (ce.getClazz() != null) {
               rangeAx.setClazz(ce.getClazz());
            }
            else if (ce.getObjectOneOf()!=null){
               rangeAx.setObjectOneOf(ce.getObjectOneOf());
            }
            else {
               for (ClassExpression inter : ce.getIntersection())
                  rangeAx.addIntersection(inter);
            }
         } else {
            if (ce.getClazz() != rangeAx.getClazz()) {
               String Concept = rangeAx.getClazz().getIri();
               rangeAx.setClazz((ConceptReference) null);
               ClassExpression union = new ClassExpression();
               union.setClazz(new ConceptReference(Concept));
               rangeAx.addUnion(union);
               rangeAx.addUnion(ce);
            }
         }
      }

   }

   private boolean duplicateRange(ClassExpression rangeAx, ClassExpression ce) {
      boolean result = false;
      if (ce.getClazz()!=null)
         for (ClassExpression oldEx:rangeAx.getUnion())
            if (oldEx.getClazz()!=null)
               if (oldEx.getClazz().equals(ce.getClazz()))
                  result= true;
      return result;
   }


   private void addSnomedPropertyDomain(TTConcept op, String domain,
                                           Integer inGroup, String card,
                                           String cardInGroup, ConceptStatus status) {
      //Assumes all properties are in a group
      //therefore groups are not modelled in this version
      TTIriRef imDomain = TTIriRef.iri(SN + domain);
      //Is it the first entry
      TTValue already = op.get(RDFS.DOMAIN);
      if (already == null) {
         op.set(RDFS.DOMAIN, imDomain);
      } else {
         if (already.isIriRef()) {
            TTNode expression = new TTNode();
            op.set(RDFS.DOMAIN, expression);
            TTArray unions = new TTArray();
            expression.set(OWL.UNIONOF, unions);
            unions.add(already);
            unions.add(imDomain);
         } else {
            TTArray unions = already.asNode().get(OWL.UNIONOF).asArray();
            unions.add(imDomain);
         }
      }
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
                  TTConcept c = conceptMap.get(fields[4]);
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

   private void addIsa(TTConcept concept,String parent){
      if (concept.get(IM.IS_A)==null) {
         TTArray isas = new TTArray();
         concept.set(IM.IS_A, isas);
      }
      TTArray isas= concept.get(IM.IS_A).asArray();
      isas.add(TTIriRef.iri(SN + parent));

   }

   private void addRelationship(TTConcept c, int group, String relationship, String target) {
      if (relationship.equals(IS_A) || relationship.equals(REPLACED_BY)) {
         addIsa(c,target);
         if (relationship.equals(REPLACED_BY)) {
            TTConcept replacement = conceptMap.get(target);
            if (replacement == null) {
               replacement = new TTConcept();
               addIsa(replacement,c.getIri());
               document.addConcept(replacement);
               conceptMap.put(target, replacement);
            }
         }
      } else {
         TTArray roleGroup = getRoleGroup(c, group);
         roleGroup.add(getRole(relationship, target));
      }
   }
   private TTNode getRole(String relationship,String target){
      TTNode subrole= new TTNode();
      subrole.set(TTIriRef.iri(SN+ relationship),TTIriRef.iri(SN+target));
      return subrole;
   }

   private TTArray getRoleGroup(TTConcept c, int group) {
      if (c.get(IM.ROLE_GROUP)==null){
         TTArray roleGroups= new TTArray();
         c.set(IM.ROLE_GROUP,roleGroups);
      }
      TTArray groups=c.get(IM.ROLE_GROUP).asArray();
      for (int i=0; i<=group; i++) {
         if (i > groups.getElements().size() - 1) {
            TTArray newGroup = new TTArray();
            groups.add(newGroup);
         }
      }
      return groups.get(group).asArray();

   }






   private static boolean hasMRCM(Concept op){

      if (op.getPropertyDomain()!=null|op.getObjectPropertyRange()!=null)
         return true;
      return false;
   }






}
