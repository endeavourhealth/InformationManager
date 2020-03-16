package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Annotation {
    private String property;
    private String value;

    @JsonProperty("Annotation")
    public String getProperty() {
        return property;
    }

    public Annotation setProperty(String property) {
        this.property = property;
        return this;
    }

    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    public Annotation setValue(String value) {
        this.value = value;
        return this;
    }
}
