package org.endeavourhealth.informationmanager.common.transform;

public enum OntologyIri {
    DISCOVERY("http://www.DiscoveryDataService.org/InformationModel/Ontology/HealthCare", "Discovery"),
    SNOMED("http://snomed.info/sct", "SNOMED");

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
