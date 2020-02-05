package org.endeavourhealth.informationmanager.common.models.definitionTypes;

import java.util.ArrayList;
import java.util.List;

public class RoleGroup {
    private List<PropertyDefinition> properties = new ArrayList<>();

    public List<PropertyDefinition> getProperties() {
        return properties;
    }

    public RoleGroup setProperties(List<PropertyDefinition> properties) {
        this.properties = properties;
        return this;
    }

    public RoleGroup addProperty(PropertyDefinition propertyDefinition) {
        this.properties.add(propertyDefinition);
        return this;
    }
}
