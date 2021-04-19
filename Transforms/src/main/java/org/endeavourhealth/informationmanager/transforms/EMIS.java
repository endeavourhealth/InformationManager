package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.ClosureGenerator;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class EMIS {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            System.err.println("You need to provide a root path containing EMIS data");
            System.exit(-1);
        }

        TTDocument document = new EMISToTTDocument().importEMIS(argv[0]);
        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        document= new EMISToTTDocument().importMaps(argv[0]);
        filer= new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);

    }
}
