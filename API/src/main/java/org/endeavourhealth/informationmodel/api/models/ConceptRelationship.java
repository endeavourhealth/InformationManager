package org.endeavourhealth.informationmodel.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConceptRelationship {
    private Long id = null;
    private Long sourceId = null;
    private Long targetId = null;
    private Long relationshipId = null;
    private Integer order = null;
    private Integer cardinality = 1;    // 0 - Unlimited, 1 - Default, n - Max Limit
    private String sourceName = null;
    private String targetName = null;
    private String relationshipName = null;

    public Long getId() {
        return id;
    }

    public ConceptRelationship setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public ConceptRelationship setSourceId(Long sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public Long getTargetId() {
        return targetId;
    }

    public ConceptRelationship setTargetId(Long targetId) {
        this.targetId = targetId;
        return this;
    }

    public Long getRelationshipId() {
        return relationshipId;
    }

    public ConceptRelationship setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
        return this;
    }

    public Integer getOrder() {
        return order;
    }

    public ConceptRelationship setOrder(Integer order) {
        this.order = order;
        return this;
    }

    public Integer getCardinality() {
        return cardinality;
    }

    public ConceptRelationship setCardinality(Integer cardinality) {
        this.cardinality = cardinality;
        return this;
    }

    public String getSourceName() {
        return sourceName;
    }

    public ConceptRelationship setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public String getTargetName() {
        return targetName;
    }

    public ConceptRelationship setTargetName(String targetName) {
        this.targetName = targetName;
        return this;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public ConceptRelationship setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
        return this;
    }
}
