package org.endeavourhealth.informationmanager.common.models.definitionTypes;

import org.endeavourhealth.informationmanager.common.models.DBEntity;

public class PropertyDomain extends DBEntity<PropertyDomain> {
    private String domain;
    private String operator;
    private Integer inGroup;
    private Boolean disjointGroup;
    private Integer minCardinality;
    private Integer maxCardinality;
    private Integer minInGroup;
    private Integer maxInGroup;
    // domaincol?

    public String getDomain() {
        return domain;
    }

    public PropertyDomain setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public PropertyDomain setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public Integer getInGroup() {
        return inGroup;
    }

    public PropertyDomain setInGroup(Integer inGroup) {
        this.inGroup = inGroup;
        return this;
    }

    public Boolean getDisjointGroup() {
        return disjointGroup;
    }

    public PropertyDomain setDisjointGroup(Boolean disjointGroup) {
        this.disjointGroup = disjointGroup;
        return this;
    }

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public PropertyDomain setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public PropertyDomain setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    public Integer getMinInGroup() {
        return minInGroup;
    }

    public PropertyDomain setMinInGroup(Integer minInGroup) {
        this.minInGroup = minInGroup;
        return this;
    }

    public Integer getMaxInGroup() {
        return maxInGroup;
    }

    public PropertyDomain setMaxInGroup(Integer maxInGroup) {
        this.maxInGroup = maxInGroup;
        return this;
    }
}
