package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeExpression {
    private String operator;
    private String property;
    private String value;
    private ConceptExpression valueConcept;

    public String getOperator() {
        return operator;
    }

    public AttributeExpression setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public AttributeExpression setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getValue() {
        return value;
    }

    public AttributeExpression setValue(String value) {
        this.value = value;
        return this;
    }

    public ConceptExpression getValueConcept() {
        return valueConcept;
    }

    public AttributeExpression setValueConcept(ConceptExpression valueConcept) {
        this.valueConcept = valueConcept;
        return this;
    }
}
