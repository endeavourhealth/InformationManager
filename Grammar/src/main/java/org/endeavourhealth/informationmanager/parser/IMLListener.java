// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\IML.g4 by ANTLR 4.9.1
package org.endeavourhealth.informationmanager.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IMLParser}.
 */
public interface IMLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IMLParser#concept}.
	 * @param ctx the parse tree
	 */
	void enterConcept(IMLParser.ConceptContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#concept}.
	 * @param ctx the parse tree
	 */
	void exitConcept(IMLParser.ConceptContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#identifierIri}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierIri(IMLParser.IdentifierIriContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#identifierIri}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierIri(IMLParser.IdentifierIriContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#iri}.
	 * @param ctx the parse tree
	 */
	void enterIri(IMLParser.IriContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#iri}.
	 * @param ctx the parse tree
	 */
	void exitIri(IMLParser.IriContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(IMLParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(IMLParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(IMLParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(IMLParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(IMLParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(IMLParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#description}.
	 * @param ctx the parse tree
	 */
	void enterDescription(IMLParser.DescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#description}.
	 * @param ctx the parse tree
	 */
	void exitDescription(IMLParser.DescriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#code}.
	 * @param ctx the parse tree
	 */
	void enterCode(IMLParser.CodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#code}.
	 * @param ctx the parse tree
	 */
	void exitCode(IMLParser.CodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#status}.
	 * @param ctx the parse tree
	 */
	void enterStatus(IMLParser.StatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#status}.
	 * @param ctx the parse tree
	 */
	void exitStatus(IMLParser.StatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLParser#version}.
	 * @param ctx the parse tree
	 */
	void enterVersion(IMLParser.VersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLParser#version}.
	 * @param ctx the parse tree
	 */
	void exitVersion(IMLParser.VersionContext ctx);
}