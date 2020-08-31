package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

@JsonPropertyOrder({"id","deprecated","version","release"})
public interface IMEntity{
    public ConceptStatus getStatus();
    public IMEntity setStatus(ConceptStatus status);
    public Integer getVersion();
    public IMEntity setVersion(Integer version);
    public IMEntity setId(String id);
    public String getId();

}

