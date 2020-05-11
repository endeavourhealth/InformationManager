-- Common/useful ids
SELECT @ns := id FROM namespace WHERE prefix = ':';
SELECT @scheme := id FROM concept WHERE iri = ':BartCernerCode';

-- CONCEPTS
INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition, status, origin)
SELECT @ns, CONCAT(':BC_', code), IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term), term, @scheme, code, '{}', 1, 1
FROM barts_cerner;

-- CREATE CORE EXPRESSION CONCEPTS
/*

INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition, status, origin)
SELECT @ns, CONCAT(':BC_', code), IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term), term, @scheme, code, '{}', 1, 1
FROM barts_cerner_snomed_expressions;

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

*/
