package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataProperty extends Concept {
    private SimpleProperty subDataPropertyOf;
    private ClassExpression propertyRange;
    private ClassExpression propertyDomain;
    private List<SimpleProperty> disjointWithProperty;
    private List<PropertyCharacteristic> characteristics;
    private List<String> isA;

    @JsonProperty("SubDataPropertyOf")
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




    @JsonProperty("Characteristics")
    public List<PropertyCharacteristic> getCharacteristics() {
        return characteristics;
    }

    public DataProperty setCharacteristics(List<PropertyCharacteristic> characteristics) {
        this.characteristics = characteristics;
        return this;
    }

    public DataProperty addCharacteristoc(PropertyCharacteristic characteristic) {
        if (this.characteristics == null)
            this.characteristics = new ArrayList<>();
        this.characteristics.add(characteristic);

        return this;
    }
}
