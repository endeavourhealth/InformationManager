package org.endeavourhealth.informationmanager.common.models;

public class Instance {
    private int dbid;
    private String name;
    private String url;

    public int getDbid() {
        return dbid;
    }

    public Instance setDbid(int dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Instance setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Instance setUrl(String url) {
        this.url = url;
        return this;
    }
}
