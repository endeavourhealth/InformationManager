package org.endeavourhealth.informationmanager;

import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class DocumentImportHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentImportHelper.class);

    private final DocumentFilerLogic logic = new DocumentFilerLogic();

    public DocumentImportHelper() throws SQLException {
    }

    public void save(Ontology ontology) throws Exception {
        try {
            // dal.beginTransaction();
            LOG.info("Saving ontology");
            LOG.info("Processing namespaces");
            processNamespaces(ontology);

            Boolean inferred = null;

            if ("ASSERTED".equals(ontology.getEntailmentType().toUpperCase())) {
                inferred = false;
            } else if ("INFERRED".equals(ontology.getEntailmentType().toUpperCase())) {
                inferred = true;
            } else {
                LOG.error("Unknown entailment type [" + ontology.getEntailmentType() + "]");
                return;
            }
            LOG.warn("Entailment: " + (inferred ? "Inferred" : "Asserted"));
            LOG.info("Processing Classes");
            processConcepts(ontology.getClazz(), inferred);
            LOG.info("Processing Object properties");
            processConcepts(ontology.getObjectProperty(), inferred);
            LOG.info("Processing Data properties");
            processConcepts(ontology.getDataProperty(), inferred);
            LOG.info("Processing Data types");
            processConcepts(ontology.getDataType(), inferred);
            LOG.info("Processing Annotation properties");
            processConcepts(ontology.getAnnotationProperty(), inferred);
            LOG.info("Ontology saved");

            Set<String> undefinedConcepts = logic.getUndefinedConcepts();
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
            logic.getNamespaceIdWithCreate(ns.getIri(), ns.getPrefix());
        }
    }

    private void processConcepts(List<? extends Concept> concepts, boolean inferred) throws Exception {
        if (concepts == null || concepts.size() == 0)
            return;

        logic.saveConcepts(concepts, inferred);
    }
}
