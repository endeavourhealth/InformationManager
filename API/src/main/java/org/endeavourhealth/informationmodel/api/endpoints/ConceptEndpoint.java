package org.endeavourhealth.informationmodel.api.endpoints;
import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.endeavourhealth.common.security.annotations.RequiresAdmin;
import org.endeavourhealth.informationmodel.api.models.Category;
import org.endeavourhealth.informationmodel.api.models.Concept;
import org.endeavourhealth.informationmodel.api.logic.ConceptLogic;
import org.endeavourhealth.informationmodel.api.models.ConceptAndRelationships;
import org.endeavourhealth.informationmodel.api.models.ConceptValueRange;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Path("/concept")
@Metrics(registry = "conceptMetricRegistry")
@Api(value = "Concepts", description = "Initial api for all calls relating to concepts")
public class ConceptEndpoint {
    ConceptLogic _conceptLogic = new ConceptLogic();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.Save")
    @ApiOperation(value = "Save a concept to the Database")
    public Response save(@Context SecurityContext sc,
                         @ApiParam(value = "Concept Entity to save") Concept concept) throws Exception {

        concept = _conceptLogic.saveConcept(concept);

        return Response
            .ok()
            .entity(concept)
            .build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.Load")
    @ApiOperation(value = "Load a concept from the Database")
    public Response load(@Context SecurityContext sc,
                         @ApiParam(value = "Id of the concept to load") @QueryParam("conceptId") Long conceptId) throws Exception {

        Concept concept = _conceptLogic.getConcept(conceptId);

        return Response
            .ok()
            .entity(concept)
            .build();

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.SaveConceptAndRelationships")
    @Path("/andRelationships")
    @ApiOperation(value = "Saves a concept and its relationships")
    public Response saveConceptAndRelationships(@Context SecurityContext sc,
                                    @ApiParam(value = "Concept and Relationships to save") ConceptAndRelationships conceptAndRelationships
    ) throws Exception {

        // _conceptLogic.addConceptValueRange(attributeConceptRange);

        return Response
            .ok()
            .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.Range.Add")
    @Path("/range")
    @ApiOperation(value = "Adds a new range to a concept")
    public Response addConceptRange(@Context SecurityContext sc,
                                    @ApiParam(value = "Concept range to add") ConceptValueRange attributeConceptRange
    ) throws Exception {

        _conceptLogic.addConceptValueRange(attributeConceptRange);

        return Response
            .ok()
            .build();
    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.Range.Load")
//    @Path("/range")
//    @ApiOperation(value = "Loads ranges for a given Concept Id")
//    public Response saveConceptRange(@Context SecurityContext sc,
//                                     @ApiParam(value = "Concept Id for which to get ranges") Long conceptId,
//                                     @ApiParam(value = "Qualifier Id for which to get ranges") Long qualifierId
//    ) throws Exception {
//
//        _conceptLogic.loadConceptValueRanges(conceptId, qualifierId);
//
//        return Response
//            .ok()
//            .build();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.Name")
    @Path("/name")
    @ApiOperation(value = "Gets the name of the given concept")
    public Response getName(@Context SecurityContext sc,
                            @ApiParam(value = "Concept to get the name for") @QueryParam("conceptId") Long conceptId) throws Exception {

        String name = _conceptLogic.getName(conceptId);

        return Response
            .ok(name)
            .build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.Context")
    @Path("/context")
    @ApiOperation(value = "Gets the contextName of the given concept")
    @RequiresAdmin
    public Response getContextName(@Context SecurityContext sc,
                                   @ApiParam(value = "Concept to get the context name for") @QueryParam("conceptId") Long conceptId) throws Exception {

        String contextName = _conceptLogic.getContextName(conceptId);

        return Response
            .ok(contextName)
            .build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.Search")
    @Path("/search")
    @ApiOperation(value = "Search for concepts based on term and class")
    public Response search(@Context SecurityContext sc,
                           @ApiParam(value = "Term to search for") @QueryParam("term") String term,
                           @ApiParam(value = "Class to filter by") @QueryParam("classId") Long classId) throws Exception {

        _conceptLogic.search(term, classId);

        return Response
            .ok()
            .build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.List")
    @Path("/list")
    @ApiOperation(value = "List concepts based on category, page, page size & (optional) filter")
    public Response list(@Context SecurityContext sc,
                         @ApiParam(value = "Categories to list") @QueryParam("categoryId") List<Integer> categoryIds,
                         @ApiParam(value = "Page number to list") @QueryParam("page") Integer page,
                         @ApiParam(value = "Page size") @QueryParam("size") Integer size,
                         @ApiParam(value = "Filter") @QueryParam("filter") String filter) throws Exception {

        List<Concept> concepts = _conceptLogic.list(categoryIds, page, size, filter);

        return Response
            .ok(concepts)
            .build();

    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.ConceptEndpoint.Count")
    @Path("/count")
    @ApiOperation(value = "Counts concepts based on category & (optional) filter")
    public Response count(@Context SecurityContext sc,
                         @ApiParam(value = "Category to list") @QueryParam("categoryId") Integer category,
                         @ApiParam(value = "Filter") @QueryParam("filter") String filter) throws Exception {

        Integer count = _conceptLogic.count(Category.getById(category), filter);

        return Response
            .ok(count)
            .build();

    }
}
