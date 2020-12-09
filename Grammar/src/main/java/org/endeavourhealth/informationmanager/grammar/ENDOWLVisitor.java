// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\ENDOWL.g4 by ANTLR 4.9
package org.endeavourhealth.informationmanager.grammar;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ENDOWLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ENDOWLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#entitytype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntitytype(ENDOWLParser.EntitytypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#classentity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassentity(ENDOWLParser.ClassentityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#classaxiomtype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassaxiomtype(ENDOWLParser.ClassaxiomtypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#subclass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubclass(ENDOWLParser.SubclassContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#equivalent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquivalent(ENDOWLParser.EquivalentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#objectproperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectproperty(ENDOWLParser.ObjectpropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#propertyaxiomtype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyaxiomtype(ENDOWLParser.PropertyaxiomtypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#subobjectproperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubobjectproperty(ENDOWLParser.SubobjectpropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#objectpropertychain}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectpropertychain(ENDOWLParser.ObjectpropertychainContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#parentclasses}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentclasses(ENDOWLParser.ParentclassesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#objectproperties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectproperties(ENDOWLParser.ObjectpropertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#propertyvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyvalue(ENDOWLParser.PropertyvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#cardinality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCardinality(ENDOWLParser.CardinalityContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#rangeexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeexpression(ENDOWLParser.RangeexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#namedclass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedclass(ENDOWLParser.NamedclassContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#valueset}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueset(ENDOWLParser.ValuesetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#union}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnion(ENDOWLParser.UnionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#complexclass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexclass(ENDOWLParser.ComplexclassContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#only}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOnly(ENDOWLParser.OnlyContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#min}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMin(ENDOWLParser.MinContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#max}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMax(ENDOWLParser.MaxContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#exact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExact(ENDOWLParser.ExactContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#iri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIri(ENDOWLParser.IriContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#ws}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWs(ENDOWLParser.WsContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(ENDOWLParser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#sp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSp(ENDOWLParser.SpContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#htab}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHtab(ENDOWLParser.HtabContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#cr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCr(ENDOWLParser.CrContext ctx);
	/**
	 * Visit a parse tree produced by {@link ENDOWLParser#lf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLf(ENDOWLParser.LfContext ctx);
}