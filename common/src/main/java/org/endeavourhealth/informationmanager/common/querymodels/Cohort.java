package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cohort {

    @JsonProperty(required = true)
    private Integer id;

    @JsonProperty(required = true)
    private String name;

    private String description;
    private String authoredDate;
    private String authoredBy;
    private String baseCohort;
    private String dataSubject;
    private List<String> retainedPropertyList;

    @JsonProperty(required = true)
    private List<Criterion> criterionList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
     * Gets the value of the retainedPropertyList.
     *
     * To add a new item, do as follows:
     * getRetainedPropertyList().add(newItem);
     *
     */
    public List<String> getRetainedPropertyList() {
        if (retainedPropertyList == null) {
            retainedPropertyList = new ArrayList<String>();
        }
        return this.retainedPropertyList;
    }

    /**
     * Gets the value of the retainedPropertyList.
     *
     * To add a new item, do as follows:
     * getCriterionList().add(newItem);
     *
     */
    public List<Criterion> getCriterionList() {
        if (criterionList == null) {
            criterionList = new ArrayList<Criterion>();
        }
        return this.criterionList;
    }
}
