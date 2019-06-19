package org.endeavourhealth.informationmanager.api.endpoints;

import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.endeavourhealth.common.security.SecurityUtils;
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
import javax.ws.rs.core.*;
import java.util.List;

@Path("instances")
@Metrics(registry = "InstancesMetricRegistry")
@Api(tags = {"Instances"})
public class InstancesEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(InstancesEndpoint.class);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InstancesEndpoint.GET")
    @ApiOperation(value = "List of registered instances")
    public Response getInstances(@Context SecurityContext sc) throws Exception {
        LOG.debug("getInstances");

        List<Instance> result = new InstanceJDBCDAL().getInstances();

        return Response
            .ok()
            .entity(result)
            .build();
    }

    @POST
    @Path("/{instanceDbid}/documents/{documentDbid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InstancesEndpoint.{id}.Documents.{id}.POST")
    @ApiOperation(value = "Send a document to an instance")
    public Response sendDocument(@Context SecurityContext sc,
                                 @PathParam("instanceDbid") Integer instanceDbid,
                                 @PathParam("documentDbid") Integer documentDbid) throws Exception {
        LOG.debug("sendDocument");

        Instance instance = new InstanceJDBCDAL().getInstance(instanceDbid);
        byte[] documentData = new InformationManagerJDBCDAL().getDocumentLatestPublished(documentDbid);

        Client client = ClientBuilder.newClient();
        WebTarget target = client
            .target(instance.getUrl())
            .path("/public/management/documents");

        return target
            .request()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.getToken(sc))
            .post(Entity.entity(documentData, MediaType.APPLICATION_OCTET_STREAM));
    }

    @GET
    @Path("/{instanceDbid}/documents/{documentDbid}/drafts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.InstanceEndpoint.{id}.Document.{id}.Drafts.GET")
    @ApiOperation(value = "Get new document drafts from an instance")
    public Response getDocument(@Context SecurityContext sc,
                                @PathParam("instanceDbid") Integer instanceDbid,
                                @PathParam("documentDbid") Integer documentDbid) throws Exception {
        LOG.debug("getDocumentDrafts");

        InformationManagerJDBCDAL dal = new InformationManagerJDBCDAL();
        Document document = dal.getDocument(documentDbid);
        Instance instance = new InstanceJDBCDAL().getInstance(instanceDbid);

        Client client = ClientBuilder.newClient();
        byte[] draftData = client
            .target(instance.getUrl())
            .path("/public/management/documents/" + document.getPath() + "/drafts")
            .request()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + SecurityUtils.getToken(sc))
            .get(byte[].class);

        String draftJson = new String(InformationManagerJDBCDAL.decompress(draftData));

        dal.processDraftDocument(
            SecurityUtils.getCurrentUserId(sc).toString(),
            SecurityUtils.getToken(sc).getPreferredUsername(),
            instance.getName(),
            document.getPath(),
            draftJson);

        return Response
            .ok()
            .build();
    }
}
