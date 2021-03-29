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
   private final List<TTIriRef> classify = new ArrayList<>();
   private Integer graph;
   private Connection conn;

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
    * @param conn  JDBC connection
    * @throws SQLException  SQL exception
    */
   public TTConceptFilerJDBC(Connection conn) throws SQLException {
      this.conn = conn;


      getConceptDbId = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
      insertConcept = conn.prepareStatement("INSERT INTO concept"
          + " (iri,name, description, code, scheme, status,definition) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

      updateConcept = conn.prepareStatement("UPDATE concept SET iri= ?," +
          " name = ?, description = ?, code = ?, scheme = ?, status = ?, definition = ? WHERE dbid = ?");
      deleteTriple = conn.prepareStatement("DELETE FROM tpl WHERE subject=? and "
          + "graph= ?");
      deleteTripleData = conn.prepareStatement("DELETE FROM tpl_data WHERE subject=? and "
          + "graph= ?");
      
      deleteConceptTypes = conn.prepareStatement("DELETE FROM concept_type where concept=?");
      insertConceptType = conn.prepareStatement("INSERT INTO concept_type (concept,type) VALUES(?,?)");
      insertTriple = conn.prepareStatement("INSERT INTO tpl " +
          "(subject,graph,group_number,predicate," +
          "object) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      insertTripleData = conn.prepareStatement("INSERT INTO tpl_data " +
          "(subject,graph,group_number,predicate," +
          "literal,data_type) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      insertTerm = conn.prepareStatement("INSERT INTO concept_term SET concept=?, term=?,code=?");
      getTermDbId = conn.prepareStatement("SELECT dbid from concept_term\n" +
          "WHERE term =? and concept=? and code=?");

      classify.add(IM.IS_A);
      classify.add(IM.IS_CHILD_OF);
      classify.add(IM.IS_CONTAINED_IN);


   }


   public void fileConcept(TTConcept concept, TTIriRef graph) throws SQLException, DataFormatException, JsonProcessingException {
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

      ObjectMapper om = new ObjectMapper();
      om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      String json = om.writeValueAsString(concept);
      conceptId = upsertConcept(conceptId,
          expand(iri),
          label, comment, code, scheme, status, json);


      deleteTriples(conceptId);
      fileTypes(concept, conceptId);
      filePropertyGroups(concept, conceptId);
      fileRoleGroups(concept, conceptId);
      fileSuperTypes(concept, conceptId);
      fileMembers(concept, conceptId);
      fileTerms(concept, conceptId);
      fileDomainsAndRanges(concept, conceptId);
   }

   private void fileDomainsAndRanges(TTConcept concept, Integer conceptId) throws DataFormatException, SQLException {
      if (concept.get(RDFS.DOMAIN) != null)
         filePropertyAxiom(concept, conceptId, RDFS.DOMAIN);
      if (concept.get(RDFS.RANGE) != null)
         filePropertyAxiom(concept, conceptId, RDFS.RANGE);
   }

   private void filePropertyAxiom(TTConcept concept, Integer conceptId, TTIriRef axiom) throws DataFormatException, SQLException {
      TTValue exp = concept.get(axiom);
      if (exp.isIriRef())
         fileTripleGroup(conceptId, 0, axiom, exp.asIriRef(), null);
      else if (exp.isNode()) {
         if (exp.asNode().get(OWL.UNIONOF) != null) {
            for (TTValue union : exp.asNode().get(OWL.UNIONOF).asArray().getElements()) {
               if (union.isIriRef())
                  fileTripleGroup(conceptId, 0, axiom, union.asIriRef(), null);
            }
         }
      } else
         throw new DataFormatException("domains or ranges have to be single or unions in this version ("
             + concept.getIri() + ")");

   }


   private void fileTypes(TTConcept concept, Integer conceptId) throws SQLException {
      if (concept.get(RDF.TYPE) != null) {
            for (TTValue vtype : concept.get(RDF.TYPE).asArray().getElements()) {
               int i = 0;
               DALHelper.setInt(insertConceptType, ++i, conceptId);
               DALHelper.setString(insertConceptType, ++i, vtype.asIriRef().getIri());
               insertConceptType.executeUpdate();
            }
      }
   }


   private void fileSuperTypes(TTConcept concept, Integer conceptId) throws  SQLException {
      for (TTIriRef isaType : classify) {
         if (concept.get(isaType) != null) {
            int i = 0;
            Integer isaId = getOrSetConceptId(isaType);
            if (concept.get(isaType).isIriRef()) {
               fileTripleObjectIri(conceptId, isaId, getOrSetConceptId(concept.get(isaType).asIriRef()));
            } else {
               TTArray isas = concept.get(isaType).asArray();
               for (TTValue parent : isas.getElements()) {
                  fileTripleObjectIri(conceptId, isaId, getOrSetConceptId(parent.asIriRef()));
               }
            }
         }
      }
   }

   private void fileTripleObjectIri(Integer subject, Integer predicate, Integer object) throws SQLException {

         int i = 0;
         DALHelper.setInt(insertTriple, ++i, subject);
         DALHelper.setInt(insertTriple, ++i, graph);
         DALHelper.setInt(insertTriple, ++i, 0);
         DALHelper.setInt(insertTriple, ++i, predicate);
         DALHelper.setInt(insertTriple, ++i, object);

         insertTriple.executeUpdate();
   }


   private void fileMembers(TTConcept concept, Integer conceptId) throws SQLException {

      if (concept.get(IM.HAS_MEMBER)!=null){
         TTArray members= concept.get(IM.HAS_MEMBER).asArray();
         Integer predicate= getOrSetConceptId(IM.HAS_MEMBER);
         for (TTValue  member: members.getElements()) {
            if (member.isIriRef()){
               TTIriRef memberConcept= member.asIriRef();
               int i = 0;
               DALHelper.setInt(insertTriple, ++i, conceptId);
               DALHelper.setInt(insertTriple, ++i, predicate);
               DALHelper.setInt(insertTriple, ++i, getOrSetConceptId(memberConcept));
               DALHelper.setInt(insertTriple, ++i, graph);
               if (insertTriple.executeUpdate() == 0)
                  throw new SQLException("Unable to insert concept tree with [" +
                      member.asIriRef().getIri() + " member of " + concept.getIri());
            }
         }

      }


   }


   private void deleteTriples(Integer conceptId) throws SQLException {
         DALHelper.setInt(deleteConceptTypes, 1, conceptId);
         deleteConceptTypes.executeUpdate();

         int i = 0;
         DALHelper.setInt(deleteTriple, ++i, conceptId);
         DALHelper.setInt(deleteTriple, ++i, graph);
         deleteTriple.executeUpdate();

         i=0;
         DALHelper.setInt(deleteTripleData, ++i, conceptId);
         DALHelper.setInt(deleteTripleData, ++i, graph);
         deleteTripleData.executeUpdate();

   }

   private void filePropertyGroups(TTConcept concept,Integer conceptId) throws SQLException {
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
               fileTripleGroup(conceptId, groupNumber,property,targetIri,data);
            }

         }
      }

   }

   private void fileRoleGroups(TTConcept concept,Integer conceptId) throws SQLException {
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

   private void fileRole(Integer conceptId, Map.Entry<TTIriRef, TTValue> role, Integer groupNumber) throws SQLException {
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
      fileTripleGroup(conceptId,groupNumber,property,targetType,data);

   }

   private Integer fileTripleGroup(Integer conceptId, Integer group,
                              TTIriRef predicate, TTIriRef targetType,String data) throws SQLException {
      int i = 0;

         if (data==null){
            DALHelper.setInt(insertTriple, ++i, conceptId);
            DALHelper.setInt(insertTriple, ++i, graph);
            DALHelper.setInt(insertTriple, ++i, group);
            DALHelper.setInt(insertTriple, ++i, getOrSetConceptId(predicate));
            DALHelper.setInt(insertTriple, ++i, getOrSetConceptId(targetType));
            insertTriple.executeUpdate();
            return DALHelper.getGeneratedKey(insertTriple);
         } else {
            DALHelper.setInt(insertTripleData, ++i, conceptId);
            DALHelper.setInt(insertTripleData, ++i, graph);
            DALHelper.setInt(insertTripleData, ++i, group);
            DALHelper.setInt(insertTripleData, ++i, getOrSetConceptId(predicate));
            DALHelper.setString(insertTripleData, ++i, data);
            DALHelper.setInt(insertTripleData, ++i, getOrSetConceptId(targetType));
            insertTripleData.executeUpdate();
            return DALHelper.getGeneratedKey(insertTripleData);

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
