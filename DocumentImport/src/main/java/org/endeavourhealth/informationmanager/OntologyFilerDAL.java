package org.endeavourhealth.informationmanager;

import org.endeavourhealth.imapi.model.*;

import java.sql.SQLException;
import java.util.zip.DataFormatException;

public interface OntologyFilerDAL {
    void startTransaction() throws SQLException;

    void commit() throws SQLException;

    void close() throws SQLException;

    void rollBack() throws SQLException;

    void fileIsa(Concept concept, String moduleIri) throws SQLException;

    // ------------------------------ NAMESPACE ------------------------------
    void upsertNamespace(Namespace ns) throws SQLException;

    // ------------------------------ ONTOLOGY ------------------------------
    void upsertOntology(String iri) throws SQLException;

    // ------------------------------ ONTOLOGY ------------------------------
    void addDocument(Ontology ontology) throws SQLException;

    // ------------------------------ MODULE ------------------------------
    void upsertModule(String iri) throws SQLException;

    void upsertConcept(Concept concept) throws DataFormatException, SQLException;

    void fileIndividual(Individual indi) throws SQLException;

    void fileAxioms(Concept concept) throws DataFormatException, SQLException;

    void dropIndexes() throws SQLException;

    void restoreIndexes() throws SQLException;

    void dropGraph();
}
