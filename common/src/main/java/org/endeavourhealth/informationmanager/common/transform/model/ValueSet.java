package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ValueSet {
    private String concept;
    private List<ClassExpression> expression;
    private List<ValueSet> valueSet;

    @JsonProperty("Concept")
    public String getConcept() {
        return concept;
    }

    public ValueSet setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    @JsonProperty("Expression")
    public List<ClassExpression> getExpression() {
        return expression;
    }

    public ValueSet setExpression(List<ClassExpression> expression) {
        this.expression = expression;
        return this;
    }

    @JsonProperty("ValueSet")
    public List<ValueSet> getValueSet() {
        return valueSet;
    }

    public ValueSet setValueSet(List<ValueSet> valueSet) {
        this.valueSet = valueSet;
        return this;
    }
}
