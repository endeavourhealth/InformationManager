// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\ENDOWL.g4 by ANTLR 4.9
package org.endeavourhealth.informationmanager.grammar;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ENDOWLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		MIN=1, MAX=2, EXACTLY=3, SOME=4, ONLY=5, PROPERTIES=6, PROPERTYCHAIN=7, 
		SUBOBJECTPROPERTY=8, ENTITYTYPE=9, OBJECTPROPERTY=10, SUBCLASS=11, EQUIVALENT=12, 
		CLASSENTITY=13, AXIOMTYPE=14, CLASS=15, INTEGER=16, LOWERCASE=17, UPPERCASE=18, 
		TAB=19, LF=20, CR=21, SPACE=22, EXCLAMATION=23, QUOTE=24, POUND=25, DOLLAR=26, 
		PERCENT=27, AMPERSAND=28, APOSTROPHE=29, LEFT_PAREN=30, RIGHT_PAREN=31, 
		ASTERISK=32, PLUS=33, COMMA=34, DASH=35, PERIOD=36, SLASH=37, ZERO=38, 
		ONE=39, TWO=40, THREE=41, FOUR=42, FIVE=43, SIX=44, SEVEN=45, EIGHT=46, 
		NINE=47, COLON=48, SEMICOLON=49, LESS_THAN=50, EQUALS=51, GREATER_THAN=52, 
		QUESTION=53, AT=54, LEFT_BRACE=55, BACKSLASH=56, RIGHT_BRACE=57, CARAT=58, 
		UNDERSCORE=59, ACCENT=60, A=61, B=62, C=63, D=64, E=65, F=66, G=67, H=68, 
		I=69, J=70, K=71, L=72, M=73, N=74, O=75, P=76, Q=77, R=78, S=79, T=80, 
		U=81, V=82, W=83, X=84, Y=85, Z=86, LEFT_CURLY_BRACE=87, PIPE=88, RIGHT_CURLY_BRACE=89, 
		TILDE=90;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"MIN", "MAX", "EXACTLY", "SOME", "ONLY", "PROPERTIES", "PROPERTYCHAIN", 
			"SUBOBJECTPROPERTY", "ENTITYTYPE", "OBJECTPROPERTY", "SUBCLASS", "EQUIVALENT", 
			"CLASSENTITY", "AXIOMTYPE", "CLASS", "INTEGER", "LOWERCASE", "UPPERCASE", 
			"TAB", "LF", "CR", "SPACE", "EXCLAMATION", "QUOTE", "POUND", "DOLLAR", 
			"PERCENT", "AMPERSAND", "APOSTROPHE", "LEFT_PAREN", "RIGHT_PAREN", "ASTERISK", 
			"PLUS", "COMMA", "DASH", "PERIOD", "SLASH", "ZERO", "ONE", "TWO", "THREE", 
			"FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "COLON", "SEMICOLON", 
			"LESS_THAN", "EQUALS", "GREATER_THAN", "QUESTION", "AT", "LEFT_BRACE", 
			"BACKSLASH", "RIGHT_BRACE", "CARAT", "UNDERSCORE", "ACCENT", "A", "B", 
			"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", 
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "LEFT_CURLY_BRACE", 
			"PIPE", "RIGHT_CURLY_BRACE", "TILDE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'min'", "'max'", "'exactly'", "'at least one'", "'only'", "'Property'", 
			"'Property chain'", "'Subproperty of'", "'Entity type'", "'Object property'", 
			"'Subclass of'", "'Equivalent to'", "'Class entity'", "'Axiom type'", 
			"'Class'", null, null, null, null, null, null, "' '", "'!'", "'\"'", 
			"'#'", "'$'", "'%'", "'&'", "'''", "'('", "')'", "'*'", "'+'", "','", 
			"'-'", "'.'", "'/'", "'0'", "'1'", "'2'", "'3'", "'4'", "'5'", "'6'", 
			"'7'", "'8'", "'9'", "':'", "';'", "'<'", "'='", "'>'", "'?'", "'@'", 
			"'['", "'\\'", "']'", "'^'", "'_'", "'`'", "'a'", "'b'", "'c'", "'d'", 
			"'e'", "'f'", "'g'", "'h'", "'i'", "'j'", "'k'", "'l'", "'m'", "'n'", 
			"'o'", "'p'", "'q'", "'r'", "'s'", "'t'", "'u'", "'v'", "'w'", "'x'", 
			"'y'", "'z'", "'{'", "'|'", "'}'", "'~'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "MIN", "MAX", "EXACTLY", "SOME", "ONLY", "PROPERTIES", "PROPERTYCHAIN", 
			"SUBOBJECTPROPERTY", "ENTITYTYPE", "OBJECTPROPERTY", "SUBCLASS", "EQUIVALENT", 
			"CLASSENTITY", "AXIOMTYPE", "CLASS", "INTEGER", "LOWERCASE", "UPPERCASE", 
			"TAB", "LF", "CR", "SPACE", "EXCLAMATION", "QUOTE", "POUND", "DOLLAR", 
			"PERCENT", "AMPERSAND", "APOSTROPHE", "LEFT_PAREN", "RIGHT_PAREN", "ASTERISK", 
			"PLUS", "COMMA", "DASH", "PERIOD", "SLASH", "ZERO", "ONE", "TWO", "THREE", 
			"FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "COLON", "SEMICOLON", 
			"LESS_THAN", "EQUALS", "GREATER_THAN", "QUESTION", "AT", "LEFT_BRACE", 
			"BACKSLASH", "RIGHT_BRACE", "CARAT", "UNDERSCORE", "ACCENT", "A", "B", 
			"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", 
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "LEFT_CURLY_BRACE", 
			"PIPE", "RIGHT_CURLY_BRACE", "TILDE"
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


	public ENDOWLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ENDOWL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\\\u01ea\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\3\2\3\2\3\2\3\2\3\3\3\3\3\3"+
		"\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3"+
		"\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3"+
		"\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&"+
		"\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60"+
		"\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67"+
		"\38\38\39\39\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3A\3A\3B\3B\3C"+
		"\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N"+
		"\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U\3V\3V\3W\3W\3X\3X\3Y\3Y\3Z"+
		"\3Z\3[\3[\2\2\\\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65"+
		"\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64"+
		"g\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085D\u0087E\u0089"+
		"F\u008bG\u008dH\u008fI\u0091J\u0093K\u0095L\u0097M\u0099N\u009bO\u009d"+
		"P\u009fQ\u00a1R\u00a3S\u00a5T\u00a7U\u00a9V\u00abW\u00adX\u00afY\u00b1"+
		"Z\u00b3[\u00b5\\\3\2\b\3\2\62;\3\2c|\3\2C\\\3\2\13\13\3\2\f\f\3\2\17\17"+
		"\2\u01e9\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2"+
		"\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2"+
		"\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2"+
		"\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S"+
		"\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2"+
		"\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2"+
		"\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y"+
		"\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3"+
		"\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2"+
		"\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095"+
		"\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2"+
		"\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7"+
		"\3\2\2\2\2\u00a9\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2"+
		"\2\2\u00b1\3\2\2\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\3\u00b7\3\2\2\2\5\u00bb"+
		"\3\2\2\2\7\u00bf\3\2\2\2\t\u00c7\3\2\2\2\13\u00d4\3\2\2\2\r\u00d9\3\2"+
		"\2\2\17\u00e2\3\2\2\2\21\u00f1\3\2\2\2\23\u0100\3\2\2\2\25\u010c\3\2\2"+
		"\2\27\u011c\3\2\2\2\31\u0128\3\2\2\2\33\u0136\3\2\2\2\35\u0143\3\2\2\2"+
		"\37\u014e\3\2\2\2!\u0154\3\2\2\2#\u0156\3\2\2\2%\u0158\3\2\2\2\'\u015a"+
		"\3\2\2\2)\u015c\3\2\2\2+\u015e\3\2\2\2-\u0160\3\2\2\2/\u0162\3\2\2\2\61"+
		"\u0164\3\2\2\2\63\u0166\3\2\2\2\65\u0168\3\2\2\2\67\u016a\3\2\2\29\u016c"+
		"\3\2\2\2;\u016e\3\2\2\2=\u0170\3\2\2\2?\u0172\3\2\2\2A\u0174\3\2\2\2C"+
		"\u0176\3\2\2\2E\u0178\3\2\2\2G\u017a\3\2\2\2I\u017c\3\2\2\2K\u017e\3\2"+
		"\2\2M\u0180\3\2\2\2O\u0182\3\2\2\2Q\u0184\3\2\2\2S\u0186\3\2\2\2U\u0188"+
		"\3\2\2\2W\u018a\3\2\2\2Y\u018c\3\2\2\2[\u018e\3\2\2\2]\u0190\3\2\2\2_"+
		"\u0192\3\2\2\2a\u0194\3\2\2\2c\u0196\3\2\2\2e\u0198\3\2\2\2g\u019a\3\2"+
		"\2\2i\u019c\3\2\2\2k\u019e\3\2\2\2m\u01a0\3\2\2\2o\u01a2\3\2\2\2q\u01a4"+
		"\3\2\2\2s\u01a6\3\2\2\2u\u01a8\3\2\2\2w\u01aa\3\2\2\2y\u01ac\3\2\2\2{"+
		"\u01ae\3\2\2\2}\u01b0\3\2\2\2\177\u01b2\3\2\2\2\u0081\u01b4\3\2\2\2\u0083"+
		"\u01b6\3\2\2\2\u0085\u01b8\3\2\2\2\u0087\u01ba\3\2\2\2\u0089\u01bc\3\2"+
		"\2\2\u008b\u01be\3\2\2\2\u008d\u01c0\3\2\2\2\u008f\u01c2\3\2\2\2\u0091"+
		"\u01c4\3\2\2\2\u0093\u01c6\3\2\2\2\u0095\u01c8\3\2\2\2\u0097\u01ca\3\2"+
		"\2\2\u0099\u01cc\3\2\2\2\u009b\u01ce\3\2\2\2\u009d\u01d0\3\2\2\2\u009f"+
		"\u01d2\3\2\2\2\u00a1\u01d4\3\2\2\2\u00a3\u01d6\3\2\2\2\u00a5\u01d8\3\2"+
		"\2\2\u00a7\u01da\3\2\2\2\u00a9\u01dc\3\2\2\2\u00ab\u01de\3\2\2\2\u00ad"+
		"\u01e0\3\2\2\2\u00af\u01e2\3\2\2\2\u00b1\u01e4\3\2\2\2\u00b3\u01e6\3\2"+
		"\2\2\u00b5\u01e8\3\2\2\2\u00b7\u00b8\7o\2\2\u00b8\u00b9\7k\2\2\u00b9\u00ba"+
		"\7p\2\2\u00ba\4\3\2\2\2\u00bb\u00bc\7o\2\2\u00bc\u00bd\7c\2\2\u00bd\u00be"+
		"\7z\2\2\u00be\6\3\2\2\2\u00bf\u00c0\7g\2\2\u00c0\u00c1\7z\2\2\u00c1\u00c2"+
		"\7c\2\2\u00c2\u00c3\7e\2\2\u00c3\u00c4\7v\2\2\u00c4\u00c5\7n\2\2\u00c5"+
		"\u00c6\7{\2\2\u00c6\b\3\2\2\2\u00c7\u00c8\7c\2\2\u00c8\u00c9\7v\2\2\u00c9"+
		"\u00ca\7\"\2\2\u00ca\u00cb\7n\2\2\u00cb\u00cc\7g\2\2\u00cc\u00cd\7c\2"+
		"\2\u00cd\u00ce\7u\2\2\u00ce\u00cf\7v\2\2\u00cf\u00d0\7\"\2\2\u00d0\u00d1"+
		"\7q\2\2\u00d1\u00d2\7p\2\2\u00d2\u00d3\7g\2\2\u00d3\n\3\2\2\2\u00d4\u00d5"+
		"\7q\2\2\u00d5\u00d6\7p\2\2\u00d6\u00d7\7n\2\2\u00d7\u00d8\7{\2\2\u00d8"+
		"\f\3\2\2\2\u00d9\u00da\7R\2\2\u00da\u00db\7t\2\2\u00db\u00dc\7q\2\2\u00dc"+
		"\u00dd\7r\2\2\u00dd\u00de\7g\2\2\u00de\u00df\7t\2\2\u00df\u00e0\7v\2\2"+
		"\u00e0\u00e1\7{\2\2\u00e1\16\3\2\2\2\u00e2\u00e3\7R\2\2\u00e3\u00e4\7"+
		"t\2\2\u00e4\u00e5\7q\2\2\u00e5\u00e6\7r\2\2\u00e6\u00e7\7g\2\2\u00e7\u00e8"+
		"\7t\2\2\u00e8\u00e9\7v\2\2\u00e9\u00ea\7{\2\2\u00ea\u00eb\7\"\2\2\u00eb"+
		"\u00ec\7e\2\2\u00ec\u00ed\7j\2\2\u00ed\u00ee\7c\2\2\u00ee\u00ef\7k\2\2"+
		"\u00ef\u00f0\7p\2\2\u00f0\20\3\2\2\2\u00f1\u00f2\7U\2\2\u00f2\u00f3\7"+
		"w\2\2\u00f3\u00f4\7d\2\2\u00f4\u00f5\7r\2\2\u00f5\u00f6\7t\2\2\u00f6\u00f7"+
		"\7q\2\2\u00f7\u00f8\7r\2\2\u00f8\u00f9\7g\2\2\u00f9\u00fa\7t\2\2\u00fa"+
		"\u00fb\7v\2\2\u00fb\u00fc\7{\2\2\u00fc\u00fd\7\"\2\2\u00fd\u00fe\7q\2"+
		"\2\u00fe\u00ff\7h\2\2\u00ff\22\3\2\2\2\u0100\u0101\7G\2\2\u0101\u0102"+
		"\7p\2\2\u0102\u0103\7v\2\2\u0103\u0104\7k\2\2\u0104\u0105\7v\2\2\u0105"+
		"\u0106\7{\2\2\u0106\u0107\7\"\2\2\u0107\u0108\7v\2\2\u0108\u0109\7{\2"+
		"\2\u0109\u010a\7r\2\2\u010a\u010b\7g\2\2\u010b\24\3\2\2\2\u010c\u010d"+
		"\7Q\2\2\u010d\u010e\7d\2\2\u010e\u010f\7l\2\2\u010f\u0110\7g\2\2\u0110"+
		"\u0111\7e\2\2\u0111\u0112\7v\2\2\u0112\u0113\7\"\2\2\u0113\u0114\7r\2"+
		"\2\u0114\u0115\7t\2\2\u0115\u0116\7q\2\2\u0116\u0117\7r\2\2\u0117\u0118"+
		"\7g\2\2\u0118\u0119\7t\2\2\u0119\u011a\7v\2\2\u011a\u011b\7{\2\2\u011b"+
		"\26\3\2\2\2\u011c\u011d\7U\2\2\u011d\u011e\7w\2\2\u011e\u011f\7d\2\2\u011f"+
		"\u0120\7e\2\2\u0120\u0121\7n\2\2\u0121\u0122\7c\2\2\u0122\u0123\7u\2\2"+
		"\u0123\u0124\7u\2\2\u0124\u0125\7\"\2\2\u0125\u0126\7q\2\2\u0126\u0127"+
		"\7h\2\2\u0127\30\3\2\2\2\u0128\u0129\7G\2\2\u0129\u012a\7s\2\2\u012a\u012b"+
		"\7w\2\2\u012b\u012c\7k\2\2\u012c\u012d\7x\2\2\u012d\u012e\7c\2\2\u012e"+
		"\u012f\7n\2\2\u012f\u0130\7g\2\2\u0130\u0131\7p\2\2\u0131\u0132\7v\2\2"+
		"\u0132\u0133\7\"\2\2\u0133\u0134\7v\2\2\u0134\u0135\7q\2\2\u0135\32\3"+
		"\2\2\2\u0136\u0137\7E\2\2\u0137\u0138\7n\2\2\u0138\u0139\7c\2\2\u0139"+
		"\u013a\7u\2\2\u013a\u013b\7u\2\2\u013b\u013c\7\"\2\2\u013c\u013d\7g\2"+
		"\2\u013d\u013e\7p\2\2\u013e\u013f\7v\2\2\u013f\u0140\7k\2\2\u0140\u0141"+
		"\7v\2\2\u0141\u0142\7{\2\2\u0142\34\3\2\2\2\u0143\u0144\7C\2\2\u0144\u0145"+
		"\7z\2\2\u0145\u0146\7k\2\2\u0146\u0147\7q\2\2\u0147\u0148\7o\2\2\u0148"+
		"\u0149\7\"\2\2\u0149\u014a\7v\2\2\u014a\u014b\7{\2\2\u014b\u014c\7r\2"+
		"\2\u014c\u014d\7g\2\2\u014d\36\3\2\2\2\u014e\u014f\7E\2\2\u014f\u0150"+
		"\7n\2\2\u0150\u0151\7c\2\2\u0151\u0152\7u\2\2\u0152\u0153\7u\2\2\u0153"+
		" \3\2\2\2\u0154\u0155\t\2\2\2\u0155\"\3\2\2\2\u0156\u0157\t\3\2\2\u0157"+
		"$\3\2\2\2\u0158\u0159\t\4\2\2\u0159&\3\2\2\2\u015a\u015b\t\5\2\2\u015b"+
		"(\3\2\2\2\u015c\u015d\t\6\2\2\u015d*\3\2\2\2\u015e\u015f\t\7\2\2\u015f"+
		",\3\2\2\2\u0160\u0161\7\"\2\2\u0161.\3\2\2\2\u0162\u0163\7#\2\2\u0163"+
		"\60\3\2\2\2\u0164\u0165\7$\2\2\u0165\62\3\2\2\2\u0166\u0167\7%\2\2\u0167"+
		"\64\3\2\2\2\u0168\u0169\7&\2\2\u0169\66\3\2\2\2\u016a\u016b\7\'\2\2\u016b"+
		"8\3\2\2\2\u016c\u016d\7(\2\2\u016d:\3\2\2\2\u016e\u016f\7)\2\2\u016f<"+
		"\3\2\2\2\u0170\u0171\7*\2\2\u0171>\3\2\2\2\u0172\u0173\7+\2\2\u0173@\3"+
		"\2\2\2\u0174\u0175\7,\2\2\u0175B\3\2\2\2\u0176\u0177\7-\2\2\u0177D\3\2"+
		"\2\2\u0178\u0179\7.\2\2\u0179F\3\2\2\2\u017a\u017b\7/\2\2\u017bH\3\2\2"+
		"\2\u017c\u017d\7\60\2\2\u017dJ\3\2\2\2\u017e\u017f\7\61\2\2\u017fL\3\2"+
		"\2\2\u0180\u0181\7\62\2\2\u0181N\3\2\2\2\u0182\u0183\7\63\2\2\u0183P\3"+
		"\2\2\2\u0184\u0185\7\64\2\2\u0185R\3\2\2\2\u0186\u0187\7\65\2\2\u0187"+
		"T\3\2\2\2\u0188\u0189\7\66\2\2\u0189V\3\2\2\2\u018a\u018b\7\67\2\2\u018b"+
		"X\3\2\2\2\u018c\u018d\78\2\2\u018dZ\3\2\2\2\u018e\u018f\79\2\2\u018f\\"+
		"\3\2\2\2\u0190\u0191\7:\2\2\u0191^\3\2\2\2\u0192\u0193\7;\2\2\u0193`\3"+
		"\2\2\2\u0194\u0195\7<\2\2\u0195b\3\2\2\2\u0196\u0197\7=\2\2\u0197d\3\2"+
		"\2\2\u0198\u0199\7>\2\2\u0199f\3\2\2\2\u019a\u019b\7?\2\2\u019bh\3\2\2"+
		"\2\u019c\u019d\7@\2\2\u019dj\3\2\2\2\u019e\u019f\7A\2\2\u019fl\3\2\2\2"+
		"\u01a0\u01a1\7B\2\2\u01a1n\3\2\2\2\u01a2\u01a3\7]\2\2\u01a3p\3\2\2\2\u01a4"+
		"\u01a5\7^\2\2\u01a5r\3\2\2\2\u01a6\u01a7\7_\2\2\u01a7t\3\2\2\2\u01a8\u01a9"+
		"\7`\2\2\u01a9v\3\2\2\2\u01aa\u01ab\7a\2\2\u01abx\3\2\2\2\u01ac\u01ad\7"+
		"b\2\2\u01adz\3\2\2\2\u01ae\u01af\7c\2\2\u01af|\3\2\2\2\u01b0\u01b1\7d"+
		"\2\2\u01b1~\3\2\2\2\u01b2\u01b3\7e\2\2\u01b3\u0080\3\2\2\2\u01b4\u01b5"+
		"\7f\2\2\u01b5\u0082\3\2\2\2\u01b6\u01b7\7g\2\2\u01b7\u0084\3\2\2\2\u01b8"+
		"\u01b9\7h\2\2\u01b9\u0086\3\2\2\2\u01ba\u01bb\7i\2\2\u01bb\u0088\3\2\2"+
		"\2\u01bc\u01bd\7j\2\2\u01bd\u008a\3\2\2\2\u01be\u01bf\7k\2\2\u01bf\u008c"+
		"\3\2\2\2\u01c0\u01c1\7l\2\2\u01c1\u008e\3\2\2\2\u01c2\u01c3\7m\2\2\u01c3"+
		"\u0090\3\2\2\2\u01c4\u01c5\7n\2\2\u01c5\u0092\3\2\2\2\u01c6\u01c7\7o\2"+
		"\2\u01c7\u0094\3\2\2\2\u01c8\u01c9\7p\2\2\u01c9\u0096\3\2\2\2\u01ca\u01cb"+
		"\7q\2\2\u01cb\u0098\3\2\2\2\u01cc\u01cd\7r\2\2\u01cd\u009a\3\2\2\2\u01ce"+
		"\u01cf\7s\2\2\u01cf\u009c\3\2\2\2\u01d0\u01d1\7t\2\2\u01d1\u009e\3\2\2"+
		"\2\u01d2\u01d3\7u\2\2\u01d3\u00a0\3\2\2\2\u01d4\u01d5\7v\2\2\u01d5\u00a2"+
		"\3\2\2\2\u01d6\u01d7\7w\2\2\u01d7\u00a4\3\2\2\2\u01d8\u01d9\7x\2\2\u01d9"+
		"\u00a6\3\2\2\2\u01da\u01db\7y\2\2\u01db\u00a8\3\2\2\2\u01dc\u01dd\7z\2"+
		"\2\u01dd\u00aa\3\2\2\2\u01de\u01df\7{\2\2\u01df\u00ac\3\2\2\2\u01e0\u01e1"+
		"\7|\2\2\u01e1\u00ae\3\2\2\2\u01e2\u01e3\7}\2\2\u01e3\u00b0\3\2\2\2\u01e4"+
		"\u01e5\7~\2\2\u01e5\u00b2\3\2\2\2\u01e6\u01e7\7\177\2\2\u01e7\u00b4\3"+
		"\2\2\2\u01e8\u01e9\7\u0080\2\2\u01e9\u00b6\3\2\2\2\3\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}