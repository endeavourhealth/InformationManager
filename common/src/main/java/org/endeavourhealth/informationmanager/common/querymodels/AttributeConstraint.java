package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeConstraint {

    private List<String> property;
    private List<String> propertyOrSubtypes;

    @JsonProperty(required = true)
    private ExpressionConstraint value;

    /**
     * Gets the value of the property list.
     *
     * To add a new item, do as follows:
     * getProperty().add(newItem);
     *
     */
    public List<String> getProperty() {
        if (property == null) {
            property = new ArrayList<String>();
        }
        return this.property;
    }

    /**
     * Gets the value of the propertyOrSubtypes list.
     *
     * To add a new item, do as follows:
     * getPropertyOrSubtypes().add(newItem);
     *
     */
    public List<String> getPropertyOrSubtypes() {
        if (propertyOrSubtypes == null) {
            propertyOrSubtypes = new ArrayList<String>();
        }
        return this.propertyOrSubtypes;
    }

    public ExpressionConstraint getValue() {
        return value;
    }

    public void setValue(ExpressionConstraint value) {
        this.value = value;
    }

}
