package org.endeavourhealth.informationmanager.common.transform.model;

import java.util.ArrayList;
import java.util.List;

public class ClassExpression {
    private Boolean inferred;
    private String operator;
    private List<String> concept;
    private List<RoleGroup> roleGroups = new ArrayList();

    public Boolean getInferred() {
        return inferred;
    }

    public ClassExpression setInferred(Boolean inferred) {
        this.inferred = inferred;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public ClassExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public List<String> getConcept() {
        return concept;
    }

    public ClassExpression setConcept(List<String> concept) {
        this.concept = concept;
        return this;
    }

    public ClassExpression addConcept(String concept) {
        if (this.concept == null)
            this.concept = new ArrayList<>();

        this.concept.add(concept);
        return this;
    }

    public List<RoleGroup> getRoleGroups() {
        return roleGroups;
    }

    public ClassExpression setRoleGroups(List<RoleGroup> roleGroups) {
        this.roleGroups = roleGroups;
        return this;
    }

    public ClassExpression addRoleGroup(RoleGroup roleGroup) {
        this.roleGroups.add(roleGroup);
        return this;
    }
}
