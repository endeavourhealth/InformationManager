package org.endeavourhealth.informationmanager.transforms;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.informationmanager.OntologyFiler;
import org.endeavourhealth.informationmanager.common.transform.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class OrganisationRoleToIMDB{
   public static String odsNs="https://directory.spineservices.nhs.uk/STU3/CodeSystem/ODSAPI-OrganizationRole-1#";
   public static int snomedOds=1100000;
   public static Integer snomedNs=1000252;

   public static void main(String[] args) throws Exception{
      DOWLManager manager= new DOWLManager();
      File originalFile=new File(args[0]);

      Ontology ontology= manager.createOntology(OntologyIri.DISCOVERY.getValue());
      if (manager.getNamespace(odsNs)==null) {
         Namespace ns = new Namespace();
         ontology.addNamespace(ns);
         ns.setPrefix("orole:");
         ns.setIri(odsNs);
      }
      if (manager.getConcept(":OrganisationRole")==null) {
         Concept orgRole = new Concept()
             .setIri(":OrganisationRole")
             .setCode(SnomedConcept.createConcept(++snomedOds, false))
             .setName("Organisation role")
             .setDescription("A common name role played by an organisation in relation to a service provided");
         orgRole.addSubClassOf(new ClassExpression()
             .setClazz(new ConceptReference(":903021000252102")));
         ontology.addConcept(orgRole);
      }
      if (manager.getConcept(":hasOrganisationRole")==null){
         ObjectProperty hasRole = (ObjectProperty) new ObjectProperty()
             .setIri(":hasOrganisationRole")
             .setName("has organisation role")
             .setDescription("Points to an organisation role for this organisation");
         hasRole.addSubObjectPropertyOf(new PropertyAxiom()
             .setProperty(new ConceptReference(":dataModelObjectProperty")));
         ontology.addConcept(hasRole);
         hasRole.addObjectPropertyRange(new ClassExpression()
             .setClazz(new ConceptReference(":OrganisationRole")));
      }
      Object obj = new JSONParser().parse(new FileReader(args[0]));
      JSONObject jo = (JSONObject) obj;
      JSONArray concepts= (JSONArray) jo.get("concept");
      for (Object o:concepts) {
         JSONObject jconcept = (JSONObject) o;
         Long id = (Long) jconcept.get("code");
         String code = id.toString();
         String uname = (String) jconcept.get("display");
         String name = StringUtils.capitalize(uname.toLowerCase());
         Concept newRole= new Concept()
             .setIri("orole:ODS_RoleType_" + code)
             .setName(name)
             .setCode(code)
             .addSubClassOf(new ClassExpression()
             .setClazz(new ConceptReference(":OrganisationRole")));
         ontology.addConcept(newRole);
      }
      DiscoveryReasoner reasoner= new DiscoveryReasoner();
      ontology = reasoner.classify(ontology);
      //manager.saveOntology(new File(originalFile.getParent() + "//CombinedWithODSCore.json"));
      OntologyFiler filer = new OntologyFiler();
      filer.fileOntology(ontology, false);
   }


}
