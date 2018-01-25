package org.endeavourhealth.informationmodel.api.logic;

import org.endeavourhealth.informationmodel.api.database.InformationModelDAL;
import org.endeavourhealth.informationmodel.api.models.AttributePrimitiveValue;
import org.endeavourhealth.informationmodel.api.models.Concept;
import org.endeavourhealth.informationmodel.api.models.ConceptAttribute;

import java.util.List;


public class AttributeLogic {
    InformationModelDAL _dal = new InformationModelDAL();

    public List<ConceptAttribute> getConceptAttributes(Long conceptId, Long attributeId) {
        return _dal.getConceptAttributes(conceptId, attributeId);
    }
    public void saveAttributeConceptValue(ConceptAttribute conceptAttribute) {
        _dal.addConceptAttribute(conceptAttribute);
    }

    public List<Concept> getConceptValueAttributes(Long conceptId, Long classId) {
        return _dal.getAttributeConceptValues(conceptId, classId);
    }

    public void saveAttributePrimitiveValue(AttributePrimitiveValue attributePrimitiveValue) {
        _dal.addAttributePrimitiveValue(attributePrimitiveValue);
    }

    public List<AttributePrimitiveValue> getPrimitiveValueAttributes(Long conceptId, Long classId) {
        return _dal.getPrimitiveValueAttributes(conceptId, classId);
    }
}
