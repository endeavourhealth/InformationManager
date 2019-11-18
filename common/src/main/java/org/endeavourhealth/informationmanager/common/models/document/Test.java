package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private List<ComplexAttribute> attribute = new ArrayList<>();

    public List<ComplexAttribute> getAttribute() {
        return attribute;
    }

    public Test setAttribute(List<ComplexAttribute> attribute) {
        this.attribute = attribute;
        return this;
    }
}
