package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    //  dal = new OntologyFilerRDF4JDAL();
    }

    // ============================== PUBLIC METHODS ============================



    /**
     * Files a Discovery syntax ontology module (graph)
     * <p>This assumes that all axioms for a concept for this module are included i.e. is a replace all axioms for concept for graph</p>
     * <p></p>Concepts from other modules can be referenced in the axiom but if the entity is declared in this ontology, then the associated axioms for this module will be filed</p>
     *<p>Note that only those declared concepts whose IRIS have module/ namespace authoring permission will be updated </p>
     * Thus if the ontology module contains annotation properties for concepts with IRIs from external namespaces that do not have autthoring permission
     * they will be ignored.
     * @param ontology  An ontology module document in Discovery syntax
        * @throws Exception
     */


    public void fileOntology(Ontology ontology,boolean large) throws SQLException, DataFormatException, JsonProcessingException {
        try {
            System.out.println("Saving ontology - " + (new Date().toString()));
            LOG.info("Processing namespaces");
            // Ensure all namespaces exist (auto-create)
            //The document prefixes (ns) may not be the same as the IM DB prefixes
            fileNamespaces(ontology.getNamespace());
            ;
            // Record document details, updating ontology and module
            LOG.info("Processing document-ontology-module");

            fileDocument(ontology);

            LOG.info("Processing Classes");
//            fileConcepts(ontology.getConcept());
  //          fileIndividuals(ontology.getIndividual());
            fileAxioms(ontology.getConcept());



            LOG.info("Ontology filed");
        } catch (Exception e) {
            LOG.info("Error - " + (new Date().toString()));
            e.printStackTrace();
            throw e;
        } finally {
            if (large)
                dal.restoreIndexes();
            close();
            System.out.println("Finished - " + (new Date().toString()));
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
        startTransaction();
        //Populates the namespace map with both namespace iri and prefix as keys
        for (Namespace ns : namespaces) {
            dal.upsertNamespace(ns);
        }
        commit();
    }

    // ------------------------------ DOCUMENT MODULE ONTOLOGY ------------------------------
    private void fileDocument(Ontology ontology) throws SQLException {
        startTransaction();
        dal.upsertOntology(ontology.getIri());
        dal.upsertModule(ontology.getIri());
        dal.addDocument(ontology);
        commit();
    }

    private void fileAxioms(Set<? extends Concept> concepts) throws SQLException, DataFormatException {
        if (concepts == null || concepts.size() == 0)
            return;
        startTransaction();
        int i = 0;
        for (Concept concept : concepts) {
            dal.fileAxioms(concept);
            if (concept.getIsA()!=null)
                dal.fileIsa(concept,null);
            i++;
            if (i % 5000 == 0) {
                LOG.info("filing " + i + " of " + concepts.size()+" axioms groups");
                System.out.println("Filed " + i + " of " + concepts.size()+" axiom groups");
                commit();
                startTransaction();
            }
        }
        System.out.println("Filed " + i +"axiom groups");
        commit();
    }
    private void fileConcepts(Set<? extends Concept> concepts) throws SQLException, DataFormatException, JsonProcessingException {
        if (concepts == null || concepts.size() == 0)
            return;

        int i = 0;
        startTransaction();
        for (Concept concept : concepts) {
           dal.upsertConcept(concept);
            i++;
            if (i % 5000 == 0) {
                LOG.info("Filed " + i + " of " + concepts.size()+" concepts");
                System.out.println("Filing " + i + " of " + concepts.size()+" concepts to model within transaction");
                commit();
                startTransaction();
            }

        }
        System.out.println("Filed "+i + " concepts");
        commit();

    }

    private void fileIndividuals(Set<Individual> individuals) throws SQLException, DataFormatException {

        if (individuals == null || individuals.size() == 0)
            return;
        startTransaction();
        int i = 0;
        for (Individual indi : individuals) {
            dal.upsertIndividual(indi);
            i++;
            if (i % 500 == 0) {
                LOG.info("Filed " + i + " of " + individuals.size()+" concepts");
                System.out.println("Filing " + i + " of " + individuals.size()+" concepts to model ");

            }
        }
        System.out.println("Filing "+i + " concepts");
        commit();

    }





}


