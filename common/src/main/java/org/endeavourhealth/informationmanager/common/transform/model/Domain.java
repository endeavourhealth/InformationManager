package org.endeavourhealth.informationmanager.common.transform.model;

public class Domain {
    private String concept;
    private Integer inGroup;

    public String getConcept() {
        return concept;
    }

    public Domain setConcept(String concept) {
        this.concept = concept;
        return this;
    }
}
