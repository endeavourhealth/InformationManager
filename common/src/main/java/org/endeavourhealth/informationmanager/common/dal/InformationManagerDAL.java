package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;

import java.util.List;

public interface InformationManagerDAL extends BaseDAL {
    Integer getOrCreateModelDbid(String modelIri, String modelVersion) throws Exception;
    Integer getModelDbid(String modelPath) throws Exception;
    Integer getDocumentDbid(String documentId) throws Exception;
    Integer createDocument(String documentInfoJson) throws Exception;

    Integer getConceptDbid(String id) throws Exception;
    void upsertConcept(int modelDbid, String conceptJson) throws Exception;
    void upsertConceptDefinition(String conceptId, String conceptDefinitionJson) throws Exception;
    void upsertPropertyDomain(int propertyDbid, int conceptDbid, int statusDbid, Integer minCardinality, Integer maxCardinality) throws Exception;
    void upsertPropertyRange(int propertyDbid, String propertyRangeJson) throws Exception;

    SearchResult getMRU(Integer size) throws Exception;
    List<Model> getModels() throws Exception;

    ConceptSummary getConceptSummary(String id) throws Exception;

    Concept getConcept(String id) throws Exception;

    //----------------------------------

    void publishDocument(int dbid, String level) throws Exception;

    Document getDocument(int dbid) throws Exception;

    void updateDocument(int dbid, String documentJson) throws Exception;

    void createConcept(int document, String json, String name) throws Exception;

    Concept updateConcept(Concept newConcept) throws Exception;


    SearchResult search(String text, Integer size, Integer page, List<Integer> documents, String relationship, String target) throws Exception;

    String getConceptJSON(String id) throws Exception;

    String getConceptName(String id) throws Exception;

    ConceptDefinition getConceptDefinition(String id) throws Exception;

    List<Document> getDocuments() throws Exception;

    String validateIds(List<String> ids) throws Exception;


    List<DraftConcept> getDocumentPending(int dbid, Integer page, Integer size) throws Exception;

    byte[] getDocumentLatestPublished(Integer dbid) throws Exception;

    List<IdNamePair> getSchemes() throws Exception;

    List<ConceptTreeNode> getChildren(String conceptId) throws Exception;
}
