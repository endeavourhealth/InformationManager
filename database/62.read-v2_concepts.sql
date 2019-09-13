-- Create DOCUMENT
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/READ2', '1.0.0');

SET @doc = LAST_INSERT_ID();

INSERT INTO concept (document, id, name, description)
VALUES (@doc, 'READ2','READ 2', 'The READ2 code scheme');

SET @scheme = LAST_INSERT_ID();

SELECT @isA := dbid FROM concept WHERE id = 'isA';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @codescheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @prefix := dbid FROM concept WHERE id = 'codePrefix';

INSERT INTO concept_property (dbid, property, concept)
VALUES (@scheme, @isA, @codescheme);

INSERT INTO concept_property (dbid, property, value)
VALUES (@scheme, @prefix, 'R2_');

-- CONCEPTS
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('R2_',code), if(length(term) > 255, concat(left(term, 252), '...'), term), term, @scheme, code
FROM read_v2;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, @codeable
FROM read_v2 v
JOIN concept c ON c.id = concat('R2_', v.code);

-- ATTRIBUTES
INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, p.dbid
FROM read_v2 r
JOIN concept c ON c.id = concat('R2_', r.code)
JOIN concept p ON p.id = concat('R2_', INSERT(r.code, INSTR(r.code, '.')-1, 1, '.'));
