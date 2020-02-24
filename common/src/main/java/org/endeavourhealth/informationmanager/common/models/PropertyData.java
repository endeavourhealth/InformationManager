package org.endeavourhealth.informationmanager.common.models;

public class PropertyData {

    private Integer concept;
    private Integer axiom;
    private Integer group;
    private Integer operator;
    private Integer valueOperator;
    private Integer property;
    private Integer minCardinality;
    private Integer maxCardinality;
    private String data;

    public Integer getConcept() {
        return concept;
    }

    public PropertyData setConcept(Integer concept) {
        this.concept = concept;
        return this;
    }

    public Integer getAxiom() {
        return axiom;
    }

    public PropertyData setAxiom(Integer axiom) {
        this.axiom = axiom;
        return this;
    }

    public Integer getGroup() {
        return group;
    }

    public PropertyData setGroup(Integer group) {
        this.group = group;
        return this;
    }

    public Integer getOperator() {
        return operator;
    }

    public PropertyData setOperator(Integer operator) {
        this.operator = operator;
        return this;
    }

    public Integer getValueOperator() {
        return valueOperator;
    }

    public PropertyData setValueOperator(Integer valueOperator) {
        this.valueOperator = valueOperator;
        return this;
    }

    public Integer getProperty() {
        return property;
    }

    public PropertyData setProperty(Integer property) {
        this.property = property;
        return this;
    }

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public PropertyData setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public PropertyData setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public String getData() {
        return data;
    }

    public PropertyData setData(String data) {
        this.data = data;
        return this;
    }

}
