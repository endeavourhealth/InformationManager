package org.endeavourhealth.informationmanager.api.endpoints;

import org.endeavourhealth.common.utility.MetricsHelper;
import org.endeavourhealth.common.utility.MetricsTimer;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
// import org.endeavourhealth.informationmanager.common.logic.ConceptLogic;
import org.endeavourhealth.informationmanager.common.models.*;
/*
import org.endeavourhealth.informationmanager.common.models.definitionTypes.PropertyDefinition;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.PropertyDomain;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.PropertyRange;
import org.endeavourhealth.informationmanager.common.models.definitionTypes.SimpleConcept;
*/
import org.endeavourhealth.informationmanager.common.transform.model.Concept;
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
    @Path("/{iri}/asserted")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAssertedDefinition(@Context SecurityContext sc,
                                  @PathParam("iri") String iri) throws Exception {
        LOG.debug("getAssertedDefinition");

        try (InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            String result = imDAL.getAssertedDefinition(iri);

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
                : imDAL.search(terms, supertypes, size, page);

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
    public Response getConceptByIri(@Context SecurityContext sc,
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
    @Path("/{iri}/Properties")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProperties(@Context SecurityContext sc,
                                  @PathParam("iri") String iri,
                                  @QueryParam("inherited") Boolean inherited
    ) throws Exception {
        try (MetricsTimer t = MetricsHelper.recordTime("Viewer.getProperties");
             InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            LOG.debug("getProperties");

            List<Property> result = imDAL.getProperties(iri, (inherited == null) ? false : inherited);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{iri}/Definition")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDefinition(@Context SecurityContext sc,
                                  @PathParam("iri") String iri) throws Exception {
        try (MetricsTimer t = MetricsHelper.recordTime("Viewer.getDefinition");
             InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            LOG.debug("getDefinition");

            List<RelatedConcept> result = imDAL.getDefinition(iri);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{iri}/Sources")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSources(@Context SecurityContext sc,
                               @PathParam("iri") String iri,
                               @QueryParam("relationship") List<String> relationships,
                               @QueryParam("limit") Integer limit,
                               @QueryParam("page") Integer page) throws Exception {

        limit = (limit == null) ? 0 : limit;
        page = (page == null) ? 0 : page;

        try (MetricsTimer t = MetricsHelper.recordTime("Viewer.getSources");
             InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            LOG.debug("getSources");

            PagedResultSet<RelatedConcept> result = imDAL.getSources(iri, relationships, limit, page);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{iri}/Targets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTargets(@Context SecurityContext sc,
                               @PathParam("iri") String iri,
                               @QueryParam("relationship") List<String> relationships,
                               @QueryParam("limit") Integer limit,
                               @QueryParam("page") Integer page) throws Exception {

        limit = (limit == null) ? 0 : limit;
        page = (page == null) ? 0 : page;

        try (MetricsTimer t = MetricsHelper.recordTime("Viewer.getTargets");
             InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            LOG.debug("getTargets");

            PagedResultSet<RelatedConcept> result = imDAL.getTargets(iri, relationships, limit, page);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/{iri}/Tree")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTree(@Context SecurityContext sc,
                            @PathParam("iri") String iri,
                            @QueryParam("root") String root,
                            @QueryParam("relationship") List<String> relationships) throws Exception {
        try (MetricsTimer t = MetricsHelper.recordTime("Viewer.getTargets");
             InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            LOG.debug("getTree");

            List<RelatedConcept> result = imDAL.getTree(iri, root, relationships);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }
/*
    @GET
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConcept(@Context SecurityContext sc,
                                @PathParam("id") int id) throws Exception {
        LOG.debug("getConcept");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            Concept result = imDAL.getConcept(id);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteConcept(@Context SecurityContext sc,
                               @PathParam("id") int conceptId) throws Exception {
        LOG.debug("deleteConcept");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            boolean result = imDAL.deleteConcept(conceptId);

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }




    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createConcept(@Context SecurityContext sc,
                                  Concept concept) throws Exception {
        LOG.debug("createConcept");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            String iri = imDal.createConcept(concept);
            return Response
                .ok(iri)
                .build();
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
    @Path("/{id}/{axiom}/supertypes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAxiomSupertype(@Context SecurityContext sc,
                                      @PathParam("id") int id,
                                  @PathParam("axiom") String axiom,
                                  SimpleConcept definition) throws Exception {
        LOG.debug("addAxiomSupertype");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.addAxiomSupertype(id, axiom, definition.getConcept()))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}/supertypes/{supertypeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delSupertype(@Context SecurityContext sc,
                                                @PathParam("id") int id,
                                                @PathParam("supertypeId") int supertypeId) throws Exception {
        LOG.debug("delSupertype");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delSupertype(supertypeId))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @POST
    @Path("/{id}/{axiom}/rolegroups/{group}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAxiomRoleGroupProperty(@Context SecurityContext sc,
                                      @PathParam("id") int id,
                                      @PathParam("axiom") String axiom,
                                      @PathParam("group") Integer group,
                                      PropertyDefinition definition) throws Exception {
        LOG.debug("addAxiomRoleGroupProperty");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.addAxiomRoleGroupProperty(id, axiom, definition, group))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}/property/{type}/{propertyId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delAxiomExpressionRoleGroupProperty(@Context SecurityContext sc,
                                                        @PathParam("id") int id,
                                                        @PathParam("type") String type,
                                                        @PathParam("propertyId") int propertyId) throws Exception {
        LOG.debug("delAxiomExpressionRoleGroupProperty");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delProperty(propertyId, type))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}/{axiom}/rolegroups/{group}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delAxiomExpressionRoleGroup(@Context SecurityContext sc,
                                                        @PathParam("id") int id,
                                                        @PathParam("axiom") String axiom,
                                                        @PathParam("group") Integer group) throws Exception {
        LOG.debug("delAxiomExpressionRoleGroup");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delAxiomRoleGroup(id, axiom, group))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @POST
    @Path("/{id}/propertyrange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPropertyRange(@Context SecurityContext sc,
                                     @PathParam("id") int id,
                                     PropertyRange propertyRange) throws Exception {
        LOG.debug("addPropertyRange");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.addPropertyRange(id, propertyRange))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}/propertyrange/{propertyRangeId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delPropertyRange(@Context SecurityContext sc,
                                     @PathParam("id") int id,
                                     @PathParam("propertyRangeId") int propertyRangeId) throws Exception {
        LOG.debug("delPropertyRange");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delPropertyRange(propertyRangeId))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @POST
    @Path("/{id}/propertydomain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPropertyDomain(@Context SecurityContext sc,
                                     @PathParam("id") int id,
                                     PropertyDomain propertyDomain) throws Exception {
        LOG.debug("addPropertyRange");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.addPropertyDomain(id, propertyDomain))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}/propertydomain/{propertyDomainId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delPropertyDomain(@Context SecurityContext sc,
                                     @PathParam("id") int id,
                                     @PathParam("propertyDomainId") int propertyDomainId) throws Exception {
        LOG.debug("delPropertyRange");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delPropertyDomain(propertyDomainId))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}/{axiom}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delAxiom(@Context SecurityContext sc,
                                                @PathParam("id") int id,
                                                @PathParam("axiom") String axiom) throws Exception {
        LOG.debug("delAxiom");
        try (InformationManagerDAL imDal = new InformationManagerJDBCDAL()) {
            if (imDal.delAxiom(id, axiom))
                return Response.ok().build();
            else
                return Response.serverError().build();
        }
    }

    @GET
    @Path("/name")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getConceptName(@Context SecurityContext sc,
                               @QueryParam("iri") String iri) throws Exception {
        LOG.debug("getConceptName");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            String result = imDAL.getConceptName(iri);

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
                                       @PathParam("id") int id) throws Exception {
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
                                       @PathParam("id") int id) throws Exception {
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
                                      @PathParam("id") int id) throws Exception {
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
                                      @PathParam("id") int id,
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
                                      @PathParam("id") int id) throws Exception {
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
*/
}
