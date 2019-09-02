package org.endeavourhealth.informationmanager.common.models;

public class ConceptDomain {
    private String property;
    private String cardinality;

    public String getProperty() {
        return property;
    }

    public ConceptDomain setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getCardinality() {
        return cardinality;
    }

    public ConceptDomain setCardinality(String cardinality) {
        this.cardinality = cardinality;
        return this;
    }
}
