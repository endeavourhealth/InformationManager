package org.endeavourhealth.informationmanager;

import java.sql.SQLException;

public class FunctionalFlagger {
	private final FunctionalFlaggerJDBC dal;

	public FunctionalFlagger() throws SQLException, ClassNotFoundException {
		this.dal = new FunctionalFlaggerJDBC();
	}


	public void setFunctionalFlags() throws SQLException {
		System.out.println("Updating functional property flags in triple table");
		dal.setFunctionalFlags();
	}
}
