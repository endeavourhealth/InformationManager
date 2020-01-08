package org.endeavourhealth.informationmanager.common.models;

public class ConceptRelation {
    private String subject;
    private int group;
    private String relation;
    private String object;

    private ConceptRelationCardinality cardinality;
    private ConceptPropertyData value;

    public String getSubject() {
        return subject;
    }

    public ConceptRelation setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public int getGroup() {
        return group;
    }

    public ConceptRelation setGroup(int group) {
        this.group = group;
        return this;
    }

    public String getRelation() {
        return relation;
    }

    public ConceptRelation setRelation(String relation) {
        this.relation = relation;
        return this;
    }

    public String getObject() {
        return object;
    }

    public ConceptRelation setObject(String object) {
        this.object = object;
        return this;
    }

    public ConceptRelationCardinality getCardinality() {
        return cardinality;
    }

    public ConceptRelation setCardinality(ConceptRelationCardinality cardinality) {
        this.cardinality = cardinality;
        return this;
    }

    public ConceptPropertyData getValue() {
        return value;
    }

    public ConceptRelation setValue(ConceptPropertyData value) {
        this.value = value;
        return this;
    }
}
