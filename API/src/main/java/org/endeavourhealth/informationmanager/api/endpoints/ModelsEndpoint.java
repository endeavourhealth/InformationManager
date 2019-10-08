package org.endeavourhealth.informationmanager.api.endpoints;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Concept;
import org.endeavourhealth.informationmanager.common.models.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("models")
public class ModelsEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(ModelsEndpoint.class);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModels(@Context SecurityContext sc) throws Exception {
        LOG.debug("getModels");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<Model> result = imDAL.getModels();

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @POST
    @Path("/{part: .*}/concepts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response insertConcept(@Context SecurityContext sc,
                                  @PathParam("part") String modelPath,
                                  @QueryParam("id") String id,
                                  @QueryParam("name") String name) throws Exception {
        LOG.debug("insertConcept");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL()) {

            Integer modelDbid = imDAL.getModelDbid(modelPath);
            if (modelDbid == null) throw new IllegalArgumentException("Unknown model [" + modelPath + "]");
            if (id == null) throw new IllegalArgumentException("Id not specified");
            if (name == null) throw new IllegalArgumentException("Name not specified");

            imDAL.createConcept(modelDbid, id, name);

            return Response
                .ok()
                .build();
        }
    }
}
