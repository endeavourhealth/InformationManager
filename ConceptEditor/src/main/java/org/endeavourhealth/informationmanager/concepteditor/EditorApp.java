package org.endeavourhealth.informationmanager.concepteditor;

import javax.swing.*;

public class EditorApp {

   public static void main(String[] args) {

/*
      MOWLChecker checker= new MOWLChecker();
      Collection<String> errors= checker.checkSyntax("Class: P",0);
      if (errors!=null){
         errors.stream().forEach((s-> System.out.println("Incorrect entry " +s)));

      }
*/
      SwingUtilities.invokeLater(() -> {
        new ConceptEditor(new IMLangChecker()).createAndShowGUI("");
      });
   }
}
