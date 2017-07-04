package org.endeavourhealth.informationmodel.api.database.models;

import javax.persistence.*;

/**
 * Created by studu on 04/07/2017.
 */
@Entity
@Table(name = "concept_expression", schema = "information_model", catalog = "")
public class ConceptExpressionEntity {
    private int id;
    private Integer order;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "order", nullable = true)
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConceptExpressionEntity that = (ConceptExpressionEntity) o;

        if (id != that.id) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }
}
