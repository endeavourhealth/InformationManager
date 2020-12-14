// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\MOWL.g4 by ANTLR 4.9
package org.endeavourhealth.informationmanager.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MOWLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MOWLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MOWLParser#iriRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIriRef(MOWLParser.IriRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#prefixedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixedName(MOWLParser.PrefixedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#entity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEntity(MOWLParser.EntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#classentity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassentity(MOWLParser.ClassentityContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#objectPropertyEntity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPropertyEntity(MOWLParser.ObjectPropertyEntityContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#coreProperties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoreProperties(MOWLParser.CorePropertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(MOWLParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#classAxiom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassAxiom(MOWLParser.ClassAxiomContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#subClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubClass(MOWLParser.SubClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#equivalent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquivalent(MOWLParser.EquivalentContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#disjoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjoint(MOWLParser.DisjointContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#objectPropertyAxiom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPropertyAxiom(MOWLParser.ObjectPropertyAxiomContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#objectPropertyExpressionAnnotatedList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPropertyExpressionAnnotatedList(MOWLParser.ObjectPropertyExpressionAnnotatedListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#classExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassExpression(MOWLParser.ClassExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#intersection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntersection(MOWLParser.IntersectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#classRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassRole(MOWLParser.ClassRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#atomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomic(MOWLParser.AtomicContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#individual}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndividual(MOWLParser.IndividualContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#restriction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestriction(MOWLParser.RestrictionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#nonNegativeInteger}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonNegativeInteger(MOWLParser.NonNegativeIntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#dataclassRole}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataclassRole(MOWLParser.DataclassRoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#objectPropertyList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPropertyList(MOWLParser.ObjectPropertyListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#objectProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectProperty(MOWLParser.ObjectPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#dataProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataProperty(MOWLParser.DataPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#dataPropertyIRI}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataPropertyIRI(MOWLParser.DataPropertyIRIContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#dataAtomic}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataAtomic(MOWLParser.DataAtomicContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(MOWLParser.DataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(MOWLParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#typedLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedLiteral(MOWLParser.TypedLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#stringLiteralNoLanguage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteralNoLanguage(MOWLParser.StringLiteralNoLanguageContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#stringLiteralWithLanguage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteralWithLanguage(MOWLParser.StringLiteralWithLanguageContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#lexicalValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLexicalValue(MOWLParser.LexicalValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#dataPropertyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataPropertyExpression(MOWLParser.DataPropertyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#dataTypeRestriction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataTypeRestriction(MOWLParser.DataTypeRestrictionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#facet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFacet(MOWLParser.FacetContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#restrictionValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRestrictionValue(MOWLParser.RestrictionValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#inverseObjectProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInverseObjectProperty(MOWLParser.InverseObjectPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#decimalLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecimalLiteral(MOWLParser.DecimalLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#integerLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerLiteral(MOWLParser.IntegerLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#floatingPointLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatingPointLiteral(MOWLParser.FloatingPointLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#dataRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataRange(MOWLParser.DataRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#dataConjunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataConjunction(MOWLParser.DataConjunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#annotationAnnotatedList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationAnnotatedList(MOWLParser.AnnotationAnnotatedListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(MOWLParser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#annotationPropertyIRI}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationPropertyIRI(MOWLParser.AnnotationPropertyIRIContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#annotationTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationTarget(MOWLParser.AnnotationTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#annotations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotations(MOWLParser.AnnotationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#literalList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralList(MOWLParser.LiteralListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#objectPropertyExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPropertyExpression(MOWLParser.ObjectPropertyExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#ataPropertyIRI}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtaPropertyIRI(MOWLParser.AtaPropertyIRIContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#datatypeIRI}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatypeIRI(MOWLParser.DatatypeIRIContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#objectPropertyIRI}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectPropertyIRI(MOWLParser.ObjectPropertyIRIContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#individualIRI}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndividualIRI(MOWLParser.IndividualIRIContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#datatypePropertyIRI}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatypePropertyIRI(MOWLParser.DatatypePropertyIRIContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#classIRI}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassIRI(MOWLParser.ClassIRIContext ctx);
	/**
	 * Visit a parse tree produced by {@link MOWLParser#and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(MOWLParser.AndContext ctx);
}