package org.endeavourhealth.informationmanager.common.dal;


import org.endeavourhealth.informationmanager.common.models.Document;
import org.endeavourhealth.informationmanager.common.models.Version;

import java.sql.ResultSet;
import java.sql.SQLException;

class DocumentHydrator {
    public static Document create(ResultSet resultSet) throws SQLException {
        return populate(new Document(), resultSet);
    }

    public static Document populate(Document document, ResultSet resultSet) throws SQLException {
        return document
            .setDbid(resultSet.getInt("dbid"))
            .setPath(resultSet.getString("path"))
            .setVersion(Version.fromString(resultSet.getString("version")))
            .setDrafts(resultSet.getInt("draft"));
    }
}
