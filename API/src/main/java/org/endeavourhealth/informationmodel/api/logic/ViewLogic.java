package org.endeavourhealth.informationmodel.api.logic;

import org.endeavourhealth.informationmodel.api.database.InformationModelDAL;
import org.endeavourhealth.informationmodel.api.models.Concept;
import org.endeavourhealth.informationmodel.api.models.View;

import java.util.List;

public class ViewLogic {
    InformationModelDAL _dal = new InformationModelDAL();

    public void addView(View view) {
        _dal.addView(view);
    }

    public View getView(Long viewId) {
        return _dal.getView(viewId);
    }

    public List<Concept> getChildren(Long viewId, Long relationshipId) {
        return _dal.getChildren(viewId, relationshipId);
    }

    public void moveConceptUp(Long conceptId) {
        _dal.moveViewConceptUp(conceptId);
    }

    public void moveConceptDown(Long conceptId) {
        _dal.moveViewConceptDown(conceptId);
    }

}
