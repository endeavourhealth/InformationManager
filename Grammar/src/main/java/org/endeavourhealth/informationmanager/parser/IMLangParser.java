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
		WS=10, EQ=11, MEMBERS=12, NOTMEMBERS=13, STATUS=14, VERSION=15, PROPERTIES=16, 
		TYPE=17, MIN=18, MAX=19, SOME=20, ONLY=21, MININCLUSIVE=22, MAXINCLUSIVE=23, 
		MINEXCLUSIVE=24, MAXEXCLUSIVE=25, SUBCLASS=26, EQUIVALENTTO=27, DISJOINT=28, 
		SUBPROPERTY=29, INVERSE=30, DOMAIN=31, RANGE=32, TARGETCLASS=33, EXACTLY=34, 
		AND=35, INTEGER=36, DOUBLE=37, DIGIT=38, OR=39, NOT=40, IRI=41, NAME=42, 
		DESCRIPTION=43, CODE=44, SCHEME=45, PNAME_NS=46, PN_PREFIX=47, PN_CHARS_BASE=48, 
		PN_CHARS_U=49, PN_CHARS=50, IRIREF=51, UCHAR=52, PNAME_LN=53, PN_LOCAL=54, 
		PLX=55, PERCENT=56, ECHAR=57, QUOTED_STRING=58, STRING_LITERAL_QUOTE=59, 
		STRING_LITERAL_SINGLE_QUOTE=60, PIPED_STRING=61, PN_LOCAL_ESC=62, HEX=63;
	public static final int
		RULE_concept = 0, RULE_directive = 1, RULE_prefixID = 2, RULE_iriLabel = 3, 
		RULE_annotationList = 4, RULE_conceptPredicateObjectList = 5, RULE_annotation = 6, 
		RULE_scheme = 7, RULE_types = 8, RULE_version = 9, RULE_axiom = 10, RULE_properties = 11, 
		RULE_membership = 12, RULE_members = 13, RULE_notmembers = 14, RULE_target = 15, 
		RULE_minInclusive = 16, RULE_maxInclusive = 17, RULE_minExclusive = 18, 
		RULE_maxExclusive = 19, RULE_status = 20, RULE_subclassOf = 21, RULE_equivalentTo = 22, 
		RULE_subpropertyOf = 23, RULE_inverseOf = 24, RULE_domain = 25, RULE_range = 26, 
		RULE_classExpression = 27, RULE_intersection = 28, RULE_subExpression = 29, 
		RULE_union = 30, RULE_complement = 31, RULE_iri = 32, RULE_literal = 33, 
		RULE_propertyRestriction = 34, RULE_some = 35, RULE_only = 36, RULE_propertyIri = 37, 
		RULE_exactCardinality = 38, RULE_rangeCardinality = 39, RULE_minCardinality = 40, 
		RULE_maxCardinality = 41, RULE_classOrDataType = 42, RULE_name = 43, RULE_description = 44, 
		RULE_code = 45;
	private static String[] makeRuleNames() {
		return new String[] {
			"concept", "directive", "prefixID", "iriLabel", "annotationList", "conceptPredicateObjectList", 
			"annotation", "scheme", "types", "version", "axiom", "properties", "membership", 
			"members", "notmembers", "target", "minInclusive", "maxInclusive", "minExclusive", 
			"maxExclusive", "status", "subclassOf", "equivalentTo", "subpropertyOf", 
			"inverseOf", "domain", "range", "classExpression", "intersection", "subExpression", 
			"union", "complement", "iri", "literal", "propertyRestriction", "some", 
			"only", "propertyIri", "exactCardinality", "rangeCardinality", "minCardinality", 
			"maxCardinality", "classOrDataType", "name", "description", "code"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'@prefix'", "';'", "':'", "','", "'['", "']'", "'('", "')'", 
			null, "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "WS", "EQ", 
			"MEMBERS", "NOTMEMBERS", "STATUS", "VERSION", "PROPERTIES", "TYPE", "MIN", 
			"MAX", "SOME", "ONLY", "MININCLUSIVE", "MAXINCLUSIVE", "MINEXCLUSIVE", 
			"MAXEXCLUSIVE", "SUBCLASS", "EQUIVALENTTO", "DISJOINT", "SUBPROPERTY", 
			"INVERSE", "DOMAIN", "RANGE", "TARGETCLASS", "EXACTLY", "AND", "INTEGER", 
			"DOUBLE", "DIGIT", "OR", "NOT", "IRI", "NAME", "DESCRIPTION", "CODE", 
			"SCHEME", "PNAME_NS", "PN_PREFIX", "PN_CHARS_BASE", "PN_CHARS_U", "PN_CHARS", 
			"IRIREF", "UCHAR", "PNAME_LN", "PN_LOCAL", "PLX", "PERCENT", "ECHAR", 
			"QUOTED_STRING", "STRING_LITERAL_QUOTE", "STRING_LITERAL_SINGLE_QUOTE", 
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
		public IriLabelContext iriLabel() {
			return getRuleContext(IriLabelContext.class,0);
		}
		public TypesContext types() {
			return getRuleContext(TypesContext.class,0);
		}
		public AnnotationListContext annotationList() {
			return getRuleContext(AnnotationListContext.class,0);
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
			setState(95);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(92);
					directive();
					}
					} 
				}
				setState(97);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(98);
			iriLabel();
			setState(99);
			types();
			setState(100);
			annotationList();
			setState(101);
			conceptPredicateObjectList();
			setState(102);
			match(T__0);
			setState(103);
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
			setState(105);
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
			setState(107);
			match(T__1);
			setState(108);
			match(PNAME_NS);
			setState(109);
			match(IRIREF);
			setState(110);
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

	public static class IriLabelContext extends ParserRuleContext {
		public TerminalNode IRI() { return getToken(IMLangParser.IRI, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public IriLabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iriLabel; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterIriLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitIriLabel(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitIriLabel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IriLabelContext iriLabel() throws RecognitionException {
		IriLabelContext _localctx = new IriLabelContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_iriLabel);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(IRI);
			setState(113);
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

	public static class AnnotationListContext extends ParserRuleContext {
		public List<AnnotationContext> annotation() {
			return getRuleContexts(AnnotationContext.class);
		}
		public AnnotationContext annotation(int i) {
			return getRuleContext(AnnotationContext.class,i);
		}
		public AnnotationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterAnnotationList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitAnnotationList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitAnnotationList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationListContext annotationList() throws RecognitionException {
		AnnotationListContext _localctx = new AnnotationListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_annotationList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(117); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(115);
					match(T__2);
					setState(116);
					annotation();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(119); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
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

	public static class ConceptPredicateObjectListContext extends ParserRuleContext {
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
		public List<MembershipContext> membership() {
			return getRuleContexts(MembershipContext.class);
		}
		public MembershipContext membership(int i) {
			return getRuleContext(MembershipContext.class,i);
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
		enterRule(_localctx, 10, RULE_conceptPredicateObjectList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(121);
				match(T__2);
				setState(125);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SUBCLASS:
				case EQUIVALENTTO:
				case SUBPROPERTY:
				case INVERSE:
				case DOMAIN:
				case RANGE:
					{
					setState(122);
					axiom();
					}
					break;
				case PROPERTIES:
					{
					setState(123);
					properties();
					}
					break;
				case MEMBERS:
					{
					setState(124);
					membership();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(129); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__2 );
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
		public SchemeContext scheme() {
			return getRuleContext(SchemeContext.class,0);
		}
		public StatusContext status() {
			return getRuleContext(StatusContext.class,0);
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
		enterRule(_localctx, 12, RULE_annotation);
		int _la;
		try {
			setState(147);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(136);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NAME:
					{
					setState(131);
					name();
					}
					break;
				case DESCRIPTION:
					{
					setState(132);
					description();
					}
					break;
				case CODE:
					{
					setState(133);
					code();
					}
					break;
				case VERSION:
					{
					setState(134);
					version();
					}
					break;
				case SCHEME:
					{
					setState(135);
					scheme();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(138);
					match(T__3);
					}
				}

				setState(141);
				match(QUOTED_STRING);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(145);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case SCHEME:
					{
					setState(143);
					scheme();
					}
					break;
				case STATUS:
					{
					setState(144);
					status();
					}
					break;
				default:
					throw new NoViableAltException(this);
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

	public static class SchemeContext extends ParserRuleContext {
		public TerminalNode SCHEME() { return getToken(IMLangParser.SCHEME, 0); }
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
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
		enterRule(_localctx, 14, RULE_scheme);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(SCHEME);
			setState(150);
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

	public static class TypesContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(IMLangParser.TYPE, 0); }
		public List<IriContext> iri() {
			return getRuleContexts(IriContext.class);
		}
		public IriContext iri(int i) {
			return getRuleContext(IriContext.class,i);
		}
		public TypesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_types; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterTypes(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitTypes(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitTypes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypesContext types() throws RecognitionException {
		TypesContext _localctx = new TypesContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_types);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(TYPE);
			setState(153);
			iri();
			setState(158);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(154);
					match(T__4);
					setState(155);
					iri();
					}
					} 
				}
				setState(160);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
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
		enterRule(_localctx, 18, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
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
		public DomainContext domain() {
			return getRuleContext(DomainContext.class,0);
		}
		public RangeContext range() {
			return getRuleContext(RangeContext.class,0);
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
		enterRule(_localctx, 20, RULE_axiom);
		try {
			setState(169);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBCLASS:
				enterOuterAlt(_localctx, 1);
				{
				setState(163);
				subclassOf();
				}
				break;
			case EQUIVALENTTO:
				enterOuterAlt(_localctx, 2);
				{
				setState(164);
				equivalentTo();
				}
				break;
			case SUBPROPERTY:
				enterOuterAlt(_localctx, 3);
				{
				setState(165);
				subpropertyOf();
				}
				break;
			case INVERSE:
				enterOuterAlt(_localctx, 4);
				{
				setState(166);
				inverseOf();
				}
				break;
			case DOMAIN:
				enterOuterAlt(_localctx, 5);
				{
				setState(167);
				domain();
				}
				break;
			case RANGE:
				enterOuterAlt(_localctx, 6);
				{
				setState(168);
				range();
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
		enterRule(_localctx, 22, RULE_properties);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			match(PROPERTIES);
			setState(173);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(172);
				match(T__3);
				}
			}

			setState(175);
			propertyRestriction();
			setState(180);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(176);
					match(T__4);
					setState(177);
					propertyRestriction();
					}
					} 
				}
				setState(182);
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

	public static class MembershipContext extends ParserRuleContext {
		public MembersContext members() {
			return getRuleContext(MembersContext.class,0);
		}
		public List<NotmembersContext> notmembers() {
			return getRuleContexts(NotmembersContext.class);
		}
		public NotmembersContext notmembers(int i) {
			return getRuleContext(NotmembersContext.class,i);
		}
		public MembershipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_membership; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMembership(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMembership(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMembership(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MembershipContext membership() throws RecognitionException {
		MembershipContext _localctx = new MembershipContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_membership);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			members();
			setState(188);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(184);
					match(T__2);
					setState(185);
					notmembers();
					}
					} 
				}
				setState(190);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
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
		enterRule(_localctx, 26, RULE_members);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(MEMBERS);
			setState(192);
			match(T__5);
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRIREF || _la==PNAME_LN) {
				{
				setState(193);
				classExpression();
				}
			}

			setState(200);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(196);
					match(T__4);
					setState(197);
					classExpression();
					}
					} 
				}
				setState(202);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			setState(203);
			match(T__6);
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

	public static class NotmembersContext extends ParserRuleContext {
		public TerminalNode NOTMEMBERS() { return getToken(IMLangParser.NOTMEMBERS, 0); }
		public List<IriContext> iri() {
			return getRuleContexts(IriContext.class);
		}
		public IriContext iri(int i) {
			return getRuleContext(IriContext.class,i);
		}
		public NotmembersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_notmembers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterNotmembers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitNotmembers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitNotmembers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NotmembersContext notmembers() throws RecognitionException {
		NotmembersContext _localctx = new NotmembersContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_notmembers);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(NOTMEMBERS);
			setState(206);
			match(T__5);
			setState(207);
			iri();
			setState(212);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(208);
					match(T__4);
					setState(209);
					iri();
					}
					} 
				}
				setState(214);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			setState(215);
			match(T__6);
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
		enterRule(_localctx, 30, RULE_target);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(217);
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
		enterRule(_localctx, 32, RULE_minInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			match(MININCLUSIVE);
			setState(220);
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
		enterRule(_localctx, 34, RULE_maxInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			match(MAXINCLUSIVE);
			setState(223);
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
		enterRule(_localctx, 36, RULE_minExclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			match(MINEXCLUSIVE);
			setState(226);
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
		enterRule(_localctx, 38, RULE_maxExclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			match(MAXEXCLUSIVE);
			setState(229);
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
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
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
		enterRule(_localctx, 40, RULE_status);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(STATUS);
			setState(232);
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
		enterRule(_localctx, 42, RULE_subclassOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			match(SUBCLASS);
			setState(235);
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
		enterRule(_localctx, 44, RULE_equivalentTo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			match(EQUIVALENTTO);
			setState(238);
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
		enterRule(_localctx, 46, RULE_subpropertyOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			match(SUBPROPERTY);
			setState(241);
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
		enterRule(_localctx, 48, RULE_inverseOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			match(INVERSE);
			setState(244);
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

	public static class DomainContext extends ParserRuleContext {
		public TerminalNode DOMAIN() { return getToken(IMLangParser.DOMAIN, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public DomainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_domain; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterDomain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitDomain(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitDomain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DomainContext domain() throws RecognitionException {
		DomainContext _localctx = new DomainContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_domain);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(DOMAIN);
			setState(247);
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

	public static class RangeContext extends ParserRuleContext {
		public TerminalNode RANGE() { return getToken(IMLangParser.RANGE, 0); }
		public ClassExpressionContext classExpression() {
			return getRuleContext(ClassExpressionContext.class,0);
		}
		public RangeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_range; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterRange(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitRange(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitRange(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeContext range() throws RecognitionException {
		RangeContext _localctx = new RangeContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_range);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(RANGE);
			setState(250);
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
		enterRule(_localctx, 54, RULE_classExpression);
		try {
			setState(255);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(252);
				iri();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(253);
				intersection();
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(254);
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
		enterRule(_localctx, 56, RULE_intersection);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			iri();
			setState(266); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(258);
					match(AND);
					setState(264);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
					case 1:
						{
						setState(259);
						iri();
						}
						break;
					case 2:
						{
						setState(260);
						propertyRestriction();
						}
						break;
					case 3:
						{
						setState(261);
						union();
						}
						break;
					case 4:
						{
						setState(262);
						complement();
						}
						break;
					case 5:
						{
						setState(263);
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
				setState(268); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
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
		enterRule(_localctx, 58, RULE_subExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(270);
			match(T__7);
			setState(275);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				{
				setState(271);
				union();
				}
				break;
			case 2:
				{
				setState(272);
				intersection();
				}
				break;
			case 3:
				{
				setState(273);
				complement();
				}
				break;
			case 4:
				{
				setState(274);
				propertyRestriction();
				}
				break;
			}
			setState(277);
			match(T__8);
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
		enterRule(_localctx, 60, RULE_union);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			iri();
			setState(285); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(280);
				match(OR);
				setState(283);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
				case 1:
					{
					setState(281);
					iri();
					}
					break;
				case 2:
					{
					setState(282);
					propertyRestriction();
					}
					break;
				}
				}
				}
				setState(287); 
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
		enterRule(_localctx, 62, RULE_complement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(289);
			match(NOT);
			setState(293);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(290);
				iri();
				}
				break;
			case 2:
				{
				setState(291);
				intersection();
				}
				break;
			case 3:
				{
				setState(292);
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
		enterRule(_localctx, 64, RULE_iri);
		int _la;
		try {
			setState(300);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRIREF:
				enterOuterAlt(_localctx, 1);
				{
				setState(295);
				match(IRIREF);
				}
				break;
			case PNAME_LN:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(296);
				match(PNAME_LN);
				setState(298);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PIPED_STRING) {
					{
					setState(297);
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
		enterRule(_localctx, 66, RULE_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(302);
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
		enterRule(_localctx, 68, RULE_propertyRestriction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(304);
			propertyIri();
			setState(311);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(305);
				exactCardinality();
				}
				break;
			case 2:
				{
				setState(306);
				rangeCardinality();
				}
				break;
			case 3:
				{
				setState(307);
				minCardinality();
				}
				break;
			case 4:
				{
				setState(308);
				maxCardinality();
				}
				break;
			case 5:
				{
				setState(309);
				some();
				}
				break;
			case 6:
				{
				setState(310);
				only();
				}
				break;
			}
			setState(313);
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
		enterRule(_localctx, 70, RULE_some);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
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
		enterRule(_localctx, 72, RULE_only);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
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
		enterRule(_localctx, 74, RULE_propertyIri);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
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
		enterRule(_localctx, 76, RULE_exactCardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(321);
			match(EXACTLY);
			setState(322);
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
		enterRule(_localctx, 78, RULE_rangeCardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			minCardinality();
			setState(325);
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
		enterRule(_localctx, 80, RULE_minCardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(327);
			match(MIN);
			setState(328);
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
		enterRule(_localctx, 82, RULE_maxCardinality);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			match(MAX);
			setState(331);
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
		enterRule(_localctx, 84, RULE_classOrDataType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
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
		enterRule(_localctx, 86, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
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
		enterRule(_localctx, 88, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
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
		enterRule(_localctx, 90, RULE_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3A\u0158\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\3\2\7\2`\n\2\f\2\16\2c\13\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\6\6x\n\6\r\6"+
		"\16\6y\3\7\3\7\3\7\3\7\5\7\u0080\n\7\6\7\u0082\n\7\r\7\16\7\u0083\3\b"+
		"\3\b\3\b\3\b\3\b\5\b\u008b\n\b\3\b\5\b\u008e\n\b\3\b\3\b\3\b\3\b\5\b\u0094"+
		"\n\b\5\b\u0096\n\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\7\n\u009f\n\n\f\n\16\n"+
		"\u00a2\13\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00ac\n\f\3\r\3\r\5"+
		"\r\u00b0\n\r\3\r\3\r\3\r\7\r\u00b5\n\r\f\r\16\r\u00b8\13\r\3\16\3\16\3"+
		"\16\7\16\u00bd\n\16\f\16\16\16\u00c0\13\16\3\17\3\17\3\17\5\17\u00c5\n"+
		"\17\3\17\3\17\7\17\u00c9\n\17\f\17\16\17\u00cc\13\17\3\17\3\17\3\20\3"+
		"\20\3\20\3\20\3\20\7\20\u00d5\n\20\f\20\16\20\u00d8\13\20\3\20\3\20\3"+
		"\21\3\21\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\24\3\25\3\25\3\25\3"+
		"\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3"+
		"\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\35\5\35\u0102\n\35\3\36"+
		"\3\36\3\36\3\36\3\36\3\36\3\36\5\36\u010b\n\36\6\36\u010d\n\36\r\36\16"+
		"\36\u010e\3\37\3\37\3\37\3\37\3\37\5\37\u0116\n\37\3\37\3\37\3 \3 \3 "+
		"\3 \5 \u011e\n \6 \u0120\n \r \16 \u0121\3!\3!\3!\3!\5!\u0128\n!\3\"\3"+
		"\"\3\"\5\"\u012d\n\"\5\"\u012f\n\"\3#\3#\3$\3$\3$\3$\3$\3$\3$\5$\u013a"+
		"\n$\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+\3+\3,\3"+
		",\3-\3-\3.\3.\3/\3/\3/\ba\u00a0\u00b6\u00be\u00ca\u00d6\2\60\2\4\6\b\n"+
		"\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\"+
		"\2\2\2\u0157\2a\3\2\2\2\4k\3\2\2\2\6m\3\2\2\2\br\3\2\2\2\nw\3\2\2\2\f"+
		"\u0081\3\2\2\2\16\u0095\3\2\2\2\20\u0097\3\2\2\2\22\u009a\3\2\2\2\24\u00a3"+
		"\3\2\2\2\26\u00ab\3\2\2\2\30\u00ad\3\2\2\2\32\u00b9\3\2\2\2\34\u00c1\3"+
		"\2\2\2\36\u00cf\3\2\2\2 \u00db\3\2\2\2\"\u00dd\3\2\2\2$\u00e0\3\2\2\2"+
		"&\u00e3\3\2\2\2(\u00e6\3\2\2\2*\u00e9\3\2\2\2,\u00ec\3\2\2\2.\u00ef\3"+
		"\2\2\2\60\u00f2\3\2\2\2\62\u00f5\3\2\2\2\64\u00f8\3\2\2\2\66\u00fb\3\2"+
		"\2\28\u0101\3\2\2\2:\u0103\3\2\2\2<\u0110\3\2\2\2>\u0119\3\2\2\2@\u0123"+
		"\3\2\2\2B\u012e\3\2\2\2D\u0130\3\2\2\2F\u0132\3\2\2\2H\u013d\3\2\2\2J"+
		"\u013f\3\2\2\2L\u0141\3\2\2\2N\u0143\3\2\2\2P\u0146\3\2\2\2R\u0149\3\2"+
		"\2\2T\u014c\3\2\2\2V\u014f\3\2\2\2X\u0151\3\2\2\2Z\u0153\3\2\2\2\\\u0155"+
		"\3\2\2\2^`\5\4\3\2_^\3\2\2\2`c\3\2\2\2ab\3\2\2\2a_\3\2\2\2bd\3\2\2\2c"+
		"a\3\2\2\2de\5\b\5\2ef\5\22\n\2fg\5\n\6\2gh\5\f\7\2hi\7\3\2\2ij\7\2\2\3"+
		"j\3\3\2\2\2kl\5\6\4\2l\5\3\2\2\2mn\7\4\2\2no\7\60\2\2op\7\65\2\2pq\7\3"+
		"\2\2q\7\3\2\2\2rs\7+\2\2st\5B\"\2t\t\3\2\2\2uv\7\5\2\2vx\5\16\b\2wu\3"+
		"\2\2\2xy\3\2\2\2yw\3\2\2\2yz\3\2\2\2z\13\3\2\2\2{\177\7\5\2\2|\u0080\5"+
		"\26\f\2}\u0080\5\30\r\2~\u0080\5\32\16\2\177|\3\2\2\2\177}\3\2\2\2\177"+
		"~\3\2\2\2\u0080\u0082\3\2\2\2\u0081{\3\2\2\2\u0082\u0083\3\2\2\2\u0083"+
		"\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\r\3\2\2\2\u0085\u008b\5X-\2\u0086"+
		"\u008b\5Z.\2\u0087\u008b\5\\/\2\u0088\u008b\5\24\13\2\u0089\u008b\5\20"+
		"\t\2\u008a\u0085\3\2\2\2\u008a\u0086\3\2\2\2\u008a\u0087\3\2\2\2\u008a"+
		"\u0088\3\2\2\2\u008a\u0089\3\2\2\2\u008b\u008d\3\2\2\2\u008c\u008e\7\6"+
		"\2\2\u008d\u008c\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008f\3\2\2\2\u008f"+
		"\u0090\7<\2\2\u0090\u0096\3\2\2\2\u0091\u0094\5\20\t\2\u0092\u0094\5*"+
		"\26\2\u0093\u0091\3\2\2\2\u0093\u0092\3\2\2\2\u0094\u0096\3\2\2\2\u0095"+
		"\u008a\3\2\2\2\u0095\u0093\3\2\2\2\u0096\17\3\2\2\2\u0097\u0098\7/\2\2"+
		"\u0098\u0099\5B\"\2\u0099\21\3\2\2\2\u009a\u009b\7\23\2\2\u009b\u00a0"+
		"\5B\"\2\u009c\u009d\7\7\2\2\u009d\u009f\5B\"\2\u009e\u009c\3\2\2\2\u009f"+
		"\u00a2\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a0\u009e\3\2\2\2\u00a1\23\3\2\2"+
		"\2\u00a2\u00a0\3\2\2\2\u00a3\u00a4\7\21\2\2\u00a4\25\3\2\2\2\u00a5\u00ac"+
		"\5,\27\2\u00a6\u00ac\5.\30\2\u00a7\u00ac\5\60\31\2\u00a8\u00ac\5\62\32"+
		"\2\u00a9\u00ac\5\64\33\2\u00aa\u00ac\5\66\34\2\u00ab\u00a5\3\2\2\2\u00ab"+
		"\u00a6\3\2\2\2\u00ab\u00a7\3\2\2\2\u00ab\u00a8\3\2\2\2\u00ab\u00a9\3\2"+
		"\2\2\u00ab\u00aa\3\2\2\2\u00ac\27\3\2\2\2\u00ad\u00af\7\22\2\2\u00ae\u00b0"+
		"\7\6\2\2\u00af\u00ae\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1"+
		"\u00b6\5F$\2\u00b2\u00b3\7\7\2\2\u00b3\u00b5\5F$\2\u00b4\u00b2\3\2\2\2"+
		"\u00b5\u00b8\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b7\31"+
		"\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00be\5\34\17\2\u00ba\u00bb\7\5\2\2"+
		"\u00bb\u00bd\5\36\20\2\u00bc\u00ba\3\2\2\2\u00bd\u00c0\3\2\2\2\u00be\u00bf"+
		"\3\2\2\2\u00be\u00bc\3\2\2\2\u00bf\33\3\2\2\2\u00c0\u00be\3\2\2\2\u00c1"+
		"\u00c2\7\16\2\2\u00c2\u00c4\7\b\2\2\u00c3\u00c5\58\35\2\u00c4\u00c3\3"+
		"\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00ca\3\2\2\2\u00c6\u00c7\7\7\2\2\u00c7"+
		"\u00c9\58\35\2\u00c8\u00c6\3\2\2\2\u00c9\u00cc\3\2\2\2\u00ca\u00cb\3\2"+
		"\2\2\u00ca\u00c8\3\2\2\2\u00cb\u00cd\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cd"+
		"\u00ce\7\t\2\2\u00ce\35\3\2\2\2\u00cf\u00d0\7\17\2\2\u00d0\u00d1\7\b\2"+
		"\2\u00d1\u00d6\5B\"\2\u00d2\u00d3\7\7\2\2\u00d3\u00d5\5B\"\2\u00d4\u00d2"+
		"\3\2\2\2\u00d5\u00d8\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d7"+
		"\u00d9\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d9\u00da\7\t\2\2\u00da\37\3\2\2"+
		"\2\u00db\u00dc\7#\2\2\u00dc!\3\2\2\2\u00dd\u00de\7\30\2\2\u00de\u00df"+
		"\7\'\2\2\u00df#\3\2\2\2\u00e0\u00e1\7\31\2\2\u00e1\u00e2\7\'\2\2\u00e2"+
		"%\3\2\2\2\u00e3\u00e4\7\32\2\2\u00e4\u00e5\7\'\2\2\u00e5\'\3\2\2\2\u00e6"+
		"\u00e7\7\33\2\2\u00e7\u00e8\7\'\2\2\u00e8)\3\2\2\2\u00e9\u00ea\7\20\2"+
		"\2\u00ea\u00eb\5B\"\2\u00eb+\3\2\2\2\u00ec\u00ed\7\34\2\2\u00ed\u00ee"+
		"\58\35\2\u00ee-\3\2\2\2\u00ef\u00f0\7\35\2\2\u00f0\u00f1\58\35\2\u00f1"+
		"/\3\2\2\2\u00f2\u00f3\7\37\2\2\u00f3\u00f4\5B\"\2\u00f4\61\3\2\2\2\u00f5"+
		"\u00f6\7 \2\2\u00f6\u00f7\5B\"\2\u00f7\63\3\2\2\2\u00f8\u00f9\7!\2\2\u00f9"+
		"\u00fa\58\35\2\u00fa\65\3\2\2\2\u00fb\u00fc\7\"\2\2\u00fc\u00fd\58\35"+
		"\2\u00fd\67\3\2\2\2\u00fe\u0102\5B\"\2\u00ff\u0102\5:\36\2\u0100\u0102"+
		"\5> \2\u0101\u00fe\3\2\2\2\u0101\u00ff\3\2\2\2\u0101\u0100\3\2\2\2\u0102"+
		"9\3\2\2\2\u0103\u010c\5B\"\2\u0104\u010a\7%\2\2\u0105\u010b\5B\"\2\u0106"+
		"\u010b\5F$\2\u0107\u010b\5> \2\u0108\u010b\5@!\2\u0109\u010b\5<\37\2\u010a"+
		"\u0105\3\2\2\2\u010a\u0106\3\2\2\2\u010a\u0107\3\2\2\2\u010a\u0108\3\2"+
		"\2\2\u010a\u0109\3\2\2\2\u010b\u010d\3\2\2\2\u010c\u0104\3\2\2\2\u010d"+
		"\u010e\3\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f;\3\2\2\2"+
		"\u0110\u0115\7\n\2\2\u0111\u0116\5> \2\u0112\u0116\5:\36\2\u0113\u0116"+
		"\5@!\2\u0114\u0116\5F$\2\u0115\u0111\3\2\2\2\u0115\u0112\3\2\2\2\u0115"+
		"\u0113\3\2\2\2\u0115\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117\u0118\7\13"+
		"\2\2\u0118=\3\2\2\2\u0119\u011f\5B\"\2\u011a\u011d\7)\2\2\u011b\u011e"+
		"\5B\"\2\u011c\u011e\5F$\2\u011d\u011b\3\2\2\2\u011d\u011c\3\2\2\2\u011e"+
		"\u0120\3\2\2\2\u011f\u011a\3\2\2\2\u0120\u0121\3\2\2\2\u0121\u011f\3\2"+
		"\2\2\u0121\u0122\3\2\2\2\u0122?\3\2\2\2\u0123\u0127\7*\2\2\u0124\u0128"+
		"\5B\"\2\u0125\u0128\5:\36\2\u0126\u0128\5> \2\u0127\u0124\3\2\2\2\u0127"+
		"\u0125\3\2\2\2\u0127\u0126\3\2\2\2\u0128A\3\2\2\2\u0129\u012f\7\65\2\2"+
		"\u012a\u012c\7\67\2\2\u012b\u012d\7?\2\2\u012c\u012b\3\2\2\2\u012c\u012d"+
		"\3\2\2\2\u012d\u012f\3\2\2\2\u012e\u0129\3\2\2\2\u012e\u012a\3\2\2\2\u012f"+
		"C\3\2\2\2\u0130\u0131\7<\2\2\u0131E\3\2\2\2\u0132\u0139\5L\'\2\u0133\u013a"+
		"\5N(\2\u0134\u013a\5P)\2\u0135\u013a\5R*\2\u0136\u013a\5T+\2\u0137\u013a"+
		"\5H%\2\u0138\u013a\5J&\2\u0139\u0133\3\2\2\2\u0139\u0134\3\2\2\2\u0139"+
		"\u0135\3\2\2\2\u0139\u0136\3\2\2\2\u0139\u0137\3\2\2\2\u0139\u0138\3\2"+
		"\2\2\u0139\u013a\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u013c\5V,\2\u013cG"+
		"\3\2\2\2\u013d\u013e\7\26\2\2\u013eI\3\2\2\2\u013f\u0140\7\27\2\2\u0140"+
		"K\3\2\2\2\u0141\u0142\5B\"\2\u0142M\3\2\2\2\u0143\u0144\7$\2\2\u0144\u0145"+
		"\7&\2\2\u0145O\3\2\2\2\u0146\u0147\5R*\2\u0147\u0148\5T+\2\u0148Q\3\2"+
		"\2\2\u0149\u014a\7\24\2\2\u014a\u014b\7&\2\2\u014bS\3\2\2\2\u014c\u014d"+
		"\7\25\2\2\u014d\u014e\7&\2\2\u014eU\3\2\2\2\u014f\u0150\5B\"\2\u0150W"+
		"\3\2\2\2\u0151\u0152\7,\2\2\u0152Y\3\2\2\2\u0153\u0154\7-\2\2\u0154[\3"+
		"\2\2\2\u0155\u0156\7.\2\2\u0156]\3\2\2\2\34ay\177\u0083\u008a\u008d\u0093"+
		"\u0095\u00a0\u00ab\u00af\u00b6\u00be\u00c4\u00ca\u00d6\u0101\u010a\u010e"+
		"\u0115\u011d\u0121\u0127\u012c\u012e\u0139";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}