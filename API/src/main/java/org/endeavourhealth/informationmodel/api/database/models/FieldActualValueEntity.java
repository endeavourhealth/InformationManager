package org.endeavourhealth.informationmodel.api.database.models;

import javax.persistence.*;

/**
 * Created by studu on 04/07/2017.
 */
@Entity
@Table(name = "field_actual_value", schema = "information_model", catalog = "")
public class FieldActualValueEntity {
    private int id;
    private Integer weighting;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "weighting", nullable = true)
    public Integer getWeighting() {
        return weighting;
    }

    public void setWeighting(Integer weighting) {
        this.weighting = weighting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldActualValueEntity that = (FieldActualValueEntity) o;

        if (id != that.id) return false;
        if (weighting != null ? !weighting.equals(that.weighting) : that.weighting != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (weighting != null ? weighting.hashCode() : 0);
        return result;
    }
}
