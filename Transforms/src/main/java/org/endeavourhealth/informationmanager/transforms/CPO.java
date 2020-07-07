package org.endeavourhealth.informationmanager.transforms;

public class CPO {
    private String concept;
    private String property;
    private String object;

    public CPO(String concept, String property, String object) {
        this.concept = concept;
        this.property = property;
        this.object = object;
    }

    public String getConcept() {
        return concept;
    }

    public CPO setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public String getProperty() {
        return property;
    }

    public CPO setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getObject() {
        return object;
    }

    public CPO setObject(String object) {
        this.object = object;
        return this;
    }
}
