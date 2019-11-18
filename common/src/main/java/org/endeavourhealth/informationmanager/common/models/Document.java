package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.informationmanager.common.models.document.ModelDocument;

public class Document {
    private ModelDocument modelDocument;

    @JsonProperty("ModelDocument")
    public ModelDocument getModelDocument() {
        return modelDocument;
    }

    public Document setModelDocument(ModelDocument modelDocument) {
        this.modelDocument = modelDocument;
        return this;
    }
}
