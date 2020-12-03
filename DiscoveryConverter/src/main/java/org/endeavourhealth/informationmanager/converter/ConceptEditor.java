package org.endeavourhealth.informationmanager.converter;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;

public class ConceptEditor {
   private Client client;
   public String editConcept(String iri) throws MalformedURLException {
      client= ClientBuilder.newClient();
      String json= getRefFromIri(iri);
      return json;
   }
   private String getRefFromIri(String request) throws MalformedURLException {
      URL url= new URL("https://directory.spineservices.nhs.uk/STU3/Organization/"+"B82025");
      //URL url= new URL("http://localhost:8080/api/concept/"+ request);
      WebTarget target = client.target(url.toString());
      Response response=  target
          .request().get();
      return response.readEntity(String.class);
   }
}
