package org.endeavourhealth.informationmodel.api.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.common.config.ConfigManager;
import org.endeavourhealth.informationmodel.api.framework.GenericCache;
import org.endeavourhealth.informationmodel.api.framework.TimedGenericCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;

public class ConnectionPool extends GenericCache<Connection> {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionPool.class);
    private static final int VALID_TIMEOUT = 5;
    private static ConnectionPool _instance = new ConnectionPool();

    @Override
    protected boolean isValid(Connection connection) {
        try {
            if (connection == null || connection.isClosed())
                return false;

            if (connection.isValid(VALID_TIMEOUT))
                return true;

            connection.close();
            return false;
        } catch (SQLException e) {
            LOG.error("Error validating/cleaning up connection", e);
            return false;
        }
    }

    @Override
    protected Connection create() {
        try {
            JsonNode json = ConfigManager.getConfigurationAsJson("database");
            String url = json.get("url").asText();
            String user = json.get("username").asText();
            String pass = json.get("password").asText();
            String driver = json.get("class") == null ? null : json.get("class").asText();

            if (driver != null && !driver.isEmpty())
                Class.forName(driver);

            Properties props = new Properties();

            props.setProperty("user", user);
            props.setProperty("password", pass);

            Connection connection = DriverManager.getConnection(url, props);

            LOG.debug("New DB Connection created");

            return connection;
        } catch (Exception e) {
            LOG.error("Error getting connection", e);
        }
        return null;
    }

    public static Connection aquire() {
        return  _instance.pop();
    }

    public static void release(Connection connection) {
        _instance.push(connection);
    }
}
