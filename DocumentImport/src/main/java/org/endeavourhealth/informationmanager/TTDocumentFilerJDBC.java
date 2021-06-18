package org.endeavourhealth.informationmanager;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class TTDocumentFilerJDBC implements TTDocumentFilerDAL {

   private final Map<String, Integer> namespaceMap = new HashMap<>();
   private final Map<String, String> prefixMap = new HashMap<>();
   private final Map<String, Integer> entityMap = new HashMap<>(1000000);
   private TTIriRef graph;
   private final Connection conn;
   private TTEntityFilerJDBC entityFiler;

   private final PreparedStatement getNamespace;
   private final PreparedStatement getNsFromPrefix;
   private final PreparedStatement insertNamespace;




   public TTDocumentFilerJDBC(TTIriRef graph) throws Exception {
      this.graph= graph;
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


      getNamespace = conn.prepareStatement("SELECT * FROM namespace WHERE iri = ?");
      getNsFromPrefix = conn.prepareStatement("SELECT * FROM namespace WHERE prefix = ?");
      insertNamespace = conn.prepareStatement("INSERT INTO namespace (iri, prefix,name) VALUES (?, ?,?)", Statement.RETURN_GENERATED_KEYS);
      entityFiler = new TTEntityFilerJDBC(conn,entityMap,prefixMap);


   }


   @Override
   public void startTransaction() throws SQLException {
      conn.setAutoCommit(false);
   }

   @Override
   public void commit() throws SQLException {
      conn.commit();
   }

   @Override
   public void close() throws SQLException {
      conn.close();

   }

   @Override
   public void rollBack() throws SQLException {
      conn.rollback();

   }

   @Override
   public void upsertNamespace(TTPrefix ns) throws SQLException {

      DALHelper.setString(getNamespace, 1, ns.getIri());
      try (ResultSet rs = getNamespace.executeQuery()) {
         if (rs.next()) {
            if (!ns.getPrefix().equals(rs.getString("prefix"))) {
               throw new SQLException("prefix in database -> " + ns.getPrefix() + " does not match the iri " + ns.getIri());
            } else {
               prefixMap.put(ns.getPrefix(),ns.getIri());
               namespaceMap.put(ns.getPrefix(), rs.getInt("dbid"));
               namespaceMap.put(ns.getIri(), rs.getInt("dbid"));
            }
         } else {
            DALHelper.setString(getNsFromPrefix, 1, ns.getPrefix());
            ResultSet ps = getNsFromPrefix.executeQuery();
            if (ps.next()) {
               if (!ns.getIri().equals(ps.getString("iri"))) {
                  throw new SQLException(ps.getString("prefix") + "->" + ps.getString("iri) "
                      + " does not match " + ns.getIri() + ns.getIri()));
               }
            } else {
               DALHelper.setString(insertNamespace, 1, ns.getIri());
               DALHelper.setString(insertNamespace, 2, ns.getPrefix());
               DALHelper.setString(insertNamespace, 3, null);
               insertNamespace.executeUpdate();
               Integer dbid = DALHelper.getGeneratedKey(insertNamespace);
               prefixMap.put(ns.getPrefix(), ns.getIri());
               namespaceMap.put(ns.getIri(), dbid);
               namespaceMap.put(ns.getPrefix(), dbid);
            }
         }
      } catch (SQLException e) {
         throw new SQLException(e.toString() + " problem adding namespace " +
             ns.getPrefix() + ns.getIri());
      }
   }


   @Override
   public void fileEntity(TTEntity entity) throws SQLException, DataFormatException, IOException {
      entityFiler.fileEntity(entity,graph);

   }



   @Override
   public void setGraph(TTIriRef graph) {
      this.graph= graph;
   }



}
