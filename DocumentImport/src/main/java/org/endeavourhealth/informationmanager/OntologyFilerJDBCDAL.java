package org.endeavourhealth.informationmanager;

import com.google.common.base.Strings;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;
import org.endeavourhealth.informationmanager.common.models.AxiomType;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.models.ConceptType;
import org.endeavourhealth.informationmanager.common.models.ExpressionType;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Files a set of declarations and axioms relating to a single module
 * <p> This uses a replace all for concept for module approach to filing axioms i.e. assumes a full set for a concept from the perspective of the module</p>
 * <p> It assumes that axioms and expressions and concepts may not have the internal DB ids. Full trnasactional filers assume they do</p>
 */
public class OntologyFilerJDBCDAL {
   private static final Logger LOG = LoggerFactory.getLogger(OntologyFilerJDBCDAL.class);

   // Prepared statements
   private final PreparedStatement getNamespace;
   private final PreparedStatement insertNamespace;
   private final PreparedStatement getOntology;
   private final PreparedStatement insertOntology;
   private final PreparedStatement getModule;
   private final PreparedStatement insertModule;
   private final PreparedStatement insertDocument;
   private final PreparedStatement getConceptDbid;
   private final PreparedStatement insertDraftConcept;
   private final PreparedStatement insertConcept;
   private final PreparedStatement updateConcept;
   private final PreparedStatement insertAxiom;
   private final PreparedStatement updateAxiom;
   private final PreparedStatement getClassAxiomIds;
   private final PreparedStatement getPropertyAxiomIds;
   private final PreparedStatement insertExpression;
   private final PreparedStatement updateExpression;
   private final PreparedStatement insertRestriction;
   private final PreparedStatement updateRestriction;

   private final Map<String, Integer> namespaceMap = new HashMap<>();
   private final Map<String, String> prefixMap = new HashMap<>();
   private final Map<String, Integer> conceptMap = new HashMap<>(1000000);
   private final List<Integer> useAxioms = new ArrayList<>();
   private final List<Integer> useExpressions = new ArrayList<>();
   private final List<Integer> useRestrictions = new ArrayList<>();


   private Integer moduleDbId;
   private Integer ontologyDbId;


   private final Connection conn;

   public OntologyFilerJDBCDAL() throws Exception {
      Map<String, String> envVars = System.getenv();

      String url = envVars.get("CONFIG_JDBC_URL");
      String user = envVars.get("CONFIG_JDBC_USERNAME");
      String pass = envVars.get("CONFIG_JDBC_PASSWORD");
      String driver = envVars.get("CONFIG_JDBC_CLASS");

      if (driver != null && !driver.isEmpty())
         Class.forName(driver);

      Properties props = new Properties();

      props.setProperty("user", user);
      props.setProperty("password", pass);

      conn = DriverManager.getConnection(url, props);    // NOSONAR
      getClassAxiomIds = conn.prepareStatement("SELECT  ax.dbid,exp.dbid,expp.dbid\n" +
          "        FROM axiom ax\n" +
          "        LEFT JOIN expression exp ON exp.axiom= ax.dbid\n" +
          "        LEFT JOIN expression_property expp ON expp.expression= exp.dbid\n" +
          "WHERE concept=? AND ax.module=? AND ax.type IN(0,1,7,15,16)\n" +
          "ORDER BY ax.dbid,exp.dbid");
      getPropertyAxiomIds = conn.prepareStatement("SELECT  ax.dbid,exp.dbid,expp.dbid\n" +
          "        FROM axiom ax\n" +
          "        LEFT JOIN expression exp ON exp.axiom= ax.dbid\n" +
          "        LEFT JOIN expression_property expp ON expp.expression= exp.dbid\n" +
          "WHERE concept=? AND ax.module=? AND ax.type IN(2,3,4,5,9,10,11,12,13,15,16)\n" +
          "ORDER BY ax.dbid,exp.dbid");
      getNamespace = conn.prepareStatement("SELECT dbid,prefix FROM namespace WHERE iri = ?");
      insertNamespace = conn.prepareStatement("INSERT INTO namespace (iri, prefix) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
      getOntology = conn.prepareStatement("SELECT dbid FROM ontology WHERE iri = ?");
      insertOntology = conn.prepareStatement("INSERT INTO ontology (iri) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      getModule = conn.prepareStatement("SELECT dbid FROM module WHERE iri = ?");
      insertModule = conn.prepareStatement("INSERT INTO module (iri) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      insertDocument = conn.prepareStatement("REPLACE INTO document (uuid, module, ontology) VALUES (?, ?, ?)");
      getConceptDbid = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
      insertDraftConcept = conn.prepareStatement("INSERT INTO concept (namespace, iri, module, type, status) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

      insertConcept = conn.prepareStatement("INSERT INTO concept (namespace, module,iri, name, description, type, code, scheme, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      updateConcept = conn.prepareStatement("UPDATE concept SET namespace= ? , module= ?, iri = ?, name = ?, description = ?, type = ?, code = ?, scheme = ?, status = ? WHERE dbid = ?");

      insertAxiom = conn.prepareStatement("INSERT INTO axiom (module, type, concept, version)\n" +
          "VALUES (?, ?, ?, ?)\n", Statement.RETURN_GENERATED_KEYS);
      updateAxiom = conn.prepareStatement("UPDATE axiom SET module=? , type=? , concept=? , version=?\n" +
          "WHERE dbid=?");
      insertExpression = conn.prepareStatement("INSERT INTO expression (type,axiom,parent,related_concept)\n"
          + "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      updateExpression = conn.prepareStatement("UPDATE expression set type=?, axiom=?,parent=?, related_concept=?\n"
          + "WHERE dbid=?");

      insertRestriction = conn.prepareStatement("INSERT INTO restriction(expression,property,\n"+
          "range_concept,inverse,min_cardinality,max_cardinality,data_value,range_expression)\n"+
          "VALUES(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
      updateRestriction = conn.prepareStatement("UPDATE restriction set expression=?\n"
      +"property=?,range_concept=?,inverse=?,min_cardinality=?,max_cardinality=?\n"
      +"data_value=?,range_expression=?\n"
      +"WHERE dbid=?");


   }

   public void startTransaction() throws SQLException {
      conn.setAutoCommit(false);
   }

   public void commit() throws SQLException {
      conn.commit();
   }

   public void rollBack() throws SQLException {
      conn.rollback();
   }

   // ------------------------------ NAMESPACE ------------------------------
   public void upsertNamespace(Namespace ns) throws SQLException {

      DALHelper.setString(getNamespace, 1, ns.getIri());
      try (ResultSet rs = getNamespace.executeQuery()) {
         if (rs.next()) {
            prefixMap.put(ns.getIri(), rs.getString("prefix"));
            prefixMap.put(ns.getPrefix(), rs.getString("prefix"));
            namespaceMap.put(ns.getPrefix(), rs.getInt("dbid"));
            namespaceMap.put(ns.getIri(), rs.getInt("dbid"));
         } else {
            createNamespace(ns);
         }
      }
   }

   private void createNamespace(Namespace ns) throws SQLException {
      DALHelper.setString(insertNamespace, 1, ns.getIri());
      DALHelper.setString(insertNamespace, 2, ns.getPrefix());
      insertNamespace.executeUpdate();
      Integer dbid = DALHelper.getGeneratedKey(insertNamespace);
      prefixMap.put(ns.getIri(), ns.getPrefix());
      prefixMap.put(ns.getPrefix(), ns.getPrefix());
      namespaceMap.put(ns.getIri(), dbid);
      namespaceMap.put(ns.getPrefix(), dbid);
   }

   // ------------------------------ ONTOLOGY ------------------------------
   public void upsertOntology(String iri) throws SQLException {
      DALHelper.setString(getOntology, 1, iri);
      try (ResultSet rs = getOntology.executeQuery()) {
         if (rs.next())
            ontologyDbId = rs.getInt("dbid");
         else
            createOntology(iri);
      }
   }

   private void createOntology(String iri) throws SQLException {
      DALHelper.setString(insertOntology, 1, iri);
      insertOntology.executeUpdate();
      ontologyDbId = DALHelper.getGeneratedKey(insertOntology);
   }

   // ------------------------------ ONTOLOGY ------------------------------
   public void addDocument(Ontology ontology) throws SQLException {
      DALHelper.setString(insertDocument, 1, ontology.getDocumentInfo().getDocumentId().toString());
      DALHelper.setInt(insertDocument, 2, moduleDbId);
      DALHelper.setInt(insertDocument, 3, ontologyDbId);
      if (insertDocument.executeUpdate() == 0) {
         throw new SQLException("Unable to record document meta data");
      }
   }

   // ------------------------------ MODULE ------------------------------
   public void upsertModule(String iri) throws SQLException {
      DALHelper.setString(getModule, 1, iri);
      try (ResultSet rs = getModule.executeQuery()) {
         if (rs.next())
            moduleDbId = rs.getInt("dbid");
         else
            createModule(iri);
      }
   }

   private void createModule(String iri) throws SQLException {
      DALHelper.setString(insertModule, 1, iri);
      insertModule.executeUpdate();
      moduleDbId = DALHelper.getGeneratedKey(insertModule);
   }

   // ------------------------------ Concept ------------------------------
   private Integer getConceptId(String iri) throws SQLException, Exception {
      if (Strings.isNullOrEmpty(iri))
         return null;
      Integer dbid = conceptMap.get(iri);
      if (dbid == null) {
         DALHelper.setString(getConceptDbid, 1, iri);
         try (ResultSet rs = getConceptDbid.executeQuery()) {
            if (rs.next()) {
               conceptMap.put(iri,rs.getInt("dbid"));
               return rs.getInt("dbid");
            }
            else {
               return createDraftConcept(iri);
            }
         }
      }
      return dbid;
   }

   public void upsertConcept(Concept concept) throws Exception {

      //reformats the document concept iri into the correct format
      concept.setIri(mapIri(concept.getIri()));
      Integer namespace = getNamespaceFromIri(concept.getIri());

      //Get Scheme and if not in db add new scheme concept
      Integer scheme = null;
      if (concept.getScheme() != null) {
         concept.setScheme(mapIri(concept.getScheme()));
         scheme = getConceptId(concept.getScheme());

      }

      if (concept.getStatus() == null)
         concept.setStatus(ConceptStatus.DRAFT);

      // Check for existing concept with same id
      //Changing an IRI is  high security function and not allowed via the filer
      Integer dbid = null;
      if (concept.getId() != null) {
         dbid = Integer.parseInt(concept.getId());
         if (getConceptId(concept.getIri()) != Integer.parseInt(concept.getId()))
            throw new Exception("cannot file a change of IRI with normal filer dbid= [" + concept.getId());
      }
      if (dbid == null) {
         if (concept.getIri() == null)
            throw new Exception("cannot have null dbid and null iri");
         DALHelper.setString(getConceptDbid, 1, concept.getIri());
         ResultSet rs = getConceptDbid.executeQuery();
         if (rs.next())
            dbid = rs.getInt("dbid");
      }

      int i = 0;
      ConceptType conceptType;
      if (concept instanceof Clazz)
         conceptType = ConceptType.CLASS;
      else if (concept instanceof ObjectProperty)
         conceptType = ConceptType.OBJECTPROPERTY;
      else if (concept instanceof DataProperty)
         conceptType = ConceptType.DATAPROPERTY;
      else if (concept instanceof AnnotationProperty)
         conceptType = ConceptType.ANNOTATION;
      else if (concept instanceof DataType)
         conceptType = ConceptType.DATATYPE;
      else if (concept instanceof Individual)
         conceptType = ConceptType.INDIVIDUAL;
      else
         conceptType = ConceptType.CLASS;

      if (dbid == null) {
         // Insert
         DALHelper.setInt(insertConcept, ++i, namespace);
         DALHelper.setInt(insertConcept, ++i, moduleDbId);
         DALHelper.setString(insertConcept, ++i, concept.getIri());
         DALHelper.setString(insertConcept, ++i, concept.getName());
         DALHelper.setString(insertConcept, ++i, concept.getDescription());
         DALHelper.setByte(insertConcept, ++i, conceptType.getValue());
         DALHelper.setString(insertConcept, ++i, concept.getCode());
         DALHelper.setInt(insertConcept, ++i, scheme);
         DALHelper.setByte(insertConcept, ++i, concept.getStatus().getValue());

         if (insertConcept.executeUpdate() == 0)
            throw new SQLException("Failed to insert concept [" + concept.getIri() + "]");
         else
            dbid = DALHelper.getGeneratedKey(insertConcept);
      } else {
         // Update
         DALHelper.setInt(updateConcept, ++i, namespace);
         DALHelper.setInt(updateConcept, ++i, moduleDbId);
         DALHelper.setString(updateConcept, ++i, concept.getIri());
         DALHelper.setString(updateConcept, ++i, concept.getName());
         DALHelper.setString(updateConcept, ++i, concept.getDescription());
         DALHelper.setByte(updateConcept, ++i, conceptType.getValue());
         DALHelper.setString(updateConcept, ++i, concept.getCode());
         DALHelper.setInt(updateConcept, ++i, scheme);
         DALHelper.setByte(updateConcept, ++i, concept.getStatus().getValue());
         DALHelper.setInt(updateConcept, ++i, dbid);

         if (updateConcept.executeUpdate() == 0)
            throw new SQLException("Failed to update concept [" + concept.getIri() + "]/[" + dbid.toString() + "]");
      }

      concept.setId(dbid.toString());
      conceptMap.put(concept.getIri(), dbid);
   }

   private Integer createDraftConcept(String iri) throws SQLException, Exception {
      Integer namespace = getNamespaceFromIri(iri);
      int i = 0;
      DALHelper.setInt(insertDraftConcept, ++i, namespace);
      DALHelper.setString(insertDraftConcept, ++i, iri);
      DALHelper.setInt(insertDraftConcept, ++i, moduleDbId);
      DALHelper.setByte(insertDraftConcept, ++i, ConceptType.CLASS.getValue());
      DALHelper.setByte(insertDraftConcept, ++i, ConceptStatus.DRAFT.getValue());

      try {
         if (insertDraftConcept.executeUpdate() == 0)
            throw new SQLException("Failed to save draft concept [" + iri + "]");
         int dbid = DALHelper.getGeneratedKey(insertDraftConcept);
         conceptMap.put(iri,dbid);
         return dbid;
      } catch (Exception e) {
         LOG.error("Failed to add draft concept [" + iri + "]");
         throw e;
      }
   }

   private void createAxiomCache(Concept concept) throws SQLException {
      useAxioms.clear();
      useExpressions.clear();
      useRestrictions.clear();
      ResultSet rs;
      if (concept instanceof Clazz) {
         DALHelper.setInt(getClassAxiomIds, 1, Integer.parseInt(concept.getId()));
         DALHelper.setInt(getClassAxiomIds, 2, moduleDbId);
         rs = getClassAxiomIds.executeQuery();
      } else {
         DALHelper.setInt(getPropertyAxiomIds, 1, Integer.parseInt(concept.getId()));
         DALHelper.setInt(getPropertyAxiomIds, 2, moduleDbId);
         rs = getPropertyAxiomIds.executeQuery();

      }

      if (rs != null) {
         int axid = 0, expid = 0, exppid = 0;
         while (rs.next()) {
            int id = rs.getInt("ax.dbid");
            if (id > axid) {
               useAxioms.add(id);
               axid = id;
            }
            id = rs.getInt("exp.dbid");
            if (id > expid) {
               useExpressions.add(id);
               expid = id;
            }
            id = rs.getInt("expp.dbid");
            if (id > exppid) {
               useRestrictions.add(id);
               exppid = id;
            }

         }
      }
   }


   public void upsertClassAxioms(Clazz clazz) throws Exception {
      createAxiomCache(clazz);
      Integer conceptId = Integer.parseInt(clazz.getId());
      Integer axiomId;
      if (clazz.getEquivalentTo() != null) {
         for (ClassAxiom ax : clazz.getEquivalentTo()) {
            if (!useAxioms.isEmpty()) {
               axiomId = updateConceptAxiom(useAxioms.get(0), moduleDbId, conceptId,
                   AxiomType.EQUIVALENTTO.getValue(), null);
               useAxioms.remove(0);
            } else {
               axiomId = addConceptAxiom(moduleDbId, conceptId, AxiomType.EQUIVALENTTO.getValue(), null);
            }

            upsertClassExpression(ax, axiomId, null);

         }
      }
      if (clazz.getSubClassOf() != null) {
         for (ClassAxiom ax : clazz.getSubClassOf()) {
            axiomId= upsertConceptAxiom(conceptId,AxiomType.SUBCLASSOF);
            upsertClassExpression(ax, axiomId, null);
         }
      }
      if (clazz.getDisjointWithClass() != null) {
         for (String disj : clazz.getDisjointWithClass()) {
            if (!useAxioms.isEmpty()) {
               axiomId = updateConceptAxiom(useAxioms.get(0), moduleDbId, conceptId,
                   AxiomType.DISJOINTWITH.getValue(), null);
               useAxioms.remove(0);
            } else
               axiomId = addConceptAxiom(moduleDbId, conceptId, AxiomType.DISJOINTWITH.getValue(), null);

            upsertExpressionIri(axiomId, null, ExpressionType.CLASS,disj);
         }
      }



   }


   public void upsertObjectPropertyAxioms(ObjectProperty objectProperty) throws Exception {
      createAxiomCache(objectProperty);
      Integer conceptId = Integer.parseInt(objectProperty.getId());
      Integer axiomId;
      if (objectProperty.getSubObjectPropertyOf()!=null) {
         for (PropertyAxiom ax : objectProperty.getSubObjectPropertyOf()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.SUBOBJECTPROPERTY);
            upsertExpressionIri(axiomId, null, ExpressionType.PROPERTY,ax.getProperty());
         }
      }
      if (objectProperty.getSubPropertyChain()!=null){
         for (SubPropertyChain chain: objectProperty.getSubPropertyChain()){
            axiomId= upsertConceptAxiom(conceptId,AxiomType.SUBPROPERTYCHAIN);
            upsertPropertyChain(axiomId,chain,null);
         }
      }
      if (objectProperty.getObjectPropertyRange()!=null){
         for (ClassExpression expression: objectProperty.getObjectPropertyRange()){
            axiomId=upsertConceptAxiom(conceptId,AxiomType.OBJECTPROPERTYRANGE);
            upsertClassExpression(expression,axiomId,null);
         }
      }
      if (objectProperty.getPropertyDomain()!=null){
         for (ClassAxiom domain: objectProperty.getPropertyDomain()){
            axiomId=upsertConceptAxiom(conceptId,AxiomType.PROPERTYDOMAIN);
            upsertClassExpression(domain,axiomId,null);
         }
      }
      if (objectProperty.getInversePropertyOf()!=null){
         axiomId=upsertConceptAxiom(conceptId,AxiomType.INVERSEPROPERTYOF);
         upsertExpressionIri(axiomId,null,ExpressionType.PROPERTY,
             objectProperty.getInversePropertyOf().getProperty());
      }
      if (objectProperty.getIsFunctional()!=null)
         upsertConceptAxiom(conceptId,AxiomType.ISFUNCTIONAL);
      if (objectProperty.getIsReflexive()!=null)
         upsertConceptAxiom(conceptId,AxiomType.ISREFLEXIVE);
      if (objectProperty.getIsSymmetric()!=null)
         upsertConceptAxiom(conceptId,AxiomType.ISSYMMETRIC);
      if (objectProperty.getIsTransitive()!=null)
         upsertConceptAxiom(conceptId,AxiomType.ISTRANSITIVE);
   }

   public void upsertDataPropertyAxioms(DataProperty dataProperty) throws Exception {
      createAxiomCache(dataProperty);
      Integer conceptId = Integer.parseInt(dataProperty.getId());
      Integer axiomId;
      if (dataProperty.getSubDataPropertyOf() != null) {
         for (PropertyAxiom ax : dataProperty.getSubDataPropertyOf()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.SUBDATAPROPERTY);
            upsertExpressionIri(axiomId, null, ExpressionType.PROPERTY, ax.getProperty());
         }
      }

      if (dataProperty.getDataPropertyRange() != null) {
         for (DataRangeAxiom ax : dataProperty.getDataPropertyRange()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.DATAPROPERTYRANGE);
            upsertDataRangeExpression(ax, axiomId);
         }
      }
   }

   private Integer upsertDataRangeExpression(DataRangeAxiom ax, Integer axiomId) {
      Integer expressionId;
      return null;
   }


   private void upsertPropertyChain(Integer axiomId,SubPropertyChain chain,Integer parent) throws Exception {
      Integer expressionId;
      if (chain.getProperty()!=null)
         for (String property:chain.getProperty()) {
            if (!useExpressions.isEmpty()) {
               expressionId = updateAxiomExpression(useExpressions.get(0), ExpressionType.PROPERTY, axiomId, parent, property);
               useExpressions.remove(0);
            } else
               expressionId = addAxiomExpression(ExpressionType.PROPERTY, axiomId, parent, property);
            parent = expressionId;
         }
   }

   private Integer upsertConceptAxiom(Integer conceptId,AxiomType axiomType) throws SQLException {
      Integer axiomId;
      if (!useAxioms.isEmpty()) {
         axiomId = updateConceptAxiom(useAxioms.get(0), moduleDbId, conceptId,
             axiomType.getValue(), null);
         useAxioms.remove(0);
      } else {
         axiomId = addConceptAxiom(moduleDbId, conceptId, axiomType.getValue(), null);
      }
      return axiomId;
   }



   /**
    * Adds a simple expression with only an iri
    *
    * @param axiomId
    * @param parent
    * @param valueIri
    * @throws SQLException
    */
   private Integer upsertExpressionIri(Integer axiomId, Integer parent, ExpressionType expType, String valueIri) throws Exception {
      Integer expressionId;
      if (!useExpressions.isEmpty()) {
         expressionId = updateAxiomExpression(useExpressions.get(0), expType, axiomId, parent, valueIri);
         useExpressions.remove(0);
      } else
         expressionId = addAxiomExpression(expType, axiomId, parent, valueIri);
      return expressionId;
   }

   private Integer addAxiomExpression(ExpressionType expType, Integer axiomId, Integer parent, String valueIri) throws Exception {
      int i = 0;
      DALHelper.setByte(insertExpression, ++i, expType.getValue());
      DALHelper.setInt(insertExpression, ++i, axiomId);
      DALHelper.setInt(insertExpression, ++i, parent);
      DALHelper.setInt(insertExpression, ++i, getConceptId(valueIri));
      if (insertExpression.executeUpdate() == 0)
         throw new SQLException("Failed to save expression ["
             + axiomId.toString() + expType.getName()
             + "]");

      return DALHelper.getGeneratedKey(insertExpression);

   }

   private Integer updateAxiomExpression(Integer expressionId, ExpressionType expType, Integer axiomId, Integer parent, String valueIri) throws Exception {
      int i = 0;
      DALHelper.setByte(updateExpression, ++i, expType.getValue());
      DALHelper.setInt(updateExpression, ++i, axiomId);
      DALHelper.setInt(updateExpression, ++i, parent);
      DALHelper.setInt(updateExpression, ++i, getConceptId(valueIri));
      if (updateExpression.executeUpdate() == 0)
         throw new SQLException("Failed to save expression ["
             + axiomId.toString() + expType.getName()
             + "]");
      return expressionId;
   }

   private Integer upsertClassExpression(ClassExpression exp, Integer axiomId, Integer parent) throws Exception {
      ExpressionType expType;
      Integer expressionId = null;

      if (exp.getClazz() != null)
         return upsertExpressionIri(axiomId, parent, exp.getClazz());

      else if (exp.getIntersection() != null) {
         if (!useExpressions.isEmpty()) {
            expressionId = updateAxiomExpression(useExpressions.get(0), ExpressionType.INTERSECTION, axiomId, parent, null);
            useExpressions.remove(0);
         } else
            expressionId = addAxiomExpression(ExpressionType.INTERSECTION, axiomId, parent, null);

         for (ClassExpression inter : exp.getIntersection())
            upsertClassExpression(inter, axiomId, expressionId);

      } else if (exp.getUnion() != null) {
         if (!useExpressions.isEmpty()) {
            expressionId = updateAxiomExpression(useExpressions.get(0), ExpressionType.INTERSECTION, axiomId, parent, null);
            useExpressions.remove(0);
         } else
            expressionId = addAxiomExpression(ExpressionType.INTERSECTION, axiomId, parent, null);

         for (ClassExpression union : exp.getUnion())
            upsertClassExpression(union, axiomId, expressionId);

      } else if (exp.getPropertyObject() != null) {
         if (!useExpressions.isEmpty()) {
            expressionId = updateAxiomExpression(useExpressions.get(0), ExpressionType.PROPERTYOBJECT, axiomId, parent, null);
            useExpressions.remove(0);
         } else
            expressionId = addAxiomExpression(ExpressionType.PROPERTYOBJECT, axiomId, parent, null);

         upsertRestriction(axiomId,expressionId,exp.getPropertyObject());

      } else if (exp.getPropertyData() != null) {
         if (!useExpressions.isEmpty()) {
            expressionId = updateAxiomExpression(useExpressions.get(0), ExpressionType.PROPERTYDATA, axiomId, parent, null);
            useExpressions.remove(0);
         } else
            expressionId = addAxiomExpression(ExpressionType.PROPERTYDATA, axiomId, parent, null);

         upsertRestriction(expressionId,exp.getPropertyData());

      } else if (exp.getComplementOf() != null) {
         if (!useExpressions.isEmpty()) {
            expressionId = updateAxiomExpression(useExpressions.get(0), ExpressionType.COMPLEMENTOF, axiomId, parent, null);
            useExpressions.remove(0);
         } else
            expressionId = addAxiomExpression(ExpressionType.COMPLEMENTOF, axiomId, parent, null);

         upsertClassExpression(exp.getComplementOf(),axiomId,expressionId);

      } else if (exp.getObjectOneOf() != null) {
         if (!useExpressions.isEmpty()) {
            expressionId = updateAxiomExpression(useExpressions.get(0), ExpressionType.OBJECTONEOF, axiomId, parent, null);
            useExpressions.remove(0);
         } else
            expressionId = addAxiomExpression(ExpressionType.OBJECTONEOF, axiomId, parent, null);

         for (String oneOf:exp.getObjectOneOf())
            upsertExpressionIri(axiomId,expressionId,oneOf);
      } else
         throw new Exception("invalid class expression axiom id ["+ axiomId.toString()+"]");

      return expressionId;
   }

   private Integer upsertRestriction(Integer axiomId,Integer expressionId, OPECardinalityRestriction po) throws Exception {
      String rangeConcept=null;
      Integer rangeExpression=null;
      byte inverse=0;
      if (po.getInverseOf()!=null)
         inverse=1;
      if (po.getClazz()!=null)
         rangeConcept= po.getClazz();
      else
         rangeExpression=upsertClassExpression(po,axiomId,expressionId);

      return upsertRestriction(expressionId,po.getProperty(),
          rangeConcept,inverse,po.getMin(),po.getMax(),null,rangeExpression);

   }
   private Integer upsertRestriction(Integer expressionId, DPECardinalityRestriction pd) throws Exception {
      
      return upsertRestriction(expressionId,pd.getProperty(),
          pd.getDataType(), (byte) 0,
          pd.getMin(),pd.getMax(),pd.getExactValue(),null);
   }
   private Integer upsertRestriction(Integer expressionId,String property,String rangeConcept,
                                    byte inverse, Integer min, Integer max,
                                    String data, Integer rangeExpression) throws Exception {
      Integer restrictionId;
      if (!useRestrictions.isEmpty()){
            restrictionId = updateRestriction(useRestrictions.get(0), expressionId,property,
                                        rangeConcept,inverse,min,max,data,rangeExpression);
            useRestrictions.remove(0);
         } else
            restrictionId = addRestriction(expressionId,property,rangeConcept,
                                           inverse,min,max,data,rangeExpression);
      return restrictionId;

      }

   private Integer addRestriction(Integer expressionId, String property, String rangeConcept, byte inverse, Integer min, Integer max, String data, Integer rangeExpression) throws Exception {
      int i=0;
      DALHelper.setInt(insertRestriction,++i, expressionId);
      DALHelper.setInt(insertRestriction,++i,getConceptId(property));
      DALHelper.setInt(insertRestriction,++i,getConceptId(rangeConcept));
      DALHelper.setByte(insertRestriction,++i,inverse);
      DALHelper.setInt(insertRestriction,++i,min);
      DALHelper.setInt(insertRestriction,++i,max);
      DALHelper.setString(insertRestriction,++i,data);
      DALHelper.setInt(insertRestriction,++i,rangeExpression);
      if (insertRestriction.executeUpdate() == 0)
         throw new SQLException("Failed to save property restriction ["
             + property + "]");

      return DALHelper.getGeneratedKey(insertRestriction);

   }

   private Integer updateRestriction(Integer restrictionId, Integer expressionId, String property, String rangeConcept, byte inverse, Integer min, Integer max, String data, Integer rangeExpression) throws Exception {
      int i=0;
      DALHelper.setInt(updateRestriction,++i, expressionId);
      DALHelper.setInt(updateRestriction,++i,getConceptId(property));
      DALHelper.setInt(updateRestriction,++i,getConceptId(rangeConcept));
      DALHelper.setByte(updateRestriction,++i,inverse);
      DALHelper.setInt(updateRestriction,++i,min);
      DALHelper.setInt(updateRestriction,++i,max);
      DALHelper.setString(updateRestriction,++i,data);
      DALHelper.setInt(updateRestriction,++i,rangeExpression);
      DALHelper.setInt(updateRestriction,++i, restrictionId);
      if (updateRestriction.executeUpdate() == 0)
         throw new SQLException("Failed to save property restriction ["
             + property + "]");
      return restrictionId;

}


   // ------------------------------ Concept Axiom ------------------------------
   public Integer addConceptAxiom(Integer moduleDbid, Integer conceptDbid,
                                  byte axiomType,
                                  Integer version) throws SQLException {
      int i = 0;
      DALHelper.setInt(insertAxiom, ++i, moduleDbid);
      DALHelper.setByte(insertAxiom, ++i, axiomType);
      DALHelper.setInt(insertAxiom, ++i, conceptDbid);
      DALHelper.setInt(insertAxiom, ++i, version);
      if (insertAxiom.executeUpdate() == 0)
         throw new SQLException("Failed to save concept axiom ["
             + conceptDbid.toString()
             + "]");

      return DALHelper.getGeneratedKey(insertAxiom);
   }

   public Integer updateConceptAxiom(Integer axiomid, Integer moduleDbid, Integer conceptDbid,
                                     byte axiomType,
                                     Integer version) throws SQLException {
      int i = 0;
      DALHelper.setInt(updateAxiom, ++i, moduleDbid);
      DALHelper.setByte(updateAxiom, ++i, axiomType);
      DALHelper.setInt(updateAxiom, ++i, conceptDbid);
      DALHelper.setInt(updateAxiom, ++i, version);
      DALHelper.setInt(updateAxiom, ++i, axiomid);
      if (updateAxiom.executeUpdate() == 0)
         throw new SQLException("Failed to save concept axiom ["
             + conceptDbid.toString()
             + "]");
      return axiomid;

   }


   // ------------------------------ Helper/Util ------------------------------

   private String mapIri(String iri) throws Exception {
      if (iri.startsWith("http")) {
         int hash = iri.indexOf("#");
         if (hash > -1) {
            String nsIri = iri.substring(0, hash) + "#";
            String prefix = prefixMap.get(nsIri);
            if (prefix == null)
               throw new Exception("iri appears not to have a valid prefix [" + iri + "]");
            return prefix + iri.substring(hash + 1);
         } else {
            int slash = iri.lastIndexOf("/");
            if (slash > -1) {
               return iri;
            } else
               throw new Exception("iri not in normal format [" + iri + "]");
         }
      }
      int colon = iri.lastIndexOf(":");
      if (colon > -1) {
         String clientPrefix = iri.substring(0, colon) + ":";
         String prefix = prefixMap.get(clientPrefix);
         if (prefix == null)
            throw new Exception("iri prefix appears not to have been declared [" + iri + "]");
         return prefix + iri.substring(colon + 1);
      }
      throw new Exception("iri format not standard [" + iri + "]");
   }


   //Assumes that the prefix in the client IRI has already been mapped
   private Integer getNamespaceFromIri(String iri) throws Exception {
      //If it is a ":" prefix then get namespace dbid from prefix
      //if it is  http iri then return the empty namespace dbid
      int colon = iri.lastIndexOf(":");
      if (colon > -1) {
         Integer result = namespaceMap.get(iri.substring(0, colon) + ":");
         if (result != null)
            return result;
         else
            return namespaceMap.get("");
      } else
         throw new Exception("unable to derive namespace from iri [" + iri + "]");

   }

}