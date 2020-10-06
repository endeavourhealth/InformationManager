package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

@JsonPropertyOrder({"id","status","version"})
public interface IMEntity{
    ConceptStatus getStatus();
    IMEntity setStatus(ConceptStatus status);
    Integer getVersion();
    IMEntity setVersion(Integer version);
    IMEntity setId(String id);
    String getId();

}

