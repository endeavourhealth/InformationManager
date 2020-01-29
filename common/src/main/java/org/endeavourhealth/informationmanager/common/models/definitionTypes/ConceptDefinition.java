package org.endeavourhealth.informationmanager.common.models.definitionTypes;

public class ConceptDefinition extends Definition<ConceptDefinition> {
    private String concept;

    public String getConcept() {
        return concept;
    }

    public ConceptDefinition setConcept(String concept) {
        this.concept = concept;
        return this;
    }
}
