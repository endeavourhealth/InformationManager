package org.endeavourhealth.informationmodel.api.models;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributePrimitiveValue {
    private Long conceptId = null;
    private Long attributeId = null;
    private Object value = null;

    public Long getConceptId() {
        return conceptId;
    }

    public AttributePrimitiveValue setConceptId(Long conceptId) {
        this.conceptId = conceptId;
        return this;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public AttributePrimitiveValue setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public AttributePrimitiveValue setValue(Object value) {
        this.value = value;
        return this;
    }
}
