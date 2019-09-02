package org.endeavourhealth.informationmanager.common.models;

public class IdNamePair {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public IdNamePair setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public IdNamePair setName(String name) {
        this.name = name;
        return this;
    }
}
