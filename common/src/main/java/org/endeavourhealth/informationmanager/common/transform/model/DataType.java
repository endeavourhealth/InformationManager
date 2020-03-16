package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataType extends Concept {
    private DataTypeDefinition dataTypeDefinition;

    @JsonProperty("DataTypeDefinition")
    public DataTypeDefinition getDataTypeDefinition() {
        return dataTypeDefinition;
    }

    public DataType setDataTypeDefinition(DataTypeDefinition dataTypeDefinition) {
        this.dataTypeDefinition = dataTypeDefinition;
        return this;
    }
}
