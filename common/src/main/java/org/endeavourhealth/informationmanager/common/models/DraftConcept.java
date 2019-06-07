package org.endeavourhealth.informationmanager.common.models;

import java.util.Date;

public class DraftConcept {
    private String name;
    private String id;
    private String published;
    private Date updated;

    public String getName() {
        return name;
    }

    public DraftConcept setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public DraftConcept setId(String id) {
        this.id = id;
        return this;
    }

    public String getPublished() {
        return published;
    }

    public DraftConcept setPublished(String published) {
        this.published = published;
        return this;
    }

    public Date getUpdated() {
        return updated;
    }

    public DraftConcept setUpdated(Date updated) {
        this.updated = updated;
        return this;
    }
}
