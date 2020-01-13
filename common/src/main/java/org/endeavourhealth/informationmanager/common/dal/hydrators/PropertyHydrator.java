package org.endeavourhealth.informationmanager.common.dal.hydrators;

import org.endeavourhealth.informationmanager.common.dal.DALHelper;
import org.endeavourhealth.informationmanager.common.models.Property;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PropertyHydrator {
    public static List<Property> createList(ResultSet rs) throws SQLException {
        List<Property> result = new ArrayList<>();

        while (rs.next())
            result.add(create(rs));

        return result;
    }

    public static Property create(ResultSet rs) throws SQLException {
        Property property = new Property();
        return populate(rs, property);
    }

    public static Property populate(ResultSet rs, Property property) throws SQLException {
        property
            .setGroup(rs.getInt("group"))
            .setProperty(rs.getString("Property"))
            .setData(rs.getString("data"))
            .setObject(rs.getString("object"))
            .setMinCardinality(rs.getInt("minCardinality"))
            .setMaxCardinality(rs.getInt("maxCardinality"))
            .setInferred(rs.getBoolean("inferred"));

        return property;
    }

}
