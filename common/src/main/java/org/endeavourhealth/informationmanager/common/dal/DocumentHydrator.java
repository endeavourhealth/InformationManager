package org.endeavourhealth.informationmanager.common.dal;


import org.endeavourhealth.informationmanager.common.models.document.DocumentInfo;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

class DocumentHydrator {
    public static DocumentInfo create(ResultSet resultSet) throws SQLException {
        return populate(new DocumentInfo(), resultSet);
    }

    public static DocumentInfo populate(DocumentInfo documentInfo, ResultSet resultSet) throws SQLException {
        DocumentInfo di = new DocumentInfo();
        di.setDocumentId(UUID.fromString(resultSet.getString("id")));
        di.setDocumentIri(URI.create(resultSet.getString("path")));
        return di;
    }
}
