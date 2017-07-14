package org.endeavourhealth.informationmodel.api.database.models;

import org.endeavourhealth.informationmodel.api.database.PersistenceManager;

import javax.persistence.*;

@Entity
@Table(name = "table_identity", schema = "information_model")
public class TableIdentityEntity {
    private String tableName;
    private Integer nextId;

    @Id
    @Column(name = "table_name", length = 50)
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Basic
    @Column(name = "next_id")
    public Integer getNextId() {
        return nextId;
    }

    public void setNextId(Integer nextId) {
        this.nextId = nextId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableIdentityEntity that = (TableIdentityEntity) o;

        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        if (nextId != null ? !nextId.equals(that.nextId) : that.nextId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (nextId != null ? nextId.hashCode() : 0);
        return result;
    }

    public static Integer getNextId(String tableName) throws Exception {
        return getNextIdBlock(tableName, 1);
    }

    public static Integer getNextIdBlock(String tableName, Integer idCount) throws Exception {
        Integer id = null;
        EntityManager entityManager = PersistenceManager.getEntityManager();

        while (id == null) {
            TableIdentityEntity ret = entityManager.find(TableIdentityEntity.class, tableName);

            if (ret == null)
                throw new IllegalArgumentException("No TableIdentity entry for " + tableName);

            Integer currId = ret.nextId;
            Integer nextId = currId + idCount;

            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery(
                "Update TableIdentityEntity " +
                    "Set nextId = :nextId " +
                    "Where tableName = :tableName " +
                    "and nextId = :currId ");
            query.setParameter("nextId", nextId);
            query.setParameter("tableName", tableName);
            query.setParameter("currId", currId);

            if (query.executeUpdate() == 1)
                id = currId;

            entityManager.getTransaction().commit();
        }

        entityManager.close();

        return id;
    }
}
