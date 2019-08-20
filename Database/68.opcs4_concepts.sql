-- Useful/common concepts
SELECT @subtype := dbid FROM concept WHERE id = 'is_subtype_of';
SELECT @codescheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @prefix := dbid FROM concept WHERE id = 'code_prefix';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';

-- Create document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/OPCS4', '1.0.0');
SET @doc = LAST_INSERT_ID();

-- Code scheme
INSERT INTO concept (document, id, name, description)
VALUES (@doc, 'OPCS4', 'OPCS4', 'The OPCS4 code scheme');
SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_property_object (dbid, property, value)
VALUES (@scheme, @subtype, @codescheme);

INSERT INTO concept_property_data (dbid, property, value)
VALUES (@scheme, @prefix, 'O4_');

-- CONCEPTS
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('O4_',code), if(length(description) > 255, concat(left(description, 252), '...'), description), description, @scheme, code
FROM opcs4;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM opcs4 o
JOIN concept c ON c.id = concat('O4_',o.code);
