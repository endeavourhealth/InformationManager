package org.endeavourhealth.informationmanager.converter;

import java.util.UUID;

public class DiscoveryIdsResult {
    private String ontologyIri;
    private String ontologyModuleIri;
    private UUID documentId;

    public String getOntologyIri() {
        return ontologyIri;
    }

    public DiscoveryIdsResult setOntologyIri(String ontologyIri) {
        this.ontologyIri = ontologyIri;
        return this;
    }

    public String getOntologyModuleIri() {
        return ontologyModuleIri;
    }

    public DiscoveryIdsResult setOntologyModuleIri(String ontologyModuleIri) {
        this.ontologyModuleIri = ontologyModuleIri;
        return this;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public DiscoveryIdsResult setDocumentId(UUID documentId) {
        this.documentId = documentId;
        return this;
    }
}
