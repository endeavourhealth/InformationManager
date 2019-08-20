-- Common/useful concepts
SELECT @subtype := dbid FROM concept WHERE id = 'is_subtype_of';    -- TODO: Replace with "is_a"?
SELECT @codeScheme := dbid FROM concept WHERE id = 'CodeScheme';
SELECT @prefix := dbid FROM concept WHERE id = 'code_prefix';
SELECT @codeable := dbid FROM concept WHERE id = 'CodeableConcept';

-- Create document
INSERT INTO document (path, version)
VALUES ('InformationModel/dm/ICD10', '1.0.0');

SET @doc = LAST_INSERT_ID();

-- Code scheme
INSERT INTO concept (document, id, name, description)
VALUES (@doc, 'ICD10', 'ICD10', 'The ICD10 code scheme');
SET @scheme = LAST_INSERT_ID();

INSERT INTO concept_property_object (dbid, property, value)
VALUES (@scheme, @subtype, @codeScheme);

INSERT INTO concept_property_data (dbid, property, value)
VALUES (@scheme, @prefix, 'I10_');

-- Core concepts
INSERT INTO concept (document, id, name) VALUES (@doc, 'I10_modifier4', 'IDC10 4th character modifier suffix');
SET @mod4 = LAST_INSERT_ID();

INSERT INTO concept (document, id, name) VALUES (@doc, 'I10_modifier5', 'IDC10 5th character modifier suffix');
SET @mod5 = LAST_INSERT_ID();

INSERT INTO concept (document, id, name) VALUES (@doc, 'I10_qualifiers', 'IDC10 dual classification (asterisk codes)');
SET @qual = LAST_INSERT_ID();

INSERT INTO concept (document, id, name) VALUES (@doc, 'I10_gender_mask', 'IDC10 gender mask');
SET @mask = LAST_INSERT_ID();

INSERT INTO concept (document, id, name) VALUES (@doc, 'I10_min_age', 'IDC10 minimum age');
SET @minage = LAST_INSERT_ID();

INSERT INTO concept (document, id, name) VALUES (@doc, 'I10_max_age', 'IDC10 maximum age');
SET @maxage = LAST_INSERT_ID();

INSERT INTO concept (document, id, name) VALUES (@doc, 'I10_tree_description', 'IDC10 tree description');
SET @tree = LAST_INSERT_ID();

INSERT INTO concept_property_object (dbid, property, value)
VALUES (@mod4, @subtype, @codeable),
       (@mod5, @subtype, @codeable),
       (@qual, @subtype, @codeable),
       (@mask, @subtype, @codeable),
       (@minage, @subtype, @codeable),
       (@maxage, @subtype, @codeable),
       (@tree, @subtype, @codeable);

-- CONCEPTS
INSERT INTO concept (document, id, name, description, scheme, code)
SELECT @doc, concat('I10_', code), if(length(description) > 255, concat(left(description, 252), '...'), description), description, @scheme, code
FROM icd10;

INSERT INTO concept_property_data (dbid, property, value)
SELECT c.dbid, @subtype, @codeable
FROM icd10 i
JOIN concept c ON c.id = concat('I10_', i.code)
;

INSERT INTO concept_property_data (dbid, property, value)
SELECT c.dbid, @mod4, modifier_4
FROM icd10 i
JOIN concept c ON c.id = concat('I10_', i.code)
WHERE modifier_4 IS NOT NULL;

INSERT INTO concept_property_data (dbid, property, value)
SELECT c.dbid, @mod5, modifier_5
FROM icd10 i
JOIN concept c ON c.id = concat('I10_', i.code)
WHERE modifier_5 IS NOT NULL;

INSERT INTO concept_property_data (dbid, property, value)
SELECT c.dbid, @mask, gender_mask
FROM icd10 i
JOIN concept c ON c.id = concat('I10_', i.code)
WHERE gender_mask IS NOT NULL;

INSERT INTO concept_property_data (dbid, property, value)
SELECT c.dbid, @minage, min_age
FROM icd10 i
JOIN concept c ON c.id = concat('I10_', i.code)
WHERE min_age IS NOT NULL;

INSERT INTO concept_property_data (dbid, property, value)
SELECT c.dbid, @maxage, max_age
FROM icd10 i
JOIN concept c ON c.id = concat('I10_', i.code)
WHERE max_age IS NOT NULL;

INSERT INTO concept_property_data (dbid, property, value)
SELECT c.dbid, @tree, tree_description
FROM icd10 i
JOIN concept c ON c.id = concat('I10_', i.code)
WHERE tree_description IS NOT NULL;


INSERT INTO concept_property_object (dbid, property, value)
SELECT c.dbid, @qual, v.dbid
FROM icd10 i
JOIN icd10 q ON (i.qualifiers = q.code OR i.qualifiers LIKE CONCAT(q.code, '^%') OR i.qualifiers LIKE CONCAT('%^', q.code) OR INSTR(i.qualifiers, CONCAT('^', q.code, '^')) > 0)
JOIN concept c ON c.id = concat('I10_', i.code)
JOIN concept v ON v.id = concat('I10_', q.code)
WHERE i.qualifiers IS NOT NULL;
