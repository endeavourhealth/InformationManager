package org.endeavourhealth.informationmodel.api.models;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConceptAndRelationships {
    private Concept concept;
    private ConceptRelationship[] related;

    public Concept getConcept() {
        return concept;
    }

    public ConceptAndRelationships setConcept(Concept concept) {
        this.concept = concept;
        return this;
    }

    public ConceptRelationship[] getRelated() {
        return related;
    }

    public ConceptAndRelationships setRelated(ConceptRelationship[] related) {
        this.related = related;
        return this;
    }
}
