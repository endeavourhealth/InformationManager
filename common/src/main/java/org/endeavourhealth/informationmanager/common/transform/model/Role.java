package org.endeavourhealth.informationmanager.common.transform.model;

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

    public String getProperty() {
        return property;
    }

    public Role setProperty(String property) {
        this.property = property;
        return this;
    }

    public ClassExpression getValueClass() {
        return valueClass;
    }

    public Role setValueClass(ClassExpression valueClass) {
        this.valueClass = valueClass;
        return this;
    }

    public String getValueData() {
        return valueData;
    }

    public Role setValueData(String valueData) {
        this.valueData = valueData;
        return this;
    }

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public Role setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

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
