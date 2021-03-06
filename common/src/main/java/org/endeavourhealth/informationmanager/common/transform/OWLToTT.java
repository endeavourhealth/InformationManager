package org.endeavourhealth.informationmanager.common.transform;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.parser.owlfs.OWLFSBaseVisitor;
import org.endeavourhealth.imapi.parser.owlfs.OWLFSLexer;
import org.endeavourhealth.imapi.parser.owlfs.OWLFSParser;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

/**
 * Converts Functional syntax to Endeavour/ Discovery syntax using an ANTLR parser
 */
public class OWLToTT extends OWLFSBaseVisitor {
   private TTEntity entity;
   private OWLFSLexer lexer;
   private OWLFSParser parser;
   private TTContext context;

   public OWLToTT(){
      this.lexer = new OWLFSLexer(null);
      this.parser= new OWLFSParser(null);
   }

   /**
    * parses an owl functional syntax string to populate an Endeavour/Discovery entity
    * Note that the entity must already have been created with an IRI and consequently the subclass/ sub property expressions in OWL are skipped
    * @param entity  the pre created entity
    * @param owl  string of owl functional syntax containing a single axiom
    * @param context Context object containing the prefixes and namespaces used in the owl string
    */
   public void convertAxiom(TTEntity entity,String owl, TTContext context){

      this.entity = entity;
      this.context = context;
      lexer.setInputStream(CharStreams.fromString(owl));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      OWLFSParser.AxiomContext axiomCtx= parser.axiom();
      visitAxiom(axiomCtx);

   }

   private void addType(TTEntity entity, TTIriRef type){
      if (entity.get(RDF.TYPE)==null){
         TTArray types= new TTArray();
         entity.set(RDF.TYPE,types);
      } else {
         TTArray types = entity.get(RDF.TYPE).asArray();
         types.add(type);
      }
   }

   @Override
   public Object visitAxiom(OWLFSParser.AxiomContext ctx) {
      if (ctx.subClassOf()!=null)
         return visitSubClassOf(ctx.subClassOf());
      else if (ctx.equivalentClasses()!=null)
         return visitEquivalentClasses(ctx.equivalentClasses());
      else if (ctx.subObjectPropertyOf()!=null)
         return visitSubObjectPropertyOf(ctx.subObjectPropertyOf());
      else if (ctx.reflexiveObjectProperty()!=null){
         addType(entity, OWL.REFLEXIVE);
      } else if (ctx.transitiveObjectProperty()!=null){
         addType(entity,OWL.TRANSITIVE);
      }

      return null;
   }

   @Override
   public Object visitSubClassOf(OWLFSParser.SubClassOfContext ctx){
      if (isGCI(ctx))
         return null;
      TTArray subClassOf= addArrayAxiom(RDFS.SUBCLASSOF);
      subClassOf.add(convertClassExpression(ctx.superClass().classExpression()));
      return null;
   }

   private TTArray addArrayAxiom(TTIriRef predicate){
      if (entity.get(predicate)==null){
         TTArray array= new TTArray();
         entity.set(predicate,array);
      }
      return entity.get(predicate).asArray();
   }
   @Override public Object visitEquivalentClasses(OWLFSParser.EquivalentClassesContext ctx) {
      TTArray equivalent= addArrayAxiom(OWL.EQUIVALENTCLASS);
      equivalent.add(convertClassExpression(ctx.classExpression().get(1)));
      return null;
   }

   @Override public Object visitSubObjectPropertyOf(OWLFSParser.SubObjectPropertyOfContext ctx) {

         if (ctx.subObjectPropertyExpression().propertyExpressionChain() != null) {
            entity.set(OWL.PROPERTYCHAIN,
                convertPropertyChain(ctx.subObjectPropertyExpression().propertyExpressionChain()));
         }
      else {
         TTArray superProp= addArrayAxiom(RDFS.SUBPROPERTYOF);
         superProp.add( new TTIriRef(expand(ctx.superObjectPropertyExpression()
             .objectPropertyExpression()
             .objectProperty()
             .iri()
             .getText())));
      }
      return null;
   }

   private TTValue convertPropertyChain(OWLFSParser.PropertyExpressionChainContext chainContext) {
      TTArray chain = new TTArray();
      for (OWLFSParser.ObjectPropertyExpressionContext opcs:chainContext.objectPropertyExpression()){
         chain.add(new TTIriRef(expand(opcs.objectProperty().iri().getText())));
      }
      return chain;
   }

   private TTValue convertClassExpression(OWLFSParser.ClassExpressionContext ctx) {
      if (ctx.iri()!=null)
         return new TTIriRef(expand(ctx.getText()));
      else if (ctx.objectIntersectionOf()!=null){
         TTNode exp= new TTNode();
         TTArray inters= new TTArray();
         exp.set(OWL.INTERSECTIONOF,inters);
         for (OWLFSParser.ClassExpressionContext ctxInter:ctx.objectIntersectionOf().classExpression()){
            inters.add(convertClassExpression(ctxInter));
         }
         return exp;
      } else if (ctx.objectSomeValuesFrom()!=null) {
         TTNode exp = new TTNode();
         exp.set(RDF.TYPE, OWL.RESTRICTION);
         exp.set(OWL.ONPROPERTY, new TTIriRef(expand(ctx.objectSomeValuesFrom()
             .objectPropertyExpression()
             .objectProperty()
             .iri()
             .getText())));
         exp.set(OWL.SOMEVALUESFROM,
             convertClassExpression(ctx.objectSomeValuesFrom().classExpression()));
         return exp;
      } else
         return null;

   }




   private boolean isGCI(OWLFSParser.SubClassOfContext ctx) {
      if (ctx.subClass().classExpression().objectIntersectionOf()!=null)
         return true;
      else return false;
   }

   private String expand(String iri) {
       return context.expand(iri);
   }

}
