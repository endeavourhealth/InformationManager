package org.endeavourhealth.informationmanager.api.endpoints;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Concept;
import org.endeavourhealth.informationmanager.common.models.Document;
import org.endeavourhealth.informationmanager.common.models.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("IM")
@Metrics(registry = "ConceptMetricRegistry")
@Api(tags = {"Concept"})
public class InformationManagerEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(InformationManagerEndpoint.class);

    /******************** DOCUMENTS ********************/
    @GET
    @Path("/Documents")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InformationManagerEndpoint.Documents.GET")
    @ApiOperation(value = "List of registered documents", response = Concept.class)
    public Response getDocuments(@Context SecurityContext sc) throws Exception {
        LOG.debug("getDocuments");

        List<Document> result = new InformationManagerJDBCDAL().getDocuments();

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @GET
    @Path("/Document/{dbid}/LatestPublished")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InformationManagerEndpoint.Document.LatestPublished.GET")
    @ApiOperation(value = "Gets the latest published version of a given document", response = Concept.class)
    public Response getDocumentLatestPublished(@Context SecurityContext sc,
                                @PathParam("dbid") Integer dbid
    ) throws Exception {
        LOG.debug("getDocument");

        byte[] documentJson = new InformationManagerJDBCDAL().getDocumentLatestPublished(dbid);

        return Response
            .ok()
            .entity(documentJson)
            .build();
    }

    @GET
    @Path("/Document/{dbid}/Pending")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InformationManagerEndpoint.DocumentPending.GET")
    @ApiOperation(value = "List of concepts pending within a document", response = Concept.class)
    public Response getDocumentPending(@Context SecurityContext sc,
                                       @PathParam("dbid") int dbid,
                                       @QueryParam("size") Integer size,
                                       @QueryParam("page") Integer page) throws Exception {
        LOG.debug("getDocumentPending");

        page = (page == null) ? 1 : page;
        size = (size == null) ? 15 : size;
        SearchResult result = new InformationManagerJDBCDAL().getDocumentPending(dbid, size, page);

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @POST
    @Path("/Document/{dbid}/Publish")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InformationManagerEndpoint.DocumentPublish.GET")
    @ApiOperation(value = "Publishes a new version of a draft document", response = Concept.class)
    public Response publishDocument(@Context SecurityContext sc,
                                       @PathParam("dbid") int dbid,
                                       @QueryParam("level") String level) throws Exception {
        LOG.debug("publishDocument");

        new InformationManagerJDBCDAL().publishDocument(dbid, level);

        return Response
            .ok()
            .build();
    }

    /******************** CONCEPTS ********************/
    @GET
    @Path("/MRU")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InformationManagerEndpoint.MRU.GET")
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
    @Path("/Search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InformationManagerEndpoint.Search.GET")
    @ApiOperation(value = "Search concepts for matching term", response = Concept.class)
    public Response search(@Context SecurityContext sc,
                           @QueryParam("term") String term,
                           @QueryParam("size") Integer size,
                           @QueryParam("page") Integer page) throws Exception {
        LOG.debug("search");

        SearchResult result = new InformationManagerJDBCDAL().search(term, size, page, null, null);

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @GET
    @Path("/Concept/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InformationManagerEndpoint.Concept.GET")
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
    @Path("/Concept/{id}/name")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Timed(absolute = true, name = "InformationModel.InformationManagerEndpoint.Concept.name.GET")
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
    @Path("/Concept/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InformationManagerEndpoint.Concept.POST")
    @ApiOperation(value = "Updates a concept", response = Concept.class)
    public Response updateConcept(@Context SecurityContext sc,
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
