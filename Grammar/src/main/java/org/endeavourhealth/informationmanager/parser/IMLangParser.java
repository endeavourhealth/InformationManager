// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\IMLang.g4 by ANTLR 4.9.1
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
		VERSION=17, IRI_LABEL=18, TYPE=19, TERM=20, RECORD=21, TARGETCLASS=22, 
		CLASS=23, OBJECTPROPERTY=24, DATAPROPERTY=25, ANNOTATION=26, PROPERTYCONSTRAINT=27, 
		DATATYPE=28, VALUESET=29, PATH=30, MINCOUNT=31, MAXCOUNT=32, NAME=33, 
		DESCRIPTION=34, CODE=35, SCHEME=36, MININCLUSIVE=37, MAXINCLUSIVE=38, 
		MINEXCLUSIVE=39, MAXEXCLUSIVE=40, SUBCLASS=41, EQUIVALENTTO=42, DISJOINT=43, 
		SUBPROPERTY=44, INVERSE=45, INTEGER=46, DOUBLE=47, DIGIT=48, EXACT=49, 
		AND=50, OR=51, PREFIXIRI=52, IRIREF=53, LOWERCASE=54, UPPERCASE=55, PLX=56, 
		PERCENT=57, QUOTED_STRING=58, HEX=59, PN_LOCAL_ESC=60, WS=61, SC=62;
	public static final int
		RULE_concept = 0, RULE_classAxiom = 1, RULE_propertyAxiom = 2, RULE_type = 3, 
		RULE_classType = 4, RULE_dataType = 5, RULE_recordType = 6, RULE_objectProperty = 7, 
		RULE_dataProperty = 8, RULE_annotationProperty = 9, RULE_members = 10, 
		RULE_expansion = 11, RULE_valueSet = 12, RULE_shapeOf = 13, RULE_propertyConstraint = 14, 
		RULE_constraintParameter = 15, RULE_minCount = 16, RULE_maxCount = 17, 
		RULE_minInclusive = 18, RULE_maxInclusive = 19, RULE_minExclusive = 20, 
		RULE_maxExclusive = 21, RULE_classValue = 22, RULE_label = 23, RULE_status = 24, 
		RULE_version = 25, RULE_identifierIri = 26, RULE_name = 27, RULE_description = 28, 
		RULE_code = 29, RULE_scheme = 30, RULE_subclassOf = 31, RULE_equivalentTo = 32, 
		RULE_disjointWith = 33, RULE_subpropertyOf = 34, RULE_inverseOf = 35, 
		RULE_classExpression = 36, RULE_iri = 37, RULE_roleGroup = 38, RULE_role = 39, 
		RULE_dataRange = 40, RULE_rangeValue = 41, RULE_typedString = 42, RULE_valueCollection = 43, 
		RULE_dataRangeCollection = 44;
	private static String[] makeRuleNames() {
		return new String[] {
			"concept", "classAxiom", "propertyAxiom", "type", "classType", "dataType", 
			"recordType", "objectProperty", "dataProperty", "annotationProperty", 
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
			null, null, null, null, null, "'or'", null, null, null, null, null, null, 
			null, null, null, null, "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "EQ", "MEMBER", 
			"EXPANSION", "STATUS", "ACTIVE", "INACTIVE", "DRAFT", "VERSION", "IRI_LABEL", 
			"TYPE", "TERM", "RECORD", "TARGETCLASS", "CLASS", "OBJECTPROPERTY", "DATAPROPERTY", 
			"ANNOTATION", "PROPERTYCONSTRAINT", "DATATYPE", "VALUESET", "PATH", "MINCOUNT", 
			"MAXCOUNT", "NAME", "DESCRIPTION", "CODE", "SCHEME", "MININCLUSIVE", 
			"MAXINCLUSIVE", "MINEXCLUSIVE", "MAXEXCLUSIVE", "SUBCLASS", "EQUIVALENTTO", 
			"DISJOINT", "SUBPROPERTY", "INVERSE", "INTEGER", "DOUBLE", "DIGIT", "EXACT", 
			"AND", "OR", "PREFIXIRI", "IRIREF", "LOWERCASE", "UPPERCASE", "PLX", 
			"PERCENT", "QUOTED_STRING", "HEX", "PN_LOCAL_ESC", "WS", "SC"
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
			setState(90);
			identifierIri();
			setState(91);
			type();
			setState(92);
			match(T__0);
			setState(93);
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
			setState(98);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBCLASS:
				enterOuterAlt(_localctx, 1);
				{
				setState(95);
				subclassOf();
				}
				break;
			case EQUIVALENTTO:
				enterOuterAlt(_localctx, 2);
				{
				setState(96);
				equivalentTo();
				}
				break;
			case DISJOINT:
				enterOuterAlt(_localctx, 3);
				{
				setState(97);
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
			setState(102);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBPROPERTY:
				enterOuterAlt(_localctx, 1);
				{
				setState(100);
				subpropertyOf();
				}
				break;
			case INVERSE:
				enterOuterAlt(_localctx, 2);
				{
				setState(101);
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
			setState(104);
			match(TYPE);
			setState(111);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CLASS:
				{
				setState(105);
				classType();
				}
				break;
			case DATATYPE:
				{
				setState(106);
				dataType();
				}
				break;
			case RECORD:
				{
				setState(107);
				recordType();
				}
				break;
			case OBJECTPROPERTY:
				{
				setState(108);
				objectProperty();
				}
				break;
			case ANNOTATION:
				{
				setState(109);
				annotationProperty();
				}
				break;
			case DATAPROPERTY:
				{
				setState(110);
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
			setState(113);
			match(CLASS);
			setState(125);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(114);
				match(SC);
				setState(115);
				label();
				setState(122);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(116);
						match(SC);
						setState(118);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(117);
							label();
							}
						}

						}
						} 
					}
					setState(124);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				}
				}
				break;
			}
			setState(138);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(127);
				match(SC);
				setState(128);
				classAxiom();
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(129);
					match(SC);
					setState(131);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SUBCLASS) | (1L << EQUIVALENTTO) | (1L << DISJOINT))) != 0)) {
						{
						setState(130);
						classAxiom();
						}
					}

					}
					}
					setState(137);
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
			setState(140);
			match(DATATYPE);
			setState(152);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(141);
				match(SC);
				setState(142);
				label();
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(143);
					match(SC);
					setState(145);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
						{
						setState(144);
						label();
						}
					}

					}
					}
					setState(151);
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

	public static class RecordTypeContext extends ParserRuleContext {
		public TerminalNode RECORD() { return getToken(IMLangParser.RECORD, 0); }
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
		enterRule(_localctx, 12, RULE_recordType);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
			match(RECORD);
			setState(166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(155);
				match(SC);
				setState(156);
				label();
				setState(163);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(157);
						match(SC);
						setState(159);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(158);
							label();
							}
						}

						}
						} 
					}
					setState(165);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				}
				}
				break;
			}
			setState(179);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(168);
				match(SC);
				setState(169);
				classAxiom();
				setState(176);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(170);
						match(SC);
						setState(172);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SUBCLASS) | (1L << EQUIVALENTTO) | (1L << DISJOINT))) != 0)) {
							{
							setState(171);
							classAxiom();
							}
						}

						}
						} 
					}
					setState(178);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
				}
				}
				break;
			}
			setState(190);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(181);
				match(SC);
				setState(182);
				role();
				setState(187);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(183);
					match(SC);
					setState(184);
					role();
					}
					}
					setState(189);
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
		enterRule(_localctx, 14, RULE_objectProperty);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			match(OBJECTPROPERTY);
			setState(204);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(193);
				match(SC);
				setState(194);
				label();
				setState(201);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(195);
						match(SC);
						setState(197);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(196);
							label();
							}
						}

						}
						} 
					}
					setState(203);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				}
				}
				break;
			}
			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(206);
				match(SC);
				setState(207);
				propertyAxiom();
				setState(212);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(208);
					match(SC);
					setState(209);
					propertyAxiom();
					}
					}
					setState(214);
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
		enterRule(_localctx, 16, RULE_dataProperty);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(217);
			match(DATAPROPERTY);
			setState(229);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(218);
				match(SC);
				setState(219);
				label();
				setState(226);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(220);
						match(SC);
						setState(222);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(221);
							label();
							}
						}

						}
						} 
					}
					setState(228);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
				}
				}
				break;
			}
			setState(240);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(231);
				match(SC);
				setState(232);
				propertyAxiom();
				setState(237);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(233);
					match(SC);
					setState(234);
					propertyAxiom();
					}
					}
					setState(239);
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
		enterRule(_localctx, 18, RULE_annotationProperty);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			match(ANNOTATION);
			setState(254);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(243);
				match(SC);
				setState(244);
				label();
				setState(251);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(245);
						match(SC);
						setState(247);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(246);
							label();
							}
						}

						}
						} 
					}
					setState(253);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				}
				}
				break;
			}
			setState(265);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(256);
				match(SC);
				setState(257);
				propertyAxiom();
				setState(262);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(258);
					match(SC);
					setState(259);
					propertyAxiom();
					}
					}
					setState(264);
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
		enterRule(_localctx, 20, RULE_members);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			match(MEMBER);
			setState(268);
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
		enterRule(_localctx, 22, RULE_expansion);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			match(EXPANSION);
			setState(271);
			match(T__1);
			setState(272);
			iri();
			setState(275); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(273);
					match(T__2);
					setState(274);
					iri();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(277); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,35,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(279);
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
		enterRule(_localctx, 24, RULE_valueSet);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			match(VALUESET);
			setState(293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				{
				setState(282);
				match(SC);
				setState(283);
				label();
				setState(290);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(284);
						match(SC);
						setState(286);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(285);
							label();
							}
						}

						}
						} 
					}
					setState(292);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
				}
				}
				break;
			}
			setState(297);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				{
				setState(295);
				match(SC);
				setState(296);
				subclassOf();
				}
				break;
			}
			setState(301);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(299);
				match(SC);
				setState(300);
				members();
				}
				break;
			}
			setState(305);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(303);
				match(SC);
				setState(304);
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
		enterRule(_localctx, 26, RULE_shapeOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(307);
			match(TARGETCLASS);
			}
			setState(308);
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
		enterRule(_localctx, 28, RULE_propertyConstraint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			match(PROPERTYCONSTRAINT);
			setState(311);
			match(T__4);
			setState(312);
			match(PATH);
			setState(313);
			iri();
			setState(323);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(314);
				match(SC);
				setState(315);
				constraintParameter();
				setState(320);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(316);
					match(SC);
					setState(317);
					constraintParameter();
					}
					}
					setState(322);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(325);
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
		enterRule(_localctx, 30, RULE_constraintParameter);
		try {
			setState(333);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(327);
				minCount();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(328);
				maxCount();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(329);
				minInclusive();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(330);
				maxInclusive();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(331);
				classValue();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(332);
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
		enterRule(_localctx, 32, RULE_minCount);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(MINCOUNT);
			setState(336);
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
		enterRule(_localctx, 34, RULE_maxCount);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(338);
			match(MAXCOUNT);
			setState(339);
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
		enterRule(_localctx, 36, RULE_minInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(341);
			match(MININCLUSIVE);
			setState(342);
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
		enterRule(_localctx, 38, RULE_maxInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(344);
			match(MAXINCLUSIVE);
			setState(345);
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
		enterRule(_localctx, 40, RULE_minExclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(347);
			match(MINEXCLUSIVE);
			setState(348);
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
		enterRule(_localctx, 42, RULE_maxExclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(350);
			match(MAXEXCLUSIVE);
			setState(351);
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
		enterRule(_localctx, 44, RULE_classValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353);
			match(CLASS);
			setState(359);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREFIXIRI:
			case IRIREF:
			case QUOTED_STRING:
				{
				setState(354);
				iri();
				}
				break;
			case T__4:
				{
				setState(355);
				match(T__4);
				setState(356);
				propertyConstraint();
				setState(357);
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
		enterRule(_localctx, 46, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAME:
				{
				setState(361);
				name();
				}
				break;
			case DESCRIPTION:
				{
				setState(362);
				description();
				}
				break;
			case CODE:
				{
				setState(363);
				code();
				}
				break;
			case SCHEME:
				{
				setState(364);
				scheme();
				}
				break;
			case STATUS:
				{
				setState(365);
				status();
				}
				break;
			case VERSION:
				{
				setState(366);
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
		enterRule(_localctx, 48, RULE_status);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(369);
			match(STATUS);
			setState(370);
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
		enterRule(_localctx, 50, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			match(VERSION);
			setState(373);
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
		enterRule(_localctx, 52, RULE_identifierIri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(376);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRI_LABEL) {
				{
				setState(375);
				match(IRI_LABEL);
				}
			}

			setState(378);
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
		enterRule(_localctx, 54, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(380);
			match(NAME);
			setState(381);
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
		enterRule(_localctx, 56, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			match(DESCRIPTION);
			setState(384);
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
		enterRule(_localctx, 58, RULE_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(386);
			match(CODE);
			setState(387);
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
		enterRule(_localctx, 60, RULE_scheme);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			match(SCHEME);
			setState(390);
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
		enterRule(_localctx, 62, RULE_subclassOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			match(SUBCLASS);
			setState(393);
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
		enterRule(_localctx, 64, RULE_equivalentTo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			match(EQUIVALENTTO);
			setState(396);
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
		enterRule(_localctx, 66, RULE_disjointWith);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			match(DISJOINT);
			setState(399);
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
		enterRule(_localctx, 68, RULE_subpropertyOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(SUBPROPERTY);
			setState(402);
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
		enterRule(_localctx, 70, RULE_inverseOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(404);
			match(INVERSE);
			setState(405);
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
		enterRule(_localctx, 72, RULE_classExpression);
		try {
			int _alt;
			setState(416);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREFIXIRI:
			case IRIREF:
			case QUOTED_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(407);
				iri();
				setState(412);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(408);
						match(T__2);
						setState(409);
						classExpression();
						}
						} 
					}
					setState(414);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,48,_ctx);
				}
				}
				break;
			case T__4:
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(415);
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
		enterRule(_localctx, 74, RULE_iri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(418);
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
		enterRule(_localctx, 76, RULE_roleGroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(420);
			_la = _input.LA(1);
			if ( !(_la==T__4 || _la==T__6) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(421);
			role();
			setState(426);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SC) {
				{
				{
				setState(422);
				match(SC);
				setState(423);
				role();
				}
				}
				setState(428);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(429);
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
		enterRule(_localctx, 78, RULE_role);
		int _la;
		try {
			setState(441);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREFIXIRI:
			case IRIREF:
			case QUOTED_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(431);
				iri();
				setState(433);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EQ) {
					{
					setState(432);
					match(EQ);
					}
				}

				setState(437);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
				case 1:
					{
					setState(435);
					classExpression();
					}
					break;
				case 2:
					{
					setState(436);
					dataRange();
					}
					break;
				}
				}
				break;
			case MINCOUNT:
				enterOuterAlt(_localctx, 2);
				{
				setState(439);
				minCount();
				}
				break;
			case MAXCOUNT:
				enterOuterAlt(_localctx, 3);
				{
				setState(440);
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
		enterRule(_localctx, 80, RULE_dataRange);
		try {
			setState(447);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,54,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(443);
				valueCollection();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(444);
				typedString();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(445);
				match(QUOTED_STRING);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(446);
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
		enterRule(_localctx, 82, RULE_rangeValue);
		int _la;
		try {
			setState(459);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MININCLUSIVE:
			case MINEXCLUSIVE:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(449);
				_la = _input.LA(1);
				if ( !(_la==MININCLUSIVE || _la==MINEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(452);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUOTED_STRING:
					{
					setState(450);
					typedString();
					}
					break;
				case DOUBLE:
					{
					setState(451);
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
				setState(454);
				_la = _input.LA(1);
				if ( !(_la==MAXINCLUSIVE || _la==MAXEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(457);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUOTED_STRING:
					{
					setState(455);
					typedString();
					}
					break;
				case DOUBLE:
					{
					setState(456);
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
		enterRule(_localctx, 84, RULE_typedString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(461);
			match(QUOTED_STRING);
			setState(462);
			match(T__8);
			setState(463);
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
		enterRule(_localctx, 86, RULE_valueCollection);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(465);
			match(T__1);
			setState(468);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,58,_ctx) ) {
			case 1:
				{
				setState(466);
				match(QUOTED_STRING);
				}
				break;
			case 2:
				{
				setState(467);
				typedString();
				}
				break;
			}
			setState(475); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(470);
					match(T__2);
					setState(473);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,59,_ctx) ) {
					case 1:
						{
						setState(471);
						match(QUOTED_STRING);
						}
						break;
					case 2:
						{
						setState(472);
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
				setState(477); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,60,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(479);
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
		enterRule(_localctx, 88, RULE_dataRangeCollection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(481);
			match(T__1);
			setState(482);
			dataRange();
			setState(485); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(483);
				match(T__2);
				setState(484);
				dataRange();
				}
				}
				setState(487); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__2 );
			setState(489);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3@\u01ee\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\5\3e\n\3\3\4\3\4\5\4"+
		"i\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5r\n\5\3\6\3\6\3\6\3\6\3\6\5\6y\n"+
		"\6\7\6{\n\6\f\6\16\6~\13\6\5\6\u0080\n\6\3\6\3\6\3\6\3\6\5\6\u0086\n\6"+
		"\7\6\u0088\n\6\f\6\16\6\u008b\13\6\5\6\u008d\n\6\3\7\3\7\3\7\3\7\3\7\5"+
		"\7\u0094\n\7\7\7\u0096\n\7\f\7\16\7\u0099\13\7\5\7\u009b\n\7\3\b\3\b\3"+
		"\b\3\b\3\b\5\b\u00a2\n\b\7\b\u00a4\n\b\f\b\16\b\u00a7\13\b\5\b\u00a9\n"+
		"\b\3\b\3\b\3\b\3\b\5\b\u00af\n\b\7\b\u00b1\n\b\f\b\16\b\u00b4\13\b\5\b"+
		"\u00b6\n\b\3\b\3\b\3\b\3\b\7\b\u00bc\n\b\f\b\16\b\u00bf\13\b\5\b\u00c1"+
		"\n\b\3\t\3\t\3\t\3\t\3\t\5\t\u00c8\n\t\7\t\u00ca\n\t\f\t\16\t\u00cd\13"+
		"\t\5\t\u00cf\n\t\3\t\3\t\3\t\3\t\7\t\u00d5\n\t\f\t\16\t\u00d8\13\t\5\t"+
		"\u00da\n\t\3\n\3\n\3\n\3\n\3\n\5\n\u00e1\n\n\7\n\u00e3\n\n\f\n\16\n\u00e6"+
		"\13\n\5\n\u00e8\n\n\3\n\3\n\3\n\3\n\7\n\u00ee\n\n\f\n\16\n\u00f1\13\n"+
		"\5\n\u00f3\n\n\3\13\3\13\3\13\3\13\3\13\5\13\u00fa\n\13\7\13\u00fc\n\13"+
		"\f\13\16\13\u00ff\13\13\5\13\u0101\n\13\3\13\3\13\3\13\3\13\7\13\u0107"+
		"\n\13\f\13\16\13\u010a\13\13\5\13\u010c\n\13\3\f\3\f\3\f\3\r\3\r\3\r\3"+
		"\r\3\r\6\r\u0116\n\r\r\r\16\r\u0117\3\r\3\r\3\16\3\16\3\16\3\16\3\16\5"+
		"\16\u0121\n\16\7\16\u0123\n\16\f\16\16\16\u0126\13\16\5\16\u0128\n\16"+
		"\3\16\3\16\5\16\u012c\n\16\3\16\3\16\5\16\u0130\n\16\3\16\3\16\5\16\u0134"+
		"\n\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\7\20\u0141"+
		"\n\20\f\20\16\20\u0144\13\20\5\20\u0146\n\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\5\21\u0150\n\21\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24"+
		"\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\5\30\u016a\n\30\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0172\n"+
		"\31\3\32\3\32\3\32\3\33\3\33\3\33\3\34\5\34\u017b\n\34\3\34\3\34\3\35"+
		"\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\3!\3\"\3\"\3\""+
		"\3#\3#\3#\3$\3$\3$\3%\3%\3%\3&\3&\3&\7&\u019d\n&\f&\16&\u01a0\13&\3&\5"+
		"&\u01a3\n&\3\'\3\'\3(\3(\3(\3(\7(\u01ab\n(\f(\16(\u01ae\13(\3(\3(\3)\3"+
		")\5)\u01b4\n)\3)\3)\5)\u01b8\n)\3)\3)\5)\u01bc\n)\3*\3*\3*\3*\5*\u01c2"+
		"\n*\3+\3+\3+\5+\u01c7\n+\3+\3+\3+\5+\u01cc\n+\5+\u01ce\n+\3,\3,\3,\3,"+
		"\3-\3-\3-\5-\u01d7\n-\3-\3-\3-\5-\u01dc\n-\6-\u01de\n-\r-\16-\u01df\3"+
		"-\3-\3.\3.\3.\3.\6.\u01e8\n.\r.\16.\u01e9\3.\3.\3.\4\u0117\u01df\2/\2"+
		"\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJL"+
		"NPRTVXZ\2\b\3\2\20\22\4\2\66\67<<\4\2\7\7\t\t\4\2\b\b\n\n\4\2\'\'))\4"+
		"\2((**\2\u020e\2\\\3\2\2\2\4d\3\2\2\2\6h\3\2\2\2\bj\3\2\2\2\ns\3\2\2\2"+
		"\f\u008e\3\2\2\2\16\u009c\3\2\2\2\20\u00c2\3\2\2\2\22\u00db\3\2\2\2\24"+
		"\u00f4\3\2\2\2\26\u010d\3\2\2\2\30\u0110\3\2\2\2\32\u011b\3\2\2\2\34\u0135"+
		"\3\2\2\2\36\u0138\3\2\2\2 \u014f\3\2\2\2\"\u0151\3\2\2\2$\u0154\3\2\2"+
		"\2&\u0157\3\2\2\2(\u015a\3\2\2\2*\u015d\3\2\2\2,\u0160\3\2\2\2.\u0163"+
		"\3\2\2\2\60\u0171\3\2\2\2\62\u0173\3\2\2\2\64\u0176\3\2\2\2\66\u017a\3"+
		"\2\2\28\u017e\3\2\2\2:\u0181\3\2\2\2<\u0184\3\2\2\2>\u0187\3\2\2\2@\u018a"+
		"\3\2\2\2B\u018d\3\2\2\2D\u0190\3\2\2\2F\u0193\3\2\2\2H\u0196\3\2\2\2J"+
		"\u01a2\3\2\2\2L\u01a4\3\2\2\2N\u01a6\3\2\2\2P\u01bb\3\2\2\2R\u01c1\3\2"+
		"\2\2T\u01cd\3\2\2\2V\u01cf\3\2\2\2X\u01d3\3\2\2\2Z\u01e3\3\2\2\2\\]\5"+
		"\66\34\2]^\5\b\5\2^_\7\3\2\2_`\7\2\2\3`\3\3\2\2\2ae\5@!\2be\5B\"\2ce\5"+
		"D#\2da\3\2\2\2db\3\2\2\2dc\3\2\2\2e\5\3\2\2\2fi\5F$\2gi\5H%\2hf\3\2\2"+
		"\2hg\3\2\2\2i\7\3\2\2\2jq\7\25\2\2kr\5\n\6\2lr\5\f\7\2mr\5\16\b\2nr\5"+
		"\20\t\2or\5\24\13\2pr\5\22\n\2qk\3\2\2\2ql\3\2\2\2qm\3\2\2\2qn\3\2\2\2"+
		"qo\3\2\2\2qp\3\2\2\2r\t\3\2\2\2s\177\7\31\2\2tu\7@\2\2u|\5\60\31\2vx\7"+
		"@\2\2wy\5\60\31\2xw\3\2\2\2xy\3\2\2\2y{\3\2\2\2zv\3\2\2\2{~\3\2\2\2|z"+
		"\3\2\2\2|}\3\2\2\2}\u0080\3\2\2\2~|\3\2\2\2\177t\3\2\2\2\177\u0080\3\2"+
		"\2\2\u0080\u008c\3\2\2\2\u0081\u0082\7@\2\2\u0082\u0089\5\4\3\2\u0083"+
		"\u0085\7@\2\2\u0084\u0086\5\4\3\2\u0085\u0084\3\2\2\2\u0085\u0086\3\2"+
		"\2\2\u0086\u0088\3\2\2\2\u0087\u0083\3\2\2\2\u0088\u008b\3\2\2\2\u0089"+
		"\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008d\3\2\2\2\u008b\u0089\3\2"+
		"\2\2\u008c\u0081\3\2\2\2\u008c\u008d\3\2\2\2\u008d\13\3\2\2\2\u008e\u009a"+
		"\7\36\2\2\u008f\u0090\7@\2\2\u0090\u0097\5\60\31\2\u0091\u0093\7@\2\2"+
		"\u0092\u0094\5\60\31\2\u0093\u0092\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0096"+
		"\3\2\2\2\u0095\u0091\3\2\2\2\u0096\u0099\3\2\2\2\u0097\u0095\3\2\2\2\u0097"+
		"\u0098\3\2\2\2\u0098\u009b\3\2\2\2\u0099\u0097\3\2\2\2\u009a\u008f\3\2"+
		"\2\2\u009a\u009b\3\2\2\2\u009b\r\3\2\2\2\u009c\u00a8\7\27\2\2\u009d\u009e"+
		"\7@\2\2\u009e\u00a5\5\60\31\2\u009f\u00a1\7@\2\2\u00a0\u00a2\5\60\31\2"+
		"\u00a1\u00a0\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a4\3\2\2\2\u00a3\u009f"+
		"\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6"+
		"\u00a9\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\u009d\3\2\2\2\u00a8\u00a9\3\2"+
		"\2\2\u00a9\u00b5\3\2\2\2\u00aa\u00ab\7@\2\2\u00ab\u00b2\5\4\3\2\u00ac"+
		"\u00ae\7@\2\2\u00ad\u00af\5\4\3\2\u00ae\u00ad\3\2\2\2\u00ae\u00af\3\2"+
		"\2\2\u00af\u00b1\3\2\2\2\u00b0\u00ac\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2"+
		"\u00b0\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\u00b6\3\2\2\2\u00b4\u00b2\3\2"+
		"\2\2\u00b5\u00aa\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00c0\3\2\2\2\u00b7"+
		"\u00b8\7@\2\2\u00b8\u00bd\5P)\2\u00b9\u00ba\7@\2\2\u00ba\u00bc\5P)\2\u00bb"+
		"\u00b9\3\2\2\2\u00bc\u00bf\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2"+
		"\2\2\u00be\u00c1\3\2\2\2\u00bf\u00bd\3\2\2\2\u00c0\u00b7\3\2\2\2\u00c0"+
		"\u00c1\3\2\2\2\u00c1\17\3\2\2\2\u00c2\u00ce\7\32\2\2\u00c3\u00c4\7@\2"+
		"\2\u00c4\u00cb\5\60\31\2\u00c5\u00c7\7@\2\2\u00c6\u00c8\5\60\31\2\u00c7"+
		"\u00c6\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00ca\3\2\2\2\u00c9\u00c5\3\2"+
		"\2\2\u00ca\u00cd\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc"+
		"\u00cf\3\2\2\2\u00cd\u00cb\3\2\2\2\u00ce\u00c3\3\2\2\2\u00ce\u00cf\3\2"+
		"\2\2\u00cf\u00d9\3\2\2\2\u00d0\u00d1\7@\2\2\u00d1\u00d6\5\6\4\2\u00d2"+
		"\u00d3\7@\2\2\u00d3\u00d5\5\6\4\2\u00d4\u00d2\3\2\2\2\u00d5\u00d8\3\2"+
		"\2\2\u00d6\u00d4\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8"+
		"\u00d6\3\2\2\2\u00d9\u00d0\3\2\2\2\u00d9\u00da\3\2\2\2\u00da\21\3\2\2"+
		"\2\u00db\u00e7\7\33\2\2\u00dc\u00dd\7@\2\2\u00dd\u00e4\5\60\31\2\u00de"+
		"\u00e0\7@\2\2\u00df\u00e1\5\60\31\2\u00e0\u00df\3\2\2\2\u00e0\u00e1\3"+
		"\2\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00de\3\2\2\2\u00e3\u00e6\3\2\2\2\u00e4"+
		"\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2"+
		"\2\2\u00e7\u00dc\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00f2\3\2\2\2\u00e9"+
		"\u00ea\7@\2\2\u00ea\u00ef\5\6\4\2\u00eb\u00ec\7@\2\2\u00ec\u00ee\5\6\4"+
		"\2\u00ed\u00eb\3\2\2\2\u00ee\u00f1\3\2\2\2\u00ef\u00ed\3\2\2\2\u00ef\u00f0"+
		"\3\2\2\2\u00f0\u00f3\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f2\u00e9\3\2\2\2\u00f2"+
		"\u00f3\3\2\2\2\u00f3\23\3\2\2\2\u00f4\u0100\7\34\2\2\u00f5\u00f6\7@\2"+
		"\2\u00f6\u00fd\5\60\31\2\u00f7\u00f9\7@\2\2\u00f8\u00fa\5\60\31\2\u00f9"+
		"\u00f8\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fc\3\2\2\2\u00fb\u00f7\3\2"+
		"\2\2\u00fc\u00ff\3\2\2\2\u00fd\u00fb\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe"+
		"\u0101\3\2\2\2\u00ff\u00fd\3\2\2\2\u0100\u00f5\3\2\2\2\u0100\u0101\3\2"+
		"\2\2\u0101\u010b\3\2\2\2\u0102\u0103\7@\2\2\u0103\u0108\5\6\4\2\u0104"+
		"\u0105\7@\2\2\u0105\u0107\5\6\4\2\u0106\u0104\3\2\2\2\u0107\u010a\3\2"+
		"\2\2\u0108\u0106\3\2\2\2\u0108\u0109\3\2\2\2\u0109\u010c\3\2\2\2\u010a"+
		"\u0108\3\2\2\2\u010b\u0102\3\2\2\2\u010b\u010c\3\2\2\2\u010c\25\3\2\2"+
		"\2\u010d\u010e\7\r\2\2\u010e\u010f\5J&\2\u010f\27\3\2\2\2\u0110\u0111"+
		"\7\16\2\2\u0111\u0112\7\4\2\2\u0112\u0115\5L\'\2\u0113\u0114\7\5\2\2\u0114"+
		"\u0116\5L\'\2\u0115\u0113\3\2\2\2\u0116\u0117\3\2\2\2\u0117\u0118\3\2"+
		"\2\2\u0117\u0115\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u011a\7\6\2\2\u011a"+
		"\31\3\2\2\2\u011b\u0127\7\37\2\2\u011c\u011d\7@\2\2\u011d\u0124\5\60\31"+
		"\2\u011e\u0120\7@\2\2\u011f\u0121\5\60\31\2\u0120\u011f\3\2\2\2\u0120"+
		"\u0121\3\2\2\2\u0121\u0123\3\2\2\2\u0122\u011e\3\2\2\2\u0123\u0126\3\2"+
		"\2\2\u0124\u0122\3\2\2\2\u0124\u0125\3\2\2\2\u0125\u0128\3\2\2\2\u0126"+
		"\u0124\3\2\2\2\u0127\u011c\3\2\2\2\u0127\u0128\3\2\2\2\u0128\u012b\3\2"+
		"\2\2\u0129\u012a\7@\2\2\u012a\u012c\5@!\2\u012b\u0129\3\2\2\2\u012b\u012c"+
		"\3\2\2\2\u012c\u012f\3\2\2\2\u012d\u012e\7@\2\2\u012e\u0130\5\26\f\2\u012f"+
		"\u012d\3\2\2\2\u012f\u0130\3\2\2\2\u0130\u0133\3\2\2\2\u0131\u0132\7@"+
		"\2\2\u0132\u0134\5\30\r\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134"+
		"\33\3\2\2\2\u0135\u0136\7\30\2\2\u0136\u0137\5L\'\2\u0137\35\3\2\2\2\u0138"+
		"\u0139\7\35\2\2\u0139\u013a\7\7\2\2\u013a\u013b\7 \2\2\u013b\u0145\5L"+
		"\'\2\u013c\u013d\7@\2\2\u013d\u0142\5 \21\2\u013e\u013f\7@\2\2\u013f\u0141"+
		"\5 \21\2\u0140\u013e\3\2\2\2\u0141\u0144\3\2\2\2\u0142\u0140\3\2\2\2\u0142"+
		"\u0143\3\2\2\2\u0143\u0146\3\2\2\2\u0144\u0142\3\2\2\2\u0145\u013c\3\2"+
		"\2\2\u0145\u0146\3\2\2\2\u0146\u0147\3\2\2\2\u0147\u0148\7\b\2\2\u0148"+
		"\37\3\2\2\2\u0149\u0150\5\"\22\2\u014a\u0150\5$\23\2\u014b\u0150\5&\24"+
		"\2\u014c\u0150\5(\25\2\u014d\u0150\5.\30\2\u014e\u0150\5R*\2\u014f\u0149"+
		"\3\2\2\2\u014f\u014a\3\2\2\2\u014f\u014b\3\2\2\2\u014f\u014c\3\2\2\2\u014f"+
		"\u014d\3\2\2\2\u014f\u014e\3\2\2\2\u0150!\3\2\2\2\u0151\u0152\7!\2\2\u0152"+
		"\u0153\7\60\2\2\u0153#\3\2\2\2\u0154\u0155\7\"\2\2\u0155\u0156\7\60\2"+
		"\2\u0156%\3\2\2\2\u0157\u0158\7\'\2\2\u0158\u0159\7\61\2\2\u0159\'\3\2"+
		"\2\2\u015a\u015b\7(\2\2\u015b\u015c\7\61\2\2\u015c)\3\2\2\2\u015d\u015e"+
		"\7)\2\2\u015e\u015f\7\61\2\2\u015f+\3\2\2\2\u0160\u0161\7*\2\2\u0161\u0162"+
		"\7\61\2\2\u0162-\3\2\2\2\u0163\u0169\7\31\2\2\u0164\u016a\5L\'\2\u0165"+
		"\u0166\7\7\2\2\u0166\u0167\5\36\20\2\u0167\u0168\7\b\2\2\u0168\u016a\3"+
		"\2\2\2\u0169\u0164\3\2\2\2\u0169\u0165\3\2\2\2\u016a/\3\2\2\2\u016b\u0172"+
		"\58\35\2\u016c\u0172\5:\36\2\u016d\u0172\5<\37\2\u016e\u0172\5> \2\u016f"+
		"\u0172\5\62\32\2\u0170\u0172\5\64\33\2\u0171\u016b\3\2\2\2\u0171\u016c"+
		"\3\2\2\2\u0171\u016d\3\2\2\2\u0171\u016e\3\2\2\2\u0171\u016f\3\2\2\2\u0171"+
		"\u0170\3\2\2\2\u0172\61\3\2\2\2\u0173\u0174\7\17\2\2\u0174\u0175\t\2\2"+
		"\2\u0175\63\3\2\2\2\u0176\u0177\7\23\2\2\u0177\u0178\7<\2\2\u0178\65\3"+
		"\2\2\2\u0179\u017b\7\24\2\2\u017a\u0179\3\2\2\2\u017a\u017b\3\2\2\2\u017b"+
		"\u017c\3\2\2\2\u017c\u017d\5L\'\2\u017d\67\3\2\2\2\u017e\u017f\7#\2\2"+
		"\u017f\u0180\7<\2\2\u01809\3\2\2\2\u0181\u0182\7$\2\2\u0182\u0183\7<\2"+
		"\2\u0183;\3\2\2\2\u0184\u0185\7%\2\2\u0185\u0186\7<\2\2\u0186=\3\2\2\2"+
		"\u0187\u0188\7&\2\2\u0188\u0189\7<\2\2\u0189?\3\2\2\2\u018a\u018b\7+\2"+
		"\2\u018b\u018c\5J&\2\u018cA\3\2\2\2\u018d\u018e\7,\2\2\u018e\u018f\5J"+
		"&\2\u018fC\3\2\2\2\u0190\u0191\7-\2\2\u0191\u0192\5J&\2\u0192E\3\2\2\2"+
		"\u0193\u0194\7.\2\2\u0194\u0195\5L\'\2\u0195G\3\2\2\2\u0196\u0197\7/\2"+
		"\2\u0197\u0198\5L\'\2\u0198I\3\2\2\2\u0199\u019e\5L\'\2\u019a\u019b\7"+
		"\5\2\2\u019b\u019d\5J&\2\u019c\u019a\3\2\2\2\u019d\u01a0\3\2\2\2\u019e"+
		"\u019c\3\2\2\2\u019e\u019f\3\2\2\2\u019f\u01a3\3\2\2\2\u01a0\u019e\3\2"+
		"\2\2\u01a1\u01a3\5N(\2\u01a2\u0199\3\2\2\2\u01a2\u01a1\3\2\2\2\u01a3K"+
		"\3\2\2\2\u01a4\u01a5\t\3\2\2\u01a5M\3\2\2\2\u01a6\u01a7\t\4\2\2\u01a7"+
		"\u01ac\5P)\2\u01a8\u01a9\7@\2\2\u01a9\u01ab\5P)\2\u01aa\u01a8\3\2\2\2"+
		"\u01ab\u01ae\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ac\u01ad\3\2\2\2\u01ad\u01af"+
		"\3\2\2\2\u01ae\u01ac\3\2\2\2\u01af\u01b0\t\5\2\2\u01b0O\3\2\2\2\u01b1"+
		"\u01b3\5L\'\2\u01b2\u01b4\7\f\2\2\u01b3\u01b2\3\2\2\2\u01b3\u01b4\3\2"+
		"\2\2\u01b4\u01b7\3\2\2\2\u01b5\u01b8\5J&\2\u01b6\u01b8\5R*\2\u01b7\u01b5"+
		"\3\2\2\2\u01b7\u01b6\3\2\2\2\u01b8\u01bc\3\2\2\2\u01b9\u01bc\5\"\22\2"+
		"\u01ba\u01bc\5$\23\2\u01bb\u01b1\3\2\2\2\u01bb\u01b9\3\2\2\2\u01bb\u01ba"+
		"\3\2\2\2\u01bcQ\3\2\2\2\u01bd\u01c2\5X-\2\u01be\u01c2\5V,\2\u01bf\u01c2"+
		"\7<\2\2\u01c0\u01c2\5T+\2\u01c1\u01bd\3\2\2\2\u01c1\u01be\3\2\2\2\u01c1"+
		"\u01bf\3\2\2\2\u01c1\u01c0\3\2\2\2\u01c2S\3\2\2\2\u01c3\u01c6\t\6\2\2"+
		"\u01c4\u01c7\5V,\2\u01c5\u01c7\7\61\2\2\u01c6\u01c4\3\2\2\2\u01c6\u01c5"+
		"\3\2\2\2\u01c7\u01ce\3\2\2\2\u01c8\u01cb\t\7\2\2\u01c9\u01cc\5V,\2\u01ca"+
		"\u01cc\7\61\2\2\u01cb\u01c9\3\2\2\2\u01cb\u01ca\3\2\2\2\u01cc\u01ce\3"+
		"\2\2\2\u01cd\u01c3\3\2\2\2\u01cd\u01c8\3\2\2\2\u01ceU\3\2\2\2\u01cf\u01d0"+
		"\7<\2\2\u01d0\u01d1\7\13\2\2\u01d1\u01d2\5L\'\2\u01d2W\3\2\2\2\u01d3\u01d6"+
		"\7\4\2\2\u01d4\u01d7\7<\2\2\u01d5\u01d7\5V,\2\u01d6\u01d4\3\2\2\2\u01d6"+
		"\u01d5\3\2\2\2\u01d7\u01dd\3\2\2\2\u01d8\u01db\7\5\2\2\u01d9\u01dc\7<"+
		"\2\2\u01da\u01dc\5V,\2\u01db\u01d9\3\2\2\2\u01db\u01da\3\2\2\2\u01dc\u01de"+
		"\3\2\2\2\u01dd\u01d8\3\2\2\2\u01de\u01df\3\2\2\2\u01df\u01e0\3\2\2\2\u01df"+
		"\u01dd\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u01e2\7\6\2\2\u01e2Y\3\2\2\2"+
		"\u01e3\u01e4\7\4\2\2\u01e4\u01e7\5R*\2\u01e5\u01e6\7\5\2\2\u01e6\u01e8"+
		"\5R*\2\u01e7\u01e5\3\2\2\2\u01e8\u01e9\3\2\2\2\u01e9\u01e7\3\2\2\2\u01e9"+
		"\u01ea\3\2\2\2\u01ea\u01eb\3\2\2\2\u01eb\u01ec\7\6\2\2\u01ec[\3\2\2\2"+
		"@dhqx|\177\u0085\u0089\u008c\u0093\u0097\u009a\u00a1\u00a5\u00a8\u00ae"+
		"\u00b2\u00b5\u00bd\u00c0\u00c7\u00cb\u00ce\u00d6\u00d9\u00e0\u00e4\u00e7"+
		"\u00ef\u00f2\u00f9\u00fd\u0100\u0108\u010b\u0117\u0120\u0124\u0127\u012b"+
		"\u012f\u0133\u0142\u0145\u014f\u0169\u0171\u017a\u019e\u01a2\u01ac\u01b3"+
		"\u01b7\u01bb\u01c1\u01c6\u01cb\u01cd\u01d6\u01db\u01df\u01e9";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}