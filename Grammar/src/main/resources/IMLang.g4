grammar IMLang;

concept : iriLabel types annotationList predicateObjectList
    '.'
      EOF
       ;
iriLabel :
    IRI iri
    ;
annotationList :
    (';' annotation)+
    ;
predicateObjectList
   : (';' (axiom|properties|membership|target))+
   ;


annotation:
    ((name|description|code|version|scheme) (':')? QUOTED_STRING)
    | (scheme|status)
    ;

 scheme :
    SCHEME iri
    ;
types :
     (';')? TYPE iri (',' iri)*?
     ;
version :
    VERSION
    ;

axiom  :
        subclassOf
        | equivalentTo
        |subpropertyOf
        |inverseOf
        |domain
        |range
        ;
     
properties :
     PROPERTIES (':')?
    propertyRestriction (',' propertyRestriction)*?

    ;
membership :
    members (';'notmembers)*?
    ;

members :
    MEMBERS (':')?
    classExpression? (',' classExpression)*?
    ;
notmembers  :
    NOTMEMBERS '['
        iri (',' iri)*?
     ']'
     ;
target  :
    TARGETCLASS
    ;


minInclusive    :MININCLUSIVE DOUBLE
    ;
maxInclusive    :MAXINCLUSIVE DOUBLE
    ;
minExclusive    :MINEXCLUSIVE DOUBLE
    ;
maxExclusive :MAXEXCLUSIVE DOUBLE
    ;

status  :
    STATUS iri
      ;

subclassOf : SUBCLASS classExpression;
equivalentTo : EQUIVALENTTO classExpression ;
subpropertyOf : SUBPROPERTY iri;
inverseOf : INVERSE iri;
domain : DOMAIN classExpression;
range : RANGE classExpression;

classExpression :
    (classIri |existential|not)
    ((and|or) classExpression)*?
    ;
classIri :
     CLASS (':')? iri
     ;

and :
    AND
    ;
or  :
    OR
    ;
not :
    NOT classExpression
    ;


iri : IRIREF
    | (PN_LOCAL PIPED_STRING?)
    ;
literal
    : QUOTED_STRING
    ;
existential :
    ('(')?
    roleIri ('='|some)
    classOrDataType
    (')')?
    ;
roleIri :
    ROLE (':')? iri
        ;

propertyRestriction :
    propertyIri
    (exactCardinality
    |rangeCardinality
    |minCardinality
    |maxCardinality
    |some
    |only)?
    classOrDataType


     ;
some : SOME
    ;

only : ONLY
    ;

 propertyIri:
    iri
    ;
 exactCardinality   :
    EXACTLY INTEGER
    ;
 rangeCardinality :
   minCardinality maxCardinality
    ;

minCardinality :
  MIN INTEGER
  ;
maxCardinality :
  MAX INTEGER
  ;


classOrDataType :
    iri
    ;
 name : NAME
    ;
 description : DESCRIPTION
    ;
 code : CODE
    ;

WS
   : ([\t\r\n\u000C] | ' ') + -> skip
   ;
// lexer

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');


EQ  : '='
    ;
MEMBERS :M E M B E R S
    ;
NOTMEMBERS  :N O T M E M B E R S
    ;

STATUS  : S T A T U S
    ;
VERSION : V E R S I O N
    ;
PROPERTIES: P R O P E R T I E S
    ;

TYPE : T Y P E;

MIN : M I N ;

MAX : M A X;

SOME : S O M E ;

ONLY : O N L Y
    ;
ROLE    : R O L E
    ;

MININCLUSIVE : (M I N I N C L U S I V E)| ('>=')
    ;
MAXINCLUSIVE : (M A X I N C L U S I V E)| ('<=')
    ;
MINEXCLUSIVE : (M I N E X C L U S I V E)| ('>')
    ;
MAXEXCLUSIVE : (M A X E X C L U S I V E)| ('<')
    ;
SUBCLASS : S U B C L A S S O F
    ;
EQUIVALENTTO    :E Q U I V A L E N T T O
    ;
DISJOINT    : D I S J O I N T W I T H
    ;
SUBPROPERTY : S U B P R O P E R T Y O F
    ;
INVERSE : I N V E R S E O F
    ;
DOMAIN  : D O M A I N
    ;
RANGE : R A N G E
    ;

TARGETCLASS :
    T A R G E T C L A S S
    ;
EXACTLY : E X A C T L Y ;

AND : A N D
    ;
CLASS : C L A S S
    ;

INTEGER : DIGIT+
    ;
DOUBLE : DIGIT+ ('.' DIGIT+)?
    ;

DIGIT : [0-9];

OR :
    O R
    ;
NOT :
   N O T
   ;


IRI : I R I
    ;
NAME : N A M E
    ;
DESCRIPTION : D E S C R I P T I O N
    ;
CODE : C O D E
    ;

SCHEME  : S C H E M E
    ;

PN_CHARS_BASE
   : 'A' .. 'Z' | 'a' .. 'z' | '\u00C0' .. '\u00D6' | '\u00D8' .. '\u00F6' | '\u00F8' .. '\u02FF' | '\u0370' .. '\u037D' | '\u037F' .. '\u1FFF' | '\u200C' .. '\u200D' | '\u2070' .. '\u218F' | '\u2C00' .. '\u2FEF' | '\u3001' .. '\uD7FF' | '\uF900' .. '\uFDCF' | '\uFDF0' .. '\uFFFD'
   ;
PN_CHARS_U
   : PN_CHARS_BASE | '_'
   ;
PN_CHARS
   : PN_CHARS_U | '-' | [0-9] | '\u00B7' | [\u0300-\u036F] | [\u203F-\u2040]
   ;



IRIREF
   : '<' (PN_CHARS | '.' | ':' | '/' | '\\' | '#' | '@' | '%' | '&' | UCHAR)* '>'
   ;

UCHAR
   : '\\u' HEX HEX HEX HEX | '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
   ;


PN_LOCAL
   : (PN_CHARS_U | ':' | [0-9] | PLX) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX))?
   ;
PLX
   : PERCENT | PN_LOCAL_ESC
   ;
PERCENT
   : '%' HEX HEX
   ;


ECHAR
   : '\\' [tbnrf"'\\]
   ;


QUOTED_STRING :
   STRING_LITERAL_QUOTE|STRING_LITERAL_SINGLE_QUOTE
   ;

STRING_LITERAL_QUOTE
   : '"' (~ ["\\\r\n] | '\'' | '\\"')* '"'
   ;
STRING_LITERAL_SINGLE_QUOTE
   : '\'' (~ [\u0027\u005C\u000A\u000D] | ECHAR | UCHAR | '"')* '\''
   ;

PIPED_STRING :
    '|' (~ [\u0027\u005C\u000A\u000D] | ECHAR | UCHAR | '"')+ '|'
    ;


 PN_LOCAL_ESC
    : '\\' ('_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%')
    ;
HEX
   : [0-9] | [A-F] | [a-f]
   ;

