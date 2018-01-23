
package org.endeavourhealth.informationmodel.api.endpoints;

import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.endeavourhealth.common.security.annotations.RequiresAdmin;
import org.endeavourhealth.informationmodel.api.models.Concept;
import org.endeavourhealth.informationmodel.api.models.ConceptRelationship;
import org.endeavourhealth.informationmodel.api.logic.RelationshipLogic;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/relationship")
@Metrics(registry = "relationshipMetricRegistry")
@Api(value = "Relationships", description = "Initial api for all calls relating to concept relationships")
public class RelationshipEndpoint {
    RelationshipLogic _relationshipLogic = new RelationshipLogic();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.RelationshipEndpoint.Save")
    @ApiOperation(value = "Adds a new concept relationship between two concepts")
    @RequiresAdmin
    public Response save(@Context SecurityContext sc,
                         @ApiParam(value = "Concept Relationship Entity to save") ConceptRelationship conceptRelationship
    ) throws Exception {

        _relationshipLogic.addRelationship(conceptRelationship);

        return Response
            .ok()
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.RelationshipEndpoint.Load")
    @ApiOperation(value = "Get all related concepts for a given concept and relationship type")
    @RequiresAdmin
    public Response related(@Context SecurityContext sc,
                            @ApiParam(value = "Concept for which to get related concepts") @QueryParam("conceptId") Long conceptId,
                            @ApiParam(value = "Relationship type to retrieve") @QueryParam("relationshipId") Long relationshipId) throws Exception {

        List<ConceptRelationship> relatedConcepts = _relationshipLogic.getRelatedConcepts(conceptId, relationshipId);

        return Response
            .ok(relatedConcepts)
            .build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.RelationshipEndpoint.Targets")
    @Path("/targets")
    @ApiOperation(value = "Get target concepts for a given concept and relationship type")
    @RequiresAdmin
    public Response targets(@Context SecurityContext sc,
                            @ApiParam(value = "Concept for which to get targets") @QueryParam("conceptId") Long conceptId,
                            @ApiParam(value = "Relationship type to retrieve") @QueryParam("relationshipId") Long relationshipId) throws Exception {

        List<ConceptRelationship> targetConcepts = _relationshipLogic.getTargetConcepts(conceptId, relationshipId);

        return Response
            .ok(targetConcepts)
            .build();

    }
}
