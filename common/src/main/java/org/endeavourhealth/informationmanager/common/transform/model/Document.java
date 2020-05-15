package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Document {
    private Ontology informationModel;

    @JsonProperty("InformationModel")
    public Ontology getInformationModel() {
        return informationModel;
    }

    public Document setInformationModel(Ontology informationModel) {
        this.informationModel = informationModel;
        return this;
    }
}
