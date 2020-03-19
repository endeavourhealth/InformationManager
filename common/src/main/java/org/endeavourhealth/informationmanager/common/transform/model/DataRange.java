package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataRange {
    private String dataType;
    private DataTypeRestriction dataTypeRestriction;

    @JsonProperty("DataType")
    public String getDataType() {
        return dataType;
    }

    public DataRange setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    @JsonProperty("DataTypeRestriction")
    public DataTypeRestriction getDataTypeRestriction() {
        return dataTypeRestriction;
    }

    public DataRange setDataTypeRestriction(DataTypeRestriction dataTypeRestriction) {
        this.dataTypeRestriction = dataTypeRestriction;
        return this;
    }
}
