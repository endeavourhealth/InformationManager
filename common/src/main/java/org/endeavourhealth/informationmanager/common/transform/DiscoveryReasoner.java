package org.endeavourhealth.informationmanager.common.transform;


import org.apache.commons.collections.map.MultiValueMap;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;

import java.util.*;


/**
 * Computes "is a" relationships from a Discovery ontology module
 */
public class DiscoveryReasoner {
    private Ontology ontology;
    private HashMap<String,Concept> conceptMap;
    private boolean consistent;
    private OWLReasoner owlReasoner;




    /**
     * Classifies an ontology using an OWL Reasoner
     * @return set of child-> parent nodes
     * @throws Exception
     */

    public Ontology classify(Ontology ontology) throws OWLOntologyCreationException, FileFormatException {
        this.ontology=ontology;
        if (ontology.getConcept()==null)
            return null;
        conceptMap= new HashMap<>();
        //builds concept map for later look up
        ontology.getConcept().forEach(c-> conceptMap.put(c.getIri(),c));
        DiscoveryToOWL transformer = new DiscoveryToOWL();
        OWLOntologyManager owlManager= transformer.transform(ontology);
        Set<OWLOntology> owlOntologySet= owlManager.getOntologies();
        Optional<OWLOntology> owlOntology = owlOntologySet.stream().findFirst();
        if (owlOntology.isPresent()) {
            OWLReasonerConfiguration config = new SimpleConfiguration();
            OWLOntology o= owlOntology.get();
            owlReasoner= new FaCTPlusPlusReasonerFactory().createReasoner(o, config);
            owlReasoner.precomputeInferences();
            if (!owlReasoner.isConsistent()){
                consistent=false;
                return null;
            }
            consistent=true;
            //Steps down the trees and populates a concept reference set.
            OWLClass topClass = owlReasoner.getTopClassNode().getRepresentativeElement();
            addSubClasses(topClass,null);

            OWLObjectProperty topOp = owlReasoner.getTopObjectPropertyNode()
                 .getRepresentativeElement().asOWLObjectProperty();
            if (topOp!=null)
                addSubObjectProperties(topOp,null);

            OWLDataProperty topDp = owlReasoner.getTopDataPropertyNode().getRepresentativeElement().asOWLDataProperty();
            if (topDp!=null)
                addSubDataProperties(topDp,null);

            owlReasoner.dispose();
            return ontology;
        }
        return null;
    }




    private void addSubClasses(OWLClass owlParentClass ,Concept parentNode) {
        owlReasoner.getSubClasses(owlParentClass, true)
            .forEach(cn -> addChildClassNode(cn,parentNode));
    }
    private void addChildClassNode(Node<OWLClass> owlChildNode, Concept parentNode) {
        OWLClass owlChildClass = owlChildNode.getRepresentativeElement();
        String childIri = DOWLManager.getShortIri(ontology.getNamespace(),
            owlChildClass.getIRI().toString());
        if (!childIri.equals("owl:Nothing")) {
            Concept childNode = conceptMap.get(childIri);
            checkAndAddIsa(parentNode,childNode);
            addSubClasses(owlChildClass, childNode);
        }
    }

    private void addSubObjectProperties(OWLObjectPropertyExpression owlOp ,Concept parentNode) {
        owlReasoner.getSubObjectProperties(owlOp, true)
            .forEach(cn -> addChildOpNode(cn,parentNode));
    }
    private void addChildOpNode(Node<OWLObjectPropertyExpression> owlChildNode, Concept parentNode){
        OWLObjectPropertyExpression owlChildOp= owlChildNode.getRepresentativeElement();
        if (owlChildOp.isAnonymous())
            return;
        String childIri = DOWLManager.getShortIri(ontology.getNamespace(),
            owlChildOp.asOWLObjectProperty().getIRI().toString());
        if (!childIri.equals("owl:bottomObjectProperty")) {
            Concept childNode= conceptMap.get(childIri);
            checkAndAddIsa(parentNode,childNode);
            addSubObjectProperties(owlChildOp,childNode);
        }
    }

    private void addSubDataProperties(OWLDataProperty owlDp ,Concept parentNode) {
        owlReasoner.getSubDataProperties(owlDp, true)
            .forEach(cn -> addChildDpNode(cn,parentNode));
    }
    private void addChildDpNode(Node<OWLDataProperty> owlChildNode, Concept parentNode){
        OWLDataProperty owlChildDp= owlChildNode.getRepresentativeElement();
        String childIri = DOWLManager.getShortIri(ontology.getNamespace(),
            owlChildDp.getIRI().toString());
        if (!childIri.equals("owl:bottomDataProperty")) {
            Concept childNode= conceptMap.get(childIri);
            checkAndAddIsa(parentNode,childNode);
            addSubDataProperties(owlChildDp,childNode);
        }
    }

    private void checkAndAddIsa(Concept parentNode, Concept childNode){
        if (parentNode!=null) {
            ConceptReference conref = new ConceptReference(parentNode.getIri());
            if (childNode.getIsA() == null)
                childNode.addIsa(conref);
            else if (!childNode.getIsA().stream().anyMatch(s->s.getIri().equals(parentNode.getIri())))
                childNode.addIsa(conref);
        }

    }




}
