package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public int getNamespaceIdWithCreate(String iri, String prefix) throws SQLException {
        Integer namespace = getNamespaceId(prefix);
        if (namespace == null) {
            namespace = dal.createNamespaceId(iri, prefix);
            namespaceMap.put(prefix, namespace);
        }

        return namespace;
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

    public void saveConcepts(List<? extends Concept> concepts, ConceptType conceptType, boolean inferred) throws Exception {
        if (concepts == null || concepts.size() == 0)
            return;

        int i = 0;
        for (Concept concept : concepts) {
            undefinedConcepts.remove(concept.getIri());

            if (inferred) {
                int dbid = getOrCreateConceptDbid(concept.getIri());
                saveInferredConcept(concept, dbid);
            } else {
                String prefix = dal.getPrefix(concept.getIri());
                int namespace = getNamespaceId(prefix);
                Integer scheme = getOrCreateConceptDbid(concept.getScheme());
                dal.upsertConcept(namespace, concept, conceptType, scheme);
                int dbid = getOrCreateConceptDbid(concept.getIri());
                saveAssertedConcept(concept, dbid);
            }

            i++;
            if (i % 1000 == 0)
                LOG.info("Processed " + i + " of " + concepts.size());
        }
    }

    private void saveAssertedConcept(Concept concept, int dbid) throws SQLException, JsonProcessingException {
        if (concept instanceof Clazz) {
            saveAssertedClassConceptAxioms(concept.getIri(), dbid, (Clazz) concept);
        } else if (concept instanceof ObjectProperty) {
            saveAssertedObjectPropertyConceptAxioms(concept.getIri(), dbid, (ObjectProperty) concept);
        } else if (concept instanceof DataProperty) {
            saveAssertedDataPropertyConceptAxioms(concept.getIri(), dbid, (DataProperty) concept);
        } else if (concept instanceof AnnotationProperty) {
            saveAssertedAnnotationPropertyConceptAxioms(concept.getIri(), dbid, (AnnotationProperty) concept);
        } else if (concept instanceof Individual) {
            saveAssertedIndividualAxioms(concept.getIri(), dbid, (Individual) concept);
        } else {
            LOG.error("Asserted - Unknown");
        }
    }

    private void saveInferredConcept(Concept concept, int dbid) throws Exception {
        dal.delCPO(concept.getIri());
        dal.delCPD(concept.getIri());

        if (concept instanceof Clazz) {
            saveInferredClassConceptProperties(concept.getIri(), dbid, (Clazz) concept);
        } else if (concept instanceof ObjectProperty) {
            saveInferredObjectPropertyProperties(concept.getIri(), dbid, (ObjectProperty) concept);
        } else if (concept instanceof DataProperty) {
            saveInferredDataPropertyProperties(concept.getIri(), dbid, (DataProperty) concept);
        } else if (concept instanceof AnnotationProperty) {
            saveInferredAnnotationPropertyProperties(concept.getIri(), dbid, (AnnotationProperty) concept);
        } else if (concept instanceof Individual) {
            saveInferredIndividual(concept.getIri(), dbid, (Individual) concept);
        } else {
            LOG.error("Inferred - Unknown");
        }
    }

    // ============================== PRIVATE METHODS ==============================


    // ------------------------------ PRIVATE METHODS - ASSERTED CONCEPTS ------------------------------
    private void saveAssertedClassConceptAxioms(String iri, int conceptDbid, Clazz clazz) throws SQLException, JsonProcessingException {
        if (clazz.getExpression() != null)
            throw new IllegalStateException("Concept axiom expressions not currently supported [" + iri + "]");

        if (!isEmpty(clazz.getSubClassOf())) {
            for (ClassAxiom ax : clazz.getSubClassOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.SUBCLASSOF, toJson(ax), ax.getVersion());
            }
        }

        if (!isEmpty(clazz.getEquivalentTo())) {
            for (ClassAxiom ax : clazz.getEquivalentTo()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.EQUIVALENTTO, toJson(ax), ax.getVersion());
            }
        }

        if (!isEmpty(clazz.getDisjointWithClass())) {
            dal.upsertConceptAxiom(iri, conceptDbid, null, AxiomType.DISJOINTWITH, toJson(clazz.getDisjointWithClass()), null);
        }

        if (!isEmpty(clazz.getAnnotations())) {
            dal.upsertConceptAxiom(iri, conceptDbid, null, AxiomType.ANNOTATION, toJson(clazz.getAnnotations()), null);
        }
    }

    private void saveAssertedObjectPropertyConceptAxioms(String iri, int conceptDbid, ObjectProperty objectProperty) throws SQLException, JsonProcessingException {
        if (!isEmpty(objectProperty.getSubObjectPropertyOf())) {
            for (PropertyAxiom ax : objectProperty.getSubObjectPropertyOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.SUBOBJECTPROPERTY, toJson(ax), ax.getVersion());
            }
        }

        if (!isEmpty(objectProperty.getObjectPropertyRange())) {
            for (ClassAxiom ax : objectProperty.getObjectPropertyRange()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.SUBPROPERTYRANGE, toJson(ax), ax.getVersion());
            }
        }

        if (!isEmpty(objectProperty.getPropertyDomain())) {
            for (ClassAxiom ax : objectProperty.getPropertyDomain()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.PROPERTYDOMAIN, toJson(ax), ax.getVersion());
            }
        }

        if (objectProperty.getAnnotations() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, null, AxiomType.ANNOTATION, toJson(objectProperty.getAnnotations()), null);
        }

        if (!isEmpty(objectProperty.getSubPropertyChain())) {
            for (ClassAxiom ax : objectProperty.getPropertyDomain()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.SUBPROPERTYCHAIN, toJson(ax), ax.getVersion());
            }
        }

        if (objectProperty.getInversePropertyOf() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, objectProperty.getInversePropertyOf().getId(), AxiomType.INVERSEPROPERTYOF, toJson(objectProperty.getInversePropertyOf()), null);
        }

        if (objectProperty.getIsFunctional() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, objectProperty.getIsFunctional().getId(), AxiomType.ISFUNCTIONAL, toJson(objectProperty.getIsFunctional()), null);
        }

        if (objectProperty.getIsTransitive() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, objectProperty.getIsTransitive().getId(), AxiomType.ISTRANSITIVE, toJson(objectProperty.getIsTransitive()), null);
        }

        if (objectProperty.getIsSymmetric() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, objectProperty.getIsSymmetric().getId(), AxiomType.ISSYMMETRIC, toJson(objectProperty.getIsSymmetric()), null);
        }

        if (objectProperty.getIsReflexive() != null) LOG.error("Unhandled Asserted ObjectProperty = IsReflexive");
    }

    private void saveAssertedDataPropertyConceptAxioms(String iri, int conceptDbid, DataProperty dataProperty) throws SQLException, JsonProcessingException {
        if (dataProperty.getSubDataPropertyOf() != null) {
            for (PropertyAxiom ax : dataProperty.getSubDataPropertyOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.SUBDATAPROPERTY, toJson(ax), ax.getVersion());
            }
        }

        if (dataProperty.getDataPropertyRange() != null) {
            for (PropertyRangeAxiom ax : dataProperty.getDataPropertyRange()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.SUBPROPERTYRANGE, toJson(ax), ax.getVersion());
            }
        }

        if (dataProperty.getPropertyDomain() != null) {
            for (ClassAxiom ax : dataProperty.getPropertyDomain()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.PROPERTYDOMAIN, toJson(ax), ax.getVersion());
            }
        }

        if (dataProperty.getAnnotations() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, null, AxiomType.ANNOTATION, toJson(dataProperty.getAnnotations()), null);
        }

        if (dataProperty.getIsFunctional() != null) {
            dal.upsertConceptAxiom(iri, conceptDbid, dataProperty.getIsFunctional().getId(), AxiomType.ISFUNCTIONAL, toJson(dataProperty.getIsFunctional()), null);
        }
    }

    private void saveAssertedAnnotationPropertyConceptAxioms(String iri, int conceptDbid, AnnotationProperty annotationProperty) throws SQLException, JsonProcessingException {
        if (annotationProperty.getSubAnnotationPropertyOf() != null) {
            for (PropertyAxiom ax : annotationProperty.getSubAnnotationPropertyOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.SUBANNOTATIONPROPERTY, toJson(ax), ax.getVersion());
            }
        }

        if (annotationProperty.getPropertyRange() != null) LOG.error("Unhandled Asserted AnnotationProperty = PropertyRange");
    }

    private void saveAssertedIndividualAxioms(String iri, int conceptDbid, Individual individual) throws SQLException, JsonProcessingException {
        if (!isEmpty(individual.getPropertyDataValue())) {
            for (DataPropertyAssertionAxiom ax: individual.getPropertyDataValue()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), AxiomType.PROPERTYDATAVALUE, toJson(ax), ax.getVersion());
            }
        }
    }
    // ------------------------------ PRIVATE METHODS - INFERRED CONCEPTS ------------------------------
    private void saveInferredClassConceptProperties(String iri, int conceptDbid, Clazz clazz) throws SQLException {
        int roleGroup = 0;
        if (clazz.getSubClassOf() != null) {
            for (ClassAxiom ax : clazz.getSubClassOf()) {
                saveConceptProperty(iri, conceptDbid, ax, roleGroup);
                roleGroup++;
            }
        }

        if (!isEmpty(clazz.getAnnotations())) {
            for (Annotation annotation: clazz.getAnnotations()) {
                dal.upsertCPD(iri, conceptDbid, annotation.getProperty(), annotation.getValue(), 0, null, null);
            }
        }

        if (clazz.getExpression() != null)
            LOG.error("Unhandled Inferred Class = Expression");

        if (!isEmpty(clazz.getDisjointWithClass()))
            LOG.error("Unhandled Inferred Class = DisjointWith");

        if (!isEmpty(clazz.getEquivalentTo()))
            LOG.error("Unhandled Inferred Class = EquivalentTo");
    }

    private void saveInferredObjectPropertyProperties(String iri, int conceptDbid, ObjectProperty objectProperty) throws Exception {
        int roleGroup = 0;
        if (objectProperty.getSubObjectPropertyOf() != null) {
            for (PropertyAxiom ax : objectProperty.getSubObjectPropertyOf()) {
                dal.upsertCPO(iri, conceptDbid, SUBTYPE, ax.getProperty(), roleGroup, null, null);
                roleGroup++;
            }
        }

        if (objectProperty.getIsSymmetric() != null) LOG.error("Unhandled Inferred ObjectProperty = IsSymmetric");
        if (objectProperty.getIsReflexive() != null) LOG.error("Unhandled Inferred ObjectProperty = IsReflexive");
        if (objectProperty.getIsFunctional() != null) LOG.error("Unhandled Inferred ObjectProperty = IsFunctional");
        if (objectProperty.getInversePropertyOf() != null) LOG.error("Unhandled Inferred ObjectProperty = InversePropertyOf");
        if (!isEmpty(objectProperty.getPropertyDomain())) LOG.error("Unhandled Inferred ObjectProperty = PropertyDomain");
        if (!isEmpty(objectProperty.getSubPropertyChain())) LOG.error("Unhandled Inferred ObjectProperty = SubPropertyChain");
        if (!isEmpty(objectProperty.getObjectPropertyRange())) LOG.error("Unhandled Inferred ObjectProperty = ObjectPropertyRange");
    }

    private void saveInferredDataPropertyProperties(String iri, int conceptDbid, DataProperty dataProperty) throws Exception {
        int roleGroup = 0;
        if (dataProperty.getSubDataPropertyOf() != null) {
            for (PropertyAxiom ax : dataProperty.getSubDataPropertyOf()) {
                dal.upsertCPO(iri, conceptDbid, SUBTYPE, ax.getProperty(), roleGroup, null, null);
                roleGroup++;
            }
        }

        if (dataProperty.getIsFunctional() != null) LOG.error("Unhandled Inferred DataProperty = IsFunctional");
        if (!isEmpty(dataProperty.getPropertyDomain())) LOG.error("Unhandled Inferred DataProperty = PropertyDomain");
        if (!isEmpty(dataProperty.getDataPropertyRange())) LOG.error("Unhandled Inferred DataProperty = DataPropertyRange");
    }

    private void saveInferredAnnotationPropertyProperties(String iri, int conceptDbid, AnnotationProperty annotationProperty) throws Exception {
        int roleGroup = 0;
        if (annotationProperty.getSubAnnotationPropertyOf() != null) {
            for (PropertyAxiom ax : annotationProperty.getSubAnnotationPropertyOf()) {
                dal.upsertCPO(iri, conceptDbid, SUBTYPE, ax.getProperty(), roleGroup, null, null);
                roleGroup++;
            }
        }

        if (!isEmpty(annotationProperty.getAnnotations())) {
            for (Annotation annotation: annotationProperty.getAnnotations()) {
                dal.upsertCPD(iri, conceptDbid, annotation.getProperty(), annotation.getValue(), 0, null, null);
            }
        }

        if (!isEmpty(annotationProperty.getPropertyRange())) LOG.error("Unhandled Inferred AnnotationProperty = PropertyRange");
    }

    private void saveInferredIndividual(String iri, int conceptDbid, Individual individual) throws Exception {
        if (!isEmpty(individual.getPropertyDataValue())) {
            for (DataPropertyAssertionAxiom ax : individual.getPropertyDataValue()) {
                dal.upsertCPD(iri, conceptDbid, ax.getProperty(), ax.getValue(), 0, null, null);
            }
        }
    }

    // ------------------------------ PRIVATE METHODS - INFERRED CONCEPTS BASE ------------------------------
    private void saveConceptProperty(String iri, int conceptDbid, ClassAxiom ax, int roleGroup) throws SQLException {
        if (ax.getClazz() != null && !ax.getClazz().isEmpty()) {
            dal.upsertCPO(iri, conceptDbid, SUBTYPE, ax.getClazz(), roleGroup, null, null);
        } else if (ax.getIntersection() != null && !ax.getIntersection().isEmpty()) {
            for (ClassExpression ex : ax.getIntersection()) {
                if (ex.getClazz() != null && !ex.getClazz().isEmpty()) {
                    dal.upsertCPO(iri, conceptDbid, SUBTYPE, ex.getClazz(), roleGroup, null, null);
                } else if (ex.getPropertyData() != null) {
                    Integer min = ex.getPropertyData().getMin();
                    Integer max = ex.getPropertyData().getMax();
                    if (ex.getPropertyData().getExact() != null)
                        min = max = ex.getPropertyData().getExact();
                    if (ex.getPropertyData().getDataType() != null)
                        dal.upsertCPO(iri, conceptDbid, ex.getPropertyData().getProperty(), ex.getPropertyData().getDataType(), roleGroup, min, max);
                    else
                        saveCPD(iri, conceptDbid, SUBTYPE, ex.getPropertyData(), roleGroup);
                } else if (ex.getPropertyObject() != null) {
                    Integer min = ex.getPropertyObject().getMin();
                    Integer max = ex.getPropertyObject().getMax();
                    if (ex.getPropertyObject().getExact() != null)
                        min = max = ex.getPropertyObject().getExact();
                    if (ex.getPropertyObject().getClazz() != null) {
                        dal.upsertCPO(iri, conceptDbid, ex.getPropertyObject().getProperty(), ex.getPropertyObject().getClazz(), 0, min, max);
                    } else if (ex.getPropertyObject().getUnion() != null) {
                        for (ClassExpression u : ex.getPropertyObject().getUnion()) {
                            dal.upsertCPO(iri, conceptDbid, ex.getPropertyObject().getProperty(), u.getClazz(), 1, min, max);
                        }
                    } else {
                        LOG.error("Unknown complex CPO axiom [" + iri + " - " + ax.getId() + "]");
                    }
                } else {
                    LOG.error("Unknown complex axiom [" + iri + " - " + ax.getId() + "]");
                }
            }
        } else {
            LOG.error("Unknown axiom [" + iri + " - " + ax.getId() + "]");
        }
    }

    private void saveCPD(String iri, int conceptDbid, String property, DPECardinalityRestriction data, int roleGroup) throws SQLException {
        Integer minCard = data.getMin();
        Integer maxCard = data.getMax();
        if (data.getExact() != null)
            minCard = maxCard = data.getExact();

        // If it has a data type, save that in the CPO
        if (data.getDataType() != null) {
            dal.upsertCPO(iri, conceptDbid, property, data.getDataType(), roleGroup, minCard, maxCard);
        }

        // Now handle CPD's
        if (data.getExactValue() != null) {
            dal.upsertCPD(iri, conceptDbid, property, data.getExactValue(), roleGroup, minCard, maxCard);
        } else if (data.getOneOf() != null) {
            for (String v : data.getOneOf()) {
                dal.upsertCPD(iri, conceptDbid, property, v, roleGroup, minCard, maxCard);
            }
        }
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

    private boolean isEmpty(List<?> list) {
        return (list == null || list.size() == 0);
    }
}
