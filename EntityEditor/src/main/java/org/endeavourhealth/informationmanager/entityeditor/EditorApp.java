package org.endeavourhealth.informationmanager.entityeditor;

import org.endeavourhealth.informationmanager.common.transform.IMLValidator;

import javax.swing.*;
import java.sql.SQLException;

public class EditorApp {

   public static void main(String[] args) {

      SwingUtilities.invokeLater(() -> {
         try {
            new org.endeavourhealth.informationmanager.entityeditor.EntityEditor(new IMLValidator(),args[0]).createAndShowGUI("iri ");
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
