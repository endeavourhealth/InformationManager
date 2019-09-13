package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;

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
            .setScheme(resultSet.getString("scheme_id"))
            .setCode(resultSet.getString("code"))
            .setStatus(resultSet.getString("status_id"))
            .setUpdated(resultSet.getTimestamp("updated"));
    }

    public static Concept create(ResultSet resultSet) throws SQLException {
        return populate(new Concept(), resultSet);
    }
    public static Concept populate(Concept concept, ResultSet resultSet) throws SQLException {
        populate((ConceptSummary)concept, resultSet);
        return concept
            .setDocument(resultSet.getInt("document"))
            .setDescription(resultSet.getString("description"))
            .setRevision(resultSet.getInt("revision"))
            .setPublished(Version.fromString(resultSet.getString("published")));
    }

    public static ConceptProperty createProperty(ResultSet resultSet) throws SQLException {
        return populate(new ConceptProperty(), resultSet);
    }
    public static ConceptProperty populate(ConceptProperty conceptProperty, ResultSet resultSet) throws SQLException {
        conceptProperty.setProperty(resultSet.getString("property"));
        conceptProperty.setValue(resultSet.getString("value"));
        conceptProperty.setConcept(resultSet.getString("concept"));
        conceptProperty.setInherits(resultSet.getString("inherits"));

        return conceptProperty;
    }

    public static ConceptDomain createDomain(ResultSet resultSet) throws SQLException {
        return populate(new ConceptDomain(), resultSet);
    }
    public static ConceptDomain populate(ConceptDomain conceptDomain, ResultSet resultSet) throws SQLException {
        return conceptDomain
            .setProperty(resultSet.getString("property"))
            .setMinimum(resultSet.getInt("minimum"))
            .setMaximum(resultSet.getInt("maximum"))
            .setInherits(resultSet.getString("inherits"));
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
}
