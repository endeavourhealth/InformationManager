package org.endeavourhealth.informationmanager.common.models.definitionTypes;

public class SimpleConcept {
    private String concept;
    private boolean inferred;

    public String getConcept() {
        return concept;
    }

    public SimpleConcept setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public boolean isInferred() {
        return inferred;
    }

    public SimpleConcept setInferred(boolean inferred) {
        this.inferred = inferred;
        return this;
    }
}
