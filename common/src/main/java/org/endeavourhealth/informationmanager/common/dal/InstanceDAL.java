package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.Instance;

import java.sql.SQLException;
import java.util.List;

public interface InstanceDAL {
    List<Instance> getInstances() throws SQLException;
    Instance getInstance(int instanceDbid) throws SQLException;
}
