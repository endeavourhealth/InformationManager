grammar XMAOWL;
iriRef
    : IRI_REF
    |  VARNAME
    | prefixedName
    | STRING_LITERAL1
    ;

prefixedName
    :PNAME_LN
    | PNAME_NS
    ;
entity: classentity| objectPropertyEntity;

classentity
    : CLASS_LABEL classIRI coreProperties (classExpression)* EOF;

objectPropertyEntity
    :OBJECT_PROPERTY_LABEL objectPropertyIRI coreProperties objectPropertyAxiom+ EOF;

coreProperties : (NAME_LABEL string ) (DESC_LABEL string )?  (CODE_LABEL string )? (SCHEME_LABEL string )?;
string
    : STRING_LITERAL1
    | STRING_LITERAL2
    /* | STRING_LITERAL_LONG('0'..'9') | STRING_LITERAL_LONG('0'..'9')*/
    ;
classAxiom: subClass | equivalent | disjoint ;
subClass: SUBCLASS_LABEL classExpression;
equivalent: EQUIVALENT_LABEL classExpression;
disjoint: DISJOINT_LABEL classExpression;
objectPropertyAxiom
	:
		RANGE_LABEL classExpression
		|DOMAIN_LABEL classExpression
		|	SUB_PROPERTY_OF_LABEL objectPropertyExpressionAnnotatedList
		|	EQUIVALENT_LABEL objectPropertyExpressionAnnotatedList
		|	DISJOINT_LABEL objectPropertyExpressionAnnotatedList
		|	INVERSE_LABEL objectPropertyExpressionAnnotatedList
		|	SUB_PROPERTY_CHAIN_LABEL annotations objectPropertyExpression (O_LABEL objectPropertyExpression)+

	;
objectPropertyExpressionAnnotatedList
	:	annotations? objectPropertyExpression (AND_LABEL objectPropertyExpressionAnnotatedList)*
	;


classExpression
    : intersection (OR_LABEL intersection)*
    | classRole
	;

intersection
    :classRole (and classRole)+
    ;
classRole
	:	(NOT_LABEL)? (restriction | atomic)
	;
atomic
	:	classIRI
	|	LEFT_PAREN classExpression RIGHT_PAREN
	;



individual
	: individualIRI
	| NODE_ID
	;

restriction
	: 	objectPropertyExpression SOME_LABEL classRole
	|	objectPropertyExpression  ONLY_LABEL classRole
	|	objectPropertyExpression VALUE_LABEL individual
	|	objectPropertyExpression SELF_LABEL
	|	objectPropertyExpression MIN_LABEL nonNegativeInteger (classRole)?
	|	objectPropertyExpression MAX_LABEL nonNegativeInteger (classRole)?
	|	objectPropertyExpression EXACTLY_LABEL nonNegativeInteger (classRole)?
	|	dataPropertyExpression SOME_LABEL dataclassRole
	|	dataPropertyExpression ONLY_LABEL dataclassRole
	|	dataPropertyExpression VALUE_LABEL literal
	|	dataPropertyExpression MIN_LABEL nonNegativeInteger (dataclassRole)?
	|	dataPropertyExpression MAX_LABEL nonNegativeInteger (dataclassRole)?
	|	dataPropertyExpression  EXACTLY_LABEL nonNegativeInteger (dataclassRole)?
	;
nonNegativeInteger
	: INTEGER
	;
dataclassRole
	:	NOT_LABEL? dataAtomic
	;
objectPropertyList
    	:	objectProperty (AND_LABEL objectProperty)*
    	;

objectProperty
    	:	objectPropertyIRI
    	;

 dataProperty
    	:	dataPropertyIRI
    	;

dataPropertyIRI
    	:	iriRef
    	;


dataAtomic
	:	dataType
	|	OPEN_CURLY_BRACE literalList CLOSE_CURLY_BRACE
	|	dataTypeRestriction
	|	LEFT_PAREN dataRange RIGHT_PAREN
	;

dataType
	: datatypeIRI
	| INTEGER_LABEL
	| DECIMAL_LABEL
	| FLOAT_LABEL
	| STRING_LABEL
	;
literal
	: typedLiteral
	| stringLiteralNoLanguage
	| stringLiteralWithLanguage
	| integerLiteral
	| decimalLiteral
	| floatingPointLiteral
	;

typedLiteral
	: lexicalValue REFERENCE dataType
	;


stringLiteralNoLanguage
	: STRING_LITERAL2
	;
stringLiteralWithLanguage
	: STRING_LITERAL2 LANGUAGE_TAG
	;

lexicalValue
	: STRING_LITERAL2
	;

dataPropertyExpression
	:	iriRef
	;

dataTypeRestriction
	:	dataType OPEN_SQUARE_BRACE facet restrictionValue  (AND_LABEL facet restrictionValue)* CLOSE_SQUARE_BRACE
	;

facet
	:	LENGTH_LABEL
	|	MIN_LENGTH_LABEL
	|	MAX_LENGTH_LABEL
	|	PATTERN_LABEL
	|	LANG_PATTERN_LABEL
	|	LESS_EQUAL
	|	LESS
	|	GREATER_EQUAL
	|	GREATER
	;

restrictionValue
	:	literal
	;

inverseObjectProperty
	:	INVERSE_LABEL objectPropertyIRI
	;

decimalLiteral
	: (PLUS | MINUS)? INTEGER DOT INTEGER
	;

integerLiteral
	: (PLUS | MINUS)? INTEGER
	;

floatingPointLiteral
	: (PLUS | MINUS)? ((INTEGER (DOT INTEGER)? EXPONENT?) | DOT INTEGER EXPONENT?)
	;

dataRange
	:	dataConjunction (OR_LABEL dataConjunction)*
	;

dataConjunction
	:	dataclassRole (AND_LABEL dataclassRole)*
	;

annotationAnnotatedList
	:	(annotations)? annotation (AND_LABEL (annotations)? annotation)*
	;

annotation
	:	annotationPropertyIRI annotationTarget
	;
annotationPropertyIRI
	:	iriRef
	;

annotationTarget
	:	NODE_ID
	|	iriRef
	|	literal
	;
annotations
	: ANNOTATIONS_LABEL annotationAnnotatedList
	;


literalList
	:	literal (COMMA literal)*
	;


objectPropertyExpression
	:	objectPropertyIRI
	|	inverseObjectProperty
	;

ataPropertyIRI
	:	iriRef
	;

datatypeIRI
	: iriRef
	;

objectPropertyIRI
	: iriRef
	;


individualIRI
	: iriRef
	;

datatypePropertyIRI
	: iriRef
	;



classIRI
	: iriRef
	;

and
    :AND_LABEL
    | COMMA
    ;


// LEXER RULES
COMMA
    :','
    ;
O_LABEL	:	'o'
    ;

RANGE_LABEL
	:	'Range:'| 'range'
	;
DOMAIN_LABEL
	:	'Domain:'| 'domain'
	;


SUB_PROPERTY_OF_LABEL
	:	'SubPropertyOf:'| 'subproperty of'| 'subpropertyof'
	;

SUB_PROPERTY_CHAIN_LABEL
	:	'SubPropertyChain:'|'subpropertychain'| 'subproperty chain'
	;

REFERENCE
	:'^^'
	;
INTEGER_LABEL
	:	'integer'
	;

DECIMAL_LABEL
	:	'decimal'
	;

FLOAT_LABEL
	:	'float'
	;
STRING_LABEL
	:	'string'
    ;



LENGTH_LABEL
	:	'length'
	;

MIN_LENGTH_LABEL
	:	'minLength'
	;

MAX_LENGTH_LABEL
	:	'maxLength'
	;

PATTERN_LABEL
	:	'pattern'
	;

LANG_PATTERN_LABEL
	:	'langPattern'
	;

THAT_LABEL
	:	'that'
	;

ANNOTATIONS_LABEL
	:	'Annotation'| 'annotation'
	;
INVERSE_LABEL
	:	'inverse'
	;

MINUS
    :'-'
    ;

DOT
    :'.'
    ;

PLUS
    :'+'
    ;

OPEN_SQUARE_BRACE
    :'['
    ;

CLOSE_SQUARE_BRACE
    :']'
    ;


NODE_ID
    :'_:'
    ;
NOT_LABEL
	:	'not'
	;

LESS_EQUAL
    :'<='
    ;

GREATER_EQUAL
    :'>='
    ;

LESS
	:'<'
	;

GREATER
	:'>'
	;

OPEN_CURLY_BRACE
	:'{'
	;

CLOSE_CURLY_BRACE
	:'}'
	;

CLASS_LABEL
    :'Class:'
    | 'class:'
    ;
SUBCLASS_LABEL
    : 'SubClassOf:'
    | 'subclassof:'
    | 'Subclass of:'
    | 'subclass of'
    ;
EQUIVALENT_LABEL
    :'EquivalentTo:'
    | 'equivalentto:'
    ;
DISJOINT_LABEL
    :'DisjointWith:'
    |'disjointwith:'
    ;
NAME_LABEL
    :'Name:'
    | 'name:'
    ;
DESC_LABEL
    :'Description:'
    |'description:'
    ;
CODE_LABEL
    :'Code:'
    |'code:'
    ;
SCHEME_LABEL
    :'Scheme:'
    |'scheme:'
    | 'codeScheme:'
    | 'codescheme:'
    ;
OR_LABEL
	:	'or '| 'OR ' | '| '
	;

AND_LABEL
	:	'and'|'AND'| '&'
	;

SOME_LABEL
	:	'some '| 'SOME '| 'at least one '| 'AT LEAST ONE '
	;

ONLY_LABEL
	:	'only '| 'ONLY '
	;

VALUE_LABEL
	:	'value ' | 'VALUE '
	;

SELF_LABEL
	:	'Self '
	;

MIN_LABEL
	:	 'min '| 'MIN '
	;

MAX_LABEL
	:	'max ' | 'MAX '
	;

EXACTLY_LABEL
	:	'exactly ' | 'EXACTLY '
	;

OBJECT_PROPERTY_LABEL
	:	'ObjectProperty:'
	;

DATA_PROPERTY_LABEL
	:	'DataProperty:'
	;

ANNOTATION_PROPERTY_LABEL
	:	'AnnotaionProperty:'
	;

NAMED_INDIVIDUAL_LABEL
	:	'NamedIndividual'
	;

LEFT_PAREN
    :'('
    ;

RIGHT_PAREN
    :')'
    ;
IRI_REF
    :'<' ( ~('<' | '>' | '"' | '{' | '}' | '|' | '^' | '\\' | '`') | (PN_CHARS))* '>'
    ;

PNAME_NS
    : PN_PREFIX? ':'
    ;

PNAME_LN
    : PNAME_NS PN_LOCAL
    ;

BLANK_NODE_LABEL
    :'_:' PN_LOCAL
    ;

VAR1
    :'?' VARNAME
    ;

VAR2
    :'$' VARNAME
    ;

LANGTAG
    :'@' PN_CHARS_BASE+ ('-' (PN_CHARS_BASE DIGIT)+)*
    ;

INTEGER
    : DIGIT+
    ;

DECIMAL
    : DIGIT+ '.' DIGIT*
    | '.' DIGIT+
    ;

DOUBLE
    : DIGIT+ '.' DIGIT* EXPONENT
    | '.' DIGIT+ EXPONENT
    | DIGIT+ EXPONENT
    ;

INTEGER_POSITIVE
    :'+' INTEGER
    ;

DECIMAL_POSITIVE
    :'+' DECIMAL
    ;

DOUBLE_POSITIVE
    :'+' DOUBLE
    ;

INTEGER_NEGATIVE
    :'-' INTEGER
    ;

DECIMAL_NEGATIVE
    :'-' DECIMAL
    ;

DOUBLE_NEGATIVE
    :'-' DOUBLE
    ;

EXPONENT
    : ('e'|'E') ('+'|'-')? DIGIT+
    ;


STRING_LITERAL1
    :'\'' ( ~('\u0027' | '\u005C' | '\u000A' | '\u000D') | ECHAR )* '\''
    ;

STRING_LITERAL2
    :'"'  ( ~('\u0022' | '\u005C' | '\u000A' | '\u000D') | ECHAR )* '"'
    ;

STRING_LITERAL_LONG1
    :'\'\'\'' ( ( '\'' | '\'\'' )? (~('\'' | '\\') | ECHAR ) )* '\'\'\''
    ;

STRING_LITERAL_LONG2
    :'"""' ( ( '"' | '""' )? ( ~('\'' | '\\') | ECHAR ) )* '"""'
    ;

ECHAR
    :'\\' ('t' | 'b' | 'n' | 'r' | 'f' | '"' | '\'')
    ;

NIL
    :'(' WS* ')'
    ;

ANON
    :'[' WS* ']'
    ;

PN_CHARS_U
    : PN_CHARS_BASE | '_'
    ;

VARNAME
    : ( PN_CHARS_U | DIGIT ) ( PN_CHARS_U | DIGIT | '\u00B7' | ('\u0300'..'\u036F') | ('\u203F'..'\u2040') )*
    ;

fragment
PN_CHARS
    : PN_CHARS_U
    | '-'
    | DIGIT
    /*| '\u00B7'
    | '\u0300'..'\u036F'
    | '\u203F'..'\u2040'*/
    ;

PN_PREFIX
    : PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?
    ;

PN_LOCAL
    : ( PN_CHARS_U | DIGIT ) ((PN_CHARS|'.')* PN_CHARS)?
    ;

fragment
PN_CHARS_BASE
    :'A'..'Z'
    | 'a'..'z'
    | '\u00C0'..'\u00D6'
    | '\u00D8'..'\u00F6'
    | '\u00F8'..'\u02FF'
    | '\u0370'..'\u037D'
    | '\u037F'..'\u1FFF'
    | '\u200C'..'\u200D'
    | '\u2070'..'\u218F'
    | '\u2C00'..'\u2FEF'
    | '\u3001'..'\uD7FF'
    | '\uF900'..'\uFDCF'
    | '\uFDF0'..'\uFFFD'
    ;

fragment
DIGIT
    :'0'..'9'
    ;

WS
    : (' '
    | '\t'
    | '\n'
    | '\r')+->skip
    ;


LANGUAGE_TAG
    :'@' (('a'..'z')|('A'..'Z'))+ (MINUS (('a'..'z')('A'..'Z')INTEGER)+)*
    //{\$this->setText(substr(\$this->getText(), 1, strlen(\$this->getText()) - 1)); }
    ;
