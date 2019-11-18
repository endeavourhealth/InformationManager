package org.endeavourhealth.informationmanager.common.models.document;

import java.util.ArrayList;
import java.util.List;

public class Synonym {
    private String status;
    private List<String> term = new ArrayList<>();
    private String concept;

    public String getStatus() {
        return status;
    }

    public Synonym setStatus(String status) {
        this.status = status;
        return this;
    }

    public List<String> getTerm() {
        return term;
    }

    public Synonym setTerm(List<String> term) {
        this.term = term;
        return this;
    }

    public String getConcept() {
        return concept;
    }

    public Synonym setConcept(String concept) {
        this.concept = concept;
        return this;
    }
}
