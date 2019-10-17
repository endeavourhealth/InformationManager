package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Function {

    private String name;
    private String description;
    private String baseFunction;
    private String functionURI;

    @JsonProperty(required = true)
    private List<Parameter> parameter;

    /**
     * Gets the value of the parameter list.
     *
     * To add a new item, do as follows:
     * getParameter().add(newItem);
     *
     */
    public List<Parameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<Parameter>();
        }
        return this.parameter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseFunction() {
        return baseFunction;
    }

    public void setBaseFunction(String baseFunction) {
        this.baseFunction = baseFunction;
    }

    public String getFunctionURI() {
        return functionURI;
    }

    public void setFunctionURI(String functionURI) {
        this.functionURI = functionURI;
    }

}
