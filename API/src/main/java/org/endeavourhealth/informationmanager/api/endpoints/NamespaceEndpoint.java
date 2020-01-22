package org.endeavourhealth.informationmanager.api.endpoints;

import org.endeavourhealth.informationmanager.common.dal.InformationManagerDAL;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("namespaces")
public class NamespaceEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(NamespaceEndpoint.class);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNamespaces(@Context SecurityContext sc) throws Exception {
        LOG.debug("getNamespaces");

        try(InformationManagerDAL imDAL = new InformationManagerJDBCDAL()) {
            List<Namespace> result = imDAL.getNamespaces();

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }
}
