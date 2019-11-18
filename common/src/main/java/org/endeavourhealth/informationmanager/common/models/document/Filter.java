package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private List<ComplexAttribute> attribute = new ArrayList<>();

    public List<ComplexAttribute> getAttribute() {
        return attribute;
    }

    public Filter setAttribute(List<ComplexAttribute> attribute) {
        this.attribute = attribute;
        return this;
    }
}
