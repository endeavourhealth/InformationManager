package org.endeavourhealth.informationmanager.common.models;

import java.util.Date;

public class Concept {
    private String id;
    private Model model;
    private String name;
    private String description;
    private String codeScheme;
    private String code;
    private String status;
    private Date updated;
    private Integer weighting;

    public String getId() {
        return id;
    }

    public Concept setId(String id) {
        this.id = id;
        return this;
    }

    public Model getModel() {
        return model;
    }

    public Concept setModel(Model model) {
        this.model = model;
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

    public String getCodeScheme() {
        return codeScheme;
    }

    public Concept setCodeScheme(String codeScheme) {
        this.codeScheme = codeScheme;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Concept setCode(String code) {
        this.code = code;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Concept setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getUpdated() {
        return updated;
    }

    public Concept setUpdated(Date updated) {
        this.updated = updated;
        return this;
    }

    public Integer getWeighting() {
        return weighting;
    }

    public Concept setWeighting(Integer weighting) {
        this.weighting = weighting;
        return this;
    }
}
