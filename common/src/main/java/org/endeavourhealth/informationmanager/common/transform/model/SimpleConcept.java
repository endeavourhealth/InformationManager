package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleConcept {
    private String concept;

    @JsonProperty("Concept")
    public String getConcept() {
        return concept;
    }

    public SimpleConcept setConcept(String concept) {
        this.concept = concept;
        return this;
    }
}
