package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InformationManagerJDBCDAL extends BaseJDBCDAL implements InformationManagerDAL {
    private static final Logger LOG = LoggerFactory.getLogger(InformationManagerJDBCDAL.class);
    private Map<String, Integer> conceptIdCache = new HashMap<>();

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

    private Integer getOntologyIdByIri(String ontologyIri) throws SQLException {
        if (ontologyIri == null || ontologyIri.isEmpty())
            throw new IllegalStateException("ontologyIri not supplied");

        try (PreparedStatement statement = conn.prepareStatement("SELECT id FROM ontology WHERE iri = ?")) {
            statement.setString(1, ontologyIri);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next())
                    return rs.getInt("id");
                else
                    return null;
            }
        }
    }

    @Override
    public void upsertConcept(Concept concept) throws SQLException {
        String sql = "INSERT INTO concept (iri, status, name, description, ontology, code)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "iri = VALUES(iri), status = VALUES(status), name = VALUES(name), description = VALUES(description), ontology = VALUES(ontology), code = VALUES(code)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            DALHelper.setString(stmt, i++, concept.getIri());
            DALHelper.setInt(stmt, i++, getConceptId(concept.getStatus()));
            DALHelper.setString(stmt, i++, concept.getName());
            DALHelper.setString(stmt, i++, concept.getDescription());
            DALHelper.setInt(stmt, i++, getOntologyIdByIri(concept.getOntology()));
            DALHelper.setString(stmt, i++, concept.getCode());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<ConceptRelation> getConceptRelations(String conceptId, Boolean includeInherited) throws SQLException {
/*        String sql = "SELECT s.id AS subject, `group`, r.id AS relation, o.id AS object,\n" +
            "card.dbid AS cardDbid, card.minCardinality, card.maxCardinality, card.maxInGroup,\n" +
            "data.dbid AS dataDbid, data.concept, data.value\n" +
            "FROM concept_relation cr\n" +
            "JOIN concept s ON s.dbid = cr.subject\n" +
            "JOIN concept r ON r.dbid = cr.relation\n" +
            "JOIN concept o ON o.dbid = cr.object\n" +
            "LEFT JOIN concept_relation_cardinality card ON card.dbid = cr.dbid\n" +
            "LEFT JOIN im_master.concept_property_data data ON data.relation = cr.dbid\n" +
            "WHERE s.id = ?";*/

        String sql = "SELECT c.iri AS subject, pc.group, p.iri as relation, o.iri AS object, pc.minCardinality, pc.maxCardinality\n" +
            "FROM concept c\n" +
            "JOIN property_class pc ON pc.concept = c.id\n" +
            "JOIN axiom a ON a.id = pc.axiom\n" +
            "JOIN concept p ON p.id = pc.property\n" +
            "JOIN concept o ON o.id = pc.object\n" +
            "WHERE c.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conceptId);
            try (ResultSet rs = stmt.executeQuery()) {
                return ConceptHydrator.createConceptRelations(rs);
            }
        }
    }
    @Override
    public void replaceConceptRelations(String conceptId, List<ConceptRelation> relations) throws SQLException {
        String sql = "DELETE cr, crc\n" +
            "FROM concept c\n" +
            "JOIN concept_relation cr ON cr.subject = c.dbid\n" +
            "LEFT JOIN concept_relation_cardinality crc ON crc.dbid = cr.dbid\n" +
            "WHERE c.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setString(stmt, 1, conceptId);
            stmt.execute();
        }

        insertConceptRelations(conceptId, relations);
    }
    @Override
    public void insertConceptRelations(String conceptId, List<ConceptRelation> relations) throws SQLException {
        Integer dbid = getConceptId(conceptId);

        String insCRsql = "INSERT INTO concept_relation (subject, `group`, relation, object)\n" +
            "SELECT ?, ?, r.dbid, o.dbid\n" +
            "FROM concept r\n" +
            "JOIN concept o ON o.id = ?\n" +
            "WHERE r.id = ?";

        String insCRCsql = "INSERT INTO concept_relation_cardinality (dbid, minCardinality, maxCardinality, maxInGroup)\n" +
            "VALUES (?, ?, ?, ?)";

        try (PreparedStatement insCR = conn.prepareStatement(insCRsql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insCRC = conn.prepareStatement(insCRCsql)) {
            for (ConceptRelation rel : relations) {
                DALHelper.setInt(insCR, 1, dbid);
                DALHelper.setInt(insCR, 2, rel.getGroup());
                DALHelper.setString(insCR, 3, rel.getObject());
                DALHelper.setString(insCR, 4, rel.getRelation());
                insCR.executeUpdate();

                ConceptRelationCardinality card = rel.getCardinality();
                if (card != null) {
                    int crDbid = DALHelper.getGeneratedKey(insCR);
                    DALHelper.setInt(insCRC, 1, crDbid);
                    DALHelper.setInt(insCRC, 2, card.getMinCardinality());
                    DALHelper.setInt(insCRC, 3, card.getMaxCardinality());
                    DALHelper.setInt(insCRC, 4, card.getMaxInGroup());
                    insCRC.executeUpdate();
                }
            }
        }
    }

    // UI Routines
    @Override
    public SearchResult getMRU(Integer size, String supertype) throws SQLException {
        SearchResult result = new SearchResult();

        if (size == null)
            size = 15;

        String sql = "\tSELECT c.*\n" +
            "\tFROM concept c\n";

/*
        if (supertype != null && !supertype.isEmpty()) {
            sql += "JOIN concept_tct t ON t.source = c.dbid\n" +
                "JOIN concept p ON p.dbid = t.property AND p.id = 'subtypeOf'\n" +
                "JOIN concept o ON o.dbid = t.target AND o.id = ? \n";
        }

        sql += "\tORDER BY c.updated DESC\n" +

 */
        sql +=    "\tLIMIT ?\n";


        sql = "SELECT c.iri, s.name AS status, c.name, c.description, o.iri AS ontology, c.code\n" +
            "FROM (\n" + sql + ") c\n" +
            "JOIN concept s ON s.id = c.status\n" +
            "LEFT JOIN ontology o ON o.id = c.ontology\n" +
            "";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            int i = 1;
            statement.setInt(i++, size);
/*
            if (supertype != null && !supertype.isEmpty())
                statement.setString(1, supertype);
*/

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
    public List<Ontology> getOntologies() throws SQLException {
        List<Ontology> result = new ArrayList<>();

        String sql = "SELECT iri, name FROM ontology";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    result.add(ConceptHydrator.createOntology(rs));
            }
        }

        return result;
    }
    @Override
    public SearchResult search(String terms, String supertype, Integer size, Integer page, List<String> models, List<String> statuses) throws SQLException {
        page = (page == null) ? 0 : page;       // Default page to 1
        size = (size == null) ? 15 : size;      // Default page size to 15
        int offset = page * size;               // Calculate offset from page & size
        String[] words = terms.split(" ");

        SearchResult result = new SearchResult()
            .setPage(page);

        String select = "SELECT c.iri, c.name, c.description, c.code, c.status, c.ontology";
        String from = "FROM concept c\n";

        if (statuses != null && statuses.size() > 0)
            from += "JOIN concept s ON s.id = c.status AND s.iri IN (" + DALHelper.inListParams(statuses.size()) + ")\n";

        String useOrder = "";
        String priOrder = "";

        for (int i = 0; i < words.length; i++) {
            select += ", ABS(cw"+i+".position - " + i + ") AS wp" + i;
            from += "JOIN word w"+i+" ON w"+i+".word LIKE ?\n" +
                "JOIN concept_word cw"+i+" ON cw"+i+".word = w"+i+".dbid AND cw"+i+".concept = c.id\n";

            if (i>0) {
                priOrder += " + ";
                useOrder += " + ";
            }
            priOrder += "wp" + i;
            useOrder += "w" + i + ".useCount";
        }
        if (supertype != null && !supertype.isEmpty())

            from += "JOIN concept_tct tct ON tct.source = c.id\n" +
                "JOIN concept r ON r.dbid = tct.property AND r.id = 'subtypeOf'\n" +
                "JOIN concept t ON t.dbid = tct.target AND t.id = ?\n";

        String sql = "SELECT SQL_CALC_FOUND_ROWS u.iri, u.name, u.description, u.code, st.name AS status, o.id AS ontology\n" +
        "FROM (\n" +
            select + "\n" +
            from +
            "ORDER BY " + useOrder + " DESC, " + priOrder + ", LENGTH(c.name)\n" +
        ") AS u\n" +
            "LEFT JOIN concept st ON st.id = u.status\n" +
            "LEFT JOIN ontology o ON o.id = u.ontology\n";

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
                for (String status: statuses)
                    stmt.setString(i++, status);

            for (String word: words)
                stmt.setString(i++, word + '%');

            if (supertype != null && !supertype.isEmpty())
                stmt.setString(i++, supertype);

            if (models != null && models.size() > 0) {
                for (String model: models)
                    stmt.setString(i++, model);
            }

            if (statuses != null && statuses.size() > 0) {
                for (String status: statuses)
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
    public List<String> complete(String terms, List<String> models, List<String> statuses) throws SQLException {
        List<String> results = new ArrayList<>();

        String[] words = terms.split(" ");
        String select = "SELECT c.name";
        String from = "FROM concept c\n";

        if (statuses != null && statuses.size() > 0)
            from += "JOIN concept s ON s.id = c.status AND s.iri IN (" + DALHelper.inListParams(statuses.size()) + ")\n";

        String useOrder = "";
        String priOrder = "";

        for (int i = 0; i < words.length; i++) {
            select += ", ABS(cw"+i+".position - " + i + ") AS wp" + i;
            from += "JOIN word w"+i+" ON w"+i+".word LIKE ?\n" +
                "JOIN concept_word cw"+i+" ON cw"+i+".word = w"+i+".dbid AND cw"+i+".concept = c.id\n";
            if (i>0) {
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
                for (String status: statuses)
                    stmt.setString(i++, status);

            for (String word: words)
                stmt.setString(i++, word + '%');

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(rs.getString("name"));
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

    private String toBoolean(String terms) {
        List<String> matchList = new ArrayList<>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(terms);
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        }

        matchList.removeAll(Arrays.asList("for", "from", "the"));

        StringBuilder sb = new StringBuilder();
        for (String match: matchList) {
            if (sb.length() > 0)
                sb.append(" ");

            if (match.startsWith("-"))
                sb.append(match);
            else if (match.startsWith("+"))
                sb.append(match);
            else
                sb.append("+").append(match);
        }

        return sb.toString();
    }
    @Override
    public Concept getConcept(String conceptId) throws SQLException {
        String sql = "SELECT c.iri, s.name AS status, c.name, c.description, o.iri AS ontology, c.code\n" +
        "FROM concept c\n" +
            "JOIN concept s ON s.id = c.status\n" +
            "JOIN ontology o ON o.id = c.ontology\n" +
            "WHERE c.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conceptId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return ConceptHydrator.createConcept(rs);
                else
                    return null;
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
    public List<Concept> getChildren(String conceptId) throws SQLException {
        String sql = "SELECT c.*\n" +
            "FROM concept p\n" +
            "JOIN subtype s ON s.supertype = p.id\n" +
            "JOIN axiom a ON a.id = s.axiom AND a.token = 'SubClassOf'\n" +
            "JOIN concept c ON c.id = s.concept\n" +
            "WHERE p.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conceptId);
            ResultSet rs = stmt.executeQuery();

            return ConceptHydrator.createConceptList(rs);
        }
    }
    @Override
    public List<Concept> getParents(String conceptId) throws SQLException {
        String sql = "SELECT p.*\n" +
            "FROM concept c\n" +
            "JOIN subtype s ON s.concept = c.id\n" +
            "JOIN axiom a ON a.id = s.axiom AND a.token = 'SubClassOf'\n" +
            "JOIN concept p ON p.id = s.supertype\n" +
            "WHERE c.iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conceptId);
            ResultSet rs = stmt.executeQuery();

            return ConceptHydrator.createConceptList(rs);
        }
    }

    @Override
    public List<ConceptTreeNode> getParentTree(String conceptId, String root) throws SQLException {
        List<ConceptTreeNode> result = new ArrayList<>();

        Concept concept = getConcept(conceptId);
        if (concept == null)
            return result;

        result.add(ConceptTreeNode.fromConcept(concept).setLevel(0));

        List<Concept> parents = getParents(conceptId);
        while (parents != null && parents.size() > 0) {
            result.forEach(n -> n.setLevel(n.getLevel() + 1));

            result.add(0, ConceptTreeNode.fromConcept(parents.get(0)));

            if (root == null || !root.equals(result.get(0).getIri()))
                parents = getParents(result.get(0).getIri());
            else
                parents = null;
        }

        return result;
    }

    @Override
    public List<Concept> getRootConcepts() throws SQLException {
        String sql = "SELECT c.*, o.*\n" +
            "FROM concept c\n" +
            "JOIN ontology o ON o.id = c.ontology\n" +
            "LEFT JOIN subtype s ON s.concept = c.id\n" +
            "WHERE s.supertype IS NULL\n" +
            "LIMIT 15";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return ConceptHydrator.createConceptList(rs);
        }
    }

    @Override
    public List<ConceptTreeNode> getHierarchy(String conceptId) throws SQLException {
        List<ConceptTreeNode> result = new ArrayList<>();

        Concept concept = getConcept(conceptId);
        if (concept == null)
            return result;

        result.add(ConceptTreeNode.fromConcept(concept).setLevel(0));

        List<Concept> children = getChildren(conceptId);
        result.addAll(children
            .stream()
            .map(c -> ConceptTreeNode.fromConcept(c).setLevel(1))
            .collect(Collectors.toList())
        );

        List<Concept> parents = getParents(conceptId);
        while (parents != null && parents.size() > 0) {
            result.forEach(n -> n.setLevel(n.getLevel() + 1));

            result.add(0, ConceptTreeNode.fromConcept(parents.get(0)));

            parents = getParents(result.get(0).getIri());
        }

        return result;
    }

    @Override
    public List<Concept> getCodeSchemes() throws SQLException {
        return getConceptsByRelationObject("subtypeOf", "CodeScheme");
    }


    private List<Concept> getConceptsByRelationObject(String relation, String object) throws SQLException{
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
