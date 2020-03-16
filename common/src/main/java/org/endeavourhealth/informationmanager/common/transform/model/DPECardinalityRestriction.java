package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DPECardinalityRestriction extends DataRange {
    private String property;
    private Integer exact;
    private Integer min;
    private Integer max;

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public DPECardinalityRestriction setProperty(String property) {
        this.property = property;
        return this;
    }

    public Integer getExact() {
        return exact;
    }

    public DPECardinalityRestriction setExact(Integer exact) {
        this.exact = exact;
        return this;
    }

    public Integer getMin() {
        return min;
    }

    public DPECardinalityRestriction setMin(Integer min) {
        this.min = min;
        return this;
    }

    public Integer getMax() {
        return max;
    }

    public DPECardinalityRestriction setMax(Integer max) {
        this.max = max;
        return this;
    }
}
