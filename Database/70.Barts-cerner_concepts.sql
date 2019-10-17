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

-- Create MODEL
INSERT INTO model (iri, version)
VALUES ('InformationModel/dm/BartsCerner', '1.0.0');
SET @model = LAST_INSERT_ID();

-- Code scheme
INSERT INTO concept(model, data)
VALUES
(@model, JSON_OBJECT('status', 'CoreActive', 'id', 'BartsCerner', 'name', 'Barts/Cerner', 'description', 'The Barts/Cerner code scheme'));
SET @scheme = LAST_INSERT_ID();


INSERT INTO concept_definition (concept, data)
VALUES (@scheme, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', JSON_ARRAY('CodeScheme')))
    ));

SELECT @bcstrt := MAX(dbid) FROM concept;

-- Barts/Cerner CONCEPTS
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',
        'id', concat('BC_',code),
        'name', if(length(term) > 255, concat(left(term, 252), '...'), term),
        'description', term,
        'codeScheme', 'BartsCerner',
        'code', code
    )
FROM barts_cerner;

INSERT INTO concept_definition (concept, data)
SELECT DISTINCT dbid, JSON_OBJECT(
        'status', 'CoreActive',
        'subtypeOf', JSON_ARRAY(JSON_OBJECT('concept', 'CodeableConcept'))
    )
FROM barts_cerner b
JOIN concept c ON c.id = concat('BC_',b.code);

-- CREATE CORE EXPRESSION CONCEPTS
INSERT INTO concept (model, data)
SELECT @model, JSON_OBJECT(
        'status', 'CoreActive',                                                     -- TODO: LegacyActive?
        'id', CONCAT('DS_BC_', dbid),
        'name', if(length(term) > 255, concat(left(term, 252), '...'), term),
        'description', term
    )
FROM barts_cerner_snomed_expressions;

INSERT INTO concept_definition (concept, data)
SELECT c.dbid,
       CONCAT(
               '{ "status": "coreActive", "subtypeOf": [ { "concept": "codeableConcept"},',
               '{"operation": "AND", "concept" : "SN_',
               REPLACE(expression, '+', '"}, {"operation": "AND", "concept" : "SN_'),
               '"}]}'
           )
FROM barts_cerner_snomed_expressions b
         JOIN concept c ON c.id = concat('DS_BC_',b.dbid)
WHERE expression LIKE '%+%';


INSERT INTO concept_definition (concept, data)
SELECT c.dbid,
       CONCAT(
               '{"status": "coreActive", "subtypeOf": [ { "concept": "SN_',
               LEFT(expression, INSTR(expression,':')-1),
               '"}, {"operator": "AND", "attribute": ',
               '{"property": "SN_',
               REPLACE(REPLACE(SUBSTR(expression, INSTR(expression, ':')+1), ':', '"}}}, {"operator": "AND", "attribute": {"property":"SN_'), '=', '", "valueConcept" : { "concept" : "SN_'),
               '"}}}]}'
           )
FROM barts_cerner_snomed_expressions b
         JOIN concept c ON c.id = concat('DS_BC_',b.dbid)
WHERE expression LIKE '%=%';

