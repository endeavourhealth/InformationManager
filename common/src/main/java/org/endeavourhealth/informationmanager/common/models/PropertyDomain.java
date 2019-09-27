package org.endeavourhealth.informationmanager.common.models;

import java.util.ArrayList;
import java.util.List;

public class PropertyDomain {
    private String status;
    private List<Domain> domain = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public PropertyDomain setStatus(String status) {
        this.status = status;
        return this;
    }

    public List<Domain> getDomain() {
        return domain;
    }

    public PropertyDomain setDomain(List<Domain> domain) {
        this.domain = domain;
        return this;
    }

    public PropertyDomain addDomain(Domain domain) {
        this.domain.add(domain);
        return this;
    }
}
