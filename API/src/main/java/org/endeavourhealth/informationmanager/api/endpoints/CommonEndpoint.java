package org.endeavourhealth.informationmanager.api.endpoints;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Axiom;
import org.endeavourhealth.informationmanager.common.models.Concept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("")
public class CommonEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(CommonEndpoint.class);
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

    @GET
    @Path("/operators")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperators(@Context SecurityContext sc) throws Exception {
        LOG.debug("getOperators");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<String> result = imDAL.getOperators();

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @GET
    @Path("/statuses")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConceptStatues(@Context SecurityContext sc) throws Exception {
        LOG.debug("getConceptStatuses");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<Concept> result = imDAL.getStatuses();

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }
}

