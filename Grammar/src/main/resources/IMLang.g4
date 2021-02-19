grammar IMLang;

concept : identifierIri type '.'
         EOF;

classAxiom  :
          subclassOf
        | equivalentTo
        | disjointWith
        ;
propertyAxiom   :
        subpropertyOf
        |inverseOf

        ;

type :
     TYPE
     (classType
     |dataType
     |recordType
     |objectProperty
     |annotationProperty
     |dataProperty
     )
     ;

classType :
    CLASS
    (';' label (';' label?)*)?
     (';' classAxiom (';' classAxiom?)*)?
    ;

dataType :
    DATATYPE
    (';' label (';' label?)*)?
    ;

recordType :
    RECORD
     (';' label (';' label?)*)?
     (';' classAxiom (';' classAxiom?)*)?
    (';' role (';' role)*)?
     ;
objectProperty :
    OBJECTPROPERTY
     (';' label (';' label?)*)?
    (';' propertyAxiom (';' propertyAxiom)*)?

    ;
dataProperty :
    DATAPROPERTY
     (';' label (';' label?)*)?
     (';' propertyAxiom (';' propertyAxiom)*)?
    ;
annotationProperty :
    ANNOTATION
    (';' label (';' label?)*)?
    (';' propertyAxiom (';' propertyAxiom)*)?
    ;
members :
    MEMBER
    classExpression
    ;
expansion   :
    EXPANSION
    '(' iri (',' iri)+? ')'
    ;

valueSet :
    VALUESET
     (';' label (';' label?)*)?
    (';' subclassOf)?
    (';' members)?
    (';' expansion)?
    ;

shapeOf:
    (TARGETCLASS)
    iri
    ;


propertyConstraint :
   PROPERTYCONSTRAINT '[' PATH iri (';' constraintParameter (';' constraintParameter)*)? ']'

    ;

constraintParameter :
    minCount
    |maxCount
    |minInclusive
    |maxInclusive
    |classValue
    |dataRange
    ;
minCount :
    MINCOUNT INTEGER;

maxCount :
    MAXCOUNT INTEGER;
minInclusive    :MININCLUSIVE DOUBLE
    ;
maxInclusive    :MAXINCLUSIVE DOUBLE
    ;
minExclusive    :MINEXCLUSIVE DOUBLE
    ;
maxExclusive :MAXEXCLUSIVE DOUBLE
    ;
classValue :
    CLASS (iri| '[' propertyConstraint ']')
    ;
label:
        (name
        |description
        |code
        |scheme
        |status
        |version)
        ;
status  :
    STATUS (ACTIVE|INACTIVE|DRAFT)

    ;
version :
    VERSION QUOTED_STRING
    ;
identifierIri :
    (IRI_LABEL? iri)
    ;
name :
    NAME
    QUOTED_STRING
    ;
description :
    DESCRIPTION
    QUOTED_STRING
    ;
code    :
    CODE
    QUOTED_STRING
    ;
scheme :
    SCHEME
    QUOTED_STRING
    ;


subclassOf: SUBCLASS classExpression;
equivalentTo: EQUIVALENTTO classExpression ;
disjointWith : DISJOINT classExpression ;
subpropertyOf : SUBPROPERTY iri;
inverseOf : INVERSE iri;

classExpression :
    iri (',' classExpression)*
    |roleGroup
    ;


iri
   :IRIREF
   |PREFIXIRI
   |QUOTED_STRING
   ;


roleGroup :
    ('['|'{') role  (';' role)*
     (']'|'}')
    ;
role :
    iri '='? (classExpression|dataRange)
    |minCount
    |maxCount
     ;

dataRange   :
    valueCollection
    |typedString
    |QUOTED_STRING
    |rangeValue
    ;
rangeValue :
    ((MININCLUSIVE|MINEXCLUSIVE) (typedString|DOUBLE))
    |((MAXINCLUSIVE|MAXEXCLUSIVE) (typedString|DOUBLE))
    ;
typedString :
    QUOTED_STRING '^^' iri
    ;

valueCollection   :
    '(' (QUOTED_STRING|typedString) (',' (QUOTED_STRING|typedString))+? ')'
    ;
dataRangeCollection :
    '(' dataRange (',' dataRange)+ ')'
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
MEMBER :M E M B E R
    ;
EXPANSION   : E X P A N S I O N
    ;
STATUS  : S T A T U S
    ;
ACTIVE  : A C T I V E
    ;
INACTIVE : I N A C T I V E
    ;
DRAFT   : D R A F T
    ;
VERSION : V E R S I O N
    ;
IRI_LABEL : I R I
    ;

TYPE : ('a '|'A '|'Type '| 'type ');

TERM    : T E R M
    ;
RECORD  : R E C O R D
    ;

TARGETCLASS : T A R G E T C L A S S
    ;
CLASS : C L A S S
    ;
OBJECTPROPERTY : O B J E C T P R O P E R T Y
    ;
DATAPROPERTY : D A T A P R O P E R T Y
    ;
ANNOTATION : A N N O T A T I O N
    ;
PROPERTYCONSTRAINT : P R O P E R T Y
    ;

DATATYPE : D A T A T Y P E
    ;
VALUESET    : V A L U E S E T
    ;
PATH    :P A T H
    ;
MINCOUNT : (M I N C O U N T)|(M I N)
    ;
MAXCOUNT : (M A X C O U N T)|(M A X)
    ;
NAME    : N A M E ' '
    ;
DESCRIPTION : D E S C R I P T I O N ' '
    ;
CODE : C O D E
    ;
SCHEME  : S C H E M E
    ;
MININCLUSIVE : (M I N I N C L U S I V E)| ('>=')
    ;
MAXINCLUSIVE : (M A X I N C L U S I V E)| ('<=')
    ;
MINEXCLUSIVE : (M I N E X C L U S I V E)| ('>')
    ;
MAXEXCLUSIVE : (M A X E X C L U S I V E)| ('<')
    ;
SUBCLASS : (S U B C L A S S O F)|('<<<')
    ;
EQUIVALENTTO    : (E Q U I V A L E N T T O)|(EQ EQ EQ)
    ;
DISJOINT    : D I S J O I N T W I T H
    ;
SUBPROPERTY : S U B P R O P E R T Y O F
    ;
INVERSE : I N V E R S E O F
    ;

INTEGER : DIGIT+
    ;
DOUBLE : DIGIT+ ('.' DIGIT+)?
    ;

DIGIT : [0-9];
EXACT : 'exactly ' DIGIT+;
AND :
    'and'
    | ','
    | '&'
    ;
OR :
    'or'
    ;



PREFIXIRI
  : LOWERCASE+ ':' ((LOWERCASE|UPPERCASE|INTEGER|'_'|'-')+)
  | ':' ((LOWERCASE|UPPERCASE|INTEGER|'_'|'-')+)
  |((LOWERCASE|UPPERCASE|INTEGER|'_'|'-')+)
  ;





IRIREF
   : '<' (UPPERCASE | LOWERCASE| INTEGER| '.' | ':' | '/' | '\\' | '#' | '@' | '%' | '&'|'_')* '>'
    ;





LOWERCASE : [a-z];
UPPERCASE : [A-Z];
PLX
   : PERCENT | PN_LOCAL_ESC
   ;
PERCENT
   : '%' HEX HEX
   ;

QUOTED_STRING :
    ('"'|'\'') ~["']* ('"'|'\'')
    ;
HEX
   : [0-9] | [A-F] | [a-f]
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
SC  :
    ';'
    ;