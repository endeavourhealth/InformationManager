grammar IMLang;

concept : identifierIri (label SC)? type
         '.' EOF;


classAxiom  :
          subclassOf
        | equivalentTo
        | disjointWith
        label?
        ;
propertyAxiom   :
        subpropertyOf
        |inverseOf
        label?
        ;

type :

     TYPE
     (classType
     |dataType
     |shape
     |valueSet
     |propertyType)
     ;

classType :
    CLASS
    (SC classAxiom)+?
    ;

dataType :
    DATATYPE
    ;
shape :
    SHAPE
    SC shapeOf
     ;
propertyType :
    PROPERTY
    (SC propertyAxiom)+?
    (SC members)?
    (SC expansion)?
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
    (SC subsetOf)?
    (SC members)?
    (SC expansion)?
    ;

shapeOf:
    (RECORDOF|TARGETCLASS)
    iri
    (SC propertyConstraint)*
    ;


propertyConstraint :
   PROPERTY '[' PATH iri (SC constraintParameter)+? ']'

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
maxExlusive :MAXEXCLUSIVE DOUBLE
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
        (SC label)*?
        ;
status  :
    STATUS (ACTIVE|INACTIVE|DRAFT)

    ;
version :
    VERSION QUOTED_STRING
    ;
identifierIri :
    (IRI_LABEL? IRI)
    |(TERM? QUOTED_STRING)
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
    objectCollection
    |roleGroup
   |iri

    ;
objectCollection :
    '(' classExpression (',' classExpression)*? ')'
    ;

iri :
    IRI | QUOTED_STRING
    ;


roleGroup :
    ('['|'{') role
     (SC role)*?
     (']'|'}')
    ;
role :
    iri (classExpression|dataRange)
     ;

dataRange   :
    valueCollection
    |QUOTED_STRING
    |typedString
    |rangeValue
    ;
rangeValue :
    ((MININCLUSIVE|MINEXCLUSIVE) QUOTED_STRING)
    |((MAXINCLUSIVE|MAXEXCLUSIVE) QUOTED_STRING)
    ;
typedString :
    QUOTED_STRING '^^' IRI
    ;

valueCollection   :
    '(' (QUOTED_STRING|typedString) (',' (QUOTED_STRING|typedString))+? ')'
    ;
dataRangeCollection :
    '(' dataRange (',' dataRange)+ ')'
    ;
subsetOf    :
    SUBSET
    iri
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
SUBSET  :(I S)? S U B S E T O F
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
SHAPE : S H A P E
    ;
RECORDOF : (I S)? R E C O R D O F
    ;
TARGETCLASS : T A R G E T C L A S S
    ;
CLASS : C L A S S
    ;
PROPERTY : P R O P E R T Y
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



IRI :
    ABBREVIATED_IRI
    | FULL_IRI
    | WORD
    ;



ABBREVIATED_IRI :
   ':' WORD
   |LOWERCASE+ ':' WORD
    ;
FULL_IRI :
    '<' WORD '>'
    ;



WORD :
    (LOWERCASE | UPPERCASE| DIGIT| '_'| '-' | '/' | '#' | '.' |':' )+;

LOWERCASE : [a-z];
UPPERCASE : [A-Z];

QUOTED_STRING :
    ('"'|'\'') ~["']* ('"'|'\'')
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