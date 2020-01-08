package org.endeavourhealth.informationmanager.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ContentModel {
    private String clazz;
    private List<ContentModelAttribute> attribute;

    @JsonProperty("class")
    public String getClazz() {
        return clazz;
    }
    public ContentModel setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public List<ContentModelAttribute> getAttribute() {
        return attribute;
    }
    public ContentModel setAttribute(List<ContentModelAttribute> attribute) {
        this.attribute = attribute;
        return this;
    }
}
