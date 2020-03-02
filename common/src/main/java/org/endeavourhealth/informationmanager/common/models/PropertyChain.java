package org.endeavourhealth.informationmanager.common.models;

public class PropertyChain {

    private Integer concept;
    private Integer linkNumber;
    private Integer linkProperty;

    public Integer getConcept() {
        return concept;
    }

    public PropertyChain setConcept(Integer concept) {
        this.concept = concept;
        return this;
    }

    public Integer getLinkNumber() {
        return linkNumber;
    }

    public PropertyChain setLinkNumber(Integer linkNumber) {
        this.linkNumber = linkNumber;
        return this;
    }

    public Integer getLinkProperty() {
        return linkProperty;
    }

    public PropertyChain setLinkProperty(Integer linkProperty) {
        this.linkProperty = linkProperty;
        return this;
    }

}
