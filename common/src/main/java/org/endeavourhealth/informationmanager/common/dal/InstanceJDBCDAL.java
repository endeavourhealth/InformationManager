package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.Instance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstanceJDBCDAL implements InstanceDAL {
    @Override
    public List<Instance> getInstances() throws SQLException {
        List<Instance> result = new ArrayList<>();
        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT dbid, name, url FROM im_instance")) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.add(new Instance()
                    .setDbid(rs.getInt("dbid"))
                    .setName(rs.getString("name"))
                    .setUrl(rs.getString("url"))
                );
            }

        } finally {
            ConnectionPool.getInstance().push(conn);
        }
        return result;
    }

    @Override
    public Instance getInstance(int instanceDbid) throws SQLException {
        Connection conn = ConnectionPool.getInstance().pop();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT dbid, name, url FROM im_instance WHERE dbid = ?")) {
            stmt.setInt(1, instanceDbid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Instance()
                    .setDbid(rs.getInt("dbid"))
                    .setName(rs.getString("name"))
                    .setUrl(rs.getString("url"));
            } else {
                return null;
            }

        } finally {
            ConnectionPool.getInstance().push(conn);
        }
    }

}
