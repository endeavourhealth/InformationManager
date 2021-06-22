package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.endeavourhealth.informationmanager.TTDocumentFiler;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.common.transform.TTManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class R2NHSImport implements TTImport {

   private static final String[] r2Terms = {".*\\\\READ\\\\Term.csv"};
   private static final String[] r2Desc = {".*\\\\READ\\\\DESC.csv"};
   private static final String[] r2Maps = {".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured\\\\rcsctmap2_uk_.*\\.txt"};
  private static final String[] altMaps = {".*\\\\SNOMED\\\\Mapping Tables\\\\Updated\\\\Clinically Assured"+
  "\\\\codesWithValues_AlternateMaps_READ2_.*\\.txt"};


  private final TTManager manager = new TTManager();
   private TTDocument document;
   private final Map<String,String> termMap= new HashMap<>();
   private final Map<String,String> readTerm= new HashMap<>();
   private final Map<String,String> emisRead = new HashMap<>();
   private final Map<String, List<Snomed>> snomedMap = new HashMap<>();
   private Connection conn;
   private PreparedStatement getEmis;

   private class Snomed {
     String entityId;
     String descId;

     public String getEntityId() {
       return entityId;
     }

     public Snomed setEntityId(String entityId) {
       this.entityId = entityId;
       return this;
     }

     public String getDescId() {
       return descId;
     }

     public Snomed setDescId(String descId) {
       this.descId = descId;
       return this;
     }
   }
   @Override
   public TTImport importData(String inFolder) throws Exception {
      document = manager.createDocument(IM.GRAPH_READ2.getIri());
      importR2Terms(inFolder);
      importEmis();
      //Maps core read code to its term as Vision doesnt provide correct terms
      importR2Entities(inFolder);
      importNHSR2SnomedMap(inFolder);
      importAltMap(inFolder);
      createTermCodes();
      TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
      filer.fileDocument(document);

      return this;
   }

  private void importEmis() throws SQLException, ClassNotFoundException {
     System.out.println("Importing emis codes for missing read codes");
     Connection conn= ImportUtils.getConnection();
     getEmis= conn.prepareStatement("select t.code,t.term from term_code t\n"+
       "join entity c on t.scheme=c.dbid\n"+
       "where c.iri='"+IM.CODE_SCHEME_EMIS.getIri()+"'");
     ResultSet rs= getEmis.executeQuery();
     while (rs.next()){
       String emis= rs.getString("code");
       if (emis.length()<6)
         if (!emis.contains("-")){
            String code = (emis + ".....").substring(0, 5);
            emisRead.put(code, rs.getString("term"));
        }
     }
  }

  private void createTermCodes() {
     for (Map.Entry<String,List<Snomed>> entry:snomedMap.entrySet()){
       String read= entry.getKey();
       String name= readTerm.get(read);
       if (name==null)
         name= emisRead.get(read);
       if (name==null)
         name="unknown";
       List<Snomed> snomeds= entry.getValue();
       for (Snomed snomed:snomeds){
         String entityId= snomed.getEntityId();
         String descId= snomed.getDescId();
          document.addEntity(TTManager
            .createTermCode(TTIriRef.iri(SNOMED.NAMESPACE+entityId),
              IM.ADD,name,read,
               IM.CODE_SCHEME_READ,descId));
       }
    }
  }

  private void importAltMap(String path) throws IOException {
    int i = 0;
    for (String mapFile : altMaps) {
      Path file = ImportUtils.findFilesForId(path, mapFile).get(0);
      System.out.println("Processing entities in " + file.getFileName().toString());
      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
        reader.readLine();    // Skip header
        String line = reader.readLine();
        while (line != null && !line.isEmpty()) {
          String[] fields = line.split("\t");
          String read= fields[0];
          String termId= fields[1];
          String entityId=fields[2];
          String descid= fields[3];
          String use= fields[4];
          if (use.equals("Y"))
            if (!entityId.equals("")){
              Snomed snomed= new Snomed();
              snomed.setEntityId(entityId);
              snomed.setDescId(descid);
              String termCode= read;
              if (!termId.equals("00"))
                termCode= termCode+termId;
              List<Snomed> maps= snomedMap.get(termCode);
              if (maps==null) {
                maps = new ArrayList<>();
                snomedMap.put(termCode, maps);
              }
              //Avoid duplicate entries
              if (!alreadyInmap(maps,entityId))
                maps.add(snomed);
            }
          i++;
          line = reader.readLine();
        }
      }
    }
    System.out.println("Imported " + i + " entities");
  }

  private boolean alreadyInmap(List<Snomed> maps, String entityId){
     for (Snomed snomed:maps)
       if (snomed.entityId.equals(entityId))
         return true;
     return false;
  }
  private void importNHSR2SnomedMap(String path) throws IOException {
    int i = 0;
    for (String mapFile : r2Maps) {
      Path file = ImportUtils.findFilesForId(path, mapFile).get(0);
      System.out.println("Processing entities in " + file.getFileName().toString());
      try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
        reader.readLine();    // Skip header
        String line = reader.readLine();
        while (line != null && !line.isEmpty()) {
          String[] fields = line.split("\t");
          String read= fields[1];
          String termId= fields[2];
          String entityId=fields[3];
          String descid= fields[4];
          String assured= fields[5];
          String status= fields[7];
          if (assured.equals("1"))
            if (status.equals("1")){
              Snomed snomed= new Snomed();
              snomed.setEntityId(entityId);
              snomed.setDescId(descid);
              String termCode= read;
              if (!termId.equals("00"))
                termCode= termCode+termId;
              List<Snomed> maps= snomedMap.get(termCode);
              if (maps==null) {
                maps = new ArrayList<>();
                snomedMap.put(termCode, maps);
              }
              //Avoid duplicate entries
              if (!alreadyInmap(maps,entityId))
                maps.add(snomed);
          }
          i++;
          line = reader.readLine();
        }
      }
    }
    System.out.println("Imported " + i + " entities");
  }


   private void importR2Terms(String folder) throws IOException {

        Path file = ImportUtils.findFileForId(folder, r2Terms[0]);
        System.out.println("Importing R2 terms");

        try( CSVReader reader = new CSVReader(new FileReader(file.toFile()))){
            reader.readNext();
            int count=0;
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                count++;
                if(count%50000 == 0){
                    System.out.println("Processed " + count +" terms");
                }
                if("C".equals(fields[1])) {
                    String termid= fields[0];
                    String term= fields[3];
                    termMap.put(termid,term);
                }
            }
            System.out.println("Process ended with " + count +" terms");
        }
    }

    private void importR2Entities(String folder) throws IOException {

        Path file = ImportUtils.findFileForId(folder, r2Desc[0]);
        System.out.println("Importing R2 entities");

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            reader.readLine();
            String line = reader.readLine();

            int count = 0;
            while (line != null && !line.isEmpty()) {

                String[] fields = line.split(",");
                if ("C".equals(fields[6])) {
                    String code= fields[0];
                    String termId= fields[1];
                    String term = termMap.get(termId);
                    String termNumber= termId.substring(0,2);
                    String termCode;
                    if (termNumber.equals("00"))
                      termCode=code;
                    else
                      termCode=code+termNumber;
                    readTerm.put(termCode, term);
                    count++;
                    if (count % 50000 == 0) {
                          System.out.println("Processed " + count + " entities");
                       }
                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " read codes");
        }
    }



   @Override
   public TTImport validateFiles(String inFolder) {
      ImportUtils.validateFiles(inFolder,r2Terms,r2Desc,r2Maps,altMaps);
      return this;
   }

   @Override
   public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
      PreparedStatement checkEMIS = conn.prepareStatement("Select tc.code from term_code tc \n"+
              "join entity c on tc.scheme= c.dbid where c.iri=? limit 1");
      checkEMIS.setString(1, IM.CODE_SCHEME_EMIS.getIri());
      ResultSet rs= checkEMIS.executeQuery();
      if (!rs.next()) {
         System.err.println("EMIS read codes not loaded");
         System.exit(-1);
      }
      PreparedStatement getVisions = conn.prepareStatement("Select read_code from vision_read2_to_snomed_map limit 1");
      rs= getVisions.executeQuery();
      if (!rs.next()) {
         System.err.println("No Vision Snomed look up table (vision_read2_to_snomed_map)");
         System.exit(-1);
      }
      return this;
   }

   @Override
   public void close() throws Exception {
      if (conn!=null)
         if (!conn.isClosed())
            conn.close();


   }
}
