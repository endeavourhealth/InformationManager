package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataTypeDefinition {
    private List<Annotation> annotation;
    private DataTypeRestriction dataTypeRestriction;

    @JsonProperty("Annotation")
    public List<Annotation> getAnnotation() {
        return annotation;
    }

    public DataTypeDefinition setAnnotation(List<Annotation> annotation) {
        this.annotation = annotation;
        return this;
    }

    @JsonProperty("DataTypeRestriction")
    public DataTypeRestriction getDataTypeRestriction() {
        return dataTypeRestriction;
    }

    public DataTypeDefinition setDataTypeRestriction(DataTypeRestriction dataTypeRestriction) {
        this.dataTypeRestriction = dataTypeRestriction;
        return this;
    }
}
