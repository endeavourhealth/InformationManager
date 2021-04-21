package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.ClosureGenerator;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class EMISReadMain {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            System.err.println("You need to provide a root path containing EMIS data");
            System.exit(-1);
        }
        EMISReadImport importer= new EMISReadImport();
        importer.importEMIS(argv[0]);


    }
}
