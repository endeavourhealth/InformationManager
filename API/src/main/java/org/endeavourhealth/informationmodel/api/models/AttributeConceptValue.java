package org.endeavourhealth.informationmodel.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeConceptValue {
    private Long conceptId;
    private Long attributeId;
    private Long valueId;

    public Long getConceptId() {
        return conceptId;
    }

    public AttributeConceptValue setConceptId(Long conceptId) {
        this.conceptId = conceptId;
        return this;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public AttributeConceptValue setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
        return this;
    }

    public Long getValueId() {
        return valueId;
    }

    public AttributeConceptValue setValueId(Long valueId) {
        this.valueId = valueId;
        return this;
    }
}
