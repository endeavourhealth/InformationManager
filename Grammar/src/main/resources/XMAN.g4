grammar MAN ;

entity : classentity;

classentity : LABEL_CLASS ws EQUAL cr;

coreproperties :name describe? code? scheme? ;

name :NAME COLON ws text cr;
describe : DESCRIPTION COLON ws text cr;
code : CODE COLON ws word cr;
scheme: SCHEME  COLON ws iri cr;

word: (UPPERCASE|LOWERCASE|DIGITS|MINUS)+;

description
	: conjunction (OR_LABEL conjunction)*
	;

conjunction
	:	primary (AND_LABEL primary)*
	|	classIRI THAT_LABEL primary (AND_LABEL primary)*
	;


primary
	:	(NOT_LABEL)? (restriction | atomic)
	;

iri :  'xyz'
//	: FULL_IRI
//	| ABBREVIATED_IRI
//	| SIMPLE_IRI
	;
	
objectPropertyExpression
	:	objectPropertyIRI
	|	inverseObjectProperty
	;

restriction
	: 	objectPropertyExpression SOME_LABEL  primary
	|	objectPropertyExpression ONLY_LABEL primary
	|	objectPropertyExpression VALUE_LABEL individual
	|	objectPropertyExpression SELF_LABEL
	|	objectPropertyExpression MIN_LABEL nonNegativeInteger (primary)?
	|	objectPropertyExpression MAX_LABEL nonNegativeInteger (primary)?
	|	objectPropertyExpression EXACTLY_LABEL nonNegativeInteger (primary)?
	|	dataPropertyExpression SOME_LABEL dataPrimary
	|	dataPropertyExpression ONLY_LABEL dataPrimary
	|	dataPropertyExpression VALUE_LABEL literal
	|	dataPropertyExpression MIN_LABEL nonNegativeInteger (dataPrimary)?
	|	dataPropertyExpression MAX_LABEL nonNegativeInteger (dataPrimary)?
	|	dataPropertyExpression EXACTLY_LABEL nonNegativeInteger (dataPrimary)?
	;

atomic
	:	classIRI
	|	OPEN_CURLY_BRACE individualList CLOSE_CURLY_BRACE
	|	LEFT_PAREN description RIGHT_PAREN
	;

classIRI
	: iri
	;


individualList
	:	individual (COMMA individual)*
	;

individual
	: individualIRI
	| NODE_ID
	;

nonNegativeInteger
	: DIGITS
	;

dataPrimary
	:	NOT_LABEL? dataAtomic
	;

dataAtomic
	:	dataType
	|	OPEN_CURLY_BRACE literalList CLOSE_CURLY_BRACE
	|	dataTypeRestriction
	|	LEFT_PAREN dataRange RIGHT_PAREN
	;

literalList
	:	literal (COMMA literal)*
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
	: quotedstring
	;
stringLiteralWithLanguage
	: quotedstring LANGUAGE_TAG
	;
	
lexicalValue
	: quotedstring
	;

dataPropertyExpression
	:	dataPropertyIRI
	;

dataTypeRestriction
	:	dataType OPEN_SQUARE_BRACE facet restrictionValue  (COMMA facet restrictionValue)* CLOSE_SQUARE_BRACE
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
	: (PLUS | MINUS)? DIGITS DOT DIGITS
	;

integerLiteral
	: (PLUS | MINUS)? DIGITS
	;

floatingPointLiteral
	: (PLUS | MINUS)? ((DIGITS (DOT DIGITS)? EXPONENT?) | DOT DIGITS EXPONENT?)
	;
	
dataRange
	:	dataConjunction (OR_LABEL dataConjunction)*
	;

dataConjunction
	:	dataPrimary (AND_LABEL dataPrimary)*
	;

annotationAnnotatedList
	:	(annotations)? annotation (COMMA (annotations)? annotation)*
	;

annotation
	:	annotationPropertyIRI annotationTarget
	;

annotationTarget
	:	NODE_ID
	|	iri
	|	literal
	;
annotations
	: ANNOTATIONS_LABEL annotationAnnotatedList
	;

descriptionAnnotatedList
	:	annotations? description (COMMA descriptionAnnotatedList)*
	;

description2List
	:	description COMMA descriptionList
	;

descriptionList
	:	description (COMMA description)*
	;

ClassExpression :
	(SUBCLASS_OF_LABEL descriptionAnnotatedList
	|	EQUIVALENT_TO_LABEL descriptionAnnotatedList
	|	DISJOINT_WITH_LABEL descriptionAnnotatedList
	)*

		(objectPropertyExpression | dataPropertyExpression)+?
	;


objectPropertyAxiom
	:	OBJECT_PROPERTY_LABEL objectPropertyIRI
	(	ANNOTATIONS_LABEL annotationAnnotatedList
		|	RANGE_LABEL descriptionAnnotatedList
		|	CHARACTERISTICS_LABEL objectPropertyCharacteristicAnnotatedList
		|	SUB_PROPERTY_OF_LABEL objectPropertyExpressionAnnotatedList
		|	EQUIVALENT_TO_LABEL objectPropertyExpressionAnnotatedList
		|	DISJOINT_WITH_LABEL objectPropertyExpressionAnnotatedList
		|	INVERSE_OF_LABEL objectPropertyExpressionAnnotatedList
		|	SUB_PROPERTY_CHAIN_LABEL annotations objectPropertyExpression (O_LABEL objectPropertyExpression)+
		)*
	;

objectPropertyCharacteristicAnnotatedList
	:	annotations? OBJECT_PROPERTY_CHARACTERISTIC (COMMA objectPropertyCharacteristicAnnotatedList)*
	;


objectPropertyExpressionAnnotatedList
	:	annotations? objectPropertyExpression (COMMA objectPropertyExpressionAnnotatedList)*
	;

dataPropertyAxiom
    : DATA_PROPERTY_LABEL  dataPropertyIRI
    (	ANNOTATIONS_LABEL annotationAnnotatedList
    |	DOMAIN_LABEL  descriptionAnnotatedList
    |	RANGE_LABEL  dataRangeAnnotatedList
    |	CHARACTERISTICS_LABEL  annotations FUNCTIONAL_LABEL
    |	SUB_PROPERTY_OF_LABEL  dataPropertyExpressionAnnotatedList
    |	EQUIVALENT_TO_LABEL  dataPropertyExpressionAnnotatedList
    |	DISJOINT_WITH_LABEL  dataPropertyExpressionAnnotatedList
    )*
    ;

dataRangeAnnotatedList
	:	annotations? dataRange (COMMA dataRangeAnnotatedList)*
	;

dataPropertyExpressionAnnotatedList
	:	annotations? dataPropertyExpression (COMMA dataPropertyExpressionAnnotatedList)*
	;

annotationPropertyAxiom
	:	ANNOTATION_PROPERTY_LABEL annotationPropertyIRI
	(	ANNOTATIONS_LABEL  annotationAnnotatedList )*
	|	DOMAIN_LABEL  iriAnnotatedList
	|	RANGE_LABEL  iriAnnotatedList
	|	SUB_PROPERTY_OF_LABEL annotationPropertyIRIAnnotatedList
	;
	
iriAnnotatedList
	:	annotations? iri (COMMA iriAnnotatedList)*
	;

annotationPropertyIRI
	:	iri
	;

annotationPropertyIRIAnnotatedList
	:	annotations? annotationPropertyIRI (COMMA annotationPropertyIRIAnnotatedList)*
	;

individualAssertion
	:	INDIVIDUAL_LABEL  individual

	;


factAnnotatedList
	:	annotations? fact (COMMA factAnnotatedList)*
	;


individualAnnotatedList
	:	annotations? individual (COMMA individualAnnotatedList)*
	;
fact	:	NOT_LABEL? (objectPropertyFact | dataPropertyFact);

objectPropertyFact
	:	objectPropertyIRI individual
	;

dataPropertyFact
	:	dataPropertyIRI literal
	;

datatypeFrame
	:	DATATYPE_LABEL  dataType
		(ANNOTATIONS_LABEL  annotationAnnotatedList)*
		(EQUIVALENT_TO_LABEL  annotations dataRange)?
		(ANNOTATIONS_LABEL  annotationAnnotatedList)*
	;


	
individual2List
	:	individual COMMA individualList
	;

dataProperty2List
	:	dataProperty COMMA dataPropertyList
	;
	
dataPropertyList
	:	dataProperty (COMMA dataProperty)*
	;

objectProperty2List
	:	objectProperty COMMA objectPropertyList
	;

objectPropertyList
	:	objectProperty (COMMA objectProperty)*
	;

objectProperty
	:	objectPropertyIRI
	;

dataProperty
	:	dataPropertyIRI
	;

dataPropertyIRI
	:	iri
	;

datatypeIRI
	: iri
	;

objectPropertyIRI
	: iri
	;





individualIRI
	: iri
	;

datatypePropertyIRI
	: iri
	;



ontology
	: ONTOLOGY_LABEL ;


ontologyIri
	: iri
	;

versionIri
	:	iri
	;
ws : ( sp | htab | comment)+;
comment : SLASH ASTERISK (DIGITS| UPPERCASE| LOWERCASE)+ ASTERISK SLASH;
sp : SPACE; // space
htab : TAB; // tab
cr : CR; // carriage return
lf : LF; // line feed
quotedstring:
    QUOTE text QUOTE
    ;
text : (UPPERCASE|LOWERCASE|DIGITS|LEFT_PAREN|RIGHT_PAREN|SPACE|MINUS)+;
O_LABEL	:	'o';

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
INVERSE_LABEL
	:	'inverse'
	;

MINUS
    : '-'
    ;

DOT
    : '.'
    ;

PLUS
    : '+'
    ;

DIGITS
    : ('0'..'9')+
    ;

NOT_LABEL
	:	'not'
	;

LESS_EQUAL
    : '<='
    ;

GREATER_EQUAL
    : '>='
    ;

LESS
	: '<'
	;

GREATER
	: '>'
	;

OPEN_CURLY_BRACE
	: '{'
	;

CLOSE_CURLY_BRACE
	: '}'
	;

OR_LABEL
	:	'or'| 'OR' | '|'
	;

AND_LABEL
	:	'and'|'AND'| '&'
	;

SOME_LABEL
	:	'some'| 'SOME'| 'at least one'| 'AT LEAST ONE'
	;

ONLY_LABEL
	:	'only'| 'ONLY'
	;

VALUE_LABEL
	:	'value' | 'VALUE'
	;

SELF_LABEL
	:	'Self'
	;

MIN_LABEL
	:	 'min'| 'MIN'
	;

MAX_LABEL
	:	'max' | 'MAX'
	;

EXACTLY_LABEL
	:	'exactly' | 'EXACTLY'
	;

COMMA	:	',';

LEFT_PAREN
    : '('
    ;

RIGHT_PAREN
    : ')'
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

REFERENCE
	: '^^'
	;


RANGE_LABEL
	:	'Range:'
	;

CHARACTERISTICS_LABEL
	:	'Characteristics:'
	;

SUB_PROPERTY_OF_LABEL
	:	'SubPropertyOf:'
	;

SUB_PROPERTY_CHAIN_LABEL
	:	'SubPropertyChain:'
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

PREFIX_LABEL
	:	'Prefix:'
	;

ONTOLOGY_LABEL
	:	'Ontology:'
	;

INDIVIDUAL_LABEL
	:	'Individual' | 'individual'
	;

TYPES_LABEL
	:	'isType'| 'istype'
	;



SAME_AS_LABEL
	:	'same as'| 'sameAs'| 'sameas'
	;



DATATYPE_LABEL
	:	'DataType'| 'datatype' | 'DATATYPE'
	;



EQUIVALENT_TO_LABEL
	:	'EquivalentTo' | 'equivalentto'| 'equivalent to'
	;

SUBCLASS_OF_LABEL
	:	'SubClassOf'| 'subclassof'| 'subclass of'
	;

DISJOINT_WITH_LABEL
	:	'DisjointWith'| 'disjointwith'| 'disjoint with'
	;


INVERSE_OF_LABEL
	:	'inverseOF'| 'inverseof' | 'inverse of'
	;



ANNOTATIONS_LABEL
	:	'Annotation'| 'annotation'
	;

LABEL_CLASS
	:	'Class'| 'class'
	;

OBJECT_PROPERTY_CHARACTERISTIC
	:	FUNCTIONAL_LABEL
	|	INVERSE_FUNCTIONAL_LABEL
	|	REFLEXIVE_LABEL
	|	IRREFLEXIVE_LABEL
	|	SYMMETRIC_LABEL
	|	ASSYMETRIC_LABEL
	|	TRANSITIVE_LABEL
	;


FUNCTIONAL_LABEL
	:	'Functional'
	;


INVERSE_FUNCTIONAL_LABEL
	:	'InverseFunctional'
	;


REFLEXIVE_LABEL
	:	'Reflexive'
	;

IRREFLEXIVE_LABEL
    : 'Irreflexive'
    ;

SYMMETRIC_LABEL
    :'Symmetric'
    ;

ASSYMETRIC_LABEL
    : 'Assymentric'
    ;
TRANSITIVE_LABEL
    : 'Transitive'
    ;
DOMAIN_LABEL
	:	'Domain:'
	;


fragment
PN_PREFIX
    : (PN_CHARS)*
    ;

fragment
EOL
    : '\n' | '\r'
    ;


fragment
PN_CHARS_BASE
    : 'A'..'Z'
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
PN_CHARS_U
    : PN_CHARS_BASE | '_'
    ;

FULL_IRI
    : LESS (| GREATER | '"' | OPEN_CURLY_BRACE | CLOSE_CURLY_BRACE | '|' | '^' | '\\' | '`' | UPPERCASE| LOWERCASE) GREATER+;
    //{\$this->setTe


NODE_ID
    : '_:'
    ;
fragment
PN_CHARS
    : PN_CHARS_U
    | MINUS
    | DIGITS
    | '\u00B7'
    | '\u0300'..'\u036F'
    | '\u203F'..'\u2040'
    ;

OPEN_SQUARE_BRACE
    : '['
    ;

CLOSE_SQUARE_BRACE
    : ']'
    ;



fragment
ECHAR
    : '\\' ('t' | 'b' | 'n' | 'r' | 'f' | '\\' | '"' | '\'')
    ;


LANGUAGE_TAG
    : '@' (('a'..'z')|('A'..'Z'))+ (MINUS (('a'..'z')('A'..'Z')DIGITS)+)*
    //{\$this->setText(substr(\$this->getText(), 1, strlen(\$this->getText()) - 1)); }
    ;

EXPONENT
	: ('e'|'E') (PLUS | MINUS)? DIGITS
	;

PREFIX_NAME:	PN_PREFIX ':'
	;
ABBREVIATED_IRI
    : PREFIX_NAME SIMPLE_IRI
    ;

SIMPLE_IRI
	: ( PN_CHARS_U) (DOT? PN_CHARS)*
	;
UPPERCASE: [A-Z];
LOWERCASE: [a-z];
TAB : [\t];
LF : [\n];
CR : [\r];
SPACE : ' ';
EXCLAMATION : '!';
QUOTE : '"';
POUND : '#';
DOLLAR : '$';
PERCENT : '%';
COLON : ':';
APOSTROPHE : '\'';
ASTERISK : '*';
SLASH : '/';
ZERO : '0';
ONE : '1';
TWO : '2';
THREE : '3';
FOUR : '4';
FIVE : '5';
SIX : '6';
SEVEN : '7';
EIGHT : '8';
NINE : '9';
NAME: 'Name'|'name';
DESCRIPTION: 'Description' | 'description';
CODE :'Code'|'code';
SCHEME: 'CodeScheme'|'Scheme'| 'codescheme'| 'code scheme'| 'scheme';
