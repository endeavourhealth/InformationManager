package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DPERestriction extends DataRange {
    private String property;

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public DPERestriction setProperty(String property) {
        this.property = property;
        return this;
    }
}
