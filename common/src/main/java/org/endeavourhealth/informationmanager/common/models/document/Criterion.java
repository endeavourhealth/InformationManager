package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class Criterion {
    private String description;
    private Operator operator;
    private String clazz;
    private Filter filter;
    private Restriction restriction;
    private Test test;
    private List<Criterion> criterion = new ArrayList<>();

    public String getDescription() {
        return description;
    }

    public Criterion setDescription(String description) {
        this.description = description;
        return this;
    }

    public Operator getOperator() {
        return operator;
    }

    public Criterion setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public String getClazz() {
        return clazz;
    }

    public Criterion setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public Filter getFilter() {
        return filter;
    }

    public Criterion setFilter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public Criterion setRestriction(Restriction restriction) {
        this.restriction = restriction;
        return this;
    }

    public Test getTest() {
        return test;
    }

    public Criterion setTest(Test test) {
        this.test = test;
        return this;
    }

    public List<Criterion> getCriterion() {
        return criterion;
    }

    public Criterion setCriterion(List<Criterion> criterion) {
        this.criterion = criterion;
        return this;
    }
}
