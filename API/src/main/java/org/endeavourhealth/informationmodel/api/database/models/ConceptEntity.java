package org.endeavourhealth.informationmodel.api.database.models;

import org.endeavourhealth.informationmodel.api.database.PersistenceManager;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Entity
@Table(name = "concept", schema = "information_model", catalog = "")
public class ConceptEntity {
    private long id;
    private String name;
    private Byte status;
    private String shortName;
    private String structureType;
    private Long structureId;
    private long count;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 250)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "short_name", nullable = true, length = 125)
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Basic
    @Column(name = "structure_type", nullable = true, length = 3)
    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    @Basic
    @Column(name = "structure_id", nullable = true)
    public Long getStructureId() {
        return structureId;
    }

    public void setStructureId(Long structureId) {
        this.structureId = structureId;
    }

    @Basic
    @Column(name = "count", nullable = false)
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConceptEntity that = (ConceptEntity) o;

        if (id != that.id) return false;
        if (count != that.count) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) return false;
        if (structureType != null ? !structureType.equals(that.structureType) : that.structureType != null)
            return false;
        if (structureId != null ? !structureId.equals(that.structureId) : that.structureId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        result = 31 * result + (structureType != null ? structureType.hashCode() : 0);
        result = 31 * result + (structureId != null ? structureId.hashCode() : 0);
        result = 31 * result + (int) (count ^ (count >>> 32));
        return result;
    }

    public static List<ConceptEntity> getAllConcepts() throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConceptEntity> cq = cb.createQuery(ConceptEntity.class);
        Root<ConceptEntity> rootEntry = cq.from(ConceptEntity.class);
        CriteriaQuery<ConceptEntity> all = cq.select(rootEntry);
        TypedQuery<ConceptEntity> allQuery = entityManager.createQuery(all);
        List<ConceptEntity> ret = allQuery.getResultList();

        entityManager.close();

        return ret;
    }
}
