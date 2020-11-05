package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

@JsonIgnoreProperties(value={ "id" })   // TODO: Remove!!!
@JsonPropertyOrder({"dbid","status","version"})
public interface IMEntity{
    ConceptStatus getStatus();
    IMEntity setStatus(ConceptStatus status);
    Integer getVersion();
    IMEntity setVersion(Integer version);
    IMEntity setDbid(Integer dbid);
    Integer getDbid();
}

