package org.endeavourhealth.informationmanager.common.models;

public class Namespace {
    private String prefix;
    private String iri;
    private String name;

    public String getPrefix() {
        return prefix;
    }

    public Namespace setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public Namespace setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public Namespace setName(String name) {
        this.name = name;
        return this;
    }
}
