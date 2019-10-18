package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleGroupConstraint {

    private List<AttributeConstraint> attribute;

    /**
     * Gets the value of the attribute list.
     *
     * To add a new item, do as follows:
     * getAttribute().add(newItem);
     *
     */
    public List<AttributeConstraint> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<AttributeConstraint>();
        }
        return this.attribute;
    }

}
