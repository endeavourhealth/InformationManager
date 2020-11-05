package org.endeavourhealth.informationmanager.common.transform.model;

public class ConceptReference {
    private Integer dbid;
    private String iri;
    private String name;

    public ConceptReference() {}

    public ConceptReference(String iri) {
        this.iri = iri;
    }

    public Integer getDbid() {
        return dbid;
    }

    public ConceptReference setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public ConceptReference setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConceptReference setName(String name) {
        this.name = name;
        return this;
    }
}
