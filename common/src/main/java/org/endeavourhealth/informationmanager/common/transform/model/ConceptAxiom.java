package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConceptAxiom {
    private String concept;
    private ClassExpression subClassOf;
    private ClassExpression equivalentTo;
    private SubProperty subPropertyOf;
    private SimpleConcept inversePropertyOf;
    private ClassExpressionConstraint propertyRange;
    private PropertyDomain propertyDomain;
    private SimpleExpressionList disjointWith;
    private Boolean isTransitive;
    private Boolean isFunctional;

    @JsonProperty("Concept")
    public String getConcept() {
        return concept;
    }

    public ConceptAxiom setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    @JsonProperty("SubClassOf")
    public ClassExpression getSubClassOf() {
        return subClassOf;
    }

    public ConceptAxiom setSubClassOf(ClassExpression subClassOf) {
        this.subClassOf = subClassOf;
        return this;
    }


    @JsonProperty("EquivalentTo")
    public ClassExpression getEquivalentTo() {
        return equivalentTo;
    }

    public ConceptAxiom setEquivalentTo(ClassExpression equivalentTo) {
        this.equivalentTo = equivalentTo;
        return this;
    }

    @JsonProperty("SubPropertyOf")
    public SubProperty getSubPropertyOf() {
        return subPropertyOf;
    }

    public ConceptAxiom setSubPropertyOf(SubProperty subPropertyOf) {
        this.subPropertyOf = subPropertyOf;
        return this;
    }

    @JsonProperty("InversePropertyOf")
    public SimpleConcept getInversePropertyOf() {
        return inversePropertyOf;
    }

    public ConceptAxiom setInversePropertyOf(SimpleConcept inversePropertyOf) {
        this.inversePropertyOf = inversePropertyOf;
        return this;
    }

    @JsonProperty("PropertyRange")
    public ClassExpressionConstraint getPropertyRange() {
        return propertyRange;
    }

    public ConceptAxiom setPropertyRange(ClassExpressionConstraint propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }

    @JsonProperty("PropertyDomain")
    public PropertyDomain getPropertyDomain() {
        return propertyDomain;
    }

    public ConceptAxiom setPropertyDomain(PropertyDomain propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }

    @JsonProperty("DisjointWith")
    public SimpleExpressionList getDisjointWith() {
        return disjointWith;
    }

    public ConceptAxiom setDisjointWith(SimpleExpressionList disjointWith) {
        this.disjointWith = disjointWith;
        return this;
    }

    @JsonProperty("IsTransitive")
    public Boolean getTransitive() {
        return isTransitive;
    }

    public ConceptAxiom setTransitive(Boolean transitive) {
        isTransitive = transitive;
        return this;
    }

    public Boolean getFunctional() {
        return isFunctional;
    }

    public ConceptAxiom setFunctional(Boolean functional) {
        isFunctional = functional;
        return this;
    }
}
