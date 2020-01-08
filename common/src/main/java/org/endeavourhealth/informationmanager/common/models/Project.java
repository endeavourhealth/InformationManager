package org.endeavourhealth.informationmanager.common.models;

public class Project {
    private Integer dbid;
    private String name;
    private String brief;
    private String description;

    public Integer getDbid() {
        return dbid;
    }

    public Project setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public String getBrief() {
        return brief;
    }

    public Project setBrief(String brief) {
        this.brief = brief;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }
}
