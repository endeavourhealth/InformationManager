package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.informationmanager.common.models.ConceptOrigin;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

public class Concept {
    private String iri;
    private String type;
    private ConceptStatus status;
    private ConceptOrigin origin;
    private String name;
    private String description;
    private String scheme;
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

    public ConceptStatus getStatus() {
        return status;
    }

    public Concept setStatus(ConceptStatus status) {
        this.status = status;
        return this;
    }

    public ConceptOrigin getOrigin() {
        return origin;
    }

    public Concept setOrigin(ConceptOrigin origin) {
        this.origin = origin;
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

    public String getScheme() {
        return scheme;
    }

    public Concept setScheme(String scheme) {
        this.scheme = scheme;
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
