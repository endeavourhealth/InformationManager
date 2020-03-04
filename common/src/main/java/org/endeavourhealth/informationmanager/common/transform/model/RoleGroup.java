package org.endeavourhealth.informationmanager.common.transform.model;

import org.endeavourhealth.informationmanager.common.models.definitionTypes.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class RoleGroup {
    private String operator;
    private List<Role> role;
    private List<RoleGroup> roleGroup;

    public String getOperator() {
        return operator;
    }

    public RoleGroup setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public List<Role> getRole() {
        return role;
    }

    public RoleGroup setRole(List<Role> role) {
        this.role = role;
        return this;
    }

    public RoleGroup addRole(Role role) {
        if (this.role == null)
            this.role = new ArrayList<>();

        this.role.add(role);
        return this;
    }

    public List<RoleGroup> getRoleGroup() {
        return roleGroup;
    }

    public RoleGroup setRoleGroup(List<RoleGroup> roleGroup) {
        this.roleGroup = roleGroup;
        return this;
    }

    public RoleGroup addRoleGroup(RoleGroup roleGroup) {
        if (this.roleGroup == null)
            this.roleGroup = new ArrayList<>();

        this.roleGroup.add(roleGroup);
        return this;

    }
}
