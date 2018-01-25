
package org.endeavourhealth.informationmodel.api.endpoints;

import com.codahale.metrics.annotation.Timed;
import io.astefanutti.metrics.aspectj.Metrics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.endeavourhealth.common.security.annotations.RequiresAdmin;
import org.endeavourhealth.informationmodel.api.logic.AttributeLogic;
import org.endeavourhealth.informationmodel.api.models.AttributePrimitiveValue;
import org.endeavourhealth.informationmodel.api.models.Concept;
import org.endeavourhealth.informationmodel.api.models.ConceptAttribute;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/attribute")
@Metrics(registry = "attributeMetricRegistry")
@Api(value="Attributes", description = "Initial api for all calls relating to concept attributes")
public class AttributeEndpoint {
    AttributeLogic _attributeLogic = new AttributeLogic();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.AttributeEndpoint.Concept.Load")
    @ApiOperation(value = "Get concept attributes for a given concept and optional attribute type")
    @RequiresAdmin
    public Response getAttributes(@Context SecurityContext sc,
                                     @ApiParam(value = "Concept for which to get attributes") @QueryParam("conceptId") Long conceptId,
                                     @ApiParam(value = "Attribute type to filter by") @QueryParam("attributeId") Long attributeId) throws Exception {

        List<ConceptAttribute> conceptAttributes = _attributeLogic.getConceptAttributes(conceptId, attributeId);

        return Response
            .ok(conceptAttributes)
            .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.AttributeEndpoint.Concept.Save")
    @Path("/concept")
    @ApiOperation(value = "Adds a new concept value to a concept attribute")
    @RequiresAdmin
    public Response saveConceptValue(@Context SecurityContext sc,
                         @ApiParam(value = "Concept value to save") ConceptAttribute conceptAttribute
    ) throws Exception {

        _attributeLogic.saveAttributeConceptValue(conceptAttribute);

        return Response
            .ok()
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.AttributeEndpoint.Concept.Load")
    @Path("/concept")
    @ApiOperation(value = "Get concept value attributes for a given concept and class")
    @RequiresAdmin
    public Response getConceptValues(@Context SecurityContext sc,
                               @ApiParam(value = "Concept for which to get attributes") @QueryParam("conceptId") Long conceptId,
                               @ApiParam(value = "Class to filter by") @QueryParam("classId") Long classId) throws Exception {

        List<Concept> attributeConceptValues = _attributeLogic.getConceptValueAttributes(conceptId, classId);

        return Response
            .ok(attributeConceptValues)
            .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.AttributeEndpoint.Primitive.Save")
    @Path("/primitive")
    @ApiOperation(value = "Adds a new primitive value to a concept attribute")
    @RequiresAdmin
    public Response savePrimitiveValue(@Context SecurityContext sc,
                                     @ApiParam(value = "Primitive value to save") AttributePrimitiveValue attributePrimitiveValue
    ) throws Exception {

        _attributeLogic.saveAttributePrimitiveValue(attributePrimitiveValue);

        return Response
            .ok()
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Timed(absolute = true, name = "InformationManager.AttributeEndpoint.Primitive.Load")
    @Path("/primitive")
    @ApiOperation(value = "Get primitive value attributes for a concept and class")
    @RequiresAdmin
    public Response getPrimitiveValues(@Context SecurityContext sc,
                                     @ApiParam(value = "Concept for which to get attributes") @QueryParam("conceptId") Long conceptId,
                                     @ApiParam(value = "Class to filter by") @QueryParam("classId") Long classId) throws Exception {

        List<AttributePrimitiveValue> attributePrimitiveValues = _attributeLogic.getPrimitiveValueAttributes(conceptId, classId);

        return Response
            .ok(attributePrimitiveValues)
            .build();
    }
}
