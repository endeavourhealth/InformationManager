package org.endeavourhealth.informationmanager.transforms;

public class CTV3TPPMain {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("You need to provide a root path containing CTV3");
            System.exit(-1);
        }

        CTV3TPPImport importer= new CTV3TPPImport();
        importer.importCTV3();

    }

}

