package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ConceptOrigin {
    CORE((byte)0, "Core"),
    LEGACY((byte)1, "Legacy");

    private byte _value;
    private String _name;

    private ConceptOrigin(byte value, String name) {
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

    public static ConceptOrigin byValue(byte value) {
        for (ConceptOrigin t: ConceptOrigin.values()) {
            if (t._value == value)
                return t;
        }

        return null;
    }

    public static ConceptOrigin byName(String name) {
        for (ConceptOrigin t: ConceptOrigin.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
}
