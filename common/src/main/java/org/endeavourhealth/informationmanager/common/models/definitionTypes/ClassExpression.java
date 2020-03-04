package org.endeavourhealth.informationmanager.common.models.definitionTypes;

import java.util.ArrayList;
import java.util.List;

public class ClassExpression {
    private List<SimpleConcept> superTypes = new ArrayList<>();
    private List<RoleGroup> roleGroups = new ArrayList();

    public List<SimpleConcept> getSuperTypes() {
        return superTypes;
    }

    public ClassExpression setSuperTypes(List<SimpleConcept> superTypes) {
        this.superTypes = superTypes;
        return this;
    }

    public ClassExpression addSuperType(SimpleConcept supertype) {
        this.superTypes.add(supertype);
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
