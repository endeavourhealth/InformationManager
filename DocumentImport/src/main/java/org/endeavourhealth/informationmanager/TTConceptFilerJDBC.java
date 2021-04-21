package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;


import java.sql.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class TTConceptFilerJDBC {
   private final Map<String, Integer> namespaceMap = new HashMap<>();
   private Map<String, String> prefixMap = new HashMap<>();
   private Map<String, Integer> conceptMap;
   private Integer graph;
   private final Connection conn;
   private ObjectMapper om;

   private final PreparedStatement getConceptDbId;
   private final PreparedStatement deleteConceptTypes;
   private final PreparedStatement insertConceptType;
   private final PreparedStatement deleteTriple;
   private final PreparedStatement deleteTripleData;
   private final PreparedStatement insertConcept;
   private final PreparedStatement updateConcept;
   private final PreparedStatement insertTriple;
   private final PreparedStatement insertTripleData;
   private final PreparedStatement insertTerm;
   private final PreparedStatement getTermDbId;

   /**
    * Constructor for use as part of a TTDocument
    *
    * @param conn       the JDBC connection
    * @param conceptMap a map between string IRIs and the concept DBID -for performance
    * @param prefixMap  a map between prefixes and namespace
    */
   public TTConceptFilerJDBC(Connection conn, Map<String, Integer> conceptMap,
                             Map<String, String> prefixMap) throws Exception {
      this(conn);
      this.conceptMap = conceptMap;
      this.prefixMap = prefixMap;

   }

   /**
    * Constructor for filing concepts with prefixed IRIs.
    *
    * @param conn      JDBC connection
    * @param prefixMap map between prefixes and namespaces
    * @throws SQLException standard sql exception thrown
    */
   public TTConceptFilerJDBC(Connection conn,
                             Map<String, String> prefixMap) throws SQLException {
      this(conn);
      this.prefixMap = prefixMap;

   }
   /**
    * Constructor to file a concept, requires fully specified IRIs.
    * If used as part of a document use the constructor with concept map to improve performance
    * If the IRIs are prefixed use a constructor with a prefix map parameter
    *
    * @param conn JDBC connection
    * @throws SQLException SQL exception
    */
   public TTConceptFilerJDBC(Connection conn) throws SQLException {
      this.conn = conn;


      getConceptDbId = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
      insertConcept = conn.prepareStatement("INSERT INTO concept"
          + " (iri,name, description, code, scheme, status) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

      updateConcept = conn.prepareStatement("UPDATE concept SET iri= ?," +
          " name = ?, description = ?, code = ?, scheme = ?, status = ? WHERE dbid = ?");
      deleteTriple = conn.prepareStatement("DELETE FROM tpl WHERE subject=? and "
          + "graph= ?");
      deleteTripleData = conn.prepareStatement("DELETE FROM tpl_data WHERE subject=? and "
          + "graph= ?");

      deleteConceptTypes = conn.prepareStatement("DELETE FROM concept_type where concept=?");
      insertConceptType = conn.prepareStatement("INSERT INTO concept_type (concept,type) VALUES(?,?)");
      insertTriple = conn.prepareStatement("INSERT INTO tpl " +
          "(subject,blank_node,graph,group_number,predicate," +
          "object) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      insertTripleData = conn.prepareStatement("INSERT INTO tpl_data " +
          "(subject,blank_node,graph,group_number,predicate," +
          "literal,data_type,json) VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      insertTerm = conn.prepareStatement("INSERT INTO concept_term SET concept=?, term=?,code=?");
      getTermDbId = conn.prepareStatement("SELECT dbid from concept_term\n" +
          "WHERE term =? and concept=? and code=?");



      om = new ObjectMapper();
      om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

   }

   public void fileConcept(TTConcept concept, TTIriRef graph) throws SQLException, DataFormatException, JsonProcessingException, DataFormatException {
      this.graph = getOrSetConceptId(graph);
      String iri = concept.getIri();
      String label = concept.getName();
      String comment = concept.getDescription();
      String code = concept.getCode();
      String scheme = null;
      if (concept.getScheme() != null)
         scheme = concept.getScheme().getIri();
      String status = IM.ACTIVE.getIri();
      if (concept.getStatus() != null)
         status = concept.getStatus().getIri();
      Integer conceptId = getConceptId(iri);
      String json = om.writeValueAsString(concept);
      //uses name for now as the proxy for owning the concept annotations
      if (label==null)
         conceptId= getOrSetConceptId(TTIriRef.iri(concept.getIri()));
      else
         conceptId = upsertConcept(conceptId,
             expand(iri),
            label, comment, code, scheme, status,null);
      deleteConceptTypes(conceptId);
      deleteTriples(conceptId);
      fileConceptTypes(concept,conceptId);
      fileTripleGroup(conceptId,null,0,IM.HAS_DEFINITION,null,null,json);
      fileNode(conceptId,null,0,concept);
      fileTerms(concept,conceptId);
   }

   private void deleteConceptTypes(Integer conceptId) throws SQLException {
      DALHelper.setInt(deleteConceptTypes, 1, conceptId);
      deleteConceptTypes.executeUpdate();
   }

   private void deleteTriples(Integer conceptId) throws SQLException {

      int i = 0;
      DALHelper.setInt(deleteTriple, ++i, conceptId);
      DALHelper.setInt(deleteTriple, ++i, graph);
      deleteTriple.executeUpdate();

      i=0;
      DALHelper.setInt(deleteTripleData, ++i, conceptId);
      DALHelper.setInt(deleteTripleData, ++i, graph);
      deleteTripleData.executeUpdate();

   }

   private void fileConceptTypes(TTConcept concept, Integer conceptId) throws SQLException, DataFormatException {
      TTArray types= concept.getAsArray(RDF.TYPE);

      int i = 0;

      for(TTValue type: types.getElements()){

         if(!type.isIriRef())
            throw new DataFormatException("Concept types must be array of IriRef ");

         DALHelper.setInt(insertConceptType, ++i, conceptId);
         DALHelper.setString(insertConceptType, ++i, type.asIriRef().getIri());
         insertConceptType.executeUpdate();

      }
   }

   private void fileArray(Integer conceptId, Long parent, Integer group, TTIriRef predicate, TTArray array) throws SQLException, DataFormatException {
      for (TTValue element : array.getElements()) {
         if (element.isIriRef()) {
            fileTripleGroup(conceptId, parent, group, predicate, element.asIriRef(), null,null);
         } else if (element.isNode()) {
            Long blankNode = fileTripleGroup(conceptId, parent, group, predicate, null, null,null);
            fileNode(conceptId,blankNode,group,element.asNode());
         } else if (element.isLiteral()){
            TTIriRef dataType = XSD.STRING;
            if (element.asLiteral().getType()!=null)
               dataType = element.asLiteral().getType();
               fileTripleGroup(conceptId, parent, group, predicate, dataType, element.asLiteral().getValue(), null);
         } else
            throw new DataFormatException("Cannot have an array of an array in RDF");
      }
   }
   private void fileNode(Integer conceptId, Long parent, Integer group,TTNode node) throws SQLException, DataFormatException {
      if (node.getPredicateMap()!=null ||(!node.getPredicateMap().isEmpty())){
      Set<Map.Entry<TTIriRef, TTValue>> entries = node.getPredicateMap().entrySet();
      if (node.get(IM.GROUP_NUMBER)!=null)
         group= node.get(IM.GROUP_NUMBER).asLiteral().intValue();
      for (Map.Entry<TTIriRef, TTValue> entry : entries) {
            TTValue object = entry.getValue();
            if (object.isIriRef()) {
               fileTripleGroup(conceptId, parent, group, entry.getKey(), object.asIriRef(), null,null);
            } else if (object.isLiteral()) {
               TTIriRef dataType = XSD.STRING;
               if (object.asLiteral().getType() != null) {
                  dataType = object.asLiteral().getType();
               }
               String data= object.asLiteral().getValue();
                  if (data.length()>1000)
                     data= data.substring(0,1000)+"...";
                  fileTripleGroup(conceptId, parent, group, entry.getKey(), dataType,data,null);
            } else if (object.isList()) {
               fileArray(conceptId, parent, group, entry.getKey(), entry.getValue().asArray());
            } else if (object.isNode()){
               Long blankNode = fileTripleGroup(conceptId, parent, group, entry.getKey(), null, null,null);
               fileNode(conceptId,blankNode,group,entry.getValue().asNode());
            }
         }
      }
   }





   private Long fileTripleGroup(Integer conceptId, Long parent, Integer group,
                              TTIriRef predicate, TTIriRef targetType,String data,String largeData) throws SQLException {
      int i = 0;

         if (data==null&(largeData==null)){
            DALHelper.setInt(insertTriple, ++i, conceptId);
            DALHelper.setLong(insertTriple,++i,parent);
            DALHelper.setInt(insertTriple, ++i, graph);
            DALHelper.setInt(insertTriple, ++i, group);
            DALHelper.setInt(insertTriple, ++i, getOrSetConceptId(predicate));
            DALHelper.setInt(insertTriple, ++i, getOrSetConceptId(targetType));
            insertTriple.executeUpdate();
            return DALHelper.getGeneratedLongKey(insertTriple);
         } else {
            DALHelper.setInt(insertTripleData, ++i, conceptId);
            DALHelper.setLong(insertTripleData,++i,parent);
            DALHelper.setInt(insertTripleData, ++i, graph);
            DALHelper.setInt(insertTripleData, ++i, group);
            DALHelper.setInt(insertTripleData, ++i, getOrSetConceptId(predicate));
            DALHelper.setString(insertTripleData, ++i, data);
            DALHelper.setInt(insertTripleData, ++i, getOrSetConceptId(targetType));
            DALHelper.setString(insertTripleData, ++i, largeData);
            insertTripleData.executeUpdate();
            return DALHelper.getGeneratedLongKey(insertTripleData);
         }
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
   private Integer getOrSetConceptId(TTIriRef iri) throws SQLException {
      if (iri==null)
         return null;
      String stringIri= expand(iri.getIri());
      Integer id = conceptMap.get(stringIri);
      if (id == null) {
         DALHelper.setString(getConceptDbId, 1, stringIri);
         try (ResultSet rs = getConceptDbId.executeQuery()) {
            if (rs.next()) {
               conceptMap.put(stringIri, rs.getInt("dbid"));
               return rs.getInt("dbid");
            } else {
               id= upsertConcept(null,stringIri,
                   null,null,null,null,IM.DRAFT.getIri(),null);
               conceptMap.put(stringIri,id);
               return id;
            }
         }
      }
      return id;
   }
   private Integer upsertConcept(Integer id, String iri, String name,
                                 String description, String code, String scheme,
                                 String  status,String json) throws SQLException {

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

   public void setGraph(String graphIri) throws SQLException {
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

   private void fileTerms(TTConcept concept,Integer conceptId) throws SQLException {
      if (concept.get(IM.SYNONYM)!=null){
         for (TTValue element:concept.get(IM.SYNONYM).asArray().getElements()){
            fileTerm(conceptId,element.asNode());
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
      if (prefixMap==null)
         return iri;
      int colonPos = iri.indexOf(":");
      String prefix = iri.substring(0, colonPos);
      String path = prefixMap.get(prefix);
      if (path == null)
         return iri;
      else
         return path + iri.substring(colonPos + 1);
   }
}
