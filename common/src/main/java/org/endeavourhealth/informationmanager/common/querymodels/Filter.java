package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter {

    @JsonProperty(required = true)
    private List<ComplexAttribute> attribute;

    /**
     * Gets the value of the attribute list.
     *
     * To add a new item, do as follows:
     * getAttribute().add(newItem);
     *
     */
    public List<ComplexAttribute> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<ComplexAttribute>();
        }
        return this.attribute;
    }

}
