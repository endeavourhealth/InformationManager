package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    DRAFT ((short)0, "Draft"),
    INCOMPLETE ((short)1, "Incomplete"),
    ACTIVE ((short)2, "Active"),
    DEPRECATED ((short)3, "Deprecated");

    private final Short value;
    private final String name;
    Status(short value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public short getValue() { return this.value; }
    public String getName() { return this.name; }
    public String toString() {
        return this.value.toString();
    }

    public static Status byValue(Short value) {
        for (Status t: Status.values()) {
            if (t.value.equals(value))
                return t;
        }

        return null;
    }
}
