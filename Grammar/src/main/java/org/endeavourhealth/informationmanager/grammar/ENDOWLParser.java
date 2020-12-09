// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\ENDOWL.g4 by ANTLR 4.9
package org.endeavourhealth.informationmanager.grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ENDOWLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		VALUESET=1, MIN=2, MAX=3, EXACTLY=4, SOME=5, ONLY=6, UNION=7, PROPERTIES=8, 
		PROPERTYCHAIN=9, SUBOBJECTPROPERTY=10, ENTITYTYPE=11, OBJECTPROPERTY=12, 
		SUBCLASS=13, EQUIVALENT=14, CLASSENTITY=15, AXIOMTYPE=16, CLASS=17, INTEGER=18, 
		LOWERCASE=19, UPPERCASE=20, TAB=21, LF=22, CR=23, SPACE=24, EXCLAMATION=25, 
		QUOTE=26, POUND=27, DOLLAR=28, PERCENT=29, AMPERSAND=30, APOSTROPHE=31, 
		LEFT_PAREN=32, RIGHT_PAREN=33, ASTERISK=34, PLUS=35, COMMA=36, DASH=37, 
		PERIOD=38, SLASH=39, ZERO=40, ONE=41, TWO=42, THREE=43, FOUR=44, FIVE=45, 
		SIX=46, SEVEN=47, EIGHT=48, NINE=49, COLON=50, SEMICOLON=51, LESS_THAN=52, 
		EQUALS=53, GREATER_THAN=54, QUESTION=55, AT=56, LEFT_BRACE=57, BACKSLASH=58, 
		RIGHT_BRACE=59, CARAT=60, UNDERSCORE=61, ACCENT=62, A=63, B=64, C=65, 
		D=66, E=67, F=68, G=69, H=70, I=71, J=72, K=73, L=74, M=75, N=76, O=77, 
		P=78, Q=79, R=80, S=81, T=82, U=83, V=84, W=85, X=86, Y=87, Z=88, LEFT_CURLY_BRACE=89, 
		PIPE=90, RIGHT_CURLY_BRACE=91, TILDE=92;
	public static final int
		RULE_entitytype = 0, RULE_classentity = 1, RULE_classaxiomtype = 2, RULE_subclass = 3, 
		RULE_equivalent = 4, RULE_objectproperty = 5, RULE_propertyaxiomtype = 6, 
		RULE_subobjectproperty = 7, RULE_objectpropertychain = 8, RULE_parentclasses = 9, 
		RULE_objectproperties = 10, RULE_propertyvalue = 11, RULE_cardinality = 12, 
		RULE_rangeexpression = 13, RULE_namedclass = 14, RULE_valueset = 15, RULE_union = 16, 
		RULE_complexclass = 17, RULE_only = 18, RULE_min = 19, RULE_max = 20, 
		RULE_exact = 21, RULE_iri = 22, RULE_ws = 23, RULE_comment = 24, RULE_sp = 25, 
		RULE_htab = 26, RULE_cr = 27, RULE_lf = 28;
	private static String[] makeRuleNames() {
		return new String[] {
			"entitytype", "classentity", "classaxiomtype", "subclass", "equivalent", 
			"objectproperty", "propertyaxiomtype", "subobjectproperty", "objectpropertychain", 
			"parentclasses", "objectproperties", "propertyvalue", "cardinality", 
			"rangeexpression", "namedclass", "valueset", "union", "complexclass", 
			"only", "min", "max", "exact", "iri", "ws", "comment", "sp", "htab", 
			"cr", "lf"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'Value set'", "'min'", "'max'", "'exactly'", "'at least one'", 
			"'only'", "'any of/or'", "'Property'", "'Property chain'", "'Subproperty of'", 
			"'Entity type'", "'Object property'", "'Subclass of'", "'Equivalent to'", 
			"'Class entity'", "'Axiom type'", "'Class'", null, null, null, null, 
			null, null, "' '", "'!'", "'\"'", "'#'", "'$'", "'%'", "'&'", "'''", 
			"'('", "')'", "'*'", "'+'", "','", "'-'", "'.'", "'/'", "'0'", "'1'", 
			"'2'", "'3'", "'4'", "'5'", "'6'", "'7'", "'8'", "'9'", "':'", "';'", 
			"'<'", "'='", "'>'", "'?'", "'@'", "'['", "'\\'", "']'", "'^'", "'_'", 
			"'`'", "'a'", "'b'", "'c'", "'d'", "'e'", "'f'", "'g'", "'h'", "'i'", 
			"'j'", "'k'", "'l'", "'m'", "'n'", "'o'", "'p'", "'q'", "'r'", "'s'", 
			"'t'", "'u'", "'v'", "'w'", "'x'", "'y'", "'z'", "'{'", "'|'", "'}'", 
			"'~'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "VALUESET", "MIN", "MAX", "EXACTLY", "SOME", "ONLY", "UNION", "PROPERTIES", 
			"PROPERTYCHAIN", "SUBOBJECTPROPERTY", "ENTITYTYPE", "OBJECTPROPERTY", 
			"SUBCLASS", "EQUIVALENT", "CLASSENTITY", "AXIOMTYPE", "CLASS", "INTEGER", 
			"LOWERCASE", "UPPERCASE", "TAB", "LF", "CR", "SPACE", "EXCLAMATION", 
			"QUOTE", "POUND", "DOLLAR", "PERCENT", "AMPERSAND", "APOSTROPHE", "LEFT_PAREN", 
			"RIGHT_PAREN", "ASTERISK", "PLUS", "COMMA", "DASH", "PERIOD", "SLASH", 
			"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", 
			"NINE", "COLON", "SEMICOLON", "LESS_THAN", "EQUALS", "GREATER_THAN", 
			"QUESTION", "AT", "LEFT_BRACE", "BACKSLASH", "RIGHT_BRACE", "CARAT", 
			"UNDERSCORE", "ACCENT", "A", "B", "C", "D", "E", "F", "G", "H", "I", 
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", 
			"X", "Y", "Z", "LEFT_CURLY_BRACE", "PIPE", "RIGHT_CURLY_BRACE", "TILDE"
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
	public String getGrammarFileName() { return "ENDOWL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ENDOWLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class EntitytypeContext extends ParserRuleContext {
		public TerminalNode ENTITYTYPE() { return getToken(ENDOWLParser.ENTITYTYPE, 0); }
		public TerminalNode COLON() { return getToken(ENDOWLParser.COLON, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public ClassentityContext classentity() {
			return getRuleContext(ClassentityContext.class,0);
		}
		public EntitytypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entitytype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterEntitytype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitEntitytype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitEntitytype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntitytypeContext entitytype() throws RecognitionException {
		EntitytypeContext _localctx = new EntitytypeContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_entitytype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(ENTITYTYPE);
			setState(59);
			match(COLON);
			setState(60);
			ws();
			setState(61);
			classentity();
			setState(62);
			ws();
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode CLASSENTITY() { return getToken(ENDOWLParser.CLASSENTITY, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public ClassaxiomtypeContext classaxiomtype() {
			return getRuleContext(ClassaxiomtypeContext.class,0);
		}
		public ClassentityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classentity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterClassentity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitClassentity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitClassentity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassentityContext classentity() throws RecognitionException {
		ClassentityContext _localctx = new ClassentityContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_classentity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			match(CLASSENTITY);
			setState(65);
			ws();
			setState(66);
			classaxiomtype();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassaxiomtypeContext extends ParserRuleContext {
		public TerminalNode AXIOMTYPE() { return getToken(ENDOWLParser.AXIOMTYPE, 0); }
		public TerminalNode COLON() { return getToken(ENDOWLParser.COLON, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public SubclassContext subclass() {
			return getRuleContext(SubclassContext.class,0);
		}
		public EquivalentContext equivalent() {
			return getRuleContext(EquivalentContext.class,0);
		}
		public ClassaxiomtypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classaxiomtype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterClassaxiomtype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitClassaxiomtype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitClassaxiomtype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassaxiomtypeContext classaxiomtype() throws RecognitionException {
		ClassaxiomtypeContext _localctx = new ClassaxiomtypeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_classaxiomtype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(AXIOMTYPE);
			setState(69);
			match(COLON);
			setState(70);
			ws();
			setState(73);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBCLASS:
			case CLASS:
				{
				setState(71);
				subclass();
				}
				break;
			case EQUIVALENT:
				{
				setState(72);
				equivalent();
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

	public static class SubclassContext extends ParserRuleContext {
		public TerminalNode SUBCLASS() { return getToken(ENDOWLParser.SUBCLASS, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public ParentclassesContext parentclasses() {
			return getRuleContext(ParentclassesContext.class,0);
		}
		public ObjectpropertiesContext objectproperties() {
			return getRuleContext(ObjectpropertiesContext.class,0);
		}
		public SubclassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subclass; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterSubclass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitSubclass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitSubclass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubclassContext subclass() throws RecognitionException {
		SubclassContext _localctx = new SubclassContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_subclass);
		try {
			setState(85);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBCLASS:
				enterOuterAlt(_localctx, 1);
				{
				setState(75);
				match(SUBCLASS);
				setState(76);
				ws();
				setState(79);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CLASS:
					{
					setState(77);
					parentclasses();
					}
					break;
				case PROPERTIES:
					{
					setState(78);
					objectproperties();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case CLASS:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(81);
				parentclasses();
				setState(82);
				ws();
				setState(83);
				objectproperties();
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

	public static class EquivalentContext extends ParserRuleContext {
		public TerminalNode EQUIVALENT() { return getToken(ENDOWLParser.EQUIVALENT, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public ParentclassesContext parentclasses() {
			return getRuleContext(ParentclassesContext.class,0);
		}
		public EquivalentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equivalent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterEquivalent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitEquivalent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitEquivalent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EquivalentContext equivalent() throws RecognitionException {
		EquivalentContext _localctx = new EquivalentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_equivalent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			match(EQUIVALENT);
			setState(88);
			ws();
			setState(89);
			parentclasses();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectpropertyContext extends ParserRuleContext {
		public TerminalNode OBJECTPROPERTY() { return getToken(ENDOWLParser.OBJECTPROPERTY, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public PropertyaxiomtypeContext propertyaxiomtype() {
			return getRuleContext(PropertyaxiomtypeContext.class,0);
		}
		public ObjectpropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectproperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterObjectproperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitObjectproperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitObjectproperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectpropertyContext objectproperty() throws RecognitionException {
		ObjectpropertyContext _localctx = new ObjectpropertyContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_objectproperty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			match(OBJECTPROPERTY);
			setState(92);
			ws();
			setState(93);
			propertyaxiomtype();
			setState(94);
			ws();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyaxiomtypeContext extends ParserRuleContext {
		public TerminalNode AXIOMTYPE() { return getToken(ENDOWLParser.AXIOMTYPE, 0); }
		public TerminalNode COLON() { return getToken(ENDOWLParser.COLON, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public SubobjectpropertyContext subobjectproperty() {
			return getRuleContext(SubobjectpropertyContext.class,0);
		}
		public ObjectpropertychainContext objectpropertychain() {
			return getRuleContext(ObjectpropertychainContext.class,0);
		}
		public PropertyaxiomtypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyaxiomtype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterPropertyaxiomtype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitPropertyaxiomtype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitPropertyaxiomtype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyaxiomtypeContext propertyaxiomtype() throws RecognitionException {
		PropertyaxiomtypeContext _localctx = new PropertyaxiomtypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_propertyaxiomtype);
		try {
			setState(104);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AXIOMTYPE:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				match(AXIOMTYPE);
				setState(97);
				match(COLON);
				setState(98);
				ws();
				setState(99);
				subobjectproperty();
				}
				break;
			case PROPERTYCHAIN:
				enterOuterAlt(_localctx, 2);
				{
				setState(101);
				objectpropertychain();
				setState(102);
				ws();
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

	public static class SubobjectpropertyContext extends ParserRuleContext {
		public TerminalNode SUBOBJECTPROPERTY() { return getToken(ENDOWLParser.SUBOBJECTPROPERTY, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public SubobjectpropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_subobjectproperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterSubobjectproperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitSubobjectproperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitSubobjectproperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SubobjectpropertyContext subobjectproperty() throws RecognitionException {
		SubobjectpropertyContext _localctx = new SubobjectpropertyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_subobjectproperty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			match(SUBOBJECTPROPERTY);
			setState(107);
			ws();
			setState(108);
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

	public static class ObjectpropertychainContext extends ParserRuleContext {
		public TerminalNode PROPERTYCHAIN() { return getToken(ENDOWLParser.PROPERTYCHAIN, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<IriContext> iri() {
			return getRuleContexts(IriContext.class);
		}
		public IriContext iri(int i) {
			return getRuleContext(IriContext.class,i);
		}
		public TerminalNode EOF() { return getToken(ENDOWLParser.EOF, 0); }
		public ObjectpropertychainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectpropertychain; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterObjectpropertychain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitObjectpropertychain(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitObjectpropertychain(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectpropertychainContext objectpropertychain() throws RecognitionException {
		ObjectpropertychainContext _localctx = new ObjectpropertychainContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_objectpropertychain);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			match(PROPERTYCHAIN);
			setState(111);
			ws();
			setState(115); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(112);
				iri();
				setState(113);
				ws();
				}
				}
				setState(117); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INTEGER) | (1L << LOWERCASE) | (1L << UPPERCASE))) != 0) );
			setState(120);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EOF) {
				{
				setState(119);
				match(EOF);
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

	public static class ParentclassesContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(ENDOWLParser.CLASS, 0); }
		public TerminalNode COLON() { return getToken(ENDOWLParser.COLON, 0); }
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public List<IriContext> iri() {
			return getRuleContexts(IriContext.class);
		}
		public IriContext iri(int i) {
			return getRuleContext(IriContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(ENDOWLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(ENDOWLParser.COMMA, i);
		}
		public ParentclassesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parentclasses; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterParentclasses(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitParentclasses(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitParentclasses(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParentclassesContext parentclasses() throws RecognitionException {
		ParentclassesContext _localctx = new ParentclassesContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_parentclasses);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			match(CLASS);
			setState(123);
			match(COLON);
			setState(124);
			ws();
			setState(130); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(125);
				iri();
				setState(128);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==COMMA) {
					{
					setState(126);
					match(COMMA);
					setState(127);
					ws();
					}
				}

				}
				}
				setState(132); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INTEGER) | (1L << LOWERCASE) | (1L << UPPERCASE))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObjectpropertiesContext extends ParserRuleContext {
		public TerminalNode PROPERTIES() { return getToken(ENDOWLParser.PROPERTIES, 0); }
		public List<PropertyvalueContext> propertyvalue() {
			return getRuleContexts(PropertyvalueContext.class);
		}
		public PropertyvalueContext propertyvalue(int i) {
			return getRuleContext(PropertyvalueContext.class,i);
		}
		public ObjectpropertiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectproperties; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterObjectproperties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitObjectproperties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitObjectproperties(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectpropertiesContext objectproperties() throws RecognitionException {
		ObjectpropertiesContext _localctx = new ObjectpropertiesContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_objectproperties);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(134);
			match(PROPERTIES);
			setState(136); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(135);
					propertyvalue();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(138); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
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

	public static class PropertyvalueContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public List<WsContext> ws() {
			return getRuleContexts(WsContext.class);
		}
		public WsContext ws(int i) {
			return getRuleContext(WsContext.class,i);
		}
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
		}
		public RangeexpressionContext rangeexpression() {
			return getRuleContext(RangeexpressionContext.class,0);
		}
		public PropertyvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterPropertyvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitPropertyvalue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitPropertyvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyvalueContext propertyvalue() throws RecognitionException {
		PropertyvalueContext _localctx = new PropertyvalueContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_propertyvalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			iri();
			setState(141);
			ws();
			setState(142);
			cardinality();
			setState(143);
			ws();
			setState(144);
			rangeexpression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CardinalityContext extends ParserRuleContext {
		public TerminalNode SOME() { return getToken(ENDOWLParser.SOME, 0); }
		public OnlyContext only() {
			return getRuleContext(OnlyContext.class,0);
		}
		public MinContext min() {
			return getRuleContext(MinContext.class,0);
		}
		public MaxContext max() {
			return getRuleContext(MaxContext.class,0);
		}
		public ExactContext exact() {
			return getRuleContext(ExactContext.class,0);
		}
		public CardinalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cardinality; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterCardinality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitCardinality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitCardinality(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CardinalityContext cardinality() throws RecognitionException {
		CardinalityContext _localctx = new CardinalityContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_cardinality);
		try {
			setState(151);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SOME:
				enterOuterAlt(_localctx, 1);
				{
				setState(146);
				match(SOME);
				}
				break;
			case ONLY:
				enterOuterAlt(_localctx, 2);
				{
				setState(147);
				only();
				}
				break;
			case MIN:
				enterOuterAlt(_localctx, 3);
				{
				setState(148);
				min();
				}
				break;
			case MAX:
				enterOuterAlt(_localctx, 4);
				{
				setState(149);
				max();
				}
				break;
			case EXACTLY:
				enterOuterAlt(_localctx, 5);
				{
				setState(150);
				exact();
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

	public static class RangeexpressionContext extends ParserRuleContext {
		public NamedclassContext namedclass() {
			return getRuleContext(NamedclassContext.class,0);
		}
		public ValuesetContext valueset() {
			return getRuleContext(ValuesetContext.class,0);
		}
		public UnionContext union() {
			return getRuleContext(UnionContext.class,0);
		}
		public RangeexpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rangeexpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterRangeexpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitRangeexpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitRangeexpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RangeexpressionContext rangeexpression() throws RecognitionException {
		RangeexpressionContext _localctx = new RangeexpressionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_rangeexpression);
		try {
			setState(156);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CLASS:
				enterOuterAlt(_localctx, 1);
				{
				setState(153);
				namedclass();
				}
				break;
			case VALUESET:
				enterOuterAlt(_localctx, 2);
				{
				setState(154);
				valueset();
				}
				break;
			case UNION:
				enterOuterAlt(_localctx, 3);
				{
				setState(155);
				union();
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

	public static class NamedclassContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(ENDOWLParser.CLASS, 0); }
		public TerminalNode COLON() { return getToken(ENDOWLParser.COLON, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public NamedclassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedclass; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterNamedclass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitNamedclass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitNamedclass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedclassContext namedclass() throws RecognitionException {
		NamedclassContext _localctx = new NamedclassContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_namedclass);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			match(CLASS);
			setState(159);
			match(COLON);
			setState(160);
			ws();
			setState(161);
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

	public static class ValuesetContext extends ParserRuleContext {
		public TerminalNode VALUESET() { return getToken(ENDOWLParser.VALUESET, 0); }
		public TerminalNode COLON() { return getToken(ENDOWLParser.COLON, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public ValuesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterValueset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitValueset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitValueset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValuesetContext valueset() throws RecognitionException {
		ValuesetContext _localctx = new ValuesetContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_valueset);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(VALUESET);
			setState(164);
			match(COLON);
			setState(165);
			ws();
			setState(166);
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

	public static class UnionContext extends ParserRuleContext {
		public TerminalNode UNION() { return getToken(ENDOWLParser.UNION, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public List<NamedclassContext> namedclass() {
			return getRuleContexts(NamedclassContext.class);
		}
		public NamedclassContext namedclass(int i) {
			return getRuleContext(NamedclassContext.class,i);
		}
		public List<ComplexclassContext> complexclass() {
			return getRuleContexts(ComplexclassContext.class);
		}
		public ComplexclassContext complexclass(int i) {
			return getRuleContext(ComplexclassContext.class,i);
		}
		public UnionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_union; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterUnion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitUnion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitUnion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnionContext union() throws RecognitionException {
		UnionContext _localctx = new UnionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_union);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(UNION);
			setState(169);
			ws();
			setState(172); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(172);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						setState(170);
						namedclass();
						}
						break;
					case 2:
						{
						setState(171);
						complexclass();
						}
						break;
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(174); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
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

	public static class ComplexclassContext extends ParserRuleContext {
		public ParentclassesContext parentclasses() {
			return getRuleContext(ParentclassesContext.class,0);
		}
		public ObjectpropertiesContext objectproperties() {
			return getRuleContext(ObjectpropertiesContext.class,0);
		}
		public ComplexclassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexclass; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterComplexclass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitComplexclass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitComplexclass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComplexclassContext complexclass() throws RecognitionException {
		ComplexclassContext _localctx = new ComplexclassContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_complexclass);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			parentclasses();
			setState(177);
			objectproperties();
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode ONLY() { return getToken(ENDOWLParser.ONLY, 0); }
		public OnlyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_only; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterOnly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitOnly(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitOnly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OnlyContext only() throws RecognitionException {
		OnlyContext _localctx = new OnlyContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_only);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
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

	public static class MinContext extends ParserRuleContext {
		public TerminalNode MIN() { return getToken(ENDOWLParser.MIN, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public TerminalNode INTEGER() { return getToken(ENDOWLParser.INTEGER, 0); }
		public MinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_min; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterMin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitMin(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitMin(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MinContext min() throws RecognitionException {
		MinContext _localctx = new MinContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_min);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			match(MIN);
			setState(182);
			ws();
			setState(183);
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

	public static class MaxContext extends ParserRuleContext {
		public TerminalNode MAX() { return getToken(ENDOWLParser.MAX, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public TerminalNode INTEGER() { return getToken(ENDOWLParser.INTEGER, 0); }
		public MaxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_max; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterMax(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitMax(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitMax(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaxContext max() throws RecognitionException {
		MaxContext _localctx = new MaxContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_max);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			match(MAX);
			setState(186);
			ws();
			setState(187);
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

	public static class ExactContext extends ParserRuleContext {
		public TerminalNode EXACTLY() { return getToken(ENDOWLParser.EXACTLY, 0); }
		public WsContext ws() {
			return getRuleContext(WsContext.class,0);
		}
		public TerminalNode INTEGER() { return getToken(ENDOWLParser.INTEGER, 0); }
		public ExactContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exact; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterExact(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitExact(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitExact(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExactContext exact() throws RecognitionException {
		ExactContext _localctx = new ExactContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_exact);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			match(EXACTLY);
			setState(190);
			ws();
			setState(191);
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

	public static class IriContext extends ParserRuleContext {
		public List<TerminalNode> LOWERCASE() { return getTokens(ENDOWLParser.LOWERCASE); }
		public TerminalNode LOWERCASE(int i) {
			return getToken(ENDOWLParser.LOWERCASE, i);
		}
		public List<TerminalNode> UPPERCASE() { return getTokens(ENDOWLParser.UPPERCASE); }
		public TerminalNode UPPERCASE(int i) {
			return getToken(ENDOWLParser.UPPERCASE, i);
		}
		public List<TerminalNode> INTEGER() { return getTokens(ENDOWLParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(ENDOWLParser.INTEGER, i);
		}
		public IriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iri; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterIri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitIri(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitIri(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IriContext iri() throws RecognitionException {
		IriContext _localctx = new IriContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_iri);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(194); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(193);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INTEGER) | (1L << LOWERCASE) | (1L << UPPERCASE))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(196); 
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

	public static class WsContext extends ParserRuleContext {
		public List<SpContext> sp() {
			return getRuleContexts(SpContext.class);
		}
		public SpContext sp(int i) {
			return getRuleContext(SpContext.class,i);
		}
		public List<HtabContext> htab() {
			return getRuleContexts(HtabContext.class);
		}
		public HtabContext htab(int i) {
			return getRuleContext(HtabContext.class,i);
		}
		public List<CrContext> cr() {
			return getRuleContexts(CrContext.class);
		}
		public CrContext cr(int i) {
			return getRuleContext(CrContext.class,i);
		}
		public List<LfContext> lf() {
			return getRuleContexts(LfContext.class);
		}
		public LfContext lf(int i) {
			return getRuleContext(LfContext.class,i);
		}
		public List<CommentContext> comment() {
			return getRuleContexts(CommentContext.class);
		}
		public CommentContext comment(int i) {
			return getRuleContext(CommentContext.class,i);
		}
		public WsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ws; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterWs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitWs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitWs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WsContext ws() throws RecognitionException {
		WsContext _localctx = new WsContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_ws);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(203); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(203);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case SPACE:
						{
						setState(198);
						sp();
						}
						break;
					case TAB:
						{
						setState(199);
						htab();
						}
						break;
					case CR:
						{
						setState(200);
						cr();
						}
						break;
					case LF:
						{
						setState(201);
						lf();
						}
						break;
					case SLASH:
						{
						setState(202);
						comment();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(205); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
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

	public static class CommentContext extends ParserRuleContext {
		public List<TerminalNode> SLASH() { return getTokens(ENDOWLParser.SLASH); }
		public TerminalNode SLASH(int i) {
			return getToken(ENDOWLParser.SLASH, i);
		}
		public List<TerminalNode> ASTERISK() { return getTokens(ENDOWLParser.ASTERISK); }
		public TerminalNode ASTERISK(int i) {
			return getToken(ENDOWLParser.ASTERISK, i);
		}
		public List<TerminalNode> INTEGER() { return getTokens(ENDOWLParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(ENDOWLParser.INTEGER, i);
		}
		public List<TerminalNode> UPPERCASE() { return getTokens(ENDOWLParser.UPPERCASE); }
		public TerminalNode UPPERCASE(int i) {
			return getToken(ENDOWLParser.UPPERCASE, i);
		}
		public List<TerminalNode> LOWERCASE() { return getTokens(ENDOWLParser.LOWERCASE); }
		public TerminalNode LOWERCASE(int i) {
			return getToken(ENDOWLParser.LOWERCASE, i);
		}
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_comment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(SLASH);
			setState(208);
			match(ASTERISK);
			setState(210); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(209);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INTEGER) | (1L << LOWERCASE) | (1L << UPPERCASE))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(212); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INTEGER) | (1L << LOWERCASE) | (1L << UPPERCASE))) != 0) );
			setState(214);
			match(ASTERISK);
			setState(215);
			match(SLASH);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpContext extends ParserRuleContext {
		public TerminalNode SPACE() { return getToken(ENDOWLParser.SPACE, 0); }
		public SpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterSp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitSp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitSp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpContext sp() throws RecognitionException {
		SpContext _localctx = new SpContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_sp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(217);
			match(SPACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HtabContext extends ParserRuleContext {
		public TerminalNode TAB() { return getToken(ENDOWLParser.TAB, 0); }
		public HtabContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_htab; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterHtab(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitHtab(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitHtab(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HtabContext htab() throws RecognitionException {
		HtabContext _localctx = new HtabContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_htab);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			match(TAB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CrContext extends ParserRuleContext {
		public TerminalNode CR() { return getToken(ENDOWLParser.CR, 0); }
		public CrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterCr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitCr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitCr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CrContext cr() throws RecognitionException {
		CrContext _localctx = new CrContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_cr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(CR);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LfContext extends ParserRuleContext {
		public TerminalNode LF() { return getToken(ENDOWLParser.LF, 0); }
		public LfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).enterLf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ENDOWLListener ) ((ENDOWLListener)listener).exitLf(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ENDOWLVisitor ) return ((ENDOWLVisitor<? extends T>)visitor).visitLf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LfContext lf() throws RecognitionException {
		LfContext _localctx = new LfContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_lf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
			match(LF);
			}
		}
		catch (RecognitionException re) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3^\u00e4\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\5\4L\n\4\3\5\3\5\3\5\3\5\5\5"+
		"R\n\5\3\5\3\5\3\5\3\5\5\5X\n\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bk\n\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n"+
		"\3\n\6\nv\n\n\r\n\16\nw\3\n\5\n{\n\n\3\13\3\13\3\13\3\13\3\13\3\13\5\13"+
		"\u0083\n\13\6\13\u0085\n\13\r\13\16\13\u0086\3\f\3\f\6\f\u008b\n\f\r\f"+
		"\16\f\u008c\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\5\16\u009a"+
		"\n\16\3\17\3\17\3\17\5\17\u009f\n\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21"+
		"\3\21\3\21\3\21\3\22\3\22\3\22\3\22\6\22\u00af\n\22\r\22\16\22\u00b0\3"+
		"\23\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3"+
		"\27\3\27\3\27\3\30\6\30\u00c5\n\30\r\30\16\30\u00c6\3\31\3\31\3\31\3\31"+
		"\3\31\6\31\u00ce\n\31\r\31\16\31\u00cf\3\32\3\32\3\32\6\32\u00d5\n\32"+
		"\r\32\16\32\u00d6\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3"+
		"\36\3\36\2\2\37\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62"+
		"\64\668:\2\3\3\2\24\26\2\u00de\2<\3\2\2\2\4B\3\2\2\2\6F\3\2\2\2\bW\3\2"+
		"\2\2\nY\3\2\2\2\f]\3\2\2\2\16j\3\2\2\2\20l\3\2\2\2\22p\3\2\2\2\24|\3\2"+
		"\2\2\26\u0088\3\2\2\2\30\u008e\3\2\2\2\32\u0099\3\2\2\2\34\u009e\3\2\2"+
		"\2\36\u00a0\3\2\2\2 \u00a5\3\2\2\2\"\u00aa\3\2\2\2$\u00b2\3\2\2\2&\u00b5"+
		"\3\2\2\2(\u00b7\3\2\2\2*\u00bb\3\2\2\2,\u00bf\3\2\2\2.\u00c4\3\2\2\2\60"+
		"\u00cd\3\2\2\2\62\u00d1\3\2\2\2\64\u00db\3\2\2\2\66\u00dd\3\2\2\28\u00df"+
		"\3\2\2\2:\u00e1\3\2\2\2<=\7\r\2\2=>\7\64\2\2>?\5\60\31\2?@\5\4\3\2@A\5"+
		"\60\31\2A\3\3\2\2\2BC\7\21\2\2CD\5\60\31\2DE\5\6\4\2E\5\3\2\2\2FG\7\22"+
		"\2\2GH\7\64\2\2HK\5\60\31\2IL\5\b\5\2JL\5\n\6\2KI\3\2\2\2KJ\3\2\2\2L\7"+
		"\3\2\2\2MN\7\17\2\2NQ\5\60\31\2OR\5\24\13\2PR\5\26\f\2QO\3\2\2\2QP\3\2"+
		"\2\2RX\3\2\2\2ST\5\24\13\2TU\5\60\31\2UV\5\26\f\2VX\3\2\2\2WM\3\2\2\2"+
		"WS\3\2\2\2X\t\3\2\2\2YZ\7\20\2\2Z[\5\60\31\2[\\\5\24\13\2\\\13\3\2\2\2"+
		"]^\7\16\2\2^_\5\60\31\2_`\5\16\b\2`a\5\60\31\2a\r\3\2\2\2bc\7\22\2\2c"+
		"d\7\64\2\2de\5\60\31\2ef\5\20\t\2fk\3\2\2\2gh\5\22\n\2hi\5\60\31\2ik\3"+
		"\2\2\2jb\3\2\2\2jg\3\2\2\2k\17\3\2\2\2lm\7\f\2\2mn\5\60\31\2no\5.\30\2"+
		"o\21\3\2\2\2pq\7\13\2\2qu\5\60\31\2rs\5.\30\2st\5\60\31\2tv\3\2\2\2ur"+
		"\3\2\2\2vw\3\2\2\2wu\3\2\2\2wx\3\2\2\2xz\3\2\2\2y{\7\2\2\3zy\3\2\2\2z"+
		"{\3\2\2\2{\23\3\2\2\2|}\7\23\2\2}~\7\64\2\2~\u0084\5\60\31\2\177\u0082"+
		"\5.\30\2\u0080\u0081\7&\2\2\u0081\u0083\5\60\31\2\u0082\u0080\3\2\2\2"+
		"\u0082\u0083\3\2\2\2\u0083\u0085\3\2\2\2\u0084\177\3\2\2\2\u0085\u0086"+
		"\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\25\3\2\2\2\u0088"+
		"\u008a\7\n\2\2\u0089\u008b\5\30\r\2\u008a\u0089\3\2\2\2\u008b\u008c\3"+
		"\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d\27\3\2\2\2\u008e"+
		"\u008f\5.\30\2\u008f\u0090\5\60\31\2\u0090\u0091\5\32\16\2\u0091\u0092"+
		"\5\60\31\2\u0092\u0093\5\34\17\2\u0093\31\3\2\2\2\u0094\u009a\7\7\2\2"+
		"\u0095\u009a\5&\24\2\u0096\u009a\5(\25\2\u0097\u009a\5*\26\2\u0098\u009a"+
		"\5,\27\2\u0099\u0094\3\2\2\2\u0099\u0095\3\2\2\2\u0099\u0096\3\2\2\2\u0099"+
		"\u0097\3\2\2\2\u0099\u0098\3\2\2\2\u009a\33\3\2\2\2\u009b\u009f\5\36\20"+
		"\2\u009c\u009f\5 \21\2\u009d\u009f\5\"\22\2\u009e\u009b\3\2\2\2\u009e"+
		"\u009c\3\2\2\2\u009e\u009d\3\2\2\2\u009f\35\3\2\2\2\u00a0\u00a1\7\23\2"+
		"\2\u00a1\u00a2\7\64\2\2\u00a2\u00a3\5\60\31\2\u00a3\u00a4\5.\30\2\u00a4"+
		"\37\3\2\2\2\u00a5\u00a6\7\3\2\2\u00a6\u00a7\7\64\2\2\u00a7\u00a8\5\60"+
		"\31\2\u00a8\u00a9\5.\30\2\u00a9!\3\2\2\2\u00aa\u00ab\7\t\2\2\u00ab\u00ae"+
		"\5\60\31\2\u00ac\u00af\5\36\20\2\u00ad\u00af\5$\23\2\u00ae\u00ac\3\2\2"+
		"\2\u00ae\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1"+
		"\3\2\2\2\u00b1#\3\2\2\2\u00b2\u00b3\5\24\13\2\u00b3\u00b4\5\26\f\2\u00b4"+
		"%\3\2\2\2\u00b5\u00b6\7\b\2\2\u00b6\'\3\2\2\2\u00b7\u00b8\7\4\2\2\u00b8"+
		"\u00b9\5\60\31\2\u00b9\u00ba\7\24\2\2\u00ba)\3\2\2\2\u00bb\u00bc\7\5\2"+
		"\2\u00bc\u00bd\5\60\31\2\u00bd\u00be\7\24\2\2\u00be+\3\2\2\2\u00bf\u00c0"+
		"\7\6\2\2\u00c0\u00c1\5\60\31\2\u00c1\u00c2\7\24\2\2\u00c2-\3\2\2\2\u00c3"+
		"\u00c5\t\2\2\2\u00c4\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c4\3\2"+
		"\2\2\u00c6\u00c7\3\2\2\2\u00c7/\3\2\2\2\u00c8\u00ce\5\64\33\2\u00c9\u00ce"+
		"\5\66\34\2\u00ca\u00ce\58\35\2\u00cb\u00ce\5:\36\2\u00cc\u00ce\5\62\32"+
		"\2\u00cd\u00c8\3\2\2\2\u00cd\u00c9\3\2\2\2\u00cd\u00ca\3\2\2\2\u00cd\u00cb"+
		"\3\2\2\2\u00cd\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00cd\3\2\2\2\u00cf"+
		"\u00d0\3\2\2\2\u00d0\61\3\2\2\2\u00d1\u00d2\7)\2\2\u00d2\u00d4\7$\2\2"+
		"\u00d3\u00d5\t\2\2\2\u00d4\u00d3\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d4"+
		"\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8\u00d9\7$\2\2\u00d9"+
		"\u00da\7)\2\2\u00da\63\3\2\2\2\u00db\u00dc\7\32\2\2\u00dc\65\3\2\2\2\u00dd"+
		"\u00de\7\27\2\2\u00de\67\3\2\2\2\u00df\u00e0\7\31\2\2\u00e09\3\2\2\2\u00e1"+
		"\u00e2\7\30\2\2\u00e2;\3\2\2\2\23KQWjwz\u0082\u0086\u008c\u0099\u009e"+
		"\u00ae\u00b0\u00c6\u00cd\u00cf\u00d6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}