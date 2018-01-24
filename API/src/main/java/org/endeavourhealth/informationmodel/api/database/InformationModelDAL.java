package org.endeavourhealth.informationmodel.api.database;

import org.endeavourhealth.informationmodel.api.models.*;
import org.endeavourhealth.informationmodel.api.models.Class;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InformationModelDAL {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionPool.class);
    private static final Integer RETRY_LIMIT = 10;

    // Common
    private Long getNextId(String tableName) {
        Long nextId = null;
        Connection conn = ConnectionPool.aquire();
        try {
            Integer retries = 0;
            while (nextId == null && retries++ < RETRY_LIMIT) {
                nextId = selectNextId(conn, tableName);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return nextId;
    }

    private Long selectNextId(Connection conn, String tableName) {
        String select = "SELECT next_id FROM table_identity WHERE table_name = ?";

        try (PreparedStatement statement = conn.prepareStatement(select)) {
            statement.setString(1, tableName);
            ResultSet res = statement.executeQuery();
            if (res.next())
                return updateNextId(conn, tableName, res.getLong("next_id"));

        } catch (Exception e) {
            LOG.error("Error saving attribute primitive value", e);
        }

        return null;
    }

    private Long updateNextId(Connection conn, String tableName, Long nextId) {
        String update = "UPDATE table_identity SET next_id = next_id + 1 WHERE table_name = ? AND next_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(update)) {
            statement.setString(1, tableName);
            statement.setLong(2, nextId);
            if (statement.executeUpdate() == 1)
                return nextId;
        } catch (Exception e) {
            LOG.error("Error saving attribute primitive value", e);
        }
        return null;
    }

    // Attributes
    public void addAttributeConceptValue(AttributeConceptValue attributeConceptValue) {
        Connection conn = ConnectionPool.aquire();
        try {
            String sql =
                "INSERT INTO attribute_concept_value " +
                    "(source_concept, attribute_concept, value_concept) " +
                    "VALUES " +
                    "(?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, attributeConceptValue.getConceptId());
                statement.setLong(2, attributeConceptValue.getAttributeId());
                statement.setLong(3, attributeConceptValue.getValueId());
            } catch (SQLException e) {
                LOG.error("Error saving attribute concept value", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
    }

    public List<Concept> getAttributeConceptValues(Long conceptId, Long attributeId) {
        List<Concept> attributeConceptValues = new ArrayList<>();

        Connection conn = ConnectionPool.aquire();
        try {
            String sql =
                "SELECT c.* " +
                "FROM attribute_concept_value acv "+
                "JOIN concept c ON c.id = acv.value_concept " +
                "WHERE source_concept = ? " +
                "AND attribute_concept = ?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, conceptId);
                statement.setLong(2, attributeId);
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    attributeConceptValues.add( new Concept()
                        .setId(res.getLong("id"))
                        .setName(res.getString("name"))
                        .setContextName(res.getString("context_name"))
                        .setShortName(res.getString("short_name"))
                        .setClazz(res.getLong("class"))
                        .setDescription(res.getString("description"))
                        .setStatus(res.getByte("status"))
                    );
                }
            } catch (Exception e) {
                LOG.error("Error saving attribute primitive value", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return attributeConceptValues;
    }

    public void addAttributePrimitiveValue(AttributePrimitiveValue attributePrimitiveValue) {
        Connection conn = ConnectionPool.aquire();
        try {
            String sql =
                "INSERT INTO attribute_primitive_value " +
                    "(source_concept, attribute_concept, value, value_type) " +
                    "VALUES " +
                    "(?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, attributePrimitiveValue.getConceptId());
                statement.setLong(2, attributePrimitiveValue.getAttributeId());
                statement.setString(3, attributePrimitiveValue.getValue().toString());
                statement.setString(4, attributePrimitiveValue.getValue().getClass().getTypeName());
            } catch (Exception e) {
                LOG.error("Error saving attribute primitive value", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
    }

    public List<AttributePrimitiveValue> getPrimitiveValueAttributes(Long conceptId, Long attributeId) {
        List<AttributePrimitiveValue> attributePrimitiveValues = new ArrayList<>();

        Connection conn = ConnectionPool.aquire();
        try {
            String sql =
                "SELECT * " +
                    "FROM attribute_primitive_value "+
                    "WHERE source_concept = ? " +
                    "AND attribute_concept = ?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, conceptId);
                statement.setLong(2, attributeId);
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    attributePrimitiveValues.add( new AttributePrimitiveValue()
                        .setConceptId(res.getLong("source_concept"))
                        .setAttributeId(res.getLong("attribute_concept"))
                        .setValue(res.getObject("value"))
                    );
                }
            } catch (Exception e) {
                LOG.error("Error getting attribute primitive value", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return attributePrimitiveValues;
    }

    // Concepts
    public void addConceptValueRange(ConceptValueRange conceptValueRange) {
        Connection conn = ConnectionPool.aquire();
        try {
            String sql =
                "INSERT INTO concept_value_range " +
                    "(source_concept, qualifier_concept, operator, minimum, maximum) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, conceptValueRange.getConceptId());
                statement.setLong(2, conceptValueRange.getQualifierId());
                statement.setString(3, conceptValueRange.getOperator());
                statement.setLong(4, conceptValueRange.getMinimum());
                statement.setLong(5, conceptValueRange.getMaximum());
            } catch (Exception e) {
                LOG.error("Error saving concept value range", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
    }

    public Concept saveConcept(Concept concept) {
        String sql;
        // NOTE : ID Field must go last for correct parameter ordering (due to UPDATE statement)
        if (concept.getId() == null) {
            concept.setId(getNextId("concept"));
            sql = "INSERT INTO concept (name, description, context_name, short_name, class, description, status, id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE concept SET name = ?, description = ?, context_name = ?, short_name = ?, class = ?, description = ?, status = ? " +
                "WHERE id = ?";
        }

        Connection conn = ConnectionPool.aquire();
        try {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, concept.getName());
                statement.setString(2, concept.getContextName());
                statement.setString(3, concept.getShortName());
                statement.setLong(4, concept.getClazz());
                statement.setString(5, concept.getDescription());
                statement.setString(6, concept.getDescription());
                statement.setLong(7, concept.getId());
            } catch (Exception e) {
                LOG.error("Error saving concept", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }

        return concept;
    }

    public Concept getConcept(Long conceptId) {
        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT * FROM concept WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, conceptId);
                ResultSet res = statement.executeQuery();
                if (res.next()) {
                    return new Concept()
                        .setId(res.getLong("id"))
                        .setName(res.getString("name"))
                        .setContextName(res.getString("context_name"))
                        .setShortName(res.getString("short_name"))
                        .setClazz(res.getLong("class"))
                        .setDescription(res.getString("description"))
                        .setStatus(res.getByte("status"));
                }
            } catch (Exception e) {
                LOG.error("Error getting concept", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return null;
    }


    public List<Concept> listConcepts(List<Long> classIds, Integer page, Integer size, String filter) {
        List<Concept> concepts = new ArrayList<>();
        Integer catIndex = 0;

        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT * FROM concept WHERE class IN (" + String.join(",", Collections.nCopies(classIds.size(), "?")) + ")";
            if (filter != null) sql += " AND name LIKE ?";
            sql += " LIMIT ? OFFSET ?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                int i = 1;
                for (Long classId : classIds)
                    statement.setLong(i++, classId);
                if (filter != null) statement.setString(i++, "%" + filter + "%");
                statement.setInt(i++, size);
                statement.setInt(i++, (page - 1) * size);

                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    concepts.add(new Concept()
                        .setId(res.getLong("id"))
                        .setName(res.getString("name"))
                        .setContextName(res.getString("context_name"))
                        .setShortName(res.getString("short_name"))
                        .setClazz(res.getLong("class"))
                        .setDescription(res.getString("description"))
                        .setStatus(res.getByte("status"))
                    );
                }
            } catch (Exception e) {
                LOG.error("Error getting concept", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }

        return concepts;
    }

    public Integer countConcepts(List<Long> classIds, String filter) {
        Integer count = 0;
        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT Count(*) FROM concept WHERE class IN (" + String.join(",", Collections.nCopies(classIds.size(), "?")) + ")";
            if (filter != null) sql += " AND name LIKE ?";

            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                int i = 1;
                for (Long classId : classIds)
                    statement.setLong(i++, classId);
                if (filter != null) statement.setString(i++, "%" + filter + "%");

                ResultSet res = statement.executeQuery();
                if (res.next())
                    count = res.getInt(1);
            } catch (Exception e) {
                LOG.error("Error getting concept", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }

        return count;
    }

    public List<Concept> searchConcepts(String term, Long classId) {
        // TODO : CONFIRM - Search by which fields?
        List<Concept> concepts = new ArrayList<>();

        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT * FROM concept WHERE LOWER(name) LIKE ?";
            if (classId != null)
                sql += " AND class = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, "%"+term.toLowerCase()+"%");
                if (classId != null)
                    statement.setLong(1, classId);
                ResultSet res = statement.executeQuery();
                while (res.next()) {
                    concepts.add(new Concept()
                        .setId(res.getLong("id"))
                        .setName(res.getString("name"))
                        .setContextName(res.getString("context_name"))
                        .setShortName(res.getString("shortname"))
                        .setClazz(res.getLong("class"))
                        .setDescription(res.getString("description"))
                        .setStatus(res.getByte("status"))
                    );
                }
            } catch (Exception e) {
                LOG.error("Error searching concepts", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return concepts;
    }

    public List<ConceptValueRange> loadConceptValueRanges(Long conceptId, Long qualifierId) {
        List<ConceptValueRange> ranges = new ArrayList<>();
        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT * FROM concept_value_range WHERE source_concept = ?";
            if (qualifierId != null)
                sql += " AND qualifier_concept = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, conceptId);
                if (qualifierId != null)
                    statement.setLong(2, qualifierId);
                ResultSet res = statement.executeQuery();

                while (res.next()) {
                    ranges.add(
                      new ConceptValueRange()
                      .setConceptId(res.getLong("source_concept"))
                      .setQualifierId(res.getLong("qualifier_concept"))
                      .setOperator(res.getString("operator"))
                      .setMinimum(res.getLong("minimum"))
                      .setMaximum(res.getLong("maximum"))
                    );
                }
            } catch (Exception e) {
                LOG.error("Error saving attribute primitive value", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return ranges;
    }

    // Relationships
    public void addRelationship(ConceptRelationship conceptRelationship) {
        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "INSERT INTO concept_relationship " +
                "(source_concept, target_concept, relationship_type, display_order, cardinality) " +
                "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, conceptRelationship.getSourceId());
                statement.setLong(2, conceptRelationship.getTargetId());
                statement.setLong(3, conceptRelationship.getRelationshipId());
                statement.setInt(4, conceptRelationship.getOrder());
                statement.setInt(5, conceptRelationship.getCardinality());
                statement.executeUpdate();
            } catch (Exception e) {
                LOG.error("Error adding relationship", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
    }

    public List<ConceptRelationship> getTargetConcepts(Long conceptId, Long relationshipId) {
        List<ConceptRelationship> relationships = new ArrayList<>();

        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT * FROM concept_relationship " +
                "WHERE source_concept = ?";
            if (relationshipId != null)
                sql += "AND relationship_type = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, conceptId);
                if (relationshipId != null)
                    statement.setLong(2, relationshipId);

                ResultSet res = statement.executeQuery();
                while(res.next()) {
                    relationships.add(new ConceptRelationship()
                        .setSourceId(res.getLong("source_concept"))
                        .setTargetId(res.getLong("target_concept"))
                        .setRelationshipId(res.getLong("relationship_type"))
                        .setOrder(res.getInt("display_order"))
                        .setCardinality(res.getInt("cardinality"))
                    );
                }
            } catch (Exception e) {
                LOG.error("Error adding relationship", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return relationships;
    }

    public List<ConceptRelationship> getRelatedConcepts(Long conceptId, Long relationshipId) {
        List<ConceptRelationship> relationships = new ArrayList<>();

        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT r.*, s.name as source_name, t.name as target_name, n.name as relationship_name " +
                "FROM concept_relationship r " +
                "JOIN concept s ON s.id = r.source_concept " +
                "JOIN concept t ON t.id = r.target_concept " +
                "JOIN concept n ON n.id = r.relationship_type " +
                "WHERE (source_concept = ? OR target_concept = ?) ";
            if (relationshipId != null)
                sql += "AND relationship_type = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, conceptId);
                statement.setLong(2, conceptId);
                if (relationshipId != null)
                    statement.setLong(3, relationshipId);

                ResultSet res = statement.executeQuery();
                while(res.next()) {
                    relationships.add(new ConceptRelationship()
                        .setSourceId(res.getLong("source_concept"))
                        .setTargetId(res.getLong("target_concept"))
                        .setRelationshipId(res.getLong("relationship_type"))
                        .setOrder(res.getInt("display_order"))
                        .setCardinality(res.getInt("cardinality"))
                        .setSourceName(res.getString("source_name"))
                        .setTargetName(res.getString("target_name"))
                        .setRelationshipName(res.getString("relationship_name"))
                    );
                }
            } catch (Exception e) {
                LOG.error("Error adding relationship", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return relationships;
    }

    // Views
    public void addView(View view) {
        Long viewId = getNextId("view");
        Long order = getMaxViewChildOrder(view.getParentId(), view.getRelationshipId()) + 1;

        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "INSERT INTO view " +
                "(id, parent_concept, relationship_concept, child_concept, display_order) " +
                "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, viewId);
                statement.setLong(2, view.getParentId());
                statement.setLong(3, view.getRelationshipId());
                statement.setLong(4, view.getChildId());
                statement.setLong(5, order);
                statement.executeUpdate();
            } catch (Exception e) {
                LOG.error("Error adding relationship", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
    }

    public View getView(Long viewId) {
        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT * FROM view WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, viewId);
                ResultSet res = statement.executeQuery();
                if (res.next()) {
                    return new View()
                        .setViewId(res.getLong("id"))
                        .setParentId(res.getLong("parent_concept"))
                        .setRelationshipId(res.getLong("relationship_concept"))
                        .setChildId(res.getLong("child_concept"))
                        .setOrder(res.getLong("display_order"));
                }
            } catch (Exception e) {
                LOG.error("Error getting concept", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return null;
    }

    public List<Concept> getChildren(Long viewId, Long relationshipId) {
        List<Concept> children = new ArrayList<>();

        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT c.* FROM view v JOIN concept c on c.id = .child_concept WHERE parent_concept = ?";
            if (relationshipId != null)
                sql += "AND relationship_concept = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, viewId);
                if (relationshipId != null)
                    statement.setLong(2, relationshipId);

                ResultSet res = statement.executeQuery();
                while(res.next()) {
                    children.add(new Concept()
                        .setId(res.getLong("id"))
                        .setName(res.getString("name"))
                        .setContextName(res.getString("context_name"))
                        .setShortName(res.getString("short_name"))
                        .setClazz(res.getLong("class"))
                        .setDescription(res.getString("description"))
                        .setStatus(res.getByte("status"))
                    );
                }
            } catch (Exception e) {
                LOG.error("Error adding relationship", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return children;
    }

    public void moveViewConceptUp(Long viewId) {
        View viewConcept = getView(viewId);

        Long order = viewConcept.getOrder();

        if (order == 0)
            return;

        swapOrders(viewConcept.getParentId(), viewConcept.getRelationshipId(), order, order - 1);
    }

    public void moveViewConceptDown(Long viewId) {
        View viewConcept = getView(viewId);
        Long maxOrder = getMaxViewChildOrder(viewConcept.getParentId(), viewConcept.getRelationshipId());

        if (viewConcept.getOrder() == maxOrder)
            return;
        Long order = viewConcept.getOrder();

        if (order == 0)
            return;

        swapOrders(viewConcept.getParentId(), viewConcept.getRelationshipId(), order, order + 1);
    }

    private void swapOrders(Long conceptId, Long relationshipId, Long orderA, Long orderB) {
        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "UPDATE view " +
                "SET display_order = CASE display_order "+
                "   WHEN ? THEN ?" +
                "   WHEN ? THEN ?" +
                "END " +
                "WHERE parent_concept = ? AND relationship_concept = ? AND display_order IN (?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, orderA);
                statement.setLong(2, orderB);
                statement.setLong(3, orderB);
                statement.setLong(4, orderA);
                statement.setLong(5, conceptId);
                statement.setLong(6, relationshipId);
                statement.setLong(7, orderA);
                statement.setLong(8, orderB);
                statement.executeUpdate();
            } catch (Exception e) {
                LOG.error("Error adding relationship", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
    }

    private Long getMaxViewChildOrder(Long conceptId, Long relationshipId) {
        Connection conn = ConnectionPool.aquire();
        try {
            String sql = "SELECT MAX(display_order) FROM view WHERE parent_concept = ? AND relationship_concept = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setLong(1, conceptId);
                statement.setLong(2, relationshipId);
                ResultSet res = statement.executeQuery();
                if (res.next()) {
                    return res.getLong("display_order");
                }
            } catch (Exception e) {
                LOG.error("Error getting concept", e);
            }
        } finally {
            ConnectionPool.release(conn);
        }
        return -1L;
    }

}