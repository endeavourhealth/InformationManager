// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\IMLang.g4 by ANTLR 4.9
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
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, IRI_LABEL=23, TYPE=24, 
		SHAPE=25, RECORDOF=26, TARGETCLASS=27, CLASS=28, PROPERTY=29, DATATYPE=30, 
		VALUESET=31, PATH=32, MINCOUNT=33, MAXCOUNT=34, NAME=35, DESCRIPTION=36, 
		CODE=37, SCHEME=38, MININCLUSIVE=39, MAXINCLUSIVE=40, MINEXCLUSIVE=41, 
		MAXEXCLUSIVE=42, SUBCLASS=43, EQUIVALENTTO=44, DISJOINT=45, SUBPROPERTY=46, 
		INVERSE=47, INTEGER=48, DOUBLE=49, DIGIT=50, EXACT=51, AND=52, OR=53, 
		IRI=54, ABBREVIATED_IRI=55, FULL_IRI=56, WORD=57, LOWERCASE=58, UPPERCASE=59, 
		QUOTED_STRING=60, WS=61, SC=62;
	public static final int
		RULE_concept = 0, RULE_classAxiom = 1, RULE_propertyAxiom = 2, RULE_type = 3, 
		RULE_classType = 4, RULE_dataType = 5, RULE_shape = 6, RULE_propertyType = 7, 
		RULE_valueSet = 8, RULE_shapeOf = 9, RULE_propertyConstraint = 10, RULE_constraintParameter = 11, 
		RULE_minCount = 12, RULE_maxCount = 13, RULE_minInclusive = 14, RULE_maxInclusive = 15, 
		RULE_minExclusive = 16, RULE_maxExlusive = 17, RULE_classValue = 18, RULE_label = 19, 
		RULE_status = 20, RULE_version = 21, RULE_identifierIri = 22, RULE_name = 23, 
		RULE_description = 24, RULE_code = 25, RULE_scheme = 26, RULE_subclassOf = 27, 
		RULE_equivalentTo = 28, RULE_disjointWith = 29, RULE_subpropertyOf = 30, 
		RULE_inverseOf = 31, RULE_classExpression = 32, RULE_objectCollection = 33, 
		RULE_iri = 34, RULE_roleGroup = 35, RULE_role = 36, RULE_dataRange = 37, 
		RULE_rangeValue = 38, RULE_typedString = 39, RULE_valueCollection = 40, 
		RULE_dataRangeCollection = 41;
	private static String[] makeRuleNames() {
		return new String[] {
			"concept", "classAxiom", "propertyAxiom", "type", "classType", "dataType", 
			"shape", "propertyType", "valueSet", "shapeOf", "propertyConstraint", 
			"constraintParameter", "minCount", "maxCount", "minInclusive", "maxInclusive", 
			"minExclusive", "maxExlusive", "classValue", "label", "status", "version", 
			"identifierIri", "name", "description", "code", "scheme", "subclassOf", 
			"equivalentTo", "disjointWith", "subpropertyOf", "inverseOf", "classExpression", 
			"objectCollection", "iri", "roleGroup", "role", "dataRange", "rangeValue", 
			"typedString", "valueCollection", "dataRangeCollection"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'['", "'path '", "']'", "'class '", "'valuetype'", "'status '", 
			"'active'", "'inactive'", "'draft'", "'version '", "'subpropertyof '", 
			"'subPropertyOf '", "'<<<'", "'inverseof '", "'inverseOf '", "'('", "','", 
			"')'", "'{'", "'}'", "'^^'", null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "'or'", 
			null, null, null, null, null, null, null, null, "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "IRI_LABEL", 
			"TYPE", "SHAPE", "RECORDOF", "TARGETCLASS", "CLASS", "PROPERTY", "DATATYPE", 
			"VALUESET", "PATH", "MINCOUNT", "MAXCOUNT", "NAME", "DESCRIPTION", "CODE", 
			"SCHEME", "MININCLUSIVE", "MAXINCLUSIVE", "MINEXCLUSIVE", "MAXEXCLUSIVE", 
			"SUBCLASS", "EQUIVALENTTO", "DISJOINT", "SUBPROPERTY", "INVERSE", "INTEGER", 
			"DOUBLE", "DIGIT", "EXACT", "AND", "OR", "IRI", "ABBREVIATED_IRI", "FULL_IRI", 
			"WORD", "LOWERCASE", "UPPERCASE", "QUOTED_STRING", "WS", "SC"
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
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public TerminalNode SC() { return getToken(IMLangParser.SC, 0); }
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			identifierIri();
			setState(88);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__10) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
				{
				setState(85);
				label();
				setState(86);
				match(SC);
				}
			}

			setState(90);
			type();
			setState(91);
			match(T__0);
			setState(92);
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
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
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
		int _la;
		try {
			setState(100);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SUBCLASS:
				enterOuterAlt(_localctx, 1);
				{
				setState(94);
				subclassOf();
				}
				break;
			case EQUIVALENTTO:
				enterOuterAlt(_localctx, 2);
				{
				setState(95);
				equivalentTo();
				}
				break;
			case DISJOINT:
				enterOuterAlt(_localctx, 3);
				{
				setState(96);
				disjointWith();
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__10) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
					{
					setState(97);
					label();
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

	public static class PropertyAxiomContext extends ParserRuleContext {
		public SubpropertyOfContext subpropertyOf() {
			return getRuleContext(SubpropertyOfContext.class,0);
		}
		public InverseOfContext inverseOf() {
			return getRuleContext(InverseOfContext.class,0);
		}
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
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
		int _la;
		try {
			setState(107);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__11:
			case T__12:
			case T__13:
				enterOuterAlt(_localctx, 1);
				{
				setState(102);
				subpropertyOf();
				}
				break;
			case T__14:
			case T__15:
				enterOuterAlt(_localctx, 2);
				{
				setState(103);
				inverseOf();
				setState(105);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__6) | (1L << T__10) | (1L << NAME) | (1L << DESCRIPTION) | (1L << CODE) | (1L << SCHEME))) != 0)) {
					{
					setState(104);
					label();
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

	public static class TypeContext extends ParserRuleContext {
		public TerminalNode TYPE() { return getToken(IMLangParser.TYPE, 0); }
		public ClassTypeContext classType() {
			return getRuleContext(ClassTypeContext.class,0);
		}
		public DataTypeContext dataType() {
			return getRuleContext(DataTypeContext.class,0);
		}
		public ShapeContext shape() {
			return getRuleContext(ShapeContext.class,0);
		}
		public ValueSetContext valueSet() {
			return getRuleContext(ValueSetContext.class,0);
		}
		public PropertyTypeContext propertyType() {
			return getRuleContext(PropertyTypeContext.class,0);
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
			setState(109);
			match(TYPE);
			setState(115);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CLASS:
				{
				setState(110);
				classType();
				}
				break;
			case DATATYPE:
				{
				setState(111);
				dataType();
				}
				break;
			case SHAPE:
				{
				setState(112);
				shape();
				}
				break;
			case VALUESET:
				{
				setState(113);
				valueSet();
				}
				break;
			case PROPERTY:
				{
				setState(114);
				propertyType();
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
		enterRule(_localctx, 8, RULE_classType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(CLASS);
			setState(120); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(118);
					match(SC);
					setState(119);
					classAxiom();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(122); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 10, RULE_dataType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
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

	public static class ShapeContext extends ParserRuleContext {
		public TerminalNode SHAPE() { return getToken(IMLangParser.SHAPE, 0); }
		public TerminalNode SC() { return getToken(IMLangParser.SC, 0); }
		public ShapeOfContext shapeOf() {
			return getRuleContext(ShapeOfContext.class,0);
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(SHAPE);
			setState(127);
			match(SC);
			setState(128);
			shapeOf();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropertyTypeContext extends ParserRuleContext {
		public TerminalNode PROPERTY() { return getToken(IMLangParser.PROPERTY, 0); }
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
		public PropertyTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterPropertyType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitPropertyType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitPropertyType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyTypeContext propertyType() throws RecognitionException {
		PropertyTypeContext _localctx = new PropertyTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_propertyType);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(PROPERTY);
			setState(133); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(131);
					match(SC);
					setState(132);
					propertyAxiom();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(135); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 16, RULE_valueSet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			match(VALUESET);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode RECORDOF() { return getToken(IMLangParser.RECORDOF, 0); }
		public TerminalNode TARGETCLASS() { return getToken(IMLangParser.TARGETCLASS, 0); }
		public List<TerminalNode> SC() { return getTokens(IMLangParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(IMLangParser.SC, i);
		}
		public List<PropertyConstraintContext> propertyConstraint() {
			return getRuleContexts(PropertyConstraintContext.class);
		}
		public PropertyConstraintContext propertyConstraint(int i) {
			return getRuleContext(PropertyConstraintContext.class,i);
		}
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
		enterRule(_localctx, 18, RULE_shapeOf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			_la = _input.LA(1);
			if ( !(_la==RECORDOF || _la==TARGETCLASS) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(140);
			iri();
			setState(145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SC) {
				{
				{
				setState(141);
				match(SC);
				setState(142);
				propertyConstraint();
				}
				}
				setState(147);
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

	public static class PropertyConstraintContext extends ParserRuleContext {
		public TerminalNode PROPERTY() { return getToken(IMLangParser.PROPERTY, 0); }
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
		enterRule(_localctx, 20, RULE_propertyConstraint);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(PROPERTY);
			setState(149);
			match(T__1);
			setState(150);
			match(T__2);
			setState(151);
			iri();
			setState(154); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(152);
					match(SC);
					setState(153);
					constraintParameter();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(156); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(158);
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
		enterRule(_localctx, 22, RULE_constraintParameter);
		try {
			setState(166);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(160);
				minCount();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(161);
				maxCount();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(162);
				minInclusive();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(163);
				maxInclusive();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(164);
				classValue();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(165);
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
		enterRule(_localctx, 24, RULE_minCount);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			match(MINCOUNT);
			setState(169);
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
		enterRule(_localctx, 26, RULE_maxCount);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			match(MAXCOUNT);
			setState(172);
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
		enterRule(_localctx, 28, RULE_minInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			match(MININCLUSIVE);
			setState(175);
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
		enterRule(_localctx, 30, RULE_maxInclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(MAXINCLUSIVE);
			setState(178);
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
		enterRule(_localctx, 32, RULE_minExclusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			match(MINEXCLUSIVE);
			setState(181);
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

	public static class MaxExlusiveContext extends ParserRuleContext {
		public TerminalNode MAXEXCLUSIVE() { return getToken(IMLangParser.MAXEXCLUSIVE, 0); }
		public TerminalNode DOUBLE() { return getToken(IMLangParser.DOUBLE, 0); }
		public MaxExlusiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_maxExlusive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterMaxExlusive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitMaxExlusive(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitMaxExlusive(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MaxExlusiveContext maxExlusive() throws RecognitionException {
		MaxExlusiveContext _localctx = new MaxExlusiveContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_maxExlusive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(MAXEXCLUSIVE);
			setState(184);
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
		enterRule(_localctx, 36, RULE_classValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			_la = _input.LA(1);
			if ( !(_la==T__4 || _la==T__5) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(192);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI:
			case QUOTED_STRING:
				{
				setState(187);
				iri();
				}
				break;
			case T__1:
				{
				setState(188);
				match(T__1);
				setState(189);
				propertyConstraint();
				setState(190);
				match(T__3);
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
		enterRule(_localctx, 38, RULE_label);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(200);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAME:
				{
				setState(194);
				name();
				}
				break;
			case DESCRIPTION:
				{
				setState(195);
				description();
				}
				break;
			case CODE:
				{
				setState(196);
				code();
				}
				break;
			case SCHEME:
				{
				setState(197);
				scheme();
				}
				break;
			case T__6:
				{
				setState(198);
				status();
				}
				break;
			case T__10:
				{
				setState(199);
				version();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(206);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(202);
					match(SC);
					setState(203);
					label();
					}
					} 
				}
				setState(208);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			match(T__6);
			setState(210);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__8) | (1L << T__9))) != 0)) ) {
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
		enterRule(_localctx, 42, RULE_version);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			match(T__10);
			setState(213);
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
		public TerminalNode IRI() { return getToken(IMLangParser.IRI, 0); }
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
		enterRule(_localctx, 44, RULE_identifierIri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRI_LABEL) {
				{
				setState(215);
				match(IRI_LABEL);
				}
			}

			setState(218);
			match(IRI);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 46, RULE_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(220);
			match(NAME);
			setState(221);
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
		enterRule(_localctx, 48, RULE_description);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
			match(DESCRIPTION);
			setState(224);
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
		enterRule(_localctx, 50, RULE_code);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			match(CODE);
			setState(227);
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
		enterRule(_localctx, 52, RULE_scheme);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(SCHEME);
			setState(230);
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
		enterRule(_localctx, 54, RULE_subclassOf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			match(SUBCLASS);
			setState(233);
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
		enterRule(_localctx, 56, RULE_equivalentTo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(235);
			match(EQUIVALENTTO);
			setState(236);
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
		enterRule(_localctx, 58, RULE_disjointWith);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			match(DISJOINT);
			setState(239);
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
		enterRule(_localctx, 60, RULE_subpropertyOf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(241);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__11) | (1L << T__12) | (1L << T__13))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(242);
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
		enterRule(_localctx, 62, RULE_inverseOf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(244);
			_la = _input.LA(1);
			if ( !(_la==T__14 || _la==T__15) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(245);
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
		public ObjectCollectionContext objectCollection() {
			return getRuleContext(ObjectCollectionContext.class,0);
		}
		public RoleGroupContext roleGroup() {
			return getRuleContext(RoleGroupContext.class,0);
		}
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
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
		enterRule(_localctx, 64, RULE_classExpression);
		try {
			setState(250);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__16:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				objectCollection();
				}
				break;
			case T__1:
			case T__19:
				enterOuterAlt(_localctx, 2);
				{
				setState(248);
				roleGroup();
				}
				break;
			case IRI:
			case QUOTED_STRING:
				enterOuterAlt(_localctx, 3);
				{
				setState(249);
				iri();
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

	public static class ObjectCollectionContext extends ParserRuleContext {
		public List<ClassExpressionContext> classExpression() {
			return getRuleContexts(ClassExpressionContext.class);
		}
		public ClassExpressionContext classExpression(int i) {
			return getRuleContext(ClassExpressionContext.class,i);
		}
		public ObjectCollectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectCollection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).enterObjectCollection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IMLangListener ) ((IMLangListener)listener).exitObjectCollection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IMLangVisitor ) return ((IMLangVisitor<? extends T>)visitor).visitObjectCollection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectCollectionContext objectCollection() throws RecognitionException {
		ObjectCollectionContext _localctx = new ObjectCollectionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_objectCollection);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			match(T__16);
			setState(253);
			classExpression();
			setState(258);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(254);
					match(T__17);
					setState(255);
					classExpression();
					}
					} 
				}
				setState(260);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			setState(261);
			match(T__18);
			}
		}
		catch (RecognitionException re) {
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
		public TerminalNode IRI() { return getToken(IMLangParser.IRI, 0); }
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
		enterRule(_localctx, 68, RULE_iri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			_la = _input.LA(1);
			if ( !(_la==IRI || _la==QUOTED_STRING) ) {
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
		enterRule(_localctx, 70, RULE_roleGroup);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(265);
			_la = _input.LA(1);
			if ( !(_la==T__1 || _la==T__19) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(266);
			role();
			setState(271);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(267);
					match(SC);
					setState(268);
					role();
					}
					} 
				}
				setState(273);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			setState(274);
			_la = _input.LA(1);
			if ( !(_la==T__3 || _la==T__20) ) {
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
		enterRule(_localctx, 72, RULE_role);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			iri();
			setState(279);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(277);
				classExpression();
				}
				break;
			case 2:
				{
				setState(278);
				dataRange();
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

	public static class DataRangeContext extends ParserRuleContext {
		public ValueCollectionContext valueCollection() {
			return getRuleContext(ValueCollectionContext.class,0);
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
		enterRule(_localctx, 74, RULE_dataRange);
		try {
			setState(284);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__16:
				enterOuterAlt(_localctx, 1);
				{
				setState(281);
				valueCollection();
				}
				break;
			case QUOTED_STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(282);
				match(QUOTED_STRING);
				}
				break;
			case MININCLUSIVE:
			case MAXINCLUSIVE:
			case MINEXCLUSIVE:
			case MAXEXCLUSIVE:
				enterOuterAlt(_localctx, 3);
				{
				setState(283);
				rangeValue();
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

	public static class RangeValueContext extends ParserRuleContext {
		public List<TerminalNode> QUOTED_STRING() { return getTokens(IMLangParser.QUOTED_STRING); }
		public TerminalNode QUOTED_STRING(int i) {
			return getToken(IMLangParser.QUOTED_STRING, i);
		}
		public TerminalNode MININCLUSIVE() { return getToken(IMLangParser.MININCLUSIVE, 0); }
		public TerminalNode MINEXCLUSIVE() { return getToken(IMLangParser.MINEXCLUSIVE, 0); }
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
		enterRule(_localctx, 76, RULE_rangeValue);
		int _la;
		try {
			setState(295);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(286);
				_la = _input.LA(1);
				if ( !(_la==MININCLUSIVE || _la==MINEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(287);
				match(QUOTED_STRING);
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				{
				setState(288);
				_la = _input.LA(1);
				if ( !(_la==MAXINCLUSIVE || _la==MAXEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(289);
				match(QUOTED_STRING);
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				{
				setState(290);
				_la = _input.LA(1);
				if ( !(_la==MININCLUSIVE || _la==MINEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(291);
				match(QUOTED_STRING);
				}
				{
				setState(293);
				_la = _input.LA(1);
				if ( !(_la==MAXINCLUSIVE || _la==MAXEXCLUSIVE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(294);
				match(QUOTED_STRING);
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

	public static class TypedStringContext extends ParserRuleContext {
		public TerminalNode QUOTED_STRING() { return getToken(IMLangParser.QUOTED_STRING, 0); }
		public TerminalNode IRI() { return getToken(IMLangParser.IRI, 0); }
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
		enterRule(_localctx, 78, RULE_typedString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			match(QUOTED_STRING);
			setState(298);
			match(T__21);
			setState(299);
			match(IRI);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 80, RULE_valueCollection);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(301);
			match(T__16);
			setState(302);
			match(QUOTED_STRING);
			setState(305); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(303);
					match(T__17);
					setState(304);
					match(QUOTED_STRING);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(307); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(309);
			match(T__18);
			}
		}
		catch (RecognitionException re) {
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
		enterRule(_localctx, 82, RULE_dataRangeCollection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			match(T__16);
			setState(312);
			dataRange();
			setState(315); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(313);
				match(T__17);
				setState(314);
				dataRange();
				}
				}
				setState(317); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__17 );
			setState(319);
			match(T__18);
			}
		}
		catch (RecognitionException re) {
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3@\u0144\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\3"+
		"\2\3\2\3\2\3\2\5\2[\n\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\5\3e\n\3\5\3g"+
		"\n\3\3\4\3\4\3\4\5\4l\n\4\5\4n\n\4\3\5\3\5\3\5\3\5\3\5\3\5\5\5v\n\5\3"+
		"\6\3\6\3\6\6\6{\n\6\r\6\16\6|\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\6\t"+
		"\u0088\n\t\r\t\16\t\u0089\3\n\3\n\3\13\3\13\3\13\3\13\7\13\u0092\n\13"+
		"\f\13\16\13\u0095\13\13\3\f\3\f\3\f\3\f\3\f\3\f\6\f\u009d\n\f\r\f\16\f"+
		"\u009e\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00a9\n\r\3\16\3\16\3\16\3"+
		"\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3"+
		"\23\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00c3\n\24\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\5\25\u00cb\n\25\3\25\3\25\7\25\u00cf\n\25\f\25\16\25\u00d2"+
		"\13\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\5\30\u00db\n\30\3\30\3\30\3"+
		"\31\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3"+
		"\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3 \3!\3!\3!\3\"\3\"\3\"\5\"\u00fd"+
		"\n\"\3#\3#\3#\3#\7#\u0103\n#\f#\16#\u0106\13#\3#\3#\3$\3$\3%\3%\3%\3%"+
		"\7%\u0110\n%\f%\16%\u0113\13%\3%\3%\3&\3&\3&\5&\u011a\n&\3\'\3\'\3\'\5"+
		"\'\u011f\n\'\3(\3(\3(\3(\3(\3(\3(\3(\3(\5(\u012a\n(\3)\3)\3)\3)\3*\3*"+
		"\3*\3*\6*\u0134\n*\r*\16*\u0135\3*\3*\3+\3+\3+\3+\6+\u013e\n+\r+\16+\u013f"+
		"\3+\3+\3+\t|\u0089\u009e\u00d0\u0104\u0111\u0135\2,\2\4\6\b\n\f\16\20"+
		"\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRT\2\f\3\2\34\35"+
		"\3\2\7\b\3\2\n\f\3\2\16\20\3\2\21\22\4\288>>\4\2\4\4\26\26\4\2\6\6\27"+
		"\27\4\2))++\4\2**,,\2\u013f\2V\3\2\2\2\4f\3\2\2\2\6m\3\2\2\2\bo\3\2\2"+
		"\2\nw\3\2\2\2\f~\3\2\2\2\16\u0080\3\2\2\2\20\u0084\3\2\2\2\22\u008b\3"+
		"\2\2\2\24\u008d\3\2\2\2\26\u0096\3\2\2\2\30\u00a8\3\2\2\2\32\u00aa\3\2"+
		"\2\2\34\u00ad\3\2\2\2\36\u00b0\3\2\2\2 \u00b3\3\2\2\2\"\u00b6\3\2\2\2"+
		"$\u00b9\3\2\2\2&\u00bc\3\2\2\2(\u00ca\3\2\2\2*\u00d3\3\2\2\2,\u00d6\3"+
		"\2\2\2.\u00da\3\2\2\2\60\u00de\3\2\2\2\62\u00e1\3\2\2\2\64\u00e4\3\2\2"+
		"\2\66\u00e7\3\2\2\28\u00ea\3\2\2\2:\u00ed\3\2\2\2<\u00f0\3\2\2\2>\u00f3"+
		"\3\2\2\2@\u00f6\3\2\2\2B\u00fc\3\2\2\2D\u00fe\3\2\2\2F\u0109\3\2\2\2H"+
		"\u010b\3\2\2\2J\u0116\3\2\2\2L\u011e\3\2\2\2N\u0129\3\2\2\2P\u012b\3\2"+
		"\2\2R\u012f\3\2\2\2T\u0139\3\2\2\2VZ\5.\30\2WX\5(\25\2XY\7@\2\2Y[\3\2"+
		"\2\2ZW\3\2\2\2Z[\3\2\2\2[\\\3\2\2\2\\]\5\b\5\2]^\7\3\2\2^_\7\2\2\3_\3"+
		"\3\2\2\2`g\58\35\2ag\5:\36\2bd\5<\37\2ce\5(\25\2dc\3\2\2\2de\3\2\2\2e"+
		"g\3\2\2\2f`\3\2\2\2fa\3\2\2\2fb\3\2\2\2g\5\3\2\2\2hn\5> \2ik\5@!\2jl\5"+
		"(\25\2kj\3\2\2\2kl\3\2\2\2ln\3\2\2\2mh\3\2\2\2mi\3\2\2\2n\7\3\2\2\2ou"+
		"\7\32\2\2pv\5\n\6\2qv\5\f\7\2rv\5\16\b\2sv\5\22\n\2tv\5\20\t\2up\3\2\2"+
		"\2uq\3\2\2\2ur\3\2\2\2us\3\2\2\2ut\3\2\2\2v\t\3\2\2\2wz\7\36\2\2xy\7@"+
		"\2\2y{\5\4\3\2zx\3\2\2\2{|\3\2\2\2|}\3\2\2\2|z\3\2\2\2}\13\3\2\2\2~\177"+
		"\7 \2\2\177\r\3\2\2\2\u0080\u0081\7\33\2\2\u0081\u0082\7@\2\2\u0082\u0083"+
		"\5\24\13\2\u0083\17\3\2\2\2\u0084\u0087\7\37\2\2\u0085\u0086\7@\2\2\u0086"+
		"\u0088\5\6\4\2\u0087\u0085\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a\3\2"+
		"\2\2\u0089\u0087\3\2\2\2\u008a\21\3\2\2\2\u008b\u008c\7!\2\2\u008c\23"+
		"\3\2\2\2\u008d\u008e\t\2\2\2\u008e\u0093\5F$\2\u008f\u0090\7@\2\2\u0090"+
		"\u0092\5\26\f\2\u0091\u008f\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3"+
		"\2\2\2\u0093\u0094\3\2\2\2\u0094\25\3\2\2\2\u0095\u0093\3\2\2\2\u0096"+
		"\u0097\7\37\2\2\u0097\u0098\7\4\2\2\u0098\u0099\7\5\2\2\u0099\u009c\5"+
		"F$\2\u009a\u009b\7@\2\2\u009b\u009d\5\30\r\2\u009c\u009a\3\2\2\2\u009d"+
		"\u009e\3\2\2\2\u009e\u009f\3\2\2\2\u009e\u009c\3\2\2\2\u009f\u00a0\3\2"+
		"\2\2\u00a0\u00a1\7\6\2\2\u00a1\27\3\2\2\2\u00a2\u00a9\5\32\16\2\u00a3"+
		"\u00a9\5\34\17\2\u00a4\u00a9\5\36\20\2\u00a5\u00a9\5 \21\2\u00a6\u00a9"+
		"\5&\24\2\u00a7\u00a9\5L\'\2\u00a8\u00a2\3\2\2\2\u00a8\u00a3\3\2\2\2\u00a8"+
		"\u00a4\3\2\2\2\u00a8\u00a5\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a7\3\2"+
		"\2\2\u00a9\31\3\2\2\2\u00aa\u00ab\7#\2\2\u00ab\u00ac\7\62\2\2\u00ac\33"+
		"\3\2\2\2\u00ad\u00ae\7$\2\2\u00ae\u00af\7\62\2\2\u00af\35\3\2\2\2\u00b0"+
		"\u00b1\7)\2\2\u00b1\u00b2\7\63\2\2\u00b2\37\3\2\2\2\u00b3\u00b4\7*\2\2"+
		"\u00b4\u00b5\7\63\2\2\u00b5!\3\2\2\2\u00b6\u00b7\7+\2\2\u00b7\u00b8\7"+
		"\63\2\2\u00b8#\3\2\2\2\u00b9\u00ba\7,\2\2\u00ba\u00bb\7\63\2\2\u00bb%"+
		"\3\2\2\2\u00bc\u00c2\t\3\2\2\u00bd\u00c3\5F$\2\u00be\u00bf\7\4\2\2\u00bf"+
		"\u00c0\5\26\f\2\u00c0\u00c1\7\6\2\2\u00c1\u00c3\3\2\2\2\u00c2\u00bd\3"+
		"\2\2\2\u00c2\u00be\3\2\2\2\u00c3\'\3\2\2\2\u00c4\u00cb\5\60\31\2\u00c5"+
		"\u00cb\5\62\32\2\u00c6\u00cb\5\64\33\2\u00c7\u00cb\5\66\34\2\u00c8\u00cb"+
		"\5*\26\2\u00c9\u00cb\5,\27\2\u00ca\u00c4\3\2\2\2\u00ca\u00c5\3\2\2\2\u00ca"+
		"\u00c6\3\2\2\2\u00ca\u00c7\3\2\2\2\u00ca\u00c8\3\2\2\2\u00ca\u00c9\3\2"+
		"\2\2\u00cb\u00d0\3\2\2\2\u00cc\u00cd\7@\2\2\u00cd\u00cf\5(\25\2\u00ce"+
		"\u00cc\3\2\2\2\u00cf\u00d2\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d0\u00ce\3\2"+
		"\2\2\u00d1)\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d3\u00d4\7\t\2\2\u00d4\u00d5"+
		"\t\4\2\2\u00d5+\3\2\2\2\u00d6\u00d7\7\r\2\2\u00d7\u00d8\7>\2\2\u00d8-"+
		"\3\2\2\2\u00d9\u00db\7\31\2\2\u00da\u00d9\3\2\2\2\u00da\u00db\3\2\2\2"+
		"\u00db\u00dc\3\2\2\2\u00dc\u00dd\78\2\2\u00dd/\3\2\2\2\u00de\u00df\7%"+
		"\2\2\u00df\u00e0\7>\2\2\u00e0\61\3\2\2\2\u00e1\u00e2\7&\2\2\u00e2\u00e3"+
		"\7>\2\2\u00e3\63\3\2\2\2\u00e4\u00e5\7\'\2\2\u00e5\u00e6\7>\2\2\u00e6"+
		"\65\3\2\2\2\u00e7\u00e8\7(\2\2\u00e8\u00e9\7>\2\2\u00e9\67\3\2\2\2\u00ea"+
		"\u00eb\7-\2\2\u00eb\u00ec\5B\"\2\u00ec9\3\2\2\2\u00ed\u00ee\7.\2\2\u00ee"+
		"\u00ef\5B\"\2\u00ef;\3\2\2\2\u00f0\u00f1\7/\2\2\u00f1\u00f2\5B\"\2\u00f2"+
		"=\3\2\2\2\u00f3\u00f4\t\5\2\2\u00f4\u00f5\5F$\2\u00f5?\3\2\2\2\u00f6\u00f7"+
		"\t\6\2\2\u00f7\u00f8\5F$\2\u00f8A\3\2\2\2\u00f9\u00fd\5D#\2\u00fa\u00fd"+
		"\5H%\2\u00fb\u00fd\5F$\2\u00fc\u00f9\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc"+
		"\u00fb\3\2\2\2\u00fdC\3\2\2\2\u00fe\u00ff\7\23\2\2\u00ff\u0104\5B\"\2"+
		"\u0100\u0101\7\24\2\2\u0101\u0103\5B\"\2\u0102\u0100\3\2\2\2\u0103\u0106"+
		"\3\2\2\2\u0104\u0105\3\2\2\2\u0104\u0102\3\2\2\2\u0105\u0107\3\2\2\2\u0106"+
		"\u0104\3\2\2\2\u0107\u0108\7\25\2\2\u0108E\3\2\2\2\u0109\u010a\t\7\2\2"+
		"\u010aG\3\2\2\2\u010b\u010c\t\b\2\2\u010c\u0111\5J&\2\u010d\u010e\7@\2"+
		"\2\u010e\u0110\5J&\2\u010f\u010d\3\2\2\2\u0110\u0113\3\2\2\2\u0111\u0112"+
		"\3\2\2\2\u0111\u010f\3\2\2\2\u0112\u0114\3\2\2\2\u0113\u0111\3\2\2\2\u0114"+
		"\u0115\t\t\2\2\u0115I\3\2\2\2\u0116\u0119\5F$\2\u0117\u011a\5B\"\2\u0118"+
		"\u011a\5L\'\2\u0119\u0117\3\2\2\2\u0119\u0118\3\2\2\2\u011aK\3\2\2\2\u011b"+
		"\u011f\5R*\2\u011c\u011f\7>\2\2\u011d\u011f\5N(\2\u011e\u011b\3\2\2\2"+
		"\u011e\u011c\3\2\2\2\u011e\u011d\3\2\2\2\u011fM\3\2\2\2\u0120\u0121\t"+
		"\n\2\2\u0121\u012a\7>\2\2\u0122\u0123\t\13\2\2\u0123\u012a\7>\2\2\u0124"+
		"\u0125\t\n\2\2\u0125\u0126\7>\2\2\u0126\u0127\3\2\2\2\u0127\u0128\t\13"+
		"\2\2\u0128\u012a\7>\2\2\u0129\u0120\3\2\2\2\u0129\u0122\3\2\2\2\u0129"+
		"\u0124\3\2\2\2\u012aO\3\2\2\2\u012b\u012c\7>\2\2\u012c\u012d\7\30\2\2"+
		"\u012d\u012e\78\2\2\u012eQ\3\2\2\2\u012f\u0130\7\23\2\2\u0130\u0133\7"+
		">\2\2\u0131\u0132\7\24\2\2\u0132\u0134\7>\2\2\u0133\u0131\3\2\2\2\u0134"+
		"\u0135\3\2\2\2\u0135\u0136\3\2\2\2\u0135\u0133\3\2\2\2\u0136\u0137\3\2"+
		"\2\2\u0137\u0138\7\25\2\2\u0138S\3\2\2\2\u0139\u013a\7\23\2\2\u013a\u013d"+
		"\5L\'\2\u013b\u013c\7\24\2\2\u013c\u013e\5L\'\2\u013d\u013b\3\2\2\2\u013e"+
		"\u013f\3\2\2\2\u013f\u013d\3\2\2\2\u013f\u0140\3\2\2\2\u0140\u0141\3\2"+
		"\2\2\u0141\u0142\7\25\2\2\u0142U\3\2\2\2\31Zdfkmu|\u0089\u0093\u009e\u00a8"+
		"\u00c2\u00ca\u00d0\u00da\u00fc\u0104\u0111\u0119\u011e\u0129\u0135\u013f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}