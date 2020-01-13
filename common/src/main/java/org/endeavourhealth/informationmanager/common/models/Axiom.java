package org.endeavourhealth.informationmanager.common.models;

import java.util.ArrayList;
import java.util.List;

public class Axiom {
    private String token;
    private Boolean transitive;
    private List<Supertype> supertypes = new ArrayList<>();
    private List<Property> properties = new ArrayList<>();

    public String getToken() {
        return token;
    }

    public Axiom setToken(String token) {
        this.token = token;
        return this;
    }

    public Boolean getTransitive() {
        return transitive;
    }

    public Axiom setTransitive(Boolean transitive) {
        this.transitive = transitive;
        return this;
    }

    public List<Supertype> getSupertypes() {
        return supertypes;
    }

    public Axiom setSupertypes(List<Supertype> supertypes) {
        this.supertypes = supertypes;
        return this;
    }

    public Axiom addSupertype(Supertype supertype) {
        this.supertypes.add(supertype);
        return this;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public Axiom setProperties(List<Property> properties) {
        this.properties = properties;
        return this;
    }

    public Axiom addProperty(Property property) {
        this.properties.add(property);
        return this;
    }
}
