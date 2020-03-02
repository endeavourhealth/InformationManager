package org.endeavourhealth.informationmanager.common.models;

public class SubType {
    private Integer concept;
    private Integer axiom;
    private Integer superType;
    private Integer inferred;
    private Integer operator;

    public Integer getConcept() {
        return concept;
    }

    public SubType setConcept(Integer concept) {
        this.concept = concept;
        return this;
    }

    public Integer getAxiom() {
        return axiom;
    }

    public SubType setAxiom(Integer axiom) {
        this.axiom = axiom;
        return this;
    }

    public Integer getSuperType() {
        return superType;
    }

    public SubType setSuperType(Integer superType) {
        this.superType = superType;
        return this;
    }

    public Integer getInferred() {
        return inferred;
    }

    public SubType setInferred(Integer inferred) {
        this.inferred = inferred;
        return this;
    }

    public Integer getOperator() {
        return operator;
    }

    public SubType setOperator(Integer operator) {
        this.operator = operator;
        return this;
    }

}
