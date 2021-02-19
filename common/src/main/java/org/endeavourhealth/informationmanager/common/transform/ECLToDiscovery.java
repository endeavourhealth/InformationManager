package org.endeavourhealth.informationmanager.common.transform;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.informationmanager.parser.*;

import java.util.UnknownFormatConversionException;

/**
 * Converts ECL to Discovery syntax, supporting commonly used constructs
 */
public class ECLToDiscovery extends ECLBaseVisitor<ClassExpression> {
   private Concept concept;
   private final ECLLexer lexer;
   private final ECLParser parser;
   private String ecl;
   public static final String SN = "sn:";
   public static final String ROLE_GROUP = "sn:609096000";

   public ECLToDiscovery(){
      this.lexer = new ECLLexer(null);
      this.parser= new ECLParser(null);
      this.parser.removeErrorListeners();
      this.parser.addErrorListener(new ECLErrorListener());
   }

   public ClassExpression getClassExpression(String ecl){
         this.ecl=ecl;
         lexer.setInputStream(CharStreams.fromString(ecl));
         CommonTokenStream tokens = new CommonTokenStream(lexer);
         parser.setTokenStream(tokens);
         ECLParser.ExpressionconstraintContext eclCtx= parser.expressionconstraint();
         return visitExpressionconstraint(eclCtx);
      }

   @Override public ClassExpression visitExpressionconstraint(ECLParser.ExpressionconstraintContext ctx) {
      return convertECContext(ctx);
   }

   private ClassExpression convertECContext(ECLParser.ExpressionconstraintContext ctx) {
      if (ctx.subexpressionconstraint() != null) {
         return convertSubECContext(ctx.subexpressionconstraint());
      } else if (ctx.compoundexpressionconstraint() != null) {
         if (ctx.compoundexpressionconstraint().disjunctionexpressionconstraint() != null) {
            return convertDisjunction(ctx.compoundexpressionconstraint().disjunctionexpressionconstraint());

         } else if (ctx.compoundexpressionconstraint().exclusionexpressionconstraint() != null) {
            return convertExclusion(ctx.compoundexpressionconstraint().exclusionexpressionconstraint());

         } else if (ctx.compoundexpressionconstraint().conjunctionexpressionconstraint() != null) {
            return convertConjunction(ctx.compoundexpressionconstraint().conjunctionexpressionconstraint());

         } else {
            throw new UnknownFormatConversionException("Unknown ECL format " + ecl);
         }

      } else if (ctx.refinedexpressionconstraint() != null) {
         return convertRefined(ctx.refinedexpressionconstraint());
      } else {
         throw new UnknownFormatConversionException(("unknown ECL layout "+ecl));
      }
   }

   private ClassExpression convertRefined(ECLParser.RefinedexpressionconstraintContext refined) {
      ClassExpression exp;
      if (refined.subexpressionconstraint().expressionconstraint() != null) {
         exp = convertECContext(refined.subexpressionconstraint().expressionconstraint());
      } else {
         exp = new ClassExpression();
         exp.addIntersection(convertSubECContext(refined.subexpressionconstraint()));
      }
      ECLParser.EclrefinementContext refinement = refined.eclrefinement();
      ECLParser.SubrefinementContext subref = refinement.subrefinement();
      if (subref.eclattributeset() != null) {
         convertAttributeSet(exp, subref.eclattributeset());
      } else if (subref.eclattributegroup()!=null) {
         convertAttributeGroup(exp, subref.eclattributegroup());
      } else
         throw new UnknownFormatConversionException("ECL attribute format not supported "+ecl);
      if (refinement.conjunctionrefinementset()!=null){
         for (ECLParser.SubrefinementContext subref2:refinement.conjunctionrefinementset().subrefinement()){
            convertAttributeGroup(exp,subref2.eclattributegroup());
         }
         return exp;

      } else
         return exp;
   }

   private ClassExpression convertAttributeSet(ClassExpression exp, ECLParser.EclattributesetContext eclAtSet) {
      if (eclAtSet.subattributeset() != null) {
         if (eclAtSet.subattributeset().eclattribute() != null) {
            exp.addIntersection(new ClassExpression()
                .setPropertyValue(convertAttribute(eclAtSet.subattributeset().eclattribute())));
            if (eclAtSet.conjunctionattributeset() != null) {
               convertAndAttributeSet(exp, eclAtSet.conjunctionattributeset());
               return exp;
            }
         }
      }
         throw new UnknownFormatConversionException("ECL Attribute format not supoorted "+ecl);
   }

   private ClassExpression convertAndAttributeSet(ClassExpression exp, ECLParser
              .ConjunctionattributesetContext eclAtAnd) {
      for (ECLParser.SubattributesetContext subAt : eclAtAnd.subattributeset()) {
         exp.addIntersection(new ClassExpression()
             .setPropertyValue(convertAttribute(subAt.eclattribute())));
      }
      return exp;
   }


   private ClassExpression convertConjunction(ECLParser.ConjunctionexpressionconstraintContext eclAnd) {
      ClassExpression exp= new ClassExpression();
      for (ECLParser.SubexpressionconstraintContext eclInter: eclAnd.subexpressionconstraint()){
         exp.addIntersection(convertSubECContext(eclInter));
      }
      return exp;
   }

   private ClassExpression convertExclusion(ECLParser.ExclusionexpressionconstraintContext eclExc) {
      ClassExpression exp = new ClassExpression();
      exp.addIntersection(convertSubECContext(eclExc.subexpressionconstraint().get(0)));
      exp.addIntersection(new ClassExpression()
          .setComplementOf(convertSubECContext(eclExc.
              subexpressionconstraint().get(1))));
      return exp;
   }

   private ClassExpression convertDisjunction(ECLParser.DisjunctionexpressionconstraintContext eclOr) {
      ClassExpression exp= new ClassExpression();
      for (ECLParser.SubexpressionconstraintContext eclUnion : eclOr.subexpressionconstraint()) {
         exp.addUnion(convertSubECContext(eclUnion));
      }
      return exp;
   }

   private PropertyValue convertAttribute(ECLParser.EclattributeContext attecl) {
      PropertyValue pv = new PropertyValue();
      pv.setQuantification(QuantificationType.SOME);
      pv.setProperty(getConRef(attecl.eclattributename()
              .subexpressionconstraint()
              .eclfocusconcept()
              .eclconceptreference()
              .conceptid(),
          attecl.eclattributename()
              .subexpressionconstraint()
              .constraintoperator()));
      if (attecl.expressioncomparisonoperator() != null) {
         if (attecl.expressioncomparisonoperator().EQUALS() != null) {
            if (attecl.subexpressionconstraint().eclfocusconcept() != null) {
               pv.setValueType(getConRef(attecl
                       .subexpressionconstraint().eclfocusconcept().eclconceptreference().conceptid(),
                   attecl.subexpressionconstraint().constraintoperator()));
               return pv;
            } else {
               throw new UnknownFormatConversionException("multi nested ECL not yest supported " + ecl);
            }
         } else {
            throw new UnknownFormatConversionException("unknown comparison type operator " + ecl);
         }
      } else {
         throw new UnknownFormatConversionException("unrecognised comparison operator " + ecl);
      }
   }


   private ConceptReference getConRef(ECLParser.ConceptidContext conceptId,
                                      ECLParser.ConstraintoperatorContext entailment){
      ConceptReference conRef= new ConceptReference(SN+ conceptId.getText());
      if (entailment==null) {
         conRef.setEntailment(EntailmentConstraint.SAME_AS);
         return conRef;
      } else if (entailment.descendantof()!=null) {
         conRef.setEntailment(EntailmentConstraint.DESCENDANT);
         return conRef;
      } else if (entailment.descendantorselfof()!=null){
         conRef.setEntailment(EntailmentConstraint.SAME_OR_DESCENDENT);
         return conRef;
      } else {
         throw new UnknownFormatConversionException("unrecognised entailment operator "+ ecl);
      }
   }

   private ClassExpression convertAttributeGroup(ClassExpression exp,
                                                 ECLParser.EclattributegroupContext eclGroup) {

      ClassExpression roleExp = new ClassExpression();
      exp.addIntersection(roleExp);
      PropertyValue roleGroup = new PropertyValue();
      roleGroup.setProperty(new ConceptReference(ROLE_GROUP));
      roleExp.setPropertyValue(roleGroup);
      ClassExpression subGroup = new ClassExpression();
      roleGroup.setExpression(subGroup);
      convertAttributeSet(subGroup, eclGroup.eclattributeset());
      return exp;
   }

   private ClassExpression convertSubECContext(ECLParser.SubexpressionconstraintContext eclSub) {

      if (eclSub.expressionconstraint()!=null) {
         return convertECContext(eclSub.expressionconstraint());
      } else {
         ClassExpression exp = new ClassExpression();
         ECLParser.ConstraintoperatorContext entailment = null;
         if (eclSub.constraintoperator() != null)
            entailment = eclSub.constraintoperator();
         if (eclSub.eclfocusconcept() != null) {
            exp.setClazz(getConRef(eclSub.eclfocusconcept().eclconceptreference().conceptid(),
                entailment));
            return exp;
         } else {
            throw new UnknownFormatConversionException("Unrecognised ECL subexpressionconstraint " + ecl);

         }
      }
   }



}
