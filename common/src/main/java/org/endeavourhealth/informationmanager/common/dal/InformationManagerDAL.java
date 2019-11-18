package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.models.document.*;

import java.util.List;
import java.util.UUID;

public interface InformationManagerDAL extends BaseDAL {
    Integer getOrCreateModelDbid(String modelIri, Version modelVersion) throws Exception;
    Integer getModelDbid(String modelPath) throws Exception;
    Integer getDocumentDbid(UUID documentId) throws Exception;
    Integer createDocument(DocumentInfo documentInfo) throws Exception;

    Integer getConceptDbid(String id) throws Exception;
    void upsertConcept(int modelDbid, Concept concept) throws Exception;
    void upsertConceptDefinition(ConceptDefinition definition) throws Exception;
    void upsertPropertyDomain(int propertyDbid, int conceptDbid, int statusDbid, Domain domain) throws Exception;
    void upsertPropertyRange(int propertyDbid, int statusDbid, List<SimpleExpressionConstraint> rangeClass) throws Exception;

    SearchResult getMRU(Integer size) throws Exception;
    List<Model> getModels() throws Exception;

    Concept getConceptSummary(String id) throws Exception;

    FullConcept getConcept(String id) throws Exception;

    //----------------------------------

    void publishDocument(int dbid, String level) throws Exception;

    DocumentInfo getDocument(int dbid) throws Exception;

    void updateDocument(int dbid, String documentJson) throws Exception;

    void createConcept(int document, String json, String name) throws Exception;

    Concept updateConcept(Concept newConcept) throws Exception;


    SearchResult search(String text, Integer size, Integer page, List<Integer> documents, String relationship, String target) throws Exception;

    String getConceptJSON(String id) throws Exception;

    String getConceptName(String id) throws Exception;

    ConceptDefinition getConceptDefinition(String id) throws Exception;

    List<DocumentInfo> getDocuments() throws Exception;

    String validateIds(List<String> ids) throws Exception;


    List<DraftConcept> getDocumentPending(int dbid, Integer page, Integer size) throws Exception;

    byte[] getDocumentLatestPublished(Integer dbid) throws Exception;

    List<IdNamePair> getSchemes() throws Exception;

    List<ConceptTreeNode> getChildren(String conceptId) throws Exception;
}
