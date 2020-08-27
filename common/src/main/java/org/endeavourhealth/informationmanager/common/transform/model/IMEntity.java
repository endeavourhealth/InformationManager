package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

@JsonPropertyOrder({"id","deprecated","version","release"})
public class IMEntity{
    private String id;
    private ConceptStatus status;
    private Integer version;

    public ConceptStatus getStatus() {
        return status;
    }

    public IMEntity setStatus(ConceptStatus status) {
        this.status = status;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public IMEntity setVersion(Integer version) {
        this.version = version;
        return this;
    }





    public IMEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

}

