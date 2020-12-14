// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\MOWL.g4 by ANTLR 4.9
package org.endeavourhealth.informationmanager.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MOWLParser}.
 */
public interface MOWLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MOWLParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void enterIriRef(MOWLParser.IriRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void exitIriRef(MOWLParser.IriRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#prefixedName}.
	 * @param ctx the parse tree
	 */
	void enterPrefixedName(MOWLParser.PrefixedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#prefixedName}.
	 * @param ctx the parse tree
	 */
	void exitPrefixedName(MOWLParser.PrefixedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#entity}.
	 * @param ctx the parse tree
	 */
	void enterEntity(MOWLParser.EntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#entity}.
	 * @param ctx the parse tree
	 */
	void exitEntity(MOWLParser.EntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#classentity}.
	 * @param ctx the parse tree
	 */
	void enterClassentity(MOWLParser.ClassentityContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#classentity}.
	 * @param ctx the parse tree
	 */
	void exitClassentity(MOWLParser.ClassentityContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#objectPropertyEntity}.
	 * @param ctx the parse tree
	 */
	void enterObjectPropertyEntity(MOWLParser.ObjectPropertyEntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#objectPropertyEntity}.
	 * @param ctx the parse tree
	 */
	void exitObjectPropertyEntity(MOWLParser.ObjectPropertyEntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#coreProperties}.
	 * @param ctx the parse tree
	 */
	void enterCoreProperties(MOWLParser.CorePropertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#coreProperties}.
	 * @param ctx the parse tree
	 */
	void exitCoreProperties(MOWLParser.CorePropertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#string}.
	 * @param ctx the parse tree
	 */
	void enterString(MOWLParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#string}.
	 * @param ctx the parse tree
	 */
	void exitString(MOWLParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#classAxiom}.
	 * @param ctx the parse tree
	 */
	void enterClassAxiom(MOWLParser.ClassAxiomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#classAxiom}.
	 * @param ctx the parse tree
	 */
	void exitClassAxiom(MOWLParser.ClassAxiomContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#subClass}.
	 * @param ctx the parse tree
	 */
	void enterSubClass(MOWLParser.SubClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#subClass}.
	 * @param ctx the parse tree
	 */
	void exitSubClass(MOWLParser.SubClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#equivalent}.
	 * @param ctx the parse tree
	 */
	void enterEquivalent(MOWLParser.EquivalentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#equivalent}.
	 * @param ctx the parse tree
	 */
	void exitEquivalent(MOWLParser.EquivalentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#disjoint}.
	 * @param ctx the parse tree
	 */
	void enterDisjoint(MOWLParser.DisjointContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#disjoint}.
	 * @param ctx the parse tree
	 */
	void exitDisjoint(MOWLParser.DisjointContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#objectPropertyAxiom}.
	 * @param ctx the parse tree
	 */
	void enterObjectPropertyAxiom(MOWLParser.ObjectPropertyAxiomContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#objectPropertyAxiom}.
	 * @param ctx the parse tree
	 */
	void exitObjectPropertyAxiom(MOWLParser.ObjectPropertyAxiomContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#objectPropertyExpressionAnnotatedList}.
	 * @param ctx the parse tree
	 */
	void enterObjectPropertyExpressionAnnotatedList(MOWLParser.ObjectPropertyExpressionAnnotatedListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#objectPropertyExpressionAnnotatedList}.
	 * @param ctx the parse tree
	 */
	void exitObjectPropertyExpressionAnnotatedList(MOWLParser.ObjectPropertyExpressionAnnotatedListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#classExpression}.
	 * @param ctx the parse tree
	 */
	void enterClassExpression(MOWLParser.ClassExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#classExpression}.
	 * @param ctx the parse tree
	 */
	void exitClassExpression(MOWLParser.ClassExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#intersection}.
	 * @param ctx the parse tree
	 */
	void enterIntersection(MOWLParser.IntersectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#intersection}.
	 * @param ctx the parse tree
	 */
	void exitIntersection(MOWLParser.IntersectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#classRole}.
	 * @param ctx the parse tree
	 */
	void enterClassRole(MOWLParser.ClassRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#classRole}.
	 * @param ctx the parse tree
	 */
	void exitClassRole(MOWLParser.ClassRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#atomic}.
	 * @param ctx the parse tree
	 */
	void enterAtomic(MOWLParser.AtomicContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#atomic}.
	 * @param ctx the parse tree
	 */
	void exitAtomic(MOWLParser.AtomicContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#individual}.
	 * @param ctx the parse tree
	 */
	void enterIndividual(MOWLParser.IndividualContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#individual}.
	 * @param ctx the parse tree
	 */
	void exitIndividual(MOWLParser.IndividualContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#restriction}.
	 * @param ctx the parse tree
	 */
	void enterRestriction(MOWLParser.RestrictionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#restriction}.
	 * @param ctx the parse tree
	 */
	void exitRestriction(MOWLParser.RestrictionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#nonNegativeInteger}.
	 * @param ctx the parse tree
	 */
	void enterNonNegativeInteger(MOWLParser.NonNegativeIntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#nonNegativeInteger}.
	 * @param ctx the parse tree
	 */
	void exitNonNegativeInteger(MOWLParser.NonNegativeIntegerContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#dataclassRole}.
	 * @param ctx the parse tree
	 */
	void enterDataclassRole(MOWLParser.DataclassRoleContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#dataclassRole}.
	 * @param ctx the parse tree
	 */
	void exitDataclassRole(MOWLParser.DataclassRoleContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#objectPropertyList}.
	 * @param ctx the parse tree
	 */
	void enterObjectPropertyList(MOWLParser.ObjectPropertyListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#objectPropertyList}.
	 * @param ctx the parse tree
	 */
	void exitObjectPropertyList(MOWLParser.ObjectPropertyListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#objectProperty}.
	 * @param ctx the parse tree
	 */
	void enterObjectProperty(MOWLParser.ObjectPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#objectProperty}.
	 * @param ctx the parse tree
	 */
	void exitObjectProperty(MOWLParser.ObjectPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#dataProperty}.
	 * @param ctx the parse tree
	 */
	void enterDataProperty(MOWLParser.DataPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#dataProperty}.
	 * @param ctx the parse tree
	 */
	void exitDataProperty(MOWLParser.DataPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#dataPropertyIRI}.
	 * @param ctx the parse tree
	 */
	void enterDataPropertyIRI(MOWLParser.DataPropertyIRIContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#dataPropertyIRI}.
	 * @param ctx the parse tree
	 */
	void exitDataPropertyIRI(MOWLParser.DataPropertyIRIContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#dataAtomic}.
	 * @param ctx the parse tree
	 */
	void enterDataAtomic(MOWLParser.DataAtomicContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#dataAtomic}.
	 * @param ctx the parse tree
	 */
	void exitDataAtomic(MOWLParser.DataAtomicContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#dataType}.
	 * @param ctx the parse tree
	 */
	void enterDataType(MOWLParser.DataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#dataType}.
	 * @param ctx the parse tree
	 */
	void exitDataType(MOWLParser.DataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(MOWLParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(MOWLParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#typedLiteral}.
	 * @param ctx the parse tree
	 */
	void enterTypedLiteral(MOWLParser.TypedLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#typedLiteral}.
	 * @param ctx the parse tree
	 */
	void exitTypedLiteral(MOWLParser.TypedLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#stringLiteralNoLanguage}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteralNoLanguage(MOWLParser.StringLiteralNoLanguageContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#stringLiteralNoLanguage}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteralNoLanguage(MOWLParser.StringLiteralNoLanguageContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#stringLiteralWithLanguage}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteralWithLanguage(MOWLParser.StringLiteralWithLanguageContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#stringLiteralWithLanguage}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteralWithLanguage(MOWLParser.StringLiteralWithLanguageContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#lexicalValue}.
	 * @param ctx the parse tree
	 */
	void enterLexicalValue(MOWLParser.LexicalValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#lexicalValue}.
	 * @param ctx the parse tree
	 */
	void exitLexicalValue(MOWLParser.LexicalValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#dataPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void enterDataPropertyExpression(MOWLParser.DataPropertyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#dataPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void exitDataPropertyExpression(MOWLParser.DataPropertyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#dataTypeRestriction}.
	 * @param ctx the parse tree
	 */
	void enterDataTypeRestriction(MOWLParser.DataTypeRestrictionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#dataTypeRestriction}.
	 * @param ctx the parse tree
	 */
	void exitDataTypeRestriction(MOWLParser.DataTypeRestrictionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#facet}.
	 * @param ctx the parse tree
	 */
	void enterFacet(MOWLParser.FacetContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#facet}.
	 * @param ctx the parse tree
	 */
	void exitFacet(MOWLParser.FacetContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#restrictionValue}.
	 * @param ctx the parse tree
	 */
	void enterRestrictionValue(MOWLParser.RestrictionValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#restrictionValue}.
	 * @param ctx the parse tree
	 */
	void exitRestrictionValue(MOWLParser.RestrictionValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#inverseObjectProperty}.
	 * @param ctx the parse tree
	 */
	void enterInverseObjectProperty(MOWLParser.InverseObjectPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#inverseObjectProperty}.
	 * @param ctx the parse tree
	 */
	void exitInverseObjectProperty(MOWLParser.InverseObjectPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void enterDecimalLiteral(MOWLParser.DecimalLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#decimalLiteral}.
	 * @param ctx the parse tree
	 */
	void exitDecimalLiteral(MOWLParser.DecimalLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntegerLiteral(MOWLParser.IntegerLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#integerLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntegerLiteral(MOWLParser.IntegerLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#floatingPointLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFloatingPointLiteral(MOWLParser.FloatingPointLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#floatingPointLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFloatingPointLiteral(MOWLParser.FloatingPointLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#dataRange}.
	 * @param ctx the parse tree
	 */
	void enterDataRange(MOWLParser.DataRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#dataRange}.
	 * @param ctx the parse tree
	 */
	void exitDataRange(MOWLParser.DataRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#dataConjunction}.
	 * @param ctx the parse tree
	 */
	void enterDataConjunction(MOWLParser.DataConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#dataConjunction}.
	 * @param ctx the parse tree
	 */
	void exitDataConjunction(MOWLParser.DataConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#annotationAnnotatedList}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationAnnotatedList(MOWLParser.AnnotationAnnotatedListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#annotationAnnotatedList}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationAnnotatedList(MOWLParser.AnnotationAnnotatedListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(MOWLParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(MOWLParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#annotationPropertyIRI}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationPropertyIRI(MOWLParser.AnnotationPropertyIRIContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#annotationPropertyIRI}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationPropertyIRI(MOWLParser.AnnotationPropertyIRIContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#annotationTarget}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTarget(MOWLParser.AnnotationTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#annotationTarget}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTarget(MOWLParser.AnnotationTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#annotations}.
	 * @param ctx the parse tree
	 */
	void enterAnnotations(MOWLParser.AnnotationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#annotations}.
	 * @param ctx the parse tree
	 */
	void exitAnnotations(MOWLParser.AnnotationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#literalList}.
	 * @param ctx the parse tree
	 */
	void enterLiteralList(MOWLParser.LiteralListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#literalList}.
	 * @param ctx the parse tree
	 */
	void exitLiteralList(MOWLParser.LiteralListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#objectPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void enterObjectPropertyExpression(MOWLParser.ObjectPropertyExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#objectPropertyExpression}.
	 * @param ctx the parse tree
	 */
	void exitObjectPropertyExpression(MOWLParser.ObjectPropertyExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#ataPropertyIRI}.
	 * @param ctx the parse tree
	 */
	void enterAtaPropertyIRI(MOWLParser.AtaPropertyIRIContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#ataPropertyIRI}.
	 * @param ctx the parse tree
	 */
	void exitAtaPropertyIRI(MOWLParser.AtaPropertyIRIContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#datatypeIRI}.
	 * @param ctx the parse tree
	 */
	void enterDatatypeIRI(MOWLParser.DatatypeIRIContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#datatypeIRI}.
	 * @param ctx the parse tree
	 */
	void exitDatatypeIRI(MOWLParser.DatatypeIRIContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#objectPropertyIRI}.
	 * @param ctx the parse tree
	 */
	void enterObjectPropertyIRI(MOWLParser.ObjectPropertyIRIContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#objectPropertyIRI}.
	 * @param ctx the parse tree
	 */
	void exitObjectPropertyIRI(MOWLParser.ObjectPropertyIRIContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#individualIRI}.
	 * @param ctx the parse tree
	 */
	void enterIndividualIRI(MOWLParser.IndividualIRIContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#individualIRI}.
	 * @param ctx the parse tree
	 */
	void exitIndividualIRI(MOWLParser.IndividualIRIContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#datatypePropertyIRI}.
	 * @param ctx the parse tree
	 */
	void enterDatatypePropertyIRI(MOWLParser.DatatypePropertyIRIContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#datatypePropertyIRI}.
	 * @param ctx the parse tree
	 */
	void exitDatatypePropertyIRI(MOWLParser.DatatypePropertyIRIContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#classIRI}.
	 * @param ctx the parse tree
	 */
	void enterClassIRI(MOWLParser.ClassIRIContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#classIRI}.
	 * @param ctx the parse tree
	 */
	void exitClassIRI(MOWLParser.ClassIRIContext ctx);
	/**
	 * Enter a parse tree produced by {@link MOWLParser#and}.
	 * @param ctx the parse tree
	 */
	void enterAnd(MOWLParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by {@link MOWLParser#and}.
	 * @param ctx the parse tree
	 */
	void exitAnd(MOWLParser.AndContext ctx);
}