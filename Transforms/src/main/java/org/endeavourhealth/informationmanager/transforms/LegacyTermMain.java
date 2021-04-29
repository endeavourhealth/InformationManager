package org.endeavourhealth.informationmanager.transforms;

public class LegacyTermMain {
   public static void main(String[] argv) throws Exception {
      if (argv.length != 1) {
         System.err.println("You need to provide a root path for Read ");
         System.exit(-1);
      }
      long start = System.currentTimeMillis();

      LegacyTermsImport importer= new LegacyTermsImport();
      importer.importTermCodes(argv[0]);;
      long end = System.currentTimeMillis();
      long duration = (end - start) / 1000 / 60;
   }
}