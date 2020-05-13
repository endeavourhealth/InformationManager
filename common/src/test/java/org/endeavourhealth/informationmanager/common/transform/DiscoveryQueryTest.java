package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.io.File;
import java.io.IOException;

class DiscoveryQueryTest {

    @Test
    void testQuery() throws OWLOntologyCreationException, IOException, FileFormatException {

        OWLOntology ontology = getOntology();

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

        DefaultPrefixManager defaultPrefixManager = new DefaultPrefixManager();

        OWLDocumentFormat ontologyFormat = ontology.getNonnullFormat();
        if (ontologyFormat instanceof PrefixDocumentFormat) {
            defaultPrefixManager.copyPrefixesFrom((PrefixDocumentFormat) ontologyFormat);
            defaultPrefixManager.setPrefixComparator(((PrefixDocumentFormat) ontologyFormat).getPrefixComparator());
        }
        OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();
        NodeSet<OWLClass> results = reasoner.getSubClasses(
            dataFactory.getOWLClass(defaultPrefixManager.getIRI("cm:LegacyTypeEncounter")),
            false
        );

        System.out.println(results);
        System.out.println("Nodes: " + results.nodes().count());


/*
        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
        DLQueryPrinter dlQueryPrinter = new DLQueryPrinter(
            new DLQueryEngine(reasoner, shortFormProvider),
            shortFormProvider
        );

        String query = "Hospital";

        dlQueryPrinter.askQuery(query);
        System.out.println();
*/

    }

    private OWLOntology getOntology() throws OWLOntologyCreationException, IOException, FileFormatException {
        ObjectMapper objectMapper = new ObjectMapper();
        Ontology discovery = objectMapper.readValue(new File("IMCoreFunc.json"), Ontology.class);

        return new DiscoveryToOWL().transform(discovery);
    }
}
