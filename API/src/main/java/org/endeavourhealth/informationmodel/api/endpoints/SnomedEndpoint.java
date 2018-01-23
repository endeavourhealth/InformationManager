package org.endeavourhealth.informationmodel.api.endpoints;
import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.endeavourhealth.common.security.annotations.RequiresAdmin;
import org.endeavourhealth.informationmodel.api.json.JsonSnomedConfig;
import org.endeavourhealth.informationmodel.api.logic.SnomedUploadLogic;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/informationModel")
@Metrics(registry = "informationManagerMetricRegistry")
@Api(description = "Initial api for all calls relating to the information model")
public class SnomedEndpoint {
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.startUpload")
    @Path("/startUpload")
    @ApiOperation(value = "Prepares the API for a bulk upload of snomed codes")
    public Response startUpload(@Context SecurityContext sc,
                                @ApiParam(value = "json configuration options") JsonSnomedConfig config) throws Exception {

        return new SnomedUploadLogic().startUpload(config);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.SnomedUpload")
    @Path("/snomedUpload")
    @ApiOperation(value = "Bulk Upload snomed concepts")
    @RequiresAdmin
    public Response uploadSnomed(@Context SecurityContext sc,
                                 @ApiParam(value = "Bulk CSV file to upload") String csvFile
    ) throws Exception {

        return new SnomedUploadLogic().bulkUploadSnomed(csvFile);

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.uploadSnomedRelationships")
    @Path("/snomedRelationshipUpload")
    @ApiOperation(value = "Bulk Upload snomed concept relationships")
    @RequiresAdmin
    public Response uploadSnomedRelationships(@Context SecurityContext sc,
                                              @ApiParam(value = "Bulk CSV file to upload") String csvFile
    ) throws Exception {

        return new SnomedUploadLogic().bulkUploadSnomedRelationships(csvFile);

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.saveConcepts")
    @Path("/saveConcepts")
    @ApiOperation(value = "Save concepts to the database.  Part of the bulk upload process")
    public Response saveConcepts(@Context SecurityContext sc,
                                 @ApiParam(value = "Number of concepts to save in this batch") @QueryParam("limit") Integer limit) throws Exception {

        return new SnomedUploadLogic().saveConcepts(limit);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.saveRelationships")
    @Path("/saveRelationships")
    @ApiOperation(value = "Save relationships to the database.  Part of the bulk upload process")
    public Response saveRelationships(@Context SecurityContext sc,
                                      @ApiParam(value = "Number of concepts to save in this batch") @QueryParam("limit") Integer limit) throws Exception {

        return new SnomedUploadLogic().saveRelationships(limit);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.setInactiveSnomed")
    @Path("/setInactiveSnomed")
    @ApiOperation(value = "As part of a delta upload, update all the now inactive snomed concepts to be inactive")
    public Response setInactiveSnomed(@Context SecurityContext sc) throws Exception {

        return new SnomedUploadLogic().setInactiveSnomedConcepts();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.deleteInactiveRelationships")
    @Path("/deleteInactiveRelationships")
    @ApiOperation(value = "As part of a delta upload, delete all the now inactive snomed relationships")
    public Response deleteInactiveRelationships(@Context SecurityContext sc) throws Exception {

        return new SnomedUploadLogic().deleteInactiveSnomedRelationships();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.completeUpload")
    @Path("/completeUpload")
    @ApiOperation(value = "Finalises the bulk upload of snomed codes by saving concepts to the database")
    public Response completeUpload(@Context SecurityContext sc) throws Exception {

        return new SnomedUploadLogic().completeUpload();
    }
}
