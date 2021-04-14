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

   public ReasonerPlus(){
      manager= new TTManager();
   }


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

   public TTDocument generateDomainRanges(TTDocument document) throws DataFormatException {
      if (document.getConcepts() == null)
         return document;
      if (manager.getDocument()==null)
         manager.setDocument(document);
      for (TTConcept concept:document.getConcepts()) {
         done = new HashSet<>();
         done.add(concept.getIri());
         inferDomainRanges(concept);
      }
      return document;
   }
   public void inferDomainRanges(TTConcept concept) throws DataFormatException {
      done = new HashSet<>();
      done.add(concept.getIri());
      bringDownPropertyAxioms(concept,RDFS.DOMAIN);
      bringDownPropertyAxioms(concept,RDFS.RANGE);
   }

   private void bringDownPropertyAxioms(TTConcept concept,TTIriRef axiom) {
      if (concept.get(RDFS.SUBPROPERTYOF) != null) {
         for (TTValue element : concept.get(RDFS.SUBPROPERTYOF).asArray().getElements()) {
            TTConcept parent = manager.getConcept(element.asIriRef().getIri());
            if (parent != null) {
               bringDownAxiom(concept, parent, axiom);
            }
         }
      }
   }

   private void bringDownAxiom(TTConcept concept,  TTConcept parent, TTIriRef axiom) {
      if (done.contains(parent.getIri()))
         return;
      done.add(parent.getIri());
      if (parent.get(axiom)!=null){
         TTValue cexp= parent.get(axiom);
         if (cexp.isIriRef()){
            TTIriRef superClass= cexp.asIriRef();
            if (!overriddenClasses(concept.get(axiom),superClass)){
               addToAxiom(concept,axiom,superClass);
            }
         } else if (cexp.isNode()){
            if (cexp.asNode().get(OWL.UNIONOF)!=null) {
               for (TTValue union : cexp.asNode().get(OWL.UNIONOF).asArray().getElements()) {
                  if (union.isIriRef())
                     if (!overriddenClasses(concept.get(axiom), union.asIriRef()))
                        addToAxiom(concept,axiom,union.asIriRef());
               }
            }
         } else {
            System.out.println("domain or range axiom array is unusual "+ parent.getIri());
         }
      }
   }

   private void addToAxiom(TTConcept concept,TTIriRef axiom,TTIriRef superClass) {
      if (concept.get(axiom)==null)
         concept.set(axiom,superClass);
      else
         if (concept.get(axiom).isIriRef()){
            TTArray union= new TTArray().add(concept.get(axiom));
            concept.set(axiom,new TTNode().set(OWL.UNIONOF,union));
         } else {
            concept.get(axiom).asNode().get(OWL.UNIONOF).asArray().add(superClass);

         }
   }


   private boolean overriddenClasses(TTValue expression, TTIriRef superClass) {
      if (expression==null)
         return false;
      if (expression.isIriRef()) {
         if (manager.isA(expression.asIriRef(), superClass))
            return true;
         else
            return false;
      } else if (expression.isNode()){
         if (expression.asNode().get(OWL.UNIONOF)!=null){
            for (TTValue union:expression.asNode().get(OWL.UNIONOF).asArray().getElements()){
               if (union.isIriRef())
                  if (manager.isA(union.asIriRef(),superClass))
                     return true;
            }
         }
      } else {
         System.out.println("unusual array for domain or range");
      }
      return false;
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
                  bringDownRoles(concept, parentConcept);
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
         propertyGroup.set(IM.GROUP_NUMBER,TTLiteral.literal(groupNumber.toString()));

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
                     propertyGroup.set(IM.GROUP_NUMBER,TTLiteral.literal(groupNumber.toString()));
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



   private void bringDownRoles(TTConcept concept,TTConcept parent) {
      if (done.contains(parent.getIri()))
         return;
      done.add(parent.getIri());
      TTArray roleGroups=null;
      if (parent.get(IM.ROLE_GROUP)!=null) {
         TTArray parentRoleGroups = parent.get(IM.ROLE_GROUP).asArray();
         TTNode newGroup=null;
         for (TTValue element : parentRoleGroups.getElements()) {
            TTNode roleGroup = element.asNode();
            for (TTValue role:roleGroup.get(IM.ROLE).asArray().getElements()){
               TTIriRef property= role.asNode().get(OWL.ONPROPERTY).asIriRef();
               if (!overriddenRole(property,concept))
                  newGroup= addInheritedRole(concept,newGroup,role);
            }
         }
      }
      if (parent.get(IM.IS_A)!=null)
         for (TTValue grandparent:parent.get(IM.IS_A).asArray().getElements()) {
            TTConcept grandparentConcept= manager.getConcept(grandparent.asIriRef().getIri());
            if (grandparentConcept!=null)
               bringDownRoles(concept, grandparentConcept);
         }

   }

   private TTNode addInheritedRole(TTConcept concept, TTNode newGroup, TTValue role) {
      Integer nextGroup = 1;
      if (concept.get(IM.ROLE_GROUP) != null) {
         nextGroup = concept.get(IM.ROLE_GROUP).asArray().size();
      } else{
         TTArray groups= new TTArray();
         concept.set(IM.ROLE_GROUP,groups);
      }
      if (newGroup==null){
         newGroup = new TTNode();
         newGroup.set(IM.GROUP_NUMBER,TTLiteral.literal(nextGroup));
         newGroup.set(IM.ROLE,new TTArray());
         concept.get(IM.ROLE_GROUP).asArray().add(newGroup);
      }
      newGroup.get(IM.ROLE).asArray().add(role);
      return newGroup;
   }

   private boolean overriddenRole(TTIriRef property, TTConcept concept) {
      if (concept.get(IM.ROLE_GROUP)!=null)
         for (TTValue roleGroup : concept.get(IM.ROLE_GROUP).asArray().getElements()) {
            for (TTValue role:roleGroup.asNode().get(IM.ROLE).asArray().getElements()){
               TTIriRef subProperty= role.asNode().get(OWL.ONPROPERTY).asIriRef();
               if (manager.isA(subProperty,property))
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
      if (concept.get(RDFS.SUBCLASSOF)!=null){
         for (TTValue superClass:concept.get(RDFS.SUBCLASSOF).asArray().getElements()){
            setExpression(concept,superClass);

         }

      }
      if (concept.get(OWL.EQUIVALENTCLASS)!=null) {
         if (concept.get(OWL.EQUIVALENTCLASS).isList()) {
            for (TTValue equClass : concept.get(OWL.EQUIVALENTCLASS).asArray().getElements()) {
               setExpression(concept, equClass);
            }
         }
      }
   }

   private void setExpression(TTConcept concept, TTValue exp) throws DataFormatException {
      if (exp.isIriRef())
         return;
      if (exp.asNode().get(OWL.INTERSECTIONOF) != null) {
         for (TTValue subExp : exp.asNode().get(OWL.INTERSECTIONOF).asArray().getElements()) {
            setExpression(concept, subExp);
         }
      } else if (exp.asNode().get(OWL.UNIONOF)!=null) {
         for (TTValue subExp : exp.asNode().get(OWL.UNIONOF).asArray().getElements()) {
            setExpression(concept, subExp);
         }
      }else
         addRole(concept,exp.asNode());
   }

   private void addRole(TTConcept concept, TTNode attribute) throws DataFormatException {


      TTValue roleGroups= concept.get(IM.ROLE_GROUP);
      if (roleGroups==null) {
         roleGroups = new TTArray();
         concept.set(IM.ROLE_GROUP, roleGroups);
         TTNode roleGroup= new TTNode();
         roleGroups.asArray().add(roleGroup);
         roleGroup.set(IM.GROUP_NUMBER,TTLiteral.literal(0));
         roleGroup.set(IM.ROLE,new TTArray());
      }
      TTValue roleGroup= roleGroups.asArray().getElements().stream().findFirst().get();
      if (roleGroup.asNode().get(IM.ROLE)==null)
         roleGroup.asNode().set(IM.ROLE, new TTArray());
      TTArray roles= roleGroup.asNode().get(IM.ROLE).asArray();
      TTNode role = new TTNode();
      roles.add(role);
      if (attribute.get(OWL.ONPROPERTY) != null) {
         role.set(RDF.TYPE,OWL.RESTRICTION);
         role.set(OWL.ONPROPERTY,attribute.get(OWL.ONPROPERTY));
         if (attribute.get(OWL.ONCLASS) != null)
               role.set(OWL.ONCLASS,attribute.get(OWL.ONCLASS));
         else if (attribute.get(OWL.SOMEVALUESFROM) != null)
               role.set(OWL.SOMEVALUESFROM,attribute.get(OWL.SOMEVALUESFROM));
         else if (attribute.get(OWL.ALLVALUESFROM) != null)
               role.set(OWL.ALLVALUESFROM,attribute.get(OWL.ALLVALUESFROM));
         else if (attribute.get(OWL.ONDATARANGE) != null)
               role.set(OWL.ONDATARANGE,attribute.get(OWL.ONDATARANGE));
         else if (attribute.get(OWL.ONDATATYPE) != null)
               role.set(OWL.ONDATATYPE,attribute.get(OWL.ONDATATYPE));
         else if (attribute.get(OWL.HASVALUE) != null)
               role.set(OWL.HASVALUE,attribute.get(OWL.HASVALUE));
            else
               throw new DataFormatException("unknown property construct");
         } else
            if (attribute.get(OWL.WITHRESTRICTIONS)==null)
               throw new DataFormatException("unknown class expression format");
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
                     superClasses.forEach(sup -> {TTIriRef iri= TTIriRef.iri(sup.getRepresentativeElement()
                         .asOWLClass()
                         .getIRI()
                         .toString());
                     if (!iri.equals(OWL.THING))
                        addIsa(c,iri);}
                     );
                  }
                  Node<OWLClass> equClasses= owlReasoner.getEquivalentClasses(owlClass);
                  if (equClasses!=null){
                     equClasses.forEach(sup -> {if (sup.isOWLClass()){
                        TTIriRef superIri= TTIriRef.iri(sup.getIRI().toString());
                        if (!superIri.equals(TTIriRef.iri(c.getIri())))
                           addIsa(c,superIri);}
                           ;});
                  }


               }
            }
         }
      return document;
   }
   private void addIsa(TTConcept concept,TTIriRef parent){
      if (concept.get(IM.IS_A)==null)
         concept.set(IM.IS_A,new TTArray());
      concept.get(IM.IS_A).asArray().add(parent);
   }

}
