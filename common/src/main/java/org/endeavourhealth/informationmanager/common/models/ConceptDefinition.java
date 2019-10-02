package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.databind.JsonNode;

public class ConceptDefinition {
    private String type;
    private JsonNode expression;

    public String getType() {
        return type;
    }

    public ConceptDefinition setType(String type) {
        this.type = type;
        return this;
    }

    public JsonNode getExpression() {
        return expression;
    }

    public ConceptDefinition setExpression(JsonNode expression) {
        this.expression = expression;
        return this;
    }
}
