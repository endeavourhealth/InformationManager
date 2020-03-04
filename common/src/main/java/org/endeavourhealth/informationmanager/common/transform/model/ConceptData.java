package org.endeavourhealth.informationmanager.common.transform.model;

public class ConceptData {
    private Concept concept = new Concept();
    private ConceptAxiom conceptAxiom;


    public Concept getConcept() {
        return concept;
    }

    public ConceptData setConcept(Concept concept) {
        this.concept = concept;
        return this;
    }

    public ConceptAxiom getConceptAxiom() {
        if (conceptAxiom == null)
            conceptAxiom = new ConceptAxiom().setConcept(concept.getIri());
        return conceptAxiom;
    }

    public ConceptData setConceptAxiom(ConceptAxiom conceptAxiom) {
        this.conceptAxiom = conceptAxiom;
        return this;
    }
}
