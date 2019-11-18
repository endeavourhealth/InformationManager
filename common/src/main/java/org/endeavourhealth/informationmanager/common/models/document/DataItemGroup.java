package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class DataItemGroup {
    private String clazz;
    private List<Item> item = new ArrayList<>();
    private Criterion criterion;

    public String getClazz() {
        return clazz;
    }

    public DataItemGroup setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public List<Item> getItem() {
        return item;
    }

    public DataItemGroup setItem(List<Item> item) {
        this.item = item;
        return this;
    }

    public Criterion getCriterion() {
        return criterion;
    }

    public DataItemGroup setCriterion(Criterion criterion) {
        this.criterion = criterion;
        return this;
    }
}
