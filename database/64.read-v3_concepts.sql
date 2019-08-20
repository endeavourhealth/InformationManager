-- Build work table
DROP TABLE IF EXISTS read_v3_current;
CREATE TABLE read_v3_current
SELECT
    c.code, ifnull(term_62, term_31) as name, term as description
FROM read_v3_concept c
         JOIN read_v3_desc d ON d.code = c.code AND d.type = 'P'
         JOIN read_v3_terms t ON t.termId = d.termId AND t.status = 'C'
WHERE c.status = 'C';

-- Common/useful concepts
SELECT @subtype := dbid FROM concept WHERE id = 'is_subtype_of';
SELECT @codescheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @prefix := dbid FROM concept WHERE id = 'code_prefix';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @parent := dbid FROM concept WHERE id = 'has_parent';        -- TODO: Replace with "is_a"?

-- Create DOCUMENT
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/CTV3', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- code scheme
INSERT INTO concept(document, id, name, description)
VALUES (@doc, 'CTV3', 'READ 3', 'The READ (CTV) 3 code scheme');
SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_property_object (dbid, property, value)
VALUES (@scheme, @subtype, @codescheme);

INSERT INTO concept_property_data (dbid, property, value)
VALUES (@scheme, @prefix, 'R3_');

-- Concepts
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('R3_',code), if(length(name) > 60, concat(left(name, 57), '...'), name), ifnull(description, name), @scheme, code
FROM read_v3_current;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM read_v3_current r
JOIN concept c ON c.id = concat('R3_',r.code);

-- Relationships
INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @parent, v.dbid
FROM read_v3_hier rel
JOIN read_v3_concept p ON p.code = rel.parent AND p.status = 'C'   -- Exclude "Optional"s
JOIN concept c ON c.id = concat('R3_', rel.code)
JOIN concept v ON v.id = concat('R3_', rel.parent);
