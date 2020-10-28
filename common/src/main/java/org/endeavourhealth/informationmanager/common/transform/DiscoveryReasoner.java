package org.endeavourhealth.informationmanager.common.transform;


import org.apache.commons.collections.map.MultiValueMap;
import org.endeavourhealth.informationmanager.common.transform.model.ClassExpression;
import org.endeavourhealth.informationmanager.common.transform.model.Clazz;
import org.endeavourhealth.informationmanager.common.transform.model.Concept;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;


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
        if (ontology.getClazz()!=null)
            ontology.getClazz().forEach(cl-> classes.put(cl.getIri(),cl));
        if (ontology.getObjectProperty()!=null)
            ontology.getObjectProperty().forEach(op-> properties.put(op.getIri(),op));
        if (ontology.getDataProperty()!=null)
            ontology.getDataProperty().forEach((dp-> properties.put(dp.getIri(),dp)));
    }

    private void initialIsaTree() {
       /* classes.forEach((iri,cl)->{
            List<ClassExpression> eqList= ((Clazz) cl).getEquivalentTo();
            if (((Clazz) cl).getEquivalentTo()!=null)
        });
*/
    }



}
