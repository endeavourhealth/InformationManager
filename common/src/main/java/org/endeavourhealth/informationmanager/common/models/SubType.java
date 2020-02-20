package org.endeavourhealth.informationmanager.common.models;

public class SubType {
    private int concept;
    private int axiom;
    private int superType;
    private int inferred;
    private int operator;

    public int getConcept() {
        return concept;
    }

    public SubType setConcept(int concept) {
        this.concept = concept;
        return this;
    }

    public int getAxiom() {
        return axiom;
    }

    public SubType setAxiom(int axiom) {
        this.axiom = axiom;
        return this;
    }

    public int getSuperType() {
        return superType;
    }

    public SubType setSuperType(int superType) {
        this.superType = superType;
        return this;
    }

    public int getInferred() {
        return inferred;
    }

    public SubType setInferred(int inferred) {
        this.inferred = inferred;
        return this;
    }

    public int getOperator() {
        return operator;
    }

    public SubType setOperator(int operator) {
        this.operator = operator;
        return this;
    }

}
