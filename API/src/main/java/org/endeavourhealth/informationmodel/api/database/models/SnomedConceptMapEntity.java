package org.endeavourhealth.informationmodel.api.database.models;

import org.endeavourhealth.informationmodel.api.database.PersistenceManager;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Entity
@Table(name = "snomed_concept_map", schema = "information_model")
public class SnomedConceptMapEntity {
    private long snomedId;
    private int conceptId;

    @Id
    @Column(name = "snomed_id", nullable = false)
    public long getSnomedId() {
        return snomedId;
    }

    public void setSnomedId(long snomedId) {
        this.snomedId = snomedId;
    }

    @Basic
    @Column(name = "concept_id", nullable = false)
    public int getConceptId() {
        return conceptId;
    }

    public void setConceptId(int conceptId) {
        this.conceptId = conceptId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SnomedConceptMapEntity that = (SnomedConceptMapEntity) o;

        if (snomedId != that.snomedId) return false;
        if (conceptId != that.conceptId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (snomedId ^ (snomedId >>> 32));
        result = 31 * result + conceptId;
        return result;
    }


    public static List<SnomedConceptMapEntity> getConceptIdFromSnomedList(List<Integer> snomedIds) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SnomedConceptMapEntity> cq = cb.createQuery(SnomedConceptMapEntity.class);
        Root<SnomedConceptMapEntity> rootEntry = cq.from(SnomedConceptMapEntity.class);

        Predicate predicate = rootEntry.get("snomedId").in(snomedIds);

        cq.where(predicate);
        TypedQuery<SnomedConceptMapEntity> query = entityManager.createQuery(cq);

        List<SnomedConceptMapEntity> ret = query.getResultList();

        entityManager.close();

        return ret;
    }

    public static List<SnomedConceptMapEntity> getAllSnomedMappings() throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SnomedConceptMapEntity> cq = cb.createQuery(SnomedConceptMapEntity.class);
        Root<SnomedConceptMapEntity> rootEntry = cq.from(SnomedConceptMapEntity.class);
        CriteriaQuery<SnomedConceptMapEntity> all = cq.select(rootEntry);
        TypedQuery<SnomedConceptMapEntity> allQuery = entityManager.createQuery(all);
        List<SnomedConceptMapEntity> ret = allQuery.getResultList();

        entityManager.close();

        return ret;
    }

    public static void bulkSaveSnomedMap(List<SnomedConceptMapEntity> snomedMap) throws Exception {
        EntityManager entityManager = PersistenceManager.getEntityManager();
        int batchSize = 100000;
        entityManager.getTransaction().begin();

        for(int i = 0; i < snomedMap.size(); ++i) {
            SnomedConceptMapEntity snomedMapEntity = snomedMap.get(i);
            entityManager.merge(snomedMapEntity);
            if(i % batchSize == 0) {
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
    }
}
