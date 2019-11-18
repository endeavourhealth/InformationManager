package org.endeavourhealth.informationmanager.common.dal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.models.document.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InformationManagerJDBCDAL extends BaseJDBCDAL implements InformationManagerDAL {
    private static final Logger LOG = LoggerFactory.getLogger(InformationManagerJDBCDAL.class);
    private Map<String, Integer> conceptIdCache = new HashMap<>();

    @Override
    public Integer getOrCreateModelDbid(String modelIri, Version modelVersion) throws Exception {
        String sql = "SELECT dbid FROM model WHERE iri = ? AND version = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setString(stmt, 1, modelIri);
            DALHelper.setString(stmt, 2, modelVersion.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt("dbid");
            }
        }

        sql = "INSERT INTO model (iri, version) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            DALHelper.setString(stmt, 1, modelIri);
            DALHelper.setString(stmt, 2, modelVersion.toString());
            stmt.executeUpdate();
            return DALHelper.getGeneratedKey(stmt);
        }
    }

    @Override
    public Integer getModelDbid(String modelPath) throws SQLException {
        String sql = "SELECT dbid FROM model WHERE iri = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setString(stmt, 1, modelPath);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt("dbid");
                else
                    return null;
            }
        }
    }

    // ********** Document Import methods **********
    @Override
    public Integer getDocumentDbid(UUID documentId) throws SQLException {
        String sql = "SELECT dbid\n" +
            "FROM document\n" +
            "WHERE id = ?\n";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setString(stmt, 1, documentId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt("dbid");
                else
                    return null;
            }
        }
    }

    @Override
    public Integer createDocument(DocumentInfo documentInfo) throws SQLException {
        String sql = "INSERT INTO document (id, iri, model, base_model_version, effective_date, purpose, publisher, target_model_version)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 1;
            DALHelper.setString(stmt, i++, documentInfo.getDocumentId().toString());
            DALHelper.setString(stmt, i++, documentInfo.getDocumentIri().toString());
            DALHelper.setInt(stmt, i++, getModelDbid(documentInfo.getModelIri().toString()));
            DALHelper.setVersion(stmt, i++, documentInfo.getBaseModelVersion());
            DALHelper.setTimestamp(stmt, i++, documentInfo.getEffectiveDate());
            // DALHelper.setInt(stmt, i++, getModelDbid(document.getDocumentStatus()));
            DALHelper.setString(stmt, i++, documentInfo.getPurpose());
            DALHelper.setInt(stmt, i++, getModelDbid(documentInfo.getPublisher()));
            DALHelper.setVersion(stmt, i++, documentInfo.getTargetVersion());
            stmt.executeUpdate();
            return DALHelper.getGeneratedKey(stmt);
        }
    }

    @Override
    public Integer getConceptDbid(String id) throws SQLException {
        if (id == null)
            LOG.warn("Concept ID is null");

        Integer dbid = conceptIdCache.get(id);

        if (dbid == null) {

            try (PreparedStatement statement = conn.prepareStatement("SELECT dbid FROM concept WHERE id = ?")) {
                statement.setString(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        dbid = rs.getInt("dbid");
                        conceptIdCache.put(id, dbid);
                    }
                }
            }
        }

        return dbid;
    }

    @Override
    public void upsertConcept(int modelDbid, Concept concept) throws SQLException {
        String sql = "INSERT INTO concept (model, id, name, description, scheme, code, status)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "model = VALUES(model), id = VALUES(id), name = VALUES(name), description = VALUES(description), scheme = VALUES(scheme), code = VALUES(code), status = VALUES(status)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            DALHelper.setInt(stmt, i++, modelDbid);
            DALHelper.setString(stmt, i++, concept.getId());
            DALHelper.setString(stmt, i++, concept.getName());
            DALHelper.setString(stmt, i++, concept.getDescription());
            DALHelper.setInt(stmt, i++, getConceptDbid(concept.getCodeScheme()));
            DALHelper.setString(stmt, i++, concept.getCode());
            DALHelper.setInt(stmt, i++, getConceptDbid(concept.getStatus()));
            stmt.executeUpdate();
        }
    }

    @Override
    public void upsertConceptDefinition(ConceptDefinition definition) throws Exception {
        String conceptId = definition.getDefinitionOf();
        int conceptDbid = getConceptDbid(conceptId);

        for(ConceptExpression exp: definition.getSubtypeOf())
            upsertConceptDefinition(conceptDbid, DefinitionType.SUBTYPE_OF, exp);

        for(ConceptExpression exp: definition.getEquivalentTo())
            upsertConceptDefinition(conceptDbid, DefinitionType.EQUIVALENT_TO, exp);

        upsertConceptDefinition(conceptDbid, DefinitionType.TERM_CODE_OF, definition.getTermCodeOf());
        upsertConceptDefinitionConcept(conceptDbid, DefinitionType.INVERSE_PROPERTY_OF, definition.getInversePropertyOf(), null, null);

        for(String map: definition.getMappedTo())
            upsertConceptDefinition(conceptDbid, DefinitionType.MAPPED_TO, map);

        upsertConceptDefinition(conceptDbid, DefinitionType.REPLACED_BY, definition.getReplacedBy());

        upsertConceptDefinition(conceptDbid, DefinitionType.CHILD_OF, definition.getChildOf());

        for (RoleGroup roleGroup: definition.getRoleGroup())
            upsertConceptDefinitionRoleGroupAttribute(conceptDbid, DefinitionType.ROLE_GROUP, roleGroup);

        for(ConceptExpression exp: definition.getDisjointWith())
            upsertConceptDefinition(conceptDbid, DefinitionType.DISJOINT_WITH, exp);
    }

    private void upsertConceptDefinition(int conceptDbid, DefinitionType type, String concept) throws SQLException {
        if (concept == null || concept.isEmpty())
            return;

        upsertConceptDefinitionConcept(conceptDbid, type, concept, null, null);
    }

    private void upsertConceptDefinition(int conceptDbid, DefinitionType type, ConceptExpression expression) throws SQLException {
        if (expression == null)
            return;

        upsertConceptDefinitionConcept(conceptDbid, type, expression.getConcept(), expression.getName(), expression.getOperator());
    }

    private void upsertConceptDefinitionConcept(int conceptDbid, DefinitionType type, String concept, String name, Operator operator) throws SQLException {
        if (concept == null || concept.isEmpty())
            return;

        String sql = "INSERT INTO concept_definition_concept (concept, type, name, operator, concept_value) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            Integer expressionConcept = getConceptDbid(concept);

            if (expressionConcept == null)
                throw new IllegalStateException("Unknown expression concept [" + concept + "]");

            Integer operatorId = (operator == null) ? null : operator.ordinal();

            DALHelper.setInt(stmt, i++, conceptDbid);
            DALHelper.setShort(stmt, i++, type.getValue());
            DALHelper.setString(stmt, i++, name);
            DALHelper.setInt(stmt, i++, operatorId);
            DALHelper.setInt(stmt, i++, expressionConcept);
            stmt.executeUpdate();
        }
    }

    private void upsertConceptDefinitionRoleGroupAttribute(int conceptDbid, DefinitionType type, RoleGroup roleGroup) throws SQLException {
        String sql = "INSERT INTO concept_definition_role_group (concept, role_group, operator, value, value_concept) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (AttributeExpression att: roleGroup.getAttribute()) {
                int i = 1;
                DALHelper.setInt(stmt, i++, conceptDbid);
                DALHelper.setInt(stmt, i++, conceptDbid);
                DALHelper.setInt(stmt, i++, att.getOperator().ordinal());
                DALHelper.setString(stmt, i++, att.getValue());
                DALHelper.setInt(stmt, i++, getConceptDbid(att.getValueConcept()));
                stmt.executeUpdate();
            }
        }
    }

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
/*        String sql = "INSERT INTO property_range (property, status, range_class)\n" +
            "VALUES (?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "property = VALUES(property), status = VALUES(status), range_class = VALUES(range_class)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            DALHelper.setInt(stmt, 1, propertyDbid);
            DALHelper.setInt(stmt, 2, statusDbid);
            DALHelper.setString(stmt, 3, rangeClassJson);
            stmt.executeUpdate();
        }*/
    }

    // ********** Manager UI methods **********

    @Override
    public SearchResult getMRU(Integer size) throws Exception {
        SearchResult result = new SearchResult();

        if (size == null)
            size = 15;

        String sql = "SELECT c.model, c.id, c.name, c.description, c.scheme, c.code, c.status, c.updated\n" +
            "FROM concept c\n" +
            "ORDER BY updated DESC\n" +
            "LIMIT ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, size);
            try (ResultSet resultSet = statement.executeQuery()) {
                result.setResults(getConceptSummariesFromResultSet(resultSet));
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
    public List<Model> getModels() throws SQLException {
        List<Model> result = new ArrayList<>();

        String sql = "SELECT iri, version FROM model";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    result.add(ConceptHydrator.createModel(rs));
            }
        }

        return result;
    }

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

                    switch (DefinitionType.byValue(definitionType)) {
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

    @Override
    public SearchResult search(String terms, Integer size, Integer page, List<Integer> documents, String relationship, String target) throws Exception {
        page = (page == null) ? 0 : page;       // Default page to 1
        size = (size == null) ? 15 : size;      // Default page size to 15
        int offset = page * size;         // Calculate offset from page & size

        SearchResult result = new SearchResult()
            .setPage(page);

        String sql = "SELECT SQL_CALC_FOUND_ROWS u.*, st.id AS status_id, s.id AS scheme_id\n" +
            "FROM (\n" +
            "SELECT c.dbid, c.model, c.id, c.name, c.description, c.scheme, c.code, c.status, c.updated, 3 AS priority, LENGTH(c.name) as len\n" +
            "FROM concept c\n" +
            "WHERE MATCH (name) AGAINST (? IN BOOLEAN MODE)\n" +
            "UNION\n" +
            "SELECT c.dbid, c.model, c.id, c.name, c.description, c.scheme, c.code, c.status, c.updated, 2 AS priority, LENGTH(c.code) as len\n" +
            "FROM concept c\n" +
            "WHERE code LIKE ?\n" +
            "UNION\n" +
            "SELECT c.dbid, c.model, c.id, c.name, c.description, c.scheme, c.code, c.status, c.updated, 1 AS priority, LENGTH(c.id) as len\n" +
            "FROM concept c\n" +
            "WHERE id LIKE ?\n" +
            ") AS u\n" +
            "LEFT JOIN concept st ON st.dbid = u.status\n" +
            "LEFT JOIN concept s ON s.dbid = u.scheme\n";

        if (relationship != null && target != null)
            sql += "JOIN concept_property o ON o.dbid = u.dbid\n" +
                "JOIN concept p ON p.dbid = o.property AND p.id = ?\n" +
                "JOIN concept v ON v.dbid = o.concept AND v.id = ?\n";

        if (documents != null && documents.size() > 0) {
            sql += "WHERE u.document IN (" + DALHelper.inListParams(documents.size()) + ")\n";
        }

        sql += "ORDER BY priority ASC, len ASC, dbid\n" +
            "LIMIT ?,?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                int i = 1;
                statement.setString(i++, toBoolean(terms));
                statement.setString(i++, terms.trim() + '%');
                statement.setString(i++, terms.trim() + '%');

                if (relationship != null && target != null) {
                    statement.setString(i++, relationship);
                    statement.setString(i++, target);
                }

                if (documents != null && documents.size() > 0) {
                    for (Integer docDbid : documents)
                        statement.setInt(i++, docDbid);
                }

                statement.setInt(i++, offset);
                statement.setInt(i++, size);

                try (ResultSet resultSet = statement.executeQuery()) {
                    result.setResults(getConceptSummariesFromResultSet(resultSet));
                }
            }

            try (PreparedStatement statement = conn.prepareStatement("SELECT FOUND_ROWS();");
                 ResultSet rs = statement.executeQuery()) {
                rs.next();
                result.setCount(rs.getInt(1));
            }

        return result;
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



/*    public FullConcept getConcept(String code_scheme, String code) throws SQLException, IOException {
        String sql = "SELECT c.*, s.id AS scheme_id FROM concept c JOIN concept s ON s.dbid = c.scheme WHERE s.id = ? AND c.code = ?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, code_scheme);
                statement.setString(2, code);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        FullConcept concept = ConceptHydrator.create(resultSet);

*//*
                        concept.setDefinition(getConceptDefinition(concept.getId()));
                        concept.setPropertyDomain(getPropertyDomain(concept.getId()));
                        concept.setPropertyRange(getPropertyRange(concept.getId()));
*//*

                        return concept;
                    }
                }
            }
        return null;
    }*/

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
    public byte[] getDocumentLatestPublished(Integer dbid) throws Exception {
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
    public List<IdNamePair> getSchemes() throws Exception {
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
    public List<ConceptTreeNode> getChildren(String conceptId) throws Exception {
        List<ConceptTreeNode> result = new ArrayList<>();

        String sql = "SELECT c.id, c.name\n" +
            "FROM concept_definition d\n" +
            "JOIN concept c ON c.dbid = d.concept\n" +
            "WHERE d.subtype = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, conceptId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new ConceptTreeNode()
                    .setId(rs.getString("id"))
                    .setName(rs.getString("name"))
                );
            }
        }

        return result;
    }

    @Override
    public void publishDocument(int dbid, String level) throws SQLException, IOException {
/*        LOG.debug("Publishing document...");
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
            publishConceptDocument(doc);*/
    }
    private void publishTermMaps(DocumentInfo doc) throws SQLException, IOException {
/*        String sql = "SELECT t.term, typ.id as type, tgt.id as target\n" +
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
        }*/
    }
    private void publishConceptDocument(DocumentInfo doc) throws SQLException, IOException {
/*        LOG.debug("Analysing document...");
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
        LOG.debug("Document publish finished");*/
    }
    private void publishDocumentToArchive(DocumentInfo doc, String docJson) throws IOException, SQLException {
/*        // Insert new doc
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
        }*/
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
/*
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
*/

        return newConcept;
    }

/*
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
*/
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
/*
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
*/

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
    public String getConceptName(String id) throws SQLException {
        String sql = "SELECT name FROM concept WHERE id = ?\n";

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
    public String validateIds(List<String> ids) throws Exception {
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
/*
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
*/
        return created;
    }
}
