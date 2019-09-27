package org.endeavourhealth.informationmanager.common.dal;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.models.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

class ConceptHydrator {
    public static ConceptSummary createSummary(ResultSet resultSet) throws SQLException {
        return populate(new ConceptSummary(), resultSet);
    }

    public static ConceptSummary populate(ConceptSummary conceptSummary, ResultSet resultSet) throws SQLException {
        return conceptSummary
            .setId(resultSet.getString("id"))
            .setName(resultSet.getString("name"))
            .setScheme(resultSet.getString("scheme"))
            .setCode(resultSet.getString("code"))
            .setStatus(resultSet.getString("status"))
            .setUpdated(resultSet.getTimestamp("updated"));
    }

    public static Concept create(ResultSet resultSet) throws SQLException, IOException {
        return populate(new Concept(), resultSet);
    }
    public static Concept populate(Concept concept, ResultSet resultSet) throws SQLException, IOException {
        return concept
            .setConcept(ObjectMapperPool.getInstance().readTree(resultSet.getString("data")));
    }

    public static JsonNode createDefinition(ResultSet resultSet) throws SQLException, IOException {
        return ObjectMapperPool.getInstance().readTree(resultSet.getString("data"));
    }

    public static PropertyDomain createPropertyDomain(ResultSet resultSet) throws SQLException {
        return populate(new PropertyDomain(), resultSet);
    }
    public static PropertyDomain populate(PropertyDomain propertyDomain, ResultSet resultSet) throws SQLException {
        propertyDomain
            .setStatus(resultSet.getString("status"))
            .addDomain(createDomain(resultSet));

        while (resultSet.next())
            propertyDomain
                .addDomain(createDomain(resultSet));

        return propertyDomain;
    }

    public static Domain createDomain(ResultSet resultSet) throws SQLException {
        return populate(new Domain(), resultSet);
    }
    public static Domain populate(Domain domain, ResultSet resultSet) throws SQLException {
        return domain
            .setProperty(resultSet.getString("property"))
            .setMinimum(resultSet.getInt("minimum"))
            .setMaximum(resultSet.getInt("maximum"));
    }

    public static JsonNode createPropertyRange(ResultSet resultSet) throws SQLException, IOException {
        return ObjectMapperPool.getInstance().readTree(resultSet.getString("data"));
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
        return model
            .setIri(resultSet.getString("iri"))
            .setVersion(Version.fromString(resultSet.getString("version")));
    }
}
