package org.endeavourhealth.informationmanager.common.dal.hydrators;

import org.endeavourhealth.informationmanager.common.models.Supertype;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupertypeHydrator {
    public static List<Supertype> createList(ResultSet rs) throws SQLException {
        List<Supertype> result = new ArrayList<>();

        while (rs.next())
            result.add(create(rs));

        return result;
    }

    public static Supertype create(ResultSet rs) throws SQLException {
        Supertype supertype = new Supertype();
        return populate(rs, supertype);
    }

    public static Supertype populate(ResultSet rs, Supertype supertype) throws SQLException {
        return supertype
            .setSupertype(rs.getString("supertype"))
            .setInferred(rs.getBoolean("inferred"));
    }

}
