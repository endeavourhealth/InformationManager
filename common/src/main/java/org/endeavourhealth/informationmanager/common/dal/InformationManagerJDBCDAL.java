package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class InformationManagerJDBCDAL implements InformationManagerDAL {
    private static final Logger LOG = LoggerFactory.getLogger(InformationManagerJDBCDAL.class);

    // ********** Document Import methods **********
    @Override
    public int getOrCreateDocumentDbid(String document) throws SQLException, MalformedURLException {
        Integer dbid = getDocumentDbid(document);

        if (dbid == null) {
            // Ignore protocol and domain if present
            URL url = new URL(document);

            String path = url.getPath();
            int i = path.lastIndexOf("/");
            String version = path.substring(i + 1);
            path = path.substring(1, i);

            Connection conn = ConnectionPool.getInstance().pop();
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO document (path, version) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, path);
                stmt.setString(2, version);
                stmt.execute();
                dbid = DALHelper.getGeneratedKey(stmt);
            } finally {
                ConnectionPool.getInstance().push(conn);
            }
        }
        return dbid;
    }

    @Override
    public Integer getDocumentDbid(String document) throws SQLException, MalformedURLException {
        // Ignore protocol and domain if present
        URL url = new URL(document);

        String path = url.getPath();
        int i = path.lastIndexOf("/");
        String version = path.substring(i + 1);
        path = path.substring(1, i);

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT dbid FROM document WHERE path = ? AND version = ?")) {
            stmt.setString(1, path);
            stmt.setString(2, version);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return rs.getInt("dbid");
            else
                return null;
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    // ********** Manager UI methods **********

    @Override
    public List<Document> getDocuments() throws SQLException {
        List<Document> result = new ArrayList<>();

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement("SELECT dbid, path, version, draft FROM document")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add( new Document()
                    .setDbid(resultSet.getInt("dbid"))
                    .setPath(resultSet.getString("path"))
                    .setVersion(Version.fromString(resultSet.getString("version")))
                    .setDraft(resultSet.getBoolean("draft"))
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
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
                result.setResults(getConceptSummariesFromResultSet(resultSet));
            }
            try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();")) {
                ResultSet rs = statement.executeQuery();
                rs.next();
                result.setCount(rs.getInt(1));
            }
        }finally {
            ConnectionPool.getInstance().push(conn);
        }
        return result;
    }

    @Override
    public SearchResult search(String text, Integer size, Integer page, String relationship, String target) throws Exception {
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
                statement.setString(i++, text);
                statement.setString(i++, text + '%');

                if (relFilter) {
                    statement.setInt(i++, relId);
                    statement.setInt(i++, tgtId);
                }

                if (page != null)
                    statement.setInt(i++, offset);

                statement.setInt(i++, size);

                ResultSet resultSet = statement.executeQuery();
                result.setResults(getConceptSummariesFromResultSet(resultSet));
            }

            try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();")) {
                ResultSet rs = statement.executeQuery();
                rs.next();
                result.setCount(rs.getInt(1));
            }

        } finally {
            ConnectionPool.getInstance().push(conn);
        }

        return result;
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
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next())
                    return new Concept()
                    .setDbid(resultSet.getInt("dbid"))
                    .setDocument(resultSet.getInt("document"))
                    .setData(resultSet.getString("data"))
                    .setStatus(Status.byValue(resultSet.getShort("status")))
                    .setUpdated(resultSet.getDate("updated"))
                    .setRevision(resultSet.getInt("revision"))
                    .setPublished(Version.fromString(resultSet.getString("published")));
            }
        }finally {
            ConnectionPool.getInstance().push(conn);
        }
        return null;
    }

    @Override
    public SearchResult getDocumentPending(int dbid, Integer size, Integer page) throws SQLException {
        SearchResult result = new SearchResult();

        String sql = "SELECT SQL_CALC_FOUND_ROWS *\n" +
            "FROM (\n" +
            "SELECT c.dbid, c.document, c.id, c.name, c.scheme, c.code, c.status, c.updated, c.published\n" +
            "FROM concept c\n" +
            "WHERE document = ?\n" +
            "AND status < 2\n" +
            ") AS u\n";

        if (size != null && page != null) {
            sql += "LIMIT ?, ?";
        }

        Connection conn = ConnectionPool.getInstance().pop();
        try {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, dbid);

                if (size != null && page != null) {
                    result.setPage(page);
                    int offset = (page - 1) * size;         // Calculate offset from page & size
                    statement.setInt(2, offset);
                    statement.setInt(3, size);
                }

                ResultSet resultSet = statement.executeQuery();
                result.setResults(getConceptSummariesFromResultSet(resultSet));
            }

            try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();")) {
                ResultSet rs = statement.executeQuery();
                rs.next();
                result.setCount(rs.getInt(1));
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
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                result = rs.getBytes("data");
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
        return result;
    }

    @Override
    public void publishDocument(int dbid, String level) throws SQLException, IOException {
        // Get concepts to publish
        List<String> concepts = new ArrayList<>();

        LOG.debug("Analysing document...");
        List<Integer> pending = getDocumentPendingIds(dbid);

        LOG.debug("Retrieving " + pending.size() + " concepts...");
        for(Integer conceptDbid: pending) {
            concepts.add(getConceptJSON(conceptDbid));
        }

        LOG.debug("Building document...");
        Document doc = getDocument(dbid);
        if ("major".equals(level.toLowerCase()))
            doc.getVersion().incMajor();
        else if ("minor".equals(level.toLowerCase()))
            doc.getVersion().incMinor();
        else
            doc.getVersion().incBuild();

        // Build new document JSON
         String docJson = "{ \"document\" : \"" + doc.getPath() + "/" + doc.getVersion().toString() + "\",";
        docJson += "\"Concepts\" : [" + String.join(",", concepts) + "] }";


        LOG.debug("Compressing document...");
        byte[] compressedDocJson = compress(docJson.getBytes());

        LOG.debug("Publishing in database...");
        // Update database
        Connection conn = ConnectionPool.getInstance().pop();
        conn.setAutoCommit(false);
        try {
            // Insert new doc
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO document_archive (dbid, version, data) VALUES (?, ?, ?)")) {
                stmt.setInt(1, dbid);
                stmt.setString(2, doc.getVersion().toString());
                stmt.setBytes(3, compressedDocJson);
                stmt.execute();
            }

            // Update doc version
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE document SET version = ?, draft = FALSE WHERE dbid = ?")) {
                stmt.setString(1, doc.getVersion().toString());
                stmt.setInt(2, dbid);
                stmt.execute();
            }

            // Update concept published versions
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept SET published = ?, status = 2 WHERE dbid = ?")) {
                for(Integer conceptDbid: pending) {
                    stmt.setString(1, doc.getVersion().toString());
                    stmt.setInt(2, conceptDbid);
                    stmt.execute();
                }
            }
            conn.commit();
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
        LOG.debug("Document publish finished");
    }

    private List<Integer> getDocumentPendingIds(int documentDbid) throws SQLException {
        List<Integer> result = new ArrayList<>();

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement("SELECT dbid FROM concept WHERE document = ? AND status < 2")) {
            statement.setInt(1, documentDbid);
            ResultSet rs = statement.executeQuery();
            while (rs.next())
                result.add(rs.getInt("dbid"));
        } finally {
            ConnectionPool.getInstance().push(conn);
        }

        return result;
    }

    @Override
    public Document getDocument(int dbid) throws SQLException {
        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT path, version, draft FROM document WHERE dbid = ?")) {
            stmt.setInt(1, dbid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return new Document()
                .setDbid(dbid)
                .setPath(rs.getString("path"))
                .setVersion(Version.fromString(rs.getString("version")))
                .setDraft(rs.getBoolean("draft"));
            else
                return null;
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
//        Connection conn = ConnectionPool.getInstance().pop();
//        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept (document, data, status) VALUES (?, ?, ?)")) {
//            stmt.setInt(1, document);
//            stmt.setString(2, conceptJson);
//            stmt.setByte(3, status.getValue());
//            stmt.execute();
//        } finally {
//            ConnectionPool.getInstance().push(conn);
//        }
    }

    @Override
    public Concept updateConcept(Concept concept) throws SQLException, IOException {
        Connection conn = ConnectionPool.getInstance().pop();
        conn.setAutoCommit(false);
        try {
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept SET document = ?, data = ?, status = ?, revision = revision + 1 WHERE dbid = ?")) {
                stmt.setInt(1, concept.getDocument());
                stmt.setString(2, concept.getData().toString());
                stmt.setShort(3, Status.DRAFT.getValue());
                stmt.setInt(4, concept.getDbid());
                stmt.execute();
            }
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
    public String getConceptJSON(int dbid) throws SQLException {
        String sql = "SELECT data FROM concept WHERE dbid = ?";

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, dbid);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getString("data");
            else
                return null;
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

    @Override
    public String getConceptJSON(String id) throws SQLException {
        String sql = "SELECT data FROM concept WHERE id = ?";

        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getString("data");
            else
                return null;
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
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultSet.getString("name");
            else
                return null;
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
                ResultSet rs = statement.executeQuery();
                if (!rs.next())
                    return id;
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
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getInt("dbid");
            else
                return null;

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
                .setUpdated(resultSet.getDate("updated"))
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
}
