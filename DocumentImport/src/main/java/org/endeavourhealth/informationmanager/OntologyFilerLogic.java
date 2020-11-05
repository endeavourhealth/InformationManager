package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public void commit() throws SQLException {
        dal.commit();
    }

    // ============================== PUBLIC METHODS ==============================
    public Set<String> getUndefinedConcepts() {
        return undefinedConcepts;
    }

    public void fileNamespaces(List<Namespace> namespaces) throws SQLException {
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


    public void fileConcepts(List<? extends Concept> concepts, ConceptType conceptType) throws Exception {
        if (concepts == null || concepts.size() == 0)
            return;

        int i = 0;
        for (Concept concept : concepts) {
            if (concept.getisRef())
                return;
            dal.upsertConcept(concept);

            i++;
            if (i % 1000 == 0) {
                LOG.info("Processed " + i + " of " + concepts.size());
                dal.commit();
            }
        }
        dal.commit();
    }


    public void fileClassAxioms(List<Clazz> clazzes) throws Exception {
        if (clazzes == null)
            return;
        for (Clazz clazz : clazzes) {
            if (clazz.getExpression() != null)
                throw new IllegalStateException("Concept axiom expressions not currently implemented []");
            dal.upsertClassAxioms(clazz);

        }
    }
    public void fileObjectPropertyAxioms(List<ObjectProperty> objectProperties) throws Exception {
        if (objectProperties== null)
            return;
        for (ObjectProperty op : objectProperties)
            dal.upsertObjectPropertyAxioms(op);

    }
    public void fileDataPropertyAxioms(List<DataProperty> dataProperties) throws Exception {
        if (dataProperties== null)
            return;
        for (DataProperty dp : dataProperties)
            dal.upsertDataPropertyAxioms(dp);

    }


}


