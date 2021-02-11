package org.endeavourhealth.informationmanager.common.transform;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.base.AbstractNamespace;
import org.eclipse.rdf4j.model.util.Namespaces;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class DRDFManager {
   private Map<String,String> namespaces;

   public DRDFManager(){
      setDefaultNamespaces();
   }

   private void setDefaultNamespaces(){
    namespaces= new HashMap<>();
      namespaces.put(":",NamespaceIri.DISCOVERY.getValue());
      namespaces.put("sn:",NamespaceIri.SNOMED.getValue());
      namespaces.put("owl:","http://www.w3.org/2002/07/owl#");
      namespaces.put("rdf:","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
      namespaces.put("xml:","http://www.w3.org/XML/1998/namespace#");
      namespaces.put("xsd:","http://www.w3.org/2001/XMLSchema#");
      namespaces.put("rdfs:","http://www.w3.org/2000/01/rdf-schema#");
      namespaces.put("r2:","http://www.EndeavourHealth.org/InformationModel/Legacy/READ2#");
      namespaces.put("ctv3:","http://www.EndeavourHealth.org/InformationModel/Legacy/CTV3#");
      namespaces.put("emis:","http://www.EndeavourHealth.org/InformationModel/Legacy/EMIS#");
      namespaces.put("tpp:","http://www.EndeavourHealth.org/InformationModel/Legacy/TPP#");
      namespaces.put("bc:","http://www.EndeavourHealth.org/InformationModel/Legacy/Barts_Cerner#");
      namespaces.put("sh:","http://www.w3.org/ns/shacl#");
      namespaces.put("prov:","http://www.w3.org/ns/prov#");
      namespaces.put("orole:","https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-OrganizationRole-1#");

   }

   public IRI getIri(String conceptIri) {
      conceptIri = conceptIri.trim();

      if (conceptIri.startsWith("http"))
         return iri(conceptIri);

      String[] parts = conceptIri.split(":");
      String firstPart= namespaces.get(parts[0]+":");
      if (firstPart!=null)
         return iri(firstPart+parts[1]);
      throw new IllegalStateException("Unknown namespace prefix [" + parts[0] + "]");

   }

   public Map<String, String> getNamespaces() {
      return namespaces;
   }

   public DRDFManager setNamespaces(Map<String, String> namespaces) {
      this.namespaces = namespaces;
      return this;
   }
   public DRDFManager addNamespace(String prefix, String localName){
      this.namespaces.put(prefix,localName);
      return this;
   }

}
