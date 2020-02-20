package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.*;

import java.util.List;

public interface InformationManagerDAL extends BaseDAL {
    // UI Routines
    SearchResult getMRU(Integer size, List<String> supertypes) throws Exception;
    Concept getConcept(String conceptId) throws Exception;
    SearchResult search(String text, List<String> supertypes, Integer size, Integer page, List<String> models, List<String> statuses) throws Exception;
    List<Concept> complete(String terms, List<String> models, List<String> statuses) throws Exception;
    String completeWord(String terms) throws Exception;
    String getConceptName(String id) throws Exception;
    List<Concept> getChildren(String conceptId) throws Exception;
    List<Concept> getParents(String conceptId) throws Exception;
    List<ConceptTreeNode> getParentTree(String conceptId, String root) throws Exception;
    List<Concept> getRootConcepts() throws Exception;
    List<ConceptTreeNode> getHierarchy(String conceptId) throws Exception;

    boolean insertConcept(Concept concept) throws Exception;
    Integer getConceptId(String conceptIri) throws Exception;
    Integer getAxiomId(String token) throws Exception;
    Integer getOperatorId(String operator) throws Exception;
    boolean createSubType(SubType subType) throws Exception;
    boolean insertPropertyClass(PropertyClass propertyClass) throws Exception;

    // Filing routines
    int allocateConceptId(String conceptIri) throws Exception;


    void upsertConcept(Concept concept) throws Exception;

/*    List<ConceptRelation> getConceptSupertypes(String conceptId, Boolean includeInherited) throws Exception;

    List<ConceptRelation> getConceptRelations(String conceptId, Boolean includeInherited) throws Exception;
    void replaceConceptRelations(String conceptId, List<ConceptRelation> relations) throws Exception;

    void insertConceptRelations(String conceptId, List<ConceptRelation> relations) throws Exception;*/


    // UI routines
    List<Namespace> getNamespaces() throws Exception;
    List<Concept> getCodeSchemes() throws Exception;

    List<Axiom> getAxioms() throws Exception;

    boolean createConcept(Concept concept) throws Exception;

    boolean updateConcept(Concept concept) throws Exception;

    List<Concept> getAncestors(String conceptIri) throws Exception;

    List<SimpleConcept> getAxiomSupertypes(int conceptId, String axiom) throws Exception;
    List<RoleGroup> getAxiomRoleGroups(int conceptId, String axiom) throws Exception;

    List<PropertyRange> getPropertyRanges(Integer conceptId) throws Exception;

    List<PropertyDomain> getPropertyDomains(Integer conceptId) throws Exception;

    List<String> getPropertyChains(int conceptId) throws Exception;

    boolean addAxiomExpressionSupertype(String conceptIri, String axiom, String supertypeIri) throws Exception;

    boolean addAxiomExpressionRoleGroupProperty(String conceptIri, String axiom, PropertyDefinition definition, Integer group) throws Exception;

    boolean delAxiomExpressionSupertype(String conceptIri, String axiom, String supertype) throws Exception;

    boolean delAxiomExpressionRoleGroupProperty(String conceptIri, String axiom, Integer group, String property, String type, String value) throws Exception;

    boolean delAxiomExpressionRoleGroup(String conceptIri, String axiom, Integer group) throws Exception;

    boolean delAxiom(String conceptIri, String axiom) throws Exception;



    /*
    void upsertConceptDefinition(ConceptDefinition definition) throws Exception;
    void upsertPropertyDomain(int propertyDbid, int conceptDbid, int statusDbid, Domain domain) throws Exception;
    void upsertPropertyRange(int propertyDbid, int statusDbid, List<SimpleExpressionConstraint> rangeClass) throws Exception;

    SearchResult getMRU(Integer size) throws Exception;
    List<Model> getModels() throws Exception;

    Concept getConceptSummary(String id) throws Exception;



    //----------------------------------

    void publishDocument(int dbid, String level) throws Exception;

    DocumentInfo getDocument(int dbid) throws Exception;

    void updateDocument(int dbid, String documentJson) throws Exception;

    void createConcept(int document, String json, String name) throws Exception;

    Concept updateConcept(Concept newConcept) throws Exception;



    String getConceptJSON(String id) throws Exception;


    ConceptDefinition getConceptDefinition(String id) throws Exception;

    List<DocumentInfo> getDocuments() throws Exception;

    String validateIds(List<String> ids) throws Exception;


    List<DraftConcept> getDocumentPending(int dbid, Integer page, Integer size) throws Exception;

    byte[] getDocumentLatestPublished(Integer dbid) throws Exception;

    List<IdNamePair> getSchemes() throws Exception;


*/
}
