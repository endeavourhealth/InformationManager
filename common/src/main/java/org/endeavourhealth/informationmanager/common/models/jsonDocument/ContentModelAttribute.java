package org.endeavourhealth.informationmanager.common.models.jsonDocument;

import java.util.ArrayList;
import java.util.List;

public class ContentModelAttribute {
    private String property;
    private Integer inGroup;
    private Integer minCardinality;
    private Integer maxCardinality;
    private Integer minInGroup;
    private Integer maxInGroup;
    private List<ContentModelAttributeRange> range = new ArrayList<>();

    public String getProperty() {
        return property;
    }

    public ContentModelAttribute setProperty(String property) {
        this.property = property;
        return this;
    }

    public Integer getInGroup() {
        return inGroup;
    }

    public ContentModelAttribute setInGroup(Integer inGroup) {
        this.inGroup = inGroup;
        return this;
    }

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public ContentModelAttribute setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public ContentModelAttribute setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public Integer getMinInGroup() {
        return minInGroup;
    }

    public ContentModelAttribute setMinInGroup(Integer minInGroup) {
        this.minInGroup = minInGroup;
        return this;
    }

    public Integer getMaxInGroup() {
        return maxInGroup;
    }

    public ContentModelAttribute setMaxInGroup(Integer maxInGroup) {
        this.maxInGroup = maxInGroup;
        return this;
    }

    public List<ContentModelAttributeRange> getRange() {
        return range;
    }

    public ContentModelAttribute setRange(List<ContentModelAttributeRange> range) {
        this.range = range;
        return this;
    }
}
