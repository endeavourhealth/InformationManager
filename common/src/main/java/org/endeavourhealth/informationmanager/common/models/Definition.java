package org.endeavourhealth.informationmanager.common.models;

import java.util.ArrayList;
import java.util.List;

public class Definition {
    private String axiom;
    private Boolean transitive;
    private List<Supertype> supertypes = new ArrayList<>();
    private List<Property> properties = new ArrayList<>();

    public String getAxiom() {
        return axiom;
    }

    public Definition setAxiom(String axiom) {
        this.axiom = axiom;
        return this;
    }

    public Boolean getTransitive() {
        return transitive;
    }

    public Definition setTransitive(Boolean transitive) {
        this.transitive = transitive;
        return this;
    }

    public List<Supertype> getSupertypes() {
        return supertypes;
    }

    public Definition setSupertypes(List<Supertype> supertypes) {
        this.supertypes = supertypes;
        return this;
    }

    public Definition addSupertype(Supertype supertype) {
        this.supertypes.add(supertype);
        return this;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public Definition setProperties(List<Property> properties) {
        this.properties = properties;
        return this;
    }

    public Definition addProperty(Property property) {
        this.properties.add(property);
        return this;
    }
}
