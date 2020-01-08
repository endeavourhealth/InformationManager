package org.endeavourhealth.informationmanager.common.models;

import java.net.URI;

public class Prefix {
    private String label;
    private URI iri;

    public String getLabel() {
        return label;
    }

    public Prefix setLabel(String label) {
        this.label = label;
        return this;
    }

    public URI getIri() {
        return iri;
    }

    public Prefix setIri(URI iri) {
        this.iri = iri;
        return this;
    }
}
