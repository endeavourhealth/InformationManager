package org.endeavourhealth.informationmanager.common.transform.model;

import java.util.ArrayList;
import java.util.List;

public class PropertyDomain {
    private String operator;
    private List<Domain> domain;

    public String getOperator() {
        return operator;
    }

    public PropertyDomain setOperator(String operator) {
        this.operator = operator;
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
        if (this.domain == null)
            this.domain = new ArrayList<>();

        this.domain.add(domain);
        return this;
    }
}
