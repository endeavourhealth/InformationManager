package org.endeavourhealth.informationmanager.transforms;


import org.endeavourhealth.informationmanager.ClosureGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AllImportMain {
   public static void main(String[] argv) throws Exception {
      if (argv.length != 2) {
         System.err.println("You need to provide a root path for all the codes and one for closure file ");
         System.exit(-1);
      }
      long start = System.currentTimeMillis();
      dropIndexes();
      DiscoveryCoreImport coreImporter= new DiscoveryCoreImport();
      coreImporter.importCore(argv[0]);
      SnomedImport importer= new SnomedImport();
      importer.importSnomed(argv[0]);;
      R2EMISVisionImport r2importer= new R2EMISVisionImport();
      r2importer.importR2EMISVision(argv[0]);
      CTV3TPPImport tppimporter= new CTV3TPPImport();
      tppimporter.importCTV3(argv[0]);
      ClosureGenerator builder = new ClosureGenerator();
      builder.generateClosure(argv[1]);

      restoreIndexes();

      long end = System.currentTimeMillis();
      long duration = (end - start) / 1000 / 60;
      System.out.println("Duration = "+ duration + " minutes");
   }
   public static void dropIndexes() throws SQLException, ClassNotFoundException {
      Connection conn= IMConnection.getConnection();
      try {

         PreparedStatement dropFull = conn.prepareStatement("DROP INDEX concept_name_ftx ON concept;");
         //"");
         dropFull.execute();
      } catch (SQLException e){}
      try {
         PreparedStatement dropFull = conn.prepareStatement("DROP INDEX ct_term_ftx on concept_term;");
         dropFull.execute();
      } catch (SQLException e){}

   }


   public static void restoreIndexes() throws SQLException, ClassNotFoundException {
      Connection conn= IMConnection.getConnection();
      try {
         PreparedStatement addFull = conn.prepareStatement("DROP INDEX concept_name_ftx ON concept;");
         //"");
         addFull.execute();
      } catch (SQLException e){}
      try {
         PreparedStatement addFull = conn.prepareStatement("DROP INDEX ct_term_ftx on concept_term;");
         addFull.execute();
      } catch (SQLException e){}

   }
}
