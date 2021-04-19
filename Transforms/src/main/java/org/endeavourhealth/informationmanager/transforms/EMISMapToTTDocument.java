package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EMISMapToTTDocument {
   private static final String EMISConcepts = ".*\\\\EMIS\\\\EMISCodes.csv";
   private Connection conn;
   private TTManager manager= new TTManager();
   private TTDocument document;
   private HashSet<String> readCodes = new HashSet<>();
   private Map<String,String> descIdMap= new HashMap<>();
   private HashSet<String> emisNameSpace = new HashSet<>(Arrays.asList("1000006","1000034","1000035","1000171"));


   public TTDocument importMaps(String folder) throws IOException, SQLException, ClassNotFoundException {
      loadRead();
      Path file = findFileForId(folder, EMISConcepts);
      document = manager.createDocument(IM.GRAPH_MAP_EMIS.getIri());
      try( CSVReader reader = new CSVReader(new FileReader(file.toFile()))){
         reader.readNext();
         int count=0;
         String[] fields;
         while ((fields = reader.readNext()) != null) {
            count++;
            if (count % 10000 == 0) {
               System.out.println("Processed " + count + " records");
            }
            String codeid= fields[0];
            String term= fields[1];
            String emis= fields[2];
            String snomed= fields[3];
            String descid= fields[4];
            String parent= fields[10];

            if (descid.equals(""))
               descid= null;
            if (descid!=null)
               descIdMap.put(codeid,descid);
            TTConcept c= new TTConcept();

            if (!readCodes.contains(emis)) {
               c.setIri("emis:"+emis);
               if (isSnomed(snomed)) {
                  MapHelper.addMap(c, iri(IM.NAMESPACE + "SupplierAssured"), "sn:" + snomed,
                      descid, null,null,null);
                  document.addConcept(c);
               }
            }

         }
         System.out.println("Process ended with " + count + " records");
      }
      return document;

   }
   private static void validateFiles(String path) throws IOException {
      String[] files =  Stream.of(EMISConcepts)
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

   private void loadRead() throws SQLException, ClassNotFoundException {
      System.out.println("Getting read codes and Snomed from database");
      conn= IMConnection.getConnection();
      PreparedStatement getRead= conn.prepareStatement("SELECT code from concept where "
          +"scheme='"+ IM.CODE_SCHEME_READ.getIri()+"'");
      ResultSet codes= getRead.executeQuery();
      while (codes.next()){
         readCodes.add(codes.getString("code"));
      }
      if (readCodes.isEmpty())
         throw new SQLException("Read codes not filed yet");

   }
   public Boolean isSnomed(String s){
      if(s.length()<=10){
         return true;
      }else {
         return !emisNameSpace.contains(getNameSpace(s));
      }
   }
   public String getNameSpace(String s){
      s = s.substring(s.length()-10, s.length()-3);
      return s;
   }


}
