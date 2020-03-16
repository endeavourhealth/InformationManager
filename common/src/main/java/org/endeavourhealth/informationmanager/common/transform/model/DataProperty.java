package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataProperty extends Concept {
    private SimpleProperty subDataPropertyOf;
    private ClassExpression propertyRange;
    private ClassExpression propertyDomain;
    private List<SimpleProperty> disjointWithProperty;
    private Boolean isFunctional;

    @JsonProperty("SubDataProperty")
    public SimpleProperty getSubDataPropertyOf() {
        return subDataPropertyOf;
    }

    public DataProperty setSubDataPropertyOf(SimpleProperty subDataPropertyOf) {
        this.subDataPropertyOf = subDataPropertyOf;
        return this;
    }

    @JsonProperty("PropertyRange")
    public ClassExpression getPropertyRange() {
        return propertyRange;
    }

    public DataProperty setPropertyRange(ClassExpression propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }

    @JsonProperty("PropertyDomain")
    public ClassExpression getPropertyDomain() {
        return propertyDomain;
    }

    public DataProperty setPropertyDomain(ClassExpression propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }

    @JsonProperty("DisjointWithProperty")
    public List<SimpleProperty> getDisjointWithProperty() {
        return disjointWithProperty;
    }

    public DataProperty setDisjointWithProperty(List<SimpleProperty> disjointWithProperty) {
        this.disjointWithProperty = disjointWithProperty;
        return this;
    }

    @JsonProperty("IsFunctional")
    public Boolean getFunctional() {
        return isFunctional;
    }

    public DataProperty setFunctional(Boolean functional) {
        isFunctional = functional;
        return this;
    }
}
