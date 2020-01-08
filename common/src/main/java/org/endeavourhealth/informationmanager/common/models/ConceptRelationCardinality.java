package org.endeavourhealth.informationmanager.common.models;

public class ConceptRelationCardinality {
    private Integer minCardinality;
    private Integer maxCardinality;
    private Integer maxInGroup;

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public ConceptRelationCardinality setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public ConceptRelationCardinality setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public Integer getMaxInGroup() {
        return maxInGroup;
    }

    public ConceptRelationCardinality setMaxInGroup(Integer maxInGroup) {
        this.maxInGroup = maxInGroup;
        return this;
    }
}
