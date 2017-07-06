package org.endeavourhealth.informationmodel.api.endpoints;
import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.endeavourhealth.common.security.annotations.RequiresAdmin;
import org.endeavourhealth.informationmodel.api.database.models.ConceptEntity;
import org.endeavourhealth.informationmodel.api.database.models.ConceptRelationshipEntity;
import org.endeavourhealth.informationmodel.api.framework.helper.CsvHelper;
import org.endeavourhealth.informationmodel.api.json.JsonConcept;
import org.endeavourhealth.informationmodel.api.json.JsonConceptRelationship;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Path("/informationModel")
@Metrics(registry = "informationManagerMetricRegistry")
@Api(description = "Initial api for all calls relating to the information model")
public class InformationModelEndpoint {

    private List<ConceptEntity> snomedConceptEntities = new ArrayList<>();
    private List<ConceptRelationshipEntity> snomedRelationshipEntities = new ArrayList<>();
    private static HashMap<Long, Long> snomedIdMap = new HashMap<>();
    private Long conceptId = (long)10000;
    private Long relationshipId = (long)10000;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.Get")
    @Path("/")
    @ApiOperation(value = "Returns a list of all concepts")
    public Response get(@Context SecurityContext sc,
                        @ApiParam(value = "Optional Concept Id") @QueryParam("conceptId") Long conceptId,
                        @ApiParam(value = "Optional Name of concept") @QueryParam("conceptName") String conceptName,
                        @ApiParam(value = "Optional Array of concept Ids") @QueryParam("conceptIdList") List<Integer> conceptIdList,
                        @ApiParam(value = "Optional page number parameter (default is 1)") @QueryParam("pageNumber") Integer pageNumber,
                        @ApiParam(value = "Optional page size paramater (default is 10)") @QueryParam("pageSize") Integer pageSize
    ) throws Exception {

        if (conceptId == null && conceptName == null && conceptIdList.size() == 0) {
            return getCommonConcepts(20);
        }
        else if (conceptId != null) {
            return getConceptById(conceptId);
        }
        else if (conceptIdList.size() > 0) {
            return getConceptsByIdList(conceptIdList);
        }
        else {
            if (pageNumber == null)
                pageNumber = 1;
            if (pageSize == null)
                pageSize = 10;
            return getConceptsByName(conceptName, pageNumber, pageSize);
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
        concept = ConceptEntity.saveConcept(concept);

        return Response
                .ok()
								.entity(concept)
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
                                  @ApiParam(value = "Concept Relationship Id to Delete") @QueryParam("conceptRelationshipId") Long conceptRelationshipId
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name="InformationManager.ConceptEndpoint.searchCount")
    @Path("/searchCount")
    @ApiOperation(value = "Returns the number of search results for a particular concept search")
    public Response getConceptSearchCount(@Context SecurityContext sc,
                                     @ApiParam(value = "Concept Name") @QueryParam("conceptName") String conceptName
    ) throws Exception {

        return getConceptsSearchCount(conceptName);
    }

    private Response getAllConcepts() throws Exception {
        List<ConceptEntity> concepts = ConceptEntity.getAllConcepts();

        return Response
                .ok()
                .entity(concepts)
                .build();
    }

    private Response getConceptById(Long conceptId) throws Exception {
        ConceptEntity concept = ConceptEntity.getConceptById(conceptId);

        return Response
                .ok()
                .entity(concept)
                .build();
    }

    private Response getConceptsByName(String conceptName, Integer pageNumber, Integer pageSize) throws Exception {
        List<ConceptEntity> concepts = ConceptEntity.getConceptsByName(conceptName, pageNumber, pageSize);

        return Response
                .ok()
                .entity(concepts)
                .build();
    }

    private Response getConceptsSearchCount(String conceptName) throws Exception {
        Long count = ConceptEntity.getCountOfConceptSearch(conceptName);

        return Response
                .ok()
                .entity(count)
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
        boolean skippedHeaders = false;

        while (scanner.hasNext()) {
            List<String> snomed = CsvHelper.parseLine(scanner.nextLine(), '\t');
            if (!skippedHeaders){
                skippedHeaders = true;
                continue;
            }
            //ignore non NHS specified codes descriptions
            if (snomed.get(6).equals("900000000000003001"))
                snomedConceptEntities.add(createConcept(snomed));
        }

        ConceptEntity.bulkSaveConcepts(snomedConceptEntities);

        return Response
                .ok()
                .build();
    }

    private Response bulkUploadSnomedRelationships(String csvFile) throws Exception {

        Scanner scanner = new Scanner(csvFile);
        boolean skippedHeaders = false;

        while (scanner.hasNext()) {
            List<String> relationship = CsvHelper.parseLine(scanner.nextLine(), '\t');
            if (!skippedHeaders){
                skippedHeaders = true;
                continue;
            }
            snomedRelationshipEntities.add(createConceptRelationship(relationship));
        }

        ConceptRelationshipEntity.bulkSaveConceptRelationships(snomedRelationshipEntities);

        return Response
                .ok()
                .build();
    }

    private ConceptEntity createConcept(List<String> snomed) {

        ConceptEntity concept = new ConceptEntity();
        String snomedName = snomed.get(7);
        Long snomedId = conceptId++;
        concept.setName(snomedName.substring(0, Math.min(249, snomedName.length())));
        concept.setId(snomedId);
        snomedIdMap.put(Long.parseLong(snomed.get(4)), snomedId);
        concept.setShortName(snomedName.substring(0, Math.min(124, snomedName.length())));
        concept.setDescription(snomedName);
        concept.setStatus((byte)1);
        concept.setClazz(13);

        return concept;
    }

    private ConceptRelationshipEntity createConceptRelationship(List<String> relationship) {

        ConceptRelationshipEntity conceptRelationship = new ConceptRelationshipEntity();
        conceptRelationship.setId(relationshipId++);
        conceptRelationship.setSourceConcept(snomedIdMap.get(Long.parseLong(relationship.get(4))));
        conceptRelationship.setTargetConcept(snomedIdMap.get(Long.parseLong(relationship.get(5))));
        conceptRelationship.setRelationshipType(snomedIdMap.get(Long.parseLong(relationship.get(7))));

        return conceptRelationship;
    }

    private ConceptRelationshipEntity createReverseConceptRelationship(List<String> relationship) {

        ConceptRelationshipEntity conceptRelationship = new ConceptRelationshipEntity();
        conceptRelationship.setSourceConcept(Long.parseLong(relationship.get(4)) + 1000000);
        conceptRelationship.setTargetConcept(Long.parseLong(relationship.get(2)) + 1000000);
        conceptRelationship.setRelationshipType((long)2); //is parent of

        return conceptRelationship;
    }

    private List<JsonConceptRelationship> convertRelationshipToJson(List<Object[]> results) throws Exception {

        List<JsonConceptRelationship> relationships = new ArrayList<>();

        for (Object[] rel : results) {
        		String Id = rel[0]==null?"":rel[0].toString();
            String sourceId = rel[1]==null?"":rel[1].toString();
            String sourceName = rel[2]==null?"":rel[2].toString();
            String sourceDescription = rel[3]==null?"":rel[3].toString();
            String sourceShortName = rel[4]==null?"":rel[4].toString();
            String relationshipId = rel[5]==null?"":rel[5].toString();
            String relationshipName = rel[6]==null?"":rel[6].toString();
            String relationshipDescription = rel[7]==null?"":rel[7].toString();
            String relationshipShortName = rel[8]==null?"":rel[8].toString();
            String targetId = rel[9]==null?"":rel[9].toString();
            String targetName = rel[10]==null?"":rel[10].toString();
            String targetDescription = rel[11]==null?"":rel[11].toString();
            String targetShortName = rel[12]==null?"":rel[12].toString();

            JsonConceptRelationship relationship = new JsonConceptRelationship();
            relationship.setId(Long.parseLong(Id));
            relationship.setSourceConcept(Long.parseLong(sourceId));
            relationship.setSourceConceptName(sourceName);
            relationship.setSourceConceptDescription(sourceDescription);
            relationship.setSourceConceptShortName(sourceShortName);
            relationship.setRelationship_type(Long.parseLong(relationshipId));
            relationship.setRelationshipTypeName(relationshipName);
            relationship.setRelationshipTypeDescription(relationshipDescription);
            relationship.setRelationshipTypeShortName(relationshipShortName);
            relationship.setTargetConcept(Long.parseLong(targetId));
            relationship.setTargetConceptName(targetName);
            relationship.setTargetConceptDescription(targetDescription);
            relationship.setTargetConceptShortName(targetShortName);

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
