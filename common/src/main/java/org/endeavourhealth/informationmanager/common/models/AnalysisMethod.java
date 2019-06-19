package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnalysisMethod {
    CONCEPT_ID ((short)0, "Concept ID"),
    CONCEPT_EQUIVALENT ((short)4, " - Equivalent to"),
    CONCEPT_RELATED ((short)5, " - Related to"),
    CONCEPT_REPLACED ((short)6, " - Replaced by"),

    SCHEME_CODE ((short)1, "Scheme/Code"),
    NAME ((short)2, "Name"),
    DESCRIPTION ((short)3, "Description")
    ;

    private final Short value;
    private final String name;
    AnalysisMethod(short value, String name) {
        this.value = value;
        this.name = name;
    }

    @JsonValue
    public short getValue() { return this.value; }
    public String getName() { return this.name; }
    public String toString() {
        return this.value.toString();
    }

    public static AnalysisMethod byValue(Short value) {
        for (AnalysisMethod t: AnalysisMethod.values()) {
            if (t.value.equals(value))
                return t;
        }

        return null;
    }
}
