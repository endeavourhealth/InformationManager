package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;

import java.util.List;

public interface InformationManagerDAL {
    int getOrCreateDocumentDbid(String document) throws Exception;

    Integer getDocumentDbid(String document) throws Exception;

    void publishDocument(int dbid, String level) throws Exception;

    Document getDocument(int dbid) throws Exception;

    void updateDocument(int dbid, String documentJson) throws Exception;

    void insertConcept(int document, String json, Status status) throws Exception;

    Concept updateConcept(Concept concept) throws Exception;

    SearchResult getMRU() throws Exception;

    SearchResult search(String text, Integer size, Integer page, String relationship, String target) throws Exception;

    String getConceptJSON(String id) throws Exception;

    String getConceptName(String id) throws Exception;

    List<Document> getDocuments() throws Exception;

    String validateIds(List<String> ids) throws Exception;

    Integer getConceptDbid(String id) throws Exception;

    Concept getConcept(String id) throws Exception;

    List<DraftConcept> getDocumentPending(int dbid, Integer page, Integer size) throws Exception;

    byte[] getDocumentLatestPublished(Integer dbid) throws Exception;
}
