// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\IMLang.g4 by ANTLR 4.9
package org.endeavourhealth.informationmanager.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IMLangParser}.
 */
public interface IMLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IMLangParser#concept}.
	 * @param ctx the parse tree
	 */
	void enterConcept(IMLangParser.ConceptContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#concept}.
	 * @param ctx the parse tree
	 */
	void exitConcept(IMLangParser.ConceptContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#classAxiom}.
	 * @param ctx the parse tree
	 */
	void enterClassAxiom(IMLangParser.ClassAxiomContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#classAxiom}.
	 * @param ctx the parse tree
	 */
	void exitClassAxiom(IMLangParser.ClassAxiomContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#propertyAxiom}.
	 * @param ctx the parse tree
	 */
	void enterPropertyAxiom(IMLangParser.PropertyAxiomContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#propertyAxiom}.
	 * @param ctx the parse tree
	 */
	void exitPropertyAxiom(IMLangParser.PropertyAxiomContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(IMLangParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(IMLangParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#classType}.
	 * @param ctx the parse tree
	 */
	void enterClassType(IMLangParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#classType}.
	 * @param ctx the parse tree
	 */
	void exitClassType(IMLangParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(IMLangParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(IMLangParser.DataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#shape}.
	 * @param ctx the parse tree
	 */
	void enterShape(IMLangParser.ShapeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#shape}.
	 * @param ctx the parse tree
	 */
	void exitShape(IMLangParser.ShapeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#recordType}.
	 * @param ctx the parse tree
	 */
	void enterRecordType(IMLangParser.RecordTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#recordType}.
	 * @param ctx the parse tree
	 */
	void exitRecordType(IMLangParser.RecordTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#objectProperty}.
	 * @param ctx the parse tree
	 */
	void enterObjectProperty(IMLangParser.ObjectPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#objectProperty}.
	 * @param ctx the parse tree
	 */
	void exitObjectProperty(IMLangParser.ObjectPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#dataProperty}.
	 * @param ctx the parse tree
	 */
	void enterDataProperty(IMLangParser.DataPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#dataProperty}.
	 * @param ctx the parse tree
	 */
	void exitDataProperty(IMLangParser.DataPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#annotationProperty}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationProperty(IMLangParser.AnnotationPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#annotationProperty}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationProperty(IMLangParser.AnnotationPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#members}.
	 * @param ctx the parse tree
	 */
	void enterMembers(IMLangParser.MembersContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#members}.
	 * @param ctx the parse tree
	 */
	void exitMembers(IMLangParser.MembersContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#expansion}.
	 * @param ctx the parse tree
	 */
	void enterExpansion(IMLangParser.ExpansionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#expansion}.
	 * @param ctx the parse tree
	 */
	void exitExpansion(IMLangParser.ExpansionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#valueSet}.
	 * @param ctx the parse tree
	 */
	void enterValueSet(IMLangParser.ValueSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#valueSet}.
	 * @param ctx the parse tree
	 */
	void exitValueSet(IMLangParser.ValueSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#shapeOf}.
	 * @param ctx the parse tree
	 */
	void enterShapeOf(IMLangParser.ShapeOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#shapeOf}.
	 * @param ctx the parse tree
	 */
	void exitShapeOf(IMLangParser.ShapeOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#propertyConstraint}.
	 * @param ctx the parse tree
	 */
	void enterPropertyConstraint(IMLangParser.PropertyConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#propertyConstraint}.
	 * @param ctx the parse tree
	 */
	void exitPropertyConstraint(IMLangParser.PropertyConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#constraintParameter}.
	 * @param ctx the parse tree
	 */
	void enterConstraintParameter(IMLangParser.ConstraintParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#constraintParameter}.
	 * @param ctx the parse tree
	 */
	void exitConstraintParameter(IMLangParser.ConstraintParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#minCount}.
	 * @param ctx the parse tree
	 */
	void enterMinCount(IMLangParser.MinCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#minCount}.
	 * @param ctx the parse tree
	 */
	void exitMinCount(IMLangParser.MinCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#maxCount}.
	 * @param ctx the parse tree
	 */
	void enterMaxCount(IMLangParser.MaxCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#maxCount}.
	 * @param ctx the parse tree
	 */
	void exitMaxCount(IMLangParser.MaxCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#minInclusive}.
	 * @param ctx the parse tree
	 */
	void enterMinInclusive(IMLangParser.MinInclusiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#minInclusive}.
	 * @param ctx the parse tree
	 */
	void exitMinInclusive(IMLangParser.MinInclusiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#maxInclusive}.
	 * @param ctx the parse tree
	 */
	void enterMaxInclusive(IMLangParser.MaxInclusiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#maxInclusive}.
	 * @param ctx the parse tree
	 */
	void exitMaxInclusive(IMLangParser.MaxInclusiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#minExclusive}.
	 * @param ctx the parse tree
	 */
	void enterMinExclusive(IMLangParser.MinExclusiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#minExclusive}.
	 * @param ctx the parse tree
	 */
	void exitMinExclusive(IMLangParser.MinExclusiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#maxExclusive}.
	 * @param ctx the parse tree
	 */
	void enterMaxExclusive(IMLangParser.MaxExclusiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#maxExclusive}.
	 * @param ctx the parse tree
	 */
	void exitMaxExclusive(IMLangParser.MaxExclusiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#classValue}.
	 * @param ctx the parse tree
	 */
	void enterClassValue(IMLangParser.ClassValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#classValue}.
	 * @param ctx the parse tree
	 */
	void exitClassValue(IMLangParser.ClassValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(IMLangParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(IMLangParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#status}.
	 * @param ctx the parse tree
	 */
	void enterStatus(IMLangParser.StatusContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#status}.
	 * @param ctx the parse tree
	 */
	void exitStatus(IMLangParser.StatusContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#version}.
	 * @param ctx the parse tree
	 */
	void enterVersion(IMLangParser.VersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#version}.
	 * @param ctx the parse tree
	 */
	void exitVersion(IMLangParser.VersionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#identifierIri}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierIri(IMLangParser.IdentifierIriContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#identifierIri}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierIri(IMLangParser.IdentifierIriContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(IMLangParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(IMLangParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#description}.
	 * @param ctx the parse tree
	 */
	void enterDescription(IMLangParser.DescriptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#description}.
	 * @param ctx the parse tree
	 */
	void exitDescription(IMLangParser.DescriptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#code}.
	 * @param ctx the parse tree
	 */
	void enterCode(IMLangParser.CodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#code}.
	 * @param ctx the parse tree
	 */
	void exitCode(IMLangParser.CodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#scheme}.
	 * @param ctx the parse tree
	 */
	void enterScheme(IMLangParser.SchemeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#scheme}.
	 * @param ctx the parse tree
	 */
	void exitScheme(IMLangParser.SchemeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#subclassOf}.
	 * @param ctx the parse tree
	 */
	void enterSubclassOf(IMLangParser.SubclassOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#subclassOf}.
	 * @param ctx the parse tree
	 */
	void exitSubclassOf(IMLangParser.SubclassOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#equivalentTo}.
	 * @param ctx the parse tree
	 */
	void enterEquivalentTo(IMLangParser.EquivalentToContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#equivalentTo}.
	 * @param ctx the parse tree
	 */
	void exitEquivalentTo(IMLangParser.EquivalentToContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#disjointWith}.
	 * @param ctx the parse tree
	 */
	void enterDisjointWith(IMLangParser.DisjointWithContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#disjointWith}.
	 * @param ctx the parse tree
	 */
	void exitDisjointWith(IMLangParser.DisjointWithContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#subpropertyOf}.
	 * @param ctx the parse tree
	 */
	void enterSubpropertyOf(IMLangParser.SubpropertyOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#subpropertyOf}.
	 * @param ctx the parse tree
	 */
	void exitSubpropertyOf(IMLangParser.SubpropertyOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#inverseOf}.
	 * @param ctx the parse tree
	 */
	void enterInverseOf(IMLangParser.InverseOfContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#inverseOf}.
	 * @param ctx the parse tree
	 */
	void exitInverseOf(IMLangParser.InverseOfContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#classExpression}.
	 * @param ctx the parse tree
	 */
	void enterClassExpression(IMLangParser.ClassExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#classExpression}.
	 * @param ctx the parse tree
	 */
	void exitClassExpression(IMLangParser.ClassExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#objectCollection}.
	 * @param ctx the parse tree
	 */
	void enterObjectCollection(IMLangParser.ObjectCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#objectCollection}.
	 * @param ctx the parse tree
	 */
	void exitObjectCollection(IMLangParser.ObjectCollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#iri}.
	 * @param ctx the parse tree
	 */
	void enterIri(IMLangParser.IriContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#iri}.
	 * @param ctx the parse tree
	 */
	void exitIri(IMLangParser.IriContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#roleGroup}.
	 * @param ctx the parse tree
	 */
	void enterRoleGroup(IMLangParser.RoleGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#roleGroup}.
	 * @param ctx the parse tree
	 */
	void exitRoleGroup(IMLangParser.RoleGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#role}.
	 * @param ctx the parse tree
	 */
	void enterRole(IMLangParser.RoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#role}.
	 * @param ctx the parse tree
	 */
	void exitRole(IMLangParser.RoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#dataRange}.
	 * @param ctx the parse tree
	 */
	void enterDataRange(IMLangParser.DataRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#dataRange}.
	 * @param ctx the parse tree
	 */
	void exitDataRange(IMLangParser.DataRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#rangeValue}.
	 * @param ctx the parse tree
	 */
	void enterRangeValue(IMLangParser.RangeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#rangeValue}.
	 * @param ctx the parse tree
	 */
	void exitRangeValue(IMLangParser.RangeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#typedString}.
	 * @param ctx the parse tree
	 */
	void enterTypedString(IMLangParser.TypedStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#typedString}.
	 * @param ctx the parse tree
	 */
	void exitTypedString(IMLangParser.TypedStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#valueCollection}.
	 * @param ctx the parse tree
	 */
	void enterValueCollection(IMLangParser.ValueCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#valueCollection}.
	 * @param ctx the parse tree
	 */
	void exitValueCollection(IMLangParser.ValueCollectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link IMLangParser#dataRangeCollection}.
	 * @param ctx the parse tree
	 */
	void enterDataRangeCollection(IMLangParser.DataRangeCollectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link IMLangParser#dataRangeCollection}.
	 * @param ctx the parse tree
	 */
	void exitDataRangeCollection(IMLangParser.DataRangeCollectionContext ctx);
}