package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
// (not needed) import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComplexAttribute {

    private String description;
    private Operator operator;
    private String property;
    private Function function;
    private String definedFunction;
    private List <ExpressionConstraint> valueExpression;
    private ValueComparison value;
    private Range valueRange;
    private String valueSet;
    private String valueCode;
    private Boolean valueBoolean;
    private Boolean hasValue;
    private Boolean includeNull;
    private String assignedProperty;
    private String assignedBooleanProperty;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getDefinedFunction() {
        return definedFunction;
    }

    public void setDefinedFunction(String definedFunction) {
        this.definedFunction = definedFunction;
    }

    /**
     * Gets the value of the valueExpression list.
     *
     * To add a new item, do as follows:
     * getValueExpression().add(newItem);
     *
     */
    public List<ExpressionConstraint> getValueExpression() {
        if (valueExpression == null) {
            valueExpression = new ArrayList<ExpressionConstraint>();
        }
        return valueExpression;
    }

    public ValueComparison getValue() {
        return value;
    }

    public void setValue(ValueComparison value) {
        this.value = value;
    }

    public Range getValueRange() {
        return valueRange;
    }

    public void setValueRange(Range valueRange) {
        this.valueRange = valueRange;
    }

    public String getValueSet() {
        return valueSet;
    }

    public void setValueSet(String valueSet) {
        this.valueSet = valueSet;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public Boolean getHasValue() {
        return hasValue;
    }

    public void setHasValue(Boolean hasValue) {
        this.hasValue = hasValue;
    }

    public Boolean getIncludeNull() {
        return includeNull;
    }

    public void setIncludeNull(Boolean includeNull) {
        this.includeNull = includeNull;
    }

    public String getAssignedProperty() {
        return assignedProperty;
    }

    public void setAssignedProperty(String assignedProperty) {
        this.assignedProperty = assignedProperty;
    }

    public String getAssignedBooleanProperty() {
        return assignedBooleanProperty;
    }

    public void setAssignedBooleanProperty(String assignedBooleanProperty) {
        this.assignedBooleanProperty = assignedBooleanProperty;
    }
}
