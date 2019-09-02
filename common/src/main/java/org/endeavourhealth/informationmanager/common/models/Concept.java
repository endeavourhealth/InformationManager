package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Concept {
    private String id;
    private ConceptMeta meta = new ConceptMeta();
    private String range;
    private List<ConceptProperty> properties = new ArrayList<>();
    private List<ConceptDomain> domain = new ArrayList<>();

    public String getId() {
        return id;
    }

    public Concept setId(String id) {
        this.id = id;
        return this;
    }

    public ConceptMeta getMeta() {
        return meta;
    }

    public Concept setMeta(ConceptMeta meta) {
        this.meta = meta;
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
