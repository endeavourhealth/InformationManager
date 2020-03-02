package org.endeavourhealth.informationmanager.common.models;

public class DBEntity<T> {
    private int id;

    public int getId() {
        return id;
    }

    public T setId(int id) {
        this.id = id;
        return (T)this;
    }
}
