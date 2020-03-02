package org.endeavourhealth.informationmanager.common.models.definitionTypes;

import org.endeavourhealth.informationmanager.common.models.DBEntity;

public class SimpleConcept extends DBEntity<SimpleConcept> {
    private String concept;
    private String operator;
    private boolean inferred;

    public String getConcept() {
        return concept;
    }

    public SimpleConcept setConcept(String concept) {
        this.concept = concept;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public SimpleConcept setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public boolean isInferred() {
        return inferred;
    }

    public SimpleConcept setInferred(boolean inferred) {
        this.inferred = inferred;
        return this;
    }
}
