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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"VALUESET", "MIN", "MAX", "EXACTLY", "SOME", "ONLY", "UNION", "PROPERTIES", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2^\u0202\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3"+
		"\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32"+
		"\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3"+
		"\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3"+
		"-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\65"+
		"\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3"+
		"?\3?\3@\3@\3A\3A\3B\3B\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3H\3H\3I\3I\3J\3"+
		"J\3K\3K\3L\3L\3M\3M\3N\3N\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U\3"+
		"V\3V\3W\3W\3X\3X\3Y\3Y\3Z\3Z\3[\3[\3\\\3\\\3]\3]\2\2^\3\3\5\4\7\5\t\6"+
		"\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24"+
		"\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K"+
		"\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o9q:s;u<w=y>{?}@\177"+
		"A\u0081B\u0083C\u0085D\u0087E\u0089F\u008bG\u008dH\u008fI\u0091J\u0093"+
		"K\u0095L\u0097M\u0099N\u009bO\u009dP\u009fQ\u00a1R\u00a3S\u00a5T\u00a7"+
		"U\u00a9V\u00abW\u00adX\u00afY\u00b1Z\u00b3[\u00b5\\\u00b7]\u00b9^\3\2"+
		"\b\3\2\62;\3\2c|\3\2C\\\3\2\13\13\3\2\f\f\3\2\17\17\2\u0201\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"+
		"\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"+
		"\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2"+
		"\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2"+
		"\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3"+
		"\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2"+
		"\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2"+
		"W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3"+
		"\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2"+
		"\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2"+
		"}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2"+
		"\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u008f"+
		"\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2"+
		"\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1"+
		"\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2"+
		"\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2\2\2\u00b3"+
		"\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\3\u00bb\3\2\2"+
		"\2\5\u00c5\3\2\2\2\7\u00c9\3\2\2\2\t\u00cd\3\2\2\2\13\u00d5\3\2\2\2\r"+
		"\u00e2\3\2\2\2\17\u00e7\3\2\2\2\21\u00f1\3\2\2\2\23\u00fa\3\2\2\2\25\u0109"+
		"\3\2\2\2\27\u0118\3\2\2\2\31\u0124\3\2\2\2\33\u0134\3\2\2\2\35\u0140\3"+
		"\2\2\2\37\u014e\3\2\2\2!\u015b\3\2\2\2#\u0166\3\2\2\2%\u016c\3\2\2\2\'"+
		"\u016e\3\2\2\2)\u0170\3\2\2\2+\u0172\3\2\2\2-\u0174\3\2\2\2/\u0176\3\2"+
		"\2\2\61\u0178\3\2\2\2\63\u017a\3\2\2\2\65\u017c\3\2\2\2\67\u017e\3\2\2"+
		"\29\u0180\3\2\2\2;\u0182\3\2\2\2=\u0184\3\2\2\2?\u0186\3\2\2\2A\u0188"+
		"\3\2\2\2C\u018a\3\2\2\2E\u018c\3\2\2\2G\u018e\3\2\2\2I\u0190\3\2\2\2K"+
		"\u0192\3\2\2\2M\u0194\3\2\2\2O\u0196\3\2\2\2Q\u0198\3\2\2\2S\u019a\3\2"+
		"\2\2U\u019c\3\2\2\2W\u019e\3\2\2\2Y\u01a0\3\2\2\2[\u01a2\3\2\2\2]\u01a4"+
		"\3\2\2\2_\u01a6\3\2\2\2a\u01a8\3\2\2\2c\u01aa\3\2\2\2e\u01ac\3\2\2\2g"+
		"\u01ae\3\2\2\2i\u01b0\3\2\2\2k\u01b2\3\2\2\2m\u01b4\3\2\2\2o\u01b6\3\2"+
		"\2\2q\u01b8\3\2\2\2s\u01ba\3\2\2\2u\u01bc\3\2\2\2w\u01be\3\2\2\2y\u01c0"+
		"\3\2\2\2{\u01c2\3\2\2\2}\u01c4\3\2\2\2\177\u01c6\3\2\2\2\u0081\u01c8\3"+
		"\2\2\2\u0083\u01ca\3\2\2\2\u0085\u01cc\3\2\2\2\u0087\u01ce\3\2\2\2\u0089"+
		"\u01d0\3\2\2\2\u008b\u01d2\3\2\2\2\u008d\u01d4\3\2\2\2\u008f\u01d6\3\2"+
		"\2\2\u0091\u01d8\3\2\2\2\u0093\u01da\3\2\2\2\u0095\u01dc\3\2\2\2\u0097"+
		"\u01de\3\2\2\2\u0099\u01e0\3\2\2\2\u009b\u01e2\3\2\2\2\u009d\u01e4\3\2"+
		"\2\2\u009f\u01e6\3\2\2\2\u00a1\u01e8\3\2\2\2\u00a3\u01ea\3\2\2\2\u00a5"+
		"\u01ec\3\2\2\2\u00a7\u01ee\3\2\2\2\u00a9\u01f0\3\2\2\2\u00ab\u01f2\3\2"+
		"\2\2\u00ad\u01f4\3\2\2\2\u00af\u01f6\3\2\2\2\u00b1\u01f8\3\2\2\2\u00b3"+
		"\u01fa\3\2\2\2\u00b5\u01fc\3\2\2\2\u00b7\u01fe\3\2\2\2\u00b9\u0200\3\2"+
		"\2\2\u00bb\u00bc\7X\2\2\u00bc\u00bd\7c\2\2\u00bd\u00be\7n\2\2\u00be\u00bf"+
		"\7w\2\2\u00bf\u00c0\7g\2\2\u00c0\u00c1\7\"\2\2\u00c1\u00c2\7u\2\2\u00c2"+
		"\u00c3\7g\2\2\u00c3\u00c4\7v\2\2\u00c4\4\3\2\2\2\u00c5\u00c6\7o\2\2\u00c6"+
		"\u00c7\7k\2\2\u00c7\u00c8\7p\2\2\u00c8\6\3\2\2\2\u00c9\u00ca\7o\2\2\u00ca"+
		"\u00cb\7c\2\2\u00cb\u00cc\7z\2\2\u00cc\b\3\2\2\2\u00cd\u00ce\7g\2\2\u00ce"+
		"\u00cf\7z\2\2\u00cf\u00d0\7c\2\2\u00d0\u00d1\7e\2\2\u00d1\u00d2\7v\2\2"+
		"\u00d2\u00d3\7n\2\2\u00d3\u00d4\7{\2\2\u00d4\n\3\2\2\2\u00d5\u00d6\7c"+
		"\2\2\u00d6\u00d7\7v\2\2\u00d7\u00d8\7\"\2\2\u00d8\u00d9\7n\2\2\u00d9\u00da"+
		"\7g\2\2\u00da\u00db\7c\2\2\u00db\u00dc\7u\2\2\u00dc\u00dd\7v\2\2\u00dd"+
		"\u00de\7\"\2\2\u00de\u00df\7q\2\2\u00df\u00e0\7p\2\2\u00e0\u00e1\7g\2"+
		"\2\u00e1\f\3\2\2\2\u00e2\u00e3\7q\2\2\u00e3\u00e4\7p\2\2\u00e4\u00e5\7"+
		"n\2\2\u00e5\u00e6\7{\2\2\u00e6\16\3\2\2\2\u00e7\u00e8\7c\2\2\u00e8\u00e9"+
		"\7p\2\2\u00e9\u00ea\7{\2\2\u00ea\u00eb\7\"\2\2\u00eb\u00ec\7q\2\2\u00ec"+
		"\u00ed\7h\2\2\u00ed\u00ee\7\61\2\2\u00ee\u00ef\7q\2\2\u00ef\u00f0\7t\2"+
		"\2\u00f0\20\3\2\2\2\u00f1\u00f2\7R\2\2\u00f2\u00f3\7t\2\2\u00f3\u00f4"+
		"\7q\2\2\u00f4\u00f5\7r\2\2\u00f5\u00f6\7g\2\2\u00f6\u00f7\7t\2\2\u00f7"+
		"\u00f8\7v\2\2\u00f8\u00f9\7{\2\2\u00f9\22\3\2\2\2\u00fa\u00fb\7R\2\2\u00fb"+
		"\u00fc\7t\2\2\u00fc\u00fd\7q\2\2\u00fd\u00fe\7r\2\2\u00fe\u00ff\7g\2\2"+
		"\u00ff\u0100\7t\2\2\u0100\u0101\7v\2\2\u0101\u0102\7{\2\2\u0102\u0103"+
		"\7\"\2\2\u0103\u0104\7e\2\2\u0104\u0105\7j\2\2\u0105\u0106\7c\2\2\u0106"+
		"\u0107\7k\2\2\u0107\u0108\7p\2\2\u0108\24\3\2\2\2\u0109\u010a\7U\2\2\u010a"+
		"\u010b\7w\2\2\u010b\u010c\7d\2\2\u010c\u010d\7r\2\2\u010d\u010e\7t\2\2"+
		"\u010e\u010f\7q\2\2\u010f\u0110\7r\2\2\u0110\u0111\7g\2\2\u0111\u0112"+
		"\7t\2\2\u0112\u0113\7v\2\2\u0113\u0114\7{\2\2\u0114\u0115\7\"\2\2\u0115"+
		"\u0116\7q\2\2\u0116\u0117\7h\2\2\u0117\26\3\2\2\2\u0118\u0119\7G\2\2\u0119"+
		"\u011a\7p\2\2\u011a\u011b\7v\2\2\u011b\u011c\7k\2\2\u011c\u011d\7v\2\2"+
		"\u011d\u011e\7{\2\2\u011e\u011f\7\"\2\2\u011f\u0120\7v\2\2\u0120\u0121"+
		"\7{\2\2\u0121\u0122\7r\2\2\u0122\u0123\7g\2\2\u0123\30\3\2\2\2\u0124\u0125"+
		"\7Q\2\2\u0125\u0126\7d\2\2\u0126\u0127\7l\2\2\u0127\u0128\7g\2\2\u0128"+
		"\u0129\7e\2\2\u0129\u012a\7v\2\2\u012a\u012b\7\"\2\2\u012b\u012c\7r\2"+
		"\2\u012c\u012d\7t\2\2\u012d\u012e\7q\2\2\u012e\u012f\7r\2\2\u012f\u0130"+
		"\7g\2\2\u0130\u0131\7t\2\2\u0131\u0132\7v\2\2\u0132\u0133\7{\2\2\u0133"+
		"\32\3\2\2\2\u0134\u0135\7U\2\2\u0135\u0136\7w\2\2\u0136\u0137\7d\2\2\u0137"+
		"\u0138\7e\2\2\u0138\u0139\7n\2\2\u0139\u013a\7c\2\2\u013a\u013b\7u\2\2"+
		"\u013b\u013c\7u\2\2\u013c\u013d\7\"\2\2\u013d\u013e\7q\2\2\u013e\u013f"+
		"\7h\2\2\u013f\34\3\2\2\2\u0140\u0141\7G\2\2\u0141\u0142\7s\2\2\u0142\u0143"+
		"\7w\2\2\u0143\u0144\7k\2\2\u0144\u0145\7x\2\2\u0145\u0146\7c\2\2\u0146"+
		"\u0147\7n\2\2\u0147\u0148\7g\2\2\u0148\u0149\7p\2\2\u0149\u014a\7v\2\2"+
		"\u014a\u014b\7\"\2\2\u014b\u014c\7v\2\2\u014c\u014d\7q\2\2\u014d\36\3"+
		"\2\2\2\u014e\u014f\7E\2\2\u014f\u0150\7n\2\2\u0150\u0151\7c\2\2\u0151"+
		"\u0152\7u\2\2\u0152\u0153\7u\2\2\u0153\u0154\7\"\2\2\u0154\u0155\7g\2"+
		"\2\u0155\u0156\7p\2\2\u0156\u0157\7v\2\2\u0157\u0158\7k\2\2\u0158\u0159"+
		"\7v\2\2\u0159\u015a\7{\2\2\u015a \3\2\2\2\u015b\u015c\7C\2\2\u015c\u015d"+
		"\7z\2\2\u015d\u015e\7k\2\2\u015e\u015f\7q\2\2\u015f\u0160\7o\2\2\u0160"+
		"\u0161\7\"\2\2\u0161\u0162\7v\2\2\u0162\u0163\7{\2\2\u0163\u0164\7r\2"+
		"\2\u0164\u0165\7g\2\2\u0165\"\3\2\2\2\u0166\u0167\7E\2\2\u0167\u0168\7"+
		"n\2\2\u0168\u0169\7c\2\2\u0169\u016a\7u\2\2\u016a\u016b\7u\2\2\u016b$"+
		"\3\2\2\2\u016c\u016d\t\2\2\2\u016d&\3\2\2\2\u016e\u016f\t\3\2\2\u016f"+
		"(\3\2\2\2\u0170\u0171\t\4\2\2\u0171*\3\2\2\2\u0172\u0173\t\5\2\2\u0173"+
		",\3\2\2\2\u0174\u0175\t\6\2\2\u0175.\3\2\2\2\u0176\u0177\t\7\2\2\u0177"+
		"\60\3\2\2\2\u0178\u0179\7\"\2\2\u0179\62\3\2\2\2\u017a\u017b\7#\2\2\u017b"+
		"\64\3\2\2\2\u017c\u017d\7$\2\2\u017d\66\3\2\2\2\u017e\u017f\7%\2\2\u017f"+
		"8\3\2\2\2\u0180\u0181\7&\2\2\u0181:\3\2\2\2\u0182\u0183\7\'\2\2\u0183"+
		"<\3\2\2\2\u0184\u0185\7(\2\2\u0185>\3\2\2\2\u0186\u0187\7)\2\2\u0187@"+
		"\3\2\2\2\u0188\u0189\7*\2\2\u0189B\3\2\2\2\u018a\u018b\7+\2\2\u018bD\3"+
		"\2\2\2\u018c\u018d\7,\2\2\u018dF\3\2\2\2\u018e\u018f\7-\2\2\u018fH\3\2"+
		"\2\2\u0190\u0191\7.\2\2\u0191J\3\2\2\2\u0192\u0193\7/\2\2\u0193L\3\2\2"+
		"\2\u0194\u0195\7\60\2\2\u0195N\3\2\2\2\u0196\u0197\7\61\2\2\u0197P\3\2"+
		"\2\2\u0198\u0199\7\62\2\2\u0199R\3\2\2\2\u019a\u019b\7\63\2\2\u019bT\3"+
		"\2\2\2\u019c\u019d\7\64\2\2\u019dV\3\2\2\2\u019e\u019f\7\65\2\2\u019f"+
		"X\3\2\2\2\u01a0\u01a1\7\66\2\2\u01a1Z\3\2\2\2\u01a2\u01a3\7\67\2\2\u01a3"+
		"\\\3\2\2\2\u01a4\u01a5\78\2\2\u01a5^\3\2\2\2\u01a6\u01a7\79\2\2\u01a7"+
		"`\3\2\2\2\u01a8\u01a9\7:\2\2\u01a9b\3\2\2\2\u01aa\u01ab\7;\2\2\u01abd"+
		"\3\2\2\2\u01ac\u01ad\7<\2\2\u01adf\3\2\2\2\u01ae\u01af\7=\2\2\u01afh\3"+
		"\2\2\2\u01b0\u01b1\7>\2\2\u01b1j\3\2\2\2\u01b2\u01b3\7?\2\2\u01b3l\3\2"+
		"\2\2\u01b4\u01b5\7@\2\2\u01b5n\3\2\2\2\u01b6\u01b7\7A\2\2\u01b7p\3\2\2"+
		"\2\u01b8\u01b9\7B\2\2\u01b9r\3\2\2\2\u01ba\u01bb\7]\2\2\u01bbt\3\2\2\2"+
		"\u01bc\u01bd\7^\2\2\u01bdv\3\2\2\2\u01be\u01bf\7_\2\2\u01bfx\3\2\2\2\u01c0"+
		"\u01c1\7`\2\2\u01c1z\3\2\2\2\u01c2\u01c3\7a\2\2\u01c3|\3\2\2\2\u01c4\u01c5"+
		"\7b\2\2\u01c5~\3\2\2\2\u01c6\u01c7\7c\2\2\u01c7\u0080\3\2\2\2\u01c8\u01c9"+
		"\7d\2\2\u01c9\u0082\3\2\2\2\u01ca\u01cb\7e\2\2\u01cb\u0084\3\2\2\2\u01cc"+
		"\u01cd\7f\2\2\u01cd\u0086\3\2\2\2\u01ce\u01cf\7g\2\2\u01cf\u0088\3\2\2"+
		"\2\u01d0\u01d1\7h\2\2\u01d1\u008a\3\2\2\2\u01d2\u01d3\7i\2\2\u01d3\u008c"+
		"\3\2\2\2\u01d4\u01d5\7j\2\2\u01d5\u008e\3\2\2\2\u01d6\u01d7\7k\2\2\u01d7"+
		"\u0090\3\2\2\2\u01d8\u01d9\7l\2\2\u01d9\u0092\3\2\2\2\u01da\u01db\7m\2"+
		"\2\u01db\u0094\3\2\2\2\u01dc\u01dd\7n\2\2\u01dd\u0096\3\2\2\2\u01de\u01df"+
		"\7o\2\2\u01df\u0098\3\2\2\2\u01e0\u01e1\7p\2\2\u01e1\u009a\3\2\2\2\u01e2"+
		"\u01e3\7q\2\2\u01e3\u009c\3\2\2\2\u01e4\u01e5\7r\2\2\u01e5\u009e\3\2\2"+
		"\2\u01e6\u01e7\7s\2\2\u01e7\u00a0\3\2\2\2\u01e8\u01e9\7t\2\2\u01e9\u00a2"+
		"\3\2\2\2\u01ea\u01eb\7u\2\2\u01eb\u00a4\3\2\2\2\u01ec\u01ed\7v\2\2\u01ed"+
		"\u00a6\3\2\2\2\u01ee\u01ef\7w\2\2\u01ef\u00a8\3\2\2\2\u01f0\u01f1\7x\2"+
		"\2\u01f1\u00aa\3\2\2\2\u01f2\u01f3\7y\2\2\u01f3\u00ac\3\2\2\2\u01f4\u01f5"+
		"\7z\2\2\u01f5\u00ae\3\2\2\2\u01f6\u01f7\7{\2\2\u01f7\u00b0\3\2\2\2\u01f8"+
		"\u01f9\7|\2\2\u01f9\u00b2\3\2\2\2\u01fa\u01fb\7}\2\2\u01fb\u00b4\3\2\2"+
		"\2\u01fc\u01fd\7~\2\2\u01fd\u00b6\3\2\2\2\u01fe\u01ff\7\177\2\2\u01ff"+
		"\u00b8\3\2\2\2\u0200\u0201\7\u0080\2\2\u0201\u00ba\3\2\2\2\3\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}