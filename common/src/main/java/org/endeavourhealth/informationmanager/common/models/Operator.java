package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Operator {
    _NULL_ (null, "NULL"),
    AND ((short)0, "AND"),
    OR ((short)1, "OR"),
    NOT ((short)2, "NOT"),
    ANDNOT ((short)3, "ANDNOT"),
    ORNOT ((short)4, "ORNOT"),
    XOR ((short)5, "XOR");

    private final Short value;
    private final String name;
    Operator(Short value, String name) {
        this.value = value;
        this.name = name;
    }

    public Short getValue() { return this.value; }

    @JsonValue
    public String getName() { return this.name; }
    public String toString() {
        return this.value.toString();
    }

    public static Operator byValue(Short value) {
        if (value == null)
            return _NULL_;

        for (Operator t: Operator.values()) {
            if (value.equals(t.value))
                return t;
        }

        throw new IllegalStateException("Unknown operator value ["+value+"]");
    }

    public static Operator byName(String name) {
        if (name == null || name.isEmpty())
            return _NULL_;

        for (Operator t: Operator.values()) {
            if (t.name.equals(name))
                return t;
        }

        throw new IllegalStateException("Unknown operator ["+name+"]");
    }

}
