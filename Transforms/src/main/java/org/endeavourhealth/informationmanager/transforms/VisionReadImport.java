package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class VisionReadImport {
   private static final String concepts = ".*\\\\READ\\\\DESC\\.csv";
   private static final String synonyms = ".*\\\\READ\\\\Term\\.csv";
   private TTManager manager= new TTManager();
   private TTDocument document;
   private Connection conn;
   private Map<String, TTConcept> conceptMap = new HashMap<>();
   private Map<String,Read2Term> termMap= new HashMap<>();

   public void importVision(String folder) throws Exception {
      validateFiles(folder);
      conn = IMConnection.getConnection();
      document = manager.createDocument(IM.GRAPH_CTV3.getIri());
      importReadTerms(folder);
      importReadConcepts(folder);
      importTerms();
      setVisionHierarchy();
      importMaps();
      TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
      filer.fileDocument(document);
   }


   private void importReadTerms(String folder) throws IOException {

      Path file = findFileForId(folder, synonyms);


      try( CSVReader reader = new CSVReader(new FileReader(file.toFile()))){
         reader.readNext();
         int count=0;
         String[] fields;
         while ((fields = reader.readNext()) != null) {
            count++;
            if(count%10000 == 0){
               System.out.println("Processed " + count +" terms");
            }
            if("C".equals(fields[1])) {
               String termid= fields[0];
               String term= fields[3];
               termMap.put(termid,new Read2Term().setName(term));
            }
         }
         System.out.println("Process ended with " + count +" terms");
      }
   }

   private void importReadConcepts(String folder) throws IOException {

      Path file = findFileForId(folder, concepts);

      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
         String line = reader.readLine();
         line = reader.readLine();

         int count = 0;
         while (line != null && !line.isEmpty()) {

            String[] fields = line.split(",");
            if ("C".equals(fields[6])) {
               count++;
               if (count % 10000 == 0) {
                  System.out.println("Processed " + count + " concepts");
               }
               String code= fields[0];
               String termCode= fields[1];
               TTConcept c = conceptMap.get(code);
               if (c == null) {
                  c = new TTConcept()
                      .setCode(code)
                      .setIri("vis:" + code)
                      .setScheme(IM.CODE_SCHEME_VISION)
                      .addType(IM.LEGACY);
                  conceptMap.put(code, c);
                  document.addConcept(c);
               }

               Read2Term t = termMap.get(fields[1]);
               c.setName(t.getName()+" (r2 "+ code+")");
               if ("S".equals(fields[2]))  //Its a term code concept
                  c.set(IM.SIMILAR,iri("r2:"+code));
               else {
                  String parent = getParent((fields[0]));
                  if (!parent.equals(""))
                     MapHelper.addChildOf(c,iri("r2:" + parent));
                  else
                     c.set(IM.IS_CONTAINED_IN, new TTArray().add(iri(IM.NAMESPACE + "DiscoveryOntology")));
               }

            }
            line = reader.readLine();
         }
         System.out.println("Process ended with " + count + " concepts");
      }
   }


   private void importTerms() throws IOException, SQLException {
      PreparedStatement getTerms= conn.prepareStatement("SELECT * from vision_read2_code");
      System.out.println("Retrieving terms from vision read+lookup2");
      ResultSet rs= getTerms.executeQuery();
      int count=0;
      while (rs.next()){
         count++;
         if(count%10000 == 0){
            System.out.println("Processed " + count +" terms");
         }
         String code= rs.getString("read_code");
         String term= rs.getString("read_term");
         TTConcept c= new TTConcept();
         c.setIri("vis:"+code);
         c.setName(term);
         c.setCode(code);
         c.setScheme(IM.CODE_SCHEME_VISION);
         c.setStatus(IM.ACTIVE);
         conceptMap.put(code,c);
         document.addConcept(c);
      }
      System.out.println("Process ended with " + count +" concepts created");
   }



   private void setVisionHierarchy() {
      for (Map.Entry<String,TTConcept> entry:conceptMap.entrySet()){
         String childCode= entry.getKey();
         TTConcept childConcept= conceptMap.get(childCode);
         if (childCode.length()>1){
            String parentCode= getParent(childCode);
            MapHelper.addChildOf(childConcept, iri("vis:"+parentCode));

         } else {
            childConcept.set(IM.IS_CHILD_OF, new TTArray().add(iri("vis:VisionReadLocalCodes")));   //emis local code folder
         }
      }
   }

   public String getParent(String code) {

      int index = code.indexOf(".");
      if (index == 0) {
         return "";
      }else if (index==1){
         return ".....";
      } else if (index == -1) {
         return code.substring(0,code.length()-1);
      } else {
         return code.substring(0, index - 1);
      }
   }



   public  void importMaps() throws IOException, SQLException {
      PreparedStatement getMaps = conn.prepareStatement("SELECT * from vision_read2_to_snomed_map");
      System.out.println("Retrieving Vision snomed maps");
      ResultSet rs = getMaps.executeQuery();
      int count = 0;
      while (rs.next()) {

         String code = rs.getString("read_code");
         String snomed = rs.getString("snomed_concept_id");
         TTConcept c = conceptMap.get(code);
         if (c != null) {
            MapHelper.addMap(c, iri(IM.NAMESPACE + "SupplierAssured"), "sn:" + snomed, null, null, 1, null);
            count++;
            if (count % 10000 == 0) {
               System.out.println("Processed " + count + "mapped concepts");
            }
         }
      }
   }

   private static void validateFiles(String path) throws IOException {
      String[] files =  Stream.of(concepts)
          .toArray(String[]::new);

      for(String file: files) {
         try {
            Path p = findFileForId(path, file);
            System.out.println("Found " + p.toString());
         } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
         }
      }
   }

   private static Path findFileForId(String path, String regex) throws IOException {
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





}
