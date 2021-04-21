package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class ICD10Main {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            System.err.println("You need to provide a root path containing ICD10 data and SNOMED map files!");
            System.exit(-1);
        }

        ICD10Import importer= new ICD10Import();

        importer.importICD10(argv[0]);
    }
}
