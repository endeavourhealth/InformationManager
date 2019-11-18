package org.endeavourhealth.informationmanager.common.models.document;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Domain {
    private Integer minCardinality;
    private Integer maxCardinality;
    private String clazz;
    private Integer maxInGroup;
    private Operator operator;
    // private List<String> queryCharacteristic = new ArrayList<>();
    // private SimpleExpressionConstraint range;

    public Integer getMinCardinality() {
        return minCardinality;
    }

    public Domain setMinCardinality(Integer minCardinality) {
        this.minCardinality = minCardinality;
        return this;
    }

    public Integer getMaxCardinality() {
        return maxCardinality;
    }

    public Domain setMaxCardinality(Integer maxCardinality) {
        this.maxCardinality = maxCardinality;
        return this;
    }

    @JsonProperty("class")
    public String getClazz() {
        return clazz;
    }

    public Domain setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }

    public Integer getMaxInGroup() {
        return maxInGroup;
    }

    public Domain setMaxInGroup(Integer maxInGroup) {
        this.maxInGroup = maxInGroup;
        return this;
    }

    public Operator getOperator() {
        return operator;
    }

    public Domain setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    /*
    public SimpleExpressionConstraint getRange() {
        return range;
    }

    public Domain setRange(SimpleExpressionConstraint range) {
        this.range = range;
        return this;
    }
*/
}
