package org.endeavourhealth.informationmanager.common.models;

public class ConceptTreeNode extends Concept {
    public static ConceptTreeNode fromConcept(Concept concept) {
        ConceptTreeNode result = new ConceptTreeNode();
        result
            .setIri(concept.getIri())
            .setName(concept.getName())
            .setDescription(concept.getDescription())
            .setCode(concept.getCode())
            .setStatus(concept.getStatus());

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
