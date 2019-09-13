package org.endeavourhealth.informationmanager.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConceptProperty {
    private String property;
    private String value;
    private String concept;
    private String inherits;


    public String getProperty() {
        return property;
    }

    public ConceptProperty setProperty(String property) {
        this.property = property;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ConceptProperty setValue(String value) {
        this.value = value;
        return this;
    }

    public String getConcept() {
        return concept;
    }

    public ConceptProperty setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public String getInherits() {
        return inherits;
    }

    public ConceptProperty setInherits(String inherits) {
        this.inherits = inherits;
        return this;
    }
}
