package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cohort {
    private String id;
    private String name;
    private String description;
    private Date authoredDate;
    private String authoredBy;
    private String baseCohort;
    private String dataSubject;
    private List<String> retainedProperty = new ArrayList<>();
    private List<Criterion> criterion = new ArrayList<>();

    public String getId() {
        return id;
    }

    public Cohort setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Cohort setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Cohort setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getAuthoredDate() {
        return authoredDate;
    }

    public Cohort setAuthoredDate(Date authoredDate) {
        this.authoredDate = authoredDate;
        return this;
    }

    public String getAuthoredBy() {
        return authoredBy;
    }

    public Cohort setAuthoredBy(String authoredBy) {
        this.authoredBy = authoredBy;
        return this;
    }

    public String getBaseCohort() {
        return baseCohort;
    }

    public Cohort setBaseCohort(String baseCohort) {
        this.baseCohort = baseCohort;
        return this;
    }

    public String getDataSubject() {
        return dataSubject;
    }

    public Cohort setDataSubject(String dataSubject) {
        this.dataSubject = dataSubject;
        return this;
    }

    public List<String> getRetainedProperty() {
        return retainedProperty;
    }

    public Cohort setRetainedProperty(List<String> retainedProperty) {
        this.retainedProperty = retainedProperty;
        return this;
    }

    public List<Criterion> getCriterion() {
        return criterion;
    }

    public Cohort setCriterion(List<Criterion> criterion) {
        this.criterion = criterion;
        return this;
    }
}
