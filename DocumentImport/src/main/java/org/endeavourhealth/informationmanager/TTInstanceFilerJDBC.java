package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
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
   private final Connection conn;

   private final PreparedStatement getInstanceId;
   private final PreparedStatement deletetripleObject;
   private final PreparedStatement deleteTripleData;
   private final PreparedStatement insertInstance;
   private final PreparedStatement updateInstance;
   private final PreparedStatement insertTripleObject;
   private final PreparedStatement getConceptDbId;
   private final PreparedStatement insertTripleData;
   private final PreparedStatement insertTerm;
   private final PreparedStatement getTermDbId;
   private final PreparedStatement updateTermCode;



   /**
    * Constructor for filing concepts with prefixed IRIs.
    *
    * @param conn      JDBC connection
    * @param conceptMap  containing a map between iris and the concepts in the document
    * @param prefixMap map between prefixes and namespaces
    * @throws SQLException standard sql exception thrown
    */
   public TTInstanceFilerJDBC(Connection conn,
                              Map<String,Integer> conceptMap,
                              Map<String, String> prefixMap) throws SQLException {
      this(conn);
      this.conceptMap= conceptMap;
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
      this.conceptMap= new HashMap<>();


      getInstanceId = conn.prepareStatement("SELECT dbid FROM instance WHERE iri = ?");
      insertInstance = conn.prepareStatement("INSERT INTO instance"
          + " (iri,type,name) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);

      updateInstance = conn.prepareStatement("UPDATE instance SET" +
          " type = ?,name= ?  WHERE iri = ?");
      deletetripleObject = conn.prepareStatement("DELETE FROM tpl_ins_object WHERE subject=?");
      deleteTripleData = conn.prepareStatement("DELETE FROM tpl_ins_data WHERE subject=?");

      insertTripleObject = conn.prepareStatement("INSERT INTO tpl_ins_object " +
          "(subject,blank_node,predicate,object)" +
          " VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

      insertTripleData = conn.prepareStatement("INSERT INTO tpl_ins_data " +
          "(subject,blank_node,predicate,literal,data_type)" +
          " VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      getConceptDbId = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
      insertTerm = conn.prepareStatement("INSERT INTO term_code SET concept=?, term=?,code=?,scheme=?,concept_term_code=?");
      getTermDbId = conn.prepareStatement("SELECT dbid from term_code\n" +
          "WHERE (term =? or code=?) and scheme=?");

      updateTermCode= conn.prepareStatement("UPDATE term_code SET concept=?, term=?," +
          "code=?,scheme=?,concept_term_code=? where dbid=?");

     /* om = new ObjectMapper();
      om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
*/
   }

   public void fileInstance(TTInstance instance) throws SQLException, DataFormatException {
      if (instance.get(RDF.TYPE).asIriRef().equals(IM.TERM_CODE))
         fileTermCode(instance);
      else {
         Long instanceId = upsertInstance(instance.getIri(),
             instance.getTypeOf(), instance.getName());
         deleteTriples(instanceId);

         fileNode(instanceId, null, instance);
      }
   }

   private void fileTermCode(TTInstance instance) throws SQLException {
      int i = 0;
      String term=null;
      if (instance.get(RDFS.LABEL)!=null) {
         term = instance.get(RDFS.LABEL).asLiteral().getValue();
         if (term.length() > 100)
            term = term.substring(0, 100);
      }
      String code=null;
      if (instance.get(IM.CODE)!=null)
         code= instance.get(IM.CODE).asLiteral().getValue();
      TTIriRef scheme= instance.getAsIriRef(IM.HAS_SCHEME);
      Integer schemeId= getConceptId(scheme.getIri());
      String conceptCode=null;
      TTArray concepts= instance.get(IM.IS_TERM_FOR).asArray();
      for (TTValue conceptIri:concepts.getElements()) {
         Integer conceptId = getConceptId(conceptIri.asIriRef().getIri());
         TTValue conceptV = instance.get(IM.MATCHED_TERM_CODE);
         if (conceptV != null)
            conceptCode = conceptV.asLiteral().getValue();

         if (conceptId == null)
            throw new IllegalArgumentException("Concept does not exist in database for " + term
            +" "+ conceptIri.asIriRef().getIri());
         DALHelper.setString(getTermDbId, ++i, term);
         DALHelper.setString(getTermDbId, ++i, code);
         DALHelper.setInt(getTermDbId, ++i, schemeId);
         try (ResultSet rs = getTermDbId.executeQuery()) {
            if (rs.next()){
               i=0;
               Integer dbid= rs.getInt("dbid");
               DALHelper.setInt(updateTermCode,++i,conceptId);
               DALHelper.setString(updateTermCode, ++i, term);
               DALHelper.setString(updateTermCode, ++i, code);
               DALHelper.setInt(updateTermCode, ++i, schemeId);
               DALHelper.setString(updateTermCode, ++i, conceptCode);
               DALHelper.setInt(updateTermCode, ++i, dbid);
               if (updateTermCode.executeUpdate() == 0)
                  throw new SQLException("Failed to save term code for  ["
                      + term + " "
                      + code + "]");
            } else {
               i = 0;
               DALHelper.setInt(insertTerm, ++i, conceptId);
               DALHelper.setString(insertTerm, ++i, term);
               DALHelper.setString(insertTerm, ++i, code);
               DALHelper.setInt(insertTerm, ++i, schemeId);
               DALHelper.setString(insertTerm, ++i, conceptCode);
               if (insertTerm.executeUpdate() == 0)
                  throw new SQLException("Failed to save term code for  ["
                      + term + " "
                      + code + "]");
            }
         }
      }
   }


   private void deleteTriples(Long instanceId) throws SQLException {
      DALHelper.setLong(deletetripleObject, 1, instanceId);
      deletetripleObject.executeUpdate();

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
      if (node.getPredicateMap() != null)
         if (!node.getPredicateMap().isEmpty()) {
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
                           TTIriRef dataType) throws SQLException, DataFormatException {
      int i = 0;
      Integer predicateId= getConceptId(predicate.getIri());

      if (data == null) {
         if (!objectValue.isTypedIri())
            objectValue.setIriType(OWL.CLASS);
         Long objectId;
         if (objectValue.getIriType()== OWL.CLASS) {
            Integer id = getConceptId(objectValue.getIri());
            if (id==null)
               throw new DataFormatException("Iri type not in information model : ("+objectValue.getIri());
            else
               objectId= Long.valueOf(id);
         }
         else
            if (objectValue.getIriType()==OWL.NAMEDINDIVIDUAL)
               objectId= getOrSetInstanceId(objectValue);
            else
               throw new DataFormatException("Iri type not supported : ("+objectValue.getIri());
         DALHelper.setLong(insertTripleObject, ++i, instanceId);
         DALHelper.setLong(insertTripleObject, ++i, parent);
         DALHelper.setInt(insertTripleObject, ++i, predicateId);
         DALHelper.setLong(insertTripleObject, ++i, objectId);
         insertTripleObject.executeUpdate();
         return DALHelper.getGeneratedLongKey(insertTripleObject);
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
                   null,null);
            }
         }
      }

   // ------------------------------ Concept ------------------------------
   private Long upsertInstance(String iri, TTIriRef type,String name) throws SQLException {
      Integer typeId=null;
      if (type!=null)

         typeId= getConceptId(type.getIri());
      DALHelper.setString(getInstanceId, 1, iri);
      try (ResultSet rs = getInstanceId.executeQuery()) {
         if (rs.next()) {
            Long dbid= rs.getLong("dbid");
            int i=0;
            DALHelper.setInt(updateInstance,++i,typeId);
            DALHelper.setString(updateInstance,++i,name);
            DALHelper.setString(updateInstance,++i,iri);
            return dbid;
         } else {
            int i = 0;
            DALHelper.setString(insertInstance, ++i, iri);
            DALHelper.setInt(insertInstance, ++i, typeId);
            DALHelper.setString(insertInstance,++i,name);
            if (insertInstance.executeUpdate() == 0)
               throw new SQLException("Failed to insert instance [" + iri + "]");
            else {
               return (long) DALHelper.getGeneratedKey(insertInstance);
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
