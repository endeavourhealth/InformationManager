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
            // loadOntologies();
            // loadConcepts("./IMCore_Concepts.txt");
            // loadConcepts("./SnomedConcepts.txt");
            loadConceptPropertyObject("../datafiles/cpo/IMCore_ConceptPropertyObject.txt");
            loadConceptPropertyObject("../datafiles/cpo/IMENC_ConceptPropertyObject.txt");
            loadConceptDataModel("../datafiles/cdm/DMCORE_DataModel.txt");
            System.out.println("Cleaning up...");
        } finally {
            ConnectionPool.getInstance().push(conn);
        }
        System.out.println("Finished.");
    }

    private void loadOntologies() throws IOException, SQLException {
        System.out.println("Importing ontologies...");
        try (BufferedReader br = new BufferedReader(new FileReader("./Ontology.txt"))) {
            // header
            String line = br.readLine();
            if (line.split("\t").length != 2)
                throw new IndexOutOfBoundsException("Ontology file should contain 2 fields");

            int i = 1;

            try (PreparedStatement stmt = conn.prepareStatement("REPLACE INTO ontology (prefix, iri) VALUES (?, ?)")) {
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split("\t");
                    stmt.setString(1, fields[0]);
                    stmt.setString(2, fields[1]);
                    if (stmt.executeUpdate() == 0)
                        System.err.println("Error upserting ontology row " + (i+1) + " - [" + line + "]");
                    i++;
                }
            }
            System.out.println("Done (" + i + ")");
        }
    }

    private void loadConcepts(String fileName) throws IOException, SQLException {
        System.out.println("Importing concepts...");
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // header
            String line = br.readLine();
            if (line.split("\t").length != 6)
                throw new IndexOutOfBoundsException("Concept file [" + fileName + "] should contain 6 fields");

            int i = 1;

            String sql = "REPLACE INTO concept (iri, name, description, code, scheme, ontology)\n" +
                "SELECT ?, ?, ?, ?, s.id, o.id\n" +
                "FROM ontology o\n" +
                "LEFT JOIN concept s ON s.iri = ?\n" +
                "WHERE o.prefix = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split("\t");
                    stmt.setString(1, fields[0]);
                    stmt.setString(2, fields[1]);
                    // status = fields[2]
                    stmt.setString(3, (fields.length > 3) ? fields[3] : null);
                    stmt.setString(4, (fields.length > 4) ? fields[4] : null);
                    stmt.setString(5, (fields.length > 5) ? fields[5] : null);
                    stmt.setString(6, getPrefix(fields[0]));
                    if (stmt.executeUpdate() == 0)
                        System.err.println("Error upserting concept row " + (i+1) + " - [" + line + "]");

                    i++;
                }
            }
            System.out.println("Done (" + i + ")");
        }
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

    private void loadConceptDataModel(String filename) throws IOException, SQLException {
        System.out.println("Importing concept data models...");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // header
            String line = br.readLine();
            if (line.split("\t").length != 8)
                throw new IndexOutOfBoundsException("ConceptDataModel file should contain 8 fields");

            int i = 1;

            String sql = "REPLACE INTO concept_data_model (id, type, attribute, value_type, min_cardinality, max_cardinality, inverse) \n" +
                "SELECT c.id, ?, a.id, v.id, ?, ?, i.id\n" +
                "FROM concept c\n" +
                "JOIN concept a ON a.iri = ?\n" +
                "JOIN concept v ON v.iri = ?\n" +
                "LEFT JOIN concept i ON i.iri = ?\n" +
                "WHERE c.iri = ?\n";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split("\t");
                    stmt.setString(1, fields[2].substring(1,1));
                    stmt.setString(2, fields[5].isEmpty() ? null : fields[5]);
                    stmt.setString(3, fields[6].isEmpty() ? null : fields[6]);
                    stmt.setString(4, fields[3]);
                    stmt.setString(5, fields[4]);
                    stmt.setString(6, fields.length < 8 ? null : fields[7]);
                    stmt.setString(7, fields[1]);
                    if (stmt.executeUpdate() == 0)
                        System.err.println("Error upserting concept_data_model row " + (i+1) + " - [" + line + "]");

                    i++;
                }
            }
            System.out.println("Done (" + i + ")");
        }
    }

    private String getPrefix(String iri) {
        int i = iri.indexOf(':');
        if (i > -1)
            return iri.substring(0, i+1);
        else
            return ":";
    }
}
