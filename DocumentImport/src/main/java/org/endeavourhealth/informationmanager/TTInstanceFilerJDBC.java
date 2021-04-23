package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.XSD;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

public class TTInstanceFilerJDBC {
   private final Map<String, Integer> namespaceMap = new HashMap<>();
   private Map<String, String> prefixMap = new HashMap<>();
   private Map<String, Integer> conceptMap;
   private Integer graph;
   private final Connection conn;
   private ObjectMapper om;

   private final PreparedStatement getInstanceId;
   private final PreparedStatement deleteTriple;
   private final PreparedStatement deleteTripleData;
   private final PreparedStatement deleteInstance;
   private final PreparedStatement insertInstance;
   private final PreparedStatement updateInstance;
   private final PreparedStatement insertTriple;
   private final PreparedStatement getConceptDbId;
   private final PreparedStatement insertTripleData;

   /**
    * Constructor for use as part of a TTDocument
    *
    * @param conn       the JDBC connection
    * @param conceptMap a map between string IRIs and the concept DBID -for performance
    * @param prefixMap  a map between prefixes and namespace
    */


   /**
    * Constructor for filing concepts with prefixed IRIs.
    *
    * @param conn      JDBC connection
    * @param prefixMap map between prefixes and namespaces
    * @throws SQLException standard sql exception thrown
    */
   public TTInstanceFilerJDBC(Connection conn,
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
   public TTInstanceFilerJDBC(Connection conn) throws SQLException {
      this.conn = conn;


      getInstanceId = conn.prepareStatement("SELECT dbid FROM instance WHERE iri = ?");
      insertInstance = conn.prepareStatement("INSERT INTO instance"
          + " (iri,type) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);

      updateInstance = conn.prepareStatement("UPDATE instance SET" +
          " type = ?  WHERE iri = ?");
      deleteTriple = conn.prepareStatement("DELETE FROM ins_tpl WHERE subject=?");
      deleteTripleData = conn.prepareStatement("DELETE FROM ins_tpl_data WHERE subject=?");
      deleteInstance = conn.prepareStatement("DELETE FROM instance where iri=?");

      insertTriple = conn.prepareStatement("INSERT INTO ins_tpl " +
          "(subject,blank_node,predicate,object)" +
          " VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      insertTripleData = conn.prepareStatement("INSERT INTO ins_tpl_data " +
          "(subject,blank_node,predicate,literal,data_type)" +
          " VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      getConceptDbId = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");


      om = new ObjectMapper();
      om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

   }

   public void fileInstance(TTInstance instance) throws SQLException, DataFormatException, JsonProcessingException, DataFormatException {
      Long instanceId = upsertInstance(instance.getIri(),instance.getType());

      deleteTriples(instanceId);
      fileNode(instanceId, null, instance);
   }


   private void deleteTriples(Long instanceId) throws SQLException {
      DALHelper.setLong(deleteTriple, 1, instanceId);
      deleteTriple.executeUpdate();

      DALHelper.setLong(deleteTripleData, 1, instanceId);
      deleteTripleData.executeUpdate();

   }

   private void fileArray(Long instanceId, Long parent, TTIriRef predicate, TTArray array) throws SQLException, DataFormatException {
      for (TTValue element : array.getElements()) {
         if (element.isIriRef()) {
            fileTriple(instanceId,parent, predicate, element.asIriRef(), null, null);
         } else if (element.isNode()) {
            Long blankNode = fileTriple(instanceId, parent, predicate, null, null, null);
            fileNode(instanceId, blankNode, element.asNode());
         } else if (element.isLiteral()) {
            TTIriRef dataType = XSD.STRING;
            if (element.asLiteral().getType() != null)
               dataType = element.asLiteral().getType();
            fileTriple(instanceId, parent, predicate, null,element.asLiteral().getValue(),
                dataType);
         } else
            throw new DataFormatException("Cannot have an array of an array in RDF");
      }
   }

   private void fileNode(Long instanceId, Long parent, TTNode node) throws SQLException, DataFormatException {
      if (node.getPredicateMap() != null || (!node.getPredicateMap().isEmpty())) {
         Set<Map.Entry<TTIriRef, TTValue>> entries = node.getPredicateMap().entrySet();

         for (Map.Entry<TTIriRef, TTValue> entry : entries) {
            TTValue object = entry.getValue();
            if (object.isIriRef()) {
               fileTriple(instanceId, parent, entry.getKey(), object.asIriRef(), null, null);
            } else if (object.isLiteral()) {
               TTIriRef dataType = XSD.STRING;
               if (object.asLiteral().getType() != null) {
                  dataType = object.asLiteral().getType();
               }
               String data = object.asLiteral().getValue();
               if (data.length() > 1000)
                  data = data.substring(0, 1000) + "...";
               fileTriple(instanceId, parent, entry.getKey(), dataType, data, null);
            } else if (object.isList()) {
               fileArray(instanceId, parent, entry.getKey(), entry.getValue().asArray());
            } else if (object.isNode()) {
               Long blankNode = fileTriple(instanceId, parent,entry.getKey(), null, null, null);
               fileNode(instanceId, blankNode, entry.getValue().asNode());
            }
         }
      }
   }

   private Long fileTriple(Long instanceId, Long parent,
                                TTIriRef predicate, TTIriRef objectValue, String data,
                           TTIriRef dataType) throws SQLException {
      int i = 0;

      if (data == null) {
         DALHelper.setLong(insertTriple, ++i, instanceId);
         DALHelper.setLong(insertTriple, ++i, parent);
         DALHelper.setInt(insertTriple, ++i, getConceptId(predicate.getIri()));
         DALHelper.setLong(insertTriple, ++i, getOrSetInstanceId(objectValue));
         insertTriple.executeUpdate();
         return DALHelper.getGeneratedLongKey(insertTriple);
      } else {
         if (dataType==null)
            dataType=XSD.STRING;
         DALHelper.setLong(insertTripleData, ++i, instanceId);
         DALHelper.setLong(insertTripleData, ++i, parent);
         DALHelper.setInt(insertTripleData, ++i, getConceptId(predicate.getIri()));
         DALHelper.setString(insertTripleData, ++i, data);
         DALHelper.setInt(insertTripleData, ++i, getConceptId(dataType.getIri()));
         insertTripleData.executeUpdate();
         return DALHelper.getGeneratedLongKey(insertTripleData);
      }
   }


   private Integer getConceptId(String iri) throws SQLException {
      if (Strings.isNullOrEmpty(iri))
         return null;
      iri = expand(iri);
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
   private Long getOrSetInstanceId(TTIriRef iri) throws SQLException {
      if (iri==null)
         return null;
      String stringIri= expand(iri.getIri());
      DALHelper.setString(getInstanceId, 1, stringIri);
      try (ResultSet rs = getInstanceId.executeQuery()) {
            if (rs.next()) {
               conceptMap.put(stringIri, rs.getInt("dbid"));
               return rs.getLong("dbid");
            } else {
               return upsertInstance(stringIri,
                   null);
            }
         }
      }

   // ------------------------------ Concept ------------------------------
   private Long upsertInstance(String iri, TTIriRef type) throws SQLException {

      Integer typeId= getConceptId(type.getIri());
      DALHelper.setString(getInstanceId, 1, iri);
      try (ResultSet rs = getInstanceId.executeQuery()) {
         if (rs.next()) {
            Long dbid= rs.getLong("dbid");
            int i=0;
            DALHelper.setInt(updateInstance,++i,typeId);
            DALHelper.setString(updateInstance,++i,iri);
            return dbid;
         } else {
            int i = 0;
            DALHelper.setString(insertInstance, ++i, iri);
            DALHelper.setInt(insertInstance, ++i, typeId);
            if (insertInstance.executeUpdate() == 0)
               throw new SQLException("Failed to insert instance [" + iri + "]");
            else {
               return Long.valueOf(DALHelper.getGeneratedKey(insertInstance));
            }
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
