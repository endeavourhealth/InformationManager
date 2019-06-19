package org.endeavourhealth.informationmanager.common.models;

public class TaskCategory {
    byte dbid;
    String name;

    public byte getDbid() {
        return dbid;
    }

    public TaskCategory setDbid(byte dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskCategory setName(String name) {
        this.name = name;
        return this;
    }
}
