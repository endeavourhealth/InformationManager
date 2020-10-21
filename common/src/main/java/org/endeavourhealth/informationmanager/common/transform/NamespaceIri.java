package org.endeavourhealth.informationmanager.common.transform;

public enum NamespaceIri {
    DISCOVERY("http://www.DiscoveryDataService.org/InformationModel/Ontology#", "Discovery",":"),
    SNOMED("http://snomed.info/sct#","Snomed","sn:");

    private final String _value;
    private final String _name;
    private final String _prefix;

    NamespaceIri(String value, String name,String prefix) {
        this._value = value;
        this._name = name;
        this._prefix= prefix;
    }

    public String getValue() {
        return this._value;
    }

    public String getName() {
        return this._name;
    }

    public static NamespaceIri byValue(String value) {
        for (NamespaceIri t: NamespaceIri.values()) {
            if (t._value.equals(value))
                return t;
        }

        return null;
    }

    public static NamespaceIri byName(String name) {
        for (NamespaceIri t : NamespaceIri.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
    public static NamespaceIri byPrefix(String prefix) {
        for (NamespaceIri t : NamespaceIri.values()) {
            if (t._prefix.equals(prefix))
                return t;
        }

        return null;
    }
}
