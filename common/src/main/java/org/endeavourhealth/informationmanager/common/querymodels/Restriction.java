package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Restriction {

    private List<String> definedProperty;
    private String property;

    @JsonProperty(required = true)
    private Order order;

    @JsonProperty(required = true)
    private Integer count;

    private String assignedProperty;
    private String assignedBooleanProperty;

    /**
     * Gets the value of the definedProperty list.
     *
     * To add a new item, do as follows:
     * getDefinedProperty().add(newItem);
     *
     */
    public List<String> getDefinedProperty() {
        if (definedProperty == null) {
            definedProperty = new ArrayList<String>();
        }
        return this.definedProperty;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getAssignedProperty() {
        return assignedProperty;
    }

    public void setAssignedProperty(String assignedProperty) {
        this.assignedProperty = assignedProperty;
    }

    public String getAssignedBooleanProperty() {
        return assignedBooleanProperty;
    }

    public void setAssignedBooleanProperty(String assignedBooleanProperty) {
        this.assignedBooleanProperty = assignedBooleanProperty;
    }

}
