package org.endeavourhealth.informationmodel.api.database.models;

import javax.persistence.*;

/**
 * Created by studu on 04/07/2017.
 */
@Entity
@Table(name = "concept_range", schema = "information_model", catalog = "")
public class ConceptRangeEntity {
    private int id;
    private String operator;
    private Double lowerLimit;
    private Double upperLimit;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "operator", nullable = true, length = 2)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Basic
    @Column(name = "lower_limit", nullable = true, precision = 0)
    public Double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    @Basic
    @Column(name = "upper_limit", nullable = true, precision = 0)
    public Double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Double upperLimit) {
        this.upperLimit = upperLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConceptRangeEntity that = (ConceptRangeEntity) o;

        if (id != that.id) return false;
        if (operator != null ? !operator.equals(that.operator) : that.operator != null) return false;
        if (lowerLimit != null ? !lowerLimit.equals(that.lowerLimit) : that.lowerLimit != null) return false;
        if (upperLimit != null ? !upperLimit.equals(that.upperLimit) : that.upperLimit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (lowerLimit != null ? lowerLimit.hashCode() : 0);
        result = 31 * result + (upperLimit != null ? upperLimit.hashCode() : 0);
        return result;
    }
}
