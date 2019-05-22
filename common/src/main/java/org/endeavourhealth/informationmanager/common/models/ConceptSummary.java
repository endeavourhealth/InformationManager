package org.endeavourhealth.informationmanager.common.models;

import java.util.Date;

public class ConceptSummary {
    private int dbid;
    private String id;
    private String name;
    private String code;
    private String scheme;
    private Short status;
    private Date updated;

    public int getDbid() {
        return dbid;
    }

    public ConceptSummary setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

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

    public String getCode() {
        return code;
    }

    public ConceptSummary setCode(String code) {
        this.code = code;
        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public ConceptSummary setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public Short getStatus() {
        return status;
    }

    public ConceptSummary setStatus(Short status) {
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
