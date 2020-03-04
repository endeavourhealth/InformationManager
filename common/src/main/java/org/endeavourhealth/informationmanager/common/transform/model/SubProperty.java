package org.endeavourhealth.informationmanager.common.transform.model;

import java.util.ArrayList;
import java.util.List;

public class SubProperty {
    private String concept;
    private List<String> propertyChain = new ArrayList<>();

    public String getConcept() {
        return concept;
    }

    public SubProperty setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public List<String> getPropertyChain() {
        return propertyChain;
    }

    public SubProperty setPropertyChain(List<String> propertyChain) {
        this.propertyChain = propertyChain;
        return this;
    }
}
