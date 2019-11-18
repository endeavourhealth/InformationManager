package org.endeavourhealth.informationmanager.common.models.document;

public class ConceptExpression {
    private String name;
    private Operator operator;
    private String concept;

    public String getName() {
        return name;
    }

    public ConceptExpression setName(String name) {
        this.name = name;
        return this;
    }

    public Operator getOperator() {
        return operator;
    }

    public ConceptExpression setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public String getConcept() {
        return concept;
    }

    public ConceptExpression setConcept(String concept) {
        this.concept = concept;
        return this;
    }
}
