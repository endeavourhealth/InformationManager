package org.endeavourhealth.informationmanager.transforms;

public class CTV3Term {

    private String name;
    private String description;

    public CTV3Term() {
    }

    public CTV3Term(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public CTV3Term setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CTV3Term setDescription(String description) {
        this.description = description;
        return this;
    }
}
