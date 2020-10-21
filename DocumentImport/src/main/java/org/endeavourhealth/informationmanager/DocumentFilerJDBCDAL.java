package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
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

    private final Map<String, Integer> conceptMap = new HashMap<>();

    // Prepared statements
    private final PreparedStatement getNamespace;
    private final PreparedStatement addNamespace;
    private final PreparedStatement getOntology;
    private final PreparedStatement addOntology;
    private final PreparedStatement getModule;
    private final PreparedStatement addModule;
    private final PreparedStatement setDocument;
    private final PreparedStatement getConceptDbid;
    private final PreparedStatement getConceptDbidByUuid;
    private final PreparedStatement addDraftConcept;
    private final PreparedStatement upsertConcept;
    private final PreparedStatement upsertAxiom;
    private final PreparedStatement getAxiomDbid;
    private final PreparedStatement upsertCpo;
    private final PreparedStatement upsertCpd;
    private final PreparedStatement addAnonymousConcept;
    private final PreparedStatement getAxiomAnonymous;
    private final PreparedStatement deleteConcept;

    public DocumentFilerJDBCDAL() throws SQLException {
        getNamespace = conn.prepareStatement("SELECT dbid FROM namespace WHERE prefix = ?");
        addNamespace = conn.prepareStatement("INSERT INTO namespace (iri, prefix) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        getOntology = conn.prepareStatement("SELECT dbid FROM ontology WHERE iri = ?");
        addOntology = conn.prepareStatement("INSERT INTO ontology (iri) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        getModule = conn.prepareStatement("SELECT dbid FROM module WHERE iri = ?");
        addModule = conn.prepareStatement("INSERT INTO module (iri) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        setDocument = conn.prepareStatement("REPLACE INTO document (document, module, ontology) VALUES (?, ?, ?)");
        getConceptDbid = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
        getConceptDbidByUuid = conn.prepareStatement("SELECT dbid FROM concept WHERE id = ?");
        addDraftConcept = conn.prepareStatement("INSERT INTO concept (namespace, iri, name, id, status) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        upsertConcept = conn.prepareStatement("INSERT INTO concept (namespace, id, iri, name, description, type, code, scheme, status)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "id = ?, iri = ?, name = ?, description = ?, type = ?, code = ?, scheme = ?, status = ?\n", Statement.RETURN_GENERATED_KEYS);

        upsertAxiom = conn.prepareStatement("INSERT INTO concept_axiom (module, axiom, type, concept, definition, version)\n" +
            "VALUES (?, ?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "module = ?, type = ?, definition = ?, version = ?", Statement.RETURN_GENERATED_KEYS);
        getAxiomDbid = conn.prepareStatement("SELECT dbid FROM concept_axiom WHERE axiom = ?");

        upsertCpo = conn.prepareStatement("REPLACE INTO concept_property_object (concept, property, inverse, object, axiom, `group`, minCardinality, maxCardinality, operator)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        upsertCpd = conn.prepareStatement("REPLACE INTO concept_property_data (concept, property, inverse, data, datatype, axiom, `group`, minCardinality, maxCardinality, operator)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        addAnonymousConcept = conn.prepareStatement("INSERT INTO concept (namespace, id, iri, type) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        getAxiomAnonymous = conn.prepareStatement("SELECT c.dbid\n" +
            "FROM concept_property_object o\n" +
            "JOIN concept c ON c.dbid = o.concept\n" +
            "WHERE o.axiom = ?\n" +
            "AND c.dbid <> ?");

        deleteConcept = conn.prepareStatement("DELETE c, a, cpo_c, cpo_o, cpd\n" +
            "FROM concept c\n" +
            "LEFT JOIN concept_axiom a ON a.concept = c.dbid\n" +
            "LEFT JOIN concept_property_object cpo_c ON cpo_c.concept = c.dbid\n" +
            "LEFT JOIN concept_property_object cpo_o ON cpo_o.object = c.dbid\n" +
            "LEFT JOIN concept_property_data cpd ON cpd.concept = c.dbid\n" +
            "WHERE c.dbid = ?;");
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

    // ------------------------------ ONTOLOGY ------------------------------
    public Integer getOntologyId(String iri) throws SQLException {
        DALHelper.setString(getOntology,1, iri);
        try (ResultSet rs = getOntology.executeQuery()) {
            if (rs.next())
                return rs.getInt("dbid");
            else
                return null;
        }
    }

    public Integer createOntologyId(String iri) throws SQLException {
        DALHelper.setString(addOntology, 1, iri);
        addOntology.executeUpdate();
        return DALHelper.getGeneratedKey(addOntology);
    }

    // ------------------------------ ONTOLOGY ------------------------------
    public void setDocument(UUID documentId, int moduleDbid, int ontologyDbid) throws SQLException {
        DALHelper.setString(setDocument,1, documentId.toString());
        DALHelper.setInt(setDocument,2, moduleDbid);
        DALHelper.setInt(setDocument,3, ontologyDbid);
        if (setDocument.executeUpdate() == 0) {
            throw new SQLException("Unable to record document meta data");
        }
    }

    // ------------------------------ MODULE ------------------------------
    public Integer getModuleId(String iri) throws SQLException {
        DALHelper.setString(getModule,1, iri);
        try (ResultSet rs = getModule.executeQuery()) {
            if (rs.next())
                return rs.getInt("dbid");
            else
                return null;
        }
    }

    public Integer createModuleId(String iri) throws SQLException {
        DALHelper.setString(addModule, 1, iri);
        addModule.executeUpdate();
        return DALHelper.getGeneratedKey(addModule);
    }

    // ------------------------------ Concept ------------------------------
    public Integer getConceptDbid(String iri) throws SQLException {
        if (Strings.isNullOrEmpty(iri))
            return null;

        DALHelper.setString(getConceptDbid, 1, iri);
        try (ResultSet rs = getConceptDbid.executeQuery()) {
            if (rs.next())
                return rs.getInt("dbid");
            else
                return null;
        }
    }

    public Integer getConceptDbidByUuid(String uuid) throws SQLException {
        if (Strings.isNullOrEmpty(uuid))
            return null;

        DALHelper.setString(getConceptDbidByUuid, 1, uuid);
        try (ResultSet rs = getConceptDbidByUuid.executeQuery()) {
            if (rs.next())
                return rs.getInt("dbid");
            else
                return null;
        }
    }

    public Integer getOrCreateConceptDbid(String iri) throws SQLException {
        return getOrCreateConceptDbid(iri, null);
    }

    public Integer getOrCreateConceptDbid(String iri, String uuid) throws SQLException {
        if (iri == null)
            return null;

        // Get from cache
        Integer dbid = conceptMap.get(iri);
        if (dbid != null)
            return dbid;

        // Get from db by iri and cache
        dbid = getConceptDbid(iri);
        if (dbid != null) {
            conceptMap.put(iri, dbid);
            return dbid;
        }

        // Get from db by uuid and cache
        dbid = getConceptDbidByUuid(uuid);
        if (dbid != null) {
            conceptMap.put(iri, dbid);
            return dbid;
        }

        // Create draft and cache
        String prefix = getPrefix(iri);
        int namespace = getNamespaceId(prefix);
        dbid = addDraftConcept(namespace, iri, iri, uuid);

        conceptMap.put(iri, dbid);
        return dbid;
    }

    public void upsertConcept(int namespace, Concept c, ConceptType conceptType, Integer scheme) throws SQLException {
        int i=0;

        // Insert
        DALHelper.setInt(upsertConcept, ++i, namespace);
        DALHelper.setString(upsertConcept, ++i, c.getId());
        DALHelper.setString(upsertConcept, ++i, c.getIri());
        DALHelper.setString(upsertConcept, ++i, c.getName());
        DALHelper.setString(upsertConcept, ++i, c.getDescription());
        DALHelper.setByte(upsertConcept, ++i, conceptType.getValue());
        DALHelper.setString(upsertConcept, ++i, c.getCode());
        DALHelper.setInt(upsertConcept, ++i, scheme);
        DALHelper.setByte(upsertConcept, ++i, c.getStatus() == null ? ConceptStatus.ACTIVE.getValue() : c.getStatus().getValue());

        // Update
        DALHelper.setString(upsertConcept, ++i, c.getId());
        DALHelper.setString(upsertConcept, ++i, c.getIri());
        DALHelper.setString(upsertConcept, ++i, c.getName());
        DALHelper.setString(upsertConcept, ++i, c.getDescription());
        DALHelper.setByte(upsertConcept, ++i, conceptType.getValue());
        DALHelper.setString(upsertConcept, ++i, c.getCode());
        DALHelper.setInt(upsertConcept, ++i, scheme);
        DALHelper.setByte(upsertConcept, ++i, c.getStatus() == null ? ConceptStatus.ACTIVE.getValue() : c.getStatus().getValue());

        try {
            if (upsertConcept.executeUpdate() == 0)
                throw new SQLException("Failed to save concept [" + c.getIri() + "]");
        } catch (Exception e) {
            LOG.error("Failed to upsert concept [" + c.getIri() + "]");
            throw e;
        }
    }

    public int addDraftConcept(int namespace, String iri, String name, String uuid) throws SQLException {
        int i = 0;
        DALHelper.setInt(addDraftConcept, ++i, namespace);
        DALHelper.setString(addDraftConcept, ++i, iri);
        DALHelper.setString(addDraftConcept, ++i, name);
        DALHelper.setString(addDraftConcept, ++i, uuid);
        DALHelper.setByte(addDraftConcept, ++i, ConceptStatus.DRAFT.getValue());

        try {
            if (addDraftConcept.executeUpdate() == 0)
                throw new SQLException("Failed to save draft concept [" + iri + "]");
            int dbid = DALHelper.getGeneratedKey(addDraftConcept);

            return dbid;
        } catch (Exception e) {
            LOG.error("Failed to add draft concept [" + iri + "]");
            throw e;
        }
    }

    public int addAnonymousConcept(UUID uuid, ConceptType type) throws SQLException {
        if (uuid == null)
            throw new SQLException("Anonymous concepts must have a UUID");

        DALHelper.setInt(addAnonymousConcept,1, getNamespaceId(":"));
        DALHelper.setString(addAnonymousConcept,2, uuid.toString());
        DALHelper.setString(addAnonymousConcept,3, ":" + uuid.toString());
        DALHelper.setByte(addAnonymousConcept,4, type.getValue());
        if (addAnonymousConcept.executeUpdate() == 0)
            throw new SQLException("Failed to create anonymous concept [" + uuid + "]");

        return DALHelper.getGeneratedKey(addAnonymousConcept);
    }

    // ------------------------------ Concept Axiom ------------------------------
    public Integer upsertConceptAxiom(String iri, int conceptDbid, int moduleDbid, String id, AxiomType axiomType, String json, Integer version) throws SQLException, JsonProcessingException {
        int i = 0;
        DALHelper.setInt(upsertAxiom, ++i, moduleDbid);
        DALHelper.setString(upsertAxiom, ++i, id);
        DALHelper.setByte(upsertAxiom, ++i, axiomType.getValue());
        DALHelper.setInt(upsertAxiom, ++i, conceptDbid);
        DALHelper.setString(upsertAxiom, ++i, json);
        DALHelper.setInt(upsertAxiom, ++i, version);
        DALHelper.setInt(upsertAxiom, ++i, moduleDbid);
        DALHelper.setByte(upsertAxiom, ++i, axiomType.getValue());
        DALHelper.setString(upsertAxiom, ++i, json);
        DALHelper.setInt(upsertAxiom, ++i, version);
        if (upsertAxiom.executeUpdate() == 0)
            throw new SQLException("Failed to save concept axiom [" + iri + " - " + id + "]");

        return getAxiomDbidByUUID(id);
    }

    public Integer getAxiomDbidByUUID(String id) throws SQLException {
        DALHelper.setString(getAxiomDbid, 1, id);
        try (ResultSet rs = getAxiomDbid.executeQuery()) {
            if (rs.next())
                return DALHelper.getInt(rs, "dbid");
            else
                return null;
        }
    }

    // ------------------------------ Concept Property Object/Data ------------------------------
    public void upsertCPO(String iri, int conceptDbid, String property, String clazz) throws SQLException {
        upsertCPO(iri, conceptDbid, getOrCreateConceptDbid(property), getOrCreateConceptDbid(clazz), null, 0, null, null, null, false);
    }
    public void upsertCPO(String iri, int conceptDbid, String property, String clazz, int axiomDbid) throws SQLException {
        upsertCPO(iri, conceptDbid, getOrCreateConceptDbid(property), getOrCreateConceptDbid(clazz), axiomDbid, 0, null, null, null, false);
    }
    public void upsertInverseCPO(String iri, int conceptDbid, String property, String clazz, int axiomDbid) throws SQLException {
        upsertCPO(iri, conceptDbid, getOrCreateConceptDbid(property), getOrCreateConceptDbid(clazz), axiomDbid, 0, null, null, null, true);
    }
    public void upsertCPO(String iri, int conceptDbid, String property, String clazz, int axiomDbid, Operator op) throws SQLException {
        upsertCPO(iri, conceptDbid, getOrCreateConceptDbid(property), getOrCreateConceptDbid(clazz), axiomDbid, 0, null, null, op, false);
    }
    private void upsertCPO(String iri, int conceptDbid, int propertyDbid, int clazzDbid, Integer axiomDbid, int roleGroup, Integer minCard, Integer maxCard, Operator operator, boolean inverse) throws SQLException {
        int i = 0;
        DALHelper.setInt(upsertCpo, ++i, conceptDbid);
        DALHelper.setInt(upsertCpo, ++i, propertyDbid);
        DALHelper.setBool(upsertCpo, ++i, inverse);
        DALHelper.setInt(upsertCpo, ++i, clazzDbid);
        DALHelper.setInt(upsertCpo, ++i, axiomDbid);
        DALHelper.setInt(upsertCpo, ++i, roleGroup);
        DALHelper.setInt(upsertCpo, ++i, minCard);
        DALHelper.setInt(upsertCpo, ++i, maxCard);
        DALHelper.setString(upsertCpo, ++i, operator == null ? null : operator.name());

        if (upsertCpo.executeUpdate() == 0)
            throw new SQLException("Failed to save cpo subtype axiom [" + iri + " - " + propertyDbid + " - " + clazzDbid + "]");
    }

    public void upsertCPD(String iri, int conceptDbid, String property, String data, String dataType, Operator op) throws SQLException {
        upsertCPD(iri, conceptDbid, getOrCreateConceptDbid(property), data, getOrCreateConceptDbid(dataType), null, 0, null, null, op, false);
    }
    private void upsertCPD(String iri, int conceptDbid, int propertyDbid, String data, int dataType, Integer axiomDbid, int roleGroup, Integer minCard, Integer maxCard, Operator operator, boolean inverse) throws SQLException {
        int i = 0;
        DALHelper.setInt(upsertCpd, ++i, conceptDbid);
        DALHelper.setInt(upsertCpd, ++i, propertyDbid);
        DALHelper.setBool(upsertCpd, ++i, inverse);
        DALHelper.setString(upsertCpd, ++i, data);
        DALHelper.setInt(upsertCpd, ++i, dataType);
        DALHelper.setInt(upsertCpd, ++i, axiomDbid);
        DALHelper.setInt(upsertCpd, ++i, roleGroup);
        DALHelper.setInt(upsertCpd, ++i, minCard);
        DALHelper.setInt(upsertCpd, ++i, maxCard);
        DALHelper.setString(upsertCpd, ++i, operator == null ? null : operator.name());
        if (upsertCpd.executeUpdate() == 0)
            throw new SQLException("Failed to save cpd axiom [" + iri + " - " + propertyDbid + " - " + data + "]");
    }

    public void cleanupAxiomConceptProperties(int conceptDbid, Integer axiomDbid) throws SQLException {
        if (axiomDbid == null)
            return;

        List<Integer> anon = new ArrayList<>();
        // Get list of anonymous concepts for a given axiom
        DALHelper.setInt(getAxiomAnonymous, 1, axiomDbid);
        DALHelper.setInt(getAxiomAnonymous, 2, conceptDbid);
        try (ResultSet rs = getAxiomAnonymous.executeQuery()) {
            while(rs.next())
                anon.add(rs.getInt("dbid"));
        }

        if (anon.isEmpty())
            return;

        // Cleanup each
        for(Integer dbid : anon) {
            DALHelper.setInt(deleteConcept, 1, dbid);
            deleteConcept.execute();
        }
    }

    // ------------------------------ Helper/Util ------------------------------
    public String getPrefix(String iri) {
        return iri.substring(0, iri.indexOf(":")) + ":";
    }
}
