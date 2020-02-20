package org.endeavourhealth.informationmanager.common.models;

public class PropertyClass {

    private Integer concept;
    private Integer axiom;
    private Integer group;
    private Integer operator;
    private Integer valueOperator;
    private Integer property;
    private Integer minCardinality;
    private Integer maxCardinality;
    private Integer object;

    public Integer getConcept() {
        return concept;
    }

    public PropertyClass setConcept(Integer concept) {
        this.concept = concept;
        return this;
    }

    public Integer getAxiom() {
        return axiom;
    }

    public PropertyClass setAxiom(Integer axiom) {
        this.axiom = axiom;
        return this;
    }

    public Integer getGroup() {
        return group;
    }

    public PropertyClass setGroup(Integer group) {
        this.group = group;
        return this;
    }

    public Integer getOperator() {
        return operator;
    }

    public PropertyClass setOperator(Integer operator) {
        this.operator = operator;
        return this;
    }

    public Integer getValueOperator() {
        return valueOperator;
    }

    public PropertyClass setValueOperator(Integer valueOperator) {
        this.valueOperator = valueOperator;
        return this;
    }

    public Integer getProperty() {
        return property;
    }

    public PropertyClass setProperty(Integer property) {
        this.property = property;
        return this;
    }

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public PropertyClass setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public PropertyClass setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public Integer getObject() {
        return object;
    }

    public PropertyClass setObject(Integer object) {
        this.object = object;
        return this;
    }

}
