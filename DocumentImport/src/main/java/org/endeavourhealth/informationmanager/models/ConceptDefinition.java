package org.endeavourhealth.informationmanager.models;

import java.util.ArrayList;
import java.util.List;

public class ConceptDefinition {
    private String definitionOf;
    private List<PropertyObject> propertyObject = new ArrayList<>();

    public String getDefinitionOf() {
        return definitionOf;
    }

    public ConceptDefinition setDefinitionOf(String definitionOf) {
        this.definitionOf = definitionOf;
        return this;
    }

    public List<PropertyObject> getPropertyObject() {
        return propertyObject;
    }

    public ConceptDefinition setPropertyObject(List<PropertyObject> propertyObject) {
        this.propertyObject = propertyObject;
        return this;
    }
}
