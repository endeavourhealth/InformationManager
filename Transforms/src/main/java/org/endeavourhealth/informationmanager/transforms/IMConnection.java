package org.endeavourhealth.informationmanager.transforms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class IMConnection {
   public static Connection getConnection() throws ClassNotFoundException, SQLException {
      Map<String, String> envVars = System.getenv();

      String url = envVars.get("CONFIG_JDBC_URL");
      String user = envVars.get("CONFIG_JDBC_USERNAME");
      String pass = envVars.get("CONFIG_JDBC_PASSWORD");
      String driver = envVars.get("CONFIG_JDBC_CLASS");

      if (url == null || url.isEmpty()
          || user == null || user.isEmpty()
          || pass == null || pass.isEmpty())
         throw new IllegalStateException("You need to set the CONFIG_JDBC_ environment variables!");

      if (driver != null && !driver.isEmpty())
         Class.forName(driver);

      Properties props = new Properties();

      props.setProperty("user", user);
      props.setProperty("password", pass);

      return DriverManager.getConnection(url, props);
   }
}
