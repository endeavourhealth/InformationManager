package org.endeavourhealth.informationmanager.common.logic;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.models.ConceptAxiom;

import java.util.*;

public class ConceptLogic {
    private InformationManagerDAL dal;

    public ConceptLogic(InformationManagerDAL dal) {
        this.dal = dal;
    }

    public Collection<ConceptAxiom> getConceptAxioms(String conceptIri) throws Exception {
        List<ConceptAxiom> result = new ArrayList<>();
        Integer conceptId = dal.getConceptId(conceptIri);

        addClassExpressionDefinitions(conceptId, "SubClassOf", result);
        addClassExpressionDefinitions(conceptId, "EquivalentTo", result);

        return result;
    }

    private void addClassExpressionDefinitions(Integer conceptId, String axiom, List<ConceptAxiom> conceptAxioms) throws Exception {
        ConceptAxiom conceptAxiom = new ConceptAxiom().setToken(axiom);
        
        conceptAxiom.addDefinitions(dal.getAxiomSupertypes(conceptId, axiom));
        conceptAxiom.addDefinitions(dal.getAxiomRoleGroups(conceptId, axiom));

        if (conceptAxiom.getDefinitions().size() > 0)
            conceptAxioms.add(conceptAxiom);
    }
}
