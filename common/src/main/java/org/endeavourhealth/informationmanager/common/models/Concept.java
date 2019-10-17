package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Concept {
    private String model;
    private ConceptSummary concept;
    private ConceptDefinition definition;
    private PropertyDomain propertyDomain;
    private JsonNode propertyRange;

    public String getModel() {
        return model;
    }

    public Concept setModel(String model) {
        this.model = model;
        return this;
    }

    public ConceptSummary getConcept() {
        return concept;
    }

    public Concept setConcept(ConceptSummary concept) {
        this.concept = concept;
        return this;
    }

    public ConceptDefinition getDefinition() {
        return definition;
    }

    public Concept setDefinition(ConceptDefinition definition) {
        this.definition = definition;
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
