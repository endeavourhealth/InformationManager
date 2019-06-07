package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Status;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DocumentImport {
    private static InformationManagerDAL db = new InformationManagerJDBCDAL();

    public static void main(String argv[]) throws Exception {
        importFile("Core_IM.json");
        importFile("Medication.json");
        //importFile("Working example-3.json", "http/DiscoveryDataService/InformationModel/dm/HealthData/1.0.1", 33);
        //importFile("Expression.json", "http/DiscoveryDataService/InformationModel/dm/Snomed/1.0.1", 1);
    }

    private static void importFile(String filename) throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get(filename));
        String JSON = new String(encoded, Charset.defaultCharset()).trim();
        JsonNode root = ObjectMapperPool.getInstance().readTree(JSON);

        String document = root.get("document").asText();

        ArrayNode concepts = (ArrayNode) root.get("Concepts");

        // Validate ids
        validateConcepts(concepts);

        // Get/Create document
        int docDbid = db.getOrCreateDocumentDbid(document);

        // Save concepts
        for(JsonNode concept: concepts) {
            db.insertConcept(docDbid, concept.toString(), Status.DRAFT);
        }
    }

    private static void validateConcepts(ArrayNode concepts) throws Exception {
        Set<String> ids = new HashSet<>();

        // Get ids defined in documents
        for(JsonNode concept: concepts) {
            ids.add(concept.get("id").textValue());
        }

        // Validate
        for(JsonNode concept: concepts) {
            validateIds(ids, concept);
        }
    }

    private static void validateIds(Set<String> ids, JsonNode concept) throws Exception {
        Iterator<String> stringIterator = concept.fieldNames();
        while (stringIterator.hasNext()) {
            String field = stringIterator.next();
            JsonNode node = concept.get(field);

            if (node.has("id")) {
                String id = node.get("id").textValue();
                if (!ids.contains(id)) {
                    Integer dbid = db.getConceptDbid(id);
                    if (dbid == null)
                        throw new ClassNotFoundException("id not found [" + id + "]");
                    else
                        ids.add(id);
                }
            }

            if (node.isArray()) {
                for(JsonNode child: (ArrayNode)node) {
                    validateIds(ids, child);
                }
            } else if (node.isObject()) {
                validateIds(ids, node);
            }
        }
    }
}
