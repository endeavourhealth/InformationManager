package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DocumentImport {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentImport.class);

    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            LOG.error("Provide an Information Model json file");
            System.exit(-1);
        }

        System.out.println("Importing [" + argv[0] + "]");

        LOG.info("Initializing");
        ConfigManager.Initialize("information-manager");
        ObjectMapper objectMapper = new ObjectMapper();

        LOG.info("Loading JSON");
        File inputFile = new File(argv[0]);
        Ontology ontology = objectMapper.readValue(inputFile, Ontology.class);

        new DocumentImportHelper().save(ontology);
    }
}
