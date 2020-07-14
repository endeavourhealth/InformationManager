USE im_next;

-- USEFUL VALUES
SELECT @scheme := id FROM concept WHERE iri = ':CM_DiscoveryCode';
SELECT @namespace := id FROM namespace WHERE prefix = ':';
SELECT @child_of := id FROM concept WHERE iri = ':CM_isChildCodeOf';

-- !!! DATA FIXES !!! --
-- FIX TPP LOCAL SCHEME!!!
UPDATE concept SET name = 'TPP local code' WHERE iri = ':CM_TPPLocalCode';

-- Inject vision local code scheme
INSERT IGNORE INTO concept (namespace, iri, name, description, scheme, code, definition)
VALUES (@namespace, ':CM_VisionLocalCode', 'Vision local code', 'Vision local code', @scheme, 'CM_VisionLocalCode', '{}');

-- !!! POPULATE VIEWER TABLES FROM META !!!


-- ******************** SNOMED ********************
-- Concepts
SELECT @scheme := id FROM concept WHERE iri = ':CM_Snomed-CT';

INSERT IGNORE INTO concept
(namespace, iri, name, description, scheme, code)
SELECT @namespace, CONCAT(':SN_', id), IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term), term, @scheme, id
FROM im_source.snomed_description_filtered;

SELECT @is_a := id FROM concept WHERE iri = ':SN_116680003';

-- Hierarchy
INSERT INTO concept_property_object
(concept, property, object, `group`)
SELECT s.id, @is_a, d.id, r.relationshipGroup
FROM im_source.snomed_relationship r
JOIN concept s ON s.iri = CONCAT(':SN_', r.sourceId)
JOIN concept d ON d.iri = CONCAT(':SN_', r.destinationId)
WHERE r.typeId = 116680003
AND r.active = 1;

-- Replacements
INSERT INTO concept_property_object
(concept, property, object)
SELECT c.id, @is_a, r.id
FROM im_source.snomed_history h
JOIN concept c ON c.iri = concat(':SN_', h.oldConceptId)
JOIN concept r ON r.iri = concat(':SN_', h.newConceptId)
GROUP BY h.oldConceptId;

-- Definitions
UPDATE concept c
JOIN im_source.snomed_description_filtered r ON c.iri = CONCAT(':SN_', r.id)
JOIN concept_property_object cpo ON cpo.concept = c.id AND cpo.property = @is_a
JOIN concept o ON o.id = cpo.object
SET c.definition = JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri))
;
-- ******************** READ 2 ********************
-- Concepts
SELECT @scheme := id FROM concept WHERE iri = ':CM_Read2';

INSERT INTO concept
(namespace, iri, name, description, code, scheme, definition)
SELECT @namespace, CONCAT(':R2_', code), term, term, code, @scheme, '{}'
FROM im_source.read_v2;

-- Hierarchy
INSERT INTO concept_property_object 
(concept, property, object)
SELECT p.id, @child_of, c.id
FROM im_source.read_v2 r
JOIN concept p ON p.iri = CONCAT(':R2_', r.code)
JOIN concept c ON c.iri = CONCAT(':R2_', LEFT(r.code, LENGTH(REPLACE(r.code, '.', ''))-1), REPEAT('.', 6 - LENGTH(REPLACE(r.code, '.', ''))))
WHERE r.code <> '.....';

-- Maps
INSERT INTO concept_property_object 
(concept, property, object)
SELECT r.id, @is_a, s.id
FROM im_source.read_v2_snomed_map m
JOIN concept r ON r.iri = CONCAT(':R2_',
              CASE termCode
                  WHEN '00' THEN
                      readCode
                  ELSE
                      IF (LENGTH(REPLACE(readCode, '.', '')) > 2,
                          CONCAT(
                                  REPLACE(readCode, '.', ''),
                                  '-',
                                  IF (SUBSTRING(termCode, 1, 1) ='1', SUBSTRING(termCode, 2, 1), termCode)
                              ),
                          CONCAT(
                                  REPLACE(readCode, '.', ''),
                                  '-',
                                  IF (SUBSTRING(termCode, 1, 1) = '1', SUBSTRING(termCode, 2, 1), termCode),
                                  REPEAT('.', 3-LENGTH(REPLACE(readCode, '.', '')))
                              )
                          )
                  END)
JOIN concept s ON s.iri = CONCAT(':SN_', conceptId);

-- Definition
UPDATE concept c
JOIN im_source.read_v2 r ON c.iri = CONCAT(':R2_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.id AND cpo.property = @is_a
JOIN concept o ON o.id = cpo.object
SET c.definition = JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri))
;

-- ******************** CTV3 ********************
-- Concepts
SELECT @scheme := id FROM concept WHERE iri = ':CM_CTV3';

INSERT INTO concept
(namespace, iri, name, description, code, scheme, definition)
SELECT @namespace, CONCAT(':CTV3_', code), IF(LENGTH(name) > 250, CONCAT(LEFT(name, 247), '...'), name), ifnull(description, name), code, @scheme, '{}'
FROM im_source.read_v3_summary;

-- Hierarchy
INSERT INTO concept_property_object
(concept, property, object)
SELECT c.id, @child_of, p.id
FROM im_source.read_v3_hier h
JOIN concept c ON c.iri = CONCAT(':CTV3_', h.code)
JOIN concept p ON p.iri = CONCAT(':CTV3_', h.parent);

-- Maps (1:1)
INSERT INTO concept_property_object
(concept, property, object)
SELECT c.id, @is_a, v.id
FROM im_source.read_v3_map_summary s
JOIN im_source.read_v3_map_tmp t ON t.ctv3Concept = s.ctv3Concept
JOIN concept c ON c.iri = CONCAT(':CTV3_', s.ctv3Concept)
JOIN concept v ON v.iri = CONCAT(':SN_', t.conceptId)
WHERE s.multi = FALSE;

-- Maps (1:n)
INSERT INTO concept_property_object
(concept, property, object)
SELECT c.id, @is_a, v.id
FROM im_source.read_v3_map_summary s
JOIN concept c ON c.iri = CONCAT(':CTV3_', s.ctv3Concept)
JOIN concept v ON v.iri = CONCAT(':SN_', s.altConceptId)
WHERE s.multi = TRUE
  AND s.altConceptID IS NOT NULL;

-- Definition
UPDATE concept c
JOIN im_source.read_v3_summary r ON c.iri = CONCAT(':CTV3_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.id AND cpo.property = @is_a
JOIN concept o ON o.id = cpo.object
SET c.definition = JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri))
;

-- ******************** ICD10 ********************
-- Concepts
SELECT @scheme := id FROM concept WHERE iri = ':CM_ICD10';

INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition)
SELECT @namespace, CONCAT(':I10_', code), IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description), description, @scheme, code, '{}'
FROM im_source.icd10;

-- Additional concepts
INSERT INTO concept
(namespace, iri, name, description, code, scheme, definition)
VALUES
(@namespace, ':I10_Modifier4', 'ICD10 Modifier 4', 'IDC10 4th character modifier suffix', 'Modifier4', @scheme, '{}'),
(@namespace, ':I10_Modifier5', 'ICD10 Modifier 5', 'IDC10 5th character modifier suffix', 'Modifier5', @scheme, '{}'),
(@namespace, ':I10_Qualifiers', 'ICD10 Modifier qualifiers', 'IDC10 dual classification (asterisk codes)', 'Qualifiers', @scheme, '{}'),
(@namespace, ':I10_GenderMask', 'ICD10 Gender mask', 'IDC10 gender mask', 'GenderMask', @scheme, '{}'),
(@namespace, ':I10_MinAge', 'IDC10 minimum age', 'IDC10 minimum age', 'MinAge', @scheme, '{}'),
(@namespace, ':I10_MaxAge', 'IDC10 maximum age', 'IDC10 maximum age', 'MaxAge', @scheme, '{}'),
(@namespace, ':I10_TreeDescription', 'IDC10 tree description', 'IDC10 tree description', 'TreeDescription', @scheme, '{}');

-- Modifier 4
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, modifier_4
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_Modifier4'
WHERE modifier_4 IS NOT NULL;

-- Modifier 5
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, modifier_5
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_Modifier5'
WHERE modifier_5 IS NOT NULL;

-- Gender mask
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, gender_mask
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_GenderMask'
WHERE gender_mask IS NOT NULL;

-- Min age
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, min_age
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_MinAge'
WHERE min_age IS NOT NULL;

-- Max age
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, max_age
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_MaxAge'
WHERE max_age IS NOT NULL;

-- Tree description
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, tree_description
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_TreeDescription'
WHERE tree_description IS NOT NULL;

-- Qualifiers
INSERT INTO concept_property_data (concept, property, data)
SELECT c.id, p.id, qualifiers
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_Qualifiers'
WHERE qualifiers IS NOT NULL;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT DISTINCT c.id, @is_a, o.id
FROM im_source.icd10_opcs4_maps m
JOIN im_source.icd10 i ON i.alt_code = m.mapTarget
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept o ON o.iri = CONCAT(':SN_', m.referencedComponentId)
WHERE m.refsetId = 999002271000000101
AND m.active = 1
ORDER BY i.code;

-- Definition
UPDATE concept c
JOIN im_source.icd10 r ON c.iri = CONCAT(':I10_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.id AND cpo.property = @is_a
JOIN concept o ON o.id = cpo.object
SET c.definition = JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri))
;
-- ******************** OPCS4 ********************
-- Concepts
SELECT @scheme := id FROM concept WHERE iri = ':CM_OPCS4';

INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition)
SELECT @namespace, CONCAT(':O4_', code), IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description), description, @scheme, code, '{}'
FROM im_source.opcs4;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT DISTINCT c.id, @is_a, p.id
FROM im_source.icd10_opcs4_maps m
JOIN im_source.opcs4 o ON o.altCode = m.mapTarget
JOIN concept c ON c.iri = CONCAT(':O4_', o.code)
JOIN concept p ON p.iri = CONCAT(':SN_', m.referencedComponentId)
WHERE m.refsetId = 999002741000000101
AND m.active = 1
ORDER BY o.code;

-- Definition
UPDATE concept c
JOIN im_source.opcs4 r ON c.iri = CONCAT(':O4_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.id AND cpo.property = @is_a
JOIN concept o ON o.id = cpo.object
SET c.definition = JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri))
;

-- ******************** BARTS ********************
-- Concepts
SELECT @scheme := id FROM concept WHERE iri = ':CM_BartCernerCode';

INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition)
SELECT @namespace, CONCAT(':BC_', code), term, term, @scheme, code, '{}'
FROM im_source.barts_cerner;

INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition)
SELECT DISTINCT @namespace, CONCAT(':CM_BC_', e.dbid), e.term, e.term, @scheme, CONCAT('BC_', e.dbid), '{}'
FROM im_source.barts_cerner b
LEFT JOIN im_source.barts_cerner_snomed_expressions e ON e.expression = b.snomed_expression
WHERE snomed_expression IS NOT NULL
AND e.dbid IS NOT NULL;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT c.id, @is_a, o.id
FROM im_source.barts_cerner b
LEFT JOIN im_source.barts_cerner_snomed_expressions e ON e.expression = b.snomed_expression
JOIN concept c ON c.iri = CONCAT(':BC_', b.code)
JOIN concept o ON o.iri = IF(e.dbid IS NULL, CONCAT(':SN_', snomed_expression), CONCAT(':CM_BC_', e.dbid))
WHERE snomed_expression IS NOT NULL;

-- Definition
UPDATE concept c
JOIN im_source.barts_cerner r ON c.iri = CONCAT(':BC_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.id AND cpo.property = @is_a
JOIN concept o ON o.id = cpo.object
SET c.definition = JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri))
;

-- ******************** EMIS LOCAL ********************
-- Concepts
SELECT @scheme := id FROM concept WHERE iri = ':CM_EMISLocal';

INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition)
SELECT DISTINCT @namespace, CONCAT(':EMLOC_', e.readTermId), e.codeTerm, e.codeTerm, @scheme, e.readTermId, '{}'
FROM im_source.emis_read_snomed e
LEFT JOIN im_source.read_v2 r ON r.code = IF(LENGTH(e.readTermId)<5, CONCAT(e.readTermId, REPEAT('.', 5-LENGTH(e.readTermId))), e.readTermId)
WHERE r.code IS NULL
GROUP BY readTermId;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT DISTINCT c.id, @is_a, o.id
FROM im_source.emis_read_snomed e
JOIN concept c ON c.iri = CONCAT(':EMLOC_', e.readTermId)
JOIN concept o ON o.iri = CONCAT(':SN_', snomedCTConceptId);

-- Definition
UPDATE concept c
JOIN im_source.emis_read_snomed r ON c.iri = CONCAT(':EMLOC_', r.readTermId)
JOIN concept_property_object cpo ON cpo.concept = c.id AND cpo.property = @is_a
JOIN concept o ON o.id = cpo.object
SET c.definition = JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri))
;

-- ******************** TPP LOCAL ********************
-- Concepts
SELECT @scheme := id FROM concept WHERE iri = ':CM_TPPLocalCode';

INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition)
SELECT DISTINCT @namespace, CONCAT(':TPPLOC_', t.ctv3Code), t.ctv3Text, t.ctv3Text, @scheme, t.ctv3Code, '{}'
FROM im_source.tpp_local_codes t
LEFT JOIN im_source.read_v3_concept r ON r.code = t.ctv3Code
WHERE r.code IS NULL;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT DISTINCT c.id, @is_a, o.id
FROM im_source.tpp_snomed_map t
JOIN concept c ON c.iri = CONCAT(':TPPLOC_', t.ctv3Code)
JOIN concept o ON o.iri = CONCAT(':SN_', t.snomedCode);

-- Definition
UPDATE concept c
JOIN im_source.tpp_local_codes r ON c.iri = CONCAT(':TPPLOC_', r.ctv3Code)
JOIN concept_property_object cpo ON cpo.concept = c.id AND cpo.property = @is_a
JOIN concept o ON o.id = cpo.object
SET c.definition = JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri))
;

-- ******************** VISION LOCAL ********************

-- Concepts
SELECT @scheme := id FROM concept WHERE iri = ':CM_VisionLocalCode';

INSERT INTO concept
(namespace, iri, name, description, scheme, code, definition)
SELECT @namespace, CONCAT(':VISLOC_', readCode), readTerm, readTerm, @scheme, readCode, '{}'
FROM im_source.vision_local_codes v;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT c.id, @is_a, o.id
FROM im_source.vision_local_codes v
JOIN concept c ON c.iri = CONCAT(':VISLOC_', readCode)
JOIN concept o ON o.iri = CONCAT(':SN_', snomedCode);

-- Definition
UPDATE concept c
JOIN im_source.vision_local_codes r ON c.iri = CONCAT(':VISLOC_', r.readCode)
JOIN concept_property_object cpo ON cpo.concept = c.id AND cpo.property = @is_a
JOIN concept o ON o.id = cpo.object
SET c.definition = JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri))
;
