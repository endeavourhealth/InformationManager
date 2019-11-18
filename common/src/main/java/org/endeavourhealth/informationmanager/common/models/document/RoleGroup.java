package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class RoleGroup {
    private List<AttributeExpression> attribute = new ArrayList<>();

    public List<AttributeExpression> getAttribute() {
        return attribute;
    }

    public RoleGroup setAttribute(List<AttributeExpression> attribute) {
        this.attribute = attribute;
        return this;
    }
}
