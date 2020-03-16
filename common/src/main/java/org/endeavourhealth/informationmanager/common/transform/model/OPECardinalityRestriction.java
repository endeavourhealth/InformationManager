package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OPECardinalityRestriction extends ClassExpression {
    private String property;
    private String inverseProperty;
    private Integer exact;
    private Integer min;
    private Integer max;
    // subClassExpression

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public OPECardinalityRestriction setProperty(String property) {
        this.property = property;
        return this;
    }

    @JsonProperty("InverseProperty")
    public String getInverseProperty() {
        return inverseProperty;
    }

    public OPECardinalityRestriction setInverseProperty(String inverseProperty) {
        this.inverseProperty = inverseProperty;
        return this;
    }

    public Integer getExact() {
        return exact;
    }

    public OPECardinalityRestriction setExact(Integer exact) {
        this.exact = exact;
        return this;
    }

    public Integer getMin() {
        return min;
    }

    public OPECardinalityRestriction setMin(Integer min) {
        this.min = min;
        return this;
    }

    public Integer getMax() {
        return max;
    }

    public OPECardinalityRestriction setMax(Integer max) {
        this.max = max;
        return this;
    }
}
