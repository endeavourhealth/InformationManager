// Generated from C:/Users/Richard Collier/Documents/IdeaProjects/Endeavour/InformationManager/Grammar/src/main/resources\IMLang.g4 by ANTLR 4.9.1
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
public class IMLangParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		EQ=10, MEMBER=11, EXPANSION=12, STATUS=13, ACTIVE=14, INACTIVE=15, DRAFT=16, 
		VERSION=17, IRI_LABEL=18, TYPE=19, TERM=20, SHAPE=21, RECORDTYPE=22, TARGETCLASS=23, 
		CLASS=24, OBJECTPROPERTY=25, DATAPROPERTY=26, ANNOTATION=27, PROPERTYCONSTRAINT=28, 
		DATATYPE=29, VALUESET=30, PATH=31, MINCOUNT=32, MAXCOUNT=33, NAME=34, 
		DESCRIPTION=35, CODE=36, SCHEME=37, MININCLUSIVE=38, MAXINCLUSIVE=39, 
		MINEXCLUSIVE=40, MAXEXCLUSIVE=41, SUBCLASS=42, EQUIVALENTTO=43, DISJOINT=44, 
		SUBPROPERTY=45, INVERSE=46, INTEGER=47, DOUBLE=48, DIGIT=49, EXACT=50, 
		AND=51, OR=52, PREFIXIRI=53, IRIREF=54, LOWERCASE=55, UPPERCASE=56, PLX=57, 
		PERCENT=58, QUOTED_STRING=59, HEX=60, PN_LOCAL_ESC=61, WS=62, SC=63;
	public static final int
		RULE_concept = 0, RULE_classAxiom = 1, RULE_propertyAxiom = 2, RULE_type = 3, 
		RULE_classType = 4, RULE_dataType = 5, RULE_shape = 6, RULE_recordType = 7, 
		RULE_objectProperty = 8, RULE_dataProperty = 9, RULE_annotationProperty = 10, 
		RULE_members = 11, RULE_expansion = 12, RULE_valueSet = 13, RULE_shapeOf = 14, 
		RULE_propertyConstraint = 15, RULE_constraintParameter = 16, RULE_minCount = 17, 
		RULE_maxCount = 18, RULE_minInclusive = 19, RULE_maxInclusive = 20, RULE_minExclusive = 21, 
		RULE_maxExclusive = 22, RULE_classValue = 23, RULE_label = 24, RULE_status = 25, 
		RULE_version = 26, RULE_identifierIri = 27, RULE_name = 28, RULE_description = 29, 
		RULE_code = 30, RULE_scheme = 31, RULE_subclassOf = 32, RULE_equivalentTo = 33, 
		RULE_disjointWith = 34, RULE_subpropertyOf = 35, RULE_inverseOf = 36, 
		RULE_classExpression = 37, RULE_iri = 38, RULE_roleGroup = 39, RULE_role = 40, 
		RULE_dataRange = 41, RULE_rangeValue = 42, RULE_typedString = 43, RULE_valueCollection = 44, 
		RULE_dataRangeCollection = 45;
	private static String[] makeRuleNames() {
		return new String[] {
			"concept", "classAxiom", "propertyAxiom", "type", "classType", "dataType", 
			"shape", "recordType", "objectProperty", "dataProperty", "annotationProperty", 
			"members", "expansion", "valueSet", "shapeOf", "propertyConstraint", 
			"constraintParameter", "minCount", "maxCount", "minInclusive", "maxInclusive", 
			"minExclusive", "maxExclusive", "classValue", "label", "status", "version", 
			"identifierIri", "name", "description", "code", "scheme", "subclassOf", 
			"equivalentTo", "disjointWith", "subpropertyOf", "inverseOf", "classExpression", 
			"iri", "roleGroup", "role", "dataRange", "rangeValue", "typedString", 
			"valueCollection", "dataRangeCollection"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'('", "','", "')'", "'['", "']'", "'{'", "'}'", "'^^'", 
			"'='", null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, "'or'", null, null, null, null, null, 
			null, null, null, null, null, "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "EQ", "MEMBER", 
			"EXPANSION", "STATUS", "ACTIVE", "INACTIVE", "DRAFT", "VERSION", "IRI_LABEL", 
			"TYPE", "TERM", "SHAPE", "RECORDTYPE", "TARGETCLASS", "CLASS", "OBJECTPROPERTY", 
			"DATAPROPERTY", "ANNOTATION", "PROPERTYCONSTRAINT", "DATATYPE", "VALUESET", 
			"PATH", "MINCOUNT", "MAXCOUNT", "NAME", "DESCRIPTION", "CODE", "SCHEME", 
			"MININCLUSIVE", "MAXINCLUSIVE", "MINEXCLUSIVE", "MAXEXCLUSIVE", "SUBCLASS", 
			"EQUIVALENTTO", "DISJOINT", "SUBPROPERTY", "INVERSE", "INTEGER", "DOUBLE", 
			"DIGIT", "EXACT", "AND", "OR", "PREFIXIRI", "IRIREF", "LOWERCASE", "UPPERCASE", 
			"PLX", "PERCENT", "QUOTED_STRING", "HEX", "PN_LOCAL_ESC", "WS", "SC"
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
	public String getGrammarFileName() { return "IMLang.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public IMLangParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ConceptContext extends ParserRuleContext {
		public IdentifierIriContext identifierIri() {
			return getRuleContext(IdentifierIriContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode EOF() { return getToken(IMLangParser.EOF, 0); }
		public ConceptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_concept; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterConcept(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitConcept(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitConcept(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptContext concept() throws RecognitionException {
		ConceptContext _localctx = new ConceptContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_concept);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			identifierIri();
			setState(93);
			type();
			setState(94);
			match(T__0);
			setState(95);
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

	public static class ClassAxiomContext extends ParserRuleContext {
		public SubclassOfContext subclassOf() {
			return getRuleContext(SubclassOfContext.class,0);
		}
		public EquivalentToContext equivalentTo() {
			return getRuleContext(EquivalentToContext.class,0);
		}
		public DisjointWithContext disjointWith() {
			return getRuleContext(DisjointWithContext.class,0);
		}
		public ClassAxiomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classAxiom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterClassAxiom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitClassAxiom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitClassAxiom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassAxiomContext classAxiom() throws RecognitionException {
		ClassAxiomContext _localctx = new ClassAxiomContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_classAxiom);
		try {
			setState(100);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBCLASS:
				enterOuterAlt(_localctx, 1);
				{
				setState(97);
				subclassOf();
				}
				break;
			case EQUIVALENTTO:
				enterOuterAlt(_localctx, 2);
				{
				setState(98);
				equivalentTo();
				}
				break;
			case DISJOINT:
				enterOuterAlt(_localctx, 3);
				{
				setState(99);
				disjointWith();
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

	public static class PropertyAxiomContext extends ParserRuleContext {
		public SubpropertyOfContext subpropertyOf() {
			return getRuleContext(SubpropertyOfContext.class,0);
		}
		public InverseOfContext inverseOf() {
			return getRuleContext(InverseOfContext.class,0);
		}
		public PropertyAxiomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyAxiom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterPropertyAxiom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitPropertyAxiom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitPropertyAxiom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyAxiomContext propertyAxiom() throws RecognitionException {
		PropertyAxiomContext _localctx = new PropertyAxiomContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_propertyAxiom);
		try {
			setState(104);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBPROPERTY:
				enterOuterAlt(_localctx, 1);
				{
				setState(102);
				subpropertyOf();
				}
				break;
			case INVERSE:
				enterOuterAlt(_localctx, 2);
				{
				setState(103);
				inverseOf();
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

	public static class TypeContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(IMLangParser.TYPE, 0); }
		public ClassTypeContext classType() {
			return getRuleContext(ClassTypeContext.class,0);
		}
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public RecordTypeContext recordType() {
			return getRuleContext(RecordTypeContext.class,0);
		}
		public ShapeContext shape() {
			return getRuleContext(ShapeContext.class,0);
		}
		public ValueSetContext valueSet() {
			return getRuleContext(ValueSetContext.class,0);
		}
		public ObjectPropertyContext objectProperty() {
			return getRuleContext(ObjectPropertyContext.class,0);
		}
		public AnnotationPropertyContext annotationProperty() {
			return getRuleContext(AnnotationPropertyContext.class,0);
		}
		public DataPropertyContext dataProperty() {
			return getRuleContext(DataPropertyContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			match(TYPE);
			setState(115);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CLASS:
				{
				setState(107);
				classType();
				}
				break;
			case DATATYPE:
				{
				setState(108);
				dataType();
				}
				break;
			case RECORDTYPE:
				{
				setState(109);
				recordType();
				}
				break;
			case SHAPE:
				{
				setState(110);
				shape();
				}
				break;
			case VALUESET:
				{
				setState(111);
				valueSet();
				}
				break;
			case OBJECTPROPERTY:
				{
				setState(112);
				objectProperty();
				}
				break;
			case ANNOTATION:
				{
				setState(113);
				annotationProperty();
				}
				break;
			case DATAPROPERTY:
				{
				setState(114);
				dataProperty();
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

	public static class ClassTypeContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(IMLangParser.CLASS, 0); }
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public List<ClassAxiomContext> classAxiom() {
			return getRuleContexts(ClassAxiomContext.class);
		}
		public ClassAxiomContext classAxiom(int i) {
			return getRuleContext(ClassAxiomContext.class,i);
		}
		public ClassTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterClassType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitClassType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitClassType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassTypeContext classType() throws RecognitionException {
		ClassTypeContext _localctx = new ClassTypeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_classType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(CLASS);
			setState(129);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(118);
				match(SC);
				setState(119);
				label();
				setState(126);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(120);
						match(SC);
						setState(122);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(121);
							label();
							}
						}

						}
						} 
					}
					setState(128);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				}
				}
				break;
			}
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(131);
				match(SC);
				setState(132);
				classAxiom();
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(133);
					match(SC);
					setState(135);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SUBCLASS) | (1L << EQUIVALENTTO) | (1L << DISJOINT))) != 0)) {
						{
						setState(134);
						classAxiom();
						}
					}

					}
					}
					setState(141);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class DataTypeContext extends ParserRuleContext {
		public TerminalNode DATATYPE() { return getToken(IMLangParser.DATATYPE, 0); }
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public DataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitDataType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitDataType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataTypeContext dataType() throws RecognitionException {
		DataTypeContext _localctx = new DataTypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_dataType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(DATATYPE);
			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(145);
				match(SC);
				setState(146);
				label();
				setState(153);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(147);
					match(SC);
					setState(149);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
						{
						setState(148);
						label();
						}
					}

					}
					}
					setState(155);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class ShapeContext extends ParserRuleContext {
		public TerminalNode SHAPE() { return getToken(IMLangParser.SHAPE, 0); }
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public ShapeOfContext shapeOf() {
			return getRuleContext(ShapeOfContext.class,0);
		}
		public List<PropertyConstraintContext> propertyConstraint() {
			return getRuleContexts(PropertyConstraintContext.class);
		}
		public PropertyConstraintContext propertyConstraint(int i) {
			return getRuleContext(PropertyConstraintContext.class,i);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public SubclassOfContext subclassOf() {
			return getRuleContext(SubclassOfContext.class,0);
		}
		public ShapeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shape; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterShape(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitShape(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitShape(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShapeContext shape() throws RecognitionException {
		ShapeContext _localctx = new ShapeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_shape);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			match(SHAPE);
			setState(170);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(159);
				match(SC);
				setState(160);
				label();
				setState(167);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(161);
						match(SC);
						setState(163);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(162);
							label();
							}
						}

						}
						} 
					}
					setState(169);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				}
				}
				break;
			}
			setState(174);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(172);
				match(SC);
				setState(173);
				subclassOf();
				}
				break;
			}
			setState(176);
			match(SC);
			setState(177);
			shapeOf();
			{
			setState(178);
			match(SC);
			setState(179);
			propertyConstraint();
			{
			setState(180);
			match(SC);
			setState(181);
			propertyConstraint();
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

	public static class RecordTypeContext extends ParserRuleContext {
		public TerminalNode RECORDTYPE() { return getToken(IMLangParser.RECORDTYPE, 0); }
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public List<ClassAxiomContext> classAxiom() {
			return getRuleContexts(ClassAxiomContext.class);
		}
		public ClassAxiomContext classAxiom(int i) {
			return getRuleContext(ClassAxiomContext.class,i);
		}
		public List<RoleContext> role() {
			return getRuleContexts(RoleContext.class);
		}
		public RoleContext role(int i) {
			return getRuleContext(RoleContext.class,i);
		}
		public RecordTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recordType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterRecordType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitRecordType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitRecordType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RecordTypeContext recordType() throws RecognitionException {
		RecordTypeContext _localctx = new RecordTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_recordType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(RECORDTYPE);
			setState(195);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(184);
				match(SC);
				setState(185);
				label();
				setState(192);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(186);
						match(SC);
						setState(188);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(187);
							label();
							}
						}

						}
						} 
					}
					setState(194);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
				}
				}
				break;
			}
			setState(208);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(197);
				match(SC);
				setState(198);
				classAxiom();
				setState(205);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(199);
						match(SC);
						setState(201);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SUBCLASS) | (1L << EQUIVALENTTO) | (1L << DISJOINT))) != 0)) {
							{
							setState(200);
							classAxiom();
							}
						}

						}
						} 
					}
					setState(207);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				}
				}
				break;
			}
			setState(219);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(210);
				match(SC);
				setState(211);
				role();
				setState(216);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(212);
					match(SC);
					setState(213);
					role();
					}
					}
					setState(218);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class ObjectPropertyContext extends ParserRuleContext {
		public TerminalNode OBJECTPROPERTY() { return getToken(IMLangParser.OBJECTPROPERTY, 0); }
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public List<PropertyAxiomContext> propertyAxiom() {
			return getRuleContexts(PropertyAxiomContext.class);
		}
		public PropertyAxiomContext propertyAxiom(int i) {
			return getRuleContext(PropertyAxiomContext.class,i);
		}
		public ObjectPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterObjectProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitObjectProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitObjectProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectPropertyContext objectProperty() throws RecognitionException {
		ObjectPropertyContext _localctx = new ObjectPropertyContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_objectProperty);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(OBJECTPROPERTY);
			setState(233);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				setState(222);
				match(SC);
				setState(223);
				label();
				setState(230);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(224);
						match(SC);
						setState(226);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(225);
							label();
							}
						}

						}
						} 
					}
					setState(232);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				}
				}
				break;
			}
			setState(244);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(235);
				match(SC);
				setState(236);
				propertyAxiom();
				setState(241);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(237);
					match(SC);
					setState(238);
					propertyAxiom();
					}
					}
					setState(243);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class DataPropertyContext extends ParserRuleContext {
		public TerminalNode DATAPROPERTY() { return getToken(IMLangParser.DATAPROPERTY, 0); }
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public List<PropertyAxiomContext> propertyAxiom() {
			return getRuleContexts(PropertyAxiomContext.class);
		}
		public PropertyAxiomContext propertyAxiom(int i) {
			return getRuleContext(PropertyAxiomContext.class,i);
		}
		public DataPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterDataProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitDataProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitDataProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataPropertyContext dataProperty() throws RecognitionException {
		DataPropertyContext _localctx = new DataPropertyContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_dataProperty);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(DATAPROPERTY);
			setState(258);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				{
				setState(247);
				match(SC);
				setState(248);
				label();
				setState(255);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(249);
						match(SC);
						setState(251);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(250);
							label();
							}
						}

						}
						} 
					}
					setState(257);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				}
				}
				break;
			}
			setState(269);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(260);
				match(SC);
				setState(261);
				propertyAxiom();
				setState(266);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(262);
					match(SC);
					setState(263);
					propertyAxiom();
					}
					}
					setState(268);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class AnnotationPropertyContext extends ParserRuleContext {
		public TerminalNode ANNOTATION() { return getToken(IMLangParser.ANNOTATION, 0); }
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public List<PropertyAxiomContext> propertyAxiom() {
			return getRuleContexts(PropertyAxiomContext.class);
		}
		public PropertyAxiomContext propertyAxiom(int i) {
			return getRuleContext(PropertyAxiomContext.class,i);
		}
		public AnnotationPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterAnnotationProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitAnnotationProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitAnnotationProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationPropertyContext annotationProperty() throws RecognitionException {
		AnnotationPropertyContext _localctx = new AnnotationPropertyContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_annotationProperty);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			match(ANNOTATION);
			setState(283);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				{
				setState(272);
				match(SC);
				setState(273);
				label();
				setState(280);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(274);
						match(SC);
						setState(276);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(275);
							label();
							}
						}

						}
						} 
					}
					setState(282);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
				}
				}
				break;
			}
			setState(294);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(285);
				match(SC);
				setState(286);
				propertyAxiom();
				setState(291);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(287);
					match(SC);
					setState(288);
					propertyAxiom();
					}
					}
					setState(293);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class MembersContext extends ParserRuleContext {
		public TerminalNode MEMBER() { return getToken(IMLangParser.MEMBER, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public MembersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_members; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMembers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMembers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMembers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MembersContext members() throws RecognitionException {
		MembersContext _localctx = new MembersContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_members);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			match(MEMBER);
			setState(297);
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

	public static class ExpansionContext extends ParserRuleContext {
		public TerminalNode EXPANSION() { return getToken(IMLangParser.EXPANSION, 0); }
		public List<IriContext> iri() {
			return getRuleContexts(IriContext.class);
		}
		public IriContext iri(int i) {
			return getRuleContext(IriContext.class,i);
		}
		public ExpansionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expansion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterExpansion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitExpansion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitExpansion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpansionContext expansion() throws RecognitionException {
		ExpansionContext _localctx = new ExpansionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_expansion);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(299);
			match(EXPANSION);
			setState(300);
			match(T__1);
			setState(301);
			iri();
			setState(304); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(302);
					match(T__2);
					setState(303);
					iri();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(306); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(308);
			match(T__3);
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

	public static class ValueSetContext extends ParserRuleContext {
		public TerminalNode VALUESET() { return getToken(IMLangParser.VALUESET, 0); }
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public List<LabelContext> label() {
			return getRuleContexts(LabelContext.class);
		}
		public LabelContext label(int i) {
			return getRuleContext(LabelContext.class,i);
		}
		public SubclassOfContext subclassOf() {
			return getRuleContext(SubclassOfContext.class,0);
		}
		public MembersContext members() {
			return getRuleContext(MembersContext.class,0);
		}
		public ExpansionContext expansion() {
			return getRuleContext(ExpansionContext.class,0);
		}
		public ValueSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueSet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterValueSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitValueSet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitValueSet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueSetContext valueSet() throws RecognitionException {
		ValueSetContext _localctx = new ValueSetContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_valueSet);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			match(VALUESET);
			setState(322);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				{
				setState(311);
				match(SC);
				setState(312);
				label();
				setState(319);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(313);
						match(SC);
						setState(315);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(314);
							label();
							}
						}

						}
						} 
					}
					setState(321);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,41,_ctx);
				}
				}
				break;
			}
			setState(326);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(324);
				match(SC);
				setState(325);
				subclassOf();
				}
				break;
			}
			setState(330);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(328);
				match(SC);
				setState(329);
				members();
				}
				break;
			}
			setState(334);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(332);
				match(SC);
				setState(333);
				expansion();
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

	public static class ShapeOfContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public TerminalNode TARGETCLASS() { return getToken(IMLangParser.TARGETCLASS, 0); }
		public ShapeOfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shapeOf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterShapeOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitShapeOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitShapeOf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShapeOfContext shapeOf() throws RecognitionException {
		ShapeOfContext _localctx = new ShapeOfContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_shapeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(336);
			match(TARGETCLASS);
			}
			setState(337);
			iri();
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

	public static class PropertyConstraintContext extends ParserRuleContext {
		public TerminalNode PROPERTYCONSTRAINT() { return getToken(IMLangParser.PROPERTYCONSTRAINT, 0); }
		public TerminalNode PATH() { return getToken(IMLangParser.PATH, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public List<ConstraintParameterContext> constraintParameter() {
			return getRuleContexts(ConstraintParameterContext.class);
		}
		public ConstraintParameterContext constraintParameter(int i) {
			return getRuleContext(ConstraintParameterContext.class,i);
		}
		public PropertyConstraintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyConstraint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterPropertyConstraint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitPropertyConstraint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitPropertyConstraint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyConstraintContext propertyConstraint() throws RecognitionException {
		PropertyConstraintContext _localctx = new PropertyConstraintContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_propertyConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
			match(PROPERTYCONSTRAINT);
			setState(340);
			match(T__4);
			setState(341);
			match(PATH);
			setState(342);
			iri();
			setState(352);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(343);
				match(SC);
				setState(344);
				constraintParameter();
				setState(349);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(345);
					match(SC);
					setState(346);
					constraintParameter();
					}
					}
					setState(351);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(354);
			match(T__5);
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

	public static class ConstraintParameterContext extends ParserRuleContext {
		public MinCountContext minCount() {
			return getRuleContext(MinCountContext.class,0);
		}
		public MaxCountContext maxCount() {
			return getRuleContext(MaxCountContext.class,0);
		}
		public MinInclusiveContext minInclusive() {
			return getRuleContext(MinInclusiveContext.class,0);
		}
		public MaxInclusiveContext maxInclusive() {
			return getRuleContext(MaxInclusiveContext.class,0);
		}
		public ClassValueContext classValue() {
			return getRuleContext(ClassValueContext.class,0);
		}
		public DataRangeContext dataRange() {
			return getRuleContext(DataRangeContext.class,0);
		}
		public ConstraintParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraintParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterConstraintParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitConstraintParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitConstraintParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstraintParameterContext constraintParameter() throws RecognitionException {
		ConstraintParameterContext _localctx = new ConstraintParameterContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_constraintParameter);
		try {
			setState(362);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(356);
				minCount();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(357);
				maxCount();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(358);
				minInclusive();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(359);
				maxInclusive();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(360);
				classValue();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(361);
				dataRange();
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

	public static class MinCountContext extends ParserRuleContext {
		public TerminalNode MINCOUNT() { return getToken(IMLangParser.MINCOUNT, 0); }
		public TerminalNode INTEGER() { return getToken(IMLangParser.INTEGER, 0); }
		public MinCountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_minCount; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMinCount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMinCount(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMinCount(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MinCountContext minCount() throws RecognitionException {
		MinCountContext _localctx = new MinCountContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_minCount);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			match(MINCOUNT);
			setState(365);
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

	public static class MaxCountContext extends ParserRuleContext {
		public TerminalNode MAXCOUNT() { return getToken(IMLangParser.MAXCOUNT, 0); }
		public TerminalNode INTEGER() { return getToken(IMLangParser.INTEGER, 0); }
		public MaxCountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_maxCount; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMaxCount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMaxCount(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMaxCount(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaxCountContext maxCount() throws RecognitionException {
		MaxCountContext _localctx = new MaxCountContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_maxCount);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			match(MAXCOUNT);
			setState(368);
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

	public static class MinInclusiveContext extends ParserRuleContext {
		public TerminalNode MININCLUSIVE() { return getToken(IMLangParser.MININCLUSIVE, 0); }
		public TerminalNode DOUBLE() { return getToken(IMLangParser.DOUBLE, 0); }
		public MinInclusiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_minInclusive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMinInclusive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMinInclusive(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMinInclusive(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MinInclusiveContext minInclusive() throws RecognitionException {
		MinInclusiveContext _localctx = new MinInclusiveContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_minInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			match(MININCLUSIVE);
			setState(371);
			match(DOUBLE);
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

	public static class MaxInclusiveContext extends ParserRuleContext {
		public TerminalNode MAXINCLUSIVE() { return getToken(IMLangParser.MAXINCLUSIVE, 0); }
		public TerminalNode DOUBLE() { return getToken(IMLangParser.DOUBLE, 0); }
		public MaxInclusiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_maxInclusive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMaxInclusive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMaxInclusive(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMaxInclusive(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaxInclusiveContext maxInclusive() throws RecognitionException {
		MaxInclusiveContext _localctx = new MaxInclusiveContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_maxInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(373);
			match(MAXINCLUSIVE);
			setState(374);
			match(DOUBLE);
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

	public static class MinExclusiveContext extends ParserRuleContext {
		public TerminalNode MINEXCLUSIVE() { return getToken(IMLangParser.MINEXCLUSIVE, 0); }
		public TerminalNode DOUBLE() { return getToken(IMLangParser.DOUBLE, 0); }
		public MinExclusiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_minExclusive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMinExclusive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMinExclusive(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMinExclusive(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MinExclusiveContext minExclusive() throws RecognitionException {
		MinExclusiveContext _localctx = new MinExclusiveContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_minExclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			match(MINEXCLUSIVE);
			setState(377);
			match(DOUBLE);
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

	public static class MaxExclusiveContext extends ParserRuleContext {
		public TerminalNode MAXEXCLUSIVE() { return getToken(IMLangParser.MAXEXCLUSIVE, 0); }
		public TerminalNode DOUBLE() { return getToken(IMLangParser.DOUBLE, 0); }
		public MaxExclusiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_maxExclusive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMaxExclusive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMaxExclusive(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMaxExclusive(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaxExclusiveContext maxExclusive() throws RecognitionException {
		MaxExclusiveContext _localctx = new MaxExclusiveContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_maxExclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(379);
			match(MAXEXCLUSIVE);
			setState(380);
			match(DOUBLE);
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

	public static class ClassValueContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(IMLangParser.CLASS, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public PropertyConstraintContext propertyConstraint() {
			return getRuleContext(PropertyConstraintContext.class,0);
		}
		public ClassValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterClassValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitClassValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitClassValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassValueContext classValue() throws RecognitionException {
		ClassValueContext _localctx = new ClassValueContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_classValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			match(CLASS);
			setState(388);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREFIXIRI:
			case IRIREF:
			case QUOTED_STRING:
				{
				setState(383);
				iri();
				}
				break;
			case T__4:
				{
				setState(384);
				match(T__4);
				setState(385);
				propertyConstraint();
				setState(386);
				match(T__5);
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

	public static class LabelContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public CodeContext code() {
			return getRuleContext(CodeContext.class,0);
		}
		public SchemeContext scheme() {
			return getRuleContext(SchemeContext.class,0);
		}
		public StatusContext status() {
			return getRuleContext(StatusContext.class,0);
		}
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitLabel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(396);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAME:
				{
				setState(390);
				name();
				}
				break;
			case DESCRIPTION:
				{
				setState(391);
				description();
				}
				break;
			case CODE:
				{
				setState(392);
				code();
				}
				break;
			case SCHEME:
				{
				setState(393);
				scheme();
				}
				break;
			case STATUS:
				{
				setState(394);
				status();
				}
				break;
			case VERSION:
				{
				setState(395);
				version();
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

	public static class StatusContext extends ParserRuleContext {
		public TerminalNode STATUS() { return getToken(IMLangParser.STATUS, 0); }
		public TerminalNode ACTIVE() { return getToken(IMLangParser.ACTIVE, 0); }
		public TerminalNode INACTIVE() { return getToken(IMLangParser.INACTIVE, 0); }
		public TerminalNode DRAFT() { return getToken(IMLangParser.DRAFT, 0); }
		public StatusContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_status; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterStatus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitStatus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitStatus(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatusContext status() throws RecognitionException {
		StatusContext _localctx = new StatusContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_status);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			match(STATUS);
			setState(399);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ACTIVE) | (1L << INACTIVE) | (1L << DRAFT))) != 0)) ) {
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

	public static class VersionContext extends ParserRuleContext {
		public TerminalNode VERSION() { return getToken(IMLangParser.VERSION, 0); }
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public VersionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_version; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterVersion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitVersion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitVersion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VersionContext version() throws RecognitionException {
		VersionContext _localctx = new VersionContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(VERSION);
			setState(402);
			match(QUOTED_STRING);
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

	public static class IdentifierIriContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public TerminalNode IRI_LABEL() { return getToken(IMLangParser.IRI_LABEL, 0); }
		public IdentifierIriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierIri; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterIdentifierIri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitIdentifierIri(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitIdentifierIri(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierIriContext identifierIri() throws RecognitionException {
		IdentifierIriContext _localctx = new IdentifierIriContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_identifierIri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(405);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRI_LABEL) {
				{
				setState(404);
				match(IRI_LABEL);
				}
			}

			setState(407);
			iri();
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

	public static class NameContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(IMLangParser.NAME, 0); }
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			match(NAME);
			setState(410);
			match(QUOTED_STRING);
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

	public static class DescriptionContext extends ParserRuleContext {
		public TerminalNode DESCRIPTION() { return getToken(IMLangParser.DESCRIPTION, 0); }
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public DescriptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_description; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterDescription(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitDescription(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitDescription(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescriptionContext description() throws RecognitionException {
		DescriptionContext _localctx = new DescriptionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(412);
			match(DESCRIPTION);
			setState(413);
			match(QUOTED_STRING);
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

	public static class CodeContext extends ParserRuleContext {
		public TerminalNode CODE() { return getToken(IMLangParser.CODE, 0); }
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public CodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_code; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterCode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitCode(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitCode(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodeContext code() throws RecognitionException {
		CodeContext _localctx = new CodeContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(415);
			match(CODE);
			setState(416);
			match(QUOTED_STRING);
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

	public static class SchemeContext extends ParserRuleContext {
		public TerminalNode SCHEME() { return getToken(IMLangParser.SCHEME, 0); }
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public SchemeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scheme; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterScheme(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitScheme(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitScheme(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchemeContext scheme() throws RecognitionException {
		SchemeContext _localctx = new SchemeContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_scheme);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
			match(SCHEME);
			setState(419);
			match(QUOTED_STRING);
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

	public static class SubclassOfContext extends ParserRuleContext {
		public TerminalNode SUBCLASS() { return getToken(IMLangParser.SUBCLASS, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public SubclassOfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subclassOf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterSubclassOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitSubclassOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitSubclassOf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubclassOfContext subclassOf() throws RecognitionException {
		SubclassOfContext _localctx = new SubclassOfContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_subclassOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(421);
			match(SUBCLASS);
			setState(422);
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

	public static class EquivalentToContext extends ParserRuleContext {
		public TerminalNode EQUIVALENTTO() { return getToken(IMLangParser.EQUIVALENTTO, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public EquivalentToContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equivalentTo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterEquivalentTo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitEquivalentTo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitEquivalentTo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EquivalentToContext equivalentTo() throws RecognitionException {
		EquivalentToContext _localctx = new EquivalentToContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_equivalentTo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(424);
			match(EQUIVALENTTO);
			setState(425);
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

	public static class DisjointWithContext extends ParserRuleContext {
		public TerminalNode DISJOINT() { return getToken(IMLangParser.DISJOINT, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public DisjointWithContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjointWith; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterDisjointWith(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitDisjointWith(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitDisjointWith(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DisjointWithContext disjointWith() throws RecognitionException {
		DisjointWithContext _localctx = new DisjointWithContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_disjointWith);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(427);
			match(DISJOINT);
			setState(428);
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

	public static class SubpropertyOfContext extends ParserRuleContext {
		public TerminalNode SUBPROPERTY() { return getToken(IMLangParser.SUBPROPERTY, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public SubpropertyOfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subpropertyOf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterSubpropertyOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitSubpropertyOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitSubpropertyOf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubpropertyOfContext subpropertyOf() throws RecognitionException {
		SubpropertyOfContext _localctx = new SubpropertyOfContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_subpropertyOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(430);
			match(SUBPROPERTY);
			setState(431);
			iri();
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

	public static class InverseOfContext extends ParserRuleContext {
		public TerminalNode INVERSE() { return getToken(IMLangParser.INVERSE, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public InverseOfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inverseOf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterInverseOf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitInverseOf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitInverseOf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InverseOfContext inverseOf() throws RecognitionException {
		InverseOfContext _localctx = new InverseOfContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_inverseOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(433);
			match(INVERSE);
			setState(434);
			iri();
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
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public List<ClassExpressionContext> classExpression() {
			return getRuleContexts(ClassExpressionContext.class);
		}
		public ClassExpressionContext classExpression(int i) {
			return getRuleContext(ClassExpressionContext.class,i);
		}
		public RoleGroupContext roleGroup() {
			return getRuleContext(RoleGroupContext.class,0);
		}
		public ClassExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterClassExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitClassExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitClassExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassExpressionContext classExpression() throws RecognitionException {
		ClassExpressionContext _localctx = new ClassExpressionContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_classExpression);
		try {
			int _alt;
			setState(445);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREFIXIRI:
			case IRIREF:
			case QUOTED_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(436);
				iri();
				setState(441);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(437);
						match(T__2);
						setState(438);
						classExpression();
						}
						} 
					}
					setState(443);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
				}
				}
				break;
			case T__4:
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(444);
				roleGroup();
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

	public static class IriContext extends ParserRuleContext {
		public TerminalNode IRIREF() { return getToken(IMLangParser.IRIREF, 0); }
		public TerminalNode PREFIXIRI() { return getToken(IMLangParser.PREFIXIRI, 0); }
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public IriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iri; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterIri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitIri(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitIri(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IriContext iri() throws RecognitionException {
		IriContext _localctx = new IriContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_iri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(447);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PREFIXIRI) | (1L << IRIREF) | (1L << QUOTED_STRING))) != 0)) ) {
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

	public static class RoleGroupContext extends ParserRuleContext {
		public List<RoleContext> role() {
			return getRuleContexts(RoleContext.class);
		}
		public RoleContext role(int i) {
			return getRuleContext(RoleContext.class,i);
		}
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public RoleGroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_roleGroup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterRoleGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitRoleGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitRoleGroup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RoleGroupContext roleGroup() throws RecognitionException {
		RoleGroupContext _localctx = new RoleGroupContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_roleGroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(449);
			_la = _input.LA(1);
			if ( !(_la==T__4 || _la==T__6) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(450);
			role();
			setState(455);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SC) {
				{
				{
				setState(451);
				match(SC);
				setState(452);
				role();
				}
				}
				setState(457);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(458);
			_la = _input.LA(1);
			if ( !(_la==T__5 || _la==T__7) ) {
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

	public static class RoleContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public DataRangeContext dataRange() {
			return getRuleContext(DataRangeContext.class,0);
		}
		public TerminalNode EQ() { return getToken(IMLangParser.EQ, 0); }
		public MinCountContext minCount() {
			return getRuleContext(MinCountContext.class,0);
		}
		public MaxCountContext maxCount() {
			return getRuleContext(MaxCountContext.class,0);
		}
		public RoleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_role; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterRole(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitRole(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitRole(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RoleContext role() throws RecognitionException {
		RoleContext _localctx = new RoleContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_role);
		int _la;
		try {
			setState(470);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREFIXIRI:
			case IRIREF:
			case QUOTED_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(460);
				iri();
				setState(462);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EQ) {
					{
					setState(461);
					match(EQ);
					}
				}

				setState(466);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
				case 1:
					{
					setState(464);
					classExpression();
					}
					break;
				case 2:
					{
					setState(465);
					dataRange();
					}
					break;
				}
				}
				break;
			case MINCOUNT:
				enterOuterAlt(_localctx, 2);
				{
				setState(468);
				minCount();
				}
				break;
			case MAXCOUNT:
				enterOuterAlt(_localctx, 3);
				{
				setState(469);
				maxCount();
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

	public static class DataRangeContext extends ParserRuleContext {
		public ValueCollectionContext valueCollection() {
			return getRuleContext(ValueCollectionContext.class,0);
		}
		public TypedStringContext typedString() {
			return getRuleContext(TypedStringContext.class,0);
		}
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public RangeValueContext rangeValue() {
			return getRuleContext(RangeValueContext.class,0);
		}
		public DataRangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataRange; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterDataRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitDataRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitDataRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataRangeContext dataRange() throws RecognitionException {
		DataRangeContext _localctx = new DataRangeContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_dataRange);
		try {
			setState(476);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(472);
				valueCollection();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(473);
				typedString();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(474);
				match(QUOTED_STRING);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(475);
				rangeValue();
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

	public static class RangeValueContext extends ParserRuleContext {
		public TerminalNode MININCLUSIVE() { return getToken(IMLangParser.MININCLUSIVE, 0); }
		public TerminalNode MINEXCLUSIVE() { return getToken(IMLangParser.MINEXCLUSIVE, 0); }
		public TypedStringContext typedString() {
			return getRuleContext(TypedStringContext.class,0);
		}
		public TerminalNode DOUBLE() { return getToken(IMLangParser.DOUBLE, 0); }
		public TerminalNode MAXINCLUSIVE() { return getToken(IMLangParser.MAXINCLUSIVE, 0); }
		public TerminalNode MAXEXCLUSIVE() { return getToken(IMLangParser.MAXEXCLUSIVE, 0); }
		public RangeValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterRangeValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitRangeValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitRangeValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeValueContext rangeValue() throws RecognitionException {
		RangeValueContext _localctx = new RangeValueContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_rangeValue);
		int _la;
		try {
			setState(488);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MININCLUSIVE:
			case MINEXCLUSIVE:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(478);
				_la = _input.LA(1);
				if ( !(_la==MININCLUSIVE || _la==MINEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(481);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUOTED_STRING:
					{
					setState(479);
					typedString();
					}
					break;
				case DOUBLE:
					{
					setState(480);
					match(DOUBLE);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				break;
			case MAXINCLUSIVE:
			case MAXEXCLUSIVE:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(483);
				_la = _input.LA(1);
				if ( !(_la==MAXINCLUSIVE || _la==MAXEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(486);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUOTED_STRING:
					{
					setState(484);
					typedString();
					}
					break;
				case DOUBLE:
					{
					setState(485);
					match(DOUBLE);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
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

	public static class TypedStringContext extends ParserRuleContext {
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public TypedStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterTypedString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitTypedString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitTypedString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedStringContext typedString() throws RecognitionException {
		TypedStringContext _localctx = new TypedStringContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_typedString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			match(QUOTED_STRING);
			setState(491);
			match(T__8);
			setState(492);
			iri();
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

	public static class ValueCollectionContext extends ParserRuleContext {
		public List<TerminalNode> QUOTED_STRING() { return getTokens(IMLangParser.QUOTED_STRING); }
		public TerminalNode QUOTED_STRING(int i) {
			return getToken(IMLangParser.QUOTED_STRING, i);
		}
		public List<TypedStringContext> typedString() {
			return getRuleContexts(TypedStringContext.class);
		}
		public TypedStringContext typedString(int i) {
			return getRuleContext(TypedStringContext.class,i);
		}
		public ValueCollectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueCollection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterValueCollection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitValueCollection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitValueCollection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueCollectionContext valueCollection() throws RecognitionException {
		ValueCollectionContext _localctx = new ValueCollectionContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_valueCollection);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(494);
			match(T__1);
			setState(497);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,62,_ctx) ) {
			case 1:
				{
				setState(495);
				match(QUOTED_STRING);
				}
				break;
			case 2:
				{
				setState(496);
				typedString();
				}
				break;
			}
			setState(504); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(499);
					match(T__2);
					setState(502);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,63,_ctx) ) {
					case 1:
						{
						setState(500);
						match(QUOTED_STRING);
						}
						break;
					case 2:
						{
						setState(501);
						typedString();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(506); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,64,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(508);
			match(T__3);
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

	public static class DataRangeCollectionContext extends ParserRuleContext {
		public List<DataRangeContext> dataRange() {
			return getRuleContexts(DataRangeContext.class);
		}
		public DataRangeContext dataRange(int i) {
			return getRuleContext(DataRangeContext.class,i);
		}
		public DataRangeCollectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dataRangeCollection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterDataRangeCollection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitDataRangeCollection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitDataRangeCollection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DataRangeCollectionContext dataRangeCollection() throws RecognitionException {
		DataRangeCollectionContext _localctx = new DataRangeCollectionContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_dataRangeCollection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(510);
			match(T__1);
			setState(511);
			dataRange();
			setState(514); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(512);
				match(T__2);
				setState(513);
				dataRange();
				}
				}
				setState(516); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__2 );
			setState(518);
			match(T__3);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3A\u020b\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\5\3g\n\3\3\4\3"+
		"\4\5\4k\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5v\n\5\3\6\3\6\3\6\3"+
		"\6\3\6\5\6}\n\6\7\6\177\n\6\f\6\16\6\u0082\13\6\5\6\u0084\n\6\3\6\3\6"+
		"\3\6\3\6\5\6\u008a\n\6\7\6\u008c\n\6\f\6\16\6\u008f\13\6\5\6\u0091\n\6"+
		"\3\7\3\7\3\7\3\7\3\7\5\7\u0098\n\7\7\7\u009a\n\7\f\7\16\7\u009d\13\7\5"+
		"\7\u009f\n\7\3\b\3\b\3\b\3\b\3\b\5\b\u00a6\n\b\7\b\u00a8\n\b\f\b\16\b"+
		"\u00ab\13\b\5\b\u00ad\n\b\3\b\3\b\5\b\u00b1\n\b\3\b\3\b\3\b\3\b\3\b\3"+
		"\b\3\b\3\t\3\t\3\t\3\t\3\t\5\t\u00bf\n\t\7\t\u00c1\n\t\f\t\16\t\u00c4"+
		"\13\t\5\t\u00c6\n\t\3\t\3\t\3\t\3\t\5\t\u00cc\n\t\7\t\u00ce\n\t\f\t\16"+
		"\t\u00d1\13\t\5\t\u00d3\n\t\3\t\3\t\3\t\3\t\7\t\u00d9\n\t\f\t\16\t\u00dc"+
		"\13\t\5\t\u00de\n\t\3\n\3\n\3\n\3\n\3\n\5\n\u00e5\n\n\7\n\u00e7\n\n\f"+
		"\n\16\n\u00ea\13\n\5\n\u00ec\n\n\3\n\3\n\3\n\3\n\7\n\u00f2\n\n\f\n\16"+
		"\n\u00f5\13\n\5\n\u00f7\n\n\3\13\3\13\3\13\3\13\3\13\5\13\u00fe\n\13\7"+
		"\13\u0100\n\13\f\13\16\13\u0103\13\13\5\13\u0105\n\13\3\13\3\13\3\13\3"+
		"\13\7\13\u010b\n\13\f\13\16\13\u010e\13\13\5\13\u0110\n\13\3\f\3\f\3\f"+
		"\3\f\3\f\5\f\u0117\n\f\7\f\u0119\n\f\f\f\16\f\u011c\13\f\5\f\u011e\n\f"+
		"\3\f\3\f\3\f\3\f\7\f\u0124\n\f\f\f\16\f\u0127\13\f\5\f\u0129\n\f\3\r\3"+
		"\r\3\r\3\16\3\16\3\16\3\16\3\16\6\16\u0133\n\16\r\16\16\16\u0134\3\16"+
		"\3\16\3\17\3\17\3\17\3\17\3\17\5\17\u013e\n\17\7\17\u0140\n\17\f\17\16"+
		"\17\u0143\13\17\5\17\u0145\n\17\3\17\3\17\5\17\u0149\n\17\3\17\3\17\5"+
		"\17\u014d\n\17\3\17\3\17\5\17\u0151\n\17\3\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\7\21\u015e\n\21\f\21\16\21\u0161\13\21\5\21"+
		"\u0163\n\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u016d\n\22\3"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3"+
		"\27\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0187\n\31\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\5\32\u018f\n\32\3\33\3\33\3\33\3\34\3\34\3\34"+
		"\3\35\5\35\u0198\n\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3"+
		" \3!\3!\3!\3\"\3\"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3%\3&\3&\3&\3\'\3\'\3\'"+
		"\7\'\u01ba\n\'\f\'\16\'\u01bd\13\'\3\'\5\'\u01c0\n\'\3(\3(\3)\3)\3)\3"+
		")\7)\u01c8\n)\f)\16)\u01cb\13)\3)\3)\3*\3*\5*\u01d1\n*\3*\3*\5*\u01d5"+
		"\n*\3*\3*\5*\u01d9\n*\3+\3+\3+\3+\5+\u01df\n+\3,\3,\3,\5,\u01e4\n,\3,"+
		"\3,\3,\5,\u01e9\n,\5,\u01eb\n,\3-\3-\3-\3-\3.\3.\3.\5.\u01f4\n.\3.\3."+
		"\3.\5.\u01f9\n.\6.\u01fb\n.\r.\16.\u01fc\3.\3.\3/\3/\3/\3/\6/\u0205\n"+
		"/\r/\16/\u0206\3/\3/\3/\4\u0134\u01fc\2\60\2\4\6\b\n\f\16\20\22\24\26"+
		"\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\\2\b\3\2\20\22\4"+
		"\2\678==\4\2\7\7\t\t\4\2\b\b\n\n\4\2((**\4\2))++\2\u0230\2^\3\2\2\2\4"+
		"f\3\2\2\2\6j\3\2\2\2\bl\3\2\2\2\nw\3\2\2\2\f\u0092\3\2\2\2\16\u00a0\3"+
		"\2\2\2\20\u00b9\3\2\2\2\22\u00df\3\2\2\2\24\u00f8\3\2\2\2\26\u0111\3\2"+
		"\2\2\30\u012a\3\2\2\2\32\u012d\3\2\2\2\34\u0138\3\2\2\2\36\u0152\3\2\2"+
		"\2 \u0155\3\2\2\2\"\u016c\3\2\2\2$\u016e\3\2\2\2&\u0171\3\2\2\2(\u0174"+
		"\3\2\2\2*\u0177\3\2\2\2,\u017a\3\2\2\2.\u017d\3\2\2\2\60\u0180\3\2\2\2"+
		"\62\u018e\3\2\2\2\64\u0190\3\2\2\2\66\u0193\3\2\2\28\u0197\3\2\2\2:\u019b"+
		"\3\2\2\2<\u019e\3\2\2\2>\u01a1\3\2\2\2@\u01a4\3\2\2\2B\u01a7\3\2\2\2D"+
		"\u01aa\3\2\2\2F\u01ad\3\2\2\2H\u01b0\3\2\2\2J\u01b3\3\2\2\2L\u01bf\3\2"+
		"\2\2N\u01c1\3\2\2\2P\u01c3\3\2\2\2R\u01d8\3\2\2\2T\u01de\3\2\2\2V\u01ea"+
		"\3\2\2\2X\u01ec\3\2\2\2Z\u01f0\3\2\2\2\\\u0200\3\2\2\2^_\58\35\2_`\5\b"+
		"\5\2`a\7\3\2\2ab\7\2\2\3b\3\3\2\2\2cg\5B\"\2dg\5D#\2eg\5F$\2fc\3\2\2\2"+
		"fd\3\2\2\2fe\3\2\2\2g\5\3\2\2\2hk\5H%\2ik\5J&\2jh\3\2\2\2ji\3\2\2\2k\7"+
		"\3\2\2\2lu\7\25\2\2mv\5\n\6\2nv\5\f\7\2ov\5\20\t\2pv\5\16\b\2qv\5\34\17"+
		"\2rv\5\22\n\2sv\5\26\f\2tv\5\24\13\2um\3\2\2\2un\3\2\2\2uo\3\2\2\2up\3"+
		"\2\2\2uq\3\2\2\2ur\3\2\2\2us\3\2\2\2ut\3\2\2\2v\t\3\2\2\2w\u0083\7\32"+
		"\2\2xy\7A\2\2y\u0080\5\62\32\2z|\7A\2\2{}\5\62\32\2|{\3\2\2\2|}\3\2\2"+
		"\2}\177\3\2\2\2~z\3\2\2\2\177\u0082\3\2\2\2\u0080~\3\2\2\2\u0080\u0081"+
		"\3\2\2\2\u0081\u0084\3\2\2\2\u0082\u0080\3\2\2\2\u0083x\3\2\2\2\u0083"+
		"\u0084\3\2\2\2\u0084\u0090\3\2\2\2\u0085\u0086\7A\2\2\u0086\u008d\5\4"+
		"\3\2\u0087\u0089\7A\2\2\u0088\u008a\5\4\3\2\u0089\u0088\3\2\2\2\u0089"+
		"\u008a\3\2\2\2\u008a\u008c\3\2\2\2\u008b\u0087\3\2\2\2\u008c\u008f\3\2"+
		"\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u0091\3\2\2\2\u008f"+
		"\u008d\3\2\2\2\u0090\u0085\3\2\2\2\u0090\u0091\3\2\2\2\u0091\13\3\2\2"+
		"\2\u0092\u009e\7\37\2\2\u0093\u0094\7A\2\2\u0094\u009b\5\62\32\2\u0095"+
		"\u0097\7A\2\2\u0096\u0098\5\62\32\2\u0097\u0096\3\2\2\2\u0097\u0098\3"+
		"\2\2\2\u0098\u009a\3\2\2\2\u0099\u0095\3\2\2\2\u009a\u009d\3\2\2\2\u009b"+
		"\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009f\3\2\2\2\u009d\u009b\3\2"+
		"\2\2\u009e\u0093\3\2\2\2\u009e\u009f\3\2\2\2\u009f\r\3\2\2\2\u00a0\u00ac"+
		"\7\27\2\2\u00a1\u00a2\7A\2\2\u00a2\u00a9\5\62\32\2\u00a3\u00a5\7A\2\2"+
		"\u00a4\u00a6\5\62\32\2\u00a5\u00a4\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a8"+
		"\3\2\2\2\u00a7\u00a3\3\2\2\2\u00a8\u00ab\3\2\2\2\u00a9\u00a7\3\2\2\2\u00a9"+
		"\u00aa\3\2\2\2\u00aa\u00ad\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ac\u00a1\3\2"+
		"\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00b0\3\2\2\2\u00ae\u00af\7A\2\2\u00af"+
		"\u00b1\5B\"\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00b2\3\2"+
		"\2\2\u00b2\u00b3\7A\2\2\u00b3\u00b4\5\36\20\2\u00b4\u00b5\7A\2\2\u00b5"+
		"\u00b6\5 \21\2\u00b6\u00b7\7A\2\2\u00b7\u00b8\5 \21\2\u00b8\17\3\2\2\2"+
		"\u00b9\u00c5\7\30\2\2\u00ba\u00bb\7A\2\2\u00bb\u00c2\5\62\32\2\u00bc\u00be"+
		"\7A\2\2\u00bd\u00bf\5\62\32\2\u00be\u00bd\3\2\2\2\u00be\u00bf\3\2\2\2"+
		"\u00bf\u00c1\3\2\2\2\u00c0\u00bc\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0"+
		"\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c6\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5"+
		"\u00ba\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00d2\3\2\2\2\u00c7\u00c8\7A"+
		"\2\2\u00c8\u00cf\5\4\3\2\u00c9\u00cb\7A\2\2\u00ca\u00cc\5\4\3\2\u00cb"+
		"\u00ca\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00ce\3\2\2\2\u00cd\u00c9\3\2"+
		"\2\2\u00ce\u00d1\3\2\2\2\u00cf\u00cd\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0"+
		"\u00d3\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d2\u00c7\3\2\2\2\u00d2\u00d3\3\2"+
		"\2\2\u00d3\u00dd\3\2\2\2\u00d4\u00d5\7A\2\2\u00d5\u00da\5R*\2\u00d6\u00d7"+
		"\7A\2\2\u00d7\u00d9\5R*\2\u00d8\u00d6\3\2\2\2\u00d9\u00dc\3\2\2\2\u00da"+
		"\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00de\3\2\2\2\u00dc\u00da\3\2"+
		"\2\2\u00dd\u00d4\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\21\3\2\2\2\u00df\u00eb"+
		"\7\33\2\2\u00e0\u00e1\7A\2\2\u00e1\u00e8\5\62\32\2\u00e2\u00e4\7A\2\2"+
		"\u00e3\u00e5\5\62\32\2\u00e4\u00e3\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e7"+
		"\3\2\2\2\u00e6\u00e2\3\2\2\2\u00e7\u00ea\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e8"+
		"\u00e9\3\2\2\2\u00e9\u00ec\3\2\2\2\u00ea\u00e8\3\2\2\2\u00eb\u00e0\3\2"+
		"\2\2\u00eb\u00ec\3\2\2\2\u00ec\u00f6\3\2\2\2\u00ed\u00ee\7A\2\2\u00ee"+
		"\u00f3\5\6\4\2\u00ef\u00f0\7A\2\2\u00f0\u00f2\5\6\4\2\u00f1\u00ef\3\2"+
		"\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4"+
		"\u00f7\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00ed\3\2\2\2\u00f6\u00f7\3\2"+
		"\2\2\u00f7\23\3\2\2\2\u00f8\u0104\7\34\2\2\u00f9\u00fa\7A\2\2\u00fa\u0101"+
		"\5\62\32\2\u00fb\u00fd\7A\2\2\u00fc\u00fe\5\62\32\2\u00fd\u00fc\3\2\2"+
		"\2\u00fd\u00fe\3\2\2\2\u00fe\u0100\3\2\2\2\u00ff\u00fb\3\2\2\2\u0100\u0103"+
		"\3\2\2\2\u0101\u00ff\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0105\3\2\2\2\u0103"+
		"\u0101\3\2\2\2\u0104\u00f9\3\2\2\2\u0104\u0105\3\2\2\2\u0105\u010f\3\2"+
		"\2\2\u0106\u0107\7A\2\2\u0107\u010c\5\6\4\2\u0108\u0109\7A\2\2\u0109\u010b"+
		"\5\6\4\2\u010a\u0108\3\2\2\2\u010b\u010e\3\2\2\2\u010c\u010a\3\2\2\2\u010c"+
		"\u010d\3\2\2\2\u010d\u0110\3\2\2\2\u010e\u010c\3\2\2\2\u010f\u0106\3\2"+
		"\2\2\u010f\u0110\3\2\2\2\u0110\25\3\2\2\2\u0111\u011d\7\35\2\2\u0112\u0113"+
		"\7A\2\2\u0113\u011a\5\62\32\2\u0114\u0116\7A\2\2\u0115\u0117\5\62\32\2"+
		"\u0116\u0115\3\2\2\2\u0116\u0117\3\2\2\2\u0117\u0119\3\2\2\2\u0118\u0114"+
		"\3\2\2\2\u0119\u011c\3\2\2\2\u011a\u0118\3\2\2\2\u011a\u011b\3\2\2\2\u011b"+
		"\u011e\3\2\2\2\u011c\u011a\3\2\2\2\u011d\u0112\3\2\2\2\u011d\u011e\3\2"+
		"\2\2\u011e\u0128\3\2\2\2\u011f\u0120\7A\2\2\u0120\u0125\5\6\4\2\u0121"+
		"\u0122\7A\2\2\u0122\u0124\5\6\4\2\u0123\u0121\3\2\2\2\u0124\u0127\3\2"+
		"\2\2\u0125\u0123\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0129\3\2\2\2\u0127"+
		"\u0125\3\2\2\2\u0128\u011f\3\2\2\2\u0128\u0129\3\2\2\2\u0129\27\3\2\2"+
		"\2\u012a\u012b\7\r\2\2\u012b\u012c\5L\'\2\u012c\31\3\2\2\2\u012d\u012e"+
		"\7\16\2\2\u012e\u012f\7\4\2\2\u012f\u0132\5N(\2\u0130\u0131\7\5\2\2\u0131"+
		"\u0133\5N(\2\u0132\u0130\3\2\2\2\u0133\u0134\3\2\2\2\u0134\u0135\3\2\2"+
		"\2\u0134\u0132\3\2\2\2\u0135\u0136\3\2\2\2\u0136\u0137\7\6\2\2\u0137\33"+
		"\3\2\2\2\u0138\u0144\7 \2\2\u0139\u013a\7A\2\2\u013a\u0141\5\62\32\2\u013b"+
		"\u013d\7A\2\2\u013c\u013e\5\62\32\2\u013d\u013c\3\2\2\2\u013d\u013e\3"+
		"\2\2\2\u013e\u0140\3\2\2\2\u013f\u013b\3\2\2\2\u0140\u0143\3\2\2\2\u0141"+
		"\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142\u0145\3\2\2\2\u0143\u0141\3\2"+
		"\2\2\u0144\u0139\3\2\2\2\u0144\u0145\3\2\2\2\u0145\u0148\3\2\2\2\u0146"+
		"\u0147\7A\2\2\u0147\u0149\5B\"\2\u0148\u0146\3\2\2\2\u0148\u0149\3\2\2"+
		"\2\u0149\u014c\3\2\2\2\u014a\u014b\7A\2\2\u014b\u014d\5\30\r\2\u014c\u014a"+
		"\3\2\2\2\u014c\u014d\3\2\2\2\u014d\u0150\3\2\2\2\u014e\u014f\7A\2\2\u014f"+
		"\u0151\5\32\16\2\u0150\u014e\3\2\2\2\u0150\u0151\3\2\2\2\u0151\35\3\2"+
		"\2\2\u0152\u0153\7\31\2\2\u0153\u0154\5N(\2\u0154\37\3\2\2\2\u0155\u0156"+
		"\7\36\2\2\u0156\u0157\7\7\2\2\u0157\u0158\7!\2\2\u0158\u0162\5N(\2\u0159"+
		"\u015a\7A\2\2\u015a\u015f\5\"\22\2\u015b\u015c\7A\2\2\u015c\u015e\5\""+
		"\22\2\u015d\u015b\3\2\2\2\u015e\u0161\3\2\2\2\u015f\u015d\3\2\2\2\u015f"+
		"\u0160\3\2\2\2\u0160\u0163\3\2\2\2\u0161\u015f\3\2\2\2\u0162\u0159\3\2"+
		"\2\2\u0162\u0163\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0165\7\b\2\2\u0165"+
		"!\3\2\2\2\u0166\u016d\5$\23\2\u0167\u016d\5&\24\2\u0168\u016d\5(\25\2"+
		"\u0169\u016d\5*\26\2\u016a\u016d\5\60\31\2\u016b\u016d\5T+\2\u016c\u0166"+
		"\3\2\2\2\u016c\u0167\3\2\2\2\u016c\u0168\3\2\2\2\u016c\u0169\3\2\2\2\u016c"+
		"\u016a\3\2\2\2\u016c\u016b\3\2\2\2\u016d#\3\2\2\2\u016e\u016f\7\"\2\2"+
		"\u016f\u0170\7\61\2\2\u0170%\3\2\2\2\u0171\u0172\7#\2\2\u0172\u0173\7"+
		"\61\2\2\u0173\'\3\2\2\2\u0174\u0175\7(\2\2\u0175\u0176\7\62\2\2\u0176"+
		")\3\2\2\2\u0177\u0178\7)\2\2\u0178\u0179\7\62\2\2\u0179+\3\2\2\2\u017a"+
		"\u017b\7*\2\2\u017b\u017c\7\62\2\2\u017c-\3\2\2\2\u017d\u017e\7+\2\2\u017e"+
		"\u017f\7\62\2\2\u017f/\3\2\2\2\u0180\u0186\7\32\2\2\u0181\u0187\5N(\2"+
		"\u0182\u0183\7\7\2\2\u0183\u0184\5 \21\2\u0184\u0185\7\b\2\2\u0185\u0187"+
		"\3\2\2\2\u0186\u0181\3\2\2\2\u0186\u0182\3\2\2\2\u0187\61\3\2\2\2\u0188"+
		"\u018f\5:\36\2\u0189\u018f\5<\37\2\u018a\u018f\5> \2\u018b\u018f\5@!\2"+
		"\u018c\u018f\5\64\33\2\u018d\u018f\5\66\34\2\u018e\u0188\3\2\2\2\u018e"+
		"\u0189\3\2\2\2\u018e\u018a\3\2\2\2\u018e\u018b\3\2\2\2\u018e\u018c\3\2"+
		"\2\2\u018e\u018d\3\2\2\2\u018f\63\3\2\2\2\u0190\u0191\7\17\2\2\u0191\u0192"+
		"\t\2\2\2\u0192\65\3\2\2\2\u0193\u0194\7\23\2\2\u0194\u0195\7=\2\2\u0195"+
		"\67\3\2\2\2\u0196\u0198\7\24\2\2\u0197\u0196\3\2\2\2\u0197\u0198\3\2\2"+
		"\2\u0198\u0199\3\2\2\2\u0199\u019a\5N(\2\u019a9\3\2\2\2\u019b\u019c\7"+
		"$\2\2\u019c\u019d\7=\2\2\u019d;\3\2\2\2\u019e\u019f\7%\2\2\u019f\u01a0"+
		"\7=\2\2\u01a0=\3\2\2\2\u01a1\u01a2\7&\2\2\u01a2\u01a3\7=\2\2\u01a3?\3"+
		"\2\2\2\u01a4\u01a5\7\'\2\2\u01a5\u01a6\7=\2\2\u01a6A\3\2\2\2\u01a7\u01a8"+
		"\7,\2\2\u01a8\u01a9\5L\'\2\u01a9C\3\2\2\2\u01aa\u01ab\7-\2\2\u01ab\u01ac"+
		"\5L\'\2\u01acE\3\2\2\2\u01ad\u01ae\7.\2\2\u01ae\u01af\5L\'\2\u01afG\3"+
		"\2\2\2\u01b0\u01b1\7/\2\2\u01b1\u01b2\5N(\2\u01b2I\3\2\2\2\u01b3\u01b4"+
		"\7\60\2\2\u01b4\u01b5\5N(\2\u01b5K\3\2\2\2\u01b6\u01bb\5N(\2\u01b7\u01b8"+
		"\7\5\2\2\u01b8\u01ba\5L\'\2\u01b9\u01b7\3\2\2\2\u01ba\u01bd\3\2\2\2\u01bb"+
		"\u01b9\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\u01c0\3\2\2\2\u01bd\u01bb\3\2"+
		"\2\2\u01be\u01c0\5P)\2\u01bf\u01b6\3\2\2\2\u01bf\u01be\3\2\2\2\u01c0M"+
		"\3\2\2\2\u01c1\u01c2\t\3\2\2\u01c2O\3\2\2\2\u01c3\u01c4\t\4\2\2\u01c4"+
		"\u01c9\5R*\2\u01c5\u01c6\7A\2\2\u01c6\u01c8\5R*\2\u01c7\u01c5\3\2\2\2"+
		"\u01c8\u01cb\3\2\2\2\u01c9\u01c7\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca\u01cc"+
		"\3\2\2\2\u01cb\u01c9\3\2\2\2\u01cc\u01cd\t\5\2\2\u01cdQ\3\2\2\2\u01ce"+
		"\u01d0\5N(\2\u01cf\u01d1\7\f\2\2\u01d0\u01cf\3\2\2\2\u01d0\u01d1\3\2\2"+
		"\2\u01d1\u01d4\3\2\2\2\u01d2\u01d5\5L\'\2\u01d3\u01d5\5T+\2\u01d4\u01d2"+
		"\3\2\2\2\u01d4\u01d3\3\2\2\2\u01d5\u01d9\3\2\2\2\u01d6\u01d9\5$\23\2\u01d7"+
		"\u01d9\5&\24\2\u01d8\u01ce\3\2\2\2\u01d8\u01d6\3\2\2\2\u01d8\u01d7\3\2"+
		"\2\2\u01d9S\3\2\2\2\u01da\u01df\5Z.\2\u01db\u01df\5X-\2\u01dc\u01df\7"+
		"=\2\2\u01dd\u01df\5V,\2\u01de\u01da\3\2\2\2\u01de\u01db\3\2\2\2\u01de"+
		"\u01dc\3\2\2\2\u01de\u01dd\3\2\2\2\u01dfU\3\2\2\2\u01e0\u01e3\t\6\2\2"+
		"\u01e1\u01e4\5X-\2\u01e2\u01e4\7\62\2\2\u01e3\u01e1\3\2\2\2\u01e3\u01e2"+
		"\3\2\2\2\u01e4\u01eb\3\2\2\2\u01e5\u01e8\t\7\2\2\u01e6\u01e9\5X-\2\u01e7"+
		"\u01e9\7\62\2\2\u01e8\u01e6\3\2\2\2\u01e8\u01e7\3\2\2\2\u01e9\u01eb\3"+
		"\2\2\2\u01ea\u01e0\3\2\2\2\u01ea\u01e5\3\2\2\2\u01ebW\3\2\2\2\u01ec\u01ed"+
		"\7=\2\2\u01ed\u01ee\7\13\2\2\u01ee\u01ef\5N(\2\u01efY\3\2\2\2\u01f0\u01f3"+
		"\7\4\2\2\u01f1\u01f4\7=\2\2\u01f2\u01f4\5X-\2\u01f3\u01f1\3\2\2\2\u01f3"+
		"\u01f2\3\2\2\2\u01f4\u01fa\3\2\2\2\u01f5\u01f8\7\5\2\2\u01f6\u01f9\7="+
		"\2\2\u01f7\u01f9\5X-\2\u01f8\u01f6\3\2\2\2\u01f8\u01f7\3\2\2\2\u01f9\u01fb"+
		"\3\2\2\2\u01fa\u01f5\3\2\2\2\u01fb\u01fc\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fc"+
		"\u01fa\3\2\2\2\u01fd\u01fe\3\2\2\2\u01fe\u01ff\7\6\2\2\u01ff[\3\2\2\2"+
		"\u0200\u0201\7\4\2\2\u0201\u0204\5T+\2\u0202\u0203\7\5\2\2\u0203\u0205"+
		"\5T+\2\u0204\u0202\3\2\2\2\u0205\u0206\3\2\2\2\u0206\u0204\3\2\2\2\u0206"+
		"\u0207\3\2\2\2\u0207\u0208\3\2\2\2\u0208\u0209\7\6\2\2\u0209]\3\2\2\2"+
		"Dfju|\u0080\u0083\u0089\u008d\u0090\u0097\u009b\u009e\u00a5\u00a9\u00ac"+
		"\u00b0\u00be\u00c2\u00c5\u00cb\u00cf\u00d2\u00da\u00dd\u00e4\u00e8\u00eb"+
		"\u00f3\u00f6\u00fd\u0101\u0104\u010c\u010f\u0116\u011a\u011d\u0125\u0128"+
		"\u0134\u013d\u0141\u0144\u0148\u014c\u0150\u015f\u0162\u016c\u0186\u018e"+
		"\u0197\u01bb\u01bf\u01c9\u01d0\u01d4\u01d8\u01de\u01e3\u01e8\u01ea\u01f3"+
		"\u01f8\u01fc\u0206";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}