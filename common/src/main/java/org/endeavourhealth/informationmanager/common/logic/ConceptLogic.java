package org.endeavourhealth.informationmanager.common.logic;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.models.ConceptAxiom;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.Definition;

import java.util.*;

public class ConceptLogic {
    private InformationManagerDAL dal;

    public ConceptLogic(InformationManagerDAL dal) {
        this.dal = dal;
    }

    public Collection<ConceptAxiom> getConceptAxioms(String conceptIri) throws Exception {
        List<ConceptAxiom> result = new ArrayList<>();
        int conceptId = dal.getConceptId(conceptIri);

        addClassExpressionDefinitions(conceptId, "SubClassOf", result);
        addClassExpressionDefinitions(conceptId, "EquivalentTo", result);
        addAxiomSupertypes(conceptId, "SubPropertyOf", result);
        addAxiomSupertypes(conceptId, "InversePropertyOf", result);
        addAxiomSupertypes(conceptId, "MappedTo", result);
        addAxiomSupertypes(conceptId, "ReplacedBy", result);
        addAxiomSupertypes(conceptId, "ChildOf", result);
        addPropertyRanges(conceptId, result);
        addPropertyDomains(conceptId, result);
        addPropertyChains(conceptId, result);

        return result;
    }

    private void addClassExpressionDefinitions(int conceptId, String axiom, List<ConceptAxiom> conceptAxioms) throws Exception {
        ConceptAxiom conceptAxiom = new ConceptAxiom().setToken(axiom);
        
        conceptAxiom.addDefinitions(dal.getAxiomSupertypes(conceptId, axiom));
        conceptAxiom.addDefinitions(dal.getAxiomRoleGroups(conceptId, axiom));

        if (conceptAxiom.getDefinitions().size() > 0)
            conceptAxioms.add(conceptAxiom);
    }

    private void addAxiomSupertypes(int conceptId, String axiom, List<ConceptAxiom> conceptAxioms) throws Exception {
        Collection<Definition> definitions = dal.getAxiomSupertypes(conceptId, axiom);
        if (definitions.size() > 0)
            conceptAxioms.add(new ConceptAxiom().setToken(axiom).addDefinitions(definitions));
    }

    private void addPropertyRanges(int conceptId, List<ConceptAxiom> conceptAxioms) throws Exception {
        Collection<Definition> definitions = dal.getPropertyRanges(conceptId);

        if (definitions.size() > 0)
            conceptAxioms.add(new ConceptAxiom().setToken("PropertyRange").addDefinitions(definitions));
    }

    private void addPropertyDomains(int conceptId, List<ConceptAxiom> conceptAxioms) throws Exception {
        Collection<Definition> definitions = dal.getPropertyDomains(conceptId);

        if (definitions.size() > 0)
            conceptAxioms.add(new ConceptAxiom().setToken("PropertyDomain").addDefinitions(definitions));
    }

    private void addPropertyChains(int conceptId, List<ConceptAxiom> conceptAxioms) throws Exception {
        Collection<Definition> definitions = dal.getPropertyChains(conceptId);

        if (definitions.size() > 0)
            conceptAxioms.add(new ConceptAxiom().setToken("PropertyChain").addDefinitions(definitions));
    }
}
