package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class TTConceptFilerJDBC {
   private final Map<String, Integer> namespaceMap = new HashMap<>();
   private Map<String, String> prefixMap = new HashMap<>();
   private Map<String, Integer> conceptMap;
   private Integer graph;
   private final ObjectMapper om;
   private final Connection conn;

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
   private final PreparedStatement updateTermCode;
   private final PreparedStatement getDefinition;
   private final PreparedStatement updateDefinition;

   /**
    * Constructor for use as part of a TTDocument
    *
    * @param conn       the JDBC connection
    * @param conceptMap a map between string IRIs and the concept DBID -for performance
    * @param prefixMap  a map between prefixes and namespace
    * @throws SQLException in the event of a connection exception
    */
   public TTConceptFilerJDBC(Connection conn, Map<String, Integer> conceptMap,
                             Map<String, String> prefixMap) throws SQLException{
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


      this.conn= conn;
      getConceptDbId = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
      insertConcept = conn.prepareStatement("INSERT INTO concept"
          + " (iri,name, description, code, scheme, status,json) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

      updateConcept = conn.prepareStatement("UPDATE concept SET iri= ?," +
          " name = ?, description = ?, code = ?, scheme = ?, status = ?, json=? WHERE dbid = ?");
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
          "literal,data_type) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      insertTerm = conn.prepareStatement("INSERT INTO term_code SET concept=?, term=?,code=?,scheme=?,concept_term_code=?");
      getTermDbId = conn.prepareStatement("SELECT dbid from term_code\n" +
          "WHERE (term =? or code=?) and scheme=?");
      updateTermCode= conn.prepareStatement("UPDATE term_code SET concept=?, term=?," +
          "code=?,scheme=?,concept_term_code=? where dbid=?");
      getDefinition= conn.prepareStatement("SELECT json from concept where dbid=?");
      updateDefinition= conn.prepareStatement("UPDATE concept set json=? where dbid=?");





      om = new ObjectMapper();
      om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
      om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      om.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

   }

   /**
    * Adds or replaces a set of predicate objects to a concept, updating the json definition and the triple tables
    * Note that predicates that need to be removed must use the remove predicate method
    * @param concept  the the concept with the predicates to replace
    * @throws SQLException in the event of a jdbc sql issue
    * @throws IOException in the event of failure to parse json definition
    * @throws DataFormatException in the node format is incorrect
    * @throws IllegalStateException if the concept is not in the datbase
    */
   public void updatePredicates(TTConcept concept) throws SQLException, DataFormatException, IOException {
      Integer conceptId = getConceptId(concept.getIri());
      if (conceptId == null)
         throw new IllegalStateException("No concept for this iri - " + concept.getIri());
      HashMap<TTIriRef,TTValue> predicates= concept.getPredicateMap();

      //Deletes previous predicate objects and replaces them for these predicates
      replaceJsonPredicates(conceptId, predicates);

      //Deletes the previous predicate objects ie. clears out all previous objects
      deletePredicates(conceptId,predicates);

      //Creates transactional adds
      TTNode subject= new TTNode();
      subject.setPredicateMap(predicates);
      fileNode(conceptId,null,0,subject);
   }

   /**
    * Adds or replaces a set of predicate objects to a concept, updating the json definition and the triple tables
    * Note that predicates that need to be removed must use the remove predicate method
    * @param concept  the the concept with the predicates to replace
    * @throws SQLException in the event of a jdbc sql issue
    * @throws IOException in the event of failure to parse json definition
    * @throws DataFormatException in the node format is incorrect
    * @throws IllegalStateException if the concept is not in the datbase
    */
   public void addPredicateObjects(TTConcept concept) throws SQLException, DataFormatException, IOException {
      Integer conceptId = getConceptId(concept.getIri());
      if (conceptId == null)
         throw new IllegalStateException("No concept for this iri - " + concept.getIri());
      HashMap<TTIriRef,TTValue> predicates= concept.getPredicateMap();

      //adds additional objects to the current predicates creating an array
      addJsonPredicates(conceptId, predicates);

      //Creates transactional adds
      TTNode subject= new TTNode();
      subject.setPredicateMap(predicates);
      fileNode(conceptId,null,0,subject);
   }

   private void deletePredicates(Integer conceptId,
                                 Map<TTIriRef, TTValue> predicates) throws SQLException {
      List<Integer> predList= new ArrayList<>();
      int i=0;
      for (Map.Entry<TTIriRef, TTValue> po : predicates.entrySet()){
         String predicateIri= po.getKey().getIri();
         Integer predicateId= getConceptId(predicateIri);
         predList.add(predicateId);
         i++;
      }
      StringBuilder builder = new StringBuilder();
      for(Integer predId:predList) {
         builder.append("?,");
      }
      String placeHolders =  builder.deleteCharAt( builder.length() -1 ).toString();

      String stmt= "DELETE from tpl where subject=? and predicate in ("+ placeHolders+")";
      PreparedStatement deleteObjectPredicates= conn.prepareStatement(stmt);
      DALHelper.setInt(deleteObjectPredicates,1,conceptId);
      i=1;
      for(Integer predDbId : predList) {
         DALHelper.setInt(deleteObjectPredicates,++i,predDbId);
      }
      deleteObjectPredicates.executeUpdate();

      stmt="DELETE from tpl_data where subject=? and predicate in ("+placeHolders+")";
      PreparedStatement deleteLiteralPredicates= conn.prepareStatement(stmt);
      i=1;
      for(Integer predDbId : predList ) {
         DALHelper.setInt(deleteLiteralPredicates,++i,predDbId);
      }
      DALHelper.setInt(deleteLiteralPredicates,1,conceptId);
      deleteLiteralPredicates.executeUpdate();
   }

   private void replaceJsonPredicates(Integer conceptId,
                                       Map<TTIriRef, TTValue> predicates) throws SQLException, IOException {
      DALHelper.setInt(getDefinition, 1, conceptId);
      ResultSet rs = getDefinition.executeQuery();
      if (rs.next()) {
         String json = rs.getString("json");
         if (json==null){
            System.err.println("no json for "+ conceptId);
         }
         TTConcept concept = om.readValue(json, TTConcept.class);
         addToSubject(concept, predicates);
         json = om.writeValueAsString(concept);
         Integer[] preds= {1,2,3,4};
         DALHelper.setString(updateDefinition,1,json);
         DALHelper.setInt(updateDefinition,2,conceptId);
         if (updateDefinition.executeUpdate()==0){
            System.err.println("Failed to update concept definition");
         }
      }
   }

   private void addToSubject(TTConcept concept,  Map<TTIriRef, TTValue> predicates){
            for (Map.Entry<TTIriRef, TTValue> entry : predicates.entrySet()) {
               TTIriRef predicate = entry.getKey().asIriRef();
               TTValue object = entry.getValue();
               concept.set(predicate,object);
            }
   }

   private void addToPredicates(TTConcept concept,  Map<TTIriRef, TTValue> predicates){
      for (Map.Entry<TTIriRef, TTValue> entry : predicates.entrySet()) {
         TTIriRef predicate = entry.getKey().asIriRef();
         TTValue object = entry.getValue();
         concept.addObject(predicate,object);
      }
   }


   private void addJsonPredicates(Integer conceptId,
                                      Map<TTIriRef, TTValue> predicates) throws SQLException, IOException {
      DALHelper.setInt(getDefinition, 1, conceptId);
      ResultSet rs = getDefinition.executeQuery();
      if (rs.next()) {
         String json = rs.getString("json");
         if (json==null){
            System.err.println("no json for "+ conceptId);
         }
         TTConcept concept = om.readValue(json, TTConcept.class);
         addToPredicates(concept, predicates);
         json = om.writeValueAsString(concept);
         Integer[] preds= {1,2,3,4};
         DALHelper.setString(updateDefinition,1,json);
         DALHelper.setInt(updateDefinition,2,conceptId);
         if (updateDefinition.executeUpdate()==0){
            System.err.println("Failed to update concept definition");
         }
      }
   }




   public void fileConcept(TTConcept concept, TTIriRef graph) throws SQLException, JsonProcessingException, DataFormatException {
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
      String json = null;
      try {
         json = om.writeValueAsString(concept);
      } catch (JsonProcessingException e) {
         System.err.println("error with serializing "+ concept.getIri());
         e.printStackTrace();
      }
      //uses name for now as the proxy for owning the concept annotations
      if (label==null)
         conceptId= getOrSetConceptId(TTIriRef.iri(concept.getIri()));
      else
         conceptId = upsertConcept(conceptId,
             expand(iri),
            label, comment, code, scheme, status,json);
      deleteConceptTypes(conceptId);
      deleteTriples(conceptId);
      fileConceptTypes(concept,conceptId);
      fileNode(conceptId,null,0,concept);
      fileCoreTerm(concept,conceptId);
      //fileTerms(concept,conceptId);
   }

   private void deleteConceptTypes(Integer conceptId) throws SQLException {
      DALHelper.setInt(deleteConceptTypes, 1, conceptId);
      deleteConceptTypes.executeUpdate();
   }

   private void deleteTriples(Integer conceptId) throws SQLException {
      DALHelper.setInt(deleteTriple, 1, conceptId);
      DALHelper.setInt(deleteTriple, 2, graph);
      deleteTriple.executeUpdate();

      DALHelper.setInt(deleteTripleData, 1, conceptId);
      DALHelper.setInt(deleteTripleData, 2, graph);
      deleteTripleData.executeUpdate();

   }

   private void fileConceptTypes(TTConcept concept, Integer conceptId) throws SQLException, DataFormatException {
       TTValue typeValue = concept.get(RDF.TYPE);

       if (typeValue == null)
           return;

       if (typeValue.isList()) {

           for (TTValue type : typeValue.asArray().getElements()) {

               if (!type.isIriRef())
                   throw new DataFormatException("Concept types must be array of IriRef ");

               DALHelper.setInt(insertConceptType, 1, conceptId);
               DALHelper.setString(insertConceptType, 2, type.asIriRef().getIri());
               insertConceptType.executeUpdate();

           }
       } else if (typeValue.isIriRef()) {
           DALHelper.setInt(insertConceptType, 1, conceptId);
           DALHelper.setString(insertConceptType, 2, typeValue.asIriRef().getIri());
           insertConceptType.executeUpdate();
       }
   }

   private void fileArray(Integer conceptId, Long parent, Integer group, TTIriRef predicate, TTArray array) throws SQLException, DataFormatException {
      for (TTValue element : array.getElements()) {
         if (element.isIriRef()) {
            fileTripleGroup(conceptId, parent, group, predicate, element.asIriRef(), null);
         } else if (element.isNode()) {
            Long blankNode = fileTripleGroup(conceptId, parent, group, predicate, null, null);
            fileNode(conceptId,blankNode,group,element.asNode());
         } else if (element.isLiteral()){
            TTIriRef dataType = XSD.STRING;
            if (element.asLiteral().getType()!=null)
               dataType = element.asLiteral().getType();
               fileTripleGroup(conceptId, parent, group, predicate, dataType,
                   element.asLiteral().getValue());
         } else
            throw new DataFormatException("Cannot have an array of an array in RDF");
      }
   }

   private void fileNode(Integer conceptId, Long parent, Integer group,TTNode node) throws SQLException, DataFormatException {
      if (node.getPredicateMap()!=null)
         if (!node.getPredicateMap().isEmpty()){
      Set<Map.Entry<TTIriRef, TTValue>> entries = node.getPredicateMap().entrySet();
      if (node.get(IM.GROUP_NUMBER)!=null)
         group= node.get(IM.GROUP_NUMBER).asLiteral().intValue();
      for (Map.Entry<TTIriRef, TTValue> entry : entries) {
            TTValue object = entry.getValue();
            if (object.isIriRef()) {
               fileTripleGroup(conceptId, parent, group, entry.getKey(), object.asIriRef(), null);
            } else if (object.isLiteral()) {
               TTIriRef dataType = XSD.STRING;
               if (object.asLiteral().getType() != null) {
                  dataType = object.asLiteral().getType();
               }
               String data= object.asLiteral().getValue();
                  if (data.length()>1000)
                     data= data.substring(0,1000)+"...";
                  fileTripleGroup(conceptId, parent, group, entry.getKey(), dataType,data);
            } else if (object.isList()) {
               fileArray(conceptId, parent, group, entry.getKey(), entry.getValue().asArray());
            } else if (object.isNode()){
               Long blankNode = fileTripleGroup(conceptId, parent, group, entry.getKey(), null, null);
               fileNode(conceptId,blankNode,group,entry.getValue().asNode());
            }
         }
      }
   }

   private Long fileTripleGroup(Integer conceptId, Long parent, Integer group,
                              TTIriRef predicate, TTIriRef targetType,String data) throws SQLException {
      int i = 0;

         if (data==null){
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
            DALHelper.setString(insertConcept, ++i, json);

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
            DALHelper.setString(updateConcept, ++i, json);
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

   private void fileCoreTerm(TTConcept concept, Integer conceptId) throws SQLException{
      if (concept.get(RDFS.LABEL)!=null){
         TTNode termCode= new TTNode();
         termCode.set(RDFS.LABEL,TTLiteral.literal(concept.getName()));
         if (concept.get(IM.CODE)!=null) {
            termCode.set(IM.CODE, TTLiteral.literal(concept.getCode()));
         }
         if (concept.get(IM.HAS_SCHEME)!=null)
            termCode.set(IM.HAS_SCHEME,concept.getScheme());
         fileTerm(conceptId,termCode);
      }
   }

   private String getHashCode(String term) {
     try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");              // Get the SHA-256 hash generator
        byte[] digest = md.digest(term.getBytes(StandardCharsets.UTF_8));     // Hash "term" (to byte array)
        String hash = Base64.getEncoder().encodeToString(digest);// Base64 encode
        return hash;
     } catch (NoSuchAlgorithmException e) {
        return term+"-"+term.hashCode();
      }
   }

   private void fileTerm(Integer conceptId, TTNode termCode) throws SQLException {
      int i = 0;
      String term= termCode.get(RDFS.LABEL).asLiteral().getValue();
      if (term==null)
         return;
      String code=null;
      if (termCode.get(IM.CODE)!=null)
         code= termCode.get(IM.CODE).asLiteral().getValue();
      String conceptCode=null;
      TTIriRef scheme;
      if (termCode.get(IM.HAS_SCHEME)==null)
         scheme= IM.DISCOVERY_CODE;
      else
         scheme= termCode.getAsIriRef(IM.HAS_SCHEME);
      Integer schemeId= getConceptId(scheme.getIri());
      TTValue conceptV= termCode.get(IM.HAS_TERM_CODE);
      if (conceptV!=null)
         conceptCode=conceptV.asLiteral().getValue();
      if (term.length() > 100)
         term = term.substring(0, 100);
      if (conceptId == null)
         throw new IllegalArgumentException("Concept does not exist in database for " + term);
      DALHelper.setString(getTermDbId, ++i, term);
      DALHelper.setString(getTermDbId, ++i, code);
      DALHelper.setInt(getTermDbId, ++i, schemeId);
      try (ResultSet rs = getTermDbId.executeQuery()) {
         if (rs.next()) {
            i = 0;
            Integer dbid = rs.getInt("dbid");
            DALHelper.setInt(updateTermCode, ++i, conceptId);
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

   private String expand(String iri) {
      if (prefixMap==null)
         return iri;
      try {
         int colonPos = iri.indexOf(":");
         String prefix = iri.substring(0, colonPos);
         String path = prefixMap.get(prefix);
         if (path == null)
            return iri;
         else
            return path + iri.substring(colonPos + 1);
      }catch (StringIndexOutOfBoundsException e){
         System.err.println("invalid iri "+ iri);
         return null;
      }
   }
}
