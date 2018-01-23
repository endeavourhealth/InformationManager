package org.endeavourhealth.informationmodel.api.endpoints;

import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.endeavourhealth.common.security.annotations.RequiresAdmin;
import org.endeavourhealth.informationmodel.api.models.Concept;
import org.endeavourhealth.informationmodel.api.models.View;
import org.endeavourhealth.informationmodel.api.logic.ViewLogic;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/view")
@Metrics(registry = "viewMetricRegistry")
@Api(value = "Views", description = "Initial api for all calls relating to views")
public class ViewEndpoint {
    ViewLogic _viewLogic = new ViewLogic();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ViewEndpoint.Add")
    @ApiOperation(value = "Adds a new view to the Database")
    @RequiresAdmin
    public Response add(@Context SecurityContext sc,
                         @ApiParam(value = "View Entity to add") View view) throws Exception {

        _viewLogic.addView(view);

        return Response
            .ok()
            .build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ViewEndpoint.Load")
    @ApiOperation(value = "Get a view")
    @RequiresAdmin
    public Response load(@Context SecurityContext sc,
                             @ApiParam(value = "Id of view to load") @QueryParam("viewId") Long viewId) throws Exception {

        View view = _viewLogic.getView(viewId);

        return Response
            .ok(view)
            .build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ViewEndpoint.Children")
    @Path("/children")
    @ApiOperation(value = "Get child concepts for a given view and relationship type")
    @RequiresAdmin
    public Response children(@Context SecurityContext sc,
                             @ApiParam(value = "View for which to get children") @QueryParam("viewId") Long viewId,
                             @ApiParam(value = "Relationship type for which to get children") @QueryParam("relationshipId") Long relationshipId) throws Exception {

        List<Concept> childConcepts = _viewLogic.getChildren(viewId, relationshipId);

        return Response
            .ok(childConcepts)
            .build();

    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ViewEndpoint.MoveUp")
    @Path("/moveUp")
    @ApiOperation(value = "Moves a view concept up the order")
    @RequiresAdmin
    public Response moveUp(@Context SecurityContext sc,
                           @ApiParam(value = "View Id within which to move") @QueryParam("viewId") Long viewId,
                           @ApiParam(value = "Concept Entity Id to move up") @QueryParam("conceptId") Long conceptId) throws Exception {

        _viewLogic.moveConceptUp(conceptId);

        return Response
            .ok()
            .build();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ViewEndpoint.MoveDown")
    @Path("/moveDown")
    @ApiOperation(value = "Moves a view concept down the order")
    @RequiresAdmin
    public Response moveDown(@Context SecurityContext sc,
                             @ApiParam(value = "View Id within which to move") @QueryParam("viewId") Long viewId,
                             @ApiParam(value = "Concept Entity Id to move down") @QueryParam("conceptId") Long conceptId) throws Exception {

        _viewLogic.moveConceptDown(conceptId);

        return Response
            .ok()
            .build();

    }
}
