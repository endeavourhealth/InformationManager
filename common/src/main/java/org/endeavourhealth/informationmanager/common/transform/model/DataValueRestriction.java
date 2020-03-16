package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataValueRestriction {
    private String property;
    private String value;

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public DataValueRestriction setProperty(String property) {
        this.property = property;
        return this;
    }

    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    public DataValueRestriction setValue(String value) {
        this.value = value;
        return this;
    }
}
