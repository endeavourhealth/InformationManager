package org.endeavourhealth.informationmanager.concepteditor;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
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
   private HTTPRepository db;
   RepositoryConnection conn;



   public IMLangChecker(){
      db= new HTTPRepository("http://localhost:7200/", "InformationModel");
      conn= db.getConnection();
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

   public String getConcept(String text){
      lexer.setInputStream(CharStreams.fromString(text));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      String textIri= parser.concept().identifierIri().iri().getText();

      return null;
   }


   public Collection<String> checkSyntax(String text) {
      expectedLiterals.clear();
      if (text=="")
         return null;
      setBadTokenStart(text.length()+1);
      lexer.setInputStream(CharStreams.fromString(text));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      //parser= new IMLangParser(tokens);
      //IMLangErrorListener errorListener= new IMLangErrorListener();
      //parser.removeErrorListeners();
      //parser.addErrorListener(errorListener);
      parser.setTokenStream(tokens);
      errorListener.getErrorMessages().clear();
      IMLangParser.ConceptContext entityContext= parser.concept();
      if (errorListener.getErrorMessages()!=null) {
         String suggestion = "Suggested input : ";
         if (errorListener.getExpectedTokens()!=null){
            for (Interval interval:errorListener.getExpectedTokens().getIntervals()) {
               String literal = parser.getVocabulary().getDisplayName(interval.a);
               if (literal != null) {
                  expectedLiterals.add(literal);
                  literal = parser.getVocabulary().getDisplayName(interval.b);
                  if (literal != null) {
                     expectedLiterals.add(literal);
                  }
               }
            }
         }
         if (errorListener.getBadToken()!=null)
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
