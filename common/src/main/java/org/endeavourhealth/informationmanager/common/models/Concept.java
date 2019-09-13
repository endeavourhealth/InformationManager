package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Concept extends ConceptSummary {
    private int document;
    private String description;
    private int revision;
    private Version published;
    private String range;
    private List<ConceptProperty> properties = new ArrayList<>();
    private List<ConceptDomain> domain = new ArrayList<>();

    public int getDocument() {
        return document;
    }

    public Concept setDocument(int document) {
        this.document = document;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Concept setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getRevision() {
        return revision;
    }

    public Concept setRevision(int revision) {
        this.revision = revision;
        return this;
    }

    public Version getPublished() {
        return published;
    }

    public Concept setPublished(Version published) {
        this.published = published;
        return this;
    }

    public String getRange() {
        return range;
    }

    public Concept setRange(String range) {
        this.range = range;
        return this;
    }

    public List<ConceptProperty> getProperties() {
        return properties;
    }

    public Concept setProperties(List<ConceptProperty> properties) {
        this.properties = properties;
        return this;
    }

    public List<ConceptDomain> getDomain() {
        return domain;
    }

    public Concept setDomain(List<ConceptDomain> domain) {
        this.domain = domain;
        return this;
    }
}
