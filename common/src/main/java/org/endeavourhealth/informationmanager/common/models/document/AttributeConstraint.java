package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class AttributeConstraint {
    private List<String> property = new ArrayList<>();
    private List<String> propertyOrSubtypes = new ArrayList<>();
    private ExpressionConstraint value;

    public List<String> getProperty() {
        return property;
    }

    public AttributeConstraint setProperty(List<String> property) {
        this.property = property;
        return this;
    }

    public List<String> getPropertyOrSubtypes() {
        return propertyOrSubtypes;
    }

    public AttributeConstraint setPropertyOrSubtypes(List<String> propertyOrSubtypes) {
        this.propertyOrSubtypes = propertyOrSubtypes;
        return this;
    }

    public ExpressionConstraint getValue() {
        return value;
    }

    public AttributeConstraint setValue(ExpressionConstraint value) {
        this.value = value;
        return this;
    }
}
