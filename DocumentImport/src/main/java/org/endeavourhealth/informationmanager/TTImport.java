package org.endeavourhealth.informationmanager;

import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * An Interface that handles the import of a variety of data sources such as Classifications and supplier look ups
 */
public interface TTImport extends AutoCloseable{

   TTImport importData(String inFolder, boolean bulkImport, Map<String,Integer> entityMap) throws Exception;
   TTImport validateFiles(String inFolder);
   TTImport validateLookUps(Connection conn) throws SQLException, ClassNotFoundException;

}
