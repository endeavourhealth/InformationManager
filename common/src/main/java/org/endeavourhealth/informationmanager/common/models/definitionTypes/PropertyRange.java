package org.endeavourhealth.informationmanager.common.models.definitionTypes;

import org.endeavourhealth.informationmanager.common.models.DBEntity;

public class PropertyRange extends DBEntity<PropertyRange> {
    private String range;
    private String subsumption;
    private String operator;

    public String getRange() {
        return range;
    }

    public PropertyRange setRange(String range) {
        this.range = range;
        return this;
    }

    public String getSubsumption() {
        return subsumption;
    }

    public PropertyRange setSubsumption(String subsumption) {
        this.subsumption = subsumption;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public PropertyRange setOperator(String operator) {
        this.operator = operator;
        return this;
    }
}
