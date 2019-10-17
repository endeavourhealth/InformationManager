package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
// (not needed) import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValueComparison {

    private Operator operator;
    private Object value;
    private Function valueFunction;
    private String variable;
    private String definedVariable;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Function getValueFunction() {
        return valueFunction;
    }

    public void setValueFunction(Function valueFunction) {
        this.valueFunction = valueFunction;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getDefinedVariable() {
        return definedVariable;
    }

    public void setDefinedVariable(String definedVariable) {
        this.definedVariable = definedVariable;
    }

}
