package org.endeavourhealth.informationmanager.common.transform.model;

import java.util.Date;

public class DocumentInfo {
    private String documentId;
    private String documentIri;
    private String documentTitle;
    private Date effectiveDate;
    private String publisher;
    private String purpose;
    private String targetVersion;
    private String documentStatus;

    public String getDocumentId() {
        return documentId;
    }

    public DocumentInfo setDocumentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public String getDocumentIri() {
        return documentIri;
    }

    public DocumentInfo setDocumentIri(String documentIri) {
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

    public String getTargetVersion() {
        return targetVersion;
    }

    public DocumentInfo setTargetVersion(String targetVersion) {
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
}
