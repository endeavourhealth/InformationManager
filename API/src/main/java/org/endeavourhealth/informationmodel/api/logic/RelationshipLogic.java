package org.endeavourhealth.informationmodel.api.logic;

import org.endeavourhealth.informationmodel.api.database.InformationModelDAL;
import org.endeavourhealth.informationmodel.api.models.Concept;
import org.endeavourhealth.informationmodel.api.models.ConceptRelationship;

import java.util.List;

public class RelationshipLogic {
    InformationModelDAL _dal = new InformationModelDAL();

    public void addRelationship(ConceptRelationship conceptRelationship) throws Exception {
        _dal.addRelationship(conceptRelationship);
    }

    public List<ConceptRelationship> getTargetConcepts(Long conceptId, Long relationshipId) {
        return _dal.getTargetConcepts(conceptId, relationshipId);
    }

    public List<ConceptRelationship> getRelatedConcepts(Long conceptId, Long relationshipId) {
        return _dal.getRelatedConcepts(conceptId, relationshipId);
    }
}
