package org.endeavourhealth.informationmanager.common.models.definitionTypes;

public class ConceptDefinition extends Definition<ConceptDefinition> {
    private String concept;
    private boolean inferred;

    public String getConcept() {
        return concept;
    }

    public ConceptDefinition setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public boolean isInferred() {
        return inferred;
    }

    public ConceptDefinition setInferred(boolean inferred) {
        this.inferred = inferred;
        return this;
    }
}
