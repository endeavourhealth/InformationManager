package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.models.ConceptType;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

public class OntologyFilerLogic {
    private static final Logger LOG = LoggerFactory.getLogger(OntologyFilerLogic.class);
    private static final String SUBTYPE = "sn:116680003";

    private final OntologyFilerJDBCDAL dal;
    private ObjectMapper objectMapper;

    private Integer ontologyId;

    private final Set<String> undefinedConcepts = new HashSet<>();


    public OntologyFilerLogic() throws Exception {
        dal = new OntologyFilerJDBCDAL();
    }

    public void startTransaction() throws SQLException {
        dal.startTransaction();
    }

    public void close() throws SQLException {
        dal.close();
    }

    public void commit() throws SQLException {
        dal.commit();
    }

    // ============================== PUBLIC METHODS ==============================
    public Set<String> getUndefinedConcepts() {
        return undefinedConcepts;
    }

    public void fileNamespaces(Set<Namespace> namespaces) throws SQLException {
        Namespace nullNamespace = new Namespace();
        nullNamespace.setIri("");
        nullNamespace.setPrefix("");
        dal.upsertNamespace(nullNamespace);
        if (namespaces == null || namespaces.size() == 0)
            return;
        //Populates the namespace map with both namespace iri and prefix as keys
        for (Namespace ns : namespaces) {
            dal.upsertNamespace(ns);

        }

    }


    // ------------------------------ DOCUMENT MODULE ONTOLOGY ------------------------------
    public void fileDocument(Ontology ontology) throws SQLException {
        dal.upsertModule(ontology.getModule());
        dal.upsertOntology(ontology.getIri());
        dal.addDocument(ontology);
    }


    public void fileIndividuals(Set<Individual> indis) throws Exception {
        if (indis == null || indis.size() == 0)
            return;

        int i = 0;
        for (Individual ind : indis) {

        }

    }

    public void fileConcepts(Set<? extends Concept> concepts) throws Exception {
        if (concepts == null || concepts.size() == 0)
            return;

        int i = 0;
        for (Concept concept : concepts) {
            dal.upsertConcept(concept);
           dal.fileAxioms(concept);

            i++;
            if (i % 1000 == 0) {
                LOG.info("Processed " + i + " of " + concepts.size());
                dal.commit();
            }
        }
        dal.commit();
    }




}


