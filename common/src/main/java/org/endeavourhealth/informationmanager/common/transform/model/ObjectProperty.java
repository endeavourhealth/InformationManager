package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ObjectProperty extends Concept {
    private List<SimpleProperty> subObjectPropertyOf;
    private SimpleProperty inversePropertyOf;
    private ClassExpression propertyRange;
    private ClassExpression propertyDomain;
    private List<SimpleProperty> disjointWithProperty;
    private List<SubPropertyChain> subPropertyChain;
    private List<PropertyCharacteristic> characteristics;


    @JsonProperty("SubObjectPropertyOf")
    public List<SimpleProperty> getSubObjectPropertyOf() {
        return subObjectPropertyOf;
    }

    public ObjectProperty setSubObjectPropertyOf(List<SimpleProperty> subObjectPropertyOf) {
        this.subObjectPropertyOf = subObjectPropertyOf;
        return this;
    }

    public ObjectProperty addSubObjectPropertyOf(SimpleProperty subObjectPropertyOf) {
        if (this.subObjectPropertyOf == null)
            this.subObjectPropertyOf = new ArrayList<>();
        this.subObjectPropertyOf.add(subObjectPropertyOf);

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
    @JsonProperty("Characteristics")
    public List<PropertyCharacteristic> getCharacteristics() {
        return characteristics;
    }

    public ObjectProperty setCharacteristics(List<PropertyCharacteristic> characteristics) {
        this.characteristics = characteristics;
        return this;
    }

    public ObjectProperty addCharacteristoc(PropertyCharacteristic characteristic) {
        if (this.characteristics == null)
            this.characteristics = new ArrayList<>();
        this.characteristics.add(characteristic);

        return this;
    }




}
