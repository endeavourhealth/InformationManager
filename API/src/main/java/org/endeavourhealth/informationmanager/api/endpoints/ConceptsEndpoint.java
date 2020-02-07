package org.endeavourhealth.informationmanager.api.endpoints;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.logic.ConceptLogic;
import org.endeavourhealth.informationmanager.common.models.*;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.PropertyDefinition;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.SimpleConcept;
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
                           @QueryParam("supertype") List<String> supertypes) throws Exception {
        LOG.debug("getMRU");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            SearchResult result = imDAL.getMRU(size, supertypes);

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
                           @QueryParam("supertype") List<String> supertypes,
                           @QueryParam("size") Integer size,
                           @QueryParam("page") Integer page,
                           @QueryParam("model") List<String> models,
                           @QueryParam("status") List<String> statuses) throws Exception {
        LOG.debug("search");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            SearchResult result = (terms == null || terms.isEmpty())
                ? imDAL.getMRU(size, supertypes)
                : imDAL.search(terms, supertypes, size, page, models, statuses);

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
            List<Concept> result = imDAL.complete(terms, models, statuses);

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
    @Path("/axioms")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAxioms(@Context SecurityContext sc) throws Exception {
        LOG.debug("getAxioms");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<Axiom> result = imDAL.getAxioms();

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    // CONCEPT SPECIFIC

    @GET
    @Path("/{iri}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConcept(@Context SecurityContext sc,
                           @PathParam("iri") String iri) throws Exception {
        LOG.debug("getConcept");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            Concept result = imDAL.getConcept(iri);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{iri}/definition")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDefinition(@Context SecurityContext sc,
                                  @PathParam("iri") String iri) throws Exception {
        LOG.debug("getDefinition");

        try (InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            ConceptLogic conceptLogic = new ConceptLogic(imDAL);
            ConceptDefinition result = conceptLogic.getConceptDefinition(iri);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createConcept(@Context SecurityContext sc,
                                  Concept concept) throws Exception {
        LOG.debug("createConcept");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.createConcept(concept))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConcept(@Context SecurityContext sc,
                                  Concept concept) throws Exception {
        LOG.debug("updateConcept");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.updateConcept(concept))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @POST
    @Path("/{iri}/{axiom}/supertypes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAxiomExpressionSupertype(@Context SecurityContext sc,
                                      @PathParam("iri") String conceptIri,
                                  @PathParam("axiom") String axiom,
                                  SimpleConcept definition) throws Exception {
        LOG.debug("addAxiomExpressionSupertype");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.addAxiomExpressionSupertype(conceptIri, axiom, definition.getConcept()))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{iri}/{axiom}/supertypes/{supertype}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delAxiomExpressionSupertype(@Context SecurityContext sc,
                                                @PathParam("iri") String conceptIri,
                                                @PathParam("axiom") String axiom,
                                                @PathParam("supertype") String supertype) throws Exception {
        LOG.debug("delAxiomExpressionSupertype");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delAxiomExpressionSupertype(conceptIri, axiom, supertype))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @POST
    @Path("/{iri}/{axiom}/rolegroups/{group}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAxiomExpressionRoleGroupProperty(@Context SecurityContext sc,
                                      @PathParam("iri") String conceptIri,
                                      @PathParam("axiom") String axiom,
                                      @PathParam("group") Integer group,
                                      PropertyDefinition definition) throws Exception {
        LOG.debug("addAxiomExpressionRoleGroupProperty");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.addAxiomExpressionRoleGroupProperty(conceptIri, axiom, definition, group))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{iri}/{axiom}/rolegroups/{group}/{property}/{type}/{value}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delAxiomExpressionRoleGroupProperty(@Context SecurityContext sc,
                                                        @PathParam("iri") String conceptIri,
                                                        @PathParam("axiom") String axiom,
                                                        @PathParam("group") Integer group,
                                                        @PathParam("property") String property,
                                                        @PathParam("type") String type,
                                                        @PathParam("value") String value) throws Exception {
        LOG.debug("delAxiomExpressionRoleGroupProperty");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delAxiomExpressionRoleGroupProperty(conceptIri, axiom, group, property, type, value))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{iri}/{axiom}/rolegroups/{group}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delAxiomExpressionRoleGroup(@Context SecurityContext sc,
                                                        @PathParam("iri") String conceptIri,
                                                        @PathParam("axiom") String axiom,
                                                        @PathParam("group") Integer group) throws Exception {
        LOG.debug("delAxiomExpressionRoleGroup");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delAxiomExpressionRoleGroup(conceptIri, axiom, group))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{iri}/{axiom}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delAxiom(@Context SecurityContext sc,
                                                @PathParam("iri") String conceptIri,
                                                @PathParam("axiom") String axiom) throws Exception {
        LOG.debug("delAxiom");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delAxiom(conceptIri, axiom))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }
/*
    @GET
    @Path("/{id}/supertypes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptSupertypes(@Context SecurityContext sc,
                                        @PathParam("id") String id,
                                        @QueryParam("includeInhertied") Boolean includeInherited) throws Exception {
        LOG.debug("getConceptSupertypes");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<ConceptRelation> result = imDAL.getConceptSupertypes(id, includeInherited);

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
    }*/

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
    @Path("/{id}/ancestors")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptAncestors(@Context SecurityContext sc,
                                      @PathParam("id") String id) throws Exception {
        LOG.debug("getConceptAncestors");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<Concept> result = imDAL.getAncestors(id);

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
