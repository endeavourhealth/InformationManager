package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeRangeMap;
import com.sun.org.apache.xpath.internal.operations.Mult;
import org.apache.commons.collections.map.MultiValueMap;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.endeavourhealth.informationmanager.common.transform.model.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * A manager class to handle ontologies and cross ontology activities
 * <p> includes conversion to and from OWL2 syntaxes</p>
 * <p> includes load/generate/ transform /save ontology methods combining load/ save and conversion between Discovery JSON into OWL syntax or saves from OWL into Discovery
 * <p>Includes saving asserted ontology and generating inferred views via a reasoner</p>
 * @since version 1.0
 * @author David Stables Endeavour, Richard Collier Ergonomics ltd.
 */
public class DOWLManager {

 private List<Ontology> ontologies;
 private Map<String,Ontology> ontologyList;
 private Map<String,Map<String, MultiValueMap>> indexes;

 public DOWLManager() {
     ontologies= new ArrayList<>();
     ontologyList= new HashMap<>();
     indexes = new HashMap<>();

 }
    /**
     * Loads an ontology in Discovery syntax, transforms and returns in OWL2 format
     * @param inputFile file to input data
     * @return OWL2 ontology as an OWL manager containing an optional ontology and Prefix format with prefixes
     * @throws IOException
     * @throws OWLOntologyCreationException
     * @throws FileFormatException
     */
    public OWLOntologyManager loadOWLFromDiscovery (File inputFile) throws IOException, OWLOntologyCreationException, FileFormatException {
        ObjectMapper objectMapper = new ObjectMapper();
        Document document = objectMapper.readValue(inputFile, Document.class);
        return  new DiscoveryToOWL().transform(document);
    }

    /**
     * Loads a discovery document file in JSON syntax
     * @param inputFile  the file name to load
     * @return the Discovery document
     * @throws IOException
     * @throws OWLOntologyCreationException
     * @throws FileFormatException
     */
    public Ontology loadFromDiscovery (File inputFile) throws IOException, OWLOntologyCreationException, FileFormatException {
        ObjectMapper objectMapper = new ObjectMapper();
        Document document = objectMapper.readValue(inputFile, Document.class);
        return  document.getInformationModel();
    }

    /**
     * Transforms an OWL2 ontology into Discovery JSON syntax and saves it
     * @param ontology the OWL 2 ontology
     * @param filterNamespaces  namespaces that should be ommitted from the transform e.g. external classes
     * @param outputFile
     * @throws IOException
     */
    public void saveOWLAsDiscovery(OWLOntologyManager manager , OWLOntology ontology
                                    ,List<String> filterNamespaces, File outputFile) throws IOException {

        OWLDocumentFormat format= manager.getOntologyFormat(ontology);
        Document document = new OWLToDiscovery().transform(ontology, format,filterNamespaces);
        saveDiscovery(document,outputFile);
    }

    /**
     * Generates a Discovery Syntax inferred view from an OWL ontology and saves it
     * @param ontology  the OWL2 ontology
     * @param outputFile
     * @throws IOException
     */
    public void saveOWLAsInferred(OWLOntologyManager manager
                                , OWLOntology ontology,
                                   File outputFile) throws IOException {

        OWLDocumentFormat format= manager.getOntologyFormat(ontology);
        Document document = new OWLToDiscovery().generateInferredView(ontology,format);
        saveDiscovery(document,outputFile);

    }

    /**
     * Saves a Discovery JSON syntax ontology
     * @param document  the Discovery ontology document
     * @param outputFile
     * @throws IOException
     */
    public static void saveDiscovery(Document document,
                                  File outputFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(document);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(json);
        }
        catch (Exception e) {
            System.err.println("Unable to transform and save ontology in JSON format");
        }
    }

    public List<Ontology> getOntologies() {
        return ontologies;
    }

    public DOWLManager setOntologies(List<Ontology> ontologies) {
        this.ontologies = ontologies;
        return this;
    }

    /**
     * Adds to the ontology list managed by the manager
     * @param ontology
     * @return modified manager
     */
    public DOWLManager addOntology(Ontology ontology){
        ontologies.add(ontology);
        ontologyList.put(ontology.getIri(),ontology);
        return this;
    }

    /**
     * Gets a previously loaded/created ontology that has the specified ontology IR
     * @param ontologyId
     * @return The ontology, or null if not found
     */
    public Ontology getOntology(String ontologyId){
        return ontologyList.get(ontologyId);
    }

    /**
     * gets a named index from a particular ontology from the list of ontologies loaded
     * <p>Example of index name would be "concept.name" for a list of </p>
     * <p>if the index has not been generated all the indexes for that ontology  are first generated </p></o>
     * @return a multivalued list sorted by default in case sensitive string order
     */
    public MultiValueMap getIndexByName(String ontologyIri, String indexName){
       Map ontologyIndex= getIndexesForOntology(ontologyIri);
       if (ontologyIndex==null)
        return null;
       return (MultiValueMap) ontologyIndex.get(indexName);
    }

    /**
     * Gets the set of indexes for an ontology
     * @param ontologyIri  the iri of the ontology for an ontology already added to manager
     * @return a map of indexes for the ontology or null if the ontology is not loaded
     */
    public Map getIndexesForOntology(String ontologyIri){

        //Does the manager have the ontology?
        if (!ontologyList.containsKey(ontologyIri))
            return null;
        Ontology ontology = ontologyList.get(ontologyIri);
        //Index not created?
        if (!indexes.containsKey(ontologyIri)) {
            createIndexesForOntology(ontology);
        }

        return indexes.get(ontologyIri);
    }

    private void createIndexesForOntology(Ontology ontology) {

        //Creates the ontology indexes for this ontology and add to list of indexes
        Map<String, MultiValueMap> ontologyIndexes = new HashMap<>();
        indexes.put(ontology.getIri(), ontologyIndexes);

        //List of indexes to build for this ontology
        String[] indexNames = {"name"};

        //Initialise the indexes;
        for (String s : indexNames)
            ontologyIndexes.put(s, new MultiValueMap());

        //Get a list of concepts from the ontology
        List<Concept> conceptList = getConcepts(ontology);
        for (Concept c : conceptList)
            for (String indName : indexNames) {
                MultiValueMap index = ontologyIndexes.get(indName);
                if (indName.equals("name"))
                    if (c.getName() != null)
                        index.put(c.getName(), c.getIri());
            }
    }


    private List<Concept> getConcepts(Ontology ontology) {
        List<Concept> conceptList= new ArrayList<>();
        if (ontology.getClazz()!=null)
            for (Concept c:ontology.getClazz())
                conceptList.add(c);
        if (ontology.getObjectProperty()!=null)
            for (Concept c:ontology.getObjectProperty())
                conceptList.add(c);
        if (ontology.getDataProperty()!=null)
            for (Concept c:ontology.getDataProperty())
               conceptList.add(c);
        if (ontology.getDataType()!=null)
            for (Concept c:ontology.getDataType())
               conceptList.add(c);
        if (ontology.getAnnotationProperty()!=null)
            for (Concept c:ontology.getAnnotationProperty())
                conceptList.add(c);
        return conceptList;
    }

    public void rebuildIndexes() {
        indexes.clear();
        ontologies.forEach(o-> createIndexesForOntology(o));
    }

    public static Ontology createOntology(String iri) {
        Ontology ontology = new Ontology();
        ontology.setIri(iri);
        ontology.setEntailmentType(Entailment.ASSERTED);
        setDefaultNamespaces(ontology);
        DocumentInfo info = new DocumentInfo();
        info.setDocumentId(iri);
        return ontology;
    }

    private static void setDefaultNamespaces(Ontology ontology) {
        Map<String,String> ns= new HashMap<>();
        ns.put(":","http://www.DiscoveryDataService.org/InformationModel/Ontology#");
        ns.put("sn:","http://snomed.info/sct#");
        ns.put("owl:","http://www.w3.org/2002/07/owl#");
        ns.put("rdf:","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        ns.put("xml:","http://www.w3.org/XML/1998/namespace");
        ns.put("xsd:","http://www.w3.org/2001/XMLSchema#");
        ns.put("rdfs:","http://www.w3.org/2000/01/rdf-schema#");
        ns.forEach((a,b) -> ontology.addNamespace(new Namespace().setPrefix(a).setIri(b)));
    }

    public static Concept createConcept(String id, ConceptStatus status,
                                        Integer version, String iri,String name,
                                        String description,String code,
                                        String codeScheme){
        Concept concept= new Concept().setId(iri)
                .setStatus(status)
                .setVersion(version)
                .setIri(iri)
                .setName(name)
                .setDescription(description)
                 .setCode(code).setScheme(":891101000252101");
        return concept;
    }
    public static ObjectProperty conceptAsObjectProperty (Concept c){
        return (ObjectProperty) new ObjectProperty().setId(c.getId())
                .setStatus(c.getStatus())
                .setVersion(c.getVersion())
                .setIri(c.getIri())
                .setName(c.getName())
                .setCode(c.getCode())
                .setScheme(c.getScheme());
    }
    public static Annotation createAnnotation(String property, String value){
        Annotation annotation= new Annotation();
        annotation.setProperty(property);
        annotation.setValue(value);
        return annotation;
    }




}
