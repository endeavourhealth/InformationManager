package org.endeavourhealth.informationmanager.common.transform;

public enum OntologyModuleIri {
    COMMON_CONCEPTS("http://www.DiscoveryDataService.org/InformationModel/Module/CommonConcepts","Common concepts"),
    DISCOVERY_DATA_MODEL("http://www.DiscoveryDataService.org/InformationModel/Module/CommonDataModel", "Common data model"),
    SEMANTIC_MODEL("http://www.DiscoveryDataService.org/InformationModel/Module/SemanticModel", "Semantic model"),
    ENCOUNTER_TYPES("http://www.DiscoveryDataService.org/InformationModel/Module/Encounters", "Encounter types"),
    VALUE_SETS("http://www.DiscoveryDataService.org/InformationModel/Module/ValueSets","Value sets"),
    LEGACY("http://www.DiscoveryDataService.org/InformationModel/Module/Legacy", "Legacy"),
    SNOMED("http://www.DiscoveryDataService.org/InformationModel/Module/Snomed", "SNOMED"),
    READ2("http://www.DiscoveryDataService.org/InformationModel/Module/Readv2", "Read 2"),
    ODS("http://www.DiscoveryDataService.org/InformationModel/Module/ODS","ODS types");

    private final String _value;
    private final String _name;

    OntologyModuleIri(String value, String name) {
        this._value = value;
        this._name = name;
    }

    public String getValue() {
        return this._value;
    }

    public String getName() {
        return this._name;
    }

    public static OntologyModuleIri byValue(String value) {
        for (OntologyModuleIri t: OntologyModuleIri.values()) {
            if (t._value.equals(value))
                return t;
        }

        return null;
    }

    public static OntologyModuleIri byName(String name) {
        for (OntologyModuleIri t: OntologyModuleIri.values()) {
            if (t._name.equals(name))
                return t;
        }

        return null;
    }
}
