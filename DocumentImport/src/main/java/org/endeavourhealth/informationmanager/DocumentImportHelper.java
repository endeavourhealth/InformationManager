package org.endeavourhealth.informationmanager;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.List;
import java.util.Set;

public class DocumentImportHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentImportHelper.class);

    private final InformationManagerDAL dal = new InformationManagerJDBCDAL();

    public void save(Ontology ontology) throws Exception {
        try {
            // dal.beginTransaction();
            LOG.info("Saving ontology");
            LOG.info("Processing namespaces");
            processNamespaces(ontology);
            LOG.info("Processing Classes");
            processConcepts(ontology.getClazz());
            LOG.info("Processing Object properties");
            processConcepts(ontology.getObjectProperty());
            LOG.info("Processing Data properties");
            processConcepts(ontology.getDataProperty());
            LOG.info("Processing Data types");
            processConcepts(ontology.getDataType());
            LOG.info("Processing Annotation properties");
            processConcepts(ontology.getAnnotationProperty());
            LOG.info("Ontology saved");

            Set<String> undefinedConcepts = dal.getUndefinedConcepts();
            if (undefinedConcepts.size() != 0)
                LOG.error("Concept(s) referenced but not defined [" + String.join(",", undefinedConcepts) + "]");
            // dal.commit();
        } catch (Exception e) {
            // dal.rollback();
            throw e;
        }
    }

    private void processNamespaces(Ontology ontology) throws Exception {
        // Ensure all namespaces exist (auto-create)
        for (Namespace ns : ontology.getNamespace()) {
            dal.getNamespaceIdWithCreate(ns.getIri(), ns.getPrefix());
        }
    }

    private void processConcepts(List<? extends Concept> concepts) throws Exception {
        if (concepts == null || concepts.size() == 0)
            return;

        dal.saveConcepts(concepts);
    }
}
