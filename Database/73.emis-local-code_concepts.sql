-- Common/useful concepts
SELECT @isA := dbid FROM concept WHERE id = 'isA';
SELECT @codescheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @prefix := dbid FROM concept WHERE id = 'codePrefix';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';
SELECT @synonym := dbid FROM concept WHERE id = 'hasSynonym';

INSERT INTO document
(path, version, draft)
VALUES
('InformationModel/dm/EmisLocal', '1.0.0', 1);

SET @doc:=LAST_INSERT_ID();

-- Code scheme
INSERT INTO concept (document, id, name, description)
VALUES (@doc, 'EMIS_LOCAL', 'EMIS Local', 'EMIS local codes scheme');

SET @scheme := LAST_INSERT_ID();

INSERT INTO concept_property (dbid, property, concept)
VALUES (@scheme, @isA, @codescheme);

INSERT INTO concept_property (dbid, property, value)
VALUES (@scheme, @prefix, 'EMLOC_');

-- Concepts
INSERT INTO concept (document, id, name, scheme, code)
SELECT @doc, CONCAT('EMLOC_', local_code), local_term, @scheme, local_code
FROM emis_local_codes
GROUP BY local_code;

INSERT INTO concept_property (dbid, property, concept)
SELECT c.dbid, @isA, @codeable
FROM emis_local_codes e
JOIN concept c ON c.id = CONCAT('EMLOC_', local_code)
GROUP BY local_code;

INSERT INTO concept_property (dbid, property, value)
SELECT c.dbid, @synonym, e.local_term
FROM emis_local_codes e
JOIN concept c ON c.id = CONCAT('EMLOC_', local_code) AND c.name <> e.local_term;
