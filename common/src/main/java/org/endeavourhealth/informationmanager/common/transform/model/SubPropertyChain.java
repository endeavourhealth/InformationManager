package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubPropertyChain {
    private List<String> property;

    @JsonProperty("Property")
    public List<String> getProperty() {
        return property;
    }

    public SubPropertyChain setProperty(List<String> property) {
        this.property = property;
        return this;
    }
}
