package org.endeavourhealth.informationmanager.common.dal.hydrators;

import org.endeavourhealth.informationmanager.common.dal.DALHelper;
import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.transform.model.Concept;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConceptHydrator {
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
            .setIri(DALHelper.getString(rs, "iri"))
            .setStatus(ConceptStatus.byValue(rs.getByte("status")))
            .setName(DALHelper.getString(rs, "name"))
            .setDescription(DALHelper.getString(rs, "description"))
            .setScheme(DALHelper.getString(rs, "scheme"))
            .setCode(DALHelper.getString(rs, "code"));
    }


    // RELATED CONCEPTS
    public static List<RelatedConcept> createRelatedConceptList(ResultSet rs) throws SQLException {
        List<RelatedConcept> result = new ArrayList<>();
        while (rs.next())
            result.add(createRelatedConcept(rs));

        return result;
    }

    public static RelatedConcept createRelatedConcept(ResultSet rs) throws SQLException {
        return populate(new RelatedConcept(), rs);
    }

    public static RelatedConcept populate(RelatedConcept c, ResultSet rs) throws SQLException {
        return c
            .setMinCardinality(DALHelper.getInt(rs, "minCardinality"))
            .setMaxCardinality(DALHelper.getInt(rs, "maxCardinality"))
            .setRelationship(new Concept()
                .setIri(rs.getString("r_iri"))
                .setName(rs.getString("r_name"))
            )
            .setConcept(
                new Concept()
                    .setIri(rs.getString("c_iri"))
                    .setName(rs.getString("c_name"))
            );
    }


    // PROPERTIES
    public static List<Property> createPropertyList(ResultSet rs) throws SQLException {
        List<Property> result = new ArrayList<>();
        while (rs.next())
            result.add(createProperty(rs));

        return result;
    }

    public static Property createProperty(ResultSet rs) throws SQLException {
        return populate(new Property(), rs);
    }

    public static Property populate(Property c, ResultSet rs) throws SQLException {
        return c
            .setProperty(new Concept(rs.getString("p_iri"), rs.getString("p_name")))
            .setMinCardinality(rs.getInt("min_cardinality"))
            .setMaxCardinality(rs.getInt("max_cardinality"))
            .setValueType(new Concept(rs.getString("v_iri"), rs.getString("v_name")))
            .setLevel(rs.getInt("level"))
            .setOwner(new Concept(rs.getString("o_iri"), rs.getString("o_name")));
    }

/*


    public static List<SimpleConcept> createConceptDefinitionList(ResultSet rs) throws SQLException {
        List<SimpleConcept> result = new ArrayList<>();
        while (rs.next())
            result.add(createConceptDefinition(rs));

        return result;
    }

    public static SimpleConcept createConceptDefinition(ResultSet rs) throws SQLException {
        return populate(new SimpleConcept(), rs);
    }

    public static SimpleConcept populate(SimpleConcept simpleConcept, ResultSet rs) throws SQLException {
        return simpleConcept
            .setId(rs.getInt("id"))
            .setConcept(rs.getString("iri"))
            .setInferred(rs.getBoolean("inferred"))
            .setOperator(rs.getString("operator"));
    }

    public static List<RoleGroup> createRoleGroupList(ResultSet rs) throws  SQLException {
        List<RoleGroup> result = new ArrayList<>();
        int previousGroup = -1;
        RoleGroup roleGroup = null;
        while (rs.next()) {
            int group = rs.getInt("group");

            if (group != previousGroup) {
                previousGroup = group;
                roleGroup = new RoleGroup().setGroup(group);
                result.add(roleGroup);
            }

            roleGroup.addProperty(createPropertyDefinition(rs));
        }

        return result;
    }

    public static PropertyDefinition createPropertyDefinition(ResultSet rs) throws SQLException {
        return populate(new PropertyDefinition(), rs);
    }

    public static PropertyDefinition populate(PropertyDefinition propertyDefinition, ResultSet rs) throws SQLException {
        propertyDefinition
            .setId(rs.getInt("id"))
            .setProperty(rs.getString("property"))
            .setMinCardinality(DALHelper.getInt(rs, "minCardinality"))
            .setMaxCardinality(DALHelper.getInt(rs, "maxCardinality"))
            .setInferred(rs.getBoolean("inferred"))
            .setOperator(rs.getString("operator"));

        if (rs.getString("object") != null)
            propertyDefinition.setObject(rs.getString("object"));
        else
            propertyDefinition.setData(rs.getString("data"));

        return propertyDefinition;
    }

    public static Collection<PropertyRange> createPropertyRangeList(ResultSet rs) throws SQLException {
        List<PropertyRange> result = new ArrayList<>();
        while (rs.next()) {
            result.add(createPropertyRange(rs));
        }
        return result;
    }

    public static PropertyRange createPropertyRange(ResultSet rs) throws SQLException {
        return populate(new PropertyRange(), rs);
    }

    public static PropertyRange populate(PropertyRange propertyRange, ResultSet rs) throws SQLException {
        return propertyRange
            .setId(rs.getInt("id"))
            .setRange(rs.getString("range"))
            .setSubsumption(rs.getString("subsumption"))
            .setOperator(rs.getInt("operator"));

    }

    public static Collection<PropertyDomain> createPropertyDomainList(ResultSet rs) throws SQLException {
        List<PropertyDomain> result = new ArrayList<>();
        while (rs.next()) {
            result.add(createPropertyDomain(rs));
        }
        return result;
    }

    public static PropertyDomain createPropertyDomain(ResultSet rs) throws SQLException {
        return populate(new PropertyDomain(), rs);
    }

    public static PropertyDomain populate(PropertyDomain propertyDomain, ResultSet rs) throws SQLException {
        return propertyDomain
            .setId(rs.getInt("id"))
            .setDomain(rs.getString("domain"))
            .setInGroup(DALHelper.getInt(rs, "ingroup"))
            .setDisjointGroup(rs.getBoolean("disjointgroup"))
            .setMinCardinality(DALHelper.getInt(rs, "minCardinality"))
            .setMaxCardinality(DALHelper.getInt(rs, "maxCardinality"))
            .setMinInGroup(DALHelper.getInt(rs, "minInGroup"))
            .setMaxInGroup(DALHelper.getInt(rs, "maxInGroup"))
            .setOperator(rs.getInt("operator"));
    }

    public static Collection<String> createPropertyChainList(ResultSet rs) throws SQLException {
        List<String> result = new ArrayList<>();
        while (rs.next()) {
            result.add(rs.getString("linkProperty"));
        }
        return result;
    }

    public static Namespace createNamespace(ResultSet rs) throws SQLException {
        return populate(new Namespace(), rs);
    }

    public static Namespace populate(Namespace namespace, ResultSet resultSet) throws SQLException {
        namespace
            .setIri(resultSet.getString("iri"))
            // .setName(resultSet.getString("name"))
            .setPrefix(resultSet.getString("prefix"))
            // .setCodePrefix(resultSet.getString("codePrefix"))
            ;

        return namespace;
    }

*/
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

*//*

*/
/*    public static JsonNode createDefinition(ResultSet resultSet) throws SQLException, IOException {
        if (resultSet.next())
            return ObjectMapperPool.getInstance().readTree(resultSet.getString("data"));
        else
            return null;
    }*//*
*/
/*


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
