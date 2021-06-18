package org.endeavourhealth.informationmanager.scratch.grix;

public class Assertion {
    private Grix grix;
    private String firstEntity;
    private String relationship;
    private boolean reverse;

    public Assertion(Grix grix, String firstEntity) {
        this.grix = grix;
        this.reverse = false;
        this.firstEntity = firstEntity;
    }

    public Assertion has(String relationshipId) {
        this.relationship = relationshipId;
        return this;
    }

    public Assertion is(String relationshipId) {
        this.relationship = relationshipId;
        this.reverse = true;
        return this;
    }

    public Relation of(String secondEntity) {
        Node source = grix.getNodeWithCreate(reverse ? secondEntity : firstEntity);
        Node target = grix.getNodeWithCreate(reverse ? firstEntity : secondEntity);

        grix.log("Assert: " + source.getId() + " -- " + relationship + " --> " + target.getId());

        return new Relation(grix, source, relationship, target);
    }
}
