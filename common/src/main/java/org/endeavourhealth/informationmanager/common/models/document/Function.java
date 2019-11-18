package org.endeavourhealth.informationmanager.common.models.document;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Function {
    private String name;
    private String description;
    private String baseFunction;
    private URI functionUri;
    private List<Parameter> parameter = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Function setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Function setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getBaseFunction() {
        return baseFunction;
    }

    public Function setBaseFunction(String baseFunction) {
        this.baseFunction = baseFunction;
        return this;
    }

    public URI getFunctionUri() {
        return functionUri;
    }

    public Function setFunctionUri(URI functionUri) {
        this.functionUri = functionUri;
        return this;
    }

    public List<Parameter> getParameter() {
        return parameter;
    }

    public Function setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
        return this;
    }
}
