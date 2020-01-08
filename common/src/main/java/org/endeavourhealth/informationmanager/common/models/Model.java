package org.endeavourhealth.informationmanager.common.models;

public class Model {
    private String iri;
    private String version;

    public String getIri() {
        return iri;
    }

    public Model setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Model setVersion(String version) {
        this.version = version;
        return this;
    }
}
