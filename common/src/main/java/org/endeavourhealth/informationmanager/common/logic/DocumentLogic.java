package org.endeavourhealth.informationmanager.common.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Concept;
import org.endeavourhealth.informationmanager.common.models.ConceptDomain;
import org.endeavourhealth.informationmanager.common.models.ConceptProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class DocumentLogic {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentLogic.class);
    private Set<String> ids = new HashSet<>();

    public void importDocument(String iri) throws Exception {
        JsonNode root = getDocument(iri);

        validateDocument(root);
        JsonNode doc = root.get("IMDocument");

        loadIdsRecursive(doc.get("Import"), doc.get("Concept"));

        validateConcepts(doc.get("Concept"));
        validateDomains(doc.get("Domain"));
        validateRanges(doc.get("Range"));
        validatePropertyExpressions(doc.get("Expression"));

        fileDocument(doc);

        LOG.info("Done");
    }

    private JsonNode getDocument(String iri) throws IOException {
        // Assume filename TODO: Online
        byte[] encoded = Files.readAllBytes(Paths.get(iri));
        String JSON = new String(encoded, Charset.defaultCharset()).trim();
        return ObjectMapperPool.getInstance().readTree(JSON);
    }

    private void validateDocument(JsonNode root) {
        LOG.info("Validating document...");

        List<String> validNodes = Arrays.asList("Concept", "Domain", "Range", "Expression", "Iri");
        Set<String> mandatoryNodes = new HashSet<>(Arrays.asList("Iri"));
        Iterator<Map.Entry<String, JsonNode>> iterator = root.fields();
        int count = 0;
        while (iterator.hasNext()) {
            count++;
            if (count > 1)
                throw new IllegalStateException("There should only be 1 (IMDocument) root element");

            Map.Entry<String, JsonNode> node = iterator.next();
            if (!node.getKey().equals("IMDocument"))
                throw new IllegalStateException("Json file does not contain a root IMDocument element");
        }

        JsonNode document = root.get("IMDocument");
        iterator = document.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> node = iterator.next();
            if (!validNodes.contains(node.getKey()))
                throw new IllegalStateException("IMDocument contains unknown node [" + node.getKey() + "]");
            mandatoryNodes.remove(node.getKey());
        }
        if(mandatoryNodes.size() > 0)
            throw new IllegalStateException("The following mandatory nodes were not specified " + mandatoryNodes.toString());
    }
    private void loadIdsRecursive(JsonNode imports, JsonNode concepts) {
        if (imports != null) {
            if (!imports.isArray())
                throw new InvalidStateException("Document \"Import\" node should be an array");

            LOG.info("Importing {}", imports.textValue());
            // TODO: Recursively import documents and their IDs
        }

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
    private void validateConcepts(JsonNode concepts) {
        if (concepts == null)
            return;

        LOG.info("Validating concepts...");
        for (JsonNode item: concepts) {
            checkFieldValueIdExists("Concept." + item.get("id").textValue(), item, "status");
        }
    }
    private void validateDomains(JsonNode domains) {
        if (domains == null)
            return;

        LOG.info("Validating ranges...");
        for (JsonNode item: domains) {
            checkFieldValueIdExists("Domain", item, "domain");
            for (JsonNode prop: item.get("property")) {
                checkFieldValueIdExists("Domain.property[]", prop, "concept");
            }
        }
    }
    private void validateRanges(JsonNode ranges) {
        if (ranges == null)
            return;

        LOG.info("Validating ranges...");
        for (JsonNode item: ranges) {
            checkFieldValueIdExists("Range", item, "property");
            checkFieldValueIdExists("Range.range", item.get("range"), "concept");
        }
    }
    private void validatePropertyExpressions(JsonNode domains) {
        List<String> validNodes = Arrays.asList("concept", "propertyGroup");

        if (domains == null)
            return;

        LOG.info("Validating property expressions...");
        for (JsonNode item: domains) {
            checkFieldValueIdExists("Expression", item, "concept");
            for (Iterator<Map.Entry<String, JsonNode>> it = item.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> propNode = it.next();

                if (!validNodes.contains(propNode.getKey()))
                    throw new InvalidStateException("Invalid expression member [" + propNode.getKey() + "]");
            }
        }
    }
    private void checkFieldValueIdExists(String type, JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null)
            return;

        if (!ids.contains(field.textValue()))
            throw new InvalidStateException("[" + type + "." + fieldName + "] contains unknown concept id [" + field.textValue() + "]");
    }

    private void fileDocument(JsonNode doc) throws Exception {
        LOG.debug("Filing document");
        try (InformationManagerJDBCDAL dal = new InformationManagerJDBCDAL()) {
            try {
                dal.beginTransaction();
                int docDbid = dal.getOrCreateDocumentDbid(doc.get("Iri").textValue());

                // Pre-create ids
                for (String id : ids) {
                    dal.createConcept(docDbid, id, null);
                }

                // Process the document
                fileConcepts(dal, docDbid, doc.get("Concept"));
                fileDomains(dal, doc.get("Domain"));
                fileRanges(dal, doc.get("Range"));
                fileExpressions(dal, doc.get("Expression"));

                dal.commit();
            } catch (Exception e) {
                dal.rollback();
                throw e;
            }
        }
    }
    private void fileConcepts(InformationManagerJDBCDAL dal, int docDbid, JsonNode concepts) throws IOException, SQLException {
        LOG.debug("...filing concepts");
        for(JsonNode concept: concepts) {
            Concept c = new Concept();
            c.setDocument(docDbid)
                .setDescription(getNodeText(concept, "description"))
                .setId(getNodeText(concept,"id"))
                .setName(getNodeText(concept,"name"))
                .setScheme(getNodeText(concept, "scheme"))
                .setCode(getNodeText(concept, "code"))
                .setStatus(getNodeText(concept, "status"));

            dal.updateConcept(c);
        }
    }
    private void fileDomains(InformationManagerJDBCDAL dal, JsonNode domains) throws SQLException, JsonProcessingException {
        LOG.debug("...filing domains");
        for (JsonNode item: domains) {
            int dbid = dal.getConceptDbid(getNodeText(item,"domain"));
            List<ConceptDomain> doms = new ArrayList<>();

            for (JsonNode prop: item.get("property")) {
                doms.add(new ConceptDomain()
                .setProperty(getNodeText(prop, "concept"))
                .setMinimum(prop.get("minimum").asInt())
                .setMaximum(prop.get("maximum").asInt())
                );
            }
            dal.checkAndUpdateDomain(dbid, doms, new ArrayList<>());
        }
    }
    private void fileRanges(InformationManagerJDBCDAL dal, JsonNode ranges) throws SQLException {
        LOG.debug("...filing ranges");
        for (JsonNode range: ranges) {
            int dbid = dal.getConceptDbid(getNodeText(range,"property"));
            dal.checkAndUpdateRange(dbid, range.get("range").toString(), null);
        }
    }
    private void fileExpressions(InformationManagerJDBCDAL dal, JsonNode expressions) throws SQLException, JsonProcessingException {
        LOG.debug("...filing property expressions");
        for (JsonNode propExp: expressions) {
            int dbid = dal.getConceptDbid(getNodeText(propExp, "concept"));
            List<ConceptProperty> props = new ArrayList<>();

            for (Iterator<Map.Entry<String, JsonNode>> it = propExp.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> propNode = it.next();

                if (propNode.getKey().equals("propertyGroup"))
                    props.addAll(buildPropertyGroup(propNode.getValue()));
            }
            dal.checkAndUpdateProperties(dbid, props, new ArrayList<>());
        }
    }
    private List<ConceptProperty> buildPropertyGroup(JsonNode propGrp) {
        LOG.debug("......Building property group");

        List<ConceptProperty> result = new ArrayList<>();

        for (JsonNode propVal: propGrp) {
            result.add(new ConceptProperty()
                .setProperty(getNodeText(propVal, "property"))
                .setConcept(getNodeText(propVal, "conceptValue"))
                .setValue(getNodeText(propVal, "value"))
            );
        }

        return result;
    }

    private String getNodeText(JsonNode node, String field) {
        if (node.has(field))
            return node.get(field).textValue();
        else
            return null;
    }
}
