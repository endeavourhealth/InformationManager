package org.endeavourhealth.informationmanager.common.models;

import org.endeavourhealth.informationmanager.common.models.document.*;

import java.util.ArrayList;
import java.util.List;

public class FullConcept {
    private String model;
    private Concept concept;
    private ConceptDefinition definition;
    private PropertyDomain propertyDomain;
    private List<PropertyRange> propertyRange = new ArrayList<>();

    public String getModel() {
        return model;
    }

    public FullConcept setModel(String model) {
        this.model = model;
        return this;
    }

    public Concept getConcept() {
        return concept;
    }

    public FullConcept setConcept(Concept concept) {
        this.concept = concept;
        return this;
    }

    public ConceptDefinition getDefinition() {
        return definition;
    }

    public FullConcept setDefinition(ConceptDefinition definition) {
        this.definition = definition;
        return this;
    }

    public PropertyDomain getPropertyDomain() {
        return propertyDomain;
    }

    public FullConcept setPropertyDomain(PropertyDomain propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }

    public List<PropertyRange> getPropertyRange() {
        return propertyRange;
    }

    public FullConcept setPropertyRange(List<PropertyRange> propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }
}
