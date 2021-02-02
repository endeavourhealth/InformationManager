package org.endeavourhealth.informationmanager.concepteditor;

import java.util.Collection;

public interface EditorChecker {

      Collection<String> checkSyntax(String text);
      Integer getBadTokenStart();
      Collection<String> getExpectedLiterals();
      String getConcept(String text);
}
