package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Domain {
    private String concept;
    private Integer inGroup;
    private Integer minCardinality;
    private Integer maxCardinality;

    @JsonProperty("Concept")
    public String getConcept() {
        return concept;
    }

    public Domain setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public Integer getInGroup() {
        return inGroup;
    }

    public Domain setInGroup(Integer inGroup) {
        this.inGroup = inGroup;
        return this;
    }

    @JsonProperty("MinCardinality")
    public Integer getMinCardinality() {
        return minCardinality;
    }

    public Domain setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    @JsonProperty("MaxCardinality")
    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public Domain setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }
}
