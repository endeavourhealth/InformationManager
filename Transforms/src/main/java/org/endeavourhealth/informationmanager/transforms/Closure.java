package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.ClosureGenerator;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class Closure {
   public static void main(String[] args) throws Exception {
      if (args.length != 1) {
         System.err.println("You need to provide a root path for placing the closure file");
         System.exit(-1);
      }
      ClosureGenerator builder = new ClosureGenerator();
      builder.generateClosure(args[1]);
   }
}
