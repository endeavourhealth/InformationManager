package org.endeavourhealth.informationmanager.common.models;

import org.endeavourhealth.informationmanager.common.models.definitionTypes.ClassExpression;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.PropertyDomain;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.PropertyRange;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.SimpleConcept;

import java.util.ArrayList;
import java.util.List;

public class ConceptDefinition {
    private ClassExpression subClassOf;
    private ClassExpression equivalentTo;
    private List<SimpleConcept> subPropertyOf = new ArrayList<>();
    private List<SimpleConcept> inversePropertyOf = new ArrayList<>();
    private List<SimpleConcept> mappedTo = new ArrayList<>();
    private List<SimpleConcept> replacedBy = new ArrayList<>();
    private List<SimpleConcept> childOf = new ArrayList<>();
    private List<PropertyRange> propertyRange = new ArrayList<>();
    private List<PropertyDomain> propertyDomain = new ArrayList<>();
    private List<String> propertyChain = new ArrayList<>();

    public ClassExpression getSubClassOf() {
        return subClassOf;
    }

    public ConceptDefinition setSubClassOf(ClassExpression subClassOf) {
        this.subClassOf = subClassOf;
        return this;
    }

    public ClassExpression getEquivalentTo() {
        return equivalentTo;
    }

    public ConceptDefinition setEquivalentTo(ClassExpression equivalentTo) {
        this.equivalentTo = equivalentTo;
        return this;
    }

    public List<SimpleConcept> getSubPropertyOf() {
        return subPropertyOf;
    }

    public ConceptDefinition setSubPropertyOf(List<SimpleConcept> subPropertyOf) {
        this.subPropertyOf = subPropertyOf;
        return this;
    }

    public List<SimpleConcept> getInversePropertyOf() {
        return inversePropertyOf;
    }

    public ConceptDefinition setInversePropertyOf(List<SimpleConcept> inversePropertyOf) {
        this.inversePropertyOf = inversePropertyOf;
        return this;
    }

    public List<SimpleConcept> getMappedTo() {
        return mappedTo;
    }

    public ConceptDefinition setMappedTo(List<SimpleConcept> mappedTo) {
        this.mappedTo = mappedTo;
        return this;
    }

    public List<SimpleConcept> getReplacedBy() {
        return replacedBy;
    }

    public ConceptDefinition setReplacedBy(List<SimpleConcept> replacedBy) {
        this.replacedBy = replacedBy;
        return this;
    }

    public List<SimpleConcept> getChildOf() {
        return childOf;
    }

    public ConceptDefinition setChildOf(List<SimpleConcept> childOf) {
        this.childOf = childOf;
        return this;
    }

    public List<PropertyRange> getPropertyRange() {
        return propertyRange;
    }

    public ConceptDefinition setPropertyRange(List<PropertyRange> propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }

    public List<PropertyDomain> getPropertyDomain() {
        return propertyDomain;
    }

    public ConceptDefinition setPropertyDomain(List<PropertyDomain> propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }

    public List<String> getPropertyChain() {
        return propertyChain;
    }

    public ConceptDefinition setPropertyChain(List<String> propertyChain) {
        this.propertyChain = propertyChain;
        return this;
    }
}
