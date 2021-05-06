package org.endeavourhealth.informationmanager.transforms;

import com.opencsv.CSVReader;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
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
import java.util.HashMap;
import java.util.Map;

public class R2NHSImport implements TTImport {

   private static final String[] r2Terms = {".*\\\\READ\\\\Term.csv"};
   private static final String[] r2Concepts = {".*\\\\READ\\\\DESC.csv"};


   private final TTManager manager = new TTManager();
   private TTDocument document;
   private final Map<String,String> termMap= new HashMap<>();
   private final Map<String,String> readTerm= new HashMap<>();
   private Connection conn;

   @Override
   public TTImport importData(String inFolder) throws Exception {
      document = manager.createDocument(IM.GRAPH_READ2.getIri());
      importR2Terms(inFolder);
      //Maps core read code to its term as Vision doesnt provide correct terms
      importR2Concepts(inFolder);
      importR2SnomedMap();
      TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
      filer.fileDocument(document);

      return this;
   }

   private void importR2SnomedMap() throws SQLException, ClassNotFoundException {
      System.out.println("Obtaining EMIS Read to Snomed look ups");
      conn= ImportUtils.getConnection();
      PreparedStatement getReadMaps= conn.prepareStatement("Select distinct tc.code as readCode,c.code as snomed, tc.concept_term_code as conceptTermCode\n" +
              "          from term_code tc\n" +
              "          join concept c on tc.concept= c.dbid\n" +
              "          join concept cscheme on tc.scheme=cscheme.dbid\n" +
              "          where cscheme.iri='http://endhealth.info/im#EMISCodeScheme'\n" +
              "          and length(tc.code)<6");
      ResultSet rs= getReadMaps.executeQuery();
      while (rs.next()){
         String code= rs.getString("readCode");
         code= (code+".....").substring(0,5);
         String snomed= rs.getString("snomed");
         String conceptTerm = rs.getString("conceptTermCode");
         String term= readTerm.get(code);
         if (term!=null) {
            document.addIndividual(TTManager.getTermCode(SNOMED.NAMESPACE + snomed,term,code, IM.CODE_SCHEME_READ,conceptTerm ));
         }
      }
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

    private void importR2Concepts(String folder) throws IOException {

        Path file = ImportUtils.findFileForId(folder, r2Concepts[0]);
        System.out.println("Importing R2 concepts");

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            reader.readLine();
            String line = reader.readLine();

            int count = 0;
            while (line != null && !line.isEmpty()) {

                String[] fields = line.split(",");
                if ("C".equals(fields[6])) {

                    String code= fields[0];
                    String term = termMap.get(fields[1]);
                    if (term!=null) {
                       readTerm.put(code, term);
                       count++;
                       if (count % 50000 == 0) {
                          System.out.println("Processed " + count + " concepts");
                       }
                    }

                }
                line = reader.readLine();
            }
            System.out.println("Process ended with " + count + " read codes");
        }
    }



   @Override
   public TTImport validateFiles(String inFolder) {
      ImportUtils.validateFiles(inFolder,r2Terms,r2Concepts);
      return this;
   }

   @Override
   public TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException {
      PreparedStatement checkEMIS = conn.prepareStatement("Select tc.code from term_code tc \n"+
              "join concept c on tc.scheme= c.dbid where c.iri='http://endhealth.info/im#EMISCodeScheme' limit 1");
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
