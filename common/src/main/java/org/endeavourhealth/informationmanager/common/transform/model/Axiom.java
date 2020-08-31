package org.endeavourhealth.informationmanager.common.transform.model;

import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

public class Axiom implements IMEntity{
    private String id;
    private ConceptStatus status;
    private Integer version;

    @Override
    public ConceptStatus getStatus() {
        return status;
    }

    @Override
    public Axiom setStatus(ConceptStatus status) {
        this.status=status;
        return this;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public Axiom setVersion(Integer version) {
        this.version= version;
        return this;
    }

    @Override
    public Axiom setId(String id) {
        this.id= id;
        return this;
    }

    @Override
    public String getId() {
        return id;
    }
}
