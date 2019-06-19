package org.endeavourhealth.informationmanager.common.models;

import java.util.Date;

public class TaskSummary {
    private int dbid;
    private String subject;
    private byte category;
    private byte status;
    private Date created;
    private Date updated;

    public int getDbid() {
        return dbid;
    }

    public TaskSummary setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public TaskSummary setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public byte getCategory() {
        return category;
    }

    public TaskSummary setCategory(byte category) {
        this.category = category;
        return this;
    }

    public byte getStatus() {
        return status;
    }

    public TaskSummary setStatus(byte status) {
        this.status = status;
        return this;
    }

    public Date getCreated() {
        return created;
    }

    public TaskSummary setCreated(Date created) {
        this.created = created;
        return this;
    }

    public Date getUpdated() {
        return updated;
    }

    public TaskSummary setUpdated(Date updated) {
        this.updated = updated;
        return this;
    }
}
