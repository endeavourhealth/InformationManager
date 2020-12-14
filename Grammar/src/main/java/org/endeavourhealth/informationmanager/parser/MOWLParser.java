// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\MOWL.g4 by ANTLR 4.9
package org.endeavourhealth.informationmanager.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MOWLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		COMMA=1, O_LABEL=2, RANGE_LABEL=3, DOMAIN_LABEL=4, SUB_PROPERTY_OF_LABEL=5, 
		SUB_PROPERTY_CHAIN_LABEL=6, REFERENCE=7, INTEGER_LABEL=8, DECIMAL_LABEL=9, 
		FLOAT_LABEL=10, STRING_LABEL=11, LENGTH_LABEL=12, MIN_LENGTH_LABEL=13, 
		MAX_LENGTH_LABEL=14, PATTERN_LABEL=15, LANG_PATTERN_LABEL=16, THAT_LABEL=17, 
		ANNOTATIONS_LABEL=18, INVERSE_LABEL=19, MINUS=20, DOT=21, PLUS=22, OPEN_SQUARE_BRACE=23, 
		CLOSE_SQUARE_BRACE=24, NODE_ID=25, NOT_LABEL=26, LESS_EQUAL=27, GREATER_EQUAL=28, 
		LESS=29, GREATER=30, OPEN_CURLY_BRACE=31, CLOSE_CURLY_BRACE=32, CLASS_LABEL=33, 
		SUBCLASS_LABEL=34, EQUIVALENT_LABEL=35, DISJOINT_LABEL=36, NAME_LABEL=37, 
		DESC_LABEL=38, CODE_LABEL=39, SCHEME_LABEL=40, OR_LABEL=41, AND_LABEL=42, 
		SOME_LABEL=43, ONLY_LABEL=44, VALUE_LABEL=45, SELF_LABEL=46, MIN_LABEL=47, 
		MAX_LABEL=48, EXACTLY_LABEL=49, OBJECT_PROPERTY_LABEL=50, DATA_PROPERTY_LABEL=51, 
		ANNOTATION_PROPERTY_LABEL=52, NAMED_INDIVIDUAL_LABEL=53, LEFT_PAREN=54, 
		RIGHT_PAREN=55, IRI_REF=56, PNAME_NS=57, PNAME_LN=58, BLANK_NODE_LABEL=59, 
		VAR1=60, VAR2=61, LANGTAG=62, INTEGER=63, DECIMAL=64, DOUBLE=65, INTEGER_POSITIVE=66, 
		DECIMAL_POSITIVE=67, DOUBLE_POSITIVE=68, INTEGER_NEGATIVE=69, DECIMAL_NEGATIVE=70, 
		DOUBLE_NEGATIVE=71, EXPONENT=72, STRING_LITERAL1=73, STRING_LITERAL2=74, 
		STRING_LITERAL_LONG1=75, STRING_LITERAL_LONG2=76, ECHAR=77, NIL=78, ANON=79, 
		PN_CHARS_U=80, VARNAME=81, PN_PREFIX=82, PN_LOCAL=83, WS=84, LANGUAGE_TAG=85;
	public static final int
		RULE_iriRef = 0, RULE_prefixedName = 1, RULE_entity = 2, RULE_classentity = 3, 
		RULE_objectPropertyEntity = 4, RULE_coreProperties = 5, RULE_string = 6, 
		RULE_classAxiom = 7, RULE_subClass = 8, RULE_equivalent = 9, RULE_disjoint = 10, 
		RULE_objectPropertyAxiom = 11, RULE_objectPropertyExpressionAnnotatedList = 12, 
		RULE_classExpression = 13, RULE_intersection = 14, RULE_classRole = 15, 
		RULE_atomic = 16, RULE_individual = 17, RULE_restriction = 18, RULE_nonNegativeInteger = 19, 
		RULE_dataclassRole = 20, RULE_objectPropertyList = 21, RULE_objectProperty = 22, 
		RULE_dataProperty = 23, RULE_dataPropertyIRI = 24, RULE_dataAtomic = 25, 
		RULE_dataType = 26, RULE_literal = 27, RULE_typedLiteral = 28, RULE_stringLiteralNoLanguage = 29, 
		RULE_stringLiteralWithLanguage = 30, RULE_lexicalValue = 31, RULE_dataPropertyExpression = 32, 
		RULE_dataTypeRestriction = 33, RULE_facet = 34, RULE_restrictionValue = 35, 
		RULE_inverseObjectProperty = 36, RULE_decimalLiteral = 37, RULE_integerLiteral = 38, 
		RULE_floatingPointLiteral = 39, RULE_dataRange = 40, RULE_dataConjunction = 41, 
		RULE_annotationAnnotatedList = 42, RULE_annotation = 43, RULE_annotationPropertyIRI = 44, 
		RULE_annotationTarget = 45, RULE_annotations = 46, RULE_literalList = 47, 
		RULE_objectPropertyExpression = 48, RULE_ataPropertyIRI = 49, RULE_datatypeIRI = 50, 
		RULE_objectPropertyIRI = 51, RULE_individualIRI = 52, RULE_datatypePropertyIRI = 53, 
		RULE_classIRI = 54, RULE_and = 55;
	private static String[] makeRuleNames() {
		return new String[] {
			"iriRef", "prefixedName", "entity", "classentity", "objectPropertyEntity", 
			"coreProperties", "string", "classAxiom", "subClass", "equivalent", "disjoint", 
			"objectPropertyAxiom", "objectPropertyExpressionAnnotatedList", "classExpression", 
			"intersection", "classRole", "atomic", "individual", "restriction", "nonNegativeInteger", 
			"dataclassRole", "objectPropertyList", "objectProperty", "dataProperty", 
			"dataPropertyIRI", "dataAtomic", "dataType", "literal", "typedLiteral", 
			"stringLiteralNoLanguage", "stringLiteralWithLanguage", "lexicalValue", 
			"dataPropertyExpression", "dataTypeRestriction", "facet", "restrictionValue", 
			"inverseObjectProperty", "decimalLiteral", "integerLiteral", "floatingPointLiteral", 
			"dataRange", "dataConjunction", "annotationAnnotatedList", "annotation", 
			"annotationPropertyIRI", "annotationTarget", "annotations", "literalList", 
			"objectPropertyExpression", "ataPropertyIRI", "datatypeIRI", "objectPropertyIRI", 
			"individualIRI", "datatypePropertyIRI", "classIRI", "and"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "','", "'o'", null, null, null, null, "'^^'", "'integer'", "'decimal'", 
			"'float'", "'string'", "'length'", "'minLength'", "'maxLength'", "'pattern'", 
			"'langPattern'", "'that'", null, "'inverse'", "'-'", "'.'", "'+'", "'['", 
			"']'", "'_:'", "'not'", "'<='", "'>='", "'<'", "'>'", "'{'", "'}'", null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"'Self '", null, null, null, "'ObjectProperty:'", "'DataProperty:'", 
			"'AnnotaionProperty:'", "'NamedIndividual'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "COMMA", "O_LABEL", "RANGE_LABEL", "DOMAIN_LABEL", "SUB_PROPERTY_OF_LABEL", 
			"SUB_PROPERTY_CHAIN_LABEL", "REFERENCE", "INTEGER_LABEL", "DECIMAL_LABEL", 
			"FLOAT_LABEL", "STRING_LABEL", "LENGTH_LABEL", "MIN_LENGTH_LABEL", "MAX_LENGTH_LABEL", 
			"PATTERN_LABEL", "LANG_PATTERN_LABEL", "THAT_LABEL", "ANNOTATIONS_LABEL", 
			"INVERSE_LABEL", "MINUS", "DOT", "PLUS", "OPEN_SQUARE_BRACE", "CLOSE_SQUARE_BRACE", 
			"NODE_ID", "NOT_LABEL", "LESS_EQUAL", "GREATER_EQUAL", "LESS", "GREATER", 
			"OPEN_CURLY_BRACE", "CLOSE_CURLY_BRACE", "CLASS_LABEL", "SUBCLASS_LABEL", 
			"EQUIVALENT_LABEL", "DISJOINT_LABEL", "NAME_LABEL", "DESC_LABEL", "CODE_LABEL", 
			"SCHEME_LABEL", "OR_LABEL", "AND_LABEL", "SOME_LABEL", "ONLY_LABEL", 
			"VALUE_LABEL", "SELF_LABEL", "MIN_LABEL", "MAX_LABEL", "EXACTLY_LABEL", 
			"OBJECT_PROPERTY_LABEL", "DATA_PROPERTY_LABEL", "ANNOTATION_PROPERTY_LABEL", 
			"NAMED_INDIVIDUAL_LABEL", "LEFT_PAREN", "RIGHT_PAREN", "IRI_REF", "PNAME_NS", 
			"PNAME_LN", "BLANK_NODE_LABEL", "VAR1", "VAR2", "LANGTAG", "INTEGER", 
			"DECIMAL", "DOUBLE", "INTEGER_POSITIVE", "DECIMAL_POSITIVE", "DOUBLE_POSITIVE", 
			"INTEGER_NEGATIVE", "DECIMAL_NEGATIVE", "DOUBLE_NEGATIVE", "EXPONENT", 
			"STRING_LITERAL1", "STRING_LITERAL2", "STRING_LITERAL_LONG1", "STRING_LITERAL_LONG2", 
			"ECHAR", "NIL", "ANON", "PN_CHARS_U", "VARNAME", "PN_PREFIX", "PN_LOCAL", 
			"WS", "LANGUAGE_TAG"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MOWL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MOWLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class IriRefContext extends ParserRuleContext {
		public TerminalNode IRI_REF() { return getToken(MOWLParser.IRI_REF, 0); }
		public TerminalNode VARNAME() { return getToken(MOWLParser.VARNAME, 0); }
		public PrefixedNameContext prefixedName() {
			return getRuleContext(PrefixedNameContext.class,0);
		}
		public TerminalNode STRING_LITERAL1() { return getToken(MOWLParser.STRING_LITERAL1, 0); }
		public IriRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iriRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterIriRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitIriRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitIriRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IriRefContext iriRef() throws RecognitionException {
		IriRefContext _localctx = new IriRefContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_iriRef);
		try {
			setState(116);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				match(IRI_REF);
				}
				break;
			case VARNAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(113);
				match(VARNAME);
				}
				break;
			case PNAME_NS:
			case PNAME_LN:
				enterOuterAlt(_localctx, 3);
				{
				setState(114);
				prefixedName();
				}
				break;
			case STRING_LITERAL1:
				enterOuterAlt(_localctx, 4);
				{
				setState(115);
				match(STRING_LITERAL1);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrefixedNameContext extends ParserRuleContext {
		public TerminalNode PNAME_LN() { return getToken(MOWLParser.PNAME_LN, 0); }
		public TerminalNode PNAME_NS() { return getToken(MOWLParser.PNAME_NS, 0); }
		public PrefixedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterPrefixedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitPrefixedName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitPrefixedName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixedNameContext prefixedName() throws RecognitionException {
		PrefixedNameContext _localctx = new PrefixedNameContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_prefixedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			_la = _input.LA(1);
			if ( !(_la==PNAME_NS || _la==PNAME_LN) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EntityContext extends ParserRuleContext {
		public ClassentityContext classentity() {
			return getRuleContext(ClassentityContext.class,0);
		}
		public ObjectPropertyEntityContext objectPropertyEntity() {
			return getRuleContext(ObjectPropertyEntityContext.class,0);
		}
		public EntityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterEntity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitEntity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitEntity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntityContext entity() throws RecognitionException {
		EntityContext _localctx = new EntityContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_entity);
		try {
			setState(122);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CLASS_LABEL:
				enterOuterAlt(_localctx, 1);
				{
				setState(120);
				classentity();
				}
				break;
			case OBJECT_PROPERTY_LABEL:
				enterOuterAlt(_localctx, 2);
				{
				setState(121);
				objectPropertyEntity();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassentityContext extends ParserRuleContext {
		public TerminalNode CLASS_LABEL() { return getToken(MOWLParser.CLASS_LABEL, 0); }
		public ClassIRIContext classIRI() {
			return getRuleContext(ClassIRIContext.class,0);
		}
		public CorePropertiesContext coreProperties() {
			return getRuleContext(CorePropertiesContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MOWLParser.EOF, 0); }
		public List<ClassExpressionContext> classExpression() {
			return getRuleContexts(ClassExpressionContext.class);
		}
		public ClassExpressionContext classExpression(int i) {
			return getRuleContext(ClassExpressionContext.class,i);
		}
		public ClassentityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classentity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterClassentity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitClassentity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitClassentity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassentityContext classentity() throws RecognitionException {
		ClassentityContext _localctx = new ClassentityContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_classentity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			match(CLASS_LABEL);
			setState(125);
			classIRI();
			setState(126);
			coreProperties();
			setState(128); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(127);
				classExpression();
				}
				}
				setState(130); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 19)) & ~0x3f) == 0 && ((1L << (_la - 19)) & ((1L << (INVERSE_LABEL - 19)) | (1L << (NOT_LABEL - 19)) | (1L << (LEFT_PAREN - 19)) | (1L << (IRI_REF - 19)) | (1L << (PNAME_NS - 19)) | (1L << (PNAME_LN - 19)) | (1L << (STRING_LITERAL1 - 19)) | (1L << (VARNAME - 19)))) != 0) );
			setState(132);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectPropertyEntityContext extends ParserRuleContext {
		public TerminalNode OBJECT_PROPERTY_LABEL() { return getToken(MOWLParser.OBJECT_PROPERTY_LABEL, 0); }
		public ObjectPropertyIRIContext objectPropertyIRI() {
			return getRuleContext(ObjectPropertyIRIContext.class,0);
		}
		public CorePropertiesContext coreProperties() {
			return getRuleContext(CorePropertiesContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MOWLParser.EOF, 0); }
		public List<ObjectPropertyAxiomContext> objectPropertyAxiom() {
			return getRuleContexts(ObjectPropertyAxiomContext.class);
		}
		public ObjectPropertyAxiomContext objectPropertyAxiom(int i) {
			return getRuleContext(ObjectPropertyAxiomContext.class,i);
		}
		public ObjectPropertyEntityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectPropertyEntity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterObjectPropertyEntity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitObjectPropertyEntity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitObjectPropertyEntity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectPropertyEntityContext objectPropertyEntity() throws RecognitionException {
		ObjectPropertyEntityContext _localctx = new ObjectPropertyEntityContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_objectPropertyEntity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			match(OBJECT_PROPERTY_LABEL);
			setState(135);
			objectPropertyIRI();
			setState(136);
			coreProperties();
			setState(138); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(137);
				objectPropertyAxiom();
				}
				}
				setState(140); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << RANGE_LABEL) | (1L << DOMAIN_LABEL) | (1L << SUB_PROPERTY_OF_LABEL) | (1L << SUB_PROPERTY_CHAIN_LABEL) | (1L << INVERSE_LABEL) | (1L << EQUIVALENT_LABEL) | (1L << DISJOINT_LABEL))) != 0) );
			setState(142);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CorePropertiesContext extends ParserRuleContext {
		public TerminalNode NAME_LABEL() { return getToken(MOWLParser.NAME_LABEL, 0); }
		public List<StringContext> string() {
			return getRuleContexts(StringContext.class);
		}
		public StringContext string(int i) {
			return getRuleContext(StringContext.class,i);
		}
		public TerminalNode DESC_LABEL() { return getToken(MOWLParser.DESC_LABEL, 0); }
		public TerminalNode CODE_LABEL() { return getToken(MOWLParser.CODE_LABEL, 0); }
		public TerminalNode SCHEME_LABEL() { return getToken(MOWLParser.SCHEME_LABEL, 0); }
		public CorePropertiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_coreProperties; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterCoreProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitCoreProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitCoreProperties(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CorePropertiesContext coreProperties() throws RecognitionException {
		CorePropertiesContext _localctx = new CorePropertiesContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_coreProperties);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(144);
			match(NAME_LABEL);
			setState(145);
			string();
			}
			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DESC_LABEL) {
				{
				setState(147);
				match(DESC_LABEL);
				setState(148);
				string();
				}
			}

			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CODE_LABEL) {
				{
				setState(151);
				match(CODE_LABEL);
				setState(152);
				string();
				}
			}

			setState(157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SCHEME_LABEL) {
				{
				setState(155);
				match(SCHEME_LABEL);
				setState(156);
				string();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL1() { return getToken(MOWLParser.STRING_LITERAL1, 0); }
		public TerminalNode STRING_LITERAL2() { return getToken(MOWLParser.STRING_LITERAL2, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_string);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(159);
			_la = _input.LA(1);
			if ( !(_la==STRING_LITERAL1 || _la==STRING_LITERAL2) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassAxiomContext extends ParserRuleContext {
		public SubClassContext subClass() {
			return getRuleContext(SubClassContext.class,0);
		}
		public EquivalentContext equivalent() {
			return getRuleContext(EquivalentContext.class,0);
		}
		public DisjointContext disjoint() {
			return getRuleContext(DisjointContext.class,0);
		}
		public ClassAxiomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classAxiom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterClassAxiom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitClassAxiom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitClassAxiom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassAxiomContext classAxiom() throws RecognitionException {
		ClassAxiomContext _localctx = new ClassAxiomContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_classAxiom);
		try {
			setState(164);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBCLASS_LABEL:
				enterOuterAlt(_localctx, 1);
				{
				setState(161);
				subClass();
				}
				break;
			case EQUIVALENT_LABEL:
				enterOuterAlt(_localctx, 2);
				{
				setState(162);
				equivalent();
				}
				break;
			case DISJOINT_LABEL:
				enterOuterAlt(_localctx, 3);
				{
				setState(163);
				disjoint();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubClassContext extends ParserRuleContext {
		public TerminalNode SUBCLASS_LABEL() { return getToken(MOWLParser.SUBCLASS_LABEL, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public SubClassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subClass; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterSubClass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitSubClass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitSubClass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubClassContext subClass() throws RecognitionException {
		SubClassContext _localctx = new SubClassContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_subClass);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			match(SUBCLASS_LABEL);
			setState(167);
			classExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EquivalentContext extends ParserRuleContext {
		public TerminalNode EQUIVALENT_LABEL() { return getToken(MOWLParser.EQUIVALENT_LABEL, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public EquivalentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equivalent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterEquivalent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitEquivalent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitEquivalent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EquivalentContext equivalent() throws RecognitionException {
		EquivalentContext _localctx = new EquivalentContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_equivalent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(EQUIVALENT_LABEL);
			setState(170);
			classExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DisjointContext extends ParserRuleContext {
		public TerminalNode DISJOINT_LABEL() { return getToken(MOWLParser.DISJOINT_LABEL, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public DisjointContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjoint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDisjoint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDisjoint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDisjoint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjointContext disjoint() throws RecognitionException {
		DisjointContext _localctx = new DisjointContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_disjoint);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(DISJOINT_LABEL);
			setState(173);
			classExpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectPropertyAxiomContext extends ParserRuleContext {
		public TerminalNode RANGE_LABEL() { return getToken(MOWLParser.RANGE_LABEL, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public TerminalNode DOMAIN_LABEL() { return getToken(MOWLParser.DOMAIN_LABEL, 0); }
		public TerminalNode SUB_PROPERTY_OF_LABEL() { return getToken(MOWLParser.SUB_PROPERTY_OF_LABEL, 0); }
		public ObjectPropertyExpressionAnnotatedListContext objectPropertyExpressionAnnotatedList() {
			return getRuleContext(ObjectPropertyExpressionAnnotatedListContext.class,0);
		}
		public TerminalNode EQUIVALENT_LABEL() { return getToken(MOWLParser.EQUIVALENT_LABEL, 0); }
		public TerminalNode DISJOINT_LABEL() { return getToken(MOWLParser.DISJOINT_LABEL, 0); }
		public TerminalNode INVERSE_LABEL() { return getToken(MOWLParser.INVERSE_LABEL, 0); }
		public TerminalNode SUB_PROPERTY_CHAIN_LABEL() { return getToken(MOWLParser.SUB_PROPERTY_CHAIN_LABEL, 0); }
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public List<ObjectPropertyExpressionContext> objectPropertyExpression() {
			return getRuleContexts(ObjectPropertyExpressionContext.class);
		}
		public ObjectPropertyExpressionContext objectPropertyExpression(int i) {
			return getRuleContext(ObjectPropertyExpressionContext.class,i);
		}
		public List<TerminalNode> O_LABEL() { return getTokens(MOWLParser.O_LABEL); }
		public TerminalNode O_LABEL(int i) {
			return getToken(MOWLParser.O_LABEL, i);
		}
		public ObjectPropertyAxiomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectPropertyAxiom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterObjectPropertyAxiom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitObjectPropertyAxiom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitObjectPropertyAxiom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectPropertyAxiomContext objectPropertyAxiom() throws RecognitionException {
		ObjectPropertyAxiomContext _localctx = new ObjectPropertyAxiomContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_objectPropertyAxiom);
		int _la;
		try {
			setState(196);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RANGE_LABEL:
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				match(RANGE_LABEL);
				setState(176);
				classExpression();
				}
				break;
			case DOMAIN_LABEL:
				enterOuterAlt(_localctx, 2);
				{
				setState(177);
				match(DOMAIN_LABEL);
				setState(178);
				classExpression();
				}
				break;
			case SUB_PROPERTY_OF_LABEL:
				enterOuterAlt(_localctx, 3);
				{
				setState(179);
				match(SUB_PROPERTY_OF_LABEL);
				setState(180);
				objectPropertyExpressionAnnotatedList();
				}
				break;
			case EQUIVALENT_LABEL:
				enterOuterAlt(_localctx, 4);
				{
				setState(181);
				match(EQUIVALENT_LABEL);
				setState(182);
				objectPropertyExpressionAnnotatedList();
				}
				break;
			case DISJOINT_LABEL:
				enterOuterAlt(_localctx, 5);
				{
				setState(183);
				match(DISJOINT_LABEL);
				setState(184);
				objectPropertyExpressionAnnotatedList();
				}
				break;
			case INVERSE_LABEL:
				enterOuterAlt(_localctx, 6);
				{
				setState(185);
				match(INVERSE_LABEL);
				setState(186);
				objectPropertyExpressionAnnotatedList();
				}
				break;
			case SUB_PROPERTY_CHAIN_LABEL:
				enterOuterAlt(_localctx, 7);
				{
				setState(187);
				match(SUB_PROPERTY_CHAIN_LABEL);
				setState(188);
				annotations();
				setState(189);
				objectPropertyExpression();
				setState(192); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(190);
					match(O_LABEL);
					setState(191);
					objectPropertyExpression();
					}
					}
					setState(194); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==O_LABEL );
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectPropertyExpressionAnnotatedListContext extends ParserRuleContext {
		public ObjectPropertyExpressionContext objectPropertyExpression() {
			return getRuleContext(ObjectPropertyExpressionContext.class,0);
		}
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public List<TerminalNode> AND_LABEL() { return getTokens(MOWLParser.AND_LABEL); }
		public TerminalNode AND_LABEL(int i) {
			return getToken(MOWLParser.AND_LABEL, i);
		}
		public List<ObjectPropertyExpressionAnnotatedListContext> objectPropertyExpressionAnnotatedList() {
			return getRuleContexts(ObjectPropertyExpressionAnnotatedListContext.class);
		}
		public ObjectPropertyExpressionAnnotatedListContext objectPropertyExpressionAnnotatedList(int i) {
			return getRuleContext(ObjectPropertyExpressionAnnotatedListContext.class,i);
		}
		public ObjectPropertyExpressionAnnotatedListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectPropertyExpressionAnnotatedList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterObjectPropertyExpressionAnnotatedList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitObjectPropertyExpressionAnnotatedList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitObjectPropertyExpressionAnnotatedList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectPropertyExpressionAnnotatedListContext objectPropertyExpressionAnnotatedList() throws RecognitionException {
		ObjectPropertyExpressionAnnotatedListContext _localctx = new ObjectPropertyExpressionAnnotatedListContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_objectPropertyExpressionAnnotatedList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ANNOTATIONS_LABEL) {
				{
				setState(198);
				annotations();
				}
			}

			setState(201);
			objectPropertyExpression();
			setState(206);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(202);
					match(AND_LABEL);
					setState(203);
					objectPropertyExpressionAnnotatedList();
					}
					} 
				}
				setState(208);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassExpressionContext extends ParserRuleContext {
		public List<IntersectionContext> intersection() {
			return getRuleContexts(IntersectionContext.class);
		}
		public IntersectionContext intersection(int i) {
			return getRuleContext(IntersectionContext.class,i);
		}
		public List<TerminalNode> OR_LABEL() { return getTokens(MOWLParser.OR_LABEL); }
		public TerminalNode OR_LABEL(int i) {
			return getToken(MOWLParser.OR_LABEL, i);
		}
		public ClassRoleContext classRole() {
			return getRuleContext(ClassRoleContext.class,0);
		}
		public ClassExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterClassExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitClassExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitClassExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassExpressionContext classExpression() throws RecognitionException {
		ClassExpressionContext _localctx = new ClassExpressionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_classExpression);
		int _la;
		try {
			setState(218);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(209);
				intersection();
				setState(214);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==OR_LABEL) {
					{
					{
					setState(210);
					match(OR_LABEL);
					setState(211);
					intersection();
					}
					}
					setState(216);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(217);
				classRole();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntersectionContext extends ParserRuleContext {
		public List<ClassRoleContext> classRole() {
			return getRuleContexts(ClassRoleContext.class);
		}
		public ClassRoleContext classRole(int i) {
			return getRuleContext(ClassRoleContext.class,i);
		}
		public List<AndContext> and() {
			return getRuleContexts(AndContext.class);
		}
		public AndContext and(int i) {
			return getRuleContext(AndContext.class,i);
		}
		public IntersectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intersection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterIntersection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitIntersection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitIntersection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntersectionContext intersection() throws RecognitionException {
		IntersectionContext _localctx = new IntersectionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_intersection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			classRole();
			setState(224); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(221);
				and();
				setState(222);
				classRole();
				}
				}
				setState(226); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMMA || _la==AND_LABEL );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassRoleContext extends ParserRuleContext {
		public RestrictionContext restriction() {
			return getRuleContext(RestrictionContext.class,0);
		}
		public AtomicContext atomic() {
			return getRuleContext(AtomicContext.class,0);
		}
		public TerminalNode NOT_LABEL() { return getToken(MOWLParser.NOT_LABEL, 0); }
		public ClassRoleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classRole; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterClassRole(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitClassRole(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitClassRole(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassRoleContext classRole() throws RecognitionException {
		ClassRoleContext _localctx = new ClassRoleContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_classRole);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT_LABEL) {
				{
				setState(228);
				match(NOT_LABEL);
				}
			}

			setState(233);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(231);
				restriction();
				}
				break;
			case 2:
				{
				setState(232);
				atomic();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtomicContext extends ParserRuleContext {
		public ClassIRIContext classIRI() {
			return getRuleContext(ClassIRIContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(MOWLParser.LEFT_PAREN, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(MOWLParser.RIGHT_PAREN, 0); }
		public AtomicContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomic; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterAtomic(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitAtomic(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitAtomic(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomicContext atomic() throws RecognitionException {
		AtomicContext _localctx = new AtomicContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_atomic);
		try {
			setState(240);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
			case STRING_LITERAL1:
			case VARNAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(235);
				classIRI();
				}
				break;
			case LEFT_PAREN:
				enterOuterAlt(_localctx, 2);
				{
				setState(236);
				match(LEFT_PAREN);
				setState(237);
				classExpression();
				setState(238);
				match(RIGHT_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndividualContext extends ParserRuleContext {
		public IndividualIRIContext individualIRI() {
			return getRuleContext(IndividualIRIContext.class,0);
		}
		public TerminalNode NODE_ID() { return getToken(MOWLParser.NODE_ID, 0); }
		public IndividualContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_individual; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterIndividual(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitIndividual(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitIndividual(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndividualContext individual() throws RecognitionException {
		IndividualContext _localctx = new IndividualContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_individual);
		try {
			setState(244);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
			case STRING_LITERAL1:
			case VARNAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(242);
				individualIRI();
				}
				break;
			case NODE_ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(243);
				match(NODE_ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RestrictionContext extends ParserRuleContext {
		public ObjectPropertyExpressionContext objectPropertyExpression() {
			return getRuleContext(ObjectPropertyExpressionContext.class,0);
		}
		public TerminalNode SOME_LABEL() { return getToken(MOWLParser.SOME_LABEL, 0); }
		public ClassRoleContext classRole() {
			return getRuleContext(ClassRoleContext.class,0);
		}
		public TerminalNode ONLY_LABEL() { return getToken(MOWLParser.ONLY_LABEL, 0); }
		public TerminalNode VALUE_LABEL() { return getToken(MOWLParser.VALUE_LABEL, 0); }
		public IndividualContext individual() {
			return getRuleContext(IndividualContext.class,0);
		}
		public TerminalNode SELF_LABEL() { return getToken(MOWLParser.SELF_LABEL, 0); }
		public TerminalNode MIN_LABEL() { return getToken(MOWLParser.MIN_LABEL, 0); }
		public NonNegativeIntegerContext nonNegativeInteger() {
			return getRuleContext(NonNegativeIntegerContext.class,0);
		}
		public TerminalNode MAX_LABEL() { return getToken(MOWLParser.MAX_LABEL, 0); }
		public TerminalNode EXACTLY_LABEL() { return getToken(MOWLParser.EXACTLY_LABEL, 0); }
		public DataPropertyExpressionContext dataPropertyExpression() {
			return getRuleContext(DataPropertyExpressionContext.class,0);
		}
		public DataclassRoleContext dataclassRole() {
			return getRuleContext(DataclassRoleContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public RestrictionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_restriction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterRestriction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitRestriction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitRestriction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RestrictionContext restriction() throws RecognitionException {
		RestrictionContext _localctx = new RestrictionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_restriction);
		try {
			setState(309);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(246);
				objectPropertyExpression();
				setState(247);
				match(SOME_LABEL);
				setState(248);
				classRole();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(250);
				objectPropertyExpression();
				setState(251);
				match(ONLY_LABEL);
				setState(252);
				classRole();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(254);
				objectPropertyExpression();
				setState(255);
				match(VALUE_LABEL);
				setState(256);
				individual();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(258);
				objectPropertyExpression();
				setState(259);
				match(SELF_LABEL);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(261);
				objectPropertyExpression();
				setState(262);
				match(MIN_LABEL);
				setState(263);
				nonNegativeInteger();
				setState(265);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
				case 1:
					{
					setState(264);
					classRole();
					}
					break;
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(267);
				objectPropertyExpression();
				setState(268);
				match(MAX_LABEL);
				setState(269);
				nonNegativeInteger();
				setState(271);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
				case 1:
					{
					setState(270);
					classRole();
					}
					break;
				}
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(273);
				objectPropertyExpression();
				setState(274);
				match(EXACTLY_LABEL);
				setState(275);
				nonNegativeInteger();
				setState(277);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
				case 1:
					{
					setState(276);
					classRole();
					}
					break;
				}
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(279);
				dataPropertyExpression();
				setState(280);
				match(SOME_LABEL);
				setState(281);
				dataclassRole();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(283);
				dataPropertyExpression();
				setState(284);
				match(ONLY_LABEL);
				setState(285);
				dataclassRole();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(287);
				dataPropertyExpression();
				setState(288);
				match(VALUE_LABEL);
				setState(289);
				literal();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(291);
				dataPropertyExpression();
				setState(292);
				match(MIN_LABEL);
				setState(293);
				nonNegativeInteger();
				setState(295);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
				case 1:
					{
					setState(294);
					dataclassRole();
					}
					break;
				}
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(297);
				dataPropertyExpression();
				setState(298);
				match(MAX_LABEL);
				setState(299);
				nonNegativeInteger();
				setState(301);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
				case 1:
					{
					setState(300);
					dataclassRole();
					}
					break;
				}
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(303);
				dataPropertyExpression();
				setState(304);
				match(EXACTLY_LABEL);
				setState(305);
				nonNegativeInteger();
				setState(307);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
				case 1:
					{
					setState(306);
					dataclassRole();
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonNegativeIntegerContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(MOWLParser.INTEGER, 0); }
		public NonNegativeIntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonNegativeInteger; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterNonNegativeInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitNonNegativeInteger(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitNonNegativeInteger(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonNegativeIntegerContext nonNegativeInteger() throws RecognitionException {
		NonNegativeIntegerContext _localctx = new NonNegativeIntegerContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_nonNegativeInteger);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataclassRoleContext extends ParserRuleContext {
		public DataAtomicContext dataAtomic() {
			return getRuleContext(DataAtomicContext.class,0);
		}
		public TerminalNode NOT_LABEL() { return getToken(MOWLParser.NOT_LABEL, 0); }
		public DataclassRoleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataclassRole; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDataclassRole(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDataclassRole(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDataclassRole(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataclassRoleContext dataclassRole() throws RecognitionException {
		DataclassRoleContext _localctx = new DataclassRoleContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_dataclassRole);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT_LABEL) {
				{
				setState(313);
				match(NOT_LABEL);
				}
			}

			setState(316);
			dataAtomic();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectPropertyListContext extends ParserRuleContext {
		public List<ObjectPropertyContext> objectProperty() {
			return getRuleContexts(ObjectPropertyContext.class);
		}
		public ObjectPropertyContext objectProperty(int i) {
			return getRuleContext(ObjectPropertyContext.class,i);
		}
		public List<TerminalNode> AND_LABEL() { return getTokens(MOWLParser.AND_LABEL); }
		public TerminalNode AND_LABEL(int i) {
			return getToken(MOWLParser.AND_LABEL, i);
		}
		public ObjectPropertyListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectPropertyList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterObjectPropertyList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitObjectPropertyList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitObjectPropertyList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectPropertyListContext objectPropertyList() throws RecognitionException {
		ObjectPropertyListContext _localctx = new ObjectPropertyListContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_objectPropertyList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			objectProperty();
			setState(323);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND_LABEL) {
				{
				{
				setState(319);
				match(AND_LABEL);
				setState(320);
				objectProperty();
				}
				}
				setState(325);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectPropertyContext extends ParserRuleContext {
		public ObjectPropertyIRIContext objectPropertyIRI() {
			return getRuleContext(ObjectPropertyIRIContext.class,0);
		}
		public ObjectPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterObjectProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitObjectProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitObjectProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectPropertyContext objectProperty() throws RecognitionException {
		ObjectPropertyContext _localctx = new ObjectPropertyContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_objectProperty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(326);
			objectPropertyIRI();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataPropertyContext extends ParserRuleContext {
		public DataPropertyIRIContext dataPropertyIRI() {
			return getRuleContext(DataPropertyIRIContext.class,0);
		}
		public DataPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDataProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDataProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDataProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataPropertyContext dataProperty() throws RecognitionException {
		DataPropertyContext _localctx = new DataPropertyContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_dataProperty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			dataPropertyIRI();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataPropertyIRIContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public DataPropertyIRIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataPropertyIRI; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDataPropertyIRI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDataPropertyIRI(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDataPropertyIRI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataPropertyIRIContext dataPropertyIRI() throws RecognitionException {
		DataPropertyIRIContext _localctx = new DataPropertyIRIContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_dataPropertyIRI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataAtomicContext extends ParserRuleContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public TerminalNode OPEN_CURLY_BRACE() { return getToken(MOWLParser.OPEN_CURLY_BRACE, 0); }
		public LiteralListContext literalList() {
			return getRuleContext(LiteralListContext.class,0);
		}
		public TerminalNode CLOSE_CURLY_BRACE() { return getToken(MOWLParser.CLOSE_CURLY_BRACE, 0); }
		public DataTypeRestrictionContext dataTypeRestriction() {
			return getRuleContext(DataTypeRestrictionContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(MOWLParser.LEFT_PAREN, 0); }
		public DataRangeContext dataRange() {
			return getRuleContext(DataRangeContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(MOWLParser.RIGHT_PAREN, 0); }
		public DataAtomicContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataAtomic; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDataAtomic(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDataAtomic(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDataAtomic(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataAtomicContext dataAtomic() throws RecognitionException {
		DataAtomicContext _localctx = new DataAtomicContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_dataAtomic);
		try {
			setState(342);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(332);
				dataType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(333);
				match(OPEN_CURLY_BRACE);
				setState(334);
				literalList();
				setState(335);
				match(CLOSE_CURLY_BRACE);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(337);
				dataTypeRestriction();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(338);
				match(LEFT_PAREN);
				setState(339);
				dataRange();
				setState(340);
				match(RIGHT_PAREN);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataTypeContext extends ParserRuleContext {
		public DatatypeIRIContext datatypeIRI() {
			return getRuleContext(DatatypeIRIContext.class,0);
		}
		public TerminalNode INTEGER_LABEL() { return getToken(MOWLParser.INTEGER_LABEL, 0); }
		public TerminalNode DECIMAL_LABEL() { return getToken(MOWLParser.DECIMAL_LABEL, 0); }
		public TerminalNode FLOAT_LABEL() { return getToken(MOWLParser.FLOAT_LABEL, 0); }
		public TerminalNode STRING_LABEL() { return getToken(MOWLParser.STRING_LABEL, 0); }
		public DataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDataType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDataType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataTypeContext dataType() throws RecognitionException {
		DataTypeContext _localctx = new DataTypeContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_dataType);
		try {
			setState(349);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
			case STRING_LITERAL1:
			case VARNAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(344);
				datatypeIRI();
				}
				break;
			case INTEGER_LABEL:
				enterOuterAlt(_localctx, 2);
				{
				setState(345);
				match(INTEGER_LABEL);
				}
				break;
			case DECIMAL_LABEL:
				enterOuterAlt(_localctx, 3);
				{
				setState(346);
				match(DECIMAL_LABEL);
				}
				break;
			case FLOAT_LABEL:
				enterOuterAlt(_localctx, 4);
				{
				setState(347);
				match(FLOAT_LABEL);
				}
				break;
			case STRING_LABEL:
				enterOuterAlt(_localctx, 5);
				{
				setState(348);
				match(STRING_LABEL);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TypedLiteralContext typedLiteral() {
			return getRuleContext(TypedLiteralContext.class,0);
		}
		public StringLiteralNoLanguageContext stringLiteralNoLanguage() {
			return getRuleContext(StringLiteralNoLanguageContext.class,0);
		}
		public StringLiteralWithLanguageContext stringLiteralWithLanguage() {
			return getRuleContext(StringLiteralWithLanguageContext.class,0);
		}
		public IntegerLiteralContext integerLiteral() {
			return getRuleContext(IntegerLiteralContext.class,0);
		}
		public DecimalLiteralContext decimalLiteral() {
			return getRuleContext(DecimalLiteralContext.class,0);
		}
		public FloatingPointLiteralContext floatingPointLiteral() {
			return getRuleContext(FloatingPointLiteralContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_literal);
		try {
			setState(357);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(351);
				typedLiteral();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(352);
				stringLiteralNoLanguage();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(353);
				stringLiteralWithLanguage();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(354);
				integerLiteral();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(355);
				decimalLiteral();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(356);
				floatingPointLiteral();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypedLiteralContext extends ParserRuleContext {
		public LexicalValueContext lexicalValue() {
			return getRuleContext(LexicalValueContext.class,0);
		}
		public TerminalNode REFERENCE() { return getToken(MOWLParser.REFERENCE, 0); }
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public TypedLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterTypedLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitTypedLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitTypedLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedLiteralContext typedLiteral() throws RecognitionException {
		TypedLiteralContext _localctx = new TypedLiteralContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_typedLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(359);
			lexicalValue();
			setState(360);
			match(REFERENCE);
			setState(361);
			dataType();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringLiteralNoLanguageContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL2() { return getToken(MOWLParser.STRING_LITERAL2, 0); }
		public StringLiteralNoLanguageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteralNoLanguage; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterStringLiteralNoLanguage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitStringLiteralNoLanguage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitStringLiteralNoLanguage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralNoLanguageContext stringLiteralNoLanguage() throws RecognitionException {
		StringLiteralNoLanguageContext _localctx = new StringLiteralNoLanguageContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_stringLiteralNoLanguage);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			match(STRING_LITERAL2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringLiteralWithLanguageContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL2() { return getToken(MOWLParser.STRING_LITERAL2, 0); }
		public TerminalNode LANGUAGE_TAG() { return getToken(MOWLParser.LANGUAGE_TAG, 0); }
		public StringLiteralWithLanguageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringLiteralWithLanguage; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterStringLiteralWithLanguage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitStringLiteralWithLanguage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitStringLiteralWithLanguage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringLiteralWithLanguageContext stringLiteralWithLanguage() throws RecognitionException {
		StringLiteralWithLanguageContext _localctx = new StringLiteralWithLanguageContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_stringLiteralWithLanguage);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(365);
			match(STRING_LITERAL2);
			setState(366);
			match(LANGUAGE_TAG);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LexicalValueContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL2() { return getToken(MOWLParser.STRING_LITERAL2, 0); }
		public LexicalValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lexicalValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterLexicalValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitLexicalValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitLexicalValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LexicalValueContext lexicalValue() throws RecognitionException {
		LexicalValueContext _localctx = new LexicalValueContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_lexicalValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(368);
			match(STRING_LITERAL2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataPropertyExpressionContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public DataPropertyExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataPropertyExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDataPropertyExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDataPropertyExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDataPropertyExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataPropertyExpressionContext dataPropertyExpression() throws RecognitionException {
		DataPropertyExpressionContext _localctx = new DataPropertyExpressionContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_dataPropertyExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataTypeRestrictionContext extends ParserRuleContext {
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public TerminalNode OPEN_SQUARE_BRACE() { return getToken(MOWLParser.OPEN_SQUARE_BRACE, 0); }
		public List<FacetContext> facet() {
			return getRuleContexts(FacetContext.class);
		}
		public FacetContext facet(int i) {
			return getRuleContext(FacetContext.class,i);
		}
		public List<RestrictionValueContext> restrictionValue() {
			return getRuleContexts(RestrictionValueContext.class);
		}
		public RestrictionValueContext restrictionValue(int i) {
			return getRuleContext(RestrictionValueContext.class,i);
		}
		public TerminalNode CLOSE_SQUARE_BRACE() { return getToken(MOWLParser.CLOSE_SQUARE_BRACE, 0); }
		public List<TerminalNode> AND_LABEL() { return getTokens(MOWLParser.AND_LABEL); }
		public TerminalNode AND_LABEL(int i) {
			return getToken(MOWLParser.AND_LABEL, i);
		}
		public DataTypeRestrictionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataTypeRestriction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDataTypeRestriction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDataTypeRestriction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDataTypeRestriction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataTypeRestrictionContext dataTypeRestriction() throws RecognitionException {
		DataTypeRestrictionContext _localctx = new DataTypeRestrictionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_dataTypeRestriction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			dataType();
			setState(373);
			match(OPEN_SQUARE_BRACE);
			setState(374);
			facet();
			setState(375);
			restrictionValue();
			setState(382);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND_LABEL) {
				{
				{
				setState(376);
				match(AND_LABEL);
				setState(377);
				facet();
				setState(378);
				restrictionValue();
				}
				}
				setState(384);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(385);
			match(CLOSE_SQUARE_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FacetContext extends ParserRuleContext {
		public TerminalNode LENGTH_LABEL() { return getToken(MOWLParser.LENGTH_LABEL, 0); }
		public TerminalNode MIN_LENGTH_LABEL() { return getToken(MOWLParser.MIN_LENGTH_LABEL, 0); }
		public TerminalNode MAX_LENGTH_LABEL() { return getToken(MOWLParser.MAX_LENGTH_LABEL, 0); }
		public TerminalNode PATTERN_LABEL() { return getToken(MOWLParser.PATTERN_LABEL, 0); }
		public TerminalNode LANG_PATTERN_LABEL() { return getToken(MOWLParser.LANG_PATTERN_LABEL, 0); }
		public TerminalNode LESS_EQUAL() { return getToken(MOWLParser.LESS_EQUAL, 0); }
		public TerminalNode LESS() { return getToken(MOWLParser.LESS, 0); }
		public TerminalNode GREATER_EQUAL() { return getToken(MOWLParser.GREATER_EQUAL, 0); }
		public TerminalNode GREATER() { return getToken(MOWLParser.GREATER, 0); }
		public FacetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_facet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterFacet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitFacet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitFacet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FacetContext facet() throws RecognitionException {
		FacetContext _localctx = new FacetContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_facet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LENGTH_LABEL) | (1L << MIN_LENGTH_LABEL) | (1L << MAX_LENGTH_LABEL) | (1L << PATTERN_LABEL) | (1L << LANG_PATTERN_LABEL) | (1L << LESS_EQUAL) | (1L << GREATER_EQUAL) | (1L << LESS) | (1L << GREATER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RestrictionValueContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public RestrictionValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_restrictionValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterRestrictionValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitRestrictionValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitRestrictionValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RestrictionValueContext restrictionValue() throws RecognitionException {
		RestrictionValueContext _localctx = new RestrictionValueContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_restrictionValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InverseObjectPropertyContext extends ParserRuleContext {
		public TerminalNode INVERSE_LABEL() { return getToken(MOWLParser.INVERSE_LABEL, 0); }
		public ObjectPropertyIRIContext objectPropertyIRI() {
			return getRuleContext(ObjectPropertyIRIContext.class,0);
		}
		public InverseObjectPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inverseObjectProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterInverseObjectProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitInverseObjectProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitInverseObjectProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InverseObjectPropertyContext inverseObjectProperty() throws RecognitionException {
		InverseObjectPropertyContext _localctx = new InverseObjectPropertyContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_inverseObjectProperty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(391);
			match(INVERSE_LABEL);
			setState(392);
			objectPropertyIRI();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DecimalLiteralContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER() { return getTokens(MOWLParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(MOWLParser.INTEGER, i);
		}
		public TerminalNode DOT() { return getToken(MOWLParser.DOT, 0); }
		public TerminalNode PLUS() { return getToken(MOWLParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(MOWLParser.MINUS, 0); }
		public DecimalLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decimalLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDecimalLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDecimalLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDecimalLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecimalLiteralContext decimalLiteral() throws RecognitionException {
		DecimalLiteralContext _localctx = new DecimalLiteralContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_decimalLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS || _la==PLUS) {
				{
				setState(394);
				_la = _input.LA(1);
				if ( !(_la==MINUS || _la==PLUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(397);
			match(INTEGER);
			setState(398);
			match(DOT);
			setState(399);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerLiteralContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(MOWLParser.INTEGER, 0); }
		public TerminalNode PLUS() { return getToken(MOWLParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(MOWLParser.MINUS, 0); }
		public IntegerLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integerLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterIntegerLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitIntegerLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitIntegerLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegerLiteralContext integerLiteral() throws RecognitionException {
		IntegerLiteralContext _localctx = new IntegerLiteralContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_integerLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(402);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS || _la==PLUS) {
				{
				setState(401);
				_la = _input.LA(1);
				if ( !(_la==MINUS || _la==PLUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(404);
			match(INTEGER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FloatingPointLiteralContext extends ParserRuleContext {
		public TerminalNode DOT() { return getToken(MOWLParser.DOT, 0); }
		public List<TerminalNode> INTEGER() { return getTokens(MOWLParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(MOWLParser.INTEGER, i);
		}
		public TerminalNode PLUS() { return getToken(MOWLParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(MOWLParser.MINUS, 0); }
		public TerminalNode EXPONENT() { return getToken(MOWLParser.EXPONENT, 0); }
		public FloatingPointLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatingPointLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterFloatingPointLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitFloatingPointLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitFloatingPointLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FloatingPointLiteralContext floatingPointLiteral() throws RecognitionException {
		FloatingPointLiteralContext _localctx = new FloatingPointLiteralContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_floatingPointLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==MINUS || _la==PLUS) {
				{
				setState(406);
				_la = _input.LA(1);
				if ( !(_la==MINUS || _la==PLUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(422);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER:
				{
				{
				setState(409);
				match(INTEGER);
				setState(412);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOT) {
					{
					setState(410);
					match(DOT);
					setState(411);
					match(INTEGER);
					}
				}

				setState(415);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EXPONENT) {
					{
					setState(414);
					match(EXPONENT);
					}
				}

				}
				}
				break;
			case DOT:
				{
				setState(417);
				match(DOT);
				setState(418);
				match(INTEGER);
				setState(420);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EXPONENT) {
					{
					setState(419);
					match(EXPONENT);
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataRangeContext extends ParserRuleContext {
		public List<DataConjunctionContext> dataConjunction() {
			return getRuleContexts(DataConjunctionContext.class);
		}
		public DataConjunctionContext dataConjunction(int i) {
			return getRuleContext(DataConjunctionContext.class,i);
		}
		public List<TerminalNode> OR_LABEL() { return getTokens(MOWLParser.OR_LABEL); }
		public TerminalNode OR_LABEL(int i) {
			return getToken(MOWLParser.OR_LABEL, i);
		}
		public DataRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDataRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDataRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDataRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataRangeContext dataRange() throws RecognitionException {
		DataRangeContext _localctx = new DataRangeContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_dataRange);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			dataConjunction();
			setState(429);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OR_LABEL) {
				{
				{
				setState(425);
				match(OR_LABEL);
				setState(426);
				dataConjunction();
				}
				}
				setState(431);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DataConjunctionContext extends ParserRuleContext {
		public List<DataclassRoleContext> dataclassRole() {
			return getRuleContexts(DataclassRoleContext.class);
		}
		public DataclassRoleContext dataclassRole(int i) {
			return getRuleContext(DataclassRoleContext.class,i);
		}
		public List<TerminalNode> AND_LABEL() { return getTokens(MOWLParser.AND_LABEL); }
		public TerminalNode AND_LABEL(int i) {
			return getToken(MOWLParser.AND_LABEL, i);
		}
		public DataConjunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataConjunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDataConjunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDataConjunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDataConjunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataConjunctionContext dataConjunction() throws RecognitionException {
		DataConjunctionContext _localctx = new DataConjunctionContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_dataConjunction);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(432);
			dataclassRole();
			setState(437);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND_LABEL) {
				{
				{
				setState(433);
				match(AND_LABEL);
				setState(434);
				dataclassRole();
				}
				}
				setState(439);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationAnnotatedListContext extends ParserRuleContext {
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<AnnotationsContext> annotations() {
			return getRuleContexts(AnnotationsContext.class);
		}
		public AnnotationsContext annotations(int i) {
			return getRuleContext(AnnotationsContext.class,i);
		}
		public List<TerminalNode> AND_LABEL() { return getTokens(MOWLParser.AND_LABEL); }
		public TerminalNode AND_LABEL(int i) {
			return getToken(MOWLParser.AND_LABEL, i);
		}
		public AnnotationAnnotatedListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationAnnotatedList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterAnnotationAnnotatedList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitAnnotationAnnotatedList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitAnnotationAnnotatedList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationAnnotatedListContext annotationAnnotatedList() throws RecognitionException {
		AnnotationAnnotatedListContext _localctx = new AnnotationAnnotatedListContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_annotationAnnotatedList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(441);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ANNOTATIONS_LABEL) {
				{
				setState(440);
				annotations();
				}
			}

			setState(443);
			annotation();
			setState(451);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND_LABEL) {
				{
				{
				setState(444);
				match(AND_LABEL);
				setState(446);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ANNOTATIONS_LABEL) {
					{
					setState(445);
					annotations();
					}
				}

				setState(448);
				annotation();
				}
				}
				setState(453);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationContext extends ParserRuleContext {
		public AnnotationPropertyIRIContext annotationPropertyIRI() {
			return getRuleContext(AnnotationPropertyIRIContext.class,0);
		}
		public AnnotationTargetContext annotationTarget() {
			return getRuleContext(AnnotationTargetContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_annotation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(454);
			annotationPropertyIRI();
			setState(455);
			annotationTarget();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationPropertyIRIContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public AnnotationPropertyIRIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationPropertyIRI; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterAnnotationPropertyIRI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitAnnotationPropertyIRI(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitAnnotationPropertyIRI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationPropertyIRIContext annotationPropertyIRI() throws RecognitionException {
		AnnotationPropertyIRIContext _localctx = new AnnotationPropertyIRIContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_annotationPropertyIRI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(457);
			iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationTargetContext extends ParserRuleContext {
		public TerminalNode NODE_ID() { return getToken(MOWLParser.NODE_ID, 0); }
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public AnnotationTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationTarget; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterAnnotationTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitAnnotationTarget(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitAnnotationTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationTargetContext annotationTarget() throws RecognitionException {
		AnnotationTargetContext _localctx = new AnnotationTargetContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_annotationTarget);
		try {
			setState(462);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NODE_ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(459);
				match(NODE_ID);
				}
				break;
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
			case STRING_LITERAL1:
			case VARNAME:
				enterOuterAlt(_localctx, 2);
				{
				setState(460);
				iriRef();
				}
				break;
			case MINUS:
			case DOT:
			case PLUS:
			case INTEGER:
			case STRING_LITERAL2:
				enterOuterAlt(_localctx, 3);
				{
				setState(461);
				literal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AnnotationsContext extends ParserRuleContext {
		public TerminalNode ANNOTATIONS_LABEL() { return getToken(MOWLParser.ANNOTATIONS_LABEL, 0); }
		public AnnotationAnnotatedListContext annotationAnnotatedList() {
			return getRuleContext(AnnotationAnnotatedListContext.class,0);
		}
		public AnnotationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterAnnotations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitAnnotations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitAnnotations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationsContext annotations() throws RecognitionException {
		AnnotationsContext _localctx = new AnnotationsContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_annotations);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(464);
			match(ANNOTATIONS_LABEL);
			setState(465);
			annotationAnnotatedList();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralListContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MOWLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MOWLParser.COMMA, i);
		}
		public LiteralListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterLiteralList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitLiteralList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitLiteralList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralListContext literalList() throws RecognitionException {
		LiteralListContext _localctx = new LiteralListContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_literalList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			literal();
			setState(472);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(468);
				match(COMMA);
				setState(469);
				literal();
				}
				}
				setState(474);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectPropertyExpressionContext extends ParserRuleContext {
		public ObjectPropertyIRIContext objectPropertyIRI() {
			return getRuleContext(ObjectPropertyIRIContext.class,0);
		}
		public InverseObjectPropertyContext inverseObjectProperty() {
			return getRuleContext(InverseObjectPropertyContext.class,0);
		}
		public ObjectPropertyExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectPropertyExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterObjectPropertyExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitObjectPropertyExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitObjectPropertyExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectPropertyExpressionContext objectPropertyExpression() throws RecognitionException {
		ObjectPropertyExpressionContext _localctx = new ObjectPropertyExpressionContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_objectPropertyExpression);
		try {
			setState(477);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
			case STRING_LITERAL1:
			case VARNAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(475);
				objectPropertyIRI();
				}
				break;
			case INVERSE_LABEL:
				enterOuterAlt(_localctx, 2);
				{
				setState(476);
				inverseObjectProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtaPropertyIRIContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public AtaPropertyIRIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ataPropertyIRI; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterAtaPropertyIRI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitAtaPropertyIRI(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitAtaPropertyIRI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtaPropertyIRIContext ataPropertyIRI() throws RecognitionException {
		AtaPropertyIRIContext _localctx = new AtaPropertyIRIContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_ataPropertyIRI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(479);
			iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DatatypeIRIContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public DatatypeIRIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datatypeIRI; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDatatypeIRI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDatatypeIRI(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDatatypeIRI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatatypeIRIContext datatypeIRI() throws RecognitionException {
		DatatypeIRIContext _localctx = new DatatypeIRIContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_datatypeIRI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(481);
			iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectPropertyIRIContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public ObjectPropertyIRIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectPropertyIRI; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterObjectPropertyIRI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitObjectPropertyIRI(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitObjectPropertyIRI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectPropertyIRIContext objectPropertyIRI() throws RecognitionException {
		ObjectPropertyIRIContext _localctx = new ObjectPropertyIRIContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_objectPropertyIRI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(483);
			iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndividualIRIContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public IndividualIRIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_individualIRI; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterIndividualIRI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitIndividualIRI(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitIndividualIRI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndividualIRIContext individualIRI() throws RecognitionException {
		IndividualIRIContext _localctx = new IndividualIRIContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_individualIRI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(485);
			iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DatatypePropertyIRIContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public DatatypePropertyIRIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datatypePropertyIRI; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterDatatypePropertyIRI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitDatatypePropertyIRI(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitDatatypePropertyIRI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatatypePropertyIRIContext datatypePropertyIRI() throws RecognitionException {
		DatatypePropertyIRIContext _localctx = new DatatypePropertyIRIContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_datatypePropertyIRI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(487);
			iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassIRIContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public ClassIRIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classIRI; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterClassIRI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitClassIRI(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitClassIRI(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassIRIContext classIRI() throws RecognitionException {
		ClassIRIContext _localctx = new ClassIRIContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_classIRI);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(489);
			iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndContext extends ParserRuleContext {
		public TerminalNode AND_LABEL() { return getToken(MOWLParser.AND_LABEL, 0); }
		public TerminalNode COMMA() { return getToken(MOWLParser.COMMA, 0); }
		public AndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).enterAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MOWLListener ) ((MOWLListener)listener).exitAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MOWLVisitor ) return ((MOWLVisitor<? extends T>)visitor).visitAnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndContext and() throws RecognitionException {
		AndContext _localctx = new AndContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_and);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(491);
			_la = _input.LA(1);
			if ( !(_la==COMMA || _la==AND_LABEL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3W\u01f0\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\3\2\3\2\3\2\3\2\5\2w\n\2"+
		"\3\3\3\3\3\4\3\4\5\4}\n\4\3\5\3\5\3\5\3\5\6\5\u0083\n\5\r\5\16\5\u0084"+
		"\3\5\3\5\3\6\3\6\3\6\3\6\6\6\u008d\n\6\r\6\16\6\u008e\3\6\3\6\3\7\3\7"+
		"\3\7\3\7\3\7\5\7\u0098\n\7\3\7\3\7\5\7\u009c\n\7\3\7\3\7\5\7\u00a0\n\7"+
		"\3\b\3\b\3\t\3\t\3\t\5\t\u00a7\n\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f"+
		"\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\6\r\u00c3\n\r\r\r\16\r\u00c4\5\r\u00c7\n\r\3\16\5\16\u00ca\n\16\3\16"+
		"\3\16\3\16\7\16\u00cf\n\16\f\16\16\16\u00d2\13\16\3\17\3\17\3\17\7\17"+
		"\u00d7\n\17\f\17\16\17\u00da\13\17\3\17\5\17\u00dd\n\17\3\20\3\20\3\20"+
		"\3\20\6\20\u00e3\n\20\r\20\16\20\u00e4\3\21\5\21\u00e8\n\21\3\21\3\21"+
		"\5\21\u00ec\n\21\3\22\3\22\3\22\3\22\3\22\5\22\u00f3\n\22\3\23\3\23\5"+
		"\23\u00f7\n\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u010c\n\24\3\24\3\24\3\24"+
		"\3\24\5\24\u0112\n\24\3\24\3\24\3\24\3\24\5\24\u0118\n\24\3\24\3\24\3"+
		"\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5"+
		"\24\u012a\n\24\3\24\3\24\3\24\3\24\5\24\u0130\n\24\3\24\3\24\3\24\3\24"+
		"\5\24\u0136\n\24\5\24\u0138\n\24\3\25\3\25\3\26\5\26\u013d\n\26\3\26\3"+
		"\26\3\27\3\27\3\27\7\27\u0144\n\27\f\27\16\27\u0147\13\27\3\30\3\30\3"+
		"\31\3\31\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\5"+
		"\33\u0159\n\33\3\34\3\34\3\34\3\34\3\34\5\34\u0160\n\34\3\35\3\35\3\35"+
		"\3\35\3\35\3\35\5\35\u0168\n\35\3\36\3\36\3\36\3\36\3\37\3\37\3 \3 \3"+
		" \3!\3!\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\7#\u017f\n#\f#\16#\u0182\13#\3"+
		"#\3#\3$\3$\3%\3%\3&\3&\3&\3\'\5\'\u018e\n\'\3\'\3\'\3\'\3\'\3(\5(\u0195"+
		"\n(\3(\3(\3)\5)\u019a\n)\3)\3)\3)\5)\u019f\n)\3)\5)\u01a2\n)\3)\3)\3)"+
		"\5)\u01a7\n)\5)\u01a9\n)\3*\3*\3*\7*\u01ae\n*\f*\16*\u01b1\13*\3+\3+\3"+
		"+\7+\u01b6\n+\f+\16+\u01b9\13+\3,\5,\u01bc\n,\3,\3,\3,\5,\u01c1\n,\3,"+
		"\7,\u01c4\n,\f,\16,\u01c7\13,\3-\3-\3-\3.\3.\3/\3/\3/\5/\u01d1\n/\3\60"+
		"\3\60\3\60\3\61\3\61\3\61\7\61\u01d9\n\61\f\61\16\61\u01dc\13\61\3\62"+
		"\3\62\5\62\u01e0\n\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67"+
		"\38\38\39\39\39\2\2:\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60"+
		"\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnp\2\7\3\2;<\3\2KL\4\2\16\22\35 "+
		"\4\2\26\26\30\30\4\2\3\3,,\2\u0203\2v\3\2\2\2\4x\3\2\2\2\6|\3\2\2\2\b"+
		"~\3\2\2\2\n\u0088\3\2\2\2\f\u0092\3\2\2\2\16\u00a1\3\2\2\2\20\u00a6\3"+
		"\2\2\2\22\u00a8\3\2\2\2\24\u00ab\3\2\2\2\26\u00ae\3\2\2\2\30\u00c6\3\2"+
		"\2\2\32\u00c9\3\2\2\2\34\u00dc\3\2\2\2\36\u00de\3\2\2\2 \u00e7\3\2\2\2"+
		"\"\u00f2\3\2\2\2$\u00f6\3\2\2\2&\u0137\3\2\2\2(\u0139\3\2\2\2*\u013c\3"+
		"\2\2\2,\u0140\3\2\2\2.\u0148\3\2\2\2\60\u014a\3\2\2\2\62\u014c\3\2\2\2"+
		"\64\u0158\3\2\2\2\66\u015f\3\2\2\28\u0167\3\2\2\2:\u0169\3\2\2\2<\u016d"+
		"\3\2\2\2>\u016f\3\2\2\2@\u0172\3\2\2\2B\u0174\3\2\2\2D\u0176\3\2\2\2F"+
		"\u0185\3\2\2\2H\u0187\3\2\2\2J\u0189\3\2\2\2L\u018d\3\2\2\2N\u0194\3\2"+
		"\2\2P\u0199\3\2\2\2R\u01aa\3\2\2\2T\u01b2\3\2\2\2V\u01bb\3\2\2\2X\u01c8"+
		"\3\2\2\2Z\u01cb\3\2\2\2\\\u01d0\3\2\2\2^\u01d2\3\2\2\2`\u01d5\3\2\2\2"+
		"b\u01df\3\2\2\2d\u01e1\3\2\2\2f\u01e3\3\2\2\2h\u01e5\3\2\2\2j\u01e7\3"+
		"\2\2\2l\u01e9\3\2\2\2n\u01eb\3\2\2\2p\u01ed\3\2\2\2rw\7:\2\2sw\7S\2\2"+
		"tw\5\4\3\2uw\7K\2\2vr\3\2\2\2vs\3\2\2\2vt\3\2\2\2vu\3\2\2\2w\3\3\2\2\2"+
		"xy\t\2\2\2y\5\3\2\2\2z}\5\b\5\2{}\5\n\6\2|z\3\2\2\2|{\3\2\2\2}\7\3\2\2"+
		"\2~\177\7#\2\2\177\u0080\5n8\2\u0080\u0082\5\f\7\2\u0081\u0083\5\34\17"+
		"\2\u0082\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085"+
		"\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0087\7\2\2\3\u0087\t\3\2\2\2\u0088"+
		"\u0089\7\64\2\2\u0089\u008a\5h\65\2\u008a\u008c\5\f\7\2\u008b\u008d\5"+
		"\30\r\2\u008c\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008c\3\2\2\2\u008e"+
		"\u008f\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0091\7\2\2\3\u0091\13\3\2\2"+
		"\2\u0092\u0093\7\'\2\2\u0093\u0094\5\16\b\2\u0094\u0097\3\2\2\2\u0095"+
		"\u0096\7(\2\2\u0096\u0098\5\16\b\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2"+
		"\2\2\u0098\u009b\3\2\2\2\u0099\u009a\7)\2\2\u009a\u009c\5\16\b\2\u009b"+
		"\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009f\3\2\2\2\u009d\u009e\7*"+
		"\2\2\u009e\u00a0\5\16\b\2\u009f\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0"+
		"\r\3\2\2\2\u00a1\u00a2\t\3\2\2\u00a2\17\3\2\2\2\u00a3\u00a7\5\22\n\2\u00a4"+
		"\u00a7\5\24\13\2\u00a5\u00a7\5\26\f\2\u00a6\u00a3\3\2\2\2\u00a6\u00a4"+
		"\3\2\2\2\u00a6\u00a5\3\2\2\2\u00a7\21\3\2\2\2\u00a8\u00a9\7$\2\2\u00a9"+
		"\u00aa\5\34\17\2\u00aa\23\3\2\2\2\u00ab\u00ac\7%\2\2\u00ac\u00ad\5\34"+
		"\17\2\u00ad\25\3\2\2\2\u00ae\u00af\7&\2\2\u00af\u00b0\5\34\17\2\u00b0"+
		"\27\3\2\2\2\u00b1\u00b2\7\5\2\2\u00b2\u00c7\5\34\17\2\u00b3\u00b4\7\6"+
		"\2\2\u00b4\u00c7\5\34\17\2\u00b5\u00b6\7\7\2\2\u00b6\u00c7\5\32\16\2\u00b7"+
		"\u00b8\7%\2\2\u00b8\u00c7\5\32\16\2\u00b9\u00ba\7&\2\2\u00ba\u00c7\5\32"+
		"\16\2\u00bb\u00bc\7\25\2\2\u00bc\u00c7\5\32\16\2\u00bd\u00be\7\b\2\2\u00be"+
		"\u00bf\5^\60\2\u00bf\u00c2\5b\62\2\u00c0\u00c1\7\4\2\2\u00c1\u00c3\5b"+
		"\62\2\u00c2\u00c0\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c4"+
		"\u00c5\3\2\2\2\u00c5\u00c7\3\2\2\2\u00c6\u00b1\3\2\2\2\u00c6\u00b3\3\2"+
		"\2\2\u00c6\u00b5\3\2\2\2\u00c6\u00b7\3\2\2\2\u00c6\u00b9\3\2\2\2\u00c6"+
		"\u00bb\3\2\2\2\u00c6\u00bd\3\2\2\2\u00c7\31\3\2\2\2\u00c8\u00ca\5^\60"+
		"\2\u00c9\u00c8\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00d0"+
		"\5b\62\2\u00cc\u00cd\7,\2\2\u00cd\u00cf\5\32\16\2\u00ce\u00cc\3\2\2\2"+
		"\u00cf\u00d2\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\33"+
		"\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d3\u00d8\5\36\20\2\u00d4\u00d5\7+\2\2"+
		"\u00d5\u00d7\5\36\20\2\u00d6\u00d4\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d6"+
		"\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00dd\3\2\2\2\u00da\u00d8\3\2\2\2\u00db"+
		"\u00dd\5 \21\2\u00dc\u00d3\3\2\2\2\u00dc\u00db\3\2\2\2\u00dd\35\3\2\2"+
		"\2\u00de\u00e2\5 \21\2\u00df\u00e0\5p9\2\u00e0\u00e1\5 \21\2\u00e1\u00e3"+
		"\3\2\2\2\u00e2\u00df\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4"+
		"\u00e5\3\2\2\2\u00e5\37\3\2\2\2\u00e6\u00e8\7\34\2\2\u00e7\u00e6\3\2\2"+
		"\2\u00e7\u00e8\3\2\2\2\u00e8\u00eb\3\2\2\2\u00e9\u00ec\5&\24\2\u00ea\u00ec"+
		"\5\"\22\2\u00eb\u00e9\3\2\2\2\u00eb\u00ea\3\2\2\2\u00ec!\3\2\2\2\u00ed"+
		"\u00f3\5n8\2\u00ee\u00ef\78\2\2\u00ef\u00f0\5\34\17\2\u00f0\u00f1\79\2"+
		"\2\u00f1\u00f3\3\2\2\2\u00f2\u00ed\3\2\2\2\u00f2\u00ee\3\2\2\2\u00f3#"+
		"\3\2\2\2\u00f4\u00f7\5j\66\2\u00f5\u00f7\7\33\2\2\u00f6\u00f4\3\2\2\2"+
		"\u00f6\u00f5\3\2\2\2\u00f7%\3\2\2\2\u00f8\u00f9\5b\62\2\u00f9\u00fa\7"+
		"-\2\2\u00fa\u00fb\5 \21\2\u00fb\u0138\3\2\2\2\u00fc\u00fd\5b\62\2\u00fd"+
		"\u00fe\7.\2\2\u00fe\u00ff\5 \21\2\u00ff\u0138\3\2\2\2\u0100\u0101\5b\62"+
		"\2\u0101\u0102\7/\2\2\u0102\u0103\5$\23\2\u0103\u0138\3\2\2\2\u0104\u0105"+
		"\5b\62\2\u0105\u0106\7\60\2\2\u0106\u0138\3\2\2\2\u0107\u0108\5b\62\2"+
		"\u0108\u0109\7\61\2\2\u0109\u010b\5(\25\2\u010a\u010c\5 \21\2\u010b\u010a"+
		"\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u0138\3\2\2\2\u010d\u010e\5b\62\2\u010e"+
		"\u010f\7\62\2\2\u010f\u0111\5(\25\2\u0110\u0112\5 \21\2\u0111\u0110\3"+
		"\2\2\2\u0111\u0112\3\2\2\2\u0112\u0138\3\2\2\2\u0113\u0114\5b\62\2\u0114"+
		"\u0115\7\63\2\2\u0115\u0117\5(\25\2\u0116\u0118\5 \21\2\u0117\u0116\3"+
		"\2\2\2\u0117\u0118\3\2\2\2\u0118\u0138\3\2\2\2\u0119\u011a\5B\"\2\u011a"+
		"\u011b\7-\2\2\u011b\u011c\5*\26\2\u011c\u0138\3\2\2\2\u011d\u011e\5B\""+
		"\2\u011e\u011f\7.\2\2\u011f\u0120\5*\26\2\u0120\u0138\3\2\2\2\u0121\u0122"+
		"\5B\"\2\u0122\u0123\7/\2\2\u0123\u0124\58\35\2\u0124\u0138\3\2\2\2\u0125"+
		"\u0126\5B\"\2\u0126\u0127\7\61\2\2\u0127\u0129\5(\25\2\u0128\u012a\5*"+
		"\26\2\u0129\u0128\3\2\2\2\u0129\u012a\3\2\2\2\u012a\u0138\3\2\2\2\u012b"+
		"\u012c\5B\"\2\u012c\u012d\7\62\2\2\u012d\u012f\5(\25\2\u012e\u0130\5*"+
		"\26\2\u012f\u012e\3\2\2\2\u012f\u0130\3\2\2\2\u0130\u0138\3\2\2\2\u0131"+
		"\u0132\5B\"\2\u0132\u0133\7\63\2\2\u0133\u0135\5(\25\2\u0134\u0136\5*"+
		"\26\2\u0135\u0134\3\2\2\2\u0135\u0136\3\2\2\2\u0136\u0138\3\2\2\2\u0137"+
		"\u00f8\3\2\2\2\u0137\u00fc\3\2\2\2\u0137\u0100\3\2\2\2\u0137\u0104\3\2"+
		"\2\2\u0137\u0107\3\2\2\2\u0137\u010d\3\2\2\2\u0137\u0113\3\2\2\2\u0137"+
		"\u0119\3\2\2\2\u0137\u011d\3\2\2\2\u0137\u0121\3\2\2\2\u0137\u0125\3\2"+
		"\2\2\u0137\u012b\3\2\2\2\u0137\u0131\3\2\2\2\u0138\'\3\2\2\2\u0139\u013a"+
		"\7A\2\2\u013a)\3\2\2\2\u013b\u013d\7\34\2\2\u013c\u013b\3\2\2\2\u013c"+
		"\u013d\3\2\2\2\u013d\u013e\3\2\2\2\u013e\u013f\5\64\33\2\u013f+\3\2\2"+
		"\2\u0140\u0145\5.\30\2\u0141\u0142\7,\2\2\u0142\u0144\5.\30\2\u0143\u0141"+
		"\3\2\2\2\u0144\u0147\3\2\2\2\u0145\u0143\3\2\2\2\u0145\u0146\3\2\2\2\u0146"+
		"-\3\2\2\2\u0147\u0145\3\2\2\2\u0148\u0149\5h\65\2\u0149/\3\2\2\2\u014a"+
		"\u014b\5\62\32\2\u014b\61\3\2\2\2\u014c\u014d\5\2\2\2\u014d\63\3\2\2\2"+
		"\u014e\u0159\5\66\34\2\u014f\u0150\7!\2\2\u0150\u0151\5`\61\2\u0151\u0152"+
		"\7\"\2\2\u0152\u0159\3\2\2\2\u0153\u0159\5D#\2\u0154\u0155\78\2\2\u0155"+
		"\u0156\5R*\2\u0156\u0157\79\2\2\u0157\u0159\3\2\2\2\u0158\u014e\3\2\2"+
		"\2\u0158\u014f\3\2\2\2\u0158\u0153\3\2\2\2\u0158\u0154\3\2\2\2\u0159\65"+
		"\3\2\2\2\u015a\u0160\5f\64\2\u015b\u0160\7\n\2\2\u015c\u0160\7\13\2\2"+
		"\u015d\u0160\7\f\2\2\u015e\u0160\7\r\2\2\u015f\u015a\3\2\2\2\u015f\u015b"+
		"\3\2\2\2\u015f\u015c\3\2\2\2\u015f\u015d\3\2\2\2\u015f\u015e\3\2\2\2\u0160"+
		"\67\3\2\2\2\u0161\u0168\5:\36\2\u0162\u0168\5<\37\2\u0163\u0168\5> \2"+
		"\u0164\u0168\5N(\2\u0165\u0168\5L\'\2\u0166\u0168\5P)\2\u0167\u0161\3"+
		"\2\2\2\u0167\u0162\3\2\2\2\u0167\u0163\3\2\2\2\u0167\u0164\3\2\2\2\u0167"+
		"\u0165\3\2\2\2\u0167\u0166\3\2\2\2\u01689\3\2\2\2\u0169\u016a\5@!\2\u016a"+
		"\u016b\7\t\2\2\u016b\u016c\5\66\34\2\u016c;\3\2\2\2\u016d\u016e\7L\2\2"+
		"\u016e=\3\2\2\2\u016f\u0170\7L\2\2\u0170\u0171\7W\2\2\u0171?\3\2\2\2\u0172"+
		"\u0173\7L\2\2\u0173A\3\2\2\2\u0174\u0175\5\2\2\2\u0175C\3\2\2\2\u0176"+
		"\u0177\5\66\34\2\u0177\u0178\7\31\2\2\u0178\u0179\5F$\2\u0179\u0180\5"+
		"H%\2\u017a\u017b\7,\2\2\u017b\u017c\5F$\2\u017c\u017d\5H%\2\u017d\u017f"+
		"\3\2\2\2\u017e\u017a\3\2\2\2\u017f\u0182\3\2\2\2\u0180\u017e\3\2\2\2\u0180"+
		"\u0181\3\2\2\2\u0181\u0183\3\2\2\2\u0182\u0180\3\2\2\2\u0183\u0184\7\32"+
		"\2\2\u0184E\3\2\2\2\u0185\u0186\t\4\2\2\u0186G\3\2\2\2\u0187\u0188\58"+
		"\35\2\u0188I\3\2\2\2\u0189\u018a\7\25\2\2\u018a\u018b\5h\65\2\u018bK\3"+
		"\2\2\2\u018c\u018e\t\5\2\2\u018d\u018c\3\2\2\2\u018d\u018e\3\2\2\2\u018e"+
		"\u018f\3\2\2\2\u018f\u0190\7A\2\2\u0190\u0191\7\27\2\2\u0191\u0192\7A"+
		"\2\2\u0192M\3\2\2\2\u0193\u0195\t\5\2\2\u0194\u0193\3\2\2\2\u0194\u0195"+
		"\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0197\7A\2\2\u0197O\3\2\2\2\u0198\u019a"+
		"\t\5\2\2\u0199\u0198\3\2\2\2\u0199\u019a\3\2\2\2\u019a\u01a8\3\2\2\2\u019b"+
		"\u019e\7A\2\2\u019c\u019d\7\27\2\2\u019d\u019f\7A\2\2\u019e\u019c\3\2"+
		"\2\2\u019e\u019f\3\2\2\2\u019f\u01a1\3\2\2\2\u01a0\u01a2\7J\2\2\u01a1"+
		"\u01a0\3\2\2\2\u01a1\u01a2\3\2\2\2\u01a2\u01a9\3\2\2\2\u01a3\u01a4\7\27"+
		"\2\2\u01a4\u01a6\7A\2\2\u01a5\u01a7\7J\2\2\u01a6\u01a5\3\2\2\2\u01a6\u01a7"+
		"\3\2\2\2\u01a7\u01a9\3\2\2\2\u01a8\u019b\3\2\2\2\u01a8\u01a3\3\2\2\2\u01a9"+
		"Q\3\2\2\2\u01aa\u01af\5T+\2\u01ab\u01ac\7+\2\2\u01ac\u01ae\5T+\2\u01ad"+
		"\u01ab\3\2\2\2\u01ae\u01b1\3\2\2\2\u01af\u01ad\3\2\2\2\u01af\u01b0\3\2"+
		"\2\2\u01b0S\3\2\2\2\u01b1\u01af\3\2\2\2\u01b2\u01b7\5*\26\2\u01b3\u01b4"+
		"\7,\2\2\u01b4\u01b6\5*\26\2\u01b5\u01b3\3\2\2\2\u01b6\u01b9\3\2\2\2\u01b7"+
		"\u01b5\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8U\3\2\2\2\u01b9\u01b7\3\2\2\2"+
		"\u01ba\u01bc\5^\60\2\u01bb\u01ba\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01bd"+
		"\3\2\2\2\u01bd\u01c5\5X-\2\u01be\u01c0\7,\2\2\u01bf\u01c1\5^\60\2\u01c0"+
		"\u01bf\3\2\2\2\u01c0\u01c1\3\2\2\2\u01c1\u01c2\3\2\2\2\u01c2\u01c4\5X"+
		"-\2\u01c3\u01be\3\2\2\2\u01c4\u01c7\3\2\2\2\u01c5\u01c3\3\2\2\2\u01c5"+
		"\u01c6\3\2\2\2\u01c6W\3\2\2\2\u01c7\u01c5\3\2\2\2\u01c8\u01c9\5Z.\2\u01c9"+
		"\u01ca\5\\/\2\u01caY\3\2\2\2\u01cb\u01cc\5\2\2\2\u01cc[\3\2\2\2\u01cd"+
		"\u01d1\7\33\2\2\u01ce\u01d1\5\2\2\2\u01cf\u01d1\58\35\2\u01d0\u01cd\3"+
		"\2\2\2\u01d0\u01ce\3\2\2\2\u01d0\u01cf\3\2\2\2\u01d1]\3\2\2\2\u01d2\u01d3"+
		"\7\24\2\2\u01d3\u01d4\5V,\2\u01d4_\3\2\2\2\u01d5\u01da\58\35\2\u01d6\u01d7"+
		"\7\3\2\2\u01d7\u01d9\58\35\2\u01d8\u01d6\3\2\2\2\u01d9\u01dc\3\2\2\2\u01da"+
		"\u01d8\3\2\2\2\u01da\u01db\3\2\2\2\u01dba\3\2\2\2\u01dc\u01da\3\2\2\2"+
		"\u01dd\u01e0\5h\65\2\u01de\u01e0\5J&\2\u01df\u01dd\3\2\2\2\u01df\u01de"+
		"\3\2\2\2\u01e0c\3\2\2\2\u01e1\u01e2\5\2\2\2\u01e2e\3\2\2\2\u01e3\u01e4"+
		"\5\2\2\2\u01e4g\3\2\2\2\u01e5\u01e6\5\2\2\2\u01e6i\3\2\2\2\u01e7\u01e8"+
		"\5\2\2\2\u01e8k\3\2\2\2\u01e9\u01ea\5\2\2\2\u01eam\3\2\2\2\u01eb\u01ec"+
		"\5\2\2\2\u01eco\3\2\2\2\u01ed\u01ee\t\6\2\2\u01eeq\3\2\2\2\61v|\u0084"+
		"\u008e\u0097\u009b\u009f\u00a6\u00c4\u00c6\u00c9\u00d0\u00d8\u00dc\u00e4"+
		"\u00e7\u00eb\u00f2\u00f6\u010b\u0111\u0117\u0129\u012f\u0135\u0137\u013c"+
		"\u0145\u0158\u015f\u0167\u0180\u018d\u0194\u0199\u019e\u01a1\u01a6\u01a8"+
		"\u01af\u01b7\u01bb\u01c0\u01c5\u01d0\u01da\u01df";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}