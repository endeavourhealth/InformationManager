package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.databind.JsonNode;

public class ConceptAxiom {
    private int typeId;
    private String type;
    private JsonNode definition;

    public ConceptAxiom(int typeId, String type, JsonNode definition) {
        this.typeId = typeId;
        this.type = type;
        this.definition = definition;
    }

    public int getTypeId() {
        return typeId;
    }

    public ConceptAxiom setTypeId(int typeId) {
        this.typeId = typeId;
        return this;
    }

    public String getType() {
        return type;
    }

    public ConceptAxiom setType(String type) {
        this.type = type;
        return this;
    }

    public JsonNode getDefinition() {
        return definition;
    }

    public ConceptAxiom setDefinition(JsonNode definition) {
        this.definition = definition;
        return this;
    }
}
