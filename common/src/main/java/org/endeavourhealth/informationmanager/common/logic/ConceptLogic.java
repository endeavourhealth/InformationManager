package org.endeavourhealth.informationmanager.common.logic;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.models.document.ConceptDefinition;
import org.endeavourhealth.informationmanager.common.models.document.ConceptExpression;
import org.endeavourhealth.informationmanager.common.models.document.Concept;
import org.endeavourhealth.informationmanager.common.models.ConceptTreeNode;

import java.util.*;

public class ConceptLogic {
    private InformationManagerDAL dal;

    public ConceptLogic(InformationManagerDAL dal) {
        this.dal = dal;
    }

    public List<ConceptTreeNode> getParentTree(String conceptId) throws Exception {
        List<ConceptTreeNode> result = new ArrayList<>();
        Concept con = this.dal.getConceptSummary(conceptId);
        result.add(new ConceptTreeNode()
        .setId(con.getId())
        .setName(con.getName()));


        ConceptDefinition def = this.dal.getConceptDefinition(conceptId);
        while (def != null && def.getSubtypeOf() != null && def.getSubtypeOf().size() > 0) {
            Optional<ConceptExpression> first = def.getSubtypeOf().stream().filter(e -> e.getConcept() != null).findFirst();
            if (first.isPresent()) {
                con = this.dal.getConceptSummary(first.get().getConcept());

                ConceptTreeNode node = new ConceptTreeNode()
                    .setId(con.getId())
                    .setName(con.getName())
                    .setChildren(result);

                result = Collections.singletonList(node);

                def = this.dal.getConceptDefinition(con.getId());

            } else
                def = null;
        }

        return result;
    }
}
