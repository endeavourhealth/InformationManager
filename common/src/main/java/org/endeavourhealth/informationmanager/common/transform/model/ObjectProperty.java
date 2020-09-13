package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;


public class ObjectProperty extends Concept {
    private List<PropertyAxiom> subObjectPropertyOf;
    private PropertyAxiom inversePropertyOf;
    private List<ClassAxiom> propertyRange;
    private List<ClassAxiom> propertyDomain;
    private List<SubPropertyChain> subPropertyChain;
    private Axiom isFunctional;
    private Axiom isSymmetric;
    private Axiom isTransitive;
    private Axiom isReflexive;

    @JsonProperty("IsFunctional")
    public Axiom getIsFunctional() {
        return isFunctional;
    }

    public void setIsFunctional(Axiom isFunctional) {
        this.isFunctional = isFunctional;
    }

    @JsonProperty("IsSymmetric")
    public Axiom getIsSymmetric() {
        return isSymmetric;
    }

    public void setIsSymmetric(Axiom isSymmetric) {
        this.isSymmetric = isSymmetric;
    }
    @JsonProperty("IsTransitive")
    public Axiom getIsTransitive() {
        return isTransitive;
    }

    public void setIsTransitive(Axiom isTransitive) {
        this.isTransitive = isTransitive;
    }

    @JsonProperty("IsReflexive")
    public Axiom getIsReflexive() {
        return isReflexive;
    }

    public void setIsReflexive(Axiom isReflexive) {
        this.isReflexive = isReflexive;
    }




    @JsonProperty("SubObjectPropertyOf")
    public List<PropertyAxiom> getSubObjectPropertyOf() {
        return subObjectPropertyOf;
    }

    public ObjectProperty setSubObjectPropertyOf(List<PropertyAxiom> subObjectPropertyOf) {
        this.subObjectPropertyOf = subObjectPropertyOf;
        return this;
    }

    public ObjectProperty addSubObjectPropertyOf(PropertyAxiom subObjectPropertyOf) {
        if (this.subObjectPropertyOf == null)
            this.subObjectPropertyOf = new ArrayList<>();
        this.subObjectPropertyOf.add(subObjectPropertyOf);

        return this;
    }

    @JsonProperty("InversePropertyOf")
    public PropertyAxiom getInversePropertyOf() {
        return inversePropertyOf;
    }

    public ObjectProperty setInversePropertyOf(PropertyAxiom inversePropertyOf) {
        this.inversePropertyOf = inversePropertyOf;
        return this;
    }

    @JsonProperty("PropertyRange")
    public List<ClassAxiom> getPropertyRange() {
        return propertyRange;
    }

    public ObjectProperty setPropertyRange(List<ClassAxiom> propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }
    public ObjectProperty addPropertyRange(ClassAxiom range) {
        if (this.propertyRange == null)
            this.propertyRange = new ArrayList<>();
        this.propertyRange.add(range);

        return this;
    }

    @JsonProperty("PropertyDomain")
    public List<ClassAxiom> getPropertyDomain() {
        return propertyDomain;
    }

    public ObjectProperty setPropertyDomain(List<ClassAxiom> propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }
    public ObjectProperty addPropertyDomain(ClassAxiom domain) {
        if (this.propertyDomain == null)
            this.propertyDomain = new ArrayList<>();
        this.propertyDomain.add(domain);

        return this;
    }



    @JsonProperty("SubPropertyChain")
    public List<SubPropertyChain> getSubPropertyChain() {
        return subPropertyChain;
    }

    public ObjectProperty setSubPropertyChain(List<SubPropertyChain> subPropertyChain) {
        this.subPropertyChain = subPropertyChain;
        return this;
    }

    public ObjectProperty addSubPropertyChain(SubPropertyChain subPropertyChain) {
        if (this.subPropertyChain == null)
            this.subPropertyChain = new ArrayList<>();

        this.subPropertyChain.add(subPropertyChain);
        return this;
    }



}
