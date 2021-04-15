package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.ClosureGenerator;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

public class CTV3 {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("You need to provide a root path containing CTV3 SNOMED map files and closure builder folder");
            System.exit(-1);
        }

        TTDocument document = new CTV3ToTTDocument().importCTV3(args[0]);
        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        ClosureGenerator builder = new ClosureGenerator();
        builder.generateClosure(args[1]);
    }

}

