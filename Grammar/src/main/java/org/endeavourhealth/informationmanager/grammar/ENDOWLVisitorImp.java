package org.endeavourhealth.informationmanager.grammar;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class ENDOWLVisitorImp extends ENDOWLBaseVisitor {

   public Object visitAxiom(ENDOWLParser.EntitytypeContext ctx) {
      List<ParseTree> kids = ctx.children;
      for (ParseTree kid : kids) {
         System.out.println(kid.getText());
         moreChildren(kid);
      }
      return null;
   }
   private void moreChildren(ParseTree parent){
      for (int i=0; i<parent.getChildCount();i++){
         ParseTree child= parent.getChild(i);
         System.out.println(child.getText());
      }

   }
}