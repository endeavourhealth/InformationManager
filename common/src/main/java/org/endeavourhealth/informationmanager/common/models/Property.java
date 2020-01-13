package org.endeavourhealth.informationmanager.common.models;

public class Property {
    private Integer group;
    private String property;
    private Integer minCardinality;
    private Integer maxCardinality;
    private Boolean inferred;
    private String data;
    private String object;

    public Integer getGroup() {
        return group;
    }

    public Property setGroup(Integer group) {
        this.group = group;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public Property setProperty(String property) {
        this.property = property;
        return this;
    }

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public Property setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public Property setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public Boolean getInferred() {
        return inferred;
    }

    public Property setInferred(Boolean inferred) {
        this.inferred = inferred;
        return this;
    }

    public String getData() {
        return data;
    }

    public Property setData(String data) {
        this.data = data;
        return this;
    }

    public String getObject() {
        return object;
    }

    public Property setObject(String object) {
        this.object = object;
        return this;
    }
}
