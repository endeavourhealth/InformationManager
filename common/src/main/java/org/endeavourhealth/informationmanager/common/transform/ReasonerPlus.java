package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Classifies an ontology using an owl reasoner, generating ISA relationships from a Discovery ontology document.
 * Generates inferred role groups (Snomed pattern) from the existential quntifiers and propogates them to subclasses
 * Generatesi inferred field groups from the SHACL property definitions and propogates them to subclasses
 */
public class ReasonerPlus {
   private TTDocument document;
   private HashMap<String, TTConcept> conceptMap;
   private boolean consistent;
   private OWLReasoner owlReasoner;
   private TTManager manager;


   public TTDocument generateInferred(TTDocument document) throws OWLOntologyCreationException,DataFormatException {
      //Creates isas
      classify(document);
      manager= new TTManager();
      manager.setDocument(document);
      generateInheritedRoles(document);
      return document;
   }

   /**
    * Takes a classified document and propogates inherited roles and properties to descendants to enable direct access to properties
    * Creates roles in role group and properties in field group
    * @param document
    */

   public TTDocument generateInheritedRoles(TTDocument document) throws DataFormatException {
      if (document.getConcepts() == null)
         return document;
      for (TTConcept concept:document.getConcepts()) {
         setRoleGroups(concept);
         setFieldGroups(concept);

      }
      //Now brings down properties
      for (TTConcept concept:document.getConcepts()){
         if (concept.get(IM.IS_A)!=null)
            for (TTValue parent:concept.get(IM.IS_A).asArray().getElements())
               getInheritedRoles(concept,manager.getConcept(parent.asIriRef().getIri()));
      }

      return document;
   }

   private void getInheritedRoles(TTConcept concept,TTConcept parent) {

   }

   private void setRoleGroups(TTConcept concept) throws DataFormatException {
      findRoles(concept,concept.get(RDFS.SUBCLASSOF),null);
      findRoles(concept,concept.get(OWL.EQUIVALENTCLASS),null);
   }

   private void findRoles(TTConcept concept, TTValue expression,TTNode roleGroup) throws DataFormatException {
      if (expression==null)
         return;
      if (expression.isList()) {
         for (TTValue exp : expression.asArray().getElements())
            findRoles(concept, exp,roleGroup);
      }
      if (expression.isNode()){
         TTNode exp= expression.asNode();
         if (exp.get(OWL.INTERSECTIONOF)!=null)
            for (TTValue subExp:exp.get(OWL.INTERSECTIONOF).asArray().getElements())
               findRoles(concept,subExp,null);
         if (exp.get(OWL.UNIONOF)!=null)
            for (TTValue subExp:exp.get(OWL.UNIONOF).asArray().getElements())
               findRoles(concept,subExp,null);
         if (exp.get(OWL.ONPROPERTY)!=null){
            if (concept.get(IM.ROLE)==null){
               concept.set(IM.ROLE,new TTArray());
            }
            if (roleGroup==null){
               roleGroup= new TTNode();
               concept.get(IM.ROLE).asArray().add(roleGroup);
            }
            TTIriRef property= exp.get(OWL.ONPROPERTY).asIriRef();
            TTValue valueType;
            if (exp.get(OWL.ONCLASS)!=null)
               valueType= exp.get(OWL.ONCLASS);
            else if (exp.get(OWL.SOMEVALUESFROM)!=null)
               valueType= exp.get((OWL.SOMEVALUESFROM));
            else if (exp.get(OWL.ALLVALUESFROM)!=null)
               valueType= exp.get(OWL.ALLVALUESFROM);
            else if (exp.get(OWL.ONDATARANGE)!=null)
               valueType= exp.get(OWL.ONDATARANGE);
            else if (exp.get(OWL.ONDATATYPE)!=null)
               valueType= exp.get(OWL.ONDATATYPE);
            else if (exp.get(OWL.HASVALUE)!=null)
               valueType= exp.get(OWL.HASVALUE);
            else
               throw new DataFormatException("unknown property construct");
            if (valueType.isIriRef())
               roleGroup.set(property,valueType);
            else if (valueType.isLiteral())
               roleGroup.set(property,valueType);
            else {
               TTNode subGroup= new TTNode();
               roleGroup.set(property,subGroup);
               findRoles(concept,valueType, subGroup);
            }
         }
      }

   }

   private void setFieldGroups(TTConcept concept) {
   }


   /**
    * Classifies an ontology using an OWL Reasoner
    *
    * @return set of child-> parent "isa" nodes
    * @throws Exception
    */

   public TTDocument classify(TTDocument document) throws OWLOntologyCreationException, DataFormatException {
      this.document = document;
      if (document.getConcepts() == null)
         return document;
      conceptMap = new HashMap<>();
      //builds concept map for later look up
      document.getConcepts().forEach(c -> conceptMap.put(c.getIri(), c));
      TTToOWLEL transformer = new TTToOWLEL();
      TTManager dmanager = new TTManager();
      dmanager.setDocument(document);
      OWLOntologyManager owlManager = transformer.transform(document, dmanager);
      Set<OWLOntology> owlOntologySet = owlManager.getOntologies();
      Optional<OWLOntology> owlOntology = owlOntologySet.stream().findFirst();
      if (owlOntology.isPresent()) {
         OWLReasonerConfiguration config = new SimpleConfiguration();
         OWLOntology o = owlOntology.get();
         owlReasoner = new FaCTPlusPlusReasonerFactory().createReasoner(o, config);
         owlReasoner.precomputeInferences();
         if (!owlReasoner.isConsistent()) {
            consistent = false;
            return null;
         }
         consistent = true;
         OWLDataFactory dataFactory = new OWLDataFactoryImpl();
         //First removes the current "isas"
         for (TTConcept c : document.getConcepts()) {
            c.getPredicateMap().remove(IM.IS_A);
            TTIriRef type = c.get(RDF.TYPE).asIriRef();
            if (type.equals(OWL.OBJECTPROPERTY)) {
               OWLObjectPropertyExpression ope = dataFactory.getOWLObjectProperty(IRI.create(c.getIri()));
               NodeSet<OWLObjectPropertyExpression> superOb = owlReasoner.getSuperObjectProperties(ope, true);
               if (superOb != null) {
                  TTArray parents = new TTArray();
                  c.set(IM.IS_A, parents);
                  superOb.forEach(sob -> parents.add(TTIriRef.iri(sob.getRepresentativeElement().toString())));
               }
            } else if (type.equals(OWL.DATAPROPERTY)) {
               OWLDataProperty dpe = dataFactory.getOWLDataProperty(IRI.create(c.getIri()));
               NodeSet<OWLDataProperty> superP = owlReasoner.getSuperDataProperties(dpe, true);
               if (superP != null) {
                  TTArray parents = new TTArray();
                  c.set(IM.IS_A, parents);
                  superP.forEach(sob -> parents.add(TTIriRef.iri(sob.getRepresentativeElement().toString())));
               }
            } else {
                  OWLClassExpression owlClass = dataFactory.getOWLClass(IRI.create(c.getIri()));
                  NodeSet<OWLClass> superClasses = owlReasoner.getSuperClasses(owlClass, true);
                  if (superClasses != null) {
                     TTArray parents = new TTArray();
                     c.set(IM.IS_A, parents);
                     superClasses.forEach(sup -> parents.add(TTIriRef.iri(sup.getRepresentativeElement().getIRI().toString())));
                  }

               }
            }
         }
      return document;
   }

}
