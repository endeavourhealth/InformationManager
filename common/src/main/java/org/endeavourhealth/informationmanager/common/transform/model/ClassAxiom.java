package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

import java.util.ArrayList;
import java.util.List;

public class ClassAxiom extends ClassExpression implements IMAnnotated{
    private ConceptStatus status;
    private Integer version;
    private List<Annotation> annotationList;

    @Override
    public ConceptStatus getStatus() {
        return status;
    }

    @Override
    public IMEntity setStatus(ConceptStatus status) {
        this.status=status;
        return this;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public IMEntity setVersion(Integer version) {
        this.version= version;
        return this;
    }



    @Override
    @JsonProperty("annotations")
    public List<Annotation> getAnnotations() {
        return annotationList;
    }

    @Override
    public IMAnnotated setAnnotations(List<Annotation> annotationList) {
        this.annotationList= annotationList;
        return this;
    }

    @Override
    public IMAnnotated addAnnotation(Annotation annotation) {
        if (annotationList==null)
            annotationList= new ArrayList<>();
        annotationList.add(annotation);
        return this;
    }
}
