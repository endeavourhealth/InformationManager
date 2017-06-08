package org.endeavourhealth.informationmodel.api.endpoints;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.awt.*;

@Path("/informationModel")
@Api(description = "Initial api for all calls relating to the information model")
public class InformationModelEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Returns a list of all concepts")
    public Response get(@Context SecurityContext sc,
                        @ApiParam(value = "Optional Concept Id") @QueryParam("conceptId") Integer conceptId,
                        @ApiParam(value = "Optional Name of concept") @QueryParam("conceptName") String conceptName
                        ) throws Exception {
        System.out.println("ohhhhhh yes");
        return Response
                .ok()
                .entity("test")
                .build();
    }
}
