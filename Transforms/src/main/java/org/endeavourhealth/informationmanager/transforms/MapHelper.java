package org.endeavourhealth.informationmanager.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class MapHelper {

   public static void addMap(TTConcept c, TTIriRef assuranceLevel,String target, String targetTermCode,
                             TTIriRef matchType, Integer priority, String advice) {
      if (matchType==null)
         matchType= IM.MATCHED_TO;
      TTValue maps= c.get(IM.HAS_MAP);
      if (maps==null) {
         maps = new TTArray();
         c.set(IM.HAS_MAP, maps);
      }
      TTNode map= new TTNode();
      maps.asArray().add(map);
      map.set(iri(IM.NAMESPACE+"assuranceLevel"),assuranceLevel);
      map.set(matchType,iri(target));
      if (targetTermCode!=null)
         if (!targetTermCode.equals(""))
            map.set(IM.MATCHED_TERM_CODE,literal(targetTermCode));
      if (advice!=null)
         map.set(TTIriRef.iri(IM.NAMESPACE+ "mapAdvice"),TTLiteral.literal(advice));
      if (priority!=null)
         map.set(TTIriRef.iri(IM.NAMESPACE+"mapPriority"),TTLiteral.literal(priority));
   }



}
