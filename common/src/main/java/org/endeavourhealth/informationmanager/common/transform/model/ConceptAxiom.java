package org.endeavourhealth.informationmanager.common.transform.model;

import java.util.ArrayList;
import java.util.List;

public class ConceptAxiom {
    private String concept;
    private ClassExpression subClassOf;
    private ClassExpression equivalentTo;
    private SubProperty subPropertyOf;
    private List<String> inversePropertyOf;
    private ClassExpressionConstraint propertyRange;
    private PropertyDomain propertyDomain;
    private List<String> disjointWith;
    private Boolean isTransitive;

    public String getConcept() {
        return concept;
    }

    public ConceptAxiom setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public ClassExpression getSubClassOf() {
        return subClassOf;
    }

    public ConceptAxiom setSubClassOf(ClassExpression subClassOf) {
        this.subClassOf = subClassOf;
        return this;
    }

    public ClassExpression getEquivalentTo() {
        return equivalentTo;
    }

    public ConceptAxiom setEquivalentTo(ClassExpression equivalentTo) {
        this.equivalentTo = equivalentTo;
        return this;
    }

    public SubProperty getSubPropertyOf() {
        return subPropertyOf;
    }

    public ConceptAxiom setSubPropertyOf(SubProperty subPropertyOf) {
        this.subPropertyOf = subPropertyOf;
        return this;
    }

    public List<String> getInversePropertyOf() {
        return inversePropertyOf;
    }

    public ConceptAxiom setInversePropertyOf(List<String> inversePropertyOf) {
        this.inversePropertyOf = inversePropertyOf;
        return this;
    }

    public ConceptAxiom addInversePropertyOf(String inverseProperty) {
        if (this.inversePropertyOf == null)
            this.inversePropertyOf = new ArrayList<>();

        this.inversePropertyOf.add(inverseProperty);
        return this;
    }

    public ClassExpressionConstraint getPropertyRange() {
        return propertyRange;
    }

    public ConceptAxiom setPropertyRange(ClassExpressionConstraint propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }

    public PropertyDomain getPropertyDomain() {
        return propertyDomain;
    }

    public ConceptAxiom setPropertyDomain(PropertyDomain propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }

    public List<String> getDisjointWith() {
        return disjointWith;
    }

    public ConceptAxiom setDisjointWith(List<String> disjointWith) {
        this.disjointWith = disjointWith;
        return this;
    }

    public ConceptAxiom addDisjointWith(String disjointWith) {
        if (this.disjointWith == null)
            this.disjointWith = new ArrayList<>();

        this.disjointWith.add(disjointWith);
        return this;
    }

    public ConceptAxiom addAllDisjointWith(List<String> disjointWith) {
        if (this.disjointWith == null)
            this.disjointWith = new ArrayList<>();

        this.disjointWith.addAll(disjointWith);
        return this;
    }

    public Boolean getTransitive() {
        return isTransitive;
    }

    public ConceptAxiom setTransitive(Boolean transitive) {
        isTransitive = transitive;
        return this;
    }
}
