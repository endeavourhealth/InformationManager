package org.endeavourhealth.informationmanager.api.endpoints;

import org.endeavourhealth.common.cache.ObjectMapperPool;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.logic.ConceptLogic;
import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.models.document.Concept;
import org.endeavourhealth.informationmanager.common.models.document.PropertyRange;
import org.endeavourhealth.informationmanager.common.models.document.SimpleExpressionConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("concepts")
public class ConceptsEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptsEndpoint.class);

    @GET
    @Path("/mru")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMRU(@Context SecurityContext sc,
                           @QueryParam("size") Integer size) throws Exception {
        LOG.debug("getMRU");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {
            SearchResult result = imDAL.getMRU(size);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@Context SecurityContext sc,
                           @QueryParam("term") String terms,
                           @QueryParam("size") Integer size,
                           @QueryParam("page") Integer page,
                           @QueryParam("docDbid") List<Integer> documents,
                           @QueryParam("relationship") String relationship,
                           @QueryParam("target") String target) throws Exception {
        LOG.debug("search");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {
            SearchResult result = (terms == null || terms.isEmpty()) && (relationship == null || relationship.isEmpty())
                ? imDAL.getMRU(size)
                : imDAL.search(terms, size, page, documents, relationship, target);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConcept(@Context SecurityContext sc,
                           @PathParam("id") String id) throws Exception {
        LOG.debug("getConcept");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {
            FullConcept result = imDAL.getConcept(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{id}/name")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getConceptName(@Context SecurityContext sc,
                               @PathParam("id") String id) throws Exception {
        LOG.debug("getConceptName");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {
            String result = imDAL.getConceptName(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{id}/range")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptRange(@Context SecurityContext sc,
                                   @PathParam("id") String id) throws Exception {
        LOG.debug("getConceptRange");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {
            List<PropertyRange> result = imDAL.getPropertyRange(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConcept(@Context SecurityContext sc,
                                  @PathParam("id") String id,
                                   String body) throws Exception {
        LOG.debug("updateConcept");

        Concept concept = ObjectMapperPool.getInstance().readValue(body, Concept.class);

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {
            imDAL.beginTransaction();
            concept = imDAL.updateConcept(concept);
            imDAL.commit();

            return Response
                .ok()
                .entity(concept)
                .build();
        }
    }

    @GET
    @Path("/schemes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSchemes(@Context SecurityContext sc) throws Exception {
        LOG.debug("getSchemes");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {
            List<IdNamePair> schemes = imDAL.getSchemes();

            return Response
                .ok()
                .entity(schemes)
                .build();
        }
    }

    @GET
    @Path("/{id}/parentTree")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptParentTree(@Context SecurityContext sc,
                                    @PathParam("id") String id) throws Exception {
        LOG.debug("getConceptParentTree");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {
            List<ConceptTreeNode> result = new ConceptLogic(imDAL).getParentTree(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{id}/children")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptChildren(@Context SecurityContext sc,
                                       @PathParam("id") String id) throws Exception {
        LOG.debug("getConceptChildren");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {
            List<ConceptTreeNode> result = imDAL.getChildren(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }}
