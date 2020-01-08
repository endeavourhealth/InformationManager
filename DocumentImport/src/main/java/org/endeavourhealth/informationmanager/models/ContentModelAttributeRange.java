package org.endeavourhealth.informationmanager.models;

public class ContentModelAttributeRange {
    private String constraint;
    private String concept;

    public String getConstraint() {
        return constraint;
    }

    public ContentModelAttributeRange setConstraint(String constraint) {
        this.constraint = constraint;
        return this;
    }

    public String getConcept() {
        return concept;
    }

    public ContentModelAttributeRange setConcept(String concept) {
        this.concept = concept;
        return this;
    }
}
