package org.endeavourhealth.informationmanager.common.transform.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.endeavourhealth.informationmanager.common.models.ConceptStatus;

import java.util.List;

@JsonPropertyOrder({"id","status","version","annotations"})
public interface IMAnnotated extends IMEntity{
    List<Annotation> getAnnotations();
    IMAnnotated setAnnotations(List<Annotation> annotationList);
    IMAnnotated addAnnotation(Annotation annotation);
}
