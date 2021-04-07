// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\IML.g4 by ANTLR 4.9.1
package org.endeavourhealth.informationmanager.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link IMLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface IMLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link IMLParser#concept}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcept(IMLParser.ConceptContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#identifierIri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierIri(IMLParser.IdentifierIriContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#iri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIri(IMLParser.IriContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(IMLParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(IMLParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(IMLParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#description}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescription(IMLParser.DescriptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode(IMLParser.CodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#status}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatus(IMLParser.StatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLParser#version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersion(IMLParser.VersionContext ctx);
}