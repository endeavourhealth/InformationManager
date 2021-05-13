package org.endeavourhealth.informationmanager.common.transform;

import java.util.Collection;

public interface EditorChecker {

      Collection<String> checkSyntax(String text);
      Integer getBadTokenStart();
      Collection<String> getExpectedLiterals();
      String getConcept(String text);
}
