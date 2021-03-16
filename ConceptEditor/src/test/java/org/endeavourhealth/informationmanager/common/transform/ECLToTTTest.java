package org.endeavourhealth.informationmanager.common.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import static org.junit.jupiter.api.Assertions.*;

class ECLToTTTest {

   @org.junit.jupiter.api.Test
   void getClassExpression() throws JsonProcessingException {
      String ecl = "<< 272379006 |Event (event)| OR "
          + "<< 363787002 |Observable entity (observable entity)| OR "
   +"<< 404684003 |Clinical finding (finding)| OR << 416698001 |Link assertion (link assertion)|";
      ECLToTT converter= new ECLToTT();
      TTValue range= converter.getClassExpression(ecl);
      TTConcept concept= new TTConcept();
      concept.set(RDFS.RANGE,range);
      concept.setIri("sn:2222222");
      ObjectMapper om = new ObjectMapper();
      String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(concept);
      System.out.println(json);
   }
}