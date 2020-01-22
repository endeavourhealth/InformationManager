package org.endeavourhealth.informationmanager.common.dal.hydrators;

import org.endeavourhealth.informationmanager.common.models.Definition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AxiomHydrator {
    public static List<Definition> createList(ResultSet rs) throws SQLException {
        List<Definition> result = new ArrayList<>();

        while (rs.next())
            result.add(create(rs));

        return result;
    }

    public static Definition create(ResultSet rs) throws SQLException {
        Definition definition = new Definition();
        return populate(rs, definition);
    }

    public static Definition populate(ResultSet rs, Definition definition) throws SQLException {
        return definition
            .setAxiom(rs.getString("token"))
            .setTransitive(rs.getBoolean("transitive"));
    }
}
