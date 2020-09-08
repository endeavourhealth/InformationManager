package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class DocumentFilerLogic {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentFilerLogic.class);
    private static final String SUBTYPE = "sn:116680003";

    private DocumentFilerJDBCDAL dal;

    private final Set<String> undefinedConcepts = new HashSet<>();
    private final Map<String, Integer> namespaceMap = new HashMap<>();

    public DocumentFilerLogic() throws SQLException {
        dal = new DocumentFilerJDBCDAL();
    }

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


    public void saveConcepts(List<? extends Concept> concepts, boolean inferred) throws Exception {
        int i = 0;
        for (Concept concept : concepts) {
            String prefix = dal.getPrefix(concept.getIri());
            int namespace = getNamespaceId(prefix);
            Integer scheme = dal.getOrCreateConceptDbid(concept.getScheme());

            dal.upsertConcept(namespace, concept, scheme);
            undefinedConcepts.remove(concept.getIri());

            int dbid = dal.getConceptDbid(concept.getIri());

            if (concept instanceof Clazz) {
                if (inferred)
                    saveConceptProperties(concept.getIri(), dbid, (Clazz) concept);
                else
                    saveConceptAxioms(concept.getIri(), dbid, (Clazz) concept);
            }

            i++;
            if (i % 1000 == 0)
                LOG.info("Processed " + i + " of " + concepts.size());
        }
    }

    // ------------------------------ PRIVATE METHODS ------------------------------
    private void saveConceptAxioms(String iri, int conceptDbid, Clazz clazz) throws SQLException, JsonProcessingException {
        if (clazz.getExpression() != null)
            throw new IllegalStateException("Concept axiom expressions not currently supported [" + iri + "]");

            if (clazz.getSubClassOf() != null) {
                for (ClassAxiom ax : clazz.getSubClassOf()) {
                    dal.upsertConceptAxiom(iri, conceptDbid, ax);
                }
            }

            if (clazz.getEquivalentTo() != null) {
                for (ClassAxiom ax : clazz.getEquivalentTo()) {
                    dal.upsertConceptAxiom(iri, conceptDbid, ax);
                }
            }
    }

    private void saveConceptProperties(String iri, int conceptDbid, Clazz clazz) throws SQLException, JsonProcessingException {
        dal.delCPO(iri);
        dal.delCPD(iri);

        if (clazz.getSubClassOf() == null || clazz.getSubClassOf().size() == 0)
            return;

        int roleGroup = 0;
        for (ClassAxiom ax : clazz.getSubClassOf()) {
            saveConceptProperty(iri, conceptDbid, ax, roleGroup);
            roleGroup++;
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
                    if (ex.getPropertyData().getDataType() != null)
                        dal.upsertCPO(iri, conceptDbid, ex.getPropertyData().getProperty(), ex.getPropertyData().getDataType(), roleGroup, null, null);
                    saveSaveCPD(iri, conceptDbid, SUBTYPE, ex.getPropertyData(), roleGroup);
                } else if (ex.getPropertyObject() != null) {
                    if (ex.getPropertyObject().getClazz() != null) {
                        dal.upsertCPO(iri, conceptDbid, ex.getPropertyObject().getProperty(), ex.getPropertyObject().getClazz(), 0, null, null);
                    } else if (ex.getPropertyObject().getUnion() != null) {
                        for (ClassExpression u : ex.getPropertyObject().getUnion()) {
                            dal.upsertCPO(iri, conceptDbid, ex.getPropertyObject().getProperty(), u.getClazz(), 1, null, null);
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

    private void saveSaveCPD(String iri, int conceptDbid, String property, DPECardinalityRestriction data, int roleGroup) throws SQLException {
        // If it has a data type, save that in the CPO
        Integer minCard = data.getMin();
        Integer maxCard = data.getMax();
        if (data.getExact() != null)
            minCard = maxCard = data.getExact();

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
}
