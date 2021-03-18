package org.endeavourhealth.informationmanager;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ClosureBuilder {
    private static final HashMap<Integer, List<Integer>> parentMap = new HashMap<>(1000000);
    private static final HashMap<Integer, List<Closure>> closureMap = new HashMap<>(1000000);
    private static String outpath = ".";
    private static boolean version1 = false;

    public static void main(String[] argv) throws SQLException, IOException, ClassNotFoundException {
        if (argv.length > 0)
            outpath = argv[0];

        if (argv.length == 2)
            version1 = Boolean.parseBoolean(argv[1]);

        System.out.println("Generating closure data...");

        try (Connection conn = getConnection()) {
            loadRelationships(conn);
            buildClosure();
            writeClosureData();
        }
    }

    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        Map<String, String> envVars = System.getenv();

        System.out.println("Connecting to database...");
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

        System.out.println("Done.");
        return connection;
    }

    private static void loadRelationships(Connection conn) throws SQLException {
        System.out.println("Loading relationships...");

        String sql = (version1)
        ? "SELECT cpo.dbid, cpo.value\n" +
            "FROM concept s\n" +
            "JOIN concept_property_object cpo ON cpo.property = s.dbid\n" +
            "WHERE s.id = 'SN_116680003'"
        : "SELECT child, parent\n" +
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

        System.out.println("Relationships loaded for " + parentMap.size() + " concepts");
    }

    private static void buildClosure() {
        System.out.println("Generating closures");
        int c = 0;
        for (Map.Entry<Integer, List<Integer>> row : parentMap.entrySet()) {
            c++;
            if (c % 1000 == 0)
                System.out.print("\rGenerating concept closure " + c + "/" + parentMap.entrySet().size());
            generateClosure(row.getKey());
        }
        System.out.println();
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
        System.out.println("Saving closures...");
        int c = 0;
        int t = 0;

        String outFile = version1
            ? outpath + "/closure_v1.csv"
            : outpath + "/closure.csv";

        try (FileWriter fw = new FileWriter(outFile)) {
            for (Map.Entry<Integer, List<Closure>> entry : closureMap.entrySet()) {
                c++;
                if (c % 1000 == 0)
                    System.out.print("\rSaving concept closure " + c + "/" + closureMap.size());

                for(Closure closure: entry.getValue()) {
                    fw.write(entry.getKey() + "\t" + closure.getParent() + "\t" + closure.getLevel() + "\r\n");
                    t++;
                }
            }
        }
        System.out.println();
        System.out.println("Done (written " + t + " rows)");
    }
}
