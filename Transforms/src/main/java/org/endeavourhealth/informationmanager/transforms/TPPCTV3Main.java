package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class TPPCTV3Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("You need to provide a root path containing CTV3");
            System.exit(-1);
        }

        TPPCTV3Import importer= new TPPCTV3Import();
        importer.importCTV3(args[0]);

    }

}

