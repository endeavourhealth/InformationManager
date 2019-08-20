package org.endeavourhealth.informationmanager.common.models;

public class ConceptDomain {
    private String property;
    private Boolean mandatory;
    private Integer limit;

    public String getProperty() {
        return property;
    }

    public ConceptDomain setProperty(String property) {
        this.property = property;
        return this;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public ConceptDomain setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public ConceptDomain setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
