package org.endeavourhealth.informationmanager.common.models.definitionTypes;

public class PropertyDefinition {
    private String property;
    private String operator;
    private Integer minCardinality;
    private Integer maxCardinality;
    private String object;
    private String data;
    private String valueOperator;
    private boolean inferred;

    public String getProperty() {
        return property;
    }

    public PropertyDefinition setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public PropertyDefinition setOperator(String operator) {
        this.operator = operator;
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

    public String getValueOperator() {
        return valueOperator;
    }

    public PropertyDefinition setValueOperator(String valueOperator) {
        this.valueOperator = valueOperator;
        return this;
    }

    public boolean isInferred() {
        return inferred;
    }

    public PropertyDefinition setInferred(boolean inferred) {
        this.inferred = inferred;
        return this;
    }
}
