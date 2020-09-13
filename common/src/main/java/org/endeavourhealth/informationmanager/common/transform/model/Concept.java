package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

@JsonPropertyOrder({"id","status","version","iri","name","description","code","scheme","shortName","elementName","order"})
public class Concept implements IMEntity{
    private String iri;
    private String name;
    private String description;
    private String code;
    private String scheme;
    private String id;
    private ConceptStatus status;
    private Integer version;


    @Override
    public ConceptStatus getStatus() {
        return status;
    }

    @Override
    public Concept setStatus(ConceptStatus status) {
        this.status=status;
        return this;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public Concept setVersion(Integer version) {
        this.version= version;
        return this;
    }

    @Override
    public Concept setId(String id) {
        this.id= id;
        return this;
    }

    @Override
    public String getId() {
        return id;
    }


    public Concept() {}

    public Concept(String iri, String name) {
        this.iri = iri;
        this.name = name;
    }

    public String getIri() {
        return iri;
    }

    public Concept setIri(String iri) {
        this.iri = iri;
        return this;
    }



    public String getName() {
        return name;
    }

    public Concept setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Concept setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public Concept setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Concept setCode(String code) {
        this.code = code;
        return this;
    }

}
