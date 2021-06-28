package org.endeavourhealth.informationmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * Updates the denormalised triple table with a flag against the functional properties so that the triple tree knows
 * When something is an array or single object/literal
 */
public class FunctionalFlaggerJDBC {
	private PreparedStatement functionalUpdate;
	private Connection conn;

	FunctionalFlaggerJDBC() throws SQLException, ClassNotFoundException {


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

		conn = DriverManager.getConnection(url, props);
		functionalUpdate= conn.prepareStatement("UPDATE tpl\n" +
			"SET functional=1\n" +
			"where tpl.dbid in (\n" +
			"\tselect dbid from \n" +
			"\t(\n" +
			"\t\tselect tpl.dbid\n" +
			"\t\tfrom tpl\n" +
			"\t\tjoin (\n" +
			"\t\t\t\tSelect e.dbid\n" +
			"\t\t\t\tfrom entity e\n" +
			"\t\t\t\tjoin entity_type t on t.entity= e.dbid\n" +
			"\t\t\t\twhere t.type='http://www.w3.org/2002/07/owl#FunctionalProperty') as fp\n" +
			"\t\ton tpl.predicate= fp.dbid\n" +
			"\t) as tmp\n" +
			"    )");
	}

	public void setFunctionalFlags() throws SQLException {
		functionalUpdate.executeUpdate();
	}
}
