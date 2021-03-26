package org.endeavourhealth.informationmanager.migration;

import org.endeavourhealth.informationmanager.migration.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MigrationEngine {

    private PreparedStatement v1Concepts;
    private PreparedStatement imv1CPO;

    private PreparedStatement v2Populated;
    private PreparedStatement v2InsNamespace;
    private PreparedStatement v2InsModule;
    private PreparedStatement v2InsConcept;

    public MigrationEngine(Connection imv1, Connection imv2) throws SQLException {
        try {
            // Compile SQL
            v1Concepts = imv1.prepareStatement("SELECT c.*, s.id AS schemeId, d.path AS docPath FROM concept c LEFT JOIN concept s ON s.dbid = c.scheme LEFT JOIN document d ON d.dbid = c.document WHERE d.path <> 'InformationModel/dm/DMD' AND (s.id IS NULL OR s.id <> 'DM+D') AND c.use_count > 0 ORDER BY c.dbid");
            imv1CPO = imv1.prepareStatement("SELECT tpl_group.*, p.id AS propertyId FROM concept_property_object tpl_group JOIN concept p ON p.dbid = tpl_group.property WHERE tpl_group.dbid = ? ORDER BY tpl_group.group, tpl_group.property");

            v2Populated = imv2.prepareStatement("SELECT 1 FROM concept LIMIT 1");
            v2InsNamespace = imv2.prepareStatement("INSERT INTO namespace (dbid, prefix, iri) VALUES (?, ?, ?)");
            v2InsModule = imv2.prepareStatement("INSERT INTO module (dbid, iri) VALUES (?, ?)");
            v2InsConcept = imv2.prepareStatement("INSERT INTO concept (dbid, namespace, module, iri, name, description, type, code, scheme, status, weighting, updated) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        } catch (SQLException e) {
            System.err.println("Failed to prepare SQL");
            e.printStackTrace();
            throw e;
        }
    }

    public void execute() throws SQLException {
        CheckSource();
        CheckDest();

        // preSeed();

        List<DBConcept> concepts = importv1Concepts();

        // storeV2Concepts(concepts);
    }


    private void CheckSource() {
        System.out.print("Checking IM v1...");
        System.out.println("Done");
    }
    private void CheckDest() throws SQLException {
        System.out.print("Checking IM v2...");
        try (ResultSet rs = v2Populated.executeQuery()) {
            if (rs.next()) {
                System.err.println("Destination db not empty - it should be to maintain DBID's");
                // throw new IllegalStateException("Destination db not empty - it should be to maintain DBID's");
            }
        }

        System.out.println("Done");
    }

    private void preSeed() throws SQLException {
        // Namespaces
        for (DBNamespace ns : DBNamespace.namespaces)
            insertNamespace(ns.dbid, ns.prefix, ns.iri);

        // Modules
        for (DBModule m : DBModule.modules)
            insertModule(m.dbid, m.iri);
    }

    private void insertNamespace(int dbid, String prefix, String iri) throws SQLException {
        v2InsNamespace.setInt(1, dbid);
        v2InsNamespace.setString(2, prefix);
        v2InsNamespace.setString(3, iri);
        v2InsNamespace.executeUpdate();
    }
    private void insertModule(int dbid, String iri) throws SQLException {
        v2InsModule.setInt(1, dbid);
        v2InsModule.setString(2, iri);
        v2InsModule.executeUpdate();
    }

    private List<DBConcept> importv1Concepts() throws SQLException {
        List<String> ignoreSchemes = Arrays.asList("SNOMED", "READ2", "CTV3", "ICD10", "OPCS4");
        List<DBConcept> concepts = new ArrayList<>();
        int c = 0;
        System.out.println("Fetching concepts...");
        try (ResultSet rs = v1Concepts.executeQuery()) {
            System.out.println("Importing...");
            while (rs.next()) {
                c++;

                String scheme = rs.getString("schemeId");
                String docPath = rs.getString("docPath");

                MapData mapData = MapData.getMap(docPath, scheme);

                String id = rs.getString("id");
                String code = rs.getString("code");

                String iri = String.format(mapData.namespace.prefix+mapData.format, code == null ? id : code);

                DBConcept concept = new DBConcept()
                    .setDbid(rs.getInt("dbid"))
                    .setNamespace(mapData.namespace.dbid)
                    .setModule(mapData.module.dbid)
                    .setIri(iri)
                    .setName(rs.getString("name"))
                    .setDescription(rs.getString("Description"))
                    .setCode(rs.getString("code"))
                    .setScheme(rs.getInt("scheme"))
                    .setStatus(rs.getByte("draft"))
                    .setWeighting(rs.getInt("use_count"))
                    .setUpdated(rs.getTimestamp("updated"));

                concepts.add(concept);

                if (!ignoreSchemes.contains(scheme))
                    importConceptDefinition(concept);

                if (c % 1000 == 0) {
                    System.out.println("\rImported concept " + c + " (" + id + " -> " + iri + ")...");
                }
            }
        }
        System.out.println("\nImported " + c + " concepts");

        return concepts;
    }

    private void importConceptDefinition(DBConcept concept) throws SQLException {
        int group = -1;
        String propertyId = "";
        DBAxiom ax = null;
        imv1CPO.setInt(1, concept.getDbid());
        try (ResultSet rs = imv1CPO.executeQuery()) {
            while (rs.next()) {
                if (!"has_parent".equals(rs.getString("propertyId"))) {
                    if (group != rs.getInt("group") || propertyId != rs.getString("propertyId")) {
                        group = rs.getInt("group");
                        propertyId = rs.getString("propertyId");
                        ax = new DBAxiom();
                        concept.getAxioms().add(ax);

                        switch (propertyId) {
                            case "is_subtype_of":
                            case "SN_116680003":
                                ax.setType(DBAxiom.SUBCLASS);
                                ax.getExpressions().add(
                                    new DBExpression(DBExpression.CLASS, rs.getInt("value"))
                                );
                                break;
                            case "is_equivalent_to":
                                ax.setType(DBAxiom.EQUIVALENT);
                                ax.getExpressions().add(
                                    new DBExpression(DBExpression.CLASS, rs.getInt("value"))
                                );
                                break;
                            default:
                                throw new IllegalStateException("Unhandled property type [" + propertyId + "] on [" + concept.getIri() + "]");
                        }
                    } else {
                        throw new IllegalStateException("Grouping issue!");
                    }
                }
            }
        }
    }

    private void storeV2Concepts(List<DBConcept> concepts) throws SQLException {
        int c = 0;
        for (DBConcept concept: concepts) {
            System.out.print("\rInserting concept " + c++ + "...");
            v2InsConcept.setInt(1, concept.getDbid());
            v2InsConcept.setInt(2, concept.getNamespace());
            v2InsConcept.setInt(3, concept.getModule());
            v2InsConcept.setString(4, concept.getIri());
            v2InsConcept.setString(5, concept.getName());
            v2InsConcept.setString(6, concept.getDescription());
            v2InsConcept.setByte(7, concept.getType());
            v2InsConcept.setString(8, concept.getCode());
            v2InsConcept.setInt(9, concept.getScheme());
            v2InsConcept.setInt(10, concept.getStatus());
            v2InsConcept.setInt(11, concept.getWeighting());
            v2InsConcept.setTimestamp(12, concept.getUpdated());
            v2InsConcept.executeUpdate();

            if (c % 1000 == 0) {
                System.out.println("\rInserting concept " + c + "...");
            }

        }
        System.out.println("\nInserted " + c + " concepts");
    }
}
