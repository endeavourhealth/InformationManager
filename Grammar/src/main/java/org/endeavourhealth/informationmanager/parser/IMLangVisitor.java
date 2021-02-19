// Generated from C:/Users/Richard Collier/Documents/IdeaProjects/Endeavour/InformationManager/Grammar/src/main/resources\IMLang.g4 by ANTLR 4.9.1
package org.endeavourhealth.informationmanager.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link IMLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface IMLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link IMLangParser#concept}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConcept(IMLangParser.ConceptContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#classAxiom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassAxiom(IMLangParser.ClassAxiomContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#propertyAxiom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyAxiom(IMLangParser.PropertyAxiomContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(IMLangParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#classType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassType(IMLangParser.ClassTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#dataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataType(IMLangParser.DataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#shape}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShape(IMLangParser.ShapeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#recordType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordType(IMLangParser.RecordTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#objectProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectProperty(IMLangParser.ObjectPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#dataProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataProperty(IMLangParser.DataPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#annotationProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationProperty(IMLangParser.AnnotationPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#members}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembers(IMLangParser.MembersContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#expansion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpansion(IMLangParser.ExpansionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#valueSet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueSet(IMLangParser.ValueSetContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#shapeOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShapeOf(IMLangParser.ShapeOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#propertyConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyConstraint(IMLangParser.PropertyConstraintContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#constraintParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintParameter(IMLangParser.ConstraintParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#minCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinCount(IMLangParser.MinCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#maxCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxCount(IMLangParser.MaxCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#minInclusive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinInclusive(IMLangParser.MinInclusiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#maxInclusive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxInclusive(IMLangParser.MaxInclusiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#minExclusive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinExclusive(IMLangParser.MinExclusiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#maxExclusive}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMaxExclusive(IMLangParser.MaxExclusiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#classValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassValue(IMLangParser.ClassValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabel(IMLangParser.LabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#status}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatus(IMLangParser.StatusContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersion(IMLangParser.VersionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#identifierIri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierIri(IMLangParser.IdentifierIriContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(IMLangParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#description}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDescription(IMLangParser.DescriptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#code}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCode(IMLangParser.CodeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#scheme}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScheme(IMLangParser.SchemeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#subclassOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubclassOf(IMLangParser.SubclassOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#equivalentTo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquivalentTo(IMLangParser.EquivalentToContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#disjointWith}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjointWith(IMLangParser.DisjointWithContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#subpropertyOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubpropertyOf(IMLangParser.SubpropertyOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#inverseOf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInverseOf(IMLangParser.InverseOfContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#classExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassExpression(IMLangParser.ClassExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#iri}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIri(IMLangParser.IriContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#roleGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoleGroup(IMLangParser.RoleGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#role}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRole(IMLangParser.RoleContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#dataRange}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataRange(IMLangParser.DataRangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#rangeValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRangeValue(IMLangParser.RangeValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#typedString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedString(IMLangParser.TypedStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#valueCollection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueCollection(IMLangParser.ValueCollectionContext ctx);
	/**
	 * Visit a parse tree produced by {@link IMLangParser#dataRangeCollection}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDataRangeCollection(IMLangParser.DataRangeCollectionContext ctx);
}