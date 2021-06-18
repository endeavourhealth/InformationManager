package org.endeavourhealth.informationmanager.scratch;

import org.endeavourhealth.informationmanager.scratch.grix.Grix;
import org.endeavourhealth.informationmanager.scratch.grix.Node;
import org.endeavourhealth.informationmanager.scratch.grix.NodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.Properties;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] argv) throws SQLException {
        System.err.println("Mem: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024 * 1024)));

        Grix g = load();

        System.err.println("Mem: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/(1024 * 1024)));

        System.out.println("Running query...");

        Date start = new Date();

/*
        NodeList nodes = g.node("Ibuprofen")
            .whereIs("active ingredient")
            .whereHas("pack")
            .whereHas("pack size")
            .whereIs("pack size")
            .whereIs("pack")
            .whereHas("active ingredient");
*/

        NodeList nodes = g.node("SN_75129005")      // Distal radius
            .whereIs("SN_405813007")                // Procedure site
            .whereHas("SN_260686004")                       // Method
        ;

        Date end = new Date();

        System.out.println("Execution: " + (end.getTime() - start.getTime()) + "ms");

        System.out.println("...results...");
        for (Node n : nodes) {
            System.out.println("Node: [" + n.getId() + "]");
        }

        // System.out.println(g);

    }

    private static Grix load() throws SQLException {
        Grix g = new Grix(6000000, 120);
/*
        g.let("Nurofen").has("active ingredient").of("Ibuprofen");
        // g.let("Ibuprofen").is("active ingredient").of("Nurofen");

        g.let("Nurofen x 28").is("pack").of("Nurofen");
        g.let("Nurofen x 28").has("pack size").of("28");

        g.let("Nurofen x 14").is("pack").of("Nurofen");
        g.let("Nurofen x 14").has("pack size").of("14");
*/
        Properties props = new Properties();

        props.setProperty("user", "root");
        props.setProperty("password", "Taz123");

/*        String sql = "SELECT o.dbid AS source, o.property AS relation, o.value AS target\n" +
            "FROM entity_property_object o\n" +
            "JOIN entity s ON s.dbid = o.dbid\n" +
            "JOIN entity p ON p.dbid = o.property\n" +
            "JOIN entity t ON t.dbid = o.value\n";*/

        String sql = "SELECT s.id AS source, p.id AS relation, t.id AS target\n" +
            "FROM entity_property_object o\n" +
            "JOIN entity s ON s.dbid = o.dbid\n" +
            "JOIN entity p ON p.dbid = o.property\n" +
            "JOIN entity t ON t.dbid = o.value\n";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/im_ceg?useSSL=false", props);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                g.let(rs.getString("source"))
                    .has(rs.getString("relation"))
                    .of(rs.getString("target"));
            }
        }

        return g;
    }
}
