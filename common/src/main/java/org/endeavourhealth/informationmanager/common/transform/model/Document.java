package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Document {
    private Ontology informationModel;


    @JsonProperty("Ontology")
    public Ontology getInformationModel() {
        return informationModel;
    }

    public Document setInformationModel(Ontology informationModel) {
        this.informationModel = informationModel;
        return this;
    }

}
