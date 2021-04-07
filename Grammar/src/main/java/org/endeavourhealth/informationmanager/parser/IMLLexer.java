// Generated from C:/Users/david/CloudStation/EhealthTrust/Discovery Data Service/InformationManager/Grammar/src/main/resources\IML.g4 by ANTLR 4.9.1
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
public class IMLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, IRI_LABEL=4, NAME=5, DESCRIPTION=6, CODE=7, STATUS=8, 
		VERSION=9, QUOTED_STRING=10, SIMPLE_STRING=11, INTEGER=12, LOWERCASE=13, 
		UPPERCASE=14, DIGIT=15, WS=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "IRI_LABEL", "NAME", "DESCRIPTION", "CODE", "STATUS", 
			"VERSION", "QUOTED_STRING", "SIMPLE_STRING", "INTEGER", "LOWERCASE", 
			"UPPERCASE", "DIGIT", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", 
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", 
			"Y", "Z", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "':'", "'\"'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "IRI_LABEL", "NAME", "DESCRIPTION", "CODE", "STATUS", 
			"VERSION", "QUOTED_STRING", "SIMPLE_STRING", "INTEGER", "LOWERCASE", 
			"UPPERCASE", "DIGIT", "WS"
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


	public IMLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "IML.g4"; }

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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\22\u00e0\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13"+
		"\3\13\5\13\u008c\n\13\3\13\6\13\u008f\n\13\r\13\16\13\u0090\3\f\3\f\3"+
		"\f\3\f\6\f\u0097\n\f\r\f\16\f\u0098\3\r\6\r\u009c\n\r\r\r\16\r\u009d\3"+
		"\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3"+
		"\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3"+
		"\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3"+
		"$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\6+\u00db\n+\r+\16+\u00dc\3"+
		"+\3+\2\2,\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33"+
		"\17\35\20\37\21!\2#\2%\2\'\2)\2+\2-\2/\2\61\2\63\2\65\2\67\29\2;\2=\2"+
		"?\2A\2C\2E\2G\2I\2K\2M\2O\2Q\2S\2U\22\3\2\"\5\2\"\".\60aa\4\2//aa\3\2"+
		"c|\3\2C\\\3\2\62;\4\2CCcc\4\2DDdd\4\2EEee\4\2FFff\4\2GGgg\4\2HHhh\4\2"+
		"IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4\2OOoo\4\2PPpp\4\2QQqq\4"+
		"\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4\2XXxx\4\2YYyy\4\2ZZz"+
		"z\4\2[[{{\4\2\\\\||\5\2\13\f\17\17\"\"\2\u00cf\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\2\37\3\2\2\2\2U\3\2\2\2\3W\3\2\2\2\5Y\3\2\2\2\7[\3\2\2"+
		"\2\t]\3\2\2\2\13a\3\2\2\2\rf\3\2\2\2\17r\3\2\2\2\21w\3\2\2\2\23~\3\2\2"+
		"\2\25\u008e\3\2\2\2\27\u0096\3\2\2\2\31\u009b\3\2\2\2\33\u009f\3\2\2\2"+
		"\35\u00a1\3\2\2\2\37\u00a3\3\2\2\2!\u00a5\3\2\2\2#\u00a7\3\2\2\2%\u00a9"+
		"\3\2\2\2\'\u00ab\3\2\2\2)\u00ad\3\2\2\2+\u00af\3\2\2\2-\u00b1\3\2\2\2"+
		"/\u00b3\3\2\2\2\61\u00b5\3\2\2\2\63\u00b7\3\2\2\2\65\u00b9\3\2\2\2\67"+
		"\u00bb\3\2\2\29\u00bd\3\2\2\2;\u00bf\3\2\2\2=\u00c1\3\2\2\2?\u00c3\3\2"+
		"\2\2A\u00c5\3\2\2\2C\u00c7\3\2\2\2E\u00c9\3\2\2\2G\u00cb\3\2\2\2I\u00cd"+
		"\3\2\2\2K\u00cf\3\2\2\2M\u00d1\3\2\2\2O\u00d3\3\2\2\2Q\u00d5\3\2\2\2S"+
		"\u00d7\3\2\2\2U\u00da\3\2\2\2WX\7=\2\2X\4\3\2\2\2YZ\7<\2\2Z\6\3\2\2\2"+
		"[\\\7$\2\2\\\b\3\2\2\2]^\5\61\31\2^_\5C\"\2_`\5\61\31\2`\n\3\2\2\2ab\5"+
		";\36\2bc\5!\21\2cd\59\35\2de\5)\25\2e\f\3\2\2\2fg\5\'\24\2gh\5)\25\2h"+
		"i\5E#\2ij\5%\23\2jk\5C\"\2kl\5\61\31\2lm\5? \2mn\5G$\2no\5\61\31\2op\5"+
		"=\37\2pq\5;\36\2q\16\3\2\2\2rs\5%\23\2st\5=\37\2tu\5\'\24\2uv\5)\25\2"+
		"v\20\3\2\2\2wx\5E#\2xy\5G$\2yz\5!\21\2z{\5G$\2{|\5I%\2|}\5E#\2}\22\3\2"+
		"\2\2~\177\5K&\2\177\u0080\5)\25\2\u0080\u0081\5C\"\2\u0081\u0082\5E#\2"+
		"\u0082\u0083\5\61\31\2\u0083\u0084\5=\37\2\u0084\u0085\5;\36\2\u0085\24"+
		"\3\2\2\2\u0086\u008b\7$\2\2\u0087\u008c\5\33\16\2\u0088\u008c\5\35\17"+
		"\2\u0089\u008c\5\31\r\2\u008a\u008c\t\2\2\2\u008b\u0087\3\2\2\2\u008b"+
		"\u0088\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008a\3\2\2\2\u008c\u008d\3\2"+
		"\2\2\u008d\u008f\7$\2\2\u008e\u0086\3\2\2\2\u008f\u0090\3\2\2\2\u0090"+
		"\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\26\3\2\2\2\u0092\u0097\5\33\16"+
		"\2\u0093\u0097\5\35\17\2\u0094\u0097\5\31\r\2\u0095\u0097\t\3\2\2\u0096"+
		"\u0092\3\2\2\2\u0096\u0093\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0095\3\2"+
		"\2\2\u0097\u0098\3\2\2\2\u0098\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099"+
		"\30\3\2\2\2\u009a\u009c\5\37\20\2\u009b\u009a\3\2\2\2\u009c\u009d\3\2"+
		"\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\32\3\2\2\2\u009f\u00a0"+
		"\t\4\2\2\u00a0\34\3\2\2\2\u00a1\u00a2\t\5\2\2\u00a2\36\3\2\2\2\u00a3\u00a4"+
		"\t\6\2\2\u00a4 \3\2\2\2\u00a5\u00a6\t\7\2\2\u00a6\"\3\2\2\2\u00a7\u00a8"+
		"\t\b\2\2\u00a8$\3\2\2\2\u00a9\u00aa\t\t\2\2\u00aa&\3\2\2\2\u00ab\u00ac"+
		"\t\n\2\2\u00ac(\3\2\2\2\u00ad\u00ae\t\13\2\2\u00ae*\3\2\2\2\u00af\u00b0"+
		"\t\f\2\2\u00b0,\3\2\2\2\u00b1\u00b2\t\r\2\2\u00b2.\3\2\2\2\u00b3\u00b4"+
		"\t\16\2\2\u00b4\60\3\2\2\2\u00b5\u00b6\t\17\2\2\u00b6\62\3\2\2\2\u00b7"+
		"\u00b8\t\20\2\2\u00b8\64\3\2\2\2\u00b9\u00ba\t\21\2\2\u00ba\66\3\2\2\2"+
		"\u00bb\u00bc\t\22\2\2\u00bc8\3\2\2\2\u00bd\u00be\t\23\2\2\u00be:\3\2\2"+
		"\2\u00bf\u00c0\t\24\2\2\u00c0<\3\2\2\2\u00c1\u00c2\t\25\2\2\u00c2>\3\2"+
		"\2\2\u00c3\u00c4\t\26\2\2\u00c4@\3\2\2\2\u00c5\u00c6\t\27\2\2\u00c6B\3"+
		"\2\2\2\u00c7\u00c8\t\30\2\2\u00c8D\3\2\2\2\u00c9\u00ca\t\31\2\2\u00ca"+
		"F\3\2\2\2\u00cb\u00cc\t\32\2\2\u00ccH\3\2\2\2\u00cd\u00ce\t\33\2\2\u00ce"+
		"J\3\2\2\2\u00cf\u00d0\t\34\2\2\u00d0L\3\2\2\2\u00d1\u00d2\t\35\2\2\u00d2"+
		"N\3\2\2\2\u00d3\u00d4\t\36\2\2\u00d4P\3\2\2\2\u00d5\u00d6\t\37\2\2\u00d6"+
		"R\3\2\2\2\u00d7\u00d8\t \2\2\u00d8T\3\2\2\2\u00d9\u00db\t!\2\2\u00da\u00d9"+
		"\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd"+
		"\u00de\3\2\2\2\u00de\u00df\b+\2\2\u00dfV\3\2\2\2\t\2\u008b\u0090\u0096"+
		"\u0098\u009d\u00dc\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}