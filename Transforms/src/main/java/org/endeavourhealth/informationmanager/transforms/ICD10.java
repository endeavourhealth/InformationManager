package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class ICD10 {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            System.err.println("You need to provide a root path containing ICD10 data and SNOMED map files!");
            System.exit(-1);
        }

        TTDocument document = new ICD10ToTTDocument().importICD10(argv[0]);
        TTDocumentFiler filer = new TTDocumentFiler(true);
        filer.fileDocument(document);
    }
}