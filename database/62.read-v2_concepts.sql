-- Create DOCUMENT
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/READ2', '1.0.0');

SET @doc = LAST_INSERT_ID();

INSERT INTO concept (document, id, name, description)
VALUES (@doc, 'READ2','READ 2', 'The READ2 code scheme');

SET @scheme = LAST_INSERT_ID();

SELECT @subtype := dbid FROM concept WHERE id = 'is_subtype_of';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @codescheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @prefix := dbid FROM concept WHERE id = 'code_prefix';
SELECT @parent := dbid FROM concept WHERE id = 'has_parent';    -- TODO: Migrate to "is_a"?

INSERT INTO concept_property_object (dbid, property, value)
VALUES (@scheme, @subtype, @codescheme);

INSERT INTO concept_property_data (dbid, property, value)
VALUES (@scheme, @prefix, 'R2_');

-- CONCEPTS
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('R2_',code), if(length(term) > 255, concat(left(term, 252), '...'), term), term, @scheme, code
FROM read_v2;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM read_v2 v
JOIN concept c ON c.id = concat('R2_', v.code);

-- ATTRIBUTES
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @parent, p.dbid
FROM read_v2 r
JOIN concept c ON c.id = concat('R2_', r.code)
JOIN concept p ON p.id = concat('R2_', INSERT(r.code, INSTR(r.code, '.')-1, 1, '.'));
