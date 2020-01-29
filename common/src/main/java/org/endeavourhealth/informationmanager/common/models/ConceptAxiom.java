package org.endeavourhealth.informationmanager.common.models;

import org.endeavourhealth.informationmanager.common.models.definitionTypes.Definition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConceptAxiom {
    private String token;
    private List<Definition> definitions = new ArrayList<>();

    public String getToken() {
        return token;
    }

    public ConceptAxiom setToken(String token) {
        this.token = token;
        return this;
    }

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public ConceptAxiom setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
        return this;
    }

    public ConceptAxiom addDefinition(Definition definition) {
        this.definitions.add(definition);
        return this;
    }

    public ConceptAxiom addDefinitions(Collection<Definition> definitions) {
        this.definitions.addAll(definitions);
        return this;
    }

}
