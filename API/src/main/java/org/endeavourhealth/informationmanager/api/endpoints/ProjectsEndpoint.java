package org.endeavourhealth.informationmanager.api.endpoints;

import org.endeavourhealth.informationmanager.common.dal.ProjectDAL;
import org.endeavourhealth.informationmanager.common.dal.ProjectJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("projects")
public class ProjectsEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectsEndpoint.class);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjects(@Context SecurityContext sc) throws Exception {
        LOG.debug("getProjects");

        try (ProjectDAL instanceDAL = new ProjectJDBCDAL()) {
            List<Project> result = instanceDAL.getProjects();

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }
}
