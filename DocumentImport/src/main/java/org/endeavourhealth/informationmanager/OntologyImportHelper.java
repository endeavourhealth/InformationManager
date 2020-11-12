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
            logic.commit();
            ;

            // Record document details, updating ontology and module
            LOG.info("Processing document-ontology-module");
            logic.fileDocument(ontology);
            logic.commit();


            LOG.info("Processing Classes");
            logic.fileConcepts(ontology.getConcept());

            logic.commit();

            logic.fileIndividuals(ontology.getIndividual());
            logic.commit();

            LOG.info("Ontology filed");

            Set<String> undefinedConcepts = logic.getUndefinedConcepts();
            if (undefinedConcepts.size() != 0)
                LOG.error("Concept(s) referenced but not defined [" + String.join(",", undefinedConcepts) + "]");
            // dal.commit();
            logic.close();
        } catch (Exception e) {
            // dal.rollback();
            throw e;
        }
        finally {
            logic.close();
        }

    }
}
