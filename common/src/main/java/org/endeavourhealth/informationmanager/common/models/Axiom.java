package org.endeavourhealth.informationmanager.common.models;

public class Axiom {
    private String token;
    private boolean subtype;
    private boolean initial;

    public String getToken() {
        return token;
    }

    public Axiom setToken(String token) {
        this.token = token;
        return this;
    }

    public boolean isSubtype() {
        return subtype;
    }

    public Axiom setSubtype(boolean subtype) {
        this.subtype = subtype;
        return this;
    }

    public boolean isInitial() {
        return initial;
    }

    public Axiom setInitial(boolean initial) {
        this.initial = initial;
        return this;
    }
}
