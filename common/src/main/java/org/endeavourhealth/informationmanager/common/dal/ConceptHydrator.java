package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ConceptHydrator {
    public static List<Concept> createConceptList(ResultSet rs) throws SQLException {
        List<Concept> result = new ArrayList<>();
        while (rs.next())
            result.add(createConcept(rs));

        return result;
    }

    public static Concept createConcept(ResultSet rs) throws SQLException {
        return populate(new Concept(), rs);
    }

    public static Concept populate(Concept c, ResultSet rs) throws SQLException {
        return c
            .setId(DALHelper.getString(rs, "id"))
            .setName(DALHelper.getString(rs, "name"))
            .setDescription(DALHelper.getString(rs, "description"))
            .setCodeScheme(DALHelper.getString(rs, "codeScheme"))
            .setCode(DALHelper.getString(rs, "code"))
            .setStatus(DALHelper.getString(rs, "status"))
            .setUpdated(DALHelper.getDate(rs, "updated"))
            .setWeighting(DALHelper.getInt(rs, "weighting"))
            .setModel(new Model()
                .setIri(DALHelper.getString(rs, "iri"))
                .setVersion(DALHelper.getString(rs, "version"))
            );
    }

    public static List<ConceptRelation> createConceptRelations(ResultSet rs) throws SQLException {
        List<ConceptRelation> relations = new ArrayList<>();

        return populate(relations, rs);
    }

    public static List<ConceptRelation> populate(List<ConceptRelation> relations, ResultSet rs) throws SQLException {
        while (rs.next()) {
            ConceptRelation rel = new ConceptRelation()
                .setSubject(DALHelper.getString(rs, "subject"))
                .setGroup(DALHelper.getInt(rs, "group"))
                .setRelation(DALHelper.getString(rs, "relation"))
                .setObject(DALHelper.getString(rs, "object"));

            if (DALHelper.getInt(rs, "cardDbid") != null) {
                rel.setCardinality(createConceptRelationCardinality(rs));
            }

            if (DALHelper.getInt(rs, "dataDbid") != null) {
                rel.setValue(createConceptPropertyData(rs)
                );
            }

            relations.add(rel);
        }

        return relations;
    }

    public static ConceptRelationCardinality createConceptRelationCardinality(ResultSet rs) throws SQLException {
        return populate(new ConceptRelationCardinality(), rs);
    }

    public static ConceptRelationCardinality populate(ConceptRelationCardinality relationCardinality, ResultSet rs) throws SQLException {
        return relationCardinality
            .setMinCardinality(DALHelper.getInt(rs, "minCardinality"))
            .setMaxCardinality(DALHelper.getInt(rs, "maxCardinality"))
            .setMaxInGroup(DALHelper.getInt(rs, "maxInGroup"));
    }

    public static ConceptPropertyData createConceptPropertyData(ResultSet rs) throws SQLException {
        return populate(new ConceptPropertyData(), rs);
    }

    public static ConceptPropertyData populate(ConceptPropertyData propertyData, ResultSet rs) throws SQLException {
        return propertyData
            .setValue(DALHelper.getString(rs, "value"))
            .setConcept(DALHelper.getString(rs, "concept"));
    }

    public static Model createModel(ResultSet rs) throws SQLException {
        return populate(new Model(), rs);
    }

    public static Model populate(Model model, ResultSet resultSet) throws SQLException {
        model.setIri(resultSet.getString("iri"));
        model.setVersion(resultSet.getString("version"));
        return model;
    }

/*











    public static FullConcept create(ResultSet resultSet) throws SQLException, IOException {
        return populate(new FullConcept(), resultSet);
    }
    public static FullConcept populate(FullConcept concept, ResultSet resultSet) throws SQLException, IOException {
        return concept
            .setModel(resultSet.getString("model"))
            .setConcept(createConcept(resultSet))
            ;
    }

*/
/*    public static JsonNode createDefinition(ResultSet resultSet) throws SQLException, IOException {
        if (resultSet.next())
            return ObjectMapperPool.getInstance().readTree(resultSet.getString("data"));
        else
            return null;
    }*//*


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


*/

}
