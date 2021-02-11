package org.endeavourhealth.informationmanager.common.transform;

public enum OntologyIri {
    DISCOVERY("http://www.EndeavourHealth.org/InformationModel/SemanticOntology", "Discovery"),
    DISCOVERY_DATA_MODEL("http://www.EndeavourHealth.org/InformationModel/CommonDataModel", "Common data model"),
    ENCOUNTER_TYPES("http://www.EndeavourHealth.org/InformationModel/Encounters", "Encounter types"),
    VALUE_SETS("http://www.EndeavourHealth.org/InformationModel/ValueSets","Value sets"),
    LEGACY("http://www.EndeavourHealth.org/InformationModel/Legacy", "Legacy"),
    READ2("http://www.EndeavourHealth.org/InformationModel/Module/Readv2", "Read 2"),
    ODS("http://www.EndeavourHealth.org/InformationModel/Module/ODS","ODS types");
    private final String _value;
    private final String _name;

    OntologyIri(String value, String name) {
        this._value = value;
        this._name = name;
    }

    public String getValue() {
        return this._value;
    }

    public String getName() {
        return this._name;
    }

    public static OntologyIri byValue(String value) {
        for (OntologyIri t: OntologyIri.values()) {
            if (t._value.equals(value))
                return t;
        }

        return null;
    }

    public static OntologyIri byName(String name) {
        for (OntologyIri t: OntologyIri.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
}
