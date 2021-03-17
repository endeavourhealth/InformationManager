package org.endeavourhealth.informationmanager;

import com.google.common.base.Strings;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;

import java.sql.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class TTDocumentFilerJDBCDAL implements TTDocumentFilerDAL {

   private final Map<String, Integer> namespaceMap = new HashMap<>();
   private final Map<String, String> prefixMap = new HashMap<>();
   private final Map<String, Integer> conceptMap = new HashMap<>(1000000);
   private final List<TTIriRef> classify = new ArrayList<>();
   private Integer graph;
   private final Connection conn;

   private final PreparedStatement getNamespace;
   private final PreparedStatement getNsFromPrefix;
   private final PreparedStatement insertNamespace;
   private final PreparedStatement getConceptDbId;
   private final PreparedStatement deleteStatements;
   private final PreparedStatement insertConcept;
   private final PreparedStatement updateConcept;
   private final PreparedStatement insertStatement;
   private final PreparedStatement insertIsa;
   private final PreparedStatement deleteIsa;
   private final PreparedStatement insertTerm;
   private final PreparedStatement getTermDbId;


   public TTDocumentFilerJDBCDAL(boolean noDelete) throws Exception {
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
      getConceptDbId = conn.prepareStatement("SELECT id FROM concept WHERE iri = ?");
      insertConcept = conn.prepareStatement("INSERT INTO concept"
          + " (iri,model_type, name, description, code, scheme, status) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

      updateConcept = conn.prepareStatement("UPDATE concept SET iri= ? ,model_type=?," +
          " name = ?, description = ?, code = ?, scheme = ?, status = ? WHERE id = ?");
      deleteStatements= conn.prepareStatement("DELETE FROM statement WHERE subject=? and "
      +"graph= ?");
      insertStatement= conn.prepareStatement("INSERT INTO statement "+
          "(subject,graph,subject_blank,predicate,node_type,"+
          "object,data_type,literal,info) VALUES(?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
      insertIsa= conn.prepareStatement("INSERT classification set child=? ,parent=? ,graph=?,type=?");
      deleteIsa= conn.prepareStatement("DELETE FROM classification WHERE child=? AND"
          +" graph=? AND type=?");
      insertTerm = conn.prepareStatement("INSERT INTO concept_term SET concept=?, term=?,code=?");
      getTermDbId = conn.prepareStatement("SELECT dbid from concept_term\n"+
          "WHERE term =? and concept=? and code=?");

      classify.add(IM.IS_A);
      classify.add(IM.IS_CHILD_OF);
      classify.add(IM.IS_CONTAINED_IN);


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
   public void fileConcept(TTConcept concept) throws SQLException, DataFormatException {
      String iri = concept.getIri();
      Map<TTIriRef, TTValue> preds= concept.getPredicateMap();
      TTValue modelType= preds.get(IM.MODELTYPE);
      TTValue label = preds.get(RDFS.LABEL);
      TTValue comment= preds.get(RDFS.LABEL);
      TTValue code= preds.get(IM.CODE);
      TTValue scheme= preds.get(IM.HAS_SCHEME);
      TTValue status= preds.get(IM.STATUS);
      Integer conceptId= getConceptId(iri);
      conceptId= upsertConcept(conceptId,
          expand(iri),
          modelType!=null ? modelType.asIriRef().getIri():null,
          label!=null ? label.asLiteral().getValue() : null,
          comment!=null ? comment.asLiteral().getValue() : null,
          code!=null ? code.asLiteral().getValue() : null,
          scheme!= null ? scheme.asIriRef().getIri() : null,
          status!= null ? status.asIriRef().getIri() : null);

      deleteStatements(conceptId);
      fileStatements(concept,conceptId,null);
      fileClassification(concept,conceptId);
   }

   private void fileClassification(TTConcept concept, Integer conceptId) throws DataFormatException, SQLException {

      Integer child = conceptId;
      for (TTIriRef isaType : classify) {
         if (concept.get(isaType) != null) {
            int i = 0;
            Integer isaId = getOrSetConceptId(isaType.getIri());
            DALHelper.setInt(deleteIsa, ++i, child);
            DALHelper.setInt(deleteIsa, ++i, graph);
            DALHelper.setInt(deleteIsa, ++i, isaId);
            deleteIsa.executeUpdate();

            TTArray isas = concept.get(isaType).asArray();
            for (TTValue parent : isas.getElements()) {
               i = 0;
               DALHelper.setInt(insertIsa, ++i, child);
               DALHelper.setInt(insertIsa, ++i, getOrSetConceptId(parent.asIriRef().getIri()));
               DALHelper.setInt(insertIsa, ++i, graph);
               DALHelper.setInt(insertIsa, ++i, isaId);
               if (insertIsa.executeUpdate() == 0)
                  throw new SQLException("Unable to insert concept tree with [" +
                      concept.getIri() + " isa " + parent.asIriRef().getIri());
            }
         }
      }
   }


   private void deleteStatements(Integer conceptId) throws SQLException {
      try {
         int i = 0;
         DALHelper.setInt(deleteStatements, ++i, conceptId);
         DALHelper.setInt(deleteStatements, ++i, graph);
         deleteStatements.executeUpdate();
      } catch (SQLException e) {
         throw (e);
      }
   }

   private void fileStatements(TTNode node, Integer conceptId, Integer subjectBlank) throws SQLException {

      HashMap<TTIriRef, TTValue> predicates= node.getPredicateMap();
      if (predicates==null)
         return;

         predicates.forEach((s, v) -> {
            try {
               if (s.equals(IM.HAS_SYNONYM))
                  fileTerm(conceptId,v.asNode());
               else
                  fileStatement(conceptId, s, v, subjectBlank);
            } catch (DataFormatException e) {
               e.printStackTrace();
            } catch (SQLException throwables) {
               throwables.printStackTrace();
            }
         });

   }

   private void fileStatement(Integer conceptId, TTIriRef predicate, TTValue value,Integer subject_blank) throws DataFormatException, SQLException {
      int i = 0;
      Integer object = null;
      Integer dataType = null;
      String literal = null;
      String info = null;
      Integer predicateId=null;
      if (predicate!=null) {
         predicateId= getOrSetConceptId(predicate.getIri());
      }
      TTValueType nodeType;
      if (value.isIriRef()) {
         nodeType = TTValueType.IRIREF;
         object = getOrSetConceptId(value.asIriRef().getIri());
      } else if (value.isLiteral()) {
         nodeType = TTValueType.LITERAL;
         literal = value.asLiteral().getValue();
         if (literal.length() > 255) {
            info = literal;
            literal = null;
         }
         if (value.asLiteral().getType() != null)
            dataType = getOrSetConceptId(value.asLiteral().getType().getIri());
      } else if (value.isList())
         nodeType = TTValueType.LIST;
      else if (value.isNode())
         nodeType = TTValueType.NODE;
      else
         throw new DataFormatException("unknown node type");

      try {
         DALHelper.setInt(insertStatement, ++i, conceptId);
         DALHelper.setInt(insertStatement, ++i, graph);
         DALHelper.setInt(insertStatement, ++i, subject_blank);
         DALHelper.setInt(insertStatement, ++i, predicateId);
         DALHelper.setByte(insertStatement, ++i, nodeType.getValue());
         DALHelper.setInt(insertStatement, ++i, object);
         DALHelper.setInt(insertStatement, ++i, dataType);
         DALHelper.setString(insertStatement, ++i, literal);
         DALHelper.setString(insertStatement, ++i, info);
         insertStatement.executeUpdate();
         Integer statementId = DALHelper.getGeneratedKey(insertStatement);
         if (nodeType == TTValueType.NODE)
            fileStatements(value.asNode(), conceptId, statementId);
         else if (nodeType == TTValueType.LIST) {
            fileList(conceptId, value.asArray(), statementId);
         }

      } catch (SQLException e){
         throw (e);
      }

   }

   private void fileList(Integer conceptId, TTArray value, Integer statementId) throws SQLException, DataFormatException {
      for (TTValue element : value.asArray().getElements()) {
         if (element.isLiteral())
            fileStatement(conceptId, null, element, statementId);
         else if (element.isNode())
            fileStatements(element.asNode(), conceptId, statementId);
         else if (element.isIriRef())
            fileStatement(conceptId, null, element, statementId);
         else
            fileList(conceptId, element.asArray(), statementId);
      }
   }


   @Override
   public void fileIndividual(TTConcept indi) throws SQLException, DataFormatException {
      fileConcept(indi);

   }







   private Integer getConceptId(String iri) throws SQLException {
      if (Strings.isNullOrEmpty(iri))
         return null;
      iri= expand(iri);
      Integer id = conceptMap.get(iri);
      if (id == null) {
         DALHelper.setString(getConceptDbId, 1, iri);
         try (ResultSet rs = getConceptDbId.executeQuery()) {
            if (rs.next()) {
               conceptMap.put(iri, rs.getInt("id"));
               return rs.getInt("id");
            } else {
               return null;
            }
         }
      }
      return id;
   }

   // ------------------------------ Concept ------------------------------
   private Integer getOrSetConceptId(String iri) throws SQLException, DataFormatException {
      if (Strings.isNullOrEmpty(iri))
         return null;
      iri= expand(iri);
      Integer id = conceptMap.get(iri);
      if (id == null) {
         DALHelper.setString(getConceptDbId, 1, iri);
         try (ResultSet rs = getConceptDbId.executeQuery()) {
            if (rs.next()) {
               conceptMap.put(iri, rs.getInt("id"));
               return rs.getInt("id");
            } else {
               id= upsertConcept(null,iri, OWL.CLASS.getIri(),
                   null,null,null,null,IM.DRAFT.getIri());
               conceptMap.put(iri,id);
               return id;
            }
         }
      }
      return id;
   }
   private Integer upsertConcept(Integer id, String iri, String type, String name,
                                 String description, String code, String scheme,
                                 String  status) throws DataFormatException, SQLException {

      try {
         if (id == null) {
            // Insert
            int i=0;
            DALHelper.setString(insertConcept, ++i, iri);
            DALHelper.setString(insertConcept, ++i, type);
            DALHelper.setString(insertConcept, ++i, name);
            DALHelper.setString(insertConcept, ++i, description);
            DALHelper.setString(insertConcept, ++i, code);
            DALHelper.setString(insertConcept, ++i, scheme);
            DALHelper.setString(insertConcept, ++i, status);

            if (insertConcept.executeUpdate() == 0)
               throw new SQLException("Failed to insert concept [" + iri + "]");
            else {
               id = DALHelper.getGeneratedKey(insertConcept);
               return id;
            }
         } else {
            //update
            int i = 0;
            DALHelper.setString(updateConcept, ++i, iri);
            DALHelper.setString(updateConcept, ++i, type);
            DALHelper.setString(updateConcept, ++i, name);
            DALHelper.setString(updateConcept, ++i, description);
            DALHelper.setString(updateConcept, ++i, code);
            DALHelper.setString(updateConcept, ++i, scheme);
            DALHelper.setString(updateConcept, ++i, status);
            DALHelper.setInt(updateConcept,++i,id);

            if (updateConcept.executeUpdate() == 0) {
               throw new SQLException("Failed to update concept [" + iri + "]");
            } else
               return id;
         }
      }
      catch (Exception e){
         System.err.println(iri+" wont file for some reason");
         throw (e);
      }
   }

   public Integer getGraph() {
      return graph;
   }

   public void setGraph(String graphIri) throws SQLException, DataFormatException {
      DALHelper.setString(getConceptDbId, 1, graphIri);
      try (ResultSet rs = getConceptDbId.executeQuery()) {
         if (rs.next()) {
               graph= rs.getInt("id");
         } else {
               graph= upsertConcept(null,graphIri,IM.GRAPH.getIri(),
                   null,null,null,null,IM.DRAFT.getIri());
            }
         }
      }

   private void fileTerm(Integer conceptId, TTNode termCode) throws SQLException {
      int i = 0;
      String term= termCode.get(RDFS.LABEL).asLiteral().getValue();
      String code= termCode.get(IM.CODE).asLiteral().getValue();
      if (term.length() > 100)
         term = term.substring(0, 100);

      if (conceptId == null)
         throw new IllegalArgumentException("Concept does not exist in database for " + term);

      DALHelper.setString(getTermDbId, ++i, term);
      DALHelper.setInt(getTermDbId, ++i, conceptId);
      DALHelper.setString(getTermDbId, ++i, code);
      try (ResultSet rs = getTermDbId.executeQuery()) {
         if (!rs.next()) {
            i = 0;
            DALHelper.setInt(insertTerm, ++i, conceptId);
            DALHelper.setString(insertTerm, ++i, term);
            DALHelper.setString(insertTerm, ++i, code);
            if (insertTerm.executeUpdate() == 0)
               throw new SQLException("Failed to save term code for  ["
                   + term+" "
                   + code+ "]");
         }
      }

   }
   private String expand(String iri) {
      int colonPos = iri.indexOf(":");
      String prefix = iri.substring(0, colonPos);
      String path = prefixMap.get(prefix);
      if (path == null)
         return iri;
      else
         return path + iri.substring(colonPos + 1);
   }

}
