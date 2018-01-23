package org.endeavourhealth.informationmodel.api.models;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConceptValueRange {
    private Long id = null;
    private Long conceptId = null;
    private Long qualifierId = null;
    private String operator = null;
    private Long minimum = null;
    private Long maximum = null;

    public Long getId() {
        return id;
    }

    public ConceptValueRange setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getConceptId() {
        return conceptId;
    }

    public ConceptValueRange setConceptId(Long conceptId) {
        this.conceptId = conceptId;
        return this;
    }

    public Long getQualifierId() {
        return qualifierId;
    }

    public ConceptValueRange setQualifierId(Long qualifierId) {
        this.qualifierId = qualifierId;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public ConceptValueRange setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public Long getMinimum() {
        return minimum;
    }

    public ConceptValueRange setMinimum(Long minimum) {
        this.minimum = minimum;
        return this;
    }

    public Long getMaximum() {
        return maximum;
    }

    public ConceptValueRange setMaximum(Long maximum) {
        this.maximum = maximum;
        return this;
    }
}
