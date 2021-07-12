package org.endeavourhealth.informationmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Static methods for dropping and creating indexes for a bulk information model filer with empty database
 */
public class BulkFilerJDBC {

	/**
	 * Drops the indexes and constraints prior to a large IM bulk refresh
	 * @throws SQLException
	 */
	public static void dropIndexes() throws SQLException{
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = TTDocumentFilerJDBC.getConnection();
			String[] statements = {"SET FOREIGN_KEY_CHECKS=0;",
				"ALTER TABLE tpl DROP FOREIGN KEY tpl_sub_fk;",
				"ALTER TABLE tpl DROP FOREIGN KEY tpl_pred_fk;",
				"ALTER TABLE tpl DROP FOREIGN KEY tpl_ob_fk;",
				"DROP INDEX ct_c_t on entity_type;",
				"DROP INDEX ct_t_c on entity_type;",
				"DROP INDEX entity_scheme_code_uq on entity;",
				"DROP INDEX entity_updated_idx on entity;",
				"DROP INDEX entity_name_idx on entity;",
				"DROP INDEX ct_tcs_idx on term_code;",
				"DROP INDEX ct_cs_idx on term_code;",
				"DROP INDEX tpl_pred_sub_idx on tpl;",
				"DROP INDEX tpl_pred_oc_idx on tpl;",
				"DROP INDEX tpl_sub_pred_obj on tpl ;",
				"DROP INDEX tpl_ob_pred_sub on tpl ;",
				" DROP INDEX tpl_l_pred_sub on tpl;"};
			System.out.println("Dropping indexes");
			statement = conn.prepareStatement(statements[0]);
			for (String sql : statements)
				statement.executeUpdate(sql);

		} catch (Exception e){
			e.printStackTrace();
		}
		finally {
			if (statement!=null)
			statement.close();
			if (conn!=null)
			conn.close();
		}
	}

	/**
	 * Recreates the indexes following a bulk file of the information model
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */

	public static void createIndexes() throws SQLException, ClassNotFoundException {
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = TTDocumentFilerJDBC.getConnection();
			String[] statements = {"create INDEX ct_c_t on entity_type(entity ASC, type ASC);",
				"create INDEX ct_t_c on entity_type(type ASC, entity ASC);",
				"create UNIQUE INDEX entity_scheme_code_uq on entity (scheme ASC, code ASC);",
				"create INDEX entity_updated_idx on entity(updated ASC) ;",
				"create index entity_name_idx on entity(name ASC) ;",
				"CREATE INDEX ct_tcs_idx on term_code(term,entity ASC);",
				"create INDEX ct_cs_idx on term_code(code,scheme,entity);",
				"create INDEX tpl_pred_sub_idx on tpl(predicate ASC,subject ASC,blank_node);",
				"create INDEX tpl_pred_oc_idx on tpl (predicate ASC,object ASC) ;",
				"create INDEX tpl_sub_pred_obj on tpl (subject ASC, predicate, object,blank_node);",
				"create INDEX tpl_ob_pred_sub on tpl (object ASC, predicate,subject,blank_node);",
				"create INDEX tpl_l_pred_sub on tpl (literal(50) ASC, predicate,subject,blank_node);" ,
				"ALTER TABLE tpl ADD CONSTRAINT tpl_sub_fk\n"+
				"FOREIGN KEY (subject)\n"+
					"REFERENCES entity (dbid) ON DELETE CASCADE ON UPDATE NO ACTION;",
				"ALTER TABLE tpl ADD CONSTRAINT tpl_pred_fk\n"+
					"FOREIGN KEY (subject)\n"+
					"REFERENCES entity (dbid) ON DELETE CASCADE ON UPDATE NO ACTION;",
				"ALTER TABLE tpl ADD CONSTRAINT tpl_ob_fk\n"+
					"FOREIGN KEY (subject)\n"+
					"REFERENCES entity (dbid) ON DELETE CASCADE ON UPDATE NO ACTION;",
					"SET FOREIGN_KEY_CHECKS=1;"};
			System.out.println("Adding indexes back in");
			statement = conn.prepareStatement(statements[0]);
			for (String sql : statements)
				statement.executeUpdate(sql);
		} finally {
			if (statement!=null)
			statement.close();
			if (conn!=null)
			conn.close();
		}
	}


}


