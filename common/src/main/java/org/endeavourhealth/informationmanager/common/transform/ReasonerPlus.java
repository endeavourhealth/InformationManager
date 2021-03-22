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
         setRoles(concept);
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

   private void setFieldGroups(TTConcept concept) {
   }

   private void getInheritedRoles(TTConcept concept,TTConcept parent) {
      if (parent.get(IM.ROLES)==null)
         return;
      TTNode parentRoles= parent.get(IM.ROLES).asNode();
      Integer roleGroup=0;
      Set<Map.Entry<TTIriRef, TTValue>> roles= parentRoles.getPredicateMap().entrySet();
      for (Map.Entry<TTIriRef, TTValue> role : roles) {
         bringDownToConcept(concept,role.getKey(), role.getValue());
      }
   }

   private void bringDownToConcept(TTConcept concept, TTIriRef property, TTValue value) {
      if (concept.get(IM.ROLES)!=null){
         TTNode conceptRoles= concept.get(IM.ROLES).asNode();
         Set<Map.Entry<TTIriRef, TTValue>> roles = conceptRoles.getPredicateMap().entrySet();
         for (Map.Entry<TTIriRef, TTValue> role : roles) {
            if (manager.isA(role.getKey(),property)){
               if (value.isIriRef())
                  if (role.getValue().isIriRef())
                     if (manager.isA(role.getValue().asIriRef(),value.asIriRef()))
                        return;
            }
         }
      }
      if (concept.get(IM.ROLES)==null)
         concept.set(IM.ROLES,new TTNode());
      concept.get(IM.ROLES).asNode().set(property,value);
   }

   private TTNode getRoleGroup(TTConcept concept, Integer groupNumber) {
      if (concept.get(IM.ROLES)!=null) {
         for (TTValue element : concept.get(IM.ROLES).asArray().getElements())
            if (element.isNode())
               if (Integer.parseInt(element.asNode().get(IM.COUNTER).asLiteral().getValue()) == groupNumber)
                  return element.asNode();
      }
      return null;

   }

   private void setRoles(TTConcept concept) throws DataFormatException {
      TTNode roles= null;
      if (concept.get(RDFS.SUBCLASSOF)!=null){
         for (TTValue superClass:concept.get(RDFS.SUBCLASSOF).asArray().getElements()){
            findRoles(concept,superClass,roles,0);
            if (roles!=null) {
               concept.set(IM.ROLES, roles);
            }
         }

      }
      if (concept.get(OWL.EQUIVALENTCLASS)!=null){
         if (concept.get(OWL.EQUIVALENTCLASS).isList()){
         for (TTValue equClass:concept.get(OWL.EQUIVALENTCLASS).asArray().getElements()) {
            findRoles(concept, equClass, roles, 0);
            if (roles != null)
               concept.set(IM.ROLES, roles);
         }
         }

      }
   }

   private Integer findRoles(TTConcept concept, TTValue expression,TTNode roles,Integer groupNumber) throws DataFormatException {
      if (expression.isList()) {
         for (TTValue exp : expression.asArray().getElements()) {
            findRoles(concept, exp, roles, groupNumber);
         }
         if (expression.isNode()) {
            TTNode exp = expression.asNode();
            if (exp.get(OWL.INTERSECTIONOF) != null)
               for (TTValue subExp : exp.get(OWL.INTERSECTIONOF).asArray().getElements()) {
                  findRoles(concept, subExp, roles, groupNumber);
               }
            if (exp.get(OWL.UNIONOF) != null)
               for (TTValue subExp : exp.get(OWL.UNIONOF).asArray().getElements())
                  findRoles(concept, subExp, roles, groupNumber);
            if (exp.get(OWL.ONPROPERTY) != null) {
               if (roles == null) {
                  roles = new TTNode();
                  roles.set(IM.COUNTER, TTLiteral.literal(groupNumber.toString()));
               }
               TTIriRef property = exp.get(OWL.ONPROPERTY).asIriRef();
               TTValue valueType;
               if (exp.get(OWL.ONCLASS) != null)
                  valueType = exp.get(OWL.ONCLASS);
               else if (exp.get(OWL.SOMEVALUESFROM) != null)
                  valueType = exp.get((OWL.SOMEVALUESFROM));
               else if (exp.get(OWL.ALLVALUESFROM) != null)
                  valueType = exp.get(OWL.ALLVALUESFROM);
               else if (exp.get(OWL.ONDATARANGE) != null)
                  valueType = exp.get(OWL.ONDATARANGE);
               else if (exp.get(OWL.ONDATATYPE) != null)
                  valueType = exp.get(OWL.ONDATATYPE);
               else if (exp.get(OWL.HASVALUE) != null)
                  valueType = exp.get(OWL.HASVALUE);
               else
                  throw new DataFormatException("unknown property construct");
               if (valueType.isIriRef())
                  roles.set(property, valueType);
               else if (valueType.isLiteral())
                  roles.set(property, valueType);
               else {
                  TTNode subGroup = new TTNode();
                  roles.set(property, subGroup);
                  findRoles(concept, valueType, subGroup, groupNumber);
                  groupNumber++;
               }
            }
         }

      }
      return groupNumber;
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
