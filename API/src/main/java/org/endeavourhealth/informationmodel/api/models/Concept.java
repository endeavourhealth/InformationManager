package org.endeavourhealth.informationmodel.api.models;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Concept {
    private Long id = null;
    private String name = null;
    private String contextName = null;
    private String shortName = null;
    private Long clazz = null;
    private String description = null;
    private byte status = 0;

    public Long getId() {
        return id;
    }

    public Concept setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Concept setName(String name) {
        this.name = name;
        return this;
    }

    public String getContextName() {
        return contextName;
    }

    public Concept setContextName(String contextName) {
        this.contextName = contextName;
        return this;
    }

    public String getShortName() {
        return shortName;
    }

    public Concept setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public Long getClazz() {
        return clazz;
    }

    public Concept setClazz(Long clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Concept setDescription(String description) {
        this.description = description;
        return this;
    }

    public byte getStatus() {
        return status;
    }

    public Concept setStatus(byte status) {
        this.status = status;
        return this;
    }
}
