package org.endeavourhealth.informationmanager.common.dal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.ZipUtils;
import org.endeavourhealth.informationmanager.common.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.sql.Types.INTEGER;
import static java.sql.Types.VARCHAR;

public class InformationManagerJDBCDAL extends BaseDAL implements InformationManagerDAL {
    private static final Logger LOG = LoggerFactory.getLogger(InformationManagerJDBCDAL.class);

    // ********** Document Import methods **********
    @Override
    public int getOrCreateDocumentDbid(String path) throws SQLException {
        Integer dbid = getDocumentDbid(path);

        if (dbid == null) {
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO document (path, version) VALUES (?, '1.0.0')", Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, path);
                stmt.execute();
                dbid = DALHelper.getGeneratedKey(stmt);
            }
        }
        return dbid;
    }

    @Override
    public Integer getDocumentDbid(String path) throws SQLException {
        String sql = "SELECT dbid\n" +
            "FROM document\n" +
            "WHERE path = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, path);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt("dbid");
                else
                    return null;
            }
        }
    }

    // ********** Manager UI methods **********

    @Override
    public List<Document> getDocuments() throws SQLException {
        List<Document> result = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("SELECT dbid, path, version, draft FROM document");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(DocumentHydrator.create(resultSet));
            }
        }

        return result;
    }

    @Override
    public SearchResult getMRU() throws Exception {
        SearchResult result = new SearchResult();

        String sql = "SELECT c.document, c.id, c.name, c.scheme, c.code, st.id as status_id, c.updated, c.published, s.id AS scheme_id\n" +
            "FROM concept c\n" +
            "LEFT JOIN concept st ON st.dbid = c.status\n" +
            "LEFT JOIN concept s ON s.dbid = c.scheme\n" +
            "ORDER BY updated DESC\n" +
            "LIMIT 15";

        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            result.setResults(getConceptSummariesFromResultSet(resultSet));
        }
        try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            result.setCount(rs.getInt(1));
        }

        return result;
    }

    @Override
    public SearchResult search(String terms, Integer size, Integer page, List<Integer> documents, String relationship, String target) throws Exception {
        page = (page == null) ? 1 : page;       // Default page to 1
        size = (size == null) ? 15 : size;      // Default page size to 15
        int offset = (page - 1) * size;         // Calculate offset from page & size

        SearchResult result = new SearchResult()
            .setPage(page);

        String sql = "SELECT SQL_CALC_FOUND_ROWS u.*, st.id AS status_id, s.id AS scheme_id\n" +
            "FROM (\n" +
            "SELECT c.dbid, c.document, c.id, c.name, c.scheme, c.code, c.status, c.updated, c.published, 3 AS priority, LENGTH(c.name) as len\n" +
            "FROM concept c\n" +
            "WHERE MATCH (name) AGAINST (? IN BOOLEAN MODE)\n" +
            "UNION\n" +
            "SELECT c.dbid, c.document, c.id, c.name, c.scheme, c.code, c.status, c.updated, c.published, 2 AS priority, LENGTH(c.code) as len\n" +
            "FROM concept c\n" +
            "WHERE code LIKE ?\n" +
            "UNION\n" +
            "SELECT c.dbid, c.document, c.id, c.name, c.scheme, c.code, c.status, c.updated, c.published, 1 AS priority, LENGTH(c.id) as len\n" +
            "FROM concept c\n" +
            "WHERE id LIKE ?\n" +
            ") AS u\n" +
            "LEFT JOIN concept st ON st.dbid = u.status\n" +
            "LEFT JOIN concept s ON s.dbid = u.scheme\n";

        if (relationship != null && target != null)
            sql += "JOIN concept_property o ON o.dbid = u.dbid\n" +
                "JOIN concept p ON p.dbid = o.property AND p.id = ?\n" +
                "JOIN concept v ON v.dbid = o.concept AND v.id = ?\n";

        if (documents != null && documents.size() > 0) {
            sql += "WHERE u.document IN (" + DALHelper.inListParams(documents.size()) + ")\n";
        }

        sql += "ORDER BY priority ASC, len ASC\n" +
            "LIMIT ?,?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                int i = 1;
                statement.setString(i++, toBoolean(terms));
                statement.setString(i++, terms.trim() + '%');
                statement.setString(i++, terms.trim() + '%');

                if (relationship != null && target != null) {
                    statement.setString(i++, relationship);
                    statement.setString(i++, target);
                }

                if (documents != null && documents.size() > 0) {
                    for (Integer docDbid : documents)
                        statement.setInt(i++, docDbid);
                }

                statement.setInt(i++, offset);
                statement.setInt(i++, size);

                try (ResultSet resultSet = statement.executeQuery()) {
                    result.setResults(getConceptSummariesFromResultSet(resultSet));
                }
            }

            try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();");
                 ResultSet rs = statement.executeQuery()) {
                rs.next();
                result.setCount(rs.getInt(1));
            }

        return result;
    }
    private String toBoolean(String terms) {
        List<String> matchList = new ArrayList<>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(terms);
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        }

        matchList.removeAll(Arrays.asList("for", "from", "the"));

        StringBuilder sb = new StringBuilder();
        for (String match: matchList) {
            if (sb.length() > 0)
                sb.append(" ");

            if (match.startsWith("-"))
                sb.append(match);
            else if (match.startsWith("+"))
                sb.append(match);
            else
                sb.append("+").append(match);
        }

        return sb.toString();
    }


    @Override
    public Concept getConcept(String id) throws SQLException {
        String sql = "SELECT c.*, st.id as status_id, s.id as scheme_id\n" +
            "FROM concept c\n" +
            "LEFT JOIN concept st ON st.dbid = c.status\n" +
            "LEFT JOIN concept s ON s.dbid = c.scheme\n" +
            "WHERE c.id = ?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Concept concept = ConceptHydrator.create(resultSet);

                        concept.setRange(getConceptRange(id));
                        concept.setProperties(getConceptProperties(id));
                        concept.setDomain(getConceptDomain(id));

                        return concept;
                    }
                }
            }
        return null;
    }
    public Concept getConcept(String code_scheme, String code) throws SQLException {
        String sql = "SELECT c.*, s.id AS scheme_id FROM concept c JOIN concept s ON s.dbid = c.scheme WHERE s.id = ? AND c.code = ?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, code_scheme);
                statement.setString(2, code);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Concept concept = ConceptHydrator.create(resultSet);

                        concept.setRange(getConceptRange(concept.getId()));
                        concept.setProperties(getConceptProperties(concept.getId()));
                        concept.setDomain(getConceptDomain(concept.getId()));

                        return concept;
                    }
                }
            }
        return null;
    }

    private List<ConceptProperty> getConceptProperties(String id) throws SQLException {
        String sql = "SELECT p.id AS property, cp.value, v.id as concept, null AS inherits\n" +
            "FROM concept c\n" +
            "JOIN concept_property cp ON cp.dbid = c.dbid\n" +
            "JOIN concept p ON p.dbid = cp.property\n" +
            "LEFT JOIN concept v ON v.dbid = cp.concept\n" +
            "WHERE c.id = ?\n";
        List<ConceptProperty> result = new ArrayList<>();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        result.add(ConceptHydrator.createProperty(rs));
                    }
                    return result;
                }
            }
    }
    private List<ConceptDomain> getConceptDomain(String id) throws SQLException {
        String sql = "SELECT p.id AS property, r.range, d.minimum, d.maximum, IF(i.id=c.id, null, i.id) AS inherits\n" +
            "FROM concept c\n" +
            "JOIN concept_tct t ON t.source = c.dbid \n" +
            "JOIN concept tp ON tp.dbid = t.property AND tp.id = 'SN_116680003'\n" +
            "JOIN concept i ON i.dbid = t.target\n" +
            "JOIN concept_domain d ON d.dbid = t.target\n" +
            "JOIN concept p ON p.dbid = d.property\n" +
            "LEFT JOIN concept_range r ON r.dbid = d.property\n" +
            "WHERE c.id = ?";
        List<ConceptDomain> result = new ArrayList<>();

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        result.add(ConceptHydrator.createDomain(rs));
                    }
                    return result;
                }
            }
    }
    public String getConceptRange(String id) throws SQLException {
        String sql = "SELECT r.range\n" +
            "FROM concept c\n" +
            "JOIN concept_range r ON r.dbid = c.dbid\n" +
            "WHERE c.id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        return rs.getString("range");
                    else
                        return null;
                }
            }
    }

    @Override
    public List<DraftConcept> getDocumentPending(int dbid, Integer size, Integer page) throws SQLException {
        if (dbid == 0)
            return getDraftTermMaps();
        else
            return getDocumentDraftConcepts(dbid);
    }
    private List<DraftConcept> getDraftTermMaps() throws SQLException {
        List<DraftConcept> result = new ArrayList<>();

        String sql = "SELECT c.id, t.term as name, t.published, t.updated\n" +
            "FROM concept_term_map t\n" +
            "JOIN concept c ON c.dbid = t.target\n" +
            "WHERE t.draft = TRUE\n" +
            "LIMIT 15\n";

        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(ConceptHydrator.createDraft(resultSet));
            }
        }

        return result;
    }
    private List<DraftConcept> getDocumentDraftConcepts(int dbid) throws SQLException {
        List<DraftConcept> result = new ArrayList<>();

        String sql = "SELECT c.id, c.name, c.published, c.updated\n" +
            "FROM concept c\n" +
            "WHERE document = ?\n" +
            "AND status < 2\n" +
            "LIMIT 15\n";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, dbid);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(ConceptHydrator.createDraft(resultSet));
                }
            }
        }

        return result;
    }

    @Override
    public byte[] getDocumentLatestPublished(Integer dbid) throws Exception {
        byte[] result = null;

        String sql = "SELECT data\n" +
            "FROM document d\n" +
            "JOIN document_archive a ON a.dbid = d.dbid AND a.version = d.version\n" +
            "WHERE d.dbid = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dbid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    result = rs.getBytes("data");
            }
        }
        return result;
    }

    @Override
    public List<IdNamePair> getSchemes() throws Exception {
        String sql = "SELECT c.id, c.name\n" +
            "FROM concept_property o\n" +
            "JOIN concept p ON p.dbid = o.property AND p.id = 'is_subtype_of'\n" +
            "JOIN concept s ON s.dbid = o.concept AND s.id = 'CodeScheme'\n" +
            "JOIN CONCEPT c ON c.dbid = o.dbid";

        List<IdNamePair> result = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(new IdNamePair()
                    .setId(rs.getString("id"))
                   .setName(rs.getString("name"))
                );
            }
        }

        return result;
    }

    @Override
    public void publishDocument(int dbid, String level) throws SQLException, IOException {
        LOG.debug("Publishing document...");
        Document doc = getDocument(dbid);
        if ("major".equals(level.toLowerCase()))
            doc.getVersion().incMajor();
        else if ("minor".equals(level.toLowerCase()))
            doc.getVersion().incMinor();
        else
            doc.getVersion().incBuild();

        if (dbid == 0)
            publishTermMaps(doc);
        else
            publishConceptDocument(doc);
    }
    private void publishTermMaps(Document doc) throws SQLException, IOException {
        String sql = "SELECT t.term, typ.id as type, tgt.id as target\n" +
            "FROM concept_term_map t\n" +
            "JOIN concept typ ON typ.dbid = t.type\n" +
            "JOIN concept tgt ON tgt.dbid = t.target\n" +
            "WHERE t.draft = TRUE\n";

        ObjectMapper om = new ObjectMapper();

        ObjectNode root = om.createObjectNode();
        root.put("document", doc.getPath() + "/" + doc.getVersion().toString());
        ArrayNode maps = root.putArray("TermMaps");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ObjectNode map = om.createObjectNode();
                map.put("term", rs.getString("term"));
                map.put("type", rs.getString("type"));
                map.put("target", rs.getString("target"));
                maps.add(map);
            }
        }

        publishDocumentToArchive(doc, om.writeValueAsString(root));

        try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept_term_map SET draft=FALSE, published=? WHERE draft=TRUE")) {
            stmt.setString(1, doc.getVersion().toString());
            stmt.execute();
        }
    }
    private void publishConceptDocument(Document doc) throws SQLException, IOException {
        LOG.debug("Analysing document...");
        List<Integer> pending = getConceptDocumentPendingIds(doc.getDbid());

        int s = pending.size();
        LOG.debug("Retrieving " + s + " concepts...");

        int i = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"document\" : \"").append(doc.getPath()).append("/").append(doc.getVersion().toString()).append("\",").append("\"Concepts\" : [");
        try (PreparedStatement statement = conn.prepareStatement("SELECT data FROM concept WHERE dbid = ?")) {
            for (Integer conceptDbid : pending) {
                if (i % 1000 == 0) {
                    LOG.debug("Checking concept " + i + "/" + s);
                }
                statement.setInt(1, conceptDbid);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        if (i > 0)
                            sb.append(",");
                        sb.append(resultSet.getString("data"));
                    }
                }
                i++;
            }
        }
        // Build new document JSON
        sb.append("] }");

        // Update database
        publishDocumentToArchive(doc, sb.toString());

        // Update concept published versions
        LOG.debug("Marking concepts published [" + doc.getVersion().toString() + "]...");
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept SET published = ?, status = 2 WHERE dbid = ?")) {
            i = 0;
            for (Integer conceptDbid : pending) {
                if (i % 1000 == 0) {
                    LOG.debug("Updating concept " + i + "/" + s);
                }
                stmt.setString(1, doc.getVersion().toString());
                stmt.setInt(2, conceptDbid);
                stmt.execute();
                i++;
            }
        }
        LOG.debug("Document publish finished");
    }
    private void publishDocumentToArchive(Document doc, String docJson) throws IOException, SQLException {
        // Insert new doc
        LOG.debug("Compressing document...");
        byte[] compressedDocJson = ZipUtils.compress(docJson.getBytes());
        LOG.debug("Compressed to " + compressedDocJson.length + " bytes");
        LOG.debug("Publishing in database [" + doc.getVersion().toString() + "]...");
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO document_archive (dbid, version, data) VALUES (?, ?, ?)")) {
            stmt.setInt(1, doc.getDbid());
            stmt.setString(2, doc.getVersion().toString());
            stmt.setBytes(3, compressedDocJson);
            stmt.execute();
        }

        // Update doc version
        LOG.debug("Updating document version  [" + doc.getVersion().toString() + "]...");
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE document SET version = ?, draft = FALSE WHERE dbid = ?")) {
            stmt.setString(1, doc.getVersion().toString());
            stmt.setInt(2, doc.getDbid());
            stmt.execute();
        }
    }
    private List<Integer> getConceptDocumentPendingIds(int documentDbid) throws SQLException {
        List<Integer> result = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("SELECT dbid FROM concept WHERE document = ? AND status < 2")) {
            statement.setInt(1, documentDbid);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next())
                    result.add(rs.getInt("dbid"));
            }
        }

        return result;
    }

    @Override
    public Document getDocument(int dbid) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT path, version, draft FROM document WHERE dbid = ?")) {
            stmt.setInt(1, dbid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return new Document()
                        .setDbid(dbid)
                        .setPath(rs.getString("path"))
                        .setVersion(Version.fromString(rs.getString("version")))
                        .setDrafts(rs.getInt("draft"));
                else
                    return null;
            }
        }
    }

    @Override
    public void updateDocument(int dbid, String documentJson) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE document SET data = ? WHERE dbid = ?")) {
            stmt.setString(1, documentJson);
            stmt.setInt(2, dbid);
            stmt.execute();
        }
    }

    @Override
    public void createConcept(int document, String id, String name) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept (document, id, name) VALUES (?, ?, ?)")) {
            stmt.setInt(1, document);
            stmt.setString(2, id);
            stmt.setString(3, name);
            stmt.execute();
        }
    }

    @Override
    public Concept updateConcept(Concept newConcept) throws SQLException, IOException {
        boolean changed;
        String id = newConcept.getId();
        Integer dbid = getConceptDbid(id);
        Concept oldConcept = getConcept(id);

        // Check properties
        changed = checkAndUpdateRange(dbid, newConcept.getRange(), oldConcept.getRange());
        changed = changed || checkAndUpdateProperties(dbid, newConcept.getProperties(), oldConcept.getProperties());
        changed = changed || checkAndUpdateDomain(dbid, newConcept.getDomain(), oldConcept.getDomain());

        if (changed || !ObjectMapperPool.getInstance().writeValueAsString(newConcept).equals(ObjectMapperPool.getInstance().writeValueAsString(oldConcept))) {
            // Update and inc revision
            newConcept.setRevision(newConcept.getRevision() + 1);
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept SET document = ?, id = ?, name = ?, description = ?, scheme = ?, code = ?, revision = ?, status = ? WHERE dbid = ?")) {
                int i = 1;
                stmt.setInt(i++, newConcept.getDocument());
                stmt.setString(i++, id);
                stmt.setString(i++, newConcept.getName());
                stmt.setString(i++, newConcept.getDescription());
                if (newConcept.getScheme() != null) {
                    stmt.setInt(i++, getConceptDbid(newConcept.getScheme()));
                    stmt.setString(i++, newConcept.getCode());
                } else {
                    stmt.setNull(i++, INTEGER);
                    stmt.setNull(i++, VARCHAR);
                }
                stmt.setInt(i++, newConcept.getRevision());
                stmt.setInt(i++, getConceptDbid(newConcept.getStatus()));
                stmt.setInt(i++, dbid);
                int rows = stmt.executeUpdate();
                if (rows != 1)
                    throw new IllegalStateException("Unable to update concept " + id);
            }
            // Archive previous concept
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept_archive (dbid, revision, data) VALUES (?, ?, ?)")) {
                stmt.setInt(1, dbid);
                stmt.setInt(2, newConcept.getRevision());
                stmt.setString(3, ObjectMapperPool.getInstance().writeValueAsString(oldConcept));
                int rows = stmt.executeUpdate();
                if (rows != 1)
                    throw new IllegalStateException("Unable to create archive entry " + id);
            }
            // Mark document as draft
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE document SET draft = TRUE WHERE dbid = ?")) {
                stmt.setInt(1, newConcept.getDocument());
                int rows = stmt.executeUpdate();
                if (rows != 1)
                    throw new IllegalStateException("Unable to update document " + id);
            }
        }

        return newConcept;
    }

    public boolean checkAndUpdateRange(Integer dbid, String newRange, String oldRange) throws SQLException {
        if ( (newRange != null && !newRange.equals(oldRange))
            || (oldRange != null && !oldRange.equals(newRange))
        ) {
            // Delete the old properties
            try (PreparedStatement stmt = conn.prepareStatement("REPLACE INTO concept_range (dbid, `range`) VALUES (?, ?)")) {
                stmt.setInt(1, dbid);
                stmt.setString(2, newRange);
                int rows = stmt.executeUpdate();
                if (rows == 0)  // Replace can be 1 (insert) or 2 (update) rows
                    throw new IllegalStateException("Failed to update range");
            }
            return true;
        } else
            return false;
    }
    public boolean checkAndUpdateProperties(Integer dbid, List<ConceptProperty> newProps, List<ConceptProperty> oldProps) throws JsonProcessingException, SQLException {
        if (!ObjectMapperPool.getInstance().writeValueAsString(newProps).equals(ObjectMapperPool.getInstance().writeValueAsString(oldProps))) {
            // Delete the old properties
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM concept_property WHERE dbid = ?")) {
                stmt.setInt(1, dbid);
                stmt.execute();
            }

            // Insert new ones
            try (PreparedStatement propInsert = conn.prepareStatement("INSERT INTO concept_property (dbid, property, value, concept) SELECT ?, p.dbid, ?, v.dbid FROM concept p LEFT JOIN concept v ON v.id = ? WHERE p.id = ?")) {
                for (ConceptProperty conceptProperty: newProps) {
                    if (conceptProperty.getInherits() == null) {
                        propInsert.setInt(1, dbid);
                        propInsert.setString(2, conceptProperty.getValue());
                        propInsert.setString(3, conceptProperty.getConcept());
                        propInsert.setString(4, conceptProperty.getProperty());
                        int rows = propInsert.executeUpdate();
                        if (rows != 1)
                            throw new IllegalStateException("Failed to insert property");
                    }
                }
            }

            return true;
        } else
            return false;
    }
    public boolean checkAndUpdateDomain(Integer dbid, List<ConceptDomain> newDomain, List<ConceptDomain> oldDomain) throws JsonProcessingException, SQLException {
        if (!ObjectMapperPool.getInstance().writeValueAsString(newDomain).equals(ObjectMapperPool.getInstance().writeValueAsString(oldDomain))) {
            // Delete the old domain
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM concept_domain WHERE dbid = ?")) {
                stmt.setInt(1, dbid);
                stmt.execute();
            }

            // Insert new ones
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept_domain (dbid, property, minimum, maximum) SELECT ?, dbid, ?, ? FROM concept WHERE id = ?")) {
                for (ConceptDomain dom: newDomain) {
                    if (dom.getInherits() == null) {
                        stmt.setInt(1, dbid);
                        stmt.setInt(2, dom.getMinimum());
                        stmt.setInt(3, dom.getMaximum());
                        stmt.setString(4, dom.getProperty());
                        int rows = stmt.executeUpdate();
                        if (rows != 1)
                            throw new IllegalStateException("Failed to insert domain");
                    }
                }
            }

            return true;
        } else
            return false;
    }

    @Override
    public String getConceptJSON(String id) throws SQLException {
        String sql = "SELECT data FROM concept WHERE id = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next())
                    return resultSet.getString("data");
                else
                    return null;
            }
        }
    }

    @Override
    public String getConceptName(String id) throws SQLException {
        String sql = "SELECT name FROM concept WHERE id = ?\n";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next())
                    return resultSet.getString("name");
                else
                    return null;
            }
        }
    }

    @Override
    public String validateIds(List<String> ids) throws Exception {
        try (PreparedStatement statement = conn.prepareStatement("SELECT 1 FROM concept WHERE id = ?")) {

            for (String id : ids) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next())
                        return id;
                }
            }
        }

        return null;
    }

    @Override
    public Integer getConceptDbid(String id) throws SQLException {
        if (id == null)
            return null;

        try (PreparedStatement statement = conn.prepareStatement("SELECT dbid FROM concept WHERE id = ?")) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next())
                    return rs.getInt("dbid");
                else
                    return null;
            }
        }
    }

    private List<ConceptSummary> getConceptSummariesFromResultSet(ResultSet resultSet) throws SQLException {
        List<ConceptSummary> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(ConceptHydrator.createSummary(resultSet));
        }

        return result;
    }

    public void processDraftDocument(String userId, String userName, String instance, String docPath, String draftJson) throws IOException, SQLException {
        byte category = -1;
        JsonNode root = ObjectMapperPool.getInstance().readTree(draftJson);
        if (root.has("Concepts")) {
            checkAndCreateUnknownDrafts(docPath, (ArrayNode) root.get("Concepts"));
            category = 0;
        } else if (root.has("TermMaps")) {
            category = 1;
        }

        String sql = "INSERT INTO workflow_task (category, user_id, user_name, subject, data)\n" +
            "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setByte(1, category);
            stmt.setString(2, userId);
            stmt.setString(3, userName);
            stmt.setString(4, "Document [" + docPath + "] drafts from [" + instance + "]");
            stmt.setString(5, draftJson);
            stmt.execute();
        }
    }

    private boolean checkAndCreateUnknownDrafts(String docpath, ArrayNode concepts) throws SQLException, JsonProcessingException {

        boolean created = false;

        Integer docDbid = getDocumentDbid(docpath);
        if (docDbid == null)
            throw new IllegalArgumentException("Unknown document [" + docpath + "]");

        Iterator<JsonNode> iterator = concepts.iterator();

        try (PreparedStatement exists = conn.prepareStatement("SELECT dbid FROM concept WHERE id = ?");
             PreparedStatement schemeCode = conn.prepareStatement("SELECT dbid FROM concept WHERE scheme = ? AND code = ?");
            PreparedStatement insert = conn.prepareStatement("INSERT INTO concept (document, data) VALUES (?, ?)")){
            while (iterator.hasNext()) {
                ObjectNode concept = (ObjectNode) iterator.next();

                // Check if it exists
                String id = concept.get("id").textValue();
                exists.setString(1,id);
                try (ResultSet rs = exists.executeQuery()) {

                    // Concept ID doesnt exist so we need to create
                    if (!rs.next()) {
                        // If scheme/code exists (under another concept) then remove from this one
                        if (concept.has("code_scheme") && concept.has("code")) {
                            schemeCode.setString(1, concept.get("code_scheme").get("id").textValue());
                            schemeCode.setString(2, concept.get("code").textValue());
                            try (ResultSet rs2 = schemeCode.executeQuery()) {
                                if (rs2.next()) {
                                    concept.remove("code_scheme");
                                    concept.remove("code");
                                }
                            }
                        }

                        // Insert into DB
                        insert.setInt(1, docDbid);
                        insert.setString(2, ObjectMapperPool.getInstance().writeValueAsString(concept));
                        insert.execute();
                        created = true;
                    }
                }
            }
        }

        // If we created any draft concepts, update the document accordingly
        if (created) {
            try (PreparedStatement draftDocument = conn.prepareStatement("UPDATE document SET draft = TRUE WHERE dbid = ?")) {
                draftDocument.setInt(1, docDbid);
                draftDocument.execute();
            }
        }

        return created;
    }
}
