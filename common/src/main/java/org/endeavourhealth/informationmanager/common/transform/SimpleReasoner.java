package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;

/**
 * A simple reasoner for discovery ontology objects using an OWL reasoner to classify and provides indexing capabilities for ontology
 */
public class SimpleReasoner {
    private Ontology ontology;
    private OWLReasoner reasoner;
    private OWLOntology owlOntology;


    /**
     * Needs a Discovery syntax ontology to operate on
     * @param ontology
     */
    public SimpleReasoner(Ontology ontology){
        this.ontology= ontology;
    }

    public SimpleReasoner classify() throws FileFormatException, OWLOntologyCreationException {
        OWLOntologyManager owlManager= new DiscoveryToOWL().transform(ontology);
        reasoner = new FaCTPlusPlusReasonerFactory().createReasoner(owlOntology);

        return this;
    }
}
