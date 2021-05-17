package org.endeavourhealth.informationmanager.common.transform;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.informationmanager.parser.ECLParser;
import org.endeavourhealth.informationmanager.parser.IMLangLexer;
import org.endeavourhealth.informationmanager.parser.IMLangParser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class IMLangChecker implements EditorChecker {

   private IMLangLexer lexer;
   private IMLangParser parser;
   private IMLangToTT visitor;
   private IMLangErrorListener errorListener;
   private Integer badTokenStart;
   private List<String> expectedLiterals;
   Connection conn;



   public IMLangChecker() throws SQLException, ClassNotFoundException {

      conn= getConnection();
      lexer = new IMLangLexer(null);
      lexer.removeErrorListeners();
      errorListener = new IMLangErrorListener();
      lexer.addErrorListener(errorListener);
      parser = new IMLangParser((null));
      parser.removeErrorListeners();
      parser.addErrorListener(errorListener);
      visitor= new IMLangToTT();
      expectedLiterals = new ArrayList<>();

   }


   private Connection getConnection() throws ClassNotFoundException, SQLException {
      Map<String, String> envVars = System.getenv();

      String url = envVars.get("CONFIG_JDBC_URL");
      String user = envVars.get("CONFIG_JDBC_USERNAME");
      String pass = envVars.get("CONFIG_JDBC_PASSWORD");
      String driver = envVars.get("CONFIG_JDBC_CLASS");

      if (url == null || url.isEmpty()
          || user == null || user.isEmpty()
          || pass == null || pass.isEmpty())
         throw new IllegalStateException("You need to set the CONFIG_JDBC_ environment variables!");

      if (driver != null && !driver.isEmpty())
         Class.forName(driver);

      Properties props = new Properties();

      props.setProperty("user", user);
      props.setProperty("password", pass);

      return DriverManager.getConnection(url, props);
   }


   public TTConcept parseToConcept(String text){
      lexer.setInputStream(CharStreams.fromString(text));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      IMLangParser.ConceptContext ctx = parser.concept();
      TTConcept concept= visitor.visitConcept(ctx);


      return concept;
   }


   public List<IMSyntaxError> checkSyntax(String text) {
      expectedLiterals.clear();
      if (text=="")
         return null;
      errorListener.getErrors().clear();
      setBadTokenStart(text.length()+1);
      lexer.setInputStream(CharStreams.fromString(text));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      IMLangParser.ConceptContext entityContext= parser.concept();
      return errorListener.getErrors();
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

   public IMLangToTT getVisitor() {
      return visitor;
   }

   public IMLangChecker setVisitor(IMLangToTT visitor) {
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
