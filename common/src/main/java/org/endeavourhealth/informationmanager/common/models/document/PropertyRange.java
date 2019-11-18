package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class PropertyRange {
    private String status;
    private String property;
    private List<SimpleExpressionConstraint> rangeClass = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public PropertyRange setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public PropertyRange setProperty(String property) {
        this.property = property;
        return this;
    }

    public List<SimpleExpressionConstraint> getRangeClass() {
        return rangeClass;
    }

    public PropertyRange setRangeClass(List<SimpleExpressionConstraint> rangeClass) {
        this.rangeClass = rangeClass;
        return this;
    }
}
