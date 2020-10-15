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

            int ontologyDbid = logic.getOrCreateOntologyDbid(ontology.getIri());

            LOG.info("Pre-caching/drafting concepts");
            logic.cacheOrCreateConcepts(ontology.getClazz());
            logic.cacheOrCreateConcepts(ontology.getObjectProperty());
            logic.cacheOrCreateConcepts(ontology.getDataProperty());
            logic.cacheOrCreateConcepts(ontology.getDataType());
            logic.cacheOrCreateConcepts(ontology.getAnnotationProperty());


            LOG.info("Processing Classes");
            logic.saveConcepts(ontologyDbid, ontology.getClazz(), ConceptType.CLASS);
            LOG.info("Processing Object properties");
            logic.saveConcepts(ontologyDbid, ontology.getObjectProperty(), ConceptType.OBJECTPROPERTY);
            LOG.info("Processing Data properties");
            logic.saveConcepts(ontologyDbid, ontology.getDataProperty(), ConceptType.DATAPROPERTY);
            LOG.info("Processing Data types");
            logic.saveConcepts(ontologyDbid, ontology.getDataType(), ConceptType.DATATYPE);
            LOG.info("Processing Annotation properties");
            logic.saveConcepts(ontologyDbid, ontology.getAnnotationProperty(), ConceptType.ANNOTATION);
            LOG.info("Processing Individuals");
            logic.saveConcepts(ontologyDbid, ontology.getIndividual(), ConceptType.INDIVIDUAL);
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
