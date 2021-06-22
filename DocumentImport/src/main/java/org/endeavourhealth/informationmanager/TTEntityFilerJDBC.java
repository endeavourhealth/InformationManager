package org.endeavourhealth.informationmanager;

import com.google.common.base.Strings;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;
import java.sql.*;
import java.util.*;
import java.util.zip.DataFormatException;

public class TTEntityFilerJDBC {

   private Map<String, String> prefixMap = new HashMap<>();
   private Map<String, Integer> entityMap;
   private Integer graph;

   private final Connection conn;

   private final PreparedStatement getEntityDbId;
   private final PreparedStatement deleteEntityTypes;
   private final PreparedStatement insertEntityType;
   private final PreparedStatement deleteTriples;
   private final PreparedStatement insertEntity;
   private final PreparedStatement updateEntity;
   private final PreparedStatement insertTriple;
   private final PreparedStatement insertTerm;
   private final PreparedStatement getTermDbIdFromTerm;
   private final PreparedStatement getTermDbIdFromCode;
   private final PreparedStatement updateTermCode;


   /**
    * Constructor for use as part of a TTDocument
    *
    * @param conn       the JDBC connection
    * @param entityMap a map between string IRIs and the entity DBID -for performance
    * @param prefixMap  a map between prefixes and namespace
    * @throws SQLException in the event of a connection exception
    */
   public TTEntityFilerJDBC(Connection conn, Map<String, Integer> entityMap,
                            Map<String, String> prefixMap) throws SQLException{
      this(conn);
      this.entityMap = entityMap;
      this.prefixMap = prefixMap;

   }

   /**
    * Constructor to file a entity, requires fully specified IRIs.
    * If used as part of a document use the constructor with entity map to improve performance
    * If the IRIs are prefixed use a constructor with a prefix map parameter
    *
    * @param conn JDBC connection
    * @throws SQLException SQL exception
    */
   public TTEntityFilerJDBC(Connection conn) throws SQLException {


      this.conn= conn;
      getEntityDbId = conn.prepareStatement("SELECT dbid FROM entity WHERE iri = ?");
      insertEntity = conn.prepareStatement("INSERT INTO entity"
          + " (iri,name, description, code, scheme, status,json) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

      updateEntity = conn.prepareStatement("UPDATE entity SET iri= ?," +
          " name = ?, description = ?, code = ?, scheme = ?, status = ?, json=? WHERE dbid = ?");
      deleteTriples = conn.prepareStatement("DELETE FROM tpl WHERE subject=? and "
          + "graph= ?");


      deleteEntityTypes = conn.prepareStatement("DELETE FROM entity_type where entity=?");
      insertEntityType = conn.prepareStatement("INSERT INTO entity_type (entity,type) VALUES(?,?)");
      insertTriple = conn.prepareStatement("INSERT INTO tpl " +
          "(subject,blank_node,graph,predicate,object,literal)" +
          " VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);


      insertTerm = conn.prepareStatement("INSERT INTO term_code SET entity=?, term=?,code=?,scheme=?,entity_term_code=?");
      getTermDbIdFromTerm = conn.prepareStatement("SELECT dbid from term_code\n" +
          "WHERE term =? and scheme=? and entity=?");
      getTermDbIdFromCode = conn.prepareStatement("SELECT dbid from term_code\n" +
        "WHERE code =? and scheme=? and entity=?");
      updateTermCode= conn.prepareStatement("UPDATE term_code SET entity=?, term=?," +
          "code=?,scheme=?,entity_term_code=? where dbid=?");



   }


   /**
    * Adds or replaces a set of predicate objects to a entity, updating the json definition and the triple tables
    * Note that predicates that need to be removed must use the remove predicate method
    * @param entity  the the entity with the predicates to replace
    * @throws SQLException in the event of a jdbc sql issue
    * @throws DataFormatException in the node format is incorrect
    * @throws IllegalStateException if the entity is not in the datbase
    */
   private void updatePredicates(TTEntity entity,Integer entityId) throws SQLException, DataFormatException {
      fileTermCodes(entity,entityId);
      HashMap<TTIriRef,TTValue> predicates= entity.getPredicateMap();

      //Deletes the previous predicate objects ie. clears out all previous objects
      deletePredicates(entityId,predicates);
      if (entity.get(RDF.TYPE)!=null)
         deleteEntityTypes(entityId);
      //Creates transactional adds
      TTNode subject= new TTNode();
      subject.setPredicateMap(predicates);
      fileNode(entityId,null,subject);
      if (entity.get(IM.HAS_TERM_CODE)!=null)
         fileTermCodes(entity,entityId);
      if (entity.get(RDF.TYPE)!=null)
         fileEntityTypes(entity,entityId);
   }

   /**
    * Adds or replaces a set of predicate objects to a entity, updating the json definition and the triple tables
    * Note that predicates that need to be removed must use the remove predicate method
    * @param entity  the the entity with the predicates to replace
    * @throws SQLException in the event of a jdbc sql issue
    * @throws DataFormatException in the node format is incorrect
    * @throws IllegalStateException if the entity is not in the datbase
    */
   public void addPredicateObjects(TTEntity entity,Integer entityId) throws SQLException, DataFormatException {

      if (entityId == null)
         throw new IllegalStateException("No entity for this iri - " + entity.getIri());
      fileTermCodes(entity, entityId);
      HashMap<TTIriRef,TTValue> predicates= entity.getPredicateMap();
      //Creates transactional adds
      TTNode subject= new TTNode();
      subject.setPredicateMap(predicates);
      fileNode(entityId,null,subject);

   }

   private void deletePredicates(Integer entityId,
                                 Map<TTIriRef, TTValue> predicates) throws SQLException {
      List<Integer> predList= new ArrayList<>();
      int i=0;
      for (Map.Entry<TTIriRef, TTValue> po : predicates.entrySet()){
         String predicateIri= po.getKey().getIri();
         Integer predicateId= getEntityId(predicateIri);
         predList.add(predicateId);
         i++;
      }
      StringBuilder builder = new StringBuilder();
      for(Integer ignored :predList) {
         builder.append("?,");
      }
      String placeHolders =  builder.deleteCharAt( builder.length() -1 ).toString();
      String stmt;
      stmt=  "DELETE from tpl where subject=? and graph=? and predicate in ("+ placeHolders+")";
      PreparedStatement deleteObjectPredicates= conn.prepareStatement(stmt);
      DALHelper.setInt(deleteObjectPredicates,1,entityId);
      DALHelper.setInt(deleteObjectPredicates,2,graph);
      i=2;
      for(Integer predDbId : predList) {
         DALHelper.setInt(deleteObjectPredicates,++i,predDbId);
      }
      deleteObjectPredicates.executeUpdate();
   }







   public void fileEntity(TTEntity entity, TTIriRef graph) throws SQLException, DataFormatException {
      this.graph = getOrSetEntityId(graph);
      Integer entityId= fileEntityTable(entity);
      if (entity.getCrud() != null) {
         if (entity.getCrud().equals(IM.UPDATE))
            updatePredicates(entity,entityId);
         else if (entity.getCrud().equals(IM.ADD))
            addPredicateObjects(entity,entityId);
         else
            replacePredicates(entity,entityId);
      } else
         replacePredicates(entity,entityId);
   }

   private Integer fileEntityTable(TTEntity entity) throws SQLException, DataFormatException {
      String iri = entity.getIri();
      Integer entityId = getEntityId(iri);
      String label = entity.getName();
      String comment = entity.getDescription();
      String code = entity.getCode();
      String scheme = null;
      if (entity.getScheme() != null)
         scheme = entity.getScheme().getIri();
      String status = IM.ACTIVE.getIri();
      if (entity.getStatus() != null)
         status = entity.getStatus().getIri();
      //uses name for now as the proxy for owning the entity annotations
      if (label == null)
         entityId = getOrSetEntityId(TTIriRef.iri(entity.getIri()));
      else {
         entityId = upsertEntity(entityId,
           expand(iri),
           label, comment, code, scheme, status, null);
      }
      return entityId;
   }

   private void replacePredicates(TTEntity entity,Integer entityId) throws SQLException, DataFormatException {

         deleteEntityTypes(entityId);
         deleteTriples(entityId);
         fileNode(entityId, null,entity);
         fileEntityTerm(entity, entityId);
         fileTermCodes(entity,entityId);
         fileEntityTypes(entity,entityId);
   }

   private void fileTermCodes(TTEntity entity, Integer entityId) throws SQLException {
      if (entity.get(IM.HAS_TERM_CODE)!=null)
         for (TTValue termCode:entity.get(IM.HAS_TERM_CODE).asArray().getElements())
            fileTermCode(termCode.asNode(),entityId);
   }

   private void deleteEntityTypes(Integer entityId) throws SQLException {
      DALHelper.setInt(deleteEntityTypes, 1, entityId);
      deleteEntityTypes.executeUpdate();
   }

   private void deleteTriples(Integer entityId) throws SQLException {
      PreparedStatement delete = deleteTriples;
      DALHelper.setInt(delete, 1, entityId);
      DALHelper.setInt(delete, 2, graph);
      delete.executeUpdate();

   }

   private void fileEntityTypes(TTEntity entity, Integer entityId) throws SQLException, DataFormatException {
      TTValue typeValue = entity.get(RDF.TYPE);
      if (typeValue == null)
         return;
      if (typeValue.isList()) {
         for (TTValue type : typeValue.asArray().getElements()) {
            if (!type.isIriRef())
               throw new DataFormatException("Entity types must be array of IriRef ");
            fileEntityType(entityId, type);
         }
      } else
         fileEntityType(entityId, typeValue);
   }

   private void fileEntityType(Integer entityId, TTValue type) throws SQLException {
               DALHelper.setInt(insertEntityType, 1, entityId);
               DALHelper.setString(insertEntityType, 2, type.asIriRef().getIri());
               insertEntityType.executeUpdate();

   }

   private void fileArray(Integer entityId, Long parent, TTIriRef predicate, TTArray array) throws SQLException, DataFormatException {
      for (TTValue element : array.getElements()) {
         if (element.isIriRef()) {
            fileTriple(entityId, parent,predicate, element.asIriRef(), null);
         } else if (element.isNode()) {
            Long blankNode = fileTriple(entityId, parent, predicate, null, null);
            fileNode(entityId,blankNode,element.asNode());
         } else if (element.isLiteral()){
            TTIriRef dataType = XSD.STRING;
            if (element.asLiteral().getType()!=null)
               dataType = element.asLiteral().getType();
               fileTriple(entityId, parent, predicate, dataType,
                   element.asLiteral().getValue());
         } else
            throw new DataFormatException("Cannot have an array of an array in RDF");
      }
   }

   private void fileNode(Integer entityId, Long parent,TTNode node) throws SQLException, DataFormatException {
      if (node.getPredicateMap()!=null)
         if (!node.getPredicateMap().isEmpty()){
      Set<Map.Entry<TTIriRef, TTValue>> entries = node.getPredicateMap().entrySet();
      for (Map.Entry<TTIriRef, TTValue> entry : entries) {
         //Term codes are denormalised into term code table
         if (!entry.getKey().equals(IM.HAS_TERM_CODE)) {
            TTValue object = entry.getValue();
            if (object.isIriRef()) {
               fileTriple(entityId, parent, entry.getKey(), object.asIriRef(), null);
            } else if (object.isLiteral()) {
               TTIriRef dataType = XSD.STRING;
               if (object.asLiteral().getType() != null) {
                  dataType = object.asLiteral().getType();
               }
               String data = object.asLiteral().getValue();
               if (data.length() > 1000)
                  data = data.substring(0, 1000) + "...";
               fileTriple(entityId, parent, entry.getKey(), dataType, data);
            } else if (object.isList()) {
               fileArray(entityId, parent,entry.getKey(), entry.getValue().asArray());
            } else if (object.isNode()) {
               Long blankNode = fileTriple(entityId, parent, entry.getKey(), null, null);
               fileNode(entityId, blankNode,entry.getValue().asNode());
            }
         }
      }
      }
   }

   private Long fileTriple(Integer entityId, Long parent,
                              TTIriRef predicate, TTIriRef targetType,String data) throws SQLException {
      int i = 0;
      PreparedStatement insert = insertTriple;
      DALHelper.setInt(insert, ++i, entityId);
      DALHelper.setLong(insert,++i,parent);
      DALHelper.setInt(insert, ++i, graph);
      DALHelper.setInt(insert, ++i, getOrSetEntityId(predicate));
      DALHelper.setInt(insert, ++i, getOrSetEntityId(targetType));
      DALHelper.setString(insert,++i,data);
      insert.executeUpdate();
      return DALHelper.getGeneratedLongKey(insert);
   }


   private Integer getEntityId(String iri) throws SQLException {
      if (Strings.isNullOrEmpty(iri))
         return null;
      iri= expand(iri);
      Integer id = entityMap.get(iri);
      if (id == null) {
         DALHelper.setString(getEntityDbId, 1, iri);
         try (ResultSet rs = getEntityDbId.executeQuery()) {
            if (rs.next()) {
               entityMap.put(iri, rs.getInt("dbid"));
               return rs.getInt("dbid");
            } else {
               return null;
            }
         }
      }
      return id;
   }

   // ------------------------------ Entity ------------------------------
   private Integer getOrSetEntityId(TTIriRef iri) throws SQLException {
      if (iri==null)
         return null;
      String stringIri= expand(iri.getIri());
      Integer id = entityMap.get(stringIri);
      if (id == null) {
         DALHelper.setString(getEntityDbId, 1, stringIri);
         try (ResultSet rs = getEntityDbId.executeQuery()) {
            if (rs.next()) {
               entityMap.put(stringIri, rs.getInt("dbid"));
               return rs.getInt("dbid");
            } else {
               id= upsertEntity(null,stringIri,
                   null,null,null,null,IM.DRAFT.getIri(),null);
               entityMap.put(stringIri,id);
               return id;
            }
         }
      }
      return id;
   }
   private Integer upsertEntity(Integer id, String iri, String name,
                                 String description, String code, String scheme,
                                 String  status,String json) throws SQLException {

      try {
         if (id == null) {
            // Insert
            int i=0;
            DALHelper.setString(insertEntity, ++i, iri);
            DALHelper.setString(insertEntity, ++i, name);
            DALHelper.setString(insertEntity, ++i, description);
            DALHelper.setString(insertEntity, ++i, code);
            DALHelper.setString(insertEntity, ++i, scheme);
            DALHelper.setString(insertEntity, ++i, status);
            DALHelper.setString(insertEntity, ++i, json);

            if (insertEntity.executeUpdate() == 0)
               throw new SQLException("Failed to insert entity [" + iri + "]");
            else {
               id = DALHelper.getGeneratedKey(insertEntity);
               return id;
            }
         } else {
            //update
            int i = 0;
            DALHelper.setString(updateEntity, ++i, iri);
            DALHelper.setString(updateEntity, ++i, name);
            DALHelper.setString(updateEntity, ++i, description);
            DALHelper.setString(updateEntity, ++i, code);
            DALHelper.setString(updateEntity, ++i, scheme);
            DALHelper.setString(updateEntity, ++i, status);
            DALHelper.setString(updateEntity, ++i, json);
            DALHelper.setInt(updateEntity,++i,id);

            if (updateEntity.executeUpdate() == 0) {
               throw new SQLException("Failed to update entity [" + iri + "]");
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


   private void fileEntityTerm(TTEntity entity, Integer entityId) throws SQLException{
      if (entity.get(RDFS.LABEL)!=null){
         TTNode termCode= new TTNode();
         termCode.set(RDFS.LABEL,TTLiteral.literal(entity.getName()));
         if (entity.get(IM.CODE)!=null) {
            termCode.set(IM.CODE, TTLiteral.literal(entity.getCode()));
         }
         if (entity.get(IM.HAS_SCHEME)!=null)
            termCode.set(IM.HAS_SCHEME,entity.getScheme());
         fileTermCode(termCode,entityId);

      }
   }

   private void fileTermCode(TTNode termCode,Integer entityId) throws SQLException {

      String term=null;
      if (termCode.get(RDFS.LABEL)!=null) {
         term = termCode.get(RDFS.LABEL).asLiteral().getValue();
         if (term.length() > 100)
            term = term.substring(0, 100);
      }
      String code=null;
      if (termCode.get(IM.CODE)!=null)
         code= termCode.get(IM.CODE).asLiteral().getValue();
      Integer schemeId=null;
      if (termCode.get(IM.HAS_SCHEME)!=null) {
         TTIriRef scheme = termCode.getAsIriRef(IM.HAS_SCHEME);
         schemeId = getEntityId(scheme.getIri());
      }
      String entityCode=null;
      if (termCode.get(IM.MATCHED_TERM_CODE)!=null)
         entityCode= termCode.get(IM.MATCHED_TERM_CODE).asLiteral().getValue();

      int i = 0;
      Integer dbid=null;
      if (code!=null) {
         DALHelper.setString(getTermDbIdFromCode, ++i, code);
         DALHelper.setInt(getTermDbIdFromCode, ++i, schemeId);
         DALHelper.setInt(getTermDbIdFromCode, ++i, entityId);
         ResultSet rs = getTermDbIdFromCode.executeQuery();
         if (rs.next())
            dbid = rs.getInt("dbid");
      } else {
         DALHelper.setString(getTermDbIdFromTerm, ++i, term);
         DALHelper.setInt(getTermDbIdFromTerm, ++i, schemeId);
         DALHelper.setInt(getTermDbIdFromTerm, ++i, entityId);
         ResultSet rs = getTermDbIdFromTerm.executeQuery();
         if (rs.next())
            dbid = rs.getInt("dbid");

      }
      if (dbid!=null){
         updateTermCode(entityId,term,code,schemeId,entityCode,dbid);
      } else {
         insertTermCode(entityId,term,code,schemeId,entityCode);
      }

   }

   private void insertTermCode(Integer entityId, String term, String code,
                               Integer schemeId, String entityCode) throws SQLException {
      int i = 0;
      DALHelper.setInt(insertTerm, ++i, entityId);
      DALHelper.setString(insertTerm, ++i, term);
      DALHelper.setString(insertTerm, ++i, code);
      DALHelper.setInt(insertTerm, ++i, schemeId);
      DALHelper.setString(insertTerm, ++i, entityCode);
      if (insertTerm.executeUpdate() == 0)
         throw new SQLException("Failed to save term code for  ["
           + term + " "
           + code + "]");
   }

   private void updateTermCode(Integer entityId, String term, String code,
                               Integer schemeId, String entityCode, Integer dbid) throws SQLException {
      int i=0;
      DALHelper.setInt(updateTermCode,++i,entityId);
      DALHelper.setString(updateTermCode, ++i, term);
      DALHelper.setString(updateTermCode, ++i, code);
      DALHelper.setInt(updateTermCode, ++i, schemeId);
      DALHelper.setString(updateTermCode, ++i, entityCode);
      DALHelper.setInt(updateTermCode, ++i, dbid);
      if (updateTermCode.executeUpdate() == 0)
         throw new SQLException("Failed to save term code for  ["
           + term + " "
           + code + "]");
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
