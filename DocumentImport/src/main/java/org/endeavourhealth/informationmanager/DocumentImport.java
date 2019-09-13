package org.endeavourhealth.informationmanager;

import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.informationmanager.common.logic.DocumentLogic;

public class DocumentImport {
    private static DocumentLogic dl = new DocumentLogic();

    public static void main(String[] argv) throws Exception {
        ConfigManager.Initialize("information-manager");

        for (String file : argv) {
            dl.importDocument(file);
        }
    }
}
