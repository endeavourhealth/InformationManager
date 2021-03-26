package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Classifies an ontology using an owl reasoner, generating ISA relationships from a Discovery ontology document.
 * Generates inferred role groups (Snomed pattern) from the existential quntifiers and propogates them to subclasses
 * Generatesi inferred Property groups from the SHACL property definitions and propogates them to subclasses
 */
public class ReasonerPlus {
   private TTDocument document;
   private HashMap<String, TTConcept> conceptMap;
   private boolean consistent;
   private OWLReasoner owlReasoner;
   private TTManager manager;
   private Set<String> done;


   public TTDocument generateInferred(TTDocument document) throws OWLOntologyCreationException,DataFormatException {
      //Creates isas
      classify(document);
      manager= new TTManager();
      manager.setDocument(document);
      generateRoleGroups(document);
      generateInheritedRoles(document);
      generatePropertyGroups(document);
      generateDomainRanges(document);
      return document;
   }

   public void generateDomainRanges(TTDocument document) throws DataFormatException {
      if (document.getConcepts() == null)
         return;
      for (TTConcept concept:document.getConcepts()) {
         done = new HashSet<>();
         done.add(concept.getIri());
         if (concept.isType(IM.RECORD))
            inferDomainRanges(concept);
      }
   }
   public void inferDomainRanges(TTConcept concept) throws DataFormatException {
      done = new HashSet<>();
      done.add(concept.getIri());
      inferDomain(concept);
      inferRange(concept);
   }
   public void inferDomain(TTConcept concept){
      TTArray domains=null;
      if (concept.get(RDFS.DOMAIN) == null)
         domains = new TTArray();
      else
         domains= concept.get(RDFS.DOMAIN).asArray();

   }
   public void inferRange(TTConcept concept){
      TTArray domains=null;
      if (concept.get(RDFS.RANGE) == null)
         domains = new TTArray();
      else
         domains= concept.get(RDFS.RANGE).asArray();

   }


   private void generatePropertyGroups(TTDocument document) {
      if (document.getConcepts() == null)
         return;
      for (TTConcept concept:document.getConcepts()) {
         done = new HashSet<>();
         done.add(concept.getIri());
         if (concept.isType(IM.RECORD))
            setPropertyGroups(concept);
      }
   }

   private void generateRoleGroups(TTDocument document) throws DataFormatException {
      if (document.getConcepts() == null)
         return;
      for (TTConcept concept:document.getConcepts()) {
         setRoleGroups(concept);


      }
   }

   /**
    * Takes a classified document and propogates inherited roles and properties to descendants to enable direct access to properties
    * Creates roles in role group and properties in Property group
    * @param document
    */

   public TTDocument generateInheritedRoles(TTDocument document) throws DataFormatException {
      //Now brings down properties
      for (TTConcept concept:document.getConcepts()){
         done = new HashSet<>();
         done.add(concept.getIri());
         if (concept.get(IM.IS_A)!=null)
            for (TTValue parent:concept.get(IM.IS_A).asArray().getElements()) {
               TTConcept parentConcept= manager.getConcept(parent.asIriRef().getIri());
               if (parentConcept!=null)
                  bringDownInheritedRoles(concept, parentConcept);
            }
      }
      return document;


   }


   private void setPropertyGroups(TTConcept concept) {
      if (concept.get(IM.PROPERTY_GROUP)!=null)
         concept.getPredicateMap().remove((IM.PROPERTY_GROUP));
      done= new HashSet<>();
      done.add(concept.getIri());
      Integer groupNumber=0;
      if (concept.get(SHACL.PROPERTY)!=null){
         //Adds the concept's property list as the first Property group for the concept
         TTArray propertyGroups = new TTArray();
         concept.set(IM.PROPERTY_GROUP,propertyGroups);
         TTNode propertyGroup= new TTNode();
         propertyGroups.add(propertyGroup);
         propertyGroup.set(SHACL.PROPERTY,concept.get(SHACL.PROPERTY));
         propertyGroup.set(IM.COUNTER,TTLiteral.literal(groupNumber.toString()));

      }
      if (concept.get(IM.IS_A)!=null)
         for (TTValue element:concept.get(IM.IS_A).asArray().getElements()){
            if (element.isIriRef()){
               TTConcept parentConcept= manager.getConcept(element.asIriRef().getIri());
               if (parentConcept!=null) {
                  groupNumber++;
                  groupNumber= bringDownPropertyGroups(concept, parentConcept,groupNumber);
               }
            }

         }
   }

   private Integer bringDownPropertyGroups(TTConcept concept, TTConcept parentConcept,Integer groupNumber) {
      if (done.contains(parentConcept.getIri()))
         return groupNumber;

      if (parentConcept.get(SHACL.PROPERTY)!=null) {
         TTNode propertyGroup = null;
         for (TTValue element : parentConcept.get(SHACL.PROPERTY).asArray().getElements()) {
            TTNode parentProperty = element.asNode();
            TTIriRef property = parentProperty.get(SHACL.PATH).asIriRef();
            TTValue value = null;
            if (parentProperty.get(SHACL.DATATYPE) != null)
               value = parentProperty.get(SHACL.DATATYPE);
            else if (parentProperty.get(SHACL.CLASS) != null) {
               value = parentProperty.get(SHACL.CLASS);
            }
            if (value != null)
               if (!overriddenProperties(property, value, concept)) {
                  if (concept.get(IM.PROPERTY_GROUP)==null){
                     TTArray propertyGroups= new TTArray();
                     concept.set(IM.PROPERTY_GROUP,propertyGroups);
                  }
                  if (propertyGroup==null){
                     propertyGroup= new TTNode();
                     propertyGroup.set(IM.COUNTER,TTLiteral.literal(groupNumber.toString()));
                     propertyGroup.set(IM.INHERITED_FROM,TTIriRef.iri(parentConcept.getIri()));
                     concept.get(IM.PROPERTY_GROUP).asArray().add(propertyGroup);
                  }
                  if (propertyGroup.get(SHACL.PROPERTY)==null)
                     propertyGroup.set(SHACL.PROPERTY,new TTArray());
                  propertyGroup.get(SHACL.PROPERTY).asArray().add(parentProperty);
               }
         }
      }
      if (parentConcept.get(IM.IS_A)!=null)
         for (TTValue grandparent:parentConcept.get(IM.IS_A).asArray().getElements()) {
            TTConcept grandparentConcept= manager.getConcept(grandparent.asIriRef().getIri());
            if (grandparentConcept!=null) {
               groupNumber++;
               return bringDownPropertyGroups(concept, grandparentConcept,groupNumber);
            }
         }
      return groupNumber;

   }



   private void bringDownInheritedRoles(TTConcept concept,TTConcept parent) {
      if (done.contains(parent.getIri()))
         return;
      done.add(parent.getIri());
      if (parent.get(IM.ROLE_GROUP)!=null) {
         TTArray parentRoles = parent.get(IM.ROLE_GROUP).asArray();
         Integer groupNumber = 0;
         for (TTValue element : parentRoles.getElements()) {
            TTNode roleGroup = element.asNode();
            if (roleGroup.get(IM.COUNTER)!=null)
               groupNumber= Integer.parseInt(roleGroup.get(IM.COUNTER).asLiteral().getValue());
            Set<Map.Entry<TTIriRef, TTValue>> roles = roleGroup.getPredicateMap().entrySet();
            for (Map.Entry<TTIriRef, TTValue> role : roles) {
               bringDownRole(concept, role.getKey(), role.getValue(),groupNumber);
            }
         }
      }
      if (parent.get(IM.IS_A)!=null)
         for (TTValue grandparent:parent.get(IM.IS_A).asArray().getElements()) {
            TTConcept grandparentConcept= manager.getConcept(grandparent.asIriRef().getIri());
            if (grandparentConcept!=null)
               bringDownInheritedRoles(concept, grandparentConcept);
         }

   }

   private void bringDownRole(TTConcept concept, TTIriRef property, TTValue value,Integer groupNumber) {
      if (concept.get(IM.ROLE_GROUP)!=null) {
         for (TTValue element : concept.get(IM.ROLE_GROUP).asArray().getElements()) {
            TTNode conceptRoles = element.asNode();
            if (conceptRoles.get(IM.COUNTER) != null)
               if (Integer.parseInt(conceptRoles.get(IM.COUNTER).asLiteral().getValue()) == groupNumber)
                  if (overriddenRoles(property, value, conceptRoles))
                     return;
         }
      }
      if (concept.get(IM.ROLE_GROUP)==null) {
         concept.set(IM.ROLE_GROUP, new TTArray());
         TTNode roleGroup = new TTNode();
         concept.get(IM.ROLE_GROUP).asArray().add(roleGroup);
         roleGroup.set(IM.COUNTER, TTLiteral.literal(groupNumber));
      }
      for (TTValue element:concept.get(IM.ROLE_GROUP).asArray().getElements()) {
         if (Integer.parseInt(element.asNode().get(IM.COUNTER).asLiteral().getValue())==groupNumber)
            element.asNode().set(property, value);
      }
   }

   private boolean overriddenRoles(TTIriRef property, TTValue value, TTNode conceptRoles) {
      Set<Map.Entry<TTIriRef, TTValue>> roles = conceptRoles.getPredicateMap().entrySet();
      for (Map.Entry<TTIriRef, TTValue> role : roles) {
         if (manager.isA(role.getKey(), property)) {
            if (value.isIriRef())
               if (role.getValue().isIriRef())
                  if (manager.isA(role.getValue().asIriRef(), value.asIriRef()))
                     return true;
         }
      }
      return false;
   }

   private boolean overriddenProperties(TTIriRef property, TTValue value, TTConcept concept) {
      if (concept.get(IM.PROPERTY_GROUP) != null) {
         TTArray propertyGroups = concept.get(IM.PROPERTY_GROUP).asArray();
         for (TTValue element : propertyGroups.getElements()) {
            TTNode propertyGroup = element.asNode();
            TTArray rootProperties = propertyGroup.get(SHACL.PROPERTY).asArray();
            for (TTValue PropertyElement : rootProperties.getElements()) {
               TTNode Property = PropertyElement.asNode();
               if (manager.isA(Property.get(SHACL.PATH).asIriRef(), property)) {
                  if (Property.get(SHACL.DATATYPE) != null) {
                     if (value.asIriRef().equals(Property.get(SHACL.DATATYPE).asIriRef()))
                        return true;
                  } else {
                     if (Property.get(SHACL.CLASS) != null) {
                        TTValue rootValue = Property.get(SHACL.CLASS);
                        if (rootValue.isIriRef())
                           if (manager.isA(Property.get(SHACL.CLASS).asIriRef(), value.asIriRef()))
                              return true;
                     }
                  }
               }
            }
         }
      }
      return false;
   }

   private void setRoleGroups(TTConcept concept) throws DataFormatException {
      if (concept.get(IM.ROLE_GROUP)!=null)
         concept.getPredicateMap().remove(IM.ROLE_GROUP);
      TTArray roleGroups= new TTArray();
      Integer groupNumber=0;
      if (concept.get(RDFS.SUBCLASSOF)!=null){
         for (TTValue superClass:concept.get(RDFS.SUBCLASSOF).asArray().getElements()){
             TTNode roles= findRoles(concept,superClass,null);
            if (roles!=null) {
               roles.set(IM.COUNTER, TTLiteral.literal(groupNumber));
               roleGroups.add(roles);
            }
         }

      }
      if (concept.get(OWL.EQUIVALENTCLASS)!=null) {
         if (concept.get(OWL.EQUIVALENTCLASS).isList()) {
            for (TTValue equClass : concept.get(OWL.EQUIVALENTCLASS).asArray().getElements()) {
               TTNode roles = findRoles(concept, equClass, null);
               if (roles != null) {
                  roles.set(IM.COUNTER, TTLiteral.literal(groupNumber));
                  roleGroups.add(roles);
               }
            }
         }
      }
      if (roleGroups.size()>0)
         concept.set(IM.ROLE_GROUP,roleGroups);
   }

   private TTNode findRoles(TTConcept concept, TTValue expression,TTNode roles) throws DataFormatException {
      if (expression.isList()) {
         for (TTValue exp : expression.asArray().getElements()) {
            roles=findRoles(concept, exp, roles);
         }
      } else if (expression.isNode()) {
         TTNode exp = expression.asNode();
         if (exp.get(OWL.INTERSECTIONOF) != null)
            for (TTValue subExp : exp.get(OWL.INTERSECTIONOF).asArray().getElements()) {
               roles=findRoles(concept, subExp, roles);
            }
         if (exp.get(OWL.UNIONOF) != null)
            for (TTValue subExp : exp.get(OWL.UNIONOF).asArray().getElements())
               roles=findRoles(concept, subExp, roles);
         if (exp.get(OWL.ONPROPERTY) != null) {
            if (roles == null) {
               roles = new TTNode();
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
               TTNode subGroup = null;
               subGroup= findRoles(concept, valueType, subGroup);
               if (subGroup!=null)
                  roles.set(property, subGroup);
            }
         }
      }
      return roles;
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
            if (c.isType(OWL.OBJECTPROPERTY)) {
               OWLObjectPropertyExpression ope = dataFactory.getOWLObjectProperty(IRI.create(c.getIri()));
               NodeSet<OWLObjectPropertyExpression> superOb = owlReasoner.getSuperObjectProperties(ope, true);
               if (superOb != null) {
                  TTArray parents = new TTArray();
                  c.set(IM.IS_A, parents);
                  superOb.forEach(sob -> parents.add(TTIriRef
                      .iri(sob.getRepresentativeElement()
                          .asOWLObjectProperty()
                          .getIRI()
                          .toString())));
               }
            } else if (c.isType(OWL.DATAPROPERTY)) {
               OWLDataProperty dpe = dataFactory.getOWLDataProperty(IRI.create(c.getIri()));
               NodeSet<OWLDataProperty> superP = owlReasoner.getSuperDataProperties(dpe, true);
               if (superP != null) {
                  TTArray parents = new TTArray();
                  c.set(IM.IS_A, parents);
                  superP.forEach(sob -> parents.add(TTIriRef
                      .iri(sob
                          .getRepresentativeElement().asOWLDataProperty()
                          .getIRI()
                          .toString())));
               }
            } else {
                  OWLClassExpression owlClass = dataFactory.getOWLClass(IRI.create(c.getIri()));
                  NodeSet<OWLClass> superClasses = owlReasoner.getSuperClasses(owlClass, true);
                  if (superClasses != null) {
                     TTArray parents = new TTArray();
                     c.set(IM.IS_A, parents);
                     superClasses.forEach(sup -> parents.add(TTIriRef
                         .iri(sup.getRepresentativeElement()
                             .asOWLClass()
                             .getIRI()
                             .toString())));
                  }

               }
            }
         }
      return document;
   }

}
