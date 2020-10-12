package org.endeavourhealth.informationmanager.common.transform;

import org.apache.commons.collections.map.MultiValueMap;
import org.endeavourhealth.informationmanager.common.transform.model.Concept;
import org.endeavourhealth.informationmanager.common.transform.model.Ontology;

import java.util.Map;

public class ClassGenerator {
    private MultiValueMap conceptMap;

    public String generateClasses(Ontology ontology, Map<String,String> conceptClasses,String outputFolder) {

    conceptMap= DOWLManager.getConceptMap(ontology);


        return "xxxx.java";
    }
}
