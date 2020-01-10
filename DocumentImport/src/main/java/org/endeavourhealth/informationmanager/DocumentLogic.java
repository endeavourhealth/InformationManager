package org.endeavourhealth.informationmanager;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Concept;
import org.endeavourhealth.informationmanager.common.models.ConceptRelation;
import org.endeavourhealth.informationmanager.common.models.ConceptRelationCardinality;
import org.endeavourhealth.informationmanager.common.models.DocumentInfo;
import org.endeavourhealth.informationmanager.models.ConceptDefinition;
import org.endeavourhealth.informationmanager.models.ModelDocument;
import org.endeavourhealth.informationmanager.models.PropertyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DocumentLogic {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentLogic.class);

    public void importDocument(ModelDocument modelDoc) throws Exception {
        try (InformationManagerDAL dal = new InformationManagerJDBCDAL()) {
            dal.beginTransaction();

            DocumentInfo docInfo = modelDoc.getDocumentInfo();
            String modelIri = docInfo.getModelIri().toString();
            Integer modelDbid = 0; // dal.getOrCreateModelDbid(modelIri, docInfo.getBaseModelVersion());

            // getAndProcessPrefixes(docInfo.getPrefix());

            preallocateMissingConceptIDs(dal, modelDbid, modelDoc.getConcept());
            fileDocument(dal, docInfo);
            fileConcepts(dal, modelDbid, modelDoc.getConcept());
            fileConceptDefinitions(dal, modelDoc.getConceptDefinition());

            LOG.info("Done");
            dal.commit();
        }
    }


/*
    private void getAndProcessPrefixes(List<Prefix> prefixes) {
        // TODO: Load prefixes
    }
*/
    private void preallocateMissingConceptIDs(InformationManagerDAL dal, int modelDbid, List<Concept> concepts) throws Exception {

        if (concepts != null && concepts.size() > 0) {
            LOG.info("Pre-allocating concept IDs...");
            for (Concept concept : concepts) {
                if (dal.getConceptId(concept.getIri()) == null) {
                    dal.allocateConceptId(concept.getIri());
                }
            }
        }
    }

    private void fileDocument(InformationManagerDAL dal, DocumentInfo documentInfo) throws Exception {
/*
        LOG.debug("Filing document");
        Integer docDbid = dal.getDocumentDbid(documentInfo.getDocumentId());
        if (docDbid == null)
            dal.createDocument(documentInfo);
*/

        // Process the document
        //filePropertyDomains(dal, doc.getPropertyDomain());
        //filePropertyRanges(dal, doc.getPropertyRange());
        // fileCohorts(dal, doc.get("Cohort"));
        // fileValueSets(dal, doc.get("ValueSet"));
        // fileDataSets(dal, doc.get("DataSet"));
        // fileDataTypeDefinitions(dal, doc.get("DataTypeDefinition"));

    }

    private void fileConcepts(InformationManagerDAL dal, int modelDbid, List<Concept> concepts) throws Exception {
        LOG.debug("...filing concepts");
        int i=0;
        for(Concept concept: concepts) {
            dal.upsertConcept(concept);
            i++;
        }
        LOG.debug("...{} concepts filed", i);
    }
    private void fileConceptDefinitions(InformationManagerDAL dal, List<ConceptDefinition> definitions) throws Exception {
        LOG.debug("...filing definitions");
        int i=0;
        for (ConceptDefinition definition: definitions) {
            String conceptId = definition.getDefinitionOf();
            filePropertyObjects(dal, conceptId, definition.getPropertyObject());
            i++;
        }
        LOG.debug("...{} definitions filed", i);
    }
    private void filePropertyObjects(InformationManagerDAL dal, String conceptId, List<PropertyObject> propertyObjects) throws Exception {
        List<ConceptRelation> relations = new ArrayList<>();
        for (PropertyObject propertyObject: propertyObjects) {
            ConceptRelation rel = new ConceptRelation()
                .setSubject(conceptId)
                .setRelation(propertyObject.getProperty())
                .setObject(propertyObject.getConceptValue());

            if (propertyObject.getMinCardinality() != null || propertyObject.getMaxCardinality() != null) {
                rel.setCardinality(new ConceptRelationCardinality()
                    .setMinCardinality(propertyObject.getMinCardinality())
                    .setMaxCardinality(propertyObject.getMaxCardinality())
                );
            }

            relations.add(rel);
        }

        dal.replaceConceptRelations(conceptId, relations);
    }
/*    private void filePropertyDomains(InformationManagerDAL dal, List<PropertyDomain> domains) throws Exception {
        if (domains == null || domains.size() == 0) {
            LOG.debug("...no domains");
            return;
        }
        LOG.debug("...filing domains");
        int i=0;
        for (PropertyDomain propertyDomain : domains) {
*/
/*            int propertyDbid = dal.getConceptDbid(propertyDomain.getProperty());
            int statusDbid = dal.getConceptDbid(propertyDomain.getStatus());
            for (Domain domain: propertyDomain.getDomain()) {
                int conceptDbid = dal.getConceptDbid(propertyDomain.getProperty());

                dal.insertConceptRelations(
                    propertyDbid,
                    conceptDbid,
                    statusDbid,
                    domain
                );

            }
*//*
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
*/
/*
            Integer propertyDbid = dal.getConceptDbid(range.getProperty());
            if (propertyDbid == null)
                throw new IllegalArgumentException("Unknown property concept [" + range.getProperty() + "]");
            int statusDbid = dal.getConceptDbid(range.getStatus());
            dal.upsertPropertyRange(propertyDbid, statusDbid, range.getRangeClass());
*//*

            i++;
        }
        LOG.debug("...{} ranges filed", i);
    }
*/
/*
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

*/

}
