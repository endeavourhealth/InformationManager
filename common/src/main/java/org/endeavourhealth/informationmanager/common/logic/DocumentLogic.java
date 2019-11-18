package org.endeavourhealth.informationmanager.common.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Document;
import org.endeavourhealth.informationmanager.common.models.document.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;

public class DocumentLogic {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentLogic.class);
    private Set<String> ids = new HashSet<>();

    public void importDocument(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        Document doc = mapper.readValue(json, Document.class);
        ModelDocument modelDoc = doc.getModelDocument();
        DocumentInfo docInfo = modelDoc.getDocumentInfo();

        // TODO: importExternalConcepts(doc.get("Import"));
        getAndProcessPrefixes(docInfo.getPrefix());
        loadAndValidateConceptIds(modelDoc.getConcept());

        fileDocument(modelDoc);

        LOG.info("Done");
    }

    private void importExternalConcepts(JsonNode imports) {
        if (imports != null) {
            if (!imports.isArray())
                throw new IllegalStateException("Document \"Import\" node should be an array");

            LOG.info("Importing {}", imports.textValue());
            // TODO: Recursively import documents and their IDs
        }
    }
    private void getAndProcessPrefixes(List<Prefix> prefixes) {
        // TODO: Load prefixes
    }
    private void loadAndValidateConceptIds(List<Concept> concepts) {

        if (concepts != null && concepts.size() > 0) {
            LOG.info("Loading concept IDs");
            for (Concept concept : concepts) {
                ids.add(concept.getId());
            }
            LOG.info("Loaded {} concept IDs", ids.size());
        }
    }

    private void fileDocument(ModelDocument doc) throws Exception {
        LOG.debug("Filing document");
        try (InformationManagerDAL dal = new InformationManagerJDBCDAL()) {
            dal.beginTransaction();
            DocumentInfo documentInfo = doc.getDocumentInfo();

            String modelIri = documentInfo.getModelIri().toString();
//             modelIri = new File(new URL(modelIri).getPath()).getParent();

            Integer modelDbid = dal.getOrCreateModelDbid(modelIri, documentInfo.getBaseModelVersion());

            Integer docDbid = dal.getDocumentDbid(documentInfo.getDocumentId());
            if (docDbid == null)
                dal.createDocument(documentInfo);

            // Process the document
            fileConcepts(dal, modelDbid, doc.getConcept());
            fileConceptDefinitions(dal, doc.getConceptDefinition());
            filePropertyDomains(dal, doc.getPropertyDomain());
            filePropertyRanges(dal, doc.getPropertyRange());
            // fileCohorts(dal, doc.get("Cohort"));
            // fileValueSets(dal, doc.get("ValueSet"));
            // fileDataSets(dal, doc.get("DataSet"));
            // fileDataTypeDefinitions(dal, doc.get("DataTypeDefinition"));


            dal.commit();
        }
    }
    private void fileConcepts(InformationManagerDAL dal, int modelDbid, List<Concept> concepts) throws Exception {
        LOG.debug("...filing concepts");
        int i=0;
        for(Concept concept: concepts) {
            dal.upsertConcept(modelDbid, concept);
            i++;
        }
        LOG.debug("...{} concepts filed", i);
    }
    private void fileConceptDefinitions(InformationManagerDAL dal, List<ConceptDefinition> definitions) throws Exception {
        LOG.debug("...filing definitions");
        int i=0;
        for (ConceptDefinition definition: definitions) {
            dal.upsertConceptDefinition(definition);
            i++;
        }
        LOG.debug("...{} definitions filed", i);
    }
    private void filePropertyDomains(InformationManagerDAL dal, List<PropertyDomain> domains) throws Exception {
        if (domains == null || domains.size() == 0) {
            LOG.debug("...no domains");
            return;
        }
        LOG.debug("...filing domains");
        int i=0;
        for (PropertyDomain propertyDomain : domains) {
            int propertyDbid = dal.getConceptDbid(propertyDomain.getProperty());
            int statusDbid = dal.getConceptDbid(propertyDomain.getStatus());
            for (Domain domain: propertyDomain.getDomain()) {
                int conceptDbid = dal.getConceptDbid(propertyDomain.getProperty());
                dal.upsertPropertyDomain(
                    propertyDbid,
                    conceptDbid,
                    statusDbid,
                    domain
                );
            }
            i++;
        }
        LOG.debug("...{} domains filed", i);
    }
    private void filePropertyRanges(InformationManagerDAL dal, List<PropertyRange> propertyRanges) throws Exception {
        if (propertyRanges == null || propertyRanges.size() == 0) {
            LOG.debug("...no ranges");
            return;
        }
        LOG.debug("...filing ranges");
        int i=0;
        for (PropertyRange range : propertyRanges) {
            Integer propertyDbid = dal.getConceptDbid(range.getProperty());
            if (propertyDbid == null)
                throw new IllegalArgumentException("Unknown property concept [" + range.getProperty() + "]");
            int statusDbid = dal.getConceptDbid(range.getStatus());
            dal.upsertPropertyRange(propertyDbid, statusDbid, range.getRangeClass());
            i++;
        }
        LOG.debug("...{} ranges filed", i);
    }

    private void fileCohorts(InformationManagerDAL dal, JsonNode cohorts) {
        // TODO: File cohorts
    }
    private void fileValueSets(InformationManagerDAL dal, JsonNode valueSets) {
        // TODO: File value sets
    }
    private void fileDataSets(InformationManagerDAL dal, JsonNode dataSets) {
        // TODO: File data sets
    }
    private void fileDataTypeDefinitions(InformationManagerDAL dal, JsonNode dataTypeDefs) {
        // TODO: File data set definitions
    }

    private String getNodeText(JsonNode node, String field) {
        if (node.has(field))
            return node.get(field).textValue();
        else
            return null;
    }

    private Integer getNodeInt(JsonNode node, String field) {
        String val = getNodeText(node, field);
        return val == null ? null : Integer.parseInt(val);
    }

}
