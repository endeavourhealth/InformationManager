package org.endeavourhealth.informationmanager.common.dal.hydrators;

import org.endeavourhealth.informationmanager.common.models.Axiom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AxiomHydrator {
    public static List<Axiom> createList(ResultSet rs) throws SQLException {
        List<Axiom> result = new ArrayList<>();

        while (rs.next())
            result.add(create(rs));

        return result;
    }

    public static Axiom create(ResultSet rs) throws SQLException {
        Axiom axiom = new Axiom();
        return populate(rs, axiom);
    }

    public static Axiom populate(ResultSet rs, Axiom axiom) throws SQLException {
        return axiom
            .setToken(rs.getString("token"))
            .setTransitive(rs.getBoolean("transitive"));
    }
}
