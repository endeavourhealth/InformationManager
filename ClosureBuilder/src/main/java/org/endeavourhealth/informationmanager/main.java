package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.common.config.ConfigManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class main {
    private static final Logger LOG = LoggerFactory.getLogger(main.class);
    private static HashMap<Integer, List<Integer>> parentMap = new HashMap<>(1000000);
    private static HashMap<Integer, List<Closure>> closureMap = new HashMap<>();
    private static String outpath = ".";

    public static void main(String[] argv) throws ConfigManagerException, SQLException, IOException, ClassNotFoundException {
        if (argv.length == 0) {
            LOG.error("Axiom must be supplied: ClosureBuilder <axiom> [<out path>]");
            System.exit(1);
        }

        String axiom = argv[0];

        if (argv.length == 2)
            outpath = argv[1];

        ConfigManager.Initialize("Information-Manager");

        LOG.info("Generating closure data for axiom [" + axiom + "]");

        try (Connection conn = getConnection()) {
            Integer axiomId = getAxiomId(conn, axiom);
            if (axiomId == null) {
                LOG.error("Unknown axiom [" + axiom + "]");
                System.exit(1);
            }
            loadSubtypes(conn, axiomId);
            buildClosure();
            writeClosureData(axiom, axiomId);
        }
    }

    private static Connection getConnection() throws SQLException, ClassNotFoundException, IOException {
        LOG.info("Connecting to database...");
        JsonNode json = ConfigManager.getConfigurationAsJson("database");
        String url = json.get("url").asText();
        String user = json.get("username").asText();
        String pass = json.get("password").asText();
        String driver = json.get("class") == null ? null : json.get("class").asText();

        if (driver != null && !driver.isEmpty())
            Class.forName(driver);

        Properties props = new Properties();

        props.setProperty("user", user);
        props.setProperty("password", pass);

        Connection connection = DriverManager.getConnection(url, props);

        LOG.info("Done.");
        return connection;
    }

    private static Integer getAxiomId(Connection conn, String axiom) throws SQLException {
        String sql = "SELECT id FROM axiom WHERE token = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, axiom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt("id");
                else
                    return null;
            }

        }
    }

    private static void loadSubtypes(Connection conn, Integer axiomId) throws SQLException {
        LOG.info("Loading supertypes...");

        String sql = "SELECT s.concept, s.supertype\n" +
            "FROM subtype s\n" +
            "WHERE s.axiom = ?\n" +
            "ORDER BY s.concept";

        Integer previousConceptId  = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setInt(1, axiomId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Integer> parents = null;
                while (rs.next()) {
                    Integer conceptId = rs.getInt("concept");
                    if (!conceptId.equals(previousConceptId)) {
                        parents = new ArrayList<>();
                        parentMap.put(conceptId, parents);
                    }

                    parents.add(rs.getInt("Supertype"));

                    previousConceptId = conceptId;
                }
            }
        }

        LOG.info("Supertypes loaded for " + parentMap.size() + " concepts");
    }

    private static void buildClosure() {
        LOG.info("Generating closures");
        for (Map.Entry<Integer, List<Integer>> row : parentMap.entrySet()) {
            generateClosure(row.getKey());
        }
    }

    private static List<Closure> generateClosure(Integer id) {
        // Get the parents
        List<Integer> parents = parentMap.get(id);
        List<Closure> closures = new ArrayList<>();

        if (parents != null) {
            for (Integer parent : parents) {
                // Add the parent
                closures.add(new Closure().setParent(parent).setLevel(0));

                // Check do we have its closure?
                List<Closure> parentClosures = closureMap.get(parent);
                if (parentClosures == null) {
                    // No, generate it
                    parentClosures = generateClosure(parent);
                }

                // Add parents closure to this closure
                for (Closure parentClosure : parentClosures) {
                    // Check for existing already

                    if (closures.stream().noneMatch(c -> c.getParent().equals(parentClosure.getParent()))) {
                        closures.add(new Closure()
                            .setParent(parentClosure.getParent())
                            .setLevel(parentClosure.getLevel() + 1)
                        );
                    }
                }
            }
        }

        closureMap.put(id, closures);
        return closures;
    }

    private static void writeClosureData(String axiom, Integer axiomId) throws IOException {
        LOG.info("Saving closures...");
        int c = 0;

        try (FileWriter fw = new FileWriter(outpath + "/" + axiom + "_closure.csv")) {
            for (Map.Entry<Integer, List<Closure>> entry : closureMap.entrySet()) {
                c++;
                if (c % 1000 == 0)
                    System.out.print("\rSaving concept closure " + c + "/" + closureMap.size());

                for(Closure closure: entry.getValue()) {
                    fw.write(closure.getParent() + "\t" + entry.getKey() + "\t" + axiomId + "\t" + closure.getLevel() + "\r\n");
                }
            }
        }
        System.out.println();
        LOG.info("Done.");
    }
}
