package org.endeavourhealth.informationmanager.concepteditor;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.endeavourhealth.informationmanager.parser.IMLangLexer;
import org.endeavourhealth.informationmanager.parser.IMLangParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IMLangChecker implements EditorChecker {

   private IMLangLexer lexer;
   private IMLangParser parser;
   private IMLangVisitorImp visitor;
   private IMLangErrorListener errorListener;
   private Integer badTokenStart;
   private List<String> expectedLiterals;


   public IMLangChecker(){
      lexer = new IMLangLexer(null);
      lexer.removeErrorListeners();
      errorListener = new IMLangErrorListener();
      lexer.addErrorListener(errorListener);
      parser = new IMLangParser((null));
      parser.removeErrorListeners();
      parser.addErrorListener(errorListener);
      visitor= new IMLangVisitorImp();
      expectedLiterals = new ArrayList<>();
   }

   @Override
   public Collection<String> checkSyntax(String text) {
      expectedLiterals.clear();
      if (text=="")
         return null;

      //IMLangLexer lexer = new IMLangLexer(CharStreams.fromString(text));
      lexer.setInputStream(CharStreams.fromString(text));
      //lexer._input= CharStreams.fromString(text);
      CommonTokenStream tokens = new CommonTokenStream(lexer);
     //IMLangParser parser= new IMLangParser(tokens);
      //IMLangErrorListener errorListener= new IMLangErrorListener();
      //parser.removeErrorListeners();
      //parser.addErrorListener(errorListener);
      parser.setTokenStream(tokens);
      errorListener.getErrorMessages().clear();
      IMLangParser.ConceptContext entityContext= parser.concept();
      if (errorListener.getErrorMessages()!=null) {
         String suggestion = "Suggested input : ";
         for (Interval interval:errorListener.getExpectedTokens().getIntervals()){
            String literal= parser.getVocabulary().getDisplayName(interval.a);
            if (literal!=null) {
               expectedLiterals.add(literal);
               literal = parser.getVocabulary().getDisplayName(interval.b);
               if (literal != null) {
                  expectedLiterals.add(literal);
               }
            }
         }
         setBadTokenStart(errorListener.getBadToken().getStartIndex());
         return errorListener.getErrorMessages();
      }
      //String result = visitor.visitEntity(parser.entity());
      return null;
   }

   public IMLangLexer getLexer() {
      return lexer;
   }

   public IMLangChecker setLexer(IMLangLexer lexer) {
      this.lexer = lexer;
      return this;
   }

   public IMLangParser getParser() {
      return parser;
   }

   public IMLangChecker setParser(IMLangParser parser) {
      this.parser = parser;
      return this;
   }

   public IMLangVisitorImp getVisitor() {
      return visitor;
   }

   public IMLangChecker setVisitor(IMLangVisitorImp visitor) {
      this.visitor = visitor;
      return this;
   }

   public IMLangErrorListener getErrorListener() {
      return errorListener;
   }

   public IMLangChecker setErrorListener(IMLangErrorListener errorListener) {
      this.errorListener = errorListener;
      return this;
   }

   public Integer getBadTokenStart() {
      return badTokenStart;
   }

   public IMLangChecker setBadTokenStart(Integer badTokenStart) {
      this.badTokenStart = badTokenStart;
      return this;
   }

   public List<String> getExpectedLiterals() {
      return expectedLiterals;
   }

   public IMLangChecker setExpectedLiterals(List<String> expectedLiterals) {
      this.expectedLiterals = expectedLiterals;
      return this;
   }
}
