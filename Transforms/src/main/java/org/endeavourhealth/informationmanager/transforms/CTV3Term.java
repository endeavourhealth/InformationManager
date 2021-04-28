package org.endeavourhealth.informationmanager.transforms;

/**
 * @Deprecated
 * Suppliers provide these codes as mapped to Snomed
 * IT may not be necessary to create concepts for coded terms that point to snomed
 */
@Deprecated
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
