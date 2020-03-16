package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SubObjectProperty {
    private String property;
    private String inverseProperty;
    private List<String> propertyChain;

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public SubObjectProperty setProperty(String property) {
        this.property = property;
        return this;
    }

    @JsonProperty("InverseProperty")
    public String getInverseProperty() {
        return inverseProperty;
    }

    public SubObjectProperty setInverseProperty(String inverseProperty) {
        this.inverseProperty = inverseProperty;
        return this;
    }

    @JsonProperty("PropertyChain")
    public List<String> getPropertyChain() {
        return propertyChain;
    }

    public SubObjectProperty setPropertyChain(List<String> propertyChain) {
        this.propertyChain = propertyChain;
        return this;
    }
}
