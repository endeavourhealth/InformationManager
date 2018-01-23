package org.endeavourhealth.informationmodel.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class View {
    private Long viewId;
    private Long parentId;
    private Long childId;
    private Long relationshipId;
    private Long order;

    public Long getViewId() {
        return viewId;
    }

    public View setViewId(Long viewId) {
        this.viewId = viewId;
        return this;
    }

    public Long getParentId() {
        return parentId;
    }

    public View setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public Long getChildId() {
        return childId;
    }

    public View setChildId(Long childId) {
        this.childId = childId;
        return this;
    }

    public Long getRelationshipId() {
        return relationshipId;
    }

    public View setRelationshipId(Long relationshipId) {
        this.relationshipId = relationshipId;
        return this;
    }

    public Long getOrder() {
        return order;
    }

    public View setOrder(Long order) {
        this.order = order;
        return this;
    }
}
