package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class OPCS4 {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            System.err.println("You need to provide a root path containing OPCS4 data and SNOMED map files!");
            System.exit(-1);
        }

        TTDocument document = new OPCS4ToTTDocument().importOPCS4(argv[0]);
        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        System.out.println("Importing maps");
        document= new OPCS4ToTTDocument().importMaps(argv[0]);
        filer.fileDocument(document);
    }
}
