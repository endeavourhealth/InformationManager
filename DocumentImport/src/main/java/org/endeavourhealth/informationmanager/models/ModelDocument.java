package org.endeavourhealth.informationmanager.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.informationmanager.common.models.Concept;
import org.endeavourhealth.informationmanager.common.models.DocumentInfo;

import java.util.ArrayList;
import java.util.List;

public class ModelDocument {
    private DocumentInfo documentInfo;
    private List<Concept> concept = new ArrayList<>();
    private List<ConceptDefinition> conceptDefinition = new ArrayList<>();
    private List<ContentModel> contentModel = new ArrayList<>();

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public ModelDocument setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
        return this;
    }

    @JsonProperty("Concept")
    public List<Concept> getConcept() {
        return concept;
    }

    public ModelDocument setConcept(List<Concept> concept) {
        this.concept = concept;
        return this;
    }

    @JsonProperty("ConceptDefinition")
    public List<ConceptDefinition> getConceptDefinition() {
        return conceptDefinition;
    }

    public ModelDocument setConceptDefinition(List<ConceptDefinition> conceptDefinition) {
        this.conceptDefinition = conceptDefinition;
        return this;
    }

    @JsonProperty("ContentModel")
    public List<ContentModel> getContentModel() {
        return contentModel;
    }

    public ModelDocument setContentModel(List<ContentModel> contentModel) {
        this.contentModel = contentModel;
        return this;
    }
}
