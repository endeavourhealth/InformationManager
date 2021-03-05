package org.endeavourhealth.informationmanager.converter;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

import java.util.*;
import java.util.stream.Collectors;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class IMAccess {


   public static List<String> getDisplayDefinition(RepositoryConnection conn, IRI iri,List<Namespace> namespaces) {
      List<String> display= new ArrayList<>();
      Resource graph= iri("http://www.ontotext.com/explicit");
      RepositoryResult<Statement> statements = conn.getStatements(iri, null, null,false,graph);
      addResourceStatementsToDisplay(conn, iri, namespaces,display,"");

      return display;
   }

   private static void addResourceStatementsToDisplay(RepositoryConnection conn, Resource resource,
                                                      List<Namespace> namespaces,
                                                      List<String> display,
                                                      String offset) {
      Resource graph= iri("http://www.ontotext.com/explicit");
      RepositoryResult<Statement> statements = conn.getStatements(resource, null, null,false,graph);
      for(Statement s : statements) {
         String item=offset;
         Resource subject= s.getSubject();
         if (offset.equals(""))
            item= getShortIri((org.eclipse.rdf4j.model.IRI) subject,namespaces);
         Resource predicate= s.getPredicate();
         item = item+" " + getShortIri((org.eclipse.rdf4j.model.IRI) predicate,namespaces);
         Value object= s.getObject();
         if (object.isBNode()){
            display.add(item);
            addResourceStatementsToDisplay(conn,(Resource) object,namespaces,display,offset+"  ");
         } else if (object.isIRI()){
            item=item+" "+getShortIri((org.eclipse.rdf4j.model.IRI) object,namespaces);
            display.add(item);
         } else {
            item=item+" "+ object.stringValue();
            display.add(item);
         }
      }
   }


   public static org.eclipse.rdf4j.model.IRI getFullIri(String siri, List<Namespace> namespaces) {
      for (org.eclipse.rdf4j.model.Namespace ns:namespaces){
         String prefix= ns.getPrefix();
         if (prefix.equals(siri.substring(0,siri.indexOf(':'))))
            return Values.iri(ns.getName() + siri.substring(siri.indexOf(':')+1));
      }
      return null;
   }

   public static List<String> getDisplay(Model model, IRI iri){
      List<String> display= new ArrayList<>();

      return display;

   }

   public static String getShortIri(IRI iri,List<Namespace> namespaces){
      for (Namespace ns:namespaces) {
         if (ns.getName().equals(iri.getNamespace()))
            return ns.getPrefix() + ":" + iri.getLocalName();
      }
      return null;
   }


}
