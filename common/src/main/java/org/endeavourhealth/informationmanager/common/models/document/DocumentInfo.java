package org.endeavourhealth.informationmanager.common.models.document;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DocumentInfo {
    private UUID documentId;
    private URI documentIri;
    private String documentTitle;
    private URI modelIri;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd:HH:mm:ss")
    private Date effectiveDate;
    private String publisher;
    private String purpose;
    private Version baseModelVersion;
    private Version targetVersion;
    private String documentStatus;
    private List<Prefix> prefix = new ArrayList<>();

    public UUID getDocumentId() {
        return documentId;
    }

    public DocumentInfo setDocumentId(UUID documentId) {
        this.documentId = documentId;
        return this;
    }

    public URI getDocumentIri() {
        return documentIri;
    }

    public DocumentInfo setDocumentIri(URI documentIri) {
        this.documentIri = documentIri;
        return this;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public DocumentInfo setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
        return this;
    }

    public URI getModelIri() {
        return modelIri;
    }

    public DocumentInfo setModelIri(URI modelIri) {
        this.modelIri = modelIri;
        return this;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public DocumentInfo setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public DocumentInfo setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public String getPurpose() {
        return purpose;
    }

    public DocumentInfo setPurpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public Version getBaseModelVersion() {
        return baseModelVersion;
    }

    public DocumentInfo setBaseModelVersion(Version baseModelVersion) {
        this.baseModelVersion = baseModelVersion;
        return this;
    }

    public Version getTargetVersion() {
        return targetVersion;
    }

    public DocumentInfo setTargetVersion(Version targetVersion) {
        this.targetVersion = targetVersion;
        return this;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public DocumentInfo setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
        return this;
    }

    public List<Prefix> getPrefix() {
        return prefix;
    }

    public DocumentInfo setPrefix(List<Prefix> prefix) {
        this.prefix = prefix;
        return this;
    }
}
