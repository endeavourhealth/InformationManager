package org.endeavourhealth.informationmanager.common.transform;


import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.IntegerLiteral;
import org.eclipse.rdf4j.model.util.Values;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.informationmanager.common.Logger;
import org.endeavourhealth.informationmanager.common.transform.Common;
import org.endeavourhealth.informationmanager.common.transform.OWLPropertyType;
import org.endeavourhealth.informationmanager.common.transform.OntologyIri;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLFacet;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static org.eclipse.rdf4j.model.util.Values.bnode;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

/**
 * Converts Discovery JSON syntax document to new Endeavour triple format
 *
 */
public class V1ToTTDocument {
   private DOWLManager discoveryMgr;
   private Integer anon=0;
   private Map<String, OWLPropertyType> owlPropertyTypeMap;
   private List<ConceptType> ignoreTypes;
   private TTDocument document;
   private Ontology ontology;




   public void setIgnoreTypes(List<ConceptType> ignoreTypes){
      this.ignoreTypes= ignoreTypes;
   }


   /**
    * Transforms a Discovery JSON ontology to an OWL ontology
    *
    * @param discoveryDocument
    * @return Model Document containing triple concept models
    * @throws FileFormatException
    */
   public TTDocument transform(Ontology ontology) throws FileFormatException, DataFormatException {


      //document= new ModelDocument();
      TTManager dmanager= new TTManager();
      document= dmanager.createDocument(ontology.getIri());
      this.ontology= ontology;
      discoveryMgr = new DOWLManager();
      discoveryMgr.setOntology(ontology);
      discoveryMgr.createIndex();

      mapPrefixes();
      mapConcepts();
      mapIndividuals();
      setPredicateTemplate(document);

      return document;
   }


   private void mapImports(OWLOntology owlOntology, OWLDataFactory dataFactory,
                               OWLOntologyManager manager, Set<String> imports) {
      if (imports != null) {
         for (String importIri : imports) {
            OWLImportsDeclaration importDeclaration = dataFactory.getOWLImportsDeclaration(IRI.create(importIri));
            manager.applyChange(new AddImport(owlOntology, importDeclaration));
         }

      }
   }


   private void mapPrefixes() {
      List<TTPrefix> prefixes= new ArrayList<>();
      if (ontology.getNamespace()!=null){
         document.setPrefixes(prefixes);
         for (Namespace ns:ontology.getNamespace()){
            TTPrefix prefix= new TTPrefix()
                .setPrefix(ns.getPrefix().replace(":",""))
                .setIri(ns.getIri());

            prefixes.add(prefix);
         }
      }
      //document.setPrefixes(ontology.getNamespace());
   }

   private void mapIndividuals() throws DataFormatException {
      if (ontology.getIndividual() == null || ontology.getIndividual().size() == 0)
         return;
      for (Individual ind : ontology.getIndividual()) {
         TTConcept concept= mapConcept(ind);
         document.addIndividual(concept);
      }
   }


   private void mapConcepts() throws DataFormatException {
      if (ontology.getConcept() == null || ontology.getConcept().size() == 0)
         return;
      int classno = 0;

      for (Concept concept : ontology.getConcept()) {
         classno = classno + 1;
         TTConcept eConcept = mapConcept(concept);
         document.addConcept(eConcept);
      }
   }
   private TTConcept mapConcept(Concept concept) throws DataFormatException {
         //Sets initial RDF type
         TTIriRef type;
         switch (concept.getConceptType()) {
            case OBJECTPROPERTY:
               type = OWL.OBJECTPROPERTY;
               break;
            case DATAPROPERTY:
               type = OWL.DATAPROPERTY;
               break;
            case CLASSONLY:
               type = OWL.CLASS;
               break;
            case ANNOTATION:
               type = OWL.ANNOTATIONPROPERTY;
               break;
            case INDIVIDUAL:
               type=OWL.NAMEDINDIVIDUAL;
               break;
            default:
               type = OWL.CLASS;
         }
         //Sets the information model type
         TTIriRef modelType;
         switch (concept.getConceptType()) {
            case VALUESET:
               modelType = IM.VALUESET;
               break;
            case RECORD:
               modelType = IM.RECORD;
               break;
            case INDIVIDUAL:
               modelType= IM.INDIVIDUAL;
               break;
            default:
               modelType = type;
         }
         //Maps the status
         TTIriRef status= IM.ACTIVE;
         if (concept.getStatus()!=null) {
            switch (concept.getStatus()) {
               case ACTIVE:
                  status = IM.ACTIVE;
                  break;
               case INACTIVE:
                  status = IM.INACTIVE;
                  break;
               case DRAFT:
                  status = IM.DRAFT;
                  break;
               default:
                  status = IM.ACTIVE;
            }
         }
         //Create and populate the concept
         TTConcept eConcept= new TTConcept(concept.getIri())
             .set(IM.MODELTYPE,modelType)
             .set(RDF.TYPE,new TTArray().add(type))
             .set(RDFS.LABEL,literal(concept.getName()));
         if (concept.getDescription()!=null)
             eConcept.set(RDFS.COMMENT,literal(concept.getDescription()));
         if (concept.getCode()!=null)
             eConcept.set(IM.CODE,literal(concept.getCode()));
         if (concept.getScheme()!=null)
             eConcept.set(IM.HAS_SCHEME,iri(concept.getScheme().getIri()));

         eConcept.set(IM.STATUS,status);

         //start on axioms
         mapConceptAxioms(eConcept,concept);
         mapObjectProperty(eConcept, concept);
         mapDataProperty(eConcept, concept);
         mapDataType(eConcept, concept);
         mapAnnotationProperty(eConcept, concept);
         mapConceptSets(eConcept,concept);
         mapRecordType(eConcept,concept);
         return eConcept;
   }

   private void mapRecordType(TTConcept eConcept, Concept concept) throws DataFormatException {
      if (concept.getProperty() != null) {
         TTArray properties= new TTArray();
         eConcept.set(org.endeavourhealth.imapi.vocabulary.SHACL.PROPERTY,properties);
         for (PropertyValue property : concept.getProperty()) {
            properties.add(mapSHACLProperty(property));
         }
      }
   }
   public TTValue mapSHACLProperty(PropertyValue opv) throws DataFormatException {
      TTNode ep = new TTNode();
      if (opv.getInverseOf() != null)
         ep.set(SHACL.INVERSEPATH, new TTNode().set(OWL.INVERSEOF,iri(opv.getInverseOf().getIri())));
       else
         ep.set(SHACL.PATH, iri(opv.getProperty().getIri()));

       TTIriRef onRange=SHACL.CLASS;
       if (opv.getValueType() != null) {
          Concept propType = discoveryMgr.getConcept(opv.getProperty().getIri());
          if (propType != null) {
             if (propType.getConceptType() == ConceptType.OBJECTPROPERTY)
                onRange = SHACL.CLASS;
             else if (propType.getConceptType() == ConceptType.DATAPROPERTY)
                onRange = SHACL.DATATYPE;
             else
                throw new DataFormatException("unknown property value type");
          } else
             onRange = SHACL.CLASS;
       }

       //Complex logic to tidy up cardinalities which are overly set
      //Firstly if max is set then set max and min if min is set
      if (opv.getMax() != null) {
         ep.set(org.endeavourhealth.imapi.vocabulary.SHACL.MAXCOUNT,
             literal(opv.getMax().toString(),XSD.INTEGER));
         if (opv.getMin() != null)
            ep.set(org.endeavourhealth.imapi.vocabulary.SHACL.MINCOUNT,
                literal(opv.getMin().toString(),XSD.INTEGER));

      } else if (opv.getMin()!=null&&opv.getMin()>1) {
         ep.set(SHACL.MINCOUNT,
             literal(opv.getMin().toString(),XSD.INTEGER));
      }
      if (opv.getValueData() != null)
         ep.set(SHACL.HASVALUE, literal(opv.getValueData(),iri(opv.getValueType().getIri())));
      if (opv.getMinInclusive()!=null)
         ep.set(org.endeavourhealth.imapi.vocabulary
             .SHACL.MININCLUSIVE, literal(opv.getMinInclusive(),XSD.INTEGER));
      if (opv.getMinExclusive()!=null)
         ep.set(org.endeavourhealth.imapi.vocabulary
                 .SHACL.MINEXCLUSIVE, literal(opv.getMinExclusive(),XSD.INTEGER));
      if (opv.getMaxInclusive()!=null)
         ep.set(org.endeavourhealth.imapi.vocabulary.SHACL.MAXINCLUSIVE,
             literal(opv.getMaxInclusive(),XSD.INTEGER));
      if (opv.getMaxExclusive()!=null)
         ep.set(org.endeavourhealth.imapi.vocabulary.SHACL.MAXEXCLUSIVE,
             literal(opv.getMaxExclusive(),XSD.INTEGER));

      if (opv.getValueType() != null)
         ep.set(onRange,iri(opv.getValueType().getIri()));
      else if (opv.getPattern()!=null)
         ep.set(SHACL.PATTERN,literal(opv.getPattern()));
      else
         ep.set(onRange,mapClassExpression(opv.getExpression()));

      return ep;
   }

   public TTValue mapOWLProperty(PropertyValue opv) throws DataFormatException {
      TTNode ep = new TTNode();
      ep.set(RDF.TYPE, OWL.RESTRICTION);
      if (opv.getInverseOf() != null)
         ep.set(OWL.ONPROPERTY, new TTNode().set(OWL.INVERSEOF,iri(opv.getInverseOf().getIri())));
      else
         ep.set(OWL.ONPROPERTY, iri(opv.getProperty().getIri()));

      TTIriRef onRange=OWL.ONCLASS;
      if (opv.getValueType() != null) {
         Concept propType = discoveryMgr.getConcept(opv.getProperty().getIri());
         if (propType != null) {
            if (propType.getConceptType() == ConceptType.OBJECTPROPERTY)
               onRange = OWL.ONCLASS;
            else if (propType.getConceptType() == ConceptType.DATAPROPERTY)
               onRange = OWL.ONDATARANGE;
            else
               throw new DataFormatException("unknown property value type");
         } else
            onRange = OWL.ONCLASS;
      }

      //Complex logic to tidy up cardinalities which are overly set
      //Firstly if max is set then set max and min if min is set
      if (opv.getMax() != null) {
         ep.set(OWL.MAXCARDINALITY,
             literal(opv.getMax().toString(),XSD.INTEGER));
         if (opv.getMin() != null)
            ep.set(OWL.MINCARDINALITY,
                literal(opv.getMin().toString(),XSD.INTEGER));

      } else if (opv.getMin()!=null&&opv.getMin()>1) {
         ep.set(OWL.MINCARDINALITY,
             literal(opv.getMin().toString(),XSD.INTEGER));
      } else if (opv.getQuantificationType()!=null) {
         onRange = opv.getQuantificationType() == QuantificationType.ONLY
             ? OWL.ALLVALUESFROM
             : OWL.SOMEVALUESFROM;
      }
      if (opv.getValueData() != null)
         ep.set(OWL.HASVALUE, literal(opv.getValueData(),iri(opv.getValueType().getIri())));
      if (opv.getMinInclusive()!=null)
         ep.set(org.endeavourhealth.imapi.vocabulary
             .OWL.MININCLUSIVE, literal(opv.getMinInclusive(),XSD.INTEGER));
      if (opv.getMinExclusive()!=null)
         ep.set(org.endeavourhealth.imapi.vocabulary
             .OWL.MINEXCLUSIVE, literal(opv.getMinExclusive(),XSD.INTEGER));
      if (opv.getMaxInclusive()!=null)
         ep.set(org.endeavourhealth.imapi.vocabulary.OWL.MAXINCLUSIVE,
             literal(opv.getMaxInclusive(),XSD.INTEGER));
      if (opv.getMaxExclusive()!=null)
         ep.set(org.endeavourhealth.imapi.vocabulary.OWL.MAXEXCLUSIVE,
             literal(opv.getMaxExclusive(),XSD.INTEGER));
      if (opv.getValueType() != null)
         ep.set(onRange,iri(opv.getValueType().getIri()));
      else
         ep.set(onRange,mapClassExpression(opv.getExpression()));

      return ep;
   }



   private void mapConceptSets(TTConcept eConcept,Concept valueSet) throws DataFormatException {
      if (valueSet.getMember() != null) {
         TTArray sets= new TTArray();
         eConcept.set(IM.HAS_MEMBER,sets);
         for (ClassExpression member : valueSet.getMember()) {
            sets.add(mapClassExpression(member));
         }
      }
      if (valueSet.getMemberExpansion() != null) {
         TTArray sets= new TTArray();
         eConcept.set(IM.HAS_EXPANSION,sets);
         for (ConceptReference cref : valueSet.getMemberExpansion()) {
            sets.add(iri(cref.getIri()));
         }
      }
      //Note that there are no category sets in the old model.
   }


   private void mapConceptAxioms(TTConcept eConcept, Concept concept) throws DataFormatException {
      if (concept.getIsA()!=null){
         TTArray isas= new TTArray();
         eConcept.set(IM.IS_A,isas);
         for (ConceptReference isa:concept.getIsA())
            isas.add(iri(isa.getIri()));
      }
      if (concept.getSubClassOf() != null) {
         TTArray superClasses= new TTArray();
         eConcept.set(RDFS.SUBCLASSOF,superClasses);
         for (ClassExpression subClass : concept.getSubClassOf()) {
            superClasses.add(mapClassExpression(subClass));
         }
      }
      if (concept.getEquivalentTo() != null) {
         TTArray equiClasses = new TTArray();
         eConcept.set(OWL.EQUIVALENTCLASS,equiClasses);
         for (ClassExpression eqClass:concept.getEquivalentTo()){
            equiClasses.add(mapClassExpression(eqClass));
         }
      }
      if (concept.getMappedFrom()!=null){
         TTArray mappedClasses = new TTArray();
         eConcept.set(IM.MAPPED_FROM,mappedClasses);
         for (ConceptReference mappedClass:concept.getMappedFrom()){
            mappedClasses.add(TTIriRef.iri(mappedClass.getIri()));
         }

      }
   }


   private TTValue mapClassExpression(ClassExpression exp) throws DataFormatException {

      if (exp.getClazz() != null) {
         return (iri(exp.getClazz().getIri()));
      } else {
         if (exp.getComplementOf() != null) {
            return new TTNode()
                .set(OWL.COMPLEMENTOF, mapClassExpression(exp.getComplementOf()));
         } else if (exp.getIntersection() != null) {
            TTNode eInter= new TTNode();
            TTArray intersects= new TTArray();
            eInter.set(OWL.INTERSECTIONOF,intersects);
            for (ClassExpression i : exp.getIntersection()) {
               intersects.add(mapClassExpression(i));
            }
            return eInter;
         } else if (exp.getUnion() != null) {
            TTNode eUnion= new TTNode();
            TTArray unions= new TTArray();
            eUnion.set(OWL.UNIONOF,unions);
            for (ClassExpression i : exp.getUnion()) {
               unions.add(mapClassExpression(i));
            }
            return eUnion;
         } else if (exp.getPropertyValue() != null) {
            return mapOWLProperty(exp.getPropertyValue());

         } else if (exp.getObjectOneOf() != null) {
            TTNode eexp= new TTNode();
            TTArray oneOf= new TTArray();
            eexp.set(OWL.ONEOF,oneOf);
            for (ConceptReference on : exp.getObjectOneOf()) {
               oneOf.add(iri(on.getIri()));
            }
            return eexp;
         }
         else {
            throw new DataFormatException("unhandled expression");
         }
      }
   }


   private void mapObjectProperty(TTConcept eConcept, Concept op) throws DataFormatException {

      if (op.getSubObjectPropertyOf() != null) {
         TTArray superProps= new TTArray();
         eConcept.set(RDFS.SUBPROPERTYOF,superProps);
         for (PropertyAxiom sop : op.getSubObjectPropertyOf()) {
            superProps.add(iri(sop.getProperty().getIri()));
         }
      }

      if (op.getPropertyDomain() != null) {
         TTArray domain = new TTArray();
         eConcept.set(RDFS.DOMAIN, domain);
         for (ClassExpression ce : op.getPropertyDomain()) {
            domain.add(mapClassExpression(ce));
         }
      }

      if (op.getObjectPropertyRange() != null) {
         TTArray range = new TTArray();
         eConcept.set(RDFS.RANGE, range);
         for (ClassExpression ce : op.getObjectPropertyRange()) {
            range.add(mapClassExpression(ce));
         }
      }


      if (op.getInversePropertyOf() != null)
         eConcept.set(OWL.INVERSEOF,iri(op.getInversePropertyOf().getProperty().getIri()));

      TTArray type = eConcept.getAsArray(RDF.TYPE);

      if (op.getSubPropertyChain() != null) {
         TTArray eChain= new TTArray();
         eConcept.set(OWL.PROPERTYCHAIN,eChain);
         SubPropertyChain chain= op.getSubPropertyChain().stream().findFirst().get();
         chain.getProperty()
                .forEach(c->eChain.add(iri(c.getIri())));
      }

      if (op.getIsTransitive() != null)
         type.add(OWL.TRANSITIVE);

      if (op.getIsFunctional() != null)
         type.add(OWL.FUNCTIONAL);

      if (op.getIsReflexive() != null)
         type.add(OWL.REFLEXIVE);

      if (op.getIsSymmetric() != null)
         type.add(OWL.SYMMETRIC);
      
   }

   private void mapDataProperty(TTConcept eConcept, Concept dp) throws DataFormatException {

         if (dp.getSubDataPropertyOf() != null) {
            TTArray superProp= new TTArray();
            eConcept.set(RDFS.SUBPROPERTYOF,superProp);
            for (PropertyAxiom ax : dp.getSubDataPropertyOf()) {
               superProp.add(iri(ax.getProperty().getIri()));
            }
         }
         if (dp.getDataPropertyRange() != null) {
            TTArray ranges= new TTArray();
            eConcept.set(RDFS.RANGE,ranges);
            for (DataPropertyRange range : dp.getDataPropertyRange()) {
               ranges.add(iri(range.getDataType().getIri()));
            }
         }
         if (dp.getPropertyDomain() != null) {
            TTArray domains= new TTArray();
            eConcept.set(RDFS.RANGE,domains);
            for (ClassExpression domain : dp.getPropertyDomain()) {
               domains.add(mapClassExpression(domain));
            }
         }
   }

   private void mapDataType(TTConcept eConcept,Concept dt){
      if (dt.getDataTypeDefinition()!=null){
         TTNode eType= new TTNode();
         eConcept.set(OWL.EQUIVALENTCLASS,eType);
         DataTypeDefinition dtd= dt.getDataTypeDefinition();
         eType.set(OWL.ONDATATYPE,iri(dtd.getDataType().getIri()));
         TTNode restriction= new TTNode();
         eType.set(OWL.WITHRESTRICTIONS,restriction);
         if (dtd.getPattern()!=null) {
            restriction.set(XSD.PATTERN,literal(dtd.getPattern(),XSD.STRING));
         } else {
            if (dtd.getMinInclusive()!=null)
               restriction.set(XSD.MININCLUSIVE, literal(dtd.getMinInclusive(),XSD.INTEGER));
            if (dtd.getMaxInclusive()!=null)
               restriction.set(XSD.MAXINCLUSIVE, literal(dtd.getMaxInclusive(),XSD.INTEGER));
            if (dtd.getMinExclusive()!=null)
               restriction.set(XSD.MINEXCLUSIVE, literal(dtd.getMaxInclusive(),XSD.INTEGER));
            if (dtd.getMaxExclusive()!=null)
               restriction.set(XSD.MAXEXCLUSIVE, literal(dtd.getMaxInclusive(),XSD.INTEGER));
         }
      }

   }






   private void mapAnnotationProperty(TTConcept eConcept,Concept ap) {
      if (ap.getSubAnnotationPropertyOf() != null){
         TTArray superprops= new TTArray();
         for (PropertyAxiom ax : ap.getSubAnnotationPropertyOf()) {
            superprops.add(iri(ax.getProperty().getIri()));
         }
      }
   }
   private boolean isNullOrEmpty(List<?> list) {
      return (list == null || list.size() == 0);
   }

   private boolean isNullOrEmpty(Set<?> list) {
      return (list == null || list.size() == 0);
   }

   private void setPredicateTemplate(TTDocument document){
      Map<Class,List<String>> predTemplate = new HashMap<>();
      List<String> cTemplate= new ArrayList<>();
      cTemplate.add(IM.MODELTYPE.getIri());
      cTemplate.add(RDF.TYPE.getIri());
      cTemplate.add(RDFS.LABEL.getIri());
      cTemplate.add(RDFS.COMMENT.getIri());
      cTemplate.add(IM.CODE.getIri());
      cTemplate.add(IM.HAS_SCHEME.getIri());
      cTemplate.add(IM.STATUS.getIri());
      cTemplate.add(IM.IS_A.getIri());
      predTemplate.put(TTConcept.class,cTemplate);
      document.setPredicateTemplate(predTemplate);
   }

}
