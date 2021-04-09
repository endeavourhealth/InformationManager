package org.endeavourhealth.informationmanager;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ClosureGenerator {
    private static final HashMap<Integer, Map<Integer,List<Integer>>> parentMap = new HashMap<>(1000000);
    private static final HashMap<Integer, Map<Integer, List<Closure>>> closureMap = new HashMap<>(1000000);

    public static void main (String[] args) throws SQLException, IOException, ClassNotFoundException {
        generateClosure(args[0]);
    }
    public static void generateClosure(String outpath) throws SQLException, IOException, ClassNotFoundException {

        List<TTIriRef> relationships= new ArrayList<>();
        relationships.add(IM.IS_A);
        relationships.add(IM.IS_CHILD_OF);
        relationships.add(IM.IS_CONTAINED_IN);
        System.out.println("Generating closure data...");

        try (Connection conn = getConnection()) {
            loadRelationships(conn, relationships);
            buildClosure();
            writeClosureData(outpath);
            importClosure(conn,outpath);
        }
    }

    private static void loadRelationships(Connection conn, List<TTIriRef> relationships) throws SQLException {
        System.out.println("Loading relationships...");
        String sql= "select subject as child,predicate, object as parent\n" +
            "from tpl\n" +
            "JOIN concept p ON p.dbid = tpl.predicate\n" +
            "WHERE p.iri IN (" +
            String.join(",", Collections.nCopies(relationships.size(), "?")) +
            ")\n" +
            "ORDER BY child";
        Integer previousChildId  = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql);) {

            for(int i = 0; i < relationships.size(); i++)
                stmt.setString(i + 1, relationships.get(i).getIri());

            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer,List<Integer>> typedParents=null;
                while (rs.next()) {
                    Integer childId = rs.getInt("child");
                    if (!childId.equals(previousChildId)) {
                        typedParents = new HashMap<>();
                        parentMap.put(childId, typedParents);
                    }
                    Integer typeId= rs.getInt("predicate");
                    List<Integer> parents = typedParents.get(typeId);
                    if (parents == null) {
                        parents = new ArrayList<>();
                        typedParents.put(typeId,parents);
                    }
                    parents.add(rs.getInt("parent"));
                    previousChildId = childId;
                }
            }
        }

        System.out.println("Relationships loaded for " + parentMap.size() + " concepts");
    }

    private static void importClosure(Connection conn,String outpath) throws SQLException {
        System.out.println("Importing closure");
        PreparedStatement dropClosure= conn.prepareStatement("TRUNCATE TABLE tct");
        dropClosure.executeUpdate();
        conn.setAutoCommit(false);
        PreparedStatement buildClosure= conn.prepareStatement("LOAD DATA INFILE ?"
        +" INTO TABLE tct"
        +" FIELDS TERMINATED BY '\t'"
        +" LINES TERMINATED BY '\r\n'"
        +" (ancestor, descendant, level,type)");
        buildClosure.setString(1,outpath+"closure.txt");
        buildClosure.executeUpdate();
        conn.commit();
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


    private static void buildClosure() {
        System.out.println("Generating closures");
        int c = 0;
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> row : parentMap.entrySet()) {
            Integer childId= row.getKey();
            for (Map.Entry<Integer,List<Integer>> typedParent :row.getValue().entrySet()) {
                Integer typeId= typedParent.getKey();
                generateClosure(childId,typeId);
                c++;
                if (c % 1000 == 0)
                    System.out.print("\rGenerating concept closure " + c + "/" + parentMap.entrySet().size());
            }

        }
        System.out.println();
    }



    private static List<Closure> generateClosure(Integer childId,Integer typeId) {
        // Get the parents
        Map<Integer,List<Closure>> typedClosures = new HashMap<>();
        closureMap.put(childId, typedClosures);
        List<Closure> closures= new ArrayList<>();
        typedClosures.put(typeId,closures);

        // Add self
        closures.add(new Closure().setParent(childId)
            .setLevel(-1));
        Map<Integer,List<Integer>> typedParents = parentMap.get(childId);
        if (typedParents!=null){
            List<Integer> parents= typedParents.get(typeId);
            if (parents != null) {
                for (Integer parent : parents) {
                    // Check do we have its closure?
                    List<Closure> parentClosures;
                    Map<Integer,List<Closure>> typedClosure= closureMap.get(parent);
                    if (typedClosure==null){
                        typedClosure= new HashMap<>();
                        parentClosures= generateClosure(parent,typeId);
                    } else {
                        parentClosures = closureMap.get(parent).get(typeId);
                        if (parentClosures == null) {
                            // No, generate it
                            parentClosures = generateClosure(parent, typeId);
                        }
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
        }

        return closures;
    }

    private static void writeClosureData(String outpath) throws IOException {
        System.out.println("Saving closures...");
        int c = 0;
        int t = 0;

        String outFile = outpath + "/closure.txt";

        try (FileWriter fw = new FileWriter(outFile)) {
            for (Map.Entry<Integer, Map<Integer,List<Closure>>> entry : closureMap.entrySet()) {
                c++;
                if (c % 1000 == 0)
                    System.out.print("\rSaving concept closure " + c + "/" + closureMap.size());
                for (Map.Entry<Integer,List<Closure>> typedClosure: entry.getValue().entrySet()) {
                    for (Closure closure : typedClosure.getValue()) {
                        fw.write(closure.getParent() + "\t"
                            + entry.getKey() + "\t"
                            + closure.getLevel() + "\t"
                            + typedClosure.getKey() + "\r\n");
                        t++;
                    }
                }
            }
        }
        System.out.println();
        System.out.println("Done (written " + t + " rows)");
    }
}
