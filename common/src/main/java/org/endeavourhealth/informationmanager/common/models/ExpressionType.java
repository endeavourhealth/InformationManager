package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExpressionType {
    CLASS((byte)0, ":class"),
    PROPERTY((byte)1, ":property"),
    INTERSECTION((byte)2, ":class"),
    UNION((byte)3, ":class"),
    PROPERTYOBJECT((byte)4, ":class"),
    PROPERTYDATA((byte)5, ":class"),
    COMPLEMENTOF((byte)6, ":class"),
    OBJECTONEOF((byte)7, ":class");
    private byte _value;
    private String _name;


    ExpressionType (byte value, String name) {
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

    public static ExpressionType byValue(byte value) {
        for (ExpressionType t: ExpressionType.values()) {
            if (t._value == value)
                return t;
        }

        return null;
    }

    public static ExpressionType byName(String name) {
        for (ExpressionType t: ExpressionType.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
}


