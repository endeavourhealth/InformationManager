package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.ClosureGenerator;
import org.endeavourhealth.informationmanager.FunctionalFlagger;
import org.endeavourhealth.informationmanager.TTImportByType;

/**
 * Utility app for importing one or all of the various source files for the ontology initial population.
 */
public class ImportApp {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("You need to provide a root path containing data and an import type");
            System.exit(-1);
        }
        String folder = args[0];
        String importType = args[1].toLowerCase();

        switch (importType) {
            case "all":
                TTImportByType importer = new Importer().validateByType(IM.GRAPH_DISCOVERY, folder);
                importer.validateByType(IM.GRAPH_SNOMED, folder)
                    .validateByType(IM.GRAPH_EMIS, folder)
                    .validateByType(IM.GRAPH_CTV3, folder)
                    .validateByType(IM.GRAPH_OPCS4, folder)
                    .validateByType(IM.GRAPH_ICD10, folder)
                    .validateByType(IM.GRAPH_VALUESETS, folder)
                  .validateByType(IM.GRAPH_APEX_KINGS,folder)
                  .validateByType(IM.GRAPH_WINPATH_KINGS,folder)
                .validateByType(IM.GRAPH_MAPS_DISCOVERY,folder)
                .validateByType(IM.GRAPH_REPORTS,folder);
                importer.importByType(IM.GRAPH_DISCOVERY,folder);
                importer.importByType(IM.GRAPH_SNOMED,folder);
                importer.importByType(IM.GRAPH_EMIS,folder);
                importer.importByType(IM.GRAPH_CTV3,folder);
                importer.importByType(IM.GRAPH_OPCS4,folder);
                importer.importByType(IM.GRAPH_ICD10,folder);
                importer.importByType(IM.GRAPH_MAPS_DISCOVERY,folder);
                importer.importByType(IM.GRAPH_VALUESETS,folder);
                importer.validateByType(IM.GRAPH_READ2,folder);
                importer.importByType(IM.GRAPH_READ2,folder);
                importer.importByType(IM.GRAPH_APEX_KINGS,folder);
                importer.importByType(IM.GRAPH_WINPATH_KINGS,folder);
                importer.importByType(IM.GRAPH_REPORTS, folder);
                ClosureGenerator builder = new ClosureGenerator();
                builder.generateClosure(args[0]);
                importer.setFunctionalFlags();
                break;
            case "prsb":
                importer = new Importer().validateByType(IM.GRAPH_PRSB,folder);
                importer.importByType(IM.GRAPH_PRSB,folder);
                importer.setFunctionalFlags();
                break;

            case "core":
                importer = new Importer().validateByType(IM.GRAPH_DISCOVERY, folder);
                importer.importByType(IM.GRAPH_DISCOVERY, folder);
                importer.setFunctionalFlags();
                break;
            case "snomed":
                importer = new Importer().validateByType(IM.GRAPH_SNOMED, folder);
                importer.importByType(IM.GRAPH_SNOMED, folder);
                importer.setFunctionalFlags();
                break;
            case "emis":
                importer = new Importer().validateByType(IM.GRAPH_EMIS, folder);
                importer.importByType(IM.GRAPH_EMIS, folder);
                importer.setFunctionalFlags();
                break;
            case "tpp":
            case "ctv3":
                importer = new Importer().validateByType(IM.GRAPH_CTV3, folder);
                importer.importByType(IM.GRAPH_CTV3, folder);
                importer.setFunctionalFlags();
                break;
            case "opcs4":
                importer = new Importer().validateByType(IM.GRAPH_OPCS4, folder);
                importer.importByType(IM.GRAPH_OPCS4, folder);
                importer.setFunctionalFlags();
                break;
            case "icd10":
                importer = new Importer().validateByType(IM.GRAPH_ICD10, folder);
                importer.importByType(IM.GRAPH_ICD10, folder);
                importer.setFunctionalFlags();
                break;
            case "discoverymaps":
                importer = new Importer().validateByType(IM.GRAPH_MAPS_DISCOVERY, folder);
                importer.importByType(IM.GRAPH_MAPS_DISCOVERY, folder);
                importer.setFunctionalFlags();
                break;
            case "valuesets":
                importer = new Importer().validateByType(IM.GRAPH_VALUESETS, folder);
                importer.importByType(IM.GRAPH_VALUESETS, folder);
                importer.setFunctionalFlags();
                break;
            case "read2":
                importer = new Importer().validateByType(IM.GRAPH_READ2, folder);
                importer.importByType(IM.GRAPH_READ2, folder);
                importer.setFunctionalFlags();
                break;
            case "kingsapex":
                importer = new Importer().validateByType(IM.GRAPH_APEX_KINGS,folder);
                importer.importByType(IM.GRAPH_APEX_KINGS,folder);
                importer.setFunctionalFlags();
                break;
            case "kingswinpath":
                importer = new Importer().validateByType(IM.GRAPH_WINPATH_KINGS,folder);
                importer.importByType(IM.GRAPH_WINPATH_KINGS,folder);
                importer.setFunctionalFlags();
                break;
            case "reports":
                importer = new Importer().validateByType(IM.GRAPH_REPORTS, folder);
                importer.importByType(IM.GRAPH_REPORTS, folder);
                break;
            default:
                throw new Exception("Unknown import type");

        }
    }
}

