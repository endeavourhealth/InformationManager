package org.endeavourhealth.informationmanager.common.transform;

import org.endeavourhealth.imapi.model.tripletree.TTConcept;

import java.util.Collection;
import java.util.List;

public interface EditorChecker {

      List<IMSyntaxError> checkSyntax(String text);
      Integer getBadTokenStart();
      Collection<String> getExpectedLiterals();
      TTConcept parseToConcept(String text);
}
