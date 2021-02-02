package org.endeavourhealth.informationmanager.concepteditor;

import javax.swing.*;

public class EditorApp {

   public static void main(String[] args) {

      SwingUtilities.invokeLater(() -> {
        new ConceptEditor(new IMLangChecker()).createAndShowGUI("");
      });
   }
}
