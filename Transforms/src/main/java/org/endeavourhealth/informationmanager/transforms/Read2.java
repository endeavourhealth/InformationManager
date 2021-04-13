package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.informationmanager.ClosureGenerator;
import org.endeavourhealth.informationmanager.TTDocumentFiler;

import java.io.*;

public class Read2 {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 2) {
            System.err.println("You need to provide a root path for Read and a path for the transitive closure!");
            System.exit(-1);
        }
        long start = System.currentTimeMillis();

        TTDocument document = new Read2ToTTDocument().importRead2(argv[0]);
        TTDocumentFiler filer = new TTDocumentFiler(document.getGraph());
        filer.fileDocument(document);
        System.out.println("Building closure table");
        ClosureGenerator builder = new ClosureGenerator();
        builder.generateClosure(argv[1]);

        long end = System.currentTimeMillis();
        long duration = (end - start) / 1000 / 60;
    }
}
