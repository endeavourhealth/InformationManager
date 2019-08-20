-- Build worktable
DROP TABLE IF EXISTS barts_cerner_snomed_expressions;
CREATE TABLE barts_cerner_snomed_expressions (
    dbid INT AUTO_INCREMENT,
    expression VARCHAR(50) NOT NULL,
    term VARCHAR(250) NOT NULL,
    PRIMARY KEY barts_cerner_snomed_expressions_pk (dbid),
    UNIQUE INDEX barts_cerner_snomed_expressions_exp (expression)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO barts_cerner_snomed_expressions (expression, term)
SELECT DISTINCT snomed_expression, snomed_term
FROM barts_cerner
WHERE snomed_expression LIKE '%:%'
OR snomed_expression LIKE '%+%';

-- Useful/common concepts
SELECT @subtype := dbid FROM concept WHERE id = 'is_subtype_of';
SELECT @codescheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @prefix := dbid FROM concept WHERE id = 'code_prefix';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @equiv := dbid FROM concept WHERE id = 'is_equivalent_to';
SELECT @isa := dbid FROM concept WHERE id = 'SN_116680003';

-- Create document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/BartsCerner', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- CODE SCHEME
INSERT INTO concept (document, id, name, description)
VALUES (@doc, 'BartsCerner', 'Barts/Cerner', 'The Barts/Cerner code scheme');
SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_property_object (dbid, property, value)
VALUES (@scheme, @subtype, @codescheme);

INSERT INTO concept_property_data (dbid, property, value)
VALUES (@scheme, @prefix, 'BC_');


SELECT @bcstrt := MAX(dbid) FROM concept;

-- Barts/Cerner CONCEPTS
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('BC_',code), if(length(term) > 255, concat(left(term, 252), '...'), term), term, @scheme, code
FROM barts_cerner;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM barts_cerner b
JOIN concept c ON c.id = concat('BC_',b.code);

-- ADD DIRECT (1:1) MAPS
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @equiv, v.dbid
FROM barts_cerner bc
JOIN concept c ON c.id = CONCAT('BC_', bc.code)
JOIN concept v ON v.id = CONCAT('SN_', bc.snomed_expression);

-- CREATE CORE EXPRESSION CONCEPTS
INSERT INTO concept (document, id, name, description)
SELECT @doc, CONCAT('DS_BC_', dbid), IF(LENGTH(term) > 255, CONCAT(LEFT(term, 252), '...'), term), term
FROM barts_cerner_snomed_expressions;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM barts_cerner_snomed_expressions e
JOIN concept c ON c.id = CONCAT('DS_BC_', e.dbid);

-- Map to expression proxies
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @equiv, v.dbid
FROM barts_cerner b
JOIN concept c ON c.id = CONCAT('BC_', b.code)
JOIN barts_cerner_snomed_expressions e ON e.expression = b.snomed_expression
JOIN concept v ON v.id = CONCAT('DS_BC_', e.dbid);

-- Combined (+) proxy concepts (2 x is_a)
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @isa, v.dbid
FROM barts_cerner_snomed_expressions e
JOIN concept c ON c.id = CONCAT('DS_BC_', e.dbid)
JOIN concept v ON v.id = CONCAT('SN_', LEFT(expression, INSTR(expression, '+')-1));

UPDATE barts_cerner_snomed_expressions
SET expression = CONCAT(SUBSTR(expression, INSTR(expression, '+')+1), '+')
WHERE expression LIKE '%+%';

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @isa, v.dbid
FROM barts_cerner_snomed_expressions e
JOIN concept c ON c.id = CONCAT('DS_BC_', e.dbid)
JOIN concept v ON v.id = CONCAT('SN_', LEFT(expression, INSTR(expression, '+')-1));

-- Expression (p=v) proxy concepts
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, p.dbid, v.dbid
FROM barts_cerner_snomed_expressions e
JOIN concept c ON c.id = CONCAT('DS_BC_', e.dbid)
JOIN concept p ON INSTR(e.expression, CONCAT(':', p.code, '=')) > 0 AND p.scheme = 81
JOIN concept v ON INSTR(CONCAT(e.expression, ':'), CONCAT(':', p.code, '=', v.code, ':')) > 0 AND v.scheme = 81;
