package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.informationmanager.common.models.DocumentInfo;

import java.util.ArrayList;
import java.util.List;

public class InformationModel {
    private List<Namespace> namespace = new ArrayList<>();
    private DocumentInfo documentInfo;
    private List<Concept> concept = new ArrayList<>();
    private List<ConceptAxiom> conceptAxiom = new ArrayList<>();

    @JsonProperty("Namespace")
    public List<Namespace> getNamespace() {
        return namespace;
    }

    public InformationModel setNamespace(List<Namespace> namespace) {
        this.namespace = namespace;
        return this;
    }

    @JsonProperty("DocumentInfo")
    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public InformationModel setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
        return this;
    }

    @JsonProperty("Concept")
    public List<Concept> getConcept() {
        return concept;
    }

    public InformationModel setConcept(List<Concept> concept) {
        this.concept = concept;
        return this;
    }

    @JsonProperty("ConceptAxiom")
    public List<ConceptAxiom> getConceptAxiom() {
        return conceptAxiom;
    }

    public InformationModel setConceptAxiom(List<ConceptAxiom> conceptAxiom) {
        this.conceptAxiom = conceptAxiom;
        return this;
    }
}
