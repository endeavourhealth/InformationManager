package org.endeavourhealth.informationmanager.common.models.jsonDocument;

public class PropertyObject {
    private String property;
    private String conceptValue;
    private Integer minCardinality;
    private Integer maxCardinality;

    public String getProperty() {
        return property;
    }

    public PropertyObject setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getConceptValue() {
        return conceptValue;
    }

    public PropertyObject setConceptValue(String conceptValue) {
        this.conceptValue = conceptValue;
        return this;
    }

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public PropertyObject setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public PropertyObject setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }
}
