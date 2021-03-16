package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.junit.jupiter.api.Test;

import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class OWLToTTTest {

   @Test
   void convertAxiom() throws DataFormatException {
      String owl="SubClassOf(:247361008 ObjectIntersectionOf(:35363006 "
      +"ObjectSomeValuesFrom(:609096000 ObjectSomeValuesFrom(:363698007 :818983003))))";
      OWLToTT converter= new OWLToTT();
      converter.convertAxiom(new TTConcept(),owl);
   }
}