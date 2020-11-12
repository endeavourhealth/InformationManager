package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class OntologyImport {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyImport.class);

    public static void fileOntology(File inputFile) throws Exception {
        System.out.println("Importing [" + inputFile + "]");

        LOG.info("Initializing");
        ObjectMapper objectMapper = new ObjectMapper();

        LOG.info("Loading JSON");

        Document document = objectMapper.readValue(inputFile, Document.class);

        new OntologyImportHelper().file(document.getInformationModel());
    }

    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            LOG.error("Provide an Information Model json file");
            System.exit(-1);
        }
        File inputFile = new File(argv[0]);
        fileOntology(inputFile);


    }
}
