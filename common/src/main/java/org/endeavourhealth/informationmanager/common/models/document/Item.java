package org.endeavourhealth.informationmanager.common.models.document;

public class Item {
    private String name;
    private String property;
    private String inferredProperty;
    private String functionProperty;

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public Item setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getInferredProperty() {
        return inferredProperty;
    }

    public Item setInferredProperty(String inferredProperty) {
        this.inferredProperty = inferredProperty;
        return this;
    }

    public String getFunctionProperty() {
        return functionProperty;
    }

    public Item setFunctionProperty(String functionProperty) {
        this.functionProperty = functionProperty;
        return this;
    }
}
