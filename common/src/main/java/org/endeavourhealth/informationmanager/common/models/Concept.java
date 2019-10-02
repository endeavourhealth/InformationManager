package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Concept {
    private String model;
    private JsonNode concept;
    private List<ConceptDefinition> definition = new ArrayList<>();
    private PropertyDomain propertyDomain;
    private JsonNode propertyRange;

    public String getModel() {
        return model;
    }

    public Concept setModel(String model) {
        this.model = model;
        return this;
    }

    public JsonNode getConcept() {
        return concept;
    }

    public Concept setConcept(JsonNode concept) {
        this.concept = concept;
        return this;
    }

    public List<ConceptDefinition> getDefinition() {
        return definition;
    }

    public Concept setDefinition(List<ConceptDefinition> definition) {
        this.definition = definition;
        return this;
    }

    public Concept addDefinition(ConceptDefinition definition) {
        this.definition.add(definition);
        return this;
    }

    public PropertyDomain getPropertyDomain() {
        return propertyDomain;
    }

    public Concept setPropertyDomain(PropertyDomain propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }

    public JsonNode getPropertyRange() {
        return propertyRange;
    }

    public Concept setPropertyRange(JsonNode propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }
}
