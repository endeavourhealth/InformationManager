package org.endeavourhealth.informationmanager.common.transform;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.endeavourhealth.informationmanager.parser.MOWLLexer;
import org.endeavourhealth.informationmanager.parser.MOWLParser;

import java.io.IOException;

public class ManchesterParser {
   private MOWLParser parser;

   public void loadFile(String  filename) throws IOException {
      CharStream input = CharStreams.fromFileName(filename);
      MOWLLexer lexer = new MOWLLexer(input);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser = new MOWLParser((tokens));
   }
}
