package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.informationmanager.common.transform.DiscoveryReasoner;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
import org.endeavourhealth.imapi.model.Document;
import org.endeavourhealth.imapi.model.Ontology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;

public class OntologyImport {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyImport.class);

    public static void fileOntology(File inputFile,boolean large) throws Exception {
            System.out.println("Importing [" + inputFile + "]");

            LOG.info("Initializing");
            ObjectMapper objectMapper = new ObjectMapper();

            LOG.info("Loading JSON");

            Document document = objectMapper.readValue(inputFile, Document.class);

            OntologyFiler filer = new OntologyFiler();
            Ontology ontology = document.getInformationModel();

            filer.fileOntology(ontology,large);
            //Create ISA tree and file inferred hierarchy
            DiscoveryReasoner reasoner = new DiscoveryReasoner(ontology);
            Set<Concept> conceptSet = reasoner.classify();
            filer = new OntologyFiler();
            filer.fileClassification(conceptSet,ontology.getModule());


    }

    public static void main(String[] argv) throws Exception {
        if (argv.length != 2) {
            LOG.error("Provide an Information Model json file and a large ontology boolean");
            System.exit(-1);
        }
        boolean large= Boolean.valueOf(argv[1]);
        File inputFile = new File(argv[0]);
        fileOntology(inputFile,large);


    }
}
