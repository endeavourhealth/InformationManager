package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
// (not needed) import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Parameter {

    private String property;
    private String definedProperty;
    private String parameterToken;
    private String variable;
    private Object valueDateTime;
    private Float valueNumeric;
    private Function function;
    private String parameterName;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDefinedProperty() {
        return definedProperty;
    }

    public void setDefinedProperty(String definedProperty) {
        this.definedProperty = definedProperty;
    }

    public String getParameterToken() {
        return parameterToken;
    }

    public void setParameterToken(String parameterToken) {
        this.parameterToken = parameterToken;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Object getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(Object valueDateTime) {
        this.valueDateTime = valueDateTime;
    }

    public Float getValueNumeric() {
        return valueNumeric;
    }

    public void setValueNumeric(Float valueNumeric) {
        this.valueNumeric = valueNumeric;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

}
