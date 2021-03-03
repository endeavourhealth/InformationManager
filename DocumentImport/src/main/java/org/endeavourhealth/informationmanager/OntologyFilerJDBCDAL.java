package org.endeavourhealth.informationmanager;

import com.google.common.base.Strings;
import org.endeavourhealth.informationmanager.common.dal.DALHelper;
import org.endeavourhealth.imapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Files a set of declarations and axioms relating to a single module
 * <p> This uses a replace all for concept for module approach to filing axioms i.e. assumes a full set for a concept from the perspective of the module</p>
 * <p> It assumes that axioms and expressions and concepts may not have the internal DB ids. Full trnasactional filers assume they do</p>
 */
public class OntologyFilerJDBCDAL implements OntologyFilerDAL {
   private static final Logger LOG = LoggerFactory.getLogger(OntologyFilerJDBCDAL.class);

   // Prepared statements
   private final PreparedStatement getNamespace;
   private final PreparedStatement getNsFromPrefix;
   private final PreparedStatement insertNamespace;
   private final PreparedStatement getOntology;
   private final PreparedStatement insertOntology;
   private final PreparedStatement getModuleStmt;
   private final PreparedStatement insertModule;
   private final PreparedStatement insertDocument;
   private final PreparedStatement getConceptDbid;
   private final PreparedStatement insertDraftConcept;
   private final PreparedStatement insertConcept;
   private final PreparedStatement updateConcept;
   private final PreparedStatement insertAxiom;
   private final PreparedStatement deleteAxioms;
   private final PreparedStatement insertExpression;
   private final PreparedStatement insertPropertyValue;
   private final PreparedStatement insertDTDefinition;
   private final PreparedStatement insertTree;
   private final PreparedStatement deleteTree;
   private final PreparedStatement insertTerm;
   private final PreparedStatement getTermDbId;
   private final PreparedStatement insertConceptAnnotation;
   private final PreparedStatement insertIndividual;
   private final PreparedStatement updateIndividual;
   private final PreparedStatement deleteAssertions;
   private final PreparedStatement insertAssertion;
   private final PreparedStatement getInstanceDbid;

   private final PreparedStatement checkSchema;



   private final Map<String, Integer> namespaceMap = new HashMap<>();
   private final Map<String, String> prefixMap = new HashMap<>();
   private final Map<String, Integer> conceptMap = new HashMap<>(1000000);



   private Integer moduleDbId;
   private Integer ontologyDbId;


   private final Connection conn;

   public OntologyFilerJDBCDAL(boolean noDelete) throws Exception {
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

      conn = DriverManager.getConnection(url, props);// NOSONAR

      checkSchema= conn.prepareStatement("SELECT version from im_schema");
      deleteAssertions= conn.prepareStatement("DELETE FROM property_assertion WHERE individual=?");
      insertIndividual= conn.prepareStatement("INSERT INTO instance SET iri=?,type=?,name=?",
          Statement.RETURN_GENERATED_KEYS);
      updateIndividual= conn.prepareStatement("UPDATE instance SET iri=?, type=?, name=?\n"+
          "WHERE dbid=?");
      insertAssertion= conn.prepareStatement("INSERT INTO property_assertion SET individual=?,\n"+
          "property=?,value_type=?, value_data=?");
      insertConceptAnnotation = conn.prepareStatement("INSERT INTO concept_annotation\n"+
          " SET concept=?,property=?,value_type=?,value_data=?");
      insertTerm = conn.prepareStatement("INSERT INTO concept_term SET concept=?, term=?,code=?");
      getTermDbId = conn.prepareStatement("SELECT dbid from concept_term\n"+
          "WHERE term =? and concept=? and code=?");
      deleteAxioms = conn.prepareStatement("DELETE FROM axiom WHERE concept=? and module=?");
      insertTree= conn.prepareStatement("INSERT classification set child=? ,parent=? ,module=?");
      deleteTree= conn.prepareStatement("DELETE FROM classification WHERE child=? AND module=?");
      getNamespace = conn.prepareStatement("SELECT dbid,prefix FROM namespace WHERE iri = ?");
      getNsFromPrefix= conn.prepareStatement("SELECT * FROM namespace WHERE prefix = ?");
      insertNamespace = conn.prepareStatement("INSERT INTO namespace (iri, prefix) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
      getOntology = conn.prepareStatement("SELECT dbid FROM ontology WHERE iri = ?");
      insertOntology = conn.prepareStatement("INSERT INTO ontology (iri) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      getModuleStmt = conn.prepareStatement("SELECT dbid FROM module WHERE iri = ?");
      insertModule = conn.prepareStatement("INSERT INTO module (iri) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
      insertDocument = conn.prepareStatement("REPLACE INTO document (uuid, module, ontology) VALUES (?, ?, ?)");
      getConceptDbid = conn.prepareStatement("SELECT dbid FROM concept WHERE iri = ?");
      getInstanceDbid=conn.prepareStatement("SELECT dbid FROM instance WHERE iri=?");
      insertDraftConcept = conn.prepareStatement("INSERT INTO concept (namespace, module, type, iri,status) VALUES(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
      insertConcept = conn.prepareStatement("INSERT INTO concept (namespace, module,type,iri, name, description, code, scheme, status) VALUES (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
      updateConcept = conn.prepareStatement("UPDATE concept SET namespace= ? , module= ?, type=?, iri = ?, name = ?, description = ?, code = ?, scheme = ?, status = ? WHERE dbid = ?");
      insertAxiom = conn.prepareStatement("INSERT INTO axiom (module, type, concept, version)\n" +
          "VALUES (?, ?, ?, ?)\n", Statement.RETURN_GENERATED_KEYS);

      insertExpression = conn.prepareStatement("INSERT INTO expression (type,axiom,parent,target_concept)\n"
          + "VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);


      insertPropertyValue = conn.prepareStatement("INSERT INTO property_value(expression,property,\n" +
          "value_type,inverse,min_cardinality,max_cardinality,value_data,value_expression)\n" +
          "VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);


      insertDTDefinition = conn.prepareStatement(
          "INSERT INTO datatype_definition(concept,module,min_operator,min_value,\n" +
              "max_operator,max_value,pattern)\n" +
              "   VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

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



   /**
    * Files a classification of a set of parents for a child concept
    * @param concept child concept
    * @param moduleIri
    * @throws Exception
    */
   @Override
   public void fileIsa(Concept concept, String moduleIri) throws SQLException, DataFormatException {
      if (moduleDbId==null)
         moduleDbId = getModuleDbId(moduleIri);
      if (moduleDbId==null){
         throw new IllegalArgumentException("no record of module in database ["+ moduleIri+"]");
      }
      Integer child= getConceptId(concept.getIri());
      int i=0;
      DALHelper.setInt(deleteTree,++i,child);
      DALHelper.setInt(deleteTree,++i,moduleDbId);
      deleteTree.executeUpdate();
      if (concept.getIsA()!=null){

         for (ConceptReference parent:concept.getIsA()) {
            i = 0;
            DALHelper.setInt(insertTree, ++i, child);
            DALHelper.setInt(insertTree, ++i, getOrSetConceptId(parent.getIri()));
            DALHelper.setInt(insertTree, ++i, moduleDbId);
            if (insertTree.executeUpdate() == 0)
               throw new SQLException("Unable to insert concept tree with [" +
                   concept.getIri() + " isa " + parent.getIri());
         }
      }

   }

   /**
    * Files a synonym , optionally with a code for the synonom
    * the same scheme as the code of the concept
    * @param conceptIri  the concept for which this is a synonym of
    * @param termCode  the code for this synonym
    * @throws SQLException
    */
   public void fileTerm(String conceptIri, TermCode termCode) throws SQLException {
      int i = 0;
      String term= termCode.getTerm();
      if (term.length() > 100)
         term = term.substring(0, 100);
      Integer conceptId = getConceptId(conceptIri);
      if (conceptId == null)
            throw new IllegalArgumentException("Concept does not exist in database for " + term);

      DALHelper.setString(getTermDbId, ++i, term);
         DALHelper.setInt(getTermDbId, ++i, conceptId);
         DALHelper.setString(getTermDbId, ++i, termCode.getCode());
         try (ResultSet rs = getTermDbId.executeQuery()) {
            if (!rs.next()) {
               i = 0;
               DALHelper.setInt(insertTerm, ++i, conceptId);
               DALHelper.setString(insertTerm, ++i, term);
               DALHelper.setString(insertTerm, ++i, conceptIri);
               if (insertTerm.executeUpdate() == 0)
                  throw new SQLException("Failed to save term code for  ["
                      + conceptIri
                      + "]");
            }
         }

   }

   // ------------------------------ NAMESPACE ------------------------------
   @Override
   public void upsertNamespace(Namespace ns) throws SQLException {

      DALHelper.setString(getNamespace, 1, ns.getIri());
      try (ResultSet rs = getNamespace.executeQuery()) {
         if (rs.next()) {
            if (!ns.getPrefix().equals(rs.getString("prefix"))){
               throw new SQLException("prefix in database -> "+ns.getPrefix()+" does not match the iri "+ ns.getIri());
            } else {
               prefixMap.put(ns.getIri(), rs.getString("prefix"));
               prefixMap.put(ns.getPrefix(), rs.getString("prefix"));
               namespaceMap.put(ns.getPrefix(), rs.getInt("dbid"));
               namespaceMap.put(ns.getIri(), rs.getInt("dbid"));
            }
         } else {
            DALHelper.setString(getNsFromPrefix,1,ns.getPrefix());
            ResultSet ps= getNsFromPrefix.executeQuery();
            if (ps.next()) {
               if (!ns.getIri().equals(ps.getString("iri"))) {
                  throw new SQLException(ps.getString("prefix") + "->" + ps.getString("iri) "
                      + " does not match " + ns.getIri() + ns.getIri()));
               }
            } else {
               createNamespace(ns);
            }

         }
      }
   }

   private void createNamespace(Namespace ns) throws SQLException {

      DALHelper.setString(insertNamespace, 1, ns.getIri());
      DALHelper.setString(insertNamespace, 2, ns.getPrefix());
      try {
         insertNamespace.executeUpdate();
         Integer dbid = DALHelper.getGeneratedKey(insertNamespace);
         prefixMap.put(ns.getIri(), ns.getPrefix());
         prefixMap.put(ns.getPrefix(), ns.getPrefix());
         namespaceMap.put(ns.getIri(), dbid);
         namespaceMap.put(ns.getPrefix(), dbid);
      } catch (SQLException e){
         throw new SQLException(e.toString()+ " problem adding namespace " +
             ns.getPrefix()+ns.getIri());
      }
   }

   // ------------------------------ ONTOLOGY ------------------------------
   @Override
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
   @Override
   public void addDocument(Ontology ontology) throws SQLException {
      try {
         ResultSet rs=checkSchema.executeQuery();
         if (rs.next())
            if (rs.getInt("version")<5)
               throw new SQLException("IM Schema version < version 5");
      } catch (Exception e){
         throw new SQLException("IM schema not up to date");
      }
      DALHelper.setString(insertDocument, 1, ontology.getDocumentInfo().getDocumentId().toString());
      DALHelper.setInt(insertDocument, 2, moduleDbId);
      DALHelper.setInt(insertDocument, 3, ontologyDbId);
      if (insertDocument.executeUpdate() == 0) {
         throw new SQLException("Unable to record document meta data");
      }

   }

   // ------------------------------ MODULE ------------------------------
   @Override
   public void upsertModule(String iri) throws SQLException {
      DALHelper.setString(getModuleStmt, 1, iri);
      try (ResultSet rs = getModuleStmt.executeQuery()) {
         if (rs.next())
            moduleDbId = rs.getInt("dbid");
         else
            createModule(iri);
      }
   }

   private Integer getModuleDbId(String iri) throws SQLException {
      DALHelper.setString(getModuleStmt, 1, iri);
      try (ResultSet rs = getModuleStmt.executeQuery()) {
         if (rs.next())
            return moduleDbId = rs.getInt("dbid");
         else
            return null;
      }
   }


   private void createModule(String iri) throws SQLException {
      DALHelper.setString(insertModule, 1, iri);
      insertModule.executeUpdate();
      moduleDbId = DALHelper.getGeneratedKey(insertModule);
   }

   // ------------------------------ Concept ------------------------------
   private Integer getOrSetConceptId(String iri) throws SQLException, DataFormatException {
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

   private Integer getConceptId(String iri) throws SQLException {
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
               return null;
            }
         }
      }
      return dbid;
   }

   @Override
   public void upsertConcept(Concept concept) throws DataFormatException, SQLException {

      try {
         //reformats the document concept iri into the correct format
         concept.setIri(mapIri(concept.getIri()));
         Integer namespace = getNamespaceFromIri(concept.getIri());
         if (concept.getCode() != null)
            if (concept.getScheme() == null)
               throw new DataFormatException("Code " + concept.getCode() + " without a code scheme");

         //Get Scheme and if not in db add new scheme concept
         Integer scheme = null;
         if (concept.getScheme() != null) {
            concept.setScheme(mapIri(concept.getScheme().getIri()));
            scheme = getOrSetConceptId(concept.getScheme().getIri());

         }

         if (concept.getStatus() == null)
            concept.setStatus(ConceptStatus.DRAFT);

         Integer dbid = null;
         DALHelper.setString(getConceptDbid, 1, concept.getIri());
         ResultSet rs = getConceptDbid.executeQuery();
         if (rs.next()) {
            dbid = rs.getInt("dbid");
         }
         if (concept.isRef())
            if (dbid==null) {
               throw new DataFormatException("Concept referenced to concept not yet filed "+ concept.getIri());
            } else {
               concept.setDbid(dbid);
               conceptMap.put(concept.getIri(), dbid);
               return;
            }

         int i = 0;
         ConceptType conceptType;
         conceptType = concept.getConceptType();
         String shortName = concept.getName();
         if (shortName != null)
            if (shortName.length() > 200)
               shortName = shortName.substring(0, 200);

         if (dbid == null) {
            // Insert
            DALHelper.setInt(insertConcept, ++i, namespace);
            DALHelper.setInt(insertConcept, ++i, moduleDbId);
            DALHelper.setByte(insertConcept, ++i, conceptType.getValue());
            DALHelper.setString(insertConcept, ++i, concept.getIri());
            DALHelper.setString(insertConcept, ++i, shortName);
            DALHelper.setString(insertConcept, ++i, concept.getDescription());
            DALHelper.setString(insertConcept, ++i, concept.getCode());
            DALHelper.setInt(insertConcept, ++i, scheme);
            DALHelper.setByte(insertConcept, ++i, concept.getStatus().getValue());
            // System.out.println("new concept "+ concept.getIri());
            if (insertConcept.executeUpdate() == 0)
               throw new SQLException("Failed to insert concept [" + concept.getIri() + "]");
            else
               dbid = DALHelper.getGeneratedKey(insertConcept);
         } else {
            // Update

            DALHelper.setInt(updateConcept, ++i, namespace);
            DALHelper.setInt(updateConcept, ++i, moduleDbId);
            DALHelper.setByte(updateConcept, ++i, conceptType.getValue());
            DALHelper.setString(updateConcept, ++i, concept.getIri());
            DALHelper.setString(updateConcept, ++i, shortName);
            DALHelper.setString(updateConcept, ++i, concept.getDescription());
            DALHelper.setString(updateConcept, ++i, concept.getCode());
            DALHelper.setInt(updateConcept, ++i, scheme);
            DALHelper.setByte(updateConcept, ++i, concept.getStatus().getValue());
            DALHelper.setInt(updateConcept, ++i, dbid);

            if (updateConcept.executeUpdate() == 0)
               throw new SQLException("Failed to update concept [" + concept.getIri() + "]/[" + dbid.toString() + "]");
         }

         concept.setDbid(dbid);
         conceptMap.put(concept.getIri(), dbid);
         if (concept.getSynonym()!=null) {
            for (TermCode termCode: concept.getSynonym())
               fileTerm(concept.getIri(), termCode);
         }
      }
      catch (Exception e){
         System.err.println(concept.getIri()+" wont file for some reason");
      }
   }

   private Integer createDraftConcept(String iri) throws SQLException, DataFormatException {
      Integer namespace = getNamespaceFromIri(iri);
      ConceptType conceptType= ConceptType.CLASSONLY;
      if (iri.equals("owl:topObjectProperty"))
         conceptType= ConceptType.OBJECTPROPERTY;
      else if (iri.equals("owl:topDataProperty"))
         conceptType= ConceptType.DATAPROPERTY;
      int i = 0;
      DALHelper.setInt(insertDraftConcept, ++i, namespace);
      DALHelper.setInt(insertDraftConcept, ++i, moduleDbId);
      DALHelper.setByte(insertDraftConcept, ++i, conceptType.getValue());
      DALHelper.setString(insertDraftConcept, ++i, iri);
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

   private void deleteConceptAxioms(Concept concept) throws DataFormatException, SQLException {
      int i=0;
      DALHelper.setInt(deleteAxioms,++i,getOrSetConceptId(concept.getIri()));
      DALHelper.setInt(deleteAxioms,++i,moduleDbId);
      deleteAxioms.executeUpdate();
   }

   @Override
   public void upsertIndividual(Individual indi) throws SQLException{
      Integer dbid=null;
      DALHelper.setString(getInstanceDbid, 1, indi.getIri());
      try (ResultSet rs = getInstanceDbid.executeQuery()) {
         if (rs.next()) {
            dbid = rs.getInt("dbid");

         }
      }
      try {
         int i = 0;
         if (dbid != null) {
            indi.setDbid(dbid);
            DALHelper.setString(updateIndividual, ++i, indi.getIri());
            DALHelper.setInt(updateIndividual, ++i, getConceptId(indi.getIsType().getIri()));
            DALHelper.setString(updateIndividual, ++i, indi.getName());
            DALHelper.setInt(updateIndividual, ++i, dbid);
            if (updateIndividual.executeUpdate() == 0)
               throw new SQLException("Failed to update individual [" + indi.getIri() + "]/[" + dbid.toString());
         } else {
            DALHelper.setString(insertIndividual, ++i, indi.getIri());
            DALHelper.setInt(insertIndividual, ++i, getConceptId(indi.getIsType().getIri()));
            DALHelper.setString(insertIndividual, ++i, indi.getName());
            if (insertIndividual.executeUpdate() == 0)
               throw new SQLException("Failed to insert concept [" + indi.getIri() + "]");
            else {
               dbid = DALHelper.getGeneratedKey(insertIndividual);
               indi.setDbid(dbid);
            }

         }

         //Remove previous entries
         DALHelper.setInt(deleteAssertions, 1, dbid);
         deleteAssertions.executeUpdate();
         if (indi.getRoleGroup()!=null) {
            for (ConceptRoleGroup rg : indi.getRoleGroup()) {
               for (ConceptRole role : rg.getRole()) {
                  fileAssertion(dbid, role);
               }
            }
         }
      } catch (SQLException e) {
         System.err.println("Individual filing problem "+ indi.getIri());
         throw new SQLException("Individual filing problem"+ indi.getIri());
      }


   }

   private void fileAssertion(Integer indiId, ConceptRole dpv) throws SQLException {
      int i = 0;
      try {
         DALHelper.setInt(insertAssertion, ++i, indiId);
         DALHelper.setInt(insertAssertion, ++i, getOrSetConceptId(dpv.getProperty().getIri()));
         DALHelper.setInt(insertAssertion, ++i, getOrSetConceptId(dpv.getValueType().getIri()));
         DALHelper.setString(insertAssertion, ++i, dpv.getValueData());
         if (insertAssertion.executeUpdate() == 0)
            throw new SQLException("Failed to save individual assertion ["
                + indiId.toString()
                + "]");
      } catch (SQLException | DataFormatException e){
         System.err.println("Failed to save individual assertion ");
         throw new SQLException("Failed to save individual assertion");
      }
   }


   @Override
   public void fileAxioms(Concept concept) throws DataFormatException, SQLException {
      if (concept.getDbid()==null)
         concept.setDbid(getOrSetConceptId(concept.getIri()));
      deleteConceptAxioms(concept);
      ConceptType conceptType = concept.getConceptType();
      fileConceptAnnotations(concept);
      fileClassExpressions(concept);
      fileObjectPropertyAxioms(concept);
      fileMembers(concept);
      fileProperties(concept);
      fileRoles(concept);
      fileLegacy(concept);
      fileDataPropertyAxioms(concept);
      if (concept.getDataTypeDefinition()!=null)
            insertDataTypeDefinition(concept);
      fileAnnotationPropertyAxioms(concept);
   }

   private void fileConceptAnnotations(Concept concept) throws DataFormatException, SQLException {
      if (concept.getAnnotations()==null)
         return;

      Integer conceptId= concept.getDbid();

      for (Annotation an:concept.getAnnotations()){
         int i=0;
         Integer propertyId= getOrSetConceptId(an.getProperty().getIri());
         DALHelper.setInt(insertConceptAnnotation,++i,conceptId);
         DALHelper.setInt(insertConceptAnnotation,++i,propertyId);
         DALHelper.setInt(insertConceptAnnotation,++i,null); // not yet supported
         DALHelper.setString(insertConceptAnnotation,++i,an.getValue());
         insertConceptAnnotation.executeUpdate();
      }
   }





   private void fileClassExpressions(Concept concept) throws SQLException, DataFormatException {
      Integer conceptId = concept.getDbid();
      Long axiomId;
      if (concept.getEquivalentTo() != null) {
         for (ClassExpression ax : concept.getEquivalentTo()) {
            axiomId = insertConceptAxiom(conceptId, AxiomType.EQUIVALENTTO);
            fileClassExpression(ax, axiomId, null);
         }

      }
      if (concept.getSubClassOf() != null) {
         for (ClassExpression ax : concept.getSubClassOf()) {
               axiomId = insertConceptAxiom(conceptId, AxiomType.SUBCLASSOF);
               fileClassExpression(ax, axiomId, null);
            }
      }
      if (concept.getDisjointWith() != null) {
         for (ConceptReference disj : concept.getDisjointWith()) {
            axiomId = insertConceptAxiom(conceptId, AxiomType.DISJOINTWITH);
            insertExpression(axiomId,null,ExpressionType.CLASS,disj.getIri());
         }
      }
   }
   private boolean notOWL(ClassExpression ax){
      if (ax.getClazz()==null)
         return true;
      else if (ax.getClazz().getIri().startsWith("owl:"))
         return false;
      else
         return true;
   }
   private void fileAnnotationPropertyAxioms(Concept ap) throws SQLException, DataFormatException {
      Integer conceptId = ap.getDbid();
      Long axiomId;
      if (ap.getSubAnnotationPropertyOf() != null){
         for (PropertyAxiom ax : ap.getSubAnnotationPropertyOf()) {
            axiomId = insertConceptAxiom(conceptId, AxiomType.SUBANNOTATIONPROPERTY);
            insertExpression(axiomId, null, ExpressionType.PROPERTY, ax.getProperty().getIri());
         }
      }
   }

   private void fileMembers(Concept vset) throws DataFormatException, SQLException {
      Integer conceptId = vset.getDbid();
      if (vset.getMember() != null) {
         Long axiomId;
         axiomId = insertConceptAxiom(conceptId, AxiomType.MEMBER);
         for (ClassExpression exp : vset.getMember()) {
            fileClassExpression(exp,axiomId,null);
         }
      }

   }

   private void fileProperties(Concept record) throws DataFormatException, SQLException {
      Integer conceptId = record.getDbid();
      if (record.getProperty() != null) {
         Long axiomId;
         axiomId = insertConceptAxiom(conceptId, AxiomType.PROPERTY);
         for (PropertyValue constraint : record.getProperty()) {
            ClassExpression exp = new ClassExpression();
            exp.setPropertyValue(constraint);
            fileClassExpression(exp, axiomId, null);
         }
      }
   }

   private void fileRoles(Concept concept) throws DataFormatException, SQLException {
      if (concept.getRoleGroup()!=null){
         Integer conceptId = concept.getDbid();
         Long axiomId;
         axiomId = insertConceptAxiom(conceptId, AxiomType.ROLE);
         for (ConceptRoleGroup rg:concept.getRoleGroup()) {
            Long expressionId= insertExpression(axiomId,null,ExpressionType.ROLE,null);
            for (ConceptRole role : rg.getRole()) {
               fileRole(axiomId, role, expressionId);
            }
         }
      }
   }
   private void fileRole(Long axiomId, ConceptRole role,Long parent) throws DataFormatException, SQLException {
         Long expressionId = insertExpression(axiomId, parent, ExpressionType.ROLE, null);
         insertRole(axiomId,expressionId,role,null);
   }

   private void fileLegacy(Concept legacy) throws DataFormatException, SQLException {
      Integer conceptId = legacy.getDbid();
      Long  axiomId = insertConceptAxiom(conceptId, AxiomType.MAPPED_FROM);
      if (legacy.getMappedFrom() != null) {
         for (ConceptReference from :legacy.getMappedFrom()) {
            ClassExpression exp= new ClassExpression();
            exp.setClazz(from);
            fileClassExpression(exp, axiomId, null);
         }
      }

   }

   private void fileObjectPropertyAxioms(Concept op) throws SQLException, DataFormatException {
      Integer conceptId= op.getDbid();
      Long axiomId;
      if (op.getSubObjectPropertyOf() != null) {
         for (PropertyAxiom ax : op.getSubObjectPropertyOf()) {
               axiomId = insertConceptAxiom(conceptId, AxiomType.SUBOBJECTPROPERTY);
               insertExpression(axiomId, null, ExpressionType.PROPERTY, ax.getProperty().getIri());
            }
      }
      if (op.getSubPropertyChain() != null) {
         for (SubPropertyChain chain : op.getSubPropertyChain()) {
            axiomId = insertConceptAxiom(conceptId, AxiomType.SUBPROPERTYCHAIN);
            filePropertyChain(axiomId, chain);
         }
      }
      if (op.getObjectPropertyRange() != null) {
         for (ClassExpression expression : op.getObjectPropertyRange()) {
            axiomId = insertConceptAxiom(conceptId, AxiomType.OBJECTPROPERTYRANGE);
            fileClassExpression(expression, axiomId, null);
         }
      }
      if (op.getPropertyDomain() != null) {
         for (ClassExpression domain : op.getPropertyDomain()) {
            axiomId = insertConceptAxiom(conceptId, AxiomType.PROPERTYDOMAIN);
            fileClassExpression(domain, axiomId, null);
         }
      }
      if (op.getInversePropertyOf() != null) {
         axiomId = insertConceptAxiom(conceptId, AxiomType.INVERSEPROPERTYOF);
         insertExpression(axiomId, null, ExpressionType.PROPERTY,
             op.getInversePropertyOf().getProperty().getIri());
      }
      if (op.getIsFunctional() != null)
         insertConceptAxiom(conceptId, AxiomType.ISFUNCTIONAL);
      if (op.getIsReflexive() != null)
         insertConceptAxiom(conceptId, AxiomType.ISREFLEXIVE);
      if (op.getIsSymmetric() != null)
         insertConceptAxiom(conceptId, AxiomType.ISSYMMETRIC);
      if (op.getIsTransitive() != null)
         insertConceptAxiom(conceptId, AxiomType.ISTRANSITIVE);
   }

   private void fileDataPropertyAxioms(Concept dp) throws SQLException, DataFormatException {
      Integer conceptId= dp.getDbid();
      Long axiomId;
         if (dp.getSubDataPropertyOf() != null) {
            for (PropertyAxiom ax : dp.getSubDataPropertyOf()) {
                  axiomId = insertConceptAxiom(conceptId, AxiomType.SUBDATAPROPERTY);
                  insertExpression(axiomId, null, ExpressionType.PROPERTY, ax.getProperty().getIri());
               }
         }
         if (dp.getDataPropertyRange() != null) {
            for (DataPropertyRange ax : dp.getDataPropertyRange()) {
               axiomId = insertConceptAxiom(conceptId, AxiomType.DATAPROPERTYRANGE);
               insertExpression(axiomId,null,
                   ExpressionType.DATATYPE,ax.getDataType().getIri());
            }
         }
         if (dp.getPropertyDomain() != null) {
            for (ClassExpression domain : dp.getPropertyDomain()) {
               axiomId = insertConceptAxiom(conceptId, AxiomType.PROPERTYDOMAIN);
               fileClassExpression(domain, axiomId, null);
            }
         }
   }



   private void insertDataTypeDefinition(Concept dataType) throws SQLException {
      Integer dtId= dataType.getDbid();
      DataTypeDefinition dtdef= dataType.getDataTypeDefinition();
         int i = 0;
         DALHelper.setInt(insertDTDefinition, ++i, dtId);
         DALHelper.setInt(insertDTDefinition, ++i, moduleDbId);
         if (dtdef.getMinInclusive()!=null) {
            DALHelper.setString(insertDTDefinition, ++i, ">=");
            DALHelper.setString(insertDTDefinition, ++i, dtdef.getMinInclusive());
         } else if (dtdef.getMinExclusive()!=null){
            DALHelper.setString(insertDTDefinition, ++i, ">");
            DALHelper.setString(insertDTDefinition, ++i, dtdef.getMinExclusive());
         } else {
            DALHelper.setString(insertDTDefinition, ++i,null);
            DALHelper.setString(insertDTDefinition, ++i, null);
         }
      if (dtdef.getMaxInclusive()!=null) {
         DALHelper.setString(insertDTDefinition, ++i, "<=");
         DALHelper.setString(insertDTDefinition, ++i, dtdef.getMaxInclusive());
      } else if (dtdef.getMaxExclusive()!=null){
         DALHelper.setString(insertDTDefinition, ++i, "<");
         DALHelper.setString(insertDTDefinition, ++i, dtdef.getMaxExclusive());
      } else {
         DALHelper.setString(insertDTDefinition, ++i,null);
         DALHelper.setString(insertDTDefinition, ++i, null);
      }
      DALHelper.setString(insertDTDefinition, ++i, dtdef.getPattern());
         if (insertDTDefinition.executeUpdate() == 0)
            throw new SQLException("Failed to save concept axiom ["
                + dtId.toString()
                + "]");

         //          return DALHelper.getGeneratedKey(insertAxiom);
   }


   private void filePropertyChain(Long axiomId,SubPropertyChain chain) throws DataFormatException, SQLException {
      long expressionId;
      Long parent = null;
      if (chain.getProperty()!=null)
         for (ConceptReference property:chain.getProperty()) {
            expressionId = insertExpression(axiomId,
                parent,
                ExpressionType.PROPERTY,
                property.getIri());
            parent = expressionId;
         }
   }

   private Long insertConceptAxiom(Integer conceptId,AxiomType axiomType) throws SQLException {
         int i = 0;
         DALHelper.setInt(insertAxiom, ++i, moduleDbId);
         DALHelper.setByte(insertAxiom, ++i, axiomType.getValue());
         DALHelper.setInt(insertAxiom, ++i, conceptId);
         DALHelper.setInt(insertAxiom, ++i, null);
         try {
            if (insertAxiom.executeUpdate() == 0)
               throw new SQLException("Failed to save concept axiom ["
                   + conceptId.toString()
                   + "]");

            return DALHelper.getGeneratedLongKey(insertAxiom);
         } catch (SQLException e){
            System.err.println("unable to file axiom " + axiomType.getName()+" "+ conceptId);
            throw new SQLException("unable to file axiom " + axiomType.getName()+" "+ conceptId);
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
   private Long insertExpression(Long axiomId, Long parent, ExpressionType expType, String valueIri) throws DataFormatException, SQLException {
         int i = 0;
         DALHelper.setByte(insertExpression, ++i, expType.getValue());
         DALHelper.setLong(insertExpression, ++i, axiomId);
         DALHelper.setLong(insertExpression, ++i, parent);
         DALHelper.setInt(insertExpression, ++i, getOrSetConceptId(valueIri));
         if (insertExpression.executeUpdate() == 0)
            throw new SQLException("Failed to save expression ["
                + axiomId.toString() + expType.getName()
                + "]");

         return DALHelper.getGeneratedLongKey(insertExpression);
      
   }
   private Long fileValueSetMember(ClassExpression member,long axiomId) throws DataFormatException, SQLException {

      if (member.getClazz()!=null) {
         Long expressionId = insertExpression(axiomId, null, ExpressionType.CLASS, member.getClazz().getIri());
         return expressionId;
      } else if (member.getIntersection()!=null){
         Long expressionId= insertExpression(axiomId,null,ExpressionType.INTERSECTION,null);
         for (ClassExpression inter:member.getIntersection()) {
            fileClassExpression(inter, axiomId, expressionId);
         }
         return expressionId;
      } else
         throw new DataFormatException("unkown value set expression type");
   }


   private Long fileClassExpression(ClassExpression exp, Long axiomId, Long parent) throws DataFormatException, SQLException {
      Long expressionId;
      if (exp.getClazz() != null)
         return insertExpression(axiomId, parent, ExpressionType.CLASS, exp.getClazz().getIri());
      else if (exp.getIntersection() != null) {
         expressionId = insertExpression(axiomId,parent,ExpressionType.INTERSECTION, null);
         for (ClassExpression inter : exp.getIntersection())
            fileClassExpression(inter, axiomId, expressionId);
      } else if (exp.getUnion() != null) {
         expressionId = insertExpression(axiomId, parent, ExpressionType.UNION, null);
         for (ClassExpression union : exp.getUnion())
            fileClassExpression(union, axiomId, expressionId);
      } else if (exp.getPropertyValue() != null) {
         expressionId=insertExpression(axiomId,parent,ExpressionType.PROPERTY_VALUE,null);
         insertPropertyValue(axiomId,expressionId,exp.getPropertyValue());

      } else if (exp.getComplementOf() != null){
            expressionId = insertExpression(axiomId, parent, ExpressionType.COMPLEMENTOF, null);
            fileClassExpression(exp.getComplementOf(), axiomId, expressionId);

      } else if (exp.getObjectOneOf() != null) {
         expressionId= insertExpression(axiomId,parent,ExpressionType.OBJECTONEOF,null);
         for (ConceptReference oneOf:exp.getObjectOneOf())
            insertExpression(axiomId,expressionId,ExpressionType.OBJECTVALUE, oneOf.getIri());
      }
      else
         throw new IllegalArgumentException("invalid class expression axiom id ["+ axiomId.toString()+"]");

      return expressionId;
   }




   private void insertRole(Long axiomId,Long expressionId,ConceptRole role,Long subRoleId) throws DataFormatException, SQLException {
      Long valueExpression=null;
      Integer propertyId=null;
      Integer valueType=null;
      propertyId= getOrSetConceptId(role.getProperty().getIri());
      valueType= getOrSetConceptId(role.getValueType().getIri());
      String valueData= role.getValueData();

      int i = 0;
      DALHelper.setLong(insertPropertyValue, ++i, expressionId);
      DALHelper.setInt(insertPropertyValue, ++i, propertyId);
      DALHelper.setInt(insertPropertyValue, ++i, valueType);
      DALHelper.setByte(insertPropertyValue, ++i, null);
      DALHelper.setInt(insertPropertyValue, ++i, null);
      DALHelper.setInt(insertPropertyValue, ++i, null);
      DALHelper.setString(insertPropertyValue, ++i, valueData);
      DALHelper.setLong(insertPropertyValue, ++i, subRoleId);
      if (insertPropertyValue.executeUpdate() == 0)
         throw new SQLException("Failed to save property PropertyValue ["
             + role.getProperty() + "]");

      // return DALHelper.getGeneratedLongKey(insertPropertyValue);
   }





   private void insertPropertyValue(Long axiomId,Long expressionId,PropertyValue pv) throws DataFormatException, SQLException {
      Long valueExpression=null;
      Integer propertyId;
      Integer valueType=null;

      byte inverse=0;
      if (pv.getInverseOf()!=null) {
         inverse = 1;
         propertyId=getOrSetConceptId(pv.getInverseOf().getIri());
      } else {
         propertyId= getOrSetConceptId(pv.getProperty().getIri());
      }

      if (pv.getValueType()!=null)
         valueType= getOrSetConceptId(pv.getValueType().getIri());
      if (pv.getExpression()!=null)
         valueExpression=fileClassExpression(pv.getExpression(),axiomId,expressionId);

      
         int i = 0;
         DALHelper.setLong(insertPropertyValue, ++i, expressionId);
         DALHelper.setInt(insertPropertyValue, ++i, getOrSetConceptId(pv.getProperty().getIri()));
         DALHelper.setInt(insertPropertyValue, ++i, valueType);
         DALHelper.setByte(insertPropertyValue, ++i, inverse);
         DALHelper.setInt(insertPropertyValue, ++i, pv.getMin());
         DALHelper.setInt(insertPropertyValue, ++i, pv.getMax());
         DALHelper.setString(insertPropertyValue, ++i, pv.getValueData());
         DALHelper.setLong(insertPropertyValue, ++i, valueExpression);
         if (insertPropertyValue.executeUpdate() == 0)
            throw new SQLException("Failed to save property PropertyValue ["
                + pv.getProperty() + "]");

         // return DALHelper.getGeneratedLongKey(insertPropertyValue);
   }




   // ------------------------------ Helper/Util ------------------------------

   private String mapIri(String iri) throws DataFormatException {
      if (iri.startsWith("http")) {
         int hash = iri.indexOf("#");
         if (hash > -1) {
            String nsIri = iri.substring(0, hash) + "#";
            String prefix = prefixMap.get(nsIri);
            if (prefix == null)
               throw new DataFormatException("iri appears not to have a valid prefix [" + iri + "]");
            return prefix + iri.substring(hash + 1);
         } else {
            int slash = iri.lastIndexOf("/");
            if (slash > -1) {
               return iri;
            } else
               throw new DataFormatException("iri not in normal format [" + iri + "]");
         }
      }
      int colon = iri.lastIndexOf(":");
      if (colon > -1) {
         String clientPrefix = iri.substring(0, colon) + ":";
         String prefix = prefixMap.get(clientPrefix);
         if (prefix == null)
            throw new DataFormatException("iri prefix appears not to have been declared [" + iri + "]");
         return prefix + iri.substring(colon + 1);
      }
      throw new DataFormatException("iri format not standard [" + iri + "]");
   }

   //Assumes that the prefix in the client IRI has already been mapped
   private Integer getNamespaceFromIri(String iri) throws DataFormatException {
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
         throw new DataFormatException("unable to derive namespace from iri [" + iri + "]");

   }

   @Override
   public void dropIndexes() throws SQLException {
      PreparedStatement dropIndex= conn.prepareStatement("DROP INDEX term_ftidx on concept_term;");
      //Note that exception occurs if index has already been dropped
      try {
         dropIndex.executeUpdate();
      } catch (SQLException e){

      }

   }
   @Override
   public void restoreIndexes() throws SQLException {
      PreparedStatement restoreIndex= conn.prepareStatement("CREATE FULLTEXT INDEX term_ftidx on concept_term(`term`);");
      //restoreIndex.executeUpdate();
   }

   public void dropGraph(){

   }

}
