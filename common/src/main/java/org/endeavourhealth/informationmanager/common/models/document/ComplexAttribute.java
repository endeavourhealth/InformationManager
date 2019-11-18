package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class ComplexAttribute {
    private String description;
    private Operator operator;

    private String property;
    private Function function;
    private String definedFunction;

    private ExpressionConstraint valueExpression;
    private ValueComparison value;
    private Range range;
    private String valueSet;
    private String valueCode;
    private Boolean valueBoolean;
    private Boolean hasValue;

    private Boolean includeNull;
    private String assignedProperty;
    private String assignedBooleanProperty;

    private List<ComplexAttribute> attribute = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public ComplexAttribute setDescription(String description) {
        this.description = description;
        return this;
    }

    public Operator getOperator() {
        return operator;
    }

    public ComplexAttribute setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public ComplexAttribute setProperty(String property) {
        this.property = property;
        return this;
    }

    public Function getFunction() {
        return function;
    }

    public ComplexAttribute setFunction(Function function) {
        this.function = function;
        return this;
    }

    public String getDefinedFunction() {
        return definedFunction;
    }

    public ComplexAttribute setDefinedFunction(String definedFunction) {
        this.definedFunction = definedFunction;
        return this;
    }

    public ExpressionConstraint getValueExpression() {
        return valueExpression;
    }

    public ComplexAttribute setValueExpression(ExpressionConstraint valueExpression) {
        this.valueExpression = valueExpression;
        return this;
    }

    public ValueComparison getValue() {
        return value;
    }

    public ComplexAttribute setValue(ValueComparison value) {
        this.value = value;
        return this;
    }

    public Range getRange() {
        return range;
    }

    public ComplexAttribute setRange(Range range) {
        this.range = range;
        return this;
    }

    public String getValueSet() {
        return valueSet;
    }

    public ComplexAttribute setValueSet(String valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public String getValueCode() {
        return valueCode;
    }

    public ComplexAttribute setValueCode(String valueCode) {
        this.valueCode = valueCode;
        return this;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public ComplexAttribute setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
        return this;
    }

    public Boolean getHasValue() {
        return hasValue;
    }

    public ComplexAttribute setHasValue(Boolean hasValue) {
        this.hasValue = hasValue;
        return this;
    }

    public Boolean getIncludeNull() {
        return includeNull;
    }

    public ComplexAttribute setIncludeNull(Boolean includeNull) {
        this.includeNull = includeNull;
        return this;
    }

    public String getAssignedProperty() {
        return assignedProperty;
    }

    public ComplexAttribute setAssignedProperty(String assignedProperty) {
        this.assignedProperty = assignedProperty;
        return this;
    }

    public String getAssignedBooleanProperty() {
        return assignedBooleanProperty;
    }

    public ComplexAttribute setAssignedBooleanProperty(String assignedBooleanProperty) {
        this.assignedBooleanProperty = assignedBooleanProperty;
        return this;
    }

    public List<ComplexAttribute> getAttribute() {
        return attribute;
    }

    public ComplexAttribute setAttribute(List<ComplexAttribute> attribute) {
        this.attribute = attribute;
        return this;
    }
}
