package org.endeavourhealth.informationmanager.common.models;

public class Concept {
    private String iri;
    private String status;
    private String name;
    private String description;
    private String ontology;
    private String code;

    public String getIri() {
        return iri;
    }

    public Concept setIri(String iri) {
        this.iri = iri;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Concept setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getName() {
        return name;
    }

    public Concept setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Concept setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getOntology() {
        return ontology;
    }

    public Concept setOntology(String ontology) {
        this.ontology = ontology;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Concept setCode(String code) {
        this.code = code;
        return this;
    }
}
