package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.ClassExpression;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ObjectPropertyValue;
import org.endeavourhealth.imapi.model.QuantificationType;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.util.SimpleRenderer;
import org.snomed.langauges.ecl.ECLObjectFactory;
import org.snomed.langauges.ecl.ECLQueryBuilder;
import org.snomed.langauges.ecl.domain.expressionconstraint.CompoundExpressionConstraint;
import org.snomed.langauges.ecl.domain.expressionconstraint.ExpressionConstraint;
import org.snomed.langauges.ecl.domain.expressionconstraint.SubExpressionConstraint;
import org.snomed.langauges.ecl.domain.refinement.Operator;

public class ECLToDiscovery {
    public static final String IRI_PREFIX = "sn:";
    public static final String MEMBER_OF = "sn:394852005";
    private ECLQueryBuilder eqbuilder= new ECLQueryBuilder(new ECLObjectFactory());


    /**xzzxczxc
     * Creates a class expression in Discovery syntax from expression constraint syntax
     * @param ecl
     * @return
     */
    public ClassExpression getClassExpression(String ecl) {

        ClassExpression classEx = new ClassExpression();
        ExpressionConstraint cons = eqbuilder.createQuery(ecl);
        return convertToclassExpression(classEx, cons);
    }
    private ClassExpression convertToclassExpression(ClassExpression classEx, ExpressionConstraint cons){
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
            if (compex.getConjunctionExpressionConstraints() != null)
                for (SubExpressionConstraint and : compex.getConjunctionExpressionConstraints()) {
                    ClassExpression inter= new ClassExpression();
                    classEx.addIntersection(inter);
                    addSubConstraint(inter,and);

                }
            //Exclusion at this level means must be an intersection
            if (compex.getExclusionExpressionConstraint() != null){
                ClassExpression inter= new ClassExpression();
                classEx.addIntersection(inter);
                ClassExpression exclude= new ClassExpression();
                inter.setComplementOf(exclude);
                addSubConstraint(exclude,compex.getExclusionExpressionConstraint());
            }
        }

        else {
            System.err.println("Invalid ECL");
        }

        return classEx;
    }

    private void addSubConstraint(ClassExpression classEx, ExpressionConstraint cons) {
        SubExpressionConstraint subex = (SubExpressionConstraint) cons;
        if (subex.getNestedExpressionConstraint()!=null){
            classEx= convertToclassExpression(classEx,subex.getNestedExpressionConstraint());
            return;
        }
        if (subex.getOperator() == Operator.descendantorselfof)
            classEx.setClazz(new ConceptReference(IRI_PREFIX + subex.getConceptId()));
        else if (subex.getOperator() == Operator.descendantof) {
            //Range of class has to exclude and instance of itself!
            ClassExpression inter1 = new ClassExpression();
            classEx.addIntersection(inter1);
            inter1.setClazz(new ConceptReference(IRI_PREFIX + subex.getConceptId()));
            ClassExpression inter2 = new ClassExpression();
            classEx.addIntersection(inter2);
            ClassExpression negate = new ClassExpression();
            inter2.setComplementOf(negate);
            negate.addObjectOneOf(IRI_PREFIX + subex.getConceptId());
        }
        else if (subex.getOperator()==Operator.memberOf) {
            ObjectPropertyValue ope= new ObjectPropertyValue();
            ope.setProperty(new ConceptReference(MEMBER_OF));
            ope.setQuantification(QuantificationType.SOME);
            ope.setValueType(new ConceptReference(IRI_PREFIX+ subex.getConceptId()));

        }
        else if (subex.getConceptId()!=null) {
            classEx.addObjectOneOf(IRI_PREFIX + subex.getConceptId());
        }
        else {
            System.err.println("Unrecognised ECL type");
        }
    }

    public OWLClassExpression getClassExpressionAsOWL(String ecl){
        ClassExpression cex= getClassExpression(ecl);
        DiscoveryToOWL converter= new DiscoveryToOWL();
        converter.addNamespace("sn:","http://snomed.info/sct#");
        return converter.getClassExpressionAsOWLClassExpression(cex);
    }

    public String getClassExpressionAsFS(String ecl){
        ClassExpression cex= getClassExpression(ecl);
        DiscoveryToOWL converter= new DiscoveryToOWL();
        converter.addNamespace("sn:","http://snomed.info/sct#");
        OWLClassExpression owlEx= converter.getClassExpressionAsOWLClassExpression(cex);
        SimpleRenderer render= new SimpleRenderer();
        render.setPrefix("sn:","http://snomed.info/sct#");
        String outString = render.render(owlEx);
        return outString;

    }


}
