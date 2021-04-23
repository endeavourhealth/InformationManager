package org.endeavourhealth.informationmanager.transforms;

public class VisionReadMain {
   public static void main(String[] args) throws Exception {

      VisionReadImport importer= new VisionReadImport();
      importer.importVision(args[0]);

   }
}
