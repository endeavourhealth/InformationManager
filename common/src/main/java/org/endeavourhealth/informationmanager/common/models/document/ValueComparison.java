package org.endeavourhealth.informationmanager.common.models.document;

public class ValueComparison {
    private Operator operator;
    private String value;
    private Function valueFunction;
    private String variable;
    private String definedVariable;

    public Operator getOperator() {
        return operator;
    }

    public ValueComparison setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ValueComparison setValue(String value) {
        this.value = value;
        return this;
    }

    public Function getValueFunction() {
        return valueFunction;
    }

    public ValueComparison setValueFunction(Function valueFunction) {
        this.valueFunction = valueFunction;
        return this;
    }

    public String getVariable() {
        return variable;
    }

    public ValueComparison setVariable(String variable) {
        this.variable = variable;
        return this;
    }

    public String getDefinedVariable() {
        return definedVariable;
    }

    public ValueComparison setDefinedVariable(String definedVariable) {
        this.definedVariable = definedVariable;
        return this;
    }
}
