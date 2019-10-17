package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConceptDefinition {
    private String status;
    private List<ConceptExpression> subtypeOf = new ArrayList<>();
    private List<ConceptExpression> equivalentTo = new ArrayList<>();
    private List<ConceptExpression> disjointWith = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public ConceptDefinition setStatus(String status) {
        this.status = status;
        return this;
    }

    public List<ConceptExpression> getSubtypeOf() {
        return subtypeOf;
    }

    public ConceptDefinition setSubtypeOf(List<ConceptExpression> subtypeOf) {
        this.subtypeOf = subtypeOf;
        return this;
    }

    public List<ConceptExpression> getEquivalentTo() {
        return equivalentTo;
    }

    public ConceptDefinition setEquivalentTo(List<ConceptExpression> equivalentTo) {
        this.equivalentTo = equivalentTo;
        return this;
    }

    public List<ConceptExpression> getDisjointWith() {
        return disjointWith;
    }

    public ConceptDefinition setDisjointWith(List<ConceptExpression> disjointWith) {
        this.disjointWith = disjointWith;
        return this;
    }
}
