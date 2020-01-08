package org.endeavourhealth.informationmanager.common.models;

import java.util.ArrayList;
import java.util.List;

public class ConceptTreeNode extends Concept {
    public static ConceptTreeNode fromConcept(Concept concept) {
        ConceptTreeNode result = new ConceptTreeNode();
        result
            .setId(concept.getId())
            .setModel(concept.getModel())
            .setName(concept.getName())
            .setDescription(concept.getDescription())
            .setCodeScheme(concept.getCodeScheme())
            .setCode(concept.getCode())
            .setStatus(concept.getStatus())
            .setUpdated(concept.getUpdated())
            .setWeighting(concept.getWeighting());

        return result;
    }

    private int level = 0;
    private boolean expandable = true;

    public int getLevel() {
        return level;
    }

    public ConceptTreeNode setLevel(int level) {
        this.level = level;
        return this;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public ConceptTreeNode setExpandable(boolean expandable) {
        this.expandable = expandable;
        return this;
    }
}
