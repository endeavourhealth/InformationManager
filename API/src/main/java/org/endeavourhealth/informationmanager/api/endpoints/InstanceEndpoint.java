package org.endeavourhealth.informationmanager.api.endpoints;

import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.endeavourhealth.informationmanager.common.dal.InformationManagerJDBCDAL;
import org.endeavourhealth.informationmanager.common.dal.InstanceJDBCDAL;
import org.endeavourhealth.informationmanager.common.models.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Map;

@Path("Instance")
@Metrics(registry = "InstanceMetricRegistry")
@Api(tags = {"Instance"})
public class InstanceEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(InstanceEndpoint.class);

    /******************** DOCUMENTS ********************/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InstanceEndpoint.GET")
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
    @Path("/{instanceDbid}/Document/{documentDbid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationModel.InstanceEndpoint.Document.POST")
    @ApiOperation(value = "Send a document to an instance")
    public Response sendDocument(@Context SecurityContext sc,
                                 @PathParam("instanceDbid") Integer instanceDbid,
                                 @PathParam("documentDbid") Integer documentDbid) throws Exception {
        LOG.debug("sendDocument");

        Instance instance = new InstanceJDBCDAL().getInstance(instanceDbid);
        byte[] documentData = new InformationManagerJDBCDAL().getDocumentLatestPublished(documentDbid);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(instance.getUrl()).path("/public/Management/document");

        return target
            .request()
            .post(Entity.entity(documentData, MediaType.APPLICATION_OCTET_STREAM));
    }
}
