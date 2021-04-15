package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.ClosureGenerator;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class EMIS {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 2) {
            System.err.println("You need to provide a root path containing EMIS data and closure ");
            System.exit(-1);
        }

        TTDocument document = new EMISToTTDocument().importEMIS(argv[0]);
        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        System.out.println("Building closure table");
        ClosureGenerator builder = new ClosureGenerator();
        builder.generateClosure(argv[1]);
    }
}
