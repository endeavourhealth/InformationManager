package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConceptMeta {
    private int document;
    private String name;
    private String description;
    private String scheme;
    private String code;
    private Status status;
    private Date updated;
    private int revision;
    private Version published;

    public int getDocument() {
        return document;
    }

    public ConceptMeta setDocument(int document) {
        this.document = document;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConceptMeta setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ConceptMeta setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public ConceptMeta setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ConceptMeta setCode(String code) {
        this.code = code;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public ConceptMeta setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Date getUpdated() {
        return updated;
    }

    public ConceptMeta setUpdated(Date updated) {
        this.updated = updated;
        return this;
    }

    public int getRevision() {
        return revision;
    }

    public ConceptMeta setRevision(int revision) {
        this.revision = revision;
        return this;
    }

    public Version getPublished() {
        return published;
    }

    public ConceptMeta setPublished(Version published) {
        this.published = published;
        return this;
    }
}
