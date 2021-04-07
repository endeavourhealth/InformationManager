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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, WS=9, 
		EQ=10, MEMBERS=11, STATUS=12, VERSION=13, PROPERTIES=14, TYPE=15, MIN=16, 
		MAX=17, SOME=18, ONLY=19, MININCLUSIVE=20, MAXINCLUSIVE=21, MINEXCLUSIVE=22, 
		MAXEXCLUSIVE=23, SUBCLASS=24, EQUIVALENTTO=25, DISJOINT=26, SUBPROPERTY=27, 
		INVERSE=28, TARGETCLASS=29, EXACTLY=30, AND=31, INTEGER=32, DOUBLE=33, 
		DIGIT=34, OR=35, NOT=36, NAME=37, DESCRIPTION=38, CODE=39, SCHEME=40, 
		PNAME_NS=41, PN_PREFIX=42, PN_CHARS_BASE=43, PN_CHARS_U=44, PN_CHARS=45, 
		IRIREF=46, UCHAR=47, PNAME_LN=48, PN_LOCAL=49, PLX=50, PERCENT=51, ECHAR=52, 
		QUOTED_STRING=53, STRING_LITERAL_QUOTE=54, STRING_LITERAL_SINGLE_QUOTE=55, 
		PIPED_STRING=56, PN_LOCAL_ESC=57, HEX=58;
	public static final int
		RULE_concept = 0, RULE_directive = 1, RULE_prefixID = 2, RULE_conceptPredicateObjectList = 3, 
		RULE_annotation = 4, RULE_predicateIri = 5, RULE_scheme = 6, RULE_type = 7, 
		RULE_version = 8, RULE_axiom = 9, RULE_properties = 10, RULE_members = 11, 
		RULE_target = 12, RULE_minInclusive = 13, RULE_maxInclusive = 14, RULE_minExclusive = 15, 
		RULE_maxExclusive = 16, RULE_status = 17, RULE_subclassOf = 18, RULE_equivalentTo = 19, 
		RULE_subpropertyOf = 20, RULE_inverseOf = 21, RULE_classExpression = 22, 
		RULE_intersection = 23, RULE_subExpression = 24, RULE_union = 25, RULE_complement = 26, 
		RULE_iri = 27, RULE_literal = 28, RULE_propertyRestriction = 29, RULE_some = 30, 
		RULE_only = 31, RULE_propertyIri = 32, RULE_exactCardinality = 33, RULE_rangeCardinality = 34, 
		RULE_minCardinality = 35, RULE_maxCardinality = 36, RULE_classOrDataType = 37, 
		RULE_name = 38, RULE_description = 39, RULE_code = 40;
	private static String[] makeRuleNames() {
		return new String[] {
			"concept", "directive", "prefixID", "conceptPredicateObjectList", "annotation", 
			"predicateIri", "scheme", "type", "version", "axiom", "properties", "members", 
			"target", "minInclusive", "maxInclusive", "minExclusive", "maxExclusive", 
			"status", "subclassOf", "equivalentTo", "subpropertyOf", "inverseOf", 
			"classExpression", "intersection", "subExpression", "union", "complement", 
			"iri", "literal", "propertyRestriction", "some", "only", "propertyIri", 
			"exactCardinality", "rangeCardinality", "minCardinality", "maxCardinality", 
			"classOrDataType", "name", "description", "code"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'@prefix'", "';'", "'['", "','", "']'", "'('", "')'", null, 
			"'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, "WS", "EQ", "MEMBERS", 
			"STATUS", "VERSION", "PROPERTIES", "TYPE", "MIN", "MAX", "SOME", "ONLY", 
			"MININCLUSIVE", "MAXINCLUSIVE", "MINEXCLUSIVE", "MAXEXCLUSIVE", "SUBCLASS", 
			"EQUIVALENTTO", "DISJOINT", "SUBPROPERTY", "INVERSE", "TARGETCLASS", 
			"EXACTLY", "AND", "INTEGER", "DOUBLE", "DIGIT", "OR", "NOT", "NAME", 
			"DESCRIPTION", "CODE", "SCHEME", "PNAME_NS", "PN_PREFIX", "PN_CHARS_BASE", 
			"PN_CHARS_U", "PN_CHARS", "IRIREF", "UCHAR", "PNAME_LN", "PN_LOCAL", 
			"PLX", "PERCENT", "ECHAR", "QUOTED_STRING", "STRING_LITERAL_QUOTE", "STRING_LITERAL_SINGLE_QUOTE", 
			"PIPED_STRING", "PN_LOCAL_ESC", "HEX"
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
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public ConceptPredicateObjectListContext conceptPredicateObjectList() {
			return getRuleContext(ConceptPredicateObjectListContext.class,0);
		}
		public TerminalNode EOF() { return getToken(IMLangParser.EOF, 0); }
		public List<DirectiveContext> directive() {
			return getRuleContexts(DirectiveContext.class);
		}
		public DirectiveContext directive(int i) {
			return getRuleContext(DirectiveContext.class,i);
		}
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
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(82);
					directive();
					}
					} 
				}
				setState(87);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(88);
			iri();
			setState(89);
			conceptPredicateObjectList();
			setState(90);
			match(T__0);
			setState(91);
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

	public static class DirectiveContext extends ParserRuleContext {
		public PrefixIDContext prefixID() {
			return getRuleContext(PrefixIDContext.class,0);
		}
		public DirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_directive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitDirective(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitDirective(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DirectiveContext directive() throws RecognitionException {
		DirectiveContext _localctx = new DirectiveContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_directive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			prefixID();
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

	public static class PrefixIDContext extends ParserRuleContext {
		public TerminalNode PNAME_NS() { return getToken(IMLangParser.PNAME_NS, 0); }
		public TerminalNode IRIREF() { return getToken(IMLangParser.IRIREF, 0); }
		public PrefixIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterPrefixID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitPrefixID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitPrefixID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixIDContext prefixID() throws RecognitionException {
		PrefixIDContext _localctx = new PrefixIDContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_prefixID);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(T__1);
			setState(96);
			match(PNAME_NS);
			setState(97);
			match(IRIREF);
			setState(98);
			match(T__0);
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

	public static class ConceptPredicateObjectListContext extends ParserRuleContext {
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public List<PredicateIriContext> predicateIri() {
			return getRuleContexts(PredicateIriContext.class);
		}
		public PredicateIriContext predicateIri(int i) {
			return getRuleContext(PredicateIriContext.class,i);
		}
		public List<AxiomContext> axiom() {
			return getRuleContexts(AxiomContext.class);
		}
		public AxiomContext axiom(int i) {
			return getRuleContext(AxiomContext.class,i);
		}
		public List<PropertiesContext> properties() {
			return getRuleContexts(PropertiesContext.class);
		}
		public PropertiesContext properties(int i) {
			return getRuleContext(PropertiesContext.class,i);
		}
		public List<MembersContext> members() {
			return getRuleContexts(MembersContext.class);
		}
		public MembersContext members(int i) {
			return getRuleContext(MembersContext.class,i);
		}
		public ConceptPredicateObjectListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conceptPredicateObjectList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterConceptPredicateObjectList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitConceptPredicateObjectList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitConceptPredicateObjectList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConceptPredicateObjectListContext conceptPredicateObjectList() throws RecognitionException {
		ConceptPredicateObjectListContext _localctx = new ConceptPredicateObjectListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_conceptPredicateObjectList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VERSION:
			case NAME:
			case DESCRIPTION:
			case CODE:
				{
				setState(100);
				annotation();
				}
				break;
			case STATUS:
			case TYPE:
			case TARGETCLASS:
			case SCHEME:
				{
				setState(101);
				predicateIri();
				}
				break;
			case SUBCLASS:
			case EQUIVALENTTO:
			case SUBPROPERTY:
			case INVERSE:
				{
				setState(102);
				axiom();
				}
				break;
			case PROPERTIES:
				{
				setState(103);
				properties();
				}
				break;
			case MEMBERS:
				{
				setState(104);
				members();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(107);
				match(T__2);
				setState(113);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case VERSION:
				case NAME:
				case DESCRIPTION:
				case CODE:
					{
					setState(108);
					annotation();
					}
					break;
				case STATUS:
				case TYPE:
				case TARGETCLASS:
				case SCHEME:
					{
					setState(109);
					predicateIri();
					}
					break;
				case SUBCLASS:
				case EQUIVALENTTO:
				case SUBPROPERTY:
				case INVERSE:
					{
					setState(110);
					axiom();
					}
					break;
				case PROPERTIES:
					{
					setState(111);
					properties();
					}
					break;
				case MEMBERS:
					{
					setState(112);
					members();
					}
					break;
				case T__0:
				case T__2:
					break;
				default:
					break;
				}
				}
				}
				setState(119);
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
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public CodeContext code() {
			return getRuleContext(CodeContext.class,0);
		}
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public AnnotationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterAnnotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitAnnotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitAnnotation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationContext annotation() throws RecognitionException {
		AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_annotation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAME:
				{
				setState(120);
				name();
				}
				break;
			case DESCRIPTION:
				{
				setState(121);
				description();
				}
				break;
			case CODE:
				{
				setState(122);
				code();
				}
				break;
			case VERSION:
				{
				setState(123);
				version();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(126);
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

	public static class PredicateIriContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public SchemeContext scheme() {
			return getRuleContext(SchemeContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public StatusContext status() {
			return getRuleContext(StatusContext.class,0);
		}
		public TargetContext target() {
			return getRuleContext(TargetContext.class,0);
		}
		public PredicateIriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateIri; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterPredicateIri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitPredicateIri(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitPredicateIri(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateIriContext predicateIri() throws RecognitionException {
		PredicateIriContext _localctx = new PredicateIriContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_predicateIri);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SCHEME:
				{
				setState(128);
				scheme();
				}
				break;
			case TYPE:
				{
				setState(129);
				type();
				}
				break;
			case STATUS:
				{
				setState(130);
				status();
				}
				break;
			case TARGETCLASS:
				{
				setState(131);
				target();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(134);
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

	public static class SchemeContext extends ParserRuleContext {
		public TerminalNode SCHEME() { return getToken(IMLangParser.SCHEME, 0); }
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
		enterRule(_localctx, 12, RULE_scheme);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			match(SCHEME);
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
		enterRule(_localctx, 14, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(TYPE);
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
		enterRule(_localctx, 16, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			match(VERSION);
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

	public static class AxiomContext extends ParserRuleContext {
		public SubclassOfContext subclassOf() {
			return getRuleContext(SubclassOfContext.class,0);
		}
		public EquivalentToContext equivalentTo() {
			return getRuleContext(EquivalentToContext.class,0);
		}
		public SubpropertyOfContext subpropertyOf() {
			return getRuleContext(SubpropertyOfContext.class,0);
		}
		public InverseOfContext inverseOf() {
			return getRuleContext(InverseOfContext.class,0);
		}
		public AxiomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_axiom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterAxiom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitAxiom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitAxiom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AxiomContext axiom() throws RecognitionException {
		AxiomContext _localctx = new AxiomContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_axiom);
		try {
			setState(146);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBCLASS:
				enterOuterAlt(_localctx, 1);
				{
				setState(142);
				subclassOf();
				}
				break;
			case EQUIVALENTTO:
				enterOuterAlt(_localctx, 2);
				{
				setState(143);
				equivalentTo();
				}
				break;
			case SUBPROPERTY:
				enterOuterAlt(_localctx, 3);
				{
				setState(144);
				subpropertyOf();
				}
				break;
			case INVERSE:
				enterOuterAlt(_localctx, 4);
				{
				setState(145);
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

	public static class PropertiesContext extends ParserRuleContext {
		public TerminalNode PROPERTIES() { return getToken(IMLangParser.PROPERTIES, 0); }
		public List<PropertyRestrictionContext> propertyRestriction() {
			return getRuleContexts(PropertyRestrictionContext.class);
		}
		public PropertyRestrictionContext propertyRestriction(int i) {
			return getRuleContext(PropertyRestrictionContext.class,i);
		}
		public PropertiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_properties; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterProperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitProperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitProperties(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertiesContext properties() throws RecognitionException {
		PropertiesContext _localctx = new PropertiesContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_properties);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(PROPERTIES);
			setState(149);
			match(T__3);
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRIREF || _la==PNAME_LN) {
				{
				setState(150);
				propertyRestriction();
				}
			}

			setState(157);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(153);
					match(T__4);
					setState(154);
					propertyRestriction();
					}
					} 
				}
				setState(159);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
			setState(160);
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

	public static class MembersContext extends ParserRuleContext {
		public TerminalNode MEMBERS() { return getToken(IMLangParser.MEMBERS, 0); }
		public List<ClassExpressionContext> classExpression() {
			return getRuleContexts(ClassExpressionContext.class);
		}
		public ClassExpressionContext classExpression(int i) {
			return getRuleContext(ClassExpressionContext.class,i);
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
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(MEMBERS);
			setState(163);
			match(T__3);
			setState(165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRIREF || _la==PNAME_LN) {
				{
				setState(164);
				classExpression();
				}
			}

			setState(171);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(167);
					match(T__4);
					setState(168);
					classExpression();
					}
					} 
				}
				setState(173);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(174);
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

	public static class TargetContext extends ParserRuleContext {
		public TerminalNode TARGETCLASS() { return getToken(IMLangParser.TARGETCLASS, 0); }
		public TargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_target; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitTarget(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TargetContext target() throws RecognitionException {
		TargetContext _localctx = new TargetContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_target);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			match(TARGETCLASS);
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
		enterRule(_localctx, 26, RULE_minInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			match(MININCLUSIVE);
			setState(179);
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
		enterRule(_localctx, 28, RULE_maxInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			match(MAXINCLUSIVE);
			setState(182);
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
		enterRule(_localctx, 30, RULE_minExclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184);
			match(MINEXCLUSIVE);
			setState(185);
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
		enterRule(_localctx, 32, RULE_maxExclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			match(MAXEXCLUSIVE);
			setState(188);
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

	public static class StatusContext extends ParserRuleContext {
		public TerminalNode STATUS() { return getToken(IMLangParser.STATUS, 0); }
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
		enterRule(_localctx, 34, RULE_status);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(STATUS);
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
		enterRule(_localctx, 36, RULE_subclassOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(192);
			match(SUBCLASS);
			setState(193);
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
		enterRule(_localctx, 38, RULE_equivalentTo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(EQUIVALENTTO);
			setState(196);
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
		enterRule(_localctx, 40, RULE_subpropertyOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			match(SUBPROPERTY);
			setState(199);
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
		enterRule(_localctx, 42, RULE_inverseOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			match(INVERSE);
			setState(202);
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
		public IntersectionContext intersection() {
			return getRuleContext(IntersectionContext.class,0);
		}
		public UnionContext union() {
			return getRuleContext(UnionContext.class,0);
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
		enterRule(_localctx, 44, RULE_classExpression);
		try {
			setState(207);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(204);
				iri();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(205);
				intersection();
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(206);
				union();
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

	public static class IntersectionContext extends ParserRuleContext {
		public List<IriContext> iri() {
			return getRuleContexts(IriContext.class);
		}
		public IriContext iri(int i) {
			return getRuleContext(IriContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(IMLangParser.AND); }
		public TerminalNode AND(int i) {
			return getToken(IMLangParser.AND, i);
		}
		public List<PropertyRestrictionContext> propertyRestriction() {
			return getRuleContexts(PropertyRestrictionContext.class);
		}
		public PropertyRestrictionContext propertyRestriction(int i) {
			return getRuleContext(PropertyRestrictionContext.class,i);
		}
		public List<UnionContext> union() {
			return getRuleContexts(UnionContext.class);
		}
		public UnionContext union(int i) {
			return getRuleContext(UnionContext.class,i);
		}
		public List<ComplementContext> complement() {
			return getRuleContexts(ComplementContext.class);
		}
		public ComplementContext complement(int i) {
			return getRuleContext(ComplementContext.class,i);
		}
		public List<SubExpressionContext> subExpression() {
			return getRuleContexts(SubExpressionContext.class);
		}
		public SubExpressionContext subExpression(int i) {
			return getRuleContext(SubExpressionContext.class,i);
		}
		public IntersectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intersection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterIntersection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitIntersection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitIntersection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntersectionContext intersection() throws RecognitionException {
		IntersectionContext _localctx = new IntersectionContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_intersection);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			iri();
			setState(218); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(210);
					match(AND);
					setState(216);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
					case 1:
						{
						setState(211);
						iri();
						}
						break;
					case 2:
						{
						setState(212);
						propertyRestriction();
						}
						break;
					case 3:
						{
						setState(213);
						union();
						}
						break;
					case 4:
						{
						setState(214);
						complement();
						}
						break;
					case 5:
						{
						setState(215);
						subExpression();
						}
						break;
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(220); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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

	public static class SubExpressionContext extends ParserRuleContext {
		public UnionContext union() {
			return getRuleContext(UnionContext.class,0);
		}
		public IntersectionContext intersection() {
			return getRuleContext(IntersectionContext.class,0);
		}
		public ComplementContext complement() {
			return getRuleContext(ComplementContext.class,0);
		}
		public PropertyRestrictionContext propertyRestriction() {
			return getRuleContext(PropertyRestrictionContext.class,0);
		}
		public SubExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterSubExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitSubExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitSubExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubExpressionContext subExpression() throws RecognitionException {
		SubExpressionContext _localctx = new SubExpressionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_subExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			match(T__6);
			setState(227);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(223);
				union();
				}
				break;
			case 2:
				{
				setState(224);
				intersection();
				}
				break;
			case 3:
				{
				setState(225);
				complement();
				}
				break;
			case 4:
				{
				setState(226);
				propertyRestriction();
				}
				break;
			}
			setState(229);
			match(T__7);
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

	public static class UnionContext extends ParserRuleContext {
		public List<IriContext> iri() {
			return getRuleContexts(IriContext.class);
		}
		public IriContext iri(int i) {
			return getRuleContext(IriContext.class,i);
		}
		public List<TerminalNode> OR() { return getTokens(IMLangParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(IMLangParser.OR, i);
		}
		public List<PropertyRestrictionContext> propertyRestriction() {
			return getRuleContexts(PropertyRestrictionContext.class);
		}
		public PropertyRestrictionContext propertyRestriction(int i) {
			return getRuleContext(PropertyRestrictionContext.class,i);
		}
		public UnionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_union; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterUnion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitUnion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitUnion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionContext union() throws RecognitionException {
		UnionContext _localctx = new UnionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_union);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			iri();
			setState(237); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(232);
				match(OR);
				setState(235);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
				case 1:
					{
					setState(233);
					iri();
					}
					break;
				case 2:
					{
					setState(234);
					propertyRestriction();
					}
					break;
				}
				}
				}
				setState(239); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==OR );
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

	public static class ComplementContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(IMLangParser.NOT, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public IntersectionContext intersection() {
			return getRuleContext(IntersectionContext.class,0);
		}
		public UnionContext union() {
			return getRuleContext(UnionContext.class,0);
		}
		public ComplementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterComplement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitComplement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitComplement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComplementContext complement() throws RecognitionException {
		ComplementContext _localctx = new ComplementContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_complement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(241);
			match(NOT);
			setState(245);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(242);
				iri();
				}
				break;
			case 2:
				{
				setState(243);
				intersection();
				}
				break;
			case 3:
				{
				setState(244);
				union();
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

	public static class IriContext extends ParserRuleContext {
		public TerminalNode IRIREF() { return getToken(IMLangParser.IRIREF, 0); }
		public TerminalNode PNAME_LN() { return getToken(IMLangParser.PNAME_LN, 0); }
		public TerminalNode PIPED_STRING() { return getToken(IMLangParser.PIPED_STRING, 0); }
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
		enterRule(_localctx, 54, RULE_iri);
		int _la;
		try {
			setState(252);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRIREF:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				match(IRIREF);
				}
				break;
			case PNAME_LN:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(248);
				match(PNAME_LN);
				setState(250);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PIPED_STRING) {
					{
					setState(249);
					match(PIPED_STRING);
					}
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

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
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

	public static class PropertyRestrictionContext extends ParserRuleContext {
		public PropertyIriContext propertyIri() {
			return getRuleContext(PropertyIriContext.class,0);
		}
		public ClassOrDataTypeContext classOrDataType() {
			return getRuleContext(ClassOrDataTypeContext.class,0);
		}
		public ExactCardinalityContext exactCardinality() {
			return getRuleContext(ExactCardinalityContext.class,0);
		}
		public RangeCardinalityContext rangeCardinality() {
			return getRuleContext(RangeCardinalityContext.class,0);
		}
		public MinCardinalityContext minCardinality() {
			return getRuleContext(MinCardinalityContext.class,0);
		}
		public MaxCardinalityContext maxCardinality() {
			return getRuleContext(MaxCardinalityContext.class,0);
		}
		public SomeContext some() {
			return getRuleContext(SomeContext.class,0);
		}
		public OnlyContext only() {
			return getRuleContext(OnlyContext.class,0);
		}
		public PropertyRestrictionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyRestriction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterPropertyRestriction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitPropertyRestriction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitPropertyRestriction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyRestrictionContext propertyRestriction() throws RecognitionException {
		PropertyRestrictionContext _localctx = new PropertyRestrictionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_propertyRestriction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			propertyIri();
			setState(263);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(257);
				exactCardinality();
				}
				break;
			case 2:
				{
				setState(258);
				rangeCardinality();
				}
				break;
			case 3:
				{
				setState(259);
				minCardinality();
				}
				break;
			case 4:
				{
				setState(260);
				maxCardinality();
				}
				break;
			case 5:
				{
				setState(261);
				some();
				}
				break;
			case 6:
				{
				setState(262);
				only();
				}
				break;
			}
			setState(265);
			classOrDataType();
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

	public static class SomeContext extends ParserRuleContext {
		public TerminalNode SOME() { return getToken(IMLangParser.SOME, 0); }
		public SomeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_some; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterSome(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitSome(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitSome(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SomeContext some() throws RecognitionException {
		SomeContext _localctx = new SomeContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_some);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			match(SOME);
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

	public static class OnlyContext extends ParserRuleContext {
		public TerminalNode ONLY() { return getToken(IMLangParser.ONLY, 0); }
		public OnlyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_only; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterOnly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitOnly(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitOnly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OnlyContext only() throws RecognitionException {
		OnlyContext _localctx = new OnlyContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_only);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			match(ONLY);
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

	public static class PropertyIriContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public PropertyIriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyIri; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterPropertyIri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitPropertyIri(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitPropertyIri(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyIriContext propertyIri() throws RecognitionException {
		PropertyIriContext _localctx = new PropertyIriContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_propertyIri);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
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

	public static class ExactCardinalityContext extends ParserRuleContext {
		public TerminalNode EXACTLY() { return getToken(IMLangParser.EXACTLY, 0); }
		public TerminalNode INTEGER() { return getToken(IMLangParser.INTEGER, 0); }
		public ExactCardinalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exactCardinality; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterExactCardinality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitExactCardinality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitExactCardinality(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExactCardinalityContext exactCardinality() throws RecognitionException {
		ExactCardinalityContext _localctx = new ExactCardinalityContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_exactCardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			match(EXACTLY);
			setState(274);
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

	public static class RangeCardinalityContext extends ParserRuleContext {
		public MinCardinalityContext minCardinality() {
			return getRuleContext(MinCardinalityContext.class,0);
		}
		public MaxCardinalityContext maxCardinality() {
			return getRuleContext(MaxCardinalityContext.class,0);
		}
		public RangeCardinalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeCardinality; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterRangeCardinality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitRangeCardinality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitRangeCardinality(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeCardinalityContext rangeCardinality() throws RecognitionException {
		RangeCardinalityContext _localctx = new RangeCardinalityContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_rangeCardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			minCardinality();
			setState(277);
			maxCardinality();
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

	public static class MinCardinalityContext extends ParserRuleContext {
		public TerminalNode MIN() { return getToken(IMLangParser.MIN, 0); }
		public TerminalNode INTEGER() { return getToken(IMLangParser.INTEGER, 0); }
		public MinCardinalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_minCardinality; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMinCardinality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMinCardinality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMinCardinality(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MinCardinalityContext minCardinality() throws RecognitionException {
		MinCardinalityContext _localctx = new MinCardinalityContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_minCardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			match(MIN);
			setState(280);
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

	public static class MaxCardinalityContext extends ParserRuleContext {
		public TerminalNode MAX() { return getToken(IMLangParser.MAX, 0); }
		public TerminalNode INTEGER() { return getToken(IMLangParser.INTEGER, 0); }
		public MaxCardinalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_maxCardinality; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMaxCardinality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMaxCardinality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMaxCardinality(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaxCardinalityContext maxCardinality() throws RecognitionException {
		MaxCardinalityContext _localctx = new MaxCardinalityContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_maxCardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			match(MAX);
			setState(283);
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

	public static class ClassOrDataTypeContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public ClassOrDataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrDataType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterClassOrDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitClassOrDataType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitClassOrDataType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassOrDataTypeContext classOrDataType() throws RecognitionException {
		ClassOrDataTypeContext _localctx = new ClassOrDataTypeContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_classOrDataType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
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

	public static class NameContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(IMLangParser.NAME, 0); }
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
		enterRule(_localctx, 76, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(287);
			match(NAME);
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
		enterRule(_localctx, 78, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			match(DESCRIPTION);
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
		enterRule(_localctx, 80, RULE_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(291);
			match(CODE);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3<\u0128\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\3\2\7\2"+
		"V\n\2\f\2\16\2Y\13\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3"+
		"\5\3\5\3\5\3\5\3\5\5\5l\n\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5t\n\5\7\5v\n\5"+
		"\f\5\16\5y\13\5\3\6\3\6\3\6\3\6\5\6\177\n\6\3\6\3\6\3\7\3\7\3\7\3\7\5"+
		"\7\u0087\n\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\5\13"+
		"\u0095\n\13\3\f\3\f\3\f\5\f\u009a\n\f\3\f\3\f\7\f\u009e\n\f\f\f\16\f\u00a1"+
		"\13\f\3\f\3\f\3\r\3\r\3\r\5\r\u00a8\n\r\3\r\3\r\7\r\u00ac\n\r\f\r\16\r"+
		"\u00af\13\r\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\22\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26"+
		"\3\26\3\27\3\27\3\27\3\30\3\30\3\30\5\30\u00d2\n\30\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\5\31\u00db\n\31\6\31\u00dd\n\31\r\31\16\31\u00de\3\32"+
		"\3\32\3\32\3\32\3\32\5\32\u00e6\n\32\3\32\3\32\3\33\3\33\3\33\3\33\5\33"+
		"\u00ee\n\33\6\33\u00f0\n\33\r\33\16\33\u00f1\3\34\3\34\3\34\3\34\5\34"+
		"\u00f8\n\34\3\35\3\35\3\35\5\35\u00fd\n\35\5\35\u00ff\n\35\3\36\3\36\3"+
		"\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u010a\n\37\3\37\3\37\3 \3 \3!\3"+
		"!\3\"\3\"\3#\3#\3#\3$\3$\3$\3%\3%\3%\3&\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3"+
		"*\3*\5W\u009f\u00ad\2+\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,"+
		".\60\62\64\668:<>@BDFHJLNPR\2\2\2\u012c\2W\3\2\2\2\4_\3\2\2\2\6a\3\2\2"+
		"\2\bk\3\2\2\2\n~\3\2\2\2\f\u0086\3\2\2\2\16\u008a\3\2\2\2\20\u008c\3\2"+
		"\2\2\22\u008e\3\2\2\2\24\u0094\3\2\2\2\26\u0096\3\2\2\2\30\u00a4\3\2\2"+
		"\2\32\u00b2\3\2\2\2\34\u00b4\3\2\2\2\36\u00b7\3\2\2\2 \u00ba\3\2\2\2\""+
		"\u00bd\3\2\2\2$\u00c0\3\2\2\2&\u00c2\3\2\2\2(\u00c5\3\2\2\2*\u00c8\3\2"+
		"\2\2,\u00cb\3\2\2\2.\u00d1\3\2\2\2\60\u00d3\3\2\2\2\62\u00e0\3\2\2\2\64"+
		"\u00e9\3\2\2\2\66\u00f3\3\2\2\28\u00fe\3\2\2\2:\u0100\3\2\2\2<\u0102\3"+
		"\2\2\2>\u010d\3\2\2\2@\u010f\3\2\2\2B\u0111\3\2\2\2D\u0113\3\2\2\2F\u0116"+
		"\3\2\2\2H\u0119\3\2\2\2J\u011c\3\2\2\2L\u011f\3\2\2\2N\u0121\3\2\2\2P"+
		"\u0123\3\2\2\2R\u0125\3\2\2\2TV\5\4\3\2UT\3\2\2\2VY\3\2\2\2WX\3\2\2\2"+
		"WU\3\2\2\2XZ\3\2\2\2YW\3\2\2\2Z[\58\35\2[\\\5\b\5\2\\]\7\3\2\2]^\7\2\2"+
		"\3^\3\3\2\2\2_`\5\6\4\2`\5\3\2\2\2ab\7\4\2\2bc\7+\2\2cd\7\60\2\2de\7\3"+
		"\2\2e\7\3\2\2\2fl\5\n\6\2gl\5\f\7\2hl\5\24\13\2il\5\26\f\2jl\5\30\r\2"+
		"kf\3\2\2\2kg\3\2\2\2kh\3\2\2\2ki\3\2\2\2kj\3\2\2\2lw\3\2\2\2ms\7\5\2\2"+
		"nt\5\n\6\2ot\5\f\7\2pt\5\24\13\2qt\5\26\f\2rt\5\30\r\2sn\3\2\2\2so\3\2"+
		"\2\2sp\3\2\2\2sq\3\2\2\2sr\3\2\2\2st\3\2\2\2tv\3\2\2\2um\3\2\2\2vy\3\2"+
		"\2\2wu\3\2\2\2wx\3\2\2\2x\t\3\2\2\2yw\3\2\2\2z\177\5N(\2{\177\5P)\2|\177"+
		"\5R*\2}\177\5\22\n\2~z\3\2\2\2~{\3\2\2\2~|\3\2\2\2~}\3\2\2\2\177\u0080"+
		"\3\2\2\2\u0080\u0081\7\67\2\2\u0081\13\3\2\2\2\u0082\u0087\5\16\b\2\u0083"+
		"\u0087\5\20\t\2\u0084\u0087\5$\23\2\u0085\u0087\5\32\16\2\u0086\u0082"+
		"\3\2\2\2\u0086\u0083\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0085\3\2\2\2\u0087"+
		"\u0088\3\2\2\2\u0088\u0089\58\35\2\u0089\r\3\2\2\2\u008a\u008b\7*\2\2"+
		"\u008b\17\3\2\2\2\u008c\u008d\7\21\2\2\u008d\21\3\2\2\2\u008e\u008f\7"+
		"\17\2\2\u008f\23\3\2\2\2\u0090\u0095\5&\24\2\u0091\u0095\5(\25\2\u0092"+
		"\u0095\5*\26\2\u0093\u0095\5,\27\2\u0094\u0090\3\2\2\2\u0094\u0091\3\2"+
		"\2\2\u0094\u0092\3\2\2\2\u0094\u0093\3\2\2\2\u0095\25\3\2\2\2\u0096\u0097"+
		"\7\20\2\2\u0097\u0099\7\6\2\2\u0098\u009a\5<\37\2\u0099\u0098\3\2\2\2"+
		"\u0099\u009a\3\2\2\2\u009a\u009f\3\2\2\2\u009b\u009c\7\7\2\2\u009c\u009e"+
		"\5<\37\2\u009d\u009b\3\2\2\2\u009e\u00a1\3\2\2\2\u009f\u00a0\3\2\2\2\u009f"+
		"\u009d\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u009f\3\2\2\2\u00a2\u00a3\7\b"+
		"\2\2\u00a3\27\3\2\2\2\u00a4\u00a5\7\r\2\2\u00a5\u00a7\7\6\2\2\u00a6\u00a8"+
		"\5.\30\2\u00a7\u00a6\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00ad\3\2\2\2\u00a9"+
		"\u00aa\7\7\2\2\u00aa\u00ac\5.\30\2\u00ab\u00a9\3\2\2\2\u00ac\u00af\3\2"+
		"\2\2\u00ad\u00ae\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ae\u00b0\3\2\2\2\u00af"+
		"\u00ad\3\2\2\2\u00b0\u00b1\7\b\2\2\u00b1\31\3\2\2\2\u00b2\u00b3\7\37\2"+
		"\2\u00b3\33\3\2\2\2\u00b4\u00b5\7\26\2\2\u00b5\u00b6\7#\2\2\u00b6\35\3"+
		"\2\2\2\u00b7\u00b8\7\27\2\2\u00b8\u00b9\7#\2\2\u00b9\37\3\2\2\2\u00ba"+
		"\u00bb\7\30\2\2\u00bb\u00bc\7#\2\2\u00bc!\3\2\2\2\u00bd\u00be\7\31\2\2"+
		"\u00be\u00bf\7#\2\2\u00bf#\3\2\2\2\u00c0\u00c1\7\16\2\2\u00c1%\3\2\2\2"+
		"\u00c2\u00c3\7\32\2\2\u00c3\u00c4\5.\30\2\u00c4\'\3\2\2\2\u00c5\u00c6"+
		"\7\33\2\2\u00c6\u00c7\5.\30\2\u00c7)\3\2\2\2\u00c8\u00c9\7\35\2\2\u00c9"+
		"\u00ca\58\35\2\u00ca+\3\2\2\2\u00cb\u00cc\7\36\2\2\u00cc\u00cd\58\35\2"+
		"\u00cd-\3\2\2\2\u00ce\u00d2\58\35\2\u00cf\u00d2\5\60\31\2\u00d0\u00d2"+
		"\5\64\33\2\u00d1\u00ce\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d0\3\2\2\2"+
		"\u00d2/\3\2\2\2\u00d3\u00dc\58\35\2\u00d4\u00da\7!\2\2\u00d5\u00db\58"+
		"\35\2\u00d6\u00db\5<\37\2\u00d7\u00db\5\64\33\2\u00d8\u00db\5\66\34\2"+
		"\u00d9\u00db\5\62\32\2\u00da\u00d5\3\2\2\2\u00da\u00d6\3\2\2\2\u00da\u00d7"+
		"\3\2\2\2\u00da\u00d8\3\2\2\2\u00da\u00d9\3\2\2\2\u00db\u00dd\3\2\2\2\u00dc"+
		"\u00d4\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00dc\3\2\2\2\u00de\u00df\3\2"+
		"\2\2\u00df\61\3\2\2\2\u00e0\u00e5\7\t\2\2\u00e1\u00e6\5\64\33\2\u00e2"+
		"\u00e6\5\60\31\2\u00e3\u00e6\5\66\34\2\u00e4\u00e6\5<\37\2\u00e5\u00e1"+
		"\3\2\2\2\u00e5\u00e2\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e5\u00e4\3\2\2\2\u00e6"+
		"\u00e7\3\2\2\2\u00e7\u00e8\7\n\2\2\u00e8\63\3\2\2\2\u00e9\u00ef\58\35"+
		"\2\u00ea\u00ed\7%\2\2\u00eb\u00ee\58\35\2\u00ec\u00ee\5<\37\2\u00ed\u00eb"+
		"\3\2\2\2\u00ed\u00ec\3\2\2\2\u00ee\u00f0\3\2\2\2\u00ef\u00ea\3\2\2\2\u00f0"+
		"\u00f1\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\65\3\2\2"+
		"\2\u00f3\u00f7\7&\2\2\u00f4\u00f8\58\35\2\u00f5\u00f8\5\60\31\2\u00f6"+
		"\u00f8\5\64\33\2\u00f7\u00f4\3\2\2\2\u00f7\u00f5\3\2\2\2\u00f7\u00f6\3"+
		"\2\2\2\u00f8\67\3\2\2\2\u00f9\u00ff\7\60\2\2\u00fa\u00fc\7\62\2\2\u00fb"+
		"\u00fd\7:\2\2\u00fc\u00fb\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00ff\3\2"+
		"\2\2\u00fe\u00f9\3\2\2\2\u00fe\u00fa\3\2\2\2\u00ff9\3\2\2\2\u0100\u0101"+
		"\7\67\2\2\u0101;\3\2\2\2\u0102\u0109\5B\"\2\u0103\u010a\5D#\2\u0104\u010a"+
		"\5F$\2\u0105\u010a\5H%\2\u0106\u010a\5J&\2\u0107\u010a\5> \2\u0108\u010a"+
		"\5@!\2\u0109\u0103\3\2\2\2\u0109\u0104\3\2\2\2\u0109\u0105\3\2\2\2\u0109"+
		"\u0106\3\2\2\2\u0109\u0107\3\2\2\2\u0109\u0108\3\2\2\2\u0109\u010a\3\2"+
		"\2\2\u010a\u010b\3\2\2\2\u010b\u010c\5L\'\2\u010c=\3\2\2\2\u010d\u010e"+
		"\7\24\2\2\u010e?\3\2\2\2\u010f\u0110\7\25\2\2\u0110A\3\2\2\2\u0111\u0112"+
		"\58\35\2\u0112C\3\2\2\2\u0113\u0114\7 \2\2\u0114\u0115\7\"\2\2\u0115E"+
		"\3\2\2\2\u0116\u0117\5H%\2\u0117\u0118\5J&\2\u0118G\3\2\2\2\u0119\u011a"+
		"\7\22\2\2\u011a\u011b\7\"\2\2\u011bI\3\2\2\2\u011c\u011d\7\23\2\2\u011d"+
		"\u011e\7\"\2\2\u011eK\3\2\2\2\u011f\u0120\58\35\2\u0120M\3\2\2\2\u0121"+
		"\u0122\7\'\2\2\u0122O\3\2\2\2\u0123\u0124\7(\2\2\u0124Q\3\2\2\2\u0125"+
		"\u0126\7)\2\2\u0126S\3\2\2\2\27Wksw~\u0086\u0094\u0099\u009f\u00a7\u00ad"+
		"\u00d1\u00da\u00de\u00e5\u00ed\u00f1\u00f7\u00fc\u00fe\u0109";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}