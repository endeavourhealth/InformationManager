package org.endeavourhealth.informationmanager.common.models;

public class ConceptPropertyData {
    private String concept;
    private String value;

    public String getConcept() {
        return concept;
    }

    public ConceptPropertyData setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ConceptPropertyData setValue(String value) {
        this.value = value;
        return this;
    }
}
