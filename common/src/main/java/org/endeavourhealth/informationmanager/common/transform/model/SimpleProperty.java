package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SimpleProperty extends IMEntity{
    private String property;

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public SimpleProperty setProperty(String property) {
        this.property = property;
        return this;
    }
}
