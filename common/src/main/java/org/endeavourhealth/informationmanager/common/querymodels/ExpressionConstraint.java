package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpressionConstraint {

    private String name;
    private Operator operator;

    @JsonProperty(value = "class")
    private List<String> clazz;

    private List<String> classOrSubtypes;
    private List<String> subtypes;
    private List<String> valueSet;
    private List<AttributeConstraint> attribute;
    private List<RoleGroupConstraint> roleGroup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    /**
     * Gets the value of the clazz list.
     *
     * To add a new item, do as follows:
     * getClazz().add(newItem);
     *
     */
    public List<String> getClazz() {
        if (clazz == null) {
            clazz = new ArrayList<String>();
        }
        return this.clazz;
    }

    /**
     * Gets the value of the classOrSubtypes list.
     *
     * To add a new item, do as follows:
     * getClassOrSubtypes().add(newItem);
     *
     */
    public List<String> getClassOrSubtypes() {
        if (classOrSubtypes == null) {
            classOrSubtypes = new ArrayList<String>();
        }
        return this.classOrSubtypes;
    }

    /**
     * Gets the value of the subtypes list.
     *
     * To add a new item, do as follows:
     * getSubtypes().add(newItem);
     *
     */
    public List<String> getSubtypes() {
        if (subtypes == null) {
            subtypes = new ArrayList<String>();
        }
        return this.subtypes;
    }

    /**
     * Gets the value of the valueSet list.
     *
     * To add a new item, do as follows:
     * getValueSet().add(newItem);
     *
     */
    public List<String> getValueSet() {
        if (valueSet == null) {
            valueSet = new ArrayList<String>();
        }
        return this.valueSet;
    }

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

    /**
     * Gets the value of the roleGroup list.
     *
     * To add a new item, do as follows:
     * getRoleGroup().add(newItem);
     *
     */
    public List<RoleGroupConstraint> getRoleGroup() {
        if (roleGroup == null) {
            roleGroup = new ArrayList<RoleGroupConstraint>();
        }
        return this.roleGroup;
    }

}
