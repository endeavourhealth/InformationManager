package org.endeavourhealth.informationmanager.scratch.grix;

import java.util.ArrayList;
import java.util.List;

public class RelType {
    private String id;
    protected List<Relation> relationList = new ArrayList<>();

    public RelType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public RelType setId(String id) {
        this.id = id;
        return this;
    }
}
