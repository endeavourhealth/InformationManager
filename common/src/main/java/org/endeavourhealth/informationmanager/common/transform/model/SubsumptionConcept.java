package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubsumptionConcept {
    private String subsumption;
    private String concept;

    @JsonProperty("Subsumption")
    public String getSubsumption() {
        return subsumption;
    }

    public SubsumptionConcept setSubsumption(String subsumption) {
        this.subsumption = subsumption;
        return this;
    }

    @JsonProperty("Concept")
    public String getConcept() {
        return concept;
    }

    public SubsumptionConcept setConcept(String concept) {
        this.concept = concept;
        return this;
    }
}
