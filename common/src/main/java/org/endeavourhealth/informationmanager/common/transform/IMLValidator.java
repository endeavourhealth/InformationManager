package org.endeavourhealth.informationmanager.common.transform;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.parser.imlang.IMLangLexer;
import org.endeavourhealth.imapi.parser.imlang.IMLangParser;

import java.sql.SQLException;
import java.util.*;


/**
 * An object that checks a string of text against the IM grammar and creates a number of helper objects such as:
 * <p> a list of suggested tokens, a help message, position of bad token, bad token, and a list of antlr syntax errors</p>
 * @throws SQLException
 * @throws ClassNotFoundException
 */
public class IMLValidator {

   private final IMLangLexer lexer = new IMLangLexer(null);
   private final IMLangParser parser= new IMLangParser(null);
   private final IMLangToTT visitor = new IMLangToTT();
   private IMLErrorListener errorListener;
   private Integer badTokenStart;
   private final List<String> autoSuggestions = new ArrayList<>();
   private final List<String> semanticErrors= new ArrayList<>();
   private int caretPos;
   private List<IMSyntaxError> syntaxErrors= new ArrayList<>();
   private int selectedToken;
   private String helpMessage;
   private boolean isGoodToken;
   private String badToken;
   private IMLSemanticCheck semanticCheck;
   private IMLangParser.EntityContext entityCtx;


   /**
    * A constructor that requires db connection parameters and creates the various lexer and parser objects
    * @throws SQLException
    * @throws ClassNotFoundException
    */
   public IMLValidator() throws SQLException, ClassNotFoundException {


      errorListener= new IMLErrorListener(this);
      lexer.removeErrorListeners();
      lexer.addErrorListener(errorListener);
      parser.removeErrorListeners();
      parser.addErrorListener(errorListener);
      semanticCheck= new IMLSemanticCheck(this);

   }


   public TTEntity parseToEntity(String text){
      lexer.setInputStream(CharStreams.fromString(text));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      IMLangParser.EntityContext ctx = parser.entity();
      TTEntity entity= visitor.visitEntity(ctx);


      return entity;
   }

   /**
    * Checks syntax and semantics of IM language including IM look ups for IRIs when being entered
    * @param text the text to be checked
    * @param cPos the caret position in the text that determines the helper objects
    */
   public void checkSyntax(String text,int cPos){
      this.caretPos= cPos;
      badToken=null;
      badTokenStart=null;
      helpMessage="";
      //Clear down the auto suggestions
      autoSuggestions.clear();
      if (text.equals(""))
         return;

      //remove previous errors
      getSyntaxErrors().clear();
      getSemanticErrors().clear();;
      //Parse the text
      lexer.setInputStream(CharStreams.fromString(text));
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      parser.setTokenStream(tokens);
      entityCtx= parser.entity();

      //Is the entity simply incomplete in which case a semantic check of only is required.
      CommonToken offendingToken= errorListener.getOffendingSymbol();
      if (offendingToken!=null){
         if (offendingToken.getText().equals("<EOF>")) {
            semanticCheck.visitEntity(entityCtx);
            setHelp();
         } else {
            //Syntax check only
            if (syntaxErrors.size()>0) {
               setAutoSuggestions();
               setHelp();
            }

         }
      } else {
         //Semantic check only
         semanticCheck.visitEntity(entityCtx);
      }


   }


   //Help text
   private void setHelp() {
      StringBuilder help= new StringBuilder();
         for (IMSyntaxError error:syntaxErrors) {
            if (error.getExpectedTokens() != null) {
               help.append("Expecting ");
               for (Interval interval : error.getExpectedTokens().getIntervals()) {
                  int first = interval.a;
                  int second = interval.b;
                  String display = error.getParser().getVocabulary().getDisplayName(first);
                  help.append(display).append(", ");
                  if (second != first)
                     for (int i = first + 1; i < (second + 1); i++) {
                        display = error.getParser().getVocabulary()
                          .getDisplayName(i);
                        help.append(display).append(", ");
                     }
               }
            } else if (error.getMsg() != null)
               help.append(error.getMsg()).append(" ");
         }
      helpMessage= help.toString();
   }
   public String getBestToken(String prefix){
      if (autoSuggestions==null)
         return null;
      String best=null;
      for (String token:autoSuggestions)
         if (token.startsWith(prefix))
            best= token;
      return best;
   }
   private boolean isGoodToken(String token){
      for (String test:autoSuggestions)
         if (test.startsWith(token))
            return true;
      return false;
   }

   //Creates auto suggestion list
   private void setAutoSuggestions() {
      selectedToken = 0;
      autoSuggestions.clear();
      if (syntaxErrors.get(0).getExpectedTokens()==null)
         return;
      badTokenStart = syntaxErrors.get(0).getBadToken().getStartIndex();
      badToken = syntaxErrors.get(0).getBadToken().getText();
      if (badTokenStart > -1) {
         IMSyntaxError error = syntaxErrors.get(0);
         if (error.getExpectedTokens() != null) {
            for (Interval interval : error.getExpectedTokens().getIntervals()) {
               int first = interval.a;
               int second = interval.b;
               String literal = error.getParser().getVocabulary().getLiteralName(first);
               String literal2 = error.getParser().getVocabulary().getLiteralName(second);
               if (literal != null) {
                  literal= literal.toLowerCase().replace("'","");
                  autoSuggestions.add(literal);
                  if (literal2 != null && (!literal2.equals(literal))) {
                     for (int i = first + 1; i < (second + 1); i++) {
                        String lit= error.getParser().getVocabulary()
                          .getLiteralName(i).toLowerCase()
                          .replace("'","");
                        autoSuggestions.add(lit);

                     }
                  }
               }
            }
         }
         Collections.sort(autoSuggestions);
         isGoodToken=false;
         for (int i=0;i<autoSuggestions.size();i++) {
            String token = autoSuggestions.get(i);
            if (token.startsWith(badToken)) {
               selectedToken = i;
               isGoodToken = true;
            }
         }
      }

   }



   public Integer getBadTokenStart() {
      return badTokenStart;
   }



   public List<String> getAutoSuggestions() {
      return autoSuggestions;
   }


   public String getHelpMessage() {
      return helpMessage;
   }


   public String getBadToken() {
      return badToken;
   }

   public int getSelectedToken() {
      return selectedToken;
   }

   public IMLValidator setSelectedToken(int selectedToken) {
      this.selectedToken = selectedToken;
      return this;
   }

   public boolean isGoodToken() {
      return isGoodToken;
   }

   public List<IMSyntaxError> getSyntaxErrors() {
      return syntaxErrors;
   }

   public IMLValidator addSyntaxError(IMSyntaxError error){
      syntaxErrors.add(error);
      return this;
   }

   public List<String> getSemanticErrors() {
      return semanticErrors;
   }

   public IMLValidator addSemanticError(String error){
      semanticErrors.add(error);
      return this;
   }
}
