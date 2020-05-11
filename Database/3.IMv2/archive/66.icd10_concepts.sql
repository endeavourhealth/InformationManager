-- Common/useful ids
SELECT @ns := id FROM namespace WHERE prefix = ':';
SELECT @scheme := id FROM concept WHERE iri = ':ICD10';

-- Core concepts
INSERT INTO concept
(namespace, iri, name, description, definition, status, origin)
VALUES
(@ns, ':I10_Modifier4', 'ICD10 Modifier 4', 'IDC10 4th character modifier suffix', '{}', 1, 1),
(@ns, ':I10_Modifier5', 'ICD10 Modifier 5', 'IDC10 5th character modifier suffix', '{}', 1, 1),
(@ns, ':I10_Qualifiers', 'ICD10 Modifier qualifiers', 'IDC10 dual classification (asterisk codes)', '{}', 1, 1),
(@ns, ':I10_GenderMask', 'ICD10 Gender mask', 'IDC10 gender mask', '{}', 1, 1),
(@ns, ':I10_MinAge', 'IDC10 minimum age', 'IDC10 minimum age', '{}', 1, 1),
(@ns, ':I10_MaxAge', 'IDC10 maximum age', 'IDC10 maximum age', '{}', 1, 1),
(@ns, ':I10_TreeDescription', 'IDC10 tree description', 'IDC10 tree description', '{}', 1, 1);

-- CONCEPTS
INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition, status, origin)
SELECT @ns, CONCAT(':I10_', code), IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description), description, @scheme, code, '{}', 1, 1
FROM icd10;

-- Modifier 4
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, modifier_4
FROM icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_Modifier4'
WHERE modifier_4 IS NOT NULL;

-- Modifier 5
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, modifier_5
FROM icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_Modifier5'
WHERE modifier_5 IS NOT NULL;

-- Gender mask
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, gender_mask
FROM icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_GenderMask'
WHERE gender_mask IS NOT NULL;

-- Min age
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, min_age
FROM icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_MinAge'
WHERE min_age IS NOT NULL;

-- Max age
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, max_age
FROM icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_MaxAge'
WHERE max_age IS NOT NULL;

-- Tree description
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, tree_description
FROM icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_TreeDescription'
WHERE tree_description IS NOT NULL;

-- Qualifiers
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, qualifiers
FROM icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_Qualifiers'
WHERE qualifiers IS NOT NULL;
