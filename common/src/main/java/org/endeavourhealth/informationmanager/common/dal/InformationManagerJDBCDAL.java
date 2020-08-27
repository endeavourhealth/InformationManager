package org.endeavourhealth.informationmanager.common.dal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.informationmanager.common.dal.hydrators.ConceptHydrator;
import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class InformationManagerJDBCDAL extends BaseJDBCDAL implements InformationManagerDAL {
    private static final Logger LOG = LoggerFactory.getLogger(InformationManagerJDBCDAL.class);

    private ObjectMapper objectMapper;
    private final Map<String, Integer> conceptMap = new HashMap<>();
    private final Set<String> undefinedConcepts = new HashSet<>();
    private final Map<String, Integer> namespaceMap = new HashMap<>();

    public Set<String> getUndefinedConcepts() {
        return undefinedConcepts;
    }

    // ------------------------------ UI ------------------------------

    @Override
    public SearchResult getMRU(Integer size, List<String> supertypes) throws SQLException {
        SearchResult result = new SearchResult();

        if (size == null)
            size = 15;

        String sql = "\tSELECT c.*\n" +
            "\tFROM concept c\n";

        sql += "\tORDER BY c.updated DESC\n" +
            "\tLIMIT ?\n";

        sql = "SELECT c.iri, c.name, c.description, s.iri AS scheme, c.code, c.status\n" +
            "FROM (\n" + sql + ") c\n" +
            "LEFT JOIN concept s ON s.id = c.scheme\n";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            int i = 1;
            statement.setInt(i++, size);

            try (ResultSet resultSet = statement.executeQuery()) {
                result.setResults(ConceptHydrator.createConceptList(resultSet));
            }
        }
        try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            result.setCount(rs.getInt(1));
        }

        return result;
    }


    @Override
    public SearchResult search(String terms, List<String> supertypes, Integer size, Integer page) throws SQLException {
        page = (page == null) ? 0 : page;       // Default page to 1
        size = (size == null) ? 15 : size;      // Default page size to 15
        int offset = page * size;               // Calculate offset from page & size

        SearchResult result = new SearchResult()
            .setPage(page);

        String sql = "SELECT c.iri, c.name, c.description, s.iri AS scheme, c.code, c.status\n" +
            "FROM concept c\n" +
            "LEFT JOIN concept s ON s.id = c.scheme\n" +
            "WHERE c.name LIKE ?\n" +
            "ORDER BY LENGTH(c.name)\n" +
            "LIMIT ?, ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + terms + "%");
            stmt.setInt(2, offset);
            stmt.setInt(3, size);
            try (ResultSet rs = stmt.executeQuery()) {
                result.setResults(ConceptHydrator.createConceptList(rs));
            }
        }

        return result;
    }

    @Override
    public String getAssertedDefinition(String iri) throws SQLException {
        String sql = "SELECT definition FROM concept WHERE iri = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("definition");
                else
                    return null;
            }
        }
    }



    // ------------------------------ NAMESPACE ------------------------------
    @Override
    public int getNamespaceIdWithCreate(String iri, String prefix) throws SQLException {
        return getNamespaceId(iri, prefix, true);
    }

    private Integer getNamespaceId(String prefix) throws SQLException {
        if (!namespaceMap.containsKey(prefix)) {
            String sql = "SELECT id FROM namespace WHERE prefix = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, prefix);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        namespaceMap.put(prefix, rs.getInt("id"));
                }
            }
        }
        return namespaceMap.get(prefix);
    }

    private Integer getNamespaceId(String iri, String prefix, boolean autoCreate) throws SQLException {
        Connection conn = ConnectionPool.getInstance().pop();
        String sql = "SELECT id FROM namespace WHERE iri = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
                else if (autoCreate) {
                    return createNamespaceId(conn, iri, prefix);
                } else
                    return null;
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    private Integer createNamespaceId(Connection conn, String iri, String prefix) throws SQLException {
        String sql = "INSERT INTO namespace (iri, prefix) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, iri);
            stmt.setString(2, prefix);
            stmt.executeUpdate();

            return DALHelper.getGeneratedKey(stmt);
        }
    }

    // ------------------------------ CONCEPTS ------------------------------
    @Override
    public void saveConcepts(List<? extends Concept> concepts) throws Exception {
        Connection conn = ConnectionPool.getInstance().pop();
        String sql = "INSERT INTO concept (namespace, iri, name, description, code, scheme, status, definition)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "name = ?, description = ?, code = ?, scheme = ?, status = ?, definition = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            for (Concept concept: concepts) {
                saveConcept(stmt, concept);
                i++;
                if (i % 1000 == 0)
                    LOG.info("Processed " + i + " of " + concepts.size());
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    private void saveConcept(PreparedStatement stmt, Concept c) throws JsonProcessingException, SQLException {
        String prefix = getPrefix(c.getIri());
        int namespace = getNamespaceId(prefix);

        int i=1;

        DALHelper.setInt(stmt, i++, namespace);
        DALHelper.setString(stmt, i++, c.getIri());
        DALHelper.setString(stmt, i++, c.getName());
        DALHelper.setString(stmt, i++, c.getDescription());
        DALHelper.setString(stmt, i++, c.getCode());
        DALHelper.setInt(stmt, i++, getConceptIdWithCreate(c.getScheme()));
        DALHelper.setByte(stmt, i++, c.getStatus() == null ? ConceptStatus.ACTIVE.getValue() : c.getStatus().getValue());
        DALHelper.setString(stmt, i++, toJson(c));

        DALHelper.setString(stmt, i++, c.getName());
        DALHelper.setString(stmt, i++, c.getDescription());
        DALHelper.setString(stmt, i++, c.getCode());
        DALHelper.setInt(stmt, i++, getConceptIdWithCreate(c.getScheme()));
        DALHelper.setByte(stmt, i++, c.getStatus() == null ? ConceptStatus.ACTIVE.getValue() : c.getStatus().getValue());
        DALHelper.setString(stmt, i++, toJson(c));

        if (stmt.executeUpdate() == 0)
            throw new SQLException("Failed to save concept [" + c.getIri() + "]");

        undefinedConcepts.remove(c.getIri());
    }


    private String getPrefix(String iri) {
        return iri.substring(0, iri.indexOf(":")) + ":";
    }

    private String toJson(Object o) throws JsonProcessingException {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    }

    private Integer getConceptIdWithCreate(String scheme) throws SQLException {
        if (scheme == null || scheme.isEmpty())
            return null;

        Integer id = conceptMap.get(scheme);
        if (id == null) {
            Connection conn = ConnectionPool.getInstance().pop();
            try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM concept WHERE iri = ?")) {
                stmt.setString(1, scheme);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        id = createDraftConcept(scheme);
                    } else {
                        id = rs.getInt("id");
                    }
                    conceptMap.put(scheme, id);
                }
            } finally {
                ConnectionPool.getInstance().push(conn);
            }
        }
        return id;
    }

    private int createDraftConcept(String iri) throws SQLException {
        String prefix = getPrefix(iri);
        String sql = "INSERT INTO concept (namespace, iri, name, status, definition)\n" +
            "VALUES(?, ?, ?, ?, ?)\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            DALHelper.setInt(stmt, 1, getNamespaceId(prefix));
            DALHelper.setString(stmt, 2, iri);
            DALHelper.setString(stmt, 3, iri);
            DALHelper.setByte(stmt, 4, ConceptStatus.DRAFT.getValue());
            DALHelper.setString(stmt, 5, "{}");

            if (stmt.executeUpdate() == 0)
                throw new SQLException("Failed to save draft scheme [" + iri + "]");

            int dbid = DALHelper.getGeneratedKey(stmt);

            undefinedConcepts.add(iri);

            return dbid;
        }
    }

    // ------------------------------ VALUE SETS ------------------------------
    @Override
    public Concept getConcept(String iri) throws SQLException {
        String sql = "SELECT c.id, c.iri, c.status, c.name, c.description, s.iri AS scheme, c.code\n" +
            "FROM concept c\n" +
            "LEFT JOIN concept s ON s.id = c.scheme\n" +
            "WHERE c.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return ConceptHydrator.createConcept(rs);
                else
                    return null;
            }
        }
    }

    public List<RelatedConcept> getDefinition(String iri) throws SQLException {
        String sql = "SELECT o.minCardinality, o.maxCardinality,\n" +
            "p.iri AS r_iri, p.name AS r_name,\n" +
            "v.iri AS c_iri, v.name AS c_name\n" +
            "FROM concept_property_object o\n" +
            "JOIN concept c ON c.id = o.concept AND c.iri = ?\n" +
            "JOIN concept p ON p.id = o.property\n" +
            "JOIN concept v ON v.id = o.object\n" +
            "WHERE NOT EXISTS (\n" +
            "\tSELECT 1 \n" +
            "    FROM concept_tct tct \n" +
            "    JOIN concept tt ON tt.iri IN (':DM_ObjectProperty', ':DM_DataProperty')\n" +
            "    WHERE tct.source = p.id AND tct.target = tt.id\n" +
            ");\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);

            try(ResultSet rs = stmt.executeQuery()) {
                return ConceptHydrator.createRelatedConceptList(rs);
            }
        }
    }

    public List<Property> getProperties(String iri, boolean inherited) throws SQLException {
        String sql = "SELECT p.iri AS p_iri, p.name AS p_name,\n" +
            "d.min_cardinality, d.max_cardinality,\n" +
            "v.iri as v_iri, v.name AS v_name,\n" +
            "-1 as level, c.iri as o_iri, c.name AS o_name\n" +
            "FROM concept c\n" +
            "JOIN concept_data_model d ON d.id = c.id\n" +
            "JOIN concept p ON p.id = d.attribute\n" +
            "JOIN concept v ON v.id = d.value_type\n" +
            "WHERE c.iri = ?\n";

        if (inherited)
            sql += "UNION SELECT p.iri AS p_iri, p.name AS p_name,\n" +
                "d.min_cardinality, d.max_cardinality,\n" +
                "v.iri as v_iri, v.name AS v_name,\n" +
                "tct.level, o.iri as o_iri, o.name AS o_name\n" +
                "FROM concept c\n" +
                "JOIN concept_tct tct ON tct.source = c.id\n" +
                "JOIN concept tp ON tp.id = tct.property AND tp.iri = ':SN_116680003'\n" +
                "JOIN concept o ON o.id = tct.target\n" +
                "JOIN concept_data_model d ON d.id = o.id\n" +
                "JOIN concept p ON p.id = d.attribute\n" +
                "JOIN concept v ON v.id = d.value_type\n" +
                "WHERE c.iri = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);
            if (inherited)
                stmt.setString(2, iri);

            try(ResultSet rs = stmt.executeQuery()) {
                return ConceptHydrator.createPropertyList(rs);
            }
        }
    }

    public PagedResultSet<RelatedConcept> getSources(String iri, List<String> relationships, int limit, int page) throws SQLException {
        String sql = "SELECT SQL_CALC_FOUND_ROWS\n" +
            "o.minCardinality, o.maxCardinality,\n" +
            "p.iri AS r_iri, p.name AS r_name,\n" +
            "c.iri AS c_iri, c.name AS c_name\n" +
            "FROM concept_property_object o\n" +
            "JOIN concept v ON v.id = o.object AND v.iri = ?\n" +
            "JOIN concept p ON p.id = o.property\n" +
            "JOIN concept c ON c.id = o.concept\n";

        if (relationships != null && !relationships.isEmpty())
            sql += "WHERE p.iri IN (" + DALHelper.inListParams(relationships.size()) + ")\n";

        if (limit > 0) {
            sql += "LIMIT ?";
            if (page > 0) {
                sql += ",?";
            }
        }

        PagedResultSet<RelatedConcept> result = new PagedResultSet<RelatedConcept>()
            .setPage(page)
            .setPageSize(limit);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, iri);

            if (relationships != null)
                for (String relationship : relationships)
                    stmt.setString(i++, relationship);

            if (limit > 0) {
                if (page == 0) {
                    stmt.setInt(i++, limit);
                } else {
                    stmt.setInt(i++, (page - 1) * limit);
                    stmt.setInt(i++, limit);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                result.setResult(ConceptHydrator.createRelatedConceptList(rs));
            }
        }

        result.setTotalRecords(DALHelper.getCalculatedRows(conn));

        return result;
    }

    public PagedResultSet<RelatedConcept> getTargets(String iri, List<String> relationships, int limit, int page) throws SQLException {
        String sql = "SELECT SQL_CALC_FOUND_ROWS\n" +
            "o.minCardinality, o.maxCardinality,\n" +
            "p.iri AS r_iri, p.name AS r_name,\n" +
            "v.iri AS c_iri, v.name AS c_name\n" +
            "FROM concept_property_object o\n" +
            "JOIN concept c ON c.id = o.concept AND c.iri = ?\n" +
            "JOIN concept p ON p.id = o.property\n" +
            "JOIN concept v ON v.id = o.object\n";

        if (relationships != null && !relationships.isEmpty())
            sql += "WHERE p.iri IN (" + DALHelper.inListParams(relationships.size()) + ")\n";

        if (limit > 0) {
            sql += "LIMIT ?";
            if (page > 0) {
                sql += ",?";
            }
        }

        PagedResultSet<RelatedConcept> result = new PagedResultSet<RelatedConcept>()
            .setPage(page)
            .setPageSize(limit);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, iri);

            if (relationships != null)
                for (String relationship : relationships)
                    stmt.setString(i++, relationship);

            if (limit > 0) {
                if (page == 0) {
                    stmt.setInt(i++, limit);
                } else {
                    stmt.setInt(i++, (page - 1) * limit);
                    stmt.setInt(i++, limit);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                result.setResult(ConceptHydrator.createRelatedConceptList(rs));
            }
        }

        result.setTotalRecords(DALHelper.getCalculatedRows(conn));

        return result;
    }

    public List<RelatedConcept> getTree(String iri, String root, List<String> relationships) throws SQLException {
        List<RelatedConcept> result = new ArrayList<>();

        String sql = "SELECT null AS minCardinality, null AS maxCardinality,\n" +
            "p.iri AS r_iri, p.name AS r_name,\n" +
            "t.iri AS c_iri, t.name AS c_name\n" +
            "FROM concept c\n" +
            "JOIN concept_tct tct ON tct.source = c.id\n" +
            "JOIN concept p ON p.id = tct.property\n" +
            "JOIN concept t ON t.id = tct.target\n" +
            "WHERE c.iri = ?\n";

        if (relationships != null && !relationships.isEmpty())
            sql += "AND p.iri IN (" + DALHelper.inListParams(relationships.size()) + ")\n";

        sql += "ORDER by tct.level";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, iri);

            if (relationships != null)
                for(String relationship: relationships)
                    stmt.setString(i++, relationship);

            try(ResultSet rs = stmt.executeQuery()) {
                boolean rootFound = false;
                while (rs.next() && !rootFound) {
                    result.add(ConceptHydrator.createRelatedConcept(rs));
                    rootFound = rs.getString("c_iri").equals(root);
                }
            }
        }

        return result;
    }
}
