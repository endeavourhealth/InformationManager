grammar IMLang;

concept : (iri conceptPredicateObjectList)
    '.'
      EOF
       ;

conceptPredicateObjectList
   : (annotation|predicateIri|axiom|properties|members)
     (';' (annotation|predicateIri|axiom|properties|members)?)*
   ;




annotation:
    (name|description|code|version)  QUOTED_STRING
    ;
predicateIri    :
    (scheme|type|status|target) iri
    ;
 scheme :
    SCHEME
    ;
type :
     TYPE
     ;
version :
    VERSION
    ;

axiom  :
        subclassOf
        | equivalentTo
        |subpropertyOf
        |inverseOf
        ;
     
properties :
     PROPERTIES '['
    propertyRestriction? (',' propertyRestriction)*?
    ']'
    ;

members :
    MEMBERS '['
    classExpression? (',' classExpression)*?
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
    STATUS   ;

subclassOf : SUBCLASS classExpression;
equivalentTo : EQUIVALENTTO classExpression ;
subpropertyOf : SUBPROPERTY iri;
inverseOf : INVERSE iri;

classExpression :
    iri
   |(intersection)
   |(union)
    ;


intersection    :
   iri (AND (iri|propertyRestriction|union|complement|subExpression))+
   ;
subExpression:
  '(' (union|intersection|complement|propertyRestriction) ')'
  ;

 union    :
    iri (OR (iri|propertyRestriction))+
    ;
 complement :
    NOT
    (iri|intersection|union)
    ;

iri :
  FULLIRI| PREFIXIRI
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
TARGETCLASS :
    T A R G E T C L A S S
    ;
EXACTLY : E X A C T L Y ;

AND : A N D
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



NAME : N A M E
    ;
DESCRIPTION : D E S C R I P T I O N
    ;
CODE : C O D E
    ;

SCHEME  : S C H E M E
    ;
PREFIXIRI    :
 ((LOWERCASE)* ':') (UPPERCASE | LOWERCASE| INTEGER)+
;

IRIREF
   : (LOWERCASE | UPPERCASE | LOWERCASE| INTEGER|PN_LOCAL_ESC)+
    ;
FULLIRI :
  '<' IRIREF '>'
  ;

LOWERCASE : [a-z];
UPPERCASE : [A-Z];

QUOTED_STRING :
    '"' (LOWERCASE|UPPERCASE|INTEGER|' '|','|'.'|'-'|'_')* '"'
    ;

 PN_LOCAL_ESC
    : '\\' ('_' | '~' | '.' | '-' | '!' | '$' | '&' | '\'' | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%')
    ;

WS
    : (' '
    | '\t'
    | '\n'
    | '\r')+->skip
    ;

