package org.endeavourhealth.informationmanager.common.dal;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.models.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            .setModel(resultSet.getString("model"))
            .setConcept(ObjectMapperPool.getInstance().readTree(resultSet.getString("data")));
    }

    public static List<ConceptDefinition> createDefinition(ResultSet resultSet) throws SQLException, IOException {
        return populate(new ArrayList<ConceptDefinition>(), resultSet);
    }

    public static List<ConceptDefinition> populate(List<ConceptDefinition> definitions, ResultSet resultSet) throws SQLException, IOException {
        while (resultSet.next()) {
            definitions.add(populate(new ConceptDefinition(), resultSet));
        }
        return definitions;
    }

    public static ConceptDefinition populate(ConceptDefinition definition, ResultSet resultSet) throws SQLException, IOException {
        definition.setExpression(ObjectMapperPool.getInstance().readTree(resultSet.getString("data")));

        switch(resultSet.getInt("type")) {
            case 0: return definition.setType("subtypeOf");
            case 1: return definition.setType("equivalentTo");
            case 2: return definition.setType("mappedTo");
            case 3: return definition.setType("replacedBy");
            default: return definition;
        }
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
