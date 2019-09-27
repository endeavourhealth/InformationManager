package org.endeavourhealth.informationmanager.common.models;

public class Domain {
    private String property;
    private int minimum;
    private int maximum;

    public String getProperty() {
        return property;
    }

    public Domain setProperty(String property) {
        this.property = property;
        return this;
    }

    public int getMinimum() {
        return minimum;
    }

    public Domain setMinimum(int minimum) {
        this.minimum = minimum;
        return this;
    }

    public int getMaximum() {
        return maximum;
    }

    public Domain setMaximum(int maximum) {
        this.maximum = maximum;
        return this;
    }
}
