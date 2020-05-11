package org.endeavourhealth.informationmanager.scratch.grix;

public class Relation {
    private Grix grix;
    private Node source;
    private RelType relType;
    private Node target;

    public Relation(Grix grix, Node source, String relationship, Node target) {
        this.grix = grix;
        RelType relType = grix.getRelTypeWithCreate(relationship);
        this.source = source;
        this.relType = relType;
        this.target = target;

        relType.relationList.add(this);
        source.addRelationship(this);
        target.addRelationship(this);
    }

    public Node getSource() {
        return source;
    }

    public Relation setSource(Node source) {
        this.source = source;
        return this;
    }

    public RelType getRelType() {
        return relType;
    }

    public Relation setRelType(RelType relType) {
        this.relType = relType;
        return this;
    }

    public Node getTarget() {
        return target;
    }

    public Relation setTarget(Node target) {
        this.target = target;
        return this;
    }
}
