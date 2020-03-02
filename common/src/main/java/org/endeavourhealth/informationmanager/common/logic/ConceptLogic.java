package org.endeavourhealth.informationmanager.common.logic;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.models.ConceptDefinition;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.ClassExpression;

public class ConceptLogic {
    private InformationManagerDAL dal;

    public ConceptLogic(InformationManagerDAL dal) {
        this.dal = dal;
    }

    public ConceptDefinition getConceptDefinition(int id) throws Exception {
        return new ConceptDefinition()
            .setSubClassOf(getConceptAxiomClassExpression(id, "SubClassOf"))
            .setEquivalentTo(getConceptAxiomClassExpression(id, "EquivalentTo"))
            .setSubPropertyOf(dal.getAxiomSupertypes(id, "SubPropertyOf"))
            .setInversePropertyOf(dal.getAxiomSupertypes(id, "InversePropertyOf"))
            .setMappedTo(dal.getAxiomSupertypes(id, "MappedTo"))
            .setReplacedBy(dal.getAxiomSupertypes(id, "ReplacedBy"))
            .setChildOf(dal.getAxiomSupertypes(id, "ChildOf"))
            .setPropertyRange(dal.getPropertyRanges(id))
            .setPropertyDomain(dal.getPropertyDomains(id))
            .setPropertyChain(dal.getPropertyChains(id))
            ;
    }

    private ClassExpression getConceptAxiomClassExpression(int conceptId, String axiom) throws Exception {
        ClassExpression result = new ClassExpression()
            .setSuperTypes(dal.getAxiomSupertypes(conceptId, axiom))
            .setRoleGroups(dal.getAxiomRoleGroups(conceptId, axiom));

        if (result.getSuperTypes() != null || result.getRoleGroups() != null)
            return result;
        else
            return null;
    }
}
