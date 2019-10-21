package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cohort {

    @JsonProperty(required = true)
    private String id;

    @JsonProperty(required = true)
    private String name;

    private String description;
    private String authoredDate;
    private String authoredBy;
    private String baseCohort;
    private String dataSubject;
    private List<String> retainedProperty;

    @JsonProperty(required = true)
    private List<Criterion> criterion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthoredDate() {
        return authoredDate;
    }

    public void setAuthoredDate(String authoredDate) {
        this.authoredDate = authoredDate;
    }

    public String getAuthoredBy() {
        return authoredBy;
    }

    public void setAuthoredBy(String authoredBy) {
        this.authoredBy = authoredBy;
    }

    public String getBaseCohort() {
        return baseCohort;
    }

    public void setBaseCohort(String baseCohort) {
        this.baseCohort = baseCohort;
    }

    public String getDataSubject() {
        return dataSubject;
    }

    public void setDataSubject(String dataSubject) {
        this.dataSubject = dataSubject;
    }

    /**
     * Gets the value of the retainedProperty list.
     *
     * To add a new item, do as follows:
     * getRetainedProperty().add(newItem);
     *
     */
    public List<String> getRetainedProperty() {
        if (retainedProperty == null) {
            retainedProperty = new ArrayList<String>();
        }
        return this.retainedProperty;
    }

    /**
     * Gets the value of the criterion list.
     *
     * To add a new item, do as follows:
     * getCriterion().add(newItem);
     *
     */
    public List<Criterion> getCriterion() {
        if (criterion == null) {
            criterion = new ArrayList<Criterion>();
        }
        return this.criterion;
    }
}
