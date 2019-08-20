package org.endeavourhealth.informationmanager.api.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.endeavourhealth.common.cache.ObjectMapperPool;
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
@Api(tags = {"Documents"})
public class DocumentsEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentsEndpoint.class);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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

    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Create a new document", response = Integer.class)
    public Response createDocument(@Context SecurityContext sc, String path) throws Exception {
        LOG.debug("publishDocument");

        Integer dbid = new InformationManagerJDBCDAL().getOrCreateDocumentDbid(path);

        return Response
            .ok()
            .entity(dbid)
            .build();
    }

    @POST
    @Path("/{dbid}/publish")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Inserts a concept", response = Concept.class)
    public Response insertConcept(@Context SecurityContext sc,
                                  @PathParam("part") String documentPath,
                                  @QueryParam("id") String id,
                                  @QueryParam("name") String name) throws Exception {
        LOG.debug("insertConcept");

        InformationManagerDAL dal = new InformationManagerJDBCDAL();
        Integer docDbid = dal.getDocumentDbid(documentPath);
        if (docDbid == null) throw new IllegalArgumentException("Unknown document [" + documentPath + "]");
        if (id == null) throw new IllegalArgumentException("Id not specified");
        if (name == null) throw new IllegalArgumentException("Name not specified");

        new InformationManagerJDBCDAL()
            .createConcept(docDbid, id, name);

        return Response
            .ok()
            .build();
    }
}
