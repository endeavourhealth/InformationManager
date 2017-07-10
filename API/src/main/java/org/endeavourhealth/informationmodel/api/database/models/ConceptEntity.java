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
    private String description;
    private Integer clazz;

    @Id
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 250)
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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConceptEntity that = (ConceptEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 10000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "class", nullable = true)
    public Integer getClazz() {
        return clazz;
    }

    public void setClazz(Integer clazz) {
        this.clazz = clazz;
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

        ConceptEntity conceptEntity = null;

        if (concept.getId() != null)
            conceptEntity = entityManager.find(ConceptEntity.class, concept.getId());
        else {
            conceptEntity = new ConceptEntity();
            Long id = TableIdentityEntity.getNextId("Concept");
            conceptEntity.setId(id);
            concept.setId(id);
        }

        entityManager.getTransaction().begin();
        conceptEntity.setName(concept.getName());
        conceptEntity.setStatus(concept.getStatus());
        conceptEntity.setShortName(concept.getShortName());
        conceptEntity.setDescription(concept.getDescription());
        entityManager.persist(conceptEntity);
        entityManager.getTransaction().commit();

        entityManager.close();

        return concept;
    }

    public static List<ConceptEntity> getCommonConcepts(Integer limit) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ConceptEntity> cq = cb.createQuery(ConceptEntity.class);
        Root<ConceptEntity> rootEntry = cq.from(ConceptEntity.class);

        cq.orderBy(cb.desc(rootEntry.get("name")));
        TypedQuery<ConceptEntity> query = entityManager.createQuery(cq);

        query.setMaxResults(limit);
        List<ConceptEntity> ret = query.getResultList();

        entityManager.close();

        return ret;
    }

    public static void bulkSaveConcepts(List<ConceptEntity> conceptEntities) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();
        int batchSize = 100000;
        entityManager.getTransaction().begin();
        long startTime = System.currentTimeMillis();

        for(int i = 0; i < conceptEntities.size(); ++i) {
            ConceptEntity conceptEntity = conceptEntities.get(i);
            entityManager.merge(conceptEntity);
            if(i % batchSize == 0) {
                //System.out.println(i + " completed");
                entityManager.flush();
                entityManager.clear();
                entityManager.getTransaction().commit();

                entityManager.getTransaction().begin();

            }
        }

        entityManager.flush();
        entityManager.clear();
        entityManager.getTransaction().commit();
        entityManager.close();
        long endTime = System.currentTimeMillis();
        System.out.println(conceptEntities.size() + " Added");
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }

    public static void deleteSnomedCodes() throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(
                "DELETE from ConceptEntity c where c.id >= :sno");
        query.setParameter("sno", (long)10000);

        int deletedCount = query.executeUpdate();

        entityManager.getTransaction().commit();

        System.out.println(deletedCount + " deleted");
        entityManager.close();
    }

    public static List<ConceptEntity> getRelationshipConcepts() throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();
        Query query = entityManager.createQuery(
                "Select c from ConceptEntity c " +
                        "where c.id >= :lowerConceptId " +
                        "and c.id < :higherConceptId " +
                        "order by c.id asc");
        query.setParameter("lowerConceptId", (long)100);
        query.setParameter("higherConceptId", (long)500);

        List<ConceptEntity> resultList = query.getResultList();

        entityManager.close();

        return resultList;
    }
}
