package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConceptExpression {
  private String name;
  private String operator;
  private String concept;
  private AttributeExpression attribute;
  private RoleGroup roleGroup;
  private List<ConceptExpression> expression = new ArrayList<>();

    public String getName() {
        return name;
    }

    public ConceptExpression setName(String name) {
        this.name = name;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public ConceptExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public String getConcept() {
        return concept;
    }

    public ConceptExpression setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public AttributeExpression getAttribute() {
        return attribute;
    }

    public ConceptExpression setAttribute(AttributeExpression attribute) {
        this.attribute = attribute;
        return this;
    }

    public RoleGroup getRoleGroup() {
        return roleGroup;
    }

    public ConceptExpression setRoleGroup(RoleGroup roleGroup) {
        this.roleGroup = roleGroup;
        return this;
    }

    public List<ConceptExpression> getExpression() {
        return expression;
    }

    public ConceptExpression setExpression(List<ConceptExpression> expression) {
        this.expression = expression;
        return this;
    }
}
