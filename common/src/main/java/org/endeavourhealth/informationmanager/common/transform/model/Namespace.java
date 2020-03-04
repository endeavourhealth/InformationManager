package org.endeavourhealth.informationmanager.common.transform.model;

public class Namespace {
    private String prefix;
    private String iri;

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
}
