package org.endeavourhealth.informationmanager.common.transform;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.informationmanager.parser.OWLFSBaseVisitor;
import org.endeavourhealth.informationmanager.parser.OWLFSLexer;
import org.endeavourhealth.informationmanager.parser.OWLFSParser;
import org.endeavourhealth.informationmanager.parser.OWLFSVisitor;

   /**
    * Converts Functional syntax to Endeavour/ Discovery syntax using an ANTLR parser
    */
   public class OWLFSToDiscovery extends OWLFSBaseVisitor {
      private Concept concept;
      private OWLFSLexer lexer;
      private OWLFSParser parser;

      public OWLFSToDiscovery(){
         this.lexer = new OWLFSLexer(null);
         this.parser= new OWLFSParser(null);
      }

      /**
       * parses an owl functional syntax string to populate an Endeavour/Discovery concept
       * Note that the concept must already have been created with an IRI and consequently the subclass/ sub property expressions in OWL are skipped
       * @param concept  the pre created concept
       * @param owl  string of owl functional syntax containing a single axiom
       * @return
       */
      public Concept convertAxiom(Concept concept, String owl){

         this.concept= concept;
         lexer.setInputStream(CharStreams.fromString(owl));
         CommonTokenStream tokens = new CommonTokenStream(lexer);
         parser.setTokenStream(tokens);
         OWLFSParser.AxiomContext axiomCtx= parser.axiom();
         visitAxiom(axiomCtx);
         return concept;

      }
      @Override public Concept visitAxiom(OWLFSParser.AxiomContext ctx) {
         if (ctx.subClassOf()!=null)
            return visitSubClassOf(ctx.subClassOf());
         else if (ctx.equivalentClasses()!=null)
            return visitEquivalentClasses(ctx.equivalentClasses());
         else if (ctx.subObjectPropertyOf()!=null)
            return visitSubObjectPropertyOf(ctx.subObjectPropertyOf());
         else if (ctx.reflexiveObjectProperty()!=null){
            concept.setIsReflexive(new Axiom());
         } else if (ctx.transitiveObjectProperty()!=null){
            concept.setIsTransitive(new Axiom());
         }

         return concept;
      }
      @Override
      public Concept visitSubClassOf(OWLFSParser.SubClassOfContext ctx) {
         if (isGCI(ctx))
            return concept;
         ClassExpression superClass= new ClassExpression();
         concept.addSubClassOf(superClass);
         convertClassExpression(ctx.superClass().classExpression(),superClass);
         return concept;
      }
      @Override public Concept visitEquivalentClasses(OWLFSParser.EquivalentClassesContext ctx) {
         ClassExpression superClass= new ClassExpression();
         concept.addEquivalentTo(superClass);
         convertClassExpression(ctx.classExpression().get(1),superClass);
         return concept;
      }

      @Override public Concept visitSubObjectPropertyOf(OWLFSParser.SubObjectPropertyOfContext ctx) {

         if (ctx.subObjectPropertyExpression().propertyExpressionChain()!=null)
            convertPropertyChain(ctx.subObjectPropertyExpression().propertyExpressionChain(),concept);
         else {
            PropertyAxiom pax = new PropertyAxiom();

            concept.addSubObjectPropertyOf(pax);
            pax.setProperty(new ConceptReference(ctx.superObjectPropertyExpression()
                .objectPropertyExpression()
                .objectProperty()
                .iri()
                .getText()));
         }
         return concept;
      }

      private void convertPropertyChain(OWLFSParser.PropertyExpressionChainContext chainContext, Concept op) {
         SubPropertyChain chain = new SubPropertyChain();
         op.addSubPropertyChain(chain);
         for (OWLFSParser.ObjectPropertyExpressionContext opcs:chainContext.objectPropertyExpression()){
            chain.addProperty(new ConceptReference(opcs.objectProperty().iri().getText()));
         }

      }

      private void convertClassExpression(OWLFSParser.ClassExpressionContext ctx, ClassExpression exp) {
         if (ctx.iri()!=null)
            exp.setClazz(new ConceptReference(ctx.getText()));
         else if (ctx.objectIntersectionOf()!=null){
            for (OWLFSParser.ClassExpressionContext ctxInter:ctx.objectIntersectionOf().classExpression()){
               ClassExpression inter= new ClassExpression();
               exp.addIntersection(inter);
               convertClassExpression(ctxInter,inter);
            }
         } else if (ctx.objectSomeValuesFrom()!=null){
            PropertyValue opv= new PropertyValue();
            exp.setPropertyValue(opv);
            opv.setProperty(new ConceptReference(ctx.objectSomeValuesFrom()
                .objectPropertyExpression()
                .objectProperty()
                .iri()
                .getText()));
            if (ctx.objectSomeValuesFrom().classExpression().objectIntersectionOf()!=null){
               ClassExpression propExp = new ClassExpression();
               opv.setExpression(propExp);
               convertClassExpression(ctx.objectSomeValuesFrom().classExpression(),propExp);
            } else if (ctx.objectSomeValuesFrom().classExpression().objectSomeValuesFrom()!=null){
               ClassExpression propExp= new ClassExpression();
               opv.setExpression(propExp);
               convertClassExpression(ctx.objectSomeValuesFrom().classExpression(),propExp);
            }
            else {
               opv.setValueType(new ConceptReference(ctx.objectSomeValuesFrom()
                   .classExpression().iri().getText()));
            }

         }
      }




      private boolean isGCI(OWLFSParser.SubClassOfContext ctx) {
         if (ctx.subClass().classExpression().objectIntersectionOf()!=null)
            return true;
         else return false;
      }

   }
