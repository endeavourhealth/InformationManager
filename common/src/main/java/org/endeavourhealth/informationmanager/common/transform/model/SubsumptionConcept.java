package org.endeavourhealth.informationmanager.common.transform.model;

public class SubsumptionConcept {
    private String subsumption;
    private String concept;

    public String getSubsumption() {
        return subsumption;
    }

    public SubsumptionConcept setSubsumption(String subsumption) {
        this.subsumption = subsumption;
        return this;
    }

    public String getConcept() {
        return concept;
    }

    public SubsumptionConcept setConcept(String concept) {
        this.concept = concept;
        return this;
    }
}
