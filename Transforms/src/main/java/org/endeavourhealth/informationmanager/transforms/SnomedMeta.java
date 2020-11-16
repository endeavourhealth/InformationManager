package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.ClassExpression;
import org.endeavourhealth.imapi.model.Concept;

import java.util.HashMap;
import java.util.Map;

public class SnomedMeta {
    private Concept concept;
    private String moduleId;
    private boolean subclass;
    private Map<String, ClassExpression> roleGroups = new HashMap<>();

    public Concept getConcept() {
        return concept;
    }

    public SnomedMeta setConcept(Concept concept) {
        this.concept = concept;
        return this;
    }

    public String getModuleId() {
        return moduleId;
    }

    public SnomedMeta setModuleId(String moduleId) {
        this.moduleId = moduleId;
        return this;
    }

    public boolean isSubclass() {
        return subclass;
    }

    public SnomedMeta setSubclass(boolean subclass) {
        this.subclass = subclass;
        return this;
    }

    public Map<String, ClassExpression> getRoleGroups() {
        return roleGroups;
    }

    public SnomedMeta setRoleGroups(Map<String, ClassExpression> roleGroups) {
        this.roleGroups = roleGroups;
        return this;
    }
}
