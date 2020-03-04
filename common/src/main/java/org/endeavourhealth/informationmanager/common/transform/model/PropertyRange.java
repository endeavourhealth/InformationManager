package org.endeavourhealth.informationmanager.common.transform.model;

public class PropertyRange {
    String concept;
    String subsumption;

    public String getConcept() {
        return concept;
    }

    public PropertyRange setConcept(String concept) {
        this.concept = concept;
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
