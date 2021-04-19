package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.ClosureGenerator;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

import java.io.*;

public class Read2 {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            System.err.println("You need to provide a root path for Read ");
            System.exit(-1);
        }
        long start = System.currentTimeMillis();

        TTDocument document = new Read2ToTTDocument().importRead2(argv[0]);
        System.out.println("Filing read 2 concepts");
        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        System.out.println("importing maps");
        document = new Read2ToTTDocument().importMaps(argv[0]);
        System.out.println("Filing read maps");
        filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);

        long end = System.currentTimeMillis();
        long duration = (end - start) / 1000 / 60;
    }
}
