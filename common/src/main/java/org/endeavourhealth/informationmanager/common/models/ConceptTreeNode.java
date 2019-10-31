package org.endeavourhealth.informationmanager.common.models;

import java.util.List;

public class ConceptTreeNode {
    private String id;
    private String name;
    private List<ConceptTreeNode> children;

    public String getId() {
        return id;
    }

    public ConceptTreeNode setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ConceptTreeNode setName(String name) {
        this.name = name;
        return this;
    }

    public List<ConceptTreeNode> getChildren() {
        return children;
    }

    public ConceptTreeNode setChildren(List<ConceptTreeNode> children) {
        this.children = children;
        return this;
    }
}
