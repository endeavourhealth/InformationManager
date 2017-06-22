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
@Table(name = "concept", schema = "information_model")
public class ConceptEntity {
    private Long id;
    private String name;
    private Byte status;
    private String shortName;
    private String structureType;
    private Long structureId;
    private long count;
    private String description;

    @Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static ConceptEntity getConceptById(Long id) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        ConceptEntity ret = entityManager.find(ConceptEntity.class, id);
        entityManager.close();

        return ret;
    }

    public static List<ConceptEntity> getConceptsByName(String conceptName, Integer pageNumber, Integer pageSize) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConceptEntity> cq = cb.createQuery(ConceptEntity.class);
        Root<ConceptEntity> rootEntry = cq.from(ConceptEntity.class);

        Predicate predicate = cb.like(cb.upper(rootEntry.get("name")), "%" + conceptName.toUpperCase() + "%");

        cq.where(predicate);
        cq.orderBy(cb.asc(rootEntry.get("name")));
        TypedQuery<ConceptEntity> query = entityManager.createQuery(cq);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<ConceptEntity> ret = query.getResultList();

        entityManager.close();

        return ret;
    }

    public static Long getCountOfConceptSearch(String conceptName) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ConceptEntity> rootEntry = cq.from(ConceptEntity.class);

        Predicate predicate = cb.like(cb.upper(rootEntry.get("name")), "%" + conceptName.toUpperCase() + "%");

        cq.select((cb.countDistinct(rootEntry)));
        cq.where(predicate);
        Long ret = entityManager.createQuery(cq).getSingleResult();


        entityManager.close();

        return ret;
    }

    public static void deleteConcept(Integer conceptId) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        Long conceptLong = Integer.toUnsignedLong(conceptId);
        ConceptEntity conceptEntity = entityManager.find(ConceptEntity.class, conceptLong);
        entityManager.getTransaction().begin();
        entityManager.remove(conceptEntity);
        entityManager.getTransaction().commit();

        entityManager.close();
    }

    public static List<ConceptEntity> getConceptsFromList(List<Integer> conceptsIds) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConceptEntity> cq = cb.createQuery(ConceptEntity.class);
        Root<ConceptEntity> rootEntry = cq.from(ConceptEntity.class);

        Predicate predicate = rootEntry.get("id").in(conceptsIds);

        cq.where(predicate);
        TypedQuery<ConceptEntity> query = entityManager.createQuery(cq);

        List<ConceptEntity> ret = query.getResultList();

        entityManager.close();

        return ret;
    }

    public static JsonConcept saveConcept(JsonConcept concept) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        ConceptEntity conceptEntity = (concept.getId() == null) ? new ConceptEntity() : entityManager.find(ConceptEntity.class, concept.getId());
        entityManager.getTransaction().begin();
        conceptEntity.setName(concept.getName());
        conceptEntity.setStatus(concept.getStatus());
        conceptEntity.setShortName(concept.getShortName());
        conceptEntity.setDescription(concept.getDescription());
        conceptEntity.setStructureType(concept.getStructureType());
        conceptEntity.setStructureId(concept.getStructureId());
        conceptEntity.setCount(concept.getCount());
        entityManager.persist(conceptEntity);
        entityManager.getTransaction().commit();

        entityManager.close();

        concept.setId(conceptEntity.getId());

        return concept;
    }

    public static List<ConceptEntity> getCommonConcepts(Integer limit) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConceptEntity> cq = cb.createQuery(ConceptEntity.class);
        Root<ConceptEntity> rootEntry = cq.from(ConceptEntity.class);

        cq.orderBy(cb.desc(rootEntry.get("count")));
        TypedQuery<ConceptEntity> query = entityManager.createQuery(cq);

        query.setMaxResults(limit);
        List<ConceptEntity> ret = query.getResultList();

        entityManager.close();

        return ret;
    }

    public static void bulkSaveConcepts(List<ConceptEntity> conceptEntities) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();
        int batchSize = 50;
        entityManager.getTransaction().begin();

        for(int i = 0; i < conceptEntities.size(); ++i) {
            ConceptEntity conceptEntity = conceptEntities.get(i);
            entityManager.persist(conceptEntity);
            if(i % batchSize == 0) {
                //System.out.println(i + " completed");
                entityManager.flush();
                entityManager.clear();


            }
        }

        entityManager.getTransaction().commit();
        entityManager.close();
        System.out.println(conceptEntities.size() + " Added");
    }

    public static void deleteSnomedCodes() throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(
                "DELETE from ConceptEntity c where c.structureType = :sno");
        query.setParameter("sno", "sno");

        int deletedCount = query.executeUpdate();

        entityManager.getTransaction().commit();

        System.out.println(deletedCount + " deleted");
        entityManager.close();
    }

    public static List<ConceptEntity> getRelationshipConcepts() throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();
        Query query = entityManager.createQuery(
                "Select c from ConceptEntity c " +
                        "join ConceptRelationshipEntity cr on cr.sourceConcept = c.id " +
                        "where cr.targetConcept = :concept " +
                        "order by c.id asc");
        query.setParameter("concept", (long)2);

        List<ConceptEntity> resultList = query.getResultList();

        entityManager.close();

        return resultList;
    }
}
