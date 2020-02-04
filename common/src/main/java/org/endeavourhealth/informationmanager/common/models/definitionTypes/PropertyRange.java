package org.endeavourhealth.informationmanager.common.models.definitionTypes;

public class PropertyRange extends Definition<PropertyRange> {
    private String range;
    private String subsumption;

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
}
