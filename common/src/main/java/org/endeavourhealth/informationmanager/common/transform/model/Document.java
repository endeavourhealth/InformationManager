package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Document {
    private InformationModel informationModel;

    @JsonProperty("InformationModel")
    public InformationModel getInformationModel() {
        return informationModel;
    }

    public Document setInformationModel(InformationModel informationModel) {
        this.informationModel = informationModel;
        return this;
    }
}
