package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class DiscoveryToOWLTest {

    @Test
    void transform() throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {

        ObjectMapper objectMapper = new ObjectMapper();
        Ontology discovery = objectMapper.readValue(new File("IMCoreFunc.json"), Ontology.class);

        OWLOntology ontology = new DiscoveryToOWL().transform(discovery);

        checkConsistency(ontology);

        OWLManager
            .createOWLOntologyManager()
            .saveOntology(
                ontology,
                new FunctionalSyntaxDocumentFormat(),
                new FileOutputStream("IMCoreOut.owl")
            );
    }

    private void checkConsistency(OWLOntology ontology) {
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        ConsoleProgressMonitor monitor = new ConsoleProgressMonitor();
        OWLReasonerConfiguration config = new SimpleConfiguration(monitor);
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
        reasoner.precomputeInferences();
        boolean consistent = reasoner.isConsistent();
        if (!consistent) {
            System.err.println("Inconsistent!");
            System.exit(1);
        } else {
            System.out.println("Ontology is consistent");
        }
    }
}
