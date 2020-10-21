package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.endeavourhealth.informationmanager.common.models.AxiomType;
import org.endeavourhealth.informationmanager.common.models.ConceptType;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class DocumentFilerLogic {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentFilerLogic.class);
    private static final String SUBTYPE = "sn:116680003";

    private DocumentFilerJDBCDAL dal;
    private ObjectMapper objectMapper;

    private final Set<String> undefinedConcepts = new HashSet<>();
    private final Map<String, Integer> namespaceMap = new HashMap<>();

    public DocumentFilerLogic() throws SQLException {
        dal = new DocumentFilerJDBCDAL();
    }

    // ============================== PUBLIC METHODS ==============================
    public Set<String> getUndefinedConcepts() {
        return undefinedConcepts;
    }

    // ------------------------------ NAMESPACE ------------------------------
    private Integer getNamespaceId(String prefix) throws SQLException {
        Integer namespace = namespaceMap.get(prefix);
        if (namespace == null) {
            namespace = dal.getNamespaceId(prefix);
            if (namespace != null)
                namespaceMap.put(prefix, namespace);
        }
        return namespaceMap.get(prefix);
    }

    public int getOrCreateNamespaceDbid(String iri, String prefix) throws SQLException {
        Integer namespace = getNamespaceId(prefix);
        if (namespace == null) {
            namespace = dal.createNamespaceId(iri, prefix);
            namespaceMap.put(prefix, namespace);
        }

        return namespace;
    }

    // ------------------------------ ONTOLOGY ------------------------------
    public int getOrCreateOntologyDbid(String iri) throws SQLException {
        Integer result = dal.getOntologyId(iri);
        if (result == null)
            result = dal.createOntologyId(iri);

        return result;
    }

    // ------------------------------ MODULE ------------------------------
    public int getOrCreateModuleDbid(String iri) throws SQLException {
        Integer result = dal.getModuleId(iri);
        if (result == null)
            result = dal.createModuleId(iri);

        return result;
    }

    // ------------------------------ MODULE ------------------------------
    public void setDocument(UUID documentId, String moduleIri, String ontologyIri) throws SQLException {
        if (Strings.isNullOrEmpty(moduleIri))
            moduleIri = ontologyIri;

        int ontologyDbid = getOrCreateOntologyDbid(ontologyIri);
        int moduleDbid = getOrCreateModuleDbid(moduleIri);

        dal.setDocument(documentId, moduleDbid, ontologyDbid);
    }


    // ------------------------------ CONCEPTS ------------------------------
    public Integer getOrCreateConceptDbid(String iri) throws SQLException {
        return getOrCreateConceptDbid(iri, null);
    }

    public Integer getOrCreateConceptDbid(String iri, String uuid) throws SQLException {
        if (iri == null || iri.isEmpty())
            return null;

        Integer dbid = dal.getOrCreateConceptDbid(iri, uuid);
        if (dbid == null)
            LOG.error("Unable to create draft concept [" + iri + "]");

        return dbid;
    }

    public void cacheOrCreateConcepts(List<? extends Concept> concepts) throws Exception {
        if (concepts == null || concepts.size() == 0)
            return;

        for (Concept concept : concepts) {
            getOrCreateConceptDbid(concept.getIri(), concept.getId());
        }
    }

    public void saveConcepts(int moduleDbid, List<? extends Concept> concepts, ConceptType conceptType) throws Exception {
        if (concepts == null || concepts.size() == 0)
            return;

        int i = 0;
        for (Concept concept : concepts) {
            undefinedConcepts.remove(concept.getIri());

            if (!concept.getisRef()) {
                String prefix = dal.getPrefix(concept.getIri());
                int namespace = getNamespaceId(prefix);

                Integer scheme = getOrCreateConceptDbid(concept.getScheme());
                dal.upsertConcept(namespace, concept, conceptType, scheme);

                int dbid = getOrCreateConceptDbid(concept.getIri());
                saveConceptDefinition(concept, dbid, moduleDbid);
            }

            i++;
            if (i % 1000 == 0)
                LOG.info("Processed " + i + " of " + concepts.size());
        }
    }

    private void saveConceptDefinition(Concept concept, int conceptDbid, int moduleDbid) throws SQLException, JsonProcessingException {
        if (concept instanceof Clazz) {
            saveClassDefinition(concept.getIri(), conceptDbid, moduleDbid, (Clazz) concept);
        } else if (concept instanceof ObjectProperty) {
            saveObjectPropertyDefinition(concept.getIri(), conceptDbid, moduleDbid, (ObjectProperty) concept);
        } else if (concept instanceof DataProperty) {
            saveDataPropertyDefinition(concept.getIri(), conceptDbid, moduleDbid, (DataProperty) concept);
        } else if (concept instanceof AnnotationProperty) {
            saveAnnotationPropertyDefinition(concept.getIri(), conceptDbid, moduleDbid, (AnnotationProperty) concept);
        } else if (concept instanceof Individual) {
            saveIndividualDefinition(concept.getIri(), conceptDbid, moduleDbid, (Individual) concept);
        } else {
            LOG.error("Unknown concept type");
        }

        if (exists(concept.getIsA())) {
            saveConceptIsAs(concept.getIri(), conceptDbid, concept.getIsA());
        }
    }
    // ============================== PRIVATE METHODS ==============================
    // ------------------------------ PRIVATE METHODS - ASSERTED CONCEPTS ------------------------------
    private void saveClassDefinition(String iri, int conceptDbid, int moduleDbid, Clazz clazz) throws SQLException, JsonProcessingException {
        // Class axioms are a straight concept_axiom save
        if (clazz.getExpression() != null)
            throw new IllegalStateException("Concept axiom expressions not currently implemented [" + iri + "]");

        if (exists(clazz.getSubClassOf())) {
            for (ClassAxiom ax : clazz.getSubClassOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.SUBCLASSOF, toJson(ax), ax.getVersion());
            }
        }

        if (exists(clazz.getEquivalentTo())) {
            for (ClassAxiom ax : clazz.getEquivalentTo()) {
                dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.EQUIVALENTTO, toJson(ax), ax.getVersion());
            }
        }

        if (exists(clazz.getDisjointWithClass())) {
            dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, null, AxiomType.DISJOINTWITH, toJson(clazz.getDisjointWithClass()), null);
        }

        if (exists(clazz.getAnnotations())) {
            dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, null, AxiomType.ANNOTATION, toJson(clazz.getAnnotations()), null);
        }
    }

    private void saveObjectPropertyDefinition(String iri, int conceptDbid, int moduleDbid, ObjectProperty objectProperty) throws SQLException, JsonProcessingException {
        // Object property definitions are concept_axiom plus range and domain into cpo/cpd
        if (exists(objectProperty.getSubObjectPropertyOf())) {
            for (PropertyAxiom ax : objectProperty.getSubObjectPropertyOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.SUBOBJECTPROPERTY, toJson(ax), ax.getVersion());
            }
        }

        if (exists(objectProperty.getObjectPropertyRange())) {
            for (ClassAxiom ax : objectProperty.getObjectPropertyRange()) {
                Integer axiomDbid = dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.SUBPROPERTYRANGE, toJson(ax), ax.getVersion());
                flattenAndSaveDefinition(iri, conceptDbid, axiomDbid, ax, AxiomType.SUBPROPERTYRANGE);
            }
        }

        if (exists(objectProperty.getPropertyDomain())) {
            for (ClassAxiom ax : objectProperty.getPropertyDomain()) {
                Integer axiomDbid = dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.PROPERTYDOMAIN, toJson(ax), ax.getVersion());
                flattenAndSaveDefinition(iri, conceptDbid, axiomDbid, ax, AxiomType.PROPERTYDOMAIN);
            }
        }

        if (objectProperty.getAnnotations() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, null, AxiomType.ANNOTATION, toJson(objectProperty.getAnnotations()), null);
        }

        if (exists(objectProperty.getSubPropertyChain())) {
            for (ClassAxiom ax : objectProperty.getPropertyDomain()) {
                dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.SUBPROPERTYCHAIN, toJson(ax), ax.getVersion());
            }
        }

        if (objectProperty.getInversePropertyOf() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, objectProperty.getInversePropertyOf().getId(), AxiomType.INVERSEPROPERTYOF, toJson(objectProperty.getInversePropertyOf()), null);
        }

        if (objectProperty.getIsFunctional() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, objectProperty.getIsFunctional().getId(), AxiomType.ISFUNCTIONAL, toJson(objectProperty.getIsFunctional()), null);
        }

        if (objectProperty.getIsTransitive() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, objectProperty.getIsTransitive().getId(), AxiomType.ISTRANSITIVE, toJson(objectProperty.getIsTransitive()), null);
        }

        if (objectProperty.getIsSymmetric() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, objectProperty.getIsSymmetric().getId(), AxiomType.ISSYMMETRIC, toJson(objectProperty.getIsSymmetric()), null);
        }

        if (objectProperty.getIsReflexive() != null) LOG.error("Unhandled Asserted ObjectProperty = IsReflexive");
    }

    private void saveDataPropertyDefinition(String iri, int conceptDbid, int moduleDbid, DataProperty dataProperty) throws SQLException, JsonProcessingException {
        // Data property definitions are concept_axiom plus range and domain into cpo/cpd
        if (dataProperty.getSubDataPropertyOf() != null) {
            for (PropertyAxiom ax : dataProperty.getSubDataPropertyOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.SUBDATAPROPERTY, toJson(ax), ax.getVersion());
            }
        }

        if (dataProperty.getDataPropertyRange() != null) {
            for (PropertyRangeAxiom ax : dataProperty.getDataPropertyRange()) {
                Integer axiomDbid = dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.SUBPROPERTYRANGE, toJson(ax), ax.getVersion());
                flattenAndSaveDefinition(iri, conceptDbid, axiomDbid, ax);
            }
        }

        if (dataProperty.getPropertyDomain() != null) {
            for (ClassAxiom ax : dataProperty.getPropertyDomain()) {
                Integer axiomDbid = dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.PROPERTYDOMAIN, toJson(ax), ax.getVersion());
                flattenAndSaveDefinition(iri, conceptDbid, axiomDbid, ax, AxiomType.PROPERTYDOMAIN);
            }
        }

        if (dataProperty.getAnnotations() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, null, AxiomType.ANNOTATION, toJson(dataProperty.getAnnotations()), null);
        }

        if (dataProperty.getIsFunctional() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, dataProperty.getIsFunctional().getId(), AxiomType.ISFUNCTIONAL, toJson(dataProperty.getIsFunctional()), null);
        }
    }

    private void saveAnnotationPropertyDefinition(String iri, int conceptDbid, int moduleDbid, AnnotationProperty annotationProperty) throws SQLException, JsonProcessingException {
        // Annotations definitions are straight concept_axiom saves
        if (annotationProperty.getSubAnnotationPropertyOf() != null) {
            for (PropertyAxiom ax : annotationProperty.getSubAnnotationPropertyOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.SUBANNOTATIONPROPERTY, toJson(ax), ax.getVersion());
            }
        }

        if (annotationProperty.getPropertyRange() != null) LOG.error("Unhandled Asserted AnnotationProperty = PropertyRange");
    }

    private void saveIndividualDefinition(String iri, int conceptDbid, int moduleDbid, Individual individual) throws SQLException, JsonProcessingException {
        // Individual definitions are straight concept_axiom saves
        if (exists(individual.getPropertyDataValue())) {
            for (DataPropertyAssertionAxiom ax: individual.getPropertyDataValue()) {
                dal.upsertConceptAxiom(iri, conceptDbid, moduleDbid, ax.getId(), AxiomType.PROPERTYDATAVALUE, toJson(ax), ax.getVersion());
            }
        }
    }

    private void saveConceptIsAs(String iri, int conceptDbid, List<Concept> concepts) throws SQLException {
        // Save "Is a" relationship definitions direct to CPO
        for (Concept concept : concepts) {
            dal.upsertCPO(iri, conceptDbid, SUBTYPE, concept.getIri());
        }
    }

    // ------------------------------ PRIVATE METHODS ------------------------------
    private void flattenAndSaveDefinition(String conceptIri, int conceptDbid, Integer axiomDbid, ClassAxiom ax, AxiomType axiomType) throws SQLException {
        dal.cleanupAxiomConceptProperties(conceptDbid, axiomDbid);

        if (ax.getClazz() != null && !ax.getClazz().isEmpty())
            // Classes can go direct into CPO
            dal.upsertCPO(conceptIri, conceptDbid, axiomType.getName(), ax.getClazz(), axiomDbid);
        else if (exists(ax.getIntersection()))
            recurseAndSaveCexListToCpo(conceptIri, conceptDbid, axiomDbid, ax.getUnion(), Operator.AND, axiomType);
        else if (exists(ax.getUnion()))
            recurseAndSaveCexListToCpo(conceptIri, conceptDbid, axiomDbid, ax.getUnion(), Operator.OR, axiomType);
        else if (ax.getPropertyObject() != null) {
            String iri = createAnonymousPropertyObjectConcept(ax.getPropertyObject(), axiomDbid);
            dal.upsertCPO(conceptIri, conceptDbid, axiomType.getName(), iri, axiomDbid);
        } else
            LOG.error("Unsupported definition");
    }

    private void flattenAndSaveDefinition(String conceptIri, int conceptDbid, Integer axiomDbid, PropertyRangeAxiom ax) throws SQLException {
        if (!Strings.isNullOrEmpty(ax.getDataType()))
            dal.upsertCPO(conceptIri, conceptDbid, AxiomType.SUBPROPERTYRANGE.getName(), ax.getDataType(), axiomDbid);
        else
            LOG.error("Unsupported data property range definition");
    }

    // ------------------------------

    private void recurseAndSaveCexListToCpo(String conceptIri, int conceptDbid, Integer axiomDbid, List<ClassExpression> union, Operator operator, AxiomType axiomType) throws SQLException {
        if (union == null)
            return;

        for (ClassExpression cex : union) {
            if (!Strings.isNullOrEmpty(cex.getClazz())) {
                // Classes can go straight into CPO
                dal.upsertCPO(conceptIri, conceptDbid, axiomType.getName(), cex.getClazz(), axiomDbid, operator);
            } else if (exists(cex.getObjectOneOf())) {
                // OneOfs can go straight into CPDS
                for (String individual : cex.getObjectOneOf()) {
                    dal.upsertCPD(conceptIri, conceptDbid, axiomType.getName(), individual, ":Concept", operator);
                }
            } else if (cex.getPropertyObject() != null) {
                String iri = createAnonymousPropertyObjectConcept(cex.getPropertyObject(), axiomDbid);

                // Add union of anonymous
                dal.upsertCPO(conceptIri, conceptDbid, axiomType.getName(), iri, axiomDbid, operator);
            } else
                LOG.warn("Nested union");
        }
    }

    private String createAnonymousPropertyObjectConcept(OPECardinalityRestriction po, Integer axiomDbid) throws SQLException {
        // Create anonymous & define it
        UUID id = UUID.randomUUID();
        int dbid = dal.addAnonymousConcept(id, ConceptType.OBJECTPROPERTY);
        String iri = ":" + id.toString();

        if (!Strings.isNullOrEmpty(po.getInverseOf())) {
            dal.upsertInverseCPO(iri, dbid, po.getInverseOf(), po.getClazz(), axiomDbid);
        } else {
            LOG.warn("Union -> PropertyObject");
        }

        return iri;
    }

    // ------------------------------ Private helpers ------------------------------
    private String toJson(Object o) throws JsonProcessingException {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }

    private boolean exists(List<?> list) {
        return (list != null && list.size() != 0);
    }

}
