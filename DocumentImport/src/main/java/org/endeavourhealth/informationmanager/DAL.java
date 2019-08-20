package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;
import org.endeavourhealth.informationmanager.common.models.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

import static java.sql.Types.VARCHAR;

public class DAL {
    private static final Logger LOG = LoggerFactory.getLogger(DAL.class);

    private Connection conn;
    private PreparedStatement document_ins;
    private PreparedStatement document_get;
    private PreparedStatement concept_get;
    private PreparedStatement concept_ins;
    private PreparedStatement codeable_ins;
    private PreparedStatement obj_prop_ins;
    private PreparedStatement val_prop_ins;
    // private PreparedStatement att_prop_ins;

    private HashMap<String, Integer> conceptIds = new HashMap<>();
    private Set<String> coreProperties = new HashSet<>(Arrays.asList("document", "id", "name", "short_name", "description", "status", "scheme", "code"));

    DAL() throws Exception {
        this.conn = connect();
        this.conn.setAutoCommit(false);
        this.document_ins = conn.prepareStatement("INSERT INTO document (path, version) VALUES (?, '1.0.0')", Statement.RETURN_GENERATED_KEYS);
        this.document_get = conn.prepareStatement("SELECT dbid FROM document WHERE path = ?");
        this.concept_get = conn.prepareStatement("SELECT dbid FROM concept WHERE id = ?");
        this.concept_ins = conn.prepareStatement("INSERT INTO concept (document, id, name, short_name, description, status) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        this.codeable_ins = conn.prepareStatement("INSERT INTO concept (document, id, name, short_name, description, status, scheme, code) SELECT ?, ?, ?, ?, ?, ?, dbid, ? FROM concept WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
        this.obj_prop_ins = conn.prepareStatement("INSERT INTO concept_property_object (dbid, property, value) SELECT ?, p.dbid, v.dbid FROM concept p JOIN concept v ON v.id = ? WHERE p.id = ?", Statement.RETURN_GENERATED_KEYS);
        this.val_prop_ins = conn.prepareStatement("INSERT INTO concept_property_data (dbid, property, value) SELECT ?, dbid, ? FROM concept WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
        // this.att_prop_ins = conn.prepareStatement("INSERT INTO concept_property_info (dbid, property, attribute, value) SELECT ?, p.dbid, a.dbid, v.dbid FROM concept p JOIN CONCEPT a ON a.id = ? JOIN concept v ON v.id = ? WHERE p.id = ?", Statement.RETURN_GENERATED_KEYS);
    }

    public int getOrCreateDocumentDbid(String document) throws SQLException {
        document_get.setString(1, document);
        try (ResultSet rs = document_get.executeQuery()) {
            if (rs.next())
                return rs.getInt(1);

            document_ins.setString(1, document);
            if (document_ins.executeUpdate() != 1)
                throw new SQLException("Document insert failed!");

            return DALHelper.getGeneratedKey(document_ins);
        }
    }

    public void insertConcept(int docDbid, JsonNode concept) throws SQLException {
        PreparedStatement stmt = concept.has("scheme") ? codeable_ins : concept_ins;
        stmt.setInt(1, docDbid);
        stmt.setString(2, concept.get("id").textValue());
        if (concept.has("name")) stmt.setString(3, concept.get("name").textValue()); else stmt.setNull(3, VARCHAR);
        if (concept.has("short_name")) stmt.setString(4, concept.get("short_name").textValue()); else stmt.setNull(4, VARCHAR);
        if (concept.has("description")) stmt.setString(5, concept.get("description").textValue()); else stmt.setNull(5, VARCHAR);
        stmt.setShort(6, Status.DRAFT.getValue());

        if (concept.has("scheme")) {
            stmt.setString(7, concept.get("code").textValue());
            stmt.setString(8, concept.get("scheme").textValue());
        }

        if (stmt.executeUpdate() != 1)
            throw new SQLException("Concept insert failed!");

        int conceptDbid = DALHelper.getGeneratedKey(stmt);

        conceptIds.put(concept.get("id").textValue(), conceptDbid);
    }

    public void insertConceptProperties(JsonNode concept) throws SQLException {
        String id = concept.get("id").textValue();

        Iterator<Map.Entry<String, JsonNode>> properties = concept.fields();
        while (properties.hasNext()) {
            Map.Entry<String, JsonNode> prop = properties.next();
            String propName = prop.getKey();

            if (!coreProperties.contains(propName)) {
                JsonNode valueNode = prop.getValue();

                if (valueNode.isValueNode()) {
                    insertConceptPropertyValue(id, propName, valueNode.textValue());
                } else if (valueNode.isObject()) {
                    if (valueNode.has("id"))
                        insertConceptPropertyObject(id, propName, valueNode.get("id").textValue());
/*
                    else
                        insertConceptPropertyInfo(id, propName, valueNode);
*/
                }
            }
        }
    }
    private void insertConceptPropertyValue(String id, String propName, String value) throws SQLException {
        if (value == null)
            throw new SQLException("Value null for [" + id + "." + propName + "]");

        int dbid = conceptIds.get(id);
        val_prop_ins.setInt(1, dbid);
        val_prop_ins.setString(2, value);
        val_prop_ins.setString(3, propName);

        if (val_prop_ins.executeUpdate() != 1)
            throw new SQLException("Concept property value insert failed for [" + id + "." + propName + "='" + value + "']");
    }

    private void insertConceptPropertyObject(String id, String propName, String value) throws SQLException {
        if (value == null)
            throw new SQLException("Value null for [" + id + "." + propName + "]");

        int dbid = conceptIds.get(id);
        obj_prop_ins.setInt(1, dbid);
        obj_prop_ins.setString(2, value);
        obj_prop_ins.setString(3, propName);

        if (obj_prop_ins.executeUpdate() != 1)
            throw new SQLException("Concept property object insert failed for [" + id + "." + propName + "='" + value + "']");
    }

/*
    private void insertConceptPropertyInfo(String id, String propName, JsonNode info) throws SQLException {
        int dbid = conceptIds.get(id);

        Iterator<Map.Entry<String, JsonNode>> atts = info.fields();
        while (atts.hasNext()) {
            Map.Entry<String, JsonNode> next = atts.next();
            String attribute = next.getKey();

            String value = next.getValue().has("id") ? next.getValue().get("id").textValue() : next.getValue().textValue();

            att_prop_ins.setInt(1, dbid);
            att_prop_ins.setString(2, attribute);
            att_prop_ins.setString(3, value);
            att_prop_ins.setString(4, propName);

            if (att_prop_ins.executeUpdate() != 1)
                throw new SQLException("Concept property info insert failed for [" + id + "." + propName + "{" + attribute + "='" + value + "'}]");
        }
    }
*/

    public void commit() throws SQLException {
        conn.commit();
        close();
    }

    public void rollback() throws SQLException {
        conn.rollback();
        close();
    }

    private void close() throws SQLException {
        this.document_ins.close();
        this.document_get.close();
        this.codeable_ins.close();
        this.concept_ins.close();
        this.conn.close();
    }

    private Connection connect() throws Exception {
            JsonNode json = ConfigManager.getConfigurationAsJson("database", "information-manager");
            String url = json.get("url").asText();
            String user = json.get("username").asText();
            String pass = json.get("password").asText();
            String driver = json.get("class") == null ? null : json.get("class").asText();

            if (driver != null && !driver.isEmpty())
                Class.forName(driver);

            Properties props = new Properties();

            props.setProperty("user", user);
            props.setProperty("password", pass);

            Connection connection = DriverManager.getConnection(url, props);    // NOSONAR

            LOG.debug("New DB Connection created");

            return connection;
    }

    public Integer getConceptDbid(String id) throws SQLException {
        Integer dbid = conceptIds.get(id);
        if (dbid == null) {
            concept_get.setString(1, id);
            try (ResultSet rs = concept_get.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
                else
                    throw new IllegalStateException("Unknown concept [" + id + "]");
            }
        }

        return dbid;
    }
}
