package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSet {
    private String id;
    private String name;
    private String description;
    private Date authoredDate;
    private String authoredBy;
    private String dataSubject;
    private List<Criterion> referenceCriterion = new ArrayList<>();
    private List<DataItemCategory> itemCategory = new ArrayList<>();

    public String getId() {
        return id;
    }

    public DataSet setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataSet setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DataSet setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getAuthoredDate() {
        return authoredDate;
    }

    public DataSet setAuthoredDate(Date authoredDate) {
        this.authoredDate = authoredDate;
        return this;
    }

    public String getAuthoredBy() {
        return authoredBy;
    }

    public DataSet setAuthoredBy(String authoredBy) {
        this.authoredBy = authoredBy;
        return this;
    }

    public String getDataSubject() {
        return dataSubject;
    }

    public DataSet setDataSubject(String dataSubject) {
        this.dataSubject = dataSubject;
        return this;
    }

    public List<Criterion> getReferenceCriterion() {
        return referenceCriterion;
    }

    public DataSet setReferenceCriterion(List<Criterion> referenceCriterion) {
        this.referenceCriterion = referenceCriterion;
        return this;
    }

    public List<DataItemCategory> getItemCategory() {
        return itemCategory;
    }

    public DataSet setItemCategory(List<DataItemCategory> itemCategory) {
        this.itemCategory = itemCategory;
        return this;
    }
}
