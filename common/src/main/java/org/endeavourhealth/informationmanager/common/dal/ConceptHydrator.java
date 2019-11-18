package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.models.document.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ConceptHydrator {
    public static Concept createConcept(ResultSet resultSet) throws SQLException {
        return populate(new Concept(), resultSet);
    }

    public static Concept populate(Concept c, ResultSet resultSet) throws SQLException {
            c.setId(resultSet.getString("id"));
            c.setName(resultSet.getString("name"));
            c.setDescription(resultSet.getString("description"));
            c.setCodeScheme(resultSet.getString("scheme_id"));
            c.setCode(resultSet.getString("code"));
            c.setStatus(resultSet.getString("status_id"));
            return c;
    }

    public static FullConcept create(ResultSet resultSet) throws SQLException, IOException {
        return populate(new FullConcept(), resultSet);
    }
    public static FullConcept populate(FullConcept concept, ResultSet resultSet) throws SQLException, IOException {
        return concept
            .setModel(resultSet.getString("model"))
            .setConcept(createConcept(resultSet))
            ;
    }

/*    public static JsonNode createDefinition(ResultSet resultSet) throws SQLException, IOException {
        if (resultSet.next())
            return ObjectMapperPool.getInstance().readTree(resultSet.getString("data"));
        else
            return null;
    }*/

    public static PropertyDomain createPropertyDomain(ResultSet resultSet) throws SQLException, IOException {
        return populate(new PropertyDomain(), resultSet);
    }
    public static PropertyDomain populate(PropertyDomain propertyDomain, ResultSet resultSet) throws SQLException, IOException {
        propertyDomain.setProperty(resultSet.getString("property"));
        propertyDomain.setStatus(resultSet.getString("status"));
        propertyDomain.getDomain().add(createDomain(resultSet));

        while (resultSet.next())
            propertyDomain.getDomain().add(createDomain(resultSet));

        return propertyDomain;
    }

    public static Domain createDomain(ResultSet resultSet) throws SQLException, IOException {
        return populate(new Domain(), resultSet);
    }
    public static Domain populate(Domain domain, ResultSet resultSet) throws SQLException, IOException {
        domain.setOperator(Operator.byValue(resultSet.getShort("operator")));
        domain.setClazz(resultSet.getString("property"));
        domain.setMinCardinality(resultSet.getInt("minimum"));
        domain.setMaxCardinality(resultSet.getInt("maximum"));
        domain.setMaxInGroup(resultSet.getInt("max_in_group"));
        return domain;
    }

    public static List<PropertyRange> createPropertyRange(ResultSet resultSet) throws SQLException, IOException {
        List<PropertyRange> ranges = new ArrayList<>();

        String property = "";
        PropertyRange pr = null;
        while (resultSet.next()) {
            if (!property.equals(resultSet.getString("property"))) {
                property = resultSet.getString("property");
                pr = new PropertyRange()
                    .setProperty(property)
                    .setStatus(resultSet.getString("status"));
                ranges.add(pr);
            }

            SimpleExpressionConstraint exp = new SimpleExpressionConstraint()
                .setName(resultSet.getString("name"));

            pr.getRangeClass().add(exp);
        }

        return ranges;
    }

    public static DraftConcept createDraft(ResultSet resultSet) throws SQLException {
        return populate(new DraftConcept(), resultSet);
    }
    public static DraftConcept populate(DraftConcept draftConcept, ResultSet resultSet) throws SQLException {
        return draftConcept
            .setId(resultSet.getString("id"))
            .setName(resultSet.getString("name"))
            .setPublished(resultSet.getString("published"))
            .setUpdated(resultSet.getTimestamp("updated"));
    }

    public static Model createModel(ResultSet rs) throws SQLException {
        return populate(new Model(), rs);
    }

    public static Model populate(Model model, ResultSet resultSet) throws SQLException {
        model.setIri(resultSet.getString("iri"));
        model.setVersion(resultSet.getString("version"));
        return model;
    }
}
