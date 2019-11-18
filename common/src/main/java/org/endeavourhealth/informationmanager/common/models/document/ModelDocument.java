package org.endeavourhealth.informationmanager.common.models.document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ModelDocument {
    private DocumentInfo documentInfo;
    private List<Concept> concept = new ArrayList<>();
    private List<ConceptDefinition> conceptDefinition = new ArrayList<>();
    private List<Synonym> synonym = new ArrayList<>();
    private List<PropertyDomain> propertyDomain = new ArrayList<>();
    private List<PropertyRange> propertyRange = new ArrayList<>();
    private List<StateDefinition> stateDefinition = new ArrayList<>();
    private List<Cohort> cohort = new ArrayList<>();
    private List<ValueSet> valueSet = new ArrayList<>();
    private DataSet dataSet;
    private List<DataTypeDefinition> dataTypeDefinition = new ArrayList<>();

    public DocumentInfo getDocumentInfo() {
        return documentInfo;
    }

    public ModelDocument setDocumentInfo(DocumentInfo documentInfo) {
        this.documentInfo = documentInfo;
        return this;
    }

    @JsonProperty("Concept")
    public List<Concept> getConcept() {
        return concept;
    }

    public ModelDocument setConcept(List<Concept> concept) {
        this.concept = concept;
        return this;
    }

    @JsonProperty("ConceptDefinition")
    public List<ConceptDefinition> getConceptDefinition() {
        return conceptDefinition;
    }

    public ModelDocument setConceptDefinition(List<ConceptDefinition> conceptDefinition) {
        this.conceptDefinition = conceptDefinition;
        return this;
    }

    public List<Synonym> getSynonym() {
        return synonym;
    }

    public ModelDocument setSynonym(List<Synonym> synonym) {
        this.synonym = synonym;
        return this;
    }

    public List<PropertyDomain> getPropertyDomain() {
        return propertyDomain;
    }

    public ModelDocument setPropertyDomain(List<PropertyDomain> propertyDomain) {
        this.propertyDomain = propertyDomain;
        return this;
    }

    public List<PropertyRange> getPropertyRange() {
        return propertyRange;
    }

    public ModelDocument setPropertyRange(List<PropertyRange> propertyRange) {
        this.propertyRange = propertyRange;
        return this;
    }

    public List<StateDefinition> getStateDefinition() {
        return stateDefinition;
    }

    public ModelDocument setStateDefinition(List<StateDefinition> stateDefinition) {
        this.stateDefinition = stateDefinition;
        return this;
    }

    public List<Cohort> getCohort() {
        return cohort;
    }

    public ModelDocument setCohort(List<Cohort> cohort) {
        this.cohort = cohort;
        return this;
    }

    public List<ValueSet> getValueSet() {
        return valueSet;
    }

    public ModelDocument setValueSet(List<ValueSet> valueSet) {
        this.valueSet = valueSet;
        return this;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public ModelDocument setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public List<DataTypeDefinition> getDataTypeDefinition() {
        return dataTypeDefinition;
    }

    public ModelDocument setDataTypeDefinition(List<DataTypeDefinition> dataTypeDefinition) {
        this.dataTypeDefinition = dataTypeDefinition;
        return this;
    }
}
