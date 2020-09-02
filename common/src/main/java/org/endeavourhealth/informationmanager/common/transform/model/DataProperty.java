package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataProperty extends Concept {
    private List<PropertyAxiom> subDataPropertyOf;
    private List<PropertyRangeAxiom> propertyRange;
    private List<ClassAxiom> propertyDomain;
    private List<PropertyAxiom> disjointWithProperty;
    private Axiom isFunctional;

    @JsonProperty("IsFunctional")
    public Axiom getIsFunctional() {
        return isFunctional;
    }

    public void setIsFunctional(Axiom isFunctional) {
        this.isFunctional = isFunctional;
    }



    @JsonProperty("SubDataPropertyOf")
    public List<PropertyAxiom> getSubDataPropertyOf() {
        return subDataPropertyOf;
    }

    public DataProperty setSubDataPropertyOf(List<PropertyAxiom> subDataPropertyOf) {
        this.subDataPropertyOf = subDataPropertyOf;
        return this;
    }
    public DataProperty addSubDataPropertyOf(PropertyAxiom prop) {
        if (this.subDataPropertyOf == null)
            this.subDataPropertyOf = new ArrayList<>();
        this.subDataPropertyOf.add(prop);

        return this;
    }


    @JsonProperty("PropertyRange")
    public List<PropertyRangeAxiom> getPropertyRange() {
        return propertyRange;
    }

    public DataProperty setPropertyRange(List<PropertyRangeAxiom> propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }
    public DataProperty addPropertyRange(PropertyRangeAxiom range) {
        if (this.propertyRange == null)
            this.propertyRange = new ArrayList<>();
        this.propertyRange.add(range);
        return this;
    }

    @JsonProperty("PropertyDomain")
    public List<ClassAxiom> getPropertyDomain() {
        return propertyDomain;
    }

    public DataProperty setPropertyDomain(List<ClassAxiom> propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }
    public DataProperty addPropertyDomain(ClassAxiom domain) {
        if (this.propertyDomain == null)
            this.propertyDomain = new ArrayList<>();
        this.propertyDomain.add(domain);

        return this;
    }

    @JsonProperty("DisjointWithProperty")
    public List<PropertyAxiom> getDisjointWithProperty() {
        return disjointWithProperty;
    }

    public DataProperty setDisjointWithProperty(List<PropertyAxiom> disjointWithProperty) {
        this.disjointWithProperty = disjointWithProperty;
        return this;
    }


}
