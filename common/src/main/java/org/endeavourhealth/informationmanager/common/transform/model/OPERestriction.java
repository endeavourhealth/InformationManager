package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OPERestriction extends ClassExpression {
    private String property;
    private String inverseProperty;

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public OPERestriction setProperty(String property) {
        this.property = property;
        return this;
    }

    @JsonProperty("InverseProperty")
    public String getInverseProperty() {
        return inverseProperty;
    }

    public OPERestriction setInverseProperty(String inverseProperty) {
        this.inverseProperty = inverseProperty;
        return this;
    }
}
