package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SimpleExpressionList {
    private List<String> concept;

    @JsonProperty("Concept")
    public List<String> getConcept() {
        return concept;
    }

    public SimpleExpressionList setConcept(List<String> concept) {
        this.concept = concept;
        return this;
    }

    public SimpleExpressionList addConcept(String concept) {
        if (this.concept == null)
            this.concept = new ArrayList<>();

        this.concept.add(concept);
        return this;
    }

    public SimpleExpressionList addAllConcepts(List<String> concepts) {
        if (this.concept == null)
            this.concept = new ArrayList<>();

        this.concept.addAll(concepts);
        return this;
    }
}
