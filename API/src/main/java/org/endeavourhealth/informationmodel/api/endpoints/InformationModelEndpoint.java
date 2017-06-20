package org.endeavourhealth.informationmodel.api.endpoints;
import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.endeavourhealth.common.security.annotations.RequiresAdmin;
import org.endeavourhealth.informationmodel.api.database.models.ConceptEntity;
import org.endeavourhealth.informationmodel.api.database.models.ConceptRelationshipEntity;
import org.endeavourhealth.informationmodel.api.database.models.RelationshipTypeEntity;
import org.endeavourhealth.informationmodel.api.framework.helper.CsvHelper;
import org.endeavourhealth.informationmodel.api.json.JsonConcept;
import org.endeavourhealth.informationmodel.api.json.JsonConceptRelationship;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Path("/informationModel")
@Metrics(registry = "informationManagerMetricRegistry")
@Api(description = "Initial api for all calls relating to the information model")
public class InformationModelEndpoint {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.Get")
    @Path("/")
    @ApiOperation(value = "Returns a list of all concepts")
    public Response get(@Context SecurityContext sc,
                        @ApiParam(value = "Optional Concept Id") @QueryParam("conceptId") Integer conceptId,
                        @ApiParam(value = "Optional Name of concept") @QueryParam("conceptName") String conceptName,
                        @ApiParam(value = "Optional Array of concept Ids") @QueryParam("conceptIdList") List<Integer> conceptIdList
    ) throws Exception {
        System.out.println("ohhhhhh yes");

        if (conceptId == null && conceptName == null && conceptIdList.size() == 0) {
            return getAllConcepts();
        }
        else if (conceptId != null) {
            return getConceptById(conceptId);
        }
        else if (conceptIdList.size() > 0) {
            return getConceptsByIdList(conceptIdList);
        }
        else {
            return getConceptsByName(conceptName);
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.Delete")
    @Path("/")
    @ApiOperation(value = "Deletes a concepts based on ConceptId")
    @RequiresAdmin
    public Response deleteConcept(@Context SecurityContext sc,
                                  @ApiParam(value = "Concept Id to Delete") @QueryParam("conceptId") Integer conceptId
                    ) throws Exception {
        ConceptEntity.deleteConcept(conceptId);

        return Response
                .ok()
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.Post")
    @Path("/")
    @ApiOperation(value = "Adds a new concept to the Database")
    @RequiresAdmin
    public Response post(@Context SecurityContext sc,
                         @ApiParam(value = "Concept Entity to post") JsonConcept concept
    ) throws Exception {

        System.out.println("saving concept " + concept.getName());
        ConceptEntity.saveConcept(concept);

        return Response
                .ok()
                .build();

    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.Put")
    @Path("/")
    @ApiOperation(value = "Updates an existing concept")
    @RequiresAdmin
    public Response put(@Context SecurityContext sc,
                         @ApiParam(value = "Concept Entity to post") JsonConcept concept
    ) throws Exception {

        ConceptEntity.updateConcept(concept);

        return Response
                .ok()
                .build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.GetRelationships")
    @Path("/relationships")
    @ApiOperation(value = "Returns a list of all concept relationships")
    public Response getRelationships(@Context SecurityContext sc,
                                     @ApiParam(value = "Concept Id") @QueryParam("conceptId") Integer conceptId
    ) throws Exception {

        return getConceptRelationships(conceptId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.PostRelationship")
    @Path("/relationships")
    @ApiOperation(value = "Adds a new concept relationship between two concepts")
    @RequiresAdmin
    public Response postRelationship(@Context SecurityContext sc,
                         @ApiParam(value = "Concept Relationship Entity to post") JsonConceptRelationship conceptRelationship
    ) throws Exception {

        ConceptRelationshipEntity.saveConceptRelationship(conceptRelationship);

        return Response
                .ok()
                .build();

    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.DeleteRelationship")
    @Path("/relationships")
    @ApiOperation(value = "Deletes a relationship between two concepts")
    @RequiresAdmin
    public Response deleteConceptRelationship(@Context SecurityContext sc,
                                  @ApiParam(value = "Concept Relationship Id to Delete") @QueryParam("conceptRelationshipId") Integer conceptRelationshipId
    ) throws Exception {
        ConceptRelationshipEntity.deleteConceptRelationship(conceptRelationshipId);

        return Response
                .ok()
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.GetCommonConcepts")
    @Path("/common")
    @ApiOperation(value = "Returns a list of common concept relationships restricted by limit passed into API")
    public Response getCommonConcepts(@Context SecurityContext sc,
                                     @ApiParam(value = "limit of number of common concepts to return") @QueryParam("limit") Integer limit
    ) throws Exception {

        return getCommonConcepts(limit);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.GetRelationshipTypes")
    @Path("/relationshipTypes")
    @ApiOperation(value = "Returns a list of the currently registered concept relationship types")
    public Response getRelationshipTypes(@Context SecurityContext sc) throws Exception {
        List<RelationshipTypeEntity> relationTypes = RelationshipTypeEntity.getAllRelationshipTypes();

        return Response
                .ok()
                .entity(relationTypes)
                .build();
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

        return bulkUploadSnomed(csvFile);

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

        return bulkUploadSnomedRelationships(csvFile);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.GetRelationshipConcepts")
    @Path("/relationshipConcepts")
    @ApiOperation(value = "Returns a list of the concepts that define relationships between two concepts")
    public Response getRelationshipConcepts(@Context SecurityContext sc) throws Exception {

        return getRelationshipConcepts();
    }

    private Response getAllConcepts() throws Exception {
        List<ConceptEntity> concepts = ConceptEntity.getAllConcepts();

        return Response
                .ok()
                .entity(concepts)
                .build();
    }

    private Response getConceptById(Integer conceptId) throws Exception {
        ConceptEntity concept = ConceptEntity.getConceptById(conceptId);

        return Response
                .ok()
                .entity(concept)
                .build();
    }

    private Response getConceptsByName(String conceptName) throws Exception {
        List<ConceptEntity> concepts = ConceptEntity.getConceptsByName(conceptName);

        return Response
                .ok()
                .entity(concepts)
                .build();
    }

    private Response getConceptsByIdList(List<Integer> conceptIdList) throws Exception {
        List<ConceptEntity> concepts = ConceptEntity.getConceptsFromList(conceptIdList);

        return Response
                .ok()
                .entity(concepts)
                .build();
    }

    private Response getConceptRelationships(Integer conceptId) throws Exception {

        List<Object[]> concepts = ConceptRelationshipEntity.getConceptsRelationships(conceptId);

        List<JsonConceptRelationship> relationships = convertRelationshipToJson(concepts);

        return Response
                .ok()
                .entity(relationships)
                .build();
    }

    private Response getCommonConcepts(Integer limit) throws Exception {
        List<ConceptEntity> concepts = ConceptEntity.getCommonConcepts(limit);

        return Response
                .ok()
                .entity(concepts)
                .build();
    }

    private Response bulkUploadSnomed(String csvFile) throws Exception {

        ConceptRelationshipEntity.deleteSnomedRelationships();
        ConceptEntity.deleteSnomedCodes();
        Scanner scanner = new Scanner(csvFile);
        List<ConceptEntity> snomedConcepts = new ArrayList<>();
        boolean skippedHeaders = false;

        while (scanner.hasNext()) {
            List<String> snomed = CsvHelper.parseLine(scanner.nextLine());
            if (!skippedHeaders){
                skippedHeaders = true;
                continue;
            }
            snomedConcepts.add(createConcept(snomed));
        }

        ConceptEntity.bulkSaveConcepts(snomedConcepts);

        return Response
                .ok()
                .build();
    }

    private Response bulkUploadSnomedRelationships(String csvFile) throws Exception {

        Scanner scanner = new Scanner(csvFile);
        List<ConceptRelationshipEntity> snomedRelationships = new ArrayList<>();
        boolean skippedHeaders = false;

        while (scanner.hasNext()) {
            List<String> relationship = CsvHelper.parseLine(scanner.nextLine());
            if (!skippedHeaders){
                skippedHeaders = true;
                continue;
            }
            snomedRelationships.add(createConceptRelationship(relationship));
            snomedRelationships.add(createReverseConceptRelationship(relationship));
        }

        ConceptRelationshipEntity.bulkSaveConceptRelationships(snomedRelationships);

        return Response
                .ok()
                .build();
    }

    private ConceptEntity createConcept(List<String> snomed) {

        ConceptEntity concept = new ConceptEntity();
        String snomedName = snomed.get(3);
        concept.setName(snomedName);
        concept.setId(Long.parseLong(snomed.get(0)) + 1000000);
        concept.setStructureType("sno");

        concept.setShortName(snomedName.substring(0, Math.min(124, snomedName.length())));
        concept.setCount((long)(0));
        return concept;
    }

    private ConceptRelationshipEntity createConceptRelationship(List<String> relationship) {

        ConceptRelationshipEntity conceptRelationship = new ConceptRelationshipEntity();
        conceptRelationship.setSourceConcept(Long.parseLong(relationship.get(2)) + 1000000);
        conceptRelationship.setTargetConcept(Long.parseLong(relationship.get(4)) + 1000000);
        conceptRelationship.setRelationshipType((long)1); //is child of
        conceptRelationship.setCount((long)(0));

        return conceptRelationship;
    }

    private ConceptRelationshipEntity createReverseConceptRelationship(List<String> relationship) {

        ConceptRelationshipEntity conceptRelationship = new ConceptRelationshipEntity();
        conceptRelationship.setSourceConcept(Long.parseLong(relationship.get(4)) + 1000000);
        conceptRelationship.setTargetConcept(Long.parseLong(relationship.get(2)) + 1000000);
        conceptRelationship.setRelationshipType((long)2); //is parent of
        conceptRelationship.setCount((long)(0));

        return conceptRelationship;
    }

    private List<JsonConceptRelationship> convertRelationshipToJson(List<Object[]> results) throws Exception {

        List<JsonConceptRelationship> relationships = new ArrayList<>();

        for (Object[] rel : results) {
            String sourceId = rel[0]==null?"":rel[0].toString();
            String sourceName = rel[1]==null?"":rel[1].toString();
            String relationshipId = rel[2]==null?"":rel[2].toString();
            String relationshipName = rel[3]==null?"":rel[3].toString();
            String targetId = rel[4]==null?"":rel[4].toString();
            String targetName = rel[5]==null?"":rel[5].toString();

            JsonConceptRelationship relationship = new JsonConceptRelationship();
            relationship.setSourceConcept(Integer.parseInt(sourceId));
            relationship.setSourceConceptName(sourceName);
            relationship.setRelationship_type(Long.parseLong(relationshipId));
            relationship.setRelationshipTypeName(relationshipName);
            relationship.setTargetConcept(Integer.parseInt(targetId));
            relationship.setTargetConceptName(targetName);

            relationships.add(relationship);
        }

        return relationships;
    }

    private Response getRelationshipConcepts() throws Exception {
        List<ConceptEntity> concepts = ConceptEntity.getRelationshipConcepts();

        return Response
                .ok()
                .entity(concepts)
                .build();
    }

}
