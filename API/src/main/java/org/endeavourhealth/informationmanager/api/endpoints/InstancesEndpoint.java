package org.endeavourhealth.informationmanager.api.endpoints;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.endeavourhealth.common.security.SecurityUtils;
import org.endeavourhealth.informationmanager.common.ZipUtils;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.dal.InstanceJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Document;
import org.endeavourhealth.informationmanager.common.models.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.util.List;

@Path("instances")
@Api(tags = {"Instances"})
public class InstancesEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(InstancesEndpoint.class);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "List of registered instances")
    public Response getInstances(@Context SecurityContext sc) throws Exception {
        LOG.debug("getInstances");

        try (InstanceJDBCDAL instanceDAL = new InstanceJDBCDAL()) {
            List<Instance> result = instanceDAL.getInstances();

            return Response
                .ok()
                .entity(result)
                .build();
        }
    }

    @POST
    @Path("/{instanceDbid}/documents/{documentDbid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Send a document to an instance")
    public Response sendDocument(@Context SecurityContext sc,
                                 @PathParam("instanceDbid") Integer instanceDbid,
                                 @PathParam("documentDbid") Integer documentDbid) throws Exception {
        LOG.debug("sendDocument");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL();
            InstanceJDBCDAL instanceDAL = new InstanceJDBCDAL()) {
            byte[] documentData = imDAL.getDocumentLatestPublished(documentDbid);
            Instance instance = instanceDAL.getInstance(instanceDbid);

            Client client = ClientBuilder.newClient();
            WebTarget target = client
                .target(instance.getUrl())
                .path("/management/documents");

            return target
                .request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.getKeycloakSecurityContext(sc).getTokenString())
                .post(Entity.entity(documentData, MediaType.APPLICATION_OCTET_STREAM));
        }
    }

    @GET
    @Path("/{instanceDbid}/documents/{documentDbid}/drafts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get new document drafts from an instance")
    public Response getDocument(@Context SecurityContext sc,
                                ContainerRequestContext requestContext,
                                @PathParam("instanceDbid") Integer instanceDbid,
                                @PathParam("documentDbid") Integer documentDbid) throws Exception {
        LOG.debug("getDocumentDrafts");

        try(InformationManagerJDBCDAL imDAL = new InformationManagerJDBCDAL();
            InstanceJDBCDAL instanceDAL = new InstanceJDBCDAL()) {

            Document document = imDAL.getDocument(documentDbid);
            Instance instance = instanceDAL.getInstance(instanceDbid);

            Client client = ClientBuilder.newClient();
            byte[] draftData = client
                .target(instance.getUrl())
                .path("/management/documents/" + document.getPath() + "/drafts")
                .request()
                .header(HttpHeaders.AUTHORIZATION, requestContext.getHeaderString(HttpHeaders.AUTHORIZATION))
                .get(byte[].class);

            String draftJson = new String(ZipUtils.decompress(draftData));

            imDAL.beginTransaction();
            imDAL.processDraftDocument(
                SecurityUtils.getCurrentUserId(sc).toString(),
                SecurityUtils.getToken(sc).getPreferredUsername(),
                instance.getName(),
                document.getPath(),
                draftJson);
            imDAL.commit();

            return Response
                .ok()
                .build();
        }
    }
}
