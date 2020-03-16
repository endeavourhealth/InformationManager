package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SimpleProperty {
    private List<Annotation> annotation;
    private String property;

    @JsonProperty("Annotation")
    public List<Annotation> getAnnotation() {
        return annotation;
    }

    public SimpleProperty setAnnotation(List<Annotation> annotation) {
        this.annotation = annotation;
        return this;
    }

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public SimpleProperty setProperty(String property) {
        this.property = property;
        return this;
    }
}
