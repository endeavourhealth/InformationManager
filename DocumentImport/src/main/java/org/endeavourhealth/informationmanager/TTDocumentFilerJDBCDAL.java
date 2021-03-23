package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
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
   private final PreparedStatement deleteConceptTypes;
   private final PreparedStatement insertConceptType;
   private final PreparedStatement deleteCPO;
   private final PreparedStatement deleteCPD;
   private final PreparedStatement insertConcept;
   private final PreparedStatement updateConcept;
   private final PreparedStatement insertCPO;
   private final PreparedStatement insertCPD;
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
      getConceptDbId = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
      insertConcept = conn.prepareStatement("INSERT INTO concept"
          + " (iri,name, description, code, scheme, status,definition) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

      updateConcept = conn.prepareStatement("UPDATE concept SET iri= ?," +
          " name = ?, description = ?, code = ?, scheme = ?, status = ?, definition = ? WHERE dbid = ?");
      deleteCPO= conn.prepareStatement("DELETE FROM cpo WHERE subject=? and "
      +"graph= ?");
      deleteCPD= conn.prepareStatement("DELETE FROM cpd WHERE subject=? and "
          +"graph= ?");
      deleteConceptTypes= conn.prepareStatement("DELETE FROM concept_type where concept=?");
      insertConceptType= conn.prepareStatement("INSERT INTO concept_type (concept,type) VALUES(?,?)");
      insertCPO= conn.prepareStatement("INSERT INTO cpo "+
          "(subject,graph,group_number,predicate,"+
          "object) VALUES(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
      insertCPD= conn.prepareStatement("INSERT INTO cpd "+
          "(subject,graph,group_number,predicate,"+
          "literal,data_type) VALUES(?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
      insertIsa= conn.prepareStatement("INSERT hierarchy set child=? ,parent=? ,graph=?,isa_type=?");
      deleteIsa= conn.prepareStatement("DELETE FROM hierarchy WHERE child=? AND"
          +" graph=? AND isa_Type=?");
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
   public void fileConcept(TTConcept concept) throws SQLException, DataFormatException, JsonProcessingException {
      String iri = concept.getIri();
      String label= concept.getName();
      String comment= concept.getDescription();
      String code= concept.getCode();
      String scheme= null;
      if (concept.getScheme()!=null)
         scheme= concept.getScheme().getIri();
      String status= null;
      if (concept.getStatus()!=null)
         status= concept.getStatus().getIri();
      Integer conceptId= getConceptId(iri);

      ObjectMapper om = new ObjectMapper();
      om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS,true);
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      String json= om.writeValueAsString(concept);
      conceptId= upsertConcept(conceptId,
          expand(iri),
          label, comment, code,scheme, status,json);

      deleteTypes(conceptId);
      deleteTriples(conceptId);
      fileTypes(concept,conceptId);
      filePropertyGroups(concept,conceptId);
      fileRoleGroups(concept,conceptId);
      fileHierarchy(concept,conceptId);
   }



   private void fileTypes(TTConcept concept, Integer conceptId) throws SQLException {
      if (concept.get(RDF.TYPE)!=null){
         try {
            for (TTValue vtype : concept.get(RDF.TYPE).asArray().getElements()) {
               int i = 0;
               DALHelper.setInt(insertConceptType, ++i, conceptId);
               DALHelper.setString(insertConceptType, ++i, vtype.asIriRef().getIri());
               insertConceptType.executeUpdate();
            }
         } catch (SQLException e){
            throw e;
         }
      }
   }

   private void deleteTypes(Integer conceptId) throws SQLException {
      try {
         DALHelper.setInt(deleteConceptTypes, 1, conceptId);
         deleteConceptTypes.executeUpdate();
      } catch (SQLException e) {
         throw (e);
      }

   }


   private void fileHierarchy(TTConcept concept, Integer conceptId) throws DataFormatException, SQLException {

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


   private void deleteTriples(Integer conceptId) throws SQLException {
      try {
         int i = 0;
         DALHelper.setInt(deleteCPO, ++i, conceptId);
         DALHelper.setInt(deleteCPO, ++i, graph);
         deleteCPO.executeUpdate();
         i=0;
         DALHelper.setInt(deleteCPD, ++i, conceptId);
         DALHelper.setInt(deleteCPD, ++i, graph);
         deleteCPD.executeUpdate();
      } catch (SQLException e) {
         throw (e);
      }
   }

   private void filePropertyGroups(TTConcept concept,Integer conceptId) throws DataFormatException, SQLException {
      if (concept.get(IM.PROPERTY_GROUP)!=null){
         for (TTValue element:concept.get(IM.PROPERTY_GROUP).asArray().getElements()) {
            TTNode propertyGroup = element.asNode();
            Integer groupNumber= 0;
            if (propertyGroup.get(IM.COUNTER)!=null)
               groupNumber= Integer.parseInt(propertyGroup.get(IM.COUNTER).asLiteral().getValue());
            for (TTValue subelement:propertyGroup.get(SHACL.PROPERTY).asArray().getElements()){
               TTNode propvalue= subelement.asNode();
                  TTIriRef targetIri= null;
                  TTIriRef property = propvalue.get(SHACL.PATH).asIriRef();
                  if (propvalue.get(SHACL.CLASS)!=null) {
                     targetIri = propvalue.get(SHACL.CLASS).asIriRef();
                  } else if (propvalue.get(SHACL.DATATYPE)!=null){
                     targetIri= propvalue.get(SHACL.DATATYPE).asIriRef();
                  }
                  String data = null;
                  if (propvalue.get(SHACL.PATTERN)!=null){
                     data= propvalue.get(SHACL.PATTERN).asLiteral().getValue();
                     targetIri= XSD.STRING;
                  } else if (propvalue.get(SHACL.HASVALUE)!=null){
                     data= propvalue.get(SHACL.HASVALUE).asLiteral().getValue();
                     if (propvalue.get(SHACL.HASVALUE).asLiteral().getType()!=null)
                        targetIri= propvalue.get(SHACL.HASVALUE).asLiteral().getType();
                  }
                  fileTriple(conceptId, groupNumber,property,targetIri,data);
               }

         }
      }

   }

   private void fileRoleGroups(TTConcept concept,Integer conceptId) throws DataFormatException, SQLException {
      if (concept.get(IM.ROLE_GROUP) != null) {
         for (TTValue element : concept.get(IM.ROLE_GROUP).asArray().getElements()) {
            TTNode roleGroup = element.asNode();
            Integer groupNumber = Integer.parseInt(roleGroup.get(IM.COUNTER).asLiteral().getValue());
            Map<TTIriRef, TTValue> roles = roleGroup.getPredicateMap();
            for (Map.Entry<TTIriRef, TTValue> role : roles.entrySet())
               fileRole(conceptId,role,groupNumber);
         }
      }
   }

   private void fileRole(Integer conceptId, Map.Entry<TTIriRef, TTValue> role, Integer groupNumber) throws DataFormatException, SQLException {
      TTIriRef property = role.getKey();
      TTValue value = role.getValue();
      TTIriRef targetType = XSD.STRING;
      String data = null;
      if (value.isIriRef())
         targetType = value.asIriRef();
      if (value.isLiteral()) {
         data = value.asLiteral().getValue();
         if (value.asLiteral().getType() != null)
            targetType = value.asLiteral().getType();
      }
      fileTriple(conceptId,groupNumber,property,targetType,data);

   }

   private Integer fileTriple(Integer conceptId, Integer group,
                           TTIriRef predicate, TTIriRef targetType,String data) throws DataFormatException, SQLException {
      int i = 0;

      try {
         if (data==null){
            DALHelper.setInt(insertCPO, ++i, conceptId);
            DALHelper.setInt(insertCPO, ++i, graph);
            DALHelper.setInt(insertCPO, ++i, group);
            DALHelper.setInt(insertCPO, ++i, getOrSetConceptId(predicate.getIri()));
            DALHelper.setInt(insertCPO, ++i, getOrSetConceptId(targetType.getIri()));
            insertCPO.executeUpdate();
            return DALHelper.getGeneratedKey(insertCPO);
         } else {
            DALHelper.setInt(insertCPD, ++i, conceptId);
            DALHelper.setInt(insertCPD, ++i, graph);
            DALHelper.setInt(insertCPD, ++i, group);
            DALHelper.setInt(insertCPD, ++i, getOrSetConceptId(predicate.getIri()));
            DALHelper.setString(insertCPD, ++i, data);
            DALHelper.setInt(insertCPD, ++i, getOrSetConceptId(targetType.getIri()));
            insertCPD.executeUpdate();
            return DALHelper.getGeneratedKey(insertCPD);

         }
      } catch (SQLException e){
         throw (e);
      }

   }



   @Override
   public void fileIndividual(TTConcept indi) throws SQLException, DataFormatException, JsonProcessingException {
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
               conceptMap.put(iri, rs.getInt("dbid"));
               return rs.getInt("dbid");
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
               conceptMap.put(iri, rs.getInt("dbid"));
               return rs.getInt("dbid");
            } else {
               id= upsertConcept(null,iri,
                   null,null,null,null,IM.DRAFT.getIri(),null);
               conceptMap.put(iri,id);
               return id;
            }
         }
      }
      return id;
   }
   private Integer upsertConcept(Integer id, String iri, String name,
                                 String description, String code, String scheme,
                                 String  status,String json) throws DataFormatException, SQLException {

      try {
         if (id == null) {
            // Insert
            int i=0;
            DALHelper.setString(insertConcept, ++i, iri);
            DALHelper.setString(insertConcept, ++i, name);
            DALHelper.setString(insertConcept, ++i, description);
            DALHelper.setString(insertConcept, ++i, code);
            DALHelper.setString(insertConcept, ++i, scheme);
            DALHelper.setString(insertConcept, ++i, status);
            DALHelper.setString(insertConcept,++i,json);

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
            DALHelper.setString(updateConcept, ++i, name);
            DALHelper.setString(updateConcept, ++i, description);
            DALHelper.setString(updateConcept, ++i, code);
            DALHelper.setString(updateConcept, ++i, scheme);
            DALHelper.setString(updateConcept, ++i, status);
            DALHelper.setString(updateConcept,++i,json);
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
               graph= rs.getInt("dbid");
         } else {
               graph= upsertConcept(null,graphIri,
                   null,null,null,null,IM.DRAFT.getIri(),null);
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
