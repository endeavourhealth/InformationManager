package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class SnomedMain {
      public static void main(String[] argv) throws Exception {
         if (argv.length != 1) {
            System.err.println("You need to provide a root path for Read ");
            System.exit(-1);
         }
         long start = System.currentTimeMillis();

         SnomedImport importer= new SnomedImport();
         importer.importSnomed(argv[0]);;
         long end = System.currentTimeMillis();
         long duration = (end - start) / 1000 / 60;
      }
}
