-- Common/useful concepts
SELECT @subtype := dbid FROM concept WHERE id = 'is_subtype_of';
SELECT @codescheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @prefix := dbid FROM concept WHERE id = 'code_prefix';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @synonym := dbid FROM concept WHERE id = 'has_synonym';

INSERT INTO document
(path, version, draft)
VALUES
('InformationModel/dm/EmisLocal', '1.0.0', 1);

SET @doc:=LAST_INSERT_ID();

-- Code scheme
INSERT INTO concept (document, id, name, description)
VALUES (@doc, 'EMIS_LOCAL', 'EMIS Local', 'EMIS local codes scheme');

SET @scheme := LAST_INSERT_ID();

INSERT INTO concept_property_object (dbid, property, value)
VALUES (@scheme, @subtype, @codescheme);

INSERT INTO concept_property_data (dbid, property, value)
VALUES (@scheme, @prefix, 'EMLOC_');

-- Concepts
INSERT INTO concept (document, id, name, scheme, code)
SELECT @doc, CONCAT('EMLOC_', local_code), local_term, @scheme, local_code
FROM emis_local_codes
GROUP BY local_code;

INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM emis_local_codes e
JOIN concept c ON c.id = CONCAT('EMLOC_', local_code)
GROUP BY local_code;

INSERT INTO concept_property_data (dbid, property, value)
SELECT c.dbid, @synonym, e.local_term
FROM emis_local_codes e
JOIN concept c ON c.id = CONCAT('EMLOC_', local_code) AND c.name <> e.local_term;
