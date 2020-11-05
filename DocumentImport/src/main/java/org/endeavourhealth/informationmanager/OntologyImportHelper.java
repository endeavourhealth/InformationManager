package org.endeavourhealth.informationmanager;

import org.endeavourhealth.informationmanager.common.models.ConceptType;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class OntologyImportHelper {
    private static final Logger LOG = LoggerFactory.getLogger(OntologyImportHelper.class);

    private final OntologyFilerLogic logic = new OntologyFilerLogic();


    public OntologyImportHelper() throws Exception {
    }

    public void file(Ontology ontology) throws Exception {
        try {

            LOG.info("Saving ontology");
            logic.startTransaction();
            LOG.info("Processing namespaces");
            // Ensure all namespaces exist (auto-create)
            //The document prefixes (ns) may not be the same as the IM DB prefixes
            logic.fileNamespaces(ontology.getNamespace());
            logic.commit();;

            // Record document details, updating ontology and module
            LOG.info("Processing document-ontology-module");
            logic.fileDocument(ontology);
            logic.commit();


            LOG.info("Processing Classes");
            logic.fileConcepts(ontology.getClazz(), ConceptType.CLASS);
            LOG.info("Processing Object properties");
            logic.fileConcepts(ontology.getObjectProperty(), ConceptType.OBJECTPROPERTY);
            LOG.info("Processing Data properties");
            logic.fileConcepts(ontology.getDataProperty(), ConceptType.DATAPROPERTY);
            LOG.info("Processing Data types");
            logic.fileConcepts(ontology.getDataType(), ConceptType.DATATYPE);
            LOG.info("Processing Annotation properties");
            logic.fileConcepts(ontology.getAnnotationProperty(), ConceptType.ANNOTATION);
            LOG.info("Processing Individuals");
            logic.fileConcepts(ontology.getIndividual(), ConceptType.INDIVIDUAL);

            LOG.info("Processing class Axioms");
            logic.fileClassAxioms(ontology.getClazz());
            LOG.info("Processing Object property axioms");
            logic.fileObjectPropertyAxioms(ontology.getObjectProperty());
            LOG.info("Processing Data property axioms");
            logic.fileDataPropertyAxioms(ontology.getDataProperty());
            /*
            LOG.info("Processing Data type definitions");
            logic.fileDataTypeAxioms(ontology.getDataType(), ConceptType.DATATYPE);
            LOG.info("Processing Annotation property axioms");
            logic.fileAnnotationAxioms(ontology.getAnnotationProperty(), ConceptType.ANNOTATION);
            LOG.info("Processing Individual assertions");
            logic.fileIndividualAxioms(ontology.getIndividual(), ConceptType.INDIVIDUAL);
            */
            logic.commit();

            LOG.info("Ontology filed");

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
