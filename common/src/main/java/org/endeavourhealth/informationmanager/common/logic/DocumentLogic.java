package org.endeavourhealth.informationmanager.common.logic;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.*;

public class DocumentLogic {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentLogic.class);
    private Set<String> ids = new HashSet<>();

    public void importDocument(String json) throws Exception {
        JsonNode root = ObjectMapperPool.getInstance().readTree(json);

        JsonNode doc = root.get("ModelDocument");

        JsonNode version = doc.get("Version");
        importExternalConcepts(doc.get("Import"));
        getAndProcessPrefixes(doc.get("Prefix"));
        loadAndValidateConceptIds(doc.get("Concept"));

        fileDocument(doc);

        LOG.info("Done");
    }

    private void importExternalConcepts(JsonNode imports) {
        if (imports != null) {
            if (!imports.isArray())
                throw new InvalidStateException("Document \"Import\" node should be an array");

            LOG.info("Importing {}", imports.textValue());
            // TODO: Recursively import documents and their IDs
        }
    }
    private void getAndProcessPrefixes(JsonNode prefixes) {
        // TODO: Load prefixes
    }
    private void loadAndValidateConceptIds(JsonNode concepts) {

        if (concepts != null) {
            if (!concepts.isArray())
                throw new InvalidStateException("Document \"Concept\" node should be an array");

            LOG.info("Loading concept IDs");
            for (JsonNode concept : concepts) {
                ids.add(concept.get("id").textValue());
            }
            LOG.info("Loaded {} concept IDs", ids.size());
        }

    }

    private void fileDocument(JsonNode doc) throws Exception {
        LOG.debug("Filing document");
        try (InformationManagerDAL dal = new InformationManagerJDBCDAL()) {
            dal.beginTransaction();
            JsonNode docInfo = doc.get("DocumentInfo");

            Integer modelDbid = dal.getOrCreateModelDbid(getNodeText(docInfo, "modelIri"), getNodeText(docInfo, "baseModelVersion"));

            Integer docDbid = dal.getDocumentDbid(docInfo.get("documentId").textValue());
            if (docDbid == null)
                dal.createDocument(docInfo.toString());

            // Process the document
            fileConcepts(dal, modelDbid, doc.get("Concept"));
            fileConceptDefinitions(dal, doc.get("ConceptDefinition"));
            filePropertyDomains(dal, doc.get("PropertyDomain"));
            filePropertyRanges(dal, doc.get("PropertyRange"));
            fileCohorts(dal, doc.get("Cohort"));
            fileValueSets(dal, doc.get("ValueSet"));
            fileDataSets(dal, doc.get("DataSet"));
            fileDataTypeDefinitions(dal, doc.get("DataTypeDefinition"));


            dal.commit();
        }
    }
    private void fileConcepts(InformationManagerDAL dal, int modelDbid, JsonNode conceptListNode) throws Exception {
        LOG.debug("...filing concepts");
        int i=0;
        for(JsonNode conceptNode: conceptListNode) {
            dal.upsertConcept(modelDbid, conceptNode.toString());
            i++;
        }
        LOG.debug("...{} concepts filed", i);
    }
    private void fileConceptDefinitions(InformationManagerDAL dal, JsonNode definitionListNode) throws Exception {
        LOG.debug("...filing definitions");
        int i=0;
        for (JsonNode definitionNode: definitionListNode) {
            int dbid = dal.getConceptDbid(getNodeText(definitionNode, "definitionOf"));
            dal.upsertConceptDefinition(dbid, definitionNode.toString());
            i++;
        }
        LOG.debug("...{} definitions filed", i);
    }
    private void filePropertyDomains(InformationManagerDAL dal, JsonNode domainListNode) throws Exception {
        LOG.debug("...filing domains");
        int i=0;
        for (JsonNode domainNode: domainListNode) {
            int propertyDbid = dal.getConceptDbid(getNodeText(domainNode, "property"));
            int statusDbid = dal.getConceptDbid(getNodeText(domainNode, "status"));
            for (JsonNode prop : domainNode.get("domain")) {
                int conceptDbid = dal.getConceptDbid(getNodeText(prop, "class"));
                dal.upsertPropertyDomain(
                    propertyDbid,
                    conceptDbid,
                    statusDbid,
                    getNodeInt(prop, "minCardinality"),
                    getNodeInt(prop, "maxCardinality")
                );
            }
            i++;
        }
        LOG.debug("...{} domains filed", i);
    }
    private void filePropertyRanges(InformationManagerDAL dal, JsonNode rangeListNode) throws Exception {
        LOG.debug("...filing ranges");
        int i=0;
        for (JsonNode rangeNode: rangeListNode) {
            Integer propertyDbid = dal.getConceptDbid(getNodeText(rangeNode,"property"));
            if (propertyDbid == null)
                throw new IllegalArgumentException("Unknown property concept [" + getNodeText(rangeNode,"property") + "]");
            dal.upsertPropertyRange(propertyDbid, rangeNode.toString());
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
