package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ontology {
    private List<Namespace> namespace;
    private DocumentInfo documentInfo;
    private List<Clazz> clazz;
    private List<ObjectProperty> objectProperty;
    private List<DataProperty> dataProperty;
    private List<DataType> dataType;
    private List<AnnotationProperty> annotationProperty;
    private List<EntityKey> key;
    private List<ValueSet> valueSet;
    private List<DataSet> dataSet;

    @JsonProperty("Namespace")
    public List<Namespace> getNamespace() {
        return namespace;
    }

    public Ontology setNamespace(List<Namespace> namespace) {
        this.namespace = namespace;
        return this;
    }

    public Ontology addNamespace(Namespace namespace) {
        if (this.namespace == null)
            this.namespace = new ArrayList<>();

        this.namespace.add(namespace);
        return this;
    }

    @JsonProperty("DocumentInfo")
    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public Ontology setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
        return this;
    }

    @JsonProperty("Class")
    public List<Clazz> getClazz() {
        return clazz;
    }

    public Ontology setClazz(List<Clazz> clazz) {
        this.clazz = clazz;
        return this;
    }

    public Ontology addClazz(Clazz clazz) {
        if (this.clazz == null)
            this.clazz = new ArrayList<>();

        this.clazz.add(clazz);
        return this;
    }

    public Ontology deleteClazz(Clazz clazz) {
        if (this.clazz != null)
            this.clazz.remove(clazz);

        return this;
    }

    @JsonProperty("ObjectProperty")
    public List<ObjectProperty> getObjectProperty() {
        return objectProperty;
    }

    public Ontology setObjectProperty(List<ObjectProperty> objectProperty) {
        this.objectProperty = objectProperty;
        return this;
    }

    public Ontology addObjectProperty(ObjectProperty objectProperty) {
        if (this.objectProperty == null)
            this.objectProperty = new ArrayList<>();

        this.objectProperty.add(objectProperty);
        return this;
    }

    @JsonProperty("DataProperty")
    public List<DataProperty> getDataProperty() {
        return dataProperty;
    }

    public Ontology setDataProperty(List<DataProperty> dataProperty) {
        this.dataProperty = dataProperty;
        return this;
    }

    public Ontology addDataProperty(DataProperty dataProperty) {
        if (this.dataProperty == null)
            this.dataProperty = new ArrayList<>();
        this.dataProperty.add(dataProperty);
        return this;
    }

    @JsonProperty("DataType")
    public List<DataType> getDataType() {
        return dataType;
    }

    public Ontology setDataType(List<DataType> dataType) {
        this.dataType = dataType;
        return this;
    }

    public Ontology addDataType(DataType dataType) {
        if (this.dataType == null)
            this.dataType = new ArrayList<>();
        this.dataType.add(dataType);
        return this;
    }

    @JsonProperty("AnnotationProperty")
    public List<AnnotationProperty> getAnnotationProperty() {
        return annotationProperty;
    }

    public Ontology setAnnotationProperty(List<AnnotationProperty> annotationProperty) {
        this.annotationProperty = annotationProperty;
        return this;
    }

    public Ontology addAnnotationProperty(AnnotationProperty annotationProperty) {
        if (this.annotationProperty == null)
            this.annotationProperty = new ArrayList<>();

        this.annotationProperty.add(annotationProperty);
        return this;
    }

    @JsonProperty("Key")
    public List<EntityKey> getKey() {
        return key;
    }

    public Ontology setKey(List<EntityKey> key) {
        this.key = key;
        return this;
    }

    @JsonProperty("ValueSet")
    public List<ValueSet> getValueSet() {
        return valueSet;
    }

    public Ontology setValueSet(List<ValueSet> valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    @JsonProperty("DataSet")
    public List<DataSet> getDataSet() {
        return dataSet;
    }

    public Ontology setDataSet(List<DataSet> dataSet) {
        this.dataSet = dataSet;
        return this;
    }
}
