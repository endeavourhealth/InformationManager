package org.endeavourhealth.informationmanager.api.endpoints;

import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.endeavourhealth.informationmanager.common.dal.WorkflowJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.AnalysisResult;
import org.endeavourhealth.informationmanager.common.models.Task;
import org.endeavourhealth.informationmanager.common.models.TaskCategory;
import org.endeavourhealth.informationmanager.common.models.TaskSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("workflow")
@Metrics(registry = "WorkflowMetricRegistry")
@Api(tags = {"Workflow"})
public class WorkflowEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowEndpoint.class);

    /******************** CATEGORIES ********************/
    @GET
    @Path("/categories")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.WorkflowEndpoint.Categories.GET")
    @ApiOperation(value = "List of workflow categories")
    public Response getCategories(@Context SecurityContext sc) throws Exception {
        LOG.debug("getCategories");

        List<TaskCategory> result = new WorkflowJDBCDAL().getCategories();

        return Response
            .ok()
            .entity(result)
            .build();
    }

    /******************** TASKS ********************/
    @GET
    @Path("/categories/{categoryDbid}/tasks/summary")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.WorkflowEndpoint.Categories.{id}.Tasks.Summary.GET")
    @ApiOperation(value = "List of tasks for a category")
    public Response getTasks(@Context SecurityContext sc,
                             @PathParam("categoryDbid") Byte categoryDbid) throws Exception {
        LOG.debug("getTasks");

        List<TaskSummary> result = new WorkflowJDBCDAL().getTasks(categoryDbid);

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @GET
    @Path("/tasks/{taskDbid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.WorkflowEndpoint.Tasks.{id}.GET")
    @ApiOperation(value = "Get a task by dbid")
    public Response getTask(@Context SecurityContext sc,
                            @PathParam("taskDbid") Integer taskDbid) throws Exception {
        LOG.debug("getTask");

        Task result = new WorkflowJDBCDAL().getTask(taskDbid);

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @POST
    @Path("/tasks/{taskDbid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.WorkflowEndpoint.Tasks.{id}.POST")
    @ApiOperation(value = "Update a task by dbid")
    public Response updateTask(@Context SecurityContext sc,
                            @PathParam("taskDbid") Integer taskDbid,
                               String taskJson) throws Exception {
        LOG.debug("updateTask");

        new WorkflowJDBCDAL().updateTask(taskDbid, taskJson);

        return Response
            .ok()
            .build();
    }

    @POST
    @Path("/draftConcept/analyse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.WorkflowEndpoint.Analyse.POST")
    @ApiOperation(value = "Analyse a draft concept")
    public Response analyseDraftConcept(@Context SecurityContext sc,
                            String conceptJson) throws Exception {
        LOG.debug("analyseDraftConcept");

        List<AnalysisResult> result = new WorkflowJDBCDAL().analyseDraftConcept(conceptJson);

        return Response
            .ok()
            .entity(result)
            .build();
    }
}
