package org.endeavourhealth.informationmanager;

import org.endeavourhealth.imapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class OntologyFiler {
    private static final Logger LOG = LoggerFactory.getLogger(OntologyFiler.class);
    private static final String SUBTYPE = "sn:116680003";
    private final OntologyFilerDAL dal;
    private final Set<String> undefinedConcepts = new HashSet<>();
    /**
     * Files a Discovery syntax ontology module and/or classification module into a relational database OWL ontoology store.
     * <p>This assumes that all axioms for a concept for this module are included i.e. is a replace all axioms for concept for module</p>
     * <p></p>Concepts from other modules can be referenced in the axiom but if the entity is declared in this ontology, then the associated axioms for this module will be filed</p>
     *<p>Note that only those declared concepts whose IRIS have module/ namespace authoring permission will be updated </p>
     * Thus if the ontology module contains annotation properties for concepts with IRIs from external namespaces that do not have autthoring permission
     * they will be ignored.
     * @throws Exception
     */
    public OntologyFiler() throws Exception {
        // TODO: Switch between JDBC and RDF4J here.

     dal = new OntologyFilerJDBCDAL();
     // dal = new OntologyFilerRDF4JDAL(true);
    }

    // ============================== PUBLIC METHODS ============================

    /**
     * Files a classification module consisting of child/parent nodes of concepts.
     * <p> Assumes that all parents are present for a child concept for this module i.e. is an add+ replacement process.
     * If other concept parents exist for this module which are not included in the set they will not be removed.</p>
     *
     *  @param conceptSet A set of concept references assuming concepts already exist in the data store
     * @throws Exception
     */
    public void fileClassification(Set<Concept> conceptSet, String moduleIri) throws Exception {
        try {
            if (!conceptSet.isEmpty()) {
                int i=0;
                startTransaction();
                for (Concept concept : conceptSet) {
                    dal.fileIsa(concept, moduleIri);
                    i++;
                    if (i % 10000 == 0) {
                        LOG.info("Filed " + i + " of " + conceptSet.size()+" classified concepts");
                        System.out.println("Filed " + i + " of " + conceptSet.size() + " classified concepts");
                        //dal.commit();
                    }
                }
            }
             commit();


        } catch (Exception e) {
            rollback();

            throw e;

        } finally {
            close();
        }
    }



    /**
     * Files a Discovery syntax ontology module into a relational database OWL ontoology store.
     * <p>This assumes that all axioms for a concept for this module are included i.e. is a replace all axioms for concept for module</p>
     * <p></p>Concepts from other modules can be referenced in the axiom but if the entity is declared in this ontology, then the associated axioms for this module will be filed</p>
     *<p>Note that only those declared concepts whose IRIS have module/ namespace authoring permission will be updated </p>
     * Thus if the ontology module contains annotation properties for concepts with IRIs from external namespaces that do not have autthoring permission
     * they will be ignored.
     * @param ontology  An ontology module document in Discovery syntax
        * @throws Exception
     */


    public void fileOntology(Ontology ontology,boolean large) throws SQLException, DataFormatException {
        try {
            if (large)
                dal.dropIndexes();
            LOG.info("Saving ontology");
            startTransaction();
            LOG.info("Processing namespaces");
            // Ensure all namespaces exist (auto-create)
            //The document prefixes (ns) may not be the same as the IM DB prefixes
            fileNamespaces(ontology.getNamespace());
           // commit();
            ;
            // Record document details, updating ontology and module
            LOG.info("Processing document-ontology-module");
            fileDocument(ontology);
            commit();

            LOG.info("Processing Classes");
            fileConcepts(ontology.getConcept());
            fileAxioms(ontology.getConcept());
            commit();

            fileIndividuals(ontology.getIndividual());
            commit();

            LOG.info("Ontology filed");
    } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(l -> System.err.println(l.toString()));
        throw e;
    } finally {
        if (large)
            dal.restoreIndexes();
        close();
        }
    }


    //==================PRIVATE METHODS=================

    private void startTransaction() throws SQLException {
        dal.startTransaction();
    }

    private void close() throws SQLException {
        dal.close();
    }

    private void rollback() throws SQLException {
        dal.rollBack();
    }

    private void commit() throws SQLException {
        dal.commit();
    }


    private void fileNamespaces(Set<Namespace> namespaces) throws SQLException {
        if (namespaces == null || namespaces.size() == 0)
            return;
        //Populates the namespace map with both namespace iri and prefix as keys
        for (Namespace ns : namespaces) {
            dal.upsertNamespace(ns);

        }

    }

    // ------------------------------ DOCUMENT MODULE ONTOLOGY ------------------------------
    private void fileDocument(Ontology ontology) throws SQLException {
        dal.upsertModule(ontology.getModule());
        dal.upsertOntology(ontology.getIri());
        dal.addDocument(ontology);
    }

    private void fileIndividuals(Set<Individual> indis) throws SQLException {
        if (indis == null || indis.size() == 0)
            return;

        int i = 0;
        for (Individual ind : indis) {
            dal.fileIndividual(ind);

        }

    }

    private void fileAxioms(Set<? extends Concept> concepts) throws SQLException, DataFormatException {
        if (concepts == null || concepts.size() == 0)
            return;

        int i = 0;
        for (Concept concept : concepts) {
            dal.fileAxioms(concept);
            if (concept.getIsA()!=null)
                dal.fileIsa(concept,null);
            i++;
            if (i % 1000 == 0) {
                LOG.info("Filed " + i + " of " + concepts.size()+" axioms groups");
                System.out.println("Filed " + i + " of " + concepts.size()+" axiom groups");
                dal.commit();
            }
        }
        dal.commit();
    }
    private void fileConcepts(Set<? extends Concept> concepts) throws SQLException, DataFormatException {
        if (concepts == null || concepts.size() == 0)
            return;

        int i = 0;
        for (Concept concept : concepts) {
           dal.upsertConcept(concept);
            i++;
            if (i % 1000 == 0) {
                LOG.info("Filed " + i + " of " + concepts.size()+" concepts");
                System.out.println("Filed " + i + " of " + concepts.size()+" concepts");
                dal.commit();
            }
        }
        dal.commit();
    }







}


