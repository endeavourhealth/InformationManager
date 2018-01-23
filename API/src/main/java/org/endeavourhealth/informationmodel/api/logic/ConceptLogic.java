package org.endeavourhealth.informationmodel.api.logic;

import org.endeavourhealth.informationmodel.api.database.InformationModelDAL;
import org.endeavourhealth.informationmodel.api.models.Category;
import org.endeavourhealth.informationmodel.api.models.Concept;
import org.endeavourhealth.informationmodel.api.models.ConceptValueRange;

import java.util.ArrayList;
import java.util.List;

public class ConceptLogic {
    InformationModelDAL _dal = new InformationModelDAL();

    public Concept saveConcept(Concept concept) throws Exception {
        return _dal.saveConcept(concept);
    }

    public Concept getConcept(Long conceptId) {
        return _dal.getConcept(conceptId);
    }

    public void addConceptValueRange(ConceptValueRange conceptValueRange){
        _dal.addConceptValueRange(conceptValueRange);
    }

    public List<ConceptValueRange> loadConceptValueRanges(Long conceptId, Long qualifierId) {
        return _dal.loadConceptValueRanges(conceptId, qualifierId);
    }

    public String getName(Long conceptId) {
        Concept concept = _dal.getConcept(conceptId);
        if (concept != null)
            return concept.getName();

        return null;
    }

    public String getContextName(Long conceptId) {
        Concept concept = _dal.getConcept(conceptId);
        if (concept != null)
            return concept.getContextName();

        return null;
    }

    public List<Concept> search(String term, Long classId) {
       return _dal.searchConcepts(term, classId);
    }

    public List<Concept> list(List<Integer> categoryIds, Integer page, Integer size, String filter) {
        List<Category> categories = new ArrayList<>();

        for (Integer categoryId : categoryIds)
            categories.add(Category.getById(categoryId));

        return _dal.listConcepts(categories, page, size, filter);
    }

    public Integer count(Category category, String filter) {
        return _dal.countConcepts(category, filter);
    }

}
