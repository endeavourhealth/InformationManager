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
		VERSION=17, IRI_LABEL=18, TYPE=19, TERM=20, RECORD=21, TARGETCLASS=22, 
		CLASS=23, OBJECTPROPERTY=24, DATAPROPERTY=25, ANNOTATION=26, PROPERTYCONSTRAINT=27, 
		DATATYPE=28, VALUESET=29, PATH=30, MINCOUNT=31, MAXCOUNT=32, NAME=33, 
		DESCRIPTION=34, CODE=35, SCHEME=36, MININCLUSIVE=37, MAXINCLUSIVE=38, 
		MINEXCLUSIVE=39, MAXEXCLUSIVE=40, SUBCLASS=41, EQUIVALENTTO=42, DISJOINT=43, 
		SUBPROPERTY=44, INVERSE=45, INTEGER=46, DOUBLE=47, DIGIT=48, EXACT=49, 
		AND=50, OR=51, PREFIXIRI=52, IRIREF=53, LOWERCASE=54, UPPERCASE=55, PLX=56, 
		PERCENT=57, QUOTED_STRING=58, HEX=59, PN_LOCAL_ESC=60, WS=61, SC=62;
	public static final int
		RULE_concept = 0, RULE_annotations = 1, RULE_classAxiom = 2, RULE_propertyAxiom = 3, 
		RULE_type = 4, RULE_classType = 5, RULE_dataType = 6, RULE_recordType = 7, 
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
			"concept", "annotations", "classAxiom", "propertyAxiom", "type", "classType", 
			"dataType", "recordType", "objectProperty", "dataProperty", "annotationProperty", 
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
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public TerminalNode SC() { return getToken(IMLangParser.SC, 0); }
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
			annotations();
			setState(94);
			match(SC);
			setState(95);
			type();
			setState(96);
			match(T__0);
			setState(97);
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

	public static class AnnotationsContext extends ParserRuleContext {
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
		public AnnotationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterAnnotations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitAnnotations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitAnnotations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationsContext annotations() throws RecognitionException {
		AnnotationsContext _localctx = new AnnotationsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_annotations);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(99);
				match(SC);
				setState(100);
				label();
				setState(107);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(101);
						match(SC);
						setState(103);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STATUS) | (1L << VERSION) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
							{
							setState(102);
							label();
							}
						}

						}
						} 
					}
					setState(109);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
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
		enterRule(_localctx, 4, RULE_classAxiom);
		try {
			setState(115);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBCLASS:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				subclassOf();
				}
				break;
			case EQUIVALENTTO:
				enterOuterAlt(_localctx, 2);
				{
				setState(113);
				equivalentTo();
				}
				break;
			case DISJOINT:
				enterOuterAlt(_localctx, 3);
				{
				setState(114);
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
		enterRule(_localctx, 6, RULE_propertyAxiom);
		try {
			setState(119);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBPROPERTY:
				enterOuterAlt(_localctx, 1);
				{
				setState(117);
				subpropertyOf();
				}
				break;
			case INVERSE:
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
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
		enterRule(_localctx, 8, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(TYPE);
			setState(128);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CLASS:
				{
				setState(122);
				classType();
				}
				break;
			case DATATYPE:
				{
				setState(123);
				dataType();
				}
				break;
			case RECORD:
				{
				setState(124);
				recordType();
				}
				break;
			case OBJECTPROPERTY:
				{
				setState(125);
				objectProperty();
				}
				break;
			case ANNOTATION:
				{
				setState(126);
				annotationProperty();
				}
				break;
			case DATAPROPERTY:
				{
				setState(127);
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
		enterRule(_localctx, 10, RULE_classType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(CLASS);
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
		enterRule(_localctx, 12, RULE_dataType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(DATATYPE);
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
			setState(146);
			match(RECORD);
			setState(158);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(147);
				match(SC);
				setState(148);
				classAxiom();
				setState(155);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(149);
						match(SC);
						setState(151);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << SUBCLASS) | (1L << EQUIVALENTTO) | (1L << DISJOINT))) != 0)) {
							{
							setState(150);
							classAxiom();
							}
						}

						}
						} 
					}
					setState(157);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				}
				}
				break;
			}
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(160);
				match(SC);
				setState(161);
				role();
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(162);
					match(SC);
					setState(163);
					role();
					}
					}
					setState(168);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			match(OBJECTPROPERTY);
			setState(181);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(172);
				match(SC);
				setState(173);
				propertyAxiom();
				setState(178);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(174);
					match(SC);
					setState(175);
					propertyAxiom();
					}
					}
					setState(180);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(DATAPROPERTY);
			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(184);
				match(SC);
				setState(185);
				propertyAxiom();
				setState(190);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(186);
					match(SC);
					setState(187);
					propertyAxiom();
					}
					}
					setState(192);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(ANNOTATION);
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(196);
				match(SC);
				setState(197);
				propertyAxiom();
				setState(202);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(198);
					match(SC);
					setState(199);
					propertyAxiom();
					}
					}
					setState(204);
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
			setState(207);
			match(MEMBER);
			setState(208);
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
			setState(210);
			match(EXPANSION);
			setState(211);
			match(T__1);
			setState(212);
			iri();
			setState(215); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(213);
					match(T__2);
					setState(214);
					iri();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(217); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(219);
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
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(VALUESET);
			setState(224);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(222);
				match(SC);
				setState(223);
				subclassOf();
				}
				break;
			}
			setState(228);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(226);
				match(SC);
				setState(227);
				members();
				}
				break;
			}
			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(230);
				match(SC);
				setState(231);
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
			setState(234);
			match(TARGETCLASS);
			}
			setState(235);
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
			setState(237);
			match(PROPERTYCONSTRAINT);
			setState(238);
			match(T__4);
			setState(239);
			match(PATH);
			setState(240);
			iri();
			setState(250);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SC) {
				{
				setState(241);
				match(SC);
				setState(242);
				constraintParameter();
				setState(247);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SC) {
					{
					{
					setState(243);
					match(SC);
					setState(244);
					constraintParameter();
					}
					}
					setState(249);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(252);
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
			setState(260);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(254);
				minCount();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(255);
				maxCount();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(256);
				minInclusive();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(257);
				maxInclusive();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(258);
				classValue();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(259);
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
			setState(262);
			match(MINCOUNT);
			setState(263);
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
			setState(265);
			match(MAXCOUNT);
			setState(266);
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
			setState(268);
			match(MININCLUSIVE);
			setState(269);
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
			setState(271);
			match(MAXINCLUSIVE);
			setState(272);
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
			setState(274);
			match(MINEXCLUSIVE);
			setState(275);
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
			setState(277);
			match(MAXEXCLUSIVE);
			setState(278);
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
			setState(280);
			match(CLASS);
			setState(286);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREFIXIRI:
			case IRIREF:
			case QUOTED_STRING:
				{
				setState(281);
				iri();
				}
				break;
			case T__4:
				{
				setState(282);
				match(T__4);
				setState(283);
				propertyConstraint();
				setState(284);
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
			setState(294);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAME:
				{
				setState(288);
				name();
				}
				break;
			case DESCRIPTION:
				{
				setState(289);
				description();
				}
				break;
			case CODE:
				{
				setState(290);
				code();
				}
				break;
			case SCHEME:
				{
				setState(291);
				scheme();
				}
				break;
			case STATUS:
				{
				setState(292);
				status();
				}
				break;
			case VERSION:
				{
				setState(293);
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
			setState(296);
			match(STATUS);
			setState(297);
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
			setState(299);
			match(VERSION);
			setState(300);
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
			setState(303);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRI_LABEL) {
				{
				setState(302);
				match(IRI_LABEL);
				}
			}

			setState(305);
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
			setState(307);
			match(NAME);
			setState(308);
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
			setState(310);
			match(DESCRIPTION);
			setState(311);
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
			setState(313);
			match(CODE);
			setState(314);
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
			setState(316);
			match(SCHEME);
			setState(317);
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
			setState(319);
			match(SUBCLASS);
			setState(320);
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
			setState(322);
			match(EQUIVALENTTO);
			setState(323);
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
			setState(325);
			match(DISJOINT);
			setState(326);
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
			setState(328);
			match(SUBPROPERTY);
			setState(329);
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
			setState(331);
			match(INVERSE);
			setState(332);
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
			setState(343);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREFIXIRI:
			case IRIREF:
			case QUOTED_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(334);
				iri();
				setState(339);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(335);
						match(T__2);
						setState(336);
						classExpression();
						}
						} 
					}
					setState(341);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
				}
				}
				break;
			case T__4:
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(342);
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
			setState(345);
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
			setState(347);
			_la = _input.LA(1);
			if ( !(_la==T__4 || _la==T__6) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(348);
			role();
			setState(353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SC) {
				{
				{
				setState(349);
				match(SC);
				setState(350);
				role();
				}
				}
				setState(355);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(356);
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
			setState(368);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PREFIXIRI:
			case IRIREF:
			case QUOTED_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(358);
				iri();
				setState(360);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EQ) {
					{
					setState(359);
					match(EQ);
					}
				}

				setState(364);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
				case 1:
					{
					setState(362);
					classExpression();
					}
					break;
				case 2:
					{
					setState(363);
					dataRange();
					}
					break;
				}
				}
				break;
			case MINCOUNT:
				enterOuterAlt(_localctx, 2);
				{
				setState(366);
				minCount();
				}
				break;
			case MAXCOUNT:
				enterOuterAlt(_localctx, 3);
				{
				setState(367);
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
			setState(374);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,36,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(370);
				valueCollection();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(371);
				typedString();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(372);
				match(QUOTED_STRING);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(373);
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
			setState(386);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MININCLUSIVE:
			case MINEXCLUSIVE:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(376);
				_la = _input.LA(1);
				if ( !(_la==MININCLUSIVE || _la==MINEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(379);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUOTED_STRING:
					{
					setState(377);
					typedString();
					}
					break;
				case DOUBLE:
					{
					setState(378);
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
				setState(381);
				_la = _input.LA(1);
				if ( !(_la==MAXINCLUSIVE || _la==MAXEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(384);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case QUOTED_STRING:
					{
					setState(382);
					typedString();
					}
					break;
				case DOUBLE:
					{
					setState(383);
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
			setState(388);
			match(QUOTED_STRING);
			setState(389);
			match(T__8);
			setState(390);
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
			setState(392);
			match(T__1);
			setState(395);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,40,_ctx) ) {
			case 1:
				{
				setState(393);
				match(QUOTED_STRING);
				}
				break;
			case 2:
				{
				setState(394);
				typedString();
				}
				break;
			}
			setState(402); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(397);
					match(T__2);
					setState(400);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
					case 1:
						{
						setState(398);
						match(QUOTED_STRING);
						}
						break;
					case 2:
						{
						setState(399);
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
				setState(404); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,42,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(406);
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
			setState(408);
			match(T__1);
			setState(409);
			dataRange();
			setState(412); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(410);
				match(T__2);
				setState(411);
				dataRange();
				}
				}
				setState(414); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__2 );
			setState(416);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3@\u01a5\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3"+
		"j\n\3\7\3l\n\3\f\3\16\3o\13\3\5\3q\n\3\3\4\3\4\3\4\5\4v\n\4\3\5\3\5\5"+
		"\5z\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u0083\n\6\3\7\3\7\3\7\3\7\3\7"+
		"\5\7\u008a\n\7\7\7\u008c\n\7\f\7\16\7\u008f\13\7\5\7\u0091\n\7\3\b\3\b"+
		"\3\t\3\t\3\t\3\t\3\t\5\t\u009a\n\t\7\t\u009c\n\t\f\t\16\t\u009f\13\t\5"+
		"\t\u00a1\n\t\3\t\3\t\3\t\3\t\7\t\u00a7\n\t\f\t\16\t\u00aa\13\t\5\t\u00ac"+
		"\n\t\3\n\3\n\3\n\3\n\3\n\7\n\u00b3\n\n\f\n\16\n\u00b6\13\n\5\n\u00b8\n"+
		"\n\3\13\3\13\3\13\3\13\3\13\7\13\u00bf\n\13\f\13\16\13\u00c2\13\13\5\13"+
		"\u00c4\n\13\3\f\3\f\3\f\3\f\3\f\7\f\u00cb\n\f\f\f\16\f\u00ce\13\f\5\f"+
		"\u00d0\n\f\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\6\16\u00da\n\16\r\16\16"+
		"\16\u00db\3\16\3\16\3\17\3\17\3\17\5\17\u00e3\n\17\3\17\3\17\5\17\u00e7"+
		"\n\17\3\17\3\17\5\17\u00eb\n\17\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\7\21\u00f8\n\21\f\21\16\21\u00fb\13\21\5\21\u00fd\n\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u0107\n\22\3\23\3\23\3\23"+
		"\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30"+
		"\3\30\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0121\n\31\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\5\32\u0129\n\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\5\35\u0132"+
		"\n\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\3!\3\"\3"+
		"\"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3%\3&\3&\3&\3\'\3\'\3\'\7\'\u0154\n\'\f"+
		"\'\16\'\u0157\13\'\3\'\5\'\u015a\n\'\3(\3(\3)\3)\3)\3)\7)\u0162\n)\f)"+
		"\16)\u0165\13)\3)\3)\3*\3*\5*\u016b\n*\3*\3*\5*\u016f\n*\3*\3*\5*\u0173"+
		"\n*\3+\3+\3+\3+\5+\u0179\n+\3,\3,\3,\5,\u017e\n,\3,\3,\3,\5,\u0183\n,"+
		"\5,\u0185\n,\3-\3-\3-\3-\3.\3.\3.\5.\u018e\n.\3.\3.\3.\5.\u0193\n.\6."+
		"\u0195\n.\r.\16.\u0196\3.\3.\3/\3/\3/\3/\6/\u019f\n/\r/\16/\u01a0\3/\3"+
		"/\3/\4\u00db\u0196\2\60\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*"+
		",.\60\62\64\668:<>@BDFHJLNPRTVXZ\\\2\b\3\2\20\22\4\2\66\67<<\4\2\7\7\t"+
		"\t\4\2\b\b\n\n\4\2\'\'))\4\2((**\2\u01b2\2^\3\2\2\2\4p\3\2\2\2\6u\3\2"+
		"\2\2\by\3\2\2\2\n{\3\2\2\2\f\u0084\3\2\2\2\16\u0092\3\2\2\2\20\u0094\3"+
		"\2\2\2\22\u00ad\3\2\2\2\24\u00b9\3\2\2\2\26\u00c5\3\2\2\2\30\u00d1\3\2"+
		"\2\2\32\u00d4\3\2\2\2\34\u00df\3\2\2\2\36\u00ec\3\2\2\2 \u00ef\3\2\2\2"+
		"\"\u0106\3\2\2\2$\u0108\3\2\2\2&\u010b\3\2\2\2(\u010e\3\2\2\2*\u0111\3"+
		"\2\2\2,\u0114\3\2\2\2.\u0117\3\2\2\2\60\u011a\3\2\2\2\62\u0128\3\2\2\2"+
		"\64\u012a\3\2\2\2\66\u012d\3\2\2\28\u0131\3\2\2\2:\u0135\3\2\2\2<\u0138"+
		"\3\2\2\2>\u013b\3\2\2\2@\u013e\3\2\2\2B\u0141\3\2\2\2D\u0144\3\2\2\2F"+
		"\u0147\3\2\2\2H\u014a\3\2\2\2J\u014d\3\2\2\2L\u0159\3\2\2\2N\u015b\3\2"+
		"\2\2P\u015d\3\2\2\2R\u0172\3\2\2\2T\u0178\3\2\2\2V\u0184\3\2\2\2X\u0186"+
		"\3\2\2\2Z\u018a\3\2\2\2\\\u019a\3\2\2\2^_\58\35\2_`\5\4\3\2`a\7@\2\2a"+
		"b\5\n\6\2bc\7\3\2\2cd\7\2\2\3d\3\3\2\2\2ef\7@\2\2fm\5\62\32\2gi\7@\2\2"+
		"hj\5\62\32\2ih\3\2\2\2ij\3\2\2\2jl\3\2\2\2kg\3\2\2\2lo\3\2\2\2mk\3\2\2"+
		"\2mn\3\2\2\2nq\3\2\2\2om\3\2\2\2pe\3\2\2\2pq\3\2\2\2q\5\3\2\2\2rv\5B\""+
		"\2sv\5D#\2tv\5F$\2ur\3\2\2\2us\3\2\2\2ut\3\2\2\2v\7\3\2\2\2wz\5H%\2xz"+
		"\5J&\2yw\3\2\2\2yx\3\2\2\2z\t\3\2\2\2{\u0082\7\25\2\2|\u0083\5\f\7\2}"+
		"\u0083\5\16\b\2~\u0083\5\20\t\2\177\u0083\5\22\n\2\u0080\u0083\5\26\f"+
		"\2\u0081\u0083\5\24\13\2\u0082|\3\2\2\2\u0082}\3\2\2\2\u0082~\3\2\2\2"+
		"\u0082\177\3\2\2\2\u0082\u0080\3\2\2\2\u0082\u0081\3\2\2\2\u0083\13\3"+
		"\2\2\2\u0084\u0090\7\31\2\2\u0085\u0086\7@\2\2\u0086\u008d\5\6\4\2\u0087"+
		"\u0089\7@\2\2\u0088\u008a\5\6\4\2\u0089\u0088\3\2\2\2\u0089\u008a\3\2"+
		"\2\2\u008a\u008c\3\2\2\2\u008b\u0087\3\2\2\2\u008c\u008f\3\2\2\2\u008d"+
		"\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2"+
		"\2\2\u0090\u0085\3\2\2\2\u0090\u0091\3\2\2\2\u0091\r\3\2\2\2\u0092\u0093"+
		"\7\36\2\2\u0093\17\3\2\2\2\u0094\u00a0\7\27\2\2\u0095\u0096\7@\2\2\u0096"+
		"\u009d\5\6\4\2\u0097\u0099\7@\2\2\u0098\u009a\5\6\4\2\u0099\u0098\3\2"+
		"\2\2\u0099\u009a\3\2\2\2\u009a\u009c\3\2\2\2\u009b\u0097\3\2\2\2\u009c"+
		"\u009f\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u00a1\3\2"+
		"\2\2\u009f\u009d\3\2\2\2\u00a0\u0095\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1"+
		"\u00ab\3\2\2\2\u00a2\u00a3\7@\2\2\u00a3\u00a8\5R*\2\u00a4\u00a5\7@\2\2"+
		"\u00a5\u00a7\5R*\2\u00a6\u00a4\3\2\2\2\u00a7\u00aa\3\2\2\2\u00a8\u00a6"+
		"\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00ac\3\2\2\2\u00aa\u00a8\3\2\2\2\u00ab"+
		"\u00a2\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\21\3\2\2\2\u00ad\u00b7\7\32\2"+
		"\2\u00ae\u00af\7@\2\2\u00af\u00b4\5\b\5\2\u00b0\u00b1\7@\2\2\u00b1\u00b3"+
		"\5\b\5\2\u00b2\u00b0\3\2\2\2\u00b3\u00b6\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b4"+
		"\u00b5\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b7\u00ae\3\2"+
		"\2\2\u00b7\u00b8\3\2\2\2\u00b8\23\3\2\2\2\u00b9\u00c3\7\33\2\2\u00ba\u00bb"+
		"\7@\2\2\u00bb\u00c0\5\b\5\2\u00bc\u00bd\7@\2\2\u00bd\u00bf\5\b\5\2\u00be"+
		"\u00bc\3\2\2\2\u00bf\u00c2\3\2\2\2\u00c0\u00be\3\2\2\2\u00c0\u00c1\3\2"+
		"\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c3\u00ba\3\2\2\2\u00c3"+
		"\u00c4\3\2\2\2\u00c4\25\3\2\2\2\u00c5\u00cf\7\34\2\2\u00c6\u00c7\7@\2"+
		"\2\u00c7\u00cc\5\b\5\2\u00c8\u00c9\7@\2\2\u00c9\u00cb\5\b\5\2\u00ca\u00c8"+
		"\3\2\2\2\u00cb\u00ce\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd"+
		"\u00d0\3\2\2\2\u00ce\u00cc\3\2\2\2\u00cf\u00c6\3\2\2\2\u00cf\u00d0\3\2"+
		"\2\2\u00d0\27\3\2\2\2\u00d1\u00d2\7\r\2\2\u00d2\u00d3\5L\'\2\u00d3\31"+
		"\3\2\2\2\u00d4\u00d5\7\16\2\2\u00d5\u00d6\7\4\2\2\u00d6\u00d9\5N(\2\u00d7"+
		"\u00d8\7\5\2\2\u00d8\u00da\5N(\2\u00d9\u00d7\3\2\2\2\u00da\u00db\3\2\2"+
		"\2\u00db\u00dc\3\2\2\2\u00db\u00d9\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00de"+
		"\7\6\2\2\u00de\33\3\2\2\2\u00df\u00e2\7\37\2\2\u00e0\u00e1\7@\2\2\u00e1"+
		"\u00e3\5B\"\2\u00e2\u00e0\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00e6\3\2"+
		"\2\2\u00e4\u00e5\7@\2\2\u00e5\u00e7\5\30\r\2\u00e6\u00e4\3\2\2\2\u00e6"+
		"\u00e7\3\2\2\2\u00e7\u00ea\3\2\2\2\u00e8\u00e9\7@\2\2\u00e9\u00eb\5\32"+
		"\16\2\u00ea\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\35\3\2\2\2\u00ec\u00ed"+
		"\7\30\2\2\u00ed\u00ee\5N(\2\u00ee\37\3\2\2\2\u00ef\u00f0\7\35\2\2\u00f0"+
		"\u00f1\7\7\2\2\u00f1\u00f2\7 \2\2\u00f2\u00fc\5N(\2\u00f3\u00f4\7@\2\2"+
		"\u00f4\u00f9\5\"\22\2\u00f5\u00f6\7@\2\2\u00f6\u00f8\5\"\22\2\u00f7\u00f5"+
		"\3\2\2\2\u00f8\u00fb\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00fa\3\2\2\2\u00fa"+
		"\u00fd\3\2\2\2\u00fb\u00f9\3\2\2\2\u00fc\u00f3\3\2\2\2\u00fc\u00fd\3\2"+
		"\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00ff\7\b\2\2\u00ff!\3\2\2\2\u0100\u0107"+
		"\5$\23\2\u0101\u0107\5&\24\2\u0102\u0107\5(\25\2\u0103\u0107\5*\26\2\u0104"+
		"\u0107\5\60\31\2\u0105\u0107\5T+\2\u0106\u0100\3\2\2\2\u0106\u0101\3\2"+
		"\2\2\u0106\u0102\3\2\2\2\u0106\u0103\3\2\2\2\u0106\u0104\3\2\2\2\u0106"+
		"\u0105\3\2\2\2\u0107#\3\2\2\2\u0108\u0109\7!\2\2\u0109\u010a\7\60\2\2"+
		"\u010a%\3\2\2\2\u010b\u010c\7\"\2\2\u010c\u010d\7\60\2\2\u010d\'\3\2\2"+
		"\2\u010e\u010f\7\'\2\2\u010f\u0110\7\61\2\2\u0110)\3\2\2\2\u0111\u0112"+
		"\7(\2\2\u0112\u0113\7\61\2\2\u0113+\3\2\2\2\u0114\u0115\7)\2\2\u0115\u0116"+
		"\7\61\2\2\u0116-\3\2\2\2\u0117\u0118\7*\2\2\u0118\u0119\7\61\2\2\u0119"+
		"/\3\2\2\2\u011a\u0120\7\31\2\2\u011b\u0121\5N(\2\u011c\u011d\7\7\2\2\u011d"+
		"\u011e\5 \21\2\u011e\u011f\7\b\2\2\u011f\u0121\3\2\2\2\u0120\u011b\3\2"+
		"\2\2\u0120\u011c\3\2\2\2\u0121\61\3\2\2\2\u0122\u0129\5:\36\2\u0123\u0129"+
		"\5<\37\2\u0124\u0129\5> \2\u0125\u0129\5@!\2\u0126\u0129\5\64\33\2\u0127"+
		"\u0129\5\66\34\2\u0128\u0122\3\2\2\2\u0128\u0123\3\2\2\2\u0128\u0124\3"+
		"\2\2\2\u0128\u0125\3\2\2\2\u0128\u0126\3\2\2\2\u0128\u0127\3\2\2\2\u0129"+
		"\63\3\2\2\2\u012a\u012b\7\17\2\2\u012b\u012c\t\2\2\2\u012c\65\3\2\2\2"+
		"\u012d\u012e\7\23\2\2\u012e\u012f\7<\2\2\u012f\67\3\2\2\2\u0130\u0132"+
		"\7\24\2\2\u0131\u0130\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0133\3\2\2\2"+
		"\u0133\u0134\5N(\2\u01349\3\2\2\2\u0135\u0136\7#\2\2\u0136\u0137\7<\2"+
		"\2\u0137;\3\2\2\2\u0138\u0139\7$\2\2\u0139\u013a\7<\2\2\u013a=\3\2\2\2"+
		"\u013b\u013c\7%\2\2\u013c\u013d\7<\2\2\u013d?\3\2\2\2\u013e\u013f\7&\2"+
		"\2\u013f\u0140\7<\2\2\u0140A\3\2\2\2\u0141\u0142\7+\2\2\u0142\u0143\5"+
		"L\'\2\u0143C\3\2\2\2\u0144\u0145\7,\2\2\u0145\u0146\5L\'\2\u0146E\3\2"+
		"\2\2\u0147\u0148\7-\2\2\u0148\u0149\5L\'\2\u0149G\3\2\2\2\u014a\u014b"+
		"\7.\2\2\u014b\u014c\5N(\2\u014cI\3\2\2\2\u014d\u014e\7/\2\2\u014e\u014f"+
		"\5N(\2\u014fK\3\2\2\2\u0150\u0155\5N(\2\u0151\u0152\7\5\2\2\u0152\u0154"+
		"\5L\'\2\u0153\u0151\3\2\2\2\u0154\u0157\3\2\2\2\u0155\u0153\3\2\2\2\u0155"+
		"\u0156\3\2\2\2\u0156\u015a\3\2\2\2\u0157\u0155\3\2\2\2\u0158\u015a\5P"+
		")\2\u0159\u0150\3\2\2\2\u0159\u0158\3\2\2\2\u015aM\3\2\2\2\u015b\u015c"+
		"\t\3\2\2\u015cO\3\2\2\2\u015d\u015e\t\4\2\2\u015e\u0163\5R*\2\u015f\u0160"+
		"\7@\2\2\u0160\u0162\5R*\2\u0161\u015f\3\2\2\2\u0162\u0165\3\2\2\2\u0163"+
		"\u0161\3\2\2\2\u0163\u0164\3\2\2\2\u0164\u0166\3\2\2\2\u0165\u0163\3\2"+
		"\2\2\u0166\u0167\t\5\2\2\u0167Q\3\2\2\2\u0168\u016a\5N(\2\u0169\u016b"+
		"\7\f\2\2\u016a\u0169\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u016e\3\2\2\2\u016c"+
		"\u016f\5L\'\2\u016d\u016f\5T+\2\u016e\u016c\3\2\2\2\u016e\u016d\3\2\2"+
		"\2\u016f\u0173\3\2\2\2\u0170\u0173\5$\23\2\u0171\u0173\5&\24\2\u0172\u0168"+
		"\3\2\2\2\u0172\u0170\3\2\2\2\u0172\u0171\3\2\2\2\u0173S\3\2\2\2\u0174"+
		"\u0179\5Z.\2\u0175\u0179\5X-\2\u0176\u0179\7<\2\2\u0177\u0179\5V,\2\u0178"+
		"\u0174\3\2\2\2\u0178\u0175\3\2\2\2\u0178\u0176\3\2\2\2\u0178\u0177\3\2"+
		"\2\2\u0179U\3\2\2\2\u017a\u017d\t\6\2\2\u017b\u017e\5X-\2\u017c\u017e"+
		"\7\61\2\2\u017d\u017b\3\2\2\2\u017d\u017c\3\2\2\2\u017e\u0185\3\2\2\2"+
		"\u017f\u0182\t\7\2\2\u0180\u0183\5X-\2\u0181\u0183\7\61\2\2\u0182\u0180"+
		"\3\2\2\2\u0182\u0181\3\2\2\2\u0183\u0185\3\2\2\2\u0184\u017a\3\2\2\2\u0184"+
		"\u017f\3\2\2\2\u0185W\3\2\2\2\u0186\u0187\7<\2\2\u0187\u0188\7\13\2\2"+
		"\u0188\u0189\5N(\2\u0189Y\3\2\2\2\u018a\u018d\7\4\2\2\u018b\u018e\7<\2"+
		"\2\u018c\u018e\5X-\2\u018d\u018b\3\2\2\2\u018d\u018c\3\2\2\2\u018e\u0194"+
		"\3\2\2\2\u018f\u0192\7\5\2\2\u0190\u0193\7<\2\2\u0191\u0193\5X-\2\u0192"+
		"\u0190\3\2\2\2\u0192\u0191\3\2\2\2\u0193\u0195\3\2\2\2\u0194\u018f\3\2"+
		"\2\2\u0195\u0196\3\2\2\2\u0196\u0197\3\2\2\2\u0196\u0194\3\2\2\2\u0197"+
		"\u0198\3\2\2\2\u0198\u0199\7\6\2\2\u0199[\3\2\2\2\u019a\u019b\7\4\2\2"+
		"\u019b\u019e\5T+\2\u019c\u019d\7\5\2\2\u019d\u019f\5T+\2\u019e\u019c\3"+
		"\2\2\2\u019f\u01a0\3\2\2\2\u01a0\u019e\3\2\2\2\u01a0\u01a1\3\2\2\2\u01a1"+
		"\u01a2\3\2\2\2\u01a2\u01a3\7\6\2\2\u01a3]\3\2\2\2.impuy\u0082\u0089\u008d"+
		"\u0090\u0099\u009d\u00a0\u00a8\u00ab\u00b4\u00b7\u00c0\u00c3\u00cc\u00cf"+
		"\u00db\u00e2\u00e6\u00ea\u00f9\u00fc\u0106\u0120\u0128\u0131\u0155\u0159"+
		"\u0163\u016a\u016e\u0172\u0178\u017d\u0182\u0184\u018d\u0192\u0196\u01a0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}