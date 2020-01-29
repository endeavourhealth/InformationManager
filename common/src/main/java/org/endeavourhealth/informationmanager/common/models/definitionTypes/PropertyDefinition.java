package org.endeavourhealth.informationmanager.common.models.definitionTypes;

public class PropertyDefinition extends Definition<PropertyDefinition> {
    private String property;
    private Integer minCardinality;
    private Integer maxCardinality;
    private String object;
    private String data;

    public String getProperty() {
        return property;
    }

    public PropertyDefinition setProperty(String property) {
        this.property = property;
        return this;
    }

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public PropertyDefinition setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public PropertyDefinition setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public String getObject() {
        return object;
    }

    public PropertyDefinition setObject(String object) {
        this.object = object;
        return this;
    }

    public String getData() {
        return data;
    }

    public PropertyDefinition setData(String data) {
        this.data = data;
        return this;
    }
}
