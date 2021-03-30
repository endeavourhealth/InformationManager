package org.endeavourhealth.informationmanager.common.dal;

/*import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;*/

public class IMOWLDAL extends BaseJDBCDAL {
/*
    private Map<String, Namespace> prefixNamespace = new HashMap<>();
    private Map<Namespace, Integer> namespaceId = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    // ------------------------------ SAVE ------------------------------

    public void save(Ontology ontology) throws SQLException, JsonProcessingException {

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        processPrefixes(ontology.getNamespace());

        processAxioms(ontology);
    }

    private void processPrefixes(List<Namespace> namespaces) {
        for(Namespace ns: namespaces) {
            prefixNamespace.put(ns.getPrefix(), ns);
        }
    }

    private void processAxioms(Ontology ontology) throws SQLException, JsonProcessingException {

        String sql = "REPLACE INTO concept\n" +
            "(ontology, iri, type, definition)\n" +
            "VALUES\n" +
            "(?, ?, ?, ?)\n";


        try (PreparedStatement upsDefn = conn.prepareStatement(sql)) {
            for (Clazz c : ontology.getClazz()) {
                upsDefn.setInt(1, getNamespaceIdByPrefix(c.getIri()));
                upsDefn.setString(2, c.getIri());
                upsDefn.setInt(3, ConceptStatus.CLASS.value());
                upsDefn.setString(4, objectMapper.writeValueAsString(c));
                upsDefn.executeUpdate();
            }
            for (ObjectProperty op : ontology.getObjectProperty()) {
                upsDefn.setInt(1, getNamespaceIdByPrefix(op.getIri()));
                upsDefn.setString(2, op.getIri());
                upsDefn.setInt(3, ConceptStatus.OBJECT_PROPERTY.value());
                upsDefn.setString(4, objectMapper.writeValueAsString(op));
                upsDefn.executeUpdate();
            }
            for (DataProperty dp : ontology.getDataProperty()) {
                upsDefn.setInt(1, getNamespaceIdByPrefix(dp.getIri()));
                upsDefn.setString(2, dp.getIri());
                upsDefn.setInt(3, ConceptStatus.DATA_PROPERTY.value());
                upsDefn.setString(4, objectMapper.writeValueAsString(dp));
                upsDefn.executeUpdate();
            }
            for (DataType dt : ontology.getDataType()) {
                upsDefn.setInt(1, getNamespaceIdByPrefix(dt.getIri()));
                upsDefn.setString(2, dt.getIri());
                upsDefn.setInt(3, ConceptStatus.DATA_TYPE.value());
                upsDefn.setString(4, objectMapper.writeValueAsString(dt));
                upsDefn.executeUpdate();
            }
            for (AnnotationProperty ap : ontology.getAnnotationProperty()) {
                upsDefn.setInt(1, getNamespaceIdByPrefix(ap.getIri()));
                upsDefn.setString(2, ap.getIri());
                upsDefn.setInt(3, ConceptStatus.ANNOTATION_PROPERTY.value());
                upsDefn.setString(4, objectMapper.writeValueAsString(ap));
                upsDefn.executeUpdate();
            }
        }
    }

    private int getNamespaceIdByPrefix(String prefixIri) throws SQLException {
        String prefix = prefixIri.substring(0, prefixIri.indexOf(":") + 1);
        Namespace ns = prefixNamespace.get(prefix);

        if (ns == null)
            throw new IllegalStateException("Unknown namespace prefix: [" + prefix + "]");

        Integer id = namespaceId.get(ns);
        if (id == null) {
            id = getNamespaceIdFromDB(ns.getIri());

            if (id == null)
                id = addNamespaceToDB(ns);

            namespaceId.put(ns, id);
        }

        return id;
    }

    private Integer getNamespaceIdFromDB(String iri) throws SQLException {
        String sql = "SELECT id FROM ontology WHERE iri=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt("id");
                else
                    return null;
            }
        }
    }

    private int addNamespaceToDB(Namespace ns) throws SQLException {
        String sql = "INSERT INTO ontology (iri, prefix, version) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, ns.getIri());
            stmt.setString(2, ns.getPrefix());
            stmt.setString(3, ns.getVersion());
            stmt.executeUpdate();
            return DALHelper.getGeneratedKey(stmt);
        }
    }

    // ------------------------------ LOAD ------------------------------

    public Ontology load(String iri) throws SQLException, IOException {
        Ontology ontology = new Ontology();
        add(iri, ontology);
        return ontology;
    }

    public void add(String iri, Ontology ontology) throws SQLException, IOException {
        if (ontology == null)
            throw new IllegalStateException("no ontology to add to");

        int nsId;

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ontology WHERE iri = ?")) {
            stmt.setString(1, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    throw new IllegalStateException("Unknown ontology iri: [" + iri + "]");
                else {
                    nsId = rs.getInt("id");

                    ontology.addNamespace(
                        new Namespace()
                            .setIri(iri)
                            .setPrefix(rs.getString("prefix"))
                            .setVersion(rs.getString("version"))
                    );

                    ontology.setDocumentInfo(
                        new DocumentInfo()
                            .setDocumentIri(iri)    // TODO : Different to namespace IRI
                    );
                }
            }
        }


        String sql = "SELECT c.type, c.definition\n" +
            "FROM concept c\n" +
            "WHERE c.ontology = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nsId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    addConcept(
                        ontology,
                        rs.getInt("type"),
                        rs.getString("definition")
                        );
                }
            }
        }
    }

    private void addConcept(Ontology ontology, int type, String definition) throws IOException {
        if (type == ConceptStatus.CLASS.value()) {
            Clazz c = objectMapper.readValue(definition, Clazz.class);
            ontology.addClazz(c);
        } else if (type == ConceptStatus.OBJECT_PROPERTY.value()) {
            ObjectProperty op = objectMapper.readValue(definition, ObjectProperty.class);
            ontology.addObjectProperty(op);
        } else if (type == ConceptStatus.DATA_PROPERTY.value()) {
            DataProperty dp = objectMapper.readValue(definition, DataProperty.class);
            ontology.addDataProperty(dp);
        } else if (type == ConceptStatus.DATA_TYPE.value()) {
            DataType dt = objectMapper.readValue(definition, DataType.class);
            ontology.addDataType(dt);
        } else if (type == ConceptStatus.ANNOTATION_PROPERTY.value()) {
            AnnotationProperty ap = objectMapper.readValue(definition, AnnotationProperty.class);
            ontology.addAnnotationProperty(ap);
        } else {
            System.err.println("Unknown concept definition type: [" + type + "]");
        }
    }
*/
}
