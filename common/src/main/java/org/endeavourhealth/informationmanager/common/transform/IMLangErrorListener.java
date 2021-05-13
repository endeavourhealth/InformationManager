package org.endeavourhealth.informationmanager.common.transform;


import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.IntervalSet;


import java.util.ArrayList;
import java.util.List;

public class IMLangErrorListener extends BaseErrorListener {

   private String partialToken;
   private List<String> errorMessages;
   private String errorOffset;
   private Recognizer recognizer;
   private org.antlr.v4.runtime.CommonToken badToken;
   private IntervalSet expectedTokens;

   public IMLangErrorListener(){
      errorMessages = new ArrayList<>();

   }

   @Override
   public void syntaxError(Recognizer<?,?> recognizer,
                           Object offendingSymbol,
                           int line,
                           int charPositionInLine,
                           String msg,
                           RecognitionException e){
         errorMessages.clear();
         this.recognizer= recognizer;
         if (offendingSymbol instanceof org.antlr.v4.runtime.CommonToken)
            badToken= (org.antlr.v4.runtime.CommonToken) offendingSymbol;
         if (recognizer instanceof Lexer){
            errorMessages.add(msg + " line "+line+ " offset "+charPositionInLine);
         }
         else {
            Parser parser = (Parser) recognizer;
            ParserRuleContext ctx = parser.getContext();
            expectedTokens = parser.getExpectedTokens();
            String message = "Expecting " + expectedTokens.toString(parser.getVocabulary());
            errorMessages.add(message + " line " + line + " offset " + charPositionInLine);
         }

   }

   public String getErrorOffset() {
      return errorOffset;
   }

   public IMLangErrorListener setErrorOffset(String errorOffset) {
      this.errorOffset = errorOffset;
      return this;
   }

   public String getPartialToken() {
      return partialToken;
   }

   public IMLangErrorListener setPartialToken(String partialToken) {
      this.partialToken = partialToken;
      return this;
   }

   public List<String> getErrorMessages() {
      return errorMessages;
   }

   public IMLangErrorListener setErrorMessages(List<String> errorMessages) {
      this.errorMessages = errorMessages;
      return this;
   }

   public Recognizer getRecognizer() {
      return recognizer;
   }

   public IMLangErrorListener setRecognizer(Recognizer recognizer) {
      this.recognizer = recognizer;
      return this;
   }

   public CommonToken getBadToken() {
      return badToken;
   }

   public IMLangErrorListener setBadToken(CommonToken badToken) {
      this.badToken = badToken;
      return this;
   }

   public IntervalSet getExpectedTokens() {
      return expectedTokens;
   }

   public IMLangErrorListener setExpectedTokens(IntervalSet expectedTokens) {
      this.expectedTokens = expectedTokens;
      return this;
   }
}
