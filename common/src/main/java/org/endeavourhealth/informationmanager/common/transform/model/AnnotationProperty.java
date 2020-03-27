package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AnnotationProperty extends Concept {
    private List<String> subAnnotationPropertyOf;
    private ClassExpression propertyRange;

    @JsonProperty("SubAnnotationPropertyOf")
    public List<String> getSubAnnotationPropertyOf() {
        return subAnnotationPropertyOf;
    }

    public AnnotationProperty setSubAnnotationPropertyOf(List<String> subAnnotationPropertyOf) {
        this.subAnnotationPropertyOf = subAnnotationPropertyOf;
        return this;
    }

    public AnnotationProperty addSubAnnotationPropertyOf(String subAnnotationProperty) {
        if (this.subAnnotationPropertyOf == null)
            this.subAnnotationPropertyOf = new ArrayList<>();

        this.subAnnotationPropertyOf.add(subAnnotationProperty);
        return this;
    }

    @JsonProperty("PropertyRange")
    public ClassExpression getPropertyRange() {
        return propertyRange;
    }

    public AnnotationProperty setPropertyRange(ClassExpression propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }
}
