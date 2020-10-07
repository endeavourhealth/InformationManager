package org.endeavourhealth.informationmanager;

import org.endeavourhealth.informationmanager.common.models.ConceptType;
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
            // Ensure all namespaces exist (auto-create)
            for (Namespace ns : ontology.getNamespace()) {
                logic.getNamespaceIdWithCreate(ns.getIri(), ns.getPrefix());
            }

            Boolean inferred = null;

            if ("ASSERTED".equals(ontology.getEntailmentType().toUpperCase())) {
                inferred = false;
            } else if ("INFERRED".equals(ontology.getEntailmentType().toUpperCase())) {
                inferred = true;
            } else {
                LOG.error("Unknown entailment type [" + ontology.getEntailmentType() + "]");
                return;
            }
            LOG.info("Entailment: " + (inferred ? "Inferred" : "Asserted"));

            LOG.info("Pre-caching/drafting concepts");
            logic.cacheOrCreateConcepts(ontology.getClazz());
            logic.cacheOrCreateConcepts(ontology.getObjectProperty());
            logic.cacheOrCreateConcepts(ontology.getDataProperty());
            logic.cacheOrCreateConcepts(ontology.getDataType());
            logic.cacheOrCreateConcepts(ontology.getAnnotationProperty());


            LOG.info("Processing Classes");
            logic.saveConcepts(ontology.getClazz(), ConceptType.CLASS, inferred);
            LOG.info("Processing Object properties");
            logic.saveConcepts(ontology.getObjectProperty(), ConceptType.OBJECTPROPERTY, inferred);
            LOG.info("Processing Data properties");
            logic.saveConcepts(ontology.getDataProperty(), ConceptType.DATAPROPERTY, inferred);
            LOG.info("Processing Data types");
            logic.saveConcepts(ontology.getDataType(), ConceptType.DATATYPE, inferred);
            LOG.info("Processing Annotation properties");
            logic.saveConcepts(ontology.getAnnotationProperty(), ConceptType.ANNOTATION, inferred);
            LOG.info("Processing Individuals");
            logic.saveConcepts(ontology.getIndividual(), ConceptType.INDIVIDUAL, inferred);
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
}
