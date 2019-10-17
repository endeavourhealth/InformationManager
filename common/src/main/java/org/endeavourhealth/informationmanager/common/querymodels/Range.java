package org.endeavourhealth.informationmanager.common.querymodels;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Range {

    @JsonProperty(required = true)
    private ValueComparison from;

    @JsonProperty(required = true)
    private ValueComparison to;

    public ValueComparison getFrom() {
        return from;
    }

    public void setFrom(ValueComparison from) {
        this.from = from;
    }

    public ValueComparison getTo() {
        return to;
    }

    public void setTo(ValueComparison to) {
        this.to = to;
    }

}
