package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AxiomType {
    SUBCLASSOF((byte)0, ":subClassOf"),
    EQUIVALENTTO((byte)1, ":equivalentTo"),
    SUBOBJECTPROPERTY((byte)2, ":SubObjectPropertyOf"),
    SUBDATAPROPERTY((byte)3, ":SubDataPropertyOf"),
    SUBPROPERTYRANGE((byte)4, ":propertyRange"),
    PROPERTYDOMAIN((byte)5, ":propertyRange"),
    SUBANNOTATIONPROPERTY((byte)6, ":propertyRange"),
    DISJOINTWITH((byte)7, ""),
    ANNOTATION((byte)8, "");

    private byte _value;
    private String _name;

    private AxiomType(byte value, String name) {
        this._value = value;
        this._name = name;
    }

    public byte getValue() {
        return this._value;
    }

    @JsonValue
    public String getName() {
        return this._name;
    }

    public static AxiomType byValue(byte value) {
        for (AxiomType t: AxiomType.values()) {
            if (t._value == value)
                return t;
        }

        return null;
    }

    public static AxiomType byName(String name) {
        for (AxiomType t: AxiomType.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
}
