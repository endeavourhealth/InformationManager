package org.endeavourhealth.informationmanager.common.dal;

public enum ConceptDefinitionType {
    CLASS(1),
    OBJECT_PROPERTY(2),
    DATA_PROPERTY(3),
    DATA_TYPE(4),
    ANNOTATION_PROPERTY(5);

    private int _value;

    private ConceptDefinitionType(int value) {
        this._value = value;
    }

    public int value() {
        return this._value;
    }
}
