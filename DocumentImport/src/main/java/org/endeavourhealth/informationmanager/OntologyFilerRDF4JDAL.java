package org.endeavourhealth.informationmanager;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResultHandler;
import org.eclipse.rdf4j.query.resultio.text.tsv.SPARQLResultsTSVWriter;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.WriterConfig;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.zip.DataFormatException;

import static org.eclipse.rdf4j.model.util.Values.*;

public class OntologyFilerRDF4JDAL implements OntologyFilerDAL {
    private static final Logger LOG = LoggerFactory.getLogger(OntologyFilerRDF4JDAL.class);

    private static final String DOCUMENT_BASE = "https://im.endeavourhealth.net/document/";
    private static final String IM_BASE = "http://www.DiscoveryDataService.org/InformationModel/Ontology#";
    private static final IRI HAS_CODE = iri(IM_BASE, "code");
    private static final IRI HAS_SCHEME = iri(IM_BASE, "has_scheme");
    private static final IRI HAS_NAME = RDFS.LABEL;
    private static final IRI HAS_DESCRIPTION = RDFS.COMMENT;
    private static final IRI HAS_STATUS = iri(IM_BASE, "has_status");
    private static final IRI CONCEPT_TYPE = RDF.TYPE;
    private static final IRI IS_A = iri("sn:116680003");
    private static final IRI FOR_MODULE = iri(IM_BASE, "for_module");
    private static final IRI FOR_ONTOLOGY = iri(IM_BASE, "for_ontology");
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

    private Repository db;
    private Model model = new TreeModel();

    public OntologyFilerRDF4JDAL() {
        db = new SailRepository(new MemoryStore());

        // File dataDir = new File("H:\\RDF4J");
        // db = new SailRepository(new NativeStore(dataDir));

        // db = new HTTPRepository("http://localhost:7200/", "InformationModel");
    }

    @Override
    public void startTransaction() throws SQLException {
    }

    @Override
    public void commit() throws SQLException {
    }

    @Override
    public void close() throws SQLException {
        try (RepositoryConnection conn = db.getConnection()) {
            model.getNamespaces().forEach(ns -> conn.setNamespace(ns.getPrefix(), ns.getName()));
            conn.add(model);

            // TODO: Uncomment to output complete model
            // LOG.info("Log entire model (Turtle)");
            // debugWrite(model);


            LOG.info("Finding value sets...");

            try (RepositoryResult<Statement> result = conn.getStatements(null, IS_A, getIri(":VSET_ValueSet"))) {
                for (Statement st : result) {
                    LOG.info(st.toString());
                }
            }

            LOG.info("Covid 4 definition...");

            Model aboutCovid4 = getDefinition(conn, getIri(":VSET_Covid4"));

            debugWrite(aboutCovid4);

            LOG.info("Sparql query (? is a VSET_Covid0)...");

            String qry = "PREFIX im: <http://www.DiscoveryDataService.org/InformationModel/Ontology#>\n" +
                "PREFIX sn: <http://snomed.info/sct#>\n" +
                "SELECT ?s ?n\n" +
                "WHERE {?s <sn:116680003> <im:VSET_Covid0>;" +
                "   rdfs:label ?n.\n" +
                "}";

            TupleQuery query = conn.prepareTupleQuery(qry);
            TupleQueryResultHandler tsvWriter = new SPARQLResultsTSVWriter(System.err);
            query.evaluate(tsvWriter);

            LOG.info("Done.");
        } finally {
            db.shutDown();
        }
    }

    @Override
    public void rollBack() throws SQLException {
    }

    @Override
    public void fileIsa(Concept concept, String moduleIri) throws SQLException {
        // TODO: Delete previous entries
        for (ConceptReference ref : concept.getIsA())
            model.add(getIri(concept.getIri()), IS_A, getIri(ref.getIri()));
    }

    @Override
    public void fileTerm(TermConcept term) throws SQLException {
        // TODO: Terms/codes
    }

    @Override
    public void upsertNamespace(Namespace ns) throws SQLException {
        if (":".equals(ns.getPrefix()))
            model.setNamespace("im", ns.getIri());
        else
            model.setNamespace(ns.getPrefix().replace(":", ""), ns.getIri());
    }

    @Override
    public void upsertOntology(String iri) throws SQLException {
        // Not required, just an iri (Subject)
    }

    @Override
    public void addDocument(Ontology ontology) throws SQLException {
        // TODO: Still needed?
        IRI docIri = iri(DOCUMENT_BASE + ontology.getDocumentInfo().getDocumentId());
        model.add(docIri, FOR_MODULE, getIri(ontology.getModule()));
        model.add(docIri, FOR_ONTOLOGY, getIri(ontology.getIri()));
    }

    @Override
    public void upsertModule(String iri) throws SQLException {
        // Not required, just an iri (Subject)
    }

    @Override
    public void upsertConcept(Concept concept) {
        if (concept.getCode() != null && concept.getScheme() == null)
            throw new IllegalStateException("Code " + concept.getCode() + " without a code scheme");

        IRI conceptIri = getIri(concept.getIri());

        if (concept.getConceptType() != null) {
            switch (concept.getConceptType()) {
                case DATATYPE: model.add(conceptIri, CONCEPT_TYPE, RDFS.DATATYPE); break;
                case CLASSONLY: model.add(conceptIri, CONCEPT_TYPE, OWL.CLASS); break;
                case OBJECTPROPERTY: model.add(conceptIri, CONCEPT_TYPE, OWL.OBJECTPROPERTY); break;
                case DATAPROPERTY: model.add(conceptIri, CONCEPT_TYPE, OWL.DATATYPEPROPERTY); break;
                case ANNOTATION: model.add(conceptIri, CONCEPT_TYPE, OWL.ANNOTATIONPROPERTY); break;
                case INDIVIDUAL: model.add(conceptIri, CONCEPT_TYPE, OWL.NAMEDINDIVIDUAL); break;
                default: throw new IllegalStateException("Unhandled concept type");
            }
        }
        if (concept.getName() != null) model.add(conceptIri, HAS_NAME, literal(concept.getName()));
        if (concept.getStatus() != null) model.add(conceptIri, HAS_STATUS, literal(concept.getStatus().getName()));
        if (concept.getDescription() != null) model.add(conceptIri, HAS_DESCRIPTION, literal(concept.getDescription()));
        if (concept.getCode() != null) {
            model.add(conceptIri, HAS_CODE, literal(concept.getCode()));
            model.add(conceptIri, HAS_SCHEME, getIri(concept.getScheme().getIri()));
        }
    }

    @Override
    public void fileIndividual(Individual indi) throws SQLException {
        upsertConcept(indi);

        IRI conceptIri = getIri(indi.getIri());
        model.add(conceptIri, CONCEPT_TYPE, OWL.NAMEDINDIVIDUAL);
        model.add(conceptIri, CONCEPT_TYPE, getIri(indi.getIsType().getIri()));

        if (indi.getObjectPropertyAssertion() != null) {
            for(ObjectPropertyValue opv: indi.getObjectPropertyAssertion()) {
                model.add(conceptIri, getIri(opv.getProperty().getIri()), getIri(opv.getValueType().getIri()));
            }
        }
        if (indi.getDataPropertyAssertion() != null) {
            for(DataPropertyValue dpv: indi.getDataPropertyAssertion()) {
                model.add(conceptIri, getIri(dpv.getProperty().getIri()), literal(dpv.getValueData()));
            }
        }
    }

    @Override
    public void fileAxioms(Concept concept) throws DataFormatException, SQLException {
        // TODO: deleteConceptAxioms(concept);
        ConceptType conceptType = concept.getConceptType();

        fileConceptAnnotations(concept);
        fileClassExpressions(concept);

        if (conceptType == ConceptType.OBJECTPROPERTY)
            fileObjectPropertyAxioms((ObjectProperty) concept);
        else if (conceptType == ConceptType.DATAPROPERTY)
            fileDataPropertyAxioms((DataProperty) concept);
        else if (conceptType == ConceptType.DATATYPE) {
            // TODO: Data types
//            DataType dataType = (DataType) concept;
//            if (dataType.getDataTypeDefinition()!=null)
//                insertDataTypeDefinition((DataType) concept);
        } else if (conceptType==ConceptType.ANNOTATION)
            fileAnnotationPropertyAxioms((AnnotationProperty) concept);
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

    private void fileClassExpressions(Concept concept) {
        IRI conceptIri = getIri(concept.getIri());
        if (concept.getEquivalentTo() != null) {
            for(ClassExpression cex: concept.getEquivalentTo()) {
                Value v = getExpressionAsValue(cex);
                model.add(conceptIri, EQUIVALENT_TO, v);
            }
        }
        if (concept.getSubClassOf() != null) {
            for(ClassExpression cex: concept.getSubClassOf()) {
                Value v = getExpressionAsValue(cex);
                model.add(conceptIri, SUBCLASS_OF, v);
            }
        }
        if (concept.getDisjointWith() != null) {
            for (ConceptReference disj : concept.getDisjointWith()) {
                model.add(conceptIri, DISJOINT_WITH, getIri(disj.getIri()));
            }
        }
    }

    private void fileObjectPropertyAxioms(ObjectProperty op) {
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
                model.add(conceptIri, PROPERTY_RANGE, getExpressionAsValue(range));
            }
        }
        if (op.getPropertyDomain() != null) {
            for (ClassExpression domain : op.getPropertyDomain()) {
                model.add(conceptIri, PROPERTY_DOMAIN, getExpressionAsValue(domain));
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

    private void fileDataPropertyAxioms(DataProperty dp) {
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
                model.add(conceptIri, PROPERTY_DOMAIN, getExpressionAsValue(domain));
            }
        }
    }

    private void fileAnnotationPropertyAxioms(AnnotationProperty ap) {
        if (ap.getSubAnnotationPropertyOf() != null){
            IRI conceptIri = getIri(ap.getIri());
            for (PropertyAxiom ax : ap.getSubAnnotationPropertyOf()) {
                model.add(conceptIri, SUB_PROPERTY, getIri(ax.getProperty().getIri()));
            }
        }
    }

    private Value getExpressionAsValue(ClassExpression exp) {
        if (exp.getClazz() != null) return getIri(exp.getClazz().getIri());
        if (exp.getIntersection() != null) {
            Resource r = bnode();
            for (ClassExpression i : exp.getIntersection()) {
                Value v = getExpressionAsValue(i);
                model.add(r, INTERSECTION, v);
            }
            return r;
        }
        if (exp.getUnion() != null) {
            Resource r = bnode();
            for (ClassExpression i : exp.getUnion()) {
                Value v = getExpressionAsValue(i);
                model.add(r, UNION, v);
            }
            return r;
        }
        if (exp.getObjectPropertyValue() != null) {
            ObjectPropertyValue opv = exp.getObjectPropertyValue();
            Resource r = bnode();
            if (opv.getValueType() != null)
                model.add(r, getIri(opv.getProperty().getIri()), getIri(opv.getValueType().getIri()));
            else if (opv.getExpression() != null)
                model.add(r, getIri(opv.getProperty().getIri()), getExpressionAsValue(opv.getExpression()));

            /*  OWL Structured representation

            model.add(r, RDF.TYPE, OWL.RESTRICTION);
            model.add(r, OWL.ONPROPERTY, getIri(opv.getProperty().getIri()));
            if (opv.getValueType() != null)
                model.add(r, OBJECTSOMEVALUES, getIri(opv.getValueType().getIri()));
            else if (opv.getExpression() != null)
                model.add(r, OBJECTSOMEVALUES, getExpressionAsValue(opv.getExpression())); */
            else
                LOG.error("Unhandled OPV");
            return r;
        }
        if (exp.getDataPropertyValue() != null) {
            DataPropertyValue dpv = exp.getDataPropertyValue();
            Resource r = bnode();
            model.add(r, RDF.TYPE, OWL.RESTRICTION);
            model.add(r, OWL.ONPROPERTY, getIri(dpv.getProperty().getIri()));
            if (dpv.getDataType() != null)
                model.add(r, OBJECT_SOME_VALUES, getIri(dpv.getDataType().getIri()));
            else
                LOG.error("Unhandled DPV");
            return r;
        }
        if (exp.getComplementOf() != null) {
            Resource r = bnode();
            model.add(r, RDF.TYPE, OWL.CLASS);
            model.add(r, OWL.COMPLEMENTOF, getExpressionAsValue(exp.getComplementOf()));
            return r;
        }
        if (exp.getObjectOneOf() != null) {
            Resource r = bnode();
            return r;
        }
        LOG.error("Unhandled expression");
        return bnode();
    }

    private IRI getIri(String conceptIri) {
        if (conceptIri.startsWith(":"))
            return iri("im" + conceptIri.trim());
        else
            return iri(conceptIri.trim());
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
            if (s.getObject().isBNode()) {
                addResourceStatementsToModel(conn, (Resource)s.getObject(), m);
            }
        }
    }
}
