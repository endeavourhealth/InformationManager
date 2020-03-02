package org.endeavourhealth.informationmanager.common.models.definitionTypes;

import org.endeavourhealth.informationmanager.common.models.DBEntity;

public class PropertyRange extends DBEntity<PropertyRange> {
    private String range;
    private String subsumption;

    private Integer concept;
    private Integer axiom;
    private Integer operator;

    public String getRange() {
        return range;
    }

    public PropertyRange setRange(String range) {
        this.range = range;
        return this;
    }

    public String getSubsumption() {
        return subsumption;
    }

    public PropertyRange setSubsumption(String subsumption) {
        this.subsumption = subsumption;
        return this;
    }

    public Integer getConcept() {
        return concept;
    }

    public PropertyRange setConcept(Integer concept) {
        this.concept = concept;
        return this;
    }

    public Integer getAxiom() {
        return axiom;
    }

    public PropertyRange setAxiom(Integer axiom) {
        this.axiom = axiom;
        return this;
    }

    public Integer getOperator() {
        return operator;
    }

    public PropertyRange setOperator(Integer operator) {
        this.operator = operator;
        return this;
    }
}
