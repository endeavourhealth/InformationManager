package org.endeavourhealth.informationmanager.common.dal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class InformationManagerJDBCDAL implements InformationManagerDAL {
    private static final Logger LOG = LoggerFactory.getLogger(InformationManagerJDBCDAL.class);

    // ********** Document Import methods **********
    @Override
    public int getOrCreateDocumentDbid(String path) throws SQLException, MalformedURLException {
        Integer dbid = getDocumentDbid(path);

        if (dbid == null) {
            Connection conn = ConnectionPool.getInstance().pop();
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO document (path, version) VALUES (?, '1.0.0')", Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, path);
                stmt.execute();
                dbid = DALHelper.getGeneratedKey(stmt);
            } finally {
                ConnectionPool.getInstance().push(conn);
            }
        }
        return dbid;
    }

    @Override
    public Integer getDocumentDbid(String path) throws SQLException, MalformedURLException {
        Connection conn = ConnectionPool.getInstance().pop();
        try {
            return getDocumentDbid(conn, path);
        }finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    private Integer getDocumentDbid(Connection conn, String path) throws SQLException {
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

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement("SELECT dbid, path, version, draft FROM document");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add( new Document()
                    .setDbid(resultSet.getInt("dbid"))
                    .setPath(resultSet.getString("path"))
                    .setVersion(Version.fromString(resultSet.getString("version")))
                    .setDrafts(resultSet.getInt("draft"))
                );
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
        }

        return result;
    }

    @Override
    public SearchResult getMRU() throws Exception {
        SearchResult result = new SearchResult();

        String sql = "SELECT dbid, document, id, name, scheme, code, status, updated, published " +
            "FROM concept " +
            "ORDER BY updated DESC " +
            "LIMIT 15";

        Connection conn = ConnectionPool.getInstance().pop();
        try {
            try (PreparedStatement statement = conn.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                result.setResults(getConceptSummariesFromResultSet(resultSet));
            }
            try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();");
                 ResultSet rs = statement.executeQuery()) {
                rs.next();
                result.setCount(rs.getInt(1));
            }
        }finally {
            ConnectionPool.getInstance().push(conn);
        }
        return result;
    }

    @Override
    public SearchResult search(String terms, Integer size, Integer page, String relationship, String target) throws Exception {
        page = (page == null) ? 1 : page;       // Default page to 1
        size = (size == null) ? 15 : size;      // Default page size to 15
        int offset = (page - 1) * size;         // Calculate offset from page & size

        SearchResult result = new SearchResult()
            .setPage(page);

        Integer relId = (relationship == null) ? null : getConceptDbid(relationship);
        Integer tgtId = (target == null) ? null : getConceptDbid(target);
        boolean relFilter = (relId != null) && (tgtId != null);

        String sql = "SELECT SQL_CALC_FOUND_ROWS *\n" +
            "FROM (\n" +
            "SELECT c.dbid, c.document, c.id, c.name, c.scheme, c.code, c.status, c.updated, c.published, LENGTH(c.name) as len\n" +
            "FROM concept c\n" +
            "WHERE MATCH (name) AGAINST (? IN BOOLEAN MODE)\n" +
            "UNION\n" +
            "SELECT c.dbid, c.document, c.id, c.name, c.scheme, c.code, c.status, c.updated, c.published, LENGTH(c.code) as len\n" +
            "FROM concept c\n" +
            "WHERE code LIKE ?\n" +
            "UNION\n" +
            "SELECT c.dbid, c.document, c.id, c.name, c.scheme, c.code, c.status, c.updated, c.published, LENGTH(c.id) as len\n" +
            "FROM concept c\n" +
            "WHERE id LIKE ?\n" +
            ") AS u\n";

        if (relFilter)
            sql += "JOIN concept_tct t ON t.source = u.dbid\n"
                +"AND t.relationship = ? AND t.target = ?\n";

        sql += "ORDER BY len\n" +
            "LIMIT ?";

        if (page != null)
            sql += ",?";

        Connection conn = ConnectionPool.getInstance().pop();
        try {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                int i = 1;
                statement.setString(i++, toBoolean(terms));
                statement.setString(i++, terms.trim() + '%');
                statement.setString(i++, terms.trim() + '%');

                if (relFilter) {
                    statement.setInt(i++, relId);
                    statement.setInt(i++, tgtId);
                }

                if (page != null)
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

        } finally {
            ConnectionPool.getInstance().push(conn);
        }

        return result;
    }
    private String toBoolean(String terms) {
        List<String> words = new ArrayList<>(Arrays.asList(terms.split("[ |,|'|@|.]")));
        words = words.stream()
            .filter(w -> w.length() > 2)
            .collect(Collectors.toList());

        words.removeAll(Arrays.asList("for", "from", "the"));

        if (words.size() > 0)
            return "+" + String.join(" +", words);
        else
            return null;
    }


    @Override
    public Concept getConcept(String id) throws SQLException, IOException {
        String sql = "SELECT dbid, document, data, status, updated, revision, published " +
            "FROM concept " +
            "WHERE id = ?";

        Connection conn = ConnectionPool.getInstance().pop();
        try {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next())
                        return getConceptFromResultSet(resultSet);
                }
            }
        }finally {
            ConnectionPool.getInstance().push(conn);
        }
        return null;
    }
    public Concept getConcept(String code_scheme, String code) throws SQLException, IOException {
        String sql = "SELECT dbid, document, data, status, updated, revision, published\n" +
            "FROM concept\n" +
            "WHERE scheme = ?\n" +
            "AND code = ?";

        Connection conn = ConnectionPool.getInstance().pop();
        try {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, code_scheme);
                statement.setString(2, code);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return getConceptFromResultSet(resultSet);
                    }
                }
            }
        }finally {
            ConnectionPool.getInstance().push(conn);
        }
        return null;
    }
    private Concept getConceptFromResultSet(ResultSet resultSet) throws IOException, SQLException {
        return new Concept()
            .setDbid(resultSet.getInt("dbid"))
            .setDocument(resultSet.getInt("document"))
            .setData(resultSet.getString("data"))
            .setStatus(Status.byValue(resultSet.getShort("status")))
            .setUpdated(resultSet.getDate("updated"))
            .setRevision(resultSet.getInt("revision"))
            .setPublished(Version.fromString(resultSet.getString("published")));
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

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(new DraftConcept()
                    .setId(resultSet.getString("id"))
                    .setName(resultSet.getString("name"))
                    .setPublished(resultSet.getString("published"))
                    .setUpdated(resultSet.getTimestamp("updated"))
                );
            }

        } finally {
            ConnectionPool.getInstance().push(conn);
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

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, dbid);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(new DraftConcept()
                        .setId(resultSet.getString("id"))
                        .setName(resultSet.getString("name"))
                        .setPublished(resultSet.getString("published"))
                        .setUpdated(resultSet.getTimestamp("updated"))
                    );
                }
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
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

        Connection conn = ConnectionPool.getInstance().pop();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dbid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    result = rs.getBytes("data");
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
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
        Connection conn = ConnectionPool.getInstance().pop();
        conn.setAutoCommit(false);

        String sql = "SELECT t.term, typ.id as type, tgt.id as target\n" +
            "FROM concept_term_map t\n" +
            "JOIN concept typ ON typ.dbid = t.type\n" +
            "JOIN concept tgt ON tgt.dbid = t.target\n" +
            "WHERE t.draft = TRUE\n";

        ObjectMapper om = new ObjectMapper();
        try {

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

            publishDocumentToArchive(doc, om.writeValueAsString(root), conn);

            try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept_term_map SET draft=FALSE, published=? WHERE draft=TRUE")) {
                stmt.setString(1, doc.getVersion().toString());
                stmt.execute();
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }
    private void publishConceptDocument(Document doc) throws SQLException, IOException {
        Connection conn = ConnectionPool.getInstance().pop();

        try {
            LOG.debug("Analysing document...");
            List<Integer> pending = getConceptDocumentPendingIds(conn, doc.getDbid());

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
            conn = ConnectionPool.getInstance().pop();
            conn.setAutoCommit(false);

            publishDocumentToArchive(doc, sb.toString(), conn);

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
            conn.commit();
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
        LOG.debug("Document publish finished");
    }
    private void publishDocumentToArchive(Document doc, String docJson, Connection conn) throws IOException, SQLException {
        // Insert new doc
        LOG.debug("Compressing document...");
        byte[] compressedDocJson = compress(docJson.getBytes());
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
    private List<Integer> getConceptDocumentPendingIds(Connection conn, int documentDbid) throws SQLException {
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
        Connection conn = ConnectionPool.getInstance().pop();
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
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    @Override
    public void updateDocument(int dbid, String documentJson) throws SQLException, IOException {
        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE document SET data = ? WHERE dbid = ?")) {
            stmt.setString(1, documentJson);
            stmt.setInt(2, dbid);
            stmt.execute();
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    @Override
    public void insertConcept(int document, String conceptJson, Status status) throws SQLException, IOException {
        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept (document, data, status) VALUES (?, ?, ?)")) {
            stmt.setInt(1, document);
            stmt.setString(2, conceptJson);
            stmt.setShort(3, status.getValue());
            stmt.execute();
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    @Override
    public Concept updateConcept(Concept concept) throws SQLException, IOException {
        Connection conn = ConnectionPool.getInstance().pop();
        conn.setAutoCommit(false);
        try {
            // Archive current concept
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept_archive (dbid, revision, data) SELECT dbid, revision, data FROM concept WHERE dbid = ?")) {
                stmt.setInt(1, concept.getDbid());
                stmt.execute();
            }
            // Update and inc revision
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept SET document = ?, data = ?, status = ?, revision = revision + 1 WHERE dbid = ?")) {
                stmt.setInt(1, concept.getDocument());
                stmt.setString(2, concept.getData().toString());
                stmt.setShort(3, Status.DRAFT.getValue());
                stmt.setInt(4, concept.getDbid());
                stmt.execute();
            }
            // Mark document as draft
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE document SET draft = TRUE WHERE dbid = ?")) {
                stmt.setInt(1, concept.getDocument());
                stmt.execute();
            }
            conn.commit();
        } finally {
            ConnectionPool.getInstance().push(conn);
        }

        // Update entity
        concept.setStatus(Status.DRAFT);
        concept.setRevision(concept.getRevision() + 1);

        return concept;
    }

    @Override
    public String getConceptJSON(String id) throws SQLException {
        String sql = "SELECT data FROM concept WHERE id = ?";

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next())
                    return resultSet.getString("data");
                else
                    return null;
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    @Override
    public String getConceptName(String id) throws SQLException {
        String sql = "SELECT name FROM concept WHERE id = ?\n";

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next())
                    return resultSet.getString("name");
                else
                    return null;
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    @Override
    public String validateIds(List<String> ids) throws Exception {
        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement("SELECT 1 FROM concept WHERE id = ?")) {

            for (String id : ids) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next())
                        return id;
                }
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
        }

        return null;
    }

    @Override
    public Integer getConceptDbid(String id) throws SQLException {
        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement("SELECT dbid FROM concept WHERE id = ?")) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next())
                    return rs.getInt("dbid");
                else
                    return null;
            }
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    private List<ConceptSummary> getConceptSummariesFromResultSet(ResultSet resultSet) throws SQLException {
        List<ConceptSummary> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(new ConceptSummary()
                .setDbid(resultSet.getInt("dbid"))
                .setId(resultSet.getString("id"))
                .setName(resultSet.getString("name"))
                .setScheme(resultSet.getString("scheme"))
                .setCode(resultSet.getString("code"))
                .setStatus(resultSet.getShort("status"))
                .setUpdated(resultSet.getTimestamp("updated"))
            );
        }

        return result;
    }

    public static byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        return output;
    }
    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        return output;
    }

    public void processDraftDocument(String userId, String userName, String instance, String docPath, String draftJson) throws IOException, SQLException {
        Connection conn = ConnectionPool.getInstance().pop();
        conn.setAutoCommit(false);

        try {
            byte category = -1;
            JsonNode root = ObjectMapperPool.getInstance().readTree(draftJson);
            if (root.has("Concepts")) {
                checkAndCreateUnknownDrafts(conn, docPath, (ArrayNode) root.get("Concepts"));
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
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        }finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    private boolean checkAndCreateUnknownDrafts(Connection conn, String docpath, ArrayNode concepts) throws SQLException, JsonProcessingException {

        boolean created = false;

        Integer docDbid = getDocumentDbid(conn, docpath);
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
