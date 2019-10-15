package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
