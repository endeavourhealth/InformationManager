package org.endeavourhealth.informationmanager.api.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("concepts")
@Api(tags = {"Concepts"})
public class ConceptsEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptsEndpoint.class);

    @GET
    @Path("/mru")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List most recently accessed concepts", response = Concept.class)
    public Response getMRU(@Context SecurityContext sc) throws Exception {
        LOG.debug("getMRU");

        SearchResult result = new InformationManagerJDBCDAL().getMRU();

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @GET
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Search concepts for matching term", response = Concept.class)
    public Response search(@Context SecurityContext sc,
                           @QueryParam("term") String terms,
                           @QueryParam("size") Integer size,
                           @QueryParam("page") Integer page) throws Exception {
        LOG.debug("search");

        SearchResult result = new InformationManagerJDBCDAL().search(terms, size, page, null, null);

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Gets a concept", response = Concept.class)
    public Response getConcept(@Context SecurityContext sc,
                           @PathParam("id") String id) throws Exception {
        LOG.debug("getConcept");

        Concept result = new InformationManagerJDBCDAL().getConcept(id);

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @GET
    @Path("/{id}/name")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Gets a concept name", response = Concept.class)
    public Response getConceptName(@Context SecurityContext sc,
                               @PathParam("id") String id) throws Exception {
        LOG.debug("getConceptName");

        String result = new InformationManagerJDBCDAL().getConceptName(id);

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Updates a concept", response = Concept.class)
    public Response updateConcept(@Context SecurityContext sc,
                                  @PathParam("id") String id,
                                   String body) throws Exception {
        LOG.debug("updateConcept");

        Concept concept = ObjectMapperPool.getInstance().readValue(body, Concept.class);

        concept = new InformationManagerJDBCDAL().updateConcept(concept);

        return Response
            .ok()
            .entity(concept)
            .build();
    }
}
