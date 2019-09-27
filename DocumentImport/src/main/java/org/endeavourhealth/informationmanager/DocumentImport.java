package org.endeavourhealth.informationmanager;

import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.informationmanager.common.logic.DocumentLogic;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DocumentImport {
    private static DocumentLogic dl = new DocumentLogic();

    public static void main(String[] argv) throws Exception {
        ConfigManager.Initialize("information-manager");

        for (String file : argv) {
            byte[] encoded = Files.readAllBytes(Paths.get(file));
            String json = new String(encoded, Charset.defaultCharset()).trim();
            dl.importDocument(json);
        }
    }
}
