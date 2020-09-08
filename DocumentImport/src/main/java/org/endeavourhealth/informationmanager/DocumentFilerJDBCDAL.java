package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.dal.BaseJDBCDAL;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;
import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class DocumentFilerJDBCDAL extends BaseJDBCDAL {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentFilerJDBCDAL.class);

    private ObjectMapper objectMapper;
    private final Map<String, Integer> conceptMap = new HashMap<>();

    // Prepared statements
    private final PreparedStatement getNamespace;
    private final PreparedStatement addNamespace;
    private final PreparedStatement getConceptDbid;
    private final PreparedStatement addDraftConcept;
    private final PreparedStatement upsertConcept;
    private final PreparedStatement upsertAxiom;
    private final PreparedStatement delCpo;
    private final PreparedStatement delCpd;
    private final PreparedStatement upsertCpo;
    private final PreparedStatement upsertCpd;

    public DocumentFilerJDBCDAL() throws SQLException {
        getNamespace = conn.prepareStatement("SELECT dbid FROM namespace WHERE prefix = ?");
        addNamespace = conn.prepareStatement("INSERT INTO namespace (iri, prefix) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        getConceptDbid = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
        addDraftConcept = conn.prepareStatement("INSERT INTO concept (namespace, iri, name, status) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        upsertConcept = conn.prepareStatement("INSERT INTO concept (namespace, id, iri, name, description, code, scheme, status)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "id = ?, iri = ?, name = ?, description = ?, code = ?, scheme = ?, status = ?\n", Statement.RETURN_GENERATED_KEYS);

        upsertAxiom = conn.prepareStatement("INSERT INTO concept_axiom (axiom, concept, definition, version)\n" +
            "VALUES (?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "definition = ?, version = ?", Statement.RETURN_GENERATED_KEYS);

        delCpo = conn.prepareStatement("DELETE cpo FROM concept c JOIN concept_property_object cpo ON cpo.concept = c.dbid WHERE c.iri = ?");
        delCpd = conn.prepareStatement("DELETE cpd FROM concept c JOIN concept_property_data cpd ON cpd.concept = c.dbid WHERE c.iri = ?");
        upsertCpo = conn.prepareStatement("REPLACE INTO concept_property_object (concept, property, object, `group`, minCardinality, maxCardinality)\n" +
            "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        upsertCpd = conn.prepareStatement("REPLACE INTO concept_property_data (concept, property, data, `group`, minCardinality, maxCardinality)\n" +
            "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    }

    // ------------------------------ NAMESPACE ------------------------------
    public Integer getNamespaceId(String prefix) throws SQLException {
        DALHelper.setString(getNamespace,1, prefix);
        try (ResultSet rs = getNamespace.executeQuery()) {
            if (rs.next())
                return rs.getInt("dbid");
            else
                return null;
        }
    }

    public Integer createNamespaceId(String iri, String prefix) throws SQLException {
        DALHelper.setString(addNamespace, 1, iri);
        DALHelper.setString(addNamespace, 2, prefix);
        addNamespace.executeUpdate();
        return DALHelper.getGeneratedKey(addNamespace);
    }

    // ------------------------------ Concept ------------------------------
    public Integer getConceptDbid(String iri) throws SQLException {
        DALHelper.setString(getConceptDbid, 1, iri);
        try (ResultSet rs = getConceptDbid.executeQuery()) {
            if (rs.next())
                return rs.getInt("dbid");
            else
                return null;
        }
    }

    public Integer getOrCreateConceptDbid(String iri) throws SQLException {
        if (iri == null)
            return null;

        Integer dbid = conceptMap.get(iri);
        if (dbid == null) {
            dbid = getConceptDbid(iri);
            if (dbid == null) {
                String prefix = getPrefix(iri);
                int namespace = getNamespaceId(prefix);
                dbid = addDraftConcept(namespace, iri, iri);
            }

            conceptMap.put(iri, dbid);
        }
        return dbid;
    }

    public void upsertConcept(int namespace, Concept c, Integer scheme) throws SQLException {
        int i=1;

        DALHelper.setInt(upsertConcept, i++, namespace);
        DALHelper.setString(upsertConcept, i++, c.getId());
        DALHelper.setString(upsertConcept, i++, c.getIri());
        DALHelper.setString(upsertConcept, i++, c.getName());
        DALHelper.setString(upsertConcept, i++, c.getDescription());
        DALHelper.setString(upsertConcept, i++, c.getCode());
        DALHelper.setInt(upsertConcept, i++, scheme);
        DALHelper.setByte(upsertConcept, i++, c.getStatus() == null ? ConceptStatus.ACTIVE.getValue() : c.getStatus().getValue());

        DALHelper.setString(upsertConcept, i++, c.getId());
        DALHelper.setString(upsertConcept, i++, c.getIri());
        DALHelper.setString(upsertConcept, i++, c.getName());
        DALHelper.setString(upsertConcept, i++, c.getDescription());
        DALHelper.setString(upsertConcept, i++, c.getCode());
        DALHelper.setInt(upsertConcept, i++, scheme);
        DALHelper.setByte(upsertConcept, i++, c.getStatus() == null ? ConceptStatus.ACTIVE.getValue() : c.getStatus().getValue());

        if (upsertConcept.executeUpdate() == 0)
            throw new SQLException("Failed to save concept [" + c.getIri() + "]");
    }

    public int addDraftConcept(int namespace, String iri, String name) throws SQLException {
            DALHelper.setInt(addDraftConcept, 1, namespace);
            DALHelper.setString(addDraftConcept, 2, iri);
            DALHelper.setString(addDraftConcept, 3, name);
            DALHelper.setByte(addDraftConcept, 4, ConceptStatus.DRAFT.getValue());

            if (addDraftConcept.executeUpdate() == 0)
                throw new SQLException("Failed to save draft scheme [" + iri + "]");
            int dbid = DALHelper.getGeneratedKey(addDraftConcept);
            return dbid;
    }

    // ------------------------------ Concept Axiom ------------------------------
    public void upsertConceptAxiom(String iri, int conceptDbid, ClassAxiom ax) throws SQLException, JsonProcessingException {
        String json = toJson(ax);
        DALHelper.setString(upsertAxiom, 1, ax.getId());
        DALHelper.setInt(upsertAxiom, 2, conceptDbid);
        DALHelper.setString(upsertAxiom, 3, json);
        DALHelper.setInt(upsertAxiom, 4, (ax.getVersion() == null) ? 1 : ax.getVersion());
        DALHelper.setString(upsertAxiom, 5, json);
        DALHelper.setInt(upsertAxiom, 6, (ax.getVersion() == null) ? 1 : ax.getVersion());
        if (upsertAxiom.executeUpdate() == 0)
            throw new SQLException("Failed to save concept axiom [" + iri + " - " + ax.getId() + "]");
    }

    // ------------------------------ Concept Property Object/Data ------------------------------
    public void delCPO(String iri) throws SQLException {
        DALHelper.setString(delCpo, 1, iri);
        delCpo.execute();
    }

    public void delCPD(String iri) throws SQLException {
        DALHelper.setString(delCpd, 1, iri);
        delCpd.execute();
    }

    public void upsertCPO(String iri, int conceptDbid, String property, String clazz, int roleGroup, Integer minCard, Integer maxCard) throws SQLException {
        DALHelper.setInt(upsertCpo, 1, conceptDbid);
        DALHelper.setInt(upsertCpo, 2, getOrCreateConceptDbid(property));
        DALHelper.setInt(upsertCpo, 3, getOrCreateConceptDbid(clazz));
        DALHelper.setInt(upsertCpo, 4, roleGroup);
        DALHelper.setInt(upsertCpo, 5, minCard);
        DALHelper.setInt(upsertCpo, 6, maxCard);
        if (upsertCpo.executeUpdate() == 0)
            throw new SQLException("Failed to save cpo subtype axiom [" + iri + " - " + property + " - " + clazz + "]");
    }

    public void upsertCPD(String iri, int conceptDbid, String property, String data, int roleGroup, Integer minCard, Integer maxCard) throws SQLException {
        DALHelper.setInt(upsertCpd, 1, conceptDbid);
        DALHelper.setInt(upsertCpd, 2, getOrCreateConceptDbid(property));
        DALHelper.setString(upsertCpd, 3, data);
        DALHelper.setInt(upsertCpd, 4, roleGroup);
        DALHelper.setInt(upsertCpd, 5, minCard);
        DALHelper.setInt(upsertCpd, 6, maxCard);
        if (upsertCpd.executeUpdate() == 0)
            throw new SQLException("Failed to save cpd axiom [" + iri + " - " + property + " - " + data + "]");
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

    public String getPrefix(String iri) {
        return iri.substring(0, iri.indexOf(":")) + ":";
    }
}
