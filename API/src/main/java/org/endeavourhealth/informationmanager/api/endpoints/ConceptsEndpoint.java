package org.endeavourhealth.informationmanager.api.endpoints;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.*;
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
                           @QueryParam("size") Integer size,
                           @QueryParam("supertype") String supertype) throws Exception {
        LOG.debug("getMRU");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            SearchResult result = imDAL.getMRU(size, supertype);

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
                           @QueryParam("supertype") String supertype,
                           @QueryParam("size") Integer size,
                           @QueryParam("page") Integer page,
                           @QueryParam("model") List<String> models,
                           @QueryParam("status") List<String> statuses) throws Exception {
        LOG.debug("search");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            SearchResult result = (terms == null || terms.isEmpty())
                ? imDAL.getMRU(size, supertype)
                : imDAL.search(terms, supertype, size, page, models, statuses);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/complete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response complete(@Context SecurityContext sc,
                           @QueryParam("term") String terms,
                           @QueryParam("model") List<String> models,
                           @QueryParam("status") List<String> statuses) throws Exception {
        LOG.debug("complete");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<String> result = imDAL.complete(terms, models, statuses);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/completeWord")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response completeWord(@Context SecurityContext sc,
                             @QueryParam("term") String terms) throws Exception {
        LOG.debug("completeWord");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            String result = imDAL.completeWord(terms);

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
            Concept result = imDAL.getConcept(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{id}/relations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptRelations(@Context SecurityContext sc,
                               @PathParam("id") String id,
                                        @QueryParam("includeInhertied") Boolean includeInherited) throws Exception {
        LOG.debug("getConceptRelations");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<ConceptRelation> result = imDAL.getConceptRelations(id, includeInherited);

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

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            String result = imDAL.getConceptName(id);

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

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<Concept> result = imDAL.getChildren(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{id}/parents")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptParents(@Context SecurityContext sc,
                                       @PathParam("id") String id) throws Exception {
        LOG.debug("getConceptParents");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<Concept> result = imDAL.getParents(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{id}/parentTree")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptParentTree(@Context SecurityContext sc,
                                      @PathParam("id") String id,
                                         @QueryParam("root") String root) throws Exception {
        LOG.debug("getConceptParentTree");

        try (InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<ConceptTreeNode> result = imDAL.getParentTree(id, root);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{id}/hierarchy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptHierarchy(@Context SecurityContext sc,
                                      @PathParam("id") String id) throws Exception {
        LOG.debug("getConceptHierarchy");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<ConceptTreeNode> result = imDAL.getHierarchy(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/root")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRootConcepts(@Context SecurityContext sc) throws Exception {
        LOG.debug("getRootConcepts");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<Concept> result = imDAL.getRootConcepts();

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/codeSchemes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCodeSchemes(@Context SecurityContext sc) throws Exception {
        LOG.debug("getCodeSchemes");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<Concept> schemes = imDAL.getCodeSchemes();

            return Response
                .ok()
                .entity(schemes)
                .build();
        }
    }

    /*    @GET
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
    }
*/
}
