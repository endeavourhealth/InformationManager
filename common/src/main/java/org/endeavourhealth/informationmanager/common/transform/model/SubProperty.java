package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SubProperty {
    private List<String> concept;
    private List<String> propertyChain;

    @JsonProperty("Concept")
    public List<String> getConcept() {
        return concept;
    }

    public SubProperty setConcept(List<String> concept) {
        this.concept = concept;
        return this;
    }

    public SubProperty addConcept(String concept) {
        if (this.concept == null)
            this.concept = new ArrayList<>();

        this.concept.add(concept);
        return this;
    }

    public List<String> getPropertyChain() {
        return propertyChain;
    }

    public SubProperty setPropertyChain(List<String> propertyChain) {
        this.propertyChain = propertyChain;
        return this;
    }

    public SubProperty addPropertyChain(String propertyChain) {
        if (this.propertyChain == null)
            this.propertyChain = new ArrayList<>();

        this.propertyChain.add(propertyChain);
        return this;
    }
}
