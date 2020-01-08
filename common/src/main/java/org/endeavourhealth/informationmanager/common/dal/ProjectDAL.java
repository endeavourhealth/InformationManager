package org.endeavourhealth.informationmanager.common.dal;

import org.endeavourhealth.informationmanager.common.models.Project;

import java.util.List;

public interface ProjectDAL extends BaseDAL {
    List<Project> getProjects() throws Exception;
}
