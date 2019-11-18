package org.endeavourhealth.informationmanager.common.models.document;

import java.util.Date;

public class Parameter {
    private String property;
    private String definedToken;
    private String parameterToken;
    private String variable;
    private Date valueDateTime;
    private Double valueNumeric;
    private Function function;
    private String parameterName;

    public String getProperty() {
        return property;
    }

    public Parameter setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getDefinedToken() {
        return definedToken;
    }

    public Parameter setDefinedToken(String definedToken) {
        this.definedToken = definedToken;
        return this;
    }

    public String getParameterToken() {
        return parameterToken;
    }

    public Parameter setParameterToken(String parameterToken) {
        this.parameterToken = parameterToken;
        return this;
    }

    public String getVariable() {
        return variable;
    }

    public Parameter setVariable(String variable) {
        this.variable = variable;
        return this;
    }

    public Date getValueDateTime() {
        return valueDateTime;
    }

    public Parameter setValueDateTime(Date valueDateTime) {
        this.valueDateTime = valueDateTime;
        return this;
    }

    public Double getValueNumeric() {
        return valueNumeric;
    }

    public Parameter setValueNumeric(Double valueNumeric) {
        this.valueNumeric = valueNumeric;
        return this;
    }

    public Function getFunction() {
        return function;
    }

    public Parameter setFunction(Function function) {
        this.function = function;
        return this;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Parameter setParameterName(String parameterName) {
        this.parameterName = parameterName;
        return this;
    }
}
