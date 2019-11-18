package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class PropertyDomain {
    private String status;
    private String property;
    private List<Domain> domain = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public PropertyDomain setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public PropertyDomain setProperty(String property) {
        this.property = property;
        return this;
    }

    public List<Domain> getDomain() {
        return domain;
    }

    public PropertyDomain setDomain(List<Domain> domain) {
        this.domain = domain;
        return this;
    }
}
