USE im_next2;

-- USEFUL VALUES
SELECT @namespace := dbid FROM namespace WHERE prefix = ':';
SELECT @module := dbid FROM module WHERE iri = 'http://www.DiscoveryDataService.org/InformationModel/Module/Legacy';

-- !!! POPULATE VIEWER TABLES FROM META !!!

-- ******************** SNOMED ********************
-- Concepts
SELECT @scheme := dbid FROM concept WHERE iri = ':891101000252101';

INSERT IGNORE INTO concept
(namespace, iri, name, description, scheme, code, status)
SELECT @namespace, CONCAT('sn:', id), IF(LENGTH(term) > 250, CONCAT(LEFT(term, 247), '...'), term), term, @scheme, id, IF(active, 1, 2)
FROM im_source.snomed_description_filtered;

SELECT @is_a := dbid FROM concept WHERE iri = 'sn:116680003';

-- Hierarchy
INSERT IGNORE INTO concept_property_object
(concept, property, object, `group`)
SELECT s.dbid, @is_a, d.dbid, r.relationshipGroup
FROM im_source.snomed_relationship r
JOIN concept s ON s.iri = CONCAT('sn:', r.sourceId)
JOIN concept d ON d.iri = CONCAT('sn:', r.destinationId)
WHERE r.typeId = 116680003
AND r.active = 1;

-- Replacements
INSERT IGNORE INTO concept_property_object
(concept, property, object)
SELECT c.dbid, @is_a, r.dbid
FROM im_source.snomed_history h
JOIN concept c ON c.iri = concat('sn:', h.oldConceptId)
JOIN concept r ON r.iri = concat('sn:', h.newConceptId)
GROUP BY h.oldConceptId;

-- Definitions
INSERT IGNORE INTO concept_axiom (concept, definition, module)
SELECT c.dbid, JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri)), @module
FROM concept c
JOIN im_source.snomed_description_filtered r ON c.iri = CONCAT('sn:', r.id)
JOIN concept_property_object cpo ON cpo.concept = c.dbid AND cpo.property = @is_a
JOIN concept o ON o.dbid = cpo.object;
;

-- Hierarchy insert
INSERT IGNORE INTO concept_property_object
(concept, property, object)
SELECT c.dbid, @is_a, o.dbid
FROM concept c
JOIN concept o ON o.iri = ':894281000252100'
WHERE c.iri = 'sn:138875005';


-- ******************** READ 2 ********************
SELECT @child_of := dbid FROM concept WHERE iri = ':3081000252106';

-- Concepts
SELECT @scheme := dbid FROM concept WHERE iri = ':891141000252104';

INSERT IGNORE INTO concept
(namespace, iri, name, description, code, scheme)
SELECT @namespace, CONCAT(':R2_', code), term, term, code, @scheme
FROM im_source.read_v2;

-- Hierarchy
INSERT IGNORE INTO concept_property_object
(concept, property, object)
SELECT p.dbid, @child_of, c.dbid
FROM im_source.read_v2 r
JOIN concept p ON p.iri = CONCAT(':R2_', r.code)
JOIN concept c ON c.iri = CONCAT(':R2_', LEFT(r.code, LENGTH(REPLACE(r.code, '.', ''))-1), REPEAT('.', 6 - LENGTH(REPLACE(r.code, '.', ''))))
WHERE r.code <> '.....';

-- Maps
INSERT IGNORE INTO concept_property_object
(concept, property, object)
SELECT r.dbid, @is_a, s.dbid
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
JOIN concept s ON s.iri = CONCAT('sn:', conceptId);

-- Definition
INSERT IGNORE INTO concept_axiom (concept, definition, module)
SELECT c.dbid, JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri)), @module
FROM concept c
JOIN im_source.read_v2 r ON c.iri = CONCAT(':R2_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.dbid AND cpo.property = @is_a
JOIN concept o ON o.dbid = cpo.object;

-- ******************** CTV3 ********************
-- Concepts
SELECT @scheme := dbid FROM concept WHERE iri = ':891051000252101';

INSERT IGNORE INTO concept
(namespace, iri, name, description, code, scheme)
SELECT @namespace, CONCAT(':CTV3_', code), IF(LENGTH(name) > 250, CONCAT(LEFT(name, 247), '...'), name), ifnull(description, name), code, @scheme
FROM im_source.read_v3_summary;

-- Hierarchy
INSERT IGNORE INTO concept_property_object
(concept, property, object)
SELECT c.dbid, @child_of, p.dbid
FROM im_source.read_v3_hier h
JOIN concept c ON c.iri = CONCAT(':CTV3_', h.code)
JOIN concept p ON p.iri = CONCAT(':CTV3_', h.parent);

-- Maps (1:1)
INSERT IGNORE INTO concept_property_object
(concept, property, object)
SELECT c.dbid, @is_a, v.dbid
FROM im_source.read_v3_map_summary s
JOIN im_source.read_v3_map_tmp t ON t.ctv3Concept = s.ctv3Concept
JOIN concept c ON c.iri = CONCAT(':CTV3_', s.ctv3Concept)
JOIN concept v ON v.iri = CONCAT('sn:', t.conceptId)
WHERE s.multi = FALSE;

-- Maps (1:n)
INSERT IGNORE INTO concept_property_object
(concept, property, object)
SELECT c.dbid, @is_a, v.dbid
FROM im_source.read_v3_map_summary s
JOIN concept c ON c.iri = CONCAT(':CTV3_', s.ctv3Concept)
JOIN concept v ON v.iri = CONCAT('sn:', s.altConceptId)
WHERE s.multi = TRUE
  AND s.altConceptID IS NOT NULL;

-- Definition
INSERT IGNORE INTO concept_axiom (concept, definition, module)
SELECT c.dbid, JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri)), @module
FROM concept c
JOIN im_source.read_v3_summary r ON c.iri = CONCAT(':CTV3_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.dbid AND cpo.property = @is_a
JOIN concept o ON o.dbid = cpo.object
;

-- ******************** ICD10 ********************
-- Concepts
SELECT @scheme := dbid FROM concept WHERE iri = ':891021000252109';

INSERT IGNORE INTO concept
(namespace, iri, name, description, scheme, code)
SELECT @namespace, CONCAT(':I10_', code), IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description), description, @scheme, code
FROM im_source.icd10;

-- Additional concepts
INSERT IGNORE INTO concept
(namespace, iri, name, description, code, scheme)
VALUES
(@namespace, ':I10_Modifier4', 'ICD10 Modifier 4', 'IDC10 4th character modifier suffix', 'Modifier4', @scheme),
(@namespace, ':I10_Modifier5', 'ICD10 Modifier 5', 'IDC10 5th character modifier suffix', 'Modifier5', @scheme),
(@namespace, ':I10_Qualifiers', 'ICD10 Modifier qualifiers', 'IDC10 dual classification (asterisk codes)', 'Qualifiers', @scheme),
(@namespace, ':I10_GenderMask', 'ICD10 Gender mask', 'IDC10 gender mask', 'GenderMask', @scheme),
(@namespace, ':I10_MinAge', 'IDC10 minimum age', 'IDC10 minimum age', 'MinAge', @scheme),
(@namespace, ':I10_MaxAge', 'IDC10 maximum age', 'IDC10 maximum age', 'MaxAge', @scheme),
(@namespace, ':I10_TreeDescription', 'IDC10 tree description', 'IDC10 tree description', 'TreeDescription', @scheme);

-- Set as sub-object-properties
SELECT @prop := dbid FROM concept WHERE iri = ':dataModelDataProperty';

INSERT IGNORE INTO concept_property_object (concept, property, object)
SELECT c.dbid, @is_a, @prop
FROM concept c
WHERE iri IN (
    ':I10_Modifier4',
    ':I10_Modifier5',
    ':I10_Qualifiers',
    ':I10_GenderMask',
    ':I10_MinAge',
    ':I10_MaxAge',
    ':I10_TreeDescription'
);

-- Modifier 4
INSERT IGNORE INTO concept_property_data (concept, property, data, datatype)
SELECT c.dbid, p.dbid, modifier_4, t.dbid
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_Modifier4'
JOIN concept t ON t.iri = 'xsd:string'
WHERE modifier_4 IS NOT NULL;

-- Modifier 5
INSERT IGNORE INTO concept_property_data (concept, property, data, datatype)
SELECT c.dbid, p.dbid, modifier_5, t.dbid
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_Modifier5'
JOIN concept t ON t.iri = 'xsd:string'
WHERE modifier_5 IS NOT NULL;

-- Gender mask
INSERT IGNORE INTO concept_property_data (concept, property, data, datatype)
SELECT c.dbid, p.dbid, gender_mask, t.dbid
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_GenderMask'
JOIN concept t ON t.iri = 'xsd:string'
WHERE gender_mask IS NOT NULL;

-- Min age
INSERT IGNORE INTO concept_property_data (concept, property, data, datatype)
SELECT c.dbid, p.dbid, min_age, t.dbid
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_MinAge'
JOIN concept t ON t.iri = 'xsd:int'
WHERE min_age IS NOT NULL;

-- Max age
INSERT IGNORE INTO concept_property_data (concept, property, data, datatype)
SELECT c.dbid, p.dbid, max_age, t.dbid
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_MaxAge'
JOIN concept t ON t.iri = 'xsd:int'
WHERE max_age IS NOT NULL;

-- Tree description
INSERT IGNORE INTO concept_property_data (concept, property, data, datatype)
SELECT c.dbid, p.dbid, tree_description, t.dbid
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_TreeDescription'
JOIN concept t ON t.iri = 'xsd:string'
WHERE tree_description IS NOT NULL;

-- Qualifiers
INSERT IGNORE INTO concept_property_data (concept, property, data, datatype)
SELECT c.dbid, p.dbid, qualifiers, t.dbid
FROM im_source.icd10 i
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept p ON p.iri = ':I10_Qualifiers'
JOIN concept t ON t.iri = 'xsd:string'
WHERE qualifiers IS NOT NULL;

-- Maps
INSERT IGNORE INTO concept_property_object
(concept, property, object)
SELECT DISTINCT c.dbid, @is_a, o.dbid
FROM im_source.icd10_opcs4_maps m
JOIN im_source.icd10 i ON i.alt_code = m.mapTarget
JOIN concept c ON c.iri = CONCAT(':I10_', i.code)
JOIN concept o ON o.iri = CONCAT('sn:', m.referencedComponentId)
WHERE m.refsetId = 999002271000000101
AND m.active = 1
ORDER BY i.code;

-- Definition
INSERT INTO concept_axiom (concept, definition, module)
SELECT c.dbid, JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri)), @module
FROM concept c
JOIN im_source.icd10 r ON c.iri = CONCAT(':I10_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.dbid AND cpo.property = @is_a
JOIN concept o ON o.dbid = cpo.object
;

-- ******************** OPCS4 ********************
-- Concepts
SELECT @scheme := dbid FROM concept WHERE iri = ':891041000252103';

INSERT INTO concept
(namespace, iri, name, description, scheme, code)
SELECT @namespace, CONCAT(':O4_', code), IF(LENGTH(description) > 250, CONCAT(LEFT(description, 247), '...'), description), description, @scheme, code
FROM im_source.opcs4;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT DISTINCT c.dbid, @is_a, p.dbid
FROM im_source.icd10_opcs4_maps m
JOIN im_source.opcs4 o ON o.altCode = m.mapTarget
JOIN concept c ON c.iri = CONCAT(':O4_', o.code)
JOIN concept p ON p.iri = CONCAT('sn:', m.referencedComponentId)
WHERE m.refsetId = 999002741000000101
AND m.active = 1
ORDER BY o.code;

-- Definition
INSERT INTO concept_axiom (concept, definition, module)
SELECT c.dbid, JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri)), @module
FROM concept c
JOIN im_source.opcs4 r ON c.iri = CONCAT(':O4_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.dbid AND cpo.property = @is_a
JOIN concept o ON o.dbid = cpo.object
;

-- ******************** BARTS ********************
-- Concepts
SELECT @scheme := dbid FROM concept WHERE iri = ':891081000252108';

INSERT INTO concept
(namespace, iri, name, description, scheme, code)
SELECT @namespace, CONCAT(':BC_', code), term, term, @scheme, code
FROM im_source.barts_cerner;

INSERT INTO concept
(namespace, iri, name, description, scheme, code)
SELECT DISTINCT @namespace, CONCAT(':CM_BC_', e.dbid), e.term, e.term, @scheme, CONCAT('BC_', e.dbid)
FROM im_source.barts_cerner b
LEFT JOIN im_source.barts_cerner_snomed_expressions e ON e.expression = b.snomed_expression
WHERE snomed_expression IS NOT NULL
AND e.dbid IS NOT NULL;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT c.dbid, @is_a, o.dbid
FROM im_source.barts_cerner b
LEFT JOIN im_source.barts_cerner_snomed_expressions e ON e.expression = b.snomed_expression
JOIN concept c ON c.iri = CONCAT(':BC_', b.code)
JOIN concept o ON o.iri = IF(e.dbid IS NULL, CONCAT('sn:', snomed_expression), CONCAT(':CM_BC_', e.dbid))
WHERE snomed_expression IS NOT NULL;

-- Definition
INSERT INTO concept_axiom (concept, definition, module)
SELECT c.dbid, JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri)), @module
FROM concept c
JOIN im_source.barts_cerner r ON c.iri = CONCAT(':BC_', r.code)
JOIN concept_property_object cpo ON cpo.concept = c.dbid AND cpo.property = @is_a
JOIN concept o ON o.dbid = cpo.object
;

-- ******************** EMIS LOCAL ********************
-- Concepts
SELECT @scheme := dbid FROM concept WHERE iri = ':891031000252107';

INSERT IGNORE INTO concept
(namespace, iri, name, description, scheme, code)
SELECT DISTINCT @namespace, CONCAT(':EMLOC_', e.readTermId), e.codeTerm, e.codeTerm, @scheme, e.readTermId
FROM im_source.emis_read_snomed e
LEFT JOIN im_source.read_v2 r ON r.code = IF(LENGTH(e.readTermId)<5, CONCAT(e.readTermId, REPEAT('.', 5-LENGTH(e.readTermId))), e.readTermId)
WHERE r.code IS NULL
AND e.readTermId = 'ALLERGY100539NEMIS'
GROUP BY readTermId;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT DISTINCT c.dbid, @is_a, o.dbid
FROM im_source.emis_read_snomed e
JOIN concept c ON c.iri = CONCAT(':EMLOC_', e.readTermId)
JOIN concept o ON o.iri = CONCAT('sn:', snomedCTConceptId);

-- Definition
INSERT INTO concept_axiom (concept, definition, module)
SELECT c.dbid, JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri)), @module
FROM concept c
JOIN im_source.emis_read_snomed r ON c.iri = CONCAT(':EMLOC_', r.readTermId)
JOIN concept_property_object cpo ON cpo.concept = c.dbid AND cpo.property = @is_a
JOIN concept o ON o.dbid = cpo.object
;

-- ******************** TPP LOCAL ********************
-- FIX TPP LOCAL SCHEME!!!
UPDATE concept SET name = 'TPP local code' WHERE iri = ':631000252102';
-- Concepts
SELECT @scheme := dbid FROM concept WHERE iri = ':631000252102';

INSERT INTO concept
(namespace, iri, name, description, scheme, code)
SELECT DISTINCT @namespace, CONCAT(':TPPLOC_', t.ctv3Code), t.ctv3Text, t.ctv3Text, @scheme, t.ctv3Code
FROM im_source.tpp_local_codes t
LEFT JOIN im_source.read_v3_concept r ON r.code = t.ctv3Code
WHERE r.code IS NULL;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT DISTINCT c.dbid, @is_a, o.dbid
FROM im_source.tpp_snomed_map t
JOIN concept c ON c.iri = CONCAT(':TPPLOC_', t.ctv3Code)
JOIN concept o ON o.iri = CONCAT('sn:', t.snomedCode);

-- Definition
INSERT INTO concept_axiom (concept, definition, module)
SELECT c.dbid, JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri)), @module
FROM concept c
JOIN im_source.tpp_local_codes r ON c.iri = CONCAT(':TPPLOC_', r.ctv3Code)
JOIN concept_property_object cpo ON cpo.concept = c.dbid AND cpo.property = @is_a
JOIN concept o ON o.dbid = cpo.object
;

-- ******************** VISION LOCAL ********************
-- TODO: VISION LOCAL NEEDS SNOMED ALLOCATING
-- Inject vision local code scheme
SELECT @scheme := dbid FROM concept WHERE iri = ':891071000252105';

INSERT IGNORE INTO concept (namespace, iri, name, description, scheme, code)
VALUES (@namespace, ':CM_VisionLocalCode', 'Vision local code', 'Vision local code', @scheme, 'CM_VisionLocalCode');

-- Concepts
SELECT @scheme := dbid FROM concept WHERE iri = ':CM_VisionLocalCode';

INSERT INTO concept
(namespace, iri, name, description, scheme, code)
SELECT @namespace, CONCAT(':VISLOC_', readCode), readTerm, readTerm, @scheme, readCode
FROM im_source.vision_local_codes v;

-- Maps
INSERT INTO concept_property_object
(concept, property, object)
SELECT c.dbid, @is_a, o.dbid
FROM im_source.vision_local_codes v
JOIN concept c ON c.iri = CONCAT(':VISLOC_', readCode)
JOIN concept o ON o.iri = CONCAT('sn:', snomedCode);

-- Definition
INSERT INTO concept_axiom (concept, definition, module)
SELECT c.dbid, JSON_OBJECT('SubClassOf', JSON_OBJECT('Class', o.iri)), @module
FROM concept c
JOIN im_source.vision_local_codes r ON c.iri = CONCAT(':VISLOC_', r.readCode)
JOIN concept_property_object cpo ON cpo.concept = c.dbid AND cpo.property = @is_a
JOIN concept o ON o.dbid = cpo.object
;
