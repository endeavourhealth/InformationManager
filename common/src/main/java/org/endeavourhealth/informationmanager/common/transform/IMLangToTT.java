package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.informationmanager.parser.IMLangBaseVisitor;
import org.endeavourhealth.informationmanager.parser.IMLangParser;

import java.util.zip.DataFormatException;

/**
 * Converts a concept in IMlangaue to RDF triple concept (TTConcept)
 */
public class IMLangToTT extends IMLangBaseVisitor<TTConcept> {
   private TTContext prefixes;
   private TTConcept concept;
   private String errorMessage;

   /**
    * Visits the antlr parser to parse text defining a conceprt
    * @param ctx  the concept entry point in the parse tree
    * @return a concept -TTConcept class
    */
   @Override public TTConcept visitConcept(IMLangParser.ConceptContext ctx)  {
      prefixes= new TTManager().createDefaultContext();
      concept= new TTConcept();
      concept.setContext(prefixes);
      concept.setIri(expand(ctx.iriLabel().iri().getText()));
      concept= setConceptTypes(ctx.types());
      concept= setAnnotations(ctx.annotationList());
      concept = setSPO(ctx.predicateObjectList());
      if (errorMessage!=null)
         return null;
      else
         return concept;
   }

   private TTConcept setSPO(IMLangParser.PredicateObjectListContext cpoList) {
      if (cpoList!=null)
      {
         for (IMLangParser.AxiomContext axiom: cpoList.axiom()){
            concept= setAxiom(axiom);
         }
      }
      if (errorMessage!=null)
         return concept;
      return concept;
   }

   private TTConcept setAxiom(IMLangParser.AxiomContext axiom) {
      if (axiom.subclassOf() != null) {
         concept.set(RDFS.SUBCLASSOF,new TTArray());
         TTValue expression= getExpression(axiom.subclassOf().classExpression());
         if (errorMessage!=null)
            return concept;
         concept.get(RDFS.SUBCLASSOF).asArray().add(expression);
         return concept;
      }
      return concept;
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

   private TTConcept setAnnotations(IMLangParser.AnnotationListContext annots) {
      if (annots!=null){
         for (IMLangParser.AnnotationContext annot:annots.annotation()){
            if (annot.name()!=null)
               concept.setName(annot.name().getText());
            if (annot.description()!=null)
               concept.setDescription(annot.description().getText());
            if (annot.code()!=null)
               concept.setCode(annot.code().getText());
            if (annot.scheme()!=null)
               concept.setScheme(TTIriRef.iri(expand(annot.scheme().iri().getText())));
            if (annot.status()!=null)
               concept.setStatus(TTIriRef.iri(expand(annot.status().iri().getText())));
         }
      }
      return concept;
   }

   private TTConcept setConceptTypes(IMLangParser.TypesContext types) {
      if (types==null){
         errorMessage= "Unable to create concept without a type";
         return null;
      }
      for (IMLangParser.IriContext iri:types.iri()){
         concept.addType(TTIriRef.iri(expand(iri.getText())));
      }
      return concept;

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
