package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StateDefinition {
    private String id;
    private String name;
    private String description;
    private Date authoredDate;
    private String authoredBy;
    private String dataSubject;
    private List<String> retainedProperty = new ArrayList<>();
    private List<Criterion> criterion = new ArrayList<>();

    public String getId() {
        return id;
    }

    public StateDefinition setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StateDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public StateDefinition setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getAuthoredDate() {
        return authoredDate;
    }

    public StateDefinition setAuthoredDate(Date authoredDate) {
        this.authoredDate = authoredDate;
        return this;
    }

    public String getAuthoredBy() {
        return authoredBy;
    }

    public StateDefinition setAuthoredBy(String authoredBy) {
        this.authoredBy = authoredBy;
        return this;
    }

    public String getDataSubject() {
        return dataSubject;
    }

    public StateDefinition setDataSubject(String dataSubject) {
        this.dataSubject = dataSubject;
        return this;
    }

    public List<String> getRetainedProperty() {
        return retainedProperty;
    }

    public StateDefinition setRetainedProperty(List<String> retainedProperty) {
        this.retainedProperty = retainedProperty;
        return this;
    }

    public List<Criterion> getCriterion() {
        return criterion;
    }

    public StateDefinition setCriterion(List<Criterion> criterion) {
        this.criterion = criterion;
        return this;
    }
}
