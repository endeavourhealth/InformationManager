package org.endeavourhealth.informationmanager.common.models;

public class InverseProperty {

    private Integer concept;
    private Integer axiom;
    private Integer inverse;

    public Integer getConcept() {
        return concept;
    }

    public InverseProperty setConcept(Integer concept) {
        this.concept = concept;
        return this;
    }

    public Integer getAxiom() {
        return axiom;
    }

    public InverseProperty setAxiom(Integer axiom) {
        this.axiom = axiom;
        return this;
    }

    public Integer getInverse() {
        return inverse;
    }

    public InverseProperty setInverse(Integer inverse) {
        this.inverse = inverse;
        return this;
    }

}
