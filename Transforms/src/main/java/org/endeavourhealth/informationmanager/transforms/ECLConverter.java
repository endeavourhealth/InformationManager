package org.endeavourhealth.informationmanager.transforms;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.endeavourhealth.informationmanager.common.transform.model.ClassAxiom;
import org.endeavourhealth.informationmanager.common.transform.model.ClassExpression;
import org.endeavourhealth.informationmanager.common.transform.model.OPECardinalityRestriction;
import org.endeavourhealth.informationmanager.common.transform.model.PropertyAxiom;
import org.snomed.langauges.ecl.ECLObjectFactory;
import org.snomed.langauges.ecl.ECLQueryBuilder;
import org.snomed.langauges.ecl.domain.expressionconstraint.CompoundExpressionConstraint;
import org.snomed.langauges.ecl.domain.expressionconstraint.ExpressionConstraint;
import org.snomed.langauges.ecl.domain.expressionconstraint.RefinedExpressionConstraint;
import org.snomed.langauges.ecl.domain.expressionconstraint.SubExpressionConstraint;
import org.snomed.langauges.ecl.domain.refinement.*;

import java.util.ArrayList;
import java.util.List;

public class ECLConverter {

    private ECLQueryBuilder eqbuilder= new ECLQueryBuilder(new ECLObjectFactory());



    public ClassExpression getClassExpression(String ecl) throws InvalidArgumentException {

        ClassExpression classEx = new ClassExpression();
        ExpressionConstraint cons = eqbuilder.createQuery(ecl);
        if (cons instanceof SubExpressionConstraint) {
            addSubConstraint(classEx,cons);
        } else if (cons instanceof CompoundExpressionConstraint) {
            CompoundExpressionConstraint compex = (CompoundExpressionConstraint) cons;
            if (compex.getDisjunctionExpressionConstraints() != null)
                for (SubExpressionConstraint or : compex.getDisjunctionExpressionConstraints()) {
                    ClassExpression union= new ClassExpression();
                    classEx.addUnion(union);
                    addSubConstraint(union,or);

                }
        }

        else {
            System.err.println("Invalid ECL");
        }

        return classEx;
    }

    private void addSubConstraint(ClassExpression classEx, ExpressionConstraint cons) {
        SubExpressionConstraint subex = (SubExpressionConstraint) cons;
        if (subex.getOperator() == Operator.descendantorselfof)
            classEx.setClazz(RF2.IRI_PREFIX + subex.getConceptId());
        else if (subex.getOperator() == Operator.descendantof) {
            //Range of class has to exclude and instance of itself!
            ClassExpression inter1 = new ClassExpression();
            classEx.addIntersection(inter1);
            inter1.setClazz(RF2.IRI_PREFIX + subex.getConceptId());
            ClassExpression inter2 = new ClassExpression();
            classEx.addIntersection(inter2);
            ClassExpression negate = new ClassExpression();
            inter2.setComplementOf(negate);
            negate.addObjectOneOf(RF2.IRI_PREFIX + subex.getConceptId());
        }
        else if (subex.getOperator()==Operator.memberOf) {
            OPECardinalityRestriction ope= new OPECardinalityRestriction();
            ope.setProperty(RF2.MEMBER_OF);
            ope.setQuantification("some");
            ope.setClazz(RF2.IRI_PREFIX+ subex.getConceptId());

        }
        else if (subex.getConceptId()!=null) {
            ClassExpression oneOf = new ClassExpression();
            classEx.addObjectOneOf(RF2.IRI_PREFIX + subex.getConceptId());
        }
        else {
            System.err.println("Unrecognised ECL type");
        }
    }

}




