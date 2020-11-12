package org.endeavourhealth.informationmanager;

import com.google.common.base.Strings;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;
import org.endeavourhealth.informationmanager.common.models.*;
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
   private final PreparedStatement getUsedAxiomIds;
   private final PreparedStatement insertExpression;
   private final PreparedStatement updateExpression;
   private final PreparedStatement insertPropertyValue;
   private final PreparedStatement updatePropertyValue;
   private final PreparedStatement insertDTDefinition;
   private final PreparedStatement updateDTDefinition;

   private final Map<String, Integer> namespaceMap = new HashMap<>();
   private final Map<String, String> prefixMap = new HashMap<>();
   private final Map<String, Integer> conceptMap = new HashMap<>(1000000);
   private final List<Integer> useAxioms = new ArrayList<>();
   private final List<Integer> useExpressions = new ArrayList<>();
   private final List<Integer> usePropertyValues = new ArrayList<>();
   private final List<Integer> useDataTypes = new ArrayList<>();


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
      getUsedAxiomIds = conn.prepareStatement(
          "SELECT  ax.dbid,exp.dbid,card.dbid,dtd.dbid\n" +
              "            FROM concept c\n" +
              "            LEFT JOIN axiom ax on ax.concept= c.dbid\n" +
              "            lEFT JOIN expression exp ON exp.axiom= ax.dbid\n" +
              "            LEFT JOIN property_value card ON card.expression= exp.dbid\n" +
              "            LEFT JOIN datatype_definition dtd on dtd.concept= c.dbid\n" +
              "            WHERE ax.concept=? AND ax.module=? ORDER BY ax.dbid,exp.dbid");
      getNamespace = conn.prepareStatement("SELECT dbid,prefix FROM namespace WHERE iri = ?");
      insertNamespace = conn.prepareStatement("INSERT INTO namespace (iri, prefix) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
      getOntology = conn.prepareStatement("SELECT dbid FROM ontology WHERE iri = ?");
      insertOntology = conn.prepareStatement("INSERT INTO ontology (iri) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      getModule = conn.prepareStatement("SELECT dbid FROM module WHERE iri = ?");
      insertModule = conn.prepareStatement("INSERT INTO module (iri) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      insertDocument = conn.prepareStatement("REPLACE INTO document (uuid, module, ontology) VALUES (?, ?, ?)");
      getConceptDbid = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
      insertDraftConcept = conn.prepareStatement("INSERT INTO concept (namespace, iri, module, type, status) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      insertConcept = conn.prepareStatement("INSERT INTO concept (namespace, module,iri, name, description, type, code, scheme, status) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      updateConcept = conn.prepareStatement("UPDATE concept SET namespace= ? , module= ?, iri = ?, name = ?, description = ?, type = ?, code = ?, scheme = ?, status = ? WHERE dbid = ?");
      insertAxiom = conn.prepareStatement("INSERT INTO axiom (module, type, concept, version)\n" +
          "VALUES (?, ?, ?, ?)\n", Statement.RETURN_GENERATED_KEYS);
      updateAxiom = conn.prepareStatement("UPDATE axiom SET module=? , type=? , concept=? , version=?\n" +
          "WHERE dbid=?");
      insertExpression = conn.prepareStatement("INSERT INTO expression (type,axiom,parent,target_concept)\n"
          + "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      updateExpression = conn.prepareStatement("UPDATE expression set type=?, axiom=?,parent=?, target_concept=?\n"
          + "WHERE dbid=?");

      insertPropertyValue = conn.prepareStatement("INSERT INTO property_value(expression,property,\n" +
          "value_type,inverse,min_cardinality,max_cardinality,value_data,value_expression)\n" +
          "VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      updatePropertyValue = conn.prepareStatement("UPDATE property_value set expression=?\n"
          + "property=?,value_type=?,inverse=?,min_cardinality=?,max_cardinality=?\n"
          + "value_data=?,value_expression=?\n"
          + "WHERE dbid=?");
      insertDTDefinition = conn.prepareStatement(
          "INSERT INTO datatype_definition(concept,module,min_operator,min_value\n" +
              "max_operator,max_value,pattern)\n" +
              "   VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      updateDTDefinition = conn.prepareStatement(
          "UPDATE datatype_definition set concept=? ,module=?, min_operator=?,min_value=?\n" +
              "max_operator=?,max_value=?,pattern=?\n" +
              "WHERE dbid=?");
   }

   public void startTransaction() throws SQLException {
      conn.setAutoCommit(false);
   }

   public void commit() throws SQLException {
      conn.commit();
   }
   public void close() throws SQLException {
      conn.close();
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
               conceptMap.put(iri, rs.getInt("dbid"));
               return rs.getInt("dbid");
            } else {
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
         concept.setScheme(mapIri(concept.getScheme().getIri()));
         scheme = getConceptId(concept.getScheme().getIri());

      }

      if (concept.getStatus() == null)
         concept.setStatus(ConceptStatus.DRAFT);

      // Check for existing concept with same id
      // Changing an IRI is high security function and not allowed via the filer
      Integer dbid = concept.getDbid();
      if (dbid != null) {
         if (getConceptId(concept.getIri()) != dbid)
            throw new Exception("cannot file a change of IRI with normal filer dbid= [" + dbid + "]");
      }

      if (dbid == null) {
         if (concept.getIri() == null)
            throw new Exception("cannot have null dbid and null iri");

         DALHelper.setString(getConceptDbid, 1, concept.getIri());
         ResultSet rs = getConceptDbid.executeQuery();
         if (rs.next()) {
            dbid = rs.getInt("dbid");
            concept.setDbid(dbid);
         }
      }

      int i = 0;
      ConceptType conceptType;
      conceptType = concept.getConceptType();

      if (dbid == null) {
         // Insert
         DALHelper.setInt(insertConcept, ++i, namespace);
         DALHelper.setInt(insertConcept, ++i, moduleDbId);
         DALHelper.setByte(insertConcept, ++i, conceptType.getValue());
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

      concept.setDbid(dbid);
      conceptMap.put(concept.getIri(), dbid);
   }

   private Integer createDraftConcept(String iri) throws SQLException, Exception {
      Integer namespace = getNamespaceFromIri(iri);
      int i = 0;
      DALHelper.setInt(insertDraftConcept, ++i, namespace);
      DALHelper.setString(insertDraftConcept, ++i, iri);
      DALHelper.setInt(insertDraftConcept, ++i, moduleDbId);
      DALHelper.setByte(insertDraftConcept, ++i, ConceptType.CLASSONLY.getValue());
      DALHelper.setByte(insertDraftConcept, ++i, ConceptStatus.DRAFT.getValue());

      try {
         if (insertDraftConcept.executeUpdate() == 0)
            throw new SQLException("Failed to save draft concept [" + iri + "]");
         int dbid = DALHelper.getGeneratedKey(insertDraftConcept);
         conceptMap.put(iri, dbid);
         return dbid;
      } catch (Exception e) {
         LOG.error("Failed to add draft concept [" + iri + "]");
         throw e;
      }
   }

   private void createAxiomCache(Concept concept) throws SQLException {
      useAxioms.clear();
      useExpressions.clear();
      usePropertyValues.clear();
      useDataTypes.clear();
      ResultSet rs;
      DALHelper.setInt(getUsedAxiomIds, 1, concept.getDbid());
      DALHelper.setInt(getUsedAxiomIds, 2, moduleDbId);
      rs = getUsedAxiomIds.executeQuery();

      if (rs != null) {
         int axid = 0, expid = 0, cardid = 0, dtdid = 0;
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
            id = rs.getInt("card.dbid");
            if (id > cardid) {
               usePropertyValues.add(id);
               cardid = id;
            }
            id = rs.getInt("dtd.dbid");
            if (id > dtdid)
               useDataTypes.add(id);
         }
      }
   }


   public void fileAxioms(Concept concept) throws Exception {
      createAxiomCache(concept);
      ConceptType conceptType = concept.getConceptType();
      upsertClassAxioms(concept);
      if (conceptType == ConceptType.OBJECTPROPERTY)
         fileObjectPropertyAxioms((ObjectProperty) concept);
      else if (conceptType == ConceptType.DATAPROPERTY)
         fileDataPropertyAxioms((DataProperty) concept);
      else if (conceptType == ConceptType.DATATYPE) {
         DataType dataType = (DataType) concept;
         if (dataType.getDataTypeDefinition()!=null)
            upsertDataTypeDefinition((DataType) concept);
      } else if (conceptType==ConceptType.ANNOTATION)
         fileAnnotationPropertyAxioms((AnnotationProperty) concept);
   }

   private void upsertClassAxioms(Concept concept) throws Exception {
      Integer conceptId = concept.getDbid();
      Integer axiomId;
      if (concept.getEquivalentTo() != null) {
         for (ClassAxiom ax : concept.getEquivalentTo()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.EQUIVALENTTO);
            fileClassExpression(ax, axiomId, null);
         }
      }
      if (concept.getSubClassOf() != null) {
         for (ClassAxiom ax : concept.getSubClassOf()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.SUBCLASSOF);
            fileClassExpression(ax, axiomId, null);
         }
      }
      if (concept.getDisjointWith() != null) {
         for (ConceptReference disj : concept.getDisjointWith()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.DISJOINTWITH);
            upsertExpression(axiomId,null,ExpressionType.CLASS,disj.getIri());
         }
      }
   }
   private void fileAnnotationPropertyAxioms(AnnotationProperty ap) throws Exception {
      Integer conceptId = ap.getDbid();
      Integer axiomId;
      if (ap.getSubAnnotationPropertyOf() != null) {
         for (PropertyAxiom ax : ap.getSubAnnotationPropertyOf()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.SUBOBJECTPROPERTY);
            upsertExpression(axiomId, null, ExpressionType.PROPERTY, ax.getProperty().getIri());
         }
      }
   }

   private void fileObjectPropertyAxioms(ObjectProperty op) throws Exception {
      Integer conceptId= op.getDbid();
      Integer axiomId;
      if (op.getSubObjectPropertyOf() != null) {
         for (PropertyAxiom ax : op.getSubObjectPropertyOf()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.SUBOBJECTPROPERTY);
            upsertExpression(axiomId, null, ExpressionType.PROPERTY, ax.getProperty().getIri());
         }
      }
      if (op.getSubPropertyChain() != null) {
         for (SubPropertyChain chain : op.getSubPropertyChain()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.SUBPROPERTYCHAIN);
            filePropertyChain(axiomId, chain, null);
         }
      }
      if (op.getObjectPropertyRange() != null) {
         for (ClassExpression expression : op.getObjectPropertyRange()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.OBJECTPROPERTYRANGE);
            fileClassExpression(expression, axiomId, null);
         }
      }
      if (op.getPropertyDomain() != null) {
         for (ClassAxiom domain : op.getPropertyDomain()) {
            axiomId = upsertConceptAxiom(conceptId, AxiomType.PROPERTYDOMAIN);
            fileClassExpression(domain, axiomId, null);
         }
      }
      if (op.getInversePropertyOf() != null) {
         axiomId = upsertConceptAxiom(conceptId, AxiomType.INVERSEPROPERTYOF);
         upsertExpression(axiomId, null, ExpressionType.PROPERTY,
             op.getInversePropertyOf().getProperty().getIri());
      }
      if (op.getIsFunctional() != null)
         upsertConceptAxiom(conceptId, AxiomType.ISFUNCTIONAL);
      if (op.getIsReflexive() != null)
         upsertConceptAxiom(conceptId, AxiomType.ISREFLEXIVE);
      if (op.getIsSymmetric() != null)
         upsertConceptAxiom(conceptId, AxiomType.ISSYMMETRIC);
      if (op.getIsTransitive() != null)
         upsertConceptAxiom(conceptId, AxiomType.ISTRANSITIVE);
   }

   private void fileDataPropertyAxioms(DataProperty dp) throws Exception {
      Integer conceptId= dp.getDbid();
      Integer axiomId;
         if (dp.getSubDataPropertyOf() != null) {
            for (PropertyAxiom ax : dp.getSubDataPropertyOf()) {
               axiomId = upsertConceptAxiom(conceptId, AxiomType.SUBDATAPROPERTY);
               upsertExpression(axiomId, null, ExpressionType.PROPERTY, ax.getProperty().getIri());
            }
         }
         if (dp.getDataPropertyRange() != null) {
            for (DataPropertyRange ax : dp.getDataPropertyRange()) {
               axiomId = upsertConceptAxiom(conceptId, AxiomType.DATAPROPERTYRANGE);
               upsertExpression(axiomId,null,
                   ExpressionType.DATATYPE,ax.getDataType().getIri());
            }
         }
         if (dp.getPropertyDomain() != null) {
            for (ClassAxiom domain : dp.getPropertyDomain()) {
               axiomId = upsertConceptAxiom(conceptId, AxiomType.PROPERTYDOMAIN);
               fileClassExpression(domain, axiomId, null);
            }
         }
   }



   private Integer upsertDataTypeDefinition(DataType dataType) throws Exception {
      Integer dtId= dataType.getDbid();
      Integer dtdefId;
      DataTypeDefinition dtdef= dataType.getDataTypeDefinition();
      if (!useDataTypes.isEmpty()) {
         dtdefId=useDataTypes.get(0);
         int i = 0;
         DALHelper.setInt(updateDTDefinition, ++i, moduleDbId);
         DALHelper.setString(updateDTDefinition, ++i, dtdef.getMinOperator());
         DALHelper.setString(updateDTDefinition, ++i, dtdef.getMinValue());
         DALHelper.setString(updateDTDefinition, ++i, dtdef.getMaxOperator());
         DALHelper.setString(updateDTDefinition, ++i, dtdef.getMaxValue());
         DALHelper.setString(updateDTDefinition, ++i, dtdef.getPattern());
         DALHelper.setInt(updateDTDefinition, ++i, dtdefId);
         if (updateDTDefinition.executeUpdate() == 0)
            throw new SQLException("Failed to save concept axiom ["
                + dtId.toString()
                + "]");
         useDataTypes.remove(0);
         return dtdefId;
      } else {
         int i = 0;

         DALHelper.setInt(insertDTDefinition, ++i, moduleDbId);
         DALHelper.setString(insertDTDefinition, ++i, dtdef.getMinOperator());
         DALHelper.setString(insertDTDefinition, ++i, dtdef.getMinValue());
         DALHelper.setString(insertDTDefinition, ++i, dtdef.getMaxOperator());
         DALHelper.setString(insertDTDefinition, ++i, dtdef.getMaxValue());
         DALHelper.setString(insertDTDefinition, ++i, dtdef.getPattern());
         if (insertDTDefinition.executeUpdate() == 0)
            throw new SQLException("Failed to save concept axiom ["
                + dtId.toString()
                + "]");
         return DALHelper.getGeneratedKey(insertAxiom);
      }
   }


   private void filePropertyChain(Integer axiomId,SubPropertyChain chain,Integer parent) throws Exception {
      Integer expressionId;
      if (chain.getProperty()!=null)
         for (ConceptReference property:chain.getProperty()) {
            expressionId = upsertExpression(axiomId,
                parent,
                ExpressionType.PROPERTY,
                property.getIri());
            parent = expressionId;
         }
   }

   private Integer upsertConceptAxiom(Integer conceptId,AxiomType axiomType) throws SQLException {
      Integer axiomId;
      if (!useAxioms.isEmpty()) {
         axiomId=useAxioms.get(0);
         int i = 0;
         DALHelper.setInt(updateAxiom, ++i, moduleDbId);
         DALHelper.setByte(updateAxiom, ++i, axiomType.getValue());
         DALHelper.setInt(updateAxiom, ++i, conceptId);
         DALHelper.setInt(updateAxiom, ++i, null);
         DALHelper.setInt(updateAxiom, ++i, axiomId);
         if (updateAxiom.executeUpdate() == 0)
            throw new SQLException("Failed to save concept axiom ["
                + conceptId.toString()
                + "]");
         useAxioms.remove(0);
         return axiomId;
      } else {
         int i = 0;
         DALHelper.setInt(insertAxiom, ++i, moduleDbId);
         DALHelper.setByte(insertAxiom, ++i, axiomType.getValue());
         DALHelper.setInt(insertAxiom, ++i, conceptId);
         DALHelper.setInt(insertAxiom, ++i, null);
         if (insertAxiom.executeUpdate() == 0)
            throw new SQLException("Failed to save concept axiom ["
                + conceptId.toString()
                + "]");

         return DALHelper.getGeneratedKey(insertAxiom);
      }
   }



   /**
    * Adds a simple expression with only an iri
    *
    * @param axiomId
    * @param parent
    * @param valueIri
    * @throws SQLException
    */
   private Integer upsertExpression(Integer axiomId, Integer parent, ExpressionType expType, String valueIri) throws Exception {
      if (!useExpressions.isEmpty()) {
         Integer expressionId = useExpressions.get(0);
         int i = 0;
         DALHelper.setByte(updateExpression, ++i, expType.getValue());
         DALHelper.setInt(updateExpression, ++i, axiomId);
         DALHelper.setInt(updateExpression, ++i, parent);
         DALHelper.setInt(updateExpression, ++i, getConceptId(valueIri));
         DALHelper.setInt(updateExpression, ++i, expressionId);
         if (updateExpression.executeUpdate() == 0)
            throw new SQLException("Failed to save expression ["
                + axiomId.toString() + expType.getName()
                + "]");
         useExpressions.remove(expressionId);
         return expressionId;
      } else {
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
   }


   private Integer fileClassExpression(ClassExpression exp, Integer axiomId, Integer parent) throws Exception {
      ExpressionType expType;
      Integer expressionId = null;
      if (exp.getClazz() != null)
         return upsertExpression(axiomId, parent, ExpressionType.CLASS, exp.getClazz().getIri());

      else if (exp.getIntersection() != null) {
         expressionId = upsertExpression(axiomId,parent,ExpressionType.INTERSECTION, null);
         for (ClassExpression inter : exp.getIntersection())
            fileClassExpression(inter, axiomId, expressionId);

      } else if (exp.getUnion() != null) {
         expressionId = upsertExpression(axiomId,parent,ExpressionType.UNION, null);
         for (ClassExpression union : exp.getUnion())
            fileClassExpression(union, axiomId, expressionId);

      } else if (exp.getObjectPropertyValue() != null) {
         upsertObjectPropertyValue(axiomId,parent,exp);

      } else if (exp.getDataPropertyValue() != null) {
         upsertDataPropertyValue(axiomId,parent,exp);

      } else if (exp.getComplementOf() != null){
            expressionId = upsertExpression(axiomId, parent, ExpressionType.COMPLEMENTOF, null);
            fileClassExpression(exp.getComplementOf(), axiomId, expressionId);

      } else if (exp.getObjectOneOf() != null) {
         for (ConceptReference oneOf:exp.getObjectOneOf())
            upsertExpression(axiomId,parent,ExpressionType.OBJECTONEOF, oneOf.getIri());
      } else
         throw new Exception("invalid class expression axiom id ["+ axiomId.toString()+"]");

      return expressionId;
   }

   private Integer upsertObjectPropertyValue(Integer axiomId,Integer parent,ClassExpression exp) throws Exception {
      ObjectPropertyValue po= exp.getObjectPropertyValue();
      QuantificationType quant=po.getQuantificationType();
      ExpressionType expType;
      Integer expressionId= upsertExpression(axiomId,parent,ExpressionType.OBJECTPROPERTYVALUE,null);
      Integer valueType=null;
      Integer valueExpression=null;

      byte inverse=0;
      if (po.getInverseOf()!=null)
         inverse=1;

      if (po.getValueType()!=null)
         valueType= getConceptId(po.getValueType().getIri());
      else
         valueExpression=fileClassExpression(po.getExpression(),axiomId,expressionId);

      if (!usePropertyValues.isEmpty()){
         int i=0;
         Integer propertyValueId= usePropertyValues.get(0);
         DALHelper.setInt(updatePropertyValue,++i, expressionId);
         DALHelper.setInt(updatePropertyValue,++i,getConceptId(po.getProperty().getIri()));
         DALHelper.setInt(updatePropertyValue,++i,valueType);
         DALHelper.setByte(updatePropertyValue,++i,inverse);
         DALHelper.setInt(updatePropertyValue,++i,po.getMin());
         DALHelper.setInt(updatePropertyValue,++i,po.getMax());
         DALHelper.setString(updatePropertyValue,++i,null);
         DALHelper.setInt(updatePropertyValue,++i,valueExpression);
         DALHelper.setInt(updatePropertyValue,++i, propertyValueId);
         if (updatePropertyValue.executeUpdate() == 0)
            throw new SQLException("Failed to save property PropertyValue ["
                + po.getProperty() + "]");
         usePropertyValues.remove(0);
         return propertyValueId;
      } else {
         int i = 0;
         DALHelper.setInt(insertPropertyValue, ++i, expressionId);
         DALHelper.setInt(insertPropertyValue, ++i, getConceptId(po.getProperty().getIri()));
         DALHelper.setInt(insertPropertyValue, ++i, valueType);
         DALHelper.setByte(insertPropertyValue, ++i, inverse);
         DALHelper.setInt(insertPropertyValue, ++i, po.getMin());
         DALHelper.setInt(insertPropertyValue, ++i, po.getMax());
         DALHelper.setString(insertPropertyValue, ++i, null);
         DALHelper.setInt(insertPropertyValue, ++i, valueExpression);
         if (insertPropertyValue.executeUpdate() == 0)
            throw new SQLException("Failed to save property PropertyValue ["
                + po.getProperty() + "]");

         return DALHelper.getGeneratedKey(insertPropertyValue);

      }
   }

   private Integer upsertDataPropertyValue(Integer axiomId,Integer parent,ClassExpression exp) throws Exception {
      DataPropertyValue pd= exp.getDataPropertyValue();
      Integer expressionId= upsertExpression(axiomId,parent,ExpressionType.DATAPROPERTYVALUE,null);
      Integer dataType= getConceptId(pd.getDataType().getIri());

      if (!usePropertyValues.isEmpty()){
         int i=0;
         Integer propertyValueId= usePropertyValues.get(0);
         DALHelper.setInt(updatePropertyValue,++i, expressionId);
         DALHelper.setInt(updatePropertyValue,++i,getConceptId(pd.getProperty().getIri()));
         DALHelper.setInt(updatePropertyValue,++i,dataType);
         DALHelper.setByte(updatePropertyValue,++i,null);
         DALHelper.setInt(updatePropertyValue,++i,pd.getMin());
         DALHelper.setInt(updatePropertyValue,++i,pd.getMax());
         DALHelper.setString(updatePropertyValue,++i,pd.getValueData());
         DALHelper.setInt(updatePropertyValue,++i,null);
         DALHelper.setInt(updatePropertyValue,++i, propertyValueId);
         if (updatePropertyValue.executeUpdate() == 0)
            throw new SQLException("Failed to save property PropertyValue ["
                + pd.getProperty() + "]");
         usePropertyValues.remove(0);
         return propertyValueId;
      } else {
         int i = 0;
         DALHelper.setInt(insertPropertyValue, ++i, expressionId);
         DALHelper.setInt(insertPropertyValue, ++i, getConceptId(pd.getProperty().getIri()));
         DALHelper.setInt(insertPropertyValue, ++i, dataType);
         DALHelper.setByte(insertPropertyValue, ++i, null);
         DALHelper.setInt(insertPropertyValue, ++i, pd.getMin());
         DALHelper.setInt(insertPropertyValue, ++i, pd.getMax());
         DALHelper.setString(insertPropertyValue, ++i, pd.getValueData());
         DALHelper.setInt(insertPropertyValue, ++i, null);
         if (insertPropertyValue.executeUpdate() == 0)
            throw new SQLException("Failed to save property PropertyValue ["
                + pd.getProperty() + "]");

         return DALHelper.getGeneratedKey(insertPropertyValue);

      }
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
