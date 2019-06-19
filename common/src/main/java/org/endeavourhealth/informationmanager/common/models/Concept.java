package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.common.cache.ObjectMapperPool;

import java.io.IOException;
import java.util.Date;

public class Concept {
    private int dbid;
    private int document;
    private JsonNode data;
    private Status status;
    private Date updated;
    private int revision;
    private Version published;

    public int getDbid() {
        return dbid;
    }

    public Concept setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public int getDocument() {
        return document;
    }

    public Concept setDocument(int document) {
        this.document = document;
        return this;
    }

    public JsonNode getData() {
        return data;
    }

    @JsonSetter("data")
    public Concept setData(JsonNode data) {
        this.data = data;
        return this;
    }

    public Concept setData(String data) throws IOException {
        this.data = ObjectMapperPool.getInstance().readTree(data);
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Concept setStatus(Status status) {
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

    public String getId() {
        return data.get("id").textValue();
    }

    public String getName() { return data.get("name").textValue(); }
}
