package org.endeavourhealth.informationmanager.common.models.document;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleExpressionConstraint {
    private String name;
    private Operator operator;
    private String clazz;
    private String classOrSubtypes;
    private String subtypes;
    private String valueSet;
    private String dataType;

    public String getName() {
        return name;
    }

    public SimpleExpressionConstraint setName(String name) {
        this.name = name;
        return this;
    }

    public Operator getOperator() {
        return operator;
    }

    public SimpleExpressionConstraint setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    @JsonProperty("class")
    public String getClazz() {
        return clazz;
    }

    public SimpleExpressionConstraint setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getClassOrSubtypes() {
        return classOrSubtypes;
    }

    public SimpleExpressionConstraint setClassOrSubtypes(String classOrSubtypes) {
        this.classOrSubtypes = classOrSubtypes;
        return this;
    }

    public String getSubtypes() {
        return subtypes;
    }

    public SimpleExpressionConstraint setSubtypes(String subtypes) {
        this.subtypes = subtypes;
        return this;
    }

    public String getValueSet() {
        return valueSet;
    }

    public SimpleExpressionConstraint setValueSet(String valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public SimpleExpressionConstraint setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }
}
