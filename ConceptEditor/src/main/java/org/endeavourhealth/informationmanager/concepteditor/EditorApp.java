package org.endeavourhealth.informationmanager.concepteditor;

import org.endeavourhealth.informationmanager.common.transform.IMLangChecker;

import javax.swing.*;
import java.sql.SQLException;

public class EditorApp {

   public static void main(String[] args) {

      SwingUtilities.invokeLater(() -> {
         try {
            new ConceptEditor(new IMLangChecker(),args[0]).createAndShowGUI("");
         } catch (SQLException throwables) {
            throwables.printStackTrace();
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         } catch (Exception e) {
            e.printStackTrace();
         }
      });
   }
}
