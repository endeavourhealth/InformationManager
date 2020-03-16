package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Concept {
    private String iri;
    private String type;
    private String status;
    private String name;
    private String description;
    private String code;
    private String version;

    public String getIri() {
        return iri;
    }

    public Concept setIri(String iri) {
        this.iri = iri;
        return this;
    }

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    public Concept setType(String type) {
        this.type = type;
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

    public String getCode() {
        return code;
    }

    public Concept setCode(String code) {
        this.code = code;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Concept setVersion(String version) {
        this.version = version;
        return this;
    }
}
