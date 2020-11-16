package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.Ontology;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Holds an im memory set of indexes by IRI or annotation property to the Discovery ontology concepts
 */
public class OntologyIndex {

    private Ontology ontology;
    private Hashtable<String,Hashtable> indexes;

    public OntologyIndex(){
        this.indexes= new Hashtable<>();
    }


    /**
     * Creates default Unique indexes for the string properties of concepts in a set of ontology modules. As classes properties and individuals may share the same IRI
     * they are considered seperate tables each with their field indexes
     * <p> This can support multiple ontologies/modules but all indexed values must be unique</p>
     * @param ontology
     */
    public OntologyIndex createDefaultIndexes(Ontology ontology){
        this.ontology=ontology;
        buildIndex("clazz","iri");
        return this;

    }

    private void buildIndex(String conceptType,String propertyName){
        String indexName= conceptType+"."+ propertyName;
        if (indexes.get(indexName)==null){
            Hashtable<String,Object> index= new Hashtable<>();
            indexes.put(indexName,index);
        }
        ArrayList<Object> table= (ArrayList) get(ontology,conceptType);
        Hashtable<String,Object> index= indexes.get(indexName);
        for (Object object:table){
            String value=get(object,propertyName);
            index.put(value,object);
        }

    }
    @SuppressWarnings("unchecked")
    public static <V> V get(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (V) field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }
    public static boolean set(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    public Object getUniqueObject(String conceptType,String propertyName, String value){
        return indexes.get(conceptType+"."+ propertyName).get(value);
    }

    public Hashtable getIndex(String conceptType,String propertyName){
        return indexes.get(conceptType+"."+ propertyName);
    }

}
