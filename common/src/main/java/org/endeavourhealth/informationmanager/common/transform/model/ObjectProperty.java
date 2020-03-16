package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ObjectProperty extends Concept {
    private SubObjectProperty subObjectPropertyOf;
    private SimpleProperty inversePropertyOf;
    private ClassExpression propertyRange;
    private ClassExpression propertyDomain;
    private List<SimpleProperty> disjointWithProperty;
    private Boolean isTransitive;
    private Boolean isReflexive;
    private Boolean isFunctional;

    @JsonProperty("SubObjectPropertyOf")
    public SubObjectProperty getSubObjectPropertyOf() {
        return subObjectPropertyOf;
    }

    public ObjectProperty setSubObjectPropertyOf(SubObjectProperty subObjectPropertyOf) {
        this.subObjectPropertyOf = subObjectPropertyOf;
        return this;
    }

    @JsonProperty("InversePropertyOf")
    public SimpleProperty getInversePropertyOf() {
        return inversePropertyOf;
    }

    public ObjectProperty setInversePropertyOf(SimpleProperty inversePropertyOf) {
        this.inversePropertyOf = inversePropertyOf;
        return this;
    }

    @JsonProperty("PropertyRange")
    public ClassExpression getPropertyRange() {
        return propertyRange;
    }

    public ObjectProperty setPropertyRange(ClassExpression propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }

    @JsonProperty("PropertyDomain")
    public ClassExpression getPropertyDomain() {
        return propertyDomain;
    }

    public ObjectProperty setPropertyDomain(ClassExpression propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }

    @JsonProperty("DisjointWithProperty")
    public List<SimpleProperty> getDisjointWithProperty() {
        return disjointWithProperty;
    }

    public ObjectProperty setDisjointWithProperty(List<SimpleProperty> disjointWithProperty) {
        this.disjointWithProperty = disjointWithProperty;
        return this;
    }

    @JsonProperty("Transitive")
    public Boolean getTransitive() {
        return isTransitive;
    }

    public ObjectProperty setTransitive(Boolean transitive) {
        isTransitive = transitive;
        return this;
    }

    @JsonProperty("Reflexive")
    public Boolean getReflexive() {
        return isReflexive;
    }

    public ObjectProperty setReflexive(Boolean reflexive) {
        isReflexive = reflexive;
        return this;
    }

    @JsonProperty("Functional")
    public Boolean getFunctional() {
        return isFunctional;
    }

    public ObjectProperty setFunctional(Boolean functional) {
        isFunctional = functional;
        return this;
    }
}
