package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class OPCS4Main {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            System.err.println("You need to provide a root path containing OPCS4 data and SNOMED map files!");
            System.exit(-1);
        }
        OPCS4Import importer= new OPCS4Import();

        importer.importOPCS4(argv[0]);

    }
}
