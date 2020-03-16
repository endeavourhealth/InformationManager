package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataTypeDefinition {
    private List<Annotation> annotation;
    private DataRange dataRange;

    @JsonProperty("Annotation")
    public List<Annotation> getAnnotation() {
        return annotation;
    }

    public DataTypeDefinition setAnnotation(List<Annotation> annotation) {
        this.annotation = annotation;
        return this;
    }

    @JsonProperty("DataRange")
    public DataRange getDataRange() {
        return dataRange;
    }

    public DataTypeDefinition setDataRange(DataRange dataRange) {
        this.dataRange = dataRange;
        return this;
    }
}
