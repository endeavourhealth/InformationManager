package org.endeavourhealth.informationmodel.api.database.models;

import javax.persistence.*;

/**
 * Created by studu on 04/07/2017.
 */
@Entity
@Table(name = "concept_actual_range", schema = "information_model", catalog = "")
public class ConceptActualRangeEntity {
    private int id;
    private Double lowerLimit;
    private String rangeOperator;
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
    @Column(name = "lower_limit", nullable = true, precision = 0)
    public Double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    @Basic
    @Column(name = "range operator", nullable = true, length = 2)
    public String getRangeOperator() {
        return rangeOperator;
    }

    public void setRangeOperator(String rangeOperator) {
        this.rangeOperator = rangeOperator;
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

        ConceptActualRangeEntity that = (ConceptActualRangeEntity) o;

        if (id != that.id) return false;
        if (lowerLimit != null ? !lowerLimit.equals(that.lowerLimit) : that.lowerLimit != null) return false;
        if (rangeOperator != null ? !rangeOperator.equals(that.rangeOperator) : that.rangeOperator != null)
            return false;
        if (upperLimit != null ? !upperLimit.equals(that.upperLimit) : that.upperLimit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (lowerLimit != null ? lowerLimit.hashCode() : 0);
        result = 31 * result + (rangeOperator != null ? rangeOperator.hashCode() : 0);
        result = 31 * result + (upperLimit != null ? upperLimit.hashCode() : 0);
        return result;
    }
}
