package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.*;

import java.sql.SQLException;
import java.util.List;

public interface InformationManagerDAL extends BaseDAL {
    // UI Routines
    SearchResult getMRU(Integer size, List<String> supertypes) throws Exception;
    List<Concept> getStatuses() throws Exception;


    Concept getConcept(int id) throws Exception;
    Concept getConcept(String iri) throws Exception;
    SearchResult search(String text, List<String> supertypes, Integer size, Integer page, List<String> models, List<String> statuses) throws Exception;
    List<Concept> complete(String terms, List<String> models, List<String> statuses) throws Exception;
    String completeWord(String terms) throws Exception;
    String getConceptName(String id) throws Exception;
    List<Concept> getChildren(int id) throws Exception;
    List<Concept> getParents(int id) throws Exception;
    List<ConceptTreeNode> getParentTree(int id, String root) throws Exception;
    List<Concept> getRootConcepts() throws Exception;
    List<ConceptTreeNode> getHierarchy(int id) throws Exception;

    boolean insertNamespace(Namespace namespace) throws Exception;
    boolean insertConcept(Concept concept) throws Exception;
    Integer getConceptId(String conceptIri) throws Exception;
    Integer getAxiomId(String token) throws Exception;
    Integer getOperatorId(String operator) throws Exception;
    boolean createSubType(SubType subType) throws Exception;
    boolean insertPropertyClass(PropertyClass propertyClass) throws Exception;
    boolean insertPropertyData(PropertyData propertyData) throws Exception;
    boolean insertPropertyChain(PropertyChain propertyChain) throws Exception;
    boolean insertInverseProperty(InverseProperty inverseProperty) throws Exception;
    boolean insertPropertyDomain(PropertyDomain propertyDomain) throws Exception;
    boolean insertPropertyRange(PropertyRange propertyRange) throws Exception;
    boolean insertPropertyTransitive(PropertyTransitive propertyTransitive) throws Exception;
    boolean updateConceptStatus() throws Exception;

    // Filing routines
    int allocateConceptId(String conceptIri) throws Exception;


    void upsertConcept(Concept concept) throws Exception;


    // UI routines
    List<Namespace> getNamespaces() throws Exception;
    List<Concept> getCodeSchemes() throws Exception;

    List<Axiom> getAxioms() throws Exception;

    String createConcept(Concept concept) throws Exception;
    boolean updateConcept(Concept concept) throws Exception;
    boolean deleteConcept(int conceptId) throws SQLException;

    List<Concept> getAncestors(int conceptId) throws Exception;

    List<SimpleConcept> getAxiomSupertypes(int conceptId, String axiom) throws Exception;
    List<RoleGroup> getAxiomRoleGroups(int conceptId, String axiom) throws Exception;

    List<PropertyRange> getPropertyRanges(Integer conceptId) throws Exception;

    List<PropertyDomain> getPropertyDomains(Integer conceptId) throws Exception;

    List<String> getPropertyChains(int conceptId) throws Exception;

    boolean addAxiomSupertype(int id, String axiom, String supertypeIri) throws Exception;

    boolean addAxiomRoleGroupProperty(int id, String axiom, PropertyDefinition definition, Integer group) throws Exception;

    boolean delSupertype(int supertypeId) throws Exception;

    boolean delProperty(int propertyId, String type) throws Exception;

    boolean delAxiomRoleGroup(int id, String axiom, Integer group) throws Exception;

    boolean delAxiom(int id, String axiom) throws Exception;

    List<String> getOperators() throws Exception;

    boolean addPropertyRange(int id, PropertyRange propertyRange) throws Exception;

    boolean delPropertyRange(int propertyRangeId) throws Exception;

    boolean addPropertyDomain(int id, PropertyDomain propertyDomain) throws Exception;

    boolean delPropertyDomain(int propertyDomainId) throws Exception;
}
