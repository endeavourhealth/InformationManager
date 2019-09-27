package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Concept {
    private JsonNode concept;
    private JsonNode definition;
    private PropertyDomain propertyDomain;
    private JsonNode propertyRange;

    public JsonNode getConcept() {
        return concept;
    }

    public Concept setConcept(JsonNode concept) {
        this.concept = concept;
        return this;
    }

    public JsonNode getDefinition() {
        return definition;
    }

    public Concept setDefinition(JsonNode definition) {
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
