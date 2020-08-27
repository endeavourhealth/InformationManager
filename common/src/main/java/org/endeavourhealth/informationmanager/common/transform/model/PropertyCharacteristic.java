package org.endeavourhealth.informationmanager.common.transform.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PropertyCharacteristic extends IMEntity{
    private Boolean isTransitive;
    private Boolean isReflexive;
    private Boolean isFunctional;
    private Boolean isSymmetric;

    @JsonProperty("IsTransitive")
    public Boolean getTransitive() {
        return isTransitive;
    }

    public PropertyCharacteristic setTransitive(Boolean transitive) {
        isTransitive = transitive;
        return this;
    }

    @JsonProperty("IsReflexive")
    public Boolean getReflexive() {
        return isReflexive;
    }

    public PropertyCharacteristic setReflexive(Boolean reflexive) {
        isReflexive = reflexive;
        return this;
    }

    @JsonProperty("IsFunctional")
    public Boolean getFunctional() {
        return isFunctional;
    }

    public PropertyCharacteristic setFunctional(Boolean functional) {
        isFunctional = functional;
        return this;
    }
    @JsonProperty("IsSymmetric")
    public Boolean getSymmetric() {
        return isSymmetric;
    }

    public PropertyCharacteristic setSymmetric(Boolean symmetric) {
        isSymmetric = symmetric;
        return this;
    }
}
