package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class CTV3 {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("You need to provide a root path containing CTV3 data and SNOMED map files!");
            System.exit(-1);
        }

        TTDocument document = new CTV3ToTTDocument().importCTV3(args[0]);
        TTDocumentFiler filer = new TTDocumentFiler(true);
        filer.fileDocument(document);
    }

}
