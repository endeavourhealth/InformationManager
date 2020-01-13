package org.endeavourhealth.informationmanager.common.models;

public class Supertype {
    private String supertype;
    private Boolean inferred;

    public String getSupertype() {
        return supertype;
    }

    public Supertype setSupertype(String supertype) {
        this.supertype = supertype;
        return this;
    }

    public Boolean getInferred() {
        return inferred;
    }

    public Supertype setInferred(Boolean inferred) {
        this.inferred = inferred;
        return this;
    }
}
