package org.endeavourhealth.informationmanager.grammar;
import org.antlr.v4.runtime.*;

import java.io.IOException;

public class Antapp {

      public static void main(String[] args) throws IOException {
            CharStream input = CharStreams.fromFileName(args[0]);
            ENDOWLLexer lexer = new ENDOWLLexer(input);
            CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
            ENDOWLParser parser = new ENDOWLParser(commonTokenStream);
            ENDOWLParser.EntitytypeContext context= parser.entitytype();
            ENDOWLVisitor visitor = new ENDOWLVisitorImp();
            visitor.visitEntitytype(context);
      }
}
