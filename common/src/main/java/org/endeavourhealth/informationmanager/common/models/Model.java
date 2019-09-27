package org.endeavourhealth.informationmanager.common.models;

public class Model {
    private String iri;
    private Version version;

    public String getIri() {
        return iri;
    }

    public Model setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public Version getVersion() {
        return version;
    }

    public Model setVersion(Version version) {
        this.version = version;
        return this;
    }
}
