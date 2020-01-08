package org.endeavourhealth.informationmanager.api.endpoints;

import org.endeavourhealth.informationmanager.common.dal.WorkflowJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Task;
import org.endeavourhealth.informationmanager.common.models.TaskCategory;
import org.endeavourhealth.informationmanager.common.models.TaskSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("workflow")
public class WorkflowEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowEndpoint.class);

    /******************** CATEGORIES ********************/
    @GET
    @Path("/categories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories(@Context SecurityContext sc) throws Exception {
        LOG.debug("getCategories");

        try (WorkflowJDBCDAL wfDAL = new WorkflowJDBCDAL()) {
            List<TaskCategory> result = wfDAL.getCategories();

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    /******************** TASKS ********************/
    @GET
    @Path("/categories/{categoryDbid}/tasks/summary")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasks(@Context SecurityContext sc,
                             @PathParam("categoryDbid") Byte categoryDbid) throws Exception {
        LOG.debug("getTasks");

        try (WorkflowJDBCDAL wfDAL = new WorkflowJDBCDAL()) {
            List<TaskSummary> result = wfDAL.getTasks(categoryDbid);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/tasks/{taskDbid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(@Context SecurityContext sc,
                            @PathParam("taskDbid") Integer taskDbid) throws Exception {
        LOG.debug("getTask");

        try (WorkflowJDBCDAL wfDAL = new WorkflowJDBCDAL()) {
            Task result = wfDAL.getTask(taskDbid);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @POST
    @Path("/tasks/{taskDbid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTask(@Context SecurityContext sc,
                            @PathParam("taskDbid") Integer taskDbid,
                               String taskJson) throws Exception {
        LOG.debug("updateTask");

        try (WorkflowJDBCDAL wfDAL = new WorkflowJDBCDAL()) {
            wfDAL.updateTask(taskDbid, taskJson);

            return Response
                .ok()
                .build();
        }
    }

/*    @POST
    @Path("/draftConcept/analyse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response analyseDraftConcept(@Context SecurityContext sc,
                            String conceptJson) throws Exception {
        LOG.debug("analyseDraftConcept");

        try (WorkflowJDBCDAL wfDAL = new WorkflowJDBCDAL()) {
            List<AnalysisResult> result = wfDAL.analyseDraftConcept(conceptJson);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }*/
}
