package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.OWL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.informationmanager.common.Logger;
import org.endeavourhealth.imapi.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

/**
 * Converts Discovery JSON Triple tree syntax document to OWL EL functional syntax using an OWL factory.
 * Note that this is a limited transform for the purposes of EL based inferrencing using a reasoner. DL axioms are ignored or converted
 * to EL similar structures
 * For example property ranges domains are ignored if present. Cardinality restrictions are converted to existential quantification.
 * Data type restrictions are ignored.
 * <p>Thus a transform back from the OWL EL version will not match the source unless the source is EL only.
 */
public class TTToOWLEL {
   private DefaultPrefixManager prefixManager;
   private OWLDataFactory dataFactory;
   private OWLOntologyManager manager;
   private Integer anon=0;
   private OWLOntology ontology;
   private TTConcept currentConcept;
   private OWLEntity placeHolder;
   private TTManager ttManager;
   private Set<String> declared;


   public TTToOWLEL() {
      manager = OWLManager.createOWLOntologyManager();
      dataFactory = manager.getOWLDataFactory();
      prefixManager = new DefaultPrefixManager();
      declared= new HashSet<>();
   }


   /**
    * Transforms an information model JSON-LD RDF ontology to an OWL ontology
    *
    * @param document
    * @return OWLOntology manager together with one ontology (optional) and a set of prefixes
    * @throws OWLOntologyCreationException
    * @throws DataFormatException
    */

   public OWLOntologyManager transform(TTDocument document,TTManager dmanager) throws DataFormatException, OWLOntologyCreationException {

      ttManager= dmanager;
      //if the dmanager is null create it
      if (dmanager==null) {
         ttManager = new TTManager();
         ttManager.setDocument(document);
      }

      //Create ontology
      ontology = manager.createOntology(IRI.create(document.getGraph()));

      processPrefixes(document.getPrefixes());
      addPlaceHolder();
      processConcepts(document.getConcepts());

      return manager;
   }

   //Place holder to reduce noise in ontology
   private void addPlaceHolder() {
      placeHolder= dataFactory.getOWLEntity(EntityType.CLASS,IRI.create(IM.NAMESPACE+"PlaceHolder"));
      OWLDeclarationAxiom declaration = dataFactory.getOWLDeclarationAxiom(placeHolder);
      manager.addAxiom(ontology, declaration);
   }


   private void processImports(OWLOntology owlOntology, OWLDataFactory dataFactory,
                               OWLOntologyManager manager, Set<String> imports) {
      if (imports != null) {
         for (String importIri : imports) {
            OWLImportsDeclaration importDeclaration = dataFactory.getOWLImportsDeclaration(IRI.create(importIri));
            manager.applyChange(new AddImport(owlOntology, importDeclaration));
         }

      }
   }


   private void processPrefixes(List<TTPrefix> prefixes) {
      for (TTPrefix prefix: prefixes) {
         prefixManager.setPrefix(prefix.getPrefix()+":", prefix.getIri());
      }
      PrefixDocumentFormat ontologyFormat = new FunctionalSyntaxDocumentFormat();
      ontologyFormat.copyPrefixesFrom(prefixManager);
      manager.setOntologyFormat(ontology, ontologyFormat);
   }



   private void processConcepts(List<TTConcept> concepts) {
      if (concepts == null || concepts.size() == 0)
         return;
      int classno = 0;

      for (TTConcept concept : concepts) {
         classno = classno + 1;
         currentConcept= concept;
         //System.out.println(concept.getIri());
         IRI iri = getIri(concept.getIri());
         addDeclaration(concept);
         HashMap<TTIriRef, TTValue> predicates = concept.getPredicateMap();
         for (Map.Entry<TTIriRef, TTValue> entry : predicates.entrySet()) {
            if (entry.getKey().equals(RDFS.SUBCLASSOF))
               addSubClassOf(iri, entry.getValue().asArray());
            else if (entry.getKey().equals(OWL.EQUIVALENTCLASS)) {
               TTValue equType = entry.getValue();
               if (equType.isList())
                  addEquivalentClasses(iri, entry.getValue().asArray());
               else if (equType.asNode().get(OWL.WITHRESTRICTIONS) == null)
                  Logger.error("unknown equivalent class type " + currentConcept.getIri());
            } else if (entry.getKey().equals(RDFS.SUBPROPERTYOF)) {
               TTIriRef propertyType;
               if (concept.isType(OWL.DATAPROPERTY))
                  propertyType=OWL.DATAPROPERTY;
               else
                  propertyType= OWL.OBJECTPROPERTY;
               addSubPropertyOf(iri, propertyType,entry.getValue().asArray());
            } else if (entry.getValue().isLiteral())
               addAnnotation(iri,entry.getKey(),entry.getValue());
         }
      }
   }
   private void addEquivalentClasses(IRI iri,TTArray eqClasses) {
      if (eqClasses==null){
         Logger.error("Null equivalent classes in " + currentConcept.getIri());
      }
      for (TTValue exp:eqClasses.getElements()) {
         if (exp.asNode().get(OWL.WITHRESTRICTIONS) == null) {
            OWLEquivalentClassesAxiom equAx;
            equAx = dataFactory.getOWLEquivalentClassesAxiom(
                dataFactory.getOWLClass(iri),
                getOWLClassExpression(exp));
            manager.addAxiom(ontology, equAx);
         }
      }
   }
   private void addSubPropertyOf(IRI iri, TTIriRef propertyType, TTArray superClasses) {
      for (TTValue exp:superClasses.getElements()) {
         if (propertyType.equals(OWL.OBJECTPROPERTY)) {
            OWLSubObjectPropertyOfAxiom subAx = dataFactory
                .getOWLSubObjectPropertyOfAxiom(
                    dataFactory
                        .getOWLObjectProperty(iri),
                    dataFactory.getOWLObjectProperty(getIri(exp.asIriRef())));

                manager.addAxiom(ontology, subAx);
         } else {
            OWLSubDataPropertyOfAxiom subAx = dataFactory.getOWLSubDataPropertyOfAxiom(
                dataFactory.getOWLDataProperty(iri),dataFactory.getOWLDataProperty(getIri(exp.asIriRef())));

         }
      }
   }

   private void addSubClassOf(IRI iri, TTArray superClasses) {
      for (TTValue exp:superClasses.getElements()) {
         OWLSubClassOfAxiom subAx;
         subAx = dataFactory.getOWLSubClassOfAxiom(
             dataFactory.getOWLClass(iri),
             getOWLClassExpression(exp));
         manager.addAxiom(ontology, subAx);
      }
   }




   private OWLClassExpression getOPERestrictionAsOWlClassExpression(TTValue cex) {
      OWLObjectPropertyExpression owlOpe;
      TTNode exp= cex.asNode();
      if (exp.get(OWL.ONPROPERTY)!=null){
         IRI prop = getIri(exp.get(OWL.ONPROPERTY).asIriRef());
         owlOpe = dataFactory.getOWLObjectProperty(prop);
      } else {
         IRI prop = getIri(exp.get(OWL.INVERSEOF).asIriRef());
         owlOpe = dataFactory
             .getOWLObjectInverseOf(
                 dataFactory.getOWLObjectProperty(prop));
      }
      if (exp.get(OWL.ALLVALUESFROM)!=null){
         return dataFactory.getOWLObjectAllValuesFrom(
             owlOpe,
             getOWLClassExpression(exp.get(OWL.ALLVALUESFROM))
         );
      } if (exp.get(OWL.SOMEVALUESFROM)!=null){
         return dataFactory.getOWLObjectAllValuesFrom(
             owlOpe,
             getOWLClassExpression(exp.get(OWL.SOMEVALUESFROM))
         );
      } else  if (exp.get(OWL.MINCARDINALITY)!=null){
         return dataFactory.getOWLObjectSomeValuesFrom(
             owlOpe,
             getOWLClassExpression(exp.get(OWL.ONCLASS))
         );
      }else  if (exp.get(OWL.MAXCARDINALITY)!=null){
         return dataFactory.getOWLObjectSomeValuesFrom(
             owlOpe,
             getOWLClassExpression(exp.get(OWL.ONCLASS))
         );
      } else if (exp.get(OWL.ONCLASS)!=null){
         return dataFactory.getOWLObjectSomeValuesFrom(
             owlOpe,
             getOWLClassExpression(exp.get(OWL.ONCLASS))
         );
      }
      else {
         Logger.error("Unknown Restriction type "+ currentConcept.getIri());
         return dataFactory.getOWLClass("not sure", prefixManager);
      }

   }

   /**
    * produces either a single data property restriction or an object intersection of several cardinalities
    *
    * @param cex Discovery propertyData expression
    * @return OWL Class expression
    */
   private OWLClassExpression getDPERestrictionAsOWLClassExpression(TTValue cex) {
      TTNode exp = cex.asNode();

      IRI prop = getIri(exp.get(OWL.ONPROPERTY).asIriRef());
      if (exp.get(OWL.SOMEVALUESFROM) != null) {
         return dataFactory.getOWLDataSomeValuesFrom(
             dataFactory.getOWLDataProperty(prop),
             getOWLDataRange(exp));
      }else if (exp.get(OWL.ALLVALUESFROM) != null) {
         return dataFactory.getOWLDataAllValuesFrom(
             dataFactory.getOWLDataProperty(prop),
             getOWLDataRange(exp));
      } else if (exp.get(OWL.ONDATARANGE)!=null) {
         return dataFactory.getOWLDataSomeValuesFrom(
             dataFactory.getOWLDataProperty(prop),
             getOWLDataRange(exp));
      }else
         return
             dataFactory.getOWLClass("OWL EL limit - cardinality and data restricions not supported", prefixManager);
   }



   public OWLClassExpression getOWLClassExpression(TTValue cex) {
      if (cex==null)
         Logger.error("null class expression in concept "+ currentConcept.getIri());
      if (cex.isIriRef()) {
         return dataFactory.getOWLClass(getIri(cex.asIriRef()));
      } else if (cex.isNode()) {
         if (cex.asNode().get(OWL.INTERSECTIONOF) != null) {
            return dataFactory.getOWLObjectIntersectionOf(
                cex.asNode().get(OWL.INTERSECTIONOF).asArray().getElements()
                    .stream()
                    .map(this::getOWLClassExpression)
                    .collect(Collectors.toSet()));
         } else if (cex.asNode().get(OWL.UNIONOF) != null) {
            return dataFactory.getOWLObjectUnionOf(
                cex.asNode().get(OWL.UNIONOF).asArray().getElements()
                    .stream()
                    .map(this::getOWLClassExpression)
                    .collect(Collectors.toSet()));
         } else if (cex.asNode().get(OWL.ONPROPERTY) != null) {
            TTIriRef propertyType = guessPropertyType(cex.asNode());
            if (propertyType.equals(OWL.DATAPROPERTY))
               return getDPERestrictionAsOWLClassExpression(cex);
            else
               return getOPERestrictionAsOWlClassExpression(cex);
         } else if (cex.asNode().get(OWL.ONEOF) != null) {
            return getOneOfAsOWLClassExpression(cex);
         } else if (cex.asNode().get(OWL.COMPLEMENTOF) != null) {
            return (getComplementOfAsAOWLClassExpression(cex));
         } else if (cex.asNode().get(OWL.UNIONOF) != null) {
            return dataFactory.getOWLClass("unions not supported", prefixManager);
         }
      }
      Logger.error("Unknown classExpression type "+ currentConcept.getIri());
      return dataFactory.getOWLClass("not sure of type of expression", prefixManager);

}

   private TTIriRef guessPropertyType(TTNode exp) {
      TTConcept concept= ttManager.getConcept(exp.get(OWL.ONPROPERTY).asIriRef().getIri());
      if (concept==null)
         return OWL.OBJECTPROPERTY;
      if (concept.isType(OWL.DATAPROPERTY))
         return OWL.DATAPROPERTY;
      else
         return OWL.OBJECTPROPERTY;
   }


   private OWLClassExpression getComplementOfAsAOWLClassExpression(TTValue cex) {
      return dataFactory
          .getOWLObjectComplementOf(
              getOWLClassExpression(
                  cex.asNode().get(OWL.COMPLEMENTOF)));
   }

   private OWLClassExpression getOneOfAsOWLClassExpression(TTValue cex) {
      Set<OWLNamedIndividual> indiList = new HashSet<>();
      for (TTValue oneOf : cex.asArray().getElements()) {
         indiList.add(dataFactory.getOWLNamedIndividual(getIri(oneOf.asIriRef())));
      }
      return dataFactory.getOWLObjectOneOf(indiList);
   }

   private OWLDataRange getOWLDataRange(TTValue exp) {
      if (exp.asNode().get(OWL.ONDATARANGE)!=null)
         return dataFactory.getOWLDatatype(getIri(exp.asNode().get(OWL.ONDATARANGE).asIriRef()));
      else
         return dataFactory.getOWLDatatype(getIri("xsd:string"));

   }

   private OWLDataRange getOWLDataRange(DataPropertyRange dr) {
      return dataFactory.getOWLDatatype(getIri(dr.getDataType().getIri()));
   }

   private void addDeclaration(TTConcept concept){

      IRI iri = getIri(concept.getIri());
      OWLEntity entity=null;;
      if (concept.isType(OWL.CLASS))
            entity = dataFactory.getOWLEntity(EntityType.CLASS, iri);
         else if (concept.isType(OWL.OBJECTPROPERTY))
            entity = dataFactory.getOWLEntity(EntityType.OBJECT_PROPERTY, iri);
         else if (concept.isType(OWL.DATAPROPERTY))
            entity = dataFactory.getOWLEntity(EntityType.DATA_PROPERTY, iri);
         else if (concept.isType(OWL.ANNOTATIONPROPERTY))
            entity = dataFactory.getOWLEntity(EntityType.ANNOTATION_PROPERTY, iri);
         else if (concept.isType(OWL.NAMEDINDIVIDUAL))
            entity = dataFactory.getOWLEntity(EntityType.NAMED_INDIVIDUAL, iri);
         else
            entity= dataFactory.getOWLEntity(EntityType.CLASS,iri);
      OWLDeclarationAxiom declaration = dataFactory.getOWLDeclarationAxiom(entity);
      manager.addAxiom(ontology, declaration);
   }





   private void addAnnotation(IRI iri, TTIriRef property,TTValue value){
            OWLAnnotation annotation = dataFactory.getOWLAnnotation(
                dataFactory.getOWLAnnotationProperty(getIri(property.getIri())),
                dataFactory.getOWLLiteral(value.asLiteral().getValue()));
         manager.addAxiom(ontology, dataFactory.getOWLAnnotationAssertionAxiom(iri, annotation));
      }



   private IRI getIri(TTIriRef tiri) {
      String iri = tiri.getIri();
      return getIri(iri);
   }
   private IRI getIri(String iri){
      //is it a none declared concept? if so add it as a subclass of the placeholder
      if (ttManager.getConcept(iri)==null)
         if (!declared.contains(iri)){
            OWLSubClassOfAxiom subAx = dataFactory.getOWLSubClassOfAxiom(
                dataFactory.getOWLClass(IRI.create(iri)),
                placeHolder.asOWLClass());
            manager.addAxiom(ontology, subAx);

         }
      if (iri.toLowerCase().startsWith("http:") || iri.toLowerCase().startsWith("https:"))
         return IRI.create(iri);
      else
         return prefixManager.getIRI(iri);
   }

}

