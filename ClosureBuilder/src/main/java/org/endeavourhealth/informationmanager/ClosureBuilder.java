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

public class ClosureBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(ClosureBuilder.class);
    private static final HashMap<Integer, List<Integer>> parentMap = new HashMap<>(1000000);
    private static final HashMap<Integer, List<Closure>> closureMap = new HashMap<>(1000000);
    private static String outpath = ".";

    public static void main(String[] argv) throws SQLException, IOException, ClassNotFoundException {
        if (argv.length == 1)
            outpath = argv[0];

        LOG.info("Generating closure data...");

        try (Connection conn = getConnection()) {
            loadRelationships(conn);
            buildClosure();
            writeClosureData();
        }
    }

    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        Map<String, String> envVars = System.getenv();

        LOG.info("Connecting to database...");
        String url = envVars.get("CONFIG_JDBC_URL");
        String user = envVars.get("CONFIG_JDBC_USERNAME");
        String pass = envVars.get("CONFIG_JDBC_PASSWORD");
        String driver = envVars.get("CONFIG_JDBC_CLASS");

        if (driver != null && !driver.isEmpty())
            Class.forName(driver);

        Properties props = new Properties();

        props.setProperty("user", user);
        props.setProperty("password", pass);

        Connection connection = DriverManager.getConnection(url, props);

        LOG.info("Done.");
        return connection;
    }

    private static void loadRelationships(Connection conn) throws SQLException {
        LOG.info("Loading relationships...");

//        String sql = "SELECT s.concept, s.object\n" +
//            "FROM concept_hierarchy s\n" +
//            "ORDER BY s.concept";

        String sql = "SELECT child, parent\n" +
            "FROM classification s\n" +
            "ORDER BY child";

        Integer previousConceptId  = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql);) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Integer> parents = null;
                while (rs.next()) {
                    Integer conceptId = rs.getInt(1);
                    if (!conceptId.equals(previousConceptId)) {
                        parents = new ArrayList<>();
                        parentMap.put(conceptId, parents);
                    }

                    parents.add(rs.getInt(2));

                    previousConceptId = conceptId;
                }
            }
        }

        LOG.info("Relationships loaded for " + parentMap.size() + " concepts");
    }

    private static void buildClosure() {
        LOG.info("Generating closures");
        int c = 0;
        for (Map.Entry<Integer, List<Integer>> row : parentMap.entrySet()) {
            c++;
            if (c % 1000 == 0)
                System.out.print("\rGenerating concept closure " + c + "/" + parentMap.entrySet().size());
            generateClosure(row.getKey());
        }
    }

    private static List<Closure> generateClosure(Integer id) {
        // Get the parents
        List<Integer> parents = parentMap.get(id);
        List<Closure> closures = new ArrayList<>();
        closureMap.put(id, closures);

        // Add self
        closures.add(new Closure().setParent(id).setLevel(-1));

        if (parents != null) {
            for (Integer parent : parents) {
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

        return closures;
    }

    private static void writeClosureData() throws IOException {
        LOG.info("Saving closures...");
        int c = 0;

        try (FileWriter fw = new FileWriter(outpath + "/closure.csv")) {
            for (Map.Entry<Integer, List<Closure>> entry : closureMap.entrySet()) {
                c++;
                if (c % 1000 == 0)
                    System.out.print("\rSaving concept closure " + c + "/" + closureMap.size());

                for(Closure closure: entry.getValue()) {
                    fw.write(entry.getKey() + "\t" + closure.getParent() + "\t" + closure.getLevel() + "\r\n");
                }
            }
        }
        System.out.println();
        LOG.info("Done.");
    }
}
