package org.endeavourhealth.informationmanager.common.logic;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.models.ConceptDefinition;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.ClassExpression;

public class ConceptLogic {
    private InformationManagerDAL dal;

    public ConceptLogic(InformationManagerDAL dal) {
        this.dal = dal;
    }

    public ConceptDefinition getConceptDefinition(String conceptIri) throws Exception {
        int conceptId = dal.getConceptId(conceptIri);

        return new ConceptDefinition()
            .setSubClassOf(getConceptAxiomClassExpression(conceptId, "SubClassOf"))
            .setEquivalentTo(getConceptAxiomClassExpression(conceptId, "EquivalentTo"))
            .setSubPropertyOf(dal.getAxiomSupertypes(conceptId, "SubPropertyOf"))
            .setInversePropertyOf(dal.getAxiomSupertypes(conceptId, "InversePropertyOf"))
            .setMappedTo(dal.getAxiomSupertypes(conceptId, "MappedTo"))
            .setReplacedBy(dal.getAxiomSupertypes(conceptId, "ReplacedBy"))
            .setChildOf(dal.getAxiomSupertypes(conceptId, "ChildOf"))
            .setPropertyRange(dal.getPropertyRanges(conceptId))
            .setPropertyDomain(dal.getPropertyDomains(conceptId))
            .setPropertyChain(dal.getPropertyChains(conceptId))
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
