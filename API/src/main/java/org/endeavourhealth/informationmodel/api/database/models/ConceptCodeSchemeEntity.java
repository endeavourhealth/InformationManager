package org.endeavourhealth.informationmodel.api.database.models;

import javax.persistence.*;

/**
 * Created by studu on 04/07/2017.
 */
@Entity
@Table(name = "concept_code_scheme", schema = "information_model", catalog = "")
public class ConceptCodeSchemeEntity {
    private int id;
    private String code;
    private String scheme;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "code", nullable = true, length = 20)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "scheme", nullable = true, length = 45)
    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConceptCodeSchemeEntity that = (ConceptCodeSchemeEntity) o;

        if (id != that.id) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (scheme != null ? !scheme.equals(that.scheme) : that.scheme != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (scheme != null ? scheme.hashCode() : 0);
        return result;
    }
}
