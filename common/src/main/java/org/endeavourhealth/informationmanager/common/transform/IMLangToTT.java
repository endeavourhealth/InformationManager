package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.parser.imlang.IMLangBaseVisitor;
import org.endeavourhealth.imapi.parser.imlang.IMLangParser;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.zip.DataFormatException;

/**
 * Converts a entity in IMlangaue to RDF triple entity (TTEntity)
 */
public class IMLangToTT extends IMLangBaseVisitor<TTEntity> {
   private TTContext prefixes;
   private TTEntity entity;
   private String errorMessage;

   /**
    * Visits the antlr parser to parse text defining a conceprt
    * @param ctx  the entity entry point in the parse tree
    * @return a entity -TTEntity class
    */
   @Override public TTEntity visitEntity(IMLangParser.EntityContext ctx)  {
      prefixes= new TTManager().createDefaultContext();
      entity= new TTEntity();
      entity.setContext(prefixes);
      entity.setIri(expand(ctx.iriLabel().iri().getText()));
      entity= setEntityTypes(ctx.types());
      entity= setAnnotations(ctx.annotationList());
      entity = setSPO(ctx.predicateObjectList());
      if (errorMessage!=null)
         return null;
      else
         return entity;
   }

   private TTEntity setSPO(IMLangParser.PredicateObjectListContext cpoList) {
      if (cpoList!=null)
      {
         for (IMLangParser.AxiomContext axiom: cpoList.axiom()){
            entity= setAxiom(axiom);
         }
      }
      if (errorMessage!=null)
         return entity;
      return entity;
   }

   private TTEntity setAxiom(IMLangParser.AxiomContext axiom) {
      if (axiom.subclassOf() != null) {
         entity.set(RDFS.SUBCLASSOF,new TTArray());
         TTValue expression= getExpression(axiom.subclassOf().classExpression());
         if (errorMessage!=null)
            return entity;
         entity.get(RDFS.SUBCLASSOF).asArray().add(expression);
         return entity;
      }
      return entity;
   }

   private TTValue getExpression(IMLangParser.ClassExpressionContext cexp) {
      if (cexp.and()!=null){
            TTNode intersection= new TTNode();
            intersection.set(OWL.INTERSECTIONOF,new TTArray());
            return intersection;
      } else if (cexp.or()!=null) {
         TTNode union = new TTNode();
         union.set(OWL.UNIONOF, new TTArray());
         return union;
      } else if (cexp.not()!=null){
         TTNode complement= new TTNode();
         complement.set(OWL.COMPLEMENTOF,getExpression(cexp.not().classExpression()));
         return complement;
      } else
         return getSubExpression(cexp);
   }
   private TTValue getSubExpression(IMLangParser.ClassExpressionContext cexp){
      if (cexp.classIri()!=null)
         return TTIriRef.iri(expand(cexp.classIri().iri().getText()));
      else if (cexp.existential()!=null)
         return getPropertyRestriction(cexp.existential());
      else {
         errorMessage= "Unknown class expression ";
         return null;
      }
   }

   private TTValue getPropertyRestriction(IMLangParser.ExistentialContext prc) {
      TTNode pres= new TTNode();
      if (prc.roleIri()!=null){
         if (prc.classOrDataType()!=null){
               pres.set(RDF.TYPE,OWL.RESTRICTION);
               pres.set(OWL.ONPROPERTY,TTIriRef.iri(expand(prc.roleIri().iri().getText())));
               pres.set(OWL.SOMEVALUESFROM,TTIriRef
                   .iri(expand(prc.classOrDataType().iri().getText())));
               return pres;
            } else {
               errorMessage="Invalid existential quantification";
               return null;
         }
      }
      errorMessage="Faulty existential quantification";
      return null;

   }

   private TTEntity setAnnotations(IMLangParser.AnnotationListContext annots) {
      if (annots!=null){
         for (IMLangParser.AnnotationContext annot:annots.annotation()){
            if (annot.name()!=null)
               entity.setName(annot.name().getText());
            if (annot.description()!=null)
               entity.setDescription(annot.description().getText());
            if (annot.code()!=null)
               entity.setCode(annot.code().getText());
            if (annot.scheme()!=null)
               entity.setScheme(TTIriRef.iri(expand(annot.scheme().iri().getText())));
            if (annot.status()!=null)
               entity.setStatus(TTIriRef.iri(expand(annot.status().iri().getText())));
         }
      }
      return entity;
   }

   private TTEntity setEntityTypes(IMLangParser.TypesContext types) {
      if (types==null){
         errorMessage= "Unable to create entity without a type";
         return null;
      }
      for (IMLangParser.IriContext iri:types.iri()){
         entity.addType(TTIriRef.iri(expand(iri.getText())));
      }
      return entity;

   }

   private String expand(String iri){
      String expanded = null;
      if (iri.contains(":"))
         expanded= prefixes.expand(iri);
     else if (iri.startsWith("<"))
           expanded= iri;
     else
        expanded = prefixes.expand("im:"+ iri);
      if (expanded==null){
         errorMessage= iri+" does not use a recognised prefix";
         return iri;

      }
      return expanded;
   }

}
