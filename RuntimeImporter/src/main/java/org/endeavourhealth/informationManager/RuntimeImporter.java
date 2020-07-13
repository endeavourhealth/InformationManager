package org.endeavourhealth.informationManager;

import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.informationmanager.common.dal.ConnectionPool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RuntimeImporter {
    private Connection conn;

    public void execute() throws Exception {
        ConfigManager.Initialize("information-manager");
        conn = ConnectionPool.getInstance().pop();

        try {
            System.out.println("Importing...");
            loadConceptPropertyObject("../datafiles/cpo/IMCore_ConceptPropertyObject.txt");
            loadConceptPropertyObject("../datafiles/cpo/IMENC_ConceptPropertyObject.txt");
            System.out.println("Cleaning up...");
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
        System.out.println("Finished.");
    }

    private void loadConceptPropertyObject(String filename) throws IOException, SQLException {
        System.out.println("Importing concept property objects...");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // header
            String line = br.readLine();
            if (line.split("\t").length != 7)
                throw new IndexOutOfBoundsException("ConceptPropertyObject file should contain 7 fields");

            int i = 1;
            String sql = "REPLACE INTO concept_property_object (concept, `group`, property, object, minCardinality, maxCardinality, operator)\n" +
                "SELECT c.id, ?, p.id, v.id, ?, ?, ?\n" +
                "FROM concept c\n" +
                "JOIN concept p\n" +
                "JOIN concept v\n" +
                "WHERE c.iri = ?\n" +
                "AND p.iri = ?\n" +
                "AND v.iri = ?\n";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split("\t");
                    stmt.setString(1, fields[1]);
                    stmt.setString(2, fields[4].isEmpty() ? null : fields[4]);
                    stmt.setString(3, fields[5].isEmpty() ? null : fields[5]);
                    stmt.setString(4, fields[6]);
                    stmt.setString(5, fields[0]);
                    stmt.setString(6, fields[2]);
                    stmt.setString(7, fields[3]);
                    if (stmt.executeUpdate() == 0)
                        System.err.println("Error upserting concept_property_object row " + (i+1) + " - [" + line + "]");

                    i++;
                }
            }
            System.out.println("Done (" + i + ")");
        }
    }
}
