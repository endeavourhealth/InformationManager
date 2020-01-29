package org.endeavourhealth.informationmanager.common.models.definitionTypes;

import java.util.ArrayList;
import java.util.List;

public class RoleGroupDefinition extends Definition<RoleGroupDefinition> {
    private int group;
    private List<PropertyDefinition> properties = new ArrayList<>();

    public int getGroup() {
        return group;
    }

    public RoleGroupDefinition setGroup(int group) {
        this.group = group;
        return this;
    }

    public List<PropertyDefinition> getProperties() {
        return properties;
    }

    public RoleGroupDefinition setProperties(List<PropertyDefinition> properties) {
        this.properties = properties;
        return this;
    }

    public RoleGroupDefinition addProperty(PropertyDefinition propertyDefinition) {
        this.properties.add(propertyDefinition);
        return this;
    }
}
