package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {
    private Integer groupNumber;
    private String property;
    private ClassExpression valueClass;
    private String valueData;
    private Integer minCardinality;
    private Integer maxCardinality;
    private Boolean universal;

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public Role setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
        return this;
    }

    @JsonProperty("Property")
    public String getProperty() {
        return property;
    }

    public Role setProperty(String property) {
        this.property = property;
        return this;
    }

    @JsonProperty("ValueClass")
    public ClassExpression getValueClass() {
        return valueClass;
    }

    public Role setValueClass(ClassExpression valueClass) {
        this.valueClass = valueClass;
        return this;
    }

    @JsonProperty("ValueData")
    public String getValueData() {
        return valueData;
    }

    public Role setValueData(String valueData) {
        this.valueData = valueData;
        return this;
    }

    @JsonProperty("MinCardinality")
    public Integer getMinCardinality() {
        return minCardinality;
    }

    public Role setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    @JsonProperty("MaxCardinality")
    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public Role setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public Boolean getUniversal() {
        return universal;
    }

    public Role setUniversal(Boolean universal) {
        this.universal = universal;
        return this;
    }
}
