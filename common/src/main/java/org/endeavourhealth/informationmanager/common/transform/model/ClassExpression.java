package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ClassExpression extends IMEntity {
    private List<Annotation> annotation;
    private Boolean inferred;
    private String clazz;
    private List<ClassExpression> intersection;
    private List<ClassExpression> union;
    private ClassExpression complement;
    private OPECardinalityRestriction propertyObject;
    private IndividualValueRestriction objectHasValue;
    private DPECardinalityRestriction propertyData;
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
    public ClassExpression getComplement() {
        return complement;
    }

    public ClassExpression setComplement(ClassExpression complement) {
        this.complement = complement;
        return this;
    }




    @JsonProperty("PropertyObject")
    public OPECardinalityRestriction getPropertyObject() {
        return propertyObject;
    }

    public ClassExpression setPropertyObject(OPECardinalityRestriction propertyObject) {
        this.propertyObject = propertyObject;
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



   

    @JsonProperty("PropertyData")
    public DPECardinalityRestriction getPropertyData() {
        return propertyData;
    }

    public ClassExpression setPropertyData(DPECardinalityRestriction propertyData) {
        this.propertyData = propertyData;
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
