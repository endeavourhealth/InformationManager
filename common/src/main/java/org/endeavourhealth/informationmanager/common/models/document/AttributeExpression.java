package org.endeavourhealth.informationmanager.common.models.document;

public class AttributeExpression {
    private Operator operator;
    private String property;
    // private String exeOrUni;
    private String valueConcept;
    private String value;

    public Operator getOperator() {
        return operator;
    }

    public AttributeExpression setOperator(Operator operator) {
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

    public String getValueConcept() {
        return valueConcept;
    }

    public AttributeExpression setValueConcept(String valueConcept) {
        this.valueConcept = valueConcept;
        return this;
    }

    public String getValue() {
        return value;
    }

    public AttributeExpression setValue(String value) {
        this.value = value;
        return this;
    }
}
