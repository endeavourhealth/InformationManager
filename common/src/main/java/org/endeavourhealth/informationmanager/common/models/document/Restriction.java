package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class Restriction {
    private List<String> definedProperty = new ArrayList<>();
    private String property;
    private Order order;
    private Byte count;
    private String assignedProperty;
    private Boolean assignedBooleanProperty;

    public List<String> getDefinedProperty() {
        return definedProperty;
    }

    public Restriction setDefinedProperty(List<String> definedProperty) {
        this.definedProperty = definedProperty;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public Restriction setProperty(String property) {
        this.property = property;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public Restriction setOrder(Order order) {
        this.order = order;
        return this;
    }

    public Byte getCount() {
        return count;
    }

    public Restriction setCount(Byte count) {
        this.count = count;
        return this;
    }

    public String getAssignedProperty() {
        return assignedProperty;
    }

    public Restriction setAssignedProperty(String assignedProperty) {
        this.assignedProperty = assignedProperty;
        return this;
    }

    public Boolean getAssignedBooleanProperty() {
        return assignedBooleanProperty;
    }

    public Restriction setAssignedBooleanProperty(Boolean assignedBooleanProperty) {
        this.assignedBooleanProperty = assignedBooleanProperty;
        return this;
    }
}
