package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class ExpressionConstraint {
    private String name;
    private Operator operator;
    private List<String> clazz = new ArrayList<>();
    private List<String> classOrSubtypes = new ArrayList<>();
    private List<String> subtypes = new ArrayList<>();
    private List<String> valueSet = new ArrayList<>();
    private List<AttributeConstraint> attributeConstraint = new ArrayList<>();
    private List<RoleGroupConstraint> roleGroupConstraint = new ArrayList<>();
    private List<ExpressionConstraint> expressionConstraint = new ArrayList<>();

    public String getName() {
        return name;
    }

    public ExpressionConstraint setName(String name) {
        this.name = name;
        return this;
    }

    public Operator getOperator() {
        return operator;
    }

    public ExpressionConstraint setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public List<String> getClazz() {
        return clazz;
    }

    public ExpressionConstraint setClazz(List<String> clazz) {
        this.clazz = clazz;
        return this;
    }

    public List<String> getClassOrSubtypes() {
        return classOrSubtypes;
    }

    public ExpressionConstraint setClassOrSubtypes(List<String> classOrSubtypes) {
        this.classOrSubtypes = classOrSubtypes;
        return this;
    }

    public List<String> getSubtypes() {
        return subtypes;
    }

    public ExpressionConstraint setSubtypes(List<String> subtypes) {
        this.subtypes = subtypes;
        return this;
    }

    public List<String> getValueSet() {
        return valueSet;
    }

    public ExpressionConstraint setValueSet(List<String> valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public List<AttributeConstraint> getAttributeConstraint() {
        return attributeConstraint;
    }

    public ExpressionConstraint setAttributeConstraint(List<AttributeConstraint> attributeConstraint) {
        this.attributeConstraint = attributeConstraint;
        return this;
    }

    public List<RoleGroupConstraint> getRoleGroupConstraint() {
        return roleGroupConstraint;
    }

    public ExpressionConstraint setRoleGroupConstraint(List<RoleGroupConstraint> roleGroupConstraint) {
        this.roleGroupConstraint = roleGroupConstraint;
        return this;
    }

    public List<ExpressionConstraint> getExpressionConstraint() {
        return expressionConstraint;
    }

    public ExpressionConstraint setExpressionConstraint(List<ExpressionConstraint> expressionConstraint) {
        this.expressionConstraint = expressionConstraint;
        return this;
    }
}
