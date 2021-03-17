package org.endeavourhealth.informationmanager;

import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class ClosureGeneratorTest {

   @Test
   public void generatorTest() throws SQLException, IOException, ClassNotFoundException {
      ClosureGenerator gen= new ClosureGenerator();
      gen.generateClosure("c:\\temp\\");

   }
}