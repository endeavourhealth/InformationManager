package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.dal.hydrators.ConceptHydrator;
import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class InformationManagerJDBCDAL extends BaseJDBCDAL implements InformationManagerDAL {
    private static final Logger LOG = LoggerFactory.getLogger(InformationManagerJDBCDAL.class);
    private Map<String, Integer> conceptIdCache = new HashMap<>();

    @Override
    public SearchResult getMRU(Integer size, List<String> supertypes) throws SQLException {
        SearchResult result = new SearchResult();

        if (size == null)
            size = 15;

        String sql = "\tSELECT c.*\n" +
            "\tFROM concept c\n";

        sql += "\tORDER BY c.updated DESC\n" +
            "\tLIMIT ?\n";

        sql = "SELECT c.id, c.iri, s.name AS status, c.name, c.description, c.code\n" +
            "FROM (\n" + sql + ") c\n" +
            "JOIN concept s ON s.id = c.status\n";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            int i = 1;
            statement.setInt(i++, size);

            try (ResultSet resultSet = statement.executeQuery()) {
                result.setResults(ConceptHydrator.createConceptList(resultSet));
            }
        }
        try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            result.setCount(rs.getInt(1));
        }

        return result;
    }

    @Override
    public List<Concept> getStatuses() throws SQLException {
        String sql = "SELECT c.*\n" +
            "FROM concept p\n" +
            "JOIN subtype s ON s.supertype = p.id\n" +
            "JOIN axiom a ON a.id = s.axiom AND a.token in ('SubClassOf', 'SubPropertyOf')\n" +
            "JOIN concept c ON c.id = s.concept\n" +
            "WHERE p.iri = 'cm:ActiveInactive'";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            return ConceptHydrator.createConceptList(rs);
        }
    }

    @Override
    public Concept getConcept(int id) throws SQLException {
        String sql = "SELECT c.id, c.iri, s.iri AS status, c.name, c.description, c.code\n" +
            "FROM concept c\n" +
            "JOIN concept s ON s.id = c.status\n" +
            "WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return ConceptHydrator.createConcept(rs);
                else
                    return null;
            }
        }
    }

    @Override
    public Concept getConcept(String iri) throws SQLException {
        String sql = "SELECT c.id, c.iri, s.iri AS status, c.name, c.description, c.code\n" +
            "FROM concept c\n" +
            "JOIN concept s ON s.id = c.status\n" +
            "WHERE c.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, iri);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return ConceptHydrator.createConcept(rs);
                else
                    return null;
            }
        }
    }


    @Override
    public SearchResult search(String terms, List<String> supertypes, Integer size, Integer page, List<String> models, List<String> statuses) throws SQLException {
        page = (page == null) ? 0 : page;       // Default page to 1
        size = (size == null) ? 15 : size;      // Default page size to 15
        int offset = page * size;               // Calculate offset from page & size
        List<String> words = Arrays.asList(terms
            .replaceAll("[(),/\\[\\].\\-\"+]", " ")
            .split(" "));

        // Strip words < 3 characters
        words = words
            .stream()
            .filter(w -> w.length() >= 3)
            .collect(Collectors.toList());

        if (words.size() == 0)
            return new SearchResult();

        SearchResult result = new SearchResult()
            .setPage(page);

        String select = "SELECT c.id, c.iri, c.name, c.description, c.code, c.status";
        String from = "FROM concept c\n";

        if (statuses != null && statuses.size() > 0)
            from += "JOIN concept s ON s.id = c.status AND s.iri IN (" + DALHelper.inListParams(statuses.size()) + ")\n";

        String useOrder = "";
        String priOrder = "";

        for (int i = 0; i < words.size(); i++) {
            select += ", ABS(cw" + i + ".position - " + i + ") AS wp" + i;
            from += "JOIN word w" + i + " ON w" + i + ".word LIKE ?\n" +
                "JOIN concept_word cw" + i + " ON cw" + i + ".word = w" + i + ".dbid AND cw" + i + ".concept = c.id\n";

            if (i > 0) {
                priOrder += " + ";
                useOrder += " + ";
            }
            priOrder += "wp" + i;
            useOrder += "w" + i + ".useCount";
        }
        if (supertypes != null && !supertypes.isEmpty())

            from += "JOIN subtype_closure tct ON tct.descendant = c.id\n" +
                "JOIN axiom r ON r.id = tct.axiom AND r.token in ('SubClassOf', 'SubPropertyOf')\n" +
                "JOIN concept t ON t.id = tct.ancestor AND t.iri IN (" + DALHelper.inListParams(supertypes.size()) + ")\n";

        String sql = "SELECT SQL_CALC_FOUND_ROWS u.id, u.iri, u.name, u.description, u.code, st.name AS status\n" +
            "FROM (\n" +
            select + "\n" +
            from +
            "ORDER BY " + useOrder + " DESC, " + priOrder + ", LENGTH(c.name)\n" +
            ") AS u\n" +
            "LEFT JOIN concept st ON st.id = u.status\n";

        List<String> conditions = new ArrayList<>();

        if (statuses == null)
            conditions.add("st.iri = 'cm:CoreActive'");
        else {
            if (!statuses.contains("inactive"))
                conditions.add("st.iri NOT IN ('cm:CoreInactive', 'cm:LegacyInactive')\n");

            if (!statuses.contains("legacy"))
                conditions.add("st.iri NOT IN ('cm:LegacyActive', 'cm:LegacyInactive')\n");
        }

        if (conditions.size() > 0)
            sql += "WHERE " + String.join(" AND ", conditions) + "\n";

        sql += "LIMIT ?,?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;

            if (statuses != null && statuses.size() > 0)
                for (String status : statuses)
                    stmt.setString(i++, status);

            for (String word : words)
                stmt.setString(i++, word + '%');

            if (supertypes != null && !supertypes.isEmpty())
                for (String supertype : supertypes)
                    stmt.setString(i++, supertype);

            if (models != null && models.size() > 0) {
                for (String model : models)
                    stmt.setString(i++, model);
            }

            if (statuses != null && statuses.size() > 0) {
                for (String status : statuses)
                    stmt.setString(i++, status);
            }

            stmt.setInt(i++, offset);
            stmt.setInt(i++, size);

            try (ResultSet resultSet = stmt.executeQuery()) {
                result.setResults(ConceptHydrator.createConceptList(resultSet));
            }
        }

        try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            result.setCount(rs.getInt(1));
        }

        return result;
    }

    @Override
    public List<Concept> complete(String terms, List<String> models, List<String> statuses) throws SQLException {
        List<Concept> results = new ArrayList<>();

        String[] words = terms.split(" ");
        String select = "SELECT c.id, c.iri, c.name";
        String from = "FROM concept c\n";

        if (statuses != null && statuses.size() > 0)
            from += "JOIN concept s ON s.id = c.status AND s.iri IN (" + DALHelper.inListParams(statuses.size()) + ")\n";

        String useOrder = "";
        String priOrder = "";

        for (int i = 0; i < words.length; i++) {
            select += ", ABS(cw" + i + ".position - " + i + ") AS wp" + i;
            from += "JOIN word w" + i + " ON w" + i + ".word LIKE ?\n" +
                "JOIN concept_word cw" + i + " ON cw" + i + ".word = w" + i + ".dbid AND cw" + i + ".concept = c.id\n";
            if (i > 0) {
                priOrder += " + ";
                useOrder += " + ";
            }
            priOrder += "wp" + i;
            useOrder += "w" + i + ".useCount";
        }

        String sql = select + "\n" +
            from +
            "ORDER BY " + useOrder + " DESC, " + priOrder + ", LENGTH(name)\n" +
            "LIMIT 10";


        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            if (statuses != null && statuses.size() > 0)
                for (String status : statuses)
                    stmt.setString(i++, status);

            for (String word : words)
                stmt.setString(i++, word + '%');

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Concept()
                        .setId(rs.getInt("id"))
                        .setIri(rs.getString("iri"))
                        .setName(rs.getString("name"))
                    );
                }
            }
        }

        return results;
    }

    @Override
    public String completeWord(String term) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT word FROM word WHERE word LIKE ? ORDER BY useCount DESC LIMIT 1")) {
            stmt.setString(1, term + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("word");
                else
                    return "";
            }
        }
    }

    @Override
    public String getConceptName(String id) throws SQLException {
        String sql = "SELECT name FROM concept WHERE iri = ?\n";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next())
                    return resultSet.getString("name");
                else
                    return null;
            }
        }
    }

    @Override
    public List<Concept> getChildren(int id) throws SQLException {
        String sql = "SELECT c.*\n" +
            "FROM concept p\n" +
            "JOIN subtype s ON s.supertype = p.id\n" +
            "JOIN axiom a ON a.id = s.axiom AND a.token in ('SubClassOf', 'SubPropertyOf')\n" +
            "JOIN concept c ON c.id = s.concept\n" +
            "WHERE p.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            return ConceptHydrator.createConceptList(rs);
        }
    }

    @Override
    public List<Concept> getParents(int id) throws SQLException {
        String sql = "SELECT p.*\n" +
            "FROM concept c\n" +
            "JOIN subtype s ON s.concept = c.id\n" +
            "JOIN axiom a ON a.id = s.axiom AND a.token in ('SubClassOf', 'SubPropertyOf')\n" +
            "JOIN concept p ON p.id = s.supertype\n" +
            "WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            return ConceptHydrator.createConceptList(rs);
        }
    }

    @Override
    public List<ConceptTreeNode> getParentTree(int id, String root) throws SQLException {
        List<ConceptTreeNode> result = new ArrayList<>();

        Concept concept = getConcept(id);
        if (concept == null)
            return result;

        result.add(ConceptTreeNode.fromConcept(concept).setLevel(0));

        List<Concept> parents = getParents(id);
        while (parents != null && parents.size() > 0) {
            result.forEach(n -> n.setLevel(n.getLevel() + 1));

            result.add(0, ConceptTreeNode.fromConcept(parents.get(0)));

            if (root == null || !root.equals(result.get(0).getIri()))
                parents = getParents(result.get(0).getId());
            else
                parents = null;
        }

        return result;
    }

    @Override
    public List<Concept> getRootConcepts() throws SQLException {
        String sql = "SELECT c.*\n" +
            "FROM concept c\n" +
            "LEFT JOIN subtype s ON s.concept = c.id\n" +
            "WHERE s.supertype IS NULL\n" +
            "LIMIT 15";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return ConceptHydrator.createConceptList(rs);
        }
    }

    @Override
    public List<ConceptTreeNode> getHierarchy(int id) throws SQLException {
        List<ConceptTreeNode> result = new ArrayList<>();

        Concept concept = getConcept(id);
        if (concept == null)
            return result;

        result.add(ConceptTreeNode.fromConcept(concept).setLevel(0));

        List<Concept> children = getChildren(id);
        result.addAll(children
            .stream()
            .map(c -> ConceptTreeNode.fromConcept(c).setLevel(1))
            .collect(Collectors.toList())
        );

        List<Concept> parents = getParents(id);
        while (parents != null && parents.size() > 0) {
            result.forEach(n -> n.setLevel(n.getLevel() + 1));

            result.add(0, ConceptTreeNode.fromConcept(parents.get(0)));

            parents = getParents(result.get(0).getId());
        }

        return result;
    }


    // Filing routines
    @Override
    public int allocateConceptId(String conceptIri) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept (iri) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(2, conceptIri);
            stmt.execute();
            return DALHelper.getGeneratedKey(stmt);
        }
    }

    @Override
    public Integer getConceptId(String conceptIri) throws SQLException {
        if (conceptIri == null)
            LOG.warn("Concept Iri is null");

        Integer id = conceptIdCache.get(conceptIri);

        if (id == null) {

            try (PreparedStatement statement = conn.prepareStatement("SELECT id FROM concept WHERE iri = ?")) {
                statement.setString(1, conceptIri);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt("id");
                        conceptIdCache.put(conceptIri, id);
                    }
                }
            }
        }

        return id;
    }

    @Override
    public void upsertConcept(Concept concept) throws SQLException {
        String sql = "INSERT INTO concept (iri, status, name, description, code)\n" +
            "VALUES (?, ?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "iri = VALUES(iri), status = VALUES(status), name = VALUES(name), description = VALUES(description), code = VALUES(code)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            DALHelper.setString(stmt, i++, concept.getIri());
            DALHelper.setInt(stmt, i++, getConceptId(concept.getStatus()));
            DALHelper.setString(stmt, i++, concept.getName());
            DALHelper.setString(stmt, i++, concept.getDescription());
            DALHelper.setString(stmt, i++, concept.getCode());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Namespace> getNamespaces() throws SQLException {
        List<Namespace> result = new ArrayList<>();

        String sql = "SELECT iri, name, prefix, codePrefix FROM namespace";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    result.add(ConceptHydrator.createNamespace(rs));
            }
        }

        return result;
    }

    // UI Routines

    @Override
    public List<Concept> getCodeSchemes() throws SQLException {
        return getConceptsByRelationObject("SubClassOf", "CodeScheme");
    }

    @Override
    public List<Axiom> getAxioms() throws SQLException {
        List<Axiom> result = new ArrayList<>();

        String sql = "SELECT id, token, subtype, initial FROM axiom ORDER BY id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                result.add(
                    new Axiom()
                        .setId(rs.getInt("id"))
                        .setToken(rs.getString("token"))
                        .setInitial(rs.getBoolean("initial"))
                );
        }

        return result;
    }

    @Override
    public String createConcept(Concept concept) throws SQLException {
        // Get the prefix
        String iri = concept.getIri();
        int i = iri.indexOf(":");
        boolean generateIri = (i == -1 || i == iri.length());
        String prefix = generateIri ? iri : iri.substring(0, i);

        List<Namespace> namespaces = getNamespaces();
        Optional<Namespace> namespace = namespaces.stream().filter(n -> n.getPrefix().equals(prefix)).findFirst();

        if (!namespace.isPresent())
            throw new IllegalStateException("Unknown concept prefix namespace [" + prefix + "]");

        if (generateIri)
            concept.setIri(UUID.randomUUID().toString()); // Ensure its unique!

        String sql = "INSERT INTO concept (iri, name, description, code, status, namespace)\n" +
            "SELECT ?, ?, ?, ?, s.id, n.id\n" +
            "FROM concept s\n" +
            "JOIN namespace n ON n.prefix = ?\n" +
            "WHERE s.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            DALHelper.setString(stmt, 1, iri);
            DALHelper.setString(stmt, 2, concept.getName());
            DALHelper.setString(stmt, 3, concept.getDescription());
            DALHelper.setString(stmt, 4, concept.getCode());
            DALHelper.setString(stmt, 5, prefix);
            DALHelper.setString(stmt, 6, concept.getStatus());

            if (stmt.executeUpdate() != 1)
                throw new SQLException("Failed to create concept");

            // Generate IRI if necessary
            if (generateIri) {
                int id = DALHelper.getGeneratedKey(stmt);

                iri = namespace.get().getPrefix() + ":"
                    + ((namespace.get().getCodePrefix() == null)
                    ? id
                    : namespace.get().getCodePrefix() + id);

                sql = "UPDATE concept SET iri = ? WHERE id = ?";
                try (PreparedStatement stmt2 = conn.prepareStatement(sql)) {
                    stmt2.setString(1, iri);
                    stmt2.setInt(2, id);
                    if (stmt2.executeUpdate() != 1)
                        throw new SQLException("Failed to create concept iri");
                }
            }

            return iri;
        }
    }

    @Override
    public boolean updateConcept(Concept concept) throws SQLException {
        String sql = "UPDATE concept c\n" +
            "JOIN concept s ON s.iri = ?\n" +
            "SET c.name = ?, c.description = ? , c.code = ?, c.status = s.id\n" +
            "WHERE c.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setString(stmt, 1, concept.getStatus());
            DALHelper.setString(stmt, 2, concept.getName());
            DALHelper.setString(stmt, 3, concept.getDescription());
            DALHelper.setString(stmt, 4, concept.getCode());
            DALHelper.setString(stmt, 5, concept.getIri());

            return stmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteConcept(int conceptId) throws SQLException {
        String[] statements = {
            "DELETE FROM property_chain WHERE concept = ?",
            "DELETE FROM property_class WHERE concept = ?",
            "DELETE FROM property_data WHERE concept = ?",
            "DELETE FROM property_domain WHERE concept = ?",
            "DELETE FROM property_range WHERE concept = ?",
            "DELETE FROM concept_word WHERE concept = ?",
            "DELETE FROM subtype WHERE concept = ?",
            "DELETE FROM subtype WHERE supertype = ?",
            "DELETE FROM concept WHERE id = ?"
        };
        beginTransaction();
        try {
            for (String sql : statements) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, conceptId);
                    stmt.executeUpdate();
                }
            }
            commit();
            return true;
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    @Override
    public List<Concept> getAncestors(int conceptId) throws SQLException {
        String sql = "SELECT p.*\n" +
            "FROM concept c\n" +
            "JOIN subtype_closure s ON s.descendant = c.id\n" +
            "JOIN axiom a ON a.id = s.axiom AND a.token in ('SubClassOf', 'SubPropertyOf')\n" +
            "JOIN concept p ON p.id = s.ancestor\n" +
            "WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conceptId);
            ResultSet rs = stmt.executeQuery();

            return ConceptHydrator.createConceptList(rs);
        }
    }

    @Override
    public List<SimpleConcept> getAxiomSupertypes(int conceptId, String axiom) throws SQLException {
        List<SimpleConcept> result = new ArrayList<>();

        String sql = "SELECT s.id, c.iri, s.inferred, o.operator\n" +
            "FROM subtype s\n" +
            "JOIN axiom a ON a.id = s.axiom AND a.token = ?\n" +
            "JOIN concept c ON c.id = s.supertype\n" +
            "JOIN operator o ON o.id = s.operator\n" +
            "WHERE s.concept = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, axiom);
            stmt.setInt(2, conceptId);
            try (ResultSet rs = stmt.executeQuery()) {
                result.addAll(ConceptHydrator.createConceptDefinitionList(rs));
            }
        }

        return result.size() > 0 ? result : null;
    }

    @Override
    public List<RoleGroup> getAxiomRoleGroups(int conceptId, String axiom) throws SQLException {
        List<RoleGroup> result = new ArrayList<>();

        int group = -1;

        String sql = "SELECT * FROM (" +
            "SELECT pc.id, pc.group, p.iri AS property, o.iri AS object, null AS data, pc.minCardinality, pc.maxCardinality, pc.inferred, op.operator, vop.operator AS value_operator\n" +
            "FROM property_class pc\n" +
            "JOIN axiom a ON a.id = pc.axiom AND a.token = ?\n" +
            "JOIN concept p ON p.id = pc.property\n" +
            "JOIN concept o ON o.id = pc.object\n" +
            "JOIN operator op ON op.id = pc.operator\n" +
            "JOIN operator vop ON vop.id = pc.value_operator\n" +
            "WHERE pc.concept = ?\n" +
            "UNION\n" +
            "SELECT pd.id, pd.group, p.iri AS property, null AS object, pd.data, pd.minCardinality, pd.maxCardinality, pd.inferred, op.operator, vop.operator AS value_operator\n" +
            "FROM property_data pd\n" +
            "JOIN axiom a ON a.id = pd.axiom AND a.token = ?\n" +
            "JOIN concept p ON p.id = pd.property\n" +
            "JOIN operator op ON op.id = pd.operator\n" +
            "JOIN operator vop ON vop.id = pd.value_operator\n" +
            "WHERE pd.concept = ?\n) u\n" +
            "ORDER BY `group`\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, axiom);
            stmt.setInt(2, conceptId);
            stmt.setString(3, axiom);
            stmt.setInt(4, conceptId);
            try (ResultSet rs = stmt.executeQuery()) {
                result.addAll(ConceptHydrator.createRoleGroupList(rs));
            }
        }

        return result.size() > 0 ? result : null;
    }

    @Override
    public List<PropertyRange> getPropertyRanges(Integer conceptId) throws SQLException {
        List<PropertyRange> result = new ArrayList<>();
        String sql = "SELECT pr.id, c.iri AS `range`, pr.subsumption, o.operator\n" +
            "FROM property_range pr\n" +
            "JOIN concept c ON c.id = pr.value\n" +
            "JOIN operator o ON o.id = pr.operator\n" +
            "WHERE pr.concept = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conceptId);
            try (ResultSet rs = stmt.executeQuery()) {
                result.addAll(ConceptHydrator.createPropertyRangeList(rs));
            }
        }
        return result.size() > 0 ? result : null;
    }

    @Override
    public List<PropertyDomain> getPropertyDomains(Integer conceptId) throws SQLException {
        List<PropertyDomain> result = new ArrayList<>();
        String sql = "SELECT pd.id, c.iri AS domain, pd.inGroup, pd.disjointGroup, pd.minCardinality, pd.maxCardinality, pd.minInGroup, pd.maxInGroup, o.operator\n" +
            "FROM property_domain pd\n" +
            "JOIN concept c ON c.id = pd.domain\n" +
            "JOIN operator o ON o.id = pd.operator\n" +
            "WHERE pd.concept = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conceptId);
            try (ResultSet rs = stmt.executeQuery()) {
                result.addAll(ConceptHydrator.createPropertyDomainList(rs));
            }
        }
        return result.size() > 0 ? result : null;
    }

    @Override
    public List<String> getPropertyChains(int conceptId) throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "SELECT c.iri AS linkProperty\n" +
            "FROM property_chain pc\n" +
            "JOIN concept c ON c.id = pc.concept\n" +
            "WHERE pc.concept = ?\n" +
            "ORDER BY pc.linkNumber\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conceptId);
            try (ResultSet rs = stmt.executeQuery()) {
                result.addAll(ConceptHydrator.createPropertyChainList(rs));
            }
        }
        return result.size() > 0 ? result : null;
    }

    @Override
    public boolean addAxiomSupertype(int id, String axiom, String supertypeIri) throws SQLException {
        String sql = "INSERT INTO subtype\n" +
            "(concept, axiom, supertype)\n" +
            "SELECT c.id, a.id, s.id\n" +
            "FROM concept c\n" +
            "JOIN axiom a ON a.token = ?\n" +
            "JOIN concept s ON s.iri = ?\n" +
            "WHERE c.id = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, axiom);
            stmt.setString(2, supertypeIri);
            stmt.setInt(3, id);
            return stmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delSupertype(int supertypeId) throws SQLException {
        String sql = "DELETE FROM subtype WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supertypeId);
            return stmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean addAxiomRoleGroupProperty(int id, String axiom, PropertyDefinition definition, Integer group) throws SQLException {
        if (group == null)
            group = 0;

        if (definition.getObject() != null) {
            String sql = "INSERT INTO property_class\n" +
                "(concept, axiom, `group`, property, object)\n" +
                "SELECT c.id, a.id, ?, p.id, o.id\n" +
                "FROM concept c\n" +
                "JOIN axiom a ON a.token = ?\n" +
                "JOIN concept p ON p.iri = ?\n" +
                "JOIN concept o ON o.iri = ?\n" +
                "WHERE c.id = ?\n";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, group);
                stmt.setString(2, axiom);
                stmt.setString(3, definition.getProperty());
                stmt.setString(4, definition.getObject());
                stmt.setInt(5, id);
                return stmt.executeUpdate() == 1;
            }
        } else {
            String sql = "INSERT INTO property_data\n" +
                "(concept, axiom, `group`, property, data)\n" +
                "SELECT c.id, a.id, ?, p.id, ?\n" +
                "FROM concept c\n" +
                "JOIN axiom a ON a.token = ?\n" +
                "JOIN concept p ON p.iri = ?\n" +
                "WHERE c.id = ?\n";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, group);
                stmt.setString(2, definition.getData());
                stmt.setString(3, axiom);
                stmt.setString(4, definition.getProperty());
                stmt.setInt(5, id);
                return stmt.executeUpdate() == 1;
            }
        }
    }

    @Override
    public boolean delProperty(int propertyId, String type) throws SQLException {
        String sql = "object".equals(type.toLowerCase())
            ? "DELETE FROM property_class WHERE id = ?"
            : "DELETE FROM property_data WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, propertyId);
            return stmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delAxiomRoleGroup(int id, String axiom, Integer group) throws SQLException {
        String sql = "DELETE pc, pd\n" +
            "FROM concept c\n" +
            "JOIN axiom a ON a.token = ?\n" +
            "LEFT JOIN property_data pd ON pd.concept = c.id AND pd.axiom = a.id AND pd.group = ?\n" +
            "LEFT JOIN property_class pc ON pc.concept = c.id AND pc.axiom = a.id AND pc.group = ?\n" +
            "WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, axiom);
            stmt.setInt(2, group);
            stmt.setInt(3, group);
            stmt.setInt(4, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delAxiom(int id, String axiom) throws SQLException {
        String sql = "DELETE s, pc, pd\n" +
            "FROM concept c\n" +
            "JOIN axiom a ON a.token = ?\n" +
            "LEFT JOIN subtype s ON s.concept = c.id AND s.axiom = a.id\n" +
            "LEFT JOIN property_data pd ON pd.concept = c.id AND pd.axiom = a.id\n" +
            "LEFT JOIN property_class pc ON pc.concept = c.id AND pc.axiom = a.id\n" +
            // TODO: Add in other tables (property_domain, property_range, etc)
            "WHERE c.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, axiom);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<String> getOperators() throws SQLException {
        List<String> operators = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT operator FROM operator");
        ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                operators.add(rs.getString("operator"));
        }
        return operators;
    }

    @Override
    public boolean addPropertyRange(int id, PropertyRange propertyRange) throws SQLException {
        String sql = "INSERT INTO property_range\n" +
            "(concept, axiom, value, subsumption, operator)\n" +
            "SELECT c.id, a.id, v.id, ?, o.id\n" +
            "FROM concept c\n" +
            "JOIN axiom a ON a.token = ?\n" +
            "JOIN concept v ON v.iri = ?\n" +
            "JOIN operator o ON o.operator = ?\n" +
            "WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            stmt.setString(i++, propertyRange.getSubsumption());
            stmt.setString(i++, "PropertyRange");
            stmt.setString(i++, propertyRange.getRange());
            stmt.setString(i++, propertyRange.getOperator());
            stmt.setInt(i++, id);
            return stmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delPropertyRange(int propertyRangeId) throws SQLException {
        String sql = "DELETE FROM property_range WHERE id = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, propertyRangeId);
            return stmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean addPropertyDomain(int id, PropertyDomain propertyDomain) throws SQLException {
        String sql = "INSERT INTO property_domain\n" +
            "(concept, axiom, domain, minCardinality, maxCardinality, minInGroup, maxInGroup, operator)\n" +
            "SELECT c.id, a.id, d.id, ?, ?, ?, ?, o.id\n" +
            "FROM concept c\n" +
            "JOIN axiom a ON a.token = ?\n" +
            "JOIN concept d ON d.iri = ?\n" +
            "JOIN operator o ON o.operator = ?\n" +
            "WHERE c.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            DALHelper.setInt(stmt, i++, propertyDomain.getMinCardinality());
            DALHelper.setInt(stmt, i++, propertyDomain.getMaxCardinality());
            DALHelper.setInt(stmt, i++, propertyDomain.getMinInGroup());
            DALHelper.setInt(stmt, i++, propertyDomain.getMaxInGroup());
            stmt.setString(i++, "PropertyRange");
            stmt.setString(i++, propertyDomain.getDomain());
            stmt.setString(i++, propertyDomain.getOperator());
            stmt.setInt(i++, id);
            return stmt.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delPropertyDomain(int propertyDomainId) throws SQLException {
        String sql = "DELETE FROM property_domain WHERE id = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, propertyDomainId);
            return stmt.executeUpdate() == 1;
        }
    }

    private List<Concept> getConceptsByRelationObject(String relation, String object) throws SQLException {
        String sql = "SELECT c.*, m.*\n" +
            "FROM concept c\n" +
            "JOIN model m ON m.dbid = c.model\n" +
            "JOIN concept_relation cr ON cr.subject = c.dbid\n" +
            "JOIN concept r ON r.dbid = cr.relation AND r.id = ?\n" +
            "JOIN concept o ON o.dbid = cr.object AND o.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, relation);
            stmt.setString(2, object);
            try (ResultSet rs = stmt.executeQuery()) {
                return ConceptHydrator.createConceptList(rs);
            }
        }
    }

    /*


    @Override
    public void upsertPropertyDomain(int propertyDbid, int conceptDbid, int statusDbid, Domain domain) throws SQLException {
        String sql = "INSERT INTO property_domain (property, class, status, minimum, maximum, max_in_group)\n" +
            "VALUES (?, ?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "property = VALUES(property), class = VALUES(class), status = VALUES(status), minimum = VALUES(minimum), maximum = VALUES(maximum), max_in_group = VALUES(max_in_group)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setInt(stmt, 1, propertyDbid);
            DALHelper.setInt(stmt, 2, conceptDbid);
            DALHelper.setInt(stmt, 3, statusDbid);
            DALHelper.setInt(stmt, 4, (domain.getMinCardinality() == null) ? 0 : domain.getMinCardinality());
            DALHelper.setInt(stmt, 5, ("1".equals(domain.getMaxCardinality())) ? 1 : 0);
            DALHelper.setInt(stmt, 6, domain.getMaxInGroup());
            stmt.executeUpdate();
        }
    }

    @Override
    public void upsertPropertyRange(int propertyDbid, int statusDbid, List<SimpleExpressionConstraint> rangeClass) throws SQLException {
*/
/*        String sql = "INSERT INTO property_range (property, status, range_class)\n" +
            "VALUES (?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "property = VALUES(property), status = VALUES(status), range_class = VALUES(range_class)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setInt(stmt, 1, propertyDbid);
            DALHelper.setInt(stmt, 2, statusDbid);
            DALHelper.setString(stmt, 3, rangeClassJson);
            stmt.executeUpdate();
        }*//*

    }
*/

    // ********** Manager UI methods **********
/*




    @Override
    public Concept getConceptSummary(String id) throws SQLException {
        String sql = "SELECT c.id, c.name, c.description, cs.id AS scheme, c.code, cs.id AS status\n" +
            "FROM concept c\n" +
            "JOIN concept s ON s.dbid = c.status\n" +
            "LEFT JOIN concept cs ON cs.dbid = c.scheme\n" +
            "WHERE c.id = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Concept conceptSummary = ConceptHydrator.createConcept(resultSet);
                    return conceptSummary;
                }
            }
        }
        return null;
    }

    @Override
    public FullConcept getConcept(String id) throws SQLException, IOException {
        String sql = "SELECT m.iri AS model, c.*, s.id AS status_id, cs.id AS scheme_id\n" +
            "FROM concept c\n" +
            "JOIN model m ON m.dbid = c.model\n" +
            "JOIN concept s ON s.dbid = c.status\n" +
            "LEFT JOIN concept cs ON cs.dbid = c.scheme\n" +
            "WHERE c.id = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    FullConcept concept = ConceptHydrator.create(resultSet);

                    concept.setDefinition(getConceptDefinition(id));
                    concept.setPropertyDomain(getPropertyDomain(id));
                    concept.setPropertyRange(getPropertyRange(id));

                    return concept;
                }
            }
        }
        return null;
    }

    @Override
    public ConceptDefinition getConceptDefinition(String id) throws SQLException, IOException {
        ConceptDefinition conceptDefinition = new ConceptDefinition();

        getConceptDefinitionConcepts(id, conceptDefinition);
        getConceptDefinitionRoleGroups(id, conceptDefinition);

        return conceptDefinition;
    }

    private void getConceptDefinitionConcepts(String id, ConceptDefinition conceptDefinition) throws SQLException {
        String sql = "SELECT cdc.type, cdc.name, cdc.operator, cv.id as concept_value\n" +
            "FROM concept c\n" +
            "JOIN concept_definition_concept cdc ON cdc.concept = c.dbid\n" +
            "JOIN concept cv ON cv.dbid = cdc.concept_value\n" +
            "WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ConceptExpression conceptExpression = new ConceptExpression()
                        .setConcept(rs.getString("concept_value"))
                        .setName(rs.getString("name"))
                        .setOperator(Operator.byValue(rs.getShort("operator")));

                    short definitionType = rs.getShort("type");

                    switch (Axiom.byValue(definitionType)) {
                        case SUBTYPE_OF:
                            conceptDefinition.getSubtypeOf().add(conceptExpression);
                        break;
                        case EQUIVALENT_TO:
                            conceptDefinition.getEquivalentTo().add(conceptExpression);
                            break;
                        case TERM_CODE_OF:
                            if (conceptDefinition.getTermCodeOf() != null)
                                throw new IllegalStateException("termCodeOf already set (maximum = 1) for concept [" + id + "]");
                            else
                                conceptDefinition.setTermCodeOf(conceptExpression.getConcept());
                            break;
                        case INVERSE_PROPERTY_OF:
                            if (conceptDefinition.getInversePropertyOf() != null)
                                throw new IllegalStateException("inversePropertyOf already set (maximum = 1) for concept [" + id + "]");
                            else
                                conceptDefinition.setInversePropertyOf(conceptExpression.getConcept());
                            break;
                        case MAPPED_TO:
                            conceptDefinition.getMappedTo().add(conceptExpression.getConcept());
                            break;
                        case REPLACED_BY:
                            if (conceptDefinition.getReplacedBy() != null)
                                throw new IllegalStateException("replacedBy already set (maximum = 1) for concept [" + id + "]");
                            else
                                conceptDefinition.setReplacedBy(conceptExpression.getConcept());
                            break;
                        case CHILD_OF:
                            if (conceptDefinition.getChildOf() != null)
                                throw new IllegalStateException("childOf already set (maximum = 1) for concept [" + id + "]");
                            else
                                conceptDefinition.setChildOf(conceptExpression.getConcept());
                            break;
                        case DISJOINT_WITH:
                            conceptDefinition.getDisjointWith().add(conceptExpression);
                            break;
                        default:
                            throw new IllegalStateException("Invalid concept definition type [" + definitionType + "] for concept [" + id + "]");
                    }
                }
            }
        }
    }

    private void getConceptDefinitionRoleGroups(String id, ConceptDefinition conceptDefinition) throws SQLException {
        String sql = "SELECT rg.role_group, rg.operator, rg.property, rg.value, rg.value_concept\n" +
            "FROM concept c\n" +
            "JOIN concept_definition_role_group rg ON rg.concept = c.dbid\n" +
            "WHERE c.id = ?\n" +
            "ORDER BY rg.role_group";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                int roleGroup = -1;
                RoleGroup rg = null;
                while (rs.next()) {
                    if (roleGroup != rs.getInt("role_group")) {
                        roleGroup = rs.getInt("role_group");
                        rg = new RoleGroup();
                        conceptDefinition.getRoleGroup().add(rg);
                    }

                    AttributeExpression attExp = new AttributeExpression()
                        .setOperator(Operator.byName(rs.getString("operator")))
                        .setProperty(rs.getString("property"))
                        .setValue(rs.getString("value"))
                        .setValueConcept(rs.getString("value_concept"));

                    rg.getAttribute().add(attExp);
                }
            }
        }
    }

    private PropertyDomain getPropertyDomain(String id) throws SQLException, IOException {
        String sql = "SELECT c.id as class, s.id as status, p.id AS property, pd.operator, pd.minimum, pd.maximum, pd.max_in_group\n" +
            "FROM concept c\n" +
            "JOIN property_domain pd ON pd.class = c.dbid\n" +
            "JOIN concept p ON p.dbid = pd.property\n" +
            "JOIN concept s ON s.dbid = pd.status\n" +
            "WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setString(stmt, 1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return ConceptHydrator.createPropertyDomain(rs);
                else
                    return null;
            }
        }
    }
    public List<PropertyRange> getPropertyRange(String id) throws SQLException, IOException {
        String sql = "SELECT c.id as class, s.id as status, p.id as property, r.name, r.operator, r.type, v.id AS value\n" +
            "FROM concept c\n" +
            "JOIN property_range r ON r.class = c.dbid\n" +
            "JOIN concept s ON s.dbid = r.status\n" +
            "JOIN concept p ON p.dbid = r.property\n" +
            "JOIN concept v ON v.dbid = r.concept\n" +
            "WHERE c.id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setString(stmt, 1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return ConceptHydrator.createPropertyRange(rs);
                else
                    return null;
            }
        }
    }








    // ------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public List<DocumentInfo> getDocuments() throws SQLException {
        List<DocumentInfo> result = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("SELECT dbid, path, version, draft FROM document");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(DocumentHydrator.create(resultSet));
            }
        }

        return result;
    }




*//*    public FullConcept getConcept(String code_scheme, String code) throws SQLException, IOException {
        String sql = "SELECT c.*, s.id AS scheme_id FROM concept c JOIN concept s ON s.dbid = c.scheme WHERE s.id = ? AND c.code = ?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, code_scheme);
                statement.setString(2, code);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        FullConcept concept = ConceptHydrator.create(resultSet);

*//**//*
                        concept.setDefinition(getConceptDefinition(concept.getId()));
                        concept.setPropertyDomain(getPropertyDomain(concept.getId()));
                        concept.setPropertyRange(getPropertyRange(concept.getId()));
*//**//*

                        return concept;
                    }
                }
            }
        return null;
    }*//*

    @Override
    public List<DraftConcept> getDocumentPending(int dbid, Integer size, Integer page) throws SQLException {
        if (dbid == 0)
            return getDraftTermMaps();
        else
            return getDocumentDraftConcepts(dbid);
    }
    private List<DraftConcept> getDraftTermMaps() throws SQLException {
        List<DraftConcept> result = new ArrayList<>();

        String sql = "SELECT c.id, t.term as name, t.published, t.updated\n" +
            "FROM concept_term_map t\n" +
            "JOIN concept c ON c.dbid = t.target\n" +
            "WHERE t.draft = TRUE\n" +
            "LIMIT 15\n";

        try (PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(ConceptHydrator.createDraft(resultSet));
            }
        }

        return result;
    }
    private List<DraftConcept> getDocumentDraftConcepts(int dbid) throws SQLException {
        List<DraftConcept> result = new ArrayList<>();

        String sql = "SELECT c.id, c.name, c.published, c.updated\n" +
            "FROM concept c\n" +
            "WHERE document = ?\n" +
            "AND status < 2\n" +
            "LIMIT 15\n";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, dbid);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(ConceptHydrator.createDraft(resultSet));
                }
            }
        }

        return result;
    }

    @Override
    public byte[] getDocumentLatestPublished(Integer dbid) throws SQLException {
        byte[] result = null;

        String sql = "SELECT data\n" +
            "FROM document d\n" +
            "JOIN document_archive a ON a.dbid = d.dbid AND a.version = d.version\n" +
            "WHERE d.dbid = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setInt(stmt,1, dbid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    result = rs.getBytes("data");
            }
        }
        return result;
    }

    @Override
    public List<IdNamePair> getSchemes() throws SQLException {
        // TODO: get from TCT
        String sql = "SELECT DISTINCT s.id, s.name\n" +
            "FROM concept c\n" +
            "JOIN concept s ON s.id = c.scheme";

        List<IdNamePair> result = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(new IdNamePair()
                    .setId(rs.getString("id"))
                   .setName(rs.getString("name"))
                );
            }
        }

        return result;
    }



    @Override
    public void publishDocument(int dbid, String level) throws SQLException, IOException {
*//*        LOG.debug("Publishing document...");
        Document doc = getDocument(dbid);
        if ("major".equals(level.toLowerCase()))
            doc.getVersion().incMajor();
        else if ("minor".equals(level.toLowerCase()))
            doc.getVersion().incMinor();
        else
            doc.getVersion().incBuild();

        if (dbid == 0)
            publishTermMaps(doc);
        else
            publishConceptDocument(doc);*//*
    }
    private void publishTermMaps(DocumentInfo doc) throws SQLException, IOException {
*//*        String sql = "SELECT t.term, typ.id as type, tgt.id as target\n" +
            "FROM concept_term_map t\n" +
            "JOIN concept typ ON typ.dbid = t.type\n" +
            "JOIN concept tgt ON tgt.dbid = t.target\n" +
            "WHERE t.draft = TRUE\n";

        ObjectMapper om = new ObjectMapper();

        ObjectNode root = om.createObjectNode();
        root.put("document", doc.getPath() + "/" + doc.getVersion().toString());
        ArrayNode maps = root.putArray("TermMaps");

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ObjectNode map = om.createObjectNode();
                map.put("term", rs.getString("term"));
                map.put("type", rs.getString("type"));
                map.put("target", rs.getString("target"));
                maps.add(map);
            }
        }

        publishDocumentToArchive(doc, om.writeValueAsString(root));

        try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept_term_map SET draft=FALSE, published=? WHERE draft=TRUE")) {
            DALHelper.setString(stmt, 1, doc.getVersion().toString());
            stmt.execute();
        }*//*
    }
    private void publishConceptDocument(DocumentInfo doc) throws SQLException, IOException {
*//*        LOG.debug("Analysing document...");
        List<Integer> pending = getConceptDocumentPendingIds(doc.getDbid());

        int s = pending.size();
        LOG.debug("Retrieving " + s + " concepts...");

        int i = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"document\" : \"").append(doc.getPath()).append("/").append(doc.getVersion().toString()).append("\",").append("\"Concepts\" : [");
        try (PreparedStatement statement = conn.prepareStatement("SELECT data FROM concept WHERE dbid = ?")) {
            for (Integer conceptDbid : pending) {
                if (i % 1000 == 0) {
                    LOG.debug("Checking concept " + i + "/" + s);
                }
                statement.setInt(1, conceptDbid);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        if (i > 0)
                            sb.append(",");
                        sb.append(resultSet.getString("data"));
                    }
                }
                i++;
            }
        }
        // Build new document JSON
        sb.append("] }");

        // Update database
        publishDocumentToArchive(doc, sb.toString());

        // Update concept published versions
        LOG.debug("Marking concepts published [" + doc.getVersion().toString() + "]...");
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept SET published = ?, status = 2 WHERE dbid = ?")) {
            i = 0;
            for (Integer conceptDbid : pending) {
                if (i % 1000 == 0) {
                    LOG.debug("Updating concept " + i + "/" + s);
                }
                DALHelper.setString(stmt, 1, doc.getVersion().toString());
                DALHelper.setInt(stmt,2, conceptDbid);
                stmt.execute();
                i++;
            }
        }
        LOG.debug("Document publish finished");*//*
    }
    private void publishDocumentToArchive(DocumentInfo doc, String docJson) throws IOException, SQLException {
*//*        // Insert new doc
        LOG.debug("Compressing document...");
        byte[] compressedDocJson = ZipUtils.compress(docJson.getBytes());
        LOG.debug("Compressed to " + compressedDocJson.length + " bytes");
        LOG.debug("Publishing in database [" + doc.getVersion().toString() + "]...");
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO document_archive (dbid, version, data) VALUES (?, ?, ?)")) {
            DALHelper.setInt(stmt,1, doc.getDbid());
            DALHelper.setString(stmt, 2, doc.getVersion().toString());
            stmt.setBytes(3, compressedDocJson);
            stmt.execute();
        }

        // Update doc version
        LOG.debug("Updating document version  [" + doc.getVersion().toString() + "]...");
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE document SET version = ?, draft = FALSE WHERE dbid = ?")) {
            DALHelper.setString(stmt, 1, doc.getVersion().toString());
            DALHelper.setInt(stmt,2, doc.getDbid());
            stmt.execute();
        }*//*
    }
    private List<Integer> getConceptDocumentPendingIds(int documentDbid) throws SQLException {
        List<Integer> result = new ArrayList<>();

        try (PreparedStatement statement = conn.prepareStatement("SELECT dbid FROM concept WHERE document = ? AND status < 2")) {
            statement.setInt(1, documentDbid);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next())
                    result.add(rs.getInt("dbid"));
            }
        }

        return result;
    }

    @Override
    public DocumentInfo getDocument(int dbid) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT path, version, draft FROM document WHERE dbid = ?")) {
            DALHelper.setInt(stmt,1, dbid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    DocumentInfo di = new DocumentInfo();
                    di.setDocumentIri(URI.create(rs.getString("path")));
                    di.setBaseModelVersion(Version.fromString(rs.getString("version")));
                    return di;
                } else
                    return null;
            }
        }
    }

    @Override
    public void updateDocument(int dbid, String documentJson) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE document SET data = ? WHERE dbid = ?")) {
            DALHelper.setString(stmt, 1, documentJson);
            DALHelper.setInt(stmt,2, dbid);
            stmt.execute();
        }
    }

    @Override
    public void createConcept(int modelDbid, String id, String name) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept (model, data) VALUES (?, JSON_OBJECT('id', ?, 'name', ?))")) {
            DALHelper.setInt(stmt,1, modelDbid);
            DALHelper.setString(stmt, 2, id);
            DALHelper.setString(stmt, 3, name);
            stmt.execute();
        }
    }

    @Override
    public Concept updateConcept(Concept newConcept) throws SQLException, IOException {
*//*
        boolean changed;
        String id = newConcept.getId();
        Integer dbid = getConceptDbid(id);
        Concept oldConcept = getConcept(id);

        // Check properties
        changed = checkAndUpdateRange(dbid, newConcept.getPropertyRange(), oldConcept.getPropertyRange());
        changed = changed || checkAndUpdateDefinition(dbid, newConcept.getDefinition(), oldConcept.getDefinition());
        changed = changed || checkAndUpdateDomain(dbid, newConcept.getPropertyDomain(), oldConcept.getPropertyDomain());
        changed = changed || checkAndUpdateRange(dbid, newConcept.getPropertyRange(), oldConcept.getPropertyRange());

        if (changed || !ObjectMapperPool.getInstance().writeValueAsString(newConcept).equals(ObjectMapperPool.getInstance().writeValueAsString(oldConcept))) {
            // Update and inc revision
            newConcept.setRevision(newConcept.getRevision() + 1);
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE concept SET document = ?, id = ?, name = ?, description = ?, scheme = ?, code = ?, revision = ?, status = ? WHERE dbid = ?")) {
                int i = 1;
                DALHelper.setInt(stmt,i++, newConcept.getDocument());
                DALHelper.setString(stmt, i++, id);
                DALHelper.setString(stmt, i++, newConcept.getName());
                DALHelper.setString(stmt, i++, newConcept.getDescription());
                if (newConcept.getScheme() != null) {
                    DALHelper.setInt(stmt,i++, getConceptDbid(newConcept.getScheme()));
                    DALHelper.setString(stmt, i++, newConcept.getCode());
                } else {
                    stmt.setNull(i++, INTEGER);
                    stmt.setNull(i++, VARCHAR);
                }
                DALHelper.setInt(stmt,i++, newConcept.getRevision());
                DALHelper.setInt(stmt,i++, getConceptDbid(newConcept.getStatus()));
                DALHelper.setInt(stmt,i++, dbid);
                int rows = stmt.executeUpdate();
                if (rows != 1)
                    throw new IllegalStateException("Unable to update concept " + id);
            }
            // Archive previous concept
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept_archive (dbid, revision, data) VALUES (?, ?, ?)")) {
                DALHelper.setInt(stmt,1, dbid);
                DALHelper.setInt(stmt,2, newConcept.getRevision());
                DALHelper.setString(stmt, 3, ObjectMapperPool.getInstance().writeValueAsString(oldConcept));
                int rows = stmt.executeUpdate();
                if (rows != 1)
                    throw new IllegalStateException("Unable to create archive entry " + id);
            }
            // Mark document as draft
            try (PreparedStatement stmt = conn.prepareStatement("UPDATE document SET draft = TRUE WHERE dbid = ?")) {
                DALHelper.setInt(stmt,1, newConcept.getDocument());
                int rows = stmt.executeUpdate();
                if (rows != 1)
                    throw new IllegalStateException("Unable to update document " + id);
            }
        }
*//*

        return newConcept;
    }

*//*
    public boolean checkAndUpdateDefinition(int dbid, ConceptDefinition newDef, ConceptDefinition oldDef) throws SQLException {
        if ((newDef != null && !newDef.equals(oldDef))
            || oldDef != null && !oldDef.equals((newDef))
        ) {
            // Replace the definition
            try (PreparedStatement stmt = conn.prepareStatement("REPLACE INTO concept_definition (dbid, status, definition) VALUES (?, ?, ?)")) {
                DALHelper.setInt(stmt,1, dbid);
                DALHelper.setInt(stmt,2, getConceptDbid(newDef.getStatus()));
                DALHelper.setString(stmt, 3, newDef.getDefinition());

                int rows = stmt.executeUpdate();
                if (rows == 0)  // Replace can be 1 (insert) or 2 (update) rows
                    throw new IllegalStateException("Failed to update definition");
            }
            return true;
        } else
            return false;
    }
*//*
    public boolean checkAndUpdateDomain(Integer dbid, PropertyDomain newDomain, PropertyDomain oldDomain) throws JsonProcessingException, SQLException {
        if (!ObjectMapperPool.getInstance().writeValueAsString(newDomain).equals(ObjectMapperPool.getInstance().writeValueAsString(oldDomain))) {
            // Delete the old domain
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM concept_domain WHERE dbid = ?")) {
                DALHelper.setInt(stmt,1, dbid);
                stmt.execute();
            }

            // Insert new ones
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept_domain (dbid, status, property, minimum, maximum) SELECT ?, ?, dbid, ?, ? FROM concept WHERE id = ?")) {
                for (Domain dom: newDomain.getDomain()) {
                        DALHelper.setInt(stmt,1, dbid);
                        DALHelper.setInt(stmt,2, getConceptDbid(newDomain.getStatus()));
                        DALHelper.setInt(stmt, 3, dom.getMinCardinality());
                        DALHelper.setInt(stmt, 4, dom.getMaxCardinality());
                        DALHelper.setString(stmt, 5, dom.getClazz());
                        int rows = stmt.executeUpdate();
                        if (rows != 1)
                            throw new IllegalStateException("Failed to insert domain");
                }
            }

            return true;
        } else
            return false;
    }
*//*
    public boolean checkAndUpdateRange(Integer dbid, PropertyRange newRange, PropertyRange oldRange) throws SQLException {
        if ( (newRange != null && !newRange.equals(oldRange))
            || (oldRange != null && !oldRange.equals(newRange))
        ) {
            // Delete the old ranges
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM concept_range WHERE dbid = ?")) {
                DALHelper.setInt(stmt,1, dbid);
                stmt.execute();
            }

            // Add the new ranges
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO concept_range (dbid, status, `range`) VALUES (?, ?, ?)")) {
                for (String range: newRange.getRange()) {
                    DALHelper.setInt(stmt,1, dbid);
                    DALHelper.setInt(stmt,2, getConceptDbid(newRange.getStatus()));
                    DALHelper.setString(stmt, 3, range);
                    int rows = stmt.executeUpdate();
                    if (rows == 0)  // Replace can be 1 (insert) or 2 (update) rows
                        throw new IllegalStateException("Failed to update range");
                }
            }
            return true;
        } else
            return false;
    }
*//*

    @Override
    public String getConceptJSON(String id) throws SQLException {
        String sql = "SELECT data FROM concept WHERE id = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next())
                    return resultSet.getString("data");
                else
                    return null;
            }
        }
    }



    @Override
    public String validateIds(List<String> ids) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement("SELECT 1 FROM concept WHERE id = ?")) {

            for (String id : ids) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next())
                        return id;
                }
            }
        }

        return null;
    }


    private List<Concept> getConceptSummariesFromResultSet(ResultSet resultSet) throws SQLException {
        List<Concept> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(ConceptHydrator.createConcept(resultSet));
        }

        return result;
    }

    public void processDraftDocument(String userId, String userName, String instance, String docPath, String draftJson) throws IOException, SQLException {
        byte category = -1;
        JsonNode root = ObjectMapperPool.getInstance().readTree(draftJson);
        if (root.has("Concepts")) {
            checkAndCreateUnknownDrafts(docPath, (ArrayNode) root.get("Concepts"));
            category = 0;
        } else if (root.has("TermMaps")) {
            category = 1;
        }

        String sql = "INSERT INTO workflow_task (category, user_id, user_name, subject, data)\n" +
            "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setByte(stmt, 1, category);
            DALHelper.setString(stmt, 2, userId);
            DALHelper.setString(stmt, 3, userName);
            DALHelper.setString(stmt, 4, "Document [" + docPath + "] drafts from [" + instance + "]");
            DALHelper.setString(stmt, 5, draftJson);
            stmt.execute();
        }
    }

    private boolean checkAndCreateUnknownDrafts(String docpath, ArrayNode concepts) throws SQLException, JsonProcessingException {

        boolean created = false;
*//*
        Integer docDbid = getDocumentDbid(docpath);
        if (docDbid == null)
            throw new IllegalArgumentException("Unknown document [" + docpath + "]");

        Iterator<JsonNode> iterator = concepts.iterator();

        try (PreparedStatement exists = conn.prepareStatement("SELECT dbid FROM concept WHERE id = ?");
             PreparedStatement schemeCode = conn.prepareStatement("SELECT dbid FROM concept WHERE scheme = ? AND code = ?");
            PreparedStatement insert = conn.prepareStatement("INSERT INTO concept (document, data) VALUES (?, ?)")){
            while (iterator.hasNext()) {
                ObjectNode concept = (ObjectNode) iterator.next();

                // Check if it exists
                String id = concept.get("id").textValue();
                exists.setString(1,id);
                try (ResultSet rs = exists.executeQuery()) {

                    // Concept ID doesnt exist so we need to create
                    if (!rs.next()) {
                        // If scheme/code exists (under another concept) then remove from this one
                        if (concept.has("code_scheme") && concept.has("code")) {
                            schemeCode.setString(1, concept.get("code_scheme").get("id").textValue());
                            schemeCode.setString(2, concept.get("code").textValue());
                            try (ResultSet rs2 = schemeCode.executeQuery()) {
                                if (rs2.next()) {
                                    concept.remove("code_scheme");
                                    concept.remove("code");
                                }
                            }
                        }

                        // Insert into DB
                        insert.setInt(1, docDbid);
                        insert.setString(2, ObjectMapperPool.getInstance().writeValueAsString(concept));
                        insert.execute();
                        created = true;
                    }
                }
            }
        }

        // If we created any draft concepts, update the document accordingly
        if (created) {
            try (PreparedStatement draftDocument = conn.prepareStatement("UPDATE document SET draft = TRUE WHERE dbid = ?")) {
                draftDocument.setInt(1, docDbid);
                draftDocument.execute();
            }
        }
*//*
        return created;
    }*/
}
