package org.endeavourhealth.informationmanager.common.transform;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.IntervalSet;

public class IMSyntaxError {
   private String msg;
   private int line;
   private int charPositionInLine;
   private IntervalSet expectedTokens;
   private org.antlr.v4.runtime.CommonToken badToken;
   private Parser parser;
   private Lexer lexer;

   public String getMsg() {
      return msg;
   }

   public IMSyntaxError setMsg(String msg) {
      this.msg = msg;
      return this;
   }

   public int getOffendingPosition(){
      if (badToken!=null)
         return badToken.getStartIndex();
      else
         return -1;
   }

   public int getLine() {
      return line;
   }

   public IMSyntaxError setLine(int line) {
      this.line = line;
      return this;
   }

   public int getCharPositionInLine() {
      return charPositionInLine;
   }

   public IMSyntaxError setCharPositionInLine(int charPositionInLine) {
      this.charPositionInLine = charPositionInLine;
      return this;
   }

   public IntervalSet getExpectedTokens() {
      return expectedTokens;
   }

   public IMSyntaxError setExpectedTokens(IntervalSet expectedTokens) {
      this.expectedTokens = expectedTokens;
      return this;
   }

   public CommonToken getBadToken() {
      return badToken;
   }

   public IMSyntaxError setBadToken(CommonToken badToken) {
      this.badToken = badToken;
      return this;
   }

   public Parser getParser() {
      return parser;
   }

   public IMSyntaxError setParser(Parser parser) {
      this.parser = parser;
      return this;
   }

   public Lexer getLexer() {
      return lexer;
   }

   public IMSyntaxError setLexer(Lexer lexer) {
      this.lexer = lexer;
      return this;
   }
}
