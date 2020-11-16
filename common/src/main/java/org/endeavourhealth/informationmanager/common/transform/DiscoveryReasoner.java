package org.endeavourhealth.informationmanager.common.transform;


import org.apache.commons.collections.map.MultiValueMap;
import org.endeavourhealth.imapi.model.*;
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
    private HashMap<String,Concept> classes;
    private HashMap<String, Concept> properties;
    private HashMap<String,ConceptReferenceNode> nodeMap;
    private Set<ConceptReferenceNode> nodeSet;
    private boolean consistent;
    private OWLReasoner owlReasoner;




    public DiscoveryReasoner(Ontology ontology){
        this.ontology= ontology;
        classes= new HashMap<>();
        properties= new HashMap<>();
        nodeMap= new HashMap<>();
        nodeSet= new HashSet<>();
    }

    /**
     * Classifies an ontology using an OWL Reasoner
     * @return set of child-> parent nodes
     * @throws Exception
     */

    public Set<ConceptReferenceNode> classify() throws Exception {
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
            return nodeSet;
        }
        return null;
    }


    private void addSubClasses(OWLClass owlParentClass ,ConceptReferenceNode parentNode) {
        owlReasoner.getSubClasses(owlParentClass, true)
            .forEach(cn -> addChildClassNode(cn,parentNode));
    }
    private void addChildClassNode(Node<OWLClass> owlChildNode, ConceptReferenceNode parentNode) {
        OWLClass owlChildClass = owlChildNode.getRepresentativeElement();
        String childIri = DOWLManager.getShortIri(ontology.getNamespace(),
            owlChildClass.getIRI().toString());
        if (!childIri.equals("owl:Nothing")) {
            ConceptReferenceNode childNode = new ConceptReferenceNode();
            childNode.setIri(childIri);
            childNode.setModuleId(ontology.getModule());
            nodeSet.add(childNode);
            if (parentNode != null)
                childNode.addParent(parentNode);
            addSubClasses(owlChildClass, childNode);
        }
    }

    private void addSubObjectProperties(OWLObjectPropertyExpression owlOp ,ConceptReferenceNode parentNode) {
        owlReasoner.getSubObjectProperties(owlOp, true)
            .forEach(cn -> addChildOpNode(cn,parentNode));
    }
    private void addChildOpNode(Node<OWLObjectPropertyExpression> owlChildNode, ConceptReferenceNode parentNode){
        OWLObjectPropertyExpression owlChildOp= owlChildNode.getRepresentativeElement();
        if (owlChildOp.isAnonymous())
            return;
        String childIri = DOWLManager.getShortIri(ontology.getNamespace(),
            owlChildOp.asOWLObjectProperty().getIRI().toString());
        if (!childIri.equals("owl:bottomObjectProperty")) {
            ConceptReferenceNode childNode= new ConceptReferenceNode();
            childNode.setIri(childIri);
            childNode.setModuleId(ontology.getModule());
            nodeSet.add(childNode);
            if (parentNode!=null)
                childNode.addParent(parentNode);
            addSubObjectProperties(owlChildOp,childNode);
        }
    }

    private void addSubDataProperties(OWLDataProperty owlDp ,ConceptReferenceNode parentNode) {
        owlReasoner.getSubDataProperties(owlDp, true)
            .forEach(cn -> addChildDpNode(cn,parentNode));
    }
    private void addChildDpNode(Node<OWLDataProperty> owlChildNode, ConceptReferenceNode parentNode){
        OWLDataProperty owlChildDp= owlChildNode.getRepresentativeElement();
        String childIri = DOWLManager.getShortIri(ontology.getNamespace(),
            owlChildDp.getIRI().toString());
        if (!childIri.equals("owl:bottomDataProperty")) {
            ConceptReferenceNode childNode= new ConceptReferenceNode();
            childNode.setIri(childIri);
            childNode.setModuleId(ontology.getModule());
            nodeSet.add(childNode);
            if (parentNode!=null)
                childNode.addParent(parentNode);
            addSubDataProperties(owlChildDp,childNode);
        }
    }




}
