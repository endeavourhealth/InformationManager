package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.transform.model.Concept;

import java.util.List;
import java.util.Set;

public interface InformationManagerDAL extends BaseDAL {
    Set<String> getUndefinedConcepts();

    SearchResult getMRU(Integer size, List<String> supertypes) throws Exception;
    SearchResult search(String text, List<String> supertypes, Integer size, Integer page) throws Exception;

    String getAssertedDefinition(String iri) throws Exception;

    int getNamespaceIdWithCreate(String iri, String prefix) throws Exception;

    void saveConcepts(List<? extends Concept> concepts) throws Exception;

    Concept getConcept(String iri) throws Exception;
    List<RelatedConcept> getDefinition(String iri) throws Exception;
    List<Property> getProperties(String iri, boolean inherited) throws Exception;
    PagedResultSet<RelatedConcept> getSources(String iri, List<String> relationships, int limit, int page) throws Exception;
    PagedResultSet<RelatedConcept> getTargets(String iri, List<String> relationships, int limit, int page) throws Exception;
    List<RelatedConcept> getTree(String iri, String root, List<String> relationships) throws Exception;
}
