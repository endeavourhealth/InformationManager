package org.endeavourhealth.informationmanager.common.models.document;

public class DataTypeDefinition {
    private String status;
    private String dataType;
    private String restriction;

    public String getStatus() {
        return status;
    }

    public DataTypeDefinition setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public DataTypeDefinition setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getRestriction() {
        return restriction;
    }

    public DataTypeDefinition setRestriction(String restriction) {
        this.restriction = restriction;
        return this;
    }
}
