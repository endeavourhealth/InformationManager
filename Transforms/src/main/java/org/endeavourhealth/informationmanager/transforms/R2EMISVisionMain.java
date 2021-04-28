package org.endeavourhealth.informationmanager.transforms;

public class R2EMISVisionMain {
    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            System.err.println("You need to provide a root path containing EMIS data");
            System.exit(-1);
        }
        R2EMISVisionImport importer= new R2EMISVisionImport();
        importer.importR2EMISVision(argv[0]);


    }
}
