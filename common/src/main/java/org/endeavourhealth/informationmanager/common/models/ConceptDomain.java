package org.endeavourhealth.informationmanager.common.models;

public class ConceptDomain {
    private String property;
    private Integer minimum;
    private Integer maximum;
    private String inherits;

    public String getProperty() {
        return property;
    }

    public ConceptDomain setProperty(String property) {
        this.property = property;
        return this;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public ConceptDomain setMinimum(Integer minimum) {
        this.minimum = minimum;
        return this;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public ConceptDomain setMaximum(Integer maximum) {
        this.maximum = maximum;
        return this;
    }

    public String getInherits() {
        return inherits;
    }

    public ConceptDomain setInherits(String inherits) {
        this.inherits = inherits;
        return this;
    }
}
