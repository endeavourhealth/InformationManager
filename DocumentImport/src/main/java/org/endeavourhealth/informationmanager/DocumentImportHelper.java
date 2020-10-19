package org.endeavourhealth.informationmanager;

import org.endeavourhealth.informationmanager.common.models.ConceptType;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
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
                logic.getOrCreateNamespaceDbid(ns.getIri(), ns.getPrefix());
            }

            // Record document details
            logic.setDocument(ontology.getDocumentInfo().getDocumentId(), ontology.getModule(), ontology.getIri());

            int moduleDbid = logic.getOrCreateModuleDbid(ontology.getModule());

            LOG.info("Pre-caching/drafting concepts");
            logic.cacheOrCreateConcepts(ontology.getClazz());
            logic.cacheOrCreateConcepts(ontology.getObjectProperty());
            logic.cacheOrCreateConcepts(ontology.getDataProperty());
            logic.cacheOrCreateConcepts(ontology.getDataType());
            logic.cacheOrCreateConcepts(ontology.getAnnotationProperty());


            LOG.info("Processing Classes");
            logic.saveConcepts(moduleDbid, ontology.getClazz(), ConceptType.CLASS);
            LOG.info("Processing Object properties");
            logic.saveConcepts(moduleDbid, ontology.getObjectProperty(), ConceptType.OBJECTPROPERTY);
            LOG.info("Processing Data properties");
            logic.saveConcepts(moduleDbid, ontology.getDataProperty(), ConceptType.DATAPROPERTY);
            LOG.info("Processing Data types");
            logic.saveConcepts(moduleDbid, ontology.getDataType(), ConceptType.DATATYPE);
            LOG.info("Processing Annotation properties");
            logic.saveConcepts(moduleDbid, ontology.getAnnotationProperty(), ConceptType.ANNOTATION);
            LOG.info("Processing Individuals");
            logic.saveConcepts(moduleDbid, ontology.getIndividual(), ConceptType.INDIVIDUAL);
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
