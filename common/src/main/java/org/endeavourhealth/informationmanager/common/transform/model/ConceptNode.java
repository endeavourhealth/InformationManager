package org.endeavourhealth.informationmanager.common.transform.model;

import java.util.HashSet;
import java.util.Set;

public class ConceptNode extends ConceptReference {
    private Set<ConceptNode> parents;

    public Set<ConceptNode> getParents() {
        return parents;
    }

    public ConceptNode setParents(Set<ConceptNode> parents) {
        this.parents = parents;
        return this;
    }

    public ConceptNode addParent(ConceptNode parent) {
        if (this.parents == null)
            this.parents = new HashSet<>();

        this.parents.add(parent);

        return this;
    }
}
