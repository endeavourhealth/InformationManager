package org.endeavourhealth.informationmanager.common.transform.model;

import java.util.ArrayList;
import java.util.List;

public class ClassExpressionConstraint {
    private String operator;
    private List<SubsumptionConcept> concept;

    public String getOperator() {
        return operator;
    }

    public ClassExpressionConstraint setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public List<SubsumptionConcept> getConcept() {
        return concept;
    }

    public ClassExpressionConstraint setConcept(List<SubsumptionConcept> concept) {
        this.concept = concept;
        return this;
    }

    public ClassExpressionConstraint addConcept(SubsumptionConcept concept) {
        if (this.concept == null)
            this.concept = new ArrayList<>();

        this.concept.add(concept);
        return this;
    }
}
