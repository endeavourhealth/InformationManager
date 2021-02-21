package org.endeavourhealth.informationmanager;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleStatement;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.URIUtil;
import org.eclipse.rdf4j.model.vocabulary.*;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResultHandler;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLUpdateDataBlockParser;
import org.eclipse.rdf4j.query.resultio.text.tsv.SPARQLResultsTSVWriter;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.query.SPARQLGraphQuery;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.WriterConfig;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.query.DropQuery;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.graphpattern.GraphPatterns;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.rdf4j.driver.VirtuosoRepository;

import java.io.File;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.zip.DataFormatException;

import static org.eclipse.rdf4j.model.util.Values.*;

public class OntologyFilerRDF4JDAL implements OntologyFilerDAL {
    private static final Logger LOG = LoggerFactory.getLogger(OntologyFilerRDF4JDAL.class);

    private static final String DOCUMENT_BASE = "https://im.endeavourhealth.net/document/";
    private static final IRI HAS_CODE = IM.CODE;
    private static final IRI HAS_SCHEME = IM.HAS_SCHEME;
    private static final IRI HAS_NAME = RDFS.LABEL;
    private static final IRI HAS_SYNONYM = IM.SYNONYM;
    private static final IRI HAS_DESCRIPTION = RDFS.COMMENT;
    private static final IRI HAS_STATUS = IM.STATUS;
    private static final IRI CONCEPT_TYPE = RDF.TYPE;
    private static final IRI HAS_MEMBERS = IM.HAS_MEMBERS;
    private static final IRI HAS_EXPANSION = IM.HAS_EXPANSION;
    private static final IRI IS_A = SNOMED.IS_A;
    private static final IRI FOR_MODULE = IM.MODULE;
    private static final IRI FOR_ONTOLOGY = IM.ONTOLOGY;
    private static final IRI EQUIVALENT_TO = OWL.EQUIVALENTCLASS;
    private static final IRI SUBCLASS_OF = RDFS.SUBCLASSOF;
    private static final IRI DISJOINT_WITH = OWL.DISJOINTWITH;
    private static final IRI INTERSECTION = OWL.INTERSECTIONOF;
    private static final IRI UNION = OWL.UNIONOF;
    private static final IRI OBJECT_SOME_VALUES = OWL.SOMEVALUESFROM;
    private static final IRI SUB_PROPERTY = RDFS.SUBPROPERTYOF;
    private static final IRI PROPERTY_CHAIN = OWL.PROPERTYCHAINAXIOM;
    private static final IRI PROPERTY_RANGE = RDFS.RANGE;
    private static final IRI PROPERTY_DOMAIN = RDFS.DOMAIN;
    private static final IRI INVERSE_PROPERTY_OF = OWL.INVERSEOF;
    private static final IRI INVERSE_OF = OWL.INVERSEOF;

    private final Update deleteConcept;


    private Repository db;
    private Model model = new TreeModel();
    private RepositoryConnection conn;

    private boolean simplifiedLists;

    public OntologyFilerRDF4JDAL(boolean simplifiedLists) {
        this.simplifiedLists = simplifiedLists;


//        db = new SailRepository(new NativeStore(new File("H:\\RDF4J")));

/*
//        Sail storage = new MemoryStore();
        Sail storage = new SailRepository(new NativeStore(new File("H:\\RDF4J")));
        LuceneSail luceneSail = new LuceneSail();
        luceneSail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");
        luceneSail.setBaseSail(storage);
        db = new SailRepository(luceneSail);
*/

         db = new HTTPRepository("http://localhost:7200/", "InformationModel");

//        db = new VirtuosoRepository("jdbc:virtuoso://localhost:1111","dba","dba");
//        db.initialize();
        conn= db.getConnection();

        deleteConcept = conn.prepareUpdate("");


    }

    @Override
    public void startTransaction() throws SQLException {
    }

    @Override
    public void commit() throws SQLException {

        try {
            conn.add(model);
            TreeModel newModel = new TreeModel();
            model.getNamespaces().forEach(ns ->
                newModel.setNamespace(ns));
            model = newModel;
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() throws SQLException {
        try (RepositoryConnection conn = db.getConnection()) {
            conn.add(model);
        }
        /*
        RepositoryConnection conn = db.getConnection();
        LOG.info("Finding value sets...");
        try (RepositoryResult<Statement> result = conn.getStatements(null, IS_A, getIri(":VSET_ValueSet"))) {
            for (Statement st : result) {
                LOG.info(st.toString());
            }
        }

        LOG.info("VSET_Covid4 definition...");

        Model aboutCovid4 = getDefinition(conn, getIri(":VSET_Covid4"));

        debugWrite(aboutCovid4);

        LOG.info("Sparql query (? is a VSET_Covid0)...");

        String qry = "PREFIX im: <http://www.EndeavourHealth.org/InformationModel/Ontology#>\n" +
            "PREFIX sn: <http://snomed.info/sct#>\n" +
            "SELECT ?s ?n\n" +
            "WHERE {?s sn:116680003 im:VSET_Covid0;" +
            "   rdfs:label ?n.\n" +
            "}";

        TupleQuery query = conn.prepareTupleQuery(qry);
        TupleQueryResultHandler tsvWriter = new SPARQLResultsTSVWriter(System.err);
        query.evaluate(tsvWriter);

        LOG.info("Lucene query....");

        qry = "PREFIX search: <http://www.openrdf.org/contrib/lucenesail#>\n" +
            "SELECT ?subj ?text\n" +
            "WHERE { ?subj search:matches [\n" +
            "   search:query ?term ;\n" +
            "   search:snippet ?text ] }";

        query = conn.prepareTupleQuery(qry);
        query.setBinding("term", literal("Tested"));
        query.evaluate(tsvWriter);

         */

        LOG.info("Done.");
        db.shutDown();
    }

    @Override
    public void rollBack() throws SQLException {
    }

    @Override
    public void fileIsa(Concept concept, String moduleIri) throws SQLException {
        // TODO: Delete previous entries
        if (concept.getIsA() != null) {
            for (ConceptReference ref : concept.getIsA())
                if (moduleIri == null)
                    model.add(getIri(concept.getIri()), IS_A, getIri(ref.getIri()));
                else
                    model.add(getIri(concept.getIri()), IS_A, getIri(ref.getIri()), getIri(moduleIri));
        }
    }


    @Override
    public void upsertNamespace(Namespace ns) throws SQLException {
        if (":".equals(ns.getPrefix())) {
            model.setNamespace("", ns.getIri());
        }
        else
            model.setNamespace(ns.getPrefix().replace(":", ""), ns.getIri());
        try (RepositoryConnection conn = db.getConnection()) {
            conn.setNamespace(ns.getPrefix().replace(":", ""), ns.getIri());
        }

    }

    @Override
    public void upsertOntology(String iri) throws SQLException {

    }


    @Override
    public void addDocument(Ontology ontology) throws SQLException {
        // TODO: Still needed?
        IRI docIri = iri(DOCUMENT_BASE + ontology.getDocumentInfo().getDocumentId());
        model.add(docIri, FOR_ONTOLOGY, getIri(ontology.getIri()));
    }

    @Override
    public void upsertModule(String iri) throws SQLException {
      //Nothing
    }

    @Override
    public void upsertConcept(Concept concept) {
        if (concept.getCode() != null && concept.getScheme() == null)

            throw new IllegalStateException("Code " + concept.getCode() + " without a code scheme");

        IRI conceptIri = getIri(concept.getIri());

        if (concept.getConceptType() != null) {
            switch (concept.getConceptType()) {
                case DATATYPE:
                    model.add(conceptIri, CONCEPT_TYPE, RDFS.DATATYPE);
                    break;
                case CLASSONLY:
                    model.add(conceptIri, CONCEPT_TYPE, OWL.CLASS);
                    break;
                case OBJECTPROPERTY:
                    model.add(conceptIri, CONCEPT_TYPE, OWL.OBJECTPROPERTY);
                    break;
                case DATAPROPERTY:
                    model.add(conceptIri, CONCEPT_TYPE, OWL.DATATYPEPROPERTY);
                    break;
                case ANNOTATION:
                    model.add(conceptIri, CONCEPT_TYPE, OWL.ANNOTATIONPROPERTY);
                    break;
                case INDIVIDUAL:
                    model.add(conceptIri, CONCEPT_TYPE, OWL.NAMEDINDIVIDUAL);
                    break;
                case TERM:
                    model.add(conceptIri, CONCEPT_TYPE, getIri(":TermCode"));
                    break;
                case VALUESET:
                    model.add(conceptIri, CONCEPT_TYPE, getIri(":ValueSet"));
                    break;
                case FOLDER:
                    model.add(conceptIri, CONCEPT_TYPE, getIri(":Folder"));
                    break;
                case RECORD:
                    model.add(conceptIri, CONCEPT_TYPE, getIri(":Record"));
                    break;
                case LEGACY:
                    model.add(conceptIri, CONCEPT_TYPE, getIri(":Legacy"));
                    break;
                default:
                    throw new IllegalStateException("Unhandled concept type");
            }
        }
        if (concept.getName() != null) model.add(conceptIri, HAS_NAME, literal(concept.getName()));
        if (concept.getStatus() != null) model.add(conceptIri, HAS_STATUS, literal(concept.getStatus().getName()));
        if (concept.getDescription() != null) model.add(conceptIri, HAS_DESCRIPTION, literal(concept.getDescription()));
        if (concept.getCode() != null) {
            model.add(conceptIri, HAS_CODE, literal(concept.getCode()));
            model.add(conceptIri, HAS_SCHEME, getIri(concept.getScheme().getIri()));
        }
        if (concept.getSynonym() != null)
            for (TermCode termCode : concept.getSynonym()) {
                Resource r = bnode();
                model.add(getIri(concept.getIri()), HAS_SYNONYM, r);
                model.add(r, HAS_NAME, literal(termCode.getTerm()));
                model.add(r, HAS_CODE, literal(termCode.getCode()));
            }

    }


    @Override
    public void upsertIndividual(Individual indi) {
        IRI indiIri = getIri(indi.getIri());
        model.add(indiIri, RDF.TYPE, getIri(indi.getIsType().getIri()));
        if (indi.getName() != null) model.add(indiIri, HAS_NAME, literal(indi.getName()));
        if (indi.getStatus() != null) model.add(indiIri, HAS_STATUS, literal(indi.getStatus().getName()));
        if (indi.getDescription() != null) model.add(indiIri, HAS_DESCRIPTION, literal(indi.getDescription()));
        for (ConceptRole role:indi.getRole()){
            model.add(indiIri,getIri(role.getProperty().getIri()),
                getIri(role.getValueType().getIri()));
            if (role.getValueData()!=null){
                model.add(indiIri,getIri(role.getProperty().getIri()),
                    literal(role.getValueData(),getIri(role.getValueType().getIri())));
            }
        }
    }

    @Override
    public void fileAxioms(Concept concept) throws DataFormatException, SQLException {
        // TODO: deleteConceptAxioms(concept);
        ConceptType conceptType = concept.getConceptType();
        fileConceptAnnotations(concept);
        fileClassAxioms(concept);
        fileProperties(concept);
        fileRoles(concept);
        fileMembers(concept);
        fileObjectPropertyAxioms(concept);
        fileDataPropertyAxioms(concept);
        fileDataTypeAxioms(concept);
        fileAnnotationPropertyAxioms(concept);
        fileLegacy(concept);

    }

    @Override
    public void dropIndexes() throws SQLException {

    }

    @Override
    public void restoreIndexes() throws SQLException {
    }

    private void fileConceptAnnotations(Concept concept) {
        if (concept.getAnnotations() == null)
            return;

        IRI conceptIri = getIri(concept.getIri());

        for (Annotation an : concept.getAnnotations()) {
            model.add(conceptIri, getIri(an.getProperty().getIri()), literal(an.getValue()));
        }
    }

    private void fileClassAxioms(Concept concept) {
        IRI conceptIri = getIri(concept.getIri());
        if (concept.getEquivalentTo() != null) {
            for (ClassExpression cex : concept.getEquivalentTo()) {
                fileClassExpression(conceptIri, EQUIVALENT_TO, cex);
            }
        }
        if (concept.getSubClassOf() != null) {
            for (ClassExpression cex : concept.getSubClassOf()) {
                fileClassExpression(conceptIri, SUBCLASS_OF, cex);
            }
        }
        if (concept.getDisjointWith() != null) {
            for (ConceptReference disj : concept.getDisjointWith()) {
                model.add(conceptIri, DISJOINT_WITH, getIri(disj.getIri()));
            }
        }
    }

    private void fileMembers(Concept valueSet) {
        IRI conceptIri = getIri(valueSet.getIri());
        if (valueSet.getMember() != null) {
            for (ClassExpression member : valueSet.getMember()) {
                fileClassExpression(conceptIri, HAS_MEMBERS, member);
            }
        }
        if (valueSet.getMemberExpansion() != null) {
            for (ConceptReference cref : valueSet.getMemberExpansion()) {
                model.add(conceptIri, HAS_EXPANSION, getIri(cref.getIri()));
            }
        }
    }

    private void fileLegacy(Concept legacy) {
        IRI conceptIri = getIri(legacy.getIri());
        if (legacy.getMappedFrom() != null) {
            for (ConceptReference mappedFrom : legacy.getMappedFrom()) {
                model.add(conceptIri, getIri(":mappedFrom"), getIri(mappedFrom.getIri()));
            }
        }
    }
    private void fileRoles(Concept concept) {
        if (concept.getRole() != null) {
            IRI conceptIri = getIri(concept.getIri());
            for (ConceptRole role : concept.getRole()) {
                fileRole(conceptIri, role);
            }
        }
    }
    private void fileRole(Resource subject,ConceptRole role){
        if (role.getValueType()!=null)
            model.add(subject,getIri(role.getProperty().getIri()), getIri(role.getValueType().getIri()));
        if (role.getValueData()!=null)
            model.add(subject,getIri(role.getProperty().getIri()),
                literal(role.getValueData(),getIri(role.getValueType().getIri())));
        if (role.getSubrole()!=null) {
            Resource bn= bnode();
            role.getSubrole().forEach(r -> fileRole(bn,r));
        }
    }

    private void fileProperties(Concept concept) {
        IRI conceptIri = getIri(concept.getIri());
        if (concept.getProperty() != null) {
            for (PropertyValue property : concept.getProperty()) {
                Resource r = bnode();
                model.add(conceptIri, SHACL.PROPERTY, r);
                model.add(r, SHACL.PATH, getIri(property.getProperty().getIri()));
                if (property.getMin() != null)
                    model.add(r, SHACL.MIN_COUNT, literal(property.getMin()));
                if (property.getMax() != null)
                    model.add(r, SHACL.MAX_COUNT, literal(property.getMax()));
                if (property.getValueType() != null)
                    model.add(r, SHACL.CLASS, getIri(property.getValueType().getIri()));
                if (property.getMinExclusive() != null)
                    model.add(r, SHACL.MIN_EXCLUSIVE, literal(property.getMinExclusive()));
                if (property.getMinInclusive() != null)
                    model.add(r, SHACL.MIN_INCLUSIVE, literal(property.getMinInclusive()));
                if (property.getMaxExclusive() != null)
                    model.add(r, SHACL.MAX_EXCLUSIVE, literal(property.getMaxExclusive()));
                if (property.getMaxInclusive() != null)
                    model.add(r, SHACL.MAX_INCLUSIVE, literal(property.getMaxInclusive()));

            }
        }

    }



    private void fileObjectPropertyAxioms(Concept op) {
        IRI conceptIri = getIri(op.getIri());
        if (op.getSubObjectPropertyOf() != null) {
            for (PropertyAxiom ax : op.getSubObjectPropertyOf()) {
                model.add(conceptIri, SUB_PROPERTY, getIri(ax.getProperty().getIri()));
            }
        }
        if (op.getSubPropertyChain() != null) {
            for (SubPropertyChain chain : op.getSubPropertyChain()) {
                chain.getProperty().forEach(i -> model.add(conceptIri, PROPERTY_CHAIN, getIri(i.getIri())));
            }
        }
        if (op.getObjectPropertyRange() != null) {
            for (ClassExpression range : op.getObjectPropertyRange()) {
                fileClassExpression(conceptIri, PROPERTY_RANGE, range);
            }
        }
        if (op.getPropertyDomain() != null) {
            for (ClassExpression domain : op.getPropertyDomain()) {
                fileClassExpression(conceptIri, PROPERTY_DOMAIN, domain);
            }
        }
        if (op.getInversePropertyOf() != null) {
            model.add(conceptIri, INVERSE_PROPERTY_OF, getIri(op.getInversePropertyOf().getProperty().getIri()));
        }
        if (op.getIsFunctional() != null)
            model.add(conceptIri, CONCEPT_TYPE, OWL.FUNCTIONALPROPERTY);
        if (op.getIsReflexive() != null)
            model.add(conceptIri, CONCEPT_TYPE, OWL.REFLEXIVEPROPERTY);
        if (op.getIsSymmetric() != null)
            model.add(conceptIri, CONCEPT_TYPE, OWL.SYMMETRICPROPERTY);
        if (op.getIsTransitive() != null)
            model.add(conceptIri, CONCEPT_TYPE, OWL.TRANSITIVEPROPERTY);
    }
    private void fileDataTypeAxioms(Concept dt){
        IRI conceptIri = getIri(dt.getIri());
        if (dt.getDataTypeDefinition()!=null){
            DataTypeDefinition dtd= dt.getDataTypeDefinition();
            Resource r = bnode();
            model.add(conceptIri,OWL.EQUIVALENTCLASS,r);
            model.add(r,OWL.ONDATATYPE,getIri(dtd.getDataType().getIri()));
            if (dtd.getPattern()!=null)
                model.add(r,getIri("xsd:pattern"),literal(dtd.getPattern()));
            IRI range;
            if (dtd.getMinValue()!=null) {
                if (dtd.getMinOperator().equals(">="))
                    range = getIri("xsd:minInclusive");
                else
                    range = getIri("xsd:minExclusive");
                model.add(r,range,literal(dtd.getMinValue()));
            }
            if (dtd.getMaxValue()!=null) {
                if (dtd.getMaxOperator().equals("<="))
                    range = getIri("xsd:maxInclusive");
                else
                    range = getIri("xsd:maxExclusive");
                model.add(r,range,literal(dtd.getMaxValue()));
            }

        }

    }


    private void fileDataPropertyAxioms(Concept dp) {
        IRI conceptIri = getIri(dp.getIri());
        if (dp.getSubDataPropertyOf() != null) {
            for (PropertyAxiom ax : dp.getSubDataPropertyOf()) {
                model.add(conceptIri, SUB_PROPERTY, getIri(ax.getProperty().getIri()));
            }
        }
        if (dp.getDataPropertyRange() != null) {
            for (DataPropertyRange range : dp.getDataPropertyRange()) {
                model.add(conceptIri, PROPERTY_RANGE, getIri(range.getDataType().getIri()));
            }
        }
        if (dp.getPropertyDomain() != null) {
            for (ClassExpression domain : dp.getPropertyDomain()) {
                fileClassExpression(conceptIri, PROPERTY_DOMAIN, domain);
            }
        }
    }

    private void fileAnnotationPropertyAxioms(Concept ap) {
        if (ap.getSubAnnotationPropertyOf() != null){
            IRI conceptIri = getIri(ap.getIri());
            for (PropertyAxiom ax : ap.getSubAnnotationPropertyOf()) {
                model.add(conceptIri, SUB_PROPERTY, getIri(ax.getProperty().getIri()));
            }
        }
    }

    private void fileClassExpression(Resource subject,IRI predicate, ClassExpression exp) {

        if (exp.getClazz() != null) {
            model.add(subject, predicate, getIri(exp.getClazz().getIri()));
        } else {
            Resource r = bnode();
            model.add(subject, predicate, r);
            if (exp.getComplementOf() != null) {
                fileClassExpression(r, OWL.COMPLEMENTOF, exp.getComplementOf());
            } else if (exp.getIntersection() != null) {
                for (ClassExpression i : exp.getIntersection()) {
                    fileClassExpression(r, OWL.INTERSECTIONOF, i);
                }
            } else if (exp.getUnion() != null) {
                for (ClassExpression i : exp.getUnion()) {
                    fileClassExpression(r, OWL.UNIONOF, i);
                }
            } else if (exp.getPropertyValue() != null) {
                PropertyValue opv = exp.getPropertyValue();
                model.add(r, RDF.TYPE, OWL.RESTRICTION);
                if (opv.getInverseOf() != null) {
                    Resource inv = bnode();
                    model.add(r, OWL.ONPROPERTY, inv);
                    model.add(inv, OWL.INVERSEOF, getIri(opv.getProperty().getIri()));
                } else {
                    model.add(r, OWL.ONPROPERTY, getIri(opv.getProperty().getIri()));
                }
                if (opv.getMax() != null) {
                    model.add(r, OWL.MAXCARDINALITY, literal(opv.getMax()));
                    if (opv.getMin() != null)
                        model.add(r, OWL.MINCARDINALITY, literal(opv.getMin()));
                    if (opv.getValueType() != null)
                        model.add(r, OWL.ONCLASS, getIri(opv.getValueType().getIri()));
                    else
                        fileClassExpression(r, OWL.ONCLASS, opv.getExpression());

                } else if (opv.getMin()!=null&&opv.getMin()>1) {
                    model.add(r, OWL.MINCARDINALITY, literal(opv.getMin()));
                    if (opv.getValueType() != null)
                        model.add(r, OWL.ONCLASS, getIri(opv.getValueType().getIri()));
                    else
                        fileClassExpression(r, OWL.ONCLASS, opv.getExpression());
                } else if (opv.getQuantificationType()==QuantificationType.ONLY) {
                    if (opv.getValueType() != null)
                        model.add(r, OWL.ALLVALUESFROM, getIri(opv.getValueType().getIri()));
                    else
                        fileClassExpression(r, OWL.ALLVALUESFROM, opv.getExpression());
                } else {
                      if (opv.getValueType() != null)
                                model.add(r, OWL.SOMEVALUESFROM, getIri(opv.getValueType().getIri()));
                      else
                          fileClassExpression(r, OWL.SOMEVALUESFROM, opv.getExpression());
                }
                if (opv.getValueData() != null)
                    model.add(r, OWL.HASVALUE, literal(opv.getValueData(), getIri(opv.getValueType().getIri())));

            } else if (exp.getObjectOneOf() != null) {
                for (ConceptReference on : exp.getObjectOneOf()) {
                    model.add(r, OWL.ONEOF, getIri(on.getIri()));
                }
            } else {
                LOG.error("Unhandled expression");
            }
        }
    }



    private IRI getIri(String conceptIri) {
        conceptIri = conceptIri.trim();

        if (conceptIri.startsWith("http"))
            return iri(conceptIri);

        String[] parts = conceptIri.split(":");
        Optional<org.eclipse.rdf4j.model.Namespace> namespace = model.getNamespace(parts[0]);

        if (!namespace.isPresent())
            throw new IllegalStateException("Unknown namespace prefix [" + parts[0] + "]");

        return iri(namespace.get().getName() + parts[1]);
    }

    private void debugWrite(Model m) {
        WriterConfig config = new WriterConfig();
        config.set(BasicWriterSettings.INLINE_BLANK_NODES, true);
        Rio.write(m, System.out, RDFFormat.TURTLE, config);
    }

    private Model getDefinition(RepositoryConnection conn, IRI iri) {
        Model result = new TreeModel();

        RepositoryResult<org.eclipse.rdf4j.model.Namespace> namespaces = conn.getNamespaces();
        namespaces.forEach(result::setNamespace);

        addResourceStatementsToModel(conn, iri, result);

        return result;
    }

    private void addResourceStatementsToModel(RepositoryConnection conn, Resource resource, Model m) {
        RepositoryResult<Statement> statements = conn.getStatements(resource, null, null);
        for(Statement s : statements) {
            m.add(s);
            Value o = s.getObject();
            if (o.isBNode()) {
                addResourceStatementsToModel(conn, (Resource)s.getObject(), m);
            }
        }
    }
}
