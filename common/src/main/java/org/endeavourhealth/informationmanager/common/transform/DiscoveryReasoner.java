package org.endeavourhealth.informationmanager.common.transform;


import org.apache.commons.collections.map.MultiValueMap;
import org.endeavourhealth.informationmanager.common.transform.model.Concept;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;

import java.util.HashMap;


/**
 * Computes "is a" relationships from a Discovery ontology module
 */
public class DiscoveryReasoner {
    private Ontology ontology;
    private HashMap<String,Concept> classes;
    private HashMap<String, Concept> properties;
    private MultiValueMap isaTree;


    public DiscoveryReasoner(Ontology ontology){
        this.ontology= ontology;
        classes= new HashMap<>();
        properties= new HashMap<>();
        isaTree = new MultiValueMap();
    }

    public Ontology classify()
    {
        indexConcepts();
        initialIsaTree();
        Ontology inferred= DOWLManager.createOntology(ontology.getIri(),ontology.getModule());
        return inferred;
    }

    private void indexConcepts() {
        if (ontology.getConcept()!=null)
            ontology.getConcept().forEach(cl-> classes.put(cl.getIri(),cl));
    }

    private void initialIsaTree() {
       /* classes.forEach((iri,cl)->{
            List<ClassExpression> eqList= ((Clazz) cl).getEquivalentTo();
            if (((Clazz) cl).getEquivalentTo()!=null)
        });
*/
    }



}
