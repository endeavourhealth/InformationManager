package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.TTImport;
import org.endeavourhealth.informationmanager.TTImportByType;

import java.util.Locale;

/**
 * Utility app for importing one or all of the various source files for the ontology initial population.
 */
public class ImportApp {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("You need to provide a root path containing data and a prefix for a type");
            System.exit(-1);
        }
        String folder= args[0];
        String importType= args[1].toLowerCase();

        switch (importType){
            case "all":
                TTImportByType importer = new Importer().validateByType(IM.GRAPH_DISCOVERY,folder);
                importer.validateByType(IM.GRAPH_SNOMED,folder)
                    .validateByType(IM.GRAPH_EMIS,folder)
                    .validateByType(IM.GRAPH_CTV3,folder)
                    .validateByType(IM.GRAPH_OPCS4,folder)
                    .validateByType(IM.GRAPH_ICD10,folder)
                .validateByType(IM.GRAPH_MAPS_DISCOVERY,folder);
                importer.importByType(IM.GRAPH_DISCOVERY,folder);
                importer.importByType(IM.GRAPH_SNOMED,folder);
                importer.importByType(IM.GRAPH_EMIS,folder);
                importer.importByType(IM.GRAPH_CTV3,folder);
                importer.importByType(IM.GRAPH_OPCS4,folder);
                importer.importByType(IM.GRAPH_ICD10,folder);
                importer.importByType(IM.GRAPH_MAPS_DISCOVERY,folder);
                break;
            case "core":
                importer = new Importer().validateByType(IM.GRAPH_DISCOVERY,folder);
                importer.importByType(IM.GRAPH_DISCOVERY,folder);
                break;
            case "snomed":
                importer = new Importer().validateByType(IM.GRAPH_SNOMED,folder);
                importer.importByType(IM.GRAPH_SNOMED,folder);
                break;
            case "emis" :
                importer = new Importer().validateByType(IM.GRAPH_EMIS,folder);
                importer.importByType(IM.GRAPH_EMIS,folder);

            case "tpp" :
                importer = new Importer().validateByType(IM.GRAPH_CTV3,folder);
                importer.importByType(IM.GRAPH_CTV3,folder);
                break;
            case "ctv3" :
                importer = new Importer().validateByType(IM.GRAPH_CTV3,folder);
                importer.importByType(IM.GRAPH_CTV3,folder);
                break;
            case "opcs4":
                importer = new Importer().validateByType(IM.GRAPH_OPCS4,folder);
                importer.importByType(IM.GRAPH_OPCS4,folder);
                break;
            case "icd10":
                importer = new Importer().validateByType(IM.GRAPH_ICD10,folder);
                importer.importByType(IM.GRAPH_ICD10,folder);
                break;
            case "discoverymaps":
                importer = new Importer().validateByType(IM.GRAPH_MAPS_DISCOVERY,folder);
                importer.importByType(IM.GRAPH_MAPS_DISCOVERY,folder);
                break;
            default :
                throw new Exception("Unknown import type");


        }

    }

}

