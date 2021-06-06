package org.endeavourhealth.informationmanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.informationmanager.common.transform.exceptions.FileFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

public class TTDocumentFiler {
   private static final Logger LOG = LoggerFactory.getLogger(TTDocumentFiler.class);
   private final TTDocumentFilerJDBC dal;

   public TTDocumentFiler(TTIriRef graph) throws Exception {
      dal = new TTDocumentFilerJDBC(graph);
   }

   public void fileDocument(TTDocument document) throws SQLException, DataFormatException, IOException, FileFormatException {
      try {
         System.out.println("Saving ontology - " + (new Date().toString()));
         LOG.info("Processing namespaces");

         // Ensure all namespaces exist (auto-create)
         //Different prefixes for filing are not allowed in this version
         fileNamespaces(document.getPrefixes());

         //Sets the graph namesepace id for use in statements so they are owned by the namespace graph
         dal.setGraph(document.getGraph());

         if (document.getCrudOperation()!=null) {
            if (document.getCrudOperation().equals(IM.UPDATE_PREDICATES))
               filePredicateUpdates(document);
            else if (document.getCrudOperation().equals(IM.ADD_OBJECTS))
               fileAddPredicateObjects(document);
            else
               fileConcepts(document);
         }
         else
            fileConcepts(document);

         fileIndividuals(document);

         // Record document details, updating ontology and module
         LOG.info("Processing document-ontology-module");




         LOG.info("Ontology filed");
      } catch (Exception e) {
         LOG.info("Error - " + (new Date().toString()));
         e.printStackTrace();
         throw e;
      } finally {
         dal.close();
         System.out.println("Finished - " + (new Date().toString()));
      }
   }

   private void fileIndividuals(TTDocument document) throws DataFormatException, SQLException, JsonProcessingException, FileFormatException {
      dal.startTransaction();
      System.out.println("Filing instances...");
      if (document.getIndividuals()!=null) {
         int i = 0;
         for (TTInstance instance : document.getIndividuals()) {
            dal.fileIndividual(instance);
            i++;
            if (i % 1000 == 0) {
               System.out.println("Filed "+i+" instances of "+document.getIndividuals().size());
               dal.commit();
               dal.startTransaction();
            }
         }
      }
      dal.commit();

   }

   private void fileConcepts(TTDocument document) throws SQLException, DataFormatException, JsonProcessingException{
      System.out.println("Filing concepts.... ");
      dal.startTransaction();
      if (document.getConcepts()!=null) {
         int i = 0;
         for (TTConcept concept : document.getConcepts()) {
            dal.fileConcept(concept);
            i++;
            if (i % 1000 == 0) {
               System.out.println("Filed "+i +" concepts from "+document.getConcepts().size()+" example "+concept.getIri());
               dal.commit();
               dal.startTransaction();
            }
         }
      }
      dal.commit();
   }
   private void filePredicateUpdates(TTDocument document) throws SQLException, DataFormatException, IOException {
      System.out.println("Filing predicate updates.... ");
      dal.startTransaction();
      if (document.getConcepts()!=null) {
         int i = 0;
         for (TTConcept concept : document.getConcepts()) {
            dal.filePredicateUpdates(concept);
            i++;
            if (i % 1000 == 0) {
               System.out.println("Filed "+i +" predicate updates from "+document.getConcepts().size()+" example "+ concept.getIri());
               dal.commit();
               dal.startTransaction();
            }
         }
      }
      dal.commit();
   }

   private void fileAddPredicateObjects(TTDocument document) throws SQLException, DataFormatException, IOException {
      System.out.println("Filing predicate updates.... ");
      dal.startTransaction();
      if (document.getConcepts()!=null) {
         int i = 0;
         for (TTConcept concept : document.getConcepts()) {
            dal.fileAddPredicateObjects(concept);
            i++;
            if (i % 1000 == 0) {
               System.out.println("Filed "+i +" predicate updates from "+document.getConcepts().size()+" example "+ concept.getIri());
               dal.commit();
               dal.startTransaction();
            }
         }
      }
      dal.commit();
   }

   private void fileNamespaces(List<TTPrefix> prefixes) throws SQLException {
      if (prefixes == null || prefixes.size() == 0)
         return;
      dal.startTransaction();
      //Populates the namespace map with both namespace iri and prefix as keys
      for (TTPrefix prefix : prefixes) {
         dal.upsertNamespace(prefix);
      }
      dal.commit();
   }


}
