package org.endeavourhealth.informationmodel.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConceptAttribute {
    private Long conceptId;
    private Long attributeId;
    private String attributeName;
    private Long valueId;
    private String valueName;

    public Long getConceptId() {
        return conceptId;
    }

    public ConceptAttribute setConceptId(Long conceptId) {
        this.conceptId = conceptId;
        return this;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public ConceptAttribute setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
        return this;
    }

    public Long getValueId() {
        return valueId;
    }

    public ConceptAttribute setValueId(Long valueId) {
        this.valueId = valueId;
        return this;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public ConceptAttribute setAttributeName(String attributeName) {
        this.attributeName = attributeName;
        return this;
    }

    public String getValueName() {
        return valueName;
    }

    public ConceptAttribute setValueName(String valueName) {
        this.valueName = valueName;
        return this;
    }
}
