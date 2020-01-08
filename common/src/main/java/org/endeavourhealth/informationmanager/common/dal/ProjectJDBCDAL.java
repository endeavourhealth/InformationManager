package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjectJDBCDAL extends BaseJDBCDAL implements ProjectDAL {
    @Override
    public List<Project> getProjects() throws Exception {
        List<Project> result = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM project ORDER BY name");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(new Project()
                .setDbid(rs.getInt("dbid"))
                .setName(rs.getString("name"))
                .setBrief(rs.getString("brief"))
                .setDescription(rs.getString("description")
                ));
            }
        }

        return result;
    }
}
