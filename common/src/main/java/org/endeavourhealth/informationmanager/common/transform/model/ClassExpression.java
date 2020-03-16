package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ClassExpression {
    private List<Annotation> annotation;
    private Boolean inferred;
    private String clazz;
    private List<ClassExpression> intersection;
    private List<ClassExpression> union;
    private ClassExpression compliment;
    private OPERestriction objectSome;
    private OPERestriction objectOnly;
    private OPECardinalityRestriction objectCardinality;
    private IndividualValueRestriction objectHasValue;
    private DPERestriction dataSome;
    private DPERestriction dataOnly;
    private DPECardinalityRestriction dataCardinality;
    private DataValueRestriction dataHasValue;

    @JsonProperty("Annotation")
    public List<Annotation> getAnnotation() {
        return annotation;
    }

    public ClassExpression setAnnotation(List<Annotation> annotation) {
        this.annotation = annotation;
        return this;
    }

    @JsonProperty("Inferred")
    public Boolean getInferred() {
        return inferred;
    }

    public ClassExpression setInferred(Boolean inferred) {
        this.inferred = inferred;
        return this;
    }

    @JsonProperty("Class")
    public String getClazz() {
        return clazz;
    }

    public ClassExpression setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    @JsonProperty("Intersection")
    public List<ClassExpression> getIntersection() {
        return intersection;
    }

    public ClassExpression setIntersection(List<ClassExpression> intersection) {
        this.intersection = intersection;
        return this;
    }

    public ClassExpression addIntersection(ClassExpression intersection) {
        if (this.intersection == null)
            this.intersection = new ArrayList<>();
        this.intersection.add(intersection);
        return this;
    }

    @JsonProperty("Union")
    public List<ClassExpression> getUnion() {
        return union;
    }

    public ClassExpression setUnion(List<ClassExpression> union) {
        this.union = union;
        return this;
    }

    @JsonProperty("Compliment")
    public ClassExpression getCompliment() {
        return compliment;
    }

    public ClassExpression setCompliment(ClassExpression compliment) {
        this.compliment = compliment;
        return this;
    }

    @JsonProperty("ObjectSome")
    public OPERestriction getObjectSome() {
        return objectSome;
    }

    public ClassExpression setObjectSome(OPERestriction objectSome) {
        this.objectSome = objectSome;
        return this;
    }

    @JsonProperty("ObjectOnly")
    public OPERestriction getObjectOnly() {
        return objectOnly;
    }

    public ClassExpression setObjectOnly(OPERestriction objectOnly) {
        this.objectOnly = objectOnly;
        return this;
    }

    @JsonProperty("ObjectCardinality")
    public OPECardinalityRestriction getObjectCardinality() {
        return objectCardinality;
    }

    public ClassExpression setObjectCardinality(OPECardinalityRestriction objectCardinality) {
        this.objectCardinality = objectCardinality;
        return this;
    }

    @JsonProperty("ObjectHasValue")
    public IndividualValueRestriction getObjectHasValue() {
        return objectHasValue;
    }

    public ClassExpression setObjectHasValue(IndividualValueRestriction objectHasValue) {
        this.objectHasValue = objectHasValue;
        return this;
    }

    @JsonProperty("DataSome")
    public DPERestriction getDataSome() {
        return dataSome;
    }

    public ClassExpression setDataSome(DPERestriction dataSome) {
        this.dataSome = dataSome;
        return this;
    }

    @JsonProperty("DataOnly")
    public DPERestriction getDataOnly() {
        return dataOnly;
    }

    public ClassExpression setDataOnly(DPERestriction dataOnly) {
        this.dataOnly = dataOnly;
        return this;
    }

    @JsonProperty("DataCardinality")
    public DPECardinalityRestriction getDataCardinality() {
        return dataCardinality;
    }

    public ClassExpression setDataCardinality(DPECardinalityRestriction dataCardinality) {
        this.dataCardinality = dataCardinality;
        return this;
    }

    @JsonProperty("DataHasValue")
    public DataValueRestriction getDataHasValue() {
        return dataHasValue;
    }

    public ClassExpression setDataHasValue(DataValueRestriction dataHasValue) {
        this.dataHasValue = dataHasValue;
        return this;
    }
}
