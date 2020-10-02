package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void saveConcepts(List<? extends Concept> concepts, boolean inferred) throws Exception {
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
                dal.upsertConcept(namespace, concept, scheme);
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
        } else {
            LOG.error("Inferred - Unknown");
        }
    }

    // ============================== PRIVATE METHODS ==============================


    // ------------------------------ PRIVATE METHODS - ASSERTED CONCEPTS ------------------------------
    private void saveAssertedClassConceptAxioms(String iri, int conceptDbid, Clazz clazz) throws SQLException, JsonProcessingException {
        if (clazz.getExpression() != null)
            throw new IllegalStateException("Concept axiom expressions not currently supported [" + iri + "]");

        if (clazz.getSubClassOf() != null) {
            for (ClassAxiom ax : clazz.getSubClassOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), toJson(ax), ax.getVersion());
            }
        }

        if (clazz.getEquivalentTo() != null) {
            for (ClassAxiom ax : clazz.getEquivalentTo()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), toJson(ax), ax.getVersion());
            }
        }
    }

    private void saveAssertedObjectPropertyConceptAxioms(String iri, int conceptDbid, ObjectProperty objectProperty) throws SQLException, JsonProcessingException {
        if (objectProperty.getSubObjectPropertyOf() != null) {
            for (PropertyAxiom ax : objectProperty.getSubObjectPropertyOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), toJson(ax), ax.getVersion());
            }
        }
    }

    private void saveAssertedDataPropertyConceptAxioms(String iri, int conceptDbid, DataProperty dataProperty) throws SQLException, JsonProcessingException {
        if (dataProperty.getSubDataPropertyOf() != null) {
            for (PropertyAxiom ax : dataProperty.getSubDataPropertyOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), toJson(ax), ax.getVersion());
            }
        }
    }

    private void saveAssertedAnnotationPropertyConceptAxioms(String iri, int conceptDbid, AnnotationProperty annotationProperty) throws SQLException, JsonProcessingException {
        if (annotationProperty.getSubAnnotationPropertyOf() != null) {
            for (PropertyAxiom ax : annotationProperty.getSubAnnotationPropertyOf()) {
                dal.upsertConceptAxiom(iri, conceptDbid, ax.getId(), toJson(ax), ax.getVersion());
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
    }

    private void saveInferredObjectPropertyProperties(String iri, int conceptDbid, ObjectProperty objectProperty) throws Exception {
        int roleGroup = 0;
        if (objectProperty.getSubObjectPropertyOf() != null) {
            for (PropertyAxiom ax : objectProperty.getSubObjectPropertyOf()) {
                dal.upsertCPO(iri, conceptDbid, SUBTYPE, ax.getProperty(), roleGroup, null, null);
                roleGroup++;
            }
        }
    }

    private void saveInferredDataPropertyProperties(String iri, int conceptDbid, DataProperty dataProperty) throws Exception {
        int roleGroup = 0;
        if (dataProperty.getSubDataPropertyOf() != null) {
            for (PropertyAxiom ax : dataProperty.getSubDataPropertyOf()) {
                dal.upsertCPO(iri, conceptDbid, SUBTYPE, ax.getProperty(), roleGroup, null, null);
                roleGroup++;
            }
        }
    }

    private void saveInferredAnnotationPropertyProperties(String iri, int conceptDbid, AnnotationProperty annotationProperty) throws Exception {
        int roleGroup = 0;
        if (annotationProperty.getSubAnnotationPropertyOf() != null) {
            for (PropertyAxiom ax : annotationProperty.getSubAnnotationPropertyOf()) {
                dal.upsertCPO(iri, conceptDbid, SUBTYPE, ax.getProperty(), roleGroup, null, null);
                roleGroup++;
            }
        }
    }

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
}
