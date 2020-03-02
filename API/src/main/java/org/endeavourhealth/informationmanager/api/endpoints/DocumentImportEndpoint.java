package org.endeavourhealth.informationmanager.api.endpoints;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.endeavourhealth.informationmanager.DocumentImportHelper;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.InputStreamReader;

@Path("document")
public class DocumentImportEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentImportEndpoint.class);

    /**
     * Rest API to Import all the Concept, ConceptAxioms from JSON to DB tables
     * @param uploadedInputStream
     * @param fileDetail
     * @return
     * @throws Exception
     */
    @POST
    @Path("/documentImport")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response documentImport(@FormDataParam("file") InputStream uploadedInputStream,
                                   @FormDataParam("file") FormDataContentDisposition fileDetail) throws Exception {

        DocumentImportHelper documentImportHelper = new DocumentImportHelper();
        JsonElement element = new JsonParser().parse(new InputStreamReader(uploadedInputStream));
        JSONObject jsonObject = new JSONObject(element.getAsJsonObject().toString());

        JSONObject informationModel = jsonObject.has("InformationModel") ? (JSONObject) jsonObject.get("InformationModel") : null;
        if(informationModel != null) {
            documentImportHelper.populateNamespaces(informationModel);
            documentImportHelper.populateConcepts(informationModel);
            documentImportHelper.populateConceptAxioms(informationModel);
        }

        return Response.ok().build();
    }

}
