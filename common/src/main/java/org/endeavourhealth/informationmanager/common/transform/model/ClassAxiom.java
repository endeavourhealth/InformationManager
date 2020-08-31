package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

import java.util.ArrayList;
import java.util.List;

public class ClassAxiom extends ClassExpression implements IMEntity{
    private String id;
    private ConceptStatus status;
    private Integer version;

    @Override
    public ConceptStatus getStatus() {
        return status;
    }

    @Override
    public IMEntity setStatus(ConceptStatus status) {
        this.status=status;
        return this;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public IMEntity setVersion(Integer version) {
        this.version= version;
        return this;
    }

    @Override
    public IMEntity setId(String id) {
        this.id= id;
        return this;
    }

    @Override
    public String getId() {
        return id;
    }
}
