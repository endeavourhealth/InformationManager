package org.endeavourhealth.informationmanager.api.endpoints;

import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Concept;
import org.endeavourhealth.informationmanager.common.models.Document;
import org.endeavourhealth.informationmanager.common.models.DraftConcept;
import org.endeavourhealth.informationmanager.common.models.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("documents")
@Metrics(registry = "DocumentsMetricRegistry")
@Api(tags = {"Documents"})
public class DocumentsEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentsEndpoint.class);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.DocumentsEndpoint.GET")
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
    @Path("/{dbid}/pending")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.DocumentsEndpoint.{id}.Pending.GET")
    @ApiOperation(value = "List of concepts pending within a document", response = Concept.class)
    public Response getDocumentPending(@Context SecurityContext sc,
                                       @PathParam("dbid") int dbid,
                                       @QueryParam("size") Integer size,
                                       @QueryParam("page") Integer page) throws Exception {
        LOG.debug("getDocumentPending");

        page = (page == null) ? 1 : page;
        size = (size == null) ? 15 : size;
        List<DraftConcept> result = new InformationManagerJDBCDAL().getDocumentPending(dbid, size, page);

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @POST
    @Path("/{dbid}/publish")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.DocumentsEndpoint.{id}.Publish.POST")
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

    @POST
    @Path("/{part: .*}/concepts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.DocumentsEndpoint.{path}.Concepts.POST")
    @ApiOperation(value = "Inserts a concept", response = Concept.class)
    public Response insertConcept(@Context SecurityContext sc,
                                  @PathParam("part") String documentPath,
                                  String body) throws Exception {
        LOG.debug("insertConcept");

        InformationManagerDAL dal = new InformationManagerJDBCDAL();
        Integer docDbid = dal.getDocumentDbid(documentPath);
        if (docDbid == null)
            throw new IllegalArgumentException("Unknown document [" + documentPath + "]");

        new InformationManagerJDBCDAL().insertConcept(docDbid, body, Status.DRAFT);

        return Response
            .ok()
            .build();
    }
}
