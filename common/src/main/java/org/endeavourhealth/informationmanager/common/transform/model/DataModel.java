package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    private String iri;
    private List<DataModelEntity> entity;

    public String getIri() {
        return iri;
    }

    public DataModel setIri(String iri) {
        this.iri = iri;
        return this;
    }

    @JsonProperty("Entity")
    public List<DataModelEntity> getEntity() {
        return entity;
    }

    public DataModel setEntity(List<DataModelEntity> entity) {
        this.entity = entity;
        return this;
    }

    public DataModel addEntity(DataModelEntity entity) {
        if (this.entity == null)
            this.entity = new ArrayList<>();

        this.entity.add(entity);
        return this;
    }
}
