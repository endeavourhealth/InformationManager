package org.endeavourhealth.informationmanager.common.models;

public class Ontology {
    private String iri;
    private String name;

    public String getIri() {
        return iri;
    }

    public Ontology setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public Ontology setName(String name) {
        this.name = name;
        return this;
    }
}
