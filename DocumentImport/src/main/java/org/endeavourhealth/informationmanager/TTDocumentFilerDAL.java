package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTInstance;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.zip.DataFormatException;

public interface TTDocumentFilerDAL {
   void startTransaction() throws SQLException;

   void commit() throws SQLException;

   void close() throws SQLException;

   void rollBack() throws SQLException;


   // ------------------------------ NAMESPACE ------------------------------
   void upsertNamespace(TTPrefix ns) throws SQLException;

   void fileConcept(TTConcept concept) throws SQLException, DataFormatException, JsonProcessingException, FileFormatException, NoSuchAlgorithmException;

   void fileIndividual(TTInstance indi) throws SQLException, DataFormatException, JsonProcessingException, FileFormatException;

   void setGraph(TTIriRef graph);

   void filePredicateUpdates(TTConcept concept) throws SQLException, IOException, DataFormatException;

   void fileAddPredicateObjects(TTConcept concept) throws SQLException, IOException, DataFormatException;



}
