package org.endeavourhealth.informationmodel.api.json;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonConceptRelationship {
    private Integer id = null;
    private Integer source_concept = null;
    private Integer target_concept = null;
    private String target_label = null;
    private Integer relationship_order = null;
    private Integer relationship_type = null;
    private Integer count = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSource_concept() {
        return source_concept;
    }

    public void setSource_concept(Integer source_concept) {
        this.source_concept = source_concept;
    }

    public Integer getTarget_concept() {
        return target_concept;
    }

    public void setTarget_concept(Integer target_concept) {
        this.target_concept = target_concept;
    }

    public String getTarget_label() {
        return target_label;
    }

    public void setTarget_label(String target_label) {
        this.target_label = target_label;
    }

    public Integer getRelationship_order() {
        return relationship_order;
    }

    public void setRelationship_order(Integer relationship_order) {
        this.relationship_order = relationship_order;
    }

    public Integer getRelationship_type() {
        return relationship_type;
    }

    public void setRelationship_type(Integer relationship_type) {
        this.relationship_type = relationship_type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
