package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTImportByType;

public class ImportUTLApp {

   public static void main(String[] args) throws Exception {
      if (args.length != 3) {
         System.err.println("You need to provide a root path containing data and a prefix for a type and an iri for a value set");
         System.exit(-1);
      }
      String folder = args[0];
      String importTypeString = args[1].toLowerCase();
      TTIriRef importType;
      if (importTypeString.equals("core"))
         importType= IM.GRAPH_DISCOVERY;
      else
         throw new Exception("unrecognised type prefex "+ importTypeString);

      String valueSet = args[2];
      TTImportByType importer= new Importer().validateByType(IM.GRAPH_VALUESETS,folder);
      importer.importByType(IM.GRAPH_VALUESETS,folder,false,null);
   }

}
