package org.endeavourhealth.informationmodel.api.database.models;

import org.endeavourhealth.informationmodel.api.database.PersistenceManager;
import org.endeavourhealth.informationmodel.api.json.JsonConcept;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Entity
@Table(name = "relationship_type", schema = "information_model")
public class RelationshipTypeEntity {
    private long id;
    private String name;
    private String description;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 10)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipTypeEntity that = (RelationshipTypeEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public static List<RelationshipTypeEntity> getAllRelationshipTypes() throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RelationshipTypeEntity> cq = cb.createQuery(RelationshipTypeEntity.class);
        Root<RelationshipTypeEntity> rootEntry = cq.from(RelationshipTypeEntity.class);
        CriteriaQuery<RelationshipTypeEntity> all = cq.select(rootEntry);
        TypedQuery<RelationshipTypeEntity> allQuery = entityManager.createQuery(all);
        List<RelationshipTypeEntity> ret = allQuery.getResultList();

        entityManager.close();

        return ret;
    }
}
