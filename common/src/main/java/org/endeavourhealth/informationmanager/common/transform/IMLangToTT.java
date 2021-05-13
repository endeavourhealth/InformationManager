package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.informationmanager.parser.IMLangBaseVisitor;
import org.endeavourhealth.informationmanager.parser.IMLangParser;

import java.util.zip.DataFormatException;

public class IMLangToTT extends IMLangBaseVisitor<TTConcept> {
   private TTContext prefixes;
   private TTConcept concept;
   private String errorMessage;

   @Override public TTConcept visitConcept(IMLangParser.ConceptContext ctx)  {
      prefixes= new TTManager().createDefaultContext();
      concept= new TTConcept();
      concept.setContext(prefixes);
      concept.setIri(expand(ctx.iriLabel().iri().getText()));
      concept= setConceptTypes(ctx.types());
      if (errorMessage!=null)
         return null;
      else
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
      String expanded= prefixes.expand(iri);
      if (expanded==null){
         errorMessage= iri+" does not use a recognised prefix";
         return iri;

      }
      return expanded;
   }

}
