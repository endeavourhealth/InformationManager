package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class RoleGroupConstraint {
    private List<AttributeConstraint> attributeConstraint = new ArrayList<>();

    public List<AttributeConstraint> getAttributeConstraint() {
        return attributeConstraint;
    }

    public RoleGroupConstraint setAttributeConstraint(List<AttributeConstraint> attributeConstraint) {
        this.attributeConstraint = attributeConstraint;
        return this;
    }
}
