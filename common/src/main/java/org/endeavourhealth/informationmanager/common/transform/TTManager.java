package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTPrefix;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TTManager {

   public static TTDocument createDocument(String graph){
      TTDocument document = new TTDocument();
      document.setPrefixes(getDefaultPrefixes());
      document.setGraph(graph);
      return document;
   }


   public static List<TTPrefix> getDefaultPrefixes() {
      List<TTPrefix> ps= new ArrayList<>();
      ps.add(new TTPrefix(IM.NAMESPACE,"im"));
      ps.add(new TTPrefix(SNOMED.NAMESPACE,"sn"));
      ps.add(new TTPrefix(OWL.NAMESPACE,"owl"));
      ps.add(new TTPrefix(RDF.NAMESPACE,"rdf"));
      ps.add(new TTPrefix(RDFS.NAMESPACE,"rdfs"));
      ps.add(new TTPrefix(XSD.NAMESPACE,"xsd"));
      ps.add(new TTPrefix("http://endhealth.info/READ2#","r2"));
      ps.add(new TTPrefix("http://endhealth.info/CTV3#","ctv3"));
      ps.add(new TTPrefix("http://endhealth.info/EMIS#","emis"));
      ps.add(new TTPrefix("http://endhealth.info/TPP#","tpp"));
      ps.add(new TTPrefix("http://endhealth.info/Barts_Cerner#","bc"));
      ps.add(new TTPrefix(SHACL.NAMESPACE,"sh"));
      ps.add(new TTPrefix("http://www.w3.org/ns/prov#","prov"));
      ps.add(new TTPrefix("https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-OrganizationRole-1#","orole"));
      return ps;

   }

}
