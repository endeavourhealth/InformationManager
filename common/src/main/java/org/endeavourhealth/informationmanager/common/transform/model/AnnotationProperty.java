package org.endeavourhealth.informationmanager.common.transform.model;

import java.util.ArrayList;
import java.util.List;

public class AnnotationProperty extends Concept {
    private List<String> subAnnotaionPropertyOf;
    private ClassExpression propertyRange;

    public List<String> getSubAnnotaionPropertyOf() {
        return subAnnotaionPropertyOf;
    }

    public AnnotationProperty setSubAnnotaionPropertyOf(List<String> subAnnotaionPropertyOf) {
        this.subAnnotaionPropertyOf = subAnnotaionPropertyOf;
        return this;
    }

    public AnnotationProperty addSubAnnotationPropertyOf(String subAnnotationProperty) {
        if (this.subAnnotaionPropertyOf == null)
            this.subAnnotaionPropertyOf = new ArrayList<>();

        this.subAnnotaionPropertyOf.add(subAnnotationProperty);
        return this;
    }

    public ClassExpression getPropertyRange() {
        return propertyRange;
    }

    public AnnotationProperty setPropertyRange(ClassExpression propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }
}
