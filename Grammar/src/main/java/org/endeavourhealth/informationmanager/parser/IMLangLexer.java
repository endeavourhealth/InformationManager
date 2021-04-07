// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\IMLang.g4 by ANTLR 4.9.1
package org.endeavourhealth.informationmanager.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class IMLangLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, EQ=8, MEMBERS=9, 
		STATUS=10, VERSION=11, PROPERTIES=12, TYPE=13, MIN=14, MAX=15, SOME=16, 
		ONLY=17, MININCLUSIVE=18, MAXINCLUSIVE=19, MINEXCLUSIVE=20, MAXEXCLUSIVE=21, 
		SUBCLASS=22, EQUIVALENTTO=23, DISJOINT=24, SUBPROPERTY=25, INVERSE=26, 
		TARGETCLASS=27, EXACTLY=28, AND=29, INTEGER=30, DOUBLE=31, DIGIT=32, OR=33, 
		NOT=34, NAME=35, DESCRIPTION=36, CODE=37, SCHEME=38, PREFIXIRI=39, IRIREF=40, 
		FULLIRI=41, LOWERCASE=42, UPPERCASE=43, QUOTED_STRING=44, PN_LOCAL_ESC=45, 
		WS=46;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "A", "B", "C", 
			"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", 
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z", "EQ", "MEMBERS", "STATUS", 
			"VERSION", "PROPERTIES", "TYPE", "MIN", "MAX", "SOME", "ONLY", "MININCLUSIVE", 
			"MAXINCLUSIVE", "MINEXCLUSIVE", "MAXEXCLUSIVE", "SUBCLASS", "EQUIVALENTTO", 
			"DISJOINT", "SUBPROPERTY", "INVERSE", "TARGETCLASS", "EXACTLY", "AND", 
			"INTEGER", "DOUBLE", "DIGIT", "OR", "NOT", "NAME", "DESCRIPTION", "CODE", 
			"SCHEME", "PREFIXIRI", "IRIREF", "FULLIRI", "LOWERCASE", "UPPERCASE", 
			"QUOTED_STRING", "PN_LOCAL_ESC", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "';'", "'['", "','", "']'", "'('", "')'", "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "EQ", "MEMBERS", "STATUS", 
			"VERSION", "PROPERTIES", "TYPE", "MIN", "MAX", "SOME", "ONLY", "MININCLUSIVE", 
			"MAXINCLUSIVE", "MINEXCLUSIVE", "MAXEXCLUSIVE", "SUBCLASS", "EQUIVALENTTO", 
			"DISJOINT", "SUBPROPERTY", "INVERSE", "TARGETCLASS", "EXACTLY", "AND", 
			"INTEGER", "DOUBLE", "DIGIT", "OR", "NOT", "NAME", "DESCRIPTION", "CODE", 
			"SCHEME", "PREFIXIRI", "IRIREF", "FULLIRI", "LOWERCASE", "UPPERCASE", 
			"QUOTED_STRING", "PN_LOCAL_ESC", "WS"
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


	public IMLangLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "IMLang.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\60\u0215\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4"+
		"I\tI\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3"+
		"\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3"+
		"\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3"+
		"\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3"+
		"\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3"+
		"%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3"+
		"\'\3(\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3+\3+\3+\3+\3+\3,\3,\3,\3,\3"+
		",\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\3-\5-\u0120\n-\3.\3.\3.\3"+
		".\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\3.\5.\u0131\n.\3/\3/\3/\3/\3/\3/\3/\3"+
		"/\3/\3/\3/\3/\3/\3/\5/\u0141\n/\3\60\3\60\3\60\3\60\3\60\3\60\3\60\3\60"+
		"\3\60\3\60\3\60\3\60\3\60\3\60\5\60\u0151\n\60\3\61\3\61\3\61\3\61\3\61"+
		"\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62"+
		"\3\62\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64\3\64"+
		"\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65\3\65"+
		"\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\66\3\67\3\67"+
		"\3\67\3\67\3\67\3\67\3\67\3\67\38\38\38\38\39\69\u01a9\n9\r9\169\u01aa"+
		"\3:\6:\u01ae\n:\r:\16:\u01af\3:\3:\6:\u01b4\n:\r:\16:\u01b5\5:\u01b8\n"+
		":\3;\3;\3<\3<\3<\3=\3=\3=\3=\3>\3>\3>\3>\3>\3?\3?\3?\3?\3?\3?\3?\3?\3"+
		"?\3?\3?\3?\3@\3@\3@\3@\3@\3A\3A\3A\3A\3A\3A\3A\3B\7B\u01e1\nB\fB\16B\u01e4"+
		"\13B\3B\3B\3B\3B\3B\6B\u01eb\nB\rB\16B\u01ec\3C\3C\3C\3C\3C\6C\u01f4\n"+
		"C\rC\16C\u01f5\3D\3D\3D\3D\3E\3E\3F\3F\3G\3G\3G\3G\3G\7G\u0205\nG\fG\16"+
		"G\u0208\13G\3G\3G\3H\3H\3H\3I\6I\u0210\nI\rI\16I\u0211\3I\3I\2\2J\3\3"+
		"\5\4\7\5\t\6\13\7\r\b\17\t\21\2\23\2\25\2\27\2\31\2\33\2\35\2\37\2!\2"+
		"#\2%\2\'\2)\2+\2-\2/\2\61\2\63\2\65\2\67\29\2;\2=\2?\2A\2C\2E\nG\13I\f"+
		"K\rM\16O\17Q\20S\21U\22W\23Y\24[\25]\26_\27a\30c\31e\32g\33i\34k\35m\36"+
		"o\37q s!u\"w#y${%}&\177\'\u0081(\u0083)\u0085*\u0087+\u0089,\u008b-\u008d"+
		".\u008f/\u0091\60\3\2\"\4\2CCcc\4\2DDdd\4\2EEee\4\2FFff\4\2GGgg\4\2HH"+
		"hh\4\2IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4\2OOoo\4\2PPpp\4\2"+
		"QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4\2XXxx\4\2YYyy\4"+
		"\2ZZzz\4\2[[{{\4\2\\\\||\3\2\62;\3\2c|\3\2C\\\5\2\"\".\60aa\t\2##%\61"+
		"==??ABaa\u0080\u0080\5\2\13\f\17\17\"\"\2\u0210\2\3\3\2\2\2\2\5\3\2\2"+
		"\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2E\3"+
		"\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2"+
		"\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2"+
		"_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3"+
		"\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2"+
		"\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083"+
		"\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b\3\2\2"+
		"\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\3\u0093\3\2\2\2\5\u0095"+
		"\3\2\2\2\7\u0097\3\2\2\2\t\u0099\3\2\2\2\13\u009b\3\2\2\2\r\u009d\3\2"+
		"\2\2\17\u009f\3\2\2\2\21\u00a1\3\2\2\2\23\u00a3\3\2\2\2\25\u00a5\3\2\2"+
		"\2\27\u00a7\3\2\2\2\31\u00a9\3\2\2\2\33\u00ab\3\2\2\2\35\u00ad\3\2\2\2"+
		"\37\u00af\3\2\2\2!\u00b1\3\2\2\2#\u00b3\3\2\2\2%\u00b5\3\2\2\2\'\u00b7"+
		"\3\2\2\2)\u00b9\3\2\2\2+\u00bb\3\2\2\2-\u00bd\3\2\2\2/\u00bf\3\2\2\2\61"+
		"\u00c1\3\2\2\2\63\u00c3\3\2\2\2\65\u00c5\3\2\2\2\67\u00c7\3\2\2\29\u00c9"+
		"\3\2\2\2;\u00cb\3\2\2\2=\u00cd\3\2\2\2?\u00cf\3\2\2\2A\u00d1\3\2\2\2C"+
		"\u00d3\3\2\2\2E\u00d5\3\2\2\2G\u00d7\3\2\2\2I\u00df\3\2\2\2K\u00e6\3\2"+
		"\2\2M\u00ee\3\2\2\2O\u00f9\3\2\2\2Q\u00fe\3\2\2\2S\u0102\3\2\2\2U\u0106"+
		"\3\2\2\2W\u010b\3\2\2\2Y\u011f\3\2\2\2[\u0130\3\2\2\2]\u0140\3\2\2\2_"+
		"\u0150\3\2\2\2a\u0152\3\2\2\2c\u015d\3\2\2\2e\u016a\3\2\2\2g\u0177\3\2"+
		"\2\2i\u0185\3\2\2\2k\u018f\3\2\2\2m\u019b\3\2\2\2o\u01a3\3\2\2\2q\u01a8"+
		"\3\2\2\2s\u01ad\3\2\2\2u\u01b9\3\2\2\2w\u01bb\3\2\2\2y\u01be\3\2\2\2{"+
		"\u01c2\3\2\2\2}\u01c7\3\2\2\2\177\u01d3\3\2\2\2\u0081\u01d8\3\2\2\2\u0083"+
		"\u01e2\3\2\2\2\u0085\u01f3\3\2\2\2\u0087\u01f7\3\2\2\2\u0089\u01fb\3\2"+
		"\2\2\u008b\u01fd\3\2\2\2\u008d\u01ff\3\2\2\2\u008f\u020b\3\2\2\2\u0091"+
		"\u020f\3\2\2\2\u0093\u0094\7\60\2\2\u0094\4\3\2\2\2\u0095\u0096\7=\2\2"+
		"\u0096\6\3\2\2\2\u0097\u0098\7]\2\2\u0098\b\3\2\2\2\u0099\u009a\7.\2\2"+
		"\u009a\n\3\2\2\2\u009b\u009c\7_\2\2\u009c\f\3\2\2\2\u009d\u009e\7*\2\2"+
		"\u009e\16\3\2\2\2\u009f\u00a0\7+\2\2\u00a0\20\3\2\2\2\u00a1\u00a2\t\2"+
		"\2\2\u00a2\22\3\2\2\2\u00a3\u00a4\t\3\2\2\u00a4\24\3\2\2\2\u00a5\u00a6"+
		"\t\4\2\2\u00a6\26\3\2\2\2\u00a7\u00a8\t\5\2\2\u00a8\30\3\2\2\2\u00a9\u00aa"+
		"\t\6\2\2\u00aa\32\3\2\2\2\u00ab\u00ac\t\7\2\2\u00ac\34\3\2\2\2\u00ad\u00ae"+
		"\t\b\2\2\u00ae\36\3\2\2\2\u00af\u00b0\t\t\2\2\u00b0 \3\2\2\2\u00b1\u00b2"+
		"\t\n\2\2\u00b2\"\3\2\2\2\u00b3\u00b4\t\13\2\2\u00b4$\3\2\2\2\u00b5\u00b6"+
		"\t\f\2\2\u00b6&\3\2\2\2\u00b7\u00b8\t\r\2\2\u00b8(\3\2\2\2\u00b9\u00ba"+
		"\t\16\2\2\u00ba*\3\2\2\2\u00bb\u00bc\t\17\2\2\u00bc,\3\2\2\2\u00bd\u00be"+
		"\t\20\2\2\u00be.\3\2\2\2\u00bf\u00c0\t\21\2\2\u00c0\60\3\2\2\2\u00c1\u00c2"+
		"\t\22\2\2\u00c2\62\3\2\2\2\u00c3\u00c4\t\23\2\2\u00c4\64\3\2\2\2\u00c5"+
		"\u00c6\t\24\2\2\u00c6\66\3\2\2\2\u00c7\u00c8\t\25\2\2\u00c88\3\2\2\2\u00c9"+
		"\u00ca\t\26\2\2\u00ca:\3\2\2\2\u00cb\u00cc\t\27\2\2\u00cc<\3\2\2\2\u00cd"+
		"\u00ce\t\30\2\2\u00ce>\3\2\2\2\u00cf\u00d0\t\31\2\2\u00d0@\3\2\2\2\u00d1"+
		"\u00d2\t\32\2\2\u00d2B\3\2\2\2\u00d3\u00d4\t\33\2\2\u00d4D\3\2\2\2\u00d5"+
		"\u00d6\7?\2\2\u00d6F\3\2\2\2\u00d7\u00d8\5)\25\2\u00d8\u00d9\5\31\r\2"+
		"\u00d9\u00da\5)\25\2\u00da\u00db\5\23\n\2\u00db\u00dc\5\31\r\2\u00dc\u00dd"+
		"\5\63\32\2\u00dd\u00de\5\65\33\2\u00deH\3\2\2\2\u00df\u00e0\5\65\33\2"+
		"\u00e0\u00e1\5\67\34\2\u00e1\u00e2\5\21\t\2\u00e2\u00e3\5\67\34\2\u00e3"+
		"\u00e4\59\35\2\u00e4\u00e5\5\65\33\2\u00e5J\3\2\2\2\u00e6\u00e7\5;\36"+
		"\2\u00e7\u00e8\5\31\r\2\u00e8\u00e9\5\63\32\2\u00e9\u00ea\5\65\33\2\u00ea"+
		"\u00eb\5!\21\2\u00eb\u00ec\5-\27\2\u00ec\u00ed\5+\26\2\u00edL\3\2\2\2"+
		"\u00ee\u00ef\5/\30\2\u00ef\u00f0\5\63\32\2\u00f0\u00f1\5-\27\2\u00f1\u00f2"+
		"\5/\30\2\u00f2\u00f3\5\31\r\2\u00f3\u00f4\5\63\32\2\u00f4\u00f5\5\67\34"+
		"\2\u00f5\u00f6\5!\21\2\u00f6\u00f7\5\31\r\2\u00f7\u00f8\5\65\33\2\u00f8"+
		"N\3\2\2\2\u00f9\u00fa\5\67\34\2\u00fa\u00fb\5A!\2\u00fb\u00fc\5/\30\2"+
		"\u00fc\u00fd\5\31\r\2\u00fdP\3\2\2\2\u00fe\u00ff\5)\25\2\u00ff\u0100\5"+
		"!\21\2\u0100\u0101\5+\26\2\u0101R\3\2\2\2\u0102\u0103\5)\25\2\u0103\u0104"+
		"\5\21\t\2\u0104\u0105\5? \2\u0105T\3\2\2\2\u0106\u0107\5\65\33\2\u0107"+
		"\u0108\5-\27\2\u0108\u0109\5)\25\2\u0109\u010a\5\31\r\2\u010aV\3\2\2\2"+
		"\u010b\u010c\5-\27\2\u010c\u010d\5+\26\2\u010d\u010e\5\'\24\2\u010e\u010f"+
		"\5A!\2\u010fX\3\2\2\2\u0110\u0111\5)\25\2\u0111\u0112\5!\21\2\u0112\u0113"+
		"\5+\26\2\u0113\u0114\5!\21\2\u0114\u0115\5+\26\2\u0115\u0116\5\25\13\2"+
		"\u0116\u0117\5\'\24\2\u0117\u0118\59\35\2\u0118\u0119\5\65\33\2\u0119"+
		"\u011a\5!\21\2\u011a\u011b\5;\36\2\u011b\u011c\5\31\r\2\u011c\u0120\3"+
		"\2\2\2\u011d\u011e\7@\2\2\u011e\u0120\7?\2\2\u011f\u0110\3\2\2\2\u011f"+
		"\u011d\3\2\2\2\u0120Z\3\2\2\2\u0121\u0122\5)\25\2\u0122\u0123\5\21\t\2"+
		"\u0123\u0124\5? \2\u0124\u0125\5!\21\2\u0125\u0126\5+\26\2\u0126\u0127"+
		"\5\25\13\2\u0127\u0128\5\'\24\2\u0128\u0129\59\35\2\u0129\u012a\5\65\33"+
		"\2\u012a\u012b\5!\21\2\u012b\u012c\5;\36\2\u012c\u012d\5\31\r\2\u012d"+
		"\u0131\3\2\2\2\u012e\u012f\7>\2\2\u012f\u0131\7?\2\2\u0130\u0121\3\2\2"+
		"\2\u0130\u012e\3\2\2\2\u0131\\\3\2\2\2\u0132\u0133\5)\25\2\u0133\u0134"+
		"\5!\21\2\u0134\u0135\5+\26\2\u0135\u0136\5\31\r\2\u0136\u0137\5? \2\u0137"+
		"\u0138\5\25\13\2\u0138\u0139\5\'\24\2\u0139\u013a\59\35\2\u013a\u013b"+
		"\5\65\33\2\u013b\u013c\5!\21\2\u013c\u013d\5;\36\2\u013d\u013e\5\31\r"+
		"\2\u013e\u0141\3\2\2\2\u013f\u0141\7@\2\2\u0140\u0132\3\2\2\2\u0140\u013f"+
		"\3\2\2\2\u0141^\3\2\2\2\u0142\u0143\5)\25\2\u0143\u0144\5\21\t\2\u0144"+
		"\u0145\5? \2\u0145\u0146\5\31\r\2\u0146\u0147\5? \2\u0147\u0148\5\25\13"+
		"\2\u0148\u0149\5\'\24\2\u0149\u014a\59\35\2\u014a\u014b\5\65\33\2\u014b"+
		"\u014c\5!\21\2\u014c\u014d\5;\36\2\u014d\u014e\5\31\r\2\u014e\u0151\3"+
		"\2\2\2\u014f\u0151\7>\2\2\u0150\u0142\3\2\2\2\u0150\u014f\3\2\2\2\u0151"+
		"`\3\2\2\2\u0152\u0153\5\65\33\2\u0153\u0154\59\35\2\u0154\u0155\5\23\n"+
		"\2\u0155\u0156\5\25\13\2\u0156\u0157\5\'\24\2\u0157\u0158\5\21\t\2\u0158"+
		"\u0159\5\65\33\2\u0159\u015a\5\65\33\2\u015a\u015b\5-\27\2\u015b\u015c"+
		"\5\33\16\2\u015cb\3\2\2\2\u015d\u015e\5\31\r\2\u015e\u015f\5\61\31\2\u015f"+
		"\u0160\59\35\2\u0160\u0161\5!\21\2\u0161\u0162\5;\36\2\u0162\u0163\5\21"+
		"\t\2\u0163\u0164\5\'\24\2\u0164\u0165\5\31\r\2\u0165\u0166\5+\26\2\u0166"+
		"\u0167\5\67\34\2\u0167\u0168\5\67\34\2\u0168\u0169\5-\27\2\u0169d\3\2"+
		"\2\2\u016a\u016b\5\27\f\2\u016b\u016c\5!\21\2\u016c\u016d\5\65\33\2\u016d"+
		"\u016e\5#\22\2\u016e\u016f\5-\27\2\u016f\u0170\5!\21\2\u0170\u0171\5+"+
		"\26\2\u0171\u0172\5\67\34\2\u0172\u0173\5=\37\2\u0173\u0174\5!\21\2\u0174"+
		"\u0175\5\67\34\2\u0175\u0176\5\37\20\2\u0176f\3\2\2\2\u0177\u0178\5\65"+
		"\33\2\u0178\u0179\59\35\2\u0179\u017a\5\23\n\2\u017a\u017b\5/\30\2\u017b"+
		"\u017c\5\63\32\2\u017c\u017d\5-\27\2\u017d\u017e\5/\30\2\u017e\u017f\5"+
		"\31\r\2\u017f\u0180\5\63\32\2\u0180\u0181\5\67\34\2\u0181\u0182\5A!\2"+
		"\u0182\u0183\5-\27\2\u0183\u0184\5\33\16\2\u0184h\3\2\2\2\u0185\u0186"+
		"\5!\21\2\u0186\u0187\5+\26\2\u0187\u0188\5;\36\2\u0188\u0189\5\31\r\2"+
		"\u0189\u018a\5\63\32\2\u018a\u018b\5\65\33\2\u018b\u018c\5\31\r\2\u018c"+
		"\u018d\5-\27\2\u018d\u018e\5\33\16\2\u018ej\3\2\2\2\u018f\u0190\5\67\34"+
		"\2\u0190\u0191\5\21\t\2\u0191\u0192\5\63\32\2\u0192\u0193\5\35\17\2\u0193"+
		"\u0194\5\31\r\2\u0194\u0195\5\67\34\2\u0195\u0196\5\25\13\2\u0196\u0197"+
		"\5\'\24\2\u0197\u0198\5\21\t\2\u0198\u0199\5\65\33\2\u0199\u019a\5\65"+
		"\33\2\u019al\3\2\2\2\u019b\u019c\5\31\r\2\u019c\u019d\5? \2\u019d\u019e"+
		"\5\21\t\2\u019e\u019f\5\25\13\2\u019f\u01a0\5\67\34\2\u01a0\u01a1\5\'"+
		"\24\2\u01a1\u01a2\5A!\2\u01a2n\3\2\2\2\u01a3\u01a4\5\21\t\2\u01a4\u01a5"+
		"\5+\26\2\u01a5\u01a6\5\27\f\2\u01a6p\3\2\2\2\u01a7\u01a9\5u;\2\u01a8\u01a7"+
		"\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01a8\3\2\2\2\u01aa\u01ab\3\2\2\2\u01ab"+
		"r\3\2\2\2\u01ac\u01ae\5u;\2\u01ad\u01ac\3\2\2\2\u01ae\u01af\3\2\2\2\u01af"+
		"\u01ad\3\2\2\2\u01af\u01b0\3\2\2\2\u01b0\u01b7\3\2\2\2\u01b1\u01b3\7\60"+
		"\2\2\u01b2\u01b4\5u;\2\u01b3\u01b2\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b3"+
		"\3\2\2\2\u01b5\u01b6\3\2\2\2\u01b6\u01b8\3\2\2\2\u01b7\u01b1\3\2\2\2\u01b7"+
		"\u01b8\3\2\2\2\u01b8t\3\2\2\2\u01b9\u01ba\t\34\2\2\u01bav\3\2\2\2\u01bb"+
		"\u01bc\5-\27\2\u01bc\u01bd\5\63\32\2\u01bdx\3\2\2\2\u01be\u01bf\5+\26"+
		"\2\u01bf\u01c0\5-\27\2\u01c0\u01c1\5\67\34\2\u01c1z\3\2\2\2\u01c2\u01c3"+
		"\5+\26\2\u01c3\u01c4\5\21\t\2\u01c4\u01c5\5)\25\2\u01c5\u01c6\5\31\r\2"+
		"\u01c6|\3\2\2\2\u01c7\u01c8\5\27\f\2\u01c8\u01c9\5\31\r\2\u01c9\u01ca"+
		"\5\65\33\2\u01ca\u01cb\5\25\13\2\u01cb\u01cc\5\63\32\2\u01cc\u01cd\5!"+
		"\21\2\u01cd\u01ce\5/\30\2\u01ce\u01cf\5\67\34\2\u01cf\u01d0\5!\21\2\u01d0"+
		"\u01d1\5-\27\2\u01d1\u01d2\5+\26\2\u01d2~\3\2\2\2\u01d3\u01d4\5\25\13"+
		"\2\u01d4\u01d5\5-\27\2\u01d5\u01d6\5\27\f\2\u01d6\u01d7\5\31\r\2\u01d7"+
		"\u0080\3\2\2\2\u01d8\u01d9\5\65\33\2\u01d9\u01da\5\25\13\2\u01da\u01db"+
		"\5\37\20\2\u01db\u01dc\5\31\r\2\u01dc\u01dd\5)\25\2\u01dd\u01de\5\31\r"+
		"\2\u01de\u0082\3\2\2\2\u01df\u01e1\5\u0089E\2\u01e0\u01df\3\2\2\2\u01e1"+
		"\u01e4\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3\u01e5\3\2"+
		"\2\2\u01e4\u01e2\3\2\2\2\u01e5\u01e6\7<\2\2\u01e6\u01ea\3\2\2\2\u01e7"+
		"\u01eb\5\u008bF\2\u01e8\u01eb\5\u0089E\2\u01e9\u01eb\5q9\2\u01ea\u01e7"+
		"\3\2\2\2\u01ea\u01e8\3\2\2\2\u01ea\u01e9\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec"+
		"\u01ea\3\2\2\2\u01ec\u01ed\3\2\2\2\u01ed\u0084\3\2\2\2\u01ee\u01f4\5\u0089"+
		"E\2\u01ef\u01f4\5\u008bF\2\u01f0\u01f4\5\u0089E\2\u01f1\u01f4\5q9\2\u01f2"+
		"\u01f4\5\u008fH\2\u01f3\u01ee\3\2\2\2\u01f3\u01ef\3\2\2\2\u01f3\u01f0"+
		"\3\2\2\2\u01f3\u01f1\3\2\2\2\u01f3\u01f2\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5"+
		"\u01f3\3\2\2\2\u01f5\u01f6\3\2\2\2\u01f6\u0086\3\2\2\2\u01f7\u01f8\7>"+
		"\2\2\u01f8\u01f9\5\u0085C\2\u01f9\u01fa\7@\2\2\u01fa\u0088\3\2\2\2\u01fb"+
		"\u01fc\t\35\2\2\u01fc\u008a\3\2\2\2\u01fd\u01fe\t\36\2\2\u01fe\u008c\3"+
		"\2\2\2\u01ff\u0206\7$\2\2\u0200\u0205\5\u0089E\2\u0201\u0205\5\u008bF"+
		"\2\u0202\u0205\5q9\2\u0203\u0205\t\37\2\2\u0204\u0200\3\2\2\2\u0204\u0201"+
		"\3\2\2\2\u0204\u0202\3\2\2\2\u0204\u0203\3\2\2\2\u0205\u0208\3\2\2\2\u0206"+
		"\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207\u0209\3\2\2\2\u0208\u0206\3\2"+
		"\2\2\u0209\u020a\7$\2\2\u020a\u008e\3\2\2\2\u020b\u020c\7^\2\2\u020c\u020d"+
		"\t \2\2\u020d\u0090\3\2\2\2\u020e\u0210\t!\2\2\u020f\u020e\3\2\2\2\u0210"+
		"\u0211\3\2\2\2\u0211\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0213\3\2"+
		"\2\2\u0213\u0214\bI\2\2\u0214\u0092\3\2\2\2\23\2\u011f\u0130\u0140\u0150"+
		"\u01aa\u01af\u01b5\u01b7\u01e2\u01ea\u01ec\u01f3\u01f5\u0204\u0206\u0211"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}