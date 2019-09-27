package org.endeavourhealth.informationmanager.common.dal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

class BaseJDBCDAL implements BaseDAL {
    private static final Logger LOG = LoggerFactory.getLogger(BaseJDBCDAL.class);

    @SuppressWarnings("resource")
    final Connection conn = ConnectionPool.getInstance().pop();

    public void beginTransaction() throws SQLException {
        LOG.debug("Begin transaction");
        conn.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        LOG.debug("Commit");
        conn.commit();
    }

    public void rollback() throws SQLException {
        LOG.debug("Rollback");
        conn.rollback();
    }

    @Override
    public void close() throws Exception {
        LOG.debug("Close");
        try {
            conn.setAutoCommit(true);
        } finally {
            ConnectionPool.getInstance().push(this.conn);
        }
    }
}
