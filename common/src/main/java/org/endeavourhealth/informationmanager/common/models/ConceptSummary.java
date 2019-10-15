package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConceptSummary {
    private String id;
    private String name;
    private String description;
    private String codeScheme;
    private String code;
    private String status;
    private Date updated;

    public String getId() {
        return id;
    }

    public ConceptSummary setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConceptSummary setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ConceptSummary setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCodeScheme() {
        return codeScheme;
    }

    public ConceptSummary setCodeScheme(String codeScheme) {
        this.codeScheme = codeScheme;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ConceptSummary setCode(String code) {
        this.code = code;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ConceptSummary setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getUpdated() {
        return updated;
    }

    public ConceptSummary setUpdated(Date updated) {
        this.updated = updated;
        return this;
    }
}
