package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;

import java.sql.SQLException;
import java.util.zip.DataFormatException;

public interface TTDocumentFilerDAL {
   void startTransaction() throws SQLException;

   void commit() throws SQLException;

   void close() throws SQLException;

   void rollBack() throws SQLException;


   // ------------------------------ NAMESPACE ------------------------------
   void upsertNamespace(TTPrefix ns) throws SQLException;

   void fileConcept(TTConcept concept) throws SQLException, DataFormatException, JsonProcessingException, FileFormatException;

   void fileIndividual(TTConcept indi) throws SQLException, DataFormatException, JsonProcessingException, FileFormatException;

   void setGraph(TTIriRef graph);



}
