package org.endeavourhealth.informationmanager.common.models.document;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DefinitionType {
    _NULL_ (null, "NULL"),
    SUBTYPE_OF ((short)0, "subtypeOf"),
    EQUIVALENT_TO ((short)1, "equivalentTo"),
    MAPPED_TO ((short)2, "mappedTo"),
    TERM_CODE_OF ((short)3, "termCodeOf"),
    REPLACED_BY ((short)4, "replacedBy"),
    CHILD_OF ((short)5, "childOf"),
    INVERSE_PROPERTY_OF ((short)6, "inversePropertyOf"),
    DISJOINT_WITH ((short)7, "disjointWith"),
    ROLE_GROUP((short)8, "roleGroup");

    private final Short value;
    private final String name;
    DefinitionType(Short value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public Short getValue() { return this.value; }
    public String getName() { return this.name; }
    public String toString() {
        return this.value.toString();
    }

    public static DefinitionType byValue(Short value) {
        if (value == null)
            return _NULL_;

        for (DefinitionType t: DefinitionType.values()) {
            if (value.equals(t.value))
                return t;
        }

        throw new IllegalStateException("Unknown definition value ["+value+"]");
    }

    public static DefinitionType byName(String name) {
        if (name == null || name.isEmpty())
            return _NULL_;

        for (DefinitionType t: DefinitionType.values()) {
            if (t.name.equals(name))
                return t;
        }

        throw new IllegalStateException("Unknown definition type ["+name+"]");
    }
}
